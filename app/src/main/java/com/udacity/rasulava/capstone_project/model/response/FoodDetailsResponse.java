package com.udacity.rasulava.capstone_project.model.response;

import com.google.gson.annotations.SerializedName;

/**
 * Created by mrasulava on 8/9/2016.
 */
public class FoodDetailsResponse {

    @SerializedName("food")
    FoodDetails foods;

    public FoodDetails getFoods() {
        return foods;
    }

    public void setFoods(FoodDetails foods) {
        this.foods = foods;
    }

}
