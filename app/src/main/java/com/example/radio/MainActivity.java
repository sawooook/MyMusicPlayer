package com.example.radio;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;


import com.example.login_activity.R;
import com.example.radio.player.PlaybackStatus;
import com.example.radio.player.RadioManager;
import com.example.radio.util.Shoutcast;
import com.example.radio.util.ShoutcastHelper;
import com.example.radio.util.ShoutcastListAdapter;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnItemClick;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.playTrigger)
    ImageButton trigger;

    @BindView(R.id.listview)
    ListView listView;

    @BindView(R.id.name)
    TextView textView;

    @BindView(R.id.sub_player)
    View subPlayer;

    RadioManager radioManager;

    String streamURL;
    private String rtmpplayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main_radio);

        ButterKnife.bind(this);

        setSupportActionBar(toolbar);

        radioManager = RadioManager.with(this);

        listView.setAdapter(new ShoutcastListAdapter(this, ShoutcastHelper.retrieveShoutcasts(this)));
    }

    @Override
    public void onStart() {

        super.onStart();

        EventBus.getDefault().register(this);
    }

    @Override
    public void onStop() {

        EventBus.getDefault().unregister(this);

        super.onStop();
    }

    @Override
    protected void onDestroy() {

        radioManager.unbind();

        super.onDestroy();
    }

    @Override
    protected void onResume() {
        super.onResume();
        radioManager.bind();
    }

    @Override
    public void onBackPressed() {

        finish();
    }

    @Subscribe
    public void onEvent(String status){

        switch (status){

            case PlaybackStatus.LOADING:

                // loading

                break;

            case PlaybackStatus.ERROR:

                Toast.makeText(this, R.string.no_stream, Toast.LENGTH_SHORT).show();

                break;

        }

        trigger.setImageResource(status.equals(PlaybackStatus.PLAYING)
                ? R.drawable.ic_pause_black
                : R.drawable.ic_play_arrow_black);

    }

//    @OnClick(R.id.playTrigger)
//    public void onClicked(){
//
//        if(TextUtils.isEmpty(streamURL)) return;
//
//        radioManager.playOrPause(streamURL,rtmpplayer);
//    }

    @OnItemClick(R.id.listview)
    public void onItemClick(AdapterView<?> parent, View view, int position, long id){

        Shoutcast shoutcast = (Shoutcast) parent.getItemAtPosition(position);

        if(shoutcast == null){

            return;
        }

        textView.setText(shoutcast.getName());
//        subPlayer.setVisibility(View.VISIBLE);

        Log.e("whatisPlayer:",shoutcast.getplayer());
        streamURL = shoutcast.getUrl();

        rtmpplayer=shoutcast.getplayer();
        //플레이어의 이름을 얻어옴
//        radioManager.playOrPause(streamURL,rtmpplayer);


        Intent Mymusic_intent = new Intent(getApplicationContext(), MyMusic_Activity.class);
        Mymusic_intent.putExtra("radio_title_name",shoutcast.getName());
        Mymusic_intent.putExtra("stramURL",streamURL);
        Mymusic_intent.putExtra("rtmpplayer",rtmpplayer);

        startActivity(Mymusic_intent);


    }
}
