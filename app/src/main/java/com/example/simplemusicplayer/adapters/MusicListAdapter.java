package com.example.simplemusicplayer.adapters;


import android.content.Context;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.itemclass.Model;
import com.example.login_activity.R;
import com.example.simplemusicplayer.entity.Mp3Info;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MusicListAdapter extends BaseAdapter {
    private List<Mp3Info> musicList;
    private LayoutInflater layoutInflater;
    private String music_check;
    private String Music_artist;
    String music_title;
    public static ArrayList<Model> modelArrayList;

    Context context;
    ArrayList<String> selectedStrings = new ArrayList<String>();

    public MusicListAdapter(List<Mp3Info> musicList,LayoutInflater layoutInflater,Context context) {
        this.musicList = musicList;
        this.layoutInflater = layoutInflater;
        this.context=context;
    }

    @Override
    public int getCount() {
        return musicList.size();
    }

    @Override
    public Object getItem(int position) {
        return musicList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {


        View v = layoutInflater.inflate(R.layout.list_item,null);
        ImageView ivMusicImage = v.findViewById(R.id.iv_music_img);
        TextView tvMusicName = v.findViewById(R.id.tv_music_name);  //노래 제목을 받아옴
        TextView tvMusicAuthor = v.findViewById(R.id.tv_music_author); //작가 이름을 받아옴
//       ImageView Love_Music = v.findViewById(R.id.iv_play);
        ToggleButton Love_Music = v.findViewById(R.id.iv_play);

        //        ImageView Love_Music_on = v.findViewById(R.id.iv_play2);


        String name = musicList.get(position).getDisplayName();
        if (name==null||name.length()==0){
            musicList.get(position).setDisplayName("알수없음");
        }
        String author = musicList.get(position).getArtist();
        if (author.equals("<unknown>"))
            musicList.get(position).setArtist("알수없음");

        Love_Music.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                music_check = String.valueOf(Love_Music.isChecked());
                Log.e("123", String.valueOf(Love_Music.isChecked()));
                //클릭되어있는 상태인지 상태를 받아온다




                if(music_check.equals("true")){
                    //만약 즐겨찾기 버튼이 활성화 되어 있으면


                    Music_artist = musicList.get(position).getArtist();
                    Log.e("1234",musicList.get(position).getArtist());
                    //해당 리스트의 아티스트의 이름을 받아온다

                    music_title=musicList.get(position).getDisplayName();
                    Log.e("1234",musicList.get(position).getDisplayName());
                    //해당 리스트의 제목을 받아온다.



                    click();

                }


            }
        });

        Love_Music.setFocusable(false);

        tvMusicAuthor.setText(musicList.get(position).getArtist());
        tvMusicName.setText(musicList.get(position).getDisplayName());

        return v;
    }

    private void click() {
        StringRequest request = new StringRequest(Request.Method.POST, "http://18.225.37.73/bookmark.php", new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
//해당 데이터를 받아온다 1일시에는 로그인성공 0일시에는 로그인실패
                Log.e("db_response",response);




            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String, String> params = new HashMap<>();
                params.put("Music_artist",Music_artist);
                params.put("Music_title",music_title);
                params.put("Music_img","123");
                return params;
            }
        };

        Volley.newRequestQueue(context).add(request);
    }
}
