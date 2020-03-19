package com.game;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

// TODO(max): is_pressed, is_released don't work in Game, actually don't know why
public class Input implements KeyListener {

    private final int KBD_LEN = 256;
    private boolean[] current_kbd = new boolean[KBD_LEN];
    private boolean[] prev_kbd = new boolean[KBD_LEN];

    public Input(Display d)
    {
        d.addKeyListener(this);
    }

    public void swap_inputs()
    {
        for (int i = 0; i < KBD_LEN; i++)
        {
            prev_kbd[i] = current_kbd[i];
        }
    }

    public boolean is_pressed(int key)
    {
        return current_kbd[key] && !prev_kbd[key];
    }

    public boolean is_released(int key)
    {
        return !current_kbd[key] && prev_kbd[key];
    }

    public boolean is_down(int key)
    {
        return current_kbd[key];
    }

    public boolean is_up(int key)
    {
        return prev_kbd[key];
    }

    @Override
    public void keyTyped(KeyEvent e)
    {
        // NOTE(max): As I understand now, this callback is used for text input that I don't need right now
    }

    @Override
    public void keyPressed(KeyEvent e)
    {
        current_kbd[e.getKeyCode()] = true;
    }

    @Override
    public void keyReleased(KeyEvent e)
    {
        current_kbd[e.getKeyCode()] = false;
        System.out.println("released");
    }

}