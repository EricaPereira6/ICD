import java.io.IOException;

public class Dancarino implements Runnable {

	private int estado;
	private final int PARAR_FALSE = 0;
	private final int FRENTE = 1;
	private final int CURVAR_DIREITA = 2;
	private final int CURVAR_ESQUERDA = 3;
	private final int RETAGUARDA = 4; 
	private final int PARAR_TRUE = 5;	
	private final int ESPERAR_TERMINAR_RETA = 6;
	private final int ESPERAR_TERMINAR_CURVA = 7;
	private final int ESPERAR_ACTIVAR = 8;
	private final int LER_COMANDO = 9;
	private final int TERMINAR = 10;

	private final int distancia = 10;
	private final int raio = 0;
	private final int angulo = 45;
	private final int tReta10 = 700; // Tempo de reta e retaguarda
	private final int tCurvar045 = 200; // Tempo de curvar Esquerda e curva Direita
	private long timestamp;

	private String title;
	private BDDancarino bd;
	private CanalComunicacao com;
	private GUIDancarino gui;
	private int ID;
	private SpyRobot robot;
	private Evitar evitar;
	private BDEvitar bdEvitar;
	private Thread e;
	
	private MyMessage msg;

	public Dancarino(String nomeProcesso, CanalComunicacao com) {
		estado = ESPERAR_ACTIVAR;
		
		msg = new MyMessage(0, 0);

		title = nomeProcesso;
		this.com = com;
		bd = new BDDancarino(); 
		gui = new GUIDancarino(title, bd);
		robot = bd.getRobotLego();

		ID = com.OpenLeitor();
		
		bdEvitar = new BDEvitar();
		evitar = new Evitar(bdEvitar, robot);
		e = new Thread(evitar);
	}

	public void run() {
		while (estado != TERMINAR) {
			automatoDancarino();
		}
	}

	private void automatoDancarino() {
		
		switch (estado) {
		case ESPERAR_ACTIVAR:
			
			bd.bloquearThread();
			e.start();
			
			if (bdEvitar.isFim() || bd.isFim()) {
				bdEvitar.terminarThread();
				bd.terminarSpyThread();
				estado = TERMINAR;
			}
			
			if (estado == ESPERAR_ACTIVAR) {
				estado = LER_COMANDO;
			}
			break;

		case LER_COMANDO:
			try {
				msg = com.ler(ID);

				if (estado == LER_COMANDO)
					estado = msg.getOrdem();

			} catch (IOException e) {
				System.err.println("Dancarino automatoDancarino() error - validate ID not working: " + e.getMessage());
			}
			
			if (!bd.isActivar())
				estado = ESPERAR_ACTIVAR;
			
			if (bdEvitar.isFim() || bd.isFim()) {
				bdEvitar.terminarThread();
				bd.terminarSpyThread();
				estado = TERMINAR;
			}
	
			break;

		case PARAR_FALSE:
			gui.myPrint("[" + msg.getNumero() + ", " + msg.getOrdem() + "] -> Parar: false");

			robot.Parar(false);

			if (estado == PARAR_FALSE)
				estado = LER_COMANDO;
			break;

		case FRENTE:
			bd.setDistancia(distancia);

			gui.myPrint("[" + msg.getNumero() + ", " + msg.getOrdem() + "] -> Reta: " + bd.getDistancia());

			timestamp = System.currentTimeMillis();
			
			robot.Reta(bd.getDistancia());

			if (estado == FRENTE)
				estado = ESPERAR_TERMINAR_RETA;
			break;

		case CURVAR_DIREITA:
			bd.setRaio(raio);
			bd.setAngulo(angulo);

			gui.myPrint("[" + msg.getNumero() + ", " + msg.getOrdem() + "] -> Direita: raio "
					+ bd.getRaio() + " angulo " + bd.getAngulo());

			timestamp = System.currentTimeMillis();
			robot.CurvarDireita(bd.getRaio(), bd.getAngulo());

			if (estado == CURVAR_DIREITA)
				estado = ESPERAR_TERMINAR_CURVA;
			break;

		case CURVAR_ESQUERDA:
			bd.setRaio(raio);
			bd.setAngulo(angulo);

			gui.myPrint("[" + msg.getNumero() + ", " + msg.getOrdem() + "] -> Esquerda: raio " 
					+ bd.getRaio() + " angulo " + bd.getAngulo());

			timestamp = System.currentTimeMillis();
			robot.CurvarEsquerda(bd.getRaio(), bd.getAngulo());

			if (estado == CURVAR_ESQUERDA)
				estado = ESPERAR_TERMINAR_CURVA;
			break;

		case RETAGUARDA:
			bd.setDistancia(distancia);

			gui.myPrint("[" + msg.getNumero() + ", " + msg.getOrdem() + "]  -> Reta: " + -bd.getDistancia());

			timestamp = System.currentTimeMillis();
			robot.Reta(-1*bd.getDistancia());

			if (estado == RETAGUARDA)
				estado = ESPERAR_TERMINAR_RETA;
			break;

		case PARAR_TRUE:
			gui.myPrint("[" + msg.getNumero() + ", " + msg.getOrdem() + "] -> Parar: true");

			robot.Parar(true);

			if (estado == PARAR_TRUE)
				estado = LER_COMANDO;
			break;

		case ESPERAR_TERMINAR_RETA:
			actividadeEsperar(100);
			if (System.currentTimeMillis() - timestamp > tReta10) {
				if (estado == ESPERAR_TERMINAR_RETA) {
					estado = LER_COMANDO;
				}
			}
			break;

		case ESPERAR_TERMINAR_CURVA:
			actividadeEsperar(100);
			if (System.currentTimeMillis() - timestamp > tCurvar045) {
				if (estado == ESPERAR_TERMINAR_CURVA) {
					estado = LER_COMANDO;
				}
			}
			break;

		case TERMINAR:
			break;

		default:
			System.err.println("Erro no Dançarino: Automato Dançarino - Estado: " + estado);
			break;
		}
	}

	private void actividadeEsperar(long tempo) {
		try {
			Thread.sleep(tempo);
		} catch (InterruptedException e) {
			System.err.println(
					"Dançarino actividadeEsperar(tempo) error - Thread.sleep(tempo) not working: " + e.getMessage());
		}
	}
	public int getId() {
		return ID;
	}
	public String getNomeProcesso() {
		return title;
	}
	public SpyRobot getRobotLego() {
		return robot;
	}
	public void ativarDancer(boolean ativar) {
		bd.setActivar(ativar);
	}
	public void activateButtons(boolean coreoAtivos) {
		gui.activateButtons(coreoAtivos);
	}
	public String getNomeRobot() {
		return bd.getNomeRobot();
	}
	public void setOnOff(boolean b) {
		bd.setOnOff(b);
	}
	public void desbloquearThread() {
		bd.desbloquearThread();
	}
}
