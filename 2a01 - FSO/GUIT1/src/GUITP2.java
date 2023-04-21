import javax.swing.JFrame;
import javax.swing.JScrollPane;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JTextArea;

import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Enumeration;
import java.awt.event.ActionEvent;
import javax.swing.JCheckBox;
import javax.swing.JList;
import javax.swing.DefaultListModel;

public class GUITP2 extends JFrame {

	private static final long serialVersionUID = 1L;
	
	private JLabel lblDancarinos, lblCoregrafos;
	private JButton btnLimpaLogging, btnInciarTodosDancarino, btnRemoverDancarino, btnAdicionarDancarino,
			btnIniciarTodosCoreografo, btnRemoverCoreografo, btnAdicionarCoreografo;
	private JCheckBox chckbxActivarTodosDancarino, chckbxActivarTodosCoreografo;
	private JTextArea textAreaLog;
	private JScrollPane scrollCoreografo, scrollDancarino, scrollLog;

	private DefaultListModel<String> modelC, modelD;
	private JList<String> listCoreografo, listDancarino;
	private ArrayList<Coreografo> processosCoreo;
	private ArrayList<Dancarino> processosDancer;
	private int contadorCoreografos, contadorDancarinos, numeroC, numeroD;
	Enumeration<String> nomesProcessos;
	private ArrayList<String> processosCoreoAtivos;
	private ArrayList<String> processosDancerAtivos;

	private final static String newline = "\n";
	
	private CanalComunicacao com;
	private BDGUIT2 bd;
	
	public void run() {	}

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		GUITP2 gui = new GUITP2();
		gui.run();		
	}

	/**
	 * Create the application.
	 */
	public GUITP2() {
		bd = new BDGUIT2();	
		
		com = new CanalComunicacao();
		Thread cc = new Thread(com);
		cc.start();
		
		initialize();
		
		printLog("(" + cc.getName() + "), CanalComunicacao");
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		
		processosCoreo = new ArrayList<Coreografo>();
		processosDancer = new ArrayList<Dancarino>();
		
		processosCoreoAtivos = new ArrayList<String>();
		processosDancerAtivos = new ArrayList<String>();
		
		contadorCoreografos = 1;
		contadorDancarinos = 1;
		numeroC = 1;
		numeroD = 1;

		setTitle("GUI T2");
	    setBounds(100, 100, 579, 633);
	    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	    getContentPane().setLayout(null);

		lblCoregrafos = new JLabel("Core\u00F3grafos");
		lblCoregrafos.setBounds(45, 5, 92, 55);
		getContentPane().add(lblCoregrafos);

		lblDancarinos = new JLabel("Dan\u00E7arinos");
		lblDancarinos.setBounds(298, 18, 70, 28);
		getContentPane().add(lblDancarinos);

		
		// TODO Jlist's Coreografo e Dancarino

		modelC = new DefaultListModel<>();
		listCoreografo = new JList<>(modelC);
		listCoreografo.setLayoutOrientation(JList.VERTICAL);

		scrollCoreografo = new JScrollPane(listCoreografo);
		scrollCoreografo.setBounds(45, 50, 221, 91);
		getContentPane().add(scrollCoreografo);

		modelD = new DefaultListModel<>();
		listDancarino = new JList<>(modelD);
		listDancarino.setBounds(342, 50, 177, 91);
		listDancarino.setLayoutOrientation(JList.VERTICAL);

		scrollDancarino = new JScrollPane(listDancarino);
		scrollDancarino.setBounds(298, 50, 225, 91);
		getContentPane().add(scrollDancarino);
		
		
		// TODO JButton's Adicionar e Remover do Coreografo e Dancarino 

		btnAdicionarCoreografo = new JButton("Adicionar");
		btnAdicionarCoreografo.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				modelC = (DefaultListModel<String>) listCoreografo.getModel();
				modelC.addElement("Coreógrafo " + contadorCoreografos);
				contadorCoreografos++;
				listCoreografo.ensureIndexIsVisible(modelC.size() - 1);
			}
		});
		btnAdicionarCoreografo.setBounds(45, 152, 221, 46);
		getContentPane().add(btnAdicionarCoreografo);

		btnRemoverCoreografo = new JButton("Remover");
		btnRemoverCoreografo.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {

				int index = listCoreografo.getSelectedIndex();
				if (index != -1) {
					modelC.removeElementAt(index);
				}
				
			}
		});
		btnRemoverCoreografo.setBounds(45, 209, 221, 46);
		getContentPane().add(btnRemoverCoreografo);
		

		btnAdicionarDancarino = new JButton("Adicionar");
		btnAdicionarDancarino.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				modelD = (DefaultListModel<String>) listDancarino.getModel();
				modelD.addElement("Dançarino " + contadorDancarinos);
				contadorDancarinos++;
				listDancarino.ensureIndexIsVisible(modelD.size() - 1);
			}
		});
		btnAdicionarDancarino.setBounds(298, 152, 225, 46);
		getContentPane().add(btnAdicionarDancarino);

		btnRemoverDancarino = new JButton("Remover");
		btnRemoverDancarino.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				int index = listDancarino.getSelectedIndex();
				if (index != -1) {
					modelD.removeElementAt(index);
				}
			}
		});
		btnRemoverDancarino.setBounds(298, 209, 225, 46);
		getContentPane().add(btnRemoverDancarino);
		
		// TODO JButton's IniciarTodos do Coreografo e Dancarino

		btnIniciarTodosCoreografo = new JButton("Iniciar Todos");
		btnIniciarTodosCoreografo.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				//get coreo model
				nomesProcessos = modelC.elements();

				//for each string
				clearArrayCoreografos();
				while (nomesProcessos.hasMoreElements()) {

					String nomeProcesso = nomesProcessos.nextElement();
					
					if(addCoreografo(nomeProcesso)) {
						processosCoreo.add(new Coreografo(nomeProcesso, com));
					}
				}
				
				//Inicia todas os processos coreografos				
				for(Coreografo c : processosCoreo) {	
					Thread threadC = new Thread(c);
					threadC.start();

					threadC.setName("(" + threadC.getName() + "), Coreógrafo " + numeroC + ", ID no canal: " + c.getId());
					numeroC++;
					printLog(threadC.getName());
					processosCoreoAtivos.add(c.getNomeProcesso());
				} 
				
				if(processosCoreo.size() > 0)
					chckbxActivarTodosCoreografo.setEnabled(true);
				else
					chckbxActivarTodosCoreografo.setEnabled(false);
			}
		});
		btnIniciarTodosCoreografo.setBounds(45, 266, 221, 46);
		getContentPane().add(btnIniciarTodosCoreografo);


		btnInciarTodosDancarino = new JButton("Iniciar Todos");
		btnInciarTodosDancarino.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				//get dancer model
				Enumeration<String> nomesProcessos = modelD.elements();
				
				//for each string
				clearArrayDancarinos();
				while (nomesProcessos.hasMoreElements()) {
					
					String nomeProcesso = nomesProcessos.nextElement();
					
					if(addDancarino(nomeProcesso)) {
						processosDancer.add(new Dancarino(nomeProcesso, com));
					}
				}
				
				//Inicia todas os processos dancarino				
				for(Dancarino d : processosDancer) {	
					Thread threadD = new Thread(d);
					threadD.start();
					
					threadD.setName("(" + threadD.getName() + "), Dançarino " + numeroD + ", ID no canal: " + d.getId());
					numeroD++;
					printLog(threadD.getName());

					processosDancerAtivos.add(d.getNomeProcesso());
				}
				
				if(processosDancer.size() > 0)
					chckbxActivarTodosDancarino.setEnabled(true);
				else
					chckbxActivarTodosDancarino.setEnabled(false);
			}
		});
		btnInciarTodosDancarino.setBounds(298, 266, 225, 46);
		getContentPane().add(btnInciarTodosDancarino);
		
		
		// TODO CheckBox's activar Todos do Coreografo e do Dancarino

		chckbxActivarTodosCoreografo = new JCheckBox("Activar Todos");
		chckbxActivarTodosCoreografo.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				printLog("");
				
				bd.setCoreoAtivos(chckbxActivarTodosCoreografo.isSelected());
				
				String adjetivo = new String("inactivo");
				if(chckbxActivarTodosCoreografo.isSelected())
					adjetivo = new String("activo");
				
				for(Coreografo c : processosCoreo) {
					c.ativarCoreo(bd.isCoreoAtivos());
					c.activateButtons(bd.isCoreoAtivos());
					printLog("Coreografo " + c.getId() + " " + adjetivo);
					if (chckbxActivarTodosCoreografo.isSelected()) {
						c.desbloquearThread();
					}
				}

			}
		});
		chckbxActivarTodosCoreografo.setBounds(45, 319, 113, 25);
		getContentPane().add(chckbxActivarTodosCoreografo);

		chckbxActivarTodosDancarino = new JCheckBox("Activar Todos");
		chckbxActivarTodosDancarino.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				printLog(""); 
				
				for(Dancarino d : processosDancer) {
					SpyRobot rb = d.getRobotLego();
//					RobotLegoEV3 rb = d.getRobotLego();
					
					if(chckbxActivarTodosDancarino.isSelected()) {
						if (rb.OpenEV3(d.getNomeRobot())) {
							d.setOnOff(true);
						}
						else {
							chckbxActivarTodosDancarino.setSelected(false);
						}
					}
					else {
						d.setOnOff(false);
						rb.CloseEV3();
					}
					
					bd.setDancerAtivos(chckbxActivarTodosDancarino.isSelected());
					
					String adjetivo = new String("inactivo");
					if(chckbxActivarTodosDancarino.isSelected())
						adjetivo = new String("activo");
					
					d.ativarDancer(bd.isDancerAtivos());
					d.activateButtons(chckbxActivarTodosDancarino.isSelected());
					printLog("Dancarino " + d.getId() + " " + adjetivo);
					
					if (chckbxActivarTodosDancarino.isSelected()) {
						d.desbloquearThread();
					}
				}							
			}
		});
		chckbxActivarTodosDancarino.setBounds(298, 319, 113, 25);
		getContentPane().add(chckbxActivarTodosDancarino);
		
		
		// TODO JButton e TextArea do LOG

		btnLimpaLogging = new JButton("Limpa Logging");
		btnLimpaLogging.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				bd.setConsolaLog(" ");
				textAreaLog.setText(bd.getConsolaLog());
				
			}
		});
		btnLimpaLogging.setBounds(45, 359, 478, 25);
		getContentPane().add(btnLimpaLogging);

		scrollLog = new JScrollPane();
		scrollLog.setBounds(45, 395, 478, 187);
		getContentPane().add(scrollLog);
		
		textAreaLog = new JTextArea();
		scrollLog.setViewportView(textAreaLog);
		textAreaLog.setLineWrap(true);
		textAreaLog.setEditable(false);

		chckbxActivarTodosCoreografo.setEnabled(false);
		chckbxActivarTodosDancarino.setEnabled(false);
		
		setVisible(true);
	}
	
	private void clearArrayDancarinos() {
		int size = processosDancer.size();
		for(int i = 0; i < size; i++) {
			processosDancer.remove(0);
		}
	}
	private void clearArrayCoreografos() {
		int size = processosCoreo.size();
		for(int i = 0; i < size; i++) {
			processosCoreo.remove(0);
		}
	}
	
	private boolean addDancarino(String nome) {
		for (String str : processosDancerAtivos) {
			if(str.equals(nome)) {
				return false;
			}
		}
		return true;
	}
	private boolean addCoreografo(String nome) {
		for (String str : processosCoreoAtivos) {	
			if(str.equals(nome)) {
				return false;
			}
		}
		return true;
	}
	
	public void printLog(String text) {
		bd.setConsolaLog(text);
		textAreaLog.append(bd.getConsolaLog() + newline);
		textAreaLog.setCaretPosition(textAreaLog.getDocument().getLength());
	}
}