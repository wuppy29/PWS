package com.wuppy.pws.src;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferStrategy;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JFrame;

public class Main extends Canvas implements Runnable
{
	private static final long serialVersionUID = 1L;

	private static final String title = "Ballen";
	public static boolean running = false;
	private Thread thread;
	
	static int width = 600;
	static int height = 600;
	
	static List<Ball> ballen = new ArrayList<Ball>();
	
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
			ballen.add(new Ball(50, 50));
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
		double secondsPerTick = 1 / 60.0;
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
				if (tickCount % 60 == 0)
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
		
		for(int i = 0; i < ballen.size(); i++)
		{
			ballen.get(i).render(g);
		}
		
		//shows the screen
		g.dispose();
		bs.show();
	}

	private void update()
	{
		
	}

	public static boolean isValidPosition(int x, int y)
	{
		if(x <= Ball.size / 2 || x >= width - Ball.size / 2)
			return false;
		if(y <= Ball.size / 2 || y >= height - Ball.size / 2)
			return false;
		
		return true;
	}
}