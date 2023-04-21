import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JTextArea;
import java.awt.Color;

public class GUIDancarino extends JFrame {

	private JPanel contentPane;
	private JTextField textFieldRobot, textFieldRaio, textFieldAngulo, textFieldDistancia;
	private JLabel lblRobot, lblRaio, lblAngulo, lblDistancia, lblConsola;
	private JRadioButton rdbtnOnOff;
	private JButton btnFrente, btnParar, btnEsquerda, btnDireita, btnRetaguarda;
	private JCheckBox chckbxDebug;
	private JTextArea textAreaConsola;
	private JScrollPane scrollPane;
	private JCheckBox chckbxActivar;

	private final static String newline = "\n";

	private BDDancarino bd;
	private MyRobotLego rb;
//	private RobotLegoEV3 rb;

	public void run() {
 
	}

	/**
	 * Launch the application.
	 */
/*	public static void main(String[] args) {
		GUIDancarino frame = new GUIDancarino();
		frame.run();
	}
*/

	/**
	 * Create the frame.
	 */
	public GUIDancarino(String title, BDDancarino b) {
		bd = b;
		rb = bd.getRobotLego();

		this.setTitle(title);
//		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 487);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		lblRobot = new JLabel("Robot");
		lblRobot.setBounds(77, 11, 39, 14);
		contentPane.add(lblRobot);

		textFieldRobot = new JTextField();
		textFieldRobot.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				
				bd.setNomeRobot(textFieldRobot.getText());
				myPrint("Nome robot: " + bd.getNomeRobot());
			}
		});
		textFieldRobot.setText(bd.getNomeRobot());
		textFieldRobot.setBounds(126, 8, 186, 20);
		contentPane.add(textFieldRobot);
		textFieldRobot.setColumns(10);

		rdbtnOnOff = new JRadioButton("On/Off");
		rdbtnOnOff.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				if (rdbtnOnOff.isSelected()) {
					
					if (rb.OpenEV3(bd.getNomeRobot())) {
						bd.setOnOff(rdbtnOnOff.isSelected());
					}
					else {
						rdbtnOnOff.setSelected(false);
					}
				}
				else {
					bd.setOnOff(rdbtnOnOff.isSelected());
					rb.CloseEV3();
				}
				activateButtons(bd.isonOff());
				myPrint("OnOff: " + bd.isonOff());
			}
		});
		rdbtnOnOff.setSelected(bd.isonOff());
		rdbtnOnOff.setBounds(333, 7, 70, 23);
		contentPane.add(rdbtnOnOff);

		lblRaio = new JLabel("Raio");
		lblRaio.setBounds(10, 39, 46, 14);
		contentPane.add(lblRaio);

		textFieldRaio = new JTextField();
		textFieldRaio.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				try {
					bd.setRaio(Integer.parseInt(textFieldRaio.getText()));
				} catch (NumberFormatException nfe) {
					textFieldRaio.setText("" + bd.getRaio());
					myPrint("Raio : Exception Error!");
				}
				myPrint("Raio: " + bd.getRaio());
			}
		});
		textFieldRaio.setText("" + bd.getRaio());
		textFieldRaio.setBounds(42, 36, 46, 20);
		contentPane.add(textFieldRaio);
		textFieldRaio.setColumns(10);

		lblAngulo = new JLabel("Angulo");
		lblAngulo.setBounds(136, 39, 39, 14);
		contentPane.add(lblAngulo);

		textFieldAngulo = new JTextField();
		textFieldAngulo.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				try {
					bd.setAngulo(Integer.parseInt(textFieldAngulo.getText()));
				} catch (NumberFormatException nfe) {
					textFieldAngulo.setText("" + bd.getAngulo());
					myPrint("Angulo : Exception Error!");
				}
				myPrint("Angulo: " + bd.getAngulo());
			}
		});
		textFieldAngulo.setText("" + bd.getAngulo());
		textFieldAngulo.setBounds(185, 39, 46, 20);
		contentPane.add(textFieldAngulo);
		textFieldAngulo.setColumns(10);

		lblDistancia = new JLabel("Distancia");
		lblDistancia.setBounds(301, 39, 46, 14);
		contentPane.add(lblDistancia);

		textFieldDistancia = new JTextField();
		textFieldDistancia.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				try {
					bd.setDistancia(Integer.parseInt(textFieldDistancia.getText()));
				} catch (NumberFormatException nfe) {
					textFieldDistancia.setText("" + bd.getDistancia());
					myPrint("Distancia : Exception Error!");
				}
				myPrint("Distancia: " + bd.getDistancia());
			}
		});
		textFieldDistancia.setText("" + bd.getDistancia());
		textFieldDistancia.setBounds(357, 37, 46, 20);
		contentPane.add(textFieldDistancia);
		textFieldDistancia.setColumns(10);

		btnFrente = new JButton("Frente");
		btnFrente.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				rb.Reta(bd.getDistancia());
				rb.Parar(false);				
				myPrint("Reta: " + bd.getDistancia());
				
			}
		});
		btnFrente.setBackground(Color.GREEN);
		btnFrente.setBounds(172, 70, 101, 44);
		contentPane.add(btnFrente);

		btnParar = new JButton("Parar");
		btnParar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				rb.Parar(true);				
				myPrint("Parar");
			}
		});
		btnParar.setBackground(Color.RED);
		btnParar.setBounds(172, 125, 101, 44);
		contentPane.add(btnParar);

		btnDireita = new JButton("Direita");
		btnDireita.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				rb.CurvarDireita(bd.getRaio(), bd.getAngulo());
				rb.Parar(false);				
				myPrint("Direita: raio " + bd.getRaio() + " angulo " + bd.getAngulo());
			}
		});
		btnDireita.setBackground(Color.MAGENTA);
		btnDireita.setBounds(283, 125, 101, 44);
		contentPane.add(btnDireita);

		btnEsquerda = new JButton("Esquerda");
		btnEsquerda.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				rb.CurvarEsquerda(bd.getRaio(), bd.getAngulo());
				rb.Parar(false);
				myPrint("Esquerda: raio " + bd.getRaio() + " angulo " + bd.getAngulo());
			}
		});
		btnEsquerda.setBackground(Color.ORANGE);
		btnEsquerda.setBounds(60, 125, 101, 44);
		contentPane.add(btnEsquerda);

		btnRetaguarda = new JButton("Retaguarda");
		btnRetaguarda.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				rb.Reta(-bd.getDistancia());
				rb.Parar(false);
				myPrint("Reta: " + -bd.getDistancia());
			}
		});
		btnRetaguarda.setBackground(Color.BLUE);
		btnRetaguarda.setBounds(172, 180, 101, 44);
		contentPane.add(btnRetaguarda);

		chckbxDebug = new JCheckBox("Debug");
		chckbxDebug.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				bd.setDebug(chckbxDebug.isSelected());
				myPrint("Debug: " + bd.isDebug());
			}
		});
		chckbxDebug.setSelected(bd.isDebug());
		chckbxDebug.setBounds(10, 212, 61, 23);
		contentPane.add(chckbxDebug);

		lblConsola = new JLabel("Consola");
		lblConsola.setBounds(10, 242, 46, 14);
		contentPane.add(lblConsola);

		scrollPane = new JScrollPane();
		scrollPane.setBounds(10, 257, 414, 180);
		contentPane.add(scrollPane);

		textAreaConsola = new JTextArea();
		textAreaConsola.setText(bd.getConsola());
		scrollPane.setViewportView(textAreaConsola);
		textAreaConsola.setLineWrap(true);
		textAreaConsola.setEditable(false);
		
		chckbxActivar = new JCheckBox("Activar");
		chckbxActivar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				if(!bd.isonOff()) 
					chckbxActivar.setSelected(false);
				bd.setActivar(chckbxActivar.isSelected());
				myPrint("Dançarino Activo - activar: " + bd.isActivar());
			}
		});
		chckbxActivar.setBounds(354, 212, 70, 23);
		contentPane.add(chckbxActivar);
		
		JButton btnClear = new JButton("Clear");
		btnClear.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				
				bd.setConsola(" ");
				textAreaConsola.setText(bd.getConsola());
			}
		});
		btnClear.setBounds(77, 212, 70, 23);
		contentPane.add(btnClear);

		activateButtons(false);

		setVisible(true);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
	}

	public void myPrint(String text) {
		if (bd.isDebug()) {
			bd.setConsola(text);
			textAreaConsola.append(bd.getConsola() + newline);
			textAreaConsola.setCaretPosition(textAreaConsola.getDocument().getLength());
		}
	}

	protected void activateButtons(boolean onOff) {
		btnFrente.setEnabled(onOff);
		btnEsquerda.setEnabled(onOff);
		btnDireita.setEnabled(onOff);
		btnRetaguarda.setEnabled(onOff);
		btnParar.setEnabled(onOff);
	}
}
