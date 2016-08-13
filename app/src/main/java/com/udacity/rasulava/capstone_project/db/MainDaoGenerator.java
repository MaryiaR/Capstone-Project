package com.udacity.rasulava.capstone_project.db;

import de.greenrobot.daogenerator.DaoGenerator;
import de.greenrobot.daogenerator.Entity;
import de.greenrobot.daogenerator.Property;
import de.greenrobot.daogenerator.Schema;

/**
 * Created by Maryia on 12.08.2016.
 */
public class MainDaoGenerator {
    private static final String PROJECT_DIR = System.getProperty("user.dir");

    public static void main(String[] args) {
        Schema schema = new Schema(1, "com.udacity.rasulava.capstone_project.db");
        schema.enableKeepSectionsByDefault();

        addTables(schema);

        try {
            new DaoGenerator().generateAll(schema, PROJECT_DIR + "\\app\\src\\main\\java");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void addTables(final Schema schema) {
        Entity product = addProduct(schema);
        Entity intake = addIntake(schema);

        Property foodId = intake.addLongProperty("foodId").notNull().getProperty();
        intake.addToOne(product, foodId);
    }

    private static Entity addProduct(final Schema schema) {
        Entity product = schema.addEntity("Product");
        product.addIdProperty().primaryKey().autoincrement();
        product.addStringProperty("name").notNull();
        product.addIntProperty("calories").notNull();
        product.addIntProperty("fat").notNull();
        product.addIntProperty("protein").notNull();
        product.addIntProperty("carbohydrate").notNull();
        return product;
    }

    private static Entity addIntake(final Schema schema) {
        Entity history = schema.addEntity("Intake");
        history.addIdProperty().primaryKey().autoincrement();
        history.addDateProperty("date").notNull();
        history.addIntProperty("weight").notNull();
        return history;
    }
}
