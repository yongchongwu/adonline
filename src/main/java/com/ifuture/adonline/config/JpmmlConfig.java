package com.ifuture.adonline.config;

import java.io.InputStream;
import java.util.List;
import javax.annotation.PostConstruct;
import org.dmg.pmml.PMML;
import org.jpmml.evaluator.Evaluator;
import org.jpmml.evaluator.InputField;
import org.jpmml.evaluator.ModelEvaluator;
import org.jpmml.evaluator.ModelEvaluatorFactory;
import org.jpmml.evaluator.OutputField;
import org.jpmml.evaluator.TargetField;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class JpmmlConfig {

    private final Logger log = LoggerFactory.getLogger(JpmmlConfig.class);

    private PMML pmml;

    private final ApplicationProperties applicationProperties;

    public JpmmlConfig(ApplicationProperties applicationProperties) {
        this.applicationProperties = applicationProperties;
    }

    @PostConstruct
    public void init() {
        log.info("loading the pmml file");
        long startTime = System.currentTimeMillis();
        String pmmlFilePath = applicationProperties.getPmmlFilePath();
        if (pmmlFilePath.startsWith("/")) {
            pmmlFilePath = pmmlFilePath.substring(1);
        }
        try (InputStream is = Thread.currentThread().getContextClassLoader()
            .getResourceAsStream(pmmlFilePath)) {
            pmml = org.jpmml.model.PMMLUtil.unmarshal(is);
            long endTime = System.currentTimeMillis();
            log.info("loading the pmml file in {} ms", (endTime - startTime));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Bean
    public PMML pmml() {
        return this.pmml;
    }

    @Bean
    public ModelEvaluator<?> modelEvaluator(ModelEvaluatorFactory modelEvaluatorFactory,
        PMML pmml) {
        ModelEvaluator<?> modelEvaluator = modelEvaluatorFactory.newModelEvaluator(pmml);
        Evaluator evaluator = (Evaluator) modelEvaluator;
        evaluator.verify();
        log.info("=============inputFields========================");
        List<InputField> inputFields = modelEvaluator.getInputFields();
        for (InputField inputField : inputFields) {
            log.info(inputField.getName().toString());
        }

        log.info("==============targetFields=======================");
        List<TargetField> targetFields = modelEvaluator.getTargetFields();
        for (TargetField targetField : targetFields) {
            log.info(targetField.getName().toString());
        }

        log.info("==============outputFields=======================");
        List<OutputField> outputFields = modelEvaluator.getOutputFields();
        for (OutputField outputField : outputFields) {
            log.info(outputField.getName().toString());
        }
        return modelEvaluator;
    }

    @Bean
    public ModelEvaluatorFactory modelEvaluatorFactory() {
        ModelEvaluatorFactory modelEvaluatorFactory = ModelEvaluatorFactory.newInstance();
        return modelEvaluatorFactory;
    }
}
