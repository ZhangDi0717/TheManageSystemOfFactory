package com.zhang.dao;

import com.zhang.entity.Permission;
import org.springframework.data.jpa.repository.JpaRepository;


public interface PermissionImpl extends JpaRepository<Permission,Long> {
}
