package com.game;

import java.util.Arrays;

public class Bitmap {

    public final int width;
    public final int height;
    private final byte[] m_components; // format: A B G R

    public Bitmap(int w, int h)
    {
        width = w;
        height = h;
        m_components = new byte[width * height * 4];
    }

    public void clear(byte shade)
    {
        Arrays.fill(m_components, shade);
    }

    public void set_pixel(int x, int y, byte a, byte r, byte g, byte b)
    {
        int i = (y * width + x) * 4;
        m_components[i] = a;
        m_components[i + 1] = b;
        m_components[i + 2] = g;
        m_components[i + 3] = r;
    }

    public void copy_to_byte_array(byte[] dest)
    {
        for (int i = 0; i < width * height; i++)
        {
            // a b g r
            dest[i * 3] = m_components[i * 4 + 1];
            dest[i * 3 + 1] = m_components[i * 4 + 2];
            dest[i * 3 + 2] = m_components[i * 4 + 3];
        }
    }

}
