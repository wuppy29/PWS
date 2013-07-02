package com.wuppy.pws.src;

import java.awt.Color;
import java.awt.Graphics;
import java.util.Random;

public class Ball
{
	double x, y;
	double vx = 0, vy = 0;
	static int size = 25;
	float r, gr, b;
	int id;
	int scale = 100;
	
	double t = 0;
	
	double g = 9.81;
	double m = 0.0425;
	double rho = 1.2041;
	double Cd = 0.45;
	double A = 4 * Math.PI * Math.pow(size /2, 2);
	
	double terminalSpeed = Math.sqrt((2 * m * g) / (rho * Cd * A)) * scale;
	//vy = terminalSpeed * Math.tanh(g * Main.dt / terminalSpeed) * scale;
	
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
		t += Main.dt;
		
		vy = g * t;
		if(vy >= terminalSpeed)
			vy = terminalSpeed;
		
		x += vx;
		y += vy;
		
		if (y + size * 2 >= Main.height && vy > 0)
			vy = 1;
		if (y <= 0)
			vy = 1;
		if (x <= 0)
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