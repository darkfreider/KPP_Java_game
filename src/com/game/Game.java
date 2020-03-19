package com.game;

import java.awt.event.KeyEvent;
import java.security.Key;
import java.util.Random;

public class Game {

    private boolean[] cells;
    private boolean[] next_cells;
    private int cells_x;
    private int cells_y;

    public Game(Bitmap buf)
    {
        assert (buf.width % 8 == 0);
        assert (buf.height % 8 == 0);

        cells_x = buf.width / 8 + 2;
        cells_y = buf.height / 8 + 2;
        cells = new boolean[cells_x * cells_y];
        next_cells = new boolean[cells_x * cells_y];

        Random r = new Random(System.nanoTime());
        for (int i = 0; i < cells_x * cells_y; i++)
        {
            cells[i] = r.nextBoolean();
        }
    }

    public void update_and_render(Bitmap buf, Input input)
    {
        for (int y = 1; y < cells_y - 1; y++)
        {
            for (int x = 1; x < cells_x - 1; x++)
            {
                int cell_index = y * cells_y + x;

                int live_cells = 0;
                for (int cy = -1; cy <= 1; cy++)
                {
                    // NOTE(max): actually can unroll the loop and replace it with 3 :? operators
                    for (int cx = -1; cx <= 1; cx++)
                    {
                        if (cells[(y + cy) * cells_y + (x + cx)])
                        {
                            live_cells++;
                        }
                    }
                }
                if (cells[cell_index])
                {
                    live_cells -= 1;
                }

                if (cells[cell_index] && (live_cells == 2 || live_cells == 3))
                {
                    next_cells[cell_index] = true;
                }
                else if (!cells[cell_index] && (live_cells == 3))
                {
                    next_cells[cell_index] = true;
                }
                else
                {
                    next_cells[cell_index] = false;
                }
            }
        }

        buf.clear((byte)0);
        for (int y = 1; y < cells_y - 1; y++)
        {
            for (int x = 1; x < cells_x - 1; x++)
            {
                if (cells[y * cells_y + x])
                    draw_rectangle(buf, x * 8 - 8, y * 8 - 8, 8, 8);
            }
        }

        boolean[] temp = cells;
        cells = next_cells;
        next_cells = temp;
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
