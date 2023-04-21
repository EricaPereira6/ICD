package demo.fso.app;

import java.lang.reflect.Constructor;
import java.util.concurrent.Semaphore;

import demo.fso.runnables.MyRunnable;
import demo.fso.sync.IMySyncPoint;
import demo.fso.sync.MyFlag;
import demo.fso.sync.MyFlagAtomic;
import demo.fso.threads.MyThread;
import demo.fso.threads.MyThreadVer02;
import demo.fso.threads.MyThreadVer03;
import demo.fso.threads.MyThreadVer04;
import demo.fso.threads.MyThreadVer05;
import demo.fso.threads.MyThreadVer06;
import demo.fso.threads.MyThreadVer07;

public class DemoThreads {

	public static void ver00() {
		try {
			MyThread t1, t2;
			t1 = new MyThread( "ola " );
			t2 = new MyThread( "mundo.\n" );
			
			t2.run();
			t1.run();
		}
		catch (Exception ex) {
			ex.printStackTrace();
		}
	}
	
	public static void ver01() {
		try {
			MyThread t1, t2;
			t1 = new MyThread( "ola " );
			t2 = new MyThread( "mundo.\n" );
			
			t2.start();
			t1.start();
		}
		catch (Exception ex) {	ex.printStackTrace(); }
	}
	
	public static void ver02() {
		try {
			MyRunnable r1, r2;
			r1 = new MyRunnable( "ola " );
			r2 = new MyRunnable( "mundo.\n" );
			
			Thread t1, t2;
			t1 = new Thread( r1 );
			t2 = new Thread( r2 );
			
			t1.start();
			t2.start();
		}
		catch (Exception ex) {	ex.printStackTrace(); }
	}
	
	public static void ver03() {
		try {
			MyRunnable r1, r2;
			r1 = new MyRunnable( "ola " );
			r2 = new MyRunnable( "mundo.\n" );
			
			Thread t1, t2;
			t1 = new Thread( r1 );
			t2 = new Thread( r2 );
			
			for(int i=0; i<5; ++i ) {
				t1.start();
				t1.join();
				
				t2.start();
				t2.join();
			}
			
		}
		catch (Exception ex) {	ex.printStackTrace(); }
	}
	
	public static void ver04() {
		try {
			MyFlag flag;
			flag = new MyFlag( false );
			
			Thread t1, t2;
			t1 = new MyThreadVer02( "ola ", flag );
			t2 = new MyThreadVer02( "mundo.\n", flag );
			
			t1.start();
			t2.start();
		}
		catch (Exception ex) {	ex.printStackTrace(); }
	}
	
	public static void ver05() {
		try {
			MyFlag flag;
			flag = new MyFlagAtomic( false );
			
			Thread t1, t2;
			t1 = new MyThreadVer02( "ola ", flag );
			t2 = new MyThreadVer02( "mundo1.\n", flag );
			
			t1.start();
			t2.start();
			
			//(new MyThreadVer02( "mundo2.\n", flag ) ).start();
			//(new MyThreadVer02( "mundo3.\n", flag ) ).start();
			//(new MyThreadVer02( "mundo4.\n", flag ) ).start();
		}
		catch (Exception ex) {	ex.printStackTrace(); }
	}
	
	public static void ver06() {
		try {
			MyFlag flag;
			flag = new MyFlagAtomic( false );
			
			Thread t1, t2;
			t1 = new MyThreadVer03( "ola ", flag, true );
			t2 = new MyThreadVer03( "mundo.", flag );
			
			t1.start();
			t2.start();
		}
		catch (Exception ex) {	ex.printStackTrace(); }
	}
	
	public static void ver07() {
		try {
			Semaphore s;
			s = new Semaphore( 0 );
			
			Thread t1, t2;
			t1 = new MyThreadVer04( "ola ", s, true );
			t2 = new MyThreadVer04( "mundo.", s );
			
			t1.start();
			t2.start();
		}
		catch (Exception ex) {	ex.printStackTrace(); }
	}
	
	public static void ver08() {
		try {
			Semaphore sHelloToWorld, sWorldToHello;
			sHelloToWorld = new Semaphore( 0 );
			sWorldToHello = new Semaphore( 1 );
			
			Thread t1, t2;
			t1 = new MyThreadVer05( 
					"ola ", 
					sHelloToWorld, 
					sWorldToHello, 
					true );
			t2 = new MyThreadVer05( 
					"mundo.",
					sHelloToWorld, 
					sWorldToHello );

			t1.start();
			t2.start();
			
			//sWorldToHello.release();
		}
		catch (Exception ex) {	ex.printStackTrace(); }
	}
	
	public static void ver09() {
		try {
			System.out.printf( "[%s] Starting.\n", Thread.currentThread().getName() );
			
			Semaphore sHelloToWorld, sWorldToHello;
			sHelloToWorld = new Semaphore( 0 );
			sWorldToHello = new Semaphore( 0 );
			
			int numberOfIterations;
			numberOfIterations = 5;
			
			Thread t1, t2;
			t1 = new MyThreadVer06( 
					"ola ", 
					sHelloToWorld, 
					sWorldToHello,
					numberOfIterations, 
					true );
			t2 = new MyThreadVer06( 
					"mundo.",
					sHelloToWorld, 
					sWorldToHello,
					numberOfIterations );

			t1.start();
			t2.start();
			
			sWorldToHello.release();
			
			t2.join();
		}
		catch (Exception ex) {
			ex.printStackTrace();
		}
		finally {
			System.out.printf( "[%s] Ending.\n", Thread.currentThread().getName() );
		}
	}
	
	public static void ver10() {
		try {
			System.out.printf( "[%s] Starting.\n", Thread.currentThread().getName() );
			
			IMySyncPoint sHelloToWorld, sWorldToHello;
			
			sHelloToWorld = new demo.fso.sync.MySyncPointSemaphore();
			sWorldToHello = new demo.fso.sync.MySyncPointSemaphore();
			
			//sHelloToWorld = new demo.fso.sync.MySyncPointMonitor();
			//sWorldToHello = new demo.fso.sync.MySyncPointMonitor();
			
			int numberOfIterations;
			numberOfIterations = 5;
			
			Thread t1, t2;
			t1 = new MyThreadVer07( 
					"ola ", 
					sHelloToWorld, 
					sWorldToHello,
					numberOfIterations, 
					true );
			t2 = new MyThreadVer07( 
					"mundo.",
					sHelloToWorld, 
					sWorldToHello,
					numberOfIterations );

			t1.start();
			t2.start();
			
			sWorldToHello.sair();
			
			t2.join();
		}
		catch (Exception ex) {
			ex.printStackTrace();
		}
		finally {
			System.out.printf( "[%s] Ending.\n", Thread.currentThread().getName() );
		}
	}
	
	public static void ver11() {
		try {
			System.out.printf( "[%s] Starting.\n", Thread.currentThread().getName() );
			
			IMySyncPoint sHelloToWorld, sWorldToHello;
			
			String syncPointClassName;			
			syncPointClassName = "demo.fso.sync.MySyncPointMonitor";
			syncPointClassName = "demo.fso.sync.MySyncPointSemaphore";
			
			Class<?> klass = Class.forName( syncPointClassName );
			Constructor<?> ctor = klass.getConstructor();
			
			sHelloToWorld = (IMySyncPoint)ctor.newInstance(new Object[] {} );
			sWorldToHello = (IMySyncPoint)ctor.newInstance(new Object[] {} );
			
			int numberOfIterations;
			numberOfIterations = 5;
			
			Thread t1, t2;
			t1 = new MyThreadVer07( 
					"ola ", 
					sHelloToWorld, 
					sWorldToHello,
					numberOfIterations, 
					true );
			t2 = new MyThreadVer07( 
					"mundo.",
					sHelloToWorld, 
					sWorldToHello,
					numberOfIterations );

			t1.start();
			t2.start();
			
			sWorldToHello.sair();
			
			t2.join();
		}
		catch (Exception ex) {
			ex.printStackTrace();
		}
		finally {
			System.out.printf( "[%s] Ending.\n", Thread.currentThread().getName() );
		}
	}
	
	public static void main(String[] args) {
		//ver00();
		//ver01();
		//ver02();
		//ver03();
		//ver04();
		//ver05();
		//ver06();
		//ver07();
		//ver08();
		ver09();
		//ver10();
		//ver11();
	}
}
