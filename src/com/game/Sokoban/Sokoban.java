package com.game.Sokoban;

import com.game.Bitmap;
import com.game.Game;
import com.game.PlatformServices.Input;
import com.game.Vector2i;

import java.awt.event.KeyEvent;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;
import java.util.Vector;

import static com.game.Sokoban.GameEventType.*;


public class Sokoban extends Game
{
    // NOTE(max): I's probably better to create an enum for this but who really cares?
    // I's for debugging purposes right now
    // 0 - game menu,  1 - game

    private int m_game_mode = 0;

    private int m_tile_width;
    private int m_tile_height;

    private int map_width;
    private int map_height;

    Stack<GameEvent> event_stack = new Stack<GameEvent>();

    GameLevel m_current_level = null;

    GameUI ui = new GameUI();
    GameUIState ui_state = new GameUIState();

    String base_path = "E:\\University\\Java_programming\\JavaGame\\src\\com\\game\\Sokoban\\";

    public Sokoban(Bitmap buf) throws IOException {
        m_tile_width = m_tile_height = 32;
        map_width = buf.width / m_tile_width;
        map_height = buf.height / m_tile_height;

        /*
        for (int i = 0; i < 6; i++)
            m_levels.add(new GameLevel(event_stack, 10, 10, map_width, map_height));

        for (int i = 0; i < 6; i++)
            m_levels.elementAt(i).load_level(base_path + "level" + i " ".txt");

        */
        /*System.out.println(map_width);
        System.out.println();
        GameLevel test_level = m_levels.elementAt(m_current_level);

        test_level.boxes.add(new Box(event_stack, 5, 5));
        test_level.boxes.add(new Box(event_stack, 12, 9));
        test_level.boxes.add(new Box(event_stack, 5, 7));
        test_level.boxes.add(new Box(event_stack, 3, 6));

        test_level.static_map[1 * map_width + 1] = 2;
        test_level.static_map[2 * map_width + 1] = 2;
        test_level.static_map[1 * map_width + 2] = 2;
        test_level.static_map[9 * map_width + 7] = 2;

        for (int y = 0; y < map_height; y++)
        {
            for (int x = 0; x < map_width; x++)
            {
                if ((x == 0 || x == map_width - 1) || (y == 0 || y == map_height - 1))
                {
                    test_level.static_map[y * map_width + x] = 1;
                }
            }
        }
        test_level.static_map[8 * map_width + 6] = 1;
        test_level.static_map[8 * map_width + 7] = 1;
        test_level.static_map[8 * map_width + 8] = 1;

        test_level.static_map[9 * map_width + 6] = 1;
        test_level.static_map[9 * map_width + 8] = 1;*/


    }

    @Override
    public void update_and_render(Bitmap buf, Input input, float dt)
    {
        if (m_game_mode == 0)
        {
            buf.clear(ui_state.bg_color);

            ui.prepare(input);

            int[] colors = {
                    0xff9966ff,
                    0xff66ccff,
                    0xffffff66,
                    0xff00cc66,
                    0xffff3300,
                    0xff66ffcc,
            };

            int base_uiid = ui.get_next_uiid();
            for (int i = 0; i < colors.length; i++)
            {
                int uiid = base_uiid + i;
                int y = i / (colors.length / 2);
                int x = i - y * (colors.length / 2);

                int box_x = ui_state.reference_x + (x  * ui.button_width) + (x * 50);
                int box_y = ui_state.reference_y + y * (ui.button_height + 20);
                if (ui.do_button(buf, uiid, box_x, box_y))
                {
                    if (uiid == (base_uiid + 0))
                    {
                        m_game_mode = 1;
                        try {
                            event_stack.clear();
                            m_current_level = new GameLevel(event_stack, base_path + "level0.txt", map_width, map_height);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    ui_state.bg_color = colors[i];
                }
            }

            ui.finish();
        }
        else
        {
            // NOTE(max): game_mode = game

            if (input.key_down_once(KeyEvent.VK_ESCAPE))
            {
                m_game_mode = 0;
            }

            GameLevel level = m_current_level;
            if (input.key_down_once(KeyEvent.VK_RIGHT))
            {
                event_stack.push(new GameEvent(GAME_EVENT_DELIMINATOR));
                if (!level.player.move_if_can(level, 1, 0))
                {
                    event_stack.pop();
                }
            }
            else if (input.key_down_once(KeyEvent.VK_LEFT))
            {
                event_stack.push(new GameEvent(GAME_EVENT_DELIMINATOR));
                if (!level.player.move_if_can(level, -1, 0))
                {
                    event_stack.pop();
                };
            }
            else if (input.key_down_once(KeyEvent.VK_DOWN))
            {
                event_stack.push(new GameEvent(GAME_EVENT_DELIMINATOR));
                if (!level.player.move_if_can(level, 0, 1))
                {
                    event_stack.pop();
                }
            }
            else if (input.key_down_once(KeyEvent.VK_UP))
            {
                event_stack.push(new GameEvent(GAME_EVENT_DELIMINATOR));
                if (!level.player.move_if_can(level,0, -1))
                {
                    event_stack.pop();
                }
            }
            else if (input.key_down_once(KeyEvent.VK_Z))
            {
                if (!event_stack.isEmpty())
                {
                    while (event_stack.peek().type != GAME_EVENT_DELIMINATOR)
                    {
                        GameEvent unknown_event = event_stack.pop();
                        switch (unknown_event.type)
                        {
                            case GAME_EVENT_PLAYER_MOVE:
                            {
                                GameEventPlayerMove e = (GameEventPlayerMove)unknown_event;
                                level.player.x = e.old_x;
                                level.player.y = e.old_y;
                            } break;

                            case GAME_EVENT_BOX_MOVE:
                            {
                                GameEventBoxMove e = (GameEventBoxMove)unknown_event;
                                int i = level.boxes.indexOf(e.box);
                                if (i != (-1))
                                {
                                    level.boxes.get(i).x = e.old_x;
                                    level.boxes.get(i).y = e.old_y;
                                }
                                else
                                {
                                    // IMPORTANT(max): This clause means that a box was deleted but references to it are
                                    //            still on the event stack.
                                    //            I ASSUME that the game will never have a mechanic to delete boxes
                                    //            so that when I undo changes the box should be restored.
                                    //            A ASSUME that a deletion of a box will occur ONLY in game editor.
                                }
                            } break;

                            default:
                            {
                                System.out.println("ERROR :: unknown event type");
                            } break;
                        }
                    }
                    event_stack.pop();
                }
            }

            // NOTE(max): game_mode = game, render the game
            int counter = m_current_level.boxes.size();
            for (Box box : m_current_level.boxes)
            {
                if (m_current_level.static_map[box.y * m_current_level.map_width + box.x] == 2)
                {
                    counter--;
                }
            }

            if (counter != 0)
            {
                buf.clear((byte)0, (byte)0, (byte)0xff, (byte)0);

                for (int y = 0; y < m_current_level.map_height; y++)
                {
                    for (int x = 0; x < m_current_level.map_width; x++)
                    {
                        int cell_val = m_current_level.static_map[y * m_current_level.map_width + x];
                        if (cell_val == 1)
                        {
                            draw_rectangle(buf, x * m_tile_width, y * m_tile_height, m_tile_width, m_tile_height, 0, 0,0);
                        }
                        else if (cell_val == 2)
                        {
                            draw_rectangle(buf, x * m_tile_width, y * m_tile_height, m_tile_width, m_tile_height, 0xff, 0xff, 0xff);
                        }
                    }
                }

                draw_rectangle(buf, level.player.x * m_tile_width, level.player.y * m_tile_height, m_tile_width, m_tile_height, 0xff, 0, 0);
                for (Box box : level.boxes)
                {
                    draw_rectangle(buf, box.x * m_tile_width, box.y * m_tile_height, m_tile_width, m_tile_height, 0xff, 0xff, 0);
                }
            }
            else
            {
                //buf.clear((byte)0, (byte)0xff, (byte)0xff, (byte)0xff);
                m_game_mode = 0;
            }
        }


    }

    private void draw_rectangle(Bitmap buf, int x, int y, int width, int height, int r, int g, int b)
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
                buf.set_pixel(xx, yy, (byte) 0, (byte)r, (byte)g, (byte)b);
            }
        }
    }
}
