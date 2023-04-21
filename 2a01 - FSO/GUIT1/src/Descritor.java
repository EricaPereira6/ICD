import java.util.concurrent.Semaphore;

public class Descritor {
	
	private Semaphore ocupados;
	private int getBuffer;
	private int ID;
	
	public Descritor(int ID) {
		ocupados = new Semaphore(0);	
		getBuffer = 0;
		this.ID = ID;
	}
	
	public Semaphore getSemaforo() {
		return ocupados;
	}
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

