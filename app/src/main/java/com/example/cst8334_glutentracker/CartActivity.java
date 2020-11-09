package com.example.cst8334_glutentracker;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.content.Intent;
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

import java.util.ArrayList;

public class CartActivity extends AppCompatActivity {

    private Adapter adapter = new Adapter();
    private static ArrayList<Product> productsArrayList = new ArrayList<Product>();
    private int productCount = 0;
    private glutenDbHelper helper = new glutenDbHelper(this);
    public static ArrayList<String> editTextList = new ArrayList<String>(); //test
    private Context context;

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
            for(Product product: productsArrayList){
                helper.insertIntoProductsTable(db, product);
            }
            helper.insertIntoReceiptsTable(db, productsArrayList, "file", 0, "0/0/0000");
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
        /*    EditText changePrice = newView.findViewById(R.id.changePriceAndQuantityText);
            changePrice.addTextChangedListener(new TextWatcher() { // working on this
                boolean flag = false; // added this for testing

                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                }

                @Override
                public void afterTextChanged(Editable s) {
                   /* //adapter.notifyDataSetChanged();
                    if(!flag){
                        flag = true;
                        //String textToSet = changePrice.getText().toString();
                        //changePrice.setText(textToSet);
                        String textToSet = s.toString();
                        changePrice.setText(textToSet);
                        flag = false;
                    }*/
               /*    if(!flag){
                       flag = true;
                       if(editTextList.size() > position){
                           editTextList.set(position, s.toString());
                       }
                       else {
                           editTextList.add(s.toString());
                       }
                       flag = false;
                   }

                }
            });
            if(editTextList.size() > 0){

                changePrice.setText(editTextList.get(position));
            } */

            TextView price = newView.findViewById(R.id.price);
            price.setText(product.getDisplayedPrice() + "");
            //EditText quantity = newView.findViewById(R.id.quantity); what it was before
            //quantity.setText("1"); not needed
            TextView quantity = newView.findViewById(R.id.quantity);
            quantity.setText(Integer.toString(product.getQuantity()));

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
                product.setDisplayedPrice(product.getPrice() * (convertedToInt + 1));
                product.setQuantity(convertedToInt + 1);
                if(product.getLinkedProduct() != null){
                    product.getLinkedProduct().setQuantity(product.getQuantity());
                    product.getLinkedProduct().setDisplayedPrice(product.getLinkedProduct().getPrice() * product.getLinkedProduct().getQuantity());
                    deductibleText.setText((product.getDisplayedPrice() - product.getLinkedProduct().getDisplayedPrice()) + "");
                }
                adapter.notifyDataSetChanged();
            });

            Button minusButton = newView.findViewById(R.id.minusButton);
            minusButton.setOnClickListener((v) ->{
                if(product.getQuantity() > 1) {
                    int convertedToInt = Integer.parseInt(quantity.getText().toString());
                    product.setDisplayedPrice(product.getPrice() * (convertedToInt - 1));
                    product.setQuantity(convertedToInt - 1);
                    if(product.getLinkedProduct() != null){
                        product.getLinkedProduct().setQuantity(product.getQuantity());
                        product.getLinkedProduct().setDisplayedPrice(product.getLinkedProduct().getPrice() * product.getLinkedProduct().getQuantity());
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

           /* Button changePriceButton = newView.findViewById(R.id.changePrice);
            changePriceButton.setOnClickListener((v) ->{
                product.setQuantity(1);
                product.setPrice(Double.valueOf(changePrice.getText().toString()));
                quantity.setText(Integer.toString(product.getQuantity()));
                product.setDisplayedPrice(product.getPrice() * product.getQuantity());
                price.setText(product.getDisplayedPrice() + "");
                if(product.getLinkedProduct() != null){
                    product.getLinkedProduct().setQuantity(product.getQuantity());
                    product.getLinkedProduct().setDisplayedPrice(product.getLinkedProduct().getPrice() * product.getLinkedProduct().getQuantity());
                    deductibleText.setText((product.getDisplayedPrice() - product.getLinkedProduct().getDisplayedPrice()) + "");
                }
                adapter.notifyDataSetChanged();

            }); */

            Button editButton = newView.findViewById(R.id.editProduct);
            editButton.setOnClickListener((v) -> {
                //Product editedProduct = product;
                Product editedProduct = new Product(product.getId(), product.getProductName(), product.getProductDescription(), product.getBarCode(),
                        product.getPrice(), product.isGlutenFree());
                editedProduct.setQuantity(product.getQuantity());
                editedProduct.setDisplayedPrice(product.getDisplayedPrice());
                if(product.getLinkedProduct() != null) {
                    editedProduct.setLinkedProduct(product.getLinkedProduct());
                    editedProduct.getLinkedProduct().setQuantity(product.getLinkedProduct().getQuantity());
                    editedProduct.getLinkedProduct().setDisplayedPrice(editedProduct.getLinkedProduct().getPrice() * editedProduct.getLinkedProduct().getQuantity());
                }
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);
                DialogInterface.OnClickListener dialogClickListener = (dialog, which) -> {
                    switch (which){
                        case DialogInterface.BUTTON_POSITIVE:
                            //Yes button clicked
                            break;

                        case DialogInterface.BUTTON_NEGATIVE:
                            //No button clicked
                            product.setQuantity(editedProduct.getQuantity());
                            product.setPrice(editedProduct.getPrice());
                            product.setDisplayedPrice(editedProduct.getDisplayedPrice());
                            if(product.getLinkedProduct() != null && editedProduct.getLinkedProduct() != null){
                                product.setLinkedProduct(editedProduct.getLinkedProduct());
                            }
                            adapter.notifyDataSetChanged();
                            break;
                    }
                };
                alertDialog.setTitle("Edit Product");
                alertDialog.setMessage(editedProduct.getProductName());
                alertDialog.setNegativeButton("Save", dialogClickListener).
                setPositiveButton("Cancel", dialogClickListener);
                //EditText test = new EditText(context);
                //alertDialog.setView(test);
                //View row = getLayoutInflater().inflate(R.layout.activity_product_list, parent, false); // worked
                View row = getLayoutInflater().inflate(R.layout.activity_edit_product, parent, false);
                TextView deductibleEdit = row.findViewById(R.id.deductibleTextEdit);
                TextView deductibleViewEdit = row.findViewById(R.id.deductibleViewEdit);
                if(editedProduct.getLinkedProduct() == null){
                    deductibleViewEdit.setVisibility(row.INVISIBLE);
                    deductibleEdit.setVisibility(row.INVISIBLE);
                }
                else{
                    deductibleEdit.setText((editedProduct.getDisplayedPrice() - editedProduct.getLinkedProduct().getDisplayedPrice()) + "");
                }
                EditText changePriceEdit = row.findViewById(R.id.changePriceAndQuantityTextEdit);
                Button changePriceEditButton = row.findViewById(R.id.changePriceEdit);
                TextView priceEdit = row.findViewById(R.id.priceEdit);
                //priceEdit.setText(product.getDisplayedPrice() + ""); originally there
                priceEdit.setText(editedProduct.getDisplayedPrice() + "");
                EditText quantityEdit = row.findViewById(R.id.quantityEdit);
                quantityEdit.setText(editedProduct.getQuantity() + "");
                changePriceEditButton.setOnClickListener((view) -> {
                   /* product.setQuantity(1);
                    product.setPrice(Double.valueOf(changePriceEdit.getText().toString()));
                    //quantity.setText(Integer.toString(product.getQuantity()));
                    product.setDisplayedPrice(product.getPrice() * product.getQuantity());
                    priceEdit.setText(product.getDisplayedPrice() + "");
                    if(product.getLinkedProduct() != null){
                        product.getLinkedProduct().setQuantity(product.getQuantity());
                        product.getLinkedProduct().setDisplayedPrice(product.getLinkedProduct().getPrice() * product.getLinkedProduct().getQuantity());
                        //deductibleText.setText((product.getDisplayedPrice() - product.getLinkedProduct().getDisplayedPrice()) + ""); all this code originally worked*/
                   if(changePriceEdit.getText().toString().trim().length() > 0) {
                       editedProduct.setQuantity(1);
                       editedProduct.setPrice(Double.valueOf(changePriceEdit.getText().toString()));
                       //quantity.setText(Integer.toString(product.getQuantity()));
                       quantityEdit.setText(Integer.toString(1));
                       editedProduct.setDisplayedPrice(editedProduct.getPrice() * editedProduct.getQuantity());
                       priceEdit.setText(editedProduct.getDisplayedPrice() + "");
                       changePriceEdit.setText("");
                       if (editedProduct.getLinkedProduct() != null) {
                           editedProduct.getLinkedProduct().setQuantity(editedProduct.getQuantity());
                           editedProduct.getLinkedProduct().setDisplayedPrice(editedProduct.getLinkedProduct().getPrice() * editedProduct.getLinkedProduct().getQuantity());
                           //deductibleText.setText((product.getDisplayedPrice() - product.getLinkedProduct().getDisplayedPrice()) + "");
                           deductibleEdit.setText((editedProduct.getDisplayedPrice() - editedProduct.getLinkedProduct().getDisplayedPrice()) + "");
                       }
                       adapter.notifyDataSetChanged();
                   }
                });
                //EditText quantityEdit = row.findViewById(R.id.quantityEdit);
                quantityEdit.addTextChangedListener(new TextWatcher(){

                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {

                    }

                    @Override
                    public void afterTextChanged(Editable s) {
                        //int newQuantity = Integer.parseInt(s.toString());
                        /*int newQuantity;
                        if(s.toString() == null || s.toString() == "" || s.toString().isEmpty()){
                            newQuantity = 1;
                        }
                        else{
                            newQuantity = Integer.parseInt(s.toString());
                        }
                        product.setDisplayedPrice(product.getPrice() * (newQuantity));
                        product.setQuantity(newQuantity);
                        priceEdit.setText(product.getDisplayedPrice() + "");
                        if(product.getLinkedProduct() != null){
                            product.getLinkedProduct().setQuantity(product.getQuantity());
                            product.getLinkedProduct().setDisplayedPrice(product.getLinkedProduct().getPrice() * product.getLinkedProduct().getQuantity());
                            //deductibleText.setText((product.getDisplayedPrice() - product.getLinkedProduct().getDisplayedPrice()) + "");
                        }
                        adapter.notifyDataSetChanged();  all this code block worked*/
                        int newQuantity;
                        if(s.toString() == null || s.toString() == "" || s.toString().isEmpty()){
                            newQuantity = 1;
                        }
                        else{
                            newQuantity = Integer.parseInt(s.toString());
                        }
                        editedProduct.setDisplayedPrice(editedProduct.getPrice() * (newQuantity));
                        editedProduct.setQuantity(newQuantity);
                        priceEdit.setText(editedProduct.getDisplayedPrice() + "");
                        if(editedProduct.getLinkedProduct() != null){
                            editedProduct.getLinkedProduct().setQuantity(editedProduct.getQuantity());
                            editedProduct.getLinkedProduct().setDisplayedPrice(editedProduct.getLinkedProduct().getPrice() * editedProduct.getLinkedProduct().getQuantity());
                            //deductibleText.setText((product.getDisplayedPrice() - product.getLinkedProduct().getDisplayedPrice()) + "");
                            deductibleEdit.setText((editedProduct.getDisplayedPrice() - editedProduct.getLinkedProduct().getDisplayedPrice()) + "");
                        }
                        adapter.notifyDataSetChanged();

                    }
                });

                Button plusButtonEdit = row.findViewById(R.id.plusButtonEdit);
                Button minusButtonEdit = row.findViewById(R.id.minusButtonEdit);
                plusButtonEdit.setOnClickListener((view) ->{

                    if(quantityEdit.getText().toString().trim().length() == 0){
                        quantityEdit.setText(1 + "");
                    }
                    int convertedToInt = Integer.parseInt(quantityEdit.getText().toString());
                    editedProduct.setDisplayedPrice(editedProduct.getPrice() * (convertedToInt + 1));
                    editedProduct.setQuantity(convertedToInt + 1);
                    quantityEdit.setText(editedProduct.getQuantity() + "");
                    if(editedProduct.getLinkedProduct() != null){
                        editedProduct.getLinkedProduct().setQuantity(editedProduct.getQuantity());
                        editedProduct.getLinkedProduct().setDisplayedPrice(editedProduct.getLinkedProduct().getPrice() * editedProduct.getLinkedProduct().getQuantity());
                        deductibleEdit.setText((editedProduct.getDisplayedPrice() - editedProduct.getLinkedProduct().getDisplayedPrice()) + "");
                    }
                    adapter.notifyDataSetChanged();
                });

                minusButtonEdit.setOnClickListener((view) -> {
                    if(editedProduct.getQuantity() > 1 && quantityEdit.getText().toString().trim().length() > 0) {
                        int convertedToInt = Integer.parseInt(quantityEdit.getText().toString());
                        editedProduct.setDisplayedPrice(editedProduct.getPrice() * (convertedToInt - 1));
                        editedProduct.setQuantity(convertedToInt - 1);
                        quantityEdit.setText(editedProduct.getQuantity() + "");
                        if(editedProduct.getLinkedProduct() != null){
                            editedProduct.getLinkedProduct().setQuantity(editedProduct.getQuantity());
                            editedProduct.getLinkedProduct().setDisplayedPrice(editedProduct.getLinkedProduct().getPrice() * editedProduct.getLinkedProduct().getQuantity());
                            deductibleEdit.setText((editedProduct.getDisplayedPrice() - editedProduct.getLinkedProduct().getDisplayedPrice()) + "");
                        }
                        adapter.notifyDataSetChanged();
                    }
                });
                alertDialog.setView(row);
                //alertDialog.setView(testView);
                alertDialog.create().show();
            });
            //adapter.notifyDataSetChanged(); //used this to try figure out why I can't change value in changeprice edittext, did not work
            return newView;
            //return null;
        }
    }


}