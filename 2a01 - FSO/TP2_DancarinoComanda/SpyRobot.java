
public class SpyRobot implements Runnable {
	
	private int estado;
	private final int ESPERAR = 0;
	private final int TERMINAR = 1;

	@Override
	public void run() {
		automatoSpy();
		
	}
	
	public SpyRobot() {
		estado = ESPERAR;
	}
	
	public void automatoSpy() {
		switch(estado){
			case ESPERAR:
				actividadeEsperar();
				break;
				
			case TERMINAR:
				break;

			default:
				System.out.println("Erro no SpyRobot: Automato - Estado: " + estado);
				break;
			}
	}
	
	private void actividadeEsperar() {
		try {
			Thread.sleep(100);
		} catch (InterruptedException e) {
			System.err.println("Dançarino actividadeEsperar() error - Thread.sleep(100) not working: " + e.getMessage());
		}
	}

}
