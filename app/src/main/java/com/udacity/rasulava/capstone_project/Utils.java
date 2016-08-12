package com.udacity.rasulava.capstone_project;

/**
 * Created by Maryia on 12.08.2016.
 */
public class Utils {

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

    /**
     * Uses Mifflin-St Jeor formula
     *
     * @param gender
     * @param age
     * @param weight
     * @param height
     * @param exercise
     * @param goal
     * @return
     */

    public static int getCaloriesNeed(GENDER gender, int age, int weight, int height, EXERCISE exercise, GOAL goal) {
        double kcal = 0;
        switch (gender) {
            case FEMALE:
                kcal = (10 * weight + 6.25 * height - 5 * age - 161) * exercise.getFactor() * goal.getFactor();
                break;
            case MALE:
                kcal = (10 * weight + 6.25 * height - 5 * age + 5) * exercise.getFactor() * goal.getFactor();
                break;
        }
        return (int) kcal;
    }
}
