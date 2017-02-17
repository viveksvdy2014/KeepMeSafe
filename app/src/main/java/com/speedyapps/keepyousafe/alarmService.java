package com.speedyapps.keepyousafe;

import android.app.Service;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.IBinder;
import android.provider.MediaStore;
import android.widget.MediaController;

public class alarmService extends Service {
    MediaPlayer mediaPlayer;
    public alarmService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
    return null;
    }

    @Override
    public void onCreate() {

        super.onCreate();
        mediaPlayer = MediaPlayer.create(this,R.raw.danger);
       // forceFullVolume();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        mediaPlayer.start();

      //  forceFullVolume();
        return super.onStartCommand(intent, flags, startId);
    }

    public void forceFullVolume(){
        final AudioManager mAudioManager = (AudioManager) getSystemService(AUDIO_SERVICE);
        mAudioManager.setStreamVolume(AudioManager.STREAM_MUSIC, mAudioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC), 0);
        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener()
        {
            @Override
            public void onCompletion(MediaPlayer mediaPlayer1)
            {
                mediaPlayer1.start();
            }
        });
    }
    @Override
    public void onDestroy() {
        final AudioManager mAudioManager = (AudioManager) getSystemService(AUDIO_SERVICE);
        final int originalVolume = mAudioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
        mAudioManager.setStreamVolume(AudioManager.STREAM_MUSIC, originalVolume, 0);
        mediaPlayer.stop();
        super.onDestroy();
    }
}
