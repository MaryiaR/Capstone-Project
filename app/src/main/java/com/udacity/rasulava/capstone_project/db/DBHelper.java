package com.udacity.rasulava.capstone_project.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.udacity.rasulava.capstone_project.DataBackupAgent;
import com.udacity.rasulava.capstone_project.Utils;
import com.udacity.rasulava.capstone_project.model.IntakeItem;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import de.greenrobot.dao.query.QueryBuilder;

/**
 * Created by Maryia on 12.08.2016.
 */
public class DBHelper {

    public static final String DB_NAME = "products_db";

    private IntakeDao intakeDao;

    private ProductDao productDao;

    private static DBHelper instance;

    private SQLiteDatabase db;

    private Context context;

    public DBHelper(Context context) {
        this.context = context;
        setupDb();
        closeDb();
    }

    public void setupDb() {
        DaoMaster.DevOpenHelper masterHelper = new DaoMaster.DevOpenHelper(context, DB_NAME, null);
        db = masterHelper.getWritableDatabase();
        DaoMaster master = new DaoMaster(db);
        DaoSession masterSession = master.newSession();
        intakeDao = masterSession.getIntakeDao();
        productDao = masterSession.getProductDao();
    }

    private void closeDb() {
        db.close();
    }

    public List<Product> getProductsByName(String name) {
        setupDb();
        List<Product> productList = productDao.queryBuilder().where(ProductDao.Properties.Name.like("%" + name + "%")).build().list();
        closeDb();
        return productList;
    }

    public List<IntakeItem> getHistoryForDate(Date date) {
        setupDb();
        QueryBuilder<Intake> queryBuilder = intakeDao.queryBuilder().where(IntakeDao.Properties.Date.eq(Utils.roundDateToDay(date)));
        List<Intake> dbIntakeList = queryBuilder.build().list();
        List<IntakeItem> intakeList = new ArrayList<>();
        for (Intake dbIntake : dbIntakeList) {
            intakeList.add(new IntakeItem(dbIntake));
        }
        closeDb();
        return intakeList;
    }


    public long save(Product product) {
        setupDb();
        long id = productDao.insert(product);
        closeDb();
        DataBackupAgent.requestBackup(context);
        return id;
    }

    public long save(Intake intake) {
        setupDb();
        Date date = Utils.roundDateToDay(intake.getDate());
        intake.setDate(date);
        long id = intakeDao.insert(intake);
        closeDb();
        DataBackupAgent.requestBackup(context);
        return id;
    }

    public boolean deleteOldData() {
        boolean isSuccess = false;
        setupDb();
        Date weekAgoDate = Utils.getDateWeekAgo();
        List<Intake> intakes = intakeDao.queryBuilder().where(IntakeDao.Properties.Date.le(weekAgoDate)).list();
        if (intakes != null && !intakes.isEmpty()) {
            intakeDao.deleteInTx(intakes);
            DataBackupAgent.requestBackup(context);
            isSuccess = true;
        }
        closeDb();
        return isSuccess;
    }


    public void delete(Long intakeId) {
        setupDb();
        intakeDao.deleteByKey(intakeId);
        closeDb();
        DataBackupAgent.requestBackup(context);
    }
}
