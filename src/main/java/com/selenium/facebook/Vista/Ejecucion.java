package com.selenium.facebook.Vista;

import java.awt.EventQueue;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import com.selenium.facebook.Controlador.*;
import com.selenium.facebook.Modelo.*;

import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import java.awt.GridLayout;
import java.sql.SQLException;
import javax.swing.JButton;
import javax.swing.LayoutStyle.ComponentPlacement;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.Font;
import javax.swing.ScrollPaneConstants;
import javax.swing.JPopupMenu;

import java.awt.Component;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;

import javax.swing.JMenuItem;
import javax.swing.JLabel;

import java.awt.AWTException;
import java.awt.Color;

public class Ejecucion extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = -280322591820670686L;
	private JPanel contentPane;
	private int categoria_id = 0;
	private List<String[]> list = new ArrayList<String[]>();
	private List<String[]> listPost = new ArrayList<String[]>(); 
	private User user = new User();
	private JPanel panelUsuario = new JPanel();
	private List<JCheckBox> listCheckBoxUsers = new ArrayList<JCheckBox>();
	private List<List<JCheckBox>> listCheckBoxHashTah = new ArrayList<List<JCheckBox>>();
	private List<List<String>> listChechBoxSelected = new ArrayList<List<String>>();
	private List<String> listCheckBoxhashTagSelected = new ArrayList<String>();
	private List<JCheckBox> listCheckBoxUsersSend = new ArrayList<JCheckBox>();
	private List<JTextArea> listTextARea = new ArrayList<JTextArea>();
	private List<JTextField> listTextFieldUser = new ArrayList<JTextField>();
	private List<JComboBox<String>> listJComboBoxGenere = new ArrayList<JComboBox<String>>();
	private JPanel panelPie = new JPanel();
	private int indice = 1;
	public static int lengthString = 0;
	private static JCheckBox check ;
	private int totalUser = 0;
	private Categorie categories = new Categorie();
	private Inicio_Aplicacion inicio;
	
	/**
	 * Launch the application.
	 */
	public void inicio() {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Ejecucion frame = new Ejecucion(categoria_id, inicio);
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
	 * @param iniApli 
	 * @throws SQLException 
	 */
	public Ejecucion(int id, Inicio_Aplicacion iniApli) throws SQLException {
		setTitle("Validacion");
		inicio = iniApli;
		
		this.categoria_id = id;
		inicio.setCategories_id(categoria_id);
		setResizable(false);
		setBounds(100, 100, 744, 733);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		scrollPane.setEnabled(false);
		
		//Se crear el boton de empezar y se agrega su ActionListener
		JButton btnEmpezar = new JButton("Empezar");
		
		//Se empieza el proceso de post
		btnEmpezar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				
				for (JCheckBox checkbox : listCheckBoxUsers) {
					if(checkbox.isSelected()) {
						String text = "";
						for (int i = 0; i < checkbox.getText().length(); i++) {
							if(!checkbox.getText().substring(i,i+1).equals("(")) {
								text += checkbox.getText().substring(i,i+1);
								
							}
							if(checkbox.getText().substring(i,i+1).equals("(")) {
								break;
							}
						}
						checkbox.setText(text);
						listCheckBoxUsersSend.add(checkbox);
					}
				}
				
				
				for(List<JCheckBox> lis : listCheckBoxHashTah) {
					listCheckBoxhashTagSelected = new ArrayList<String>();
					for(JCheckBox ch : lis) if(ch.isSelected()) listCheckBoxhashTagSelected.add(ch.getText());
					if(!listCheckBoxhashTagSelected.isEmpty()) {
						listChechBoxSelected.add(listCheckBoxhashTagSelected);
					}
				}
				if(listCheckBoxUsersSend.size() == 0) {
					JOptionPane.showMessageDialog(null,"Debe seleccionar al menos un usuario");
				} else if(listTextARea.size() == 0) {
					JOptionPane.showMessageDialog(null,"Debe escribir al menos un pie para la foto");
				}else {
					for(JComboBox<String> st : listJComboBoxGenere) {
						Genere gene = new Genere();
						gene.setName(st.getSelectedItem().toString());
						inicio.setGeneres_id(gene.getIdGenere());
						inicio.insert();
					}
					InicioController init = new InicioController(categoria_id,listCheckBoxUsersSend, listTextARea,listChechBoxSelected,listTextFieldUser,listJComboBoxGenere);
					setExtendedState(ICONIFIED);
					try {
						init.init();
					} catch (SQLException  e) {
						e.printStackTrace();
					} catch (InterruptedException e) {
						e.printStackTrace();
					} catch (AWTException e) {
						e.printStackTrace();
					} catch (IOException e) {
						e.printStackTrace();
					}
					
				}
				
				
				
			}
		});
		
		JScrollPane scrollPane_1 = new JScrollPane();
		scrollPane_1.setEnabled(false);
		
		Genere genero = new Genere();
		//Crear los pies de foto de manera dinamica
		JButton btnAgregar = new JButton("Agregar");
		genero.setCategories_id(categoria_id);
		final List<String> map = genero.getGeneresForCategories(); 
		btnAgregar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				JTextArea textA = new JTextArea("Pie De Foto: "+indice);
				final JComboBox<String> combo = new JComboBox<String>();
				final JPanel panel = new JPanel();
				final HashTag hash = new HashTag();
				JScrollPane scrollPane = new JScrollPane();
				JTextField textF = new JTextField();
				final Genere ge = new Genere();
				scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
				scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
				panel.setEnabled(false);
				panel.setLayout(new GridLayout(0, 4, 0, 0));
				scrollPane.setViewportView(panel);
				textF.setText("Usuario");
				final List<JCheckBox>  listaHashTag = new ArrayList<JCheckBox>();
				
				combo.addActionListener(new ActionListener() {
					
					public void actionPerformed(ActionEvent e) {
						hash.setCategories_id(categoria_id);
						ge.setCategories_id(categoria_id);
						ge.setName((String) combo.getSelectedItem());
						hash.setGeneres_id(ge.getIdGenere());
						listCheckBoxHashTah.remove(listaHashTag);
						listaHashTag.clear();
						List<String> lista = hash.getHashTagForCategories();
						panel.removeAll();
						for(String st : lista) {
							
							JCheckBox ch = new JCheckBox(st);
							listaHashTag.add(ch);
							panel.add(ch);
						}
						panelPie.updateUI();
						listCheckBoxHashTah.add(listaHashTag);

					}
				});
				for (String st : map) {
					combo.addItem(st);
				}
				panel.add(combo);
				
				JSeparator separator = new JSeparator();
				separator.setEnabled(false);
				separator.setBackground(new Color(0, 255, 0));
				
				listJComboBoxGenere.add(combo);
				listTextARea.add(textA);
				listTextFieldUser.add(textF);
				panelPie.add(textA);
				panelPie.add(textF);
				panelPie.add(combo);
				panelPie.add(panel);
				panelPie.add(separator);
				panelPie.updateUI();
				indice++;
				
			}
		});
		
		final JCheckBox chckbxNewCheckBox_1 = new JCheckBox("Seleccionar Todo");
		chckbxNewCheckBox_1.setFont(new Font("Arial", Font.PLAIN, 12));
		
		JLabel lblTotal = new JLabel("Total");
		lblTotal.setBackground(new Color(0, 0, 128));
		lblTotal.setFont(new Font("Arial", Font.PLAIN, 12));
		GroupLayout gl_contentPane = new GroupLayout(contentPane);
		gl_contentPane.setHorizontalGroup(
			gl_contentPane.createParallelGroup(Alignment.TRAILING)
				.addGroup(gl_contentPane.createSequentialGroup()
					.addContainerGap(469, Short.MAX_VALUE)
					.addComponent(btnAgregar)
					.addGap(109))
				.addGroup(gl_contentPane.createSequentialGroup()
					.addGap(25)
					.addComponent(chckbxNewCheckBox_1)
					.addPreferredGap(ComponentPlacement.RELATED, 380, Short.MAX_VALUE)
					.addComponent(lblTotal)
					.addGap(83))
				.addGroup(gl_contentPane.createSequentialGroup()
					.addContainerGap(302, Short.MAX_VALUE)
					.addComponent(btnEmpezar)
					.addGap(274))
				.addGroup(Alignment.LEADING, gl_contentPane.createSequentialGroup()
					.addContainerGap()
					.addComponent(scrollPane, GroupLayout.DEFAULT_SIZE, 708, Short.MAX_VALUE)
					.addContainerGap())
				.addGroup(gl_contentPane.createSequentialGroup()
					.addContainerGap()
					.addComponent(scrollPane_1, GroupLayout.DEFAULT_SIZE, 708, Short.MAX_VALUE)
					.addContainerGap())
		);
		gl_contentPane.setVerticalGroup(
			gl_contentPane.createParallelGroup(Alignment.TRAILING)
				.addGroup(gl_contentPane.createSequentialGroup()
					.addGap(18)
					.addGroup(gl_contentPane.createParallelGroup(Alignment.BASELINE)
						.addComponent(chckbxNewCheckBox_1)
						.addComponent(lblTotal))
					.addPreferredGap(ComponentPlacement.UNRELATED)
					.addComponent(scrollPane, GroupLayout.PREFERRED_SIZE, 254, GroupLayout.PREFERRED_SIZE)
					.addGap(37)
					.addComponent(btnAgregar)
					.addGap(18)
					.addComponent(scrollPane_1, GroupLayout.DEFAULT_SIZE, 275, Short.MAX_VALUE)
					.addPreferredGap(ComponentPlacement.UNRELATED)
					.addComponent(btnEmpezar)
					.addGap(6))
		);
		
		
		scrollPane_1.setViewportView(panelPie);
		panelPie.setLayout(new GridLayout(0, 1, 0, 0));
		
		list = user.getUserCategorie(categoria_id);
		scrollPane.setViewportView(panelUsuario);
		

		
		JMenuItem mntmNewMenuItem = new JMenuItem("Desbloquear");
		
		panelUsuario.setLayout(new GridLayout(0, 3, 0, 0));
		
		Post post = new Post();
		listPost = post.getCountPostUsers(categoria_id);
		
		JPopupMenu popMenu = new JPopupMenu();
		popMenu.add(mntmNewMenuItem);
		//Agregar a los usuarios al panel 
		for (String[] usuarios : list) {
			JCheckBox chckbxNewCheckBox = new JCheckBox(usuarios[1]);
			listCheckBoxUsers.add(chckbxNewCheckBox);
			//Si el usuario esta bloqueado o en el d�a tiene mas de 3 publicaciones mostrar el checkbox disable
			if(usuarios[7] != null || (chckbxNewCheckBox.isEnabled() && Integer.parseInt(usuarios[8]) >= 12)) {
				chckbxNewCheckBox.setEnabled(false);
			}
			
			//Agregar a los usuarios la cantidad de post en el d�a
			for(String[] pt : listPost) {
				if(pt[0].equals(usuarios[1])) {
					chckbxNewCheckBox.setText(chckbxNewCheckBox.getText()+" ("+pt[1]+")");
					break;
				}
			}
			
			addPopup(chckbxNewCheckBox, popMenu);
			
			panelUsuario.add(chckbxNewCheckBox);
			totalUser++;
		}
		
		lblTotal.setText(lblTotal.getText()+": "+totalUser);
		
		//Seleccionar o deseleccionar todos los checkbox
		chckbxNewCheckBox_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if(chckbxNewCheckBox_1.isSelected()) {
					for(JCheckBox ch : listCheckBoxUsers) {
						if(ch.isEnabled()) ch.setSelected(true);
						
					}
				}else {
					for(JCheckBox ch : listCheckBoxUsers) {
						ch.setSelected(false);
					}
				}
			}
		});

	    addListenerDesbloquear(mntmNewMenuItem);
		contentPane.setLayout(gl_contentPane);
	}
	
	//Mostrar menu contextual a los checkbox deshabilitados
	private static void addPopup(Component component, final JPopupMenu popup) {
		component.addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent e) {
				if(!e.getComponent().isEnabled()) {
					if (e.isPopupTrigger()) {
						showMenu(e);
					}
				}
				
			}
			public void mouseReleased(MouseEvent e) {
				if(!e.getComponent().isEnabled()) {
					if (e.isPopupTrigger()) {
						showMenu(e);
					}
				}
			}
			private void showMenu(MouseEvent e) {
				if(!e.getComponent().isEnabled()) {
					if (e.isPopupTrigger()) {
						popup.show(e.getComponent(), e.getX(), e.getY());
						check = (JCheckBox) e.getComponent();
					}
				}
				
			}
		});
	}
	
	private void addListenerDesbloquear(JMenuItem menu) {
		menu.addMouseListener(new MouseAdapter() {
			
			public void mouseReleased(MouseEvent a) {
				
				User_Block userb = new User_Block();
				if(userb.desblockUser(check.getText())) {
					check.setEnabled(true);
					JOptionPane.showMessageDialog(null,"Usuario desbloqueado correctamente");
				}else {
					JOptionPane.showMessageDialog(null,"Error al desbloquear");
				}
				
			}
		});
	}
}
