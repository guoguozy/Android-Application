package com.example.lenovo.cardviewsimple;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.widget.SeekBar;

public class MainActivity extends AppCompatActivity {

   /* app:cardBackgroundColor=""设置背景色
    app:cardMaxElevation="" 设置Z轴最大高度值
    app:cardUseCompatPadding="" 是否使用CompatPadding
    app:cardPreventCornerOverlap="" 是否使用PreventCornerOverlap
    app:contentPadding="" 内容的Padding
    app:contentPaddingTop="" 内容的上Padding
    app:contentPaddingLeft="" 内容的左Padding
    app:contentPaddingRight="" 内容的右Padding
    app:contentPaddingBottom="" 内容的下Padding*/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final CardView mCardview = (CardView) findViewById(R.id.cv_cardview);
        final CardView mCardview1 = (CardView) findViewById(R.id.cv_cardview1);
        SeekBar sb_1 = (SeekBar) findViewById(R.id.sb_1);
        SeekBar sb_2 = (SeekBar) findViewById(R.id.sb_2);
        SeekBar sb_3 = (SeekBar) findViewById(R.id.sb_3);

        sb_1.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                mCardview.setRadius(i);//设置圆角半径
                mCardview1.setRadius(i);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        sb_2.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                mCardview.setCardElevation(i);//设置阴影半径
                mCardview1.setCardElevation(i);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        sb_3.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                mCardview.setContentPadding(i, i, i, i);//设置cardView中子控件和父控件的距离
                mCardview1.setContentPadding(i, i, i, i);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }
}
