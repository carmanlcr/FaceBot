package com.selenium.facebook.Vista;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import com.selenium.facebook.Modelo.*;

import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JTextArea;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.JComboBox;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.util.List;
import java.awt.event.ActionEvent;

public class RegistrarHashTag extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = -193771187999846622L;
	private JPanel contentPane;
	private Categorie cate = new Categorie();
	private List<String> list = cate.getAllActive();
	/**
	 * Launch the application.
	 */
	public void inicio() {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					RegistrarHashTag frame = new RegistrarHashTag();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 * @throws SQLException 
	 */
	public RegistrarHashTag() throws SQLException {
		setResizable(false);
		setTitle("Registrar HashTag");
		setBounds(100, 100, 373, 329);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		
		final JTextArea textArea = new JTextArea();
		
		JLabel lblHashtag = new JLabel("HashTag");
		
		JLabel lblGenero = new JLabel("Genero");
		
		final Genere genero = new Genere();
		
		JLabel lblSubCategoria = new JLabel("Sub Categoria");
		
		final JComboBox<String> comboBoxSubCategorie = new JComboBox<String>();
		
		comboBoxSubCategorie.setEnabled(false);
		
		
		final JComboBox<Object> comboBoxGenere = new JComboBox<Object>();
		final JComboBox<String> comboCategorie = new JComboBox<String>() ;
		comboCategorie.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				comboBoxSubCategorie.removeAllItems();
				Sub_Categorie subCate = new Sub_Categorie();
				subCate.setCategories_id(comboCategorie.getSelectedIndex()+1);
				List<String> listSubCate = subCate.getSubCategories();
				genero.setCategories_id(comboCategorie.getSelectedIndex()+1);
				List<String> map = genero.getGeneresActive(); 
				if(!listSubCate.isEmpty()) {
					comboBoxSubCategorie.setEnabled(true);
					for(String st : listSubCate) comboBoxSubCategorie.addItem(st);
					comboBoxGenere.setEnabled(false);
				}else {
					comboBoxSubCategorie.removeAllItems();
					comboBoxSubCategorie.setEnabled(false);
					comboBoxGenere.setEnabled(true);
					comboBoxGenere.removeAllItems();
				}
				
				if(!map.isEmpty()) {
					for(String st : map) comboBoxGenere.addItem(st);
				}
			}
		});

		final Genere gen = new Genere();
		JButton btnNewButton = new JButton("Registrar");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				
				if(!textArea.getText().isEmpty()) {
					String[] hasht = textArea.getText().split(";");
					HashTag hashtag = new HashTag();
					if(hasht.length < 1) {
						if(!textArea.getText().substring(0, 1).equals("#")) {
							JOptionPane.showMessageDialog(null, "Debe empezar por un #","Failed",JOptionPane.ERROR_MESSAGE);
						}else {
							
							

							hashtag.setName(textArea.getText().trim());
							if(comboBoxGenere.isEnabled()) {
								gen.setName((String)comboBoxGenere.getSelectedItem());
								hashtag.setGeneres_id(gen.getIdGenere());
							}
							hashtag.setCategories_id(comboCategorie.getSelectedIndex() + 1);
							if(comboBoxSubCategorie.isEnabled()) {
								hashtag.setSub_categories_id(comboBoxSubCategorie.getSelectedIndex()+1);
							}
							
							
							try {
								hashtag.insert();
								JOptionPane.showMessageDialog(null, "Se ha registrado el HashTag correctamente");
								textArea.setText("");
								comboBoxGenere.setSelectedIndex(0);
								comboCategorie.setSelectedIndex(0);
								comboBoxSubCategorie.removeAllItems();
								comboBoxSubCategorie.setEnabled(false);
							} catch (SQLException e) {
								e.printStackTrace();
							}
						}
					}else {
						for(String h : hasht) {
							if(!h.trim().substring(0,1).equals("#")) {
								System.out.println("El hashtag "+h+"No empieza con #");
							}else {
								hashtag.setName(h.trim());
								if(comboBoxGenere.isEnabled()) {
									gen.setName((String)comboBoxGenere.getSelectedItem());
									hashtag.setGeneres_id(gen.getIdGenere());
								}
								hashtag.setCategories_id(comboCategorie.getSelectedIndex() + 1);
								if(comboBoxSubCategorie.isEnabled()) {
									hashtag.setSub_categories_id(comboBoxSubCategorie.getSelectedIndex()+1);
								}
								
								
								try {
									hashtag.insert();
									
								} catch (SQLException e) {
									e.printStackTrace();
								}
							}
						}
						textArea.setText("");
						comboBoxGenere.setSelectedIndex(0);
						comboCategorie.setSelectedIndex(0);
						comboBoxSubCategorie.removeAllItems();
						comboBoxSubCategorie.setEnabled(false);
						JOptionPane.showMessageDialog(null, "Se han registrado los HashTag correctamente");
					}
					
					
				}else {
					JOptionPane.showMessageDialog(null, "El HashTag no puede estar vacio");
				}
			}
		});
		
		JLabel Categoria = new JLabel("Categoria");
		
		
		for(String st : list) {
			comboCategorie.addItem(st);
		}
		
		
		
		GroupLayout gl_contentPane = new GroupLayout(contentPane);
		gl_contentPane.setHorizontalGroup(
			gl_contentPane.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_contentPane.createSequentialGroup()
					.addContainerGap()
					.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_contentPane.createSequentialGroup()
							.addGroup(gl_contentPane.createParallelGroup(Alignment.TRAILING)
								.addGroup(Alignment.LEADING, gl_contentPane.createSequentialGroup()
									.addComponent(Categoria)
									.addPreferredGap(ComponentPlacement.RELATED, 27, Short.MAX_VALUE)
									.addComponent(comboCategorie, GroupLayout.PREFERRED_SIZE, 197, GroupLayout.PREFERRED_SIZE))
								.addGroup(gl_contentPane.createSequentialGroup()
									.addComponent(lblHashtag, GroupLayout.PREFERRED_SIZE, 53, GroupLayout.PREFERRED_SIZE)
									.addPreferredGap(ComponentPlacement.RELATED, 33, Short.MAX_VALUE)
									.addComponent(textArea, GroupLayout.PREFERRED_SIZE, 197, GroupLayout.PREFERRED_SIZE))
								.addGroup(gl_contentPane.createSequentialGroup()
									.addComponent(lblGenero, GroupLayout.DEFAULT_SIZE, 46, Short.MAX_VALUE)
									.addGap(28)
									.addComponent(comboBoxGenere, GroupLayout.PREFERRED_SIZE, 197, GroupLayout.PREFERRED_SIZE))
								.addGroup(Alignment.LEADING, gl_contentPane.createSequentialGroup()
									.addComponent(lblSubCategoria)
									.addGap(18)
									.addComponent(comboBoxSubCategorie, GroupLayout.PREFERRED_SIZE, 197, GroupLayout.PREFERRED_SIZE)))
							.addGap(64))
						.addGroup(Alignment.TRAILING, gl_contentPane.createSequentialGroup()
							.addComponent(btnNewButton, GroupLayout.PREFERRED_SIZE, 104, GroupLayout.PREFERRED_SIZE)
							.addGap(124))))
		);
		gl_contentPane.setVerticalGroup(
			gl_contentPane.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_contentPane.createSequentialGroup()
					.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING, false)
						.addGroup(gl_contentPane.createSequentialGroup()
							.addGap(28)
							.addComponent(textArea, GroupLayout.PREFERRED_SIZE, 59, GroupLayout.PREFERRED_SIZE)
							.addGap(31))
						.addGroup(Alignment.TRAILING, gl_contentPane.createSequentialGroup()
							.addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
							.addComponent(lblHashtag, GroupLayout.PREFERRED_SIZE, 29, GroupLayout.PREFERRED_SIZE)
							.addGap(42)))
					.addGroup(gl_contentPane.createParallelGroup(Alignment.BASELINE)
						.addComponent(lblGenero, GroupLayout.PREFERRED_SIZE, 27, GroupLayout.PREFERRED_SIZE)
						.addComponent(comboBoxGenere, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addGap(18)
					.addGroup(gl_contentPane.createParallelGroup(Alignment.BASELINE)
						.addComponent(Categoria, GroupLayout.PREFERRED_SIZE, 27, GroupLayout.PREFERRED_SIZE)
						.addComponent(comboCategorie, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addGap(18)
					.addGroup(gl_contentPane.createParallelGroup(Alignment.BASELINE)
						.addComponent(lblSubCategoria, GroupLayout.PREFERRED_SIZE, 27, GroupLayout.PREFERRED_SIZE)
						.addComponent(comboBoxSubCategorie, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addGap(22)
					.addComponent(btnNewButton)
					.addContainerGap())
		);
		contentPane.setLayout(gl_contentPane);
	}
	
}
