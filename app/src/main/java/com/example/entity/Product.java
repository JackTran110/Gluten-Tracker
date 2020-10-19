package com.example.entity;

public class Product {

    private long id = 0;
    private String productName = "default";
    private String productDescription = "default";
    private String barCode = "default";
    private double price = 0;
    private double displayedPrice = 0;
    private int quantity = 0;
    private boolean isGlutenFree = false;

    public Product(long id, String productName, String productDescription, String barCode, double price, boolean isGlutenFree){
        setId(id);
        setProductName(productName);
        setProductDescription(productDescription);
        setBarCode(barCode);
        setPrice(price);
        setIsGlutenFree(isGlutenFree);
        setQuantity(1);
        setDisplayedPrice(price);
    }

    public Product setId(long id){
        this.id = id;
        return this;
    }

    public long getId() {
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

    public double getDisplayedPrice() {
        return displayedPrice;
    }

    public void setDisplayedPrice(double displayedPrice) {
        this.displayedPrice = displayedPrice;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}