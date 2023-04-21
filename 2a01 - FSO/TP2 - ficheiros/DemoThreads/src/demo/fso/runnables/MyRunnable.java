package demo.fso.runnables;

import java.util.Random;

public class MyRunnable implements Runnable {
	
	private Random rnd;
	
	private String argument;
	
	public MyRunnable(String argument) {
		this.rnd = new Random();
		this.argument = argument;
	}
	
	@Override
	public void run() {
		System.out.printf( "[%s] Running.\n", Thread.currentThread().getName() );
		
		try {
			Thread.sleep( 500 + this.rnd.nextInt( 2500) );
			System.out.print( this.argument );
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
}
