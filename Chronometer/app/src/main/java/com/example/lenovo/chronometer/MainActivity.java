package com.example.lenovo.chronometer;

import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutCompat;
import android.view.View;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    private Chronometer ch;
    private long recordingTime = 0;
    private int i = 0, j = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ch = (Chronometer) findViewById(R.id.chronometer);
        final Button start = (Button) findViewById(R.id.button1);
        final Button pause = (Button) findViewById(R.id.button2);
        final Button reset = (Button) findViewById(R.id.button3);
        final TextView tv = (TextView) findViewById(R.id.text1);

        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                j -= 2;
                ch.setBase(SystemClock.elapsedRealtime() - recordingTime);
                ch.start();
                pause.setEnabled(true);
                start.setEnabled(false);
            }
        });

        pause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                j -= 1;
                ch.stop();
                recordingTime = SystemClock.elapsedRealtime() - ch.getBase();
                start.setEnabled(true);
                pause.setEnabled(false);
            }
        });

        reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String time = ch.getText().toString();
                reset.setEnabled(false);
                recordingTime = 0;
                ch.setBase(SystemClock.elapsedRealtime());
            }
        });

        //ch.setVisibility(View.GONE);//隐藏chronometer
        ch.setOnChronometerTickListener(new Chronometer.OnChronometerTickListener() {
            @Override
            public void onChronometerTick(Chronometer chronometer) {
                String time = ch.getText().toString();
                if (!time.equals("00:00"))
                    reset.setEnabled(true);
                else
                    reset.setEnabled(false);
                j += 1;
                if (j == 60) {
                    j = 0;
                    i += 1;
                }
                if (i == 60) {
                    i = 0;
                    j = 0;
                }
                tv.setText(String.format("%02d:%02d", i, j));
            }
        });
    }
}
