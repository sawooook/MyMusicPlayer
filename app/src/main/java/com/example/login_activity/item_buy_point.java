package com.example.login_activity;

public class item_buy_point {


    private  String Buy_point_name;
    private  String Buy_point_amount;



    public item_buy_point(String Buy_point_name,String Buy_point_amount) {
        this.Buy_point_name = Buy_point_name;
        this.Buy_point_amount = Buy_point_amount;

    }




    public String get_Buy_point_name() {

        return Buy_point_name;
    }


    public String get_Buy_point_amount() {

        return Buy_point_amount;
    }



}
