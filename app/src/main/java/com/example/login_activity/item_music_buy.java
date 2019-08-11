package com.example.login_activity;

public class item_music_buy {


    private  String Music_buy_title;
    private  String Music_buy_price;



    public item_music_buy(String Music_buy_title,String Music_buy_price) {
        this.Music_buy_title = Music_buy_title;
        this.Music_buy_price = Music_buy_price;

    }



    public String get_Music_buy_title() {

        return Music_buy_title;
    }


    public String get_Music_buy_price() {

        return Music_buy_price;
    }

}
