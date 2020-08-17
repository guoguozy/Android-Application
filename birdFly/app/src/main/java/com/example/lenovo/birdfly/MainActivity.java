package com.example.lenovo.birdfly;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.graphics.drawable.AnimationDrawable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Annotation;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.LinearInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private ImageView imageView, circle;//鸟，红点
    private MyView border;//Myview
    private boolean direction = true, head_direction = true, flying = false, recording = false;
    //direction代表运动方向，head_direction代表头朝方向，flying代表飞行状态，recording代表录像状态
    private ArrayList<Pos> paths;//记录路径
    private Pos curPos;//保存录像前位置
    private boolean last_head_direction = false, stop = false;//用于保存录像前头朝方向，停止与否
    private int index = 0;//用于遍历路径

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        border = (MyView) findViewById(R.id.border);
        paths = new ArrayList<>();

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(false);

        imageView = (ImageView) findViewById(R.id.sprite);
        final AnimationDrawable sprite = (AnimationDrawable) imageView.getBackground();
        sprite.start();

        circle = (ImageView) findViewById(R.id.circle);
        final AnimationDrawable redpoint = (AnimationDrawable) circle.getBackground();
        circle.setVisibility(View.INVISIBLE);
        redpoint.start();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_actionbar, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.start:
                circle.setVisibility(View.VISIBLE);
                recording = true;
                paths.clear();
                paths.add(new Pos(imageView.getX(), imageView.getY(), direction, head_direction));
                break;
            case R.id.end:
                circle.setVisibility(View.INVISIBLE);
                recording = false;
                break;
            case R.id.review:
                System.out.println(paths.size());
                if (!recording && index + 1 < paths.size()) {
                    flying = true;
                    curPos = new Pos(imageView.getX(), imageView.getY(), direction, head_direction);
                    border.pathMoveTo(paths.get(index).getX() + imageView.getWidth() / 2, paths.get(index).getY() + imageView.getHeight() / 2);
                    fly(paths.get(index), paths.get(index + 1));
                }
                break;
            case R.id.endreview:
                if (!recording) {
                    if (flying)
                        stop = true;
                    // System.out.println(last_head_direction);System.out.println(curPos.head_direction);
                    index = 0;
                    recover();
                }
                break;
            default:
                break;
        }
        return true;
    }

    private double dist(float xDelata, float yDelta) {
        return Math.sqrt(xDelata * xDelata + yDelta * yDelta);
    }

    public void recover() {
        // 头朝方向与开始头朝方向不符，进行相应旋转
        if (last_head_direction != curPos.head_direction && !last_head_direction)
            ObjectAnimator.ofFloat(imageView, "rotationY", 180f, 0f).setDuration(0).start();
        else if (last_head_direction != curPos.head_direction && last_head_direction)
            ObjectAnimator.ofFloat(imageView, "rotationY", 0f, 180f).setDuration(0).start();
        imageView.setX(curPos.getX());
        imageView.setY(curPos.getY());
        head_direction = curPos.head_direction;
        direction = curPos.direction;
        border.path.reset();
        border.invalidate();
    }

    public void fly(Pos from, Pos to) {

        border.pathLineTo(to.getX() + imageView.getWidth() / 2, to.getY() + imageView.getHeight() / 2);
        border.invalidate();

        //System.out.println(head_direction);
        //System.out.println(from.head_direction);
        // 头朝方向与开始头朝方向不符，进行相应旋转
        if (head_direction != from.head_direction && !head_direction)
            ObjectAnimator.ofFloat(imageView, "rotationY", 180f, 0f).setDuration(0).start();
        else if (head_direction != from.head_direction && head_direction)
            ObjectAnimator.ofFloat(imageView, "rotationY", 0f, 180f).setDuration(0).start();
        //System.out.println(from.direction);
        //System.out.println(from.head_direction);

        if (to.head_direction && !to.direction) {
            ObjectAnimator.ofFloat(imageView, "rotationY", 180f).setDuration(500).start();
        } else if (!to.head_direction && to.direction) {
            ObjectAnimator.ofFloat(imageView, "rotationY", 0f).setDuration(500).start();
        }
        final boolean h_d = from.head_direction;
        ObjectAnimator translationX = new ObjectAnimator().ofFloat(imageView, "translationX", from.getX(), to.getX());
        ObjectAnimator translationY = new ObjectAnimator().ofFloat(imageView, "translationY", from.getY(), to.getY());
        final AnimatorSet animatorSet = new AnimatorSet();  //组合动画
        animatorSet.playTogether(translationX, translationY); //设置动画
        long time = (long) (5000 * dist(to.getX() - from.getX(), to.getY() - from.getY()) / dist(border.getWidth(), border.getHeight()));
        translationX.setStartDelay(500);
        translationY.setStartDelay(500);
        animatorSet.setDuration(time); // 动画次序时间为0.5秒
        animatorSet.setInterpolator(new LinearInterpolator());
        animatorSet.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animator) {

            }

            @Override
            public void onAnimationEnd(Animator animator) {
                index++;
                if (index + 1 >= paths.size()) {
                    index = 0;
                    last_head_direction = h_d;
                    flying = false;
                    return;
                }
                if (stop) {
                    stop = false;
                    index = 0;
                    //imageView.clearAnimation();
                    recover();
                    return;
                }
                fly(paths.get(index), paths.get(index + 1));
            }

            @Override
            public void onAnimationCancel(Animator animator) {

            }

            @Override
            public void onAnimationRepeat(Animator animator) {

            }
        });
        animatorSet.start(); //启动
    }


    @Override
    public boolean onTouchEvent(final MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN && !flying) {
            final float headX = head_direction == true ? imageView.getX() + imageView.getWidth() : imageView.getX();
            direction = event.getX() - headX > 0 ? true : false;
            //System.out.println(direction);System.out.println(head_direction);
            final boolean cur_h = head_direction, cur_d = direction;

            // 头朝方向与开始头朝方向不符，进行相应旋转
            if (head_direction && !direction) {
                ObjectAnimator.ofFloat(imageView, "rotationY", 180f).setDuration(500).start();
                head_direction = !head_direction;
            } else if (!head_direction && direction) {
                ObjectAnimator.ofFloat(imageView, "rotationY", 0f).setDuration(500).start();
                head_direction = !head_direction;
            }
            //imageView.setRotationY(direction == true ? 0f : 180f);

            final float bird_width = imageView.getWidth(), bird_height = imageView.getHeight();
            float toXDelta = event.getX() - imageView.getX() - bird_width / 2, toYDelta = event.getY() - imageView.getY() - bird_height;
            if (imageView.getX() + toXDelta + imageView.getWidth() > border.getWidth()) {
                toXDelta = border.getWidth() - imageView.getWidth() - imageView.getX();
            }
            if (imageView.getY() + toYDelta + imageView.getHeight() > border.getHeight()) {
                toYDelta = border.getHeight() - imageView.getHeight() - imageView.getY();
            }
            if (imageView.getX() + toXDelta < 0) {
                toXDelta = -imageView.getX();
            }
            if (imageView.getY() + toYDelta < 0) {
                toYDelta = -imageView.getY();
            }
            final float x = toXDelta, y = toYDelta;
            final TranslateAnimation anim = new TranslateAnimation(0, toXDelta, 0, toYDelta);
            long time = (long) (5000 * dist(toXDelta, toYDelta) / dist(border.getWidth(), border.getHeight()));
            anim.setStartOffset(500);
            anim.setDuration(time); // 动画次序时间为0.5秒
            anim.setInterpolator(new LinearInterpolator());
            anim.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {
                    flying = true;
                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    imageView.setX(imageView.getX() + x);
                    imageView.setY(imageView.getY() + y);
                    flying = false;
                    if (recording)
                        paths.add(new Pos(imageView.getX(), imageView.getY(), cur_d, cur_h));
                }
                @Override
                public void onAnimationRepeat(Animation animation) {
                }
            });
            //anim.setFillAfter(true);
            imageView.startAnimation(anim);
        }
        return true;
    }
}
