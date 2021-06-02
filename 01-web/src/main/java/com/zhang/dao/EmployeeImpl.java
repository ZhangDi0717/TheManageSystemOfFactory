package com.zhang.dao;

import com.zhang.entity.Employee;
import org.springframework.data.jpa.repository.JpaRepository;


public interface EmployeeImpl extends JpaRepository<Employee,Long> {

    Employee findByUsername(String username);
}
