package demo.fso.threads;

import java.util.Random;

public class MyThread extends Thread {
	
	private Random rnd;
	
	private String argument;
	
	public MyThread(String argument) {
		this.rnd = new Random();
		this.argument = argument;
	}
	
	@Override
	public void run() {
		System.out.printf( "[%s] Running.\n", this.getName() );
		
		try {
			Thread.sleep( 500 + this.rnd.nextInt( 2500) );
			
			System.out.print( this.argument );
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
}
