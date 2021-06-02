package com.zhang.entity;

import javax.persistence.*;
import java.util.Collection;
import java.util.Date;

//订单
@Entity
@Table(name = "requisition")
public class Requisition {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private Integer state;

    private Date date;

    private Date dateline;

    public Requisition() {
    }

    public Requisition(Integer state, Date date, Date dateline, Employee employee, Distribution distribution) {
        this.state = state;
        this.date = date;
        this.dateline = dateline;
        this.employee = employee;
        this.distribution = distribution;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Date getDateline() {
        return dateline;
    }

    public void setDateline(Date dateline) {
        this.dateline = dateline;
    }

    //订单申请人
    @ManyToOne(optional = false)
    private Employee employee;

    public Employee getEmployee() {
        return employee;
    }

    public void setEmployee(Employee employee) {
        this.employee = employee;
    }

//  订单
    @OneToOne(cascade = CascadeType.ALL, optional = false)
    private Distribution distribution;

    public Distribution getDistribution() {
        return distribution;
    }

    public void setDistribution(Distribution distribution) {
        this.distribution = distribution;
    }
}
