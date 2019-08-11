package com.example.simplemusicplayer.activities;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;


import com.example.login_activity.R;

import java.util.Timer;
import java.util.TimerTask;

public class WelcomeActivity extends BaseActivity {

    private Timer timer;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        requestStoragePermission();
        init();

    }
    private void requestStoragePermission() {

        int hasCameraPermission = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE);
        Log.e("TAG","시작" + hasCameraPermission);
        if (hasCameraPermission == PackageManager.PERMISSION_GRANTED){
            Log.e("TAG", "권한허용");
        }else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                Log.e("TAG", "권한 사용자 허용");
                requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 10086);
            }
        }

    }

    private void init() {
        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                toMain();
                finish();
            }
        },1);
    }

    private void toMain() {
        Intent intent = new Intent(this,MusicListActivity.class);
        startActivity(intent);
    }


}
