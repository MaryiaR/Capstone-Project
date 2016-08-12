package com.udacity.rasulava.capstone_project;

/**
 * Created by Maryia on 12.08.2016.
 */
public interface ResultListener<T> {
    void onSuccess(T result);

    void onFailure();
}
