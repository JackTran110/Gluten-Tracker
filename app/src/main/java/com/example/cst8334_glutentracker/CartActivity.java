package com.example.cst8334_glutentracker;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.entity.Product;

import java.util.ArrayList;
import java.util.Date;

public class CartActivity extends AppCompatActivity {

    private Adapter adapter = new Adapter();
    private static ArrayList<Product> productsArrayList = new ArrayList<Product>();
    private int productCount = 0;
    private GlutenDatabase db = new GlutenDatabase(this);
    public static ArrayList<String> editTextList = new ArrayList<String>(); //test
    private Context context;
    private TextView totalDeductibleDisplay;
    private TextView total;
    private  double totalPaid = 0;
    private double totalDeductible = 0;
    Toolbar cartTbar;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        cartTbar = (Toolbar)findViewById(R.id.cartToolbar);

        SharedPreferences pre = getSharedPreferences("cart_activity", Context.MODE_PRIVATE);
        productCount = pre.getInt("Product count", 0);
        for(int i = 1; i <= productCount; i++){
//            productsArrayList.add()
        }

        ListView purchases = findViewById(R.id.purchases);
        Button checkoutButton = findViewById(R.id.checkout_button);

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
                        db.insertIntoProductsTable(productsArrayList);
                        //helper.insertIntoReceiptsTable(db, productsArrayList, "file", totalDeductible, totalPrice, LocalDateTime.now().format(formatter));
                        db.insertIntoReceiptsTable(productsArrayList, "file", totalDeductible, totalPaid, new Date().toString());
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.toolbar, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.scannerButton:
                Intent goToScanner = new Intent(CartActivity.this, ScanActivity.class);
                startActivity(goToScanner);
            break;
            case R.id.cartButton:
                Intent goToCart = new Intent(CartActivity.this, CartActivity.class);
                startActivity(goToCart);
            break;
            case R.id.receiptButton:
                Intent goToReceipt = new Intent(CartActivity.this, ReceiptActivity.class);
                startActivity(goToReceipt);
            break;
            case R.id.reportButton:
                Intent goToReport = new Intent(CartActivity.this, ReportActivity.class);
                startActivity(goToReport);
            break;
        }
        return true;
    }

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
                View row = getLayoutInflater().inflate(R.layout.activity_edit_receipt, parent, false);
                CartListViewHolder.editProduct(CartActivity.this,product,adapter,row,null,0);
              /*  //Product editedProduct = product;
                Product editedProduct = new Product(product.getId(), product.getProductName(), product.getProductDescription(),
                        product.getPrice(), product.isGlutenFree());
                editedProduct.setQuantity(product.getQuantity());
                editedProduct.setDisplayedPrice(product.getDisplayedPrice());
                if(product.getLinkedProduct() != null) {
                    //editedProduct.setLinkedProduct(product.getLinkedProduct());
                    editedProduct.setLinkedProduct(new Product(product.getLinkedProduct().getId(), product.getLinkedProduct().getProductName(),
                            product.getLinkedProduct().getProductDescription(), product.getLinkedProduct().getPrice(),
                            product.getLinkedProduct().isGlutenFree())); // testing
                    editedProduct.getLinkedProduct().setQuantity(product.getLinkedProduct().getQuantity());
                    editedProduct.getLinkedProduct().setDisplayedPrice(editedProduct.getLinkedProduct().getPrice() * editedProduct.getLinkedProduct().getQuantity());
                }
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);
                // DialogInterface learned from https://stackoverflow.com/questions/20494542/using-dialoginterfaces-onclick-method
                DialogInterface.OnClickListener dialogClickListener = (dialog, which) -> {
                    switch (which){
                        case DialogInterface.BUTTON_POSITIVE:
                            break;

                        case DialogInterface.BUTTON_NEGATIVE:
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
                    if(changePriceEdit.getText().toString().trim().length() > 0) {
                        double newPrice = Double.valueOf(changePriceEdit.getText().toString());
                        editedProduct.changeQuantityAndOriginalPrice(newPrice);
                        quantityEdit.setText(Integer.toString(1));
                        priceEdit.setText(editedProduct.getDisplayedPrice() + "");
                        changePriceEdit.setText("");
                        if (editedProduct.getLinkedProduct() != null) {
                            deductibleEdit.setText((editedProduct.getDisplayedPrice() - editedProduct.getLinkedProduct().getDisplayedPrice()) + "");
                        }
                        adapter.notifyDataSetChanged();
                    }
                });
                quantityEdit.addTextChangedListener(new TextWatcher(){

                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {

                    }

                    @Override
                    public void afterTextChanged(Editable s) {
                        int newQuantity;
                        if(s.toString() == null || s.toString() == "" || s.toString().isEmpty()){
                            newQuantity = 1;
                        }
                        else{
                            newQuantity = Integer.parseInt(s.toString());
                        }
                        editedProduct.changeQuantityAndDisplayedPrice(newQuantity);
                        priceEdit.setText(editedProduct.getDisplayedPrice() + "");
                        if(editedProduct.getLinkedProduct() != null){
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
                    int convertedToInt = (Integer.parseInt(quantityEdit.getText().toString())) + 1;
                    editedProduct.changeQuantityAndDisplayedPrice(convertedToInt);
                    quantityEdit.setText(editedProduct.getQuantity() + "");
                    if(editedProduct.getLinkedProduct() != null){
                        deductibleEdit.setText((editedProduct.getDisplayedPrice() - editedProduct.getLinkedProduct().getDisplayedPrice()) + "");
                    }
                    adapter.notifyDataSetChanged();
                });

                minusButtonEdit.setOnClickListener((view) -> {
                    if(editedProduct.getQuantity() > 1 && quantityEdit.getText().toString().trim().length() > 0) {
                        int convertedToInt = (Integer.parseInt(quantityEdit.getText().toString())) - 1;
                        editedProduct.changeQuantityAndDisplayedPrice(convertedToInt);
                        quantityEdit.setText(editedProduct.getQuantity() + "");
                        if(editedProduct.getLinkedProduct() != null){
                            deductibleEdit.setText((editedProduct.getDisplayedPrice() - editedProduct.getLinkedProduct().getDisplayedPrice()) + "");
                        }
                        adapter.notifyDataSetChanged();
                    }
                });
                alertDialog.setView(row);
                //alertDialog.setView(testView);
                alertDialog.create().show(); */
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