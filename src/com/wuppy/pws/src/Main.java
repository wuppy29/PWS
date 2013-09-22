package com.wuppy.pws.src;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferStrategy;
import java.util.ArrayList;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JFrame;

public class Main extends Canvas implements Runnable
{
	private static final long serialVersionUID = 1L;

	private static final String title = "Ballen";
	public static boolean running = false;
	private Thread thread;
	
	static int width = 1280;
	static int height = 720;
	
	static List<Ball> ballen = new ArrayList<Ball>();
	
	static double ups = 60.0;
	int sec = 0;
	double totPot = 0, totKin = 0, totTot = 0;
	
	static double dt = 1 / ups;
	
	Image background;
	
	static int left = 110;
	static int right = 710;
	static int top = 53;
	static int bottom = 653;
	
	static int ballId = 0;
	static int ballSize = 25;
	
	public static void main(String[] args)
	{
		Main main = new Main();
		JFrame frame = new JFrame();
		frame.add(main);
		frame.pack();
		frame.setTitle(title);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(width, height);
		frame.setLocationRelativeTo(null);
		frame.setResizable(false);
		frame.setVisible(true);
		frame.requestFocus();
		main.start();
	}

	public void start()
	{
		if (running)
		{
			return;
		}
		else
		{
			ImageIcon backimg = new ImageIcon(this.getClass().getResource("/size.png"));
			background = backimg.getImage();
			ballen.add(new Ball(200, 300, 0, ballSize));
			addMouseListener(new MouseHandler());
			running = true;
			thread = new Thread(this);
			thread.start();
		}
	}

	public void run()
	{
		int frames = 0;
		double unprocessedSeconds = 0;
		long previousTime = System.nanoTime();
		double secondsPerTick = 1 / ups;
		int tickCount = 0;
		boolean ticked = false;

		while (running)
		{
			// fps counter
			long currentTime = System.nanoTime();
			long passedTime = currentTime - previousTime;
			previousTime = currentTime;
			unprocessedSeconds += passedTime / 1000000000.0;
			
			while (unprocessedSeconds > secondsPerTick)
			{
				update();
				unprocessedSeconds -= secondsPerTick;
				ticked = true;
				tickCount++;
				
				if (tickCount % ups == 0)
				{
					System.out.println(frames + " fps");
					previousTime += 1000;
					frames = 0;
				}
			}

			// render
			if (ticked)
			{
				render();
				frames++;
			}

			render();
			frames++;
		}
	}

	private void render()
	{
		//setting up graphics
		BufferStrategy bs = this.getBufferStrategy();
		if (bs == null)
		{
			createBufferStrategy(3);
			return;
		}
		Graphics g = bs.getDrawGraphics();
		g.setColor(Color.black);
		g.fillRect(0, 0, width, height);
		
		g.drawImage(background, 50, 50, null);
		
		for(int i = 0; i < ballen.size(); i++)
		{
			ballen.get(i).render(g);
		}
		
		RenderTable.render(g, ballen.get(ballId));
		
		RenderTable.renderGraph(g);
		
		//shows the screen
		g.dispose();
		bs.show();
	}

	private void update()
	{
		for(int i = 0; i < ballen.size(); i++)
		{
			ballen.get(i).update();
		}
		
		RenderTable.addE(ballen.get(ballId).ePot, ballen.get(ballId).eKin, ballen.get(ballId).eTot);
	}

	public static boolean isValidPosition(int x, int y, int ballSize)
	{
		if(x <= left || x >= right - ballSize)
			return false;
		if(y <= top || y >= bottom - ballSize)
			return false;
		if(isBallInPosition(x, y))
			return false;
		
		return true;
	}

	public static boolean isBallInPosition(int x, int y)
	{
		for(int i = 0; i < ballen.size(); i++)
		{
			Ball ball = ballen.get(i);
			double bx = ball.getX();
			double by = ball.getY();
			
			if(x >= bx - ball.size && x <= bx + ball.size)
			{
				if(y >= by - ball.size && y <= by + ball.size)
				{
					return true;
				}
			}
		}
		return false;
	}
	
	public static int getBallAtPosition(int x, int y)
	{
		for(int i = 0; i < ballen.size(); i++)
		{
			Ball ball = ballen.get(i);
			
			double bx = ball.getX();
			double by = ball.getY();
			
			if(x >= bx && x < bx + ball.size)
			{
				if(y >= by && by < by + ball.size)
				{
					return ball.id;
				}
			}
		}
		return ballId;
	}
}