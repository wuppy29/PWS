package com.wuppy.pws.src;

import java.awt.Color;
import java.awt.Graphics;
import java.util.Random;

public class Ball
{
    double x, y;
    double vx = 0, vy = 0;
    double dir = 0;
    int size;
    double radius = (double) size / 2D / 100D;
    float r, gr, b;                                            //random kleur
    int id;
    int scale = 100;

    double g = 9.81;
    double m = 0.0425;
    double rho = 1.2041;		//air density
    double Cd = 0.45;			//drag coeffecient
    double A = 4D * Math.PI * Math.pow(radius, 2);
    double V = 4D / 3D * Math.PI * Math.pow(radius, 3);

    double Fres = 0;
    double Fzw = g * m;
    double Fd = 0.5 * rho * Math.pow(vy, 2) * Cd * A;
    double Fb = rho * V * g;

    double a = 0;
    double t = 0;

    public Ball(int x, int y, int id, int size)
    {
        this.x = x;
        this.y = y;
        this.id = id;
        this.size = size;
        Random rand = new Random();
        r = rand.nextFloat();
        gr = rand.nextFloat();
        b = rand.nextFloat();
    }

    public void render(Graphics g)
    {
        g.setColor(new Color(r, gr, b));
        g.fillOval((int) (x), (int) (y), size, size);
    }

    public void update()
    {
        //forces
        calculateForces();

        //time
        t += Main.dt;

        //speed
        vy += a * Main.dt;

        //direction
        dir = Math.atan2(vy, vx);
        dir = Math.toDegrees(dir);

        //movement
        if (vy > 0 && y + vy > Main.bottom - size)
        {
            y = Main.bottom - size;
        }
        else if (vy < 0 && y - vy < Main.top)
        {
            y = Main.top;
        }
        else
            y += vy;

        if (x + vx > Main.right - size)
        {
            x = Main.right - size;
        }
        else if (vx < 0 && x + vx < Main.left)
        {
            x = Main.left;
        }
        else
            x += vx;

        //bounce sides
        if (y + size * 2 >= Main.bottom + size)
            vy = -vy;
        if (y <= Main.top)
            vy = -vy;
        if (x <= Main.left)
            vx = -vx;
        if (x + size >= Main.right && vx > 0)
            vx = -vx;

        //bounce between balls
        for (int i = 0; i < Main.ballen.size(); i++)
        {
            if (i != id)
            {
                Ball b2 = Main.ballen.get(i);

                double distance = Math.sqrt(Math.pow((x + (size / 2)) - (b2.x + (b2.size / 2)), 2) + Math.pow((y + (size / 2)) - (b2.y + (b2.size / 2)), 2));

                if (distance <= size && b2.dir - dir < 180)
                {
                    //verschil x en y
                    double dx = Math.abs(b2.x - x);
                    double dy = Math.abs(b2.y - y);

                    //hoek tussen ballen
                    double difdir = Math.atan2(dy, dx);
                    difdir = Math.toDegrees(difdir);

                    //totale snelheid b en b2
                    double v1 = Math.sqrt(Math.pow(vx, 2) + Math.pow(vy, 2));
                    double v2 = Math.sqrt(Math.pow(b2.vx, 2) + Math.pow(b2.vy, 2));

                    //gewicht tweede bal
                    double m2 = b2.m;

                    double vxe1 = ((((v1 * Math.cos(dir - difdir)) * (m - m2) + 2 * m2 * (v2 * Math.cos(b2.dir - difdir)))) / (m + m2)) * Math.cos(difdir) + (v1 * Math.sin(dir - difdir) * Math.cos(difdir + (Math.PI / 2)));
                    double vye1 = ((((v1 * Math.cos(dir - difdir)) * (m - m2) + 2 * m2 * (v2 * Math.cos(b2.dir - difdir)))) / (m + m2)) * Math.sin(difdir) + (v1 * Math.sin(dir - difdir) * Math.sin(difdir + (Math.PI / 2)));

                    double vxe2 = ((((v2 * Math.cos(b2.dir - difdir)) * (m2 - m) + 2 * m * (v1 * Math.cos(dir - difdir)))) / (m + m2)) * Math.cos(difdir) + (v2 * Math.sin(b2.dir - difdir) * Math.cos(difdir + (Math.PI / 2)));
                    double vye2 = ((((v2 * Math.cos(b2.dir - difdir)) * (m2 - m) + 2 * m * (v1 * Math.cos(dir - difdir)))) / (m + m2)) * Math.sin(difdir) + (v2 * Math.sin(b2.dir - difdir) * Math.sin(difdir + (Math.PI / 2)));

                    b2.vy = vye2;
                    b2.vx = vxe2;
                    vy = vye1;
                    vx = vxe1;
                }
            }
        }
    }

    public void calculateForces()
    {
        Fd = 0;//0.5 * rho * Math.pow(vy, 2) * Cd * A;
        
        if (vy > 0)
        {
            Fres = Fzw - Fd - Fb;
        }
        else
            Fres = Fzw + Fd - Fb;

        a = Fres / m;
    }

    public double getX()
    {
        return x;
    }

    public double getY()
    {
        return y;
    }

    public int getId()
    {
        return id;
    }
}