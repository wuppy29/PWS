package com.wuppy.pws.src;

import java.awt.Color;
import java.awt.Graphics;
import java.util.Random;

public class Ball
{
	//locatie
	double x, y;

	//snelheid
	double vx = 0, vy = 0;

	//verplaatsing
	double dx = 0, dy = 0;

	//extra verplaatsing stuiter randen
	double dbx = 0, dby = 0;

	//diameter
	int size;

	//straal
	double radius;

	//willekeurige kleur
	float r, gr, b;

	//bal nummer
	int id;

	//zwaartekracht
	double g = 9.81;

	//gewicht bal
	double m = 0.0425;

	//lucht dichtheid
	double rho = 1.2041;

	//Weerstandscoëfficiënt
	double Cd = 0.45;

	//oppervlakte & volume
	double A, V;

	//Resulterende krachten
	double Fresx = 0, Fresy = 0;

	//zwaartekracht
	double Fzw = m * g;

	//Luchtweerstand
	double Fdx, Fdy;

	//totale energie
	double eTot = 0;

	//kinetische & potentiele energie
	double eKin, ePot;

	//hoogte & snelheid
	double h, v;

	//versnelling
	double ax = 0, ay = 0;

	public Ball(int x, int y, int id, int size)
	{
		//instellen locatie etc. voor deze Ball
		this.x = x;
		this.y = y;
		this.id = id;
		this.size = size;

		//uitrekenen straal, volume, oppervlakte
		radius = (double) size / 2D / 100D;
		V = 4D / 3D * Math.PI * Math.pow(radius, 3);
		A = Math.PI * Math.pow(radius, 2);

		//uitrekenen luchtweerstand startwaarde
		Fdx = 0.5 * rho * Math.pow(vx, 2) * Cd * A;
		Fdy = 0.5 * rho * Math.pow(vy, 2) * Cd * A;

		//willekeurige kleur voor bal
		Random rand = new Random();
		r = rand.nextFloat();
		gr = rand.nextFloat();
		b = rand.nextFloat();
	}

	public void render(Graphics g)
	{
		//tekencode bal
		g.setColor(new Color(r, gr, b));
		g.fillOval((int) (x), (int) (y), size, size);
	}

	public void update()
	{
		//forces
		calculateForces();

		//snelheid y
		vy += ay * Main.dt;

		//verplaatsing y
		dy = vy * Main.dt * 100;

		//snelheid x
		vx += ax * Main.dt;

		//verplaatsing x
		dx = vx * Main.dt * 100;

		//als bal op de grond en naar beneden bewegend
		if (vy > 0 && y + dy > Main.bottom - size)
		{
			//extra onder streep
			dby = y + dy - Main.bottom + size;

			//ga omhoog met stuk
			y = Main.bottom - size - dby;

			//verander richting
			vy = -vy;
		}
		//als bal tegen bovenstreep en naar boven bewegend
		else if (vy < 0 && y + dy < Main.top)
		{
			//extra boven streep
			dby = y + dy - Main.top;

			//ga omlaag met stuk
			y = Main.top + dby;

			//verander richting
			vy = -vy;
		}
		//als gewone beweging
		else
			y += dy;

		//als bal naar rechts
		if (vx > 0 && x + dx > Main.right - size)
		{
			//extra rechts
			dbx = x + dx - Main.right + size;

			//ga stuk naar links
			x = Main.right - size + dbx;

			//omkeren snelheid
			vx = -vx;
		}
		//als bal links
		else if (vx < 0 && x + dx < Main.left)
		{
			//extra links
			dbx = x + dx - Main.left;

			//ga stuk naar rechts
			x = Main.left + dbx;

			//omkeren snelheid
			vx = -vx;
		}
		//als gewone beweging
		else
			x += dx;

		/*
		 * //bounce between balls
		for (int i = 0; i < Main.ballen.size(); i++)
		{
			if (i != id)
			{
				Ball b2 = Main.ballen.get(i);
				
				if(b2 != null)
				{
					double distance = Math.sqrt(Math.pow((x + (size / 2)) - (b2.x + (b2.size / 2)), 2) + Math.pow((y + (size / 2)) - (b2.y + (b2.size / 2)), 2));
					
					if (distance <= size && b2.dir - dir < 180 && !bouncer[b2.id] && !b2.bouncer[id])
					{
						//verschil x en y
						double dx = Math.abs(b2.x - x);
						double dy = Math.abs(b2.y - y);

						//hoek tussen ballen
						double difdir = Math.atan2(dy, dx);
						difdir = Math.toDegrees(difdir);

						//totale snelheid b en b2
						double v1 = Math.sqrt(Math.pow(vx, 2) + Math.pow(vy, 2));
						double v2 = Math.sqrt(Math.pow(b2.vx, 2) + Math.pow(b2.vy, 2));

						//gewicht tweede bal
						double m2 = b2.m;

						double vxe1 = ((((v1 * Math.cos(dir - difdir)) * (m - m2) + 2 * m2 * (v2 * Math.cos(b2.dir - difdir)))) / (m + m2)) * Math.cos(difdir) + (v1 * Math.sin(dir - difdir) * Math.cos(difdir + (Math.PI / 2)));
						double vye1 = ((((v1 * Math.cos(dir - difdir)) * (m - m2) + 2 * m2 * (v2 * Math.cos(b2.dir - difdir)))) / (m + m2)) * Math.sin(difdir) + (v1 * Math.sin(dir - difdir) * Math.sin(difdir + (Math.PI / 2)));

						double vxe2 = ((((v2 * Math.cos(b2.dir - difdir)) * (m2 - m) + 2 * m * (v1 * Math.cos(dir - difdir)))) / (m + m2)) * Math.cos(difdir) + (v2 * Math.sin(b2.dir - difdir) * Math.cos(difdir + (Math.PI / 2)));
						double vye2 = ((((v2 * Math.cos(b2.dir - difdir)) * (m2 - m) + 2 * m * (v1 * Math.cos(dir - difdir)))) / (m + m2)) * Math.sin(difdir) + (v2 * Math.sin(b2.dir - difdir) * Math.sin(difdir + (Math.PI / 2)));

						b2.vy = vye2;
						b2.vx = vxe2;
						vy = vye1;
						vx = vxe1;

						bouncer[b2.id] = true;
						b2.bouncer[id] = true;
					}
				}
			}
		}
		 */

		//snelheid hoogte en energie berekening
		v = Math.sqrt(Math.pow(vx, 2) + Math.pow(vy, 2));
		h = (-y + Main.bottom + size) / 100;
		eKin = 0.5 * m * Math.pow(v, 2);
		ePot = m * g * h;
		eTot = eKin + ePot;
	}

	public void calculateForces()
	{
		//drag y
		Fdy = 0;//0.5 * rho * Math.pow(vy, 2) * Cd * A;

		//Fres y
		if (vy > 0)
		{
			Fresy = Fzw - Fdy;
		}
		else
		{
			Fresy = Fzw + Fdy;
		}

		//versnelling y
		ay = Fresy / m;

		//Drag x
		Fdx = 0;//0.5 * rho * Math.pow(vx, 2) * Cd * A;

		//Fres x
		if (vx > 0)
			Fresx = -Fdx;
		else
			Fresx = Fdx;

		//versnelling x
		ax = Fresx / m;
	}

	public static void handleCollisions()
	{
		double xDist, yDist;
		for (int i = 0; i < Main.ballen.size(); i++)
		{
			Ball a = Main.ballen.get(i);
			for (int j = i + 1; j < Main.ballen.size(); j++)
			{
				Ball b = Main.ballen.get(j);
				xDist = a.getCenterX() - b.getCenterX();
				yDist = a.getCenterY() - b.getCenterY();
				double distSquared = xDist * xDist + yDist * yDist;
				//De kwadratische afstand nakijken, voorkomt een wortel
				if (distSquared <= (a.getWidth() + b.getWidth()) * (a.getWidth() + b.getWidth()))
				{
					double vxocity = b.vx - a.vx;
					double vyocity = b.vy - a.vy;
					double dotProduct = xDist * vxocity + yDist * vyocity;
					//Bewegen de ballen naar elkaar toe

					if (dotProduct > 0)
					{
						double collisionScale = dotProduct / distSquared;
						double xCollision = xDist * collisionScale;
						double yCollision = yDist * collisionScale;

						double combinedMass = a.m + b.m;
						double collisionWeightA = 2 * b.m / combinedMass;
						double collisionWeightB = 2 * a.m / combinedMass;
						a.vx += collisionWeightA * xCollision;
						a.vy += collisionWeightA * yCollision;
						b.vx -= collisionWeightB * xCollision;
						b.vy -= collisionWeightB * yCollision;
					}
				}
			}
		}
	}

	private double getCenterX()
	{
		return x + (size / 2);
	}

	private double getCenterY()
	{
		return y + (size / 2);
	}

	private double getWidth()
	{
		return size / 2;
	}

	//x positie bal
	public double getX()
	{
		return x;
	}

	//y positie bal
	public double getY()
	{
		return y;
	}
}