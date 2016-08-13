package com.udacity.rasulava.capstone_project.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import java.util.List;

import de.greenrobot.dao.query.QueryBuilder;

/**
 * Created by Maryia on 12.08.2016.
 */
public class DBHelper {

    private final String DB_NAME = "products_db";

    private IntakeDao intakeDao;

    private ProductDao productDao;

    private static DBHelper instance;

    private DBHelper() {
    }

    public static DBHelper getInstance(Context context) {
        if (instance == null) {
            instance = new DBHelper();
            instance.init(context);
        }
        return instance;
    }

    public void init(Context context) {
        setupDb(context);
    }

    public void setupDb(Context context) {
        DaoMaster.DevOpenHelper masterHelper = new DaoMaster.DevOpenHelper(context, DB_NAME, null);
        SQLiteDatabase db = masterHelper.getWritableDatabase();
        DaoMaster master = new DaoMaster(db);
        DaoSession masterSession = master.newSession();
        intakeDao = masterSession.getIntakeDao();
        productDao = masterSession.getProductDao();
    }

    public List<Product> getProductsByName(String name) {
        List<Product> productList = productDao.queryBuilder().where(ProductDao.Properties.Name.like(name + "%")).build().list();
        return productList;
    }

    public List<Intake> getHistoryForDate(String date) {
        QueryBuilder<Intake> queryBuilder = intakeDao.queryBuilder().where(IntakeDao.Properties.Date.eq(date));
        List<Intake> history = queryBuilder.build().list();
        return history;
    }


    public long save(Product product) {
        return productDao.insert(product);
    }

    public long save(Intake history) {
        return intakeDao.insert(history);
    }
}
