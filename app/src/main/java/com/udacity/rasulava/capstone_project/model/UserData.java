package com.udacity.rasulava.capstone_project.model;

/**
 * Created by Maryia on 12.08.2016.
 */
public class UserData {
    private int age;
    private GENDER gender;
    private int weight;
    private int height;
    private EXERCISE exercise;
    private GOAL goal;
    private int kcal;

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public GENDER getGender() {
        return gender;
    }

    public void setGender(GENDER gender) {
        this.gender = gender;
    }

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public EXERCISE getExercise() {
        return exercise;
    }

    public void setExercise(EXERCISE exercise) {
        this.exercise = exercise;
    }

    public GOAL getGoal() {
        return goal;
    }

    public void setGoal(GOAL goal) {
        this.goal = goal;
    }

    public int getKcal() {
        return kcal;
    }

    public void setKcal(int kcal) {
        this.kcal = kcal;
    }

    public enum GENDER {
        FEMALE, MALE
    }

    public enum EXERCISE {
        LITTLE(1.2), THREE_TIMES(1.375), FITNESS_FIVE_TIMES(1.4625), INTENSE_FIVE_TIMES(1.550), DAILY(1.6375), INTENSE_DAILY(1.725), DAILY_PHYSICAL(1.9);

        double factor;

        EXERCISE(double factor) {
            this.factor = factor;
        }

        public double getFactor() {
            return factor;
        }
    }

    public enum GOAL {
        MAINTENANCE(1), LOSS(0.8), EXTREME_LOSS(0.6);

        double factor;

        GOAL(double factor) {
            this.factor = factor;
        }

        public double getFactor() {
            return factor;
        }
    }
}
