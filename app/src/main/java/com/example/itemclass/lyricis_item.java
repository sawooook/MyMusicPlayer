package com.example.itemclass;

public class lyricis_item {

    private  String lyricis_title;
    private  String lyricis_artist;



    public lyricis_item(String lyricis_title,String lyricis_artist) {
        this.lyricis_title = lyricis_title;
        this.lyricis_artist = lyricis_artist;

    }




    public String get_lyricis_title() {
        return lyricis_title;
    }


    public String get_lyricis_artist() {
        return lyricis_artist;
    }




}
