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

    private int m_tile_width;
    private int m_tile_height;

    private int m_map_width;
    private int m_map_height;
    private int[] m_static_map;

    Stack<GameEvent> event_stack = new Stack<GameEvent>();
    Vector<Box> boxes = new Vector<Box>();
    Player player = new Player(event_stack, 10, 10);

    Vector<Vector2i> points = new Vector<Vector2i>();

    public Sokoban(Bitmap buf)
    {
        m_tile_width = m_tile_height = 32;
        m_map_width = buf.width / 32;
        m_map_height = buf.height / 32;
        m_static_map = new int[m_map_width * m_map_height];

        boxes.add(new Box(event_stack, 5, 5));
        boxes.add(new Box(event_stack, 12, 9));
        boxes.add(new Box(event_stack, 15, 16));
        boxes.add(new Box(event_stack, 3, 17));

        points.add(new Vector2i(1, 1));
        points.add(new Vector2i(1, 2));
        points.add(new Vector2i(2, 1));
        points.add(new Vector2i(7, 9));

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

        int counter = points.size();
        for (Box box : boxes)
        {
            for (Vector2i point : points)
            {
                if (point.x == box.x && point.y == box.y)
                {
                    counter--;
                    break;
                }
            }
        }

        if (counter != 0)
        {
            buf.clear((byte)0, (byte)0, (byte)0xff, (byte)0);

            // DEBUG(max): drawing static collision map for debugging purposes
            for (int y = 0; y < m_map_height; y++)
            {
                for (int x = 0; x < m_map_width; x++)
                {
                    if (m_static_map[y * m_map_width + x] == 1)
                    {
                        draw_rectangle(buf, x * m_tile_width, y * m_tile_height, m_tile_width, m_tile_height, 0, 0,0);
                    }
                }
            }

            for (Vector2i point : points)
            {
                draw_rectangle(buf, point.x * m_tile_width, point.y * m_tile_height, m_tile_width, m_tile_height, 0xff, 0xff, 0xff);
            }

            draw_rectangle(buf, player.x * m_tile_width, player.y * m_tile_height, m_tile_width, m_tile_height, 0xff, 0, 0);
            for (Box box : boxes) {
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
