package com.example.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.itemclass.lyricis_item;
import com.example.itemclass.recommand_item;
import com.example.login_activity.R;

import java.util.ArrayList;

public class recommand_adapter extends BaseAdapter {

    ArrayList<recommand_item> recommandation_arrayList;
    Context applicationContext;


    public recommand_adapter(Context applicationContext, ArrayList<recommand_item> recommandation_arrayList) {
        this.recommandation_arrayList =recommandation_arrayList;
        this.applicationContext=applicationContext;

    }

    @Override
    public int getCount() {
        return recommandation_arrayList.size();
    }

    @Override
    public Object getItem(int i) {
        return recommandation_arrayList.get(i);
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
            view = inflater.inflate(R.layout.list_recommand, null); //브로드캐스터 아이템 리스트를 인플레이트 시킨다



            holder.recommand_title = view.findViewById(R.id.recommand_title); //
            holder.recommand_artistc = view.findViewById(R.id.recommand_artist); //

            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }
        holder.recommand_title.setText(recommandation_arrayList.get(i).get_recommand_title()); //어레이리스트에 장착
        holder.recommand_artistc.setText(recommandation_arrayList.get(i).get_recommand_artist()); // 어레이 리스트에 장착착

        return view;

    }


    static class ViewHolder {

        TextView recommand_title; //노래 추천 제목
        TextView recommand_artistc; //노래 추천 가수

    }
}