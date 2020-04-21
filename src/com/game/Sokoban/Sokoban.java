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
    private int game_mode = 0;
    private int editor_mode = 1;

    private int m_tile_width;
    private int m_tile_height;

    Stack<GameEvent> event_stack = new Stack<GameEvent>();

    private int m_map_width;
    private int m_map_height;
    private int[] m_static_map;

    Vector<Box> boxes = new Vector<Box>();
    Player player = new Player(event_stack, 10, 10);

    public Sokoban(Bitmap buf)
    {
        m_tile_width = m_tile_height = 32;
        m_map_width = buf.width / 32;
        m_map_height = buf.height / 32;
        m_static_map = new int[m_map_width * m_map_height];

        boxes.add(new Box(event_stack, 5, 5));
        boxes.add(new Box(event_stack, 12, 9));
        boxes.add(new Box(event_stack, 5, 7));
        boxes.add(new Box(event_stack, 3, 6));

        m_static_map[1 * m_map_width + 1] = 2;
        m_static_map[2 * m_map_width + 1] = 2;
        m_static_map[1 * m_map_width + 2] = 2;
        m_static_map[9 * m_map_width + 7] = 2;

        for (int y = 0; y < m_map_height; y++)
        {
            for (int x = 0; x < m_map_width; x++)
            {
                if ((x == 0 || x == m_map_width - 1) || (y == 0 || y == m_map_height - 1))
                {
                    m_static_map[y * m_map_width + x] = 1;
                }
            }
        }
        m_static_map[8 * m_map_width + 6] = 1;
        m_static_map[8 * m_map_width + 7] = 1;
        m_static_map[8 * m_map_width + 8] = 1;

        m_static_map[9 * m_map_width + 6] = 1;
        m_static_map[9 * m_map_width + 8] = 1;


    }

    @Override
    public void update_and_render(Bitmap buf, Input input, float dt)
    {
        if (input.key_down_once(KeyEvent.VK_F1))
        {
            game_mode = (game_mode == 0) ? 1 : 0;
        }

        if (game_mode == 0)
        {
            if (input.key_down_once(KeyEvent.VK_RIGHT))
            {
                event_stack.push(new GameEvent(GAME_EVENT_DELIMINATOR));
                if (!player.move_if_can(m_static_map, m_map_width, m_map_height, boxes, 1, 0))
                {
                    event_stack.pop();
                }
            }
            else if (input.key_down_once(KeyEvent.VK_LEFT))
            {
                event_stack.push(new GameEvent(GAME_EVENT_DELIMINATOR));
                if (!player.move_if_can(m_static_map, m_map_width, m_map_height, boxes, -1, 0))
                {
                    event_stack.pop();
                };
            }
            else if (input.key_down_once(KeyEvent.VK_DOWN))
            {
                event_stack.push(new GameEvent(GAME_EVENT_DELIMINATOR));
                if (!player.move_if_can(m_static_map, m_map_width, m_map_height,  boxes, 0, 1))
                {
                    event_stack.pop();
                }
            }
            else if (input.key_down_once(KeyEvent.VK_UP))
            {
                event_stack.push(new GameEvent(GAME_EVENT_DELIMINATOR));
                if (!player.move_if_can(m_static_map, m_map_width, m_map_height, boxes, 0, -1))
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
                                player.x = e.old_x;
                                player.y = e.old_y;
                            } break;

                            case GAME_EVENT_BOX_MOVE:
                            {
                                GameEventBoxMove e = (GameEventBoxMove)unknown_event;
                                int i = boxes.indexOf(e.box);
                                if (i != (-1))
                                {
                                    boxes.get(i).x = e.old_x;
                                    boxes.get(i).y = e.old_y;
                                }
                                else
                                {
                                    System.out.println("NOT GOOD");
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

            // IMPORTANT(max): The things that you do in an editor can have some bad side effects
            //                 if an event stack in not empty.
            //                 Most problems will occur (I guess) in box removal code, because
            //                 GameEventBoxMove assumes that the box is ALWAYS present in the game
            //                 and never removed.
            //                 Say you added a box, then you move it in a different place
            //                 and then delete the box. Event stack now has a reference to a DELETED object
            //                 and this is BAD.
            //                 So the way to go is to edit a level (with event stack empty) and then play a level
            //                 DO NOT intermix level editing and playing a level.


            int mx = input.mouse_x / m_tile_width;
            int my = input.mouse_y / m_tile_height;

            if (input.key_down_once(KeyEvent.VK_1))
            {
                // NOTE(max): wall placement mode
                editor_mode = 1;
            }
            else if (input.key_down_once(KeyEvent.VK_2))
            {
                // NOTE(max): boxes placement mode
                editor_mode = 2;
            }
            else if (input.key_down_once((KeyEvent.VK_3)))
            {
                // NOTE(max): win slots(points) placement mode
                editor_mode = 3;
            }

            switch (editor_mode)
            {
                case 1:
                {
                    if (input.mbutton_down_once(1))
                    {
                        m_static_map[my * m_map_width + mx] = 1;
                    }
                    else if (input.mbutton_down_once(3))
                    {
                        m_static_map[my * m_map_width + mx] = 0;
                    }
                } break;

                case 2:
                {
                    if (input.mbutton_down_once(1))
                    {
                       boxes.add(new Box(event_stack, mx, my));
                    }
                    else if (input.mbutton_down_once(3))
                    {
                        // TODO(max): box removal
                    }
                } break;

                case 3:
                {
                    if (input.mbutton_down_once(1))
                    {
                        m_static_map[my * m_map_width + mx] = 2;
                    }
                    else if (input.mbutton_down_once(3))
                    {
                        // TODO(max): i actually just clears a cell value, even if i contains a wall
                        //            so I could probably make it more robust and check for this case
                        m_static_map[my * m_map_width + mx] = 0;
                    }
                } break;

                default:
                {
                    System.out.println("Undefined editor mode");
                } break;
            }
        }

        int counter = boxes.size();
        for (Box box : boxes)
        {
            if (m_static_map[box.y * m_map_width + box.x] == 2)
            {
                counter--;
            }
        }

        if (counter != 0)
        {
            buf.clear((byte)0, (byte)0, (byte)0xff, (byte)0);

            for (int y = 0; y < m_map_height; y++)
            {
                for (int x = 0; x < m_map_width; x++)
                {
                    int cell_val = m_static_map[y * m_map_width + x];
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

            draw_rectangle(buf, player.x * m_tile_width, player.y * m_tile_height, m_tile_width, m_tile_height, 0xff, 0, 0);
            for (Box box : boxes)
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
