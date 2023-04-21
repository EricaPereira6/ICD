package demo.fso.threads;

import java.util.Random;
import java.util.concurrent.Semaphore;

public class MyThreadVer04 extends Thread {
	private boolean isHello;
	
	private Semaphore s;
	
	private Random rnd;
	
	private String argument;
	
	public MyThreadVer04(String argument, Semaphore s, boolean isHello) {
		super( argument );
		
		this.rnd = new Random();
		this.argument = argument;
		this.s = s;
		this.isHello = isHello;
	}
	
	public MyThreadVer04(String argument, Semaphore s) {
		this(argument, s, false);
	}
		
	@Override
	public void run() {
		System.out.printf( "[%s] Running.\n", this.getName() );
		try {
			if ( this.isHello ) {
				Thread.sleep( 2500 + this.rnd.nextInt( 2500) );
				
				System.out.print( this.argument );
				this.s.release();
			}
			else {
				Thread.sleep( 250 + this.rnd.nextInt( 250 ) );
				
				this.s.acquire();

				System.out.println( this.argument );				
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
}
