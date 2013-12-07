package com.wuppy.pws.src;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class KeyHandler implements KeyListener
{
	public void keyPressed(KeyEvent e)
	{
		//pause functie met spatie
		if (e.getKeyCode() == KeyEvent.VK_SPACE)
		{
			Main.paused = !Main.paused;
		}
	}

	public void keyReleased(KeyEvent e)
	{

	}

	public void keyTyped(KeyEvent e)
	{

	}
}