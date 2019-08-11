package com.example.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.itemclass.Mnent_item;
import com.example.itemclass.lyricis_item;
import com.example.login_activity.R;

import java.util.ArrayList;

public class lyricis_adapter extends BaseAdapter {

    ArrayList<lyricis_item> Music_lyrics_recommandation_arrayList;
    Context applicationContext;


    public lyricis_adapter(Context applicationContext, ArrayList<lyricis_item> Music_lyrics_recommandation_arrayList) {
        this.Music_lyrics_recommandation_arrayList =Music_lyrics_recommandation_arrayList;
        this.applicationContext=applicationContext;

    }

    @Override
    public int getCount() {
        return Music_lyrics_recommandation_arrayList.size();
    }

    @Override
    public Object getItem(int i) {
        return Music_lyrics_recommandation_arrayList.get(i);
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
            view = inflater.inflate(R.layout.list_lyricis, null); //브로드캐스터 아이템 리스트를 인플레이트 시킨다



            holder.lyricis_recommand_title = view.findViewById(R.id.lyricis_recommand_title); //
            holder.lyricis_recommand_artistc = view.findViewById(R.id.lyricis_recommand_artist); //

            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }
        holder.lyricis_recommand_title.setText(Music_lyrics_recommandation_arrayList.get(i).get_lyricis_title()); //어레이리스트에 장착
        holder.lyricis_recommand_artistc.setText(Music_lyrics_recommandation_arrayList.get(i).get_lyricis_artist()); // 어레이 리스트에 장착착

        return view;

    }


    static class ViewHolder {

        TextView lyricis_recommand_title; //노래 추천 제목
        TextView lyricis_recommand_artistc; //노래 추천 가수

    }
}
