package demo.fso.threads;

import java.util.Random;

import demo.fso.sync.MyFlag;

public class MyThreadVer03 extends Thread {
	private boolean isHello;
	
	private MyFlag flag;
	
	private Random rnd;
	
	private String argument;
	
	public MyThreadVer03(String argument, MyFlag flag, boolean isHello) {
		super( argument );
		
		this.rnd = new Random();
		this.argument = argument;
		this.flag = flag;
		this.isHello = isHello;
	}
	
	public MyThreadVer03(String argument, MyFlag flag) {
		this(argument, flag, false);
	}
		
	@Override
	public void run() {
		System.out.printf( "[%s] Running.\n", this.getName() );
		try {
			if ( this.isHello ) {
				Thread.sleep( 2500 + this.rnd.nextInt( 2500) );
				
				System.out.print( this.argument );
				this.flag.setFlag( true );
			}
			else {
				Thread.sleep( 250 + this.rnd.nextInt( 250 ) );
				
				while ( this.flag.isFlag()==false ) {
					Thread.sleep(1);
				}
				System.out.println( this.argument );
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
}
