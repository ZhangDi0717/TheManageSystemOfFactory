package com.zhang.vo.employeeTableResult;

import com.alibaba.fastjson.annotation.JSONField;

public class EmployeeTable {
    @JSONField(name = "employeeId")
    private Long employeeId;

    @JSONField(name = "username")
    private String username;

    @JSONField(name = "employeeName")
    private String employeeName;

    @JSONField(name = "sex")
    private String sex;

    @JSONField(name = "position")
    private String position;

    @JSONField(name = "telephone")
    private String telephone;

    public EmployeeTable() {
    }

    public EmployeeTable(Long employeeId, String username, String employeeName, String sex, String position, String telephone) {
        this.employeeId = employeeId;
        this.username = username;
        this.employeeName = employeeName;
        this.sex = sex;
        this.position = position;
        this.telephone = telephone;
    }

    public Long getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(Long employeeId) {
        this.employeeId = employeeId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmployeeName() {
        return employeeName;
    }

    public void setEmployeeName(String employeeName) {
        this.employeeName = employeeName;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    @Override
    public boolean equals(Object obj) {
        if(this.employeeId == ((EmployeeTable)obj).employeeId){
            return true;
        }else {
            return false;
        }
    }

    @Override
    public String toString() {
        return this.employeeId.toString();
    }

}
