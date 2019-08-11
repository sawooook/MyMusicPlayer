package com.example.login_activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;

public class Buy_point_Activity extends AppCompatActivity {

    private ListView Buy_point_Listview;
    private ArrayList<item_buy_point> Buy_Point_arrayList;
    private adapter_buy_point buy_point_adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buy_point_);


        Buy_point_Listview=(ListView)findViewById(R.id.buy_point_listview); //vod를 불러오는 리스트 뷰
        Buy_Point_arrayList = new ArrayList<item_buy_point>(); //리스트뷰 안에 들어 있는 아이템들
        buy_point_adapter = new adapter_buy_point(getApplicationContext(),Buy_Point_arrayList);
        Buy_point_Listview.setAdapter(buy_point_adapter);


        Buy_Point_arrayList.add(new item_buy_point("1000원","1000"));
        Buy_Point_arrayList.add(new item_buy_point("2000원","2000"));
        Buy_Point_arrayList.add(new item_buy_point("3000원","3000"));

        buy_point_adapter.notifyDataSetChanged();


        Buy_point_Listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                //포인트를 담아서 카카오 액티비트로 보낸다
                String Buy_point_name = Buy_Point_arrayList.get(i).get_Buy_point_amount();
                Intent kakao_pay_intent = new Intent(getApplicationContext(),kakao_Activity.class);
                kakao_pay_intent.putExtra("Buy_point_name",Buy_point_name);
                Log.e("Buy_point_name123",Buy_point_name);
                startActivity(kakao_pay_intent);

            }
        });


    }
}
