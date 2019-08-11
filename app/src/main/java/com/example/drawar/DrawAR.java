package com.example.drawar;

import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.icu.text.SimpleDateFormat;
import android.media.CamcorderProfile;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.net.Uri;
import android.opengl.GLES20;
import android.opengl.GLException;
import android.opengl.GLSurfaceView;
import android.opengl.Matrix;
import android.os.Build;
import android.os.Environment;
import android.support.annotation.ColorInt;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GestureDetectorCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.drawar.Permissions.PermissionHelper;
import com.example.drawar.Rendering.BackgroundRenderer;
import com.example.drawar.Rendering.BiquadFilter;
import com.example.drawar.Rendering.LineShaderRenderer;
import com.example.drawar.Rendering.LineUtils;
import com.example.drawar.Settings.AppSettings;
import com.example.login_activity.Login_Activity;
import com.example.login_activity.R;
import com.example.simplemusicplayer.activities.BaseActivity;
import com.example.simplemusicplayer.activities.MainActivity;
import com.example.simplemusicplayer.activities.MusicListActivity;
import com.example.simplemusicplayer.adapters.MusicListAdapter;
import com.example.simplemusicplayer.entity.Mp3Info;
import com.example.simplemusicplayer.helpers.MediaPlayerHelper;
import com.example.simplemusicplayer.util.mp3.MediaUtil;
import com.google.ar.core.ArCoreApk;
import com.google.ar.core.Camera;
import com.google.ar.core.Config;
import com.google.ar.core.Frame;
import com.google.ar.core.Session;
import com.google.ar.core.TrackingState;
import com.google.ar.core.exceptions.CameraNotAvailableException;
import com.google.ar.core.exceptions.UnavailableApkTooOldException;
import com.google.ar.core.exceptions.UnavailableArcoreNotInstalledException;
import com.google.ar.core.exceptions.UnavailableSdkTooOldException;
import com.google.ar.core.exceptions.UnavailableUserDeclinedInstallationException;
import com.oguzdev.circularfloatingactionmenu.library.FloatingActionMenu;
import com.oguzdev.circularfloatingactionmenu.library.SubActionButton;
import com.pes.androidmaterialcolorpickerdialog.ColorPicker;
import com.pes.androidmaterialcolorpickerdialog.ColorPickerCallback;
import com.oguzdev.circularfloatingactionmenu.library.FloatingActionButton;
import com.uncorkedstudios.android.view.recordablesurfaceview.RecordableSurfaceView;


import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

import javax.microedition.khronos.egl.EGL10;
import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.egl.EGLContext;
import javax.microedition.khronos.opengles.GL10;
import javax.vecmath.Vector2f;
import javax.vecmath.Vector3f;


public class DrawAR extends AppCompatActivity implements GLSurfaceView.Renderer, GestureDetector.OnGestureListener,
        GestureDetector.OnDoubleTapListener {


    private Bitmap snapshotBitmap;
    private Bitmap bitmap;
    private ColorPicker cp;


    private interface BitmapReadyCallbacks {
        void onBitmapReady(Bitmap bitmap);
    }

    private RecordableSurfaceView mGLView;


    String TAG = DrawAR.class.getSimpleName();
    GLSurfaceView mSurfaceView;
    Session mSession;
//    private WritingArFragment arFragment;

    boolean bInstallRequested;
    DisplayRotationHelper mDisplayRotationHelper;
    boolean mPaused = false;
    float mScreenWidth = 0;
    ImageButton line_button; //선의 두께 , 색을 변경할수 있음
    float mScreenHeight = 0;
    TextView music_name;
    MediaRecorder recorder; // 화면 녹화
    String recordPath = "/storage/emulated/0/Download";
    BackgroundRenderer mBackgroundRenderer = new BackgroundRenderer();
    Frame mFrame;
    AtomicBoolean bIsTracking = new AtomicBoolean(true);
    TrackingState mState;
    private float[] mZeroMatrix = new float[16];
    private Vector3f mLastPoint;
    private GestureDetectorCompat mDetector;
    private ArrayList<ArrayList<Vector3f>> mStrokes;
    private float[] projmtx = new float[16];
    private float[] viewmtx = new float[16];
    private LineShaderRenderer mLineShaderRenderer = new LineShaderRenderer();
    private AtomicBoolean bTouchDown = new AtomicBoolean(false);
    private float[] mLastFramePosition;
    private AtomicBoolean bNewStroke = new AtomicBoolean(false);
    private AtomicReference<Vector2f> lastTouch = new AtomicReference<>();
    private AtomicBoolean bReCenterView = new AtomicBoolean(false);
    private AtomicBoolean bClearDrawing = new AtomicBoolean(false);
    private AtomicBoolean bUndo = new AtomicBoolean(false);
    private AtomicBoolean bLineParameters = new AtomicBoolean(false);
    private float mLineWidthMax = 0.33f;
    private float mDistanceScale = 0.0f;
    private float mLineSmoothing = 0.1f;
    private ImageButton startButton,nextButton;
    private BiquadFilter biquadFilter;
    List<Mp3Info> mp3Infos;
    static int music_number=0;
    private SeekBar mLineWidthBar;
    private SeekBar mLineDistanceScaleBar;
    private SeekBar mSmoothingBar;
    private MediaPlayerHelper mMediaPlayerHelper;

    ImageButton Take_picture; //사진을 찍을수 있는버튼
    ImageButton stopButton; //노래를 멈추는 버튼
    ImageButton line_color; //선의 색을 넣는 버튼

    ImageButton delete_draw; //화면에 그려진것을 삭제한다
    private OnImportMusicListener onImportMusicListener;
    private String mPath;
    private Mp3Info mp3Info; //
    private int mp3infosize;
    private VideoRecorder videoRecorder;
    //    private VideoRecorder videoRecorder;
    FrameLayout surfaceFrame;


    ImageView imageView3;
    public void setOnImportMusicListener(OnImportMusicListener onImportMusicListener) {
        this.onImportMusicListener = onImportMusicListener;
    }

    @TargetApi(Build.VERSION_CODES.M)
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_draw_ar);
        getSupportActionBar().hide();
        music_name=(TextView)findViewById(R.id.music_name);
        startButton=(ImageButton)findViewById(R.id.music_start);
        nextButton=(ImageButton)findViewById(R.id.imageButton2);
        Take_picture=(ImageButton)findViewById(R.id.takePicture);
        final SharedPreferences sharedPref = this.getPreferences(Context.MODE_PRIVATE);
//        surfaceFrame=(FrameLayout)findViewById(R.id.topView);
        stopButton=(ImageButton)findViewById(R.id.stopButton);
        delete_draw=(ImageButton)findViewById(R.id.delete_draw); //화면에 그려진 그림들을 삭제함
        line_button=(ImageButton)findViewById(R.id.line_draw);
//        line_color=(ImageButton)findViewById(R.id.line_color);


//        line_color.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                /* Show color picker dialog */
//                cp.getWindow().setBackgroundDrawableResource(R.color.black);
//                cp.show();
//                cp.enableAutoClose(); // Enable auto-dismiss for the dialog
//
//                /* Set a new Listener called when user click "select" */
//                cp.setCallback(new ColorPickerCallback() {
//                    @Override
//                    public void onColorChosen(@ColorInt int color) {
//                        Vector3f curColor = new Vector3f(Color.red(color)/255f,Color.green(color)/255f,Color.blue(color)/255f);
//                        AppSettings.setColor(curColor);
//                        cp.dismiss();
//                    }
//                });
//            }
//        });

        line_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                   Dialog dialog = new Dialog(DrawAR.this);
                    dialog.setContentView(R.layout.dialog_settings);
                    dialog.show();

//                    mLineDistanceScaleBar = dialog.findViewById(R.id.distanceScale);
                    mLineWidthBar = dialog.findViewById(R.id.lineWidth);
//                    mSmoothingBar = dialog.findViewById(R.id.smoothingSeekBar);

//                    mLineDistanceScaleBar.setProgress(sharedPref.getInt("mLineDistanceScale", 1));
                    mLineWidthBar.setProgress(sharedPref.getInt("mLineWidth", 10));
//                    mSmoothingBar.setProgress(sharedPref.getInt("mSmoothing", 50));

//                    mDistanceScale = LineUtils.map((float) mLineDistanceScaleBar.getProgress(), 0, 100, 1, 200, true);
                    mLineWidthMax = LineUtils.map((float) mLineWidthBar.getProgress(), 0f, 100f, 0.1f, 5f, true);
//                    mLineSmoothing = LineUtils.map((float) mSmoothingBar.getProgress(), 0, 100, 0.01f, 0.2f, true);


                    SeekBar.OnSeekBarChangeListener seekBarChangeListener = new SeekBar.OnSeekBarChangeListener() {
                        /**
                         * Listen for seekbar changes, and update the settings
                         */
                        @Override
                        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                            SharedPreferences.Editor editor = sharedPref.edit();

//                            if (seekBar == mLineDistanceScaleBar) {
//                                editor.putInt("mLineDistanceScale", progress);
//                                mDistanceScale = LineUtils.map((float) progress, 0f, 100f, 1f, 200f, true);
//                            }
                            if (seekBar == mLineWidthBar) {
                                editor.putInt("mLineWidth", progress);
                                mLineWidthMax = LineUtils.map((float) progress, 0f, 100f, 0.1f, 5f, true);
                            }
//                            else if (seekBar == mSmoothingBar) {
//                                editor.putInt("mSmoothing", progress);
//                                mLineSmoothing = LineUtils.map((float) progress, 0, 100, 0.01f, 0.2f, true);
//                            }
                            mLineShaderRenderer.bNeedsUpdate.set(true);

                            editor.apply();

                        }

                        @Override
                        public void onStartTrackingTouch(SeekBar seekBar) {
                        }

                        @Override
                        public void onStopTrackingTouch(SeekBar seekBar) {
                        }
                    };

//                    mLineDistanceScaleBar.setOnSeekBarChangeListener(seekBarChangeListener);
                    mLineWidthBar.setOnSeekBarChangeListener(seekBarChangeListener);
//                    mSmoothingBar.setOnSeekBarChangeListener(seekBarChangeListener);

                }
        });

//        Mp3Info mp3Info = (Mp3Info) getIntent().getSerializableExtra("mp3");
//        int index = getIntent().getIntExtra("index",0);
//        String mPath = mp3Info.getUrl();
//
//        Log.e("mpath",mPath);


        // Create a new video recorder instance.
//        videoRecorder = new VideoRecorder();

// Specify the AR scene view to be recorded.
//        videoRecorder.setSceneView();

// Set video quality and recording orientation to match that of the device.
//        int orientation = getResources().getConfiguration().orientation;
//        videoRecorder.setVideoQuality(CamcorderProfile.QUALITY_2160P, orientation);
//        videoRecorder.setSceneView(arFragment());



        ImageView icon = new ImageView(this); // Create an icon
        icon.setColorFilter(ContextCompat.getColor(this, R.color.colorPrimary), android.graphics.PorterDuff.Mode.MULTIPLY);
        icon.setImageDrawable(getDrawable(R.drawable.baseline_menu_white_24dp));

//
//        FloatingActionButton actionButton = new FloatingActionButton.Builder(this).setContentView(icon).build();
//        actionButton.setBackground(getDrawable(R.drawable.back));
//        FloatingActionButton actionButton = new FloatingActionButton.Builder(this)
//                .setContentView(icon)
//                .build();
//        actionButton.setBackground(getDrawable(R.drawable.back));


       cp = new ColorPicker(DrawAR.this, 255, 255, 255);


        SubActionButton.Builder itemBuilder = new SubActionButton.Builder(this);
        itemBuilder.setBackgroundDrawable(getDrawable(R.drawable.back));
        // repeat many times:
        ImageView itemIcon1 = new ImageView(this);
        itemIcon1.setColorFilter(ContextCompat.getColor(this, R.color.whirecolor), android.graphics.PorterDuff.Mode.MULTIPLY);
        itemIcon1.setImageDrawable(getDrawable(R.drawable.baseline_color_lens_white_24dp));
        SubActionButton button1 = itemBuilder.setContentView(itemIcon1).build();


//        button1.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                /* Show color picker dialog */
//                cp.getWindow().setBackgroundDrawableResource(R.color.black);
//                cp.show();
//                cp.enableAutoClose(); // Enable auto-dismiss for the dialog
//
//                /* Set a new Listener called when user click "select" */
//                cp.setCallback(new ColorPickerCallback() {
//                    @Override
//                    public void onColorChosen(@ColorInt int color) {
//                        Vector3f curColor = new Vector3f(Color.red(color)/255f,Color.green(color)/255f,Color.blue(color)/255f);
//                        AppSettings.setColor(curColor);
//                        cp.dismiss();
//                    }
//                });
//            }
//        });




        /*
         * 노래를 시작함
         * 노래가 시작되면 스탑 버튼을 보이게 하였으며
         * 스타트 버튼인 보이지 않도록함
         * */
        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                stopButton.setVisibility(View.VISIBLE);
                startButton.setVisibility(View.GONE);


                music_path();


            }
        });



        //화면에 그려진 그림들을 삭제한다
        delete_draw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                AlertDialog.Builder builder = new AlertDialog.Builder(DrawAR.this)
                        .setMessage(Html.fromHtml("<font color='#FFFFFF'>정말로 그림을 지우시겠습니까?</font>"));


                //만약 삭제버튼을 누르면 화면에 그림들이 지워짐
                builder.setPositiveButton("예 ", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        bClearDrawing.set(true);
                    }
                });
                builder.setNegativeButton("아니요", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

//                builder.show();
                AlertDialog alert = builder.create();
                alert.show();
                alert.getWindow().setBackgroundDrawableResource(R.color.black);

                Button nbutton = alert.getButton(DialogInterface.BUTTON_NEGATIVE);
                //Set negative button background
                nbutton.setBackgroundColor(getColor(R.color.black));
                //Set negative button text color
                nbutton.setTextColor(getColor(R.color.whirecolor));
                Button pbutton = alert.getButton(DialogInterface.BUTTON_POSITIVE);
                //Set positive button background
                pbutton.setBackgroundColor(getColor(R.color.black));
                //Set positive button text color
                pbutton.setTextColor(getColor(R.color.whirecolor));


            }
        });

        ImageView itemIcon2 = new ImageView(this);
        itemIcon2.setColorFilter(ContextCompat.getColor(this, R.color.whirecolor), android.graphics.PorterDuff.Mode.MULTIPLY);
        itemIcon2.setImageDrawable(getDrawable(R.drawable.baseline_undo_white_24dp));
        SubActionButton button2 = itemBuilder.setContentView(itemIcon2).build();
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bUndo.set(true);
            }
        });




//        ImageView itemIcon3 = new ImageView(this);
//        itemIcon3.setColorFilter(ContextCompat.getColor(this, R.color.green), android.graphics.PorterDuff.Mode.MULTIPLY);
//        itemIcon3.setImageDrawable(getDrawable(R.drawable.baseline_delete_white_24dp));
//        SubActionButton button3 = itemBuilder.setContentView(itemIcon3).build();
//        button3.setOnClickListener(new View.OnClickListener() {
//            @RequiresApi(api = Build.VERSION_CODES.M)
//            @Override
//            public void onClick(View v) {
//                AlertDialog.Builder builder = new AlertDialog.Builder(DrawAR.this)
//                        .setMessage(Html.fromHtml("<font color='#00B551'>Sure you want to clear?</font>"));
//
//                // Set up the buttons
//                builder.setPositiveButton("Clear ", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        bClearDrawing.set(true);
//                    }
//                });
//                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        dialog.cancel();
//                    }
//                });

//                builder.show();
//                AlertDialog alert = builder.create();
//                alert.show();
//                alert.getWindow().setBackgroundDrawableResource(R.color.black);
//
//                Button nbutton = alert.getButton(DialogInterface.BUTTON_NEGATIVE);
//                //Set negative button background
//                nbutton.setBackgroundColor(getColor(R.color.black));
//                //Set negative button text color
//                nbutton.setTextColor(getColor(R.color.green));
//                Button pbutton = alert.getButton(DialogInterface.BUTTON_POSITIVE);
//                //Set positive button background
//                pbutton.setBackgroundColor(getColor(R.color.black));
//                //Set positive button text color
//                pbutton.setTextColor(getColor(R.color.green));
//            }
//        });

//        ImageView itemIcon4 = new ImageView(this);
//        itemIcon4.setColorFilter(ContextCompat.getColor(this, R.color.green), android.graphics.PorterDuff.Mode.MULTIPLY);
//        itemIcon4.setImageDrawable(getDrawable(R.drawable.baseline_settings_white_24dp));
//        SubActionButton button4 = itemBuilder.setContentView(itemIcon4).build();



        //선의 색을 설정하는부분임
//
//        button4.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Dialog dialog = new Dialog(DrawAR.this);
//                dialog.setContentView(R.layout.dialog_settings);
//                dialog.show();
//
//                mLineDistanceScaleBar = dialog.findViewById(R.id.distanceScale);
//                mLineWidthBar = dialog.findViewById(R.id.lineWidth);
//                mSmoothingBar = dialog.findViewById(R.id.smoothingSeekBar);
//
//                mLineDistanceScaleBar.setProgress(sharedPref.getInt("mLineDistanceScale", 1));
//                mLineWidthBar.setProgress(sharedPref.getInt("mLineWidth", 10));
//                mSmoothingBar.setProgress(sharedPref.getInt("mSmoothing", 50));
//
//                mDistanceScale = LineUtils.map((float) mLineDistanceScaleBar.getProgress(), 0, 100, 1, 200, true);
//                mLineWidthMax = LineUtils.map((float) mLineWidthBar.getProgress(), 0f, 100f, 0.1f, 5f, true);
//                mLineSmoothing = LineUtils.map((float) mSmoothingBar.getProgress(), 0, 100, 0.01f, 0.2f, true);
//
//
//                SeekBar.OnSeekBarChangeListener seekBarChangeListener = new SeekBar.OnSeekBarChangeListener() {
//                    /**
//                     * Listen for seekbar changes, and update the settings
//                     */
//                    @Override
//                    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
//                        SharedPreferences.Editor editor = sharedPref.edit();
//
//                        if (seekBar == mLineDistanceScaleBar) {
//                            editor.putInt("mLineDistanceScale", progress);
//                            mDistanceScale = LineUtils.map((float) progress, 0f, 100f, 1f, 200f, true);
//                        } else if (seekBar == mLineWidthBar) {
//                            editor.putInt("mLineWidth", progress);
//                            mLineWidthMax = LineUtils.map((float) progress, 0f, 100f, 0.1f, 5f, true);
//                        } else if (seekBar == mSmoothingBar) {
//                            editor.putInt("mSmoothing", progress);
//                            mLineSmoothing = LineUtils.map((float) progress, 0, 100, 0.01f, 0.2f, true);
//                        }
//                        mLineShaderRenderer.bNeedsUpdate.set(true);
//
//                        editor.apply();
//
//                    }
//
//                    @Override
//                    public void onStartTrackingTouch(SeekBar seekBar) {
//                    }
//
//                    @Override
//                    public void onStopTrackingTouch(SeekBar seekBar) {
//                    }
//                };
//
//                mLineDistanceScaleBar.setOnSeekBarChangeListener(seekBarChangeListener);
//                mLineWidthBar.setOnSeekBarChangeListener(seekBarChangeListener);
//                mSmoothingBar.setOnSeekBarChangeListener(seekBarChangeListener);
//
//            }
//        });


//        FloatingActionMenu actionMenu = new FloatingActionMenu.Builder(this)
//                .addSubActionView(button1)
//                .addSubActionView(button2)
//                .addSubActionView(button3)
//                .addSubActionView(button4)
//                .attachTo(actionButton)
//                .build();




//        if (!PermissionHelper.hasCameraPermission(this)) {
//            Log.v("TAG","AAYA");
//            PermissionHelper.requestCameraPermission(this);
//            return;
//        }

        mSurfaceView = findViewById(R.id.surfaceview);

        mDisplayRotationHelper = new DisplayRotationHelper(this);
        Matrix.setIdentityM(mZeroMatrix, 0);

        mLastPoint = new Vector3f(0, 0, 0);
        bInstallRequested = false;

        // Set up renderer.
        mSurfaceView.setPreserveEGLContextOnPause(true);
        mSurfaceView.setEGLContextClientVersion(2);
        mSurfaceView.setEGLConfigChooser(8, 8, 8, 8, 16, 0); // Alpha used for plane blending.
        mSurfaceView.setRenderer(this);
        mSurfaceView.setRenderMode(GLSurfaceView.RENDERMODE_CONTINUOUSLY);

        // Setup touch detector
        mDetector = new GestureDetectorCompat(this, this);
        mDetector.setOnDoubleTapListener(this);
        mStrokes = new ArrayList<>();



        Take_picture.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onClick(View view) {

                take();

            }
        });


    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void take() {


        captureBitmap(new BitmapReadyCallbacks() {

            @Override
            public void onBitmapReady(Bitmap bitmap) {


//                imageView3.setImageBitmap(bitmap);
                make_jpg(bitmap);


            }
        });
    }

    private void make_jpg(Bitmap bitmap) {

        SaveBitmapToFileCache(bitmap,"/storage/emulated/0/Pictures/Screenshots/","kkk");



    }


    private void music_path() {


        //음악에 대한 정보를 모두 불러옴

        OnImportMusicListener onImportMusicListener = new OnImportMusicListener() {

            @Override
            public void onImport(final List<Mp3Info> mp3Infos) {
                MediaPlayerHelper.getInstance(getApplicationContext()).setMp3Infos(mp3Infos);
                //mp3리스트에 노래를 추가시킴
            }
        };

        mp3Infos = MediaUtil.getMusicInfo(getApplicationContext());
        onImportMusicListener.onImport(mp3Infos);
        Log.e("whatismsyic", String.valueOf(mp3Infos));
        mp3Info = mp3Infos.get(music_number);


        mp3infosize =mp3Infos.size();
        //로컬에 mp3파일이 몇개들어있는지 확인함


        mMediaPlayerHelper=MediaPlayerHelper.getInstance(getApplicationContext());

        int index = getIntent().getIntExtra("index",0);
        mMediaPlayerHelper.setCurrentIndex(index);
//        Log.e("whatismpath:",mPath);


        playMusic(mp3Info.getUrl());
        music_name.setText(mp3Infos.get(music_number).getTitle());

    }



    //노래를 실행시킨다
    private void playMusic(String path) {
        mPath = path;
        //해당노래의 주소를 불러옴

        Log.e("mpathreal",path);
//        isPlaying = true;
        if (mMediaPlayerHelper.getPath()!=null&& mMediaPlayerHelper.getPath().equals(path)){
            mMediaPlayerHelper.start();
        }else {
            mMediaPlayerHelper.setPath(path);
            mMediaPlayerHelper.setOnBeforePlayerHelperListener(new MediaPlayerHelper.OnBeforePlayerHelperListener() {
                @Override
                public void onBefore(MediaPlayer mp) {
//                    mRolling.show();
                }
            });
            mMediaPlayerHelper.setOnMediaPlayerHelperFinishHelper(new MediaPlayerHelper.OnMediaPlayerHelperFinishHelper() {
                @Override
                public void onCompleted(MediaPlayer mp) {
//                    exit=true;
//
//                    startButton.setVisibility(View.VISIBLE);
//                    pauseButton.setVisibility(View.GONE);
                }
            });


            //뮤직플레이어가 진행되고있는동안 컨트롤 할수 있음
            mMediaPlayerHelper.setOnMediaPlayerHelperListener(new MediaPlayerHelper.OnMediaPlayerHelperListener() {
                @Override
                public void onPrepared(final MediaPlayer mp) {

                    //播放
                    startButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {


                            //뮤직을 실행시킴
                            playMusic(mPath);

                            Log.e("mpath",mPath);
                            stopButton.setVisibility(View.VISIBLE);
                            startButton.setVisibility(View.GONE);
                        }
                    });
                    //다음곡으로 넘김
                    nextButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {





                            if(music_number==mp3infosize){
                                music_number=0;
                                Log.e("plzplz","plzpl;z");
                                stopMusic();
                                playMusic(mp3Infos.get(music_number).getUrl());
                                music_name.setText(mp3Infos.get(music_number).getTitle());
                                stopButton.setVisibility(View.GONE);
                                startButton.setVisibility(View.VISIBLE);


                            }else if(music_number!=mp3infosize){
                                stopMusic();
                                music_number++;
                                Log.e("plzplz","plzpl;z");
                                playMusic(mp3Infos.get(music_number).getUrl());
                                music_name.setText(mp3Infos.get(music_number).getTitle());
                                stopButton.setVisibility(View.GONE);
                                startButton.setVisibility(View.VISIBLE);

                            }


                        }
                    });

                    stopButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            stopButton.setVisibility(View.GONE);
                            startButton.setVisibility(View.VISIBLE);
                            stopMusic();



                        }
                    });


//                    mRolling.hide();
                    mMediaPlayerHelper.start();
//                    updateSeekBar();
//                    seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
//                        @Override
//                        public void onProgressChanged(SeekBar seekBar, final int progress, boolean fromUser) {
//                            if (fromUser){
//                                mp.seekTo(progress);
//                            }
//                        }

//                        @Override
//                        public void onStartTrackingTouch(SeekBar seekBar) {
//                            isSeek=true;
//                        }

//                        @Override
//                        public void onStopTrackingTouch(SeekBar seekBar) {
//                            isSeek = false;
//                        }
//                    });
                }
            });
        }





    }

    public interface  OnImportMusicListener{
        void onImport(List<Mp3Info> mp3Infos);
    }
    public void stopMusic() {
        mMediaPlayerHelper.pause();
    }
    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onResume() {
        super.onResume();

        if (mSession == null) {
            Exception exception = null;
            String message = null;
            try {
                switch (ArCoreApk.getInstance().requestInstall(this, !bInstallRequested)) {
                    case INSTALL_REQUESTED:
                        bInstallRequested = true;
                        return;
                    case INSTALLED:
                        break;
                }

                // ARCore requires camera permissions to operate. If we did not yet obtain runtime
                // permission on Android M and above, now is a good time to ask the user for it.
                if (!PermissionHelper.hasCameraPermission(this)) {
                    PermissionHelper.requestCameraPermission(this);
                    return;
                }

                mSession = new Session(/* context= */ this);
            } catch (UnavailableArcoreNotInstalledException
                    | UnavailableUserDeclinedInstallationException e) {
                message = "Please install ARCore";
                exception = e;
            } catch (UnavailableApkTooOldException e) {
                message = "Please update ARCore";
                exception = e;
            } catch (UnavailableSdkTooOldException e) {
                message = "Please update this app";
                exception = e;
            } catch (Exception e) {
                message = "This device does not support AR";
                exception = e;
            }

            if (message != null) {
                Log.e(TAG, "Exception creating session", exception);
                return;
            }

            // Create default config and check if supported.
            Config config = new Config(mSession);
            if (!mSession.isSupported(config)) {
                Log.e(TAG, "Exception creating session Device Does Not Support ARCore", exception);
            }
            mSession.configure(config);
        }
        // Note that order matters - see the note in onPause(), the reverse applies here.
        try {
            mSession.resume();
        } catch (CameraNotAvailableException e) {
            e.printStackTrace();
        }
        mSurfaceView.onResume();
        mDisplayRotationHelper.onResume();
        mPaused = false;
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onPause() {
        super.onPause();

        // Note that the order matters - GLSurfaceView is paused first so that it does not try
        // to query the session. If Session is paused before GLSurfaceView, GLSurfaceView may
        // still call mSession.update() and get a SessionPausedException.

        if (mSession != null) {
            mDisplayRotationHelper.onPause();
            mSurfaceView.onPause();
            mSession.pause();
        }

        mPaused = true;


        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        mScreenHeight = displayMetrics.heightPixels;
        mScreenWidth = displayMetrics.widthPixels;
    }

    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        if (mSession == null) {
            return;
        }

        GLES20.glClearColor(0.1f, 0.1f, 0.1f, 1.0f);

        // Create the texture and pass it to ARCore session to be filled during update().
        mBackgroundRenderer.createOnGlThread(/*context=*/this);

        try {

            mSession.setCameraTextureName(mBackgroundRenderer.getTextureId());
            mLineShaderRenderer.createOnGlThread(this);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {
        GLES20.glViewport(0, 0, width, height);
        // Notify ARCore session that the view size changed so that the perspective matrix and
        // the video background can be properly adjusted.
        mDisplayRotationHelper.onSurfaceChanged(width, height);
        mScreenWidth = width;
        mScreenHeight = height;
    }

    @Override
    public void onDrawFrame(GL10 gl) {
        if (mPaused) return;

        update();

        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT | GLES20.GL_DEPTH_BUFFER_BIT);

        if (mFrame == null) {
            return;
        }

        // Draw background.
        mBackgroundRenderer.draw(mFrame);

        // Draw Lines
        if (mFrame.getCamera().getTrackingState() == TrackingState.TRACKING) {
            mLineShaderRenderer.draw(viewmtx, projmtx, mScreenWidth, mScreenHeight, AppSettings.getNearClip(), AppSettings.getFarClip());
        }
    }


    private void update() {

        if (mSession == null) {
            return;
        }

        mDisplayRotationHelper.updateSessionIfNeeded(mSession);

        try {

            mSession.setCameraTextureName(mBackgroundRenderer.getTextureId());

            mFrame = mSession.update();
            Camera camera = mFrame.getCamera();

            mState = camera.getTrackingState();

            // Update tracking states
            if (mState == TrackingState.TRACKING && !bIsTracking.get()) {
                bIsTracking.set(true);
            } else if (mState == TrackingState.STOPPED && bIsTracking.get()) {
                bIsTracking.set(false);
                bTouchDown.set(false);
            }

            // Get projection matrix.
            camera.getProjectionMatrix(projmtx, 0, AppSettings.getNearClip(), AppSettings.getFarClip());
            camera.getViewMatrix(viewmtx, 0);

            float[] position = new float[3];
            camera.getPose().getTranslation(position, 0);

            // Check if camera has moved much, if that's the case, stop touchDown events
            // (stop drawing lines abruptly through the air)
            if (mLastFramePosition != null) {
                Vector3f distance = new Vector3f(position[0], position[1], position[2]);
                distance.sub(new Vector3f(mLastFramePosition[0], mLastFramePosition[1], mLastFramePosition[2]));

                if (distance.length() > 0.15) {
                    bTouchDown.set(false);
                }
            }
            mLastFramePosition = position;

            // Multiply the zero matrix
            Matrix.multiplyMM(viewmtx, 0, viewmtx, 0, mZeroMatrix, 0);


            if (bNewStroke.get()) {
                bNewStroke.set(false);
                addStroke(lastTouch.get());
                mLineShaderRenderer.bNeedsUpdate.set(true);
            } else if (bTouchDown.get()) {
                addPoint(lastTouch.get());
                mLineShaderRenderer.bNeedsUpdate.set(true);
            }

            if (bReCenterView.get()) {
                bReCenterView.set(false);
                mZeroMatrix = getCalibrationMatrix();
            }

            if (bClearDrawing.get()) {
                bClearDrawing.set(false);
                clearDrawing();
                mLineShaderRenderer.bNeedsUpdate.set(true);
            }

            if (bUndo.get()) {
                bUndo.set(false);
                if (mStrokes.size() > 0) {
                    mStrokes.remove(mStrokes.size() - 1);
                    mLineShaderRenderer.bNeedsUpdate.set(true);
                }
            }
            mLineShaderRenderer.setDrawDebug(bLineParameters.get());
            if (mLineShaderRenderer.bNeedsUpdate.get()) {
                mLineShaderRenderer.setColor(AppSettings.getColor());
                mLineShaderRenderer.mDrawDistance = AppSettings.getStrokeDrawDistance();
                mLineShaderRenderer.setDistanceScale(mDistanceScale);
                mLineShaderRenderer.setLineWidth(mLineWidthMax);
                mLineShaderRenderer.clear();
                mLineShaderRenderer.updateStrokes(mStrokes);
                mLineShaderRenderer.upload();
            }


        } catch (Exception e) {
            e.printStackTrace();
        }
    }



    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (!PermissionHelper.hasCameraPermission(this)) {
            Toast.makeText(this,
                    "Camera permission is needed to run this application", Toast.LENGTH_LONG).show();
//            finish();
        }

    }

    private void addStroke(Vector2f touchPoint) {
        Vector3f newPoint = LineUtils.GetWorldCoords(touchPoint, mScreenWidth, mScreenHeight, projmtx, viewmtx);
        addStroke(newPoint);
    }

    private void addPoint(Vector2f touchPoint) {
        Vector3f newPoint = LineUtils.GetWorldCoords(touchPoint, mScreenWidth, mScreenHeight, projmtx, viewmtx);
        addPoint(newPoint);
    }

    private void addStroke(Vector3f newPoint) {
        biquadFilter = new BiquadFilter(mLineSmoothing);
        for (int i = 0; i < 1500; i++) {
            biquadFilter.update(newPoint);
        }
        Vector3f p = biquadFilter.update(newPoint);
        mLastPoint = new Vector3f(p);
//        mLastPoint = newPoint;
        mStrokes.add(new ArrayList<Vector3f>());
        mStrokes.get(mStrokes.size() - 1).add(mLastPoint);
    }

    private void addPoint(Vector3f newPoint) {
        if (LineUtils.distanceCheck(newPoint, mLastPoint)) {
            Vector3f p = biquadFilter.update(newPoint);
            mLastPoint = new Vector3f(p);
//            mLastPoint = newPoint;
            mStrokes.get(mStrokes.size() - 1).add(mLastPoint);
        }
    }

    /**
     * Get a matrix usable for zero calibration (only position and compass direction)
     */
    public float[] getCalibrationMatrix() {
        float[] t = new float[3];
        float[] m = new float[16];

        mFrame.getCamera().getPose().getTranslation(t, 0);
        float[] z = mFrame.getCamera().getPose().getZAxis();
        Vector3f zAxis = new Vector3f(z[0], z[1], z[2]);
        zAxis.y = 0;
        zAxis.normalize();

        double rotate = Math.atan2(zAxis.x, zAxis.z);

        Matrix.setIdentityM(m, 0);
        Matrix.translateM(m, 0, t[0], t[1], t[2]);
        Matrix.rotateM(m, 0, (float) Math.toDegrees(rotate), 0, 1, 0);
        return m;
    }

    /**
     * Clears the Datacollection of Strokes and sets the Line Renderer to clear and update itself
     * Designed to be executed on the GL Thread
     */
    public void clearDrawing() {
        mStrokes.clear();
        mLineShaderRenderer.clear();
    }

    @Override
    public boolean onTouchEvent(MotionEvent tap) {
        this.mDetector.onTouchEvent(tap);

        if (tap.getAction() == MotionEvent.ACTION_DOWN ) {
            lastTouch.set(new Vector2f(tap.getX(), tap.getY()));
            bTouchDown.set(true);
            bNewStroke.set(true);
            return true;
        } else if (tap.getAction() == MotionEvent.ACTION_MOVE || tap.getAction() == MotionEvent.ACTION_POINTER_DOWN) {
            lastTouch.set(new Vector2f(tap.getX(), tap.getY()));
            bTouchDown.set(true);
            return true;
        } else if (tap.getAction() == MotionEvent.ACTION_UP || tap.getAction() == MotionEvent.ACTION_CANCEL) {
            bTouchDown.set(false);
            lastTouch.set(new Vector2f(tap.getX(), tap.getY()));
            return true;
        }

        return super.onTouchEvent(tap);
    }

    @Override
    public boolean onSingleTapConfirmed(MotionEvent e) {
        return false;
    }

    @Override
    public boolean onDoubleTap(MotionEvent e) {
        return false;
    }

    @Override
    public boolean onDoubleTapEvent(MotionEvent e) {
        return false;
    }

    @Override
    public boolean onDown(MotionEvent e) {
        return false;
    }

    @Override
    public void onShowPress(MotionEvent e) {

    }

    @Override
    public boolean onSingleTapUp(MotionEvent e) {
        return false;
    }

    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
        return false;
    }

    @Override
    public void onLongPress(MotionEvent e) {

    }

    @Override
    public void onBackPressed() {
        Intent a = new Intent(Intent.ACTION_MAIN);
        a.addCategory(Intent.CATEGORY_HOME);
        a.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(a);

    }

    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        return false;
    }

    private Bitmap createBitmapFromGLSurface(int x, int y, int w, int h, GL10 gl)
            throws OutOfMemoryError {
        int bitmapBuffer[] = new int[w * h];
        int bitmapSource[] = new int[w * h];
        IntBuffer intBuffer = IntBuffer.wrap(bitmapBuffer);
        intBuffer.position(0);

        try {
            gl.glReadPixels(x, y, w, h, GL10.GL_RGBA, GL10.GL_UNSIGNED_BYTE, intBuffer);
            int offset1, offset2;
            for (int i = 0; i < h; i++) {
                offset1 = i * w;
                offset2 = (h - i - 1) * w;
                for (int j = 0; j < w; j++) {
                    int texturePixel = bitmapBuffer[offset1 + j];
                    int blue = (texturePixel >> 16) & 0xff;
                    int red = (texturePixel << 16) & 0x00ff0000;
                    int pixel = (texturePixel & 0xff00ff00) | red | blue;
                    bitmapSource[offset2 + j] = pixel;
                }
            }
        } catch (GLException e) {
            return null;
        }

        return Bitmap.createBitmap(bitmapSource, w, h, Bitmap.Config.ARGB_8888);
    }


    private void captureBitmap(final BitmapReadyCallbacks bitmapReadyCallbacks) {
        mSurfaceView.queueEvent(new Runnable() {
            @Override
            public void run() {
                EGL10 egl = (EGL10) EGLContext.getEGL();
                GL10 gl = (GL10)egl.eglGetCurrentContext().getGL();
                snapshotBitmap = createBitmapFromGLSurface(0, 0, mSurfaceView.getWidth(), mSurfaceView.getHeight(), gl);

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        bitmapReadyCallbacks.onBitmapReady(snapshotBitmap);
                    }
                });

            }
        });

    }

    public static void SaveBitmapToFileCache(Bitmap bitmap, String strFilePath, String filename) {

        File file = new File(strFilePath);

        if (!file.exists())

            file.mkdirs();


        File fileCacheItem = new File(strFilePath + filename+".jpg");

        OutputStream out = null;

        try {

            fileCacheItem.createNewFile();

            out = new FileOutputStream(fileCacheItem);



            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);

        } catch (Exception e) {

            e.printStackTrace();

        } finally {

            try {

                out.close();

            } catch (IOException e) {

                e.printStackTrace();

            }

        }

    }



}
//        startButton.setVisibility(View.GONE);
//        pauseButton.setVisibility(View.VISIBLE);
//
//    updateSeekBar();


