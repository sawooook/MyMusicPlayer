package com.example.adapter;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.login_activity.R;
import com.example.login_activity.adapter_buy_point;
import com.example.login_activity.item_buy_point;

import java.util.ArrayList;

public class Mnet_adapter extends BaseAdapter {

    ArrayList<com.example.itemclass.Mnent_item> Mnet_music_arraylist;
    Context applicationContext;


    public Mnet_adapter(Context applicationContext, ArrayList<com.example.itemclass.Mnent_item> Mnet_music_arraylist) {
        this.Mnet_music_arraylist = Mnet_music_arraylist;
        this.applicationContext = applicationContext;
    }

    @Override
    public int getCount() {
        return Mnet_music_arraylist.size();
    }

    @Override
    public Object getItem(int i) {
        return Mnet_music_arraylist.get(i);
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
            view = inflater.inflate(R.layout.list_mnet, null); //브로드캐스터채팅리스트를 인플레이트 시킨다


            holder.mnent_music_title = view.findViewById(R.id.mnent_music_title); //
            holder.mnent_music_link = view.findViewById(R.id.mnent_music_link); //
            holder.mnent_music_artist = view.findViewById(R.id.mnent_music_artist); //
            holder.mnet_music_img = view.findViewById(R.id.mnent_image_view); //
            holder.mnent_music_rank = view.findViewById(R.id.mnent_music_ranking); //

            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }
        holder.mnent_music_title.setText(Mnet_music_arraylist.get(i).get_mnent_music_title());
        holder.mnent_music_link.setText(Mnet_music_arraylist.get(i).get_mnent_music_link());
        holder.mnent_music_artist.setText(Mnet_music_arraylist.get(i).get_mnent_music_artist());
        Glide.with(applicationContext).load(Mnet_music_arraylist.get(i).get_mnent_music_img()).into(holder.mnet_music_img);
//        holder.mnet_music_img.setImageURI(Uri.parse(Mnet_music_arraylist.get(i).get_mnent_music_img()));
        holder.mnent_music_rank.setText(Mnet_music_arraylist.get(i).get_mnent_music_rank());

        return view;

    }



    static class ViewHolder {
        TextView mnent_music_title;
        TextView mnent_music_link;
        TextView mnent_music_artist;
        ImageView mnet_music_img;
        TextView mnent_music_rank;

    }
}