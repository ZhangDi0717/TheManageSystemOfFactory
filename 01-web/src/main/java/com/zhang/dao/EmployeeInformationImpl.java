package com.zhang.dao;

import com.zhang.entity.Employee;
import com.zhang.entity.EmployeeInformation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface EmployeeInformationImpl extends JpaRepository<EmployeeInformation,Long> {
    EmployeeInformation findByEmployee(Employee employee);

    List<EmployeeInformation> findBySex(Integer sex);
}
