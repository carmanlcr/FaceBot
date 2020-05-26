package com.selenium.facebook.Vista;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.filechooser.FileNameExtensionFilter;

import com.selenium.facebook.Modelo.Categorie;
import com.selenium.facebook.Modelo.Genere;
import com.selenium.facebook.Modelo.Path_Photo;
import com.selenium.facebook.Modelo.Sub_Categorie;

import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JFileChooser;
import javax.swing.JButton;
import javax.swing.LayoutStyle.ComponentPlacement;
import java.awt.event.ActionListener;
import java.io.File;
import java.sql.SQLException;
import java.util.List;
import java.awt.event.ActionEvent;
import javax.swing.JTextField;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

/**
 * Las frases yano se guardan de forma manual, se generan en el aplicativo web por tareas
 * 
 * @author Luis Morales
 * @deprecated
 */
public class RegistrarDireccionFotos extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4838763825545240347L;
	private JPanel contentPane;
	private JTextField textField;
	private Categorie cate = new Categorie();
	private List<String> listCategories = cate.getAllActive();
	private Genere gene = new Genere();
	private Sub_Categorie subc = new Sub_Categorie();
	private List<String> listSubCateg = subc.getSubCategories();
	private JComboBox<String> comboBox = new JComboBox<String>();
	private JComboBox<String> comboBox_1 = new JComboBox<String>();
	private JComboBox<String> comboBox_2 = new JComboBox<String>();
	/**
	 * Launch the application.
	 */
	public void inicio() {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					RegistrarDireccionFotos frame = new RegistrarDireccionFotos();
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
	public RegistrarDireccionFotos() {
		setResizable(false);
		setTitle("Registrar Direccion de Fotos");
		setBounds(100, 100, 408, 368);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		final Genere gen = new Genere();
		
		JButton btnRegistrar = new JButton("Registrar");
		btnRegistrar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if(textField.getText().equals("")){
					JOptionPane.showMessageDialog(null, "Debe buscar la dirección de la foto","Alert",JOptionPane.ERROR_MESSAGE);
				}else {
					Path_Photo path = new Path_Photo();
					String patha = "";
					String[] paths = textField.getText().split(";");
					for(int i = 0; i < paths.length; i++) {
						for(int j = 0; j < paths[i].length(); j++) {
							if(paths[i].substring(j, j+1).equals("\\")) {
								patha += "\\\\";
							}else {
								patha += paths[i].substring(j, j+1);
							}
						}
						path.setPath(patha);
						gen.setName((String)comboBox.getSelectedItem());
						path.setGeneres_id(gen.getIdGenere());
						path.setCategories_id(comboBox_1.getSelectedIndex()+1);
						path.setSub_categories_id(comboBox_2.getSelectedIndex()+1);
						try {
							path.insert();
							patha = "";
						} catch (SQLException e) {
							e.printStackTrace();
						}
					}

					
					JOptionPane.showMessageDialog(null, "Se registro la dirección de las fotos con exito","Extio",JOptionPane.INFORMATION_MESSAGE);
					textField.setText("");

					
				}
			}
		});
		
		JLabel lblCategorias = new JLabel("Genero");
		JLabel lblNewLabel = new JLabel("Categoria");
		JLabel lblGenero = new JLabel("Sub Categoria");
		
		
		
		
		comboBox_1 = setComboBoxCategories(listCategories, comboBox_1);
		
		comboBox_2 = setComboBoxSubCategories(listSubCateg, comboBox_2);
		
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
					comboBox.setEnabled(true);
					for(String st : listGenere) comboBox.addItem(st);
				}else {
					comboBox.removeAllItems();
					comboBox.setEnabled(false);
				}
			}
		});
		
		
		JButton btnNewButton = new JButton("Abrir");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				JFileChooser jf = new JFileChooser();
				FileNameExtensionFilter filtroImagen=new FileNameExtensionFilter("JPG, PNG & GIF","jpg","png","gif");
				jf.setFileFilter(filtroImagen);
				jf.setFileSelectionMode(JFileChooser.FILES_ONLY);
				jf.setMultiSelectionEnabled(true);
				int respuesta = jf.showOpenDialog(contentPane);
				if(respuesta == JFileChooser.APPROVE_OPTION) {
					
					File[] archivoElegido = jf.getSelectedFiles();
					String paths = "";
					for(File fi: archivoElegido) {
						paths += fi.getPath() + ";";
					}
					textField.setText(paths);
				}
			}
		});
		
		textField = new JTextField();
		textField.setEditable(false);
		textField.setColumns(10);
		
		
		GroupLayout gl_contentPane = new GroupLayout(contentPane);
		gl_contentPane.setHorizontalGroup(
			gl_contentPane.createParallelGroup(Alignment.TRAILING)
				.addGroup(gl_contentPane.createSequentialGroup()
					.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_contentPane.createSequentialGroup()
							.addGap(22)
							.addComponent(textField, GroupLayout.DEFAULT_SIZE, 229, Short.MAX_VALUE)
							.addGap(18)
							.addComponent(btnNewButton))
						.addGroup(gl_contentPane.createSequentialGroup()
							.addGroup(gl_contentPane.createParallelGroup(Alignment.TRAILING, false)
								.addGroup(Alignment.LEADING, gl_contentPane.createSequentialGroup()
									.addGap(39)
									.addComponent(lblGenero, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
								.addGroup(Alignment.LEADING, gl_contentPane.createSequentialGroup()
									.addGap(31)
									.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
										.addComponent(lblCategorias)
										.addComponent(lblNewLabel))))
							.addGap(38)
							.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
								.addComponent(comboBox_2, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
								.addComponent(comboBox_1, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
								.addComponent(comboBox, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)))
						.addGroup(gl_contentPane.createSequentialGroup()
							.addGap(122)
							.addComponent(btnRegistrar)))
					.addContainerGap())
		);
		gl_contentPane.setVerticalGroup(
			gl_contentPane.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_contentPane.createSequentialGroup()
					.addGap(61)
					.addGroup(gl_contentPane.createParallelGroup(Alignment.BASELINE)
						.addComponent(btnNewButton)
						.addComponent(textField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addGap(29)
					.addGroup(gl_contentPane.createParallelGroup(Alignment.BASELINE)
						.addComponent(lblCategorias)
						.addComponent(comboBox, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addGap(34)
					.addGroup(gl_contentPane.createParallelGroup(Alignment.BASELINE)
						.addComponent(lblNewLabel)
						.addComponent(comboBox_1, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addGap(37)
					.addGroup(gl_contentPane.createParallelGroup(Alignment.BASELINE)
						.addComponent(lblGenero)
						.addComponent(comboBox_2, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addPreferredGap(ComponentPlacement.RELATED, 30, Short.MAX_VALUE)
					.addComponent(btnRegistrar)
					.addGap(23))
		);
		contentPane.setLayout(gl_contentPane);
	}
	
	
	private JComboBox<String> setComboBoxCategories(List<String> map, JComboBox<String> comboBox) {
		comboBox = new JComboBox<String>();
	
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
