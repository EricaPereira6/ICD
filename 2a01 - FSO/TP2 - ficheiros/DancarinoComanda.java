import java.util.ArrayList;

public class DancarinoComanda implements Runnable {

	private int estado;
	private final int PARAR_FALSE = 0;
	private final int FRENTE = 1;
	private final int CURVAR_DIREITA = 2;
	private final int CURVAR_ESQUERDA = 3;
	private final int RETAGUARDA = 4;
	private final int PARAR_TRUE = 5;
	private final int ESPERAR_COMANDO = 6;
	private final int TERMINAR = 7;
	private final int ESPERAR_TERMINAR_RETA = 8;
	private final int ESPERAR_TERMINAR_CURVA = 9;
	
	// Tempo de reta e retaguarda sao iguais 720
	private final int tReta10 = 700;
	// Tempo de curvar Esquerda e curva Direita sao iguais 180
	private final int tCurvar045 = 200;
	private long timestamp;

	private SpyRobot robot;
//	private String title;
	private BDDancarino bd;
//	private GUIDancarino gui;

	private MyMessage memMsg;
	
	private ArrayList<MyMessage> memoria;
	
	
	public DancarinoComanda(BDDancarino bd) {
		estado = ESPERAR_COMANDO;

		memMsg = new MyMessage(0, 0);
	
		this.bd = bd;
		robot = bd.getRobotLego();
		memoria = bd.getMemoria();
	}

	
	public void run() {
		while (estado != TERMINAR) {
			if (bd.isToque() || bd.isFim()) {
				robot.Parar(false);
				estado = TERMINAR;
			}
			automatoComandos();
		}	
	}

	public void automatoComandos() {			
		switch (estado) {
		case ESPERAR_COMANDO:
			memoria = bd.getMemoria();
			if (!memoria.isEmpty()) {
				memMsg = memoria.get(0);
				if (estado == ESPERAR_COMANDO)
					estado = memMsg.getOrdem();
			}
			actividadeEsperar(100);
			break;

		case PARAR_FALSE:
			System.err.println(Thread.currentThread() + ": [" + memMsg.getNumero() + ", " + memMsg.getOrdem() + "] -> Parar: false");
			memoria.remove(0);
			bd.setMemoria(memoria);

			robot.Parar(false);

			if (estado == PARAR_FALSE)
				estado = ESPERAR_COMANDO;
			break;

		case FRENTE:
			System.err.println(Thread.currentThread() + ": [" + memMsg.getNumero() + ", " + memMsg.getOrdem() + "] -> Reta: " + bd.getDistancia());
			memoria.remove(0);
			bd.setMemoria(memoria);

			bd.setDistancia(10);
			
			timestamp = System.currentTimeMillis();
			robot.Reta(bd.getDistancia());

			if (estado == FRENTE)
				estado = ESPERAR_TERMINAR_RETA;
			break;

		case CURVAR_DIREITA:
			System.err.println(Thread.currentThread() + ": [" + memMsg.getNumero() + ", " + memMsg.getOrdem() + "] -> Direita: raio " + bd.getRaio()
			+ " angulo " + bd.getAngulo());
			memoria.remove(0);
			bd.setMemoria(memoria);

			bd.setRaio(0);
			bd.setAngulo(45);
			
			timestamp = System.currentTimeMillis();
			robot.CurvarDireita(bd.getRaio(), bd.getAngulo());

			if (estado == CURVAR_DIREITA)
				estado =ESPERAR_TERMINAR_CURVA;
			break;

		case CURVAR_ESQUERDA:
			System.err.println(Thread.currentThread() + ": [" + memMsg.getNumero() + ", " + memMsg.getOrdem() + "] -> Esquerda: raio " + bd.getRaio()
			+ " angulo " + bd.getAngulo());
			memoria.remove(0);
			bd.setMemoria(memoria);

			bd.setRaio(0);
			bd.setAngulo(45);
			
			timestamp = System.currentTimeMillis();
			robot.CurvarEsquerda(bd.getRaio(), bd.getAngulo());

			if (estado == CURVAR_ESQUERDA)
				estado = ESPERAR_TERMINAR_CURVA;
			break;

		case RETAGUARDA:
			System.err.println(Thread.currentThread() + ": [" + memMsg.getNumero() + ", " + memMsg.getOrdem() + "]  -> Reta: " + -bd.getDistancia());
			memoria.remove(0);
			bd.setMemoria(memoria);

			bd.setDistancia(10);
			
			timestamp = System.currentTimeMillis();
			robot.Reta(-bd.getDistancia());

			if (estado == RETAGUARDA)
				estado = ESPERAR_TERMINAR_RETA;
			break;

		case PARAR_TRUE:
			System.err.println(Thread.currentThread() + ": [" + memMsg.getNumero() + ", " + memMsg.getOrdem() + "] -> Parar: true");
			memoria.remove(0);
			bd.setMemoria(memoria);

			robot.Parar(true);

			if (estado == PARAR_TRUE)
				estado = ESPERAR_COMANDO;
			break;

		case ESPERAR_TERMINAR_RETA:
			actividadeEsperar(50);
			if (System.currentTimeMillis() - timestamp > tReta10)
				if (estado == ESPERAR_TERMINAR_RETA)
					estado = ESPERAR_COMANDO;
			break;

		case ESPERAR_TERMINAR_CURVA:
			actividadeEsperar(50);
			if (System.currentTimeMillis() - timestamp > tCurvar045)
				if (estado == ESPERAR_TERMINAR_CURVA)
					estado = ESPERAR_COMANDO;
			break;

		case TERMINAR:
			break;

		default:
			System.err.println("Erro no DançarinoComanda: AutomatoComandos  - Estado: " + estado);
			break;
		}
	}	
	
	private void actividadeEsperar(long tempo) {
		try {
			Thread.sleep(tempo);
		} catch (InterruptedException e) {
			System.err.println("Dançarino actividadeEsperar() error - Thread.sleep(tempo) not working: " + e.getMessage());
		}
	}
}
