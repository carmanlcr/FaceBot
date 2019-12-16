package com.selenium.facebook.Vista;

import java.awt.EventQueue;
import java.sql.SQLException;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import com.selenium.facebook.Modelo.Categories;
import com.selenium.facebook.Modelo.Sub_Categorie;

import javax.swing.GroupLayout;
import javax.swing.ImageIcon;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.JComboBox;
import javax.swing.JButton;
import javax.swing.LayoutStyle.ComponentPlacement;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class RegistrarSubCategorie extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8000729742626321453L;
	private JPanel contentPane;
	private JTextField textField;
	private Categories cate = new Categories();
	/**
	 * Launch the application.
	 */
	public void init() {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					RegistrarSubCategorie frame = new RegistrarSubCategorie();
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
	public RegistrarSubCategorie() throws SQLException {
		setTitle("Registrar Sub Categoria");
		setIconImage(new ImageIcon("C:\\Users\\Usuario\\workspace\\TwitterBot\\src\\main\\java\\com\\selenium\\twitter\\Imagen\\iconotwitter.png").getImage());
		setBounds(100, 100, 372, 242);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		
		JLabel lblSubCategoria = new JLabel("Sub Categoria");
		
		textField = new JTextField();
		textField.setColumns(10);
		
		JLabel lblCategoria = new JLabel("Categoria");
		
		final JComboBox<String> comboBox = new JComboBox<String>();
		List<String> list = cate.getAllActive();
		for(String st : list) comboBox.addItem(st);
		
		JButton btnRegistrar = new JButton("Registrar");
		btnRegistrar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				if(textField.getText().isEmpty()) {
					JOptionPane.showMessageDialog(null, "El campo no puede estar vacio","Error",JOptionPane.ERROR_MESSAGE);
				}else {
					Sub_Categorie subc = new Sub_Categorie();
					subc.setName(textField.getText());
					subc.setCategories_id(comboBox.getSelectedIndex() + 1);
					try {
						subc.insert();
						textField.setText("");
						comboBox.setSelectedIndex(0);
						JOptionPane.showConfirmDialog(null, "Registrada la sub categoria con exito","Exito",JOptionPane.CLOSED_OPTION);
					} catch (SQLException e1) {
						e1.printStackTrace();
					}
				}
				
				
				
			}
		});
		GroupLayout gl_contentPane = new GroupLayout(contentPane);
		gl_contentPane.setHorizontalGroup(
			gl_contentPane.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_contentPane.createSequentialGroup()
					.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_contentPane.createSequentialGroup()
							.addGap(21)
							.addComponent(lblSubCategoria)
							.addGap(55)
							.addComponent(textField, GroupLayout.PREFERRED_SIZE, 134, GroupLayout.PREFERRED_SIZE))
						.addGroup(gl_contentPane.createSequentialGroup()
							.addGap(29)
							.addComponent(lblCategoria)
							.addGap(68)
							.addComponent(comboBox, 0, 134, Short.MAX_VALUE)))
					.addGap(68))
				.addGroup(gl_contentPane.createSequentialGroup()
					.addGap(127)
					.addComponent(btnRegistrar)
					.addContainerGap(142, Short.MAX_VALUE))
		);
		gl_contentPane.setVerticalGroup(
			gl_contentPane.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_contentPane.createSequentialGroup()
					.addGap(44)
					.addGroup(gl_contentPane.createParallelGroup(Alignment.BASELINE)
						.addComponent(lblSubCategoria)
						.addComponent(textField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addGap(37)
					.addGroup(gl_contentPane.createParallelGroup(Alignment.BASELINE)
						.addComponent(lblCategoria)
						.addComponent(comboBox, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addPreferredGap(ComponentPlacement.RELATED, 39, Short.MAX_VALUE)
					.addComponent(btnRegistrar)
					.addContainerGap())
		);
		contentPane.setLayout(gl_contentPane);
	}
}
