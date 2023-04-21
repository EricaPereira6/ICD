package demo.fso.sync;

public interface IMySyncPoint {
	
	public void entrar() throws InterruptedException;
	
	public void sair() throws InterruptedException;
}
