package com.ifuture.adonline.service.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class AdsMaterialDTO implements Serializable {

    private String collect_date;

    private Integer count;

    private List<MaterialDTO> materials = new ArrayList<>();

    public String getCollect_date() {
        return collect_date;
    }

    public void setCollect_date(String collect_date) {
        this.collect_date = collect_date;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public List<MaterialDTO> getMaterials() {
        return materials;
    }

    public void setMaterials(List<MaterialDTO> materials) {
        this.materials = materials;
    }
}
