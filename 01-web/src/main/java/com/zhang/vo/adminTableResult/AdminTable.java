package com.zhang.vo.adminTableResult;

import com.alibaba.fastjson.annotation.JSONField;

import java.util.Date;

/*
         "id": 68,
 *       "state": "0",
 *       "state1": "0"
 *       "date": "2021-05-20 13:48:22.705000",
         "dateline": "2021-05-20 13:48:22.705000",
         "applyEmployeeId"

 */
public class AdminTable {
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

    @JSONField(name = "applyEmployeeId")
    private Long applyEmployeeId;

    public AdminTable() {
    }

    public AdminTable(Integer id, Date dateline, Date date, Integer state, Integer state1, Long applyEmployeeId) {
        this.id = id;
        this.dateline = dateline;
        this.date = date;
        this.state = state;
        this.state1 = state1;
        this.applyEmployeeId = applyEmployeeId;
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

    public Long getApplyEmployeeId() {
        return applyEmployeeId;
    }

    public void setApplyEmployeeId(Long applyEmployeeId) {
        this.applyEmployeeId = applyEmployeeId;
    }
}
