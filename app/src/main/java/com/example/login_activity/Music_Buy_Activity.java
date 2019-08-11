package com.example.login_activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Music_Buy_Activity extends AppCompatActivity {

    private ListView Music_Buy_listview;
    private ArrayList<item_music_buy> Music_buy_arrayList;
    adapter_Music_buy adapter_Music_buy;
    public String Music_list_response;
    private JSONArray jsonArray;
    private String music_buy_title,music_buy_price;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_music__buy_);


        Music_Buy_listview=(ListView)findViewById(R.id.Music_by_listview); //vod를 불러오는 리스트 뷰
        Music_buy_arrayList = new ArrayList<item_music_buy>(); //리스트뷰 안에 들어 있는 아이템들
        adapter_Music_buy = new adapter_Music_buy(getApplicationContext(),Music_buy_arrayList);
        Music_Buy_listview.setAdapter(adapter_Music_buy);


        adapter_Music_buy.notifyDataSetChanged();
        music_buy_list_load();

        Music_Buy_listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent test_intent = new Intent(getApplicationContext(),test_activity.class);
                String music_name = Music_buy_arrayList.get(i).get_Music_buy_title();
                test_intent.putExtra("music_name",music_name);
                startActivity(test_intent);



            }
        });


    }

    private void music_buy_list_load() {
        StringRequest request = new StringRequest(Request.Method.POST, "http://18.225.37.73/Music_buy_list_load.php", new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
//해당 데이터를 받아온다 1일시에는 로그인성공 0일시에는 로그인실패
                Log.e("db_response",response);
                Music_list_response=response;
                Music_buy_list_load();



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

    private void Music_buy_list_load() {
        try {
            JSONObject jsonObject = new JSONObject(Music_list_response);
            jsonArray = jsonObject.getJSONArray("result");
            Log.e("Vod_json_array", String.valueOf(jsonArray));


            for(int i=0;i<jsonArray.length();i++){

                JSONObject item = jsonArray.getJSONObject(i);
                Log.e("jsonArray", String.valueOf(item));


                music_buy_title = item.getString("music_title");
                music_buy_price = item.getString("music_price");



                Music_buy_arrayList.add(new item_music_buy(music_buy_title,music_buy_price));


                adapter_Music_buy.notifyDataSetChanged();
            }



        } catch (JSONException e) {


        }


    }

}
