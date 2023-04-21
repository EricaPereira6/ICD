
public class BDEvitar {

	private boolean fim;
	
	public BDEvitar() {
		fim = false;
	}
	
	public boolean isFim() {
		return fim; 
	}
	
	public void terminarThread() {
		fim = true;
	}
		
}


