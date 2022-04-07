package com.example.mob201_levanchung_ps19319_asm.model;

public class TinTuc {
    private String title;
    private String link;
    private String date;
    private String img;

    public TinTuc(String title, String link, String date, String img) {
        this.title = title;
        this.link = link;
        this.date = date;
        this.img = img;
    }

    public String getTitle() {
        return title;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }
}
