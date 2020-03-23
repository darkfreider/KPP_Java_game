package com.game;

import com.game.Bitmap;
import com.game.PlatformServices.Input;

public abstract class Game
{
    public abstract void update_and_render(Bitmap buf, Input input, float dt);
}
