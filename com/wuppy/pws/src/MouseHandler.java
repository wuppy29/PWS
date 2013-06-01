package com.wuppy.pws.src;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class MouseHandler implements MouseListener
{
	public void mouseClicked(MouseEvent e)
	{
		
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
		
		if(Main.isValidPosition(mousex, mousey))
		{
			Main.ballen.add(new Ball(mousex, mousey));
		}
	}
}