package com.wuppy.pws.src;

import java.awt.Color;
import java.awt.Graphics;
import java.text.DecimalFormat;

public class RenderTable
{
	static double x = 0, y = 0;
	
	public static void render(Graphics g, Ball ball)
	{
		g.setColor(Color.white);
		
		x = (ball.getX() - 110) / 100;
		g.drawString("X: " + new DecimalFormat("#.##").format(x) + "m", 750, 50);
		
		y = (600 + -ball.getY() + 53) / 100;
		g.drawString("Y: " + new DecimalFormat("#.##").format(y) + "m", 750, 60);
		
		g.drawString("X Speed" + new DecimalFormat("#.##").format(ball.vx), 750, 70);
		
		g.drawString("Y Speed" + new DecimalFormat("#.##").format(ball.vy), 750, 80);
		
		g.drawString("id " + ball.id, 750 , 90);
	}
}