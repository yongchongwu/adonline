package com.ifuture.adonline.domain;


import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A Material.
 */
@Entity
@Table(name = "ads_material")
public class Material implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Size(max = 200)
    @Column(name = "position", length = 200, nullable = false)
    private String position;

    @NotNull
    @Size(max = 200)
    @Column(name = "tags", length = 200, nullable = false)
    private String tags;

    @NotNull
    @Size(max = 20)
    @Column(name = "start_date", length = 20, nullable = false)
    private String startDate;

    @NotNull
    @Size(max = 20)
    @Column(name = "end_date", length = 20, nullable = false)
    private String endDate;

    @NotNull
    @Column(name = "img_url", nullable = false)
    private String imgUrl;

    @NotNull
    @Column(name = "href_url", nullable = false)
    private String hrefUrl;

    @NotNull
    @Column(name = "customer_price", nullable = false)
    private Double customerPrice;

    @Column(name = "copy")
    private String copy;

    @NotNull
    @Size(max = 20)
    @Column(name = "collect_date", length = 20, nullable = false)
    private String collectDate;

    // jhipster-needle-entity-add-field - Jhipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPosition() {
        return position;
    }

    public Material position(String position) {
        this.position = position;
        return this;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public String getTags() {
        return tags;
    }

    public Material tags(String tags) {
        this.tags = tags;
        return this;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }

    public String getStartDate() {
        return startDate;
    }

    public Material startDate(String startDate) {
        this.startDate = startDate;
        return this;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public Material endDate(String endDate) {
        this.endDate = endDate;
        return this;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public Material imgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
        return this;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public String getHrefUrl() {
        return hrefUrl;
    }

    public Material hrefUrl(String hrefUrl) {
        this.hrefUrl = hrefUrl;
        return this;
    }

    public void setHrefUrl(String hrefUrl) {
        this.hrefUrl = hrefUrl;
    }

    public Double getCustomerPrice() {
        return customerPrice;
    }

    public Material customerPrice(Double customerPrice) {
        this.customerPrice = customerPrice;
        return this;
    }

    public void setCustomerPrice(Double customerPrice) {
        this.customerPrice = customerPrice;
    }

    public String getCopy() {
        return copy;
    }

    public Material copy(String copy) {
        this.copy = copy;
        return this;
    }

    public void setCopy(String copy) {
        this.copy = copy;
    }

    public String getCollectDate() {
        return collectDate;
    }

    public Material collectDate(String collectDate) {
        this.collectDate = collectDate;
        return this;
    }

    public void setCollectDate(String collectDate) {
        this.collectDate = collectDate;
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
        Material material = (Material) o;
        if (material.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), material.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Material{" +
            "id=" + getId() +
            ", position='" + getPosition() + "'" +
            ", tags='" + getTags() + "'" +
            ", startDate='" + getStartDate() + "'" +
            ", endDate='" + getEndDate() + "'" +
            ", imgUrl='" + getImgUrl() + "'" +
            ", hrefUrl='" + getHrefUrl() + "'" +
            ", customerPrice='" + getCustomerPrice() + "'" +
            ", copy='" + getCopy() + "'" +
            ", collectDate='" + getCollectDate() + "'" +
            "}";
    }
}
