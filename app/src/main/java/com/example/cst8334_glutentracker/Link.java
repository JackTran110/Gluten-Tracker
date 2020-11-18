package com.example.cst8334_glutentracker;

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
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.example.entity.Product;

import java.util.ArrayList;

public class Link extends AppCompatActivity {
    ArrayList<Product> listOfProducts = new ArrayList<Product>();
    //Bundle dataFromActivity;
    FragmentAdapter adapter = new FragmentAdapter();
    Intent fromActivity;
    int passedIndex;
    Context context;
    private SQLiteDatabase database;
    private GlutenDbHelper dbOpener = new GlutenDbHelper(this);


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_link);
        //dataFromActivity = getArguments();
        fromActivity = getIntent();
        passedIndex = fromActivity.getIntExtra("Index", 3);
        ListView linkTest = findViewById(R.id.linkTest);
        listOfProducts.add(new Product(3, "Chip", "A bag of chips", 1.00, false));
        linkTest.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

    private void loadTestValuesFromDatabase(){
        database = dbOpener.getReadableDatabase();
        String[] columns = {DatabaseActivity.Products.COLUMN_NAME_ID, DatabaseActivity.Products.COLUMN_NAME_PRODUCT_NAME, DatabaseActivity.Products.COLUMN_NAME_DESCRIPTION,
            DatabaseActivity.Products.COLUMN_NAME_GLUTEN, DatabaseActivity.Products.COLUMN_NAME_PRICE};
        Cursor resultsQuery = database.query(false, DatabaseActivity.Products.TABLE_NAME, columns, "isGlutenFree = ?", new String[]{"0"},
                null, null, null, null);

        int idColIndex = resultsQuery.getColumnIndex(DatabaseActivity.Products.COLUMN_NAME_ID);
        int nameColIndex = resultsQuery.getColumnIndex(DatabaseActivity.Products.COLUMN_NAME_PRODUCT_NAME);
        int descriptionColIndex = resultsQuery.getColumnIndex(DatabaseActivity.Products.COLUMN_NAME_DESCRIPTION);
        int glutenColIndex = resultsQuery.getColumnIndex(DatabaseActivity.Products.COLUMN_NAME_GLUTEN);
        int priceColIndex = resultsQuery.getColumnIndex(DatabaseActivity.Products.COLUMN_NAME_PRICE);

        while(resultsQuery.moveToNext()){
            int id = resultsQuery.getInt(idColIndex);
            String name = resultsQuery.getString(nameColIndex);
            String description = resultsQuery.getString(descriptionColIndex);
            double price = resultsQuery.getDouble(priceColIndex);
            listOfProducts.add(new Product(id, name, description, price, false));
        }

        resultsQuery.close();
    }

    class FragmentAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return listOfProducts.size();
        }

        @Override
        public Product getItem(int position) {
            return listOfProducts.get(position);
        }

        @Override
        public long getItemId(int position) {
            return listOfProducts.get(position).getId();
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            Product product = (Product) getItem(position);
            product.setQuantity(CartActivity.getProductsArrayList().get(passedIndex).getQuantity());
            product.setDisplayedPrice(product.getPrice() * product.getQuantity());
            LayoutInflater inflater = getLayoutInflater();
            //View newView = inflater.inflate(R.layout.activity_product_list, parent, false);
            View newView = inflater.inflate(R.layout.activity_fragment_populate_listview, parent, false);
            context = newView.getContext();
            TextView nameText = newView.findViewById(R.id.productFoundName);
            TextView descriptionText = newView.findViewById(R.id.productFoundDescription);
            TextView priceText = newView.findViewById(R.id.productFoundPrice);
            //EditText priceText = newView.findViewById(R.id.productFoundPrice);
            nameText.setText(product.getProductName() + " ");
            descriptionText.setText(product.getProductDescription() + " ");
            priceText.setText(product.getDisplayedPrice() + " ");
   // worked        TextView textTest = newView.findViewById(R.id.textTest);
   //  worked       TextView testPrice = newView.findViewById(R.id.testPrice);
           // int passedIndex = dataFromActivity.getInt("Index");
            //int passedIndex = fromActivity.getIntExtra("Index");
 //           int passedIndex = fromActivity.getIntExtra("Index", 3);
            //testPrice.setText(product.getPrice() + "");
   //   worked      testPrice.setText(CartActivity.getProductsArrayList().get(passedIndex).getPrice() + "");
            //Product passedProduct = (Product) dataFromActivity.getSerializable("Product");

            Button testButton = newView.findViewById(R.id.linkCommit);
            testButton.setOnClickListener((v) -> {
                //passedProduct.setPrice(product.getPrice());
               /* CartActivity.getProductsArrayList().get(passedIndex).setDisplayedPrice(product.getPrice());
                CartActivity.getProductsArrayList().add(new Product(5, "M", "Meow", "M", 4.00, false)); */
                CartActivity.getProductsArrayList().get(passedIndex).setLinkedProduct(product);
                adapter.notifyDataSetChanged();
                finish();
            });

           /* EditText changeFoundPrice = newView.findViewById(R.id.changeFoundPriceAndQuantityText);
            Button changePriceButton = newView.findViewById(R.id.changeFoundPrice);
            changePriceButton.setOnClickListener((v) ->{
                product.setQuantity(1);
                product.setPrice(Double.valueOf(changeFoundPrice.getText().toString()));
                //quantity.setText(Integer.toString(product.getQuantity()));
                product.setDisplayedPrice(product.getPrice() * product.getQuantity());
                priceText.setText(product.getDisplayedPrice() + "");
//                if(product.getLinkedProduct() != null){
//                    product.getLinkedProduct().setQuantity(product.getQuantity());
//                    product.getLinkedProduct().setDisplayedPrice(product.getLinkedProduct().getPrice() * product.getLinkedProduct().getQuantity());
//                    deductibleText.setText((product.getDisplayedPrice() - product.getLinkedProduct().getDisplayedPrice()) + "");
//                }
                adapter.notifyDataSetChanged();

            }); */
           Button editButton = newView.findViewById(R.id.editGlutenButton);
           editButton.setOnClickListener((v) -> {
//               View row = getLayoutInflater().inflate(R.layout.activity_edit_product, parent, false);
               View row = getLayoutInflater().inflate(R.layout.activity_edit_receipt, parent, false);
               CartListViewHolder.editProduct(context, product, adapter, row,null);
           });


            /*TextView productName = newView.findViewById(R.id.productName);
            productName.setText(product.getProductName());
            TextView price = newView.findViewById(R.id.price);
            price.setText(product.getDisplayedPrice() + "");
            EditText quantity = newView.findViewById(R.id.quantity);
            //quantity.setText("1");
            quantity.setText(Integer.toString(product.getQuantity()));

            Button plusButton = newView.findViewById(R.id.plusButton);
            plusButton.setOnClickListener((v) ->{

                int convertedToInt = Integer.parseInt(quantity.getText().toString());
                product.setDisplayedPrice(product.getPrice() * (convertedToInt + 1));
                product.setQuantity(convertedToInt + 1);
                fragmentAdapter.notifyDataSetChanged();
            });

            Button minusButton = newView.findViewById(R.id.minusButton);
            minusButton.setOnClickListener((v) ->{
                if(product.getQuantity() > 1) {
                    int convertedToInt = Integer.parseInt(quantity.getText().toString());
                    product.setDisplayedPrice(product.getPrice() * (convertedToInt - 1));
                    product.setQuantity(convertedToInt - 1);
                    fragmentAdapter.notifyDataSetChanged();
                }
            });

            Button removeButton = newView.findViewById(R.id.removeFromCart);
            removeButton.setOnClickListener((v) -> {
                listOfProducts.remove(position);
                //Toast.makeText(this, "Should be working", Toast.LENGTH_SHORT).show();
                fragmentAdapter.notifyDataSetChanged();
            });

            Button linkButton = newView.findViewById(R.id.linkButton);
            linkButton.setOnClickListener((v) -> {
                // Finish the rest of this
            }); */

            return newView;
            //return null;
        }
    }
}