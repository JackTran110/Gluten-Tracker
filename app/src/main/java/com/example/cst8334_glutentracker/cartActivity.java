package com.example.cst8334_glutentracker;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class cartActivity extends AppCompatActivity {

    private Adapter adapter = new Adapter();
    private ArrayList<TestProduct> productsArrayList = new ArrayList<TestProduct>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        ListView purchases = findViewById(R.id.purchases);
        Button addProductButton = findViewById(R.id.addNewProductButton);
        productsArrayList.add(new TestProduct("Testing", 3.00));
        productsArrayList.add(new TestProduct("Testing 2", 5.00));
        purchases.setAdapter(adapter);
        adapter.notifyDataSetChanged();

        addProductButton.setOnClickListener((View v)->{

        });

       /* if(productsArrayList.size() > 0){
            Button removeButton = findViewById(R.id.removeFromCart);
            removeButton.setOnClickListener((v) ->{
                int position = purchases.getPositionForView(v);
                productsArrayList.remove(position);
                Toast.makeText(this, "Should be working", Toast.LENGTH_SHORT).show();
                adapter.notifyDataSetChanged();
            });
        } */

    }

    class Adapter extends BaseAdapter{

        @Override
        public int getCount() {
            return productsArrayList.size();
        }

        @Override
        public TestProduct getItem(int position) {
            return productsArrayList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return productsArrayList.get(position).getId();
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            TestProduct product = (TestProduct) getItem(position);
            LayoutInflater inflater = getLayoutInflater();
            View newView = inflater.inflate(R.layout.activity_product_list, parent, false);
            TextView productName = newView.findViewById(R.id.productName);
            productName.setText(product.getProductName());
            TextView price = newView.findViewById(R.id.price);
            price.setText(product.getPrice() + "");
            EditText quantity = newView.findViewById(R.id.quantity);
            quantity.setText("1");

            Button plusButton = newView.findViewById(R.id.plusButton);
            plusButton.setOnClickListener((v) ->{
               /* String currentQuantity = quantity.getText().toString();
                int converted = Integer.parseInt(currentQuantity);
                converted = converted + 1;
                quantity.setText(Integer.toString(converted));
                double multiPrice = Double.parseDouble(price.getText().toString());
                multiPrice = multiPrice * converted;
                price.setText(Double.toString(multiPrice));
                adapter.notifyDataSetChanged(); */
                int convertedToInt = Integer.parseInt(quantity.getText().toString());
                product.setPrice(product.getPrice() * (convertedToInt + 1));
                adapter.notifyDataSetChanged();
            });

           Button removeButton = newView.findViewById(R.id.removeFromCart);
            removeButton.setOnClickListener((v) -> {
                productsArrayList.remove(position);
                //Toast.makeText(this, "Should be working", Toast.LENGTH_SHORT).show();
                adapter.notifyDataSetChanged();
            });
            return newView;
            //return null;
        }
    }
}