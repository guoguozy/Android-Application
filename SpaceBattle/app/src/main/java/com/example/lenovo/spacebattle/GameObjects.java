package com.example.lenovo.spacebattle;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.Log;
import android.view.SurfaceHolder;

public class GameObjects {
    private Context context;
    private Canvas canvas;
    Buttons buttons;
    Sprites sprites;
    Sprite mySprite = null;  // 本玩家精灵
    String myName = null;    // 本玩家精灵的名字
    Sprite otherSprite = null;
    boolean existed;
    boolean other_existed;
    boolean networkMode = false;

    GameObjects(Context context, Canvas canvas) {
        this.context = context;
        this.canvas = canvas;
        buttons = new Buttons(context);
        buttons.pos();
        existed = false;
        other_existed = false;
    }

    void draw(long loopTime) {
        drawBackground(canvas);
        buttons.draw(canvas);
        if (existed) {
            mySprite.draw(canvas, loopTime);
            otherSprite.draw(canvas, loopTime);
        }
    }

    void drawBackground(Canvas canvas) {
        if (canvas == null) return;
    }

    String getPressedButton(float x, float y) {
        return buttons.getPressedButton(x, y);
    }

    void addSprite() {
        if (!existed) {
            mySprite = new Sprite(context);
            existed = true;
        } else {
            otherSprite = new Sprite(context);
            otherSprite.ai=true;
            other_existed = false;
        }
    }

    boolean isExistedSprite() {
        return existed;
    }

}

