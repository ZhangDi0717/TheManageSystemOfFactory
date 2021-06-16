package com.zhang.vo;

import com.alibaba.fastjson.annotation.JSONField;

import java.util.Date;

public class SensorDataResult1 {
    @JSONField(name = "value")
    private Double value;

    @JSONField(name = "time")
    private Date time;

    public SensorDataResult1() {
    }

    public SensorDataResult1(Double value, Date time) {
        this.value = value;
        this.time = time;
    }

    public Double getValue() {
        return value;
    }

    public void setValue(Double value) {
        this.value = value;
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }
}
