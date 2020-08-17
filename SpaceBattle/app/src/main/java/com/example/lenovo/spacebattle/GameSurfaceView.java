package com.example.lenovo.spacebattle;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import org.w3c.dom.Attr;

import static android.media.MediaPlayer.create;

public class GameSurfaceView extends SurfaceView implements SurfaceHolder.Callback,Runnable{
    Context context;                // 带入Activity
    private SurfaceHolder holder;
    private boolean isRun=true;
    private Bitmap mBitmap= null;
    final float PERIOD = 16;
    Buttons buttons;
    GameObjects gameObjects = null;
    MediaPlayer mp = null;
    Thread myThread;
    boolean networkMode = false;
    public GameSurfaceView(Context context) {
        super(context);
        this.context = context;   //可以采用(MainActivity)context得到Activity
        init();
    }

    public GameSurfaceView(Context context, AttributeSet attr) {
        super(context, attr);
        this.context = context;   //可以采用(MainActivity)context得到Activity
        init();
    }

    public void init(){
        // TODO Auto-generated constructor stub
        holder = this.getHolder();
        holder.addCallback(this);         // 给SurfaceView当前的持有者一个回调对象
        setFocusable(true);
        setFocusableInTouchMode(true);
        this.setKeepScreenOn(true);
        holder.setFormat(PixelFormat.TRANSPARENT);
        myThread = new Thread(this); // 创建一个绘图线程
        myThread.start();
    }

    @Override    // 在surface的大小发生改变时激发
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
    }

    @Override  // 在创建时激发，一般在这里调用画图线程
    public void surfaceCreated(SurfaceHolder holder) {
        isRun = true;
        Global.realW = getWidth();    // 获取屏幕宽度
        Global.realH = getHeight();   // 获取屏幕高度
        buttons = new Buttons(context);
        System.out.println("Real Width: "+Global.realW+"Real Height: "+ Global.realH);
    }

    @Override  // 销毁时激发，一般在这里将画图的线程停止、释放
    public void surfaceDestroyed(SurfaceHolder holder) {
        isRun = false;
    }

    @Override
    public void run() {
        long start = 0;    // 开始时间
        while (isRun) {
            start = System.currentTimeMillis();
            Canvas canvas = null;
            try {
                canvas = holder.lockCanvas();//锁定画布并返回画布对象Canvas
                Paint p = new Paint(); //创建画笔
                mBitmap = BitmapFactory.decodeResource(this.getResources(), R.drawable.cloud);
                if (canvas != null) {
                    if (mBitmap != null) {
                        Matrix matrix = new Matrix();
                        int w = mBitmap.getWidth();
                        int h = mBitmap.getHeight();
                        float sx = Global.realW / w;
                        float sy = Global.realH / h;
                        matrix.setScale(sx, sy);
                        canvas.drawBitmap(mBitmap, matrix, p);
                        if (gameObjects == null) {
                            gameObjects = new GameObjects(context, canvas);
                        }
                        gameObjects.draw(50);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (canvas != null) {
                    holder.unlockCanvasAndPost(canvas);
                    sleep((float)System.currentTimeMillis()-start);  // 睡眠一段时间
                }
            }
        }
    }
    // 睡眠一段时间，使每次循环的时间为PERIOD
    void sleep(float runTime){
        try {
            float leftTime = PERIOD - runTime;  // 剩余时间
            Thread.sleep(leftTime>0 ? (int)leftTime : 0);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);
    }

    public boolean onTouchEvent(MotionEvent event){
        int action = event.getAction();
        switch (action){
            case MotionEvent.ACTION_DOWN:
                float x = event.getX();
                float y = event.getY();
                String result = gameObjects.getPressedButton(x, y);
                Log.i("System.out", "按了[" + result + "]按钮");
                if(result.equals("关闭")){
                    if(mp != null){
                        mp.release();
                    }
                    ((MainActivity)context).finish();
                }
                else if(result.equals("开始")){
                    gameObjects.addSprite();
                }
                else if(result.equals("开火")){
                    if(gameObjects.isExistedSprite()){
                        gameObjects.mySprite.shot();
                        if(mp == null){
                            mp = create(context, R.raw.bullet);
                            mp.setAudioStreamType(AudioManager.STREAM_MUSIC);
                            mp.setVolume(14.0f, 14.0f); //声音调不了
                        }
                        mp.start();

                    }
                }
                else if(result.equals("自动")){

                }
                else{
                    if(gameObjects.isExistedSprite()){
                        gameObjects.mySprite.dir = gameObjects.mySprite.getDirection(gameObjects.mySprite.x, gameObjects.mySprite.y, x, y);
                    }
                }
                break;
            case MotionEvent.ACTION_MOVE:
                break;
            case MotionEvent.ACTION_UP:
                break;
            default:
                break;
        }
        return true;
    }
}