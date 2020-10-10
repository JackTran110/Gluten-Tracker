package com.example.cst8334_glutentracker;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import java.util.ArrayList;

public class ReceiptActivity extends AppCompatActivity {
    ArrayList<ReceiptSummary> receipt;
    ListView receiptList;
    private static ReceiptAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_receipt);
        receiptList=(ListView)findViewById(R.id.receiptList);
        receipt= new ArrayList<>();
        receipt.add(new ReceiptSummary(BitmapFactory.decodeResource(getApplicationContext().getResources(),
                R.drawable.image),57.78,"10/8/2020"));
        adapter=new ReceiptAdapter(receipt,this);
        receiptList.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

    public class ReceiptSummary{
        private Bitmap image;
        private double amount;
        private String date;

        public ReceiptSummary(Bitmap image,double amount, String date){
            this.image=image;
            this.amount=amount;
            this.date=date;
        }

        public Bitmap getImage(){
            return image;
        }

        public double getAmount(){
            return amount;
        }

        public String getDate(){
            return date;
        }


    }

    public class ReceiptAdapter extends ArrayAdapter<ReceiptSummary> {
        private ArrayList<ReceiptSummary> rData;

        Context mContext;
        ImageView img;
        TextView amt;
        TextView dte;



        public ReceiptAdapter(ArrayList<ReceiptSummary> data, Context context)  {
            super(context,R.layout.receipt_layout,data);
            this.rData=data;
            this.mContext=context;
        }


        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            ReceiptSummary rS = getItem(position);

                LayoutInflater inflater = LayoutInflater.from(mContext);
                convertView = inflater.inflate(R.layout.receipt_layout, parent, false);
                img = (ImageView) convertView.findViewById(R.id.rpt);
                amt = (TextView) convertView.findViewById(R.id.deduction);
                dte = (TextView) convertView.findViewById(R.id.summarydate);

                img.setImageBitmap(rS.getImage());
                amt.setText(Double.toString(rS.getAmount()));
                dte.setText(rS.getDate());

            return convertView;
        }
    }
    



}