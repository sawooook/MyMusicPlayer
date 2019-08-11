package com.example.login_activity;

import android.content.Context;
import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.music_lyricis.Music_lyrics_recommandation;

public class MainActivity extends AppCompatActivity {

    private Context mContext;
    private TabLayout mTabLayout;
    private ViewPager mViewPager;
    private com.example.adapter.ContentsPagerAdapter mContentPagerAdapte;
    LinearLayout Music_recommand;

    LinearLayout Mypage_Activity,My_Music_Player,Main_Activity_Home;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main1);
        mContext = getApplicationContext();

        Music_recommand=(LinearLayout)findViewById(R.id.Chat_Activity); //음악추천버튼
        mTabLayout = (TabLayout) findViewById(R.id.layout_tab);
        mTabLayout.addTab(mTabLayout.newTab().setCustomView(createTabView("엠넷")));
        mTabLayout.addTab(mTabLayout.newTab().setCustomView(createTabView("지니뮤직")));
        mTabLayout.setTabGravity(TabLayout.GRAVITY_FILL);


        Main_Activity_Home =(LinearLayout)findViewById(R.id.Main_Activity_Home);
        Mypage_Activity= (LinearLayout)findViewById(R.id.Mypage_Activity);
        My_Music_Player=(LinearLayout)findViewById(R.id.Add_Activity);



        //버튼을 클릭시 마이페이지로 이동을함
        Mypage_Activity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent Mypage_intent =new Intent(getApplicationContext(), MyPage_Activity.class);
                startActivity(Mypage_intent);


            }
        });


        //음악추천 하는 홈페이지로 넘어감
        Music_recommand.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent Music_recommand =new Intent(getApplicationContext(), Music_lyrics_recommandation.class);
                startActivity(Music_recommand);

            }
        });



        My_Music_Player.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                Intent My_Music_intent = new Intent(getApplicationContext(),com.example.simplemusicplayer.activities.WelcomeActivity.class);
//                Intent My_Music_intent = new Intent(getApplicationContext(),com.example.simplemusicplayer.activities.WelcomeActivity.class);
                startActivity(My_Music_intent);


            }
        });


        mViewPager = (ViewPager) findViewById(R.id.pager_content);

        mContentPagerAdapte = new com.example.adapter.ContentsPagerAdapter(

                getSupportFragmentManager(), mTabLayout.getTabCount());

        mViewPager.setAdapter(mContentPagerAdapte);



        mViewPager.addOnPageChangeListener(

                new TabLayout.TabLayoutOnPageChangeListener(mTabLayout));

        mTabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {

            @Override

            public void onTabSelected(TabLayout.Tab tab) {
                mViewPager.setCurrentItem(tab.getPosition());

            }



            @Override

            public void onTabUnselected(TabLayout.Tab tab) {



            }



            @Override

            public void onTabReselected(TabLayout.Tab tab) {



            }

        });

    }

    private View createTabView(String tabName) {
        View tabView = LayoutInflater.from(mContext).inflate(R.layout.custom_tab, null);
        TextView txt_name = (TextView) tabView.findViewById(R.id.txt_name);
        txt_name.setText(tabName);

        return tabView;

    }
}

