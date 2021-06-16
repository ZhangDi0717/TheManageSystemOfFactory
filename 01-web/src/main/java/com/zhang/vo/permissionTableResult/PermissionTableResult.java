package com.zhang.vo.permissionTableResult;

import com.alibaba.fastjson.annotation.JSONField;

import java.util.List;

public class PermissionTableResult {
    @JSONField(name = "code")
    private Integer code;

    @JSONField(name = "msg")
    private String msg;

    @JSONField(name = "count")
    private Integer count;

    @JSONField(name = "data")
    private List<PermissionTable> data;

    public PermissionTableResult() {
    }

    public PermissionTableResult(Integer code, String msg, Integer count, List<PermissionTable> data) {
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

    public List<PermissionTable> getData() {
        return data;
    }

    public void setData(List<PermissionTable> data) {
        this.data = data;
    }
}
