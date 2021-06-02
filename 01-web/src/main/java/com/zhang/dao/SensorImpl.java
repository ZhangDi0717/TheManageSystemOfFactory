package com.zhang.dao;

import com.zhang.entity.Sensor;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 *  Sensor: 接口需要封装的实体类
 *  Integer:实体类的ID
*/
public interface SensorImpl  extends JpaRepository<Sensor,Integer> {
}
