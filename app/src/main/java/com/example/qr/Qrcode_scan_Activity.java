package com.example.qr;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.example.login_activity.R;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

public class Qrcode_scan_Activity extends AppCompatActivity {


    private IntentIntegrator qrScan;
    private IntentResult result;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        qrScan = new IntentIntegrator(this);
        qrScan.setOrientationLocked(false); // default가 세로모드인데 휴대폰 방향에 따라 가로, 세로로 자동 변경됩니다.
        qrScan.initiateScan();

    }
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
         result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if(result != null) {
            if(result.getContents() == null) {
                //결과가 아무것도 없을때

                Toast.makeText(this, "Cancelled", Toast.LENGTH_LONG).show();
                // todo
            } else {
                //만약 결과가 있을경우
                Log.e("whatwhatresult?",result.getContents());
                Intent recommanded_intent =new Intent(getApplicationContext(),Qrcode_recommanded_Activity.class);

                String result_data= result.getContents();
                //노래 데이터를 담은후 qrcode_recommanded_activity로 보낸다
//                recommanded_intent.putExtra("resultdata","안녕하세요;홍사욱입니다;머하시나요;면소갑니다");

                startActivity(recommanded_intent);

                finish();
                // todo
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }
}
