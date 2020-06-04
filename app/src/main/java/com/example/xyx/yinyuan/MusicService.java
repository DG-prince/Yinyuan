package com.example.xyx.yinyuan;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

public class MusicService extends Service {
    public MediaPlayer mediaPlayer;
    public boolean tag = false;
    private int[] musics={R.raw.music1,R.raw.music2};
    public static int i=0;

    public MusicService() {
    }
    public void onCreate(){
        //super.onCreate();
        mediaPlayer = new MediaPlayer();
        mediaPlayer=MediaPlayer.create(this,musics[i%2]);
        mediaPlayer.setLooping(true);

    }

    //  通过 Binder 来保持 Activity 和 Service 的通信
    public MyBinder binder = new MyBinder();
    public class MyBinder extends Binder {
        MusicService getService() {
            return MusicService.this;
        }
    }

    public void playOrPause() {
        if (mediaPlayer.isPlaying()) {
            mediaPlayer.pause();
        } else {
            mediaPlayer.start();
        }
    }

    public void next() {
        if (mediaPlayer != null) {
            mediaPlayer.stop();
            try {
                mediaPlayer.reset();
                i=i+1;
                Log.d("music",i+"play");
                mediaPlayer = new MediaPlayer();
                mediaPlayer=MediaPlayer.create(this,musics[i%2]);
                mediaPlayer.setLooping(true);

                mediaPlayer.seekTo(0);
                mediaPlayer.start();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public boolean onUnbind(Intent intent) {
        mediaPlayer.stop();
        return super.onUnbind(intent);
    }

    @Override
    public IBinder onBind(Intent intent) {
        return binder;
    }
}
