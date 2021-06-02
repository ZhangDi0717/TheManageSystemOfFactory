package com.zhang.vo;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.annotation.JSONField;

import java.util.List;

public class MaterialResult {
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
    @JSONField(name = "code")
    private Integer code;
    @JSONField(name = "msg")
    private String msg;
    @JSONField(name = "data")
    private List<Material> materials;


    public MaterialResult() {
    }

    public MaterialResult(Integer code, String msg, List<Material> materials) {
        this.code = code;
        this.msg = msg;
        this.materials = materials;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public List<Material> getMaterials() {
        return materials;
    }

    public void setMaterials(List<Material> materials) {
        this.materials = materials;
    }
}

