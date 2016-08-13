package com.udacity.rasulava.capstone_project.model;

import com.udacity.rasulava.capstone_project.db.Product;
import com.udacity.rasulava.capstone_project.model.response.ResponseFood;

/**
 * Created by Maryia on 13.08.2016.
 */
public class FoodSearch {

    private Product productInDb;

    private ResponseFood responseFood;

    private String name;

    public FoodSearch(Product productInDb) {
        this.productInDb = productInDb;
        name = productInDb.getName();
    }

    public FoodSearch(ResponseFood responseFood) {
        this.responseFood = responseFood;
        name = responseFood.getName();
    }

    public Product getProductInDb() {
        return productInDb;
    }

    public ResponseFood getResponseFood() {
        return responseFood;
    }

    public String getName() {
        return name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        FoodSearch that = (FoodSearch) o;

        return name != null ? name.equals(that.name) : that.name == null;

    }

    @Override
    public int hashCode() {
        return name != null ? name.hashCode() : 0;
    }
}
