import java.awt.EventQueue;
import java.awt.Window;

import javax.swing.JFrame;
import javax.swing.JButton;
import javax.swing.JTextArea;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class GUISpyRobot {

	private JFrame frame;
	private JLabel lblGravarTrajetria, lblPararGravao, lblReproduzirTrajetria, lblNomeDoFicheiro;
	private JTextField textField;
	private JButton btnGravar, btnParar, btnReproduzir;
	private JTextArea textArea;
	private JScrollPane scrollPane;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					GUISpyRobot window = new GUISpyRobot();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public GUISpyRobot() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 515, 287);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		
		lblGravarTrajetria = new JLabel("Gravar Trajet\u00F3ria");
		lblGravarTrajetria.setBounds(66, 35, 83, 14);
		frame.getContentPane().add(lblGravarTrajetria);
		
		lblPararGravao = new JLabel("Parar Grava\u00E7\u00E3o");
		lblPararGravao.setBounds(71, 104, 75, 14);
		frame.getContentPane().add(lblPararGravao);
		
		lblReproduzirTrajetria = new JLabel("Reproduzir Trajet\u00F3ria");
		lblReproduzirTrajetria.setBounds(58, 173, 108, 14);
		frame.getContentPane().add(lblReproduzirTrajetria);
		
		lblNomeDoFicheiro = new JLabel("Nome do ficheiro");
		lblNomeDoFicheiro.setBounds(247, 22, 83, 14);
		frame.getContentPane().add(lblNomeDoFicheiro);
		
		btnGravar = new JButton("Gravar");
		btnGravar.setBounds(22, 55, 175, 32);
		frame.getContentPane().add(btnGravar);
		
		btnParar = new JButton("Parar");
		btnParar.setBounds(22, 123, 175, 32);
		frame.getContentPane().add(btnParar);
		
		btnReproduzir = new JButton("Reproduzir");
		btnReproduzir.setBounds(22, 191, 175, 32);
		frame.getContentPane().add(btnReproduzir);
		
		textField = new JTextField();
		textField.setBounds(340, 19, 120, 20);
		frame.getContentPane().add(textField);
		textField.setColumns(10);
		
		scrollPane = new JScrollPane();
		scrollPane.setBounds(219, 50, 270, 188);
		frame.getContentPane().add(scrollPane);
		
		textArea = new JTextArea();
		scrollPane.setViewportView(textArea);
		textArea.setLineWrap(true);
		textArea.setEditable(false);
		
		frame.setVisible(true);
	
	}
}
