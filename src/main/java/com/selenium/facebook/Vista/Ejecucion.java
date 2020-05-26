package com.selenium.facebook.Vista;


import java.util.ArrayList;
import java.util.List;

import com.selenium.facebook.Controlador.*;
import com.selenium.facebook.Modelo.*;

import java.sql.SQLException;


import javax.swing.JMenuItem;
import javax.swing.JLabel;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JScrollPane;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JButton;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.ScrollPaneConstants;
import javax.swing.JPopupMenu;

import java.awt.EventQueue;
import java.awt.GridLayout;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.Font;
import java.awt.Color;

import java.io.IOException;

public class Ejecucion extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = -280322591820670686L;
	private JPanel contentPane;
	private int categoria_id = 0;
	private int generes_id = 0;
	private List<String> list = new ArrayList<>();
	private List<String[]> listPost = new ArrayList<>(); 
	private User user = new User();
	private JPanel panelUsuario = new JPanel();
	private List<JLabel> listCheckBoxUsers = new ArrayList<>();
	private List<String> listUsers = new ArrayList<>();
	private int totalUser = 0;
	private Categorie categories = new Categorie();
	private Inicio_Aplicacion inicio;
	private boolean isFanPage;
	private boolean isManual;
	/**
	 * Launch the application.
	 */
	public void inicio() {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Ejecucion frame = new Ejecucion(categoria_id, generes_id, inicio, isFanPage,isManual);
					frame.setTitle(categories.getNameCategories(categoria_id));
					frame.setVisible(true);
					
					
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 * @param id_genere 
	 * @param iniApli 
	 * @param chckbxNewCheckBox2 
	 * @throws SQLException 
	 */
	public Ejecucion(int id, int id_genere, final Inicio_Aplicacion iniApli, final boolean isFanPage,final boolean isManual) throws SQLException {
		setTitle("Validacion");
		inicio = iniApli;
		
		this.categoria_id = id;
		this.generes_id = id_genere;
		this.isFanPage = isFanPage;
		this.isManual = isManual;
		inicio.setCategories_id(categoria_id);
		inicio.setGeneres_id(generes_id);
		setResizable(false);
		setBounds(100, 100, 795, 733);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		scrollPane.setEnabled(false);
		
		//Se crear el boton de empezar y se agrega su ActionListener
		JButton btnEmpezar = new JButton("Empezar");
	
		
		JLabel lblTotal = new JLabel("Total");
		lblTotal.setBackground(new Color(0, 0, 128));
		lblTotal.setFont(new Font("Arial", Font.PLAIN, 12));
		GroupLayout gl_contentPane = new GroupLayout(contentPane);
		gl_contentPane.setHorizontalGroup(
			gl_contentPane.createParallelGroup(Alignment.TRAILING)
				.addGroup(gl_contentPane.createSequentialGroup()
					.addContainerGap()
					.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_contentPane.createSequentialGroup()
							.addComponent(lblTotal)
							.addGap(83))
						.addGroup(gl_contentPane.createSequentialGroup()
							.addComponent(scrollPane, GroupLayout.DEFAULT_SIZE, 759, Short.MAX_VALUE)
							.addContainerGap())
						.addGroup(Alignment.TRAILING, gl_contentPane.createSequentialGroup()
							.addComponent(btnEmpezar, GroupLayout.PREFERRED_SIZE, 95, GroupLayout.PREFERRED_SIZE)
							.addGap(340))))
		);
		gl_contentPane.setVerticalGroup(
			gl_contentPane.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_contentPane.createSequentialGroup()
					.addGap(18)
					.addComponent(lblTotal)
					.addPreferredGap(ComponentPlacement.UNRELATED)
					.addComponent(scrollPane, GroupLayout.PREFERRED_SIZE, 503, GroupLayout.PREFERRED_SIZE)
					.addGap(39)
					.addComponent(btnEmpezar, GroupLayout.PREFERRED_SIZE, 34, GroupLayout.PREFERRED_SIZE)
					.addContainerGap(75, Short.MAX_VALUE))
		);
		
		list = user.getUserCategorie(categoria_id,generes_id,isFanPage);
		if(list.isEmpty()) {
			JOptionPane.showMessageDialog(null, "YA NO HAY TAREAS PARA HOY PARA ESTA CAMPANA");
		}
		scrollPane.setViewportView(panelUsuario);
		

		
		JMenuItem mntmNewMenuItem = new JMenuItem("Desbloquear");
		
		panelUsuario.setLayout(new GridLayout(0, 3, 0, 0));
		
		Post post = new Post();
		listPost = post.getCountPostUsers(categoria_id);
		
		JPopupMenu popMenu = new JPopupMenu();
		popMenu.add(mntmNewMenuItem);
		//Agregar a los usuarios al panel 
		for (String usuarios : list) {
			JLabel chckbxNewCheckBox = new JLabel(usuarios);
			listCheckBoxUsers.add(chckbxNewCheckBox);
			
			//Agregar a los usuarios la cantidad de post en el d�a
			for(String[] pt : listPost) {
				if(pt[0].equals(usuarios)) {
					chckbxNewCheckBox.setText(chckbxNewCheckBox.getText()+" ("+pt[1]+")");
					break;
				}
			}
			
			
			panelUsuario.add(chckbxNewCheckBox);
			totalUser++;
		}
		
		lblTotal.setText(lblTotal.getText()+": "+totalUser);

		contentPane.setLayout(gl_contentPane);
		
		//Se empieza el proceso de post
		if(isManual) {
			btnEmpezar.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent arg0) {
					
					for (JLabel checkbox : listCheckBoxUsers) {
						String text = "";
						for (int i = 0; i < checkbox.getText().length(); i++) {
							if(!checkbox.getText().substring(i,i+1).equals("(")) {
								text += checkbox.getText().substring(i,i+1);
								
							}
							if(checkbox.getText().substring(i,i+1).equals("(")) {
								break;
							}
						}
						listUsers.add(text.trim());
					}
					if(listCheckBoxUsers.isEmpty()) {
						JOptionPane.showMessageDialog(null, "NO HAY USUARIOS PARA ESTA CAMPAÑA PARA PUBLICAR");
					}else {
						iniApli.setCategories_id(categoria_id);
						iniApli.setGeneres_id(generes_id);
						iniApli.insert();
						InicioController init = new InicioController(categoria_id,listUsers,isFanPage);
						setExtendedState(ICONIFIED);
						try {
							init.init();
						} catch (SQLException |IOException | InterruptedException e) {
							e.printStackTrace();
						} 	
					}
					
				}
			});
		}else {
			
			for (JLabel checkbox : listCheckBoxUsers) {
				String text = "";
				for (int i = 0; i < checkbox.getText().length(); i++) {
					if(!checkbox.getText().substring(i,i+1).equals("(")) {
						text += checkbox.getText().substring(i,i+1);
						
					}
					if(checkbox.getText().substring(i,i+1).equals("(")) {
						break;
					}
				}
				listUsers.add(text.trim());
			}
			
			if(listCheckBoxUsers.isEmpty()) {
				JOptionPane.showMessageDialog(null, "NO HAY USUARIOS PARA ESTA CAMPAÑA PARA PUBLICAR");
			}else {
				iniApli.setCategories_id(categoria_id);
				iniApli.setGeneres_id(generes_id);
				iniApli.insert();
				InicioController init = new InicioController(categoria_id,listUsers,isFanPage);
				setExtendedState(ICONIFIED);
				try {
					init.init();
				} catch (SQLException |IOException | InterruptedException e) {
					e.printStackTrace();
				} 	
			}
			
		}
	}
	
}
