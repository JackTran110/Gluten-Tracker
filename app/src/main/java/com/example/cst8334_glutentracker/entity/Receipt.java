package com.example.cst8334_glutentracker.entity;

import android.graphics.Bitmap;

import java.util.ArrayList;
import java.util.List;

public class Receipt {  
    private long id = 0;
    private List<Product> products = new ArrayList<>();
    private String receiptFile = "default";
    private double taxDeductionTotal = 0;
    private double totalPrice = 0;
    private String date = "now";
    private Bitmap image;

    public Receipt(long id, List<Product> products, String receiptFile, double taxDeductionTotal, double totalPrice, String date){
        setId(id).setProducts(products).setReceiptFile(receiptFile).setTaxDeductionTotal(taxDeductionTotal).setTotalPrice(totalPrice).setDate(date);
    }

    public Receipt(long id, List<Product> products, String receiptFile, double taxDeductionTotal, double totalPrice, String date, Bitmap image){
        setId(id).setProducts(products).setReceiptFile(receiptFile).setTaxDeductionTotal(taxDeductionTotal).setTotalPrice(totalPrice).setDate(date).setImage(image);
    }

    public long getId() {
        return id;
    }

    public Receipt setId(long id) {
        this.id = id;
        return this;
    }

    public List<Product> getProducts() {
        return products;
    }

    public Receipt setProducts(List<Product> products) {
        this.products = products;
        return this;
    }

    public String getReceiptFile() {
        return receiptFile;
    }

    public Receipt setReceiptFile(String receiptFile) {
        this.receiptFile = receiptFile;
        return this;
    }

    public double getTaxDeductionTotal() {
        return taxDeductionTotal;
    }

    public Receipt setTaxDeductionTotal(double taxDeductionTotal) {
        this.taxDeductionTotal = taxDeductionTotal;
        return this;
    }

    public String getDate() {
        return date;
    }

    public Receipt setDate(String date) {
        this.date = date;
        return this;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public Receipt setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
        return this;
    }

    public Bitmap getImage(){return image;}

    public void setImage(Bitmap image){this.image = image;}
}
