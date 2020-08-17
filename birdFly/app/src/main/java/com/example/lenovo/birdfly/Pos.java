package com.example.lenovo.birdfly;

/**
 * Created by lenovo on 2020/6/15.
 */

public class Pos {
    private float x;
    private float y;
    public boolean direction;
    public boolean head_direction;

    public Pos( float x, float y,boolean direction,boolean head_direction) {
        this.x = x;
        this.y = y;
        this.direction=direction;
        this.head_direction=head_direction;
    }

    float getX() {
        return this.x;
    }

    float getY() {
        return this.y;
    }
}
