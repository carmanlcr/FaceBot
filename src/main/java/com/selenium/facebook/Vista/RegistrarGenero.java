package com.selenium.facebook.Vista;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import com.selenium.facebook.Modelo.Categories;
import com.selenium.facebook.Modelo.Genere;

import javax.swing.GroupLayout;
import javax.swing.ImageIcon;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import java.awt.Font;
import javax.swing.JTextField;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.util.List;
import java.awt.event.ActionEvent;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.JComboBox;

public class RegistrarGenero extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8141458851232732273L;
	private JPanel contentPane;
	private JTextField textField;
	private Categories cate = new Categories();
	
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
		setIconImage(new ImageIcon("C:\\Users\\Usuario\\workspace\\TwitterBot\\src\\main\\java\\com\\selenium\\twitter\\Imagen\\iconotwitter.png").getImage());
		setResizable(false);
		setBounds(100, 100, 309, 217);
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
		
		final JButton btnRegistrar = new JButton("Registrar");
		btnRegistrar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				String generoTextField = textField.getText();
				if(generoTextField.equals("")) {
					JOptionPane.showMessageDialog(null, "El campo de genero no debe estar vacio ");
				}else {
					btnRegistrar.setEnabled(false);
					Genere gene = new Genere();
					gene.setName(generoTextField);
					gene.setCategories_id(comboBox.getSelectedIndex()+1);
					gene.insert();
					
					textField.setText("");
					btnRegistrar.setEnabled(true);
					JOptionPane.showMessageDialog(null, "Se registro el genero con exito");
				}
				
			}
		});
		btnRegistrar.setFont(new Font("Arial", Font.BOLD, 11));
		
		
		GroupLayout gl_contentPane = new GroupLayout(contentPane);
		gl_contentPane.setHorizontalGroup(
			gl_contentPane.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_contentPane.createSequentialGroup()
					.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_contentPane.createSequentialGroup()
							.addGap(108)
							.addComponent(lblGenero, GroupLayout.PREFERRED_SIZE, 73, GroupLayout.PREFERRED_SIZE))
						.addGroup(gl_contentPane.createSequentialGroup()
							.addGap(46)
							.addComponent(textField, GroupLayout.PREFERRED_SIZE, 175, GroupLayout.PREFERRED_SIZE))
						.addGroup(gl_contentPane.createSequentialGroup()
							.addGap(97)
							.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
								.addComponent(comboBox, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
								.addComponent(btnRegistrar))))
					.addContainerGap(72, Short.MAX_VALUE))
		);
		gl_contentPane.setVerticalGroup(
			gl_contentPane.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_contentPane.createSequentialGroup()
					.addGap(21)
					.addComponent(lblGenero, GroupLayout.PREFERRED_SIZE, 25, GroupLayout.PREFERRED_SIZE)
					.addGap(18)
					.addComponent(textField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.UNRELATED)
					.addComponent(comboBox, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.RELATED, 18, Short.MAX_VALUE)
					.addComponent(btnRegistrar)
					.addGap(23))
		);
		contentPane.setLayout(gl_contentPane);
	}
}
