package com.example.lenovo.spacebattle;

import android.content.Context;
import android.graphics.Canvas;
import android.util.Log;

import java.util.Iterator;
import java.util.concurrent.ConcurrentLinkedQueue;

public class Bullets {
    Context context;
    ConcurrentLinkedQueue<Bullet> lqBullets;
    String myName;


    Bullets(Context context,String myName){
        this.context = context;
        lqBullets = new ConcurrentLinkedQueue<Bullet>();
        this.myName = myName;
    }

    void draw(Canvas canvas, long intl){
        Iterator<Bullet> it = lqBullets.iterator();
        while(it.hasNext()){
            Bullet bullet= it.next();
            if(bullet.active) {
                bullet.draw(canvas,intl);
            }
        }
    }

    Bullet add(String spName, long index, float x, float y, float dir, float step){
        Bullet bullet = new Bullet(context, spName, index, x, y, dir, step);
        lqBullets.add(bullet);
        return bullet;
    }

}
