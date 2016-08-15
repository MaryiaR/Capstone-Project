package com.udacity.rasulava.capstone_project.db;


public class Intake {

    private Long id;

    private java.util.Date date;

    private int weight;

    private Product product;


    public Intake() {
    }

    public Intake(Long id) {
        this.id = id;
    }

    public Intake(Long id, java.util.Date date, int weight) {
        this.id = id;
        this.date = date;
        this.weight = weight;
    }


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }


    public java.util.Date getDate() {
        return date;
    }

    public void setDate(java.util.Date date) {
        this.date = date;
    }

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

}
