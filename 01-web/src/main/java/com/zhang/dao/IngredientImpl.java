package com.zhang.dao;

import com.zhang.entity.Ingredient;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IngredientImpl extends JpaRepository<Ingredient,Integer> {
}
