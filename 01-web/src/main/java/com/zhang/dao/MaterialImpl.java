package com.zhang.dao;

import com.zhang.entity.Material;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MaterialImpl extends JpaRepository<Material,Integer> {

    List<Material> findAllByState(int sate);
}
