package com.example.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.itemclass.Geni_item;
import com.example.login_activity.R;

import java.util.ArrayList;

public class geni_adapter extends BaseAdapter {

    ArrayList<Geni_item> Geni_music_arraylist;
    Context applicationContext;


    public geni_adapter(Context applicationContext, ArrayList<Geni_item> Geni_music_arraylist) {
        this.Geni_music_arraylist = Geni_music_arraylist;
        this.applicationContext = applicationContext;
    }

    @Override
    public int getCount() {
        return Geni_music_arraylist.size();
    }

    @Override
    public Object getItem(int i) {
        return Geni_music_arraylist.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        geni_adapter.ViewHolder holder;

        if (view == null) {
            holder = new geni_adapter.ViewHolder();

            LayoutInflater inflater = (LayoutInflater) applicationContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.list_geni, null); //브로드캐스터채팅리스트를 인플레이트 시킨다


            holder.geni_music_title = view.findViewById(R.id.geni_music_title); //
            holder.geni_music_link = view.findViewById(R.id.geni_music_link); //
            holder.geni_music_artist = view.findViewById(R.id.geni_music_artist); //
            holder.geni_music_img = view.findViewById(R.id.geni_image_view);
            holder.geni_music_rank = view.findViewById(R.id.geni_music_ranking); //

            view.setTag(holder);
        } else {
            holder = (geni_adapter.ViewHolder) view.getTag();
        }
        holder.geni_music_title.setText(Geni_music_arraylist.get(i).get_geni_music_title());
        holder.geni_music_link.setText(Geni_music_arraylist.get(i).get_geni_music_link());
        holder.geni_music_artist.setText(Geni_music_arraylist.get(i).get_geni_music_artist());
        Glide.with(applicationContext).load(Geni_music_arraylist.get(i).get_geni_music_img()).into(holder.geni_music_img);
//        holder.geni_music_img.setImageURI(Uri.parse(geni_music_arraylist.get(i).get_geni_music_img()));
        holder.geni_music_rank.setText(Geni_music_arraylist.get(i).get_geni_music_rank());

        return view;

    }


    static class ViewHolder {
        TextView geni_music_title;
        TextView geni_music_link;
        TextView geni_music_artist;
        ImageView geni_music_img;
        TextView geni_music_rank;

    }
}