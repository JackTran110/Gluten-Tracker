package com.example.cst8334_glutentracker;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.entity.Product;

import org.w3c.dom.Text;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;

public class CartActivity extends AppCompatActivity {

    private Adapter adapter = new Adapter();
    private static ArrayList<Product> productsArrayList = new ArrayList<Product>();
    private int productCount = 0;
    private GlutenDbHelper helper = new GlutenDbHelper(this);
    public static ArrayList<String> editTextList = new ArrayList<String>(); //test
    private Context context;
    private TextView totalDeductibleDisplay;
    private TextView total;
    private  double totalPaid = 0;
    private double totalDeductible = 0;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        SharedPreferences pre = getSharedPreferences("cart_activity", Context.MODE_PRIVATE);
        productCount = pre.getInt("Product count", 0);
        for(int i = 1; i <= productCount; i++){
//            productsArrayList.add()
        }

        ListView purchases = findViewById(R.id.purchases);
        Button checkoutButton = findViewById(R.id.checkout_button);
        SQLiteDatabase db = helper.getWritableDatabase();

//        productsArrayList.add(new Product(1, "Oreo", "Milk's favorite cookie", 0, 3.00, false));
//        productCount += 1;
//        productsArrayList.add(new Product(2, "Gluten Free Cookie", "A gluten free cookie", 0, 5.00, true));
//        productCount += 1;

        Button addNewProductButton = findViewById(R.id.addNewProductButton);
        addNewProductButton.setOnClickListener((v) -> {
            startActivity(new Intent(CartActivity.this, ScanActivity.class));
        });

        //productsArrayList.add(new Product(1, "Oreo", "Milk's favorite cookie", "test", 3.00, false));
        //productsArrayList.add(new Product(2, "Gluten Free Cookie", "A gluten free cookie", "test", 5.00, true));
        totalDeductibleDisplay = findViewById(R.id.totalDeductible);
        total = findViewById(R.id.amount);
        purchases.setAdapter(adapter);
        adapter.notifyDataSetChanged();

//        addProductButton.setOnClickListener((View v)->{
//
//        });

       /* if(productsArrayList.size() > 0){
            Button removeButton = findViewById(R.id.removeFromCart);
            removeButton.setOnClickListener((v) ->{
                int position = purchases.getPositionForView(v);
                productsArrayList.remove(position);
                Toast.makeText(this, "Should be working", Toast.LENGTH_SHORT).show();
                adapter.notifyDataSetChanged();
            });
        } */
        checkoutButton.setOnClickListener((View v) -> {
            if(!productsArrayList.isEmpty()) {
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
                // DialogInterface learned from https://stackoverflow.com/questions/20494542/using-dialoginterfaces-onclick-method
                DialogInterface.OnClickListener dialogClickListener = (dialog, which) -> {
                    switch (which) {
                        case DialogInterface.BUTTON_POSITIVE:
                            break;

                    case DialogInterface.BUTTON_NEGATIVE:
                        //Double totalPrice = 0.0;
                       // DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                        for(Product product: productsArrayList){
                            helper.insertIntoProductsTable(db, product);
                            if(product.getLinkedProduct()!=null)
                            {helper.insertIntoProductsTable(db,product.getLinkedProduct());}
                            //totalPrice += product.getPrice();
                        }
                        //helper.insertIntoReceiptsTable(db, productsArrayList, "file", totalDeductible, totalPrice, LocalDateTime.now().format(formatter));
                        helper.insertIntoReceiptsTable(db, productsArrayList, "file", totalDeductible, totalPaid, new Date().toString());
                        getProductsArrayList().clear();
                        totalDeductibleDisplay.setText("");
                        total.setText("");
                        adapter.notifyDataSetChanged();
                        Toast.makeText(this, "Purchase finalized", Toast.LENGTH_SHORT).show();
                        break;
                }
            };
            alertDialog.setTitle("Finalize Purchase");
            alertDialog.setMessage("Are you sure you would like to checkout? This will clear your current cart.");
            alertDialog.setPositiveButton("No", dialogClickListener);
            alertDialog.setNegativeButton("Yes", dialogClickListener);
            alertDialog.create().show();
          /*  for(Product product: productsArrayList){
                helper.insertIntoProductsTable(db, product);
            }
            //helper.insertIntoReceiptsTable(db, productsArrayList, "file", 0, "0/0/0000");
            helper.insertIntoReceiptsTable(db, productsArrayList, "file", totalDeductible, new Date().toString());
            getProductsArrayList().clear();
            totalDeductibleDisplay.setText("");
            adapter.notifyDataSetChanged(); */
            }
            else{
                Toast.makeText(this, "Cart is empty, unable to checkout", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public static ArrayList<Product> getProductsArrayList(){
        return productsArrayList;
    }

    @Override
    protected void onResume() {
        super.onResume();
        //getProductsArrayList().get(1).setPrice(9.00);
       /* if(changePrice != null){
            if(changePrice.getText().toString().trim().length() > 0){
                editTextTemp = Integer.parseInt((changePrice.getText().toString()));
                int test = 2;
            }
        } */
      // Log.w("Resume", "It went to onResume");
        adapter.notifyDataSetChanged();

    }

    @Override
    protected void onPause() {
        super.onPause();

        SharedPreferences pre = getSharedPreferences("cart_activity", Context.MODE_PRIVATE);
        SharedPreferences.Editor edit = pre.edit();
    }

 /*   // added this to check edittext bug
    @Override
    protected void onStart() {
        super.onStart();
        Log.w("test", "Testing");
    } */

    class Adapter extends BaseAdapter{

        @Override
        public int getCount() {
            return productsArrayList.size();
        }

        @Override
        public Product getItem(int position) {
            return productsArrayList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return productsArrayList.get(position).getId();
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            Product product = (Product) getItem(position);
            LayoutInflater inflater = getLayoutInflater();
            View newView = inflater.inflate(R.layout.activity_product_list, parent, false);

            if(product.getLinkedProduct() == null){
                newView = inflater.inflate(R.layout.activity_product_list, parent, false);

            }
            else{
                newView = inflater.inflate(R.layout.activity_product_list_linked, parent, false);
                /*TextView deductibleText = newView.findViewById(R.id.deductibleText);
                deductibleText.setText((product.getPrice() - product.getLinkedProduct().getPrice()) + ""); */
            }
            //context = parent.getContext();
            context = newView.getContext();
            //final View testView = newView;
            TextView deductibleText = newView.findViewById(R.id.deductibleText);
            if(product.getLinkedProduct() != null) {
                deductibleText.setText((product.getDisplayedPrice() - product.getLinkedProduct().getDisplayedPrice()) + ""); //changed to displayed price
            }

            TextView productName = newView.findViewById(R.id.productName);
            productName.setText(product.getProductName());

            TextView price = newView.findViewById(R.id.price);
            price.setText(product.getDisplayedPrice() + "");
            //EditText quantity = newView.findViewById(R.id.quantity); what it was before
            //quantity.setText("1"); not needed
            TextView quantity = newView.findViewById(R.id.quantity);
            quantity.setText(Integer.toString(product.getQuantity()));

            Button plusButton = newView.findViewById(R.id.plusButton);
            plusButton.setOnClickListener((v) ->{
                int convertedToInt = (Integer.parseInt(quantity.getText().toString())) +1;
                product.changeQuantityAndDisplayedPrice(convertedToInt);
                if(product.getLinkedProduct() != null){
                    deductibleText.setText((product.getDisplayedPrice() - product.getLinkedProduct().getDisplayedPrice()) + "");
                }
                adapter.notifyDataSetChanged();
            });

            Button minusButton = newView.findViewById(R.id.minusButton);
            minusButton.setOnClickListener((v) ->{
                if(product.getQuantity() > 1) {
                    int convertedToInt = (Integer.parseInt(quantity.getText().toString())) - 1;
                    product.changeQuantityAndDisplayedPrice(convertedToInt);
                    if(product.getLinkedProduct() != null){
                        deductibleText.setText((product.getDisplayedPrice() - product.getLinkedProduct().getDisplayedPrice()) + "");
                    }
                    adapter.notifyDataSetChanged();
                }
            });

           Button removeButton = newView.findViewById(R.id.removeFromCart);
            removeButton.setOnClickListener((v) -> {
                productsArrayList.remove(position);
                //Toast.makeText(this, "Should be working", Toast.LENGTH_SHORT).show();
                adapter.notifyDataSetChanged();
            });

            Button linkButton = newView.findViewById(R.id.linkButton);
            linkButton.setOnClickListener((v) -> {
                // Finish the rest of this
                Bundle dataToPass = new Bundle();
                //dataToPass.putSerializable("Product", product);
                dataToPass.putInt("Index", position);

               /* Intent intent = new Intent(CartActivity.this, EmptyActivity.class);
                intent.putExtras(dataToPass);
                startActivity(intent); // may need to be changed */
                 Intent intent = new Intent(CartActivity.this, Link.class);
                //intent.putExtras(dataToPass);
                intent.putExtra("Index", position);
                startActivity(intent); // may need to be changed
            });

            Button editButton = newView.findViewById(R.id.editProduct);
            editButton.setOnClickListener((v) -> {
                //Product editedProduct = product;
                View row = getLayoutInflater().inflate(R.layout.activity_edit_receipt, parent, false);
                CartListViewHolder.editProduct(CartActivity.this,product,adapter,row,null);
            });
            //adapter.notifyDataSetChanged(); //used this to try figure out why I can't change value in changeprice edittext, did not work
            double totalDeductibleAsDouble = 0;
            double totalAsDouble = 0;
            for(Product products: getProductsArrayList()){
                if(products.getLinkedProduct() != null){
                    totalDeductibleAsDouble += products.getDisplayedPrice() - products.getLinkedProduct().getDisplayedPrice();
                }
                totalAsDouble += products.getDisplayedPrice();
                total.setText(totalAsDouble + "");
                totalPaid = totalAsDouble;
                totalDeductibleDisplay.setText(totalDeductibleAsDouble + "");
                totalDeductible = totalDeductibleAsDouble;
            }
            return newView;
            //return null;
        }
    }


}