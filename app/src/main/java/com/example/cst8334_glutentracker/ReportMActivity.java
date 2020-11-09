package com.example.cst8334_glutentracker;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;

public class ReportMActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {

     ListView lstMReport;
     ArrayList<String> arrayList;
     Button btnReport;
     Button btnCsvFile;

     AlertDialog.Builder dialogBuilder;
     AlertDialog dialog;
     TextView txtA;
     TextView txtFrom;
     TextView txtTo;

     String pickDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report_m);

        btnReport = (Button)findViewById(R.id.btnReceipt);
        btnCsvFile = (Button)findViewById(R.id.btnCsvFile);

        txtFrom =(TextView)findViewById(R.id.txtFrom_data);
        txtTo = (TextView)findViewById(R.id.txtTo_data);

        btnReport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ReportMActivity.this,ReportActivity.class));
            }
        });

        btnCsvFile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                outputCsvFile();
            }
        });

        txtFrom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDailog();
                txtFrom.setText(pickDate);

            }
        });

        txtTo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDailog();
                txtTo.setText(pickDate);
            }
        });




        //    receipts.receiptID
        //    receipts.receiptFile
        //    receipts.totalTaxDeduction
        //    receipts.date

        arrayList = new ArrayList<>();
        arrayList.add("DATE        TOTAL                  TAX     ");
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

//
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

    private void showDatePickerDailog(){
        DatePickerDialog datePickerDialog = new DatePickerDialog(
                this,
                this,
                Calendar.getInstance().get(Calendar.YEAR),
                Calendar.getInstance().get(Calendar.MONTH),
                Calendar.getInstance().get(Calendar.DAY_OF_MONTH)
        );
        datePickerDialog.show();
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        pickDate = month + "/" + dayOfMonth + "/" + year;


    }

    public void createNewDiaglog(){
        dialogBuilder = new AlertDialog.Builder(this);
        final View rptView = getLayoutInflater().inflate(R.layout.popup, null);

        txtA = (TextView) rptView.findViewById(R.id.txtA);

        dialogBuilder.setView(rptView);
        dialog = dialogBuilder.create();
        dialog.show();

    }
    //#34
    public void outputCsvFile()  {
        //generate data
        StringBuilder data = new StringBuilder();
        data.append("Date,Total,Tax");

        data.append("\n" + "2020-5" + "," + "$125.50" + "," + "8.10");
        data.append("\n" + "2020-6" + "," + "$625.50" + "," + "12.51");
        data.append("\n" + "2020-7" + "," + "$30.40" + "," + "7.12");
        data.append("\n" + "2020-8" + "," + "$88.14" + "," + "12.78");
        data.append("\n" + "2020-10" + "," + "$56.90" + "," + "6.23");
        data.append("\n" + "2020-12" + "," + "$15.00" + "," + "12.50");



        // saving the file into device
        try {
            FileOutputStream out = openFileOutput("Report.csv", Context.MODE_PRIVATE);
            out.write((data.toString().getBytes()));
            out.close();

            //exporting
            Context context = getApplicationContext();
            File fileLocation = new File(getFilesDir(),"Report.csv");
            Uri path = FileProvider.getUriForFile(context,"com.example.cst8334_glutentracker.fileprovider",fileLocation);
            Intent fileIntent = new Intent(Intent.ACTION_SEND);
            fileIntent.setType("text/csv");
            fileIntent.putExtra(Intent.EXTRA_SUBJECT, "Receipt Report");
            fileIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            fileIntent.putExtra(Intent.EXTRA_STREAM,path);
            startActivity(Intent.createChooser(fileIntent,"Send mail"));


        } catch (Exception e){
            e.printStackTrace();
        }
    }



}