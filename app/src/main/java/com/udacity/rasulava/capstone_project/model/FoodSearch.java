package com.udacity.rasulava.capstone_project.model;

import com.udacity.rasulava.capstone_project.db.Product;
import com.udacity.rasulava.capstone_project.model.response.ResponseFood;

/**
 * Created by Maryia on 13.08.2016.
 */
public class FoodSearch {

    private Product productInDb;

    private ResponseFood responseFood;

    public FoodSearch(Product productInDb) {
        this.productInDb = productInDb;
    }

    public FoodSearch(ResponseFood responseFood) {
        this.responseFood = responseFood;
    }

    public Product getProductInDb() {
        return productInDb;
    }

    public ResponseFood getResponseFood() {
        return responseFood;
    }

    public String getName() {
        if (productInDb != null)
            return productInDb.getName();
        else
            return responseFood.getName();
    }
}
