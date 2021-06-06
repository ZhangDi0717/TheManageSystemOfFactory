package com.zhang.dao;

import com.zhang.entity.Employee;
import com.zhang.entity.Position;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface EmployeeImpl extends JpaRepository<Employee,Long> {

    Employee findByUsername(String username);

    List<Employee> findByPosition(Position position);

    Employee findByUsernameNot(String username);

    Boolean existsByUsername(String username);
}
