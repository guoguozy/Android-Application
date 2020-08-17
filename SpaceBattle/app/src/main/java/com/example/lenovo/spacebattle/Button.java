package com.example.lenovo.spacebattle;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.Log;

public class Button {
    Context context;
    final float RADIUS = 80;  // 半径 （虚拟单位）
    final int FONT_SIZE = 48; // 字体大小（虚拟单位）
    String text;               // 按钮文本
    float centerX;
    float centerY;
    Paint paint1;
    Paint paint2;
    int position;

    Button(Context context,int position){
        this.context = context;
        paint1 = new Paint();
        paint1.setColor(Color.argb(128,100,160,100));
        paint2 = new Paint();
        this.position = position;
        paint2.setColor(Color.argb(128,50,50,50));
        paint2.setTextSize(Global.v2Rx(FONT_SIZE)); //px
    }

    public void draw(Canvas canvas){
        if(canvas==null){
            return;
        }
        int y = 1750;
        int x = 168 + position * 248;
        this.centerX = Global.v2Rx(x);
        this.centerY = Global.v2Ry(y);
        canvas.drawCircle(this.centerX, this.centerY, Global.v2Rx(RADIUS), paint1);// Global.v2Rx！！！
        canvas.drawText(text,Global.v2Rx(x-48), Global.v2Ry(y+24),paint2);
    }

    public boolean getPressed(float x, float y){
        return Math.sqrt((x-centerX)*(x-centerX)
                +(y-centerY)*(y-centerY))<RADIUS;
    }

    String getText(){
        return text;
    }
}
