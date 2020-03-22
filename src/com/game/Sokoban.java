package com.game;

import java.awt.event.KeyEvent;

public class Sokoban extends Game
{

    private int m_tile_width;
    private int m_tile_height;

    private int m_map_width;
    private int m_map_height;
    private int[] m_map;

    private int m_player_x = 0;
    private int m_player_y = 0;

    public Sokoban(Bitmap buf)
    {
        // NOTE(max): I don't know how asserts work in java
        //assert (buf.width % 32 == 0);
        //assert (buf.height % 32 == 0);

        m_tile_width = m_tile_height = 32;
        m_map_width = buf.width / 32;
        m_map_height = buf.height / 32;
        m_map = new int[m_map_width * m_map_height];

    }

    @Override
    public void update_and_render(Bitmap buf, Input input, float dt)
    {
        if (input.keyDownOnce(KeyEvent.VK_RIGHT))
        {
            m_player_x += 1;
        }
        if (input.keyDownOnce(KeyEvent.VK_LEFT))
        {
            m_player_x -= 1;
        }
        if (input.keyDownOnce(KeyEvent.VK_DOWN))
        {
            m_player_y += 1;
        }
        if (input.keyDownOnce(KeyEvent.VK_UP))
        {
            m_player_y -= 1;
        }

        buf.clear((byte)0);
        for (int y = 0; y < m_map_height; y++)
        {
            for (int x = 0; x < m_map_width; x++)
            {
                draw_rectangle(buf, x * m_tile_width, y * m_tile_height, m_tile_width, m_tile_height, 0, 0xff, 0);
            }
        }
        draw_rectangle(buf, m_player_x * m_tile_width, m_player_y * m_tile_height, m_tile_width, m_tile_height, 0xff, 0, 0);
    }

    private void draw_rectangle(Bitmap buf, int x, int y, int width, int height, int r, int g, int b)
    {
        for (int yy = y; yy < y + height; yy++)
        {
            for (int xx = x; xx < x + width; xx++)
            {
                buf.set_pixel(xx, yy, (byte)0, (byte)r, (byte)g, (byte)b);
            }
        }
    }
}
