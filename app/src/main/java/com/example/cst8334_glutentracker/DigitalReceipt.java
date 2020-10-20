package com.example.cst8334_glutentracker;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.entity.Product;

import java.util.ArrayList;

public class DigitalReceipt extends AppCompatActivity {
    private ProductAdapter adapter;
    private ArrayList<Product> products;
    private ListView productList;
    private SQLiteDatabase database;
    private glutenDbHelper dbOpener = new glutenDbHelper(this);
    TextView rrid;
    TextView rdate;
    TextView ded;
    Intent fromActivity;
    int passedIndex;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_digital_receipt);

        fromActivity=getIntent();
        passedIndex=fromActivity.getIntExtra("index",0);
        productList=findViewById(R.id.products);
        products=new ArrayList<>();
        rrid=findViewById(R.id.ridf);
        rdate=findViewById(R.id.datef);
        ded=findViewById(R.id.dedf);



        loadFromDatabase();
        adapter=new ProductAdapter(products,this);
        productList.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

    private void loadFromDatabase()
    {
        database = dbOpener.getReadableDatabase();
        Cursor rc = database.query(false, databaseActivity.Receipts.TABLE_NAME, new String[]{databaseActivity.Receipts.COLUMN_NAME_ID, databaseActivity.Receipts.COLUMN_NAME_FILE, databaseActivity.Receipts.COLUMN_NAME_DATE,databaseActivity.Receipts.COLUMN_NAME_DEDUCTION}, "receiptID=?",new String[]{Integer.toString(passedIndex)}, null, null, null, null, null);

        int idIndex=rc.getColumnIndex(databaseActivity.Receipts.COLUMN_NAME_ID);
        int dateIndex= rc.getColumnIndex(databaseActivity.Receipts.COLUMN_NAME_DATE);
        int deductionIndex= rc.getColumnIndex(databaseActivity.Receipts.COLUMN_NAME_DEDUCTION);

        while(rc.moveToNext()){
            String rid= rc.getString(idIndex);
            String date=rc.getString(dateIndex);
            Double deduction=rc.getDouble(deductionIndex);

            rrid.setText(rid);
            rdate.setText(date);
            ded.setText(Double.toString(deduction));
        }
        Cursor pc = database.query(false, databaseActivity.Products.TABLE_NAME, new String[]{databaseActivity.Products.COLUMN_NAME_ID,databaseActivity.Products.COLUMN_NAME_PNAME,databaseActivity.Products.COLUMN_NAME_DESCRIPTION,databaseActivity.Products.COLUMN_NAME_GLUTEN,databaseActivity.Products.COLUMN_NAME_PRICE}, "receiptID=?",new String[]{Integer.toString(passedIndex)}, null, null, null, null, null);
        int pid=pc.getColumnIndex(databaseActivity.Products.COLUMN_NAME_ID);
        int nameIndex=pc.getColumnIndex(databaseActivity.Products.COLUMN_NAME_PNAME);
        int descIndex=pc.getColumnIndex(databaseActivity.Products.COLUMN_NAME_DESCRIPTION);
        int glutenIndex=pc.getColumnIndex(databaseActivity.Products.COLUMN_NAME_GLUTEN);
        int dedIndex=pc.getColumnIndex(databaseActivity.Products.COLUMN_NAME_PRICE);


        while(pc.moveToNext()){
            boolean glutenf=false;
            int productId=pc.getInt(pid);
            String name=pc.getString(nameIndex);
            String desc=pc.getString(descIndex);
            int gluten=pc.getInt(glutenIndex);

            if(gluten==0)
            {glutenf=false;}
            if(gluten==1)
            {glutenf=true;}

            double ded=pc.getDouble(dedIndex);

            products.add(new Product(productId,name,desc,0,ded,glutenf));
        }
    }

    public class ProductAdapter extends ArrayAdapter<Product> {
        private ArrayList<Product> rData;

        Context mContext;
        TextView name;
        TextView desc;
        TextView gluten;
        TextView ded;

        public ProductAdapter(ArrayList<Product> data, Context context)  {
            super(context,R.layout.digital_receipt_layout,data);
            this.rData=data;
            this.mContext=context;
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            Product rS = getItem(position);
            LayoutInflater inflater = LayoutInflater.from(mContext);
            convertView = inflater.inflate(R.layout.digital_receipt_layout, parent, false);
            name = (TextView) convertView.findViewById(R.id.pname);
            desc = (TextView) convertView.findViewById(R.id.pdesc);
            gluten = (TextView) convertView.findViewById(R.id.gluten);
            ded = (TextView) convertView.findViewById(R.id.prize);

            name.setText(rS.getProductName());
            desc.setText(rS.getProductDescription());
            gluten.setText(Boolean.toString(rS.isGlutenFree()));
            ded.setText(Double.toString(rS.getDisplayedPrice()));
            return convertView;
        }
    }
}