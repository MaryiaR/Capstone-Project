package com.udacity.rasulava.capstone_project.db;

import android.database.sqlite.SQLiteDatabase;

import java.util.Map;

import de.greenrobot.dao.AbstractDao;
import de.greenrobot.dao.AbstractDaoSession;
import de.greenrobot.dao.identityscope.IdentityScopeType;
import de.greenrobot.dao.internal.DaoConfig;

import com.udacity.rasulava.capstone_project.db.Product;
import com.udacity.rasulava.capstone_project.db.Intake;

import com.udacity.rasulava.capstone_project.db.ProductDao;
import com.udacity.rasulava.capstone_project.db.IntakeDao;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.

/**
 * {@inheritDoc}
 * 
 * @see de.greenrobot.dao.AbstractDaoSession
 */
public class DaoSession extends AbstractDaoSession {

    private final DaoConfig productDaoConfig;
    private final DaoConfig intakeDaoConfig;

    private final ProductDao productDao;
    private final IntakeDao intakeDao;

    public DaoSession(SQLiteDatabase db, IdentityScopeType type, Map<Class<? extends AbstractDao<?, ?>>, DaoConfig>
            daoConfigMap) {
        super(db);

        productDaoConfig = daoConfigMap.get(ProductDao.class).clone();
        productDaoConfig.initIdentityScope(type);

        intakeDaoConfig = daoConfigMap.get(IntakeDao.class).clone();
        intakeDaoConfig.initIdentityScope(type);

        productDao = new ProductDao(productDaoConfig, this);
        intakeDao = new IntakeDao(intakeDaoConfig, this);

        registerDao(Product.class, productDao);
        registerDao(Intake.class, intakeDao);
    }
    
    public void clear() {
        productDaoConfig.getIdentityScope().clear();
        intakeDaoConfig.getIdentityScope().clear();
    }

    public ProductDao getProductDao() {
        return productDao;
    }

    public IntakeDao getIntakeDao() {
        return intakeDao;
    }

}
