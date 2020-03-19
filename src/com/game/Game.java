package com.game;

import java.awt.event.KeyEvent;
import java.security.Key;

public class Game {

    private int rx = 50;
    private  int ry = 50;

    public Game()
    {

    }

    public void update_and_render(Bitmap buf, Input input)
    {
        if (input.is_down(KeyEvent.VK_A))
        {
            rx -= 5;
        }
        if (input.is_down(KeyEvent.VK_D))
        {
            rx += 5;
        }
        if (input.is_down(KeyEvent.VK_W))
        {
            ry -= 5;
        }
        if (input.is_down(KeyEvent.VK_S))
        {
            ry += 5;
        }

        buf.clear((byte)0);
        draw_rectangle(buf, rx, ry, 200, 100);
    }

    private void draw_rectangle(Bitmap buf, int x, int y, int width, int height)
    {
        for (int yy = y; yy < y + height; yy++)
        {
            for (int xx = x; xx < x + width; xx++)
            {
                buf.set_pixel(xx, yy, (byte)0, (byte)0, (byte)0xff, (byte)0);
            }
        }
    }

}
