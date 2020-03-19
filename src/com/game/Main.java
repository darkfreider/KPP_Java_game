package com.game;

public class Main {

    private final float frame_time = 1.0f / 30.0f;

    public Main(String[] args)
    {
        Display d = new Display(800, 600, "Java window!");

        long last_time = System.nanoTime();
        while (true)
        {
            // TODO(max): get keyboard input
            // TODO(max): update game

            d.swap_buffers();


            long current_time = System.nanoTime();
            float elapsed_sec = (float)(current_time - last_time) / 1000000000.0f;
            // TODO(max): use some sort of sleep instead of MELTING CPU
            while (elapsed_sec < frame_time)
            {
                current_time = System.nanoTime();
                elapsed_sec = (float)(current_time - last_time) / 1000000000.0f;
            }
            last_time = current_time;

            System.out.println((elapsed_sec * 1000.0f));

            // TODO(max): swap current and last keyboard input
        }
    }

    public static void main(String[] args) {
        new Main(args);
    }
}
