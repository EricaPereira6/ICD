import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import javax.swing.JTextArea;
import javax.swing.JCheckBox;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class GUICoreografo extends JFrame {


	private static final long serialVersionUID = 1L;

	private JPanel contentPane;
	
	private JLabel lblUltimosComandos;
	private JTextArea textArea;
	private JCheckBox chckbxActivar;
	private JButton btnGerar1Comando, btnGerar16Comandos, btnGerar32Comandos, 
	btnGerarComandosIlimitados, btnPararComandos;
	private JScrollPane scrollPane;
	
	private final static String newline = "\n";

	private BDCoreografo bd;
	
	/**
	 * Launch the application.
	 */
/*	public static void main(String[] args) {
		GUICoreografo GUI = new GUICoreografo();
		GUI.run();
	}
*/	
	public void run() {}

	/**
	 * Create the frame.
	 */
	public GUICoreografo(String title, BDCoreografo b) {
		setTitle(title);
		bd = b;	
		
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent arg0) {
				bd.terminarThread();
			}
		});	
		
//		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 523, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		lblUltimosComandos = new JLabel("Últimos 10 Comandos");
		lblUltimosComandos.setBounds(77, 11, 129, 14);
		contentPane.add(lblUltimosComandos);
		
		scrollPane = new JScrollPane();
		scrollPane.setBounds(10, 36, 277, 214);
		contentPane.add(scrollPane);
		
		textArea = new JTextArea();
		scrollPane.setViewportView(textArea);
		textArea.setLineWrap(true);
		textArea.setEditable(false);		
		
		chckbxActivar = new JCheckBox("Activar");
		chckbxActivar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				
				bd.setActivar(chckbxActivar.isSelected());
				activateButtons(chckbxActivar.isSelected());
				myPrint("Coreografo Activo - activar: " + bd.isActivar());
				
				if (chckbxActivar.isSelected()) {
					bd.desbloquearThread();
				}
			}
		});
		chckbxActivar.setBounds(368, 7, 101, 23);
		contentPane.add(chckbxActivar);
		
		btnGerar1Comando = new JButton("Gerar 1 comando");
		btnGerar1Comando.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				bd.setComando1(true);
			}
		});
		btnGerar1Comando.setBounds(297, 48, 188, 29);
		contentPane.add(btnGerar1Comando);
		
		btnGerar16Comandos = new JButton("Gerar 16 comandos");
		btnGerar16Comandos.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				bd.setComandos16(true);
			}
		});
		btnGerar16Comandos.setBounds(297, 88, 188, 29);
		contentPane.add(btnGerar16Comandos);
		
		btnGerar32Comandos = new JButton("Gerar 32 comandos");
		btnGerar32Comandos.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				bd.setComandos32(true);
			}
		});
		btnGerar32Comandos.setBounds(297, 128, 188, 29);
		contentPane.add(btnGerar32Comandos);
		
		btnGerarComandosIlimitados = new JButton("Gerar comandos ilimitados");
		btnGerarComandosIlimitados.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				bd.setComandosIlimitados(true);
			}
		});
		btnGerarComandosIlimitados.setBounds(297, 168, 188, 29);
		contentPane.add(btnGerarComandosIlimitados);
		
		btnPararComandos = new JButton("Parar comandos");
		btnPararComandos.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				bd.setPararComandos(true);
			}
		});
		btnPararComandos.setBounds(297, 209, 188, 30);
		contentPane.add(btnPararComandos);
		
		JButton btnClear = new JButton("Clear");
		btnClear.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				
				bd.setConsola(" ");
				textArea.setText(bd.getConsola());
			}
		});
		btnClear.setBounds(216, 7, 71, 23);
		contentPane.add(btnClear);
		
		activateButtons(false);
		
		setVisible(true);
	}
	
	public void myPrint(String text) {
		bd.setConsola(text);
		textArea.append(bd.getConsola() + newline);
		textArea.setCaretPosition(textArea.getDocument().getLength());
	}
	
	public void activateButtons(boolean bool) {
		btnGerar1Comando.setEnabled(bool);
		btnGerar16Comandos.setEnabled(bool);
		btnGerar32Comandos.setEnabled(bool);
		btnGerarComandosIlimitados.setEnabled(bool);
		btnPararComandos.setEnabled(bool);
	}
}
