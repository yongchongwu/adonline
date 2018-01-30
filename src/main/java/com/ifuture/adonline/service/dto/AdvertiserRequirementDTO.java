package com.ifuture.adonline.service.dto;

import java.io.Serializable;
import java.util.List;

public class AdvertiserRequirementDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;
    private Long masterId;
    private Double minAmount;
    private List<Integer> meid;
    private List<Integer> orderNo;
    private List<Integer> wuid;
    private List<String> city;
    private List<String> category;
    private List<String> date;
    private List<String> time;
    private Boolean clicked;
    private Boolean overAvgAmount;
    private String os;
    private String network;
    private String sex;
    private String tradeType;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getMasterId() {
        return masterId;
    }

    public void setMasterId(Long masterId) {
        this.masterId = masterId;
    }

    public Double getMinAmount() {
        return minAmount;
    }

    public void setMinAmount(Double minAmount) {
        this.minAmount = minAmount;
    }

    public List<Integer> getMeid() {
        return meid;
    }

    public void setMeid(List<Integer> meid) {
        this.meid = meid;
    }

    public List<Integer> getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(List<Integer> orderNo) {
        this.orderNo = orderNo;
    }

    public List<Integer> getWuid() {
        return wuid;
    }

    public void setWuid(List<Integer> wuid) {
        this.wuid = wuid;
    }

    public List<String> getCity() {
        return city;
    }

    public void setCity(List<String> city) {
        this.city = city;
    }

    public List<String> getCategory() {
        return category;
    }

    public void setCategory(List<String> category) {
        this.category = category;
    }

    public List<String> getDate() {
        return date;
    }

    public void setDate(List<String> date) {
        this.date = date;
    }

    public List<String> getTime() {
        return time;
    }

    public void setTime(List<String> time) {
        this.time = time;
    }

    public Boolean getClicked() {
        return clicked;
    }

    public void setClicked(Boolean clicked) {
        this.clicked = clicked;
    }

    public Boolean getOverAvgAmount() {
        return overAvgAmount;
    }

    public void setOverAvgAmount(Boolean overAvgAmount) {
        this.overAvgAmount = overAvgAmount;
    }

    public String getOs() {
        return os;
    }

    public void setOs(String os) {
        this.os = os;
    }

    public String getNetwork() {
        return network;
    }

    public void setNetwork(String network) {
        this.network = network;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getTradeType() {
        return tradeType;
    }

    public void setTradeType(String tradeType) {
        this.tradeType = tradeType;
    }
}
