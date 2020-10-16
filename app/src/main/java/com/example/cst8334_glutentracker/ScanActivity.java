package com.example.cst8334_glutentracker;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.SurfaceView;
import android.widget.Button;

public class ScanActivity extends AppCompatActivity {

    Button okScanButton = (Button) findViewById(R.id.okEditTextButton);
    Button cancelScanButton = (Button) findViewById(R.id.cancelEditTextButton);
    SurfaceView cameraView = (SurfaceView) findViewById(R.id.scanSurfaceView);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan);
    }
}