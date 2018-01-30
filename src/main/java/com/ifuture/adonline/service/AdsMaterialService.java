package com.ifuture.adonline.service;

import com.ifuture.adonline.config.ApplicationProperties;
import com.ifuture.adonline.domain.MaterialVo;
import com.ifuture.adonline.service.dto.AdsMaterialDTO;
import com.ifuture.adonline.util.DateTools;
import fpay.bills.grpc.AdvRequest;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import org.apache.commons.lang.math.NumberUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.hadoop.hbase.Cell;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.util.Bytes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.hadoop.hbase.HbaseTemplate;
import org.springframework.data.hadoop.hbase.RowMapper;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SessionCallback;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

/**
 * 广告服务类
 */
@Service
@SuppressWarnings("unchecked")
public class AdsMaterialService {

    private final Logger log = LoggerFactory.getLogger(AdsMaterialService.class);

    private final static String REDIS_KEY_PREFIX = "AdsMaterials:";

    @Autowired
    private ApplicationProperties applicationProperties;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @Autowired
    private HbaseTemplate hbaseTemplate;

    @Autowired
    private JpmmlService jpmmlService;

    @Autowired
    private MaterialService materialService;

    /**
     * 采集广告素材
     */
    public void collectAdsMaterials() {
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        headers.add("Authorization", "");
        HttpEntity<String> requestEntity = new HttpEntity<String>(headers);

        ResponseEntity<AdsMaterialDTO> responseEntity = restTemplate
            .exchange(String
                    .format(applicationProperties.getAdsCollectUrl(), DateTools.todayStr("yyyy-MM-dd")),
                HttpMethod.GET,
                requestEntity,
                AdsMaterialDTO.class);

        AdsMaterialDTO dto = responseEntity.getBody();
        dto.setCollect_date(DateTools.todayStr("yyyy-MM-dd"));

        //将广告素材存放到Mysql
        if (null != dto && null != dto.getCount() && dto.getCount() > 0) {
            materialService.saveAdsMaterials(dto);
        }

    }

    /**
     * 根据参数从mysql中获取匹配的素材数据
     */
    public List<MaterialVo> getMatchedListFromMysql(Map<String, Object> params) {
        List list = materialService.getMatchedListFromMysql(params);
        return list;
    }

    /**
     * 根据规则从redis中获取匹配的素材数据
     */
    public List<MaterialVo> getMatchedListFromRedis(String redisKey) {
        HashOperations hashOperations = redisTemplate.opsForHash();
        List<MaterialVo> list = new ArrayList<>();
        Map<String, Object> map = hashOperations.entries(redisKey);
        for (String hkey : map.keySet()) {
            list.add((MaterialVo) map.get(hkey));
        }
        return list;
    }

    /**
     * 把匹配的素材数据缓存到Redis中
     */
    public void saveMatchedListToRedis(String redisKey, List<MaterialVo> matchedList) {
        if (null != matchedList && matchedList.size() > 0) {
            redisTemplate.executePipelined(new SessionCallback<Object>() {
                @Override
                public Object execute(RedisOperations redisOperations)
                    throws DataAccessException {
                    for (MaterialVo vo : matchedList) {
                        redisOperations.opsForHash().put(redisKey, String.valueOf(vo.getId()), vo);
                    }
                    return null;
                }
            });
        }
    }

    /**
     * 根据表名和行关键字从Hbase中获取列数据HashMap
     */
    public Map<String, String> getMapsFromHbaseByRowKey(String tableName, String rowName) {
        return hbaseTemplate.get(tableName, rowName, new RowMapper<Map<String, String>>() {
            @Override
            public Map<String, String> mapRow(Result result, int i) throws Exception {
                List<Cell> ceList = result.listCells();
                Map<String, String> map = new HashMap<String, String>();
                if (ceList != null && ceList.size() > 0) {
                    for (Cell cell : ceList) {
                        map.put(Bytes.toString(cell.getQualifierArray(), cell.getQualifierOffset(),
                            cell.getQualifierLength()),
                            Bytes.toString(cell.getValueArray(), cell.getValueOffset(),
                                cell.getValueLength()));
                    }
                }
                return map;
            }
        });
    }

    /**
     * 根据GRPC请求构建map过滤参数
     */
    public Map<String, Object> getParamsByAdvRequest(Map<String, Object> map) {

        Map<String, Object> params = new HashMap<>();

        params.put("os", map.get("os"));

        params.put("network", map.get("network"));

        String sex = String.valueOf(map.get("gender"));
        if ("female".equalsIgnoreCase(sex)) {
            sex = "女";
        } else if ("male".equalsIgnoreCase(sex)) {
            sex = "男";
        }
        params.put("sex", sex);

        return params;
    }

    /**
     * 根据特征值计算广告的点击率
     */
    public Map<String, Double> getAdsCtrMap(List<MaterialVo> matchedAds, Map<String, Object> map) {
        Map<String, Double> ctrMap = new HashMap<>();
        Calendar c = Calendar.getInstance();
        Map<String, Object> featureMap = null;
        String ads_id = null;
        for (MaterialVo vo : matchedAds) {

            featureMap = new HashMap<>();
            ads_id = String.valueOf(vo.getId());

            //从hbase获取ads特征值
            Map<String, String> adsMap = getMapsFromHbaseByRowKey("ads", ads_id);

            //从hbase获取ads_ctr特征值

            Map<String, String> adsCtrMap = getMapsFromHbaseByRowKey("ads_ctr",
                ads_id + c.get(Calendar.HOUR_OF_DAY));

            //从hbase获取industry_ctr特征值
            String industry_id = adsMap.get("industry");
            Map<String, String> industryCtrMap = getMapsFromHbaseByRowKey("industry_ctr",
                industry_id + c.get(Calendar.HOUR_OF_DAY));

            //从hbase获取user_spending_bar_industry特征值
            Map<String, String> usbiMap = getMapsFromHbaseByRowKey("user_spending_bar_industry",
                map.get("user_id") + industry_id);

            //计算点击率
            featureMap.putAll(map);
            featureMap.putAll(adsMap);
            featureMap.putAll(adsCtrMap);
            featureMap.putAll(industryCtrMap);
            featureMap.putAll(usbiMap);
            //ctrMap.put(ads_id, computeAdsCtr(featureMap));
            ctrMap.put(ads_id, jpmmlService.computeAdsCtrByJPMML(featureMap));
        }
        return ctrMap;
    }

    public Double computeAdsCtr(Map<String, String> featureMap) {
        Double result = Double.valueOf("0");
        Double a = Double.valueOf("1");
        List<Double> list = new ArrayList<>();

        putFeatureValueToList(list, featureMap);

        for (Double x : list) {
            a += (1 * x);
        }
        double exp = Math.exp(a);
        result = exp / (1 + exp);
        return result;
    }

    public void putFeatureValueToList(List<Double> list, Map<String, String> map) {
        if (null != map) {
            for (Map.Entry<String, String> entry : map.entrySet()) {
                String value = entry.getValue();
                if (StringUtils.isNotBlank(value) && NumberUtils.isNumber(value)) {
                    list.add(Double.valueOf(value));
                }
            }
        }
    }

    /**
     * 获得根据点击率降序排序的素材
     */
    public List<String> getSortedAds(Map<String, Double> computeMap) {

        List<String> list = new ArrayList<>();

        List<Map.Entry<String, Double>> sortList = new ArrayList<Map.Entry<String, Double>>(
            computeMap.entrySet());

        Collections.sort(sortList, new Comparator<Entry<String, Double>>() {
            @Override
            public int compare(Entry<String, Double> o1, Entry<String, Double> o2) {
                return o2.getValue().compareTo(o1.getValue());
            }
        });

        for (Map.Entry<String, Double> obj : sortList) {
            list.add(obj.getKey());
        }
        return list;
    }

    /**
     * 根据Grpc请求参数返回广告列表ID
     */
    public List<String> getAdvertisement(AdvRequest request) {
        List<String> list = new ArrayList<>();
        if (null != request) {
            //收集grpc特征
            Map<String, Object> map = new HashMap<>();
            map.put("user_id", request.getMaid());
            map.put("shop_id", request.getBussinessId());
            map.put("os", request.getUa());
            map.put("ip", request.getIp());
            map.put("network", request.getNetworkId());
            map.put("payment_method", request.getPayMethond());
            map.put("pay_amount", String.valueOf(request.getPay()));
            map.put("card_kind", "DEBIT");

            //从hbase获取用户特征值
            Map<String, String> userMap = getMapsFromHbaseByRowKey("user",
                String.valueOf(map.get("user_id")));

            //从hbase获取商家特征值
            Map<String, String> shopMap = getMapsFromHbaseByRowKey("shops",
                String.valueOf(map.get("shop_id")));

            //合并特性
            map.putAll(userMap);
            map.putAll(shopMap);

            //构建过滤参数
            Map<String, Object> params = getParamsByAdvRequest(map);

            //根据过滤参数从数据库中获取匹配的素材列表
            List<MaterialVo> matchedAds = getMatchedListFromMysql(params);

            //根据公式计算每个素材的点击率
            Map<String, Double> ctrMap = getAdsCtrMap(matchedAds, map);

            //按点击率降序排序匹配的素材
            list = getSortedAds(ctrMap);
        }
        return list;
    }

}
