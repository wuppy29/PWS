package com.wuppy.pws.src;

import java.awt.Color;
import java.awt.Graphics;
import java.util.Random;

public class Ball
{
	double x, y;
	double vx, vy;
	static int size = 25;
	float r, gr, b;
	int id;
	
	double gra = 9.81;
	double weight = 0.0425;
	double Fz = gra * weight;

	boolean started = true;
	
	public Ball(int x, int y, int id)
	{
		this.x = x;
		this.y = y;
		this.id = id;
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
		if(started)
		{
			vy = Fz * Main.dt;
			started = false;
		}
		
		x += vx;
		y += vy;
		
		if (y + size * 2 >= Main.height && vy > 0)
			vy = -1;
		if (y <= 0 && vy < 0)
			vy = Fz * Main.dt;
		if (x <= 0 && vx < 0)
			vx = 1;
		if (x + size >= Main.width && vx > 0)
			vx = -1;
		
		for (int i = 0; i < Main.ballen.size(); i++)
		{
			if(i != id)
			{
				double distance = Math.sqrt(Math.pow((x + (size/2)) - (Main.ballen.get(i).x + (size/2)), 2) + Math.pow((y + (size/2)) - (Main.ballen.get(i).y + (size/2)), 2));
				
				if(distance <= size)
					vy = -vy;
			}
		}
	}

	public double getX()
	{
		return x;
	}

	public double getY()
	{
		return y;
	}
}