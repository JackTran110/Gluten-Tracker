package com.example.cst8334_glutentracker;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.entity.Product;
import com.example.entity.Receipt;

import java.util.ArrayList;
import java.util.List;

public class glutenDbHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "GlutenTracker.db";
    public static final int DATABASE_VERSION = 2;

    public glutenDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_PRODUCTS);
        db.execSQL(SQL_CREATE_RECEIPTS);
        db.execSQL(SQL_CREATE_PRODUCT_RECEIPT);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(SQL_DELETE_PRODUCTS);
        db.execSQL(SQL_DELETE_RECEIPTS);
        db.execSQL(SQL_DELETE_PRODUCT_RECEIPT);
        onCreate(db);
    }

    public long insertIntoProductsTable(SQLiteDatabase db, Product product){
        ContentValues cv = new ContentValues();
        cv.put(databaseActivity.Products.COLUMN_NAME_PNAME, product.getProductName());
        cv.put(databaseActivity.Products.COLUMN_NAME_DESCRIPTION, product.getProductDescription());
        cv.put(databaseActivity.Products.COLUMN_NAME_PRICE, product.getPrice());
        cv.put(databaseActivity.Products.COLUMN_NAME_GLUTEN, (product.isGlutenFree() ? 0:1));
        return db.insert(databaseActivity.Products.TABLE_NAME, null, cv);
    }

    public long insertIntoReceiptsTable(SQLiteDatabase db, List<Product> products, String file, double deduction, String date){
        ContentValues cv = new ContentValues();
        cv.put(databaseActivity.Receipts.COLUMN_NAME_FILE, file);
        cv.put(databaseActivity.Receipts.COLUMN_NAME_DEDUCTION, deduction);
        cv.put(databaseActivity.Receipts.COLUMN_NAME_DATE, date);
        long id = db.insert(databaseActivity.Receipts.TABLE_NAME, null, cv);
        for(Product product: products){
            insertIntoProductReceiptTable(db, product.getId(), id);
        }
        return id;
    }

    private long insertIntoProductReceiptTable(SQLiteDatabase db, long productID, long receiptID){
        ContentValues cv = new ContentValues();
        cv.put(databaseActivity.ProductReceipt.COLUMN_NAME_PRODUCT_ID, productID);
        cv.put(databaseActivity.ProductReceipt.COLUMN_NAME_RECEIPT_ID, receiptID);
        return db.insert(databaseActivity.ProductReceipt.TABLE_NAME, null, cv);
    }

    public Product selectProductByID(SQLiteDatabase db, int id){
        Cursor cs = db.rawQuery("SELECT * FROM " + databaseActivity.Products.TABLE_NAME + " WHERE ? = ?",
                new String[]{databaseActivity.Products.COLUMN_NAME_ID, id + ""}, null);
        cs.moveToNext();
        Product product = new Product(cs.getInt(0),
                cs.getString(1),
                cs.getString(2),
                cs.getLong(3),
                cs.getDouble(4),
                cs.getInt(5) == 0);
        return product;
    }

    public Receipt selectReceiptByID(SQLiteDatabase db, int id){
        Cursor cs = db.rawQuery("SELECT * FROM " + databaseActivity.ProductReceipt.TABLE_NAME + " WHERE ? = ?",
                new String[]{databaseActivity.ProductReceipt.COLUMN_NAME_RECEIPT_ID, id + ""}, null);
        cs.moveToNext();
        List<Product> products = new ArrayList<>();
        do{
            products.add(selectProductByID(db, cs.getInt(0)));
        }while(!cs.isLast());

        cs = db.rawQuery("SELECT * FROM " + databaseActivity.Receipts.TABLE_NAME + " WHERE ? = ?",
                new String[]{databaseActivity.Receipts.COLUMN_NAME_ID, id + ""}, null);
        cs.moveToNext();
        Receipt receipt = new Receipt(cs.getInt(0),
                products, cs.getString(1),
                cs.getDouble(2), cs.getString(3));
        return receipt;
    }

    private static final String SQL_CREATE_PRODUCTS = "CREATE TABLE " +
            databaseActivity.Products.TABLE_NAME + " (" +
            databaseActivity.Products.COLUMN_NAME_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            databaseActivity.Products.COLUMN_NAME_PNAME + " TEXT, " +
            databaseActivity.Products.COLUMN_NAME_DESCRIPTION + " TEXT, " +
            databaseActivity.Products.COLUMN_NAME_BARCODE + " BIGINT, " +
            databaseActivity.Products.COLUMN_NAME_PRICE + " REAL, " +
            databaseActivity.Products.COLUMN_NAME_GLUTEN + " INTEGER)";

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

    private static final String SQL_CREATE_PRODUCT_RECEIPT = "CREATE TABLE " +
            databaseActivity.ProductReceipt.TABLE_NAME + " (" +
            databaseActivity.ProductReceipt.COLUMN_NAME_PRODUCT_ID + " INTEGER, " +
            databaseActivity.ProductReceipt.COLUMN_NAME_RECEIPT_ID + " INTEGER, " +
            "CONSTRAINT " + "fk_" + databaseActivity.ProductReceipt.TABLE_NAME +
            databaseActivity.Products.TABLE_NAME + " FOREIGN KEY (" +
            databaseActivity.ProductReceipt.COLUMN_NAME_PRODUCT_ID + ") REFERENCES " +
            databaseActivity.Products.TABLE_NAME + "(" +
            databaseActivity.Products.COLUMN_NAME_ID + "), " +
            "CONSTRAINT " + "fk_" + databaseActivity.ProductReceipt.TABLE_NAME +
            databaseActivity.Receipts.TABLE_NAME + " FOREIGN KEY (" +
            databaseActivity.ProductReceipt.COLUMN_NAME_RECEIPT_ID + ") REFERENCES " +
            databaseActivity.Receipts.TABLE_NAME + "(" +
            databaseActivity.Receipts.COLUMN_NAME_ID + "))";

    private static final String SQL_DELETE_PRODUCT_RECEIPT = "DROP TABLE IF EXISTS " +
            databaseActivity.ProductReceipt.TABLE_NAME;
}
