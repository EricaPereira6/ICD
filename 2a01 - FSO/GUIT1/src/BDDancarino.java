import java.util.concurrent.Semaphore;

public class BDDancarino {
	private SpyRobot robotLego;
	private String nomeRobot, consola; 
	private boolean onOff, debug;
	private int raio, angulo, distancia;
	private boolean activar;
	private Semaphore activo;
	private boolean fim;
	 
	public BDDancarino() {
		robotLego = new SpyRobot();
		new Thread(robotLego).start();
		nomeRobot = new String("EV7");
		onOff = false;
		debug = true;
		raio = 0;
		angulo = 45;
		distancia = 10;
		consola = new String("Fazendo Debug ... \n");
		activar = false;
		activo = new Semaphore(0);
		fim = false;
	}
	
	public boolean isFim() {
		return fim;
	}
	
	public SpyRobot getRobotLego() {
		return robotLego;
	}
	
	public boolean isActivar() {
		return activar;
	}
	public void setActivar(boolean activar) {
		this.activar = activar;
	}
	
	public String getNomeRobot() {
		return nomeRobot;
	}
	public void setNomeRobot(String nomeRobot) {
		this.nomeRobot = nomeRobot;
	}
	
	public String getConsola() {
		return consola;
	}
	public void setConsola(String consola) {
		this.consola = consola;
	}
	
	public boolean isonOff() {
		return onOff;
	}
	public void setOnOff(boolean onOff) {
		this.onOff = onOff;
	}
	
	public boolean isDebug() {
		return debug;
	}
	public void setDebug(boolean debug) {
		this.debug = debug;
	}
	public int getRaio() {
		return raio;
	}
	public void setRaio(int raio) {
		this.raio = raio;
	}	
	public int getAngulo() {
		return angulo;
	}
	public void setAngulo(int angulo) {
		this.angulo = angulo;
	}
		public int getDistancia() {
		return distancia;
	}
	public void setDistancia(int distancia) {
		this.distancia = distancia;
	}
	public void bloquearThread() {
		try {
			activo.acquire();
		} catch (InterruptedException e) {
			System.err.println("BDDancarino bloquearThread error - Semaphore activo not working: " + e.getMessage());
		}
	}
	public void desbloquearThread() {
		activo.release();
	}
	public void terminarThread() {
		fim = true;	
	}
	public void terminarSpyThread() {
		robotLego.terminarThread();
	}
}