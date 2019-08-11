package com.example.radio;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.login_activity.R;
import com.example.radio.player.PlaybackStatus;
import com.example.radio.player.RadioManager;

import org.greenrobot.eventbus.Subscribe;

import butterknife.BindView;

public class MyMusic_Activity extends AppCompatActivity {
    RadioManager radioManager;
    TextView radio_title;
    ImageButton playTrigger_button;
    LinearLayout sub_player2;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_music_radio);

        sub_player2 =(LinearLayout)findViewById(R.id.sub_player2);
//        playTrigger_button=(ImageButton)findViewById(R.id.playTrigger_button);
        radioManager = RadioManager.with(this);
        radio_title = (TextView)findViewById(R.id.radio_name);
        Intent redio_gettitle_intent = getIntent();

        String radio_title_name=redio_gettitle_intent.getStringExtra("radio_title_name");
        //라디오 타이틀의 이름을 가져옴
        String radio_streamURL=redio_gettitle_intent.getStringExtra("stramURL");
        //인터넷 방송의 주소를 가죠옴
        String radio_player=redio_gettitle_intent.getStringExtra("rtmpplayer");
        //어떤플레이어를 쓰는지 가져옴
        radio_title.setText(radio_title_name);

        radioManager.playOrPause(radio_streamURL,radio_player);
        sub_player2.setVisibility(View.VISIBLE);


    }
}
