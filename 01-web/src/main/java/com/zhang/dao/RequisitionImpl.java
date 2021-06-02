package com.zhang.dao;

import com.zhang.entity.Employee;
import com.zhang.entity.Requisition;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Date;
import java.util.List;

public interface RequisitionImpl extends JpaRepository<Requisition,Integer> {
    List<Requisition> getByEmployee(Employee employee);

    List<Requisition> findByState(Integer state);

    List<Requisition> findByIdAndState(Integer id,Integer state);

    List<Requisition> findByDatelineBetween(Date beginDate, Date endDate);
}
