package com.game.Demos;

import com.game.Bitmap;
import com.game.Game;
import com.game.PlatformServices.Input;
import com.game.Sokoban.GameEvent;
import com.game.Vector2i;

import java.awt.event.KeyEvent;

import static com.game.Sokoban.GameEventType.GAME_EVENT_DELIMINATOR;

public class ImgGuiDemo extends Game
{
    Vector2i ui_mouse_pos = new Vector2i(0, 0);
    boolean ui_mouse_down = false;

    int ui_hot_item = 0;
    int ui_active_item = 0;

    Vector2i pos = new Vector2i(100, 100);
    Vector2i dim = new Vector2i(100, 80);

    public ImgGuiDemo(Bitmap buf)
    {

    }

    private boolean region_on_hit(int x, int y, int width, int height)
    {
        if (ui_mouse_pos.x < x || ui_mouse_pos.x >= (x + width) ||
                ui_mouse_pos.y < y || ui_mouse_pos.y >= (y + height))
        {
            return (false);
        }

        return (true);
    }

    private boolean do_button(int id, int x, int y)
    {
        
    }


    @Override
    public void update_and_render(Bitmap buf, Input input, float dt) {

        ui_mouse_pos.x = input.mouse_x;
        ui_mouse_pos.y = input.mouse_y;
        ui_mouse_down = input.mbutton_down_once(1);


        byte green = (byte)126;
        if (region_on_hit(pos.x, pos.y, dim.x, dim.y))
        {
            green = (byte)255;
        }

        buf.clear((byte)0, (byte)147, (byte)112, (byte)219);
        draw_rectangle(buf, pos.x, pos.y, dim.x, dim.y, green);
    }

    private void draw_rectangle(Bitmap buf, int x, int y, int width, int height, byte shade_of_green)
    {
        if (x < 0)
        {
            width += x;
            x = 0;
        }
        if ((x + width) >= buf.width)
        {
            width = buf.width - x;
        }
        if (y < 0)
        {
            height += y;
            y = 0;
        }
        if ((y + height) >= buf.height)
        {
            height = buf.height - y;
        }

        for (int yy = y; yy < y + height; yy++)
        {
            for (int xx = x; xx < x + width; xx++)
            {
                buf.set_pixel(xx, yy, (byte)0, (byte)0, shade_of_green, (byte)0);
            }
        }
    }
}
