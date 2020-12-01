package com.example.cst8334_glutentracker.entity;

public class Product {

    private long id = 0;
    private String productName = "default";
    private String productDescription = "default";
    private double price = 0;
    private double displayedPrice = 0;
    private int quantity = 0;
    private boolean isGlutenFree = false;
    private Product linkedProduct = null;
    private double deduction = 0;


    public Product(long id, String productName, String productDescription, double price, boolean isGlutenFree){
        setId(id);
        setProductName(productName);
        setProductDescription(productDescription);
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

    public Product setDisplayedPrice(double displayedPrice) {
        this.displayedPrice = displayedPrice;
        return this;
    }

    public int getQuantity() {
        return quantity;
    }

    public Product setQuantity(int quantity) {
        this.quantity = quantity;
        return this;
    }

    public Product getLinkedProduct(){
        return linkedProduct;
    }

    public void setLinkedProduct(Product linkedProduct){
        this.linkedProduct = linkedProduct;
    }

    public void changeQuantityAndDisplayedPrice(int newQuantity){
        setQuantity(newQuantity);
        setDisplayedPrice(getPrice() * getQuantity());
        //quantityEdit.setText(editedProduct.getQuantity() + "");
        if(getLinkedProduct() != null){
            getLinkedProduct().setQuantity(getQuantity());
            getLinkedProduct().setDisplayedPrice(getLinkedProduct().getPrice() * getLinkedProduct().getQuantity());
            //deductibleEdit.setText((editedProduct.getDisplayedPrice() - editedProduct.getLinkedProduct().getDisplayedPrice()) + "");
        }
    }

    public void changeQuantityAndOriginalPrice(double newPrice){
        setQuantity(1);
        setPrice(newPrice);
        //quantity.setText(Integer.toString(product.getQuantity()));
        //quantityEdit.setText(Integer.toString(1));
        setDisplayedPrice(getPrice() * getQuantity());
        //priceEdit.setText(editedProduct.getDisplayedPrice() + "");
        //changePriceEdit.setText("");
        if (getLinkedProduct() != null) {
            getLinkedProduct().setQuantity(getQuantity());
            getLinkedProduct().setDisplayedPrice(getLinkedProduct().getPrice() * getLinkedProduct().getQuantity());
            //deductibleText.setText((product.getDisplayedPrice() - product.getLinkedProduct().getDisplayedPrice()) + "");
            //deductibleEdit.setText((editedProduct.getDisplayedPrice() - editedProduct.getLinkedProduct().getDisplayedPrice()) + "");
        }
    }

    /*public double getDeduction() {
        return deduction;
    } */

    public double getDeduction() {
        if(getLinkedProduct() != null)
            return getDisplayedPrice() - getLinkedProduct().getDisplayedPrice();
        else
            return 0;
    }

    public void setDeduction(double deduction) {
        this.deduction = deduction;
    }

    public String getDisplayedPriceAsString(){
        return String.format("%.2f", getDisplayedPrice());
    }

    public String getDeductionAsString(){
        return String.format("%.2f", getDeduction());
    }
}