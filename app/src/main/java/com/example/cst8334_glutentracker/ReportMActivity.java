package com.example.cst8334_glutentracker;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class ReportMActivity extends AppCompatActivity {

     ListView lstMReport;
     ArrayList<String> arrayList;
     Button btnReport;

     AlertDialog.Builder dialogBuilder;
     AlertDialog dialog;
     TextView txtA, txtB,txtC, txtD, txtE;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report_m);

        btnReport = (Button)findViewById(R.id.btnReceipt);
        btnReport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ReportMActivity.this,ReportActivity.class));
            }
        });

        arrayList = new ArrayList<>();
        arrayList.add("2019-Jan   Total:$230.50          dTax:$23.80");
        arrayList.add("2019-Feb   Total:$230.50          dTax:$23.80");
        arrayList.add("2019-Mar   Total:$230.50          dTax:$23.80");
        arrayList.add("2019-Apr   Total:$230.50         dTax:$23.80");
        arrayList.add("2019-May   Total:$230.50         dTax:$23.80");
        arrayList.add("2019-Jun   Total:$230.50          dTax:$23.80");
        arrayList.add("2019-Jul   Total:$230.50          dTax:$23.80");
        arrayList.add("2019-Aug   Total:$230.50          dTax:$23.80");
        arrayList.add("2019-Sept   Total:$230.50         dTax:$23.80");
        arrayList.add("2019-Oct   Total:$230.50         dTax:$23.80");
        arrayList.add("2019-Nov   Total:$230.50         dTax:$23.80");
        arrayList.add("2019-Dec   Total:$230.50          dTax:$23.80");
        arrayList.add("2020-Jan   Total:$230.50          dTax:$23.80");
        arrayList.add("2020-Feb   Total:$230.50          dTax:$23.80");
        arrayList.add("2020-Mar   Total:$230.50          dTax:$23.80");
        arrayList.add("2020-Apr   Total:$230.50         dTax:$23.80");
        arrayList.add("2020-May   Total:$230.50         dTax:$23.80");
        arrayList.add("2020-Jun   Total:$230.50          dTax:$23.80");
        arrayList.add("2020-Jul   Total:$230.50          dTax:$23.80");
        arrayList.add("2020-Aug   Total:$230.50          dTax:$23.80");
        arrayList.add("2020-Sept   Total:$230.50         dTax:$23.80");
        arrayList.add("2020-Oct   Total:$230.50         dTax:$23.80");
        arrayList.add("2020-Nov   Total:$230.50         dTax:$23.80");
        arrayList.add("2020-Dec   Total:$230.50          dTax:$23.80");


          lstMReport = (ListView) findViewById(R.id.lstMReport);

          ArrayAdapter adapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,arrayList);
          lstMReport.setAdapter(adapter);

          lstMReport.setOnItemClickListener(new AdapterView.OnItemClickListener() {
              @Override
              public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                  Toast.makeText(ReportMActivity.this,arrayList.get(position),Toast.LENGTH_LONG).show();
                  createNewDiaglog();



              }
          });

    }

    public void createNewDiaglog(){
        dialogBuilder = new AlertDialog.Builder(this);
        final View rptView = getLayoutInflater().inflate(R.layout.popup, null);

        txtA = (TextView) rptView.findViewById(R.id.txtA);

        dialogBuilder.setView(rptView);
        dialog = dialogBuilder.create();
        dialog.show();



    }


}