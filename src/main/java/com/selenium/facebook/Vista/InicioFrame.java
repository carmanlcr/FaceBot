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
import java.util.List;
import java.awt.event.ActionEvent;
import javax.swing.JMenuBar;
import java.awt.Color;
import javax.swing.JMenu;
import javax.swing.JMenuItem;

import java.awt.Font;



import javax.swing.JTextArea;
import javax.swing.JComboBox;
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
	private final JMenuItem actualizarUser = new JMenuItem("Actualizar Usuario");
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
	private final JMenuItem registerTaskGrid = new JMenuItem("Crear Parrilla De Tarea");
	private final JMenu mnPhotos = new JMenu("Fotos");
	private final JMenuItem registrarDireccionDeFotos = new JMenuItem("Registrar Fotos"); 
	public ArrayList<JTextArea> textA = new ArrayList<JTextArea>();
	private static Categorie cate = new Categorie();
	private static JComboBox<String> comboBox;
	private JButton empezar = new JButton("Comenzar");
	private Inicio_Aplicacion iniApli = new Inicio_Aplicacion();
	
	/**
	 * Launch the application.
	 * @throws SQLException 
	 */
	public static void main(String[] args) throws SQLException {
		List<String> list = cate.getAllActive();
		comboBox = setComboBox(list);
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
		setTitle("FaceBot");
		setResizable(false);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 559, 383);
		menuBar.setFont(new Font("Arial", Font.PLAIN, 12));
		menuBar.setBackground(new Color(51, 153, 204));
		
		setJMenuBar(menuBar);
		mnUsuarios.setFont(new Font("Arial", Font.BOLD, 12));
		
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
		
		mnUsuarios.add(actualizarUser);
		actualizarUser.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				UpdateUser update = new UpdateUser();
				update.init();
			}
		});
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
					// TODO Auto-generated catch block
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
		
		mnTask.add(registerTaskGrid);
		
		registerTaskGrid.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				RegisterTaskGrid regis_T = new RegisterTaskGrid();
				regis_T.init();
				
			}
		});
		
		mnPhotos.add(registrarDireccionDeFotos);
		menuBar.add(mnPhotos);
		
		registrarDireccionDeFotos.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				RegistrarDireccionFotos dirFotos = new RegistrarDireccionFotos();
				dirFotos.inicio();
			}
		});
		
		GroupLayout gl_contentPane = new GroupLayout(contentPane);
		gl_contentPane.setHorizontalGroup(
			gl_contentPane.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_contentPane.createSequentialGroup()
					.addGap(139)
					.addComponent(comboBox, GroupLayout.PREFERRED_SIZE, 213, GroupLayout.PREFERRED_SIZE)
					.addContainerGap(191, Short.MAX_VALUE)
					.addGap(80)
					.addComponent(empezar)
					.addContainerGap(191, Short.MAX_VALUE))
		);
		gl_contentPane.setVerticalGroup(
			gl_contentPane.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_contentPane.createSequentialGroup()
					.addGap(81)
					.addComponent(comboBox, GroupLayout.PREFERRED_SIZE, 39, GroupLayout.PREFERRED_SIZE)
					.addContainerGap(204, Short.MAX_VALUE)
					.addGap(81)
					.addComponent(empezar,GroupLayout.PREFERRED_SIZE,50,GroupLayout.PREFERRED_SIZE)
					.addContainerGap(204, Short.MAX_VALUE))
		);
		contentPane.setLayout(gl_contentPane);
		
		iniApli.setVersion("1.0.0");
		
		empezar.addActionListener(new ActionListener() {
			
			public void actionPerformed(ActionEvent e) {
				try {
					int id = cate.getIdCategories((String) comboBox.getSelectedItem());
					Ejecucion eje = new Ejecucion(id,iniApli);
					setExtendedState(ICONIFIED);
					eje.inicio();
				} catch (SQLException e1) {
					e1.printStackTrace();
				}
				
				
			}
		});
	}
	
	private static JComboBox<String> setComboBox(List<String> map) {
		comboBox = new JComboBox<String>();
		
		for (String string : map) {
			comboBox.addItem(string);
		}
	    return comboBox;
	}

//	private void scrapping() {
//		try {
//			
//			
//		      // Here we create a document object and use JSoup to fetch the website
//			Connection.Response res = Jsoup.connect("https://mbasic.facebook.com/notifications.php")
//		    		  .data("email","luis.andres.carman@gmail.com","pass","Facebookl4ms","lsd","AVptuGRS")
//		    		  .method(Method.POST)
//		    		  .execute();
//
//			System.out.println("Respuesta code: "+res.statusCode());
//			
//			Document doc = res.parse();
//		      // With the document fetched, we use JSoup's title() method to fetch the title
//		      System.out.printf("Title: %s\n", doc.title());
//
//		      // Get the list of repositories
//		      Elements repositories = doc.getElementsByClass("repo-iem");
//
//		      /**
//		       * For each repository, extract the following information:
//		       * 1. Title
//		       * 2. Number of issues
//		       * 3. Description
//		       * 4. Full name on github
//		       */
//		      for (Element repository : repositories) {
//		        // Extract the title
//		        String repositoryTitle = repository.getElementsByClass("repo-item-title").text();
//
//		        // Extract the number of issues on the repository
//		        String repositoryIssues = repository.getElementsByClass("repo-item-issues").text();
//
//		        // Extract the description of the repository
//		        String repositoryDescription = repository.getElementsByClass("repo-item-description").text();
//
//		        // Get the full name of the repository
//		        String repositoryGithubName = repository.getElementsByClass("repo-item-full-name").text();
//
//		        // The reposiory full name contains brackets that we remove first before generating the valid Github link.
//		        String repositoryGithubLink = "https://github.com/" + repositoryGithubName.replaceAll("[()]", "");
//
//		        // Format and print the information to the console
//		        System.out.println(repositoryTitle + " - " + repositoryIssues);
//		        System.out.println("\t" + repositoryDescription);
//		        System.out.println("\t" + repositoryGithubLink);
//		        System.out.println("\n");
//		      }
//
//		    // In case of any IO errors, we want the messages written to the console
//		    } catch (IOException e) {
//		      e.printStackTrace();
//		    }
//	}
}
