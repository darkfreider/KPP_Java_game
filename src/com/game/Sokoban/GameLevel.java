package com.game.Sokoban;

import java.util.Stack;
import java.util.Vector;

public class GameLevel
{
    public int map_width;
    public int map_height;
    public int[] static_map;

    public Vector<Box> boxes = new Vector<Box>();

    Player player;

    public GameLevel(Stack<GameEvent> event_stack, int px, int py, int mw, int mh)
    {
        map_width = mw;
        map_height = mh;
        static_map = new int[map_width * map_height];

        player = new Player(event_stack, px, py);
    }

    public void load_level()
    {

    }
}
