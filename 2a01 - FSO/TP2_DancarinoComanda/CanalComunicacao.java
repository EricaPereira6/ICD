import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.concurrent.Semaphore;

public class CanalComunicacao implements Runnable {

	// dimensão máxima em bytes do buffer
//	final int BUFFER_MAX = 256; // 32 mensagens de 2 ints com 4 bytes por cada int - 32*8 = 256
	private final int BUFFER_MAX = 32; // 4 mensagens para teste
	private final int DIVISOR = 8;
	private final int NUMERO_MSG = (BUFFER_MAX / DIVISOR);

	private ByteBuffer buffer;

	private int putBuffer, number, order;

	private Semaphore exclusaoMutua, bloquearCanal, bloquearEscritor, bloquearLeitor, livres, bloquearValidacao, esperar;

	private int estado;
	private final int BLOQUEAR_CANAL = 0;
	private final int AUTENTICAR = 1;
	private final int ESPERAR = 2;
	private final int LER = 3;
	private final int ESCREVER = 4;
	private final int TERMINAR = 5;
	private final int LER_AUX = 10;
	private final int ESCREVER_AUX = 11;
	

	private int ID;
	private ArrayList<Descritor> leitores;
	private ArrayList<Integer> escritores;
	private ArrayList<Integer> clientesleitores;
	private ArrayList<Integer> clientesescritores;
	
	private GUICanal gui;
	private BDCanal v;

//	private boolean autorizado;

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
		bloquearEscritor = new Semaphore(0);
		bloquearLeitor = new Semaphore(0);
		livres = new Semaphore(NUMERO_MSG);
		bloquearValidacao = new Semaphore(0);
		esperar = new Semaphore(0);

		leitores = new ArrayList<Descritor>();
		escritores = new ArrayList<Integer>();
		clientesleitores = new ArrayList<Integer>();
		clientesescritores = new ArrayList<Integer>();

		ID = 0;
//		autorizado = false;
		
		v = new BDCanal();
		gui = new GUICanal(v);
	}
	
	public void run() {
		while (estado != TERMINAR) {
			automatoCanal();
		}
	}

	public void automatoCanal() {
		switch (estado) {
			case BLOQUEAR_CANAL:
				try {
					gui.printChannel("");
					gui.printChannel("-> BLOQUEAR_CANAL");
					bloquearCanal.acquire();
					
					if(!clientesescritores.isEmpty()) {
						if(estado == BLOQUEAR_CANAL)
							estado = ESCREVER_AUX;
					}
					if(!clientesleitores.isEmpty()) {
						if(estado == BLOQUEAR_CANAL)
							estado = LER_AUX;
					}
					
//					if(estado == BLOQUEAR_CANAL)
//						estado = AUTENTICAR;
				} catch (InterruptedException e) {
					System.err.println("CanalComunicacao error - Semaphore bloquearCanal not working: " + e.getMessage());
				}
				break;
				
			case LER_AUX:
				gui.printChannel("-> LER_AUX -> ");
				gui.printChannel("actualizar livres ");
				gui.printChannel("livres antes: " + livres.availablePermits());
				actualizarLivres();
				gui.printChannel("livres depois: " + livres.availablePermits());

				clientesleitores.remove(0);
				if (estado == LER_AUX)
					estado = BLOQUEAR_CANAL;
				
				break;
			
			case ESCREVER_AUX:
				gui.printChannel("-> ESCREVER_AUX -> ");
				gui.printChannel("Semaforos dos leitores: ");
				for(Descritor d : leitores) {
					d.getSemaforo().release();
					gui.printChannel(" - leitor " + d.getID() + ", n atual: " + d.getSemaforo().availablePermits());
				}
				clientesescritores.remove(0);
				if (estado == ESCREVER_AUX)
					estado = BLOQUEAR_CANAL;
				break;
				
			case AUTENTICAR:
				gui.printChannel("-> AUTENTICAR -> ");
				if (idLeitorAutorizado(clientesleitores.get(0))) {
					bloquearValidacao.release();
				}
				else if (idEscritorAutorizado(clientesescritores.get(0))) {
					bloquearValidacao.release();
				} else {
					// exception
//					gui.printChannel("invalid ID: " + clientes.get(0));
				}
				if (estado == AUTENTICAR)
					estado = BLOQUEAR_CANAL;
				break;
				
			case ESPERAR:
				try {
					Thread.sleep(100);
				} catch (InterruptedException e) {
					System.err.println("CanalComunicacao error - Thread.sleep(100) not working: " + e.getMessage());
				}
				break;
			
				
			case LER:
				gui.printChannel("-> LER -> ");
//				try {
//					esperar.acquire();
//				} catch (InterruptedException e1) {
//					System.err.println("CanalComunicacao error - Semaphore esperar not working:" + e1.getMessage());
//				}
				gui.printChannel("actualizar livres ");
				gui.printChannel("antes: " + livres.availablePermits());
				actualizarLivres();
				gui.printChannel("depois: " + livres.availablePermits());

				if (estado == LER)
					estado = BLOQUEAR_CANAL;
				break;
				
			case ESCREVER:
				gui.printChannel("-> ESCREVER -> ");
//				try {
//					esperar.acquire();
//				} catch (InterruptedException e1) {
//					System.err.println("CanalComunicacao error - Semaphore esperar not working:" + e1.getMessage());
//				}
				gui.printChannel("Semaforos dos leitores: ");
				for(Descritor d : leitores) {
					d.getSemaforo().release();
					gui.printChannel(" - leitor " + d.getID() + ", nPermits: " + d.getSemaforo().availablePermits());
				}
				
				if (estado == ESCREVER)
					estado = BLOQUEAR_CANAL;
				break; 
				
			default:
				System.out.println("Erro CanalComunicacao - automatoCanal() - estado " + estado);
				break;
		}
	}
	public void escrever(MyMessage msgEscritor, int id) {
		gui.printChannel("");
		gui.printChannel("escritor " + id + " quer escrever " + msgEscritor.toString() + " ! --- " + Thread.currentThread());
		
		if(!idEscritorAutorizado(id)) {
			//throw exception
			gui.printChannel("INVALID ID !!!!!");
		} 

		try {
			gui.printChannel("livres will be acquired");
			gui.printChannel("-> livres before: " + livres.availablePermits());
			livres.acquire();
			gui.printChannel("-> livres after: " + livres.availablePermits());
			
			clientesescritores.add(id);
			bloquearCanal.release();

			escreverMensagem(msgEscritor);
			
		} catch (InterruptedException e1) {
			System.err.println(
					"CanalComunicacao escrever() error - Semaphore livres not working: "
							+ e1.getMessage());
		}

		gui.printChannel("!!!!! Escreveu !!!!!");
	}
	
	public MyMessage ler(int id) {
		gui.printChannel("");
		gui.printChannel("leitor " + id + " quer ler ! --- " + Thread.currentThread());
		MyMessage msgLeitor = new MyMessage(0,0);
		if(!idLeitorAutorizado(id)) {
			//throw exception
			gui.printChannel("INVALID ID !!!!!");
		}
		Descritor leitor = getLeitor(id);
		Semaphore semaforoLeitor = leitor.getSemaforo();
		
		try {
			gui.printChannel("semaforoLeitor will be acquired");
			gui.printChannel("-> leitor before: " + semaforoLeitor.availablePermits());
			semaforoLeitor.acquire();
			gui.printChannel("-> leitor after: " + semaforoLeitor.availablePermits());
			
			clientesleitores.add(id);
			bloquearCanal.release();
			
			msgLeitor = lerMensagem(leitor);
		

		} catch (InterruptedException e) {
			System.err.println(
					"CanalComunicacao lerMensagem() error - Semaphore semaforoLeitor not working: "
							+ e.getMessage());
		}

		gui.printChannel("!!!!! Mensagem lida pelo leitor " + leitor.getID() + ": " + msgLeitor + "!!!!!");
		return msgLeitor;
	}

//	public boolean escrever(MyMessage msgEscritor, int id) {
//		gui.printChannel("");
//		gui.printChannel("escritor " + id + " quer escrever " + msgEscritor.toString() + " ! --- " + Thread.currentThread());
//		clientesescritores.add(id);
//
//		bloquearCanal.release(); 
//		try {
//			bloquearValidacao.acquire();
////			if(autorizado) {  
//			try {
//				gui.printChannel("livres will be acquired");
//				gui.printChannel("-> before: " + livres.availablePermits());
//				livres.acquire();
//				gui.printChannel("-> after: " + livres.availablePermits());
//		
//				escreverMensagem(msgEscritor);
//				
//				if (bloquearCanal.availablePermits() < 1) {
//					gui.printChannel("^^^^^^^^^^^^ entrou ^^^^^^^^^^^^ escritor");
//					bloquearCanal.release();
//				}
//				estado = ESCREVER;
//				
//			} catch (InterruptedException e1) {
//				System.err.println(
//						"CanalComunicacao escrever() error - Semaphore livres not working: "
//								+ e1.getMessage());
//			}
////			}
//		} catch (InterruptedException e) {
//			System.err.println("CanalComunicacao escrever() error - Semaphore bloquearValidacao not working: "
//							+ e.getMessage());
//		}
//		gui.printChannel("!!!!! Escreveu !!!!!");
//		clientesescritores.remove(0);
//		return true; 
//	}
	
//	public MyMessage ler(int id) throws IOException {
//	public MyMessage ler(int id) {
//		gui.printChannel("");
//		gui.printChannel("leitor " + id + " quer ler ! --- " + Thread.currentThread());
//		MyMessage msgLeitor = new MyMessage(0,0);
//		clientesleitores.add(id);
//		if (estado != BLOQUEAR_CANAL) {
//			return new MyMessage(0,0);
//		}
//		bloquearCanal.release();
//		Descritor leitor = null;
//		try {
//			bloquearValidacao.acquire();
//			//			if(autorizado) {
//			leitor = getLeitor(clientesleitores.get(0));
//			Semaphore semaforoLeitor = leitor.getSemaforo();
//			try {
//				gui.printChannel("semaforoLeitor will be acquired");
//				gui.printChannel("-> before: " + semaforoLeitor.availablePermits());
//				semaforoLeitor.acquire();
//				gui.printChannel("-> after: " + semaforoLeitor.availablePermits());
//
//				msgLeitor = lerMensagem(leitor);
//				
//				if (bloquearCanal.availablePermits() < 1) {
//					gui.printChannel("^^^^^^^^^^^^ entrou ^^^^^^^^^^^^ leitor");
//					bloquearCanal.release();
//				}
//				estado = LER;
//
//			} catch (InterruptedException e) {
//				System.err.println(
//						"CanalComunicacao lerMensagem() error - Semaphore semaforoLeitor not working: "
//								+ e.getMessage());
//			}
//			//			}
//			//			else {
//			//				throw new IOException("Not enough privileges");
//			//			}
//		} catch (InterruptedException e) {
//			System.err.println("CanalComunicacao escrever() error - Semaphore bloquearValidacao not working: "
//					+ e.getMessage());
//		}
//		gui.printChannel("!!!!! Mensagem lida pelo leitor " + leitor.getID() + ": " + msgLeitor + "!!!!!");
//		clientesleitores.remove(0);
//		return msgLeitor;
//	}


	public void escreverMensagem(MyMessage mensagem) {
		gui.printChannel("");
		gui.printChannel(" >>> BUFFER: escreverMensagem " + mensagem.toString() + " por " + Thread.currentThread());
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

	public MyMessage lerMensagem(Descritor leitor) {
		gui.printChannel("");
		gui.printChannel(" >>> BUFFER: lerMensagem por " + Thread.currentThread());
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
		
		gui.printChannel("Mensagem recebida: " + new MyMessage(number, order).toString());
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
//				return autorizado;
			}
		}
//		autorizado = false;
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
		gui.printChannel("");
		gui.printChannel("actualizar livres: " + livres.availablePermits());
		if (livres.availablePermits() < NUMERO_MSG) {
			gui.printChannel("entrou no if (" + livres.availablePermits() + " < " + NUMERO_MSG + ")");
			for (int i = 0; i < leitores.size(); i++) {
				Descritor d = leitores.get(i);
				gui.printChannel("((" + NUMERO_MSG + " - " + livres.availablePermits() + ") == "
				+ d.getSemaforo().availablePermits() + ") => break: "
						+ ((NUMERO_MSG - livres.availablePermits()) == d.getSemaforo().availablePermits()));
				if ((NUMERO_MSG - livres.availablePermits()) == d.getSemaforo().availablePermits()) {
					break;
				}
				if (i == leitores.size() - 1) {
					gui.printChannel("if (" + i + " == " + (leitores.size() - 1) + ") -> " + (i == leitores.size() - 1));
					livres.release();
					gui.printChannel("livres.release()");
				}
			}
		}
		gui.printChannel("");
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

	public void bloquearEscritor() {
		try {
			bloquearEscritor.acquire();
		} catch (InterruptedException e) {
			System.err.println("CanalComunicacao bloquearEscritor() error - Semaphore bloquearEscritor not working: "
					+ e.getMessage());
		}
	}
	public void desbloquearEscritor() {
		bloquearEscritor.release();
	}
	
	public void bloquearLeitor() {
		try {
			bloquearLeitor.acquire();
		} catch (InterruptedException e) {
			System.err.println("CanalComunicacao bloquearLeitor() error - Semaphore bloquearLeitor not working: "
					+ e.getMessage());
		}
	}
	public void desbloquearLeitor() {
		bloquearLeitor.release();
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
