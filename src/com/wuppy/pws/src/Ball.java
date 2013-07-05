package com.wuppy.pws.src;

import java.awt.Color;
import java.awt.Graphics;
import java.util.Random;

public class Ball
{
	double x, y;
	double vx = 0, vy = 0;
	static int size = 25;
	static double radius = (double) size / 2D / 100D;
	float r, gr, b;
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
		//forces
		calculateForces();
		
		//time
		t += Main.dt;
		
		//speed
		vy += a * Main.dt;
		
		//outside check
		if(y > Main.bottom - size + 3)
			vy = 0;
		if(y < Main.top - 3)
			vy = 0;
		if(x > Main.right - size + 3)
			vx = 0;
		if(x < Main.left - 3)
			vx = 0;
		
		//movement
		x += vx;
		y += vy;
		
		//bounce sides
		if (y + size * 2 >= Main.bottom + size)
			vy = -vy;
		if (y <= Main.top)
			vy = 1;
		if (x <= 0)
			vx = 1;
		if (x + size >= Main.width && vx > 0)
			vx = -1;
		
		//bounce between balls
		for (int i = 0; i < Main.ballen.size(); i++)
		{
			if(i != id)
			{
				double distance = Math.sqrt(Math.pow((x + (size/2)) - (Main.ballen.get(i).x + (size/2)), 2) + Math.pow((y + (size/2)) - (Main.ballen.get(i).y + (size/2)), 2));
				
				if(distance <= size)
				{
					vy = -vy;
				}
			}
		}
	}
	
	public void calculateForces()
	{
		Fd = 0.5 * rho * Math.pow(vy, 2) * Cd * A;
		
		Fres = Fzw - Fd - Fb;
		
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