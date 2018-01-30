package com.ifuture.adonline.service.dto;

import java.io.Serializable;

public class MaterialDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;
    private String position;
    private String tags;
    private String startDate;
    private String endDate;
    private String imgUrl;
    private String hrefUrl;
    private Double customerPrice;
    private String copy;
    private String collectDate;
    private AdvertiserRequirementDTO advertiserRequirements;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public String getTags() {
        return tags;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public String getHrefUrl() {
        return hrefUrl;
    }

    public void setHrefUrl(String hrefUrl) {
        this.hrefUrl = hrefUrl;
    }

    public Double getCustomerPrice() {
        return customerPrice;
    }

    public void setCustomerPrice(Double customerPrice) {
        this.customerPrice = customerPrice;
    }

    public String getCopy() {
        return copy;
    }

    public void setCopy(String copy) {
        this.copy = copy;
    }

    public String getCollectDate() {
        return collectDate;
    }

    public void setCollectDate(String collectDate) {
        this.collectDate = collectDate;
    }

    public AdvertiserRequirementDTO getAdvertiserRequirements() {
        return advertiserRequirements;
    }

    public void setAdvertiserRequirements(
        AdvertiserRequirementDTO advertiserRequirements) {
        this.advertiserRequirements = advertiserRequirements;
    }
}
