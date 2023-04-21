import javax.swing.JFrame;
import javax.swing.JButton;
import javax.swing.JTextArea;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.SwingConstants;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class GUISpyRobot extends JFrame {

	private static final long serialVersionUID = 1L;
	private static final String newline = "\n";
	
	private JLabel lblGravarTrajetria, lblReproduzirTrajetria, lblNomeDoFicheiro;
	private JTextField textField;
	private JButton btnGravarParar, btnReproduzir;
	private JTextArea textAreaConsola;
	private JScrollPane scrollPane;
	
	private BDSpyRobot bdSpy;

	public GUISpyRobot(String title, BDSpyRobot bd) {
		setTitle(title);
		bdSpy = bd;
		initialize();
	}

	private void initialize() {		
		
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent arg0) {
				bdSpy.terminarThread();
			}
		});
		
		setBounds(100, 100, 515, 287);
//		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		getContentPane().setLayout(null);
		
		lblGravarTrajetria = new JLabel("Gravar / Parar Trajet\u00F3ria");
		lblGravarTrajetria.setHorizontalAlignment(SwingConstants.CENTER);
		lblGravarTrajetria.setBounds(22, 56, 175, 14);
		getContentPane().add(lblGravarTrajetria);
		
		lblReproduzirTrajetria = new JLabel("Reproduzir Trajet\u00F3ria");
		lblReproduzirTrajetria.setHorizontalAlignment(SwingConstants.CENTER);
		lblReproduzirTrajetria.setBounds(22, 148, 175, 14);
		getContentPane().add(lblReproduzirTrajetria);
		
		lblNomeDoFicheiro = new JLabel("Nome do ficheiro");
		lblNomeDoFicheiro.setBounds(219, 22, 111, 14);
		getContentPane().add(lblNomeDoFicheiro);
		
		
		btnGravarParar = new JButton("Gravar");
		btnGravarParar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if(btnGravarParar.getText().equals("Gravar")) {
					btnGravarParar.setText("Parar");
					bdSpy.iniciarGravacao();
					myPrint("Esta gravando ...");
				}
				else {
					btnGravarParar.setText("Gravar");
					bdSpy.pararGravacao();
					myPrint("Parou de gravar!");
				}
			}
		});
		btnGravarParar.setBounds(22, 81, 175, 41);
		getContentPane().add(btnGravarParar);
		
		btnReproduzir = new JButton("Reproduzir");
		btnReproduzir.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				bdSpy.iniciarReproducao();
				myPrint("Reproduzindo ...");
			}
		});
		btnReproduzir.setBounds(22, 173, 175, 41);
		getContentPane().add(btnReproduzir);
		
		
		textField = new JTextField();
		textField.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {	
				
				bdSpy.setNomeFicheiro(textField.getText());
				myPrint("Nome de ficheiro: " + bdSpy.getNomeFicheiro());
				
			}
		});
		textField.setText(bdSpy.getNomeFicheiro());
		textField.setBounds(340, 19, 149, 20);
		getContentPane().add(textField);
		textField.setColumns(10);
		
		scrollPane = new JScrollPane();
		scrollPane.setBounds(219, 50, 270, 188);
		getContentPane().add(scrollPane);
		
		textAreaConsola = new JTextArea();
		scrollPane.setViewportView(textAreaConsola);
		textAreaConsola.setLineWrap(true);
		textAreaConsola.setEditable(false);
		
		setVisible(true);
	
	}
	
	public void myPrint(String text) {
			bdSpy.setConsola(text);
			textAreaConsola.append(bdSpy.getConsola() + newline);
			textAreaConsola.setCaretPosition(textAreaConsola.getDocument().getLength());
	}
}
