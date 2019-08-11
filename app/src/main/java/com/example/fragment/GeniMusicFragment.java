package com.example.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.itemclass.Geni_item;
import com.example.login_activity.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class GeniMusicFragment extends Fragment {

    public ListView geni_listview;
    com.example.adapter.geni_adapter geni_Music_adapter;
    ArrayList<Geni_item> geni_music_arraylist;
    private String geni_music_response;
    private JSONArray jsonArray;
    private String geni_music_rank,geni_music_title,geni_music_artist,geni_music_link,geni_music_cover;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.tab_genimusic, container, false);
        geni_listview=(ListView)v.findViewById(R.id.genemusic_listview);  //리스트뷰 초기화

        geni_music_arraylist = new ArrayList<Geni_item>(); //리스트뷰 안에 들어 있는 아이템들
        geni_Music_adapter = new  com.example.adapter.geni_adapter(getActivity(),geni_music_arraylist);
        geni_listview.setAdapter(geni_Music_adapter);
        geni_Music_adapter.notifyDataSetChanged();

        geni_music_list_load();



        return v;
    }


    //크롤링한 엠넷의 데이터들을 불러온다
    private void geni_music_list_load() {

        StringRequest request = new StringRequest(Request.Method.POST, "http://18.222.216.223/geni_music.php", new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
//해당 데이터를 받아온다 1일시에는 로그인성공 0일시에는 로그인실패
                Log.e("db_response",response);
                geni_music_response=response;
                geni_music_parsing();



            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Log.e("???",">>>");
                Map<String, String> params = new HashMap<>();


                return params;
            }
        };

        Volley.newRequestQueue(getActivity()).add(request);
    }




    private void geni_music_parsing() {
        try {
            JSONObject jsonObject = new JSONObject(geni_music_response);
            jsonArray = jsonObject.getJSONArray("result");
            Log.e("Vod_json_array", String.valueOf(jsonArray));

            geni_music_arraylist.add(new com.example.itemclass.Geni_item("그대라는 시","/album/3286779","태연 (TAEYEON)","1위","http://image.genie.co.kr/Y/IMAGE/IMG_ALBUM/081/246/905/81246905_1563523038613_1_140x140.JPG"));

            for(int i=0;i<jsonArray.length();i++){

                JSONObject item = jsonArray.getJSONObject(i);
                Log.e("jsonArray", String.valueOf(item));


                geni_music_rank = item.getString("rank");
                geni_music_title = item.getString("title");
                geni_music_artist = item.getString("artist");
//                geni_music_link = item.getString("link");
                geni_music_cover = item.getString("cover");
                geni_music_arraylist.add(new com.example.itemclass.Geni_item(geni_music_title,"1",geni_music_artist,geni_music_rank,geni_music_cover));

                geni_Music_adapter.notifyDataSetChanged();

            }



        } catch (JSONException e) {


        }


    }
}
