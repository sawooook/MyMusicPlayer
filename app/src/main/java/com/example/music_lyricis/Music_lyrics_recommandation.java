package com.example.music_lyricis;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
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
import com.example.itemclass.lyricis_item;
import com.example.login_activity.R;
import com.example.login_activity.adapter_Music_buy;
import com.example.login_activity.item_buy_point;
import com.example.login_activity.item_music_buy;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Music_lyrics_recommandation extends AppCompatActivity {

    ListView music_recommand_lyricis;
    private ArrayList<lyricis_item> Music_lyrics_recommandation_arrayList;
    lyricis_adapter adapter_lyricis;
    public String music_title; //음악 제목을 가져옴

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_music_lyrics_recommandation);
        init();
        Music_lyrics_recommandation_arrayList.add(new lyricis_item("노래방에서","장범준"));
        Music_lyrics_recommandation_arrayList.add(new lyricis_item("다시 나 그대를","멜로망스"));
        Music_lyrics_recommandation_arrayList.add(new lyricis_item("사월이 지나면 우리 헤어져요","첸"));
        Music_lyrics_recommandation_arrayList.add(new lyricis_item("서울 밤","윤민수 & 장혜진"));
        Music_lyrics_recommandation_arrayList.add(new lyricis_item("이뻐이뻐","크레파스"));
        Music_lyrics_recommandation_arrayList.add(new lyricis_item("열대야","여자친구"));
        Music_lyrics_recommandation_arrayList.add(new lyricis_item("움직여","SIXC"));
        Music_lyrics_recommandation_arrayList.add(new lyricis_item("질풍가도","쾌걸근육몬 OST"));
        Music_lyrics_recommandation_arrayList.add(new lyricis_item("헤어져줘서 고마워","벤"));
        Music_lyrics_recommandation_arrayList.add(new lyricis_item("FANCY","트와이스"));
        Music_lyrics_recommandation_arrayList.add(new lyricis_item("Love Shot","엑소"));
        Music_lyrics_recommandation_arrayList.add(new lyricis_item("My Love","양다일"));
        Music_lyrics_recommandation_arrayList.add(new lyricis_item("Snapping","청하"));


        adapter_lyricis.notifyDataSetChanged();


        music_recommand_lyricis.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {


                music_title =Music_lyrics_recommandation_arrayList.get(i).get_lyricis_title();
                adapter_lyricis.getCount();

                Music_Recommandation();
                CheckTypesTask task = new CheckTypesTask();
                task.execute();


            }
        });

    }
    private class CheckTypesTask extends AsyncTask<Void, Void, Void> {

        ProgressDialog Loading_dialog = new ProgressDialog(Music_lyrics_recommandation.this);

        @Override
        protected void onPreExecute() {
            Loading_dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            Loading_dialog.setMessage("가사를 분석중 입니다...");
            Loading_dialog.show();
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... arg0) {
            try {
                    //asyncDialog.setProgress(i * 30);
                    Thread.sleep(10000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            Loading_dialog.dismiss();
            super.onPostExecute(result);
            Intent recommand_lyricis_intent = new Intent(getApplicationContext(),recommanded_Activity.class);
            startActivity(recommand_lyricis_intent);
            //추천노래를 보여주는 화면으로 이동한다.

        }
    }




    private void Music_Recommandation() {
        //서버를 통해 db에 노래 제목을 저장을 함
        StringRequest request = new StringRequest(Request.Method.POST, "http://13.58.19.177/lyrics_recommand.php", new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
//해당 데이터를 받아온다 1일시에는 로그인성공 0일시에는 로그인실패
                Log.e("whatresponese",response);


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String, String> params = new HashMap<>();
                params.put("music_title",music_title);
                Log.e("musictitle?:",music_title);
                //노래의 타이틀을 db에저장함


                return params;
            }
        };

        Volley.newRequestQueue(getApplicationContext()).add(request);

    }

    private void init() {
        music_recommand_lyricis=(ListView)findViewById(R.id.music_recommand_lyricis); //리스트뷰 선언
        Music_lyrics_recommandation_arrayList = new ArrayList<lyricis_item>(); // 어레이리스트 선언
        adapter_lyricis= new lyricis_adapter(getApplicationContext(),Music_lyrics_recommandation_arrayList);
        music_recommand_lyricis.setAdapter(adapter_lyricis); //어뎁터와 리스트뷰 연결


    }

}
