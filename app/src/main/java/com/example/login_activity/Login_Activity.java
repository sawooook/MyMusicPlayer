package com.example.login_activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.icu.text.SimpleDateFormat;
import android.net.Uri;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.radio.MainActivity;
import com.example.simplemusicplayer.activities.MusicListActivity;
import com.kakao.util.KakaoParameterException;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Date;

import static com.kakao.util.helper.Utility.getPackageInfo;

public class Login_Activity extends AppCompatActivity {
    EditText Login_id_edt;
    Button Login_id_btn;
    SharedPreferences Login_ic_shared;
    //디비를 생성함
    final static String dbName = "music.db"; //db이름름
    final static int dbVersion = 2;
    String sql;
    SQLiteDatabase db;
    DBHelper dbHelper;
    private File tempSelectFile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_);
        Login_id_edt=(EditText)findViewById(R.id.Login_id_edt);
        Login_id_btn= (Button)findViewById(R.id.Login_id_btn);
        Login_ic_shared = getSharedPreferences("Login_id", MODE_PRIVATE);
        dbHelper = new DBHelper(this, dbName, null, dbVersion);
//        getKeyHash(getApplicationContext());

        Login_id_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


//                FileUploadUtils.send2Server(new File("/storage/emulated/0/Pictures/KakaoTalk/1565008617241"));


//                kakaolink();


//                name TEXT, img TEXT, favo TEXT, artist TEXT

//                String name = "hello";
//                String img = "img";
//                String favo = "0";
//                String artist = "saouk";
//

//                db = dbHelper.getWritableDatabase();
//                sql = String.format("INSERT INTO music VALUES('" + name + "'  , '" + img + "'  ,  '" + favo + "'  ,  '" + artist + "'  );");
//
//                db.execSQL(sql);


//                db = dbHelper.getReadableDatabase();
////
////
//                sql = "SELECT * FROM music;";
//                Cursor cursor = db.rawQuery(sql, null);
//
//                Log.e("cursor", String.valueOf(cursor));
//                if (cursor.getCount() > 0) {
//                    while (cursor.moveToNext()) {
//                        Login_id_edt.append(String.format("\n이름 = %s, 메모 = %s, 우선순위 = %s, 날짜 = %s",
//                                cursor.getString(0), cursor.getString(1), cursor.getString(2), cursor.getString(3)));
//                    }
//                }



//                SharedPreferences.Editor editor = Login_ic_shared.edit();
//                editor.putString("Login_id", Login_id_edt.getText().toString());
//                editor.commit();
//
//                String gg=Login_ic_shared.getString("Login_id","??");
//                Log.e("Login_id_debuging",gg);

//                Intent login_btn_intent = new Intent(getApplicationContext(), com.example.drawar.DrawAR.class);

//                Intent login_btn_intent = new Intent(getApplicationContext(), com.example.radio.MainActivity.class);

//                Intent login_btn_intent = new Intent(getApplicationContext(),qrtest.class);
//                startActivity(login_btn_intent);
                Intent login_btn_intent = new Intent(getApplicationContext(), com.example.login_activity.MainActivity.class);
                startActivity(login_btn_intent);


            }
        });

    }
//    public static String getKeyHash(final Context context){
//        PackageInfo packageInfo = getPackageInfo(context, PackageManager.GET_SIGNATURES);
//        if (packageInfo == null)
//            return null;
//
//        for (Signature signature : packageInfo.signatures) {
//            try {
//                MessageDigest md = MessageDigest.getInstance("SHA");
//                md.update(signature.toByteArray());
//
//                Log.e("keyhash:",Base64.encodeToString(md.digest(),Base64.DEFAULT));
//            } catch (NoSuchAlgorithmException e) {
//                Log.w("keykey", "Unable to get MessageDigest. signature=" + signature, e);
//            }
//        }
//        return null;
//    }

//    @Override protected void onActivityResult(int requestCode, int resultCode, Intent data){
//        if (requestCode != 1 || resultCode != RESULT_OK) {
//            return;
//        }
//        Uri dataUri = data.getData();
//        try {
//            // ImageView 에 이미지 출력
//            InputStream in = getContentResolver().openInputStream(dataUri);
//            Bitmap image = BitmapFactory.decodeStream(in);
//            in.close(); // 선택한 이미지 임시 저장
//            String date = new SimpleDateFormat("yyyy_MM_dd_hh_mm_ss").format(new Date());
//            tempSelectFile = new File(Environment.getExternalStorageDirectory()+"/Pictures/Test/", "temp_" + date + ".jpeg");
//            OutputStream out = new FileOutputStream(tempSelectFile);
//            image.compress(Bitmap.CompressFormat.JPEG, 100, out);
//        } catch(IOException ioe)
//        { ioe.printStackTrace();
//        }
//        Login_id_btn.setEnabled(true);
//    }
}
