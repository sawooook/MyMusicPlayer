package com.example.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.itemclass.qr_item;
import com.example.itemclass.recommand_item;
import com.example.login_activity.R;

import java.util.ArrayList;

public class qr_adapter extends BaseAdapter {

    ArrayList<qr_item> qrcode_arrayList;
    Context applicationContext;


    public qr_adapter(Context applicationContext, ArrayList<qr_item> qrcode_arrayList) {
        this.qrcode_arrayList =qrcode_arrayList;
        this.applicationContext=applicationContext;

    }

    @Override
    public int getCount() {
        return qrcode_arrayList.size();
    }

    @Override
    public Object getItem(int i) {
        return qrcode_arrayList.get(i);
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
            view = inflater.inflate(R.layout.list_qr, null); //브로드캐스터 아이템 리스트를 인플레이트 시킨다



            holder.qr_title = view.findViewById(R.id.qr_title); //
            holder.qr_artist = view.findViewById(R.id.qr_artist); //

            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }
        holder.qr_title.setText(qrcode_arrayList.get(i).get_qr_title()); //어레이리스트에 장착
        holder.qr_artist.setText(qrcode_arrayList.get(i).get_qr_artist()); // 어레이 리스트에 장착착

        return view;

    }


    static class ViewHolder {

        TextView qr_title; //노래 추천 제목
        TextView qr_artist; //노래 추천 가수

    }
}
