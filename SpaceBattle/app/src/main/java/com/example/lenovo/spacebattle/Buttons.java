package com.example.lenovo.spacebattle;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.Log;

public class Buttons {
    Button[] arrButtons;
    Buttons(Context context){
        arrButtons = new Button[4];
        String[] texts = {"开始","开火","自动","关闭"};
        for(int i=0; i< arrButtons.length;i++) {
            arrButtons[i] = new Button(context, i);
            arrButtons[i].text = texts[i];
        }
    }
    // 定位所有按钮
    public void pos() {
    }
    // 绘制所有按钮
    public void draw(Canvas canvas) {
        for(Button button : arrButtons){
            button.draw(canvas);
        }
    }
    // 判断哪个按钮被点击
    public String getPressedButton(float x,float y) {
        String answer = "Not hit";
        for(Button button : arrButtons){
            if(button.getPressed(x, y)){
                answer = button.getText();
                break;
            }
        }
        return answer;
    }
}