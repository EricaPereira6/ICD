public enum Ordem {
	PARAR_FALSE(0, "Parar: false"),
	FRENTE(1, "Reta: 10"),
	CURVAR_DIREITA(2, "Direita: 0 45"),
	CURVAR_ESQUERDA(3, "Esquerda: 0 45"),
	RETAGUARDA(4, "Reta: -10"),
	PARAR_TRUE(5, "Parar: true");
	
	private final int valor;
	private final String nome;
	Ordem(int valorOpcao, String nomeOrdem){
	    valor = valorOpcao; 
	    nome = nomeOrdem;
	}
	public int getValor(){
		return valor;
	}	
	public String toString(){
		return nome;
	}
	public static Ordem findOrdem(int v) {
	    for (Ordem e : Ordem.values()) {
	        if (e.getValor() == v) {
	            return e;
	        }
	    }
	    return null;
	}
}


