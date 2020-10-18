package com.example.cst8334_glutentracker;

import androidx.appcompat.app.AppCompatActivity;

import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.SurfaceView;
import android.widget.Button;
import android.widget.EditText;

import org.json.JSONObject;

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

        acceptScannerButton = (Button) findViewById(R.id.acceptEditTextButton);
        cancelScannerButton = (Button) findViewById(R.id.cancelEditTextButton);
        dbOpener = new glutenDbHelper(this);
        db = dbOpener.getWritableDatabase();
        cameraView = (SurfaceView) findViewById(R.id.scanSurfaceView);

        if (acceptScannerButton != null) {
            acceptScannerButton.setOnClickListener(acceptClick -> {
                if (getUPCEditText().toString().trim().length() > 0) {
                    this.runQuery(getUPCEditText(), "result");
                }
            });
        }
    }

    // Run the API query to Edamam
    private void runQuery(Integer upc, String response) {
        EdamamQuery eq = new EdamamQuery(response, upc);
        eq.execute();
    }

    // Get Edit text field
    private Integer getUPCEditText() {
        return Integer.parseInt(upcBarcode.getText().toString());
    }

}