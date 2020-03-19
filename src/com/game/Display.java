package com.game;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;

public class Display extends Canvas {

    private final JFrame m_frame;
    private final BufferStrategy m_buffer_strategy;
    private final Graphics m_graphics;

    private final Bitmap m_frame_buffer;

    private final BufferedImage m_display_image;
    private final byte[] m_display_components;

    public Display(int width, int height, String title)
    {
         Dimension size = new Dimension(width, height);
         setPreferredSize(size);
         setMinimumSize(size);
         setMaximumSize(size);

         m_frame_buffer = new Bitmap(width, height);
         m_display_image = new BufferedImage(width, height, BufferedImage.TYPE_3BYTE_BGR);
         m_display_components = ((DataBufferByte)m_display_image.getRaster().getDataBuffer()).getData();

         m_frame_buffer.clear((byte)0x50);
         m_frame_buffer.set_pixel(50, 50, (byte)0, (byte)0, (byte)0xff, (byte)0);

         m_frame = new JFrame(title);
         m_frame.add(this);
         m_frame.pack();
         m_frame.setResizable(false);
         m_frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
         m_frame.setVisible(true);
         m_frame.setLocationRelativeTo(null);

         createBufferStrategy(1);
         m_buffer_strategy = getBufferStrategy();
         m_graphics = m_buffer_strategy.getDrawGraphics();
    }

    public void swap_buffers()
    {
        m_frame_buffer.copy_to_byte_array(m_display_components);
        m_graphics.drawImage(m_display_image, 0, 0, m_frame_buffer.width, m_frame_buffer.height, null);
        m_buffer_strategy.show();
    }

}
