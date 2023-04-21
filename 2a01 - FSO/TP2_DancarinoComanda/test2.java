import java.util.ArrayList;
import java.util.concurrent.Semaphore;

public class test2 implements Runnable {
	
	private int estado;
	private int i;
	private Semaphore s;
	
	private ArrayList<MyMessage> memoria;
	private testv v;
	
	@Override
	public void run() {
		while(estado != 1) {
			automato2();
		}
		
	}
	
	public test2(testv tv) {
		System.out.println("test2 construtor");
		memoria = new ArrayList<MyMessage>();
		estado = 0;
		i = 0;
		v = tv;
		s = new Semaphore(5);
	}

	public void automato2() {
		switch(estado){
			case 0:
				i++;
				memoria = v.getMemoria();
				memoria.add(new MyMessage(i, 0));
				v.setMemoria(memoria);
				System.out.println("2: " + memoria);
				try {
					Thread.sleep(2000);
				} catch (InterruptedException e) {
					System.err.println("Thread.sleep(2000): deu erro test2: " + e.getMessage());
				}
			try {
				s.acquire();
				System.out.println("test2 acquired - nPermits: " + s.availablePermits());
			} catch (InterruptedException e) {
				System.err.println("s.acquire(): deu erro test2: " + e.getMessage());
			}
				break;
			case 1:
				System.out.println("fim");
				break;
		}
	}

}
