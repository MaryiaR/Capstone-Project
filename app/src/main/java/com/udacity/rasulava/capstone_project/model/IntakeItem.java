package com.udacity.rasulava.capstone_project.model;

import com.udacity.rasulava.capstone_project.db.Intake;
import com.udacity.rasulava.capstone_project.db.Product;

/**
 * Created by Maryia on 13.08.2016.
 */
public class IntakeItem {

    private Long id;

    private int weight;

    private String name;

    private int kcal;

    private int fat;

    private int carbs;

    private int protein;

    public IntakeItem(Intake intake) {
        Product product = intake.getProduct();

        setId(intake.getId());
        setName(product.getName());
        setWeight(intake.getWeight());

        double weightFactor = intake.getWeight() / 100.;

        setFat((int) (product.getFat() * weightFactor));
        setCarbs((int) (product.getCarbohydrate() * weightFactor));
        setProtein((int) (product.getProtein() * weightFactor));
        setKcal((int) (product.getCalories() * weightFactor));
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getKcal() {
        return kcal;
    }

    public void setKcal(int kcal) {
        this.kcal = kcal;
    }

    public int getFat() {
        return fat;
    }

    public void setFat(int fat) {
        this.fat = fat;
    }

    public int getCarbs() {
        return carbs;
    }

    public void setCarbs(int carbs) {
        this.carbs = carbs;
    }

    public int getProtein() {
        return protein;
    }

    public void setProtein(int protein) {
        this.protein = protein;
    }
}
