import java.io.IOException;
import java.util.ArrayList;

public class Dancarino implements Runnable {

	private ArrayList<MyMessage> memoria;

	private int estado;
	private final int LER_COMANDO = 1;
	private final int GUARDAR_COMANDO = 2;
	private final int TERMINAR = 3;

	private SpyRobot robot;
	
	private String title;
	private BDDancarino bd;
	private CanalComunicacao com;
	private GUIDancarino gui;

	private MyMessage msg;

	private int ID;

	private boolean spying;

	private Evitar evitar;
	private BDEvitar bdEvitar;

	private DancarinoComanda comanda;
	
	public Dancarino() {
		estado = LER_COMANDO;

		memoria = new ArrayList<MyMessage>();
//		numeroAnterior = 0;
		msg = new MyMessage(0, 0);
//		memMsg = new MyMessage(0, 0);
//		canalMsg = new MyMessage(0, 0);
		
//		title = "Dançarino";
		com = new CanalComunicacao();
		bd = new BDDancarino();
//		gui = new GUIDancarino(title, bd);
		robot = bd.getRobotLego();
	}

	
	public Dancarino(String nomeProcesso, CanalComunicacao com) {
		estado = LER_COMANDO;

		msg = new MyMessage(0, 0);
//		canalMsg = new MyMessage(0, 0);
	
		this.com = com;
		
		bd = new BDDancarino();		
		gui = new GUIDancarino(title, bd);
		robot = bd.getRobotLego();
		memoria = bd.getMemoria();
		
		ID = com.OpenLeitor();
		spying = false;
		
		bdEvitar = new BDEvitar();
		evitar = new Evitar(bdEvitar, robot);
		
		comanda = new DancarinoComanda(bd);
	}

	
	public void run() {
		while(estado != TERMINAR) {
			
			if (!bd.isActivar()) {
				bd.bloquearThread();
//				new Thread(evitar).start();
				new Thread(comanda).start();
			}
			
			if(bdEvitar.isToque() || bd.isFim()) {
				gui.myPrint("bateeeeeeeuuu!!!!!!");
//				bdEvitar.terminarThread();
				estado = TERMINAR;
			}
			automatoDancarino();
		}
	}
	
	public void automatoDancarino() {			
		switch (estado) {
		case LER_COMANDO:
			try {
				msg = com.ler(ID);
				
				gui.myPrint("eu li: " + msg.toString());

				if(robot.isEspiar()) {
					if(!spying) {	
						gui.myPrint("");
						gui.myPrint(" --------------------- start Spying -----------------------");
						spying = true;
						gui.myPrint("");
					}

					robot.guardarMensagenEspiada(msg);
				}
				else if(spying) {
					gui.myPrint("");
					gui.myPrint(" --------------------- stop Spying -----------------------");
					spying = false;
					gui.myPrint("");
				}

			} catch (IOException e) {
				System.err.println(
						"Dancarino automatoDancarino() error - validate ID not working: " + e.getMessage());
			}
			if (estado == LER_COMANDO)
				estado = GUARDAR_COMANDO;		
			break;

		case GUARDAR_COMANDO:
			gui.myPrint("vou guardar: " + msg.toString());
			
			memoria = bd.getMemoria();
			memoria.add(msg);
			bd.setMemoria(memoria);
			
			if (estado == GUARDAR_COMANDO)
				estado = LER_COMANDO;
			break;

		case TERMINAR:
			break;

		default:
			System.out.println("Erro no Dançarino: Automato Dançarino - Estado: " + estado);
			break;
		}
	}
		

	public int getId() {
		return ID;
	}
	
	public String geNomeProcesso() {
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
