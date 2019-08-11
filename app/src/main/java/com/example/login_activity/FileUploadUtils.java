package com.example.login_activity;

import android.util.Log;

import java.io.File;
import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

class FileUploadUtils {

    public static void send2Server(File file) {
        RequestBody requestBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("files", file.getName(),
                        RequestBody.create(MultipartBody.FORM, file)).build();
        Request request = new Request.Builder()
                .url("http://18.225.37.73/upload.php")
                // Server URL 은 본인 IP를 입력
                .post(requestBody).build();
        OkHttpClient client = new OkHttpClient();
        client.newCall(request).enqueue(new Callback() {

            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                Log.d("TEST : ", response.body().string());
            }
        });
    }

}
