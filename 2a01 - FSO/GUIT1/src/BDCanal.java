
public class BDCanal {
	private String consola;
	private boolean fim;
	
	public BDCanal() {
		consola = new String("");
		fim = false;
	}

	public String getConsola() {
		return consola;
	}
	public void setConsola(String consola) {
		this.consola = consola;
	}
	public boolean isFim() {
		return fim;
	}
	public void terminarThread() {
		fim = true;	
	}
}


