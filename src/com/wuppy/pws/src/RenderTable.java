package com.wuppy.pws.src;

import java.awt.Color;
import java.awt.Graphics;
import java.text.DecimalFormat;

public class RenderTable
{
    static double x = 0, y = 0;

    static int currentIndex = -1;
    static int graphWidth = 480, graphHeight = 480;
    static int graphLeft = 760, graphBottom = 650;
    static int lineWidth = 5;

    static int size = graphWidth;

    static Double[] ePotArray = new Double[size];
    static Double[] eKinArray = new Double[size];
    static Double[] eTotArray = new Double[size];

    static double biggest = 0;
    static int pointSize = 4;
    static int xPointOffset = graphWidth / size;
    static int yPointOffset = 0;

    public static void render(Graphics g, Ball ball)
    {
        g.setColor(Color.white);

        x = (ball.getX() - 110) / 100;
        g.drawString("X: " + new DecimalFormat("#.##").format(x) + "m", 750, 50);

        y = (600 + -ball.getY() + 53 - (0.5 * ball.size)) / 100;
        g.drawString("Y: " + new DecimalFormat("#.##").format(y) + "m", 750, 60);

        g.drawString("X Speed" + new DecimalFormat("#.##").format(ball.vx), 750, 70);

        g.drawString("Y Speed" + new DecimalFormat("#.##").format(ball.vy), 750, 80);

        g.drawString("id " + ball.id, 750, 90);

        g.drawString("Ekin " + new DecimalFormat("#.###").format(ball.eKin), 750, 100);

        g.drawString("Epot " + new DecimalFormat("#.###").format(ball.ePot), 750, 110);

        g.drawString("Etot " + new DecimalFormat("#.###").format(ball.eTot), 750, 120);
    }

    public static void addE(double ePot, double eKin, double eTot)
    {
        currentIndex++;
        if (currentIndex >= size)
            currentIndex = 0;

        ePotArray[currentIndex] = ePot;
        eKinArray[currentIndex] = eKin;
        eTotArray[currentIndex] = eTot;

        if (eTot > biggest)
            biggest = eTot;
    }

    public static void renderGraph(Graphics g)
    {
        //draw edges
        g.setColor(Color.red);
        g.fillRect(graphLeft - lineWidth, graphBottom, graphWidth + (lineWidth * 2) + 1, lineWidth); //bottom
        g.fillRect(graphLeft - lineWidth, graphBottom, lineWidth, -graphHeight); //left
        g.fillRect(graphLeft - lineWidth, graphBottom - graphHeight - lineWidth, graphWidth + (lineWidth * 2) + 1, lineWidth); //top
        g.fillRect(graphLeft + graphWidth + lineWidth, graphBottom - graphHeight - lineWidth, lineWidth, graphHeight + (lineWidth * 2)); //right

        //draw numbers bottom
        g.setColor(Color.white);
        g.drawString("0", graphLeft, graphBottom + lineWidth + 10);
        g.drawString("2", (int) (graphLeft + (0.25 * graphWidth)), graphBottom + lineWidth + 10);
        g.drawString("4", (int) (graphLeft + (0.5 * graphWidth)), graphBottom + lineWidth + 10);
        g.drawString("5", (int) (graphLeft + (0.75 * graphWidth)), graphBottom + lineWidth + 10);
        g.drawString("8", graphLeft + graphWidth - 15, graphBottom + lineWidth + 10);

        //draw numbers left
        g.drawString(new DecimalFormat("#.###").format(biggest), graphLeft - lineWidth - 35, graphBottom - graphHeight + 5);
        g.drawString(new DecimalFormat("#.###").format(biggest * 0.75), graphLeft - lineWidth - 35, (int) (graphBottom - (graphHeight * 0.75) + 5));
        g.drawString(new DecimalFormat("#.###").format(biggest * 0.5), graphLeft - lineWidth - 35, (int) (graphBottom - (graphHeight * 0.5) + 5));
        g.drawString(new DecimalFormat("#.###").format(biggest * 0.25), graphLeft - lineWidth - 35, (int) (graphBottom - (graphHeight * 0.25) + 5));
        g.drawString("0", graphLeft - lineWidth - 10, graphBottom);

        for (int i = 0; i < size; i++)
        {
            if (eTotArray[i] != null)
            {
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
        }
    }
}