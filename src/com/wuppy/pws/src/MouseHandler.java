package com.wuppy.pws.src;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class MouseHandler implements MouseListener
{
	public void mouseClicked(MouseEvent e)
	{
		//plek
		int x = e.getX();
		int y = e.getY();

		//veranderen bal voor tabel
		if (e.getButton() == 3)
		{
			if (Main.isBallInPosition(x, y))
			{
				Main.ballId = Main.getBallAtPosition(x, y);
			}
		}

		//linker muisknop
		if (e.getButton() == 1)
		{
			//als in knop
			if (x >= 950 && x <= 1200 && y >= 50 && y <= 100)
			{
				//verander knop
				Main.all = !Main.all;
			}
		}
	}

	public void mouseEntered(MouseEvent e)
	{

	}

	public void mouseExited(MouseEvent e)
	{

	}

	public void mousePressed(MouseEvent e)
	{

	}

	//muis losgelaten
	public void mouseReleased(MouseEvent e)
	{
		//plek
		int mousex = e.getX();
		int mousey = e.getY();

		//is goede plek en muis knop
		if (Main.isValidPosition(mousex, mousey, Main.ballSize) && e.getButton() == 1)
		{
			//nieuwe bal
			int id = Main.ballen.size();
			Main.ballen.add(new Ball(mousex, mousey, id, Main.ballSize));
		}
	}
}