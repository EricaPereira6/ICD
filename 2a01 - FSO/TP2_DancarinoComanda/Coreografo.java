
public class Coreografo implements Runnable {

	private int estado;
	private final int ESPERAR_INACTIVO = 0;
	private final int ENVIAR_1_COMANDO = 1;
	private final int ENVIAR_16_COMANDOS = 2;
	private final int ENVIAR_32_COMANDOS = 3;
	private final int ENVIAR_COMANDOS_ILIMITADOS = 4;
	private final int PARAR_COMANDOS = 5;
	private final int ESPERAR_COMANDO = 6;
	private final int TERMINAR = 7;

	private int contador, numComandos, numero;

	private CanalComunicacao com;
	private String title;
	private BDCoreografo bd;
	private GUICoreografo gui;
	private String comando;
	
	private int ID;

	public Coreografo() {
		estado = ESPERAR_INACTIVO;
		contador = 0;
		numComandos = 0;
		numero = 0;

		title = "Coreógrafo";
		com = new CanalComunicacao();
		bd = new BDCoreografo();
		gui = new GUICoreografo(title, bd);
	}

	public Coreografo(String nomeProcesso, CanalComunicacao com) {
		estado = ESPERAR_INACTIVO;
		contador = 0;
		numComandos = 0;
		numero = 0;
		
		title = nomeProcesso;
		this.com = com;
		bd = new BDCoreografo();
		gui = new GUICoreografo(title, bd);
		
		ID = com.OpenEscritor();
	}

	public void run() {
		while (!Thread.currentThread().isInterrupted()) {
		    automatoCoreografo();
		}	
	}

//	public static void main(String[] args) {
//		Coreografo coreo = new Coreografo();
//		while (true) {
//			coreo.run();
//		}
//	}

	public void automatoCoreografo() {
		switch (estado) {
		case ESPERAR_INACTIVO:			
			if (bd.isActivar()) {
				if (estado != TERMINAR)
					estado = ESPERAR_COMANDO;
			}
			actividadeEsperar(100);
//			com.bloquearEscritor();
//			if (estado != TERMINAR)
//				estado = ESPERAR_COMANDO;
			break;

		case ESPERAR_COMANDO:
			if (bd.isComando1()) {
				numComandos = 1;
				if (estado != TERMINAR)
					estado = ENVIAR_1_COMANDO;

			} else if (bd.isComandos16()) {
				numComandos = 1;
				if (estado != TERMINAR)
					estado = ENVIAR_16_COMANDOS;

			} else if (bd.isComandos32()) {
				numComandos = 1;
				if (estado != TERMINAR)
					estado = ENVIAR_32_COMANDOS;

			} else if (bd.isComandosIlimitados()) {
				numComandos = 1;
				if (estado != TERMINAR)
					estado = ENVIAR_COMANDOS_ILIMITADOS;

			} else if (bd.isPararComandos())
				if (estado != TERMINAR)
					estado = PARAR_COMANDOS;

			if (!bd.isActivar()) {
				inactivarComandos();
				if (estado != TERMINAR)
					estado = ESPERAR_INACTIVO;
			}
			actividadeEsperar(100);
			break;

		case ENVIAR_1_COMANDO:
			enviar1Comando();
			bd.setComando1(false);
			enviarComandoParar();
			if (estado != TERMINAR)
				estado = ESPERAR_INACTIVO;
			break;

		case ENVIAR_16_COMANDOS:
			enviar1Comando();
			if (++contador == 16) {
				bd.setComandos16(false);
				contador = 0;
				enviarComandoParar();
				if (estado != TERMINAR)
					estado = ESPERAR_INACTIVO;
			}
			if (bd.isPararComandos())
				if (estado != TERMINAR)
					estado = PARAR_COMANDOS;
			break;

		case ENVIAR_32_COMANDOS:
			enviar1Comando();
			if (++contador == 32) {
				bd.setComandos32(false);
				contador = 0;
				enviarComandoParar();
				if (estado != TERMINAR)
					estado = ESPERAR_INACTIVO;
			}
			if (bd.isPararComandos())
				if (estado != TERMINAR)
					estado = PARAR_COMANDOS;
			break;

		case ENVIAR_COMANDOS_ILIMITADOS:
			enviar1Comando();
			if (bd.isPararComandos())
				if (estado != TERMINAR)
					estado = PARAR_COMANDOS;
			break;

		case PARAR_COMANDOS:
			inactivarComandos();
			enviarComandoParar();
			if (estado != TERMINAR)
				estado = ESPERAR_INACTIVO;
			break;

		case TERMINAR:
			break;

		default:
			System.out.println("Erro no Coreografo: automatoCoreografo");
			if (estado != TERMINAR)
				estado = ESPERAR_INACTIVO;
			break;
		}

	}

	private void actividadeEsperar(long tempo) {
		try {
			Thread.sleep(tempo);
		} catch (InterruptedException e) {
			System.err.println("Coreógrafo actividadeEsperar(long tempo) error - Thread.sleep(tempo) not working: "
					+ e.getMessage());
		}
	}

	private void enviar1Comando() {
		int ordem = (int) (Math.random() * ((4 - 0) + 1)) + 0;
		MyMessage msg = new MyMessage(++numero, ordem);
		com.escrever(msg, ID);
		comando = (ordem == 0) ? "Parar: false"
				: (ordem == 1) ? "Reta: 10"
						: (ordem == 2) ? "Direita: 0 45"
								: (ordem == 3) ? "Esquerda: 0 45" : (ordem == 4) ? "Reta: -10" : "Parar: true";
		gui.myPrint("Mensagem " + numComandos + ": [ " + msg.getNumero() + ", " + msg.getOrdem() + "] -> " + comando);
		numComandos++;
		//TODO 500		
		actividadeEsperar(100);
	}

	private void enviarComandoParar() {
		MyMessage msg = new MyMessage(++numero, 0);
		com.escrever(msg, ID);
		gui.myPrint("Mensagem " + numComandos + " : [ " + msg.getNumero() + ", " + msg.getOrdem() + "] -> Parar: false (PARAR)");
		//TODO 500	
		actividadeEsperar(100);
	}

	private void inactivarComandos() {
		bd.setComando1(false);
		bd.setComandos16(false);
		bd.setComandos32(false);
		bd.setComandosIlimitados(false);
		bd.setPararComandos(false);
	}
	
	protected void ativarCoreo(boolean ativar) {
		
		bd.setActivar(ativar);
	}

	protected void activateButton(boolean coreoAtivos) {
		gui.activateButton(coreoAtivos);
	}

	public int getId() {
		return ID;
	}

}
