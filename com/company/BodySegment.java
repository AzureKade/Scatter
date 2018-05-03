package com.company;



public class BodySegment {

    public int x, y;
    public int xspeed, yspeed;

    public void setPos(int x, int y)
    {
        this.x = x;
        this.y = y;
    }

    public void setSpeed(Vector2D speed)
    {
        this.xspeed = speed.x;
        this.yspeed = speed.y;
    }
}