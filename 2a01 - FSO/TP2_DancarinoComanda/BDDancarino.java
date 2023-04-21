
public class BDDancarino {
//	private RobotLegoEV3 robotLego;
	private MyRobotLego robotLego;
	private String nomeRobot, consola;
	private boolean onOff, debug;
	private int raio, angulo, distancia;
	private boolean activar;
	
	public BDDancarino() {
//		robotLego = new RobotLegoEV3();
		robotLego = new MyRobotLego();
		nomeRobot = new String("EV7"); // Link
		onOff = false;
		debug = true;
		raio = 10;
		angulo = 90;
		distancia = 20;
		consola = new String("Fazendo Debug ... \n");
		
		activar = false;
	}
	
	public MyRobotLego getRobotLego() {
		return robotLego;
	}
//	public RobotLegoEV3 getRobotLego() {
//		return robotLego;
//	}
	
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

}
