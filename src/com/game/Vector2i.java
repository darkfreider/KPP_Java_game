package com.game;

public class Vector2i
{
    public int x = 0;
    public int y = 0;

    public Vector2i()
    {

    }

    public Vector2i(int x, int y)
    {
        this.x = x;
        this.y = y;
    }

    public Vector2i add(Vector2i v)
    {
        x += v.x;
        y += v.y;

        return (this);
    }

    public  Vector2i add(int x, int y)
    {
        this.x += x;
        this.y += y;

        return (this);
    }

    public Vector2i sub(Vector2i v)
    {
        x -= v.x;
        y -= v.y;

        return (this);
    }

    public  Vector2i sub(int x, int y)
    {
        this.x -= x;
        this.y -= y;

        return (this);
    }
}
