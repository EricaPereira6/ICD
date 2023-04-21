import java.util.concurrent.Semaphore;

public class BDCoreografo {
	private boolean activar, comando1, comandos16, comandos32,
	comandosIlimitados, pararComandos;
	private String consola;
	private Semaphore activo;
	private boolean fim;

	public BDCoreografo() {
		comando1 = false;
		comandos16 = false;
		comandos32 = false;
		comandosIlimitados = false;
		pararComandos = false;
		consola = new String("");
		
		activar = false;
		activo = new Semaphore(0);
		fim = false;
	}

	public boolean isFim() {
		return fim;
	}	
	public boolean isActivar() {
		return activar;
	}
	public void setActivar(boolean activar) {
		this.activar = activar;
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
	public void bloquearThread() {
		try {
			activo.acquire();
		} catch (InterruptedException e) {
			System.err.println("BDCoreografo bloquearThread() error - Semaphore activo not working: " + e.getMessage());
		}
	}
	public void desbloquearThread() {
		activo.release();
	}
	public void terminarThread() {
		fim = true;
	}
}
