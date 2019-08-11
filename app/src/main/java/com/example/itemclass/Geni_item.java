package com.example.itemclass;

public class Geni_item {

    private  String geni_music_title;
    private  String geni_music_link;
    private  String geni_music_artist;
    private  String geni_music_img;
    private  String geni_music_rank;


    public Geni_item(String geni_music_title,String geni_music_link,String geni_music_artist,String geni_music_rank,String geni_music_img) {
        this.geni_music_title = geni_music_title;
        this.geni_music_link = geni_music_link;
        this.geni_music_artist = geni_music_artist;
        this.geni_music_rank = geni_music_rank;
        this.geni_music_img = geni_music_img;
    }




    public String get_geni_music_title() {
        return geni_music_title;
    }


    public String get_geni_music_link() {
        return geni_music_link;
    }

    public String get_geni_music_artist() {
        return geni_music_artist;
    }

    public String get_geni_music_rank() {
        return geni_music_rank;
    }


    public String get_geni_music_img() {
        return geni_music_img;
    }


}
