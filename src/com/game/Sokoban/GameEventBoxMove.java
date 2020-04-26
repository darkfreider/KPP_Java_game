package com.game.Sokoban;

public class GameEventBoxMove extends GameEvent
{
    // IMPORTANT(max): Even if the box was removed from the game and references to it are still on the event stack
    //                 there will be no dereference of a deleted object because the code that actually handles this event
    //                 will just pop the event and will not touch the object (because it's not in a current box vector)
    public Box box;

    public int old_x;
    public int old_y;

    public GameEventBoxMove(Box box, int old_x, int old_y)
    {
        super(GameEventType.GAME_EVENT_BOX_MOVE);

        this.box = box;
        this.old_x = old_x;
        this.old_y = old_y;
    }
}
