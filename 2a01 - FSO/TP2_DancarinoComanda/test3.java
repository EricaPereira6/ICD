import java.util.ArrayList;

public class test3 implements Runnable {

	private int estado;
	private testv v;
	private ArrayList<MyMessage> memoria;
	
	@Override
	public void run() {
		while(estado != 1) {
			automato3();
		}
	}
	
	public test3(testv tv) {
		System.out.println("test3 construtor");
		estado = 0;
		v = tv;
		memoria = v.getMemoria();
	}
	
	public void automato3() {
		switch(estado) {
		case 0:
			memoria = v.getMemoria();
			System.out.println("3: " + memoria);
			if(!memoria.isEmpty()) {
				System.out.println("mensagem da memoria: [" + memoria.get(0).getNumero() + ", " + memoria.get(0).getOrdem() + "]");
				memoria.remove(0);
				v.setMemoria(memoria);
			}
			try {
				Thread.sleep(800);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			break;
		case 1:
			System.out.println(estado);
			System.out.println("FIM automato3");
			break;
		}
	}

}
