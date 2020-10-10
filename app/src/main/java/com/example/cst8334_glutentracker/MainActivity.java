package com.example.cst8334_glutentracker;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;


//version feng

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button toCart = findViewById(R.id.toCart);
        toCart.setOnClickListener((v) ->{startActivity(new Intent(MainActivity.this, cartActivity.class));});
    }
}