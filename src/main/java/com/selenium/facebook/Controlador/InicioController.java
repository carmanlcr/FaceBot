package com.selenium.facebook.Controlador;

import java.awt.AWTException;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.SQLException;
import java.util.*;

import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JOptionPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import org.openqa.selenium.ElementClickInterceptedException;

import com.selenium.facebook.Modelo.*;
import com.selenium.facebook.Controlador.DriverController;
import com.selenium.facebook.Controlador.RobotController;


public class InicioController {
	private final String PAGE = "https://mbasic.facebook.com/";
	private static DriverController drive;
	private static User users;
	private static RobotController robot;
	private static VpnController vpn;
	private List<JCheckBox> usuarios;
	private List<JTextArea> pieDeFoto = new ArrayList<JTextArea>();
	private List<List<String>> checkBoxHashTag = new ArrayList<List<String>>();
	private List<JComboBox<String>> comboBoxGenere = new ArrayList<JComboBox<String>>();
	private List<JTextField> listUsers = new ArrayList<JTextField>();
	private int categoria_id;
	private int idUser;
	private static int idGenere;
	private static int ini = 0;
	private int count = 0;
	private boolean banderaVpn = false;
	private boolean banderaBlockeo = true;
	private Post po = new Post();

	public InicioController(int categoria_id, List<JCheckBox> listCheckBoxUsersSend, List<JTextArea> listTextARea,
			List<List<String>> listChechBoxSelected, List<JTextField> listTextFieldUser,
			List<JComboBox<String>> listJComboBoxGenere) {
		this.categoria_id = categoria_id;
		this.usuarios = listCheckBoxUsersSend;
		this.pieDeFoto = listTextARea;
		this.checkBoxHashTag = listChechBoxSelected;
		this.listUsers = listTextFieldUser;
		this.comboBoxGenere = listJComboBoxGenere;
	}

	public void init() throws InterruptedException, AWTException, SQLException, IOException {
		count = comboBoxGenere.size() - 1;
		int usuariosAProcesar = 0;
		for (JCheckBox jCheckBox : usuarios) {
			usuariosAProcesar++;
			users = new User();
			users.setUsername(jCheckBox.getText());
			users.setEmail(jCheckBox.getText());
			users = users.getUser();
			idUser = users.getUsers_id();
			po.setUsers_id(idUser);
			
			
			String generes = (String) comboBoxGenere.get(ini).getSelectedItem();
			Genere gene = new Genere();
			gene.setName(generes);
			idGenere = gene.getIdGenere();
			
			int idlistTask = po.getLastsTasktPublic();
			
			if(idlistTask == 0) {
				System.out.println("El usuario no tiene mas tareas por publicar");
			}else if(po.getCountPostUser() >= 9) {
				System.out.println("El usuario ya hizo las dos publicaciones del día");
			}else {
				String ip = validateIP();
				robot = new RobotController();
				vpn = new VpnController(robot);
				Vpn v = new Vpn();
				v.setVpn_id(users.getVpn_id());
				v = v.getVpn();
				vpn.iniciarVpn(v.getName(), banderaVpn);
				String ipActual = validateIP();
				
				System.out.println(usuariosAProcesar + " usuario(s) de " + usuarios.size() + " usuario(s)");
				// Valida si la vpn conecto
				if (ip.equals(ipActual)) {
					System.err.println("El usuario " + users.getUsername() + " no se puedo conectar a la vpn");
				} else {
					// Setear valores a google Chrome
					drive = new DriverController();
					drive.optionsChrome();
					System.out.println("*********************" + users.getUsername() + "***********************");
					IniciaSesion sesion = new IniciaSesion(drive, users.getUsername(), users.getPassword());
					sesion.init();

					// Esperar que cargue la pagina para que cargue el dom completamente
					Thread.sleep(getNumberRandomForSecond(5250, 5650));

					

					if (!validateBlockOUserIncorrect()) {
						if (drive.searchElement(1, "/html/body/div/div/div/div/table/tbody/tr/td/div/div[3]/a") != 0) {
							drive.clickButton(1, "/html/body/div/div/div/div/table/tbody/tr/td/div/div[3]/a","Ahora no");
						}

						if (drive.searchElement(1, "/html/body/div/div/div[2]/div/table/tbody/tr/td/div/div[3]/a") != 0) {
							drive.clickButton(1, "/html/body/div/div/div[2]/div/table/tbody/tr/td/div/div[3]/a","Ahora no");
						}
						
						if(drive.searchElement(1, "/html/body/div/div/div/div/div[1]/table/tbody/tr/td[2]/table/tbody/tr/td/h3/a") != 0
						  || drive.searchElement(1, "//h3[text()[contains(.,'Siguiente')]]") != 0
						  || drive.searchElement(1, "//a[text()[contains(.,'Siguiente')]]") != 0
						  || drive.searchElement(1, "//*[text()[contains(.,'Siguiente')]]") != 0) {
							
							System.out.println("El usuario pide agregar a otros usuarios, saltar");
							try {
								drive.clickButton(1, "//*[text()[contains(.,'Siguiente')]]", "Siguiente");
							}catch(ElementClickInterceptedException e) {
								System.out.println("No se puede hacer click en el elemento Siguiente xPath");
								try {
									drive.clickButton(1, "html/body/div/div/div/div/div[1]/table/tbody/tr/td[3]/a", "Siguiente elemento a");
									drive.clickButton(1, "//*[text()[contains(.,'Siguiente')]]", "");
								}catch(ElementClickInterceptedException ex) {
									System.out.println("No se puede hacer click en el elemento Siguiente elemento a");	
									try {
										drive.clickButton(1, "//*[text()[contains(.,'Siguiente')]]", "Siguiente elemento a");
									}catch(ElementClickInterceptedException ea) {
										System.out.println("No se puede hacer click en el elemento Siguiente elemento general");	
									}
								}
							}
						}
						startProgram(idlistTask);
					}

				} // fin del else

				// Desconectar la vpn para el siguiente usuario
				if (drive != null) {
					drive.quit();
				}
				vpn.desconectVpn();
				banderaVpn = true;
				Thread.sleep(getNumberRandomForSecond(1999, 2125));

			} // Fin del for
			
			}
		System.out.println("Finalizo con exito el programa");
		System.exit(1);
			
	}// Fin del init
	
	private boolean validateBlockOUserIncorrect() {
		if(drive.searchElement(1,
				"/html/body/div/div/div[2]/div/form/div/div[2]/div[3]/table/tbody/tr/td/input") != 0) {
			userBlock("El usuario pide verificacion de telefono");
			System.out.println("Usuario bloqueado");
			return true;
		}else if (drive.searchElement(1,
				"//*[text()[contains(.,'El correo electrónico que has introducido no coincide con ninguna cuenta. ')]]") != 0) {
			System.out.println("El correo electro no coincide");
			return true;
		} else if (drive.searchElement(1,
				"//*[text()[contains(.,'Sigue unos pasos más para iniciar sesión')]]") != 0) {
			userBlock("El usuario pide pasos para iniciar sesion");
			System.out.println("El usuario pide verificación por catcha");
			return true;
		} else if (drive.searchElement(1, "//*[text()[contains(.,'Ingresa el código a continuación')]]") != 0) {
			System.out.println("El usuario pide verificación por catcha");
			userBlock("El usuario pide verificación por catcha");
			return true;
		}else if(drive.searchElement(1, "//*[text()[contains(.,'Confirma tu identidad')]]") != 0) {
			System.out.println("El usuario pide confirmación de identidad");
			userBlock("El usuario pide confirmación de identidad");
			return true;
		}else if(drive.searchElement(1, "//*[text()[contains(.,'Control de seguridad')]]") != 0) {
			System.out.println("El usuario pide un control de seguridad");
			userBlock("El usuario pide un control de seguridad");
			return true;
		}else if(drive.searchElement(1, "//*[text()[contains(.,'No puedes usar esta función en este momento.')]]") != 0
				|| drive.searchElement(1, "//h2[text()[contains(.,'No puedes usar esta función en este momento.')]]") != 0
				|| drive.searchElement(1, "/html/body/div/div/div[2]/div[1]/h2") != 0) {
			System.out.println("El usuario alcanzo el maximo de publicaciones al día");
			return true;
		}else if(drive.searchElement(1, "/html/body/div/div/div/div/table[1]/tbody/tr/td[2]/div") != 0
				|| drive.searchElement(1, "//*[text()[contains(.,'Reconocimiento facial en Facebook')]]") != 0) {
			userBlock("Reconocimiento facial en facebook");
			System.out.println("El usuario pide reconocimiento facial");
			return true;
		}else if(drive.searchElement(1, "/html/body/div/div/div[2]/div/form/div/div[2]/div[1]/div[2]/div/div[1]/div") != 0 
				|| drive.searchElement(1, "//*[text()[contains(.,'Completa los siguientes pasos para iniciar sesión')]]") != 0) {
			userBlock("Completa los siguientes pasos para iniciar sesión");
			System.out.println("Completa los siguientes pasos para iniciar sesión");
			return true;
		}else if(drive.searchElement(1, "/html/body/div/div/div[2]/div/form/div/div[2]/div[1]/div[2]/span/div") != 0) {
			userBlock("Cuenta inhabilitada");
			System.out.println("La cuenta esta inhabilitada");
			return true;
		}else if(drive.searchElement(1, "//*[text()[contains(.,'Has usado una contraseña antigua. Si has olvidado tu contraseña actual, puedes solicitar nueva.')]]") != 0
				|| drive.searchElement(1, "/html/body/div/div/div[2]/div/table/tbody/tr/td/div[1]/div") != 0) {
			System.out.println("La contraseña es una antigua");
			return true;
		}
		return false;
	}

	/**
	 * Inico del proceso de instagram luego que el inicio de sesión sea exitoso
	 * 
	 * @throws InterruptedException
	 * @throws SQLException
	 */
	private void startProgram(int listTask_id) throws InterruptedException, SQLException {

		System.out.println("Usuario logueado");

		
		Task_Model_Detail tmd = new Task_Model_Detail();
		tmd.setTasks_model_id(listTask_id);
		List<Integer> listTask = tmd.getTaskModelDetailDiferent();
		System.out.println("Buscando tarea para que el usuario realice");
		random(listTask, listTask_id);

		
		
		if (banderaBlockeo) {
			// Darle al panel de opciones
			if (drive.searchElement(1, "//a[text()[contains(.,'Menu')]]") != 0) {
				drive.clickButton(1, "//a[text()[contains(.,'Menu')]]","Menu");
			} else if (drive.searchElement(1, "/html/body/div/div/div[1]/div/div/a[8]") != 0) {
				drive.clickButton(1, "/html/body/div/div/div[1]/div/div/a[8]","Boton de Menú ");
			}

			Thread.sleep(getNumberRandomForSecond(2002, 3501));
			// Darle click a la barra de opciones
			drive.clickButton(3, "mbasic_logout_button","Boton de cerrar sesión");
			Thread.sleep(getNumberRandomForSecond(3001, 3099));
			if (drive.searchElement(1, "/html/body/div/div/div/div/table/tbody/tr/td/div/form[2]/input[3]") != 0) {
				drive.clickButton(1, "/html/body/div/div/div/div/table/tbody/tr/td/div/form[2]/input[3]","Guardar contraseña");
			}
		}

		System.out.println("Se cerro la sesión del usuario " + users.getUsername());
	}
	
	
	
	
	private void random(List<Integer> listTask, int listTask_id) throws InterruptedException, SQLException {
		// int valueScroll = (int) (Math.random() * 15) + 1;
		int taskModelId = listTask_id;
		for (Integer li : listTask) {
			Thread.sleep(getNumberRandomForSecond(2501, 2654));
			robot.mouseScroll(25);
			Thread.sleep(getNumberRandom(940, 1130));
			robot.mouseScroll(-25);
			Thread.sleep(getNumberRandom(940, 1130));
			switch (li) {
			case 1:
				// Entrar en Editar Perfil
				System.out.println("ENTRAR EN PERFIL Y DAR LIKE A FOTO");
				likeUsers();

				break;
			case 2:
				System.out.println("SUBIR IMAGEN NORMAL TIPO COMIDA, MEME, BEBIDA");
				uploadImage();
				break;
			case 3:
				// Revisar los grupos
				// Ingresar en la seccion de grupos
				System.out.println("BUSCAR GRUPO Y AGREGARSE");
				addGroup();
				break;
			case 4:
				// Publicar en Grupo
				// Ingresar en la seccion de grupos
				if(categoria_id != 3) {
					Genere gene = new Genere();
					gene.setGeneres_id(idGenere);
					gene = gene.getGenereWithPhrasesPhotosHashtag();
					//Si el genero seleccionado no tiene frase o foto o hashtag 
					//Solo debe publicar desde la fan page
					if(gene == null) {
						System.out.println("SOLO PUBLICAR EN FAN PAGE");
						System.out.println("INGRESAR EN FAN PAGE Y COMPARTIR");
						gene = new Genere();
						gene.setGeneres_id(idGenere);
						gene = gene.getFanPage();
						if(gene != null) {
							goFanPage(gene.getFan_page());
							publicGroup(gene,taskModelId);
						}else {
							System.out.println("El genero seleccionado no tiene fan page");
						}
						
					}else {
						//Si el genero tiene frase, foto y hashtag validar si tiene fan page
						gene = new Genere();
						gene.setGeneres_id(idGenere);
						gene = gene.getFanPage();
						//Si el genero tiene fan page ingresar en la fan page y publicar
						if(gene != null) {
							System.out.println("INGRESAR EN FAN PAGE Y COMPARTIR");
							goFanPage(gene.getFan_page());
							publicGroup(gene,taskModelId);
						}
						gene = new Genere();
						gene.setGeneres_id(idGenere);
						gene = gene.getGenere();
						//Si el genero esta clasificado como basura ingresar entre 9 y 14 
						//grupos y publicar
						if(gene != null && gene.isTrash()) {
							System.out.println("PUBLICAR EN GRUPOS RANDOM");
							publicGroupTrash(listTask_id);
						}else {
							
							System.out.println("PUBLICAR EN GRUPOS SEGUN CATEGORIA");
							publicGroupPublicity(taskModelId);
						}
					}
				}
				break;
			case 5:
				// Publicacion final
				if(categoria_id != 3) {
					System.out.println("HACER PUBLICACION FINAL");
					Genere gene = new Genere();
					gene.setGeneres_id(idGenere);
					gene = gene.getGenereWithPhrasesPhotosHashtag();
					if(gene != null) {
						publicFinal(taskModelId);
					}else {
						System.out.println("NO TIENE FRASE, FOTO O HASHTAG PARA PUBLICAR");
					}
					
				}
				
				break;
			case 6:
				// Revisar los mensajes
				System.out.println("REVISAR LOS MENSAJES");
				reviewMessage();
				break;
			case 7:
				// Revisar notificaciones
				// html/body/div/div/div[1]/div/div/a[4]
				System.out.println("REVISAR NOTIFICACIONES");
				reviewNotifaction();
				break;
			case 8:
				if(categoria_id != 3) {
					System.out.println("LEER TODOS LOS GRUPOS DEL USUARIO");
					reviewGroups();
				}
				break;
			default:
				break;
			}
		} // Fin del For

	}

	private String uploadImageFinal() throws InterruptedException, SQLException {

		System.out.println("Darle click a la opción de fotos");
		String hashTag = "";
		if (drive.searchElement(2, "view_photo") != 0) {
			drive.clickButton(2, "view_photo","view_photo para subir foto");
			hashTag = publicIfExistElementFoto();
			return hashTag;
		} else if (drive.searchElement(1, "//*[text()[contains(.,'Foto')]]") != 0) {
			drive.clickButton(2, "//*[text()[contains(.,'Foto')]]","Subir foto");
			hashTag = publicIfExistElementFoto();
			return hashTag;
		}else {
			System.out.println("No se puede publicar en este grupo");
			
		}
		
		return hashTag;
	}
	
	private String getIdGroup() {
		String urlImage = drive.getCurrentUrl();
		String idGroup = "";
		urlImage = urlImage.substring(35);
		for(int j = 0; j<urlImage.length(); j++) {
			if(urlImage.substring(j,j+1).equals("?")) {
				break;
			}else {
				idGroup += urlImage.substring(j,j+1);
			}
		}
		
		return idGroup;
	}
	
	private void publicGroupTrash(int listTask_id) throws InterruptedException, SQLException {
		User_Group gro = new User_Group();
		gro.setUsers_id(idUser);
		int cantGrupo = gro.getCountGroups();
		
		if(cantGrupo < 1) {
			System.out.println("El usuario no tiene grupos agregados en la base de datos ");
		}else {
			System.out.println("El usuario tiene "+cantGrupo+" registrados.");
			int groupsPublication = 0;
			if(cantGrupo > 5 && cantGrupo < 10) {
				groupsPublication = getNumberRandomForSecond(6, 9);
			}else if(cantGrupo > 10) {
				groupsPublication = getNumberRandomForSecond(9, 13);
			}
			
			System.out.println("Buscando "+groupsPublication+" para publicar ");
			List<Group> listGroups = gro.getGroupNotPublication(groupsPublication);
			System.out.println("La cantidad de grupos son "+listGroups.size());
			if(listGroups.size() < 1) {
				System.out.println("NO HAY GRUPOS PARA PUBLICAR");
			}else{
				for(Group g : listGroups) {
					String urlGroup = PAGE+"groups/"+g.getGroups_id();
					String idGroups = g.getGroups_id();
					System.out.println("Publicar en el grupo "+g.getName()+ " id: "+idGroups);
					drive.goPage(urlGroup);
					
					Thread.sleep(getNumberRandomForSecond(1234, 1456));
					int quantityPublicationsNormal = getNumberRandomForSecond(1, 2);
					for(int i = 1; i <= quantityPublicationsNormal; i++) {
						uploadImage();
						Thread.sleep(getNumberRandomForSecond(2456, 3478));
						drive.goPage(urlGroup);
						Thread.sleep(getNumberRandomForSecond(2456, 3478));
					}
					String hash = uploadImageFinal();
					ini++;
					if (ini >= count) {
						ini = 0;
					}
					if(!hash.isEmpty()) {
						String[] ha = hash.split(" ");
						System.out.println("Registrando post");
						
						po.setCategories_id(categoria_id);
						po.setTasks_model_id(listTask_id);
						po.setUsers_id(idUser);
						po.setGroups(idGroups);
						po.setFanPage(false);
						po.insert();

						Post_Detail poDe = new Post_Detail();
						poDe.setPosts_id(po.getLast());
						HashTag ht = new HashTag();
						ht.setCategories_id(categoria_id);
						ht.setGeneres_id(idGenere);
						System.out.println("Registrando HashTag");
						for (int j = 0; j < ha.length; j++) {

							ht.setName(ha[j]);

							poDe.setHashtag_id(ht.getIdCategorieHashTag());

							poDe.insert();
						}
						System.out.println("El usuario publico correctamente");
					}else {
						System.out.println("El usuario no publico");
					}//Fin del if para validar si se publicaron hashtag
				}
			}
		}
	}

	private void uploadImage() throws InterruptedException, SQLException {
		
		if(ifElementPhotoExist()) {
			System.out.println("Darle click a la opción de fotos");
			Thread.sleep(getNumberRandom(2560, 4980));
			Categorie ca = new Categorie();
			List<Integer> la = ca.getSubCategorieConcat();
			Path_Photo pa_ph = new Path_Photo();
			pa_ph.setCategories_id(la.get(0));
			pa_ph.setSub_categories_id(la.get(1));
			System.out.println("Extraer el path de la foto");
			String path = pa_ph.getPathPhotos();
			System.out.println("Colocar el path de la foto en el file");
			drive.inputWriteFile(2, "file1", path);
			Thread.sleep(getNumberRandomForSecond(1201, 1899));
			System.out.println("Darle click al boton siguiente para escribir el pie de foto");
			drive.clickButton(2, "add_photo_done","Darle click a siguiente para subir foto");

			// Esperar que aparezca el boton de publicar
			while (drive.searchElement(2, "view_post") == 0)
				;
			System.out.println("Extraer frase random");
			Phrases ph = new Phrases();
			ph.setCategories_id(la.get(0));
			ph.setSub_categories_id(la.get(1));
			System.out.println("Extraer frase random");
			String post = ph.getPhraseRandomSubCategorie();
			System.out.println("Escribir el pie de la foto");
			drive.inputWrite(2, "xc_message", post,120);

			Thread.sleep(getNumberRandom(1250, 2000));
			System.out.println("Darle a postear imagen");
			if (drive.searchElement(2, "view_post") != 0) {
				drive.clickButton(2, "view_post","view_post postear imagen");
			} else if (drive.searchElement(1, "//*[text()[contains(.,'Post')]]") != 0) {
				drive.clickButton(1, "//*[text()[contains(.,'Post')]]","Post postear imagen");
			} else if (drive.searchElement(1, "//*[text()[contains(.,'Publicar')]]") != 0) {
				drive.clickButton(1, "//*[text()[contains(.,'Publicar')]]","Publicar postear imagen");
			}
			
			Thread.sleep(getNumberRandomForSecond(3250, 4000));
			
			if(drive.searchElement(1, "//*[text()[contains(.,'Etiquetar Foto')]]") != 0) {
				System.out.println("Darle a cancelar para no etiquetar foto");
				drive.clickButton(1, "/html/body/div/div/div[1]/table/tbody/tr/td[3]/a", "Cancelar la etiqueta de personas xpath");
			}
			if(drive.searchElement(1, "//*[text()[contains(.,'Omitir')]]") != 0) {
				System.out.println("Darle a cancelar para no etiquetar foto");
				drive.clickButton(1, "/html/body/div/div/div[1]/table/tbody/tr/td[3]/a", "Cancelar la etiqueta de personas xpath");
			}else if(drive.searchElement(1, "//*[text()[contains(.,'Skip')]]") != 0) {
				System.out.println("Darle a cancelar para no etiquetar foto");
				drive.clickButton(1, "/html/body/div/div/div[1]/table/tbody/tr/td[3]/a", "Cancelar la etiqueta de personas xpath");
			}else if(drive.searchElement(1, "/html/body/div/div/div[2]/div/table/tbody/tr/td/div[2]/div[1]/a") != 0) {
				System.out.println("Darle a cancelar para no etiquetar foto");
				drive.clickButton(1, "/html/body/div/div/div[1]/table/tbody/tr/td[3]/a", "Cancelar la etiqueta de personas xpath");
			}
		}else {
			System.out.println("No se le puede dar click a la opcion de fotos");
		}
		
	}
	
	private boolean ifElementPhotoExist() {
		if (drive.searchElement(2, "view_photo") != 0) {
			drive.clickButton(2, "view_photo","view_photo para subir foto");
			return true;
		} else if (drive.searchElement(1, "//*[text()[contains(.,'Foto')]]") != 0) {
			drive.clickButton(2, "//*[text()[contains(.,'Foto')]]","Subir foto");
			return true;
		}
		return false;
	}
	
	private String publicIfExistElementFoto() throws InterruptedException, SQLException {
		Thread.sleep(getNumberRandom(2562, 4979));
		Path_Photo pa_ph = new Path_Photo();
		pa_ph.setCategories_id(categoria_id);
		pa_ph.setGeneres_id(idGenere);
		System.out.println("Extraer el path de la foto");
		String path = pa_ph.getPathPhotos();
		System.out.println("Colocar el path de la foto en el file");
		drive.inputWriteFile(2, "file1", path);
		Thread.sleep(getNumberRandomForSecond(1201, 1899));
		System.out.println("Darle click al boton siguiente para escribir el pie de foto");
		drive.clickButton(2, "add_photo_done","Darle click a siguiente para subir foto");

		// Esperar que aparezca el boton de publicar
		while (drive.searchElement(2, "view_post") == 0)
			;

		Phrases ph = new Phrases();
		ph.setCategories_id(categoria_id);
		ph.setGeneres_id(idGenere);
		System.out.println("Extraer frase random");
		ph = ph.getPhraseRandom();
		List<String> copia = checkBoxHashTag.get(ini);
		String hash = "";
		System.out.println("Elegir HashTag");
		
		if (copia.size() > 2) {
			Collections.shuffle(copia);
			
			hash += copia.get(0) +  " ";
			hash += copia.get(1) +  " ";
			hash += copia.get(2) +  " ";
			
		} else if (copia.size() > 0 && copia.size() < 2) {
			Collections.shuffle(copia);
			
			hash += copia.get(0) +  " ";
		}

		String pieDe = pieDeFoto.get(ini).getText();
		String usuario = listUsers.get(ini).getText();
		System.out.println("Escribir el pie de la foto");
		drive.inputWrite(2, "xc_message", ph.getPhrase() + " " + pieDe + " " + usuario + " " + hash,150);

		Thread.sleep(getNumberRandomForSecond(1250, 2000));
		System.out.println("Darle a postear imagen");
		if (drive.searchElement(2, "view_post") != 0) {
			drive.clickButton(2, "view_post","view_post postear imagen");
		} else if (drive.searchElement(1, "//*[text()[contains(.,'Post')]]") != 0) {
			drive.clickButton(1, "//*[text()[contains(.,'Post')]]","Post postear imagen");
		} else if (drive.searchElement(1, "//*[text()[contains(.,'Publicar')]]") != 0) {
			drive.clickButton(1, "//*[text()[contains(.,'Publicar')]]","Publicar postear imagen");
		}
		
		
		Thread.sleep(getNumberRandomForSecond(3250, 4000));
		
		if(drive.searchElement(1, "//*[text()[contains(.,'Etiquetar Foto')]]") != 0) {
			System.out.println("Darle a cancelar para no etiquetar foto");
			drive.clickButton(1, "/html/body/div/div/div[1]/table/tbody/tr/td[3]/a", "Cancelar la etiqueta de personas xpath");
		}
		if(drive.searchElement(1, "//*[text()[contains(.,'Omitir')]]") != 0) {
			System.out.println("Darle a cancelar para no etiquetar foto");
			drive.clickButton(1, "/html/body/div/div/div[1]/table/tbody/tr/td[3]/a", "Cancelar la etiqueta de personas xpath");
		}else if(drive.searchElement(1, "//*[text()[contains(.,'Skip')]]") != 0) {
			System.out.println("Darle a cancelar para no etiquetar foto");
			drive.clickButton(1, "/html/body/div/div/div[1]/table/tbody/tr/td[3]/a", "Cancelar la etiqueta de personas xpath");
		}else if(drive.searchElement(1, "/html/body/div/div/div[2]/div/table/tbody/tr/td/div[2]/div[1]/a") != 0) {
			System.out.println("Darle a cancelar para no etiquetar foto");
			drive.clickButton(1, "/html/body/div/div/div[1]/table/tbody/tr/td[3]/a", "Cancelar la etiqueta de personas xpath");
		}
		
		Thread.sleep(getNumberRandomForSecond(2250, 3100));

		return hash;
	}
		
	private void likeUsers() throws InterruptedException {
		if (drive.searchElement(1, "//*[text()[contains(.,'Editar perfil')]]") != 0) {
			drive.clickButton(1, "//*[text()[contains(.,'Editar perfil')]]","Editar perfil");
		} else if (drive.searchElement(1, "/html/body/div/div/div[1]/div/div/a[2]") != 0) {
			drive.clickButton(1, "/html/body/div/div/div[1]/div/div/a[2]","Editar perfil xpath");
		}
		Thread.sleep(getNumberRandom(1253, 1754));

		// Darle click a Amigos
		if (drive.searchElement(1, "/html/body/div/div/div[2]/div/div[1]/div[1]/div[3]/a[2]") != 0) {
			System.out.println("Entrar en amigos");
			drive.clickButton(1, "/html/body/div/div/div[2]/div/div[1]/div[1]/div[3]/a[2]","Boton de amigos xpath");
		}

		Thread.sleep(getNumberRandom(1253, 1754));

		// Contar cantidad amigos
		int quantityFriends = drive.getQuantityElements(1,
				"/html/body/div/div/div[2]/div/div[1]/div[2]/div[2]/div");
		System.out.println("Contando cantidad de amigos");
		if (quantityFriends == 0) {
			System.out.println("Sin ningún amigo en facebook");
		} else {
			int ramdonFriends = getNumberRandomForSecond(1, quantityFriends);
			// Ingresar un usuario
			System.out.println("Ingresar en usuario de manera random");
			drive.clickButton(1, "/html/body/div/div/div[2]/div/div[1]/div[2]/div[2]/div[" + ramdonFriends
					+ "]/table/tbody/tr/td[2]/a","Ingresar en un amigo random");
			Thread.sleep(getNumberRandomForSecond(2503, 3652));

			if (drive.searchElement(1, "//*[text()[contains(.,'Esta cuenta se ha desactivado.')]]") != 0
					|| drive.searchElement(1,
							"//*[text()[contains(.,'This account has been deactivate')]]") != 0) {
				System.out.println("La cuenta a la que se ingreso se encuentra desactivada");
				drive.back();
				Thread.sleep(getNumberRandomForSecond(2503, 3652));
				// Fin del if de las cuentas desactivadas
			} else {
				// Ingresar en las fotos
				System.out.println("Ingresar en las fotos del amigo");
				if (drive.searchElement(1, "//*[text()[contains(.,'Fotos')]]") != 0) {
					drive.clickButton(1, "//*[text()[contains(.,'Fotos')]]","Fotos de amigo");
				} else {
					drive.clickButton(1, "/html/body/div/div/div[2]/div/div[1]/div[1]/div[4]/a[2]","Fotos xPath");
				}

				Thread.sleep(getNumberRandom(2503, 3652));
				int quantityPhotos = drive.getQuantityElements(1,
						"/html/body/div/div/div[2]/div/div[2]/div[1]/div[1]/table/tbody/tr/td");
				System.out.println("Contar cantidad de fotos del usuario");
				if (quantityPhotos == 0) {
					System.out.println("El usuario no tiene foto");
				} else {
					int randomPhotos = getNumberRandomForSecond(1, quantityPhotos);
					System.out.println("Elegir foto random");
					drive.clickButton(1, "/html/body/div/div/div[2]/div/div[2]/div[1]/div[1]/table/tbody/tr/td["
							+ randomPhotos + "]/a","Ingresar en foto Random xPath");
					
					Thread.sleep(getNumberRandom(2503, 3652));

					// Darle click a Me Gusta
					drive.clickButton(1,
							"/html/body/div/div/div[2]/div/div[1]/div/div/div[2]/div/table/tbody/tr/td[1]/a","Me Gusta xPath");
					System.out.println("Darle like a la foto");
					Thread.sleep(getNumberRandom(2503, 3652));

					// Ir al inicio
					if (drive.searchElement(1, "/html/body/div/div/div[1]/table/tbody/tr/td[1]/a") != 0) {
						drive.clickButton(1, "/html/body/div/div/div[1]/table/tbody/tr/td[1]/a","Boton de inicio clasico");
					} else if (drive.searchElement(1,
							"/html/body/div/div/div[1]/div/table/tbody/tr/td[1]/a") != 0) {
						drive.clickButton(1, "/html/body/div/div/div[1]/div/table/tbody/tr/td[1]/a","Boton de inicio si muestra cambiar de me gusta");
					} else {
						drive.goPage("https://mbasic.facebook.com/");
					}
					System.out.println("Volver al inicio");
				}
			}
		}
	}
	
	private void addGroup() throws InterruptedException {
		Group_Categorie gp = new Group_Categorie();
		gp.setCategories_id(categoria_id);
		gp = gp.getGroupSearch();
		String name = "";
		if(gp != null) {
			name = gp.getName();
		}else {
			name = JOptionPane.showInputDialog("INGRESE LA BUSQUEDA DEL GRUPO Y PULSE ACEPTAR");
			
			while(name == null || name.isEmpty()) {
				name = JOptionPane.showInputDialog("INGRESE LA BUSQUEDA DEL GRUPO Y PULSE ACEPTAR");
			}
		}
		
		searchGroup(name);
		
		Thread.sleep(getNumberRandomForSecond(2540, 2980));
		if(drive.searchElement(1, "//*[text()[contains(.,'No se encontraron resultados')]]") != 0) {
			System.out.println("No se encontro resultados para la busqueda "+name);
			
		}else {
			if(drive.searchElement(1, "/html/body/div/div/div[2]/div[2]/div[1]/div[2]/div[2]/a")!= 0) {
				System.out.println("Ver mas");
				drive.clickButton(1, "/html/body/div/div/div[2]/div[2]/div[1]/div[2]/div[2]/a", "Ver mas xpath");
			}else if(drive.searchElement(1, "//a[text()[contains(.,'Ver Mas Resultados')]]") != 0) {
				System.out.println("Ver mas");
				drive.clickButton(1, "//a[text()[contains(.,'Ver Mas Resultados')]]", "Ver Mas Resultados ");
			}else if(drive.searchElement(1, "//a[text()[contains(.,'See More Results')]]") != 0) {
				System.out.println("Ver mas");
				drive.clickButton(1, "//a[text()[contains(.,'See More Results')]]", "See More Results");
			}else {
				
			}
			countQuantityGroup();
		}
		
		drive.clickButton(1, "/html/body/div/div/div[1]/div/div/a[1]", "Volver Inicio xpath");
		
		
	}
	
	private void countQuantityGroup() throws InterruptedException {
		Thread.sleep(getNumberRandomForSecond(1980, 2460));
		System.out.println("Contar cantidad de grupos");
		int quantityGroups = drive.getQuantityElements(1,"/html/body/div/div/div[2]/div[2]/div[1]/div[2]/div[1]/div/div/div") - 1;
		
		if (quantityGroups < 1) {

		} else {
			System.out.println("Elegir un grupo random");
			int ramdonGroups = getNumberRandomForSecond(1, quantityGroups);
			
				if(drive.searchElement(1, "/html/body/div/div/div[2]/div[2]/div[1]/div[2]/div[1]/div/div/div["+4+"]/table/tbody/tr/td[3]/div/div/table/tbody/tr/td[2]/a") != 0) {
					System.out.println("Darle click a unirse");
					drive.clickButton(1, "/html/body/div/div/div[2]/div[2]/div[1]/div[2]/div[1]/div/div/div["+4+"]/table/tbody/tr/td[3]/div/div/table/tbody/tr/td[2]/a", "Unirse xPath "+ramdonGroups);
					Thread.sleep(getNumberRandomForSecond(1806, 2450));
					
					if(drive.searchElement(1, "/html/body/div/div/div[2]/div/table/tbody/tr/td/form/div[2]/div[3]/div[1]/div[1]/span") != 0) {
						System.out.println("Contando cantidad de preguntas");
						int quantiQuestion = drive.getQuantityElements(1, "/html/body/div/div/div[2]/div/table/tbody/tr/td/form/div[2]/div[3]/div");
						System.out.println("Hay "+quantiQuestion+" pregunta(s)");
						if(quantiQuestion <2) {
							String pregunta = drive.getText(1, "/html/body/div/div/div[2]/div/table/tbody/tr/td/form/div[2]/div[3]/div[1]/div[1]/span");
							String preguntaModificada = "";
							for(int j = 0;j<pregunta.length();j++) {
								if(pregunta.substring(j, j+1).equals("­")) {
									preguntaModificada += ""; 
								}else {
									preguntaModificada += pregunta.subSequence(j, j+1);
								}
							}	
							Question question = new Question();
							question.setQuestion(preguntaModificada);
							String answerB = question.getAnswerQuestion();
							if(answerB.isEmpty()) {
								System.out.println("La pregunta no se encuentra en la base de datos, se debe responder manual");
								String respuesta = JOptionPane.showInputDialog("Ingrese la respuesta para la pregunta 1 y pulse aceptar");
								
								while(respuesta == null || respuesta.isEmpty()) respuesta = JOptionPane.showInputDialog("Ingrese la respuesta para la pregunta 1 y pulse aceptar");
								
								System.out.println("Se ingresará la respuesta ingresada en el cuadro de texto");
								drive.inputWrite(1, "/html/body/div/div/div[2]/div/table/tbody/tr/td/form/div[2]/div[3]/div[1]/div[2]/div/textarea", respuesta,150);
								
								question.setAnswer(respuesta);
								try {
									question.insert();
									System.out.println("Se agrego el usuario y la respuesta a la base de datos");
								} catch (SQLException e) {
									e.printStackTrace();
								}
							}else {
								System.out.println("Se ingresará la respuesta ingresada en el cuadro de texto");
								drive.inputWrite(1, "/html/body/div/div/div[2]/div/table/tbody/tr/td/form/div[2]/div[3]/div[1]/div[2]/div/textarea", answerB,150);
								
							}
							System.out.println(preguntaModificada);
						}else {
							for(int i = 1; i<= quantiQuestion;i++) {
								String pregunta = drive.getText(1, "/html/body/div/div/div[2]/div/table/tbody/tr/td/form/div[2]/div[3]/div["+i+"]/div[1]/span");
								String preguntaModificada = "";
								for(int j = 0;j<pregunta.length();j++) {
									if(pregunta.substring(j, j+1).equals("­")) {
										preguntaModificada += ""; 
									}else {
										preguntaModificada += pregunta.subSequence(j, j+1);
									}
								}	
								
								Question question = new Question();
								question.setQuestion(preguntaModificada);
								String answerB = question.getAnswerQuestion();
								if(answerB.isEmpty()) {
									System.out.println("La pregunta no se encuentra en la base de datos, se debe responder manual");
									String respuesta = JOptionPane.showInputDialog("Ingrese la respuesta para la pregunta "+i+" y pulse aceptar");
									
									while(respuesta == null || respuesta.isEmpty()) respuesta = JOptionPane.showInputDialog("Ingrese la respuesta para la pregunta "+i+" y pulse aceptar");
									
									System.out.println("Se ingresará la respuesta ingresada en el cuadro de texto");
									drive.inputWrite(1, "/html/body/div/div/div[2]/div/table/tbody/tr/td/form/div[2]/div[3]/div["+i+"]/div[2]/div/textarea", respuesta,150);
									
									question.setAnswer(respuesta);
									try {
										question.insert();
										System.out.println("Se agrego el usuario y la respuesta a la base de datos");
									} catch (SQLException e) {
										e.printStackTrace();
									}
								}else {
									System.out.println("Se ingresará la respuesta ingresada en el cuadro de texto");
									drive.inputWrite(1, "/html/body/div/div/div[2]/div/table/tbody/tr/td/form/div[2]/div[3]/div["+i+"]/div[2]/div/textarea", answerB,150);
									
								}
							}	
							System.out.println("Darle click al boton de enviar solicitud");	
							drive.clickButton(1, "/html/body/div/div/div[2]/div/table/tbody/tr/td/form/div[2]/div[4]/input", "Boton de enviar solicitud");
						}
					}
					if(drive.searchElement(1, "html/body/div/div/div[2]/div/table/tbody/tr/td/form/div[2]/div[3]/div[1]/div[1]") != 0) {
					}else if(drive.searchElement(1, "/html/body/div/div/div[2]/div/table/tbody/tr/td/form/div[2]/div[3]/div/div[1]") != 0) {
					}else {
						if(drive.searchElement(1, "//*[text()[contains(.,'Cancelar solicitud')]]")!= 0) {
							System.out.println("Se unío al grupo: "+drive.getText(1, "/html/body/div/div/div[2]/div/div[1]/div[2]/table/tbody/tr/td[1]/a/table/tbody/tr/td[2]/h1/div"));
						}else {
							System.out.println("Se unío al grupo");
						}
					}
					drive.back();
				}else {
					System.out.println("No existe el boton unirse en la posición aleatoria");
				}
				
				
				
			

		}
	}
	
	private void goFanPage(String urlFanPage) throws InterruptedException {
		System.out.println("Ingresar en la fan page "+urlFanPage);
		drive.goPage(urlFanPage);
		Thread.sleep(getNumberRandomForSecond(1545, 2456));
		System.out.println("Actualizar pagina");
		drive.refresh();
		Thread.sleep(getNumberRandomForSecond(1945, 2456));
		drive.scrollDown(1);
		Thread.sleep(getNumberRandomForSecond(845, 996));
		drive.scrollUp(1);
		Thread.sleep(getNumberRandomForSecond(3047, 3396));

	}
	
	private void publicGroup(Genere ge, int taskModelId) throws InterruptedException, SQLException {
		if(drive.searchElement(1, "//*[text()[contains(.,'Me gusta')]]") != 0) {
			System.out.println("Darle like");
			drive.clickButton(1, "//*[text()[contains(.,'Me gusta')]]","Me gusta");
		}else if(drive.searchElement(0, "likeButton") != 0) {
			drive.clickButton(0, "likeButton", "likeButton className");
		}
		if(drive.searchElement(1, "/html/body/div[1]/div[6]/div[1]/div/div/div[4]/div/div[1]/div/div/div/div/div/div/div/div[1]/div/div/div[2]/div[2]/div[4]/a") != 0) {
			drive.clickButton(1, "/html/body/div[1]/div[6]/div[1]/div/div/div[4]/div/div[1]/div/div/div/div/div/div/div/div[1]/div/div/div[2]/div[2]/div[4]/a", "Cerrar el mensaje");
		}
		if(drive.searchElement(1, "//*[text()[contains(.,'Comentar')]]") != 0) {
			System.out.println("Darle a comentar");
			drive.clickButton(1, "//*[text()[contains(.,'Comentar')]]", "Comentar");
		}
		Thread.sleep(getNumberRandomForSecond(2545, 2856));
		
		robot.pressEsc();
		Thread.sleep(getNumberRandomForSecond(1645, 1987));
		robot.pulsarShiftTabulador();
		Thread.sleep(getNumberRandomForSecond(1645, 1987));
		robot.enter();
		Thread.sleep(getNumberRandom(3345, 4126));
		
		System.out.println("Compartir en un grupo");
		if(drive.searchElement(1, "//*[text()[contains(.,'Compartir…')]]") != 0) {
			drive.clickButton(1, "//*[text()[contains(.,'Compartir…')]]", "Compartir ");
			Thread.sleep(getNumberRandomForSecond(3245, 4348));
			robot.enter();
			Thread.sleep(getNumberRandomForSecond(2245, 2348));
			robot.pressDown();
			Thread.sleep(getNumberRandomForSecond(478,645));
			robot.pressDown();
			Thread.sleep(getNumberRandomForSecond(478,645));
			robot.enter();
			Thread.sleep(getNumberRandomForSecond(478,645));
		}else if(drive.searchElement(1, "//*[text()[contains(.,'Compartir en un grupo')]]") != 0) {
			drive.clickButton(1, "//*[text()[contains(.,'Compartir en un grupo')]]", "Compartir en un grupo");
			
		}
		Thread.sleep(getNumberRandom(2345, 3126));
		writeGroupAndPublic(taskModelId);

		drive.goPage(PAGE);
	}
	
	private void writeGroupAndPublic(int taskModelId) throws InterruptedException {
		Group_Categorie gp = new Group_Categorie();
		gp.setCategories_id(categoria_id);
		Group group_c = new Group();
		List<Group_Categorie> list = gp.getGroupCategorie();
		System.out.println("Buscar grupos segun categoria ");
		if(list.size() > 0) {
			User_Group user_G = new User_Group();
			user_G.setUsers_id(idUser);
			for(Group_Categorie group : list) {
				
				String[] values = group.getName().trim().split(" ");
				String string1 = "";
				String string2 = "";
				for(int i = 0; i<values.length; i++) {
					if(i == 0) {
						string1 = values[i];
					}else if(i == 1) {
						string2 = values[i];
					}
				}
				System.out.println("Buscando grupo por categoria "+string1+" "+string2);
				group_c = user_G.getOneGroupNotPublication(string1, string2);
				if(group_c != null) {
					System.out.println("se consiguio grupo "+group_c.getName());
					break;
				}else {
					System.out.println("No se consiguio grupo para esta categoria");
				}
			}
			
			
			
			
			if(group_c != null) {
				System.out.println("El grupo a publicar es "+group_c.getName());
				int aux = 0;
				for(int i =1; i< 100; i++) {
					
					if(drive.searchElement(1, "/html/body/div["+i+"]/div[2]/div/div/div/div/div/div[2]/div[2]/table/tbody/tr/td[2]/span/span/label/input") != 0) {
						try {
							drive.inputWrite(1, "/html/body/div["+i+"]/div[2]/div/div/div/div/div/div[2]/div[2]/table/tbody/tr/td[2]/span/span/label/input", group_c.getName(), 125);
							System.out.println("Se escribio el nombre del grupo");
							Thread.sleep(getNumberRandomForSecond(1560, 1789));
							robot.pressDown();
							Thread.sleep(getNumberRandomForSecond(456, 789));
							robot.enter();
							Thread.sleep(getNumberRandomForSecond(1560, 1789));
							aux = i;
							break;
						}catch (Exception e) {
						}
					}
					
				}
				Thread.sleep(getNumberRandomForSecond(895, 985));
				System.out.println(aux);
				if(aux != 0) {
					if(drive.searchElement(1, "/html/body/div["+aux+"]/div[2]/div/div/div/div/div/div[3]/div/div/div[3]/label/input") != 0) {
						System.out.println("Compartir publicacion completa");
						robot.pulsarTabulador();
						Thread.sleep(789);
						robot.pulsarTabulador();
						Thread.sleep(789);
						robot.pulsarTabulador();
						Thread.sleep(789);
						robot.pressSpace();
						
					}
					
					System.out.println("Darle click a publicar");
					if(drive.searchElement(1, "/html/body/div["+aux+"]/div[2]/div/div/div/div/div/div[3]/div/div/div[4]/div[2]/div/div[2]/div/div[2]/button[2]") != 0) {
						drive.clickButton(1, "/html/body/div["+aux+"]/div[2]/div/div/div/div/div/div[3]/div/div/div[4]/div[2]/div/div[2]/div/div[2]/button[2]", "Publicar xPath");
					}else if(drive.searchElement(1, "/html/body/div["+aux+"]/div[2]/div/div/div/div/div/div[3]/div/div/div[5]/div[2]/div/div[2]/div/div[2]/button[2]") != 0) {
						drive.clickButton(1, "/html/body/div["+aux+"]/div[2]/div/div/div/div/div/div[3]/div/div/div[5]/div[2]/div/div[2]/div/div[2]/button[2]", "Publicar xPath");
					}else if(drive.searchElement(1, "") != 0) {
						drive.clickButton(1, "//*[text()[contains(.,'Publicar')]]", "Publicar");
					}else {
						robot.pulsarShiftTabulador();
						Thread.sleep(789);
						robot.pulsarShiftTabulador();
						Thread.sleep(789);
						robot.pulsarShiftTabulador();
						Thread.sleep(789);
						robot.pulsarShiftTabulador();
						Thread.sleep(789);
						robot.pulsarShiftTabulador();
						Thread.sleep(789);
						robot.enter();
					}
					
					
					po.setGroups(group_c.getGroups_id());
					po.setTasks_model_id(taskModelId);
					po.setCategories_id(categoria_id);
					po.setUsers_id(idUser);
					po.setFanPage(true);
					po.insert();
					System.out.println("Guardada la publicacion en la fan page correctamente");
				}else {
					System.out.println("No se encuentra el elemento para escribir");
				}
				
			}else {
				System.out.println("No hay grupos para publicar");
			}
			
		}
	}
	
	private void searchGroup(String search) throws InterruptedException {
		System.out.println("Buscar: "+search);
		if(drive.searchElement(2, "query") != 0 ) {
			drive.inputWrite(2, "query", search,120);
		}else if(drive.searchElement(1, "/html/body/div/div/div[1]/div/form/table/tbody/tr/td[2]/input")!= 0) {
			drive.inputWrite(1, "/html/body/div/div/div[1]/div/form/table/tbody/tr/td[2]/input", search,120);
		}
		
		Thread.sleep(getNumberRandomForSecond(1540, 1980));
		//Darle click a Buscar
		if(drive.searchElement(1, "/html/body/div/div/div[1]/div/form/table/tbody/tr/td[3]/input") != 0) {
			drive.clickButton(1, "/html/body/div/div/div[1]/div/form/table/tbody/tr/td[3]/input", "Buscar xpath");
		}else if(drive.searchElement(1, "//input[text()[contains(.,'Buscar')]]") != 0) {
			drive.clickButton(1, "//input[text()[contains(.,'Buscar')]]", "Buscar");
		}else if(drive.searchElement(1, "//input[text()[contains(.,'Search')]]") != 0) {
			drive.clickButton(1, "//input[text()[contains(.,'Search')]]","Search");
		}
		System.out.println("Darle click a buscar");
		Thread.sleep(getNumberRandomForSecond(2540, 2980));
		
		//Darle click a Mas
		System.out.println("Pulsar el boton mas");
		drive.clickButton(1, "/html/body/div/div/div[2]/div[2]/div[1]/div[1]/div/a[2]", "Mas ");
		
		Thread.sleep(getNumberRandomForSecond(2540, 2980));
		System.out.println("Pulsar la opción de grupos");
		if(drive.searchElement(1, "/html/body/div/div/div[2]/div/table/tbody/tr/td/ul/li[8]/table/tbody/tr/td/a") != 0) {
			drive.clickButton(1, "/html/body/div/div/div[2]/div/table/tbody/tr/td/ul/li[8]/table/tbody/tr/td/a", "Grupos xpath");
		}else if(drive.searchElement(1, "//a[text()[contains(.,'Grupos')]]") != 0) {
			drive.clickButton(1, "//a[text()[contains(.,'Grupos')]]", "Grupos element");
		}else if(drive.searchElement(1, "//a[text()[contains(.,'Groups')]]") != 0) {
			drive.clickButton(1, "//a[text()[contains(.,'Groups')]]", "Groups element");
		}
	}
	
	private void publicGroupPublicity(int taskModelId) throws InterruptedException, SQLException {
		User_Group gro = new User_Group();
		gro.setUsers_id(idUser);
		int cantGrupo = gro.getCountGroups();
		int cantMaxPublications = 12;
		int cantiPublications = 0;
		if(cantGrupo < 1) {
			System.out.println("El usuario no tiene grupos agregados en la base de datos ");
		}else {
			Group_Categorie groC = new Group_Categorie();
			groC.setCategories_id(categoria_id);
			List<Group_Categorie> list = groC.getGroupCategorie();
				
			System.out.println("Buscar grupos segun categoria ");
			if(list.size() > 0) {
				List<Group> listG = new ArrayList<Group>();
				Group gr = new Group();
				for(Group_Categorie group : list) {
					if(cantiPublications <= cantMaxPublications) {
						String[] values = group.getName().trim().split(" ");
						String string1 = "";
						String string2 = "";
						for(int j = 0; j<values.length; j++) {
							if(j == 0) {
								string1 = values[j];
							}else if(j == 1) {
								string2 = values[j];
							}
						}
						
						System.out.println("Buscando grupo por categoria "+string1+" "+string2);
						listG = gr.getGroupNotPublication(string1, string2, idUser);
						if(listG.size() < 1) {
							System.out.println("No hay grupos a publicar para la categoria "+group.getName());
						}else {
							for(Group g : listG) {
								String urlGroup = PAGE+"groups/"+g.getGroups_id();
								String idGroups = g.getGroups_id();
								System.out.println("Publicar en el grupo "+g.getName()+ " id: "+idGroups);
								drive.goPage(urlGroup);
								
								Thread.sleep(getNumberRandomForSecond(1234, 1456));
								
								String hash = uploadImageFinal();
								cantiPublications++;
								System.out.println("El usuario hizo su publicación "+cantiPublications);
								ini++;
								if (ini >= count) {
									ini = 0;
								}
								if(!hash.isEmpty()) {
									String[] ha = hash.split(" ");
									System.out.println("Registrando post");
									
									po.setCategories_id(categoria_id);
									po.setTasks_model_id(taskModelId);
									po.setUsers_id(idUser);
									po.setGroups(idGroups);
									po.setFanPage(false);
									po.insert();

									Post_Detail poDe = new Post_Detail();
									poDe.setPosts_id(po.getLast());
									HashTag ht = new HashTag();
									ht.setCategories_id(categoria_id);
									ht.setGeneres_id(idGenere);
									System.out.println("Registrando HashTag");
									for (int j = 0; j < ha.length; j++) {

										ht.setName(ha[j]);

										poDe.setHashtag_id(ht.getIdCategorieHashTag());

										poDe.insert();
									}
									System.out.println("El usuario publico correctamente");
								}else {
									System.out.println("El usuario no publico");
								}//Fin del if para validar si se publicaron hashtag
							}
						}
					}
					
				}
			}
		}
	}
	
	
	private void reviewGroups() throws InterruptedException, SQLException {
		if(drive.searchElement(1, "//*[text()[contains(.,'Grupos')]]") != 0) {
			drive.clickButton(1, "//*[text()[contains(.,'Grupos')]]", "Grupos ");
		}else if(drive.searchElement(1, "//*[text()[contains(.,'Groups')]]") != 0) {
			drive.clickButton(1, "//*[text()[contains(.,'Groups')]]", "Grupos ");
		}
		
		if(drive.searchElement(1, "/html/body/div/div/div[2]/div/table/tbody/tr/td/div[2]/ul/li[6]/div/a") != 0 ) {
			System.out.println("Ver todos los grupos");
			pressSeeAll("/html/body/div/div/div[2]/div/table/tbody/tr/td/div[2]/ul/li[6]/div/a");
			if(readGroups("/html/body/div/div/div[2]/div/table/tbody/tr/td/div[2]/ul/li[6]/div/a")) {
				
			}
		}else if(drive.searchElement(1, "//*[text()[contains(.,'Ver todos')]]") != 0) {
			System.out.println("Ver todos los grupos");
			pressSeeAll("//*[text()[contains(.,'Ver todos')]]");
			if(readGroups("//*[text()[contains(.,'Ver todos')]]")) {
				
			}
		}else if(drive.searchElement(1, "//*[text()[contains(.,'See all')]]") != 0) {
			System.out.println("Ver todos los grupos");
			pressSeeAll("//*[text()[contains(.,'See all')]]");
			if(readGroups("//*[text()[contains(.,'See all')]]")) {
				
			}
		}else {
			if(readGroups("")) {
				
			}
		}
		
		if(drive.searchElement(1, "/html/body/div/div/div[1]/div/div/a[1]") != 0) {
			drive.clickButton(1, "/html/body/div/div/div[1]/div/div/a[1]", "Inicio xPath");
		}
	}
	
	private void pressSeeAll(String element) throws InterruptedException {
		drive.clickButton(1, element, "elemento der Ver todos");
		Thread.sleep(getNumberRandomForSecond(1265, 1980));
	}
	
	private boolean readGroups(String element) throws InterruptedException, SQLException {
		
		int quantityGroups = drive.getQuantityElements(1, "/html/body/div/div/div[2]/div/table/tbody/tr/td/div[2]/ul/li");
		
		if(quantityGroups < 1) {
			System.out.println("No hay grupos para este usuario");
			return false;
		}else {
			Group gp = new Group();
			for(int i = 1; i<= quantityGroups;i++) {
				if(!element.isEmpty()) {
					if(drive.searchElement(1, element) != 0) {
						drive.clickButton(1, element, "Elemento ver todos");
					}
				}
				String nameGroup = drive.getText(1, "/html/body/div/div/div[2]/div/table/tbody/tr/td/div[2]/ul/li["+i+"]/table/tbody/tr/td[1]/a");
				drive.clickButton(1, "/html/body/div/div/div[2]/div/table/tbody/tr/td/div[2]/ul/li["+i+"]/table/tbody/tr/td[1]/a", "Entrar en grupo xPath");
				Thread.sleep(1250);
				if(validateElementViewPost()) {
					System.out.println("Grupo a guardar " +nameGroup);
					String idGroup = getIdGroup();
					gp.setGroups_id(idGroup);
					
					gp = gp.find();
					//Si el grupo no esta en la base de datos ingresarlo
					if(gp == null) {
						gp = new Group();
						gp.setGroups_id(idGroup);
						System.out.println("Ingresar en los miembros");
						int cantMiembros = 0;

						if(drive.searchElement(1, "/html/body/div/div/div[2]/div/div[1]/div[2]/table/tbody/tr/td[2]/a") != 0) {
							drive.clickButton(1, "/html/body/div/div/div[2]/div/div[1]/div[2]/table/tbody/tr/td[2]/a", "Miembro xPath");
							Thread.sleep(getNumberRandomForSecond(1250, 1456));
							if(drive.getText(3, "u_0_0").equals("")) {
								cantMiembros = 0;
							}else {
								cantMiembros = Integer.parseInt(drive.getText(3, "u_0_0"));
							}
							if(cantMiembros == 0) {
								if(drive.getText(1, "/html/body/div/div/div[2]/div/table/tbody/tr/td/div[3]/ul/li[5]/table/tbody/tr/td[2]/span").equals("")) {
									cantMiembros = 0;
								}else {
									cantMiembros = Integer.parseInt(drive.getText(1, "/html/body/div/div/div[2]/div/table/tbody/tr/td/div[3]/ul/li[5]/table/tbody/tr/td[2]/span"));
								}
							}
							System.out.println("La cantidad de miembros son: "+cantMiembros);
							gp.setCant_miembros(cantMiembros);
							drive.back();
						}else if(drive.searchElement(1, "//*[text()[contains(.,'Miembro')]]") != 0) {
							drive.clickButton(1, "//*[text()[contains(.,'Miembro')]]", "Miembro");
							Thread.sleep(getNumberRandomForSecond(1250, 1456));
							if(drive.getText(3, "u_0_0").equals("")) {
								cantMiembros = 0;
							}else {
								cantMiembros = Integer.parseInt(drive.getText(3, "u_0_0"));
							}
							if(cantMiembros == 0) {
								cantMiembros = Integer.parseInt(drive.getText(1, "/html/body/div/div/div[2]/div/table/tbody/tr/td/div[3]/ul/li[5]/table/tbody/tr/td[2]/span"));
								if(drive.getText(1, "/html/body/div/div/div[2]/div/table/tbody/tr/td/div[3]/ul/li[5]/table/tbody/tr/td[2]/span").equals("")) {
									cantMiembros = 0;
								}else {
									cantMiembros = Integer.parseInt(drive.getText(1, "/html/body/div/div/div[2]/div/table/tbody/tr/td/div[3]/ul/li[5]/table/tbody/tr/td[2]/span"));
								}
							}
							System.out.println("La cantidad de miembros son: "+cantMiembros);
							gp.setCant_miembros(cantMiembros);
							drive.back();
						}
						gp.setName(nameGroup);
						try {
							gp.insert();
						} catch (SQLException e) {
							e.printStackTrace();
						}
						
						
						drive.back();
						Thread.sleep(1250);
					}else {
						System.out.println("Este grupo ya esta en la base de datos");
						
						drive.back();
					}
					User_Group ug = new User_Group();
					ug.setGroups_id(idGroup);
					ug.setUsers_id(idUser);
					
					ug = ug.find();
					
					if(ug == null) {
						System.out.println("El usuario no tiene este grupo en la base de datos, agregarlo");
						ug = new User_Group();
						ug.setGroups_id(idGroup);
						ug.setUsers_id(idUser);
						ug.insert();
					}else {
						System.out.println("El usuario ya tiene el grupo en la base de datos");
					}
					
				}else {
					drive.back();
					Thread.sleep(1250);
				}
			}
			return true;
		}
	}
	
	private boolean validateElementViewPost() {
		
		if (drive.searchElement(2, "xc_message") != 0) {
			return true;
		}else if(drive.searchElement(2, "view_post") != 0) {
			return true;
		}else {
			return false;
		}
	}
	
	
	
	private void publicFinal(int taskModelId) throws InterruptedException, SQLException {
		uploadImageFinal();
		ini++;
		if (ini >= count) {
			ini = 0;
		}
		
	}
	
	private void reviewMessage() throws InterruptedException {
		Thread.sleep(getNumberRandomForSecond(1250, 2563));
		// Ingresar en mensajes
		drive.clickButton(1, "/html/body/div/div/div[1]/div/div/a[3]","Mensajes xpath");
		Thread.sleep(getNumberRandomForSecond(4530, 5600));
		robot.mouseScroll(5);
		Thread.sleep(getNumberRandomForSecond(256, 1024));
		robot.mouseScroll(-5);
		Thread.sleep(getNumberRandomForSecond(256, 1024));
		if (drive.searchElement(2, "body") != 0) {
			drive.clickButton(1, "/html/body/div/div/div[1]/div/div/a[3]","Entro en mensaje directo");
		}
		robot.mouseScroll(5);
		Thread.sleep(getNumberRandomForSecond(256, 1024));
		robot.mouseScroll(-5);
		Thread.sleep(getNumberRandomForSecond(256, 1024));
		System.out.println("Contar cantidad de mensajes");
		int quantityMessage = drive.getQuantityElements(1,
				"/html/body/div/div/div[2]/div[2]/div[1]/div[2]/div/table");
		if (quantityMessage == 0) {
			System.out.println("No hay mensajes INGRESAR EN SUGERENCIAS DE MENSAJES");
			drive.clickButton(1, "/html/body/div/div/div[2]/div[2]/div[1]/div[1]/table/tbody/tr/td[1]/a","Sugerencias de mensajes xpath");
			Thread.sleep(getNumberRandomForSecond(2560, 3125));
			System.out.println("Contar cantidad de sugerencias");
			int quantitySugerence = drive.getQuantityElements(1,
					"/html/body/div/div/div[2]/div/table/tbody/tr/td/form/div[3]/ul/li");

			if (quantitySugerence == 0) {
				// Darle al boton de cancelar
				System.out.println("NO HAY SUGERENCIAS, darle al boton de cancelar");
				drive.clickButton(1, "/html/body/div/div/div[1]/table/tbody/tr/td[3]/a","Cancelar sugerencia xPath");
				Thread.sleep(getNumberRandomForSecond(2560, 3158));
			} else {
				System.out.println("Elegir una sugerencia randon para ingresar");
				int randomSugerence = getNumberRandom(1, quantitySugerence);
				// Darle click a un usuario de sugerencia
				System.out.println("Darle click al usuario de sugerencia");
				drive.clickButton(1, "/html/body/div/div/div[2]/div/table/tbody/tr/td/form/div[3]/ul/li["
						+ randomSugerence + "]/table/tbody/tr/td/a","Entrar en mensaje random");
				Thread.sleep(getNumberRandomForSecond(1250, 2654));

				// Escribir mensaje
				if(drive.searchElement(1, "//*[text()[contains(.,'No puedes responder a esta conversación.')]]") == 0) {
					System.out.println("Escribir Hola a usuario");
					if (drive.searchElement(2, "body") != 0) {
						drive.inputWrite(2, "body", "Hola",120);
					} else if (drive.searchElement(1, "composerInput") != 0) {
						drive.inputWrite(1, "composerInput", "Hola",120);
					}

					Thread.sleep(getNumberRandom(150, 980));
					System.out.println("Enviar Mensajes");
					drive.clickButton(2, "send","Enviar mensaje");

					Thread.sleep(getNumberRandomForSecond(1520, 2560));
				}
				

			}
		} else {
			System.out.println("Elegir un mensajes randon para ingresar");
			int randomMessage = getNumberRandomForSecond(1, quantityMessage);
			// Entrar en un mensaje random
			System.out.println("Ingresar en mensaje random");
			drive.clickButton(1, "/html/body/div/div/div[2]/div[2]/div[1]/div[2]/div/table[" + randomMessage
					+ "]/tbody/tr/td/div/h3[1]/a","Entrar en mensaje random");

			Thread.sleep(getNumberRandom(2540, 3001));
			// Escribir mensaje
			if(drive.searchElement(1, "//*[text()[contains(.,'No puedes responder a esta conversación.')]]") == 0) {
				System.out.println("Escribir Hola a usuario");
				if (drive.searchElement(2, "body") != 0) {
					drive.inputWrite(2, "body", "Hola",120);
				} else if (drive.searchElement(1, "composerInput") != 0) {
					drive.inputWrite(1, "composerInput", "Hola",120);
				}

				Thread.sleep(getNumberRandom(150, 980));
				System.out.println("Enviar Mensajes");
				drive.clickButton(2, "send","Enviar mensaje");

				Thread.sleep(getNumberRandomForSecond(1520, 2560));
			}
			

		}
	}
	
	private void reviewNotifaction() throws InterruptedException {
		if (drive.searchElement(1, "//*[text()[contains(.,'Notificaciones')]]") != 0) {
			drive.clickButton(1, "//*[text()[contains(.,'Notificaciones')]]","Notificaciones");
		} else if (drive.searchElement(1, "/html/body/div/div/div[1]/div/div/a[4]") != 0) {
			drive.clickButton(1, "/html/body/div/div/div[1]/div/div/a[4]","Notificaciones xPath");
		}
		
		Thread.sleep(getNumberRandom(2501, 2650));
		System.out.println("Ver mas notificaciones");
		if (drive.searchElement(1, "//*[text()[contains(.,'Ver más notificaciones')]]") != 0)
			drive.clickButton(1, "//*[text()[contains(.,'Ver más notificaciones')]]","Ver Mas Nofitificaciones ");
		Thread.sleep(getNumberRandom(2100, 2405));
		robot.mouseScroll(-8);
		Thread.sleep(getNumberRandom(1240, 1780));
		robot.mouseScroll(8);
		Thread.sleep(getNumberRandom(1240, 1780));
		System.out.println("Volver al inicio");
		drive.clickButton(1, "/html/body/div/div/div[1]/div/div/a[1]", "Inicio xPath");
	}
	
	private void userBlock(String name) {
		User_Block userB = new User_Block();
		userB.setUsers_id(idUser);
		userB.setComentario(name);
		if (userB.getIdUser() == 0) {
			userB.insert();
		}
	}

	private String validateIP() {

		try {

			URL whatismyip = new URL("http://checkip.amazonaws.com");

			BufferedReader in = new BufferedReader(new InputStreamReader(whatismyip.openStream()));

			return in.readLine();

		} catch (MalformedURLException ex) {

			System.err.println(ex);
			return "190.146.186.130";
		} catch (IOException ex) {

			System.err.println(ex);
			return "190.146.186.130";
		}

	}

	private static int getNumberRandomForSecond(int min, int max) {
		return new Random().nextInt((max - min) + 1) + min;
	}

	private static int getNumberRandom(int min, int max) {
		return (int) (Math.random() * min) + max;
	}

}
