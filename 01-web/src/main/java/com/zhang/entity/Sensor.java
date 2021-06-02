package com.zhang.entity;

import javax.persistence.*;
import javax.swing.*;
import java.util.Collection;
import java.util.Set;

@Entity
@Table(name = "sensor")
public class Sensor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String unit;

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
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

    @Override
    public String toString() {
        return "Sensor{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER, mappedBy = "sensor")
    private Set<SensorData> sensordate;

    public Set<SensorData> getSensordate() {
        return sensordate;
    }

    public void setSensordate(Set<SensorData> sensordate) {
        this.sensordate = sensordate;
    }
}
