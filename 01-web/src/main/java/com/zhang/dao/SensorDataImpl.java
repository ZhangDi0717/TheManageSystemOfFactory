package com.zhang.dao;

import com.zhang.entity.Sensor;
import com.zhang.entity.SensorData;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SensorDataImpl extends JpaRepository<SensorData,Long> {
    //通过传感器查询数据
    List<SensorData> findBySensor(Sensor sensor);

    //通过传感器查询最新数据
    List<SensorData> findBySensorOrderByTimeDesc(Sensor sensor);

    SensorData findTop1BySensorOrderByTime(Sensor sensor);

    SensorData findTop1BySensorOrderByTimeDesc(Sensor sensor);

    SensorData findTop1BySensorOrderByTimeAsc(Sensor sensor);

    SensorData findLimit1BySensor(Sensor sensor);

    SensorData findMaxTimeBySensor(Sensor sensor);

    SensorData findTopBySensorOrderByTimeDesc(Sensor sensor);

    List<SensorData> findTop4BySensorOrderByTimeAsc(Sensor sensor);

    List<SensorData> findTop4BySensorOrderByTimeDesc(Sensor sensor);
}
