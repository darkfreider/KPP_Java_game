package com.game;

import java.awt.event.KeyEvent;

public class Main {

    private final float frame_time = 1.0f / 30.0f;

    public Main(String[] args)
    {
        Display d = new Display(800, 600, "Java window!");
        Input input = new Input(d);
        Game game = new StarField(d.get_frame_buffer(), 1000);

        long last_time = System.nanoTime();
        while (true) {
            game.update_and_render(d.get_frame_buffer(), input, frame_time);

            if (input.is_pressed(KeyEvent.VK_A)) {
                System.out.println("PRESSED A");
            }

            d.swap_buffers();

            long current_time = System.nanoTime();
            float elapsed_sec = (float) (current_time - last_time) / 1000000000.0f;
            // TODO(max): use some sort of sleep instead of MELTING CPU
            if (elapsed_sec < frame_time) {
                while (elapsed_sec < frame_time) {
                    current_time = System.nanoTime();
                    elapsed_sec = (float) (current_time - last_time) / 1000000000.0f;
                }
            }
            else
            {
                // TODO(max): add thread governing
                System.out.println("MISSED A FRAME");
            }
            last_time = current_time;

            input.swap_inputs();
        }
    }

    public static void main(String[] args) {
        new Main(args);
    }
}
