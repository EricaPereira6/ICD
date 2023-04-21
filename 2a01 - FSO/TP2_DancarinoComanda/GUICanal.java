import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

public class GUICanal {

	private JFrame frame;
	private JTextArea textArea;
	private JScrollPane scrollPane;
	
	private final static String newline = "\n";
	
	private BDCanal v;

	/**
	 * Launch the application.
	 */
//	public static void main(String[] args) {
//		EventQueue.invokeLater(new Runnable() {
//			public void run() {
//				try {
//					GUICanal window = new GUICanal();
//					window.frame.setVisible(true);
//				} catch (Exception e) {
//					e.printStackTrace();
//				}
//			}
//		});
//	}

	/**
	 * Create the application.
	 */
	public GUICanal(BDCanal v) {
		this.v = v;
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame("Canal de Comunicação");
		frame.setBounds(100, 100, 679, 300);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		
		scrollPane = new JScrollPane();
		scrollPane.setBounds(10, 11, 643, 239);
		frame.getContentPane().add(scrollPane);
		
		textArea = new JTextArea();
		scrollPane.setViewportView(textArea);
		textArea.setLineWrap(true);
		textArea.setEditable(false);
		
		frame.setVisible(true);
	}
	
	public void printChannel(String text) {
		v.setConsola(text);
		textArea.append(v.getConsola() + newline);
		textArea.setCaretPosition(textArea.getDocument().getLength());
	}
}
