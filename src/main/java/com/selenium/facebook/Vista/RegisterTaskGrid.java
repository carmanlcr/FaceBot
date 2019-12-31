package com.selenium.facebook.Vista;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.GroupLayout;
import javax.swing.JCheckBox;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JScrollPane;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JComboBox;
import java.awt.Font;
import javax.swing.LayoutStyle.ComponentPlacement;

import com.selenium.facebook.Modelo.Categorie;
import com.selenium.facebook.Modelo.Genere;
import com.selenium.facebook.Modelo.Task_Grid;
import com.selenium.facebook.Modelo.User;

import javax.swing.JPanel;
import javax.swing.ScrollPaneConstants;
import java.awt.GridLayout;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.awt.event.ActionEvent;
import com.toedter.calendar.JDateChooser;
import javax.swing.JButton;
import javax.swing.border.TitledBorder;

/**
 * 
 * @author Luis Morales
 *
 */
public class RegisterTaskGrid {

	private JFrame frmRegistrarParrillaDe;
	private JComboBox<String> comboBox_Categoria = new JComboBox<String>();
	private JComboBox<String> comboBox_Genero = new JComboBox<String>();
	private JDateChooser dateChooser = new JDateChooser();
	private List<JCheckBox> listCheck;
	private List<String> listCheckUserSelected;
	private HashMap<String, Integer> mapCategorie;
	private HashMap<String, Integer> mapGenere;
	private int idCategoria = 0;
	private int idGenere = 0;
	/**
	 * Launch the application.
	 */
	public void init() {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					RegisterTaskGrid window = new RegisterTaskGrid();
					window.frmRegistrarParrillaDe.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public RegisterTaskGrid() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		Categorie cate = new Categorie();
		mapCategorie = cate.getComboBox();
		frmRegistrarParrillaDe = new JFrame();
		frmRegistrarParrillaDe.setResizable(false);
		frmRegistrarParrillaDe.setTitle("Registrar Parrilla de Tareas");
		frmRegistrarParrillaDe.setBounds(100, 100, 830, 536);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		
		JLabel lblNewLabel = new JLabel("Categoria");
		lblNewLabel.setFont(new Font("Arial", Font.BOLD, 11));
		
		
		
		JLabel lblGenero = new JLabel("Genero");
		lblGenero.setFont(new Font("Arial", Font.BOLD, 11));
		
		for(String st : mapCategorie.keySet()) {
			comboBox_Categoria.addItem(st);
		}
		
		
		
		JLabel lblUsuarios = new JLabel("Usuarios");
		lblUsuarios.setFont(new Font("Arial", Font.BOLD, 11));
		
		
		
		JLabel lblFechaDePublicacion = new JLabel("Fecha De Publicacion");
		lblFechaDePublicacion.setFont(new Font("Arial", Font.BOLD, 11));
		
		final JCheckBox check_FanPage = new JCheckBox("Fan Page");
		check_FanPage.setFont(new Font("Arial", Font.BOLD, 11));
		
		final JCheckBox check_Groups = new JCheckBox("Grupos");
		check_Groups.setFont(new Font("Arial", Font.BOLD, 11));
		
		JButton btnNewButton = new JButton("Registrar");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				
				listCheckUserSelected = new ArrayList<String>();
				for(JCheckBox st : listCheck) {
					
					if(st.isSelected()) listCheckUserSelected.add(st.getText());
				}
				
				if(listCheckUserSelected.size() < 1) {
					JOptionPane.showMessageDialog(null, "Debe seleccionar al menos un usuario","Failed",JOptionPane.ERROR_MESSAGE);
				}else if(idGenere == 0) {
					JOptionPane.showMessageDialog(null, "Debe seleccionar un genero","Failed",JOptionPane.ERROR_MESSAGE);
				}else if(dateChooser.getDate() == null) {
					JOptionPane.showMessageDialog(null, "Debe seleccionar la fecha de publicacion","Failed",JOptionPane.ERROR_MESSAGE);
				}else if(!check_FanPage.isSelected() && !check_Groups.isSelected()) {
					JOptionPane.showMessageDialog(null, "Debe seleccionar la fecha de publicacion","Failed",JOptionPane.ERROR_MESSAGE);
				}else {
					SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
					
					Task_Grid task_G = new Task_Grid();
					task_G.setGeneres_id(idGenere);
					task_G.setDate_publication(dateFormat.format(dateChooser.getDate()));
					if(check_FanPage.isSelected()) {
						task_G.setFan_page_publication(true);
					}else {
						task_G.setFan_page_publication(false);
					}
					if(check_Groups.isSelected()) {
						task_G.setGroups_publication(true);
					}else {
						task_G.setGroups_publication(false);
					}
					
					for(String st : listCheckUserSelected) {
						User user = new User();
						user.setUsername(st);
						
						int id= user.getIdUser();
						
						task_G.setUsers_id(id);
						
						try {
							task_G.insert();
						} catch (SQLException e) {
							e.printStackTrace();
						}
						
					}
					
					check_FanPage.setSelected(false);
					check_Groups.setSelected(false);
					comboBox_Categoria.setSelectedIndex(0);
					JOptionPane.showMessageDialog(null, "La tarea se registro con exito","Success",JOptionPane.INFORMATION_MESSAGE);
				}
			}
		});
		btnNewButton.setFont(new Font("Arial", Font.BOLD, 11));
		
		
		GroupLayout groupLayout = new GroupLayout(frmRegistrarParrillaDe.getContentPane());
		groupLayout.setHorizontalGroup(
			groupLayout.createParallelGroup(Alignment.TRAILING)
				.addGroup(groupLayout.createSequentialGroup()
					.addGap(52)
					.addComponent(scrollPane, GroupLayout.PREFERRED_SIZE, 711, GroupLayout.PREFERRED_SIZE)
					.addContainerGap(51, Short.MAX_VALUE))
				.addGroup(groupLayout.createSequentialGroup()
					.addGap(73)
					.addComponent(lblNewLabel)
					.addGap(18)
					.addComponent(comboBox_Categoria, GroupLayout.PREFERRED_SIZE, 189, GroupLayout.PREFERRED_SIZE)
					.addGap(36)
					.addGroup(groupLayout.createParallelGroup(Alignment.TRAILING)
						.addGroup(groupLayout.createSequentialGroup()
							.addComponent(lblGenero, GroupLayout.PREFERRED_SIZE, 53, GroupLayout.PREFERRED_SIZE)
							.addGap(39)
							.addComponent(comboBox_Genero, GroupLayout.PREFERRED_SIZE, 189, GroupLayout.PREFERRED_SIZE)
							.addGap(99))
						.addGroup(groupLayout.createSequentialGroup()
							.addComponent(lblUsuarios, GroupLayout.PREFERRED_SIZE, 84, GroupLayout.PREFERRED_SIZE)
							.addContainerGap(361, Short.MAX_VALUE))))
				.addGroup(groupLayout.createSequentialGroup()
					.addGap(70)
					.addComponent(lblFechaDePublicacion, GroupLayout.PREFERRED_SIZE, 126, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.UNRELATED)
					.addComponent(dateChooser, GroupLayout.PREFERRED_SIZE, 134, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.RELATED, 277, Short.MAX_VALUE)
					.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
						.addComponent(check_Groups, GroupLayout.PREFERRED_SIZE, 75, GroupLayout.PREFERRED_SIZE)
						.addComponent(check_FanPage))
					.addGap(122))
				.addGroup(groupLayout.createSequentialGroup()
					.addContainerGap(380, Short.MAX_VALUE)
					.addComponent(btnNewButton)
					.addGap(361))
		);
		groupLayout.setVerticalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(groupLayout.createSequentialGroup()
					.addGap(34)
					.addGroup(groupLayout.createParallelGroup(Alignment.BASELINE)
						.addComponent(lblNewLabel)
						.addComponent(comboBox_Categoria, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addComponent(comboBox_Genero, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addComponent(lblGenero))
					.addGap(18)
					.addComponent(lblUsuarios, GroupLayout.PREFERRED_SIZE, 19, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(scrollPane, GroupLayout.PREFERRED_SIZE, 251, GroupLayout.PREFERRED_SIZE)
					.addGap(26)
					.addGroup(groupLayout.createParallelGroup(Alignment.TRAILING)
						.addComponent(lblFechaDePublicacion)
						.addComponent(dateChooser, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addComponent(check_FanPage))
					.addPreferredGap(ComponentPlacement.UNRELATED)
					.addComponent(check_Groups)
					.addPreferredGap(ComponentPlacement.RELATED, 31, Short.MAX_VALUE)
					.addComponent(btnNewButton)
					.addGap(25))
		);
		groupLayout.setHonorsVisibility(false);
		dateChooser.setBorder(new TitledBorder(null, "", TitledBorder.CENTER, TitledBorder.TOP, null, null));
		dateChooser.setDateFormatString("yyyy/MM/dd");
	    
		
		
		final JPanel panel_Usuarios = new JPanel();
		scrollPane.setViewportView(panel_Usuarios);
		panel_Usuarios.setLayout(new GridLayout(0, 3, 0, 0));
		frmRegistrarParrillaDe.getContentPane().setLayout(groupLayout);
		
		comboBox_Categoria.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				comboBox_Genero.removeAllItems();
				idCategoria = Integer.parseInt(mapCategorie.get(comboBox_Categoria.getSelectedItem().toString()).toString());
				Genere gene = new Genere();
				gene.setCategories_id(idCategoria);
				mapGenere = gene.getGeneresForCategorieActive();
				if(mapGenere.size() > 0) {
					for(String st : mapGenere.keySet()) comboBox_Genero.addItem(st);
					idGenere = Integer.parseInt(mapGenere.get(comboBox_Genero.getSelectedItem().toString()).toString());
				}else {
					idGenere = 0;
				}
				
				panel_Usuarios.removeAll();
				panel_Usuarios.updateUI();
				User us = new User();
				us.setCategories_id(idCategoria);
				try {
					listCheck = new ArrayList<JCheckBox>();
					List<String> list = us.getUserCategories();
					for(String li : list) {
						JCheckBox che = new JCheckBox(li);
						panel_Usuarios.add(che);
						listCheck.add(che);
					} 
					panel_Usuarios.updateUI();
				} catch (SQLException e) {
					e.printStackTrace();
				}
				
			}
		});
		comboBox_Categoria.setSelectedIndex(0);
	
	}
}
