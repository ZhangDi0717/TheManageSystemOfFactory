package com.zhang.entity;

import javax.persistence.*;

@Entity
@Table(name = "mainingredient")
public class MainIngredient {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false)
    private Double dosage;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Double getDosage() {
        return dosage;
    }

    public void setDosage(Double dosage) {
        this.dosage = dosage;
    }


    public MainIngredient() {

    }

    public MainIngredient(Double dosage, Distribution distribution, Material material) {
        this.dosage = dosage;
        this.distribution = distribution;
        this.material = material;
    }

    //所属分配单
    @ManyToOne(optional = false)
    private Distribution distribution;

    public Distribution getDistribution() {
        return distribution;
    }

    public void setDistribution(Distribution distribution) {
        this.distribution = distribution;
    }


    @ManyToOne(optional = false)
    private Material material;

    public Material getMaterial() {
        return material;
    }

    public void setMaterial(Material material) {
        this.material = material;
    }
}
