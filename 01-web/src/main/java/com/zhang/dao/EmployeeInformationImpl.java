package com.zhang.dao;

import com.zhang.entity.Employee;
import com.zhang.entity.EmployeeInformation;
import org.springframework.data.jpa.repository.JpaRepository;


public interface EmployeeInformationImpl extends JpaRepository<EmployeeInformation,Long> {
    EmployeeInformation findByEmployee(Employee employee);

}
