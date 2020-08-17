package com.example.lenovo.spacebattle;

import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.content.Intent;
import android.util.Log;
import android.view.MotionEvent;

public class MainActivity extends AppCompatActivity {
    GameSurfaceView gameSurfaceView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        gameSurfaceView = (GameSurfaceView)findViewById(R.id.bg);
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

    }

    @Override
    protected void onStart(){
        super.onStart();
        Intent intent = new Intent();
        intent.putExtra("position", 2);
        intent.setAction("android.intent.action.FIRST_SERVICE");
        sendBroadcast(intent);   //发送广播
    }


    @Override
    protected void onStop(){
        super.onStop();
        Intent intent = new Intent();
        intent.putExtra("position", 0);
        intent.setAction("android.intent.action.FIRST_SERVICE");
        sendBroadcast(intent);   //发送广播

    }

}
