package com.ifuture.adonline.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.ifuture.adonline.service.AdsMaterialService;
import com.ifuture.adonline.service.JpmmlService;
import com.ifuture.adonline.util.CsvUtils;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.hadoop.hbase.Cell;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.util.Bytes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.hadoop.hbase.HbaseTemplate;
import org.springframework.data.hadoop.hbase.RowMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class DemoResource {

    private final Logger log = LoggerFactory.getLogger(DemoResource.class);

    @Autowired
    private HbaseTemplate hbaseTemplate;

    @Autowired
    private JpmmlService jpmmlService;

    @Autowired
    private AdsMaterialService adsMaterialService;

    @GetMapping("/demos/test")
    @Timed
    public ResponseEntity<String> test() {
        log.debug("REST request to test demos");
        String data = hbaseTemplate.get("tb_user", "1", "info", "name", new RowMapper<String>() {
            @Override
            public String mapRow(Result result, int i) throws Exception {
                List<Cell> ceList = result.listCells();
                String res = "";
                if (ceList != null && ceList.size() > 0) {
                    for (Cell cell : ceList) {
                        res = Bytes.toString(cell.getValueArray(), cell.getValueOffset(),
                            cell.getValueLength());
                    }
                }
                log.info("name:" + res);
                return res;
            }
        });
        return ResponseEntity.ok().body(data);
    }

    @GetMapping("/demos/collect")
    @Timed
    public ResponseEntity<String> collect() {
        log.debug("REST request to collect demos");
        adsMaterialService.collectAdsMaterials();
        return ResponseEntity.ok().body("ok");
    }

    @GetMapping("/demos/hbase/{tableName}/{rowName}")
    @Timed
    public ResponseEntity<Map> hbase(@PathVariable String tableName, @PathVariable String rowName) {
        log.debug("REST request to hbase demos");
        Map map = adsMaterialService.getMapsFromHbaseByRowKey(tableName, rowName);
        return ResponseEntity.ok().body(map);
    }

    @GetMapping("/demos/getMatchedListFromMysql")
    @Timed
    public ResponseEntity<Map> getMatchedListFromMysql(
        @RequestParam(value = "os", required = false) String os,
        @RequestParam(value = "network", required = false) String network,
        @RequestParam(value = "sex", required = false) String sex) {
        log.debug("REST request to getMatchedListFromMysql");
        Map<String, Object> params = new HashMap<>();
        params.put("os", os);
        params.put("network", network);
        params.put("sex", sex);

        List list = adsMaterialService.getMatchedListFromMysql(params);
        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("total", list.size());
        resultMap.put("materials", list);
        return ResponseEntity.ok().body(resultMap);
    }

    @GetMapping("/demos/pmml")
    @Timed
    public ResponseEntity<String> pmml() {

        log.debug("REST request to test pmml");

        String csvFilePath = Thread.currentThread().getContextClassLoader()
            .getResource("csv/feature_df.csv").getPath();

        List<Map<String, Object>> paramList = CsvUtils.csvToMapList(csvFilePath);

        List<Map<String, Object>> resultList = new ArrayList();

        List sortList = new ArrayList();

        for (Map<String, Object> paramMap : paramList) {
            Map<String, Object> map = jpmmlService.getAdsCtrMapByJPMML(paramMap);
            resultList.add(map);
            sortList.add(map.get("probability(1)"));
        }

        CsvUtils
            .mapListToCsv(new String[]{"label", "pmml(prediction)", "prediction", "probability(0)",
                "probability(1)"}, resultList, "F:\\result.csv");

        Collections.sort(sortList, new Comparator<Object>() {
            @Override
            public int compare(Object v1, Object v2) {
                return Double.valueOf(v2.toString()).compareTo(Double.valueOf(v1.toString()));
            }
        });

        CsvUtils.listToCsv(new String[]{"probability(1)"}, sortList, "F:\\result_sorted.csv");

        return ResponseEntity.ok().body("ok");
    }

    @GetMapping("/demos/pmml2")
    @Timed
    public ResponseEntity<String> pmml2() {

        log.debug("REST request to test pmml2");

        String csvFilePath = Thread.currentThread().getContextClassLoader()
            .getResource("csv/feature_df.csv").getPath();

        List<Map<String, Object>> paramList = CsvUtils.csvToMapList(csvFilePath);

        long startTime = System.currentTimeMillis();
        List<Map<String, Object>> resultList = new ArrayList();
        List sortList = new ArrayList();
        for (Map<String, Object> paramMap : paramList) {
            Map<String, Object> map = jpmmlService.getAdsCtrMapByJPMML(paramMap);
            resultList.add(map);
            sortList.add(map.get("probability(1)"));
        }
        Collections.sort(sortList, new Comparator<Object>() {
            @Override
            public int compare(Object v1, Object v2) {
                return Double.valueOf(v2.toString()).compareTo(Double.valueOf(v1.toString()));
            }
        });
        long endTime = System.currentTimeMillis();
        log.info("jpmml exec in {} ms",(endTime-startTime));

        return ResponseEntity.ok().body("ok");
    }

}
