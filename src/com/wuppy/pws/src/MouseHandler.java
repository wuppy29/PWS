package com.wuppy.pws.src;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class MouseHandler implements MouseListener
{
	public void mouseClicked(MouseEvent e)
	{
		if(e.getButton() == 2)
		{
			if(Main.isBallInPosition(e.getX(), e.getY()))
			{
				Main.ballId = Main.getBallAtPosition(e.getX(), e.getY());
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
	
	public void mouseReleased(MouseEvent e)
	{
		int mousex = e.getX();
		int mousey = e.getY();
		
		if(Main.isValidPosition(mousex, mousey) && e.getButton() == 1)
		{
			Main.ballen.add(new Ball(mousex, mousey, Main.ballen.size()));
		}
	}
}