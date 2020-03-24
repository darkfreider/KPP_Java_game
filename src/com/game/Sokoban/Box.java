package com.game.Sokoban;

import java.util.Stack;
import java.util.Vector;

public class Box
{
    private Stack<GameEvent> event_stack;

    public int x;
    public int y;

    public Box(Stack<GameEvent> es, int x, int y)
    {
        this.event_stack = es;
        this.x = x;
        this.y = y;
    }

    public boolean move_if_can(int[] static_map, int sm_width, int sm_height, Vector<Box> boxes, int dir_x, int dir_y)
    {
        // TODO(max): make proper checks for box movement and collisions
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
                result = false;
                break;
            }
        }
        if (result)
        {
            event_stack.push(new GameEventBoxMove(this, x, y));
            x = new_x;
            y = new_y;
        }

        return (result);
    }

}
