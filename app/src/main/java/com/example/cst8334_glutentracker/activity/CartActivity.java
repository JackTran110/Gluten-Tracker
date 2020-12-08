package com.example.cst8334_glutentracker.activity;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.cst8334_glutentracker.functionality.CartListViewHolder;
import com.example.cst8334_glutentracker.R;
import com.example.cst8334_glutentracker.database.GlutenDatabase;
import com.example.cst8334_glutentracker.entity.Product;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Pattern;

public class CartActivity extends AppCompatActivity {

    private Adapter adapter = new Adapter();
    private static ArrayList<Product> productsArrayList;
    private static List<Long> productIdList;
    private static int productCount;
    private GlutenDatabase db = new GlutenDatabase(this);
    public static ArrayList<String> editTextList = new ArrayList<String>(); //test
    private Context context;
    private TextView totalDeductibleDisplay;
    private TextView total;
    private  double totalPaid = 0;
    private double totalDeductible = 0;
    Toolbar cartTbar;
    private static final int REQUEST_IMAGE_CAPTURE = 1;

    /**
     * This method is called when the page is first loaded
     * @param savedInstanceState
     */
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        cartTbar = (Toolbar)findViewById(R.id.cartToolbar);

        SharedPreferences pre = getSharedPreferences("cart_activity", Context.MODE_PRIVATE);
        productIdList = new ArrayList<>();
        productsArrayList = new ArrayList<>();
        productCount = pre.getInt("Product count", 0);

        while (productCount >0){
            productIdList.add(pre.getLong(Integer.toString(productCount), 1));
            productCount--;
        }
        for(String key: pre.getAll().keySet()){
            pre.edit().remove(key).apply();
        }
        for(long id: productIdList){
            productsArrayList.add(db.selectProductByID(id));
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
        //total = findViewById(R.id.amount);
        total = findViewById(R.id.total);
        purchases.setAdapter(adapter);

 /*       if(!getProductsArrayList().isEmpty()) {
            double totalDeductibleAsDouble = 0;
            double totalAsDouble = 0;
            for (Product products : getProductsArrayList()) {
                if (products.getLinkedProduct() != null) {
                    totalDeductibleAsDouble += products.getDisplayedPrice() - products.getLinkedProduct().getDisplayedPrice();
                }
                totalAsDouble += products.getDisplayedPrice();
                // total.setText(totalAsDouble + "");
                total.setText(String.format("%.2f", totalAsDouble));
                totalPaid = totalAsDouble;
                //totalDeductibleDisplay.setText(totalDeductibleAsDouble + "");
                totalDeductibleDisplay.setText(String.format("%.2f", totalDeductibleAsDouble));
                totalDeductible = totalDeductibleAsDouble;
            }
            adapter.notifyDataSetChanged();
        } */

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
                int numberOfNotLinkedProducts = 0;
                String message;
                for(Product product: getProductsArrayList()){
                    if(product.getLinkedProduct() == null && product.isGlutenFree() == true)
                        numberOfNotLinkedProducts++;
                }
                if(numberOfNotLinkedProducts > 0 && numberOfNotLinkedProducts > 1)
                    message = "There are " + numberOfNotLinkedProducts + " gluten-free products not linked. Do you still wish to continue checkout? "
                        + "This will clear your current cart and finalize your purchase.";
                else if(numberOfNotLinkedProducts == 1)
                    message = "There is " + numberOfNotLinkedProducts + " gluten-free product not linked. Do you still wish to continue checkout? "
                            + "This will clear your current cart and finalize your purchase.";
                else
                    message = "Are you sure you would like to checkout? This will clear your current cart.";
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
                // DialogInterface learned from https://stackoverflow.com/questions/20494542/using-dialoginterfaces-onclick-method
                DialogInterface.OnClickListener dialogClickListener = (dialog, which) -> {
                    switch (which) {
                        case DialogInterface.BUTTON_POSITIVE:
                            break;

                    case DialogInterface.BUTTON_NEGATIVE:
                      /*  //Double totalPrice = 0.0;
                       // DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                        db.insertIntoProductsTable(productsArrayList);
                        //helper.insertIntoReceiptsTable(db, productsArrayList, "file", totalDeductible, totalPrice, LocalDateTime.now().format(formatter));
                        db.insertIntoReceiptsTable(productsArrayList, "file", totalDeductible, totalPaid, new Date().toString());
                        getProductsArrayList().clear();
                        totalDeductibleDisplay.setText("");
                        total.setText("");
                        adapter.notifyDataSetChanged();
                        Toast.makeText(this, "Purchase finalized", Toast.LENGTH_SHORT).show();
                        break; */
                        takePicture();
                        break;
                }
            };
            alertDialog.setTitle("Finalize Purchase");
            alertDialog.setMessage(message);
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

    private void takePicture(){
        Intent pictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if(pictureIntent.resolveActivity(getPackageManager()) != null)
            startActivityForResult(pictureIntent, REQUEST_IMAGE_CAPTURE);
    }

    private void makeAlertDialog(Bitmap receiptImage){
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        ImageView receipt = new ImageView(this);
        receipt.setImageBitmap(receiptImage);
        DialogInterface.OnClickListener dialogClickListener = (dialog, which) -> {
            switch (which) {
                case DialogInterface.BUTTON_POSITIVE:
                    break;

                case DialogInterface.BUTTON_NEGATIVE:
                    //Double totalPrice = 0.0;
                    // DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                    db.insertIntoProductsTable(productsArrayList);
                    //helper.insertIntoReceiptsTable(db, productsArrayList, "file", totalDeductible, totalPrice, LocalDateTime.now().format(formatter));
                  //  db.insertIntoReceiptsTable(productsArrayList, "file", totalDeductible, totalPaid, new Date().toString()); original
                    db.insertIntoReceiptsTableWithImage(productsArrayList, "file", totalDeductible, totalPaid, new Date().toString(), receiptImage);
                    getProductsArrayList().clear();
                    totalDeductibleDisplay.setText("");
                    total.setText("");
                    adapter.notifyDataSetChanged();
                    Toast.makeText(this, "Purchase finalized", Toast.LENGTH_SHORT).show();
                    break;
            }
        };
        alertDialog.setTitle("Accept this picture?");
        alertDialog.setView(receipt);
        alertDialog.setPositiveButton("No", dialogClickListener);
        alertDialog.setNegativeButton("Yes", dialogClickListener);
        alertDialog.create().show();

    }

    /**
     * This method returns the arraylist of products
     * @return The arraylist of products
     */
    public static ArrayList<Product> getProductsArrayList(){
        return productsArrayList;
    }

    /**
     * This method is called when the activity is resumed
     */
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
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK){
            Bundle extras = data.getExtras();
            Bitmap image = (Bitmap) extras.get("data");
            makeAlertDialog(image);
        }

    }

    /**
     * This method pauses the activity
     */
    @Override
    protected void onPause() {
        super.onPause();

        SharedPreferences pre = getSharedPreferences("cart_activity", Context.MODE_PRIVATE);
        SharedPreferences.Editor edit = pre.edit();
        productCount = productsArrayList.size();
        for(int i = 0; i< productCount; i++){
            edit.putLong(Integer.toString(i), productsArrayList.get(i).getId());
        }
        edit.putInt("Product count", productCount);
        edit.apply();
        db.insertIntoProductsTable(productsArrayList);
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
        menu.findItem(R.id.cartButton).setVisible(false);
        menu.findItem(R.id.search_view).setVisible(false);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.scannerButton:
                setResult(MainMenuActivity.RESULT_CODE_NAVIGATE_TO_SCANNER);
                finish();
                break;
            case R.id.cartButton:
                setResult(MainMenuActivity.RESULT_CODE_NAVIGATE_TO_CART);
                finish();
                break;
            case R.id.receiptButton:
                setResult(MainMenuActivity.RESULT_CODE_NAVIGATE_TO_RECEIPT);
                finish();
                break;
            case R.id.reportButton:
                setResult(MainMenuActivity.RESULT_CODE_NAVIGATE_TO_REPORT);
                finish();
                break;
        }
        return true;
    }

    /**
     * This inner class is an adapter for the arraylist.
     */
    class Adapter extends BaseAdapter{

        /**
         * Counts the number of items in the arraylist
         * @return The number of items in the arraylist
         */
        @Override
        public int getCount() {
            return productsArrayList.size();
        }

        /**
         * This method gets the Product in the selected position
         * @param position The position of the arraylist
         * @return The found Product
         */
        @Override
        public Product getItem(int position) {
            return productsArrayList.get(position);
        }

        /**
         * This method returns the id (upc code) of the Product in the selected position
         * @param position The position of the arraylist
         * @return The product's upc code
         */
        @Override
        public long getItemId(int position) {
            return productsArrayList.get(position).getId();
        }

        /**
         * This method returns the view that populates a row. This view changes whether the Product is linked or not, and it changes depending on
         * if the product is gluten-free or not (only gluten-free items may be linked, otherwise the link button will be disabled).
         * @param position The position of the arraylist
         * @param convertView Allows for recycling of views when scrolling
         * @param parent The parent class.
         * @return The view for the row
         */
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            Product product = (Product) getItem(position);
            LayoutInflater inflater = getLayoutInflater();
            View newView = inflater.inflate(R.layout.activity_product_list, parent, false);

        /*    if(product.getLinkedProduct() == null){
                newView = inflater.inflate(R.layout.activity_product_list, parent, false);
            }
            else{
                newView = inflater.inflate(R.layout.activity_product_list_linked, parent, false);
            } */
            //context = parent.getContext();
            context = newView.getContext();
            //final View testView = newView;
            TextView isGluten = newView.findViewById(R.id.glutenFree);
            if(product.isGlutenFree())
                isGluten.setText("Gluten-Free");
            else
                isGluten.setText("Not Gluten-Free");
            TextView deductibleText = newView.findViewById(R.id.deductibleText);
            TextView linkedProductName = newView.findViewById(R.id.linkedProductName);
            if(product.getLinkedProduct() != null) {
              //  deductibleText.setText((product.getDisplayedPrice() - product.getLinkedProduct().getDisplayedPrice()) + ""); //changed to displayed price
                deductibleText.setText(getString(R.string.deductible) + product.getDeductionAsString());
                linkedProductName.setText(getString(R.string.linkedProductName) + product.getLinkedProduct().getProductName());
            }
            else{ // added else statement
                deductibleText.setVisibility(View.INVISIBLE);
                linkedProductName.setVisibility(View.INVISIBLE);
            }

            TextView productName = newView.findViewById(R.id.productName);
            productName.setText(product.getProductName());

            TextView price = newView.findViewById(R.id.price);
     //       price.setText(product.getDisplayedPrice() + "");
            price.setText(getString(R.string.price) + product.getDisplayedPriceAsString());
            //EditText quantity = newView.findViewById(R.id.quantity); what it was before
            //quantity.setText("1"); not needed
            TextView quantity = newView.findViewById(R.id.quantity);
            quantity.setText(Integer.toString(product.getQuantity()));

            //Button plusButton = newView.findViewById(R.id.plusButton);
            ImageButton plusButton = newView.findViewById(R.id.plusButton);
            plusButton.setOnClickListener((v) ->{
                int convertedToInt = (Integer.parseInt(quantity.getText().toString())) +1;
                product.changeQuantityAndDisplayedPrice(convertedToInt);
                if(product.getLinkedProduct() != null){
                   // deductibleText.setText((product.getDisplayedPrice() - product.getLinkedProduct().getDisplayedPrice()) + "");
                    deductibleText.setText(getString(R.string.deductible) + product.getDeductionAsString());
                }
                adapter.notifyDataSetChanged();
            });

            //Button minusButton = newView.findViewById(R.id.minusButton);
            ImageButton minusButton = newView.findViewById(R.id.minusButton);
            minusButton.setOnClickListener((v) ->{
                if(product.getQuantity() > 1) {
                    int convertedToInt = (Integer.parseInt(quantity.getText().toString())) - 1;
                    product.changeQuantityAndDisplayedPrice(convertedToInt);
                    if(product.getLinkedProduct() != null){
                      //  deductibleText.setText((product.getDisplayedPrice() - product.getLinkedProduct().getDisplayedPrice()) + "");
                        deductibleText.setText(getString(R.string.deductible) + product.getDeductionAsString());
                    }
                    adapter.notifyDataSetChanged();
                }
            });

           Button removeButton = newView.findViewById(R.id.removeFromCart);
            removeButton.setOnClickListener((v) -> {
                totalPaid = totalPaid - getProductsArrayList().get(position).getDisplayedPrice();
                totalDeductible = totalDeductible - getProductsArrayList().get(position).getDeduction();
                //productsArrayList.remove(position);
                getProductsArrayList().remove(position);
                //Toast.makeText(this, "Should be working", Toast.LENGTH_SHORT).show();
                adapter.notifyDataSetChanged();
            });

            Button linkButton = newView.findViewById(R.id.linkButton);
            if(product.getLinkedProduct() == null){
                linkButton.setText("Link Product");
            }
            else{
                linkButton.setText("Linked!");
            }
            if(!product.isGlutenFree()){
                linkButton.setEnabled(false);
            }
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
                Link.setPassedContext(CartActivity.this);
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
               // total.setText(totalAsDouble + "");
                total.setText(getString(R.string.total) + String.format("%.2f", totalAsDouble));
                totalPaid = totalAsDouble;
                //totalDeductibleDisplay.setText(totalDeductibleAsDouble + "");
                totalDeductibleDisplay.setText(getString(R.string.total_deductible) + String.format("%.2f", totalDeductibleAsDouble));
                totalDeductible = totalDeductibleAsDouble;
            }
            return newView;
            //return null;
        }
    }


}