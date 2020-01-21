package com.selenium.facebook.Vista;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import com.selenium.facebook.Modelo.Categorie;
import com.selenium.facebook.Modelo.Genere;

import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import java.awt.Font;
import javax.swing.JTextField;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.util.List;
import java.awt.event.ActionEvent;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.JComboBox;
import javax.swing.JTextArea;

public class RegistrarGenero extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8141458851232732273L;
	private JPanel contentPane;
	private JTextField textField;
	private Categorie cate = new Categorie();
	
	/**
	 * Launch the application.
	 */
	public void inicio() {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					RegistrarGenero frame = new RegistrarGenero();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public RegistrarGenero() {
		setTitle("Registrar Genero");
		setResizable(false);
		setBounds(100, 100, 316, 392);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		
		JLabel lblGenero = new JLabel("Genero");
		lblGenero.setFont(new Font("Arial", Font.BOLD, 11));
		
		textField = new JTextField();
		textField.setColumns(10);
		final JComboBox<String> comboBox = new JComboBox<String>();
		List<String> list = cate.getAllActive();
		for(String st : list) comboBox.addItem(st);
		final JTextArea textArea = new JTextArea();
		textArea.setLineWrap(true);
		textArea.setTabSize(0);
		textArea.setColumns(1);
		textArea.setRows(20);

		final JButton btnRegistrar = new JButton("Registrar");
		btnRegistrar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				String generoTextField = textField.getText();
				if(generoTextField.equals("")) {
					JOptionPane.showMessageDialog(null, "El campo de genero no debe estar vacio");
				}else {
					btnRegistrar.setEnabled(false);
					Genere gene = new Genere();
					gene.setName(generoTextField.trim());
					Categorie cate = new Categorie();
					
					try {
						gene.setCategories_id(cate.getIdCategories((String)comboBox.getSelectedItem()));
					} catch (SQLException e) {
						e.printStackTrace();
					}
					if(textArea.getText().isEmpty()) {
						gene.setFan_page(null);
					}else {
						gene.setFan_page(textArea.getText().trim());
					}
					gene.insert();
					
					textField.setText("");
					textArea.setText("");
					btnRegistrar.setEnabled(true);
					JOptionPane.showMessageDialog(null, "Se registro el genero con exito");
				}
				
			}
		});
		btnRegistrar.setFont(new Font("Arial", Font.BOLD, 11));
		
				
		JLabel lblFanPage = new JLabel("Fan Page");
		lblFanPage.setFont(new Font("Arial", Font.BOLD, 11));
		
		
		GroupLayout gl_contentPane = new GroupLayout(contentPane);
		gl_contentPane.setHorizontalGroup(
			gl_contentPane.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_contentPane.createSequentialGroup()
					.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_contentPane.createSequentialGroup()
							.addGap(97)
							.addComponent(btnRegistrar))
						.addGroup(gl_contentPane.createSequentialGroup()
							.addGap(107)
							.addComponent(comboBox, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
						.addGroup(gl_contentPane.createSequentialGroup()
							.addGap(46)
							.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
								.addComponent(textField, GroupLayout.PREFERRED_SIZE, 202, GroupLayout.PREFERRED_SIZE)
								.addComponent(textArea, GroupLayout.DEFAULT_SIZE, 192, Short.MAX_VALUE))))
					.addContainerGap(52, GroupLayout.PREFERRED_SIZE))
				.addGroup(gl_contentPane.createSequentialGroup()
					.addGap(118)
					.addComponent(lblFanPage)
					.addContainerGap(133, Short.MAX_VALUE))
				.addGroup(Alignment.TRAILING, gl_contentPane.createSequentialGroup()
					.addContainerGap(116, Short.MAX_VALUE)
					.addComponent(lblGenero, GroupLayout.PREFERRED_SIZE, 73, GroupLayout.PREFERRED_SIZE)
					.addGap(111))
		);
		gl_contentPane.setVerticalGroup(
			gl_contentPane.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_contentPane.createSequentialGroup()
					.addGap(21)
					.addComponent(lblGenero, GroupLayout.PREFERRED_SIZE, 25, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.UNRELATED)
					.addComponent(textField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.UNRELATED)
					.addComponent(lblFanPage)
					.addPreferredGap(ComponentPlacement.RELATED, 15, Short.MAX_VALUE)
					.addComponent(textArea, GroupLayout.PREFERRED_SIZE, 142, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.UNRELATED)
					.addComponent(comboBox, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
					.addGap(18)
					.addComponent(btnRegistrar)
					.addGap(23))
		);
		contentPane.setLayout(gl_contentPane);
	}
}
