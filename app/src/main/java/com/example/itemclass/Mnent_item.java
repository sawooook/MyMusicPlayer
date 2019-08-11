package com.example.itemclass;

public class Mnent_item {

    private  String mnent_music_title;
    private  String mnent_music_link;
    private  String mnent_music_artist;
    private  String mnent_music_rank;
    private  String mnent_music_img;


    public Mnent_item(String mnent_music_title,String mnent_music_link,String mnent_music_artist,String mnent_music_rank, String mnent_music_img) {
        this.mnent_music_title = mnent_music_title;
        this.mnent_music_link = mnent_music_link;
        this.mnent_music_artist = mnent_music_artist;
        this.mnent_music_rank = mnent_music_rank;
        this.mnent_music_img = mnent_music_img;
    }




    public String get_mnent_music_title() {
        return mnent_music_title;
    }


    public String get_mnent_music_link() {
        return mnent_music_link;
    }

    public String get_mnent_music_artist() {
        return mnent_music_artist;
    }

    public String get_mnent_music_img() {
        return mnent_music_img;
    }

    public String get_mnent_music_rank() {
        return mnent_music_rank;
    }




}
