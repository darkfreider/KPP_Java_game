package com.game;

public class Main {

    public static void main(String[] args) {
        Display d = new Display(800, 600, "Java window!");

        while (true)
        {
            d.swap_buffers();
        }
    }
}
