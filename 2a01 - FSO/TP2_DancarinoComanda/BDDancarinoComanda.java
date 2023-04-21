import java.util.ArrayList;

public class BDDancarinoComanda {
	
	private ArrayList<MyMessage> memoria;
	private GUIDancarino gui;
	
	public BDDancarinoComanda() {
		memoria = new ArrayList<MyMessage>();
	}

	public ArrayList<MyMessage> getMemoria() {
		return memoria;
	}

	public void setMemoria(ArrayList<MyMessage> memoria) {
		this.memoria = memoria;
	}

	public void activateButtons(boolean bool) {
		gui.activateButtons(bool);
	}
	
	public void setGUI(GUIDancarino gui) {
		this.gui = gui;
	}
	
	

}
