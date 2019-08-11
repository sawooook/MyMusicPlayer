package com.example.login_activity;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class adapter_Music_buy extends BaseAdapter {

    ArrayList<item_music_buy> Music_buy_arrayList;
    Context applicationContext;


    public adapter_Music_buy(Context applicationContext, ArrayList<item_music_buy> Music_buy_arrayList) {
        this.Music_buy_arrayList = Music_buy_arrayList;
        this.applicationContext = applicationContext;
    }

    @Override
    public int getCount() {
        return Music_buy_arrayList.size();
    }

    @Override
    public Object getItem(int i) {
        return Music_buy_arrayList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        ViewHolder holder;

        if (view == null) {
            holder = new ViewHolder();

            LayoutInflater inflater = (LayoutInflater) applicationContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.list_music_buy, null); //브로드캐스터채팅리스트를 인플레이트 시킨다


            holder.Music_Buy_title = view.findViewById(R.id.Buy_music_title);
            holder.Music_Buy_price = view.findViewById(R.id.Buy_music_price);

            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }
        holder.Music_Buy_title.setText(Music_buy_arrayList.get(i).get_Music_buy_title());
        holder.Music_Buy_price.setText(Music_buy_arrayList.get(i).get_Music_buy_price());

        return view;

    }

    static class ViewHolder {
        TextView Music_Buy_title;
        TextView Music_Buy_price;


    }
}