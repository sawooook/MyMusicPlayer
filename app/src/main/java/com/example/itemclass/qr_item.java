package com.example.itemclass;

public class qr_item {

    private  String qr_title;
    private  String qr_artist;


    public qr_item(String qr_title,String qr_artist) {
        this.qr_title = qr_title;
        this.qr_artist = qr_artist;

    }


    public String get_qr_title() {
        return qr_title;
    }
    public String get_qr_artist() {
        return qr_artist;
    }




}
