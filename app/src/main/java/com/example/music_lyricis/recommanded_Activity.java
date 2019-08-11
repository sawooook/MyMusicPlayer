package com.example.music_lyricis;

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
import com.example.adapter.lyricis_adapter;
import com.example.adapter.recommand_adapter;
import com.example.itemclass.lyricis_item;
import com.example.itemclass.recommand_item;
import com.example.login_activity.R;
import com.example.login_activity.adapter_Music_buy;
import com.example.login_activity.item_music_buy;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class recommanded_Activity extends AppCompatActivity {
    ListView recommanded_music;
    private ArrayList<recommand_item> recommandation_arrayList;
    recommand_adapter adapter_recommanded;
    private String recommanded_music_response;
    private JSONArray jsonArray;
    private String load_music_name;
    private String load_music_index;
    String select_music_title;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recommanded_);
        init();
        recommanded_music_load(); //db로부터 추천된 노래를 불러옴
        adapter_recommanded.notifyDataSetChanged();


        //노래의 제목을 전송한다
        recommanded_music.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {


                select_music_title=recommandation_arrayList.get(i).get_recommand_title();

                //노래상세정보를 볼수 있는 페이지로 이동한다
                //노래의 제목을 전송시킴
                Intent music_detailed_information_intent = new Intent(getApplicationContext(),Music_detail_information_Activity.class);
                music_detailed_information_intent.putExtra("music_title",select_music_title);
                startActivity(music_detailed_information_intent);






            }
        });


    }

    private void recommanded_music_load() {
        StringRequest request = new StringRequest(Request.Method.POST, "http://13.58.19.177/recommanded_load.php", new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
//해당 데이터를 받아온다 1일시에는 로그인성공 0일시에는 로그인실패
                Log.e("db_response",response);
                recommanded_music_response = response;
                load_json_music(recommanded_music_response);


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

    private void load_json_music(String recommanded_music_response) {
        try {
            JSONObject jsonObject = new JSONObject(recommanded_music_response);
            jsonArray = jsonObject.getJSONArray("result");
            Log.e("load_josn", String.valueOf(jsonArray));

            for(int i=0;i<jsonArray.length();i++){
                JSONObject item = jsonArray.getJSONObject(i);
                Log.e("jsonArray", String.valueOf(item));
                load_music_name = item.getString("music_name");
                load_music_index= item.getString("idd");
                recommandation_arrayList.add(new recommand_item(load_music_name,load_music_index));
                adapter_recommanded.notifyDataSetChanged();
            }



        } catch (JSONException e) {

        }


    }


    private void init() {
        recommanded_music=(ListView)findViewById(R.id.recommanded_music); //리스트뷰 선언
        recommandation_arrayList = new ArrayList<recommand_item>(); // 어레이리스트 선언
        adapter_recommanded= new recommand_adapter(getApplicationContext(),recommandation_arrayList);
        recommanded_music.setAdapter(adapter_recommanded); //어뎁터와 리스트뷰 연결


    }
}
