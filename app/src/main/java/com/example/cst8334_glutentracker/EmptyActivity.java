package com.example.cst8334_glutentracker;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

public class EmptyActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_empty);
        Bundle dataToPass = getIntent().getExtras();
        ProductLinkFragment fragment = new ProductLinkFragment();
        fragment.setArguments(dataToPass);
        getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout, fragment).commit();
    }
}