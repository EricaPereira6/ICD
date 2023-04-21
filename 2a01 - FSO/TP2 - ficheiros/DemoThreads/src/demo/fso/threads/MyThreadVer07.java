package demo.fso.threads;

import java.util.Random;

import demo.fso.sync.IMySyncPoint;

public class MyThreadVer07 extends Thread {
	private boolean isHello;
	
	private int numberOfIterations;
	
	private IMySyncPoint sHelloToWorld;
	private IMySyncPoint sWorldToHello;
	
	private Random rnd;
	
	private String argument;
	
	public MyThreadVer07(
			String argument, 
			IMySyncPoint sHelloToWorld, 
			IMySyncPoint sWorldToHello,
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
	
	public MyThreadVer07( 
			String argument,
			IMySyncPoint sHelloToWorld, 
			IMySyncPoint sWorldToHello,
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
					this.sWorldToHello.entrar();
	
					System.out.print( this.argument );
					
					this.sHelloToWorld.sair();
				}
				else {
					this.sHelloToWorld.entrar();
	
					System.out.println( this.argument );
					
					this.sWorldToHello.sair();
				}
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
}
