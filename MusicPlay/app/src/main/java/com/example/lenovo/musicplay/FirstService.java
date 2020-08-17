package com.example.lenovo.musicplay;

import android.app.IntentService;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;

import java.util.ArrayList;

import static android.media.MediaPlayer.create;

public class FirstService extends IntentService {
    static private MediaPlayer mp;
    private ArrayList<Integer> songs;
    public FirstService() {

        super("FirstService");
        songs = new ArrayList<>();
        songs.add(R.raw.summer);
        songs.add(R.raw.waka);
        songs.add(R.raw.wish);
    }

    // IntentService会使用单独的线程来执行该方法的代码
    @Override
    protected void onHandleIntent(Intent intent) {
        // 该方法内可以执行任何耗时任务，比如下载文件等，此处只是让线程暂停20秒
        Bundle bundle = intent.getExtras();
        Log.d("sss", "启动服务");
        int position;
        if(bundle == null){
            position = 1;
        }
        else{
            position = bundle.getInt("position");
        }

        if(position == 0 && mp != null){
            mp.stop();
        }
        else if(position == 0 && mp == null){

        }
        else{
            try{
                if(mp!=null) {
                    mp.stop();
                    mp.reset();
                }
                mp = create(FirstService.this, songs.get(position-1));
                mp.setAudioStreamType(AudioManager.STREAM_MUSIC);
                mp.setLooping(true);
                mp.start();
                mp.setVolume(14.0f, 14.0f);
            }catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
