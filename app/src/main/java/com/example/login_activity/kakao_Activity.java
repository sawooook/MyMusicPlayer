package com.example.login_activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

public class kakao_Activity extends AppCompatActivity {
    private WebView mainWebView;
    private final String APP_SCHEME = "iamportkakao://";
    private SharedPreferences kakaopay_count,Login_ic_shared;

    String URL = "http://18.225.37.73/kakaopay.php";
    int kakopay_num;
    String buy_point;
    private String MyPageLogin_id;
    private Handler handler;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kakao_);

        Intent get_buy_point = getIntent();
        buy_point=get_buy_point.getStringExtra("Buy_point_name");
        Log.e("what_what_buy_point", String.valueOf(buy_point));

        Login_ic_shared = getSharedPreferences("Login_id", MODE_PRIVATE);
        MyPageLogin_id = Login_ic_shared.getString("Login_id", "??");

        String pay_url = "http://18.225.37.73/kakaopay.php?point_name="+buy_point;



        mainWebView = (WebView) findViewById(R.id.mainWebView);
        //웹뷰 선언
        mainWebView.setWebViewClient(new KakaoWebViewClient(this));
        //카카오 웹뷰를 보여주겠다
        WebSettings settings = mainWebView.getSettings();
        settings.setJavaScriptEnabled(true);
        //자바스크립트 허용
//        mainWebView.loadUrl("http://18.225.37.73/kakaolink.php");


        mainWebView.loadUrl(pay_url);

        //get으로 데이터를 보내서 받을것임




        kakaopay_count = getSharedPreferences("pay", MODE_PRIVATE);

        SharedPreferences.Editor editor = kakaopay_count.edit();
        editor.putInt("pay", 0);
        editor.commit();





    }





    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
    }

    @Override
    protected void onResume() {

        super.onResume();

        Intent intent = getIntent();
        if ( intent != null ) {
            Uri intentData = intent.getData();
            SharedPreferences.Editor editor = kakaopay_count.edit();
            editor.putInt("pay", kakopay_num++);
            editor.commit();


            if(kakopay_num==2){


                buy_my_point();
                Log.e("buypoint11", String.valueOf(buy_point));

                Intent hello = new Intent(getApplicationContext(),MyPage_Activity.class);

//                plus_money();

                startActivity(hello);
                finish();


            }


            Log.e("what??", String.valueOf(intentData));
            if ( intentData != null ) {
                //카카오페이 인증 후 복귀했을 때 결제 후속조치
                String url = intentData.toString();
                Log.e("this3","thos1");
                Intent hello35 = new Intent(getApplicationContext(),MyPage_Activity.class);
                startActivity(hello35);

                if ( url.startsWith(APP_SCHEME) ) {
                    String path = url.substring(APP_SCHEME.length());
                    if ( "process".equalsIgnoreCase(path) ) {
                        Intent hello = new Intent(getApplicationContext(),MyPage_Activity.class);
                        startActivity(hello);
                        Log.e("this1","thos1");
                    } else {
                        mainWebView.loadUrl("javascript:IMP.communicate({result:'cancel'})");
                        Log.e("this2","thos1");

                    }
                }
            }
        }

    }

    private void plus_money() {

        StringRequest request = new StringRequest(Request.Method.POST, "http://18.225.37.73/buy_point.php", new Response.Listener<String>() {
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
                params.put("Login_id",MyPageLogin_id); //Login_id를 전송함
                params.put("buy_point", String.valueOf(buy_point)); //Login_id를 전송함

                Log.e("testLog","testLog");


                return params;
            }
        };

        Volley.newRequestQueue(getApplicationContext()).add(request);
    }



    //카카오결제로부터 구매한 포인트를 받아와서
    //db를 수정시킴

    private void buy_my_point(){
        StringRequest request = new StringRequest(Request.Method.POST, "http://18.225.37.73/Mymusic_point_update.php", new Response.Listener<String>() {

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
                params.put("Login_id",MyPageLogin_id); //Login_id를 전송함
                params.put("buy_point", String.valueOf(buy_point)); //Login_id를 전송함

                Log.e("testLog","testLog");


                return params;
            }
        };

        Volley.newRequestQueue(getApplicationContext()).add(request);
    }




//    private class PayBridge {
//        @JavascriptInterface
//        public void successPay() {
//            handler.post(new Runnable() {
//                @Override
//                public void run() {
////                    pay_webview.setVisibility(View.INVISIBLE);
////                    pay_status.setText("결제 완료");
////                    pay_status.setTextColor(getResources().getColor(R.color.blackcolor));
//                }
//            });
//        }
//    }
//
//    private class AndroidBridge {
//        @JavascriptInterface
//        public void setAddress(final String arg1, final String arg2, final String arg3) {
//            handler.post(new Runnable() {
//                @Override
//                public void run() {
////                    address_input.setText(String.format("(%s) %s %s", arg1, arg2, arg3));
////                    address_webview.setVisibility(View.INVISIBLE);
////                    // WebView를 초기화 하지않으면 재사용할 수 없음
////                    init_address_webView();
//                }
//            });
//        }
//    }
}
