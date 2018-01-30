package com.ifuture.adonline.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Properties specific to JHipster.
 * <p>
 * Properties are configured in the application.yml file.
 */
@ConfigurationProperties(prefix = "application", ignoreUnknownFields = false)
public class ApplicationProperties {

    private String dialect;

    private String adsCollectUrl;

    private String pmmlFilePath;

    public String getDialect() {
        return dialect;
    }

    public void setDialect(String dialect) {
        this.dialect = dialect;
    }

    public String getAdsCollectUrl() {
        return adsCollectUrl;
    }

    public void setAdsCollectUrl(String adsCollectUrl) {
        this.adsCollectUrl = adsCollectUrl;
    }

    public String getPmmlFilePath() {
        return pmmlFilePath;
    }

    public void setPmmlFilePath(String pmmlFilePath) {
        this.pmmlFilePath = pmmlFilePath;
    }
}
