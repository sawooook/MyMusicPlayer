package com.example.adapter;


import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.example.fragment.GeniMusicFragment;
import com.example.fragment.MnetMusicFragment;
import com.example.itemclass.Mnent_item;

import java.util.ArrayList;


public class ContentsPagerAdapter extends FragmentStatePagerAdapter {

    private int mPageCount;


    public ContentsPagerAdapter(FragmentManager fm, int pageCount) {
        super(fm);
        this.mPageCount = pageCount;
    }



    @Override

    public Fragment getItem(int position) {

        switch (position) {

            case 0:
                MnetMusicFragment MnetMusicFragment = new MnetMusicFragment();
                return MnetMusicFragment;


            case 1:
                GeniMusicFragment GeniMusicFragment = new GeniMusicFragment();
                return GeniMusicFragment;


            default:
                return null;

        }

    }



    @Override

    public int getCount() {

        return mPageCount;

    }

}