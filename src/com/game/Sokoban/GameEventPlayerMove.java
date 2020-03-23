package com.game.Sokoban;

public class GameEventPlayerMove extends GameEvent
{
    public int old_x;
    public int old_y;

    public GameEventPlayerMove(int old_x, int old_y)
    {
        super(GameEventType.GAME_EVENT_PLAYER_MOVE);

        this.old_x = old_x;
        this.old_y = old_y;
    }
}
