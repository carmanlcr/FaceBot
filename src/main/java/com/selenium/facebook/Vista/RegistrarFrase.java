package com.selenium.facebook.Vista;

import java.awt.EventQueue;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import com.selenium.facebook.Modelo.*;

import javax.swing.GroupLayout;
import javax.swing.ImageIcon;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextPane;
import java.awt.Font;
import javax.swing.JComboBox;
import javax.swing.JButton;
import javax.swing.LayoutStyle.ComponentPlacement;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.awt.event.ActionEvent;
import javax.swing.event.PopupMenuListener;
import javax.swing.event.PopupMenuEvent;

public class RegistrarFrase extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5738897748169831720L;
	private JPanel contentPane;
	private Categories cate = new Categories();
	private List<String> listCategories = cate.getAllActive();
	private Genere gene = new Genere();
	private Sub_Categorie subc = new Sub_Categorie();
	private List<String> listSubCateg = subc.getSubCategories();
	private JComboBox<String> comboBox = new JComboBox<String>();
	private JComboBox<String> comboBox_1 = new JComboBox<String>();
	private JComboBox<String> comboBox_2 = new JComboBox<String>();
	private JButton btnRegistrar = new JButton("Registrar");
	/**
	 * Launch the application.
	 */
	public void inicio() {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					RegistrarFrase frame = new RegistrarFrase();
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
	public RegistrarFrase() {
		setResizable(false);
		setTitle("Registrar Frase");
		setIconImage(new ImageIcon("C:\\Users\\Usuario\\workspace\\TwitterBot\\src\\main\\java\\com\\selenium\\twitter\\Imagen\\iconotwitter.png").getImage());
		setBounds(100, 100, 542, 429);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		
		JLabel lblFrase = new JLabel("Frase");
		
		final JTextPane textPane = new JTextPane();
		textPane.setFont(new Font("Arial", Font.PLAIN, 11));
		
		
		
		comboBox_1 = setComboBoxCategories(listCategories, comboBox_1);
		
		comboBox_2 = setComboBoxSubCategories(listSubCateg, comboBox_2);
		JLabel lblGenero = new JLabel("Genero");
		
		JLabel lblCategoria = new JLabel("Categoria");
		
		JLabel lblSubCategoria = new JLabel("Sub Categoria");
		final Genere gen = new Genere();
		
		btnRegistrar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Phrases frase = new Phrases();
				if(!textPane.getText().isEmpty()) {
					frase.setPhrase(textPane.getText());
					gen.setName((String)comboBox.getSelectedItem());
					frase.setCategories_id(comboBox_1.getSelectedIndex() + 1);
					if(comboBox.isEnabled()) {
						frase.setGeneres_id(gen.getIdGenere());
					}
					if(comboBox_2.isEnabled()) {
						frase.setSub_categories_id(comboBox_2.getSelectedIndex()+1);
					}
					
					try {
						frase.insert();
						JOptionPane.showMessageDialog(null, "Se ha registrado la frase correctamente","Success",JOptionPane.INFORMATION_MESSAGE);
						textPane.setText("");
						comboBox_1.setSelectedIndex(0);
						comboBox_1.setSelectedIndex(0);
						comboBox_2.removeAllItems();
						comboBox_2.setEnabled(false);
						frase = null;
					} catch (SQLException e1) {
						e1.printStackTrace();
					}
				}else {
					JOptionPane.showMessageDialog(null, "Tiene que escribir una frase","Failed",JOptionPane.ERROR_MESSAGE);
				}
				
			}
		});
		
		comboBox_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				comboBox_2.removeAllItems();
				Sub_Categorie subCate = new Sub_Categorie();
				subCate.setCategories_id(comboBox_1.getSelectedIndex()+1);
				List<String> listSubCate = subCate.getSubCategories();
				gene.setCategories_id(comboBox_1.getSelectedIndex()+1);
				List<String> listGenere = gene.getGeneresActive();
				if(!listSubCate.isEmpty()) {
					comboBox_2.setEnabled(true);
					for(String st : listSubCate) comboBox_2.addItem(st);
					comboBox.setEnabled(false);
				}else {
					comboBox_2.removeAllItems();
					comboBox_2.setEnabled(false);
					comboBox.setEnabled(true);
					comboBox.removeAllItems();
				}
				
				if(!listGenere.isEmpty()) {
					for(String st : listGenere) comboBox.addItem(st);
				}
			}
		});
		GroupLayout gl_contentPane = new GroupLayout(contentPane);
		gl_contentPane.setHorizontalGroup(
			gl_contentPane.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_contentPane.createSequentialGroup()
					.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_contentPane.createSequentialGroup()
							.addGap(192)
							.addComponent(lblFrase))
						.addGroup(gl_contentPane.createSequentialGroup()
							.addGap(49)
							.addComponent(textPane, GroupLayout.PREFERRED_SIZE, 324, GroupLayout.PREFERRED_SIZE))
						.addGroup(gl_contentPane.createSequentialGroup()
							.addGap(60)
							.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
								.addComponent(lblGenero)
								.addComponent(lblCategoria)
								.addComponent(lblSubCategoria))
							.addGap(50)
							.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
								.addComponent(comboBox_1, GroupLayout.PREFERRED_SIZE, 230, GroupLayout.PREFERRED_SIZE)
								.addComponent(comboBox, GroupLayout.PREFERRED_SIZE, 230, GroupLayout.PREFERRED_SIZE)
								.addComponent(comboBox_2, GroupLayout.PREFERRED_SIZE, 230, GroupLayout.PREFERRED_SIZE))))
					.addContainerGap(72, Short.MAX_VALUE))
				.addGroup(Alignment.TRAILING, gl_contentPane.createSequentialGroup()
					.addContainerGap(185, Short.MAX_VALUE)
					.addComponent(btnRegistrar)
					.addGap(183))
		);
		gl_contentPane.setVerticalGroup(
			gl_contentPane.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_contentPane.createSequentialGroup()
					.addGap(20)
					.addComponent(lblFrase)
					.addGap(25)
					.addComponent(textPane, GroupLayout.PREFERRED_SIZE, 104, GroupLayout.PREFERRED_SIZE)
					.addGap(40)
					.addGroup(gl_contentPane.createParallelGroup(Alignment.BASELINE)
						.addComponent(comboBox, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addComponent(lblGenero))
					.addGap(40)
					.addGroup(gl_contentPane.createParallelGroup(Alignment.BASELINE)
						.addComponent(comboBox_1, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addComponent(lblCategoria))
					.addGap(40)
					.addGroup(gl_contentPane.createParallelGroup(Alignment.BASELINE)
						.addComponent(comboBox_2, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addComponent(lblSubCategoria))
					.addPreferredGap(ComponentPlacement.RELATED, 27, Short.MAX_VALUE)
					.addComponent(btnRegistrar)
					.addGap(21))
		);
		contentPane.setLayout(gl_contentPane);
	}
	
	
	private JComboBox<String> setComboBoxCategories(List<String> map, JComboBox<String> comboBox) {
		comboBox = new JComboBox<String>();
		comboBox.addPopupMenuListener(new PopupMenuListener() {
			public void popupMenuCanceled(PopupMenuEvent arg0) {
			}
			public void popupMenuWillBecomeInvisible(PopupMenuEvent arg0) {
			}
			public void popupMenuWillBecomeVisible(PopupMenuEvent arg0) {
			}
		});
	
		for (String string : map) {
			comboBox.addItem(string);
		}
	    return comboBox;
	}
	
	private JComboBox<String> setComboBoxSubCategories(List<String> map, JComboBox<String> comboBox) {
		comboBox = new JComboBox<String>();
		
		for (String string : map) {
			comboBox.addItem(string);
		}
	    return comboBox;
	}
	

}
