import java.util.ArrayList;
import java.util.concurrent.Semaphore;

public class Descritor {

//	private ArrayList<Semaphore> semaforos;
	private Semaphore ocupado;
	private int getBuffer;
	private int ID;
	
	public Descritor(int ID) {
		
		ocupado = new Semaphore(0);
//		semaforos = new ArrayList<Semaphore>();
//		for(int i = 0; i < BUFFER_MAX; i++) {
//			semaforos.add(new Semaphore(0));
//		}
		
		getBuffer = 0;
		this.ID = ID;
	}
	
	public Semaphore getSemaforo() {
		return ocupado;
	}

//	public ArrayList<Semaphore> getSemaforos() {
//		return semaforos;
//	}
//	
//	public Semaphore getSemaforo(int posicao) {
//		return semaforos.get(posicao);
//	}

	public int getGetBufferPosition() {
		return getBuffer;
	}

	public void setGetBufferPosition(int getBuffer) {
		this.getBuffer = getBuffer;
	}

	public int getID() {
		return ID;
	}
	
}
