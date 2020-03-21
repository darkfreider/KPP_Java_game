package com.game;

import java.awt.event.KeyEvent;
import java.security.Key;
import java.util.Random;

public abstract class Game
{
    public abstract void update_and_render(Bitmap buf, Input input, float dt);
}
