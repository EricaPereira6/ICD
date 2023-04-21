import java.util.ArrayList;

public class DancarinoComanda implements Runnable {

	private int estadoComandos;
	private final int PARAR_FALSE = 0;
	private final int FRENTE = 1;
	private final int CURVAR_DIREITA = 2;
	private final int CURVAR_ESQUERDA = 3;
	private final int RETAGUARDA = 4;
	private final int PARAR_TRUE = 5;
	private final int ESPERAR_COMANDO = 6;
	private final int TERMINAR = 9;
	private final int ESPERAR_TERMINAR_RETA = 10;
	private final int ESPERAR_TERMINAR_CURVA = 11;
	
	//TODO
	// Tempo de reta e retaguarda sao iguais 720
	private final int tReta10 = 0;
	// Tempo de curvar Esquerda e curva Direita sao iguais 180
	private final int tCurvar045 = 0;
	private long timestamp;

	private MyRobotLego robot;
//	private RobotLegoEV3 robot;
	private String title;
	private BDDancarino bd;
	private BDDancarinoComanda bd2;
	private GUIDancarino gui;

	private MyMessage memMsg;
	
	private ArrayList<MyMessage> memoria;
	
	
	public DancarinoComanda(String nomeProcesso, BDDancarino bd, BDDancarinoComanda bd2) {
		estadoComandos = ESPERAR_COMANDO;

		memMsg = new MyMessage(0, 0);
		
		this.bd = bd;
		this.bd2 = bd2;
		robot = bd.getRobotLego();
		title = nomeProcesso;
		gui = new GUIDancarino(title, bd);
		bd2.setGUI(gui);
		memoria = bd2.getMemoria();
	}

	
	public void run() {
		while (estadoComandos != TERMINAR) {
			automatoComandos();
		}	
	}

	public void automatoComandos() {
		switch (estadoComandos) {
		case ESPERAR_COMANDO:
			memoria = bd2.getMemoria();
//			gui.myPrint("m: " + memoria);
			if (!memoria.isEmpty()) {
				memMsg = memoria.get(0);
				if (estadoComandos != TERMINAR)
					estadoComandos = memMsg.getOrdem();
			}
			actividadeEsperar();
			break;

		case PARAR_FALSE:
			robot.Parar(false);
			gui.myPrint("[" + memMsg.getNumero() + ", " + memMsg.getOrdem() + "] -> Parar: false");
			memoria.remove(0);
			bd2.setMemoria(memoria);
			if (estadoComandos != TERMINAR)
				estadoComandos = ESPERAR_COMANDO;
			break;

		case FRENTE:
			bd.setDistancia(10);
			robot.Reta(bd.getDistancia());
			gui.myPrint("[" + memMsg.getNumero() + ", " + memMsg.getOrdem() + "] -> Reta: " + bd.getDistancia());
			memoria.remove(0);
			bd2.setMemoria(memoria);
			if (estadoComandos != TERMINAR)
				estadoComandos = ESPERAR_TERMINAR_RETA;
			timestamp = System.currentTimeMillis();
			break;

		case CURVAR_DIREITA:
			bd.setRaio(0);
			bd.setAngulo(45);
			robot.CurvarDireita(bd.getRaio(), bd.getAngulo());
			gui.myPrint("[" + memMsg.getNumero() + ", " + memMsg.getOrdem() + "] -> Direita: raio " + bd.getRaio()
					+ " angulo " + bd.getAngulo());
			memoria.remove(0);
			bd2.setMemoria(memoria);
			if (estadoComandos != TERMINAR)
				estadoComandos = ESPERAR_TERMINAR_CURVA;
			timestamp = System.currentTimeMillis();
			break;

		case CURVAR_ESQUERDA:
			bd.setRaio(0);
			bd.setAngulo(45);
			robot.CurvarEsquerda(bd.getRaio(), bd.getAngulo());
			gui.myPrint("[" + memMsg.getNumero() + ", " + memMsg.getOrdem() + "] -> Esquerda: raio " + bd.getRaio()
					+ " angulo " + bd.getAngulo());
			memoria.remove(0);
			bd2.setMemoria(memoria);
			if (estadoComandos != TERMINAR)
				estadoComandos = ESPERAR_TERMINAR_CURVA;
			timestamp = System.currentTimeMillis();
			break;

		case RETAGUARDA:
			bd.setDistancia(10);
			robot.Reta(-bd.getDistancia());
			gui.myPrint("[" + memMsg.getNumero() + ", " + memMsg.getOrdem() + "]  -> Reta: " + -bd.getDistancia());
			memoria.remove(0);
			bd2.setMemoria(memoria);
			if (estadoComandos != TERMINAR)
				estadoComandos = ESPERAR_TERMINAR_RETA;
			timestamp = System.currentTimeMillis();
			break;

		case PARAR_TRUE:
			robot.Parar(true);
			gui.myPrint("[" + memMsg.getNumero() + ", " + memMsg.getOrdem() + "] -> Parar: true");
			memoria.remove(0);
			bd2.setMemoria(memoria);
			if (estadoComandos != TERMINAR)
				estadoComandos = ESPERAR_COMANDO;
			break;

		case ESPERAR_TERMINAR_RETA:
			actividadeEsperar();
			if (System.currentTimeMillis() - timestamp > tReta10)
				if (estadoComandos != TERMINAR)
					estadoComandos = ESPERAR_COMANDO;
			break;

		case ESPERAR_TERMINAR_CURVA:
			actividadeEsperar();
			if (System.currentTimeMillis() - timestamp > tCurvar045)
				if (estadoComandos != TERMINAR)
					estadoComandos = ESPERAR_COMANDO;
			break;

		case TERMINAR:
			break;

		default:
			System.out.println("Erro no DançarinoComanda: AutomatoComandos  - Estado: " + estadoComandos);
			if (estadoComandos != TERMINAR)
				estadoComandos = ESPERAR_COMANDO;
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


}
