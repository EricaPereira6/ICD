public class BDSpyRobot {
	
	public enum estados {
		ESPERAR(0),
		TERMINAR(1),
		REPRODUZIR(2),
		GRAVAR(3), 
		PARAR(4), 
		ESCREVER(5);
	     
		private final int valor;
		estados(int valorOpcao){
		    valor = valorOpcao;
		}
		public int getValor(){
			return valor;
		}
	}
	
	private String nomeFicheiro;
	private String consola;
	private int estado;
	private boolean fim;
	
	public BDSpyRobot() {
		nomeFicheiro = new String("espiao");
		consola = new String("");
		estado = estados.ESPERAR.getValor();
		fim = false;
	}
	public boolean isFim() {
		return fim;
	}
	public void setNomeFicheiro(String nomeFicheiro) {
		this.nomeFicheiro = nomeFicheiro;
	}	
	public String getNomeFicheiro() {
		return nomeFicheiro;
	}	
	public String getConsola() {
		return consola;
	}
	public void setConsola(String consola) {
		this.consola = consola;
	}
	public int getEstado() {
		return estado;
	}	
	public void setEstado(int estado) {
		this.estado = estado;
	}
	public void iniciarGravacao() {
		estado = estados.GRAVAR.getValor();
	}
	public void pararGravacao() {
		estado = estados.PARAR.getValor();
	}
	public void iniciarReproducao() {
		estado = estados.REPRODUZIR.getValor();
	}
	public void terminarThread() {
		fim = true;
	}
}