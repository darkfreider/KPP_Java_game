package com.game.Sokoban;

import com.game.Bitmap;
import com.game.MutableFloat;
import com.game.PlatformServices.Input;
import com.game.Vector2i;

public class GameUI
{
    Vector2i ui_mouse_pos = new Vector2i(0, 0);
    boolean ui_mouse_down = false;

    private int ui_hot_item = 0;
    private int ui_active_item = 0;

    // NOTE(max): currently reinit is in immgui_prepare()
    private int next_uiid = 1;

    public int button_width = 64;
    public int button_height = 48;

    public GameUI()
    {

    }

    public int get_next_uiid()
    {
        return (next_uiid++);
    }

    public void prepare(Input input)
    {
        ui_mouse_pos.x = input.mouse_x;
        ui_mouse_pos.y = input.mouse_y;
        ui_mouse_down = input.mbutton_down(1);

        ui_hot_item = 0;
        // NOTE(max): see next_uiid note if you want to change the place of initialization
        next_uiid = 1;
    }

    public void finish()
    {
        if (!ui_mouse_down)
        {
            ui_active_item = 0;
        }
        else if (ui_active_item == 0) {
            ui_active_item = 0;
        }
    }

    private boolean region_on_hit(int x, int y, int width, int height)
    {
        return ui_mouse_pos.x >= x && ui_mouse_pos.x < (x + width) &&
                ui_mouse_pos.y >= y && ui_mouse_pos.y < (y + height);
    }

    public boolean do_check_box(Bitmap buf, int id, int x, int y, boolean checked)
    {
        if (region_on_hit(x, y, 16, 16))
        {
            ui_hot_item = id;
            if (ui_active_item == 0 && ui_mouse_down)
            {
                ui_active_item = id;
            }
        }

        draw_rectangle(buf, x, y, 8, 8, 0xaaaaaaaa);

        if (ui_active_item == id && ui_hot_item == id && !ui_mouse_down)
        {
            draw_rectangle(buf, x, y, 16, 16, checked ? 0xffffffff : 0xaaaaaaaa);
            return (true);
        }
        else
        {
            draw_rectangle(buf, x, y, 16, 16, checked ? 0xffffffff : 0xaaaaaaaa);
        }

        return (false);
    }

    public boolean do_slider(Bitmap buf, int id, int x, int y, MutableFloat val)
    {
        if (region_on_hit(x, y, 32, 256))
        {
            ui_hot_item = id;
            if (ui_active_item == 0 && ui_mouse_down)
            {
                ui_active_item = id;
            }
        }

        draw_rectangle(buf, x, y, 32, 256, 0xaaaaaaaa);

        if (ui_active_item == id)
        {
            int y_pos = ui_mouse_pos.y - y;
            if (y_pos < 0) y_pos = 0;
            if (y_pos > 256) y_pos = 256;


            draw_rectangle(buf, x, y + y_pos, 32, 256 - y_pos, 0xFFFFFF00);


            float curr_val = 1.0f - (float)y_pos / 256.0f;
            if (curr_val != val.val)
            {
                val.val = curr_val;
                return (true);
            }
        }
        else
        {
            int height = (int)(val.val * 256.0f);
            draw_rectangle(buf, x, y + (256 - height), 32, height, 0xFFFFFF00);
        }

        return (false);
    }

    public boolean do_button(Bitmap buf, int id, int x, int y)
    {
        int ui_button_width = 64;
        int ui_button_height = 48;
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


    /*
    public void update_and_render(Bitmap buf, Input input, float dt) {

        ui_mouse_pos.x = input.mouse_x;
        ui_mouse_pos.y = input.mouse_y;
        ui_mouse_down = input.mbutton_down(1);

        immgui_prepare();


        immgui_finish();
    }
    */

    // NOTE(max): this algorithm is a garbage, this is only for testing purposes
    private void draw_filled_circle(Bitmap buf, int center_x, int center_y, int radius, int argb)
    {
        byte a = (byte)(argb >> 24);
        byte r = (byte)(argb >> 16);
        byte g = (byte)(argb >> 8);
        byte b = (byte)(argb);

        int x = center_x - radius;
        int y = center_y - radius;

        for (int yy = y; yy < y + 2 * radius; yy++)
        {
            for (int xx = x; xx < x + 2 *radius; xx++)
            {
                int aa = (xx - center_x) * (xx - center_x);
                int bb = (yy - center_y) * (yy - center_y);
                int cc = radius * radius;
                if (aa + bb < cc)
                {
                    buf.set_pixel(xx, yy, a, r, g, b);
                }
            }
        }
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
