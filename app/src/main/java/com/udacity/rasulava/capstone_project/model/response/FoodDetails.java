package com.udacity.rasulava.capstone_project.model.response;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by mrasulava on 8/9/2016.
 */
public class FoodDetails {

    @SerializedName("food_id")
    private String id;

    @SerializedName("food_name")
    private String name;

    @SerializedName("food_type")
    private String type;

    @SerializedName("food_url")
    private String url;

    private Servings mServings;

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

    public Servings getmServings() {
        return mServings;
    }

    public void setmServings(Servings mServings) {
        this.mServings = mServings;
    }

    public static class Servings {

        private List<Serving> serving;

        public List<Serving> getServing() {
            return serving;
        }

        public void setServing(List<Serving> serving) {
            this.serving = serving;
        }
    }

    public static class Serving {

        @SerializedName("serving_description")
        private String servingDescription;

        @SerializedName("serving_id")
        private String servingId;

        @SerializedName("calories")
        private String calories;

        @SerializedName("fat")
        private String fat;

        @SerializedName("protein")
        private String protein;

        @SerializedName("carbohydrate")
        private String carbohydrate;

        public String getServingDescription() {
            return servingDescription;
        }

        public void setServingDescription(String servingDescription) {
            this.servingDescription = servingDescription;
        }

        public String getServingId() {
            return servingId;
        }

        public void setServingId(String servingId) {
            this.servingId = servingId;
        }

        public String getCalories() {
            return calories;
        }

        public void setCalories(String calories) {
            this.calories = calories;
        }

        public String getFat() {
            return fat;
        }

        public void setFat(String fat) {
            this.fat = fat;
        }

        public String getProtein() {
            return protein;
        }

        public void setProtein(String protein) {
            this.protein = protein;
        }

        public String getCarbohydrate() {
            return carbohydrate;
        }

        public void setCarbohydrate(String carbohydrate) {
            this.carbohydrate = carbohydrate;
        }
    }

    public Serving get100grServing() {
        if (mServings == null || mServings.getServing().isEmpty())
            return null;
        Serving serving = mServings.getServing().get(0);
        for (Serving s : mServings.getServing()) {
            if ("100 g".equalsIgnoreCase(s.getServingDescription())) {
                return s;
            }
        }
        return serving;
    }
}
