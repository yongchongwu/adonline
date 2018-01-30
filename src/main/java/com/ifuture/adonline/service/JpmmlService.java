package com.ifuture.adonline.service;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import org.dmg.pmml.FieldName;
import org.jpmml.evaluator.ModelEvaluator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

/**
 * Service for JPMML.
 */
@Service
@SuppressWarnings("unchecked")
public class JpmmlService {

    @Autowired
    private ModelEvaluator modelEvaluator;

    public void buildPmmlParams(Map<FieldName, Object> paramsMap, Map<String, Object> featureMap) {

        paramsMap.put(new FieldName("30_ctr"),
            StringUtils.isEmpty(featureMap.get("30_ctr")) ? 0
                : featureMap.get("30_ctr"));
        paramsMap.put(new FieldName("industry_3_ctr"),
            StringUtils.isEmpty(featureMap.get("industry_3_ctr")) ? 0
                : featureMap.get("industry_3_ctr"));
        paramsMap.put(new FieldName("industry_8_ctr"),
            StringUtils.isEmpty(featureMap.get("industry_8_ctr")) ? 0
                : featureMap.get("industry_8_ctr"));
        paramsMap.put(new FieldName("industry_15_ctr"),
            StringUtils.isEmpty(featureMap.get("industry_15_ctr")) ? 0
                : featureMap.get("industry_15_ctr"));
        paramsMap.put(new FieldName("industry"),
            StringUtils.isEmpty(featureMap.get("industry")) ? 0
                : featureMap.get("industry"));

        paramsMap.put(new FieldName("payment_method"), featureMap.get("payment_method"));
        paramsMap.put(new FieldName("card_kind"), featureMap.get("card_kind"));
        paramsMap.put(new FieldName("network"), featureMap.get("network"));
    }

    public Double computeAdsCtrByJPMML(Map<String, Object> featureMap) {
        Map<FieldName, Object> paramsMap = new HashMap<>();

        buildPmmlParams(paramsMap, featureMap);

        Map<FieldName, ?> result = modelEvaluator.evaluate(paramsMap);

        return (Double) result.get(new FieldName("probability(1)"));
    }

    public Map<String, Object> getAdsCtrMapByJPMML(Map<String, Object> featureMap) {
        Map<FieldName, Object> paramsMap = new HashMap<>();

        buildPmmlParams(paramsMap, featureMap);

        Map<FieldName, ?> result = modelEvaluator.evaluate(paramsMap);

        Map<String, Object> map = new HashMap<>();

        Set<FieldName> keySet = result.keySet();
        for (FieldName f : keySet) {
            map.put(f.getValue(), result.get(f));
        }
        return map;
    }

}
