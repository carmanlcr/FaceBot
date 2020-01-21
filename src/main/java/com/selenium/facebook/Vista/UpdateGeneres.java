package com.selenium.facebook.Vista;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JComboBox;
import javax.swing.JButton;
import javax.swing.JTextField;
import javax.swing.JTextArea;

import com.selenium.facebook.Modelo.Categorie;
import com.selenium.facebook.Modelo.Genere;

import javax.swing.JCheckBox;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map.Entry;
import java.awt.event.ActionEvent;

public class UpdateGeneres {

	private JFrame frmActualizarCampaa;
	private JTextField genero_Text;
	private JComboBox<String> comboBox_Campana = new JComboBox<String>();
	private JComboBox<String> comboBox_Genero = new JComboBox<String>();
	private JComboBox<String> comboBox_CampanaChange = new JComboBox<String>();
	private HashMap<String, Integer> hashCampana = new HashMap<String, Integer>();
	private HashMap<String, Integer> hashGenere = new HashMap<String, Integer>();
	private Genere gene;
	
	/**
	 * Launch the application.
	 */
	public void init() {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					UpdateGeneres window = new UpdateGeneres();
					window.frmActualizarCampaa.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public UpdateGeneres() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frmActualizarCampaa = new JFrame();
		frmActualizarCampaa.setResizable(false);
		frmActualizarCampaa.setTitle("Actualizar Campaña");
		frmActualizarCampaa.setBounds(100, 100, 559, 565);
		frmActualizarCampaa.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		JLabel lblNewLabel = new JLabel("Campaña");
		
		
		
		JLabel lblNewLabel_1 = new JLabel("Generos");
		
		
		
		JButton btnNewButton = new JButton("Buscar");
		
		
		JLabel lblNewLabel_2 = new JLabel("Genero");
		
		genero_Text = new JTextField();
		genero_Text.setEnabled(false);
		genero_Text.setEditable(false);
		genero_Text.setColumns(10);
		
		Categorie cate = new Categorie();
		hashCampana = cate.getComboBox();
		
		for(String st : hashCampana.keySet()) comboBox_Campana.addItem(st);
		comboBox_Campana.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				comboBox_Genero.removeAllItems();
				Genere gene = new Genere();
				gene.setCategories_id(Integer.parseInt(hashCampana.get(comboBox_Campana.getSelectedItem().toString()).toString()));
				hashGenere = gene.getGeneresActiveForCategorie();
				for(String string : hashGenere.keySet()) comboBox_Genero.addItem(string);
			}
		});
		
		
		comboBox_CampanaChange.setEnabled(false);
		
		JLabel label = new JLabel("Campaña");
		
		JLabel lblNewLabel_3 = new JLabel("Fan Page");
		
		final JTextArea fanpage = new JTextArea();
		fanpage.setEditable(false);
		fanpage.setEnabled(false);
		fanpage.setLineWrap(true);
		
		final JCheckBox basura = new JCheckBox("Basura");
		basura.setEnabled(false);
		final JCheckBox activo = new JCheckBox("Activo");
		activo.setEnabled(false);
		final JButton btnNewButton_1 = new JButton("Actualizar");
		btnNewButton_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				if(genero_Text.getText().isEmpty()) {
					JOptionPane.showMessageDialog(null, "El campo del Genero no puede quedar vacio");
				}else {
					Genere genere = new Genere();
					genere.setName(genero_Text.getText());
					genere.setCategories_id(Integer.parseInt(hashCampana.get(comboBox_CampanaChange.getSelectedItem().toString()).toString()));
					genere.setFan_page(fanpage.getText());
					if(basura.isSelected()){
						genere.setTrash(true);
					}else {
						genere.setTrash(false);
					}
					
					if(activo.isSelected()) {
						genere.setActive(true);
					}else {
						genere.setActive(false);
					}
					genere.setGeneres_id(gene.getGeneres_id());
					
					try {
						genere.update();
						genero_Text.setText("");
						genero_Text.setEditable(false);
						genero_Text.setEnabled(false);
						comboBox_CampanaChange.removeAllItems();
						comboBox_CampanaChange.setEditable(false);
						comboBox_CampanaChange.setEnabled(false);
						fanpage.setText("");
						fanpage.setEditable(false);
						fanpage.setEnabled(false);
						activo.setSelected(false);
						activo.setEnabled(false);
						basura.setSelected(false);
						basura.setEnabled(false);
						btnNewButton_1.setEnabled(false);
						comboBox_Campana.setSelectedIndex(0);
						JOptionPane.showMessageDialog(null, "Se actualizo la categoria con exito","Success",JOptionPane.INFORMATION_MESSAGE);
					} catch (SQLException e1) {
						e1.printStackTrace();
					}
				}
				
			}
		});
		
		
		GroupLayout groupLayout = new GroupLayout(frmActualizarCampaa.getContentPane());
		groupLayout.setHorizontalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(groupLayout.createSequentialGroup()
					.addContainerGap(259, Short.MAX_VALUE)
					.addComponent(btnNewButton)
					.addGap(229))
				.addGroup(groupLayout.createSequentialGroup()
					.addGap(47)
					.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
						.addComponent(lblNewLabel)
						.addComponent(lblNewLabel_2)
						.addComponent(lblNewLabel_3)
						.addComponent(label, GroupLayout.PREFERRED_SIZE, 60, GroupLayout.PREFERRED_SIZE))
					.addGap(18)
					.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
						.addComponent(fanpage, GroupLayout.PREFERRED_SIZE, 265, GroupLayout.PREFERRED_SIZE)
						.addGroup(groupLayout.createSequentialGroup()
							.addComponent(comboBox_Campana, GroupLayout.PREFERRED_SIZE, 143, GroupLayout.PREFERRED_SIZE)
							.addGap(40)
							.addComponent(lblNewLabel_1)
							.addGap(18)
							.addComponent(comboBox_Genero, GroupLayout.PREFERRED_SIZE, 163, GroupLayout.PREFERRED_SIZE))
						.addGroup(groupLayout.createParallelGroup(Alignment.TRAILING, false)
							.addComponent(comboBox_CampanaChange, Alignment.LEADING, 0, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
							.addComponent(genero_Text, Alignment.LEADING, GroupLayout.DEFAULT_SIZE, 191, Short.MAX_VALUE))
						.addGroup(groupLayout.createParallelGroup(Alignment.TRAILING, false)
							.addComponent(basura, Alignment.LEADING, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
							.addComponent(activo, Alignment.LEADING, GroupLayout.DEFAULT_SIZE, 104, Short.MAX_VALUE)))
					.addContainerGap(24, Short.MAX_VALUE))
				.addGroup(groupLayout.createSequentialGroup()
					.addGap(234)
					.addComponent(btnNewButton_1)
					.addContainerGap(240, Short.MAX_VALUE))
		);
		groupLayout.setVerticalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(groupLayout.createSequentialGroup()
					.addGap(36)
					.addGroup(groupLayout.createParallelGroup(Alignment.BASELINE)
						.addComponent(lblNewLabel)
						.addComponent(comboBox_Campana, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addComponent(lblNewLabel_1)
						.addComponent(comboBox_Genero, GroupLayout.PREFERRED_SIZE, 20, GroupLayout.PREFERRED_SIZE))
					.addGap(35)
					.addComponent(btnNewButton)
					.addGap(45)
					.addGroup(groupLayout.createParallelGroup(Alignment.BASELINE)
						.addComponent(lblNewLabel_2)
						.addComponent(genero_Text, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addGap(46)
					.addGroup(groupLayout.createParallelGroup(Alignment.BASELINE)
						.addComponent(comboBox_CampanaChange, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addComponent(label))
					.addGap(46)
					.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
						.addComponent(lblNewLabel_3)
						.addComponent(fanpage, GroupLayout.PREFERRED_SIZE, 61, GroupLayout.PREFERRED_SIZE))
					.addGap(33)
					.addComponent(basura)
					.addGap(18)
					.addComponent(activo)
					.addGap(34)
					.addComponent(btnNewButton_1)
					.addGap(53))
		);
		frmActualizarCampaa.getContentPane().setLayout(groupLayout);
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				
				
				if(comboBox_Genero.getSelectedItem() == null) {
					JOptionPane.showMessageDialog(null, "Debe seleccionar un genero ");
				}else {
					gene = new Genere();
					gene.setGeneres_id(Integer.parseInt(hashGenere.get(comboBox_Genero.getSelectedItem().toString()).toString()));
					gene = gene.getGenere();
					genero_Text.setEditable(true);
					genero_Text.setEnabled(true);
					genero_Text.setText(gene.getName());
					comboBox_CampanaChange.removeAllItems();
					for(String s : hashCampana.keySet()) comboBox_CampanaChange.addItem(s);
					comboBox_CampanaChange.setEnabled(true);
					for(Entry<String, Integer> entry : hashCampana.entrySet()) 
						if(entry.getValue() == gene.getCategories_id()) comboBox_CampanaChange.setSelectedItem(entry.getKey());;
					fanpage.setEditable(true);
					fanpage.setEnabled(true);
					fanpage.setText(gene.getFan_page());
					if(gene.isActive()) activo.setSelected(true);
					activo.setEnabled(true);
					if(gene.isTrash()) basura.setSelected(true);
					basura.setEnabled(true);
					btnNewButton_1.setEnabled(true);
					
				}
				
			}
		});
	}
}
