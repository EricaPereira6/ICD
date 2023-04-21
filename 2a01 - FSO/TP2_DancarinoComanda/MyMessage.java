
public class MyMessage {
	
	private int numero;
	private int ordem;
	
	
	
	public MyMessage(int numero, int ordem) {
		this.numero = numero;
		this.ordem = ordem;
	}

	public int getNumero() {
		return numero;
	}
	
	public int getOrdem() {
		return ordem;
	}
	
	@Override
	public String toString() {
		return "[" + getNumero() + ", " + getOrdem() + "]";
	}
}

