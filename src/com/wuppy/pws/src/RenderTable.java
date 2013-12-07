package com.wuppy.pws.src;

import java.awt.Color;
import java.awt.Graphics;
import java.text.DecimalFormat;

public class RenderTable
{
	//bal plek
	static double x = 0, y = 0;

	//start
	static int currentIndex = -1;

	//plek grafiek
	static int graphWidth = 480, graphHeight = 480;
	static int graphLeft = 760, graphBottom = 650;

	//dikte lijnen eromheen
	static int lineWidth = 5;

	//aantal waarden
	static int size = graphWidth;

	//energie en hoogte lijsten
	static Double[] ePotArray = new Double[size];
	static Double[] eKinArray = new Double[size];
	static Double[] eTotArray = new Double[size];
	static Double[] hArray = new Double[size];

	//hoogste waarde voor schaling
	static double biggest = 0;

	//punt grootte
	static int pointSize = 4;

	//punt locatie
	static int xPointOffset = graphWidth / size;
	static int yPointOffset = 0;

	public static void render(Graphics g, Ball ball)
	{
		//voor witte tekst
		g.setColor(Color.black);

		//x
		x = (ball.getX() - 110) / 100;
		g.drawString("X: " + new DecimalFormat("#.##").format(x) + "m", 750, 50);

		//y
		y = (600 + -ball.getY() + 53 - ball.size) / 100;
		g.drawString("Y: " + new DecimalFormat("#.##").format(y) + "m", 750, 60);

		//vx
		g.drawString("X Speed" + new DecimalFormat("#.##").format(ball.vx), 750, 70);

		//vy
		g.drawString("Y Speed" + new DecimalFormat("#.##").format(ball.vy), 750, 80);

		//bal id
		g.drawString("id " + ball.id, 750, 90);

		//kinetische energie
		g.drawString("Ekin " + new DecimalFormat("#.###").format(ball.eKin), 750, 100);

		//potentiele energie
		g.drawString("Epot " + new DecimalFormat("#.###").format(ball.ePot), 750, 110);

		//totale energie
		g.drawString("Etot " + new DecimalFormat("#.###").format(ball.eTot), 750, 120);
	}

	public static void addValues(double ePot, double eKin, double eTot)
	{
		addValues(ePot, eKin, eTot, 0);
	}

	public static void addValues(double ePot, double eKin, double eTot, double h)
	{
		//volgende plek behalve als laatste, dan terug naar 0
		currentIndex++;
		if (currentIndex >= size)
			currentIndex = 0;

		//waardes toevoegen
		ePotArray[currentIndex] = ePot;
		eKinArray[currentIndex] = eKin;
		eTotArray[currentIndex] = eTot;
		hArray[currentIndex] = h;

		//hoogste waarde fixen
		if (eTot > biggest)
			biggest = eTot;
		if (h > biggest)
			biggest = h;
	}

	public static void renderGraph(Graphics g)
	{
		//randen tekenen
		g.setColor(Color.red);
		g.fillRect(graphLeft - lineWidth, graphBottom, graphWidth + (lineWidth * 2) + 1, lineWidth); 											//onder
		g.fillRect(graphLeft - lineWidth, graphBottom, lineWidth, -graphHeight); 																//links
		g.fillRect(graphLeft - lineWidth, graphBottom - graphHeight - lineWidth, graphWidth + (lineWidth * 2) + 1, lineWidth);					//boven
		g.fillRect(graphLeft + graphWidth + lineWidth, graphBottom - graphHeight - lineWidth, lineWidth, graphHeight + (lineWidth * 2));		//rechts

		//tijd schaal onderaan tekeken
		g.setColor(Color.white);
		g.drawString("0", graphLeft, graphBottom + lineWidth + 10);
		g.drawString("2", (int) (graphLeft + (0.25 * graphWidth)), graphBottom + lineWidth + 10);
		g.drawString("4", (int) (graphLeft + (0.5 * graphWidth)), graphBottom + lineWidth + 10);
		g.drawString("5", (int) (graphLeft + (0.75 * graphWidth)), graphBottom + lineWidth + 10);
		g.drawString("8", graphLeft + graphWidth - 15, graphBottom + lineWidth + 10);

		//waarde schaal links tekeken
		g.drawString(new DecimalFormat("#.###").format(biggest), graphLeft - lineWidth - 35, graphBottom - graphHeight + 5);
		g.drawString(new DecimalFormat("#.###").format(biggest * 0.75), graphLeft - lineWidth - 35, (int) (graphBottom - (graphHeight * 0.75) + 5));
		g.drawString(new DecimalFormat("#.###").format(biggest * 0.5), graphLeft - lineWidth - 35, (int) (graphBottom - (graphHeight * 0.5) + 5));
		g.drawString(new DecimalFormat("#.###").format(biggest * 0.25), graphLeft - lineWidth - 35, (int) (graphBottom - (graphHeight * 0.25) + 5));
		g.drawString("0", graphLeft - lineWidth - 10, graphBottom);

		//voor elke waarde in de energie arrays
		for (int i = 0; i < size; i++)
		{
			//als niet leeg
			if (eTotArray[i] != null)
			{
				//teken totale energie
				g.setColor(Color.white);
				g.fillOval(graphLeft + xPointOffset * i, (int) (graphBottom - (graphHeight * (eTotArray[i] / biggest))), pointSize, pointSize);
			}
			if (eKinArray[i] != null)
			{
				g.setColor(Color.cyan);
				g.fillOval(graphLeft + xPointOffset * i, (int) (graphBottom - lineWidth - (graphHeight * (eKinArray[i] / biggest))), pointSize, pointSize);
			}
			if (ePotArray[i] != null)
			{
				g.setColor(Color.green);
				g.fillOval(graphLeft + xPointOffset * i, (int) (graphBottom - lineWidth - (graphHeight * (ePotArray[i] / biggest))), pointSize, pointSize);
			}
			if (hArray[i] != null)
			{
				g.setColor(Color.orange);
				g.fillOval(graphLeft + xPointOffset * i, (int) (graphBottom - lineWidth - (graphHeight * (hArray[i] / biggest))), pointSize, pointSize);
			}
		}
	}
}