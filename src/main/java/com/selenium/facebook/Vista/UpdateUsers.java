package com.selenium.facebook.Vista;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import com.selenium.facebook.Modelo.Categorie;
import com.selenium.facebook.Modelo.User;
import com.selenium.facebook.Modelo.User_Categorie;

import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JScrollPane;
import java.awt.GridLayout;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JButton;
import javax.swing.JCheckBox;

import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.awt.event.ActionEvent;

public class UpdateUsers extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = -977443173967880214L;
	private JPanel contentPane;
	private JScrollPane scrollPane = new JScrollPane();
	private JLabel lblCategoras = new JLabel("Categorías");
	private JButton btnActualizar = new JButton("Actualizar");
	private JPanel panel = new JPanel();
	private JComboBox<String> comboBox = new JComboBox<>();
	private JComboBox<String> comboBox_1 = new JComboBox<>();
	private Categorie cate = new Categorie();
	private List<JCheckBox> listCheck;
	private List<String> listUsersSelected = new ArrayList<>();
	private Map<String, Integer> list;
	/**
	 * Launch the application.
	 */
	public void init() {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					UpdateUsers frame = new UpdateUsers();
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
	public UpdateUsers() {
		setResizable(false);
		setTitle("Actualizar Usuarios De Categorias");
		setBounds(100, 100, 578, 579);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		
		list = cate.getComboBox();
		for(String st : list.keySet()) comboBox.addItem(st);
		for(String sta : list.keySet()) comboBox_1.addItem(sta);
		
		JLabel lblNewLabel = new JLabel("Cambio de Categoria");
		
		btnActualizar.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				for(JCheckBox list : listCheck) {
					if(list.isSelected()) listUsersSelected.add(list.getText());
				}
				
				if(listUsersSelected.isEmpty()) {
					JOptionPane.showMessageDialog(null, "Debe seleccionar al menos un usuario","Failed",JOptionPane.ERROR_MESSAGE);
				}else if(comboBox.getSelectedIndex() == comboBox_1.getSelectedIndex()) {
					JOptionPane.showMessageDialog(null, "Deben ser diferente las categorias","Failed",JOptionPane.ERROR_MESSAGE);
				}else {
					setEnabled(false);
					for(String l : listUsersSelected) {
						User us = new User();
						us.setUsername(l);
						User_Categorie usc = new User_Categorie();
						usc.setCategories_id(Integer.parseInt(list.get(comboBox_1.getSelectedItem().toString()).toString()));
						usc.setUsers_id(us.getIdUser());
						try {
							usc.updateCategories();
						} catch (SQLException e1) {
							System.err.println("Error al guardar el usuario "+l);
						}
					}
					JOptionPane.showMessageDialog(null, "Se hicieron los cambios de categoria");
					setEnabled(true);
					comboBox.setSelectedIndex(0);
					comboBox_1.setSelectedIndex(0);
				}
			}
		});
		
		
		GroupLayout gl_contentPane = new GroupLayout(contentPane);
		gl_contentPane.setHorizontalGroup(
			gl_contentPane.createParallelGroup(Alignment.TRAILING)
				.addGroup(gl_contentPane.createSequentialGroup()
					.addContainerGap()
					.addGroup(gl_contentPane.createParallelGroup(Alignment.TRAILING)
						.addGroup(gl_contentPane.createSequentialGroup()
							.addComponent(scrollPane, GroupLayout.DEFAULT_SIZE, 472, Short.MAX_VALUE)
							.addContainerGap())
						.addGroup(gl_contentPane.createSequentialGroup()
							.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
								.addComponent(lblCategoras)
								.addComponent(lblNewLabel))
							.addGap(94)
							.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
								.addComponent(comboBox_1, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
								.addComponent(comboBox, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
							.addGap(181))))
				.addGroup(gl_contentPane.createSequentialGroup()
					.addContainerGap(215, Short.MAX_VALUE)
					.addComponent(btnActualizar)
					.addGap(198))
		);
		gl_contentPane.setVerticalGroup(
			gl_contentPane.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_contentPane.createSequentialGroup()
					.addGap(52)
					.addComponent(scrollPane, GroupLayout.PREFERRED_SIZE, 293, GroupLayout.PREFERRED_SIZE)
					.addGap(57)
					.addGroup(gl_contentPane.createParallelGroup(Alignment.BASELINE)
						.addComponent(comboBox, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addComponent(lblCategoras))
					.addGap(18)
					.addGroup(gl_contentPane.createParallelGroup(Alignment.BASELINE)
						.addComponent(comboBox_1, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addComponent(lblNewLabel))
					.addGap(18)
					.addComponent(btnActualizar)
					.addGap(30))
		);
		
		
		comboBox.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				panel.removeAll();
				panel.updateUI();
				int idCategoria = Integer.parseInt(list.get(comboBox.getSelectedItem().toString()).toString()) ;
				User us = new User();
				us.setCategories_id(idCategoria);
				try {
					listCheck = new ArrayList<>();
					List<String> list = us.getUserCategories();
					for(String li : list) {
						JCheckBox che = new JCheckBox(li);
						panel.add(che);
						listCheck.add(che);
					} 
					panel.updateUI();
				} catch (SQLException e) {
					e.printStackTrace();
				}
				
				
			}
		});
		
		
		scrollPane.setViewportView(panel);
		panel.setLayout(new GridLayout(0, 2, 0, 0));
		contentPane.setLayout(gl_contentPane);
	}
}
