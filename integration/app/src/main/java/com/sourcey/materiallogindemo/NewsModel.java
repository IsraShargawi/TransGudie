package com.sourcey.materiallogindemo;

public class NewsModel {
    private String title;
    private int id;
    private String author;
    private String date;
    private String imgsrc;
    private int likesCount;


    public NewsModel(int id ,String title, String date, int likesCount, String imgsrc, String author) {
        this.id = id;
        this.title = title;
        this.date = date;
        this.likesCount = likesCount;
        this.imgsrc = imgsrc;
        this.author = author;
    }
    public String getImgsrc() {
        return imgsrc;
    }

    public void setImgsrc(String imgsrc) {
        this.imgsrc = imgsrc;
    }

    public int getLikesCount() {
        return likesCount;
    }

    public void setLikesCount(int likesCount) {
        this.likesCount = likesCount;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }
    public String getDate() {
        return date;
    }
    public void setDate(String date) {
        this.date = date;
    }
}