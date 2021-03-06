package com.example.login_activity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.os.PowerManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;
import java.util.Map;

public class test_activity extends AppCompatActivity {

    public String music_url;
    public String music_name;
    public String music_extension;
    private ProgressDialog progressBar;
    static final int PERMISSION_REQUEST_CODE = 1;
    String[] PERMISSIONS = {"android.permission.READ_EXTERNAL_STORAGE", "android.permission.WRITE_EXTERNAL_STORAGE"};
    private File outputFile; //파일명까지 포함한 경로
    private File path;//디렉토리경로
    Button button;
    private SharedPreferences Login_ic_shared;
    private String My_login_id;
    private long pressedTime;

    private boolean hasPermissions(String[] permissions) {
        int res = 0;
        //스트링 배열에 있는 퍼미션들의 허가 상태 여부 확인
        for (String perms : permissions) {
            res = checkCallingOrSelfPermission(perms);
            if (!(res == PackageManager.PERMISSION_GRANTED)) {
                //퍼미션 허가 안된 경우
                return false;
            }

        }
        //퍼미션이 허가된 경우
        return true;
    }


    private void requestNecessaryPermissions(String[] permissions) {
        //마시멜로( API 23 )이상에서 런타임 퍼미션(Runtime Permission) 요청
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(permissions, PERMISSION_REQUEST_CODE);
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_activity);
        button = (Button) findViewById(R.id.button3333);
        Login_ic_shared = getSharedPreferences("Login_id", MODE_PRIVATE);



        Intent music_name_intent = getIntent();

        music_name=music_name_intent.getStringExtra("music_name");

        Log.e("music_name",music_name);
        music_extension=music_name+".mp3";
        Log.e("music_name2",music_extension);


        if (!hasPermissions(PERMISSIONS)) { //퍼미션 허가를 했었는지 여부를 확인
            requestNecessaryPermissions(PERMISSIONS);//퍼미션 허가안되어 있다면 사용자에게 요청
        } else {
            //이미 사용자에게 퍼미션 허가를 받음.
        }

        progressBar = new ProgressDialog(test_activity.this);
        progressBar.setMessage("다운로드중");
        progressBar.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        progressBar.setIndeterminate(true);
        progressBar.setCancelable(true);




        music_url= "http://18.225.37.73/mp3/";
        final String fileURL= music_url+music_extension;
        Log.e("musicurl",fileURL);
//                final String fileURL = "http://18.225.37.73/mp3/질풍가도.mp3";
        path= Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
        Log.e("pathname2", String.valueOf(path));
        outputFile= new File(path, music_extension); //파일명까지 포함함 경로의 File 객체 생성

        if (outputFile.exists()) { //이미 다운로드 되어 있는 경우

            AlertDialog.Builder builder = new AlertDialog.Builder(test_activity.this);
            builder.setTitle("파일 다운로드");
            builder.setMessage("이미 있는노래 입니다. 다시 다운로드 받을까요?");
            builder.setNegativeButton("아니오",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog,
                                            int which) {
                            Toast.makeText(getApplicationContext(),"기존 파일을 플레이합니다.",Toast.LENGTH_LONG).show();
//                                    playVideo(outputFile.getPath());
                        }
                    });
            builder.setPositiveButton("예",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            outputFile.delete(); //파일 삭제

                            final DownloadFilesTask downloadTask = new DownloadFilesTask(test_activity.this);
                            downloadTask.execute(fileURL);

                            progressBar.setOnCancelListener(new DialogInterface.OnCancelListener() {
                                @Override
                                public void onCancel(DialogInterface dialog) {
                                    downloadTask.cancel(true);
                                }
                            });
                        }
                    });
            builder.show();

        } else { //새로 다운로드 받는 경우
            final DownloadFilesTask downloadTask = new DownloadFilesTask(test_activity.this);
            downloadTask.execute(fileURL);

            progressBar.setOnCancelListener(new DialogInterface.OnCancelListener() {
                @Override
                public void onCancel(DialogInterface dialog) {
                    downloadTask.cancel(true);
                }
            });
        }
        //웹브라우저에 아래 링크를 입력하면 Alight.avi 파일이 다운로드됨.
//        button.setOnClickListener(new View.OnClickListener() {
//            public void onClick(View v) { //1
//                //웹브라우저에 아래 링크를 입력하면 Alight.avi 파일이 다운로드됨.
////                final String fileURL = "http://webnautes.tistory.com/attachment/cfile4.uf@267BB53E58451C582BD045.avi";
//            }
//        });
    }
    private class DownloadFilesTask extends AsyncTask<String, String, Long> {

        private Context context;
        private PowerManager.WakeLock mWakeLock;

        public DownloadFilesTask(Context context) {
            this.context = context;
        }


        //파일 다운로드를 시작하기 전에 프로그레스바를 화면에 보여줍니다.
        @Override
        protected void onPreExecute() { //2
            super.onPreExecute();

            //사용자가 다운로드 중 파워 버튼을 누르더라도 CPU가 잠들지 않도록 해서
            //다시 파워버튼 누르면 그동안 다운로드가 진행되고 있게 됩니다.
            PowerManager pm = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
            mWakeLock = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, getClass().getName());
            mWakeLock.acquire();

            progressBar.show();
        }


        //파일 다운로드를 진행합니다.
        @Override
        protected Long doInBackground(String... string_url) { //3
            int count;
            long FileSize = -1;
            InputStream input = null;
            OutputStream output = null;
            URLConnection connection = null;

            try {
                URL url = new URL(string_url[0]);
                connection = url.openConnection();
                connection.connect();


                //파일 크기를 가져옴
                FileSize = connection.getContentLength();

                //URL 주소로부터 파일다운로드하기 위한 input stream
                input = new BufferedInputStream(url.openStream(), 8192);

                path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
                Log.e("path_name", String.valueOf(path));
                outputFile = new File("/storage/emulated/0/Download", music_extension); //파일명까지 포함함 경로의 File 객체 생성

                // SD카드에 저장하기 위한 Output stream
                output = new FileOutputStream(outputFile);


                byte data[] = new byte[1024];
                long downloadedSize = 0;
                while ((count = input.read(data)) != -1) {
                    //사용자가 BACK 버튼 누르면 취소가능
                    if (isCancelled()) {
                        input.close();
                        return Long.valueOf(-1);
                    }

                    downloadedSize += count;

                    if (FileSize > 0) {
                        float per = ((float) downloadedSize / FileSize) * 100;
                        String str = "Downloaded " + downloadedSize + "KB / " + FileSize + "KB (" + (int) per + "%)";
                        publishProgress("" + (int) ((downloadedSize * 100) / FileSize), str);

                    }

                    //파일에 데이터를 기록합니다.
                    output.write(data, 0, count);
                }
                // Flush output
                output.flush();

                // Close streams
                output.close();
                input.close();


            } catch (Exception e) {
                Log.e("Error: ", e.getMessage());
            } finally {
                try {
                    if (output != null)
                        output.close();
                    if (input != null)
                        input.close();
                } catch (IOException ignored) {
                }

                mWakeLock.release();

            }
            return FileSize;
        }


        //다운로드 중 프로그레스바 업데이트
        @Override
        protected void onProgressUpdate(String... progress) { //4
            super.onProgressUpdate(progress);

            // if we get here, length is known, now set indeterminate to false
            progressBar.setIndeterminate(false);
            progressBar.setMax(100);
            progressBar.setProgress(Integer.parseInt(progress[0]));
            progressBar.setMessage(progress[1]);
        }

//        파일 다운로드 완료 후
        @Override
        protected void onPostExecute(Long size) { //5
            super.onPostExecute(size);

            progressBar.dismiss();

            if (size > 0) {
                Toast.makeText(getApplicationContext(), "다운로드 완료되었습니다. 파일 크기=" + size.toString(), Toast.LENGTH_LONG).show();

                My_login_id=Login_ic_shared.getString("Login_id","??");
                Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
                mediaScanIntent.setData(Uri.fromFile(outputFile));
                sendBroadcast(mediaScanIntent);

                buy_music();


                finish();

//                playVideo(outputFile.getPath());

            } else
                Toast.makeText(getApplicationContext(), "다운로드 에러", Toast.LENGTH_LONG).show();
            finish();

        }

    }

    private void buy_music() {

        StringRequest request = new StringRequest(Request.Method.POST, "http://18.225.37.73/buy_point.php", new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
//해당 데이터를 받아온다 1일시에는 로그인성공 0일시에는 로그인실패
                Log.e("db_response",response);
//                M>response;
//                Music_buy_list_load();



            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String, String> params = new HashMap<>();
                params.put("Login_id",My_login_id);


                return params;
            }
        };

        Volley.newRequestQueue(getApplicationContext()).add(request);
    }




    @Override
    public void onRequestPermissionsResult(int permsRequestCode, String[] permissions, int[] grantResults) {
        switch (permsRequestCode) {

            case PERMISSION_REQUEST_CODE:
                if (grantResults.length > 0) {
                    boolean readAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    boolean writeAccepted = grantResults[1] == PackageManager.PERMISSION_GRANTED;

                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

                        if (!readAccepted || !writeAccepted) {
                            showDialogforPermission("앱을 실행하려면 퍼미션을 허가하셔야합니다.");
                            return;
                        }
                    }
                }
                break;
        }
    }

    private void showDialogforPermission(String msg) {

        final AlertDialog.Builder myDialog = new AlertDialog.Builder(test_activity.this);
        myDialog.setTitle("알림");
        myDialog.setMessage(msg);
        myDialog.setCancelable(false);
        myDialog.setPositiveButton("예", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface arg0, int arg1) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    requestPermissions(PERMISSIONS, PERMISSION_REQUEST_CODE);
                }

            }
        });
        myDialog.setNegativeButton("아니오", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface arg0, int arg1) {
                finish();
            }
        });
        myDialog.show();
    }

//    private void playVideo(String path) {
//        Uri videoUri = Uri.fromFile(new File(path));
//        Intent videoIntent = new Intent(Intent.ACTION_VIEW);
//        videoIntent.setDataAndType(videoUri, "video/*");
//        if (videoIntent.resolveActivity(getPackageManager()) != null) {
//            startActivity(Intent.createChooser(videoIntent, null));
//        }
//    }


    @Override
    public void onBackPressed() {
        Intent go_main = new Intent(getApplicationContext(),MainActivity.class);
        startActivity(go_main);
    }


}
