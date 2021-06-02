package com.zhang.dao;

import com.zhang.entity.Position;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;


@Transactional
public interface PositionImpl extends JpaRepository<Position,Long> {
}
