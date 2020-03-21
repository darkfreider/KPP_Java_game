package com.game;

import java.util.Random;

public class StarField extends Game
{
    private Random m_r = new Random();
    float speed = 1.4f;
    float[] position_x;
    float[] position_y;
    float[] position_z;

    public StarField(Bitmap buf, int nstars)
    {
        position_x = new float[nstars];
        position_y = new float[nstars];
        position_z = new float[nstars];

        for (int i = 0; i < nstars; i++)
        {
            position_x[i] = -1.0f + 2.0f * m_r.nextFloat();
            position_y[i] = -1.0f + 2.0f * m_r.nextFloat();
            position_z[i] = -4.0f + 2.0f * (-1.0f + 2.0f * m_r.nextFloat());
        }
    }

    @Override
    public void update_and_render(Bitmap buf, Input input, float dt) {

        for (int i = 0; i < position_x.length; i++)
        {
            position_z[i] += speed * dt;
            if (position_z[i] >= 0.0f)
            {
                position_x[i] = -1.0f + 2.0f * m_r.nextFloat();
                position_y[i] = -1.0f + 2.0f * m_r.nextFloat();
                position_z[i] = -4.0f + 2.0f * (-1.0f + 2.0f * m_r.nextFloat());
            }
        }

        buf.clear((byte)0x00);
        for (int i = 0; i < position_x.length; i++)
        {
            float hw = (float)buf.width / 2.0f;
            float hh = (float)buf.height / 2.0f;

            int x = (int)((position_x[i] / position_z[i]) * hw + hw);
            int y = (int)((position_y[i] / position_z[i]) * hh + hh);
            buf.set_pixel(x, y, (byte)0, (byte)0xff, (byte)0xff, (byte)0xff);
        }
    }
}
