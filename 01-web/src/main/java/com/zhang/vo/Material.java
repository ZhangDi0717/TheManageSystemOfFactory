package com.zhang.vo;

import com.alibaba.fastjson.annotation.JSONField;

public class Material {
    /**
     * "code": 1,
     *   "msg": "主料信息发送成功",
     *   "data":[
     *     {
     *       "id" : 1,
     *       "name": "主料1",
     *       "unit": "kg"
     *     },
     */
    @JSONField(name = "id")
    private Integer id;
    @JSONField(name = "name")
    private String name;
    @JSONField(name = "unit")
    private String unit;


    public Material() {
    }

    public Material(Integer id, String name, String unit) {
        this.id = id;
        this.name = name;
        this.unit = unit;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }
}
