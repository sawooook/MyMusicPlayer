package com.example.simplemusicplayer.activities;

import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.util.Base64;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.ToggleButton;


import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpResponse;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.itemclass.qr_item;
import com.example.itemclass.recommand_item;
import com.example.login_activity.R;
import com.example.music_lyricis.Music_detail_information_Activity;
import com.example.simplemusicplayer.adapters.MusicListAdapter;
import com.example.simplemusicplayer.entity.Mp3Info;
import com.example.simplemusicplayer.helpers.MediaPlayerHelper;
import com.example.simplemusicplayer.util.mp3.MediaUtil;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.common.BitMatrix;
import com.journeyapps.barcodescanner.BarcodeEncoder;
import com.kakao.kakaolink.v2.KakaoLinkResponse;
import com.kakao.kakaolink.v2.KakaoLinkService;
import com.kakao.message.template.ContentObject;
import com.kakao.message.template.FeedTemplate;
import com.kakao.message.template.LinkObject;
import com.kakao.network.ErrorResult;
import com.kakao.network.callback.ResponseCallback;
import com.kakao.util.helper.log.Logger;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MusicListActivity extends BaseActivity {

    public int pos;
    private ListView lvMusicList;
    private MusicListAdapter musicListAdapter;
    Button music_share;

    final static String dbName = "music.db"; //db이름름
    final static int dbVersion = 2;
    String sql;
    SQLiteDatabase db;
    private ToggleButton iv_play;
    private String respone_load_music;
    private JSONArray jsonArray;
    private ArrayList loadmusic_Arraylist;
    private ArrayList<String> selectedStrings;
//    DBHelper dbHelper;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_music_list);
        initView();
        lvMusicList.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);


//        dbHelper = new DBHelper(this, dbName, null, dbVersion);
//
//
//        db = dbHelper.getWritableDatabase();
//        sql = String.format("INSERT INTO t3 VALUES('" + "123" + "','" + "345" + "'," + "4543" + ",'" + "123" + "',0);");
//        db.execSQL(sql);


        //음악을 공유하겠다는 버튼
        music_share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
             kakaolink();

                lvMusicList.getCheckedItemPosition();


//
            }


        });


    }


    private void initView() {
        initNavBar(false, "재생목록", true);
        music_share = (Button) findViewById(R.id.music_share);

        OnImportMusicListener onImportMusicListener = new OnImportMusicListener() {

            @Override
            public void onImport(final List<Mp3Info> mp3Infos) {
                Toast.makeText(getApplicationContext(), "뮤직플레이어!", Toast.LENGTH_SHORT).show();
                lvMusicList = fd(R.id.lv_music);
                musicListAdapter = new MusicListAdapter(mp3Infos, getLayoutInflater(), getApplicationContext());
                lvMusicList.setAdapter(musicListAdapter);
                MediaPlayerHelper.getInstance(getApplicationContext()).setMp3Infos(mp3Infos);

                //노래 리스트뷰
                lvMusicList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {


                        Intent intent = new Intent(MusicListActivity.this, com.example.simplemusicplayer.activities.MainActivity.class);
                        Mp3Info mp3Info = mp3Infos.get(position);
                        intent.putExtra("mp3", mp3Info);
                        intent.putExtra("index", position);
                        startActivity(intent);


                    }
                });
            }
        };
        super.setOnImportMusicListener(onImportMusicListener);

        List<Mp3Info> mp3Infos = MediaUtil.getMusicInfo(getApplicationContext());
        onImportMusicListener.onImport(mp3Infos);

    }

    private void kakaolink() {
        FeedTemplate params = FeedTemplate
                .newBuilder(ContentObject.newBuilder("친구에게 노래추천 ",
                        "http://18.225.37.73/qrcode.jpg",
                        LinkObject.newBuilder().setWebUrl("https://developers.kakao.com")
                                .setMobileWebUrl("https://developers.kakao.com").build())
                        .build()).build();

        Map<String, String> serverCallbackArgs = new HashMap<String, String>();
        serverCallbackArgs.put("user_id", "${current_user_id}");
        serverCallbackArgs.put("product_id", "${shared_product_id}");

        KakaoLinkService.getInstance().sendDefault(this, params, serverCallbackArgs, new ResponseCallback<KakaoLinkResponse>() {
            @Override
            public void onFailure(ErrorResult errorResult) {
                Logger.e(errorResult.toString());
            }

            @Override
            public void onSuccess(KakaoLinkResponse result) {
                // 템플릿 밸리데이션과 쿼터 체크가 성공적으로 끝남. 톡에서 정상적으로 보내졌는지 보장은 할 수 없다. 전송 성공 유무는 서버콜백 기능을 이용하여야 한다.
            }
        });
    }


}


