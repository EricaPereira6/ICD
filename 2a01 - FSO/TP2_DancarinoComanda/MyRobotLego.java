
public class MyRobotLego {
	public boolean OpenEV3(String s) {
		System.out.println("OpenEV3 ligado" + s);
		return true;
	}
	public void CloseEV3(){
		System.out.println("CloseEV3");
	}
	public void CurvarDireita(int r, int a){
		System.out.println("Curva à Direita: raio " + r + " angulo " + a);
	}
	public void CurvarEsquerda(int r, int a){
		System.out.println("Curva à Esquerda: raio " + r + " angulo " + a);
	}
	public void Parar(boolean b){
		System.out.println("Parar: " + b);
	}
	public void Reta(int d){
		System.out.println("Reta: " + d);
	}
	public void OffsetEsquerdo(int offset){
		System.out.println("Offset Esquerdo: " + offset);
	}
	public void OffsetDireito(int offset){
		System.out.println("Offset Direito: " + offset);
	}
	public boolean Sensor(int input){
		int r = (int) Math.round(Math.random() * 100);
		if( r <= 10)
			return true;
		return false;
	}
}



