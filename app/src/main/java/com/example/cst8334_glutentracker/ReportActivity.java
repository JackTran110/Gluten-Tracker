package com.example.cst8334_glutentracker;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;

public class ReportActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report);

        String[] strLst = {"Rec.Id      Date     Item       SubTotal      Tax Deduction","No.563  Jul.10   Oatmeal         $56.23        $1.56","No.003  Oct.04    Pasta          $34.23        $0.86","No.897  Nov.12   Cereals         $126.50        $5.06"};
        ListAdapter theAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,strLst);
        ListView theListView = (ListView) findViewById(R.id.rptlst);
        theListView.setAdapter(theAdapter);

    }
}