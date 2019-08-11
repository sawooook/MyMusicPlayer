package com.example.login_activity;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class adapter_buy_point extends BaseAdapter {

    ArrayList<item_buy_point> Buy_Point_arrayList;
    Context applicationContext;


    public adapter_buy_point(Context applicationContext, ArrayList<item_buy_point> Buy_Point_arrayList) {
        this.Buy_Point_arrayList = Buy_Point_arrayList;
        this.applicationContext = applicationContext;
    }

    @Override
    public int getCount() {
        return Buy_Point_arrayList.size();
    }

    @Override
    public Object getItem(int i) {
        return Buy_Point_arrayList.get(i);
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

            LayoutInflater inflater = (LayoutInflater)applicationContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.list_buy_point, null); //브로드캐스터채팅리스트를 인플레이트 시킨다


            holder.Buy_point_name=view.findViewById(R.id.Buy_point_name); //
            holder.Buy_point_amount=view.findViewById(R.id.Buy_point_amount); //

            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }
        holder.Buy_point_name.setText(Buy_Point_arrayList.get(i).get_Buy_point_name());
        holder.Buy_point_amount.setText(Buy_Point_arrayList.get(i).get_Buy_point_amount());

        return view;

    }
    static class ViewHolder {
        TextView Buy_point_name;
        TextView Buy_point_amount;



    }
}
