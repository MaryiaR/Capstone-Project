package com.udacity.rasulava.capstone_project.model;

/**
 * Created by mrasulava on 7/6/2016.
 */

import com.google.gson.annotations.SerializedName;

public class Food {

    @SerializedName("food_description")
    private String description;

    @SerializedName("food_id")
    private String id;

    @SerializedName("food_name")
    private String name;

    @SerializedName("food_type")
    private String type;

    @SerializedName("food_url")
    private String url;

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
