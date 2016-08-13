package com.udacity.rasulava.capstone_project.db;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;

import de.greenrobot.dao.AbstractDao;
import de.greenrobot.dao.Property;
import de.greenrobot.dao.internal.DaoConfig;

import com.udacity.rasulava.capstone_project.db.Product;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.
/** 
 * DAO for table "PRODUCT".
*/
public class ProductDao extends AbstractDao<Product, Long> {

    public static final String TABLENAME = "PRODUCT";

    /**
     * Properties of entity Product.<br/>
     * Can be used for QueryBuilder and for referencing column names.
    */
    public static class Properties {
        public final static Property Id = new Property(0, Long.class, "id", true, "_id");
        public final static Property Name = new Property(1, String.class, "name", false, "NAME");
        public final static Property Calories = new Property(2, int.class, "calories", false, "CALORIES");
        public final static Property Fat = new Property(3, int.class, "fat", false, "FAT");
        public final static Property Protein = new Property(4, int.class, "protein", false, "PROTEIN");
        public final static Property Carbohydrate = new Property(5, int.class, "carbohydrate", false, "CARBOHYDRATE");
    };


    public ProductDao(DaoConfig config) {
        super(config);
    }
    
    public ProductDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
    }

    /** Creates the underlying database table. */
    public static void createTable(SQLiteDatabase db, boolean ifNotExists) {
        String constraint = ifNotExists? "IF NOT EXISTS ": "";
        db.execSQL("CREATE TABLE " + constraint + "\"PRODUCT\" (" + //
                "\"_id\" INTEGER PRIMARY KEY AUTOINCREMENT ," + // 0: id
                "\"NAME\" TEXT NOT NULL ," + // 1: name
                "\"CALORIES\" INTEGER NOT NULL ," + // 2: calories
                "\"FAT\" INTEGER NOT NULL ," + // 3: fat
                "\"PROTEIN\" INTEGER NOT NULL ," + // 4: protein
                "\"CARBOHYDRATE\" INTEGER NOT NULL );"); // 5: carbohydrate
    }

    /** Drops the underlying database table. */
    public static void dropTable(SQLiteDatabase db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "\"PRODUCT\"";
        db.execSQL(sql);
    }

    /** @inheritdoc */
    @Override
    protected void bindValues(SQLiteStatement stmt, Product entity) {
        stmt.clearBindings();
 
        Long id = entity.getId();
        if (id != null) {
            stmt.bindLong(1, id);
        }
        stmt.bindString(2, entity.getName());
        stmt.bindLong(3, entity.getCalories());
        stmt.bindLong(4, entity.getFat());
        stmt.bindLong(5, entity.getProtein());
        stmt.bindLong(6, entity.getCarbohydrate());
    }

    /** @inheritdoc */
    @Override
    public Long readKey(Cursor cursor, int offset) {
        return cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0);
    }    

    /** @inheritdoc */
    @Override
    public Product readEntity(Cursor cursor, int offset) {
        Product entity = new Product( //
            cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0), // id
            cursor.getString(offset + 1), // name
            cursor.getInt(offset + 2), // calories
            cursor.getInt(offset + 3), // fat
            cursor.getInt(offset + 4), // protein
            cursor.getInt(offset + 5) // carbohydrate
        );
        return entity;
    }
     
    /** @inheritdoc */
    @Override
    public void readEntity(Cursor cursor, Product entity, int offset) {
        entity.setId(cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0));
        entity.setName(cursor.getString(offset + 1));
        entity.setCalories(cursor.getInt(offset + 2));
        entity.setFat(cursor.getInt(offset + 3));
        entity.setProtein(cursor.getInt(offset + 4));
        entity.setCarbohydrate(cursor.getInt(offset + 5));
     }
    
    /** @inheritdoc */
    @Override
    protected Long updateKeyAfterInsert(Product entity, long rowId) {
        entity.setId(rowId);
        return rowId;
    }
    
    /** @inheritdoc */
    @Override
    public Long getKey(Product entity) {
        if(entity != null) {
            return entity.getId();
        } else {
            return null;
        }
    }

    /** @inheritdoc */
    @Override    
    protected boolean isEntityUpdateable() {
        return true;
    }
    
}
