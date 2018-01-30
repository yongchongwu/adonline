package com.ifuture.adonline.domain;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * 广告规则列表或广告主定向要求
 */
@ApiModel(description = "广告规则列表或广告主定向要求")
@Entity
@Table(name = "ads_advertiser_requirement")
public class AdvertiserRequirement implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 广告素材ID
     */
    @NotNull
    @ApiModelProperty(value = "广告素材ID", required = true)
    @Column(name = "master_id", nullable = false)
    private Long masterId;

    /**
     * 最小金额
     */
    @ApiModelProperty(value = "最小金额")
    @Column(name = "min_amount")
    private Double minAmount;

    /**
     * 门店ID列表
     */
    @ApiModelProperty(value = "门店ID列表")
    @Column(name = "meid")
    private String meid;

    /**
     * 订单尾号列表
     */
    @ApiModelProperty(value = "订单尾号列表")
    @Column(name = "order_no")
    private String orderNo;

    /**
     * 用户ID列表
     */
    @ApiModelProperty(value = "用户ID列表")
    @Column(name = "wuid")
    private String wuid;

    /**
     * 城市ID列表
     */
    @ApiModelProperty(value = "城市ID列表")
    @Column(name = "city")
    private String city;

    /**
     * 行业分类列表
     */
    @ApiModelProperty(value = "行业分类列表")
    @Column(name = "category")
    private String category;

    /**
     * 日期列表
     */
    @ApiModelProperty(value = "日期列表")
    @Column(name = "jhi_date")
    private String date;

    /**
     * 时间列表
     */
    @ApiModelProperty(value = "时间列表")
    @Column(name = "jhi_time")
    private String time;

    /**
     * 点击后是否隐藏
     */
    @ApiModelProperty(value = "点击后是否隐藏")
    @Column(name = "clicked")
    private Boolean clicked;

    /**
     * 支付金额是否高于客单价
     */
    @ApiModelProperty(value = "支付金额是否高于客单价")
    @Column(name = "over_avg_amount")
    private Boolean overAvgAmount;

    /**
     * 操作系统[Android, iOS]
     */
    @Size(max = 200)
    @ApiModelProperty(value = "操作系统[Android, iOS]")
    @Column(name = "os", length = 200)
    private String os;

    /**
     * 网络类型
     */
    @Size(max = 200)
    @ApiModelProperty(value = "网络类型")
    @Column(name = "jhi_network", length = 200)
    private String network;

    /**
     * 性别[男,女]
     */
    @Size(max = 20)
    @ApiModelProperty(value = "性别[男,女]")
    @Column(name = "sex", length = 20)
    private String sex;

    /**
     * 交易类型
     */
    @ApiModelProperty(value = "交易类型")
    @Column(name = "trade_type")
    private String tradeType;

    // jhipster-needle-entity-add-field - Jhipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getMasterId() {
        return masterId;
    }

    public AdvertiserRequirement masterId(Long masterId) {
        this.masterId = masterId;
        return this;
    }

    public void setMasterId(Long masterId) {
        this.masterId = masterId;
    }

    public Double getMinAmount() {
        return minAmount;
    }

    public AdvertiserRequirement minAmount(Double minAmount) {
        this.minAmount = minAmount;
        return this;
    }

    public void setMinAmount(Double minAmount) {
        this.minAmount = minAmount;
    }

    public String getMeid() {
        return meid;
    }

    public AdvertiserRequirement meid(String meid) {
        this.meid = meid;
        return this;
    }

    public void setMeid(String meid) {
        this.meid = meid;
    }

    public String getOrderNo() {
        return orderNo;
    }

    public AdvertiserRequirement orderNo(String orderNo) {
        this.orderNo = orderNo;
        return this;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public String getWuid() {
        return wuid;
    }

    public AdvertiserRequirement wuid(String wuid) {
        this.wuid = wuid;
        return this;
    }

    public void setWuid(String wuid) {
        this.wuid = wuid;
    }

    public String getCity() {
        return city;
    }

    public AdvertiserRequirement city(String city) {
        this.city = city;
        return this;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCategory() {
        return category;
    }

    public AdvertiserRequirement category(String category) {
        this.category = category;
        return this;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getDate() {
        return date;
    }

    public AdvertiserRequirement date(String date) {
        this.date = date;
        return this;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public AdvertiserRequirement time(String time) {
        this.time = time;
        return this;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public Boolean isClicked() {
        return clicked;
    }

    public AdvertiserRequirement clicked(Boolean clicked) {
        this.clicked = clicked;
        return this;
    }

    public void setClicked(Boolean clicked) {
        this.clicked = clicked;
    }

    public Boolean isOverAvgAmount() {
        return overAvgAmount;
    }

    public AdvertiserRequirement overAvgAmount(Boolean overAvgAmount) {
        this.overAvgAmount = overAvgAmount;
        return this;
    }

    public void setOverAvgAmount(Boolean overAvgAmount) {
        this.overAvgAmount = overAvgAmount;
    }

    public String getOs() {
        return os;
    }

    public AdvertiserRequirement os(String os) {
        this.os = os;
        return this;
    }

    public void setOs(String os) {
        this.os = os;
    }

    public String getNetwork() {
        return network;
    }

    public AdvertiserRequirement network(String network) {
        this.network = network;
        return this;
    }

    public void setNetwork(String network) {
        this.network = network;
    }

    public String getSex() {
        return sex;
    }

    public AdvertiserRequirement sex(String sex) {
        this.sex = sex;
        return this;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getTradeType() {
        return tradeType;
    }

    public AdvertiserRequirement tradeType(String tradeType) {
        this.tradeType = tradeType;
        return this;
    }

    public void setTradeType(String tradeType) {
        this.tradeType = tradeType;
    }
    // jhipster-needle-entity-add-getters-setters - Jhipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        AdvertiserRequirement advertiserRequirement = (AdvertiserRequirement) o;
        if (advertiserRequirement.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), advertiserRequirement.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "AdvertiserRequirement{" +
            "id=" + getId() +
            ", masterId='" + getMasterId() + "'" +
            ", minAmount='" + getMinAmount() + "'" +
            ", meid='" + getMeid() + "'" +
            ", orderNo='" + getOrderNo() + "'" +
            ", wuid='" + getWuid() + "'" +
            ", city='" + getCity() + "'" +
            ", category='" + getCategory() + "'" +
            ", date='" + getDate() + "'" +
            ", time='" + getTime() + "'" +
            ", clicked='" + isClicked() + "'" +
            ", overAvgAmount='" + isOverAvgAmount() + "'" +
            ", os='" + getOs() + "'" +
            ", network='" + getNetwork() + "'" +
            ", sex='" + getSex() + "'" +
            ", tradeType='" + getTradeType() + "'" +
            "}";
    }
}
