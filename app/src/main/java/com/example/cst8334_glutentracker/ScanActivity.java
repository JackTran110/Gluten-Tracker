package com.example.cst8334_glutentracker;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;

import com.budiyev.android.codescanner.CodeScanner;
import com.budiyev.android.codescanner.CodeScannerView;
import com.budiyev.android.codescanner.DecodeCallback;
import com.budiyev.android.codescanner.ScanMode;
import com.example.entity.Product;
import com.google.zxing.Result;

import static java.lang.Long.parseLong;

public class ScanActivity extends AppCompatActivity {
    String apiReturnMessage;
    int CAMERA_PERMISSION_CODE;
    Button acceptScannerButton;
    Button cancelScannerButton;
    EditText upcBarcode;
    CheckBox checkBox;
    GlutenDatabase dbOpener;
    SQLiteDatabase db;
    CodeScanner codeScanner;
    CodeScannerView scannerView;
    Toolbar scannerTbar;
    AlertDialog.Builder alertDialog;

    // Adding six second delay between scans, https://github.com/journeyapps/zxing-android-embedded/issues/59
    static final int DELAY = 6000;
    long delayTimeStamp = 0;

    String test;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan);

        scannerTbar = (Toolbar)findViewById(R.id.scannerToolbar);
        checkBox = (CheckBox) findViewById(R.id.glutenCheckBox);
        upcBarcode = (EditText) findViewById(R.id.barcodeEditText);
        acceptScannerButton = (Button) findViewById(R.id.acceptScannerButton);
        cancelScannerButton = (Button) findViewById(R.id.cancelScannerButton);
        dbOpener = new GlutenDatabase(this);
        db = dbOpener.getWritableDatabase();

        if (ContextCompat.checkSelfPermission(ScanActivity.this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(ScanActivity.this, new String[] {Manifest.permission.CAMERA}, CAMERA_PERMISSION_CODE);
        }
        scannerView = (CodeScannerView) findViewById(R.id.barcodeScanner);
        codeScanner = new CodeScanner(this, scannerView);
        codeScanner.getCamera();
        codeScanner.setScanMode(ScanMode.CONTINUOUS);
        codeScanner.setFormats(CodeScanner.ALL_FORMATS);

        codeScanner.setDecodeCallback(new DecodeCallback() {
            @Override
            public void onDecoded(@NonNull final Result result) {
                ScanActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (System.currentTimeMillis() - delayTimeStamp < DELAY){
                            return;
                        } else {
                            ScanActivity.this.runQuery(parseLong(result.getText()), checkBox.isChecked());
                            delayTimeStamp = System.currentTimeMillis();
                        }
                    }
                });
            }
        });

        if (acceptScannerButton != null) {
            acceptScannerButton.setOnClickListener(acceptClick -> {
                if (upcBarcode.getText().toString().length() > 0) {
                    this.runQuery(getUPCEditText(), checkBox.isChecked());
                }
            });
        }

        if (cancelScannerButton != null) {
            cancelScannerButton.setOnClickListener(cancelClick -> {
                finish();
            });
        }

        if (codeScanner.isFlashEnabled()) {
            codeScanner.setFlashEnabled(true);
        } else {
            codeScanner.setFlashEnabled(false);
        }

//        checkBox.setOnCheckedChangeListener( (buttonView, isChecked) -> {
//            if (checkBox.isChecked())
//        });
    }

    // Run the API query to Edamam
    private void runQuery(long upc, boolean isGluten) {
        boolean boolCartItem = false;
        db = dbOpener.getReadableDatabase();
        Product barcodeCheck = dbOpener.selectProductByID(upc);

        DialogInterface.OnClickListener dialogInterfaceListener = (dialog, which) -> {
            switch (which) {
                case DialogInterface.BUTTON_POSITIVE:
                    CartActivity.getProductsArrayList().add(barcodeCheck);
                    Toast.makeText(this, barcodeCheck.getProductName() + " added to the cart from database", Toast.LENGTH_LONG).show();
                    break;
                case DialogInterface.BUTTON_NEGATIVE:
                    Toast.makeText(this, barcodeCheck.getProductName() + " was not added to the cart", Toast.LENGTH_LONG).show();
                    break;
            }
        };

        // if iterator found in array, Toast.maketext (Scanactivity.this, "message", Toast.LENGTH_LONG).show();
        if (CartActivity.getProductsArrayList().size() != 0){
            for (Product prod : CartActivity.getProductsArrayList()) {
                if (prod.getId() == upc) {
                    boolCartItem = true;
                    Toast.makeText(ScanActivity.this, "Item already exists in the cart", Toast.LENGTH_LONG).show();
                }
            }
        }

        if (barcodeCheck != null && boolCartItem == false) {
            if (barcodeCheck.isGlutenFree()){
                alertDialog = new AlertDialog.Builder(this).setTitle("Add gluten item to cart?")
                        .setMessage("Would you like to add this gluten item to the cart?")
                        .setPositiveButton("Yes", dialogInterfaceListener).setNegativeButton("No", dialogInterfaceListener);;
                alertDialog.create().show();
            } else {
                CartActivity.getProductsArrayList().add(barcodeCheck);
                Toast.makeText(this, barcodeCheck.getProductName() + " added to the cart from database", Toast.LENGTH_LONG).show();
            }
        } else if (barcodeCheck == null && boolCartItem == false) {
            new EdamamQuery(ScanActivity.this, upc, isGluten).execute();
        }
    }

    // Get Edit text field
    private long getUPCEditText() {
        return Long.valueOf(upcBarcode.getText().toString());
    }

    @Override
    protected void onResume() {
        super.onResume();
        codeScanner.startPreview();
    }

    @Override
    protected void onPause() {
        codeScanner.releaseResources();
        super.onPause();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.toolbar, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.scannerButton:
                Intent goToScanner = new Intent(ScanActivity.this, ScanActivity.class);
                startActivity(goToScanner);
                break;
            case R.id.cartButton:
                Intent goToCart = new Intent(ScanActivity.this, CartActivity.class);
                startActivity(goToCart);
                break;
            case R.id.receiptButton:
                Intent goToReceipt = new Intent(ScanActivity.this, ReceiptActivity.class);
                startActivity(goToReceipt);
                break;
            case R.id.reportButton:
                Intent goToReport = new Intent(ScanActivity.this, ReportActivity.class);
                startActivity(goToReport);
                break;
        }
        return true;
    }
}