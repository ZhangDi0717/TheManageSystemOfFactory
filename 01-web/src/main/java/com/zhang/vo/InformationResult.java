package com.zhang.vo;

import com.alibaba.fastjson.annotation.JSONField;

public class InformationResult {
    @JSONField(name = "code")
    private Integer code;

    @JSONField(name = "msg")
    private String msg;

    @JSONField(name = "name")
    private String name;

    //部门

    //职位
    @JSONField(name = "position")
    private String position;

    @JSONField(name = "sex")
    private Integer sex;

    @JSONField(name = "phone")
    private String phone;

    @JSONField(name = "email")
    private String email;

    @JSONField(name = "address")
    private String address;

    public InformationResult() {
    }

    public InformationResult(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public InformationResult(Integer code, String msg, String name,String position,Integer sex, String phone, String email, String address) {
        this.code = code;
        this.msg = msg;
        this.name = name;
        this.position = position;
        this.sex = sex;
        this.phone = phone;
        this.email = email;
        this.address = address;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getSex() {
        return sex;
    }

    public void setSex(Integer sex) {
        this.sex = sex;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
