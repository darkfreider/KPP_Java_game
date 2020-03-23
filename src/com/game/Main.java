package com.game;

import com.game.Sokoban.Sokoban;
import com.game.PlatformServices.Display;
import com.game.PlatformServices.Input;

public class Main {

    private final float frame_time = 1.0f / 30.0f;
    boolean is_running = true;

    public Main(String[] args)
    {
        int dwidth = 800;
        int dheight = 640;
        Display d = new Display(dwidth, dheight, "Java window!");
        Input input = new Input();
        d.addKeyListener(input);
        Game game = new Sokoban(d.get_frame_buffer());

        long last_time = System.nanoTime();
        while (is_running) {
            input.poll();

            game.update_and_render(d.get_frame_buffer(), input, frame_time);

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
                System.out.println("MISSED A FRAME, time: " + elapsed_sec);
            }
            last_time = current_time;

            d.swap_buffers();
        }
    }

    public static void main(String[] args) {
        new Main(args);
    }
}
