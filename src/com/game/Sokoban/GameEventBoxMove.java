package com.game.Sokoban;

public class GameEventBoxMove extends GameEvent
{
    // IMPORTANT(max): So right now I assume that box that is attached to this event NEVER removed from the game,
    // i.e. the object is always present.
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
