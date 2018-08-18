package com.sourcey.materiallogindemo.Chat;

public class CRNModel {
    private String title;
    private int id ;
    //get id from database .. using volley
    public CRNModel(String title){
        this.title=title;
    }
    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }
}