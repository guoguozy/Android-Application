package com.example.lenovo.spacebattle;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Point;
import android.util.Log;

public class Sprite {
    final float HEIGHT = 120;   // 精灵高度（虚拟尺寸）。final变量(Java常量)只能赋一次值
    final float WIDTH = 120;    // 精灵宽度（虚拟尺寸）
    final int FONT_SIZE = 24;   // 字体大小（虚拟尺寸）
    final long FRAME_DURATION = 1 * Global.LOOP_TIME;  // 每帧持续时间（ms）
    float step = 10;           // 每个LOOP_TIME的移动步幅（用于控制速度）取值1~ Any(虚拟单位)
    String spName;              // 精灵名称
    float x;                    // 精灵x轴定位
    float y;                    // 精灵y轴定位
    float dir;                  // 移动方向，单位：degree
    boolean me = true;          // 区分自己的精灵还是其它用户的精灵
    boolean active = true;     // 是否活动（可见）。重用（reuse）方式可用
    int curFrameIndex;       // 当前帧号，取值0~7
    long frameDuration = 0;   // 当前帧已显示时间
    boolean ai=false;  // 是否启动了自动模式（托管模式）
    Bullets bullets;

    int[] frames = { R.drawable.sprite1,R.drawable.sprite2,R.drawable.sprite3,
            R.drawable.sprite4,R.drawable.sprite5,R.drawable.sprite6,
            R.drawable.sprite7,R.drawable.sprite8
    };
    Paint paint1;   // 精灵图像画笔
    Paint paint2;   // 精灵文字画笔
    Context context;
    Sprite(Context context){
        this.context = context;
        paint1 = new Paint();
        paint2 = new Paint();
        paint2.setColor(Color.argb(60,50,50,50));
        paint2.setTextSize(Global.v2Rx(FONT_SIZE));
        bullets = new Bullets(context, this.spName);
        curFrameIndex = 0;
        x = 0;
        y = 0;
        spName = "0001";
        bullets = new Bullets(this.context, this.spName);
    }
    // 绘制精灵：先计算下一帧的索引，计算精灵的位置，再绘制精灵
    void draw(Canvas canvas, long loopTime){
        pos(loopTime);
        if(canvas==null) return;
        nextFrame(loopTime);
        Bitmap mBitmap = BitmapFactory.decodeResource(context.getResources(),frames[curFrameIndex]);
        int bitWidth = mBitmap.getWidth();
        int bitHeight = mBitmap.getHeight();
        float bitVWidth = Global.v2Rx(WIDTH);
        float bitVHeight = Global.v2Ry(HEIGHT);

        float sx =  bitVWidth / bitWidth;
        float sy = bitVHeight / bitHeight;
        Matrix matrix = new Matrix();
        matrix.setScale(sx, sy);

        Bitmap newBitmap = Bitmap.createBitmap(mBitmap, 0, 0, bitWidth,bitHeight, matrix, true);
        newBitmap = rotateSprite(newBitmap, dir);
        canvas.drawBitmap(newBitmap, x, y, paint1);
        Point po = getTextPosition();
        canvas.drawText(spName, po.x, po.y, paint2);
        bullets.draw(canvas, loopTime);
    }

    // 设置精灵图像画笔和文字画笔
    void setImagePaint(){

    }

    // 通过累计frameDuration计算下一个帧的索引
    private void nextFrame(long loopTime){
        frameDuration += loopTime;
        frameDuration = frameDuration % (FRAME_DURATION * 8);
        curFrameIndex = Math.round(frameDuration / FRAME_DURATION);
        curFrameIndex = curFrameIndex % 8;
        //Log.d("index", String.valueOf(curFrameIndex));
    }

    private void pos(long loopTime){
        float step1 = this.step * loopTime/Global.LOOP_TIME;
        x = x + step1 * (float)Math.cos(dir * Math.PI / 180);
        y = y + step1 * (float)Math.sin(dir * Math.PI / 180);
        if(x <= 0){
            x = 0;
        }else if(x >= Global.realW){
            x = Global.realW;
        }

        if(y <= 0){
            y = 0;
        }else if(y >= Global.realH){
            y = Global.realH;
        }
    }

    float getDirection(float left,float top, float newLeft, float newTop){
        float dx = newLeft - left;
        float dy = newTop - top;
        double dist = Math.sqrt(dx*dx+dy*dy);
        float theta=0;
        if((int)dist!=0) {
            theta = (float) Math.asin(Math.abs(dy) / dist) * 180.0f / (float)Math.PI;
        }
        else {
            theta = 0;
        }
        if(dx<=0 && dy>=0){
            theta= 180-theta;
        }
        if(dx<=0 && dy<=0){
            theta= 180+theta;
        }
        if(dx>=0 && dy<=0){
            theta= 360-theta;
        }
        return theta;
    }

    Bitmap rotateSprite(Bitmap bitmap, float degree){
        float deg = degree;
        Matrix matrix = new Matrix();

        if(degree>=0 && degree<=90) {
        }
        if(degree>=90 && degree<=180) {
            matrix.postTranslate(-bitmap.getWidth(),0);
            matrix.postScale(-1, 1);    //镜像水平翻转. 垂直(1, -1)
            deg = degree-180;
        }
        if(degree>=180 && degree<=270) {
            matrix.postTranslate(-bitmap.getWidth(),0);
            matrix.postScale(-1, 1);    //镜像水平翻转. 垂直(1, -1)
            deg = degree-180;
        }
        if(degree>=270 && degree<=360) {
            deg = degree-360;
        }
        Bitmap tempBmp = Bitmap.createBitmap(bitmap.getWidth(),bitmap.getHeight(),Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(tempBmp);
        matrix.postRotate(deg,bitmap.getWidth()/2,bitmap.getHeight()/2);    //旋转-deg度
        canvas.drawBitmap(bitmap,matrix,null);
        return tempBmp;
    }

    Bullet shot(){
        Point point = getShotStPos();
        return bullets.add(spName,-1,point.x,point.y,dir,step*3);  //-1为序号
    }

    // 根据精灵的位置定位新子弹的初始位置，除了精灵的大小，还需根据精灵的方向做出调整?
    Point getShotStPos(){
        double x1;
        double y1;
        float bitVWidth = Global.v2Rx(WIDTH);
        float bitVHeight = Global.v2Ry(HEIGHT);
        x1 = x + bitVWidth/2;
        y1 = y + bitVHeight/2;
        return new Point((int)x1,(int)y1);
    }

    Point getTextPosition(){
        return new Point((int)x, (int)y);
    }
}
