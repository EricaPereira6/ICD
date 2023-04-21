import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.concurrent.Semaphore;

public class CanalComunicacao implements Runnable {

	// dimensão máxima em bytes do buffer
	final int BUFFER_MAX = 256; // 32 mensagens de 2 ints com 4 bytes por cada int - 32*8 = 256
//	private final int BUFFER_MAX = 32; // 4 mensagens para teste
	
	private final int DIVISOR = 8;
	private final int NUMERO_MSG = (BUFFER_MAX / DIVISOR);

	private ByteBuffer buffer;

	private int putBuffer, number, order;

	private Semaphore exclusaoMutua, bloquearCanal, livres;

	private int estado;
	private final int BLOQUEAR_CANAL = 0;
	private final int LER = 1;
	private final int ESCREVER = 2;
	private final int TERMINAR = 3;
	

	private int ID;
	private ArrayList<Descritor> leitores;
	private ArrayList<Integer> escritores;
	private ArrayList<Integer> clientesleitores;
	private ArrayList<Integer> clientesescritores;
	
	private BDCanal bd;

	public CanalComunicacao() {
		estado = BLOQUEAR_CANAL;
		buffer = ByteBuffer.allocate(BUFFER_MAX);

		putBuffer = 0;
		number = 0;
		order = 0;

		clearBuffer();

		// Semaforos
		exclusaoMutua = new Semaphore(1);
		bloquearCanal = new Semaphore(0);
		livres = new Semaphore(NUMERO_MSG);

		leitores = new ArrayList<Descritor>();
		escritores = new ArrayList<Integer>();
		clientesleitores = new ArrayList<Integer>();
		clientesescritores = new ArrayList<Integer>();

		ID = 0;
		
		bd = new BDCanal();
//		gui = new GUICanal(bd);
	}
	
	
	public void run() {
		while (estado != TERMINAR) {
			automatoCanal();
		}
	}

	
	private void automatoCanal() {
		switch (estado) {
			case BLOQUEAR_CANAL:
				if(bd.isFim()) {
					estado = TERMINAR;
				}
				try {
//					gui.printChannel("");
//					gui.printChannel("-> BLOQUEAR_CANAL");
					bloquearCanal.acquire();
					
					if(!clientesescritores.isEmpty()) {
						if(estado == BLOQUEAR_CANAL)
							estado = ESCREVER;
					}
					if(!clientesleitores.isEmpty()) {
						if(estado == BLOQUEAR_CANAL)
							estado = LER;
					}
				} catch (InterruptedException e) {
					System.err.println("CanalComunicacao automatoCanal() error - Semaphore bloquearCanal not working: "
							+ e.getMessage());
				}
				break;
				
			case LER:
//				gui.printChannel("-> LER_AUX -> ");
//				gui.printChannel("actualizar livres ");
//				gui.printChannel("livres antes: " + livres.availablePermits());
				actualizarLivres();
//				gui.printChannel("livres depois: " + livres.availablePermits());

				clientesleitores.remove(0);
				
				if (estado == LER)
					estado = BLOQUEAR_CANAL;			
				break;
			
			case ESCREVER:
//				gui.printChannel("-> ESCREVER_AUX -> ");
//				gui.printChannel("Semaforos dos leitores: ");
				for(Descritor d : leitores) {
					d.getSemaforo().release();
//					gui.printChannel(" - leitor " + d.getID() + ", n atual: " + d.getSemaforo().availablePermits());
				}
				clientesescritores.remove(0);
				
				if (estado == ESCREVER)
					estado = BLOQUEAR_CANAL;
				break;
			
			case TERMINAR:
				break;
				
			default:
				System.err.println("Erro CanalComunicacao - automatoCanal() - estado " + estado);
				break;
		}
	}
	
	
	public void escrever(MyMessage msgEscritor, int id) throws IOException {
//		gui.printChannel("");
//		gui.printChannel("escritor " + id + " quer escrever " + msgEscritor.toString() + " ! --- " + Thread.currentThread());
		
		if(!idEscritorAutorizado(id)) {
			throw new IOException("INVALID ID");
		} 

		try {
//			gui.printChannel("livres will be acquired");
//			gui.printChannel("-> livres before: " + livres.availablePermits());
			livres.acquire();
//			gui.printChannel("-> livres after: " + livres.availablePermits());
			
			escreverMensagem(msgEscritor);
			
			clientesescritores.add(id);
			bloquearCanal.release();
			
		} catch (InterruptedException e1) {
			System.err.println(
					"CanalComunicacao escrever() error - Semaphore livres not working: "
							+ e1.getMessage());
		}

	}
	
	
	public MyMessage ler(int id) throws IOException {
//		gui.printChannel("");
//		gui.printChannel("leitor " + id + " quer ler ! --- " + Thread.currentThread());
		
		MyMessage msgLeitor = new MyMessage(0,0);
		
		if(!idLeitorAutorizado(id)) {
			throw new IOException("INVALID ID");
		}
		
		Descritor leitor = getLeitor(id);
		Semaphore semaforoLeitor = leitor.getSemaforo();
		
		try {
//			gui.printChannel("semaforoLeitor will be acquired");
//			gui.printChannel("-> leitor before: " + semaforoLeitor.availablePermits());
			semaforoLeitor.acquire();
//			gui.printChannel("-> leitor after: " + semaforoLeitor.availablePermits());
			
			msgLeitor = lerMensagem(leitor);
			
			clientesleitores.add(id);
			bloquearCanal.release();

		} catch (InterruptedException e) {
			System.err.println(
					"CanalComunicacao lerMensagem() error - Semaphore semaforoLeitor not working: "
							+ e.getMessage());
		}

//		gui.printChannel("!!!!! Mensagem lida pelo leitor " + leitor.getID() + ": " + msgLeitor + "!!!!!");
		return msgLeitor;
	}

	
	private void escreverMensagem(MyMessage mensagem) {
		try {
			exclusaoMutua.acquire();

			buffer.position(putBuffer);
			buffer.putInt(mensagem.getNumero());
			buffer.putInt(mensagem.getOrdem());

			putBuffer = (putBuffer += 8) % BUFFER_MAX;
			
			exclusaoMutua.release();

		} catch (InterruptedException e) {
			System.err.println("CanalComunicacao escreverMensagem() error - Semaphore exclusaoMutua not working: "
					+ e.getMessage());
		}
	}

	
	private MyMessage lerMensagem(Descritor leitor) {
		try {
			exclusaoMutua.acquire();
			
			int getBuffer = leitor.getGetBufferPosition();
			
			number = buffer.getInt(getBuffer);
			order = buffer.getInt(getBuffer + 4);

			getBuffer = (getBuffer += 8) % BUFFER_MAX;
			
			leitor.setGetBufferPosition(getBuffer);

			exclusaoMutua.release();

		} catch (InterruptedException e) {
			System.err.println("CanalComunicacao lerMensagem() error - Semaphore exclusaoMutua not working: "
					+ e.getMessage());
		}
		return new MyMessage(number, order);
	}

	
	private boolean idLeitorAutorizado(int id) {
		boolean autorizado = false;
		for (Descritor d : leitores) {
			if (d.getID() == id) {
				autorizado = true;
			}
		}
		return autorizado;
	}

	
	private boolean idEscritorAutorizado(int id) {
		boolean autorizado = false;
		for (int i : escritores) {
			if (i == id) {
				autorizado = true;
			}
		}
		return autorizado;
	}
	
	
	private Descritor getLeitor(int id) {
		for (Descritor d : leitores) {
			if (d.getID() == id) {
				return d;
			}
		}
		return null;
	}
	
	
	private void actualizarLivres() {		
		if (livres.availablePermits() < NUMERO_MSG) {			
			for (int i = 0; i < leitores.size(); i++) {
				Descritor d = leitores.get(i);			
				if ((NUMERO_MSG - livres.availablePermits()) == d.getSemaforo().availablePermits()) {
					break;
				}
				if (i == leitores.size() - 1) {
					livres.release();
				}
			}
		}
	}
	
	
	public int OpenLeitor() {
		Descritor leitor = new Descritor(++ID);
		leitores.add(leitor);
		System.out.println("ID leitor: " + ID);
		return ID;
	}

	
	public int OpenEscritor() {
		escritores.add(++ID);
		System.out.println("ID escritor: " + ID);
		return ID;
	}	
	
	public void Close(int ID) {
		for (Descritor d : leitores)
			if (d.getID() == ID)
				leitores.remove(d);
		for (int id : escritores)
			if (id == ID)
				escritores.remove(new Integer(id));
		}

	
	private void clearBuffer() {
		for (int i = 0; i < BUFFER_MAX; i++) {
			buffer.position(i);
			buffer.put((byte) 0);
		}
	}
}
