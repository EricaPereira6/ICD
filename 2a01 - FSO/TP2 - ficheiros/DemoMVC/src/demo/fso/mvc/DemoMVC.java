package demo.fso.mvc;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import javax.swing.DefaultComboBoxModel;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

public class DemoMVC {

	private JFrame frmDemoMvc;
	private JTextField textFieldDetalhes;
	private JList<MySpecialObject> list;
	private JComboBox<MySpecialObject> comboBox;
	
	private DefaultListModel<MySpecialObject> modelJList;
	private DefaultComboBoxModel<MySpecialObject> modelJComboBox;
	
	private int nextId;

	private void myInit() {
		this.modelJList = new DefaultListModel<MySpecialObject>();
		this.list.setModel( this.modelJList );
		
		this.modelJComboBox = new DefaultComboBoxModel<MySpecialObject>();
		this.comboBox.setModel( this.modelJComboBox );
		
		this.nextId = 0;
	}
	
	private void handleAddToJList() {
		this.modelJList.addElement( new MySpecialObject( this.nextId++ ) );
	}
	
	private void handleRemoveFromJList() {
		int[] idxs;
		idxs = this.list.getSelectedIndices();
		
		if ( idxs.length==0 ) {
			return;
		}
		
		for(int idx=idxs.length-1; idx>=0; --idx) {
			this.modelJList.remove( idxs[idx] );
		}
		
		this.textFieldDetalhes.setText( "" );
	}
	
	private void handleSelectJList() {
		int idx;
		idx = this.list.getSelectedIndex();
		
		if ( idx==-1 ) {
			return;
		}
		
		MySpecialObject obj;
		obj = this.modelJList.elementAt( idx );
		
		this.textFieldDetalhes.setText( obj.getDescription() );
	}
	
	private void handleAddToJComboBox() {
		this.modelJComboBox.addElement( new MySpecialObject( this.nextId++ ) );
	}
	
	private void handleRemoveFromJComboBox() {
		int idx;
		idx = this.comboBox.getSelectedIndex();
		
		if ( idx==-1 ) {
			return;
		}
		
		this.modelJComboBox.removeElementAt( idx );
		
		this.textFieldDetalhes.setText( "" );
	}
	
	private void handleSelectJComboBox() {
		int idx;
		idx = this.comboBox.getSelectedIndex();
		
		if ( idx==-1 ) {
			return;
		}
		
		MySpecialObject obj;
		obj = this.modelJComboBox.getElementAt( idx );
		
		this.textFieldDetalhes.setText( obj.getDescription() );
	}
	
	public DemoMVC() {
		initialize();
		myInit();
	}
	
	public void setVisible(boolean visible) {
		this.frmDemoMvc.setVisible(visible);
	}

	private void initialize() {
		frmDemoMvc = new JFrame();
		frmDemoMvc.setTitle("Demo MVC - Model View Controller");
		frmDemoMvc.setBounds(100, 100, 588, 468);
		frmDemoMvc.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frmDemoMvc.getContentPane().setLayout(null);
		
		JLabel lblElementosNumaJList = new JLabel("Elementos numa JList");
		lblElementosNumaJList.setBounds(21, 21, 250, 26);
		frmDemoMvc.getContentPane().add(lblElementosNumaJList);
		
		JScrollPane scrollPaneJList = new JScrollPane();
		scrollPaneJList.setBounds(21, 68, 250, 116);
		frmDemoMvc.getContentPane().add(scrollPaneJList);
		
		list = new JList<MySpecialObject>();
		list.addListSelectionListener(new ListSelectionListener() {
			public void valueChanged(ListSelectionEvent arg0) {
				handleSelectJList();
			}
		});
		scrollPaneJList.setViewportView(list);
		
		JLabel lblElementosNumaJcombobox = new JLabel("Elementos numa JComboBox");
		lblElementosNumaJcombobox.setBounds(292, 21, 250, 26);
		frmDemoMvc.getContentPane().add(lblElementosNumaJcombobox);
		
		comboBox = new JComboBox<MySpecialObject>();
		comboBox.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent arg0) {
				handleSelectJComboBox();
			}
		});
		comboBox.setBounds(292, 67, 250, 32);
		frmDemoMvc.getContentPane().add(comboBox);
		
		JButton btnButtonAddToJList = new JButton("Adiciona na JList");
		btnButtonAddToJList.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				handleAddToJList();
			}
		});
		btnButtonAddToJList.setBounds(21, 205, 250, 35);
		frmDemoMvc.getContentPane().add(btnButtonAddToJList);
		
		JButton btnButtonAddToJComboBox = new JButton("Adiciona na JComboBox");
		btnButtonAddToJComboBox.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				handleAddToJComboBox();
			}
		});
		btnButtonAddToJComboBox.setBounds(292, 205, 250, 35);
		frmDemoMvc.getContentPane().add(btnButtonAddToJComboBox);
		
		JButton btnButtonRemoveFromJList = new JButton("Remove da JList");
		btnButtonRemoveFromJList.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				handleRemoveFromJList();
			}
		});
		btnButtonRemoveFromJList.setBounds(21, 261, 250, 35);
		frmDemoMvc.getContentPane().add(btnButtonRemoveFromJList);
		
		JButton btnButtonRemoveFromJComboBox = new JButton("Remove da JComboBox");
		btnButtonRemoveFromJComboBox.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				handleRemoveFromJComboBox();
			}
		});
		btnButtonRemoveFromJComboBox.setBounds(292, 261, 250, 35);
		frmDemoMvc.getContentPane().add(btnButtonRemoveFromJComboBox);
		
		JLabel lblDetalhes = new JLabel("Detalhes");
		lblDetalhes.setBounds(21, 317, 92, 26);
		frmDemoMvc.getContentPane().add(lblDetalhes);
		
		textFieldDetalhes = new JTextField();
		textFieldDetalhes.setEditable(false);
		textFieldDetalhes.setBounds(21, 364, 521, 32);
		frmDemoMvc.getContentPane().add(textFieldDetalhes);
		textFieldDetalhes.setColumns(10);
	}
}
