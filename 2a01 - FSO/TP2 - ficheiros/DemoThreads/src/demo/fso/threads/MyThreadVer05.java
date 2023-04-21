package demo.fso.threads;

import java.util.Random;
import java.util.concurrent.Semaphore;

public class MyThreadVer05 extends Thread {
	private boolean isHello;
	
	private Semaphore sHelloToWorld;
	private Semaphore sWorldToHello;
	
	private Random rnd;
	
	private String argument;
	
	public MyThreadVer05(
			String argument, 
			Semaphore sHelloToWorld, 
			Semaphore sWorldToHello, 
			boolean isHello) {
		super( argument );
		
		this.rnd = new Random();
		this.argument = argument;
		this.sHelloToWorld = sHelloToWorld;
		this.sWorldToHello = sWorldToHello;
		this.isHello = isHello;
	}
	
	public MyThreadVer05( 
			String argument,
			Semaphore sHelloToWorld, 
			Semaphore sWorldToHello) {
		this(argument, sHelloToWorld, sWorldToHello, false);
	}
		
	@Override
	public void run() {
		System.out.printf( "[%s] Running.\n", this.getName() );
		try {
			Thread.sleep( this.rnd.nextInt( 2500) );
			
			if ( this.isHello ) {
				this.sWorldToHello.acquire();

				System.out.print( this.argument );
				
				this.sHelloToWorld.release();
			}
			else {
				this.sHelloToWorld.acquire();

				System.out.println( this.argument );
				
				this.sWorldToHello.release();
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
}
