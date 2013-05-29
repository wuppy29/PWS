package com.wuppy.pws.src;

import java.awt.Graphics;

public class Ball
{
	double x, y;
	double vx, vy;
	int size = 5;
	
	public void render(Graphics g)
	{
		g.drawOval((int) (x), (int) (y), size, size);
	}
	
	public void update()
	{
		
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