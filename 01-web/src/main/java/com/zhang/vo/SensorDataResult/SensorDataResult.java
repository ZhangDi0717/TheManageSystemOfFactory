package com.zhang.vo.SensorDataResult;

import com.alibaba.fastjson.annotation.JSONField;
import com.zhang.vo.employeeTableResult.EmployeeTable;

import java.util.List;

public class SensorDataResult {
    @JSONField(name = "code")
    private Integer code;

    @JSONField(name = "msg")
    private String msg;

    @JSONField(name = "count")
    private Integer count;

    @JSONField(name = "data")
    private List<SensorDatas> data;

    public SensorDataResult() {
    }

    public SensorDataResult(Integer code, String msg, Integer count, List<SensorDatas> data) {
        this.code = code;
        this.msg = msg;
        this.count = count;
        this.data = data;
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

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public List<SensorDatas> getData() {
        return data;
    }

    public void setData(List<SensorDatas> data) {
        this.data = data;
    }
}
