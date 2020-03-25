package com.game.Sokoban;

import java.util.List;
import java.util.Stack;
import java.util.Vector;

public class Player
{
    private Stack<GameEvent> event_stack = null;

    public int x;
    public int y;

    public Player(Stack<GameEvent> event_stack, int x, int y)
    {
        this.event_stack = event_stack;
        this.x = x;
        this.y = y;
    }

    public boolean move_if_can(int[] static_map, int sm_width, int sm_height, Vector<Box> boxes, int dir_x, int dir_y)
    {
        boolean result = true;

        int new_x = x + dir_x;
        int new_y = y + dir_y;

        if (static_map[new_y * sm_width + new_x] == 1)
        {
            return (false);
        }

        for (int i = 0; i < boxes.size(); i++)
        {
            if (boxes.get(i).x == new_x && boxes.get(i).y == new_y)
            {
                result = boxes.get(i).move_if_can(static_map, sm_width, sm_height, boxes, dir_x, dir_y);
                break;
            }
        }

        if (result)
        {
            event_stack.push(new GameEventPlayerMove(x, y));
            x = new_x;
            y = new_y;
        }

        return (result);
    }
}

























