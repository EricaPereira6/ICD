import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class GUICanal extends JFrame {
	
	private JTextArea textArea;
	private JScrollPane scrollPane;
	
	private final static String newline = "\n";
	
	private BDCanal bd;

	/**
	 * Create the application.
	 */
	public GUICanal(BDCanal bd) {
		this.bd = bd;
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent arg0) {
				bd.terminarThread();
			}
		});
		
		setTitle("Canal de Comunicação");
		setBounds(100, 100, 679, 300);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		getContentPane().setLayout(null);
		
		scrollPane = new JScrollPane();
		scrollPane.setBounds(10, 11, 643, 239);
		getContentPane().add(scrollPane);
		
		textArea = new JTextArea();
		scrollPane.setViewportView(textArea);
		textArea.setLineWrap(true);
		textArea.setEditable(false);
		
		setVisible(true);
	}
	
	public void printChannel(String text) {
		bd.setConsola(text);
		textArea.append(bd.getConsola() + newline);
		textArea.setCaretPosition(textArea.getDocument().getLength());
	}
}
