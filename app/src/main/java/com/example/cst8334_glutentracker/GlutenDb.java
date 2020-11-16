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

public class GlutenDb extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "GlutenTracker.db";
    public static final int DATABASE_VERSION = 2;
    private SQLiteDatabase db;

    public GlutenDb(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_PRODUCTS);
        db.execSQL(SQL_CREATE_RECEIPTS);
        db.execSQL(SQL_CREATE_PRODUCT_RECEIPT);
        this. db = db;
    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(SQL_DELETE_PRODUCTS);
        db.execSQL(SQL_DELETE_RECEIPTS);
        db.execSQL(SQL_DELETE_PRODUCT_RECEIPT);
        onCreate(db);
    }

    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(SQL_DELETE_PRODUCTS);
        db.execSQL(SQL_DELETE_RECEIPTS);
        db.execSQL(SQL_DELETE_PRODUCT_RECEIPT);
        onCreate(db);    }

    public long insertIntoProductsTable(Product product){
        ContentValues cv = new ContentValues();
        //Added by Joel
        cv.put(DatabaseActivity.Products.COLUMN_NAME_ID, product.getId());
        cv.put(DatabaseActivity.Products.COLUMN_NAME_PRODUCT_NAME, product.getProductName());
        cv.put(DatabaseActivity.Products.COLUMN_NAME_DESCRIPTION, product.getProductDescription());
        cv.put(DatabaseActivity.Products.COLUMN_NAME_PRICE, product.getPrice());
        cv.put(DatabaseActivity.Products.COLUMN_NAME_GLUTEN, (product.isGlutenFree() ? 0:1));
        return db.insert(DatabaseActivity.Products.TABLE_NAME, null, cv);
    }

    public long insertIntoReceiptsTable(List<Product> products, String file, double deduction, double totalPrice, String date){
        ContentValues cv = new ContentValues();

        cv.put(DatabaseActivity.Receipts.COLUMN_NAME_FILE, file);
        cv.put(DatabaseActivity.Receipts.COLUMN_NAME_TOTAL_DEDUCTION, deduction);
        cv.put(DatabaseActivity.Receipts.COLUMN_NAME_TOTAL_PRICE, totalPrice);
        cv.put(DatabaseActivity.Receipts.COLUMN_NAME_DATE, date);
        long id = db.insert(DatabaseActivity.Receipts.TABLE_NAME, null, cv);

        for(Product product: products){
            insertIntoProductReceiptTable(product, id, product.getLinkedProduct());
        }
        return id;
    }

    private long insertIntoProductReceiptTable(Product product, long receiptID, Product linkedProduct){
        ContentValues cv = new ContentValues();
        cv.put(DatabaseActivity.ProductReceipt.COLUMN_NAME_PRODUCT_ID, product.getId());
        cv.put(DatabaseActivity.ProductReceipt.COLUMN_NAME_RECEIPT_ID, receiptID);
        cv.put(DatabaseActivity.ProductReceipt.COLUMN_NAME_PRICE, product.getPrice());
        cv.put(DatabaseActivity.ProductReceipt.COLUMN_NAME_QUANTITY, product.getQuantity());
        if(linkedProduct != null){
            cv.put(DatabaseActivity.ProductReceipt.COLUMN_NAME_LINKED_PRODUCT_ID, linkedProduct.getId());
            cv.put(DatabaseActivity.ProductReceipt.COLUMN_NAME_LINKED_PRODUCT_PRICE, linkedProduct.getPrice());
            cv.put(DatabaseActivity.ProductReceipt.COLUMN_NAME_DEDUCTION, product.getPrice()-linkedProduct.getPrice());
        }
        return db.insert(DatabaseActivity.ProductReceipt.TABLE_NAME, null, cv);
    }

    public Product selectProductByID(long id){
        Cursor cs = db.rawQuery("SELECT * FROM " + DatabaseActivity.Products.TABLE_NAME + " WHERE ? = ?",
                new String[]{DatabaseActivity.Products.COLUMN_NAME_ID, id + ""}, null);
        cs.moveToNext();
        Product product = new Product(cs.getLong(0),
                cs.getString(1),
                cs.getString(2),
                cs.getDouble(3),
                cs.getInt(4) == 0);
        return product;
    }

    public Receipt selectReceiptByID(long id){
        Cursor cs = db.rawQuery("SELECT * FROM " + DatabaseActivity.ProductReceipt.TABLE_NAME + " WHERE ? = ?",
                new String[]{DatabaseActivity.ProductReceipt.COLUMN_NAME_RECEIPT_ID, id + ""}, null);
        cs.moveToNext();
        List<Product> products = new ArrayList<>();
        do{
            Product newProduct = selectProductByID(cs.getLong(0));
            newProduct.setPrice(cs.getDouble(2));
            newProduct.setQuantity(cs.getInt(3));
            try{
                Product linkedProduct = selectProductByID(cs.getLong(5));
                linkedProduct.setPrice(cs.getDouble(6));
                linkedProduct.setQuantity(cs.getInt(3));
                newProduct.setLinkedProduct(linkedProduct);
            }catch (Exception e){}
            products.add(newProduct);
        }while(!cs.isLast());

        cs = db.rawQuery("SELECT * FROM " + DatabaseActivity.Receipts.TABLE_NAME + " WHERE ? = ?",
                new String[]{DatabaseActivity.Receipts.COLUMN_NAME_ID, id + ""}, null);
        cs.moveToNext();
        Receipt receipt = new Receipt(cs.getLong(0),
                products, cs.getString(1),
                cs.getDouble(3),
                cs.getDouble(2),
                cs.getString(4));
        return receipt;
    }

    public int deleteReceiptByID(long id){
        db.delete(DatabaseActivity.ProductReceipt.TABLE_NAME,
                DatabaseActivity.ProductReceipt.COLUMN_NAME_RECEIPT_ID + " = ?",
                new String[]{id + ""});
        return db.delete(DatabaseActivity.Receipts.TABLE_NAME,
                DatabaseActivity.Receipts.COLUMN_NAME_ID + " + ?",
                new String[]{id + ""});
    }

    public int deleteProductByID(SQLiteDatabase db, long id){
        return db.delete(DatabaseActivity.Products.TABLE_NAME,
                DatabaseActivity.Products.COLUMN_NAME_ID + "= ?",
                new String[]{id + ""});
    }

//   private static final String SQL_CREATE_PRODUCTS = "CREATE TABLE " +
//            databaseActivity.Products.TABLE_NAME + " (" +
//            databaseActivity.Products.COLUMN_NAME_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
//            databaseActivity.Products.COLUMN_NAME_PNAME + " TEXT, " +
//            databaseActivity.Products.COLUMN_NAME_DESCRIPTION + " TEXT, " +
//            databaseActivity.Products.COLUMN_NAME_BARCODE + " BIGINT, " +
//            databaseActivity.Products.COLUMN_NAME_PRICE + " REAL, " +
//            databaseActivity.Products.COLUMN_NAME_GLUTEN + " INTEGER)";

    private static final String SQL_CREATE_PRODUCTS = "CREATE TABLE " +
            DatabaseActivity.Products.TABLE_NAME + " (" +
            DatabaseActivity.Products.COLUMN_NAME_ID + " BIGINT PRIMARY KEY, " +
            DatabaseActivity.Products.COLUMN_NAME_PRODUCT_NAME + " TEXT, " +
            DatabaseActivity.Products.COLUMN_NAME_DESCRIPTION + " TEXT, " +
            DatabaseActivity.Products.COLUMN_NAME_PRICE + " REAL, " +
            DatabaseActivity.Products.COLUMN_NAME_GLUTEN + " INTEGER)";


    public void updateProductById(SQLiteDatabase db,long id, double price){
        ContentValues cv= new ContentValues();
        cv.put(DatabaseActivity.Products.COLUMN_NAME_PRICE,price);
        db.update(DatabaseActivity.Products.TABLE_NAME,cv, DatabaseActivity.Products.COLUMN_NAME_ID+" = ? ",new String[]{Long.toString(id)});

    }


    private static final String SQL_DELETE_PRODUCTS = "DROP TABLE IF EXISTS " +
            DatabaseActivity.Products.TABLE_NAME;

    private static final String SQL_CREATE_RECEIPTS = "CREATE TABLE " +
            DatabaseActivity.Receipts.TABLE_NAME + " (" +
            DatabaseActivity.Receipts.COLUMN_NAME_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            DatabaseActivity.Receipts.COLUMN_NAME_FILE + " TEXT, " +
            DatabaseActivity.Receipts.COLUMN_NAME_TOTAL_PRICE + " REAL, " +
            DatabaseActivity.Receipts.COLUMN_NAME_TOTAL_DEDUCTION + " REAL, " +
            DatabaseActivity.Receipts.COLUMN_NAME_DATE + " TEXT)";

    private static final String SQL_DELETE_RECEIPTS = "DROP TABLE IF EXISTS " +
            DatabaseActivity.Receipts.TABLE_NAME;

    private static final String SQL_CREATE_PRODUCT_RECEIPT = "CREATE TABLE " +
            DatabaseActivity.ProductReceipt.TABLE_NAME + " (" +
            DatabaseActivity.ProductReceipt.COLUMN_NAME_PRODUCT_ID + " BIGINT, " +
            DatabaseActivity.ProductReceipt.COLUMN_NAME_RECEIPT_ID + " INTEGER, " +
            DatabaseActivity.ProductReceipt.COLUMN_NAME_PRICE + " REAL, " +
            DatabaseActivity.ProductReceipt.COLUMN_NAME_QUANTITY + " INTERGER, " +
            DatabaseActivity.ProductReceipt.COLUMN_NAME_DEDUCTION + " REAL, " +
            DatabaseActivity.ProductReceipt.COLUMN_NAME_LINKED_PRODUCT_ID + " BIGINT, " +
            DatabaseActivity.ProductReceipt.COLUMN_NAME_LINKED_PRODUCT_PRICE + " REAL, " +
            "CONSTRAINT " + "fk_" + DatabaseActivity.ProductReceipt.TABLE_NAME +
            DatabaseActivity.Products.TABLE_NAME + " FOREIGN KEY (" +
            DatabaseActivity.ProductReceipt.COLUMN_NAME_PRODUCT_ID + ") REFERENCES " +
            DatabaseActivity.Products.TABLE_NAME + "(" +
            DatabaseActivity.Products.COLUMN_NAME_ID + "), " +
            "CONSTRAINT " + "fk_" + DatabaseActivity.ProductReceipt.TABLE_NAME + "linked" +
            DatabaseActivity.Products.TABLE_NAME + " FOREIGN KEY (" +
            DatabaseActivity.ProductReceipt.COLUMN_NAME_LINKED_PRODUCT_ID + ") REFERENCES " +
            DatabaseActivity.Products.TABLE_NAME + "(" +
            DatabaseActivity.Products.COLUMN_NAME_ID + "), " +
            "CONSTRAINT " + "fk_" + DatabaseActivity.ProductReceipt.TABLE_NAME +
            DatabaseActivity.Receipts.TABLE_NAME + " FOREIGN KEY (" +
            DatabaseActivity.ProductReceipt.COLUMN_NAME_RECEIPT_ID + ") REFERENCES " +
            DatabaseActivity.Receipts.TABLE_NAME + "(" +
            DatabaseActivity.Receipts.COLUMN_NAME_ID + "))";

    private static final String SQL_DELETE_PRODUCT_RECEIPT = "DROP TABLE IF EXISTS " +
            DatabaseActivity.ProductReceipt.TABLE_NAME;
}
