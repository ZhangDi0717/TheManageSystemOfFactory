package com.zhang.dao;

import com.zhang.entity.Distribution;
import com.zhang.entity.Employee;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DistributionImpl extends JpaRepository<Distribution,Integer> {
    List<Distribution> findByEmployee(Employee employee);
}
