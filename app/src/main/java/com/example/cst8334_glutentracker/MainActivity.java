package com.example.cst8334_glutentracker;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.Button;


//version feng

public class MainActivity extends AppCompatActivity {
    int CAMERA_PERMISSION_CODE;

   /* private SQLiteDatabase database;
    private glutenDbHelper dbOpener = new glutenDbHelper(this); */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //insertTestValuesIntoDatabase();

        Button toCart = findViewById(R.id.toCart);
        toCart.setOnClickListener((v) ->{startActivity(new Intent(MainActivity.this, CartActivity.class));});

        Button toReceipt = findViewById(R.id.toReceiptPage);
        toReceipt.setOnClickListener((v) -> {startActivity(new Intent(MainActivity.this, ReceiptActivity.class));});

        Button toReport = findViewById(R.id.toReport);
        toReport.setOnClickListener((v) -> {startActivity(new Intent(MainActivity.this, ReportActivity.class));});

        Button toScanner = findViewById(R.id.toScannerPage);
        toScanner.setOnClickListener((v) -> {startActivity(new Intent(MainActivity.this, ScanActivity.class));});
   }

   /* // this is a method to test the database
    private void insertTestValuesIntoDatabase(){
        database = dbOpener.getWritableDatabase();
        ContentValues newRowValues = new ContentValues();
        newRowValues.put(databaseActivity.Receipts.COLUMN_NAME_FILE, "test1");
        newRowValues.putNull(databaseActivity.Receipts.COLUMN_NAME_DEDUCTION);
        newRowValues.putNull(databaseActivity.Receipts.COLUMN_NAME_DEDUCTION);
        database.insert(databaseActivity.Receipts.TABLE_NAME, null, newRowValues);

        ContentValues productRowValues = new ContentValues();
        productRowValues.put(databaseActivity.Products.COLUMN_NAME_PNAME, "Cookie");
        productRowValues.put(databaseActivity.Products.COLUMN_NAME_DESCRIPTION, "A cookie");
        productRowValues.put(databaseActivity.Products.COLUMN_NAME_PRICE, 1.00);
        productRowValues.put(databaseActivity.Products.COLUMN_NAME_GLUTEN, 0);
        productRowValues.put(databaseActivity.Products.COLUMN_NAME_RID, 1);
        database.insert(databaseActivity.Products.TABLE_NAME, null, productRowValues);

    }*/
}
