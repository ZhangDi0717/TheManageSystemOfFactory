package com.zhang.entity;

import javax.persistence.*;
import java.util.Collection;

@Entity
@Table(name = "material")
public class Material {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String name;
    private String unit;

    //主料为1；辅料为0
    private Integer state;

    private Double allowance;

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }

    public Double getAllowance() {
        return allowance;
    }

    public void setAllowance(Double allowance) {
        this.allowance = allowance;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }


    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "material")
    private Collection<MainIngredient> mainingredient;

    public Collection<MainIngredient> getMainingredient() {
        return mainingredient;
    }

    public void setMainingredient(Collection<MainIngredient> mainingredient) {
        this.mainingredient = mainingredient;
    }

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "material")
    private Collection<Ingredient> ingredient;

    public Collection<Ingredient> getIngredient() {
        return ingredient;
    }

    public void setIngredient(Collection<Ingredient> ingredient) {
        this.ingredient = ingredient;
    }
}
