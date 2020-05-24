package com.game.Sokoban;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Stack;
import java.util.Vector;

public class GameLevel
{
    public int map_width;
    public int map_height;
    public int[] static_map;

    Player player;
    public Vector<Box> boxes = new Vector<Box>();

    private String m_data = "";
    private int m_i = 0;

    public GameLevel(Stack<GameEvent> event_stack, String path, int mw, int mh) throws IOException {
        map_width = mw;
        map_height = mh;
        static_map = new int[map_width * map_height];

        m_data = new String(Files.readAllBytes(Paths.get(path)));

        player = new Player(event_stack, read_int(), read_int());

        int box_count = read_int();
        while (box_count-- > 0)
        {
            boxes.add(new Box(event_stack, read_int(), read_int()));
        }

        for (int i = 0; i < map_width * map_height; i++)
        {
            int val = read_int();
            if (val != -1)
            {
                static_map[i] = val;
            }
            else
            {
                System.out.println("ERROR :: Map is too small");
                System.exit(1);
            }
        }
    }

    private boolean is_space(char c)
    {
        return (c == ' ') || (c == '\n') || (c == '\r') || (c == '\t');
    }

    private int read_int() {

        while (m_i < m_data.length())
        {
            switch (m_data.charAt(m_i))
            {
                case ' ':  case '\n': case '\r': case '\t': {
                    while ((m_i < m_data.length()) && is_space(m_data.charAt(m_i)))
                    {
                        m_i++;
                    }
                } break;

                case '0': case '1': case '2': case '3': case '4':
                case '5': case '6': case '7': case '8': case '9':
                {
                    int val = 0;
                    while ((m_i < m_data.length()) && Character.isDigit(m_data.charAt(m_i)))
                    {
                        val = val * 10 + Character.getNumericValue(m_data.charAt(m_i));
                        m_i++;
                    }
                    return (val);
                }

                default:
                {
                    System.out.println("syntactic error");
                    m_i++;
                } break;
            }
        }

        return (-1);
    }
}
