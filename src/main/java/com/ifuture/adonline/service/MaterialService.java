package com.ifuture.adonline.service;

import com.ifuture.adonline.config.ApplicationProperties;
import com.ifuture.adonline.domain.Material;
import com.ifuture.adonline.domain.MaterialVo;
import com.ifuture.adonline.repository.AdvertiserRequirementRepository;
import com.ifuture.adonline.repository.MaterialRepository;
import com.ifuture.adonline.service.dto.AdsMaterialDTO;
import com.ifuture.adonline.service.dto.AdvertiserRequirementDTO;
import com.ifuture.adonline.service.dto.MaterialDTO;
import com.ifuture.adonline.service.mapper.AdsMapper;
import com.ifuture.adonline.util.DateTools;
import com.ifuture.adonline.util.SqlWhere;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing Material.
 */
@Service
@Transactional
@SuppressWarnings("unchecked")
public class MaterialService {

    private final Logger log = LoggerFactory.getLogger(MaterialService.class);

    private final ApplicationProperties applicationProperties;
    private final MaterialRepository materialRepository;
    private final AdvertiserRequirementRepository advertiserRequirementRepository;
    private final JdbcTemplate jdbcTemplate;


    public MaterialService(ApplicationProperties applicationProperties,
        MaterialRepository materialRepository,
        AdvertiserRequirementRepository advertiserRequirementRepository,
        JdbcTemplate jdbcTemplate) {
        this.applicationProperties = applicationProperties;
        this.materialRepository = materialRepository;
        this.advertiserRequirementRepository = advertiserRequirementRepository;
        this.jdbcTemplate = jdbcTemplate;
    }

    /**
     * Save a material.
     *
     * @param material the entity to save
     * @return the persisted entity
     */
    public Material save(Material material) {
        log.debug("Request to save Material : {}", material);
        return materialRepository.save(material);
    }

    /**
     * Get all the materials.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<Material> findAll(Pageable pageable) {
        log.debug("Request to get all Materials");
        return materialRepository.findAll(pageable);
    }

    /**
     * Get one material by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Transactional(readOnly = true)
    public Material findOne(Long id) {
        log.debug("Request to get Material : {}", id);
        return materialRepository.findOne(id);
    }

    /**
     * Delete the  material by id.
     *
     * @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete Material : {}", id);
        materialRepository.delete(id);
    }

    /**
     * 将采集的广告素材存放到数据库.
     *
     * @param dto the dto of the AdsMaterialDTO
     */
    public void saveAdsMaterials(AdsMaterialDTO dto) {
        List<MaterialDTO> materialDTOList = dto.getMaterials();
        for (MaterialDTO materialDTO : materialDTOList) {
            //保存广告素材
            materialDTO.setCollectDate(dto.getCollect_date());

            materialRepository.save(AdsMapper.getMaterial(materialDTO));
            //删除广告规则列表或广告主定向要求
            advertiserRequirementRepository.deleteByMasterId(materialDTO.getId());
            //保存广告规则列表或广告主定向要求
            AdvertiserRequirementDTO advertiserRequirementDTO = materialDTO
                .getAdvertiserRequirements();
            if (null != advertiserRequirementDTO) {
                advertiserRequirementDTO.setMasterId(materialDTO.getId());
                advertiserRequirementRepository.save(
                    AdsMapper
                        .getAdvertiserRequirement(materialDTO.getAdvertiserRequirements(), ","));
            }
        }
    }

    /**
     * 根据参数从mysql中获取匹配的素材数据.
     *
     * @param params the params of the Map
     */
    @Transactional(readOnly = true)
    public List<MaterialVo> getMatchedListFromMysql(Map<String, Object> params) {
        StringBuilder sqlBuilder = new StringBuilder();
        sqlBuilder.append("select a.*, b.min_amount, b.meid, b.order_no, b.wuid,");
        sqlBuilder
            .append(" b.city, b.category, b.jhi_date as date, b.jhi_time as time, b.clicked,");
        sqlBuilder
            .append(" b.over_avg_amount, b.os, b.jhi_network as network, b.sex, b.trade_type");
        sqlBuilder.append(" from ads_material a");
        sqlBuilder.append(" inner join ads_advertiser_requirement b");
        sqlBuilder.append(" on a.id = b.master_id");
        sqlBuilder.append(" where 1=1 %s ");
        sqlBuilder.append(" order by a.id");

        String sqlWhereStr = getWhereByParams(params, applicationProperties.getDialect());

        String sql = String.format(sqlBuilder.toString(), sqlWhereStr);

        log.debug(sql);

        return jdbcTemplate
            .query(sql, new BeanPropertyRowMapper(MaterialVo.class));
    }

    private String getWhereByParams(Map<String, Object> params, String dialect) {
        SqlWhere sqlWhere = new SqlWhere();
        //构建查询条件
        if ("mysql".equalsIgnoreCase(dialect)) {
            sqlWhere.addWhereMysqlDate("a.start_date", DateTools.todayStr(), "<=",
                SqlWhere.MYSQL_FORMAT);
            sqlWhere
                .addWhereMysqlDate("a.end_date", DateTools.todayStr(), ">=", SqlWhere.MYSQL_FORMAT);
        } else {
            sqlWhere.addWhereDate("a.start_date", DateTools.todayStr(), "<=", SqlWhere.DB_FORMAT);
            sqlWhere.addWhereDate("a.end_date", DateTools.todayStr(), ">=", SqlWhere.DB_FORMAT);
        }
        List<String> jhiList = Arrays.asList("DATE", "TIME", "NETWORK");
        StringBuilder strBuilder = new StringBuilder();
        String key = null;
        Object val = null;
        String valStr = null;
        if (null != params) {
            for (Map.Entry<String, Object> entry : params.entrySet()) {
                key = entry.getKey();
                val = entry.getValue();
                valStr = String.valueOf(val);
                if (StringUtils.isNotBlank(valStr) && !"unknown"
                    .equalsIgnoreCase(valStr)) {

                    if (jhiList.contains(key.toUpperCase())) {
                        key = "jhi_" + key;
                    }
                    if (val instanceof Number) {
                        strBuilder.append(
                            String.format(" and (lower(%1$s)='%2$s' or %1$s is null ) ", key,
                                valStr.toLowerCase()));
                    } else if (val instanceof String) {
                        strBuilder.append(
                            String.format(
                                " and (lower(%1$s)='%2$s' or lower(%1$s)='unknown' or %1$s is null ) ",
                                key,
                                valStr.toLowerCase()));
                    }
                }
            }
        }
        return sqlWhere.getWhere() + strBuilder.toString();
    }

}
