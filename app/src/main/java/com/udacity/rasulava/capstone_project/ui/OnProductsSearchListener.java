package com.udacity.rasulava.capstone_project.ui;

/**
 * Created by mrasulava on 8/11/2016.
 */
public interface OnProductsSearchListener {

    void onSearchSuccessful();

    void onSearchEmpty();

    void onSearchFailed();

    void onProductSelected(String foodId, String name);
}
