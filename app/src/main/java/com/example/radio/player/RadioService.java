package com.example.radio.player;


import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.net.wifi.WifiManager;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.media.MediaMetadataCompat;
import android.support.v4.media.session.MediaControllerCompat;
import android.support.v4.media.session.MediaSessionCompat;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Log;

import com.example.login_activity.R;
import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.PlaybackParameters;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.Timeline;
import com.google.android.exoplayer2.ext.rtmp.RtmpDataSourceFactory;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.TrackGroupArray;
import com.google.android.exoplayer2.source.hls.HlsDataSourceFactory;
import com.google.android.exoplayer2.source.hls.HlsMediaSource;
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelectionArray;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory;
import com.google.android.exoplayer2.util.Util;


import org.greenrobot.eventbus.EventBus;

public class RadioService extends Service implements Player.EventListener, AudioManager.OnAudioFocusChangeListener {

    public static final String ACTION_PLAY = "com.mcakir.radio.player.ACTION_PLAY";
    public static final String ACTION_PAUSE = "com.mcakir.radio.player.ACTION_PAUSE";
    public static final String ACTION_STOP = "com.mcakir.radio.player.ACTION_STOP";

    private final IBinder iBinder = new LocalBinder();


    public static int Music_status=0;
    private Handler handler;
    private final DefaultBandwidthMeter BANDWIDTH_METER = new DefaultBandwidthMeter();
    private SimpleExoPlayer exoPlayer;
    private MediaSessionCompat mediaSession;
    private MediaControllerCompat.TransportControls transportControls;

    private boolean onGoingCall = false;
    private TelephonyManager telephonyManager;
    //단말기 정보를 얻어 오는 메서드

    private WifiManager.WifiLock wifiLock;
    //내주변에 있는 와이파이를 가져온다

    private AudioManager audioManager;
    //오디오 관련

    private MediaNotificationManager notificationManager;
    //앱자체내에서 알림을 발생시키고 싶을때 사용함

    private String status;

    private String strAppName;
    private String strLiveBroadcast;
    private String streamUrl; //방송 url
    private String player; //각 방송이 어떤 프로토콜을 사용하는지 ex) rtmp, rtsp, http 등의 정보를 가져옴

    public class LocalBinder extends Binder {
        public RadioService getService() {
            return RadioService.this;
        }
    }

    private BroadcastReceiver becomingNoisyReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {

            pause();
        }
    };

    private PhoneStateListener phoneStateListener = new PhoneStateListener() {
        //핸드폰이 전화상태인지 알아내는 메서드

        @Override
        public void onCallStateChanged(int state, String incomingNumber) {
            //전화상태인지 알아낸다
            if(state == TelephonyManager.CALL_STATE_OFFHOOK
                    || state == TelephonyManager.CALL_STATE_RINGING){

                if(!isPlaying()) return;

                onGoingCall = true;
                stop();

                //전화상태이면 잠시 라디오방송을 멈추고

                /*
                 * CALL_STATE_OFFHOOK의 경우 전화를 걸거나 전화중인 상태
                 * CALL_STATE_RINGING의 경우 전화가 오고있는상태
                 */

            } else if (state == TelephonyManager.CALL_STATE_IDLE){
                //아무상태가 아닐경우 라디오를 계속 틈

                if(!onGoingCall) return;

                onGoingCall = false;
                resume();
            }
        }
    };

    private MediaSessionCompat.Callback mediasSessionCallback = new MediaSessionCompat.Callback() {
        @Override
        public void onPause() {
            super.onPause();

            pause();
        }

        @Override
        public void onStop() {
            super.onStop();

            stop();

            notificationManager.cancelNotify();
        }

        @Override
        public void onPlay() {
            super.onPlay();

            resume();
        }
    };

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {

        return iBinder;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        strAppName = getResources().getString(R.string.app_name);
        strLiveBroadcast = getResources().getString(R.string.live_broadcast);

        onGoingCall = false;

        audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);

        notificationManager = new MediaNotificationManager(this);

        wifiLock = ((WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE))
                .createWifiLock(WifiManager.WIFI_MODE_FULL, "mcScPAmpLock");

        /*
         * 와이파이 락을 거는 이유
         * 안드로이드는 대기 모드에 들어가게되면 배터리 소모를 줄이기 위해 wifi를 자동으로 꺼버린다
         * 대기모드 상태에서 자동으로 꺼버리게 되면 라디오도 재생되는게 정지되기때매 와이파이의 상태를 유지시키기위해
         * 와이파이락을 검
         * */

        mediaSession = new MediaSessionCompat(this, getClass().getSimpleName());
        transportControls = mediaSession.getController().getTransportControls();
        mediaSession.setActive(true);
        mediaSession.setFlags(MediaSessionCompat.FLAG_HANDLES_MEDIA_BUTTONS | MediaSessionCompat.FLAG_HANDLES_TRANSPORT_CONTROLS);
        mediaSession.setMetadata(new MediaMetadataCompat.Builder()
                .putString(MediaMetadataCompat.METADATA_KEY_ARTIST, "...")
                .putString(MediaMetadataCompat.METADATA_KEY_ALBUM, strAppName)
                .putString(MediaMetadataCompat.METADATA_KEY_TITLE, strLiveBroadcast)
                .build());
        mediaSession.setCallback(mediasSessionCallback);



        telephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        telephonyManager.listen(phoneStateListener, PhoneStateListener.LISTEN_CALL_STATE);
        //전화생태를 받아옴


        handler = new Handler();
        DefaultBandwidthMeter bandwidthMeter = new DefaultBandwidthMeter();
        //데이터 전송을 수신하여 대역폭을 예측함

        AdaptiveTrackSelection.Factory trackSelectionFactory = new AdaptiveTrackSelection.Factory(bandwidthMeter);
        DefaultTrackSelector trackSelector = new DefaultTrackSelector(trackSelectionFactory);
        exoPlayer = ExoPlayerFactory.newSimpleInstance(getApplicationContext(), trackSelector);
        exoPlayer.addListener(this);

        registerReceiver(becomingNoisyReceiver, new IntentFilter(AudioManager.ACTION_AUDIO_BECOMING_NOISY));

        status = PlaybackStatus.IDLE;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        String action = intent.getAction();

        if(TextUtils.isEmpty(action))
            return START_NOT_STICKY;

        int result = audioManager.requestAudioFocus(this, AudioManager.STREAM_MUSIC, AudioManager.AUDIOFOCUS_GAIN);
        if(result != AudioManager.AUDIOFOCUS_REQUEST_GRANTED){

            stop();

            return START_NOT_STICKY;
        }

        if(action.equalsIgnoreCase(ACTION_PLAY)){

            transportControls.play();

        } else if(action.equalsIgnoreCase(ACTION_PAUSE)) {

            transportControls.pause();

        } else if(action.equalsIgnoreCase(ACTION_STOP)){

            transportControls.stop();

        }

        return START_NOT_STICKY;
    }

    @Override
    public boolean onUnbind(Intent intent) {

        if(status.equals(PlaybackStatus.IDLE))
            stopSelf();

        return super.onUnbind(intent);
    }

    @Override
    public void onRebind(final Intent intent) {

    }

    @Override
    public void onDestroy() {

        pause();

        exoPlayer.release();
        exoPlayer.removeListener(this);

        if(telephonyManager != null)
            telephonyManager.listen(phoneStateListener, PhoneStateListener.LISTEN_NONE);

        notificationManager.cancelNotify();

        mediaSession.release();

        unregisterReceiver(becomingNoisyReceiver);

        super.onDestroy();
    }

    @Override
    public void onAudioFocusChange(int focusChange) {

        switch (focusChange) {
            case AudioManager.AUDIOFOCUS_GAIN:

                exoPlayer.setVolume(0.8f);

                resume();

                break;

            case AudioManager.AUDIOFOCUS_LOSS:

                stop();

                break;

            case AudioManager.AUDIOFOCUS_LOSS_TRANSIENT:

                if (isPlaying()) pause();

                break;

            case AudioManager.AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK:

                if (isPlaying())
                    exoPlayer.setVolume(0.1f);

                break;
        }

    }

    @Override
    public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {
    //플레이어가 현재 어떤 상태인지 체크함


        switch (playbackState) {
            case Player.STATE_BUFFERING:
                status = PlaybackStatus.LOADING;
                Log.e("gggggg","ggggggggggggg");

                break;
            //플레이어는 현재 위치에서 재생을 할수 없음

            case Player.STATE_ENDED:
                status = PlaybackStatus.STOPPED;
                break;
            //플레이어가 미디어 재생을 마쳤음

            case Player.STATE_IDLE:
                status = PlaybackStatus.IDLE;
                break;
                //플레이어는 현재 대기상태

            case Player.STATE_READY:
                status = playWhenReady ? PlaybackStatus.PLAYING : PlaybackStatus.PAUSED;
                break;
                //플레이어는 현재 준비상태

            default:
                status = PlaybackStatus.IDLE;
                break;
        }

        if(!status.equals(PlaybackStatus.IDLE))
            notificationManager.startNotify(status);


        EventBus.getDefault().post(status);
    }

    @Override
    public void onTimelineChanged(Timeline timeline, Object manifest, int reason) {

    }

    @Override
    public void onTracksChanged(TrackGroupArray trackGroups, TrackSelectionArray trackSelections) {

    }

    @Override
    public void onLoadingChanged(boolean isLoading) {

    }

    @Override
    public void onPlayerError(ExoPlaybackException error) {
        EventBus.getDefault().post(PlaybackStatus.ERROR);
    }

    @Override
    public void onRepeatModeChanged(int repeatMode) {

    }

    @Override
    public void onShuffleModeEnabledChanged(boolean shuffleModeEnabled) {

    }

    @Override
    public void onPositionDiscontinuity(int reason) {

    }

    @Override
    public void onPlaybackParametersChanged(PlaybackParameters playbackParameters) {

    }

    @Override
    public void onSeekProcessed() {

    }

    public void play(final String streamUrl, String player) {
    //오디오를 재생시키는 메서드

        this.streamUrl = streamUrl;  //재생 url을 받아옴
        this.player=player; //해당 오디오주소가 어떤 프로토콜을 사용하는지 파악함

//        Log.e("myplayer",)
        if (wifiLock != null && !wifiLock.isHeld()) {
            wifiLock.acquire();
            //와이파이를 계속해서 켜둠

        }
//        DefaultHttpDataSourceFactory dataSourceFactory = new DefaultHttpDataSourceFactory(getUserAgent());


//        RtmpDataSourceFactory rtmpDataSourceFactory = new RtmpDataSourceFactory();
// This is the MediaSource representing the media to be played.
//        MediaSource mediaSource = new ExtractorMediaSource.Factory(rtmpDataSourceFactory)
//                .createMediaSource(Uri.parse(streamUrl));


        //해당 오디오주소가 rtmp 프로토콜을 사용할경우
        if(player.equals("rtmp")){

            RtmpDataSourceFactory rtmpDataSourceFactory = new RtmpDataSourceFactory();
            MediaSource mediaSource = new ExtractorMediaSource.Factory(rtmpDataSourceFactory).createMediaSource(Uri.parse(streamUrl));
            exoPlayer.prepare(mediaSource);
            exoPlayer.setPlayWhenReady(true);


            //해당 오디오 주소가 http 프로토콜을 사용할경우
        }else if(player.equals("default")){


            DefaultDataSourceFactory dataSourceFactory = new DefaultDataSourceFactory(this, getUserAgent(), BANDWIDTH_METER);
            ExtractorMediaSource mediaSource = new ExtractorMediaSource.Factory(dataSourceFactory)
                    .setExtractorsFactory(new DefaultExtractorsFactory())
                    .createMediaSource(Uri.parse(streamUrl));

            exoPlayer.prepare(mediaSource);
            exoPlayer.setPlayWhenReady(true);

        }else if(player.equals("hls")){
            DataSource.Factory dataSourceFactory = new DefaultDataSourceFactory(this,getUserAgent());
            MediaSource mediaSource = new HlsMediaSource.Factory(dataSourceFactory).createMediaSource(Uri.parse(streamUrl));
            exoPlayer.prepare(mediaSource);
            exoPlayer.setPlayWhenReady(true);


        }


    }

    public void resume() {

        if(streamUrl != null)
            play(streamUrl,player);
    }

    public void pause() {

        exoPlayer.setPlayWhenReady(false);

        audioManager.abandonAudioFocus(this);
        wifiLockRelease();
    }

    public void stop() {

        exoPlayer.stop();

        audioManager.abandonAudioFocus(this);
        wifiLockRelease();
    }

    public void playOrPause(String url,String player){
        this.player =player;
        Log.e("whatispalter",player);
        if(streamUrl != null && streamUrl.equals(url)){

            if(!isPlaying()){

                play(streamUrl,player);

            } else {

                pause();
            }

        } else {

            if(isPlaying()){

                pause();

            }

            play(url,player);
        }
    }

    public String getStatus(){

        return status;
    }

    public MediaSessionCompat getMediaSession(){

        return mediaSession;
    }

    public boolean isPlaying(){

        Log.e("playeingcheck","playingCheckt");

        return this.status.equals(PlaybackStatus.PLAYING);
    }

    private void wifiLockRelease(){

        if (wifiLock != null && wifiLock.isHeld()) {

            wifiLock.release();
        }
    }

    private String getUserAgent(){

        return Util.getUserAgent(this, getClass().getSimpleName());
    }
}
