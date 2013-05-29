package com.wuppy.pws.src;

import javax.swing.JFrame;

public class Main extends JFrame implements Runnable
{
	private static final long serialVersionUID = 1L;
	
	public static void main(String[] args)
	{
		@SuppressWarnings("unused")
		Main main = new Main();
	}
	
	public Main()
	{
		setTitle("Ballen");
		setSize(600, 600);
		setResizable(false);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setVisible(true);
	}
	
	public void run()
	{
		
	}
}