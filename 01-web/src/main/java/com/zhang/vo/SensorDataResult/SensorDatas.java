package com.zhang.vo.SensorDataResult;

import com.alibaba.fastjson.annotation.JSONField;

import java.util.Date;

public class SensorDatas {

    @JSONField(name = "id")
    private Integer id;

    @JSONField(name = "temperature")
    private Double temperature;

    @JSONField(name = "time")
    private Date time;

    public SensorDatas() {
    }


    public SensorDatas(Integer id, Double temperature, Date time) {
        this.id = id;
        this.temperature = temperature;
        this.time = time;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Double getTemperature() {
        return temperature;
    }

    public void setTemperature(Double temperature) {
        this.temperature = temperature;
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }
}
