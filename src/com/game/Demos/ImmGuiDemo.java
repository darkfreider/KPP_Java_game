package com.game.Demos;

import com.game.Bitmap;
import com.game.Game;
import com.game.PlatformServices.Input;
import com.game.Sokoban.GameEvent;
import com.game.Vector2i;

import java.awt.event.KeyEvent;

import static com.game.Sokoban.GameEventType.GAME_EVENT_DELIMINATOR;

public class ImmGuiDemo extends Game
{
    Vector2i ui_mouse_pos = new Vector2i(0, 0);
    boolean ui_mouse_down = false;

    int ui_hot_item = 0;
    int ui_active_item = 0;

    int ui_button_width = 64;
    int ui_button_height = 48;

    // NOTE(max): currently reinit is in immgui_prepare()
    int next_uiid = 1;

    Vector2i pos = new Vector2i(100, 100);
    Vector2i dim = new Vector2i(100, 80);

    int bg_color = 0xff9966ff;

    public ImmGuiDemo(Bitmap buf)
    {

    }

    private int get_next_uiid()
    {
        return (next_uiid++);
    }

    private void immgui_prepare()
    {
        ui_hot_item = 0;

        // NOTE(max): see next_uiid note if you want to change the place of initialization
        next_uiid = 1;
    }

    private void immgui_finish()
    {
        if (!ui_mouse_down)
        {
            ui_active_item = 0;
        }
        else
        {
            if (ui_active_item == 0)
            {
                ui_active_item = -1;
            }
        }
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

    private boolean do_button(Bitmap buf, int id, int x, int y)
    {
        if (region_on_hit(x, y, ui_button_width, ui_button_height))
        {
            ui_hot_item = id;
            if (ui_active_item == 0 && ui_mouse_down)
            {
                ui_active_item = id;
            }
        }

        draw_rectangle(buf, x + 8, y + 8, ui_button_width, ui_button_height, 0);
        if (ui_hot_item == id)
        {
            if (ui_active_item == id)
            {
                draw_rectangle(buf, x + 2, y + 2, ui_button_width, ui_button_height, 0xffffffff);
            }
            else
            {
                draw_rectangle(buf, x, y, ui_button_width, ui_button_height, 0xffffffff);
            }
        }
        else
        {
            draw_rectangle(buf, x, y, ui_button_width, ui_button_height, 0xaaaaaaaa);
        }

        if (!ui_mouse_down && ui_hot_item == id && ui_active_item == id)
        {
            return (true);
        }

        return (false);
    }


    @Override
    public void update_and_render(Bitmap buf, Input input, float dt) {

        ui_mouse_pos.x = input.mouse_x;
        ui_mouse_pos.y = input.mouse_y;
        ui_mouse_down = input.mbutton_down(1);



        buf.clear(bg_color);

        immgui_prepare();

        int reference_x = 75;
        int reference_y = 50;

        if (input.key_down(KeyEvent.VK_E))
        {
            if (do_button(buf, get_next_uiid(), reference_x, reference_y + ui_button_height + 50))
            {
                bg_color = 0xff777777;
            }
        }

        int[] colors = {
                0xff9966ff,
                0xff66ccff,
                0xffffff66,
                0xff00cc66,
                0xffff3300,
                0xff66ffcc,
        };

        for (int i = 0; i < colors.length; i++)
        {
            int id = get_next_uiid();
            if (do_button(buf, id, reference_x + (i * ui_button_width) + (i * 50), reference_y))
            {
                bg_color = colors[i];
            }
        }

        immgui_finish();
    }

    private void draw_rectangle(Bitmap buf, int x, int y, int width, int height, int argb)
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

        byte a = (byte)(argb >> 24);
        byte r = (byte)(argb >> 16);
        byte g = (byte)(argb >> 8);
        byte b = (byte)(argb);

        for (int yy = y; yy < y + height; yy++)
        {
            for (int xx = x; xx < x + width; xx++)
            {
                buf.set_pixel(xx, yy, a, r, g, b);
            }
        }
    }
}
