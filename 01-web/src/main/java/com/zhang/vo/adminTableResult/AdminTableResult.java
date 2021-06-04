package com.zhang.vo.adminTableResult;

import com.alibaba.fastjson.annotation.JSONField;
import java.util.List;

/**
 * {
 *   "code": 0,
 *   "msg": "",
 *   "count": 1000,
 *   "data": [
 *     {
 *       "id": 68,
 *       "dateline": "2021-05-20 13:48:22.705000",
 *       "date": "2021-05-20 13:48:22.705000",
 *       "state": "0",
 *       "state1": "0"
 *     },
 */
public class AdminTableResult {
    @JSONField(name = "code")
    private Integer code;

    @JSONField(name = "msg")
    private String msg;

    @JSONField(name = "count")
    private Integer count;

    @JSONField(name = "data")
    private List<AdminTable> data;

    public AdminTableResult() {
    }

    public AdminTableResult(Integer code, String msg, Integer count, List<AdminTable> data) {
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

    public List<AdminTable> getData() {
        return data;
    }

    public void setData(List<AdminTable> data) {
        this.data = data;
    }
}
