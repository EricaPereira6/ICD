package demo.fso.sync;

public class MySyncPointMonitor implements IMySyncPoint {
	private Object o;
	
	private boolean isFree;
	
	public MySyncPointMonitor() {
		this.o = new Object();
		this.isFree = true;
	}
	
	@Override
	public void entrar() throws InterruptedException {
		synchronized ( this.o ) {
			while ( this.isFree==false ) {
				this.o.wait();
			}
			
			this.isFree = false;
		}
	}

	@Override
	public void sair() throws InterruptedException {
		synchronized ( this.o ) {
			this.isFree = true;
			
			this.o.notifyAll();
		}
	}

}
