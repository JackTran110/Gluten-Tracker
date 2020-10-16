package com.example.cst8334_glutentracker;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class glutenDbHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "GlutenTracker.db";
    public static final int DATABASE_VERSION = 1;

    public glutenDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_PRODUCTS);
        db.execSQL(SQL_CREATE_RECEIPTS);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(SQL_DELETE_PRODUCTS);
        db.execSQL(SQL_DELETE_RECEIPTS);
        onCreate(db);
    }

    private static final String SQL_CREATE_PRODUCTS = "CREATE TABLE " +
            databaseActivity.Products.TABLE_NAME + " (" +
            databaseActivity.Products.COLUMN_NAME_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            databaseActivity.Products.COLUMN_NAME_PNAME + " TEXT, " +
            databaseActivity.Products.COLUMN_NAME_DESCRIPTION + " TEXT, " +
            databaseActivity.Products.COLUMN_NAME_PRICE + "REAL, " +
            databaseActivity.Products.COLUMN_NAME_GLUTEN + " INTEGER, " +
            databaseActivity.Products.COLUMN_NAME_RID + " INTEGER, " +
            "CONSTRAINT " + "fk_" + databaseActivity.Receipts.TABLE_NAME +
            " FOREIGN KEY (" + databaseActivity.Products.COLUMN_NAME_RID +
            ") REFERENCES " + databaseActivity.Receipts.TABLE_NAME + "(" +
            databaseActivity.Receipts.COLUMN_NAME_ID + "))";

    private static final String SQL_DELETE_PRODUCTS = "DROP TABLE IF EXISTS " +
            databaseActivity.Products.TABLE_NAME;

    private static final String SQL_CREATE_RECEIPTS = "CREATE TABLE " +
            databaseActivity.Receipts.TABLE_NAME + " (" +
            databaseActivity.Receipts.COLUMN_NAME_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            databaseActivity.Receipts.COLUMN_NAME_FILE + " TEXT, " +
            databaseActivity.Receipts.COLUMN_NAME_DEDUCTION + " REAL, " +
            databaseActivity.Receipts.COLUMN_NAME_DATE + " TEXT)";

    private static final String SQL_DELETE_RECEIPTS = "DROP TABLE IF EXISTS " +
            databaseActivity.Receipts.TABLE_NAME;
}
