package com.zhang.vo.applyTableResult;

import com.alibaba.fastjson.annotation.JSONField;

import java.util.Date;

/*
{
 *       "id": 68,
 *       "dateline": "2021-05-20 13:48:22.705000",
 *       "date": "2021-05-20 13:48:22.705000",
 *       "state": "0",
 *       "state1": "0"
 *     }
 */
public class ApplyTable {

    @JSONField(name = "id")
    private Integer id;

    @JSONField(name = "dateline")
    private Date dateline;

    @JSONField(name = "date")
    private Date date;

    @JSONField(name = "state")
    private Integer state;

    @JSONField(name = "state1")
    private Integer state1;

    public ApplyTable() {
    }

    public ApplyTable(Integer id, Date dateline, Date date, Integer state, Integer state1) {
        this.id = id;
        this.dateline = dateline;
        this.date = date;
        this.state = state;
        this.state1 = state1;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Date getDateline() {
        return dateline;
    }

    public void setDateline(Date dateline) {
        this.dateline = dateline;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }

    public Integer getState1() {
        return state1;
    }

    public void setState1(Integer state1) {
        this.state1 = state1;
    }
}
