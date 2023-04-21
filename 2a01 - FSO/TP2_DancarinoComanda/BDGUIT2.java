
public class BDGUIT2 {
	
	private String consolaLog;

	private boolean dancerAtivos, coreoAtivos;
	
	public BDGUIT2() {
		consolaLog = new String("");
	}

	public String getConsolaLog() {
		return consolaLog;
	}
	public void setConsolaLog(String consolaLog) {
		this.consolaLog = consolaLog;
	}


	public void setDancerAtivos(boolean selected) {
		dancerAtivos = selected;
	}
	
	public boolean isDancerAtivos() {
		return dancerAtivos;
	}
	
	public void setCoreoAtivos(boolean selected) {
		coreoAtivos = selected;
	}
	
	public boolean isCoreoAtivos() {
		return coreoAtivos;
	}

}
