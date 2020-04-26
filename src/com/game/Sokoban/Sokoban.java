package com.game.Sokoban;

import com.game.Bitmap;
import com.game.Game;
import com.game.PlatformServices.Input;
import com.game.Vector2i;

import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;
import java.util.Vector;

import static com.game.Sokoban.GameEventType.*;


public class Sokoban extends Game
{
    // NOTE(max): I's probably better to create an enum for this but who really cares?
    // I's for debugging purposes right now
    // 0 - normal mode, 1 - edit mode

    private int m_game_mode = 0;
    private int m_editor_mode = 1;

    private int m_tile_width;
    private int m_tile_height;

    Stack<GameEvent> event_stack = new Stack<GameEvent>();

    Vector<GameLevel> m_levels = new Vector<GameLevel>();
    int m_current_level = 0;

    public Sokoban(Bitmap buf)
    {
        m_tile_width = m_tile_height = 32;
        int map_width = buf.width / m_tile_width;
        int map_height = buf.height / m_tile_height;
        m_levels.add(new GameLevel(event_stack, 10, 10, map_width, map_height));

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
        test_level.static_map[9 * map_width + 8] = 1;


    }

    @Override
    public void update_and_render(Bitmap buf, Input input, float dt)
    {
        if (input.key_down_once(KeyEvent.VK_F1))
        {
            m_game_mode = (m_game_mode == 0) ? 1 : 0;
        }

        if (m_game_mode == 0)
        {
            GameLevel level = m_levels.elementAt(m_current_level);

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
        }
        else
        {
            // NOTE(max): editor mode
            GameLevel level = m_levels.elementAt(m_current_level);

            int mx = input.mouse_x / m_tile_width;
            int my = input.mouse_y / m_tile_height;

            if (input.key_down_once(KeyEvent.VK_1))
            {
                // NOTE(max): wall placement mode
                m_editor_mode = 1;
            }
            else if (input.key_down_once(KeyEvent.VK_2))
            {
                // NOTE(max): boxes placement mode
                m_editor_mode = 2;
            }
            else if (input.key_down_once((KeyEvent.VK_3)))
            {
                // NOTE(max): win slots(points) placement mode
                m_editor_mode = 3;
            }

            switch (m_editor_mode)
            {
                case 1:
                {
                    if (input.mbutton_down_once(1))
                    {
                        level.static_map[my * level.map_width + mx] = 1;
                    }
                    else if (input.mbutton_down_once(3))
                    {
                        level.static_map[my * level.map_width + mx] = 0;
                    }
                } break;

                case 2:
                {
                    if (input.mbutton_down_once(1))
                    {
                        level.boxes.add(new Box(event_stack, mx, my));
                    }
                    else if (input.mbutton_down_once(3))
                    {
                        for (Box box : level.boxes)
                        {
                            if (box.x == mx && box.y == my)
                            {
                                // TODO(max): I think that instead of using a vector I need to use linked list
                                //            because deletion of elements from vectors is not great and it shifts items internally.
                                //            But this is an editor code so maybe it's ok.
                                if (!level.boxes.removeElement(box))
                                {
                                    // NOTE(max): I suppose this is impossible to reach this statement
                                    System.out.println("BAD vector element removal");
                                }
                                break;
                            }
                        }
                    }
                } break;

                case 3:
                {
                    if (input.mbutton_down_once(1))
                    {
                        level.static_map[my * level.map_width + mx] = 2;
                    }
                    else if (input.mbutton_down_once(3))
                    {
                        // TODO(max): i actually just clears a cell value, even if i contains a wall
                        //            so I could probably make it more robust and check for this case
                        level.static_map[my * level.map_width + mx] = 0;
                    }
                } break;

                default:
                {
                    System.out.println("Undefined editor mode");
                } break;
            }
        }

        int counter = m_levels.elementAt(m_current_level).boxes.size();
        for (Box box : m_levels.elementAt(m_current_level).boxes)
        {
            if (m_levels.elementAt(m_current_level).static_map[box.y * m_levels.elementAt(m_current_level).map_width + box.x] == 2)
            {
                counter--;
            }
        }

        if (counter != 0)
        {
            GameLevel level = m_levels.elementAt(m_current_level);

            buf.clear((byte)0, (byte)0, (byte)0xff, (byte)0);

            for (int y = 0; y < m_levels.elementAt(m_current_level).map_height; y++)
            {
                for (int x = 0; x < m_levels.elementAt(m_current_level).map_width; x++)
                {
                    int cell_val = m_levels.elementAt(m_current_level).static_map[y * m_levels.elementAt(m_current_level).map_width + x];
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
            buf.clear((byte)0, (byte)0xff, (byte)0xff, (byte)0xff);

        }
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
