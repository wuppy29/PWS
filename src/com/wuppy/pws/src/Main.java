package com.wuppy.pws.src;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferStrategy;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JFrame;

public class Main extends Canvas implements Runnable
{
	private static final long serialVersionUID = 1L;

	//basis variabelen
	private static final String title = "Ballen";
	public static boolean running = false;
	private Thread thread;

	//schermgrootte
	static int width = 1280;
	static int height = 720;

	//de ballen
	static List<Ball> ballen = new ArrayList<Ball>();

	//updates per seconde
	static double ups = 10000.0;

	//alle energie
	double totPot = 0, totKin = 0, totTot = 0;

	//alle ballen of 1
	static boolean all = false;

	//tijdsvershil per update
	static double dt = 1 / ups;

	Image achtergrond;

	//binnenkant lijnen om stuitergebied
	static int left = 110;
	static int right = 710;
	static int top = 53;
	static int bottom = 653;

	//uniek bal nummer, start op 0
	static int ballId = 0;

	//grootte bal in cm
	static int ballSize = 25;

	//is gepauseerd
	static boolean paused = false;

	//puls in x
	//static double Px = 0;

	//snelheidsverdeling output
	static File output;

	//writer voor output
	static BufferedWriter outputWriter;

	public static void main(String[] args)
	{
		//basis start code Java
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
		//als al bezig
		if (running)
		{
			return;
		}
		else
		{
			//achtergrond openen
			ImageIcon backimg = new ImageIcon(this.getClass().getResource("/size.png"));
			achtergrond = backimg.getImage();

			//eerste bal toevoegen
			ballen.add(new Ball(200, 300, 0, ballSize));

			for (int i = 0; i < 12; i++)
			{
				for (int j = 0; j < 7; j++)
				{
					ballen.add(new Ball(left + i * (ballSize + 1), top + j * (ballSize + 1), ballen.size(), ballSize));
				}
			}

			//muis en toetsenbord toevoeging
			addMouseListener(new MouseHandler());
			addKeyListener(new KeyHandler());

			output = new File("output.txt");
			try
			{
				FileOutputStream is = new FileOutputStream(output);
				OutputStreamWriter osw = new OutputStreamWriter(is);
				outputWriter = new BufferedWriter(osw);
			}
			catch (FileNotFoundException e)
			{
				e.printStackTrace();
			}

			//opstarten herhalende code
			running = true;
			thread = new Thread(this);
			thread.start();
		}
	}

	public void run()
	{
		//variabelen voor goede update
		int frames = 0;
		double unprocessedSeconds = 0;
		long previousTime = System.nanoTime();
		double secondsPerTick = 1 / ups;
		int tickCount = 0;
		boolean ticked = false;

		while (running)
		{
			// tijdsverschil per loop
			long currentTime = System.nanoTime();
			long passedTime = currentTime - previousTime;
			previousTime = currentTime;
			unprocessedSeconds += passedTime / 1000000000.0;

			//als de vergane tijd groter is dan de maximale update tijd
			while (unprocessedSeconds > secondsPerTick)
			{
				//doe updates tot je onder de maximale tijd zit
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

		try
		{
			outputWriter.close();
		}
		catch (IOException e)
		{
			e.printStackTrace();
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

		//zwarte achtergrond
		g.setColor(Color.gray);
		g.fillRect(0, 0, width, height);

		//grootte scherm
		g.drawImage(achtergrond, 50, 50, null);

		g.setColor(Color.black);
		g.fillRect(950, 50, 250, 50);

		g.setColor(Color.white);
		g.setFont(g.getFont().deriveFont(25F));

		if (!all)
			g.drawString("Alle ballen bij elkaar", 960, 85);
		else
			g.drawString("Een bal", 1020, 85);

		g.setFont(g.getFont().deriveFont(12F));

		//teken elke bal
		for (int i = 0; i < ballen.size(); i++)
		{
			ballen.get(i).render(g);
		}

		//teken waarden rechts
		RenderTable.render(g, ballen.get(ballId));

		//teken grafiek
		RenderTable.renderGraph(g);

		//shows the screen
		g.dispose();
		bs.show();
	}

	int update = 0;
	int writedata = 0;

	private void update()
	{
		update++;
		writedata++;

		if (!paused)
		{
			for (int i = 0; i < ballen.size(); i++)
			{
				Ball bal = ballen.get(i);

				//update bal
				bal.update();

				//Px += bal.vx * bal.m;

				//toevoegen nieuwe waarde tabel voor enkele bal
				if (!all && i == ballId)
				{
					if (update > ups / RenderTable.size * 8)
					{
						update -= ups / RenderTable.size * 8;
						RenderTable.addValues(bal.ePot, bal.eKin, bal.eTot, bal.h);
					}
				}
				else
				{
					totPot += bal.ePot;
					totKin += bal.eKin;
					totTot += bal.eTot;
				}
			}

			/*
			if (writedata > ups)
			{
				writedata = 0;

				int lagernul = 0;
				int eentwee = 0;
				int drievier = 0;
				int vijfzes = 0;
				int zevenacht = 0;
				int negentien = 0;
				int elftwaalf = 0;
				int hoger = 0;

				for (int i = 0; i < ballen.size(); i++)
				{
					Ball bal = ballen.get(i);

					if (bal.v <= 0)
						lagernul++;
					else if (bal.v > 0 && bal.v <= 2)
						eentwee++;
					else if (bal.v > 2 && bal.v <= 4)
						drievier++;
					else if (bal.v > 4 && bal.v <= 6)
						vijfzes++;
					else if (bal.v > 6 && bal.v <= 8)
						zevenacht++;
					else if (bal.v > 8 && bal.v <= 10)
						negentien++;
					else if (bal.v > 10 && bal.v <= 12)
						elftwaalf++;
					else
						hoger++;
				}

				try
				{
					outputWriter.write(ballen.size() + ", " + lagernul + ", " + eentwee + ", " + drievier + ", " + vijfzes + ", " + zevenacht + ", " + negentien + ", " + elftwaalf + ", " + hoger);
					outputWriter.newLine();
					outputWriter.flush();
					System.out.println(ballen.size() + ", " + lagernul + ", " + eentwee + ", " + drievier + ", " + vijfzes + ", " + zevenacht + ", " + negentien + ", " + elftwaalf + ", " + hoger);
				}
				catch (IOException e)
				{
					e.printStackTrace();
				}
			}*/

			if (update > ups / RenderTable.size * 8)
			{
				update -= ups / RenderTable.size * 8;
				RenderTable.addValues(totPot, totKin, totTot);
			}

			totPot = 0;
			totKin = 0;
			totTot = 0;

			Ball.handleCollisions();
		}

		//System.out.println(Px);
		//Px = 0;
	}

	//goede plek voor bal?
	public static boolean isValidPosition(int x, int y, int ballSize)
	{
		//in stuiter gebied
		if (x <= left || x >= right - ballSize)
			return false;
		if (y <= top || y >= bottom - ballSize)
			return false;
		if (isBallInPosition(x, y))
			return false;

		return true;
	}

	//is er niet al een bal?
	public static boolean isBallInPosition(int x, int y)
	{
		for (int i = 0; i < ballen.size(); i++)
		{
			Ball ball = ballen.get(i);
			double bx = ball.getX();
			double by = ball.getY();

			if (x >= bx - ball.size && x <= bx + ball.size)
			{
				if (y >= by - ball.size && y <= by + ball.size)
				{
					return true;
				}
			}
		}
		return false;
	}

	//zoekt bal op klikplek
	public static int getBallAtPosition(int x, int y)
	{
		for (int i = 0; i < ballen.size(); i++)
		{
			Ball ball = ballen.get(i);

			double bx = ball.getX();
			double by = ball.getY();

			if (x >= bx && x < bx + ball.size)
			{
				if (y >= by && by < by + ball.size)
				{
					return ball.id;
				}
			}
		}
		return ballId;
	}
}