package demo.fso.threads;

import java.util.Random;

import demo.fso.sync.MyFlag;

public class MyThreadVer02 extends Thread {
	private MyFlag flag;
	
	private Random rnd;
	private String argument;
	public MyThreadVer02(String argument, MyFlag flag) {
		this.rnd = new Random();
		this.argument = argument;
		
		this.flag = flag;
	}
	
	@Override
	public void run() {
		System.out.printf( "[%s] Running.\n", this.getName() );
		
		try {
			Thread.sleep( 500 + this.rnd.nextInt( 2500) );
			if ( this.argument.startsWith( "o" ) ) {
				System.out.print( this.argument );
				this.flag.setFlag( true );
			}
			else {
				while ( this.flag.isFlag()==false ) {
					Thread.sleep(1);
				}
				System.out.print( this.argument );
				
				//this.flag.setFlag( true );
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
}
