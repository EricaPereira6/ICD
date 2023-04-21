import java.io.IOException;

public class Coreografo implements Runnable {

	private int estado;
	private final int ESPERAR_ACTIVAR = 0;
	private final int ESPERAR_COMANDO = 1;
	private final int ENVIAR_1_COMANDO = 2;
	private final int ENVIAR_16_COMANDOS = 3;
	private final int ENVIAR_32_COMANDOS = 4;
	private final int ENVIAR_COMANDOS_ILIMITADOS = 5;
	private final int PARAR_COMANDOS = 6;
	private final int TERMINAR = 7;

	private int contador, numComandos, numero;

	private CanalComunicacao com;
	private String title;
	private BDCoreografo bd;
	private GUICoreografo gui;
	private String comando;

	private int ID;

	public Coreografo(String nomeProcesso, CanalComunicacao com) {
		estado = ESPERAR_ACTIVAR;
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
		while (estado != TERMINAR) {
			automatoCoreografo();
		}
	}

	private void automatoCoreografo() {
		switch (estado) {
		case ESPERAR_ACTIVAR:
			
			bd.bloquearThread();
			
			if (bd.isFim()) {
				estado = TERMINAR;
			}
			if (estado == ESPERAR_ACTIVAR)
				estado = ESPERAR_COMANDO;
			
			break;
			
		case ESPERAR_COMANDO:
			
			actividadeEsperar(100);
			
			numComandos = 1;
			
			if (bd.isComando1()) {
				if (estado == ESPERAR_COMANDO)
					estado = ENVIAR_1_COMANDO;

			} else if (bd.isComandos16()) {
				if (estado == ESPERAR_COMANDO)
					estado = ENVIAR_16_COMANDOS;

			} else if (bd.isComandos32()) {
				if (estado == ESPERAR_COMANDO)
					estado = ENVIAR_32_COMANDOS;

			} else if (bd.isComandosIlimitados()) {
				if (estado == ESPERAR_COMANDO)
					estado = ENVIAR_COMANDOS_ILIMITADOS;

			} else if (bd.isPararComandos())
				if (estado == ESPERAR_COMANDO)
					estado = PARAR_COMANDOS;
			
			if (!bd.isActivar())
				estado = ESPERAR_ACTIVAR; 
			if (bd.isFim()) 
				estado = TERMINAR;
			break;

		case ENVIAR_1_COMANDO:
			enviar1Comando();
			bd.setComando1(false);
			enviarComandoParar();			
			if (estado == ENVIAR_1_COMANDO)
				estado = ESPERAR_COMANDO;
			
			if (!bd.isActivar())
				estado = ESPERAR_ACTIVAR; 
			if (bd.isFim()) 
				estado = TERMINAR;
			break;

		case ENVIAR_16_COMANDOS:
			enviar1Comando();
			
			if (++contador == 16) {
				bd.setComandos16(false);
				contador = 0;
				enviarComandoParar();	
				
				if (estado == ENVIAR_16_COMANDOS)
					estado = ESPERAR_COMANDO;
			}
			
			if (bd.isPararComandos())
				if (estado == ENVIAR_16_COMANDOS)
					estado = PARAR_COMANDOS;
			
			if (!bd.isActivar()) {
				enviarComandoParar();
				estado = ESPERAR_ACTIVAR; 	
			}
			if (bd.isFim()) 
				estado = TERMINAR;
			break;

		case ENVIAR_32_COMANDOS:
			enviar1Comando();
			
			if (++contador == 32) {
				bd.setComandos32(false);
				contador = 0;
				enviarComandoParar();
				
				if (estado == ENVIAR_32_COMANDOS)
					estado = ESPERAR_COMANDO;
			}
			if (bd.isPararComandos())
				if (estado == ENVIAR_32_COMANDOS)
					estado = PARAR_COMANDOS;
			
			if (!bd.isActivar()) {
				enviarComandoParar();
				estado = ESPERAR_ACTIVAR; 	
			}
			if (bd.isFim()) 
				estado = TERMINAR;
			break;

		case ENVIAR_COMANDOS_ILIMITADOS:
			enviar1Comando();

			if (bd.isPararComandos())
				if (estado == ENVIAR_COMANDOS_ILIMITADOS)
					estado = PARAR_COMANDOS;
			
			if (!bd.isActivar()) {
				enviarComandoParar();
				estado = ESPERAR_ACTIVAR; 	
			}
			if (bd.isFim()) 
				estado = TERMINAR;
			break;

		case PARAR_COMANDOS:
			inactivarComandos();
			enviarComandoParar();
			
			if (estado == PARAR_COMANDOS)
				estado = ESPERAR_COMANDO;
			
			if (!bd.isActivar())
				estado = ESPERAR_ACTIVAR; 
			
			if (bd.isFim()) 
				estado = TERMINAR;
			break;

		case TERMINAR:
			break;

		default:
			System.err.println("Erro no Coreografo: automatoCoreografo - estado: " + estado);
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

		int ordem = (int) (Math.random() * ((4 - 1) + 1)) + 1;

		MyMessage msg = new MyMessage(++numero, ordem);
		try {
			com.escrever(msg, ID);
		} catch (IOException e) {
			System.err.println("Coreografo enviar1Comando() error - validate ID not working: " + e.getMessage());
		}
		
		comando = Ordem.findOrdem(ordem).toString();
		gui.myPrint("Mensagem " + numComandos + ": [ " + msg.getNumero() + ", " + msg.getOrdem() + "] -> " + comando);
		numComandos++;
		actividadeEsperar(500);
	}

	private void enviarComandoParar() {
		MyMessage msg = new MyMessage(++numero, Ordem.PARAR_FALSE.getValor());
		try {
			com.escrever(msg, ID);
		} catch (IOException e) {
			System.err.println("Coreografo enviarComandoParar() error - validate ID not working: " + e.getMessage());
		}
		gui.myPrint("Mensagem " + numComandos + " : [ " + msg.getNumero() + ", " + msg.getOrdem()
				+ "] -> Parar: false (PARAR)");
		
		actividadeEsperar(500);
	}

	private void inactivarComandos() {
		bd.setComando1(false);
		bd.setComandos16(false);
		bd.setComandos32(false);
		bd.setComandosIlimitados(false);
		bd.setPararComandos(false);
	}

	public int getId() {
		return ID;
	}

	public String getNomeProcesso() {
		return title;
	}

	public void ativarCoreo(boolean ativar) {
		bd.setActivar(ativar);
	}

	public void activateButtons(boolean coreoAtivos) {
		gui.activateButtons(coreoAtivos);
	}

	public void desbloquearThread() {
		bd.desbloquearThread();
	}

}
