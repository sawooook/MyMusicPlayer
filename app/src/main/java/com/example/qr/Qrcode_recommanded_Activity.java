package com.example.qr;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.adapter.qr_adapter;
import com.example.adapter.recommand_adapter;
import com.example.itemclass.qr_item;
import com.example.itemclass.recommand_item;
import com.example.login_activity.MyPage_Activity;
import com.example.login_activity.R;
import com.example.music_lyricis.Music_detail_information_Activity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Qrcode_recommanded_Activity extends AppCompatActivity {


    ListView qrcode_recommanded_listview;
    private ArrayList<qr_item> qrcode_arrayList;
    qr_adapter qr_adapter;
    String qrcode_result_data;
    public String wo;
    String get_tile;
    String[] Split_music_name;
    private String respone_load_music;
    private JSONArray jsonArray;
    private String load_music_title;
    private long pressedTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qrcode_recommanded_);
        init_qrcode();

        load_Music();

        qr_adapter.notifyDataSetChanged();


        qrcode_recommanded_listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                get_tile =qrcode_arrayList.get(i).get_qr_title();
                Intent detatil_intent = new Intent(getApplicationContext(), Music_detail_information_Activity.class);
                detatil_intent.putExtra("music_title",get_tile);


                startActivity(detatil_intent);


            }
        });


    }

    private void init_qrcode() {
        qrcode_recommanded_listview=(ListView)findViewById(R.id.qrcode_recommanded_listview);
        qrcode_arrayList=new ArrayList<qr_item>();
        qr_adapter= new qr_adapter(getApplicationContext(),qrcode_arrayList);
        qrcode_recommanded_listview.setAdapter(qr_adapter); //어뎁터와 리스트뷰 연결


    }



    //qr코드에 저장된 음악들을 불러온다
    private void load_Music() {
        StringRequest request = new StringRequest(Request.Method.POST, "http://18.225.37.73/bookmark_delete.php", new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
//해당 데이터를 받아온다 1일시에는 로그인성공 0일시에는 로그인실패
                Log.e("bookmark_response",response);
                respone_load_music=response;
                load_Music_json();




            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String, String> params = new HashMap<>();




                return params;
            }
        };

        Volley.newRequestQueue(getApplicationContext()).add(request);

    }

    private void load_Music_json() {
        try {
            JSONObject jsonObject = new JSONObject(respone_load_music);
            jsonArray = jsonObject.getJSONArray("result");
            Log.e("load_josn", String.valueOf(jsonArray));

            for(int i=0;i<jsonArray.length();i++){
                JSONObject item = jsonArray.getJSONObject(i);
                Log.e("jsonArray", String.valueOf(item));
                load_music_title = item.getString("book_title");
                qrcode_arrayList.add(new qr_item(load_music_title,"1"));

            }

            qr_adapter.notifyDataSetChanged();




        } catch (JSONException e) {

        }





    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        delete_data();
    }

    private void delete_data() {
        StringRequest request = new StringRequest(Request.Method.POST, "http://18.225.37.73/delete_db.php", new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
//해당 데이터를 받아온다 1일시에는 로그인성공 0일시에는 로그인실패
                Log.e("delete_response",response);


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String, String> params = new HashMap<>();


                return params;
            }
        };

        Volley.newRequestQueue(getApplicationContext()).add(request);
    }


}
