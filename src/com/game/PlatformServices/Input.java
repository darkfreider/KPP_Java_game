package com.game.PlatformServices;

import java.awt.event.*;

// NOTE(max): for now I'll stick with this input class
// all the credits go to this article -> https://www.gamedev.net/articles/programming/general-and-gameplay-programming/java-games-keyboard-and-mouse-r2439/
// turns out that the problem was tha MY code that handled keyboard input (those callbacks) were running in as different thread
// so yeah, remember kids SYNCHRONIZATION is quite important

public class Input implements KeyListener, MouseListener, MouseMotionListener {

    private static final int KEY_COUNT = 256;

    private enum KeyState {
        RELEASED, // Not down
        PRESSED,  // Down, but not the first time
        ONCE      // Down for the first time
    }

    // KEYBOARD INPUT

    // Current state of the keyboard
    private boolean[] currentKeys = null;

    // Polled keyboard state
    private KeyState[] keys = null;

    // MOUSE INPUT
    private static final int MOUSE_BUTTON_COUNT = 3;

    public int mouse_x = 0;
    public int mouse_y = 0;

    private int current_mouse_x = 0;
    private int current_mouse_y = 0;

    private boolean[] mouse_state = null;
    private KeyState[] mouse_poll = null;

    public Input(Display d) {
        currentKeys = new boolean[ KEY_COUNT ];
        keys = new KeyState[ KEY_COUNT ];
        for( int i = 0; i < KEY_COUNT; ++i ) {
            keys[ i ] = KeyState.RELEASED;
        }

        mouse_state = new boolean[MOUSE_BUTTON_COUNT];
        mouse_poll = new KeyState[MOUSE_BUTTON_COUNT];
        for (int i = 0; i < MOUSE_BUTTON_COUNT; i++)
        {
            mouse_poll[i] = KeyState.RELEASED;
        }

        d.addKeyListener(this);
        d.addMouseListener(this);
        d.addMouseMotionListener(this);
    }

    public synchronized void poll() {
        for( int i = 0; i < KEY_COUNT; ++i ) {
            // Set the key state
            if( currentKeys[ i ] ) {
                if( keys[ i ] == KeyState.RELEASED )
                    keys[ i ] = KeyState.ONCE;
                else
                    keys[ i ] = KeyState.PRESSED;
            } else {
                keys[ i ] = KeyState.RELEASED;
            }
        }

        mouse_x = current_mouse_x;
        mouse_y = current_mouse_y;
        for (int i = 0; i < MOUSE_BUTTON_COUNT; i++)
        {
            if (mouse_state[i])
            {
                if (mouse_poll[i] == KeyState.RELEASED)
                {
                    mouse_poll[i] = KeyState.ONCE;
                }
                else
                {
                    mouse_poll[i] = KeyState.PRESSED;
                }
            }
            else
            {
                mouse_poll[i] = KeyState.RELEASED;
            }
        }

    }

    public boolean key_down( int keyCode ) {
        return keys[ keyCode ] == KeyState.ONCE ||
                keys[ keyCode ] == KeyState.PRESSED;
    }

    public boolean key_down_once( int keyCode ) {
        return keys[ keyCode ] == KeyState.ONCE;
    }

    public synchronized void keyPressed( KeyEvent e ) {
        int keyCode = e.getKeyCode();
        if( keyCode >= 0 && keyCode < KEY_COUNT ) {
            currentKeys[ keyCode ] = true;
        }
    }

    public synchronized void keyReleased( KeyEvent e ) {
        int keyCode = e.getKeyCode();
        if( keyCode >= 0 && keyCode < KEY_COUNT ) {
            currentKeys[ keyCode ] = false;
        }
    }

    public void keyTyped( KeyEvent e ) {
        // Not needed
    }

    public boolean mbutton_down_once( int button ) {
        return mouse_poll[ button-1 ] == KeyState.ONCE;
    }

    public boolean mbutton_down( int button ) {
        return mouse_poll[ button-1 ] == KeyState.ONCE ||
                mouse_poll[ button-1 ] == KeyState.PRESSED;
    }

    public synchronized void mousePressed( MouseEvent e ) {
        mouse_state[ e.getButton()-1 ] = true;
    }

    public synchronized void mouseReleased( MouseEvent e ) {
        mouse_state[ e.getButton()-1 ] = false;
    }

    public synchronized void mouseEntered( MouseEvent e ) {
        mouseMoved( e );
    }

    public synchronized void mouseExited( MouseEvent e ) {
        mouseMoved( e );
    }

    public synchronized void mouseDragged( MouseEvent e ) {
        mouseMoved( e );
    }

    public synchronized void mouseMoved( MouseEvent e ) {
        current_mouse_x = e.getPoint().x;
        current_mouse_y = e.getPoint().y;
    }

    public void mouseClicked( MouseEvent e ) {
        // Not needed
    }
}




























