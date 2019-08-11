package com.example.itemclass;

public class recommand_item {

    private  String recommand_title;
    private  String recommand_artist;


    public recommand_item(String recommand_title,String recommand_artist) {
        this.recommand_title = recommand_title;
        this.recommand_artist = recommand_artist;

    }


    public String get_recommand_title() {
        return recommand_title;
    }
    public String get_recommand_artist() {
        return recommand_artist;
    }



}
