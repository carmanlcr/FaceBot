package com.selenium.facebook.Vista;


import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import com.selenium.facebook.Modelo.Categories;

import javax.swing.GroupLayout;
import javax.swing.ImageIcon;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JTextField;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import java.awt.Font;
import javax.swing.JButton;
import javax.swing.LayoutStyle.ComponentPlacement;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
public class RegistrarCategories extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2182409473609349755L;
	private JPanel contentPane;
	private JTextField textField;

	/**
	 * Launch the application.
	 */
	public void inicio() {
		RegistrarCategories frame = new RegistrarCategories();
		frame.setVisible(true);
	}

	/**
	 * Create the frame.
	 */
	public RegistrarCategories() {
		setResizable(false);
		setIconImage(new ImageIcon("C:\\Users\\Usuario\\workspace\\TwitterBot\\src\\main\\java\\com\\selenium\\twitter\\Imagen\\iconotwitter.png").getImage());
		setTitle("Registrar Categoria");
		setBounds(100, 100, 450, 209);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		
		textField = new JTextField();
		textField.setColumns(10);
		
		JLabel lblCampaa = new JLabel("Categoria");
		lblCampaa.setFont(new Font("Arial", Font.PLAIN, 12));
		
		JButton btnRegistrar = new JButton("Registrar");
		btnRegistrar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				Categories categorias = new Categories();
				categorias.setName(textField.getText());
				categorias.insert();
				JOptionPane.showMessageDialog(null,"Categoria registrada correctamente");
				textField.setText("");
			}
		});
		
		GroupLayout gl_contentPane = new GroupLayout(contentPane);
		gl_contentPane.setHorizontalGroup(
			gl_contentPane.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_contentPane.createSequentialGroup()
					.addGap(22)
					.addComponent(lblCampaa)
					.addGap(66)
					.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
						.addComponent(btnRegistrar)
						.addComponent(textField, GroupLayout.PREFERRED_SIZE, 166, GroupLayout.PREFERRED_SIZE))
					.addContainerGap(126, Short.MAX_VALUE))
		);
		gl_contentPane.setVerticalGroup(
			gl_contentPane.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_contentPane.createSequentialGroup()
					.addGap(48)
					.addGroup(gl_contentPane.createParallelGroup(Alignment.BASELINE)
						.addComponent(lblCampaa)
						.addComponent(textField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addPreferredGap(ComponentPlacement.RELATED, 47, Short.MAX_VALUE)
					.addComponent(btnRegistrar)
					.addGap(33))
		);
		contentPane.setLayout(gl_contentPane);
	}
}
