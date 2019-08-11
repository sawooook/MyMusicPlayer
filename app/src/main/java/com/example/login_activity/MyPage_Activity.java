package com.example.login_activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.databinding.DataBindingUtil;
import android.graphics.Point;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.drawar.DrawAR;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class MyPage_Activity extends AppCompatActivity {
    String My_point;
    Button Mypage_kakaopay_btn,music_buy_btn;
    TextView Mypage_Login_id, Mypage_point;
    SharedPreferences Login_ic_shared;
    String MyPageLogin_id;
    LinearLayout Mymusic_activity;
    Button music_draw,radio_btn,qrcode_scan_button;

    private IntentIntegrator qrScan;
    private IntentResult result;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_page_);

        Mymusic_activity=(LinearLayout)findViewById(R.id.Mymusic_activity);
        Mypage_kakaopay_btn=(Button)findViewById(R.id.Mpage_kakaopay_btn);
        Mypage_point=(TextView)findViewById(R.id.Mypage_point);
        Mypage_Login_id=(TextView)findViewById(R.id.Mypage_Login_id);
        music_buy_btn=(Button)findViewById(R.id.music_buy_btn);
        Login_ic_shared = getSharedPreferences("Login_id", MODE_PRIVATE);
        MyPageLogin_id = Login_ic_shared.getString("Login_id", "??");
        Mypage_Login_id.setText(MyPageLogin_id);
        music_draw=(Button)findViewById(R.id.music_draw); //음악을 들으면서 그림을 그릴수 있는 액티비티로 이동
        radio_btn=(Button)findViewById(R.id.radio_btn); //라디오를 들을수있는 액티비티로 이동함
        qrcode_scan_button=(Button)findViewById(R.id.qrcode_scan_button);


        My_Point(); //내가 현재 갖고 있는 포인트를 불러옴

        music_draw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent music_draw_intent = new Intent(getApplicationContext(), DrawAR.class);
                startActivity(music_draw_intent);
            }
        });

        radio_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent radio_intent = new Intent(getApplicationContext(),com.example.radio.MainActivity.class);
                startActivity(radio_intent);

            }
        });

        qrcode_scan_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent qrcode_intent = new Intent(getApplicationContext(),com.example.qr.Qrcode_scan_Activity.class);
                startActivity(qrcode_intent);


            }
        });

        Mypage_kakaopay_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent kakao_pay = new Intent(getApplicationContext(),Buy_point_Activity.class);
                startActivity(kakao_pay);


            }
        });

        Mymusic_activity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent Mymusic_activity = new Intent(getApplicationContext(),MainActivity.class);
                startActivity(Mymusic_activity);
            }
        });

        music_buy_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent music_buy_intent = new Intent(getApplicationContext(),Music_Buy_Activity.class);
                startActivity(music_buy_intent);
            }
        });

    }


    private void My_Point(){
        StringRequest request = new StringRequest(Request.Method.POST, "http://18.225.37.73/MyMusic_point.php", new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
//해당 데이터를 받아온다 1일시에는 로그인성공 0일시에는 로그인실패
                Log.e("db_response",response);
                 My_point = response;
                Log.e("testLog2","testLo2g");

                Mypage_point.setText(My_point);




            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String, String> params = new HashMap<>();
                params.put("Login_id",MyPageLogin_id); //Login_id를 전송함

                Log.e("testLog","testLog");


                return params;
            }
        };

        Volley.newRequestQueue(getApplicationContext()).add(request);
    }





}
