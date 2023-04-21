import java.util.ArrayList;

public class Dancarino implements Runnable {

	private ArrayList<MyMessage> memoria;

	private int estado;
	private final int ESPERAR_COMANDO = 6;
	private final int LER_COMANDO = 7;
	private final int GUARDAR_COMANDO = 8;
	private final int TERMINAR = 9;
	
	private BDDancarino bd;
	private BDDancarinoComanda bd2;
	private CanalComunicacao com;

	private MyMessage msg;
	private MyMessage canalMsg;

	private int ID;
	
	public Dancarino() {
		estado = ESPERAR_COMANDO;

		memoria = new ArrayList<MyMessage>();
		limparMemoria();
//		numeroAnterior = 0;
		msg = new MyMessage(0, 0);
//		memMsg = new MyMessage(0, 0);
		canalMsg = new MyMessage(0, 0);
		
//		title = "Dançarino";
		com = new CanalComunicacao();
		bd = new BDDancarino();
//		gui = new GUIDancarino(title, bd);
//		robot = bd.getRobotLego();
	}

	
	public Dancarino(CanalComunicacao com, BDDancarino bd, BDDancarinoComanda bd2) {
		estado = ESPERAR_COMANDO;

		memoria = new ArrayList<MyMessage>();
		msg = new MyMessage(0, 0);
		canalMsg = new MyMessage(0, 0);
		this.com = com;
		this.bd = bd;
		this.bd2 = bd2;
		
		memoria = bd2.getMemoria();
		
		ID = com.OpenLeitor();
	}

	
	public void run() {
		while (estado != TERMINAR) {
			automatoDancarino();
		}	
	}
	
//	public static void main(String[] args) {
//		Dancarino dancer = new Dancarino();
//		while (true) {
//			dancer.run();
//		}
//	}
	
	public void automatoDancarino() {
		switch (estado) {
		case ESPERAR_COMANDO:
			if (bd.isActivar())
				if (estado != TERMINAR)
					estado = LER_COMANDO;
			actividadeEsperar();
//			com.bloquearLeitor();
//			if (estado != TERMINAR)
//				estado = LER_COMANDO;
			break;

		case LER_COMANDO:
//			try {
			canalMsg = com.ler(ID);
			//if (canalMsg.getNumero() > numeroAnterior)
			if(canalMsg.getNumero() != 0) {
				if (estado != TERMINAR)
					estado = GUARDAR_COMANDO;
			}
			if (!bd.isActivar())
				if (estado != TERMINAR)
					estado = ESPERAR_COMANDO;
			actividadeEsperar();

//			} catch (IOException e) {
//				System.err.println("Leitor necessita de se registar no canal de comunicação antes de ler." + e.getMessage());
//			}
	
			break;

		case GUARDAR_COMANDO:
			msg = canalMsg;
//			numeroAnterior = msg.getNumero();
			memoria = bd2.getMemoria();
			memoria.add(msg);
			bd2.setMemoria(memoria);
			if (estado != TERMINAR)
				estado = LER_COMANDO;
			if (!bd.isActivar())
				if (estado != TERMINAR)
					estado = ESPERAR_COMANDO;
			break;

		case TERMINAR:
			break;

		default:
			System.out.println("Erro no Dançarino: Automato Dançarino - Estado: " + estado);
			break;
		}
	}

	private void actividadeEsperar() {
		try {
			Thread.sleep(100);
		} catch (InterruptedException e) {
			System.err.println("Dançarino actividadeEsperar() error - Thread.sleep(100) not working: " + e.getMessage());
		}
	}

	private void limparMemoria() {
		int size = memoria.size();
		for (int i = 0; i < size; i++)
			memoria.remove(0);
	}

	protected void ativarDancer(boolean ativar) {	
		bd.setActivar(ativar);
	}
	
	protected void activateButtons(boolean coreoAtivos) {
		bd2.activateButtons(coreoAtivos);
	}
	
	public int getId() {
		return ID;
	}

}
