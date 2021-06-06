package com.zhang.vo.employeeTableResult;

import com.alibaba.fastjson.annotation.JSONField;

import java.util.List;

public class EmployeeTableResult {
    @JSONField(name = "code")
    private Integer code;

    @JSONField(name = "msg")
    private String msg;

    @JSONField(name = "count")
    private Integer count;

    @JSONField(name = "data")
    private List<EmployeeTable> data;

    public EmployeeTableResult() {
    }

    public EmployeeTableResult(Integer code, String msg, Integer count, List<EmployeeTable> data) {
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

    public List<EmployeeTable> getData() {
        return data;
    }

    public void setData(List<EmployeeTable> data) {
        this.data = data;
    }
}
