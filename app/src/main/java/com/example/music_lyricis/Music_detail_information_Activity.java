package com.example.music_lyricis;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.example.itemclass.recommand_item;
import com.example.login_activity.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class Music_detail_information_Activity extends AppCompatActivity {


    String detai_recommanded_music_response;
    String Get_select_music_title;
    String phpfilename;
    TextView recommanded_music_title,recommanded_music_artist,recommanded_music_content,recommanded_music_genre;
    ImageView recommanded_music_album;
    private JSONArray jsonArray;
    private String detai_music_name,detai_music_artist,detai_music_genre,detai_music_album,detai_music_content;
    private long pressedTime;
    ImageButton music_start_button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_music_detail_information_);
        init();


        load_music_title();


//        music_start_button

    }



    private void init() {
        Intent get_music_title = getIntent();
        Get_select_music_title=get_music_title.getStringExtra("music_title");
        music_start_button=(ImageButton)findViewById(R.id.music_start);
        recommanded_music_title=(TextView)findViewById(R.id.recommanded_music_title); //노래 제목
        recommanded_music_artist=(TextView)findViewById(R.id.recommanded_music_artist); //노래 아티스트
        recommanded_music_content=(TextView)findViewById(R.id.recommanded_music_content); //노래 가사
        recommanded_music_genre=(TextView)findViewById(R.id.recommanded_music_genre); // 노래 장르
        recommanded_music_album=(ImageView)findViewById(R.id.recommanded_music_album); //노래 이미지




    }

    private void load_music_title() {
        StringRequest request = new StringRequest(Request.Method.POST, "http://13.58.19.177/detail_music.php", new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
//해당 데이터를 받아온다 1일시에는 로그인성공 0일시에는 로그인실패
                Log.e("db_response",response);
                detai_recommanded_music_response = response;
                music_load_json_music(detai_recommanded_music_response);


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String, String> params = new HashMap<>();
                params.put("music_title",Get_select_music_title);


                return params;
            }
        };

        Volley.newRequestQueue(getApplicationContext()).add(request);
    }

    private void music_load_json_music(String detai_recommanded_music_response) {
        try {
            JSONObject jsonObject = new JSONObject(detai_recommanded_music_response);
            jsonArray = jsonObject.getJSONArray("result");
            Log.e("load_josn", String.valueOf(jsonArray));


            for(int i=0;i<jsonArray.length();i++){
                JSONObject item = jsonArray.getJSONObject(i);
                Log.e("jsonArray", String.valueOf(item));
                detai_music_name = item.getString("detai_music_name");
                detai_music_artist= item.getString("detai_music_artist");
                detai_music_genre= item.getString("detai_music_genre");
                detai_music_album= item.getString("detai_music_album");
                detai_music_content= item.getString("detai_music_content");


            }
            layout_text();



        } catch (JSONException e) {

        }


    }

    private void layout_text() {

        recommanded_music_title.setText(detai_music_name);
        recommanded_music_artist.setText(detai_music_artist);
        recommanded_music_content.setText(detai_music_content);
        recommanded_music_genre.setText(detai_music_genre);
        Glide.with(getApplicationContext()).load(detai_music_album).into(recommanded_music_album);

    }

}
