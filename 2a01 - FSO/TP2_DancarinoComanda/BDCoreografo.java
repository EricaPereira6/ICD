
public class BDCoreografo {
	private boolean activar, debug, comando1, comandos16, comandos32,
	comandosIlimitados, pararComandos;
	private String consola;

	public BDCoreografo() {
		activar = false;
		debug = false;
		comando1 = false;
		comandos16 = false;
		comandos32 = false;
		comandosIlimitados = false;
		pararComandos = false;
		consola = new String("");
	}

	public boolean isActivar() {
		return activar;
	}

	public void setActivar(boolean activar) {
		this.activar = activar;
	}

	public boolean isDebug() {
		return debug;
	}

	public void setDebug(boolean debug) {
		this.debug = debug;
	}

	public boolean isComando1() {
		return comando1;
	}

	public void setComando1(boolean comando1) {
		this.comando1 = comando1;
	}

	public boolean isComandos16() {
		return comandos16;
	}

	public void setComandos16(boolean comandos16) {
		this.comandos16 = comandos16;
	}

	public boolean isComandos32() {
		return comandos32;
	}

	public void setComandos32(boolean comandos32) {
		this.comandos32 = comandos32;
	}

	public boolean isComandosIlimitados() {
		return comandosIlimitados;
	}

	public void setComandosIlimitados(boolean comandosIlimitados) {
		this.comandosIlimitados = comandosIlimitados;
	}

	public boolean isPararComandos() {
		return pararComandos;
	}

	public void setPararComandos(boolean pararComandos) {
		this.pararComandos = pararComandos;
	}

	public String getConsola() {
		return consola;
	}

	public void setConsola(String consola) {
		this.consola = consola;
	}
}
