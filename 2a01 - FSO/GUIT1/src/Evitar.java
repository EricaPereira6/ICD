public class Evitar implements Runnable {

	private int estado;
	private final int TESTAR_SENSOR = 0;
	private final int TERMINAR = 1;
	
	private BDEvitar bd; 
	private SpyRobot robot;

	public Evitar(BDEvitar bd, SpyRobot robot) {
		estado = TESTAR_SENSOR;
		this.bd = bd;
		this.robot = robot;
	}

	@Override
	public void run() {
		while (estado != TERMINAR) {
			automatoEvitar();
		}
	}

	private void automatoEvitar() {
		switch (estado) {
		case TESTAR_SENSOR:
			
			actividadeEsperar(200);
			
			if(robot.SensorToque(robot.S_1) != 0) {
				robot.Parar(true);
				System.err.println("Robot bateu: a terminar");
				bd.terminarThread();
				estado = TERMINAR;
			}
			if (bd.isFim()) {
				estado = TERMINAR;
			}
			break;

		case TERMINAR:
			break;
		}
	}

	private void actividadeEsperar(long tempo) {
		try {
			Thread.sleep(tempo);
		} catch (InterruptedException e) {
			System.err.println(
					"Dançarino actividadeEsperar() error - Thread.sleep(tempo) not working: " + e.getMessage());
		}
	}

}
