import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.concurrent.Semaphore;

public class SpyRobot implements Runnable {
	
	private int estado;
	private final int ESPERAR = 0;
	private final int TERMINAR = 1;
	private final int REPRODUZIR = 2;
	private final int GRAVAR = 3;
	private final int PARAR = 4;
	private final int ESCREVER = 5; 
	
	public final int S_1 = RobotLegoEV3.S_1;
	public final int S_2 = RobotLegoEV3.S_2;
	public final int S_3 = RobotLegoEV3.S_3;
	public final int S_4 = RobotLegoEV3.S_4;
	
	private BDSpyRobot bdSpy;
	private GUISpyRobot guiSpy;
	private MyRobotLego robot;
//	private RobotLegoEV3 robot;

	// Tempo de reta e retaguarda sao iguais 720
	private final int tReta10 = 700;
	// Tempo de curvar Esquerda e curva Direita sao iguais 180
	private final int tCurvar045 = 200;
	private long timestamp;
	
	private ArrayList<Integer> mensagensEspiadas;
	private boolean espiar;
	
	private Semaphore exclusaoMutua;
	
	@Override
	public void run() {
		while (estado != TERMINAR) {
			automatoSpy();
		}
	}
	
	public SpyRobot() {		
		estado = ESPERAR;
		bdSpy = new BDSpyRobot();
		guiSpy = new GUISpyRobot("SpyRobot", bdSpy);
		robot = new MyRobotLego();
//		robot = new RobotLegoEV3();
		
		timestamp = 0;
		
		mensagensEspiadas = new ArrayList<Integer>();
		espiar = false;
		
		exclusaoMutua = new Semaphore(1);
	}
	
	
	private void automatoSpy() {	
		switch(estado){
			case ESPERAR:
				if(bdSpy.isFim()) {
					estado = TERMINAR;
				}
				actividadeEsperar(100);
				if (estado == ESPERAR)
					estado = bdSpy.getEstado();
				break;
				
			case REPRODUZIR:
				lerFicheiro();
				
				if (estado == REPRODUZIR) {
					estado = ESPERAR;
					bdSpy.setEstado(estado);
				}
				break;
				
			case ESCREVER:
				escreverFicheiro();
				
				if (estado == ESCREVER) {
					estado = ESPERAR;
					bdSpy.setEstado(estado);
				}
				break;
				
			case GRAVAR:
				escutarCanal();
				
				if(estado == GRAVAR) {
					estado = ESPERAR;
					bdSpy.setEstado(estado);
				}
				break;
				
			case PARAR:
				pararEscutar();
				
				if (estado == PARAR) {
					estado = ESCREVER;
					bdSpy.setEstado(estado);
				}
				break;
				
			case TERMINAR:
				break;

			default:
				System.err.println("Erro no SpyRobot: Automato - Estado: " + estado);
				break;
			}
	}
	
	private void escutarCanal() {
		
		espiar = true;
		
		mensagensEspiadas = new ArrayList<Integer>();
		
		
	}
	
	public boolean isEspiar() {
		return espiar;
	}

	private void pararEscutar() {
		
		espiar = false;
		
		guiSpy.myPrint("");
		
		for(Integer ordem:  mensagensEspiadas) {
			String str = Ordem.findOrdem(ordem).toString();
			guiSpy.myPrint("Mensagem espiada: " + str);
		}
		
		guiSpy.myPrint("");
		
	}

	private void escreverFicheiro() {
		
		OutputStream os = null;
		File ficheiro = null;
		
		try {
			
			ficheiro = new File(bdSpy.getNomeFicheiro());
			ficheiro.createNewFile();
			
			os = new FileOutputStream(ficheiro);
									
//			System.err.println("Ficheiro Válido? " + ficheiro.isFile( ) 
//				+ "Permissões de escrita? " + ficheiro.canWrite()
//					+ "Dir?" + ficheiro.getAbsolutePath()
//						+ "Existe? " + ficheiro.exists());
						
			for(Integer ordem : mensagensEspiadas) {
				
//				os.write(msg.getNumero());
//				os.write(msg.getOrdem());
				os.write(ordem);
			}
			
		}catch(Exception e) {
			
			e.printStackTrace();
			guiSpy.myPrint("Não foi possível escrever no ficheiro fornecido");
		
		} finally {
			
			try {
				os.close();
			
			} catch (IOException e) {
				
				e.printStackTrace();
			}
			
		}
		
	}

	private void lerFicheiro(){
		guiSpy.myPrint("");
		
		String nomeFicheiro = null;
		File ficheiro = null;
		InputStream is = null;
//		MyMessage msg = null;
		
		try {
			nomeFicheiro = bdSpy.getNomeFicheiro();
			ficheiro = new File(nomeFicheiro);
			
			is = new FileInputStream(ficheiro);
			
//			int numero;
			int ordem;
//			while((numero = is.read()) != -1) {
			while((ordem = is.read()) != -1) {
				
//				ordem = is.read();
				
//				msg = new MyMessage(numero, ordem);
				comandarRobot(ordem);
				String str = Ordem.findOrdem(ordem).toString();
				guiSpy.myPrint("Mensagem espiada: " + str);
			}
			
		}catch (IOException e) {	
			guiSpy.myPrint("Não foi possível ler o ficheiro: " + ficheiro.getPath() + e.getMessage());
		
		}finally {
			try {
				is.close();
				
			} catch (IOException e) {
				
				e.printStackTrace();
			}
		}
		guiSpy.myPrint("");
	}
	
	private void actividadeEsperar(long tempo) {
		try {
			Thread.sleep(tempo);
		} catch (InterruptedException e) {
			System.err.println("Dançarino actividadeEsperar() error - Thread.sleep(tempo) not working: " + e.getMessage());
		}
	}

	private void comandarRobot(int ordem) {		
		switch(ordem) {
			case 0:
				Parar(false);
				break;
			case 1:
				timestamp = System.currentTimeMillis();
				Reta(10);
				while (System.currentTimeMillis() - timestamp < tReta10);
				break;
			case 2:
				timestamp = System.currentTimeMillis();
				CurvarDireita(0, 45);
				while (System.currentTimeMillis() - timestamp < tCurvar045);
				break;
			case 3:
				timestamp = System.currentTimeMillis();
				CurvarEsquerda(0, 45);
				while (System.currentTimeMillis() - timestamp < tCurvar045);
				break;
			case 4:
				timestamp = System.currentTimeMillis();
				Reta(-10);
				while (System.currentTimeMillis() - timestamp < tReta10);
				break;
			case 5: 
				Parar(true);
				break;
			default:
				System.err.println("SpyRobot comandarRobot(msg) - Ordem inválida : " + ordem);
		}
		
	}
	public boolean OpenEV3(String nomeRobot) {
		boolean abriu = false;
		try {
			exclusaoMutua.acquire();
			abriu = robot.OpenEV3(nomeRobot);
			exclusaoMutua.release();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		return abriu;
	}

	public void CloseEV3(){
		try {
			exclusaoMutua.acquire();
			robot.CloseEV3();
			exclusaoMutua.release();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	public void Parar(boolean b) {	
		try {
			exclusaoMutua.acquire();
			robot.Parar(b);
			if (espiar) {
				if (b) {
					mensagensEspiadas.add(Ordem.PARAR_TRUE.getValor());
				}
				else {
					mensagensEspiadas.add(Ordem.PARAR_FALSE.getValor());
				}
			}
			exclusaoMutua.release();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	public void Reta(int distancia) {
		try {
			exclusaoMutua.acquire();
			robot.Reta(distancia);
			if (espiar) {
				if (distancia < 0) {
					mensagensEspiadas.add(Ordem.RETAGUARDA.getValor());
				}
				else {
					mensagensEspiadas.add(Ordem.FRENTE.getValor());
				}
			}
			exclusaoMutua.release();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

	}
	public void CurvarDireita(int raio, int angulo) {
		try {
			exclusaoMutua.acquire();
			robot.CurvarDireita(raio, angulo);
			if (espiar) {
				mensagensEspiadas.add(Ordem.CURVAR_DIREITA.getValor());
			}
			exclusaoMutua.release();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	public void CurvarEsquerda(int raio, int angulo) {
		try {
			exclusaoMutua.acquire();
			robot.CurvarEsquerda(raio, angulo);
			if (espiar) {
				mensagensEspiadas.add(Ordem.CURVAR_ESQUERDA.getValor());
			}
			exclusaoMutua.release();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	public int SensorToque(int sensor) {			
		int sentiu = -1;	
		try {
			exclusaoMutua.acquire();
			sentiu = robot.SensorToque(sensor);
			exclusaoMutua.release();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		return sentiu;
	}

	public void terminarThread() {
		bdSpy.terminarThread();
	}
}
