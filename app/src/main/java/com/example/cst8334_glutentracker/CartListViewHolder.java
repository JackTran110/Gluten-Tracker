package com.example.cst8334_glutentracker;

import android.content.Context;
import android.content.DialogInterface;
import android.database.sqlite.SQLiteDatabase;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;

import com.example.entity.Product;

/**
 * This class is used to hold a method shared between multiple classes.
 */
public class CartListViewHolder {

    /**
     * This method creates an AlertDialog that allows the user to edit a product's quantity and price depending on the context passed. If the context is an instance of Link.java,
     * then editing a product's quantity is disabled as we want to compare the price difference between a gluten-free product and a non-gluten free product of the same quantity
     * being purchased. If the context is an instance of DigitalReceipt.java, changes will reflected in the ProductReceipt table.
     * @param context The context from where this method is being called. Depending on the context passed, certain features may be enabled or disabled.
     * @param product The product to be edited.
     * @param adapter The adapter from where this method is being called. This is used to reflect changes in the calling class's activity.
     * @param row The view to inflate the AlertDialog with.
     * @param dbOpener An instance of the GlutenDatabase class. This is only used if this method is being called by the DigitalReceipt.java class, as it will update the ProductReceipt
     *                 table in the database.
     * @param index The ReceiptId. This will be used to find the row(s) to update the ProductReceipt table.
     */
    public static void editProduct(Context context, Product product, BaseAdapter adapter, View row,GlutenDatabase dbOpener,long index){
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
        DialogInterface.OnClickListener dialogClickListener = (dialog, which) ->  {
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
                    if(context instanceof DigitalReceipt){
//                        SQLiteDatabase db = dbOpener.getWritableDatabase();
//                        dbOpener.updateProductById(db,product.getId(),product.getPrice(),product.getQuantity());
                        dbOpener.updateProductReceiptById(product,index);
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
        // View row = getLayoutInflater().inflate(R.layout.activity_edit_product, parent, false); original
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
//        Button changePriceEditButton = row.findViewById(R.id.changePriceEdit);
        TextView priceEdit = row.findViewById(R.id.priceEdit);
        //priceEdit.setText(product.getDisplayedPrice() + ""); originally there
        priceEdit.setText(editedProduct.getDisplayedPrice() + "");
        EditText quantityEdit = row.findViewById(R.id.quantityEdit);
        if(context instanceof Link){
            //quantityEdit.setVisibility(row.INVISIBLE);
            quantityEdit.setEnabled(false);
        }
        else {
            quantityEdit.setText(editedProduct.getQuantity() + "");
        }
        quantityEdit.setText(editedProduct.getQuantity() + "");
        changePriceEdit.addTextChangedListener(new TextWatcher(){

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
              /*  int newQuantity;
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
                adapter.notifyDataSetChanged(); */
                double newPrice;
              /*  if(s.toString() != null || s.toString() != "" || !s.toString().isEmpty()) {
                    newPrice = Double.valueOf(s.toString());
                    if(s.toString() == null || s.toString().equals("") || s.toString().isEmpty()){
                        newPrice = product.getDisplayedPrice();
                    }
                } */
                if(s.toString() == null || s.toString().equals("") || s.toString().isEmpty()){
                    newPrice = product.getDisplayedPrice();
                }
                else{
                    newPrice = Double.valueOf(s.toString());
                }
                    editedProduct.setDisplayedPrice(newPrice);
                    editedProduct.setPrice(newPrice / editedProduct.getQuantity());
                    priceEdit.setText(editedProduct.getDisplayedPrice() + "");
                    if(editedProduct.getLinkedProduct() != null){
                        deductibleEdit.setText(editedProduct.getDisplayedPrice() - editedProduct.getLinkedProduct().getDisplayedPrice() + "");
                    }
                    adapter.notifyDataSetChanged();


            }
        });
  /*      changePriceEditButton.setOnClickListener((view) -> {
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
        }); */
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

        ImageButton plusButtonEdit = row.findViewById(R.id.plusButtonEdit);
        ImageButton minusButtonEdit = row.findViewById(R.id.minusButtonEdit);
        if(context instanceof Link){
            //plusButtonEdit.setVisibility(row.INVISIBLE);
           // minusButtonEdit.setVisibility(row.INVISIBLE);
            plusButtonEdit.setEnabled(false);
            minusButtonEdit.setEnabled(false);
        }
        else {
            plusButtonEdit.setOnClickListener((view) -> {
                if (quantityEdit.getText().toString().trim().length() == 0) {
                    quantityEdit.setText(1 + "");
                }
                int convertedToInt = (Integer.parseInt(quantityEdit.getText().toString())) + 1;
                editedProduct.changeQuantityAndDisplayedPrice(convertedToInt);
                quantityEdit.setText(editedProduct.getQuantity() + "");
                if (editedProduct.getLinkedProduct() != null) {
                    deductibleEdit.setText((editedProduct.getDisplayedPrice() - editedProduct.getLinkedProduct().getDisplayedPrice()) + "");
                }
                adapter.notifyDataSetChanged();
            });

            minusButtonEdit.setOnClickListener((view) -> {
                if (editedProduct.getQuantity() > 1 && quantityEdit.getText().toString().trim().length() > 0) {
                    int convertedToInt = (Integer.parseInt(quantityEdit.getText().toString())) - 1;
                    editedProduct.changeQuantityAndDisplayedPrice(convertedToInt);
                    quantityEdit.setText(editedProduct.getQuantity() + "");
                    if (editedProduct.getLinkedProduct() != null) {
                        deductibleEdit.setText((editedProduct.getDisplayedPrice() - editedProduct.getLinkedProduct().getDisplayedPrice()) + "");
                    }
                    adapter.notifyDataSetChanged();
                }
            });
        }
        alertDialog.setView(row);
        //alertDialog.setView(testView);
        alertDialog.create().show();
    }
}
