package com.example.entity;

public class ItemsModel {

    private String rId;
    private String rDate;
    private int rYear;
    private int rMonth;
    private int rDay;
    private String rItem;
    private String rSub;
    private String rTax;

    public ItemsModel(String rId, String rDate, String rItem, String rSub, String rTax) {
        this.rId = rId;
        this.rDate = rDate;
        this.rItem = rItem;
        this.rSub = rSub;
        this.rTax = rTax;
    }

    public ItemsModel(String rId, String rDate, int rYear, int rMonth,int rDay, String rItem, String rSub, String rTax) {
        this.rId = rId;
        this.rDate = rDate;
        this.rYear = rYear;
        this.rMonth = rMonth;
        this.rDate = rDate;
        this.rItem = rItem;
        this.rSub = rSub;
        this.rTax = rTax;
    }

    public String getrId() {
        return rId;
    }

    public String getrDate() {
        return rDate;
    }

    public String getrItem() {
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

    public void setrItem(String rItem) {
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


