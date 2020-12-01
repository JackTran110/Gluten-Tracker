/**
 * Copyright (c) 2017 Team Novus
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package com.example.cst8334_glutentracker;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

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
    boolean cameraPermission;
    int CAMERA_PERMISSION_CODE;
    // Adding six second delay between scans, https://github.com/journeyapps/zxing-android-embedded/issues/59
    static final int DELAY = 6000;
    long delayTimeStamp = 0;

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


        /**
         * Logic to request camera permissions from the user if not already granted.
         */
        if (checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            cameraPermission = false;
            ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.CAMERA}, CAMERA_PERMISSION_CODE);
        } else {
            cameraPermission = true;
        }

        scannerView = (CodeScannerView) findViewById(R.id.barcodeScanner);
        codeScanner = new CodeScanner(this, scannerView);
        codeScanner.getCamera();

        /**
         * Set the camera to continuously scan for barcodes
         */
        codeScanner.setScanMode(ScanMode.CONTINUOUS);

        /**
         * Enable all barcode formats to be suitable for API queries to Edamam
         */
        codeScanner.setFormats(CodeScanner.ALL_FORMATS);

        /**
         * Code example taken from the following github repository:
         * @link https://github.com/yuriy-budiyev/code-scanner
         *
         * Enables the camera to constantly refresh and wait until a barcode image is found.
         *
         */
        codeScanner.setDecodeCallback(new DecodeCallback() {
            @Override
            public void onDecoded(@NonNull final Result result) {
                ScanActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        /**
                         * Logic to delay the time between scans.
                         */
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

        /**
         * Logic for the accept button, pass the barcode from the EditText field and
         * CheckBox if pressed.
         */
        if (acceptScannerButton != null) {
            acceptScannerButton.setOnClickListener(acceptClick -> {
                if (upcBarcode.getText().toString().length() > 0) {
                    this.runQuery(getUPCEditText(), checkBox.isChecked());
                }
            });
        }

        /**
         * Logic for the cancel button, return to previous activity if pressed.
         */
        if (cancelScannerButton != null) {
            cancelScannerButton.setOnClickListener(cancelClick -> {
                finish();
            });
        }

        /**
         * Logic to enable/disable the flashlight
         */
        if (codeScanner.isFlashEnabled()) {
            codeScanner.setFlashEnabled(true);
        } else {
            codeScanner.setFlashEnabled(false);
        }
    }

    /**
     Performs verification checks and performs the necessary actions:
        1. If the item is in the cart, do not perform database or API query to Edamam.
        2. If the item is not in the cart but it exists in the database, perform the following:
            A) Retrieve the item from the database
            B) Ask the user if they want to add the item to the cart.
        3. If the item does not exist in the cart or database, perform an API query to Edamam and
            retrieve the item if it exists.

         @param upc - Barcode value retrieved from Edamam or the database
         @param isGluten - parameter passed from the Check Box to declare if the item is gluten free or not

     */
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


        /**
         * Logic to confirm if the item has already been added to the cart. If it has,
         * simply display a message to the user.
         */
        if (CartActivity.getProductsArrayList().size() != 0){
            for (Product prod : CartActivity.getProductsArrayList()) {
                if (prod.getId() == upc) {
                    boolCartItem = true;
                    Toast.makeText(ScanActivity.this, "Item already exists in the cart", Toast.LENGTH_LONG).show();
                }
            }
        }

        /**
         * Logic that occurs if the item doesn't exist in the cart, database query is performed
         * and adds the item to the cart.
         *
         * Also verifies if the item retrieved is gluten and will
         * ask the user if they want to also add this to the cart (they are not by default).
         *
         */
        if (barcodeCheck != null && boolCartItem == false) {
            if (!barcodeCheck.isGlutenFree()){
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

    /**
     *     Retrieve Edit text field
      */
    private long getUPCEditText() {
        return Long.valueOf(upcBarcode.getText().toString());
    }

    /**
     * Overwrite onResume method, used to display camera only if camera permissions
     * were accepted.
     */
    @Override
    protected void onResume() {
        super.onResume();
        if (cameraPermission){
            codeScanner.startPreview();
        }
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

    /**
     * Overwritten method used to enable/disable camera based on permissions selected
     * by the user.
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            cameraPermission = true;
            codeScanner.startPreview();
        } else {
            cameraPermission = false;
        }
    }
}