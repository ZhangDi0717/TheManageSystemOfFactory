package com.zhang.entity;

import javax.persistence.*;
import java.util.Collection;
import java.util.List;


//运送单
@Entity
@Table(name = "distribution")
public class Distribution {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "distribution")
    private List<Ingredient> ingredient;

    public List<Ingredient> getIngredient() {
        return ingredient;
    }

    public void setIngredient(List<Ingredient> ingredient) {
        this.ingredient = ingredient;
    }



    @ManyToOne(optional = false)
    private Employee employee;

    public Employee getEmployee() {
        return employee;
    }

    public void setEmployee(Employee employee) {
        this.employee = employee;
    }

    @OneToOne(mappedBy = "distribution", optional = false)
    private Requisition requisition;

    public Requisition getRequisition() {
        return requisition;
    }

    public void setRequisition(Requisition requisition) {
        this.requisition = requisition;
    }


    @OneToMany(cascade = CascadeType.ALL, mappedBy = "distribution")
    private List<MainIngredient> mainingredient;

    public List<MainIngredient> getMainingredient() {
        return mainingredient;
    }

    public void setMainingredient(List<MainIngredient> mainingredient) {
        this.mainingredient = mainingredient;
    }
}
