package com.example.cst8334_glutentracker;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.example.entity.Product;

import java.util.ArrayList;

public class DigitalReceipt extends AppCompatActivity {
    private ProductAdapter adapter;
    private ArrayList<Product> products;
    private ListView productList;
    private SQLiteDatabase database;
    private GlutenDbHelper dbOpener = new GlutenDbHelper(this);
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
        Cursor rc = database.query(false, DatabaseActivity.Receipts.TABLE_NAME, new String[]{DatabaseActivity.Receipts.COLUMN_NAME_ID, DatabaseActivity.Receipts.COLUMN_NAME_FILE, DatabaseActivity.Receipts.COLUMN_NAME_DATE, DatabaseActivity.Receipts.COLUMN_NAME_TOTAL_DEDUCTION}, "receiptID=?",new String[]{Integer.toString(passedIndex)}, null, null, null, null, null);

        int idIndex=rc.getColumnIndex(DatabaseActivity.Receipts.COLUMN_NAME_ID);
        int dateIndex= rc.getColumnIndex(DatabaseActivity.Receipts.COLUMN_NAME_DATE);
        int deductionIndex= rc.getColumnIndex(DatabaseActivity.Receipts.COLUMN_NAME_TOTAL_DEDUCTION);

        while(rc.moveToNext()){
            String rid= rc.getString(idIndex);
            String date=rc.getString(dateIndex);
            Double deduction=rc.getDouble(deductionIndex);

            rrid.setText(rid);
            rdate.setText(date);
            ded.setText(Double.toString(deduction));
        }
//        Cursor pc = database.query(false, databaseActivity.Products.TABLE_NAME, new String[]{databaseActivity.Products.COLUMN_NAME_ID,databaseActivity.Products.COLUMN_NAME_PNAME,databaseActivity.Products.COLUMN_NAME_DESCRIPTION,databaseActivity.Products.COLUMN_NAME_GLUTEN,databaseActivity.Products.COLUMN_NAME_PRICE}, "receiptID=?",new String[]{Integer.toString(passedIndex)}, null, null, null, null, null);
        Cursor pc=database.rawQuery("SELECT * FROM "+ DatabaseActivity.Products.TABLE_NAME+ " JOIN " +
                DatabaseActivity.ProductReceipt.TABLE_NAME+" ON products."+ DatabaseActivity.Products.COLUMN_NAME_ID+" = productReceipt." +
                DatabaseActivity.ProductReceipt.COLUMN_NAME_PRODUCT_ID+" WHERE "+
                DatabaseActivity.ProductReceipt.COLUMN_NAME_RECEIPT_ID+" = ? ",new String[]{Integer.toString(passedIndex)});

        int pid=pc.getColumnIndex(DatabaseActivity.Products.COLUMN_NAME_ID);
        int nameIndex=pc.getColumnIndex(DatabaseActivity.Products.COLUMN_NAME_PRODUCT_NAME);
        int descIndex=pc.getColumnIndex(DatabaseActivity.Products.COLUMN_NAME_DESCRIPTION);
        int glutenIndex=pc.getColumnIndex(DatabaseActivity.Products.COLUMN_NAME_GLUTEN);

//      int dedIndex=pc.getColumnIndex(DatabaseActivity.Products.COLUMN_NAME_PRICE);

         int dedIndex=pc.getColumnIndex(DatabaseActivity.ProductReceipt.COLUMN_NAME_PRICE);
         int quantityIndex=pc.getColumnIndex(DatabaseActivity.ProductReceipt.COLUMN_NAME_QUANTITY);

        while(pc.moveToNext()){
            boolean glutenf=false;
            int productId=pc.getInt(pid);
            String name=pc.getString(nameIndex);
            String desc=pc.getString(descIndex);
            int gluten=pc.getInt(glutenIndex);
            int quantity=pc.getInt(quantityIndex);

            if(gluten==0)
            {glutenf=false;}
            if(gluten==1)
            if(gluten==1)
            {glutenf=true;}

            double ded=pc.getDouble(dedIndex);

            products.add(new Product(productId,name,desc,ded,glutenf).setQuantity(quantity));
        }
    }

    public class ProductAdapter extends ArrayAdapter<Product> {
        private ArrayList<Product> rData;

        Context mContext;
        TextView name;
        TextView desc;
        TextView gluten;
        TextView quantity;
        TextView ded;
        Button edit;

        public ProductAdapter(ArrayList<Product> data, Context context)  {
            super(context,R.layout.digital_receipt_layout,data);
            this.rData=data;
            this.mContext=context;
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            Product product = getItem(position);

            LayoutInflater inflater = LayoutInflater.from(mContext);
            convertView = inflater.inflate(R.layout.digital_receipt_layout, parent, false);
            name = (TextView) convertView.findViewById(R.id.pname);
            desc = (TextView) convertView.findViewById(R.id.pdesc);
            gluten = (TextView) convertView.findViewById(R.id.gluten);
            quantity=(TextView) convertView.findViewById(R.id.qty);
            ded = (TextView) convertView.findViewById(R.id.prize);
            edit=convertView.findViewById(R.id.edit);
            Context context=convertView.getContext();

            name.setText(product.getProductName());
            desc.setText(product.getProductDescription());
            gluten.setText(Boolean.toString(product.isGlutenFree()));
            quantity.setText(Integer.toString(product.getQuantity()));
            ded.setText(Double.toString(product.getDisplayedPrice()));

            edit.setOnClickListener((v) -> {
                View row = getLayoutInflater().inflate(R.layout.activity_edit_receipt, parent, false);
//                SQLiteDatabase db = dbOpener.getWritableDatabase();
//                dbOpener.updateProductById(db,product.getId(),product.getDisplayedPrice());
                CartListViewHolder.editProduct(DigitalReceipt.this,product,adapter,row,dbOpener);
            });
            return convertView;
        }
    }

}