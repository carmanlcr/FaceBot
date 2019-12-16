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


public class InicioController {

	private static DriverController drive;
	private static String[] user;
	private static User users;
	private static RobotClick robot;
	private static VpnController vpn = new VpnController();
	private List<JCheckBox> usuarios;
	private static List<JTextArea> pieDeFoto = new ArrayList<JTextArea>();
	private static List<List<String>> checkBoxHashTag = new ArrayList<List<String>>();
	private static List<JComboBox<String>> comboBoxGenere = new ArrayList<JComboBox<String>>();
	private static List<JTextField> listUsers = new ArrayList<JTextField>();
	private static int categoria_id;
	private static int idGenere;
	private int usuariosAProcesar = 1;
	private static int ini = 0;
	private int count = 0;
	private int listTask_id;
	private String groups_id;
	private boolean banderaVpn = false;
	private boolean banderaBlockeo = true;
	private Post po = new Post();

	@SuppressWarnings("static-access")
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

		for (JCheckBox jCheckBox : usuarios) {

			users = new User();
			users.setUsername(jCheckBox.getText());
			users.setEmail(jCheckBox.getText());
			user = users.getUser();
			po.setUsers_id(Integer.parseInt(user[0]));
			
			int idlistTask = po.getLastsTasktPublic();
			
			if(idlistTask == 0) {
				System.out.println("El usuario no tiene mas tareas por publicar");
			}else if(po.getCountPostUser() >= 2) {
				System.out.println("El usuario ya hizo las dos publicaciones del día");
			}else {
				String ip = validateIP();
				vpn.iniciarVpn(user[4], banderaVpn);
				String ipActual = validateIP();
				
				System.out.println(usuariosAProcesar + " usuario(s) de " + usuarios.size() + " usuario(s)");
				// Valida si la vpn conecto
				if (ip.equals(ipActual)) {
					System.err.println("El usuario " + user[1] + " no se puedo conectar a la vpn");
					usuariosAProcesar++;
				} else {
					// Setear valores a google Chrome
					drive = new DriverController();
					drive.optionsChrome();

					IniciaSesion sesion = new IniciaSesion(drive, user[1], user[3]);
					sesion.init();

					// Esperar que cargue la pagina para que cargue el dom completamente
					Thread.sleep(getNumberRandomForSecond(5250, 5650));

					System.out.println("*********************" + user[1] + "***********************");

					if (drive.searchElement(1,
							"/html/body/div/div/div[2]/div/form/div/div[2]/div[3]/table/tbody/tr/td/input") != 0) {
						userBlock("El usuario pide verificacion de telefono");
						System.out.println("Usuario bloqueado");
					} else if (drive.searchElement(1,
							"//*[text()[contains(.,'El correo electrónico que has introducido no coincide con ninguna cuenta. ')]]") != 0) {

						System.out.println("El correo electro no coincide");
					} else if (drive.searchElement(1,
							"//*[text()[contains(.,'Sigue unos pasos más para iniciar sesión')]]") != 0) {
						userBlock("El usuario pide pasos para iniciar sesion");
						System.out.println("El usuario pide verificación por catcha");
					} else if (drive.searchElement(1, "//*[text()[contains(.,'Ingresa el código a continuación')]]") != 0) {
						System.out.println("El usuario pide verificación por catcha");
						userBlock("El usuario pide verificación por catcha");
					}else if(drive.searchElement(1, "//*[text()[contains(.,'Confirma tu identidad')]]") != 0) {
						System.out.println("El usuario pide confirmación de identidad");
						userBlock("El usuario pide confirmación de identidad");
					}else if(drive.searchElement(1, "//*[text()[contains(.,'Control de seguridad')]]") != 0) {
						System.out.println("El usuario pide un control de seguridad");
						userBlock("El usuario pide un control de seguridad");
					}else if(drive.searchElement(1, "//*[text()[contains(.,'No puedes usar esta función en este momento.')]]") != 0
							|| drive.searchElement(1, "//h2[text()[contains(.,'No puedes usar esta función en este momento.')]]") != 0
							|| drive.searchElement(1, "/html/body/div/div/div[2]/div[1]/h2") != 0) {
						System.out.println("El usuario alcanzo el maximo de publicaciones al día");
						
					} else {//Si el usuario no esta bloqueado
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
								drive.clickButton(1, "html/body/div/div/div/div/div[1]/table/tbody/tr/td[3]/a", "Siguiente xPath");
							}catch(ElementClickInterceptedException e) {
								System.out.println("No se puede hacer click en el elemento Siguiente xPath");
								try {
									drive.clickButton(1, "//a[text()[contains(.,'Siguiente')]]", "Siguiente elemento a");
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
				usuariosAProcesar++;
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

	/**
	 * Inico del proceso de instagram luego que el inicio de sesión sea exitoso
	 * 
	 * @throws InterruptedException
	 * @throws SQLException
	 */
	private void startProgram(int listTask_id) throws InterruptedException, SQLException {

		System.out.println("Usuario logueado");

		robot = new RobotClick();
		Task_Model_Detail tmd = new Task_Model_Detail();
		tmd.setTasks_model_id(listTask_id);
		List<Integer> listTask = tmd.getTaskModelDetailDiferent();
		System.out.println("Buscando tarea para que el usuario realice");
		random(listTask);
		
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

		System.out.println("Se cerro la sesión del usuario " + user[1]);
	}
	
	private void random(List<Integer> listTask) throws InterruptedException, SQLException {
		// int valueScroll = (int) (Math.random() * 15) + 1;
		int taskModelId = listTask_id;
		for (Integer li : listTask) {
			Thread.sleep(getNumberRandomForSecond(2501, 2654));
			robot.mouseScroll(25);
			Thread.sleep(getNumberRandom(940, 1130));
			robot.mouseScroll(-25);
			li = 4;
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
				reviewGroups();
				break;
			case 4:
				// Publicar en Grupo
				// Ingresar en la seccion de grupos
				System.out.println("INGRESAR EN GRUPO Y PUBLICAR");
				if(pieDeFoto.get(ini).getText().isEmpty()) {
					publicGroup(taskModelId);
				}else {
					publicGroupPublicity(taskModelId);
				}
				
				break;
			case 5:
				// Publicacion final
				System.out.println("HACER PUBLICACION FINAL");
				publicFinal(taskModelId);
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
			default:
				break;
			}
			// Volver al inicio
			//drive.clickButton(1, "/html/body/div/div/div[1]/div/div/a[1]","inicio xpath");
		} // Fin del For

	}


	private String uploadImageFinal() throws InterruptedException, SQLException {

		System.out.println("Darle click a la opción de fotos");
		String hashTag = "";
		if (drive.searchElement(2, "view_photo") != 0) {
			drive.clickButton(2, "view_photo","view_photo para subir foto");
			hashTag = publicIfExistElementFoto();
			groups_id = getIdGroup();
			return hashTag;
		} else if (drive.searchElement(1, "//*[text()[contains(.,'Foto')]]") != 0) {
			drive.clickButton(2, "//*[text()[contains(.,'Foto')]]","Subir foto");
			hashTag = publicIfExistElementFoto();
			groups_id = getIdGroup();
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

	private static void uploadImage() throws InterruptedException, SQLException {
		
		System.out.println("Darle click a la opción de fotos");
		if (drive.searchElement(2, "view_photo") != 0) {
			drive.clickButton(2, "view_photo","view_photo para subir foto");
		} else if (drive.searchElement(1, "//*[text()[contains(.,'Foto')]]") != 0) {
			drive.clickButton(2, "//*[text()[contains(.,'Foto')]]","Subir foto");
		}

		Thread.sleep(getNumberRandom(2560, 4980));
		Categories ca = new Categories();
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
	}
	
	private String publicIfExistElementFoto() throws InterruptedException, SQLException {
		Thread.sleep(getNumberRandom(2562, 4980));
		String user = (String) comboBoxGenere.get(ini).getSelectedItem();
		Genere gene = new Genere();
		gene.setName(user);
		idGenere = gene.getIdGenere();
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
		String post = ph.getPhraseRandom();
		List<String> copia = checkBoxHashTag.get(ini);
		String hash = "";
		System.out.println("Elegir HashTag");
		if (copia.size() > 2) {
			
			int x = getNumberRandomForSecond(0, copia.size() - 1);
			String hashTx = copia.get(x);
			System.out.println("HashTag 1: "+hashTx);
			copia.remove(x);
			int y = getNumberRandomForSecond(0, copia.size() - 1);
			String hashTy = copia.get(y);
			System.out.println("HashTag 2: "+hashTy);
			copia.remove(y);
			int z = getNumberRandomForSecond(0, copia.size() - 1);
			String hashTz = copia.get(z);
			System.out.println("HashTag 3: "+hashTz);
			copia.remove(z);
			hash = hashTx + " " + hashTy + " " + hashTz;
		} else if (copia.size() > 0 && copia.size() < 2) {
			int x = getNumberRandomForSecond(0, copia.size() - 1);
			String hashTx = copia.get(x);
			System.out.println("HashTag 1: "+hashTx);
			copia.remove(x);
			int y = getNumberRandomForSecond(0, copia.size() - 1);
			String hashTy = copia.get(y);
			System.out.println("HashTag 2: "+hashTy);
			copia.remove(y);
			hash = hashTx + " " + hashTy;
		}

		String pieDe = pieDeFoto.get(ini).getText();
		String usuario = listUsers.get(ini).getText();
		System.out.println("Escribir el pie de la foto");
		drive.inputWrite(2, "xc_message", post + " " + pieDe + " " + usuario + " " + hash,150);

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
		/**
		 * Comunicado /html/body/div/div/div[2]/div/div[1]/div[4]/div[1]/table/tbody/tr/td[1]/h3
		 *            /html/body/div/div/div[2]/div/div[1]/div[4]/div[1]/table/tbody/tr/td[1]/h3
		 *            /html/body/div/div/div[2]/div/div[1]/div[4]/div[1]/table/tbody/tr/td[1]/h3
		 *            /html/body/div/div/div[2]/div/div[1]/div[4]/div[1]/table/tbody/tr/td[1]/h3
		 * 
		 * Historia Completa /html/body/div/div/div[2]/div/div[1]/div[5]/div[1]/div[1]/div[2]/div[2]/a[2]
		 *                   /html/body/div/div/div[2]/div/div[1]/div[5]/div[1]/div[1]/div[2]/div[2]/a[2]
		 */
		
		/**
		 * Sin comunicado
		 * 
		 * Historia completa /html/body/div/div/div[2]/div/div[1]/div[4]/div[1]/div[1]/div[2]/div[2]/a[3]
		 */
		
		/**
		 * /html/body/div/div/div[2]/div/div[1]/div[1]/div/div[1]/div[2]/p
		 */
		
		
//		if(drive.searchElement(1, "/html/body/div/div/div[2]/div/div[1]/div[4]/div[1]/table/tbody/tr/td[1]/h3") != 0) {
//			System.out.println("Existe un comunicado, pulsar en Historia completa para ver la historia que publicamos");
//			drive.clickButton(1, "/html/body/div/div/div[2]/div/div[1]/div[5]/div[1]/div[1]/div[2]/div[2]/a[2]", "Historia Completa xPath");
//			po.setLink_post(drive.getCurrentUrl());
//			
//			drive.back();
//			
//		}else {
//			System.out.println("No existe comunicado");
//			if(drive.searchElement(1, "/html/body/div/div/div[2]/div/div[1]/div[4]/div[1]/div[1]/div[2]/div[2]/a[3]") != 0) {
//				drive.clickButton(1, "/html/body/div/div/div[2]/div/div[1]/div[4]/div[1]/div[1]/div[2]/div[2]/a[3]", "Historia Completa xPath");
//			}
//			
//			po.setLink_post(drive.getCurrentUrl());
//			
//		}
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

				// html/body/div/div/div[2]/div/div[2]/div[1]/div[1]/table/tbody/tr/td[1]/a
				// html/body/div/div/div[2]/div/div[2]/div[1]/div[1]/table/tbody/tr/td["+randomPhotos+"]/a
				
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
	
	private void reviewGroups() throws InterruptedException {
		String name = JOptionPane.showInputDialog("INGRESE LA BUSQUEDA DEL GRUPO Y PULSE ACEPTAR");
		
		while(name == null || name.isEmpty()) {
			name = JOptionPane.showInputDialog("INGRESE LA BUSQUEDA DEL GRUPO Y PULSE ACEPTAR");
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
		
		//html/body/div/div/div[2]/div[2]/div[1]/div[2]/div[1]/div/div/div[1]/table/tbody/tr/td[2]/a
		//html/body/div/div/div[2]/div[2]/div[1]/div[2]/div[1]/div/div/div[1]/table/tbody/tr/td[3]/div/div/table/tbody/tr/td[2]/a
		//html/body/div/div/div[2]/div[2]/div[1]/div[2]/div[1]/div/div/div[2]/table/tbody/tr/td[2]/a
		//html/body/div/div/div[2]/div[2]/div[1]/div[2]/div[1]/div/div/div[3]/table/tbody/tr/td[2]/a
		//html/body/div/div/div[2]/div[2]/div[1]/div[2]/div[1]/div/div/div[5]/table/tbody/tr/td[3]/div/div/table/tbody/tr/td[2]/a
		
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
									//html/body/div/div/div[2]/div/table/tbody/tr/td/form/div[2]/div[3]/div[1]/div[2]/div/textarea
									//html/body/div/div/div[2]/div/table/tbody/tr/td/form/div[2]/div[3]/div[2]/div[2]/div/textarea
									
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
	private void publicGroup(int taskModelId) throws InterruptedException, SQLException {
		drive.clickButton(1, "/html/body/div/div/div[1]/div/div/a[7]","Grupos xPath");
		Thread.sleep(getNumberRandomForSecond(2540, 2980));
		System.out.println("Contar cantidad de grupos");
		int quantityGroups1 = drive.getQuantityElements(1,
				"/html/body/div/div/div[2]/div/table/tbody/tr/td/div[2]/ul/li") - 1;
		
		if (quantityGroups1 < 1) {
			System.out.println("No existen grupos para este perfil");
		} else {
			int ramdonGroups = 1;
			if(drive.searchElement(1, "/html/body/div/div/div[2]/div/table/tbody/tr/td/div[2]/ul/li[6]/div/a") != 0
				|| drive.searchElement(1, "//*[text()[contains(.,'Ver todos')]]") != 0) {
				System.out.println("El usuario tiene mas de 5 grupos");
				try {
					System.out.println("Click en Ver Todos");
					drive.clickButton(1, "/html/body/div/div/div[2]/div/table/tbody/tr/td/div[2]/ul/li[6]/div/a", "Ver todos xpath");
					Thread.sleep(2514);
					System.out.println("Contar cantidad de grupos");
					int quantityGroups2 = drive.getQuantityElements(1,
							"/html/body/div/div/div[2]/div/table/tbody/tr/td/div[2]/ul/li") - 1;
					System.out.println("Elegir grupo random");
					ramdonGroups = getNumberRandomForSecond(1, quantityGroups2);
				}catch (ElementClickInterceptedException e) {
					try {
						System.out.println("Click en Ver Todos");
						drive.clickButton(1, "//*[text()[contains(.,'Ver todos')]]", "Ver todos");
						Thread.sleep(2514);
						System.out.println("Contar cantidad de grupos");
						int quantityGroups2 = drive.getQuantityElements(1,
								"/html/body/div/div/div[2]/div/table/tbody/tr/td/div[2]/ul/li") - 1;
						System.out.println("Elegir grupo random");
						ramdonGroups = getNumberRandomForSecond(1, quantityGroups2);
					}catch (ElementClickInterceptedException e2) {
						e.getStackTrace();
					}
					
				}
				
			}else {
				//html/body/div/div/div[2]/div/table/tbody/tr/td/div[2]/ul/li[7]/table/tbody/tr/td[1]/a
				//html/body/div/div/div[2]/div/table/tbody/tr/td/div[2]/ul/li[12]/table/tbody/tr/td[1]/a
				System.out.println("Elegir grupo random");
				ramdonGroups = getNumberRandomForSecond(1, quantityGroups1);
			}
			
			groups_id = "";
			// Entrar en un grupo
			System.out.println("Entrar en grupo");
			drive.clickButton(1, "/html/body/div/div/div[2]/div/table/tbody/tr/td/div[2]/ul/li[" + ramdonGroups
					+ "]/table/tbody/tr/td[1]/a","Entrar en grupo random");
			Thread.sleep(getNumberRandom(2490, 3540));
			robot.mouseScroll(-15);
			Thread.sleep(150);
			robot.mouseScroll(15);
			String hashTag = uploadImageFinal();
			ini++;
			if (ini >= count) {
				ini = 0;
			}
			
			if(!hashTag.isEmpty()) {
				String[] ha = hashTag.split(" ");
				System.out.println("Registrando post");
				
				po.setCategories_id(categoria_id);
				po.setTasks_model_id(taskModelId);
				po.setUsers_id(Integer.parseInt(user[0]));
				po.setGroups(groups_id);
				po.insert();

				Post_Detail poDe = new Post_Detail();
				poDe.setPosts_id(po.getLast());
				HashTag ht = new HashTag();
				ht.setCategories_id(categoria_id);
				ht.setGeneres_id(idGenere);
				System.out.println("Registrando HashTag");
				for (int i = 0; i < ha.length; i++) {

					ht.setName(ha[i]);

					poDe.setHashtag_id(ht.getIdCategorieHashTag());

					poDe.insert();
				}
				System.out.println("El usuario publico correctamente");
			}else {
				System.out.println("El usuario no publico");
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
		Group_Categorie group = new Group_Categorie();
		group.setCategories_id(categoria_id);
		group = group.getGroupSearch();
		
		searchGroup(group.getName());
		
		validateGroups();
		
	}
	
	private void validateGroups() throws InterruptedException {
		int quantityGroups = drive.getQuantityElements(1, "/html/body/div/div/div[2]/div[2]/div[1]/div[2]/div[1]/div/div/div");
		
		if(quantityGroups < 1) {
			System.out.println("No existen grupos para publicar");
		}else {
			for(int i = 1; i<=quantityGroups; i++) {
				if(drive.searchElement(1, 
						"/html/body/div/div/div[2]/div[2]/div[1]/div[2]/div[1]/div/div/div["+i+"]/table/tbody/tr/td[3]/div/div/table") != 0) {
					
				}else {
					if(drive.searchElement(1, 
							"/html/body/div/div/div[2]/div[2]/div[1]/div[2]/div[1]/div/div/div["+i+"]/table/tbody/tr/td[2]/a/div[2]/abbr") != 0) {
						
						if(drive.searchElement(1, "/html/body/div/div/div[2]/div[2]/div[1]/div[2]/div[1]/div/div/div["+i+"]/table/tbody/tr/td[2]/a") != 0) {
							System.out.println("Entrar en grupo");
							drive.clickButton(1, "/html/body/div/div/div[2]/div[2]/div[1]/div[2]/div[1]/div/div/div["+i+"]/table/tbody/tr/td[2]/a", "Entrar en grupo");
							Thread.sleep(getNumberRandom(1263, 1890));
							
							Post pos = new Post();
							groups_id = getIdGroup();
							pos.setGroups(groups_id);
							pos = pos.getPostForGroup();
							
							if(pos.getPosts_id() != 0 || pos.getGroups() != null) {
								drive.back();
							}else {
								if(validateElementViewPost()) {
									uploadLink();
									System.out.println("Se publico en grupo");
									po.setUsers_id(Integer.parseInt(user[0]));
									po.setCategories_id(categoria_id);
									po.setTasks_model_id(listTask_id);
									po.setLink_post(drive.getCurrentUrl());
									po.setGroups(groups_id);
									po.insert();
									
									ini++;
									if (ini >= count) {
										ini = 0;
									}
								}
								
								break;
							}
							
						}
					}
				}
			}
		}
		
	}
	
	private boolean validateElementViewPost() {
		
		if (drive.searchElement(2, "xc_message") != 0) {
			return true;
		}else if(drive.searchElement(2, "view_post") != 0) {
			return true;
		} else if (drive.searchElement(1, "//*[text()[contains(.,'Foto')]]") != 0) {
			return true;
		}else {
			return false;
		}
	}
	
	private void uploadLink() throws InterruptedException {
		Thread.sleep(getNumberRandom(2562, 4980));
		String user = (String) comboBoxGenere.get(ini).getSelectedItem();
		Genere gene = new Genere();
		gene.setName(user);
		idGenere = gene.getIdGenere();


		drive.inputWrite(2, "xc_message", pieDeFoto.get(ini).getText(),12);
		
		Thread.sleep(getNumberRandomForSecond(2250, 3100));
		
		drive.clickButton(2, "view_post", "view_post publicar name");
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
				System.out.println("Escribir Hola a usuario");
				if (drive.searchElement(2, "body") != 0) {
					drive.inputWrite(2, "body", "Hola",120);
				} else if (drive.searchElement(1, "composerInput") != 0) {
					drive.inputWrite(1, "composerInput", "Hola",120);
				}

				Thread.sleep(getNumberRandom(150, 980));
				System.out.println("Enviar Mensajes");
				drive.clickButton(2, "Send","Enviar mensaje");

				Thread.sleep(getNumberRandomForSecond(1520, 2560));

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
			System.out.println("Escribir Hola Random");
			drive.inputWrite(2, "body", "Hola",120);

			Thread.sleep(getNumberRandom(150, 980));
			System.out.println("Pulsar Enviar");
			drive.clickButton(2, "send","Enviar mensaje");

			Thread.sleep(getNumberRandomForSecond(1520, 2560));

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
		userB.setUsers_id(Integer.parseInt(user[0]));
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
