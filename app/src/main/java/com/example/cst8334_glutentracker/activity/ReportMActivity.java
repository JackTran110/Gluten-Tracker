package com.example.cst8334_glutentracker.activity;

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

import com.example.cst8334_glutentracker.R;
import com.example.cst8334_glutentracker.entity.ItemsModel;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Calendar;

public class ReportMActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {

     ListView lstMReport;
     ArrayList<String> arrayList;
     ArrayList<ItemsModel> arrayReceipt;
     StringBuilder data;
     Button btnReport;
     Button btnCsvFile;
     Button btnMonth;
     Button btnSearch;

     AlertDialog.Builder dialogBuilder;
     AlertDialog dialog;
     TextView txtA;
     TextView txtFrom;
     TextView txtTo;

     int dateFromD,dateFromM,dateFromY, dateToD,dateToM,dateToY;
     boolean isFrom = false;

     String pickDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report_m);

        lstMReport = (ListView) findViewById(R.id.lstMReport);

        btnReport = (Button)findViewById(R.id.btnReceipt);
        btnCsvFile = (Button)findViewById(R.id.btnCsvFile);

        btnMonth = (Button)findViewById(R.id.btnMonth);
        btnSearch = (Button)findViewById(R.id.btnSearch);

        txtFrom =(TextView)findViewById(R.id.txtFrom_data);
        txtTo = (TextView)findViewById(R.id.txtTo_data);

        arrayList = new ArrayList<>();
       // arrayList.add("2020-Oct   Total:$450.23         dTax:$67.25");
       // arrayList.add("2020-Nov   Total:$230.50         dTax:$56.98");

// 2020-8

        arrayReceipt = new ArrayList<>();
        arrayReceipt.add(new ItemsModel("90001", "2020-8-5",2020,8,5, "Purchease detail is Oatmeal", "23.50", "0.8"));
        arrayReceipt.add(new ItemsModel("90002", "2020-8-12",2020,8,12, "Purchease detail is Apple", "120.78", "23.45"));
        arrayReceipt.add(new ItemsModel("90003", "2020-8-15",2020,8,15, "Purchease detail is Orange", "45.60", "4.8"));
        arrayReceipt.add(new ItemsModel("90004", "2020-8-20",2020,8,20, "Purchease detail is rice", "29.50", "2.3"));

// 2020-9


        arrayReceipt.add(new ItemsModel("90005", "2020-9-2",2020,9,2, "Purchease detail is Oatmeal", "213.50", "1.8"));
        arrayReceipt.add(new ItemsModel("90006", "2020-9-10",2020,9,10, "Purchease detail is Apple", "20.75", "20.45"));
        arrayReceipt.add(new ItemsModel("90007", "2020-9-11",2020,9,11, "Purchease detail is Orange", "65.30", "14.8"));
        arrayReceipt.add(new ItemsModel("90008", "2020-9-22",2020,9,22, "Purchease detail is rice", "19.56", "21.3"));

// 2020-10


        arrayReceipt.add(new ItemsModel("90009", "2020-10-15",2020,10,15, "Purchease detail is Oatmeal", "56.50", "10.18"));
        arrayReceipt.add(new ItemsModel("90010", "2020-10-16",2020,10,16, "Purchease detail is Apple", "120.78", "7.45"));
        arrayReceipt.add(new ItemsModel("90011", "2020-10-19",2020,10,19, "Purchease detail is Orange", "45.60", "40.83"));
        arrayReceipt.add(new ItemsModel("90012", "2020-10-27",2020,10,27, "Purchease detail is rice", "29.50", "20.53"));
// 2020-11


        arrayReceipt.add(new ItemsModel("90013", "2020-11-2",2020,11,2, "Purchease detail is Oatmeal", "203.50", "0.98"));
        arrayReceipt.add(new ItemsModel("90014", "2020-11-10",2020,11,10, "Purchease detail is Apple", "20.78", "3.52"));
        arrayReceipt.add(new ItemsModel("90015", "2020-11-11",2020,11,11, "Purchease detail is Orange", "49.60", "14.38"));
        arrayReceipt.add(new ItemsModel("90016", "2020-11-17",2020,11,17, "Purchease detail is rice", "79.10", "21.36"));


        ArrayAdapter adapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,arrayList);
        lstMReport.setAdapter(adapter);

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

        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                arrayList.clear();
                arrayList.add("Receipt No.           Date                Total                Tax");
                data = new StringBuilder();
                data.append("ID,Date,Total,Tax");

                for (int i = 0; i < arrayReceipt.size(); i++){

                            int tmpday = arrayReceipt.get(i).getrMonth()*100 + arrayReceipt.get(i).getrDay();


                        if (tmpday >= (dateFromD-100) && tmpday <= dateToD) {

                                String str = arrayReceipt.get(i).getrId() + "         " + arrayReceipt.get(i).getrDate() + "           " + arrayReceipt.get(i).getrSub() + "          " + arrayReceipt.get(i).getrTax();
                                arrayList.add(str);
                                data.append("\n" + arrayReceipt.get(i).getrId() + "," + arrayReceipt.get(i).getrDate() + "," + arrayReceipt.get(i).getrSub() + "," + arrayReceipt.get(i).getrTax());

                        }

                }



                adapter.notifyDataSetChanged();
                Toast.makeText(ReportMActivity.this,"Report for Monthly",Toast.LENGTH_LONG).show();



            }
        });

        btnMonth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                arrayList.clear();
                arrayList.add("Month                    Total                    Tax");
                data = new StringBuilder();
                data.append("Date,Total,Tax");

                int tmpMonth = 0;
                double total = 0, tax = 0;
                String str = "";

                for (int i = 0; i < arrayReceipt.size(); i++){


                    if(tmpMonth == arrayReceipt.get(i).getrMonth()) {
                        total = total + Double.valueOf(arrayReceipt.get(i).getrSub());
                        tax = tax + Double.valueOf(arrayReceipt.get(i).getrTax());
                    }else{

                            str = Integer.toString(tmpMonth) + "                        " + Double.toString(total) + "                       " + Double.toString(tax);
                        if (tmpMonth != 0 ) {
                            arrayList.add(str);
                            data.append("\n" + Integer.toString(tmpMonth) + "," + Double.toString(total) + "," + Double.toString(tax));

                        }
                            tmpMonth = arrayReceipt.get(i).getrMonth();

                    }
                }


                adapter.notifyDataSetChanged();
                Toast.makeText(ReportMActivity.this,"Report for Customer Search",Toast.LENGTH_LONG).show();

            }
        });

        txtFrom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isFrom = true;
                showDatePickerDailog();


            }
        });

        txtTo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isFrom = false;
                showDatePickerDailog();



            }
        });


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
        pickDate = (month + 1) + "/" + dayOfMonth + "/" + year;

        if( isFrom == false ){

            txtTo.setText(pickDate);
            dateToY = year;
            dateToM = month + 1;
            dateToD = ( month + 1)*100 + dayOfMonth;

        }else{

            txtFrom.setText(pickDate);
            dateFromY = year;
            dateFromM = month  + 1;
            dateFromD = ( month + 1)*100 + dayOfMonth;
        }




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