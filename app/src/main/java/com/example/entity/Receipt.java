package com.example.entity;

import java.util.ArrayList;
import java.util.List;

public class Receipt {

    private long id = 0;
    private List<Product> products = new ArrayList<>();
    private String receiptFile = "default";
    private double taxDeductionTotal = 0;
    private String date = "now";

    public Receipt(long id, List<Product> products, String receiptFile, double taxDeductionTotal, String date){
        setId(id).setProducts(products).setReceiptFile(receiptFile).setTaxDeductionTotal(taxDeductionTotal).setDate(date);

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
}
