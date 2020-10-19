package com.example.cst8334_glutentracker;

import androidx.appcompat.app.AppCompatActivity;

import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.SurfaceView;
import android.widget.Button;
import android.widget.EditText;

import org.json.JSONObject;

import static java.lang.Long.valueOf;

public class ScanActivity extends AppCompatActivity {

    Button acceptScannerButton;
    Button cancelScannerButton;
    EditText upcBarcode;
    glutenDbHelper dbOpener;
    JSONObject edamamObject;
    SQLiteDatabase db;
    SurfaceView cameraView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan);

        upcBarcode = (EditText) findViewById(R.id.barcodeEditText);
        acceptScannerButton = (Button) findViewById(R.id.acceptScannerButton);
        cancelScannerButton = (Button) findViewById(R.id.cancelScannerButton);
        dbOpener = new glutenDbHelper(this);
        db = dbOpener.getWritableDatabase();
        cameraView = (SurfaceView) findViewById(R.id.scanSurfaceView);

        if (acceptScannerButton != null) {
            acceptScannerButton.setOnClickListener(acceptClick -> {
                long test = Long.valueOf(upcBarcode.getText().toString());
//                Integer test2 = Integer.parseInt(test);
                if (upcBarcode.toString().trim().length() > 0) {
                    this.runQuery(getUPCEditText(), "acceptButton");
                }
            });
        }
    }

    // Run the API query to Edamam
    private void runQuery(long upc, String response) {
        EdamamQuery eq = new EdamamQuery(response, upc);
        eq.execute();
    }

    // Get Edit text field
    private long getUPCEditText() {
        return Long.valueOf(upcBarcode.getText().toString());
    }

}