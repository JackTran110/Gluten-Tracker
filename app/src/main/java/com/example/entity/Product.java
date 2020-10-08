package com.example.entity;

public class Product {

    private int id = 0;
    private String productName = "default";
    private String productDescription = "default";
    private String barCode = "default";
    private double price = 0;
    private boolean isGlutenFree = false;

    public Product setId(int id){
        this.id = id;
        return this;
    }

    public int getId() {
        return id;
    }

    public Product setProductName(String productName){
        this.productName = productName;
        return this;
    }

    public String getProductName(){
        return productName;
    }

    public Product setProductDescription(String productDescription){
        this.productDescription = productDescription;
        return this;
    }

    public String getProductDescription(){
        return productDescription;
    }

    public Product setBarCode(String barCode){
        this.barCode = barCode;
        return this;
    }

    public String getBarCode(){
        return barCode;
    }

    public Product setPrice(double price){
        this.price = price;
        return this;
    }

    public double getPrice(){
        return price;
    }

    public Product setIsGlutenFree(boolean isGlutenFree){
        this.isGlutenFree = isGlutenFree;
        return this;
    }

    public boolean isGlutenFree(){
        return isGlutenFree;
    }
}