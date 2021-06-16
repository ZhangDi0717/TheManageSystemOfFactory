package com.zhang.controller;

import com.alibaba.fastjson.JSONObject;
import com.zhang.dao.SensorDataImpl;
import com.zhang.dao.SensorImpl;
import com.zhang.entity.Sensor;
import com.zhang.entity.SensorData;
import com.zhang.vo.SensorDataResult.SensorDataResult;
import com.zhang.vo.SensorDataResult.SensorDatas;
import com.zhang.vo.SensorDataResult1;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Controller
public class ProductionController {


    @Autowired
    private SensorImpl sensorimpl;

    @Autowired
    private SensorDataImpl sensorDataimpl;




    /**
     * 请求地址 ： production/manage
     *
     *
     *
     * 生产参数的检测
     * @return
     */
    @RequestMapping(value = "/production/manage")
    private ModelAndView productionManage(){
        System.out.println("go into productionManage");
        ModelAndView mv = new ModelAndView();

        mv.setViewName("static/page/admin/production");
        return mv;
    }




    /**
     * 获取传感器数据
     * @param param
     * @return
     */
    @RequestMapping(value = "/getsensordata",method = RequestMethod.POST)
    @ResponseBody
    String getSensorData(@RequestBody Map param) {//json:{"id":1,"value":20.0,"time":1623588666664}
        System.out.println("go into getSensorData");

        SensorData sensorData = new SensorData();
        String responseString = null;

        try {
            Sensor sensor = sensorimpl.getOne(Integer.parseInt(param.get("id").toString()));
            sensorData.setSensor(sensor);

            sensorData.setValue(Double.parseDouble(param.get("value").toString()));

            SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

            sensorData.setTime(new Date(Long.parseLong(param.get("time").toString())));

            sensorDataimpl.save(sensorData);
            responseString = new Date().toString();

        }catch (Exception e){
            System.out.println("getSensorData processing failed");
            System.out.println(e.toString());
            responseString = "getSensorData processing failed ======== "+ new Date().toString();
        }

        return responseString;
    }


    //向Web推送数据
    @RequestMapping(value = "/production/pushDataFir",method = RequestMethod.POST)
    @ResponseBody
    public JSONObject pushDataFir(){
        System.out.println("go into pushDataFir");

        SensorDataResult sensorDataResult = new SensorDataResult();

        Sensor sensor = sensorimpl.getOne(1);

        List<SensorData> sensorDataList = sensorDataimpl.findBySensor(sensor);

        List<SensorDatas> sensorDatasList = new ArrayList<SensorDatas>();


        for (SensorData sensorData :sensorDataList) {
            sensorDatasList.add(new SensorDatas(
                    sensorData.getSensor().getId(),
                    sensorData.getValue(),
                    sensorData.getTime())
            );
        }

        sensorDataResult.setCode(0);
        sensorDataResult.setMsg("查询成功");
        sensorDataResult.setCount(sensorDatasList.size());
        sensorDataResult.setData(sensorDatasList);

        return (JSONObject) JSONObject.toJSON(sensorDataResult);
    }

    //向Web推送数据
    @RequestMapping(value = "/production/pushData",method = RequestMethod.POST)
    @ResponseBody
    public JSONObject pushData(Integer id){
        System.out.println("go into pushData");

        Sensor sensor = sensorimpl.getOne(id);
        SensorData sensorData = sensorDataimpl.findTopBySensorOrderByTimeDesc(sensor);

        List<SensorData> top4BySensorOrderByTimeAsc = sensorDataimpl.findTop4BySensorOrderByTimeAsc(sensor);

        List<SensorData> top4BySensorOrderByTimeDesc = sensorDataimpl.findTop4BySensorOrderByTimeDesc(sensor);


        SensorDataResult1 sensorDataResult1 = new SensorDataResult1(sensorData.getValue(), sensorData.getTime());

        return (JSONObject) JSONObject.toJSON(sensorDataResult1);
    }
}
