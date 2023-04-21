
public class BDt1 {
	private String nomeRobot, consola;
	private boolean onOff, debug;
	private int raio, angulo, distancia;
	
	public BDt1() {
		nomeRobot = new String("ZE");
		onOff = false;
		debug = true;
		raio = 10;
		angulo = 90;
		distancia = 20;
		consola = new String(" ... ");
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
