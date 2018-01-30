package com.ifuture.adonline.service.mapper;

import com.ifuture.adonline.domain.AdvertiserRequirement;
import com.ifuture.adonline.domain.Material;
import com.ifuture.adonline.service.dto.AdvertiserRequirementDTO;
import com.ifuture.adonline.service.dto.MaterialDTO;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import org.apache.commons.lang3.StringUtils;

@SuppressWarnings("unchecked")
public class AdsMapper {

    public static Material getMaterial(MaterialDTO dto) {
        Material obj = new Material();

        obj.setId(dto.getId());
        obj.setPosition(dto.getPosition());
        obj.setTags(dto.getTags());
        obj.setStartDate(dto.getStartDate());
        obj.setEndDate(dto.getEndDate());
        obj.setImgUrl(dto.getImgUrl());
        obj.setHrefUrl(dto.getHrefUrl());
        obj.setCustomerPrice(dto.getCustomerPrice());
        obj.setCopy(dto.getCopy());
        obj.setCollectDate(dto.getCollectDate());

        return obj;
    }

    public static MaterialDTO getMaterialDTO(Material obj) {
        MaterialDTO dto = new MaterialDTO();

        dto.setId(obj.getId());
        dto.setPosition(obj.getPosition());
        dto.setTags(obj.getTags());
        dto.setStartDate(obj.getStartDate());
        dto.setEndDate(obj.getEndDate());
        dto.setImgUrl(obj.getImgUrl());
        dto.setHrefUrl(obj.getHrefUrl());
        dto.setCustomerPrice(obj.getCustomerPrice());
        dto.setCopy(obj.getCopy());
        dto.setCollectDate(obj.getCollectDate());

        return dto;
    }

    public static AdvertiserRequirement getAdvertiserRequirement(AdvertiserRequirementDTO dto,
        String separator) {
        AdvertiserRequirement obj = new AdvertiserRequirement();

        obj.setId(dto.getId());
        obj.setMasterId(dto.getMasterId());
        obj.setMinAmount(dto.getMinAmount());

        obj.setMeid(listToString(dto.getMeid(), separator));
        obj.setOrderNo(listToString(dto.getOrderNo(), separator));
        obj.setWuid(listToString(dto.getWuid(), separator));

        obj.setCity(listToString(dto.getCity(), separator));
        obj.setCategory(listToString(dto.getCategory(), separator));
        obj.setDate(listToString(dto.getDate(), separator));
        obj.setTime(listToString(dto.getTime(), separator));

        obj.setClicked(dto.getClicked());
        obj.setOverAvgAmount(dto.getOverAvgAmount());
        obj.setOs(dto.getOs());
        obj.setNetwork(dto.getNetwork());
        obj.setSex(dto.getSex());
        obj.setTradeType(dto.getTradeType());

        return obj;
    }

    public static AdvertiserRequirementDTO getAdvertiserRequirementDTO(AdvertiserRequirement obj,
        String separator) {
        AdvertiserRequirementDTO dto = new AdvertiserRequirementDTO();

        dto.setId(obj.getId());
        dto.setMasterId(obj.getMasterId());
        dto.setMinAmount(obj.getMinAmount());

        dto.setMeid(stringToList(obj.getMeid(), separator, Integer.class));
        dto.setOrderNo(stringToList(obj.getOrderNo(), separator, Integer.class));
        dto.setWuid(stringToList(obj.getWuid(), separator, Integer.class));

        dto.setCity(stringToList(obj.getCity(), separator, String.class));
        dto.setCategory(stringToList(obj.getCategory(), separator, String.class));
        dto.setDate(stringToList(obj.getDate(), separator, String.class));
        dto.setTime(stringToList(obj.getTime(), separator, String.class));

        dto.setClicked(obj.isClicked());
        dto.setOverAvgAmount(obj.isOverAvgAmount());
        dto.setOs(obj.getOs());
        dto.setNetwork(obj.getNetwork());
        dto.setSex(obj.getSex());
        dto.setTradeType(obj.getTradeType());

        return dto;
    }


    public static String listToString(List list, String separator) {
        if (null != list && list.size() > 0) {
            return StringUtils.join(list, separator);
        }
        return null;
    }

    public static List stringToList(String str, String separator, Class clazz) {
        if (StringUtils.isNotBlank(str)) {
            if (String.class.getName().equalsIgnoreCase(clazz.getName())) {
                return Arrays.asList(str.split(separator));
            } else if (Long.class.getName().equalsIgnoreCase(clazz.getName())) {
                return Arrays.asList(str.split(separator)).stream()
                    .map(s -> Long.valueOf(s.trim())).collect(
                        Collectors.toList());
            } else if (Integer.class.getName().equalsIgnoreCase(clazz.getName())) {
                return Arrays.asList(str.split(separator)).stream()
                    .map(s -> Integer.valueOf(s.trim())).collect(
                        Collectors.toList());
            }
        }
        return null;
    }

}
