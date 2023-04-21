package demo.fso.threads;

import java.util.Random;
import java.util.concurrent.Semaphore;

public class MyThreadVer06 extends Thread {
	private boolean isHello;
	
	private int numberOfIterations;
	
	private Semaphore sHelloToWorld;
	private Semaphore sWorldToHello;
	
	private Random rnd;
	
	private String argument;
	
	public MyThreadVer06(
			String argument, 
			Semaphore sHelloToWorld, 
			Semaphore sWorldToHello,
			int numberOfIterations,
			boolean isHello) {
		super( argument );
		
		this.rnd = new Random();
		this.argument = argument;
		this.sHelloToWorld = sHelloToWorld;
		this.sWorldToHello = sWorldToHello;
		this.numberOfIterations = numberOfIterations;
		this.isHello = isHello;
	}
	
	public MyThreadVer06( 
			String argument,
			Semaphore sHelloToWorld, 
			Semaphore sWorldToHello,
			int numberOfIterations) {
		this(argument, sHelloToWorld, sWorldToHello, numberOfIterations, false);
	}
		
	@Override
	public void run() {
		System.out.printf( "[%s] Running.\n", this.getName() );
		try {
			for(int i=0; i<this.numberOfIterations; ++i) {
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
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
}
