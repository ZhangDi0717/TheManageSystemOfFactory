package com.zhang.entity;

import javax.persistence.*;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "employee")
public class Employee {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true,nullable = false)
    private String username;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private Boolean isexpired;

    @Column(nullable = false)
    private Boolean islock;

    @Column(nullable = false)
    private Boolean iscredentials;

    @Column(nullable = false)
    private Boolean isenable;


    @Column(nullable = false)
    private Date createtime;

    @Column(nullable = false)
    private Date logintime;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Boolean getIsenable() {
        return isenable;
    }

    public Boolean getIsexpired() {
        return isexpired;
    }

    public void setIsexpired(Boolean isexpired) {
        this.isexpired = isexpired;
    }


    public void setIsenable(Boolean isenable) {
        this.isenable = isenable;
    }

    public Boolean getIslock() {
        return islock;
    }

    public void setIslock(Boolean islock) {
        this.islock = islock;
    }

    public Boolean getIscredentials() {
        return iscredentials;
    }

    public void setIscredentials(Boolean iscredentials) {
        this.iscredentials = iscredentials;
    }

    public Date getCreatetime() {
        return createtime;
    }

    public void setCreatetime(Date createtime) {
        this.createtime = createtime;
    }

    public Date getLogintime() {
        return logintime;
    }

    public void setLogintime(Date logintime) {
        this.logintime = logintime;
    }

    //个人信息
    @OneToOne(cascade = CascadeType.ALL, mappedBy = "employee", optional = false)
    private EmployeeInformation employeeinformation;

    public EmployeeInformation getEmployeeinformation() {
        return employeeinformation;
    }

    public void setEmployeeinformation(EmployeeInformation employeeinformation) {
        this.employeeinformation = employeeinformation;
    }

    //所属职位-部门
    @ManyToMany
    private Set<Position> position;

    public Set<Position> getPosition() {
        return position;
    }

    public void setPosition(Set<Position> position) {
        this.position = position;
    }

    //所属订单
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "employee")
    private Set<Requisition> requisition;

    public Set<Requisition> getRequisition() {
        return requisition;
    }

    public void setRequisition(Set<Requisition> requisition) {
        this.requisition = requisition;
    }

    //所属分配单
    @OneToMany(fetch = FetchType.EAGER, mappedBy = "employee")
    private Set<Distribution> distribution;

    public Set<Distribution> getDistribution() {
        return distribution;
    }

    public void setDistribution(Set<Distribution> distribution) {
        this.distribution = distribution;
    }
}
