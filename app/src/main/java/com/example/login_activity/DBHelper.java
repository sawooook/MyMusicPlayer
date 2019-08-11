package com.example.login_activity;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

class DBHelper extends SQLiteOpenHelper {

    //생성자 - database 파일을 생성한다.
    public DBHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    //DB 처음 만들때 호출. - 테이블 생성 등의 초기 처리.
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE music (name TEXT, img TEXT, favo TEXT, artist TEXT);");
//result.append("\nt3 테이블 생성 완료.");
    }

    //DB 업그레이드 필요 시 호출. (version값에 따라 반응)
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS music");
        onCreate(db);
    }




}
