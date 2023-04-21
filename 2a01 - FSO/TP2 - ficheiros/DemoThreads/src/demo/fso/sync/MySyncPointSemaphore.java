package demo.fso.sync;

import java.util.concurrent.Semaphore;

public class MySyncPointSemaphore implements IMySyncPoint {
	private Semaphore s;
	
	public MySyncPointSemaphore() {
		this.s = new Semaphore( 0 );
	}
	
	@Override
	public void entrar() throws InterruptedException {
		this.s.acquire();
	}

	@Override
	public void sair() throws InterruptedException {
		this.s.release();
	}
}
