package com.example.lenovo.musicplay;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.content.Intent;
import android.content.IntentFilter;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Spinner;
import android.widget.Toast;

import static android.media.MediaPlayer.create;

public class MainActivity extends AppCompatActivity {
    private MyReceiver receiver = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        receiver = new MyReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction("android.intent.action.FIRST_SERVICE");
        MainActivity.this.registerReceiver(receiver, filter);

        Spinner spinner=(Spinner)findViewById(R.id.spinner1);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){
            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {
                Intent intent = new Intent();
                intent.putExtra("position", position);
                intent.setAction("android.intent.action.FIRST_SERVICE");
                sendBroadcast(intent);   //发送广播
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

}
