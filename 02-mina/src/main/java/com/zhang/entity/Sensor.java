package com.zhang.entity;

import java.util.Date;

public class Sensor {
    private Integer id;
    private Double value;
    private Date time;

    public Sensor(Integer id, Double value, Date time) {
        this.id = id;
        this.value = value;
        this.time = time;
    }

    public Sensor(){}

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
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

    @Override
    public String toString() {
        return "Sensor{" +
                "id=" + id +
                ", value=" + value +
                ", time=" + time +
                '}';
    }
}
