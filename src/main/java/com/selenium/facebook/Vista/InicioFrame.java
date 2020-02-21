package com.selenium.facebook.Vista;



import com.selenium.facebook.Modelo.*;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JButton;

import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.awt.event.ActionEvent;
import javax.swing.JMenuBar;
import java.awt.Color;
import javax.swing.JMenu;
import javax.swing.JMenuItem;

import java.awt.Font;



import javax.swing.JTextArea;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.LayoutStyle.ComponentPlacement;
public class InicioFrame extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private JPanel contentPane;
	private final JMenuBar menuBar = new JMenuBar();
	private final JMenu mnUsuarios = new JMenu("Usuarios");
	private final JMenuItem registrarUsuario = new JMenuItem("Registrar");
	private final JMenuItem importarUsuarios = new JMenuItem("Importar Usuarios");
	private final JMenuItem buscarUsuario = new JMenuItem("Buscar");
	private final JMenuItem actualizarUsuario = new JMenuItem("Actualizar Usuarios");
	private final JMenu mnVpn = new JMenu("Vpn");
	private final JMenuItem registrarVpn = new JMenuItem("Registrar");
	private final JMenuItem actualizarVpn = new JMenuItem("Actualizar");
	private final JMenu mnCategorias = new JMenu("Categorias");
	private final JMenuItem registrarCategoria = new JMenuItem("Registrar");
	private final JMenuItem mntmRegistrarSubCategorie = new JMenuItem("Registrar Sub Categoria");
	private final JMenu mnFrases = new JMenu("Frases");
	private final JMenuItem registrarFrase = new JMenuItem("Registrar");
	private final JMenuItem registrarHashTag = new JMenuItem("Registrar Hashtag"); 
	private final JMenu mnGenero = new JMenu("Genero");
	private final JMenuItem registrarGenero = new JMenuItem("Registrar Genero");
	private final JMenuItem actualizarGenero = new JMenuItem("Actualizar Genero");
	private final JMenu mnTask = new JMenu("Tarea");
	private final JMenuItem registrarTarea = new JMenuItem("Registrar Tarea");
	//private final JMenuItem registerTaskGrid = new JMenuItem("Crear Parrilla De Tarea");
	private final JMenu mnPhotos = new JMenu("Fotos");
	private final JMenuItem registrarDireccionDeFotos = new JMenuItem("Registrar Fotos"); 
	public ArrayList<JTextArea> textA = new ArrayList<JTextArea>();
	private static JComboBox<String> comboBox = new JComboBox<String>();
	private static JComboBox<String> comboBox_1 = new JComboBox<String>();
	private static JButton empezar = new JButton("Comenzar");
	private Inicio_Aplicacion iniApli = new Inicio_Aplicacion();
	private static HashMap<String, Integer> hashCa;
	private static HashMap<String, Integer> hashGe = new HashMap<String, Integer>();
	private final JLabel lblNewLabel_1 = new JLabel("Campaña");
	private final JLabel lblNewLabel_2 = new JLabel("Genero");
	
	/**
	 * Launch the application.
	 * @throws SQLException 
	 */
	public static void main(String[] args) throws SQLException {
		final Task_Grid task_G = new Task_Grid();
		hashCa = task_G.getCategoriesToday();
		setComboBox(hashCa);
		
		comboBox.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				comboBox_1.removeAll();
				comboBox_1.removeAllItems();
				task_G.setCategories_id(Integer.parseInt(hashCa.get(comboBox.getSelectedItem().toString()).toString()));
				hashGe = task_G.getCategoriesAndGeneresToday();
				for(String st : hashGe.keySet()) comboBox_1.addItem(st);
				
				if(hashGe.size() > 0) {
					empezar.setEnabled(true);
				}
			}
		});
		
		if(hashCa.size() > 0) {
			comboBox.setSelectedIndex(0);
		}
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					InicioFrame frame = new InicioFrame();
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
	public InicioFrame() {
		lblNewLabel_1.setFont(new Font("Arial", Font.PLAIN, 12));
		lblNewLabel_2.setFont(new Font("Arial", Font.PLAIN, 12));
		setTitle("FaceBot");
		setResizable(false);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 559, 383);
		menuBar.setFont(new Font("Arial", Font.PLAIN, 12));
		menuBar.setBackground(new Color(51, 153, 204));
		
		setJMenuBar(menuBar);
		mnUsuarios.setFont(new Font("Arial", Font.BOLD, 12));
		empezar.setEnabled(false);
		menuBar.add(mnUsuarios);
		registrarUsuario.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				RegistrarUsuario registerUser;
				try {
					registerUser = new RegistrarUsuario();
					registerUser.inicio();
					
				} catch (SQLException e) {
					e.printStackTrace();
				}
				
			}
		});
		
//		scrapping();
		
		mnUsuarios.add(registrarUsuario);
		
		importarUsuarios.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					ImportUsers impo = new ImportUsers();
					impo.inicio();
				}catch (Exception e1) {
					System.err.println(e1);
				}
			}
		});
		
		mnUsuarios.add(importarUsuarios);
		
		buscarUsuario.setEnabled(false);
		
		mnUsuarios.add(buscarUsuario);
		actualizarUsuario.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					UpdateUsers up = new UpdateUsers();
					up.init();
				}catch (Exception e1) {
					System.err.println(e1);
				}
			}
		});
		
		
		
		
		mnUsuarios.add(actualizarUsuario);
		
		mnVpn.setFont(new Font("Arial", Font.BOLD, 12));
		
		menuBar.add(mnVpn);
		registrarVpn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				RegistrarVPN registerVpn = new RegistrarVPN();
				registerVpn.inicio();
			}
		});
		
		mnVpn.add(registrarVpn);
		actualizarVpn.setEnabled(false);
		
		mnVpn.add(actualizarVpn);
		contentPane = new JPanel();
		contentPane.setBackground(new Color(102, 153, 204));
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		
		mnCategorias.add(registrarCategoria);
		mnCategorias.add(mntmRegistrarSubCategorie);
		menuBar.add(mnCategorias);
		
		registrarCategoria.addActionListener(new ActionListener() {
			
			public void actionPerformed(ActionEvent e) {
				RegistrarCategories registrar = new RegistrarCategories();
				registrar.inicio();
			}
		});
		
		mntmRegistrarSubCategorie.addActionListener(new ActionListener() {
			
			public void actionPerformed(ActionEvent arg0) {
				RegistrarSubCategorie regis;
				try {
					regis = new RegistrarSubCategorie();
					regis.init();
				} catch (SQLException e) {
					e.printStackTrace();
				}
				
				
			}
		});
		
		
		mnFrases.add(registrarFrase);
		mnFrases.add(registrarHashTag);
		menuBar.add(mnFrases);
		
		registrarFrase.addActionListener(new ActionListener() {
			
			public void actionPerformed(ActionEvent e) {
				RegistrarFrase registrar;
				
					registrar = new RegistrarFrase();
					registrar.inicio();
				
				
			}
		});
		
		registrarHashTag.addActionListener(new ActionListener() {
			
			public void actionPerformed(ActionEvent e) {
				RegistrarHashTag registrar;
				try {
					registrar = new RegistrarHashTag();
					registrar.inicio();
				} catch (SQLException e1) {
					e1.printStackTrace();
				}
				
				
			}
		});
		
		
		mnGenero.add(registrarGenero);
		menuBar.add(mnGenero);
		
		registrarGenero.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent arg0) {
				RegistrarGenero registrar = new RegistrarGenero();
				registrar.inicio();
			}
			
		});
		
		mnGenero.add(actualizarGenero);
		
		actualizarGenero.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				UpdateGeneres upG = new UpdateGeneres();
				upG.init();
			}
		});
		
		mnTask.add(registrarTarea);
		menuBar.add(mnTask);
		
		registrarTarea.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				RegistrarTarea regi = new RegistrarTarea();
				regi.init();
			}
		});
		
		/*mnTask.add(registerTaskGrid);
		
		registerTaskGrid.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				RegisterTaskGrid regis_T = new RegisterTaskGrid();
				regis_T.init();
				
			}
		});*/
		
		mnPhotos.add(registrarDireccionDeFotos);
		menuBar.add(mnPhotos);
		
		registrarDireccionDeFotos.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				RegistrarDireccionFotos dirFotos = new RegistrarDireccionFotos();
				dirFotos.inicio();
			}
		});
		
		JLabel lblNewLabel = new JLabel("v 2.0.8");
		lblNewLabel.setFont(new Font("Arial", Font.PLAIN, 12));
		
		
		
		GroupLayout gl_contentPane = new GroupLayout(contentPane);
		gl_contentPane.setHorizontalGroup(
			gl_contentPane.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_contentPane.createSequentialGroup()
					.addGap(36)
					.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_contentPane.createSequentialGroup()
							.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING, false)
								.addComponent(lblNewLabel_2, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
								.addComponent(lblNewLabel_1, GroupLayout.DEFAULT_SIZE, 58, Short.MAX_VALUE))
							.addGap(65)
							.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
								.addComponent(comboBox_1, GroupLayout.PREFERRED_SIZE, 213, GroupLayout.PREFERRED_SIZE)
								.addComponent(comboBox, GroupLayout.PREFERRED_SIZE, 213, GroupLayout.PREFERRED_SIZE))
							.addContainerGap(170, Short.MAX_VALUE))
						.addGroup(gl_contentPane.createSequentialGroup()
							.addComponent(lblNewLabel)
							.addPreferredGap(ComponentPlacement.RELATED, 348, Short.MAX_VALUE)
							.addComponent(empezar)
							.addGap(43))))
		);
		
		gl_contentPane.setVerticalGroup(
			gl_contentPane.createParallelGroup(Alignment.TRAILING)
				.addGroup(gl_contentPane.createSequentialGroup()
					.addGap(62)
					.addGroup(gl_contentPane.createParallelGroup(Alignment.BASELINE)
						.addComponent(comboBox, GroupLayout.PREFERRED_SIZE, 39, GroupLayout.PREFERRED_SIZE)
						.addComponent(lblNewLabel_1))
					.addGap(38)
					.addGroup(gl_contentPane.createParallelGroup(Alignment.BASELINE)
						.addComponent(comboBox_1, GroupLayout.PREFERRED_SIZE, 39, GroupLayout.PREFERRED_SIZE)
						.addComponent(lblNewLabel_2))
					.addPreferredGap(ComponentPlacement.RELATED, 85, Short.MAX_VALUE)
					.addComponent(lblNewLabel)
					.addGap(46))
				.addGroup(gl_contentPane.createSequentialGroup()
					.addContainerGap(238, Short.MAX_VALUE)
					.addComponent(empezar, GroupLayout.PREFERRED_SIZE, 50, GroupLayout.PREFERRED_SIZE)
					.addGap(36))
		);
		contentPane.setLayout(gl_contentPane);
		
		iniApli.setVersion("2.0.8");
		
		empezar.addActionListener(new ActionListener() {
			
			public void actionPerformed(ActionEvent e) {
				try {
					int id = Integer.parseInt(hashCa.get(comboBox.getSelectedItem().toString()).toString());
					int id_genere = Integer.parseInt(hashGe.get(comboBox_1.getSelectedItem().toString()).toString());
					Ejecucion eje = new Ejecucion(id,id_genere,iniApli);
					setExtendedState(ICONIFIED);
					eje.inicio();
				} catch (SQLException e1) {
					e1.printStackTrace();
				}
				
				
			}
		});
	}
	
	private static JComboBox<String> setComboBox(HashMap<String, Integer> map) {
		comboBox = new JComboBox<String>();
		
		for (String string : map.keySet()) {
			comboBox.addItem(string);
		}
	    return comboBox;
	}
}
