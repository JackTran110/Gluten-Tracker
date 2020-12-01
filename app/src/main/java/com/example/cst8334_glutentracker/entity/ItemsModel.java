package com.example.cst8334_glutentracker.entity;

import com.example.cst8334_glutentracker.entity.Product;

import java.util.List;

public class ItemsModel {

    private String rId;
    private String rDate;
    private int rYear;
    private int rMonth;
    private int rDay;
    private  List<Product> rItem;
    private String rSub;
    private String rTax;

    public ItemsModel(String rId, String rDate, List<Product> rItem, String rSub, String rTax) {
        this.rId = rId;
        this.rDate = rDate;
        this.rItem = rItem;
        this.rSub = rSub;
        this.rTax = rTax;
    }

    public ItemsModel(String rId, String rDate, String rSub, String rTax) {
        this.rId = rId;
        this.rDate = rDate;

        this.rDate = rDate;

        this.rSub = rSub;
        this.rTax = rTax;
    }

    public String getrId() {
        return rId;
    }

    public String getrDate() {
        return rDate;
    }

    public List<Product> getrItem() {
        return rItem;
    }

    public String getrSub() {
        return rSub;
    }

    public String getrTax() {
        return rTax;
    }

    public void setrId(String rId) {
        this.rId = rId;
    }

    public void setrDate(String rDate) {
        this.rDate = rDate;
    }

    public void setrItem(List<Product> rItem) {
        this.rItem = rItem;
    }

    public void setrSub(String rSub) {
        this.rSub = rSub;
    }

    public void setrTax(String rTax) {
        this.rTax = rTax;
    }


    public int getrYear() {
        return rYear;
    }

    public void setrYear(int rYear) {
        this.rYear = rYear;
    }

    public int getrMonth() {
        return rMonth;
    }

    public void setrMonth(int rMonth) {
        this.rMonth = rMonth;
    }

    public int getrDay() {
        return rDay;
    }

    public void setrDay(int rDay) {
        this.rDay = rDay;
    }
}