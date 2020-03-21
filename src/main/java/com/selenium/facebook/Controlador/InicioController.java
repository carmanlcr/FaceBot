package com.selenium.facebook.Controlador;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

import javax.swing.JOptionPane;

import org.openqa.selenium.ElementClickInterceptedException;

import com.selenium.facebook.Modelo.*;

import configurations.controller.VpnController;



public class InicioController {
	private static final String PAGE = "https://mbasic.facebook.com/";
	protected static final  String PATH_IMAGE_DOWNLOAD_FTP = "C:\\imagesSftp\\";
	private configurations.controller.DriverController drive;
	private User users;
	private configurations.controller.RobotController robot;
	private List<String> listCheckBoxUsers = new ArrayList<>();
	private int categoria_id;
	private int idUser;
	private int tasks_grid_id;
	private String phrase;
	private String image;
	private String fullName;
	private int idGenere;
	private boolean isAddGroups;
	private boolean isAddFriends;
	private boolean isGroupsInSpanish;
	private int quantity_groups;
	private int quantity_min;
	private int cantGroupsAdd;
	private boolean banderaBlockeo = true;
	private boolean isFanPage;
	private boolean isGroups;
	private boolean fanPage;
	private Post po = new Post();
	private Date date = new Date();
	private SimpleDateFormat simpleFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

	
	public InicioController(int categoria_id, List<String> listCheckBoxUsers, boolean isFanPage) {
		this.listCheckBoxUsers = listCheckBoxUsers;
		this.categoria_id = categoria_id;
		this.fanPage = isFanPage;
	}

	public void init() throws InterruptedException, SQLException, IOException {
		int usuariosAProcesar = 0;
		Task_Grid taskG = new Task_Grid();
		boolean fanP = this.fanPage;
		taskG.setCategories_id(this.categoria_id);
		taskG.setFanPage(fanP);
		List<Task_Grid> listTask = taskG.getTaskGridToday();
		if(listTask.isEmpty()) {
			JOptionPane.showMessageDialog(null, "Ya no quedan tareas para hoy");
		}else {
			for (String list : listCheckBoxUsers) {
				VpnController vpn = null;
				usuariosAProcesar++;
				users = new User();
				users.setUsername(list);
				users.setEmail(list);
				users = users.getUser();
				cantGroupsAdd = 0;
				System.out.println(usuariosAProcesar + " usuario(s) de " + listTask.size() + " usuario(s)");
				System.out.println("*********************" + users.getUsername() + "***********************");
				int idlistTask = po.getLastsTasktPublic();
				idUser = users.getUsers_id();
				fullName = users.getFull_name();
				taskG = new Task_Grid();
				taskG = taskG.getTaskForUser(idUser,fanP);
				tasks_grid_id = taskG.getTasks_grid_id();
				if(!users.isBlock()) {
					po.setUsers_id(idUser);
					
					
					if(taskG != null) {
						String dateCu = simpleFormat.format(date);
						while(dateCu.compareTo(taskG.getDate_publication()) < 0) {
							Thread.sleep(5000);
							date = new Date();
							dateCu = simpleFormat.format(date);
						}
						idGenere = taskG.getGeneres_id();
						phrase = taskG.getPhrase();
						image = taskG.getImage();
						isFanPage = taskG.isFanPage();
						isGroups = taskG.isGroups();
						quantity_groups = taskG.getQuantity_groups();
						quantity_min = taskG.getQuantity_min();
						isAddGroups = taskG.isAddGroups();
						isAddFriends = taskG.isAddFriends();
						isGroupsInSpanish = taskG.isGroupsInSpanish();
						
						
						if(idlistTask == 0) {
							System.out.println("El usuario no tiene mas tareas por publicar");
						}else {
							String ip = validateIP("");
							robot = new configurations.controller.RobotController();
							String ipActual = "01.02.03.04";
							if(users.getVpn_id() != 0) {
								Vpn v = new Vpn();
								v.setVpn_id(users.getVpn_id());
								v = v.getVpn();
								vpn = new VpnController(v.getName());
								vpn.connectVpn();
								ipActual = validateIP(ip);
							}
							
							
							// Valida si la vpn conecto
							if (ip.equals(ipActual)) {
								System.err.println("El usuario " + users.getUsername() + " no se puedo conectar a la vpn");
							} else {
								// Setear valores a google Chrome
								drive = new configurations.controller.DriverController();
								drive.optionsChrome();
								
								IniciaSesion sesion = new IniciaSesion(drive, users.getUsername(), users.getPassword());
								sesion.init();

								// Esperar que cargue la pagina para que cargue el dom completamente
								Thread.sleep(getNumberRandomForSecond(5250, 5650));
								
								if (!validateBlockOUserIncorrect(idlistTask)) {
									for(int i = 0; i<6;i++) {
										validateOpcionBeforeInitSesion();
									}
									startProgram(idlistTask);
								}

							} // fin del else

							// Desconectar la vpn para el siguiente usuario
							if (drive != null) {
								drive.quit();
							}
							if(vpn != null) {
								vpn.disconnectVpn();	
							}
							Thread.sleep(getNumberRandomForSecond(1999, 2125));

							}//El usuario tiene tareas por hacer
					}//Si el usuario tienes tarea por publicar
				}//Fin del if si el usuario no esta bloqueado
				else {
					System.out.println("Usuario bloqueado");
					addPostMadurate(idlistTask);
				}
				
			}
		}
		
		System.out.println("Finalizo con exito el programa");
		System.exit(1);
			
	}// Fin del init
	
	private boolean validateBlockOUserIncorrect(int idlistTask) {
		if(drive.searchElement(1,
				"/html/body/div/div/div[2]/div/form/div/div[2]/div[3]/table/tbody/tr/td/input") != 0) {
			userBlock("El usuario pide verificacion de telefono");
			System.out.println("Usuario bloqueado");
			addPostMadurate(idlistTask);
			return true;
		}else if (drive.searchElement(1,
				"//*[text()[contains(.,'El correo electrónico que has introducido no coincide con ninguna cuenta. ')]]") != 0) {
			System.out.println("El correo electro no coincide");
			return true;
		} else if (drive.searchElement(1,
				"//*[text()[contains(.,'Sigue unos pasos más para iniciar sesión')]]") != 0) {
			userBlock("El usuario pide pasos para iniciar sesion");
			System.out.println("El usuario pide verificación por catcha");
			addPostMadurate(idlistTask);
			return true;
		} else if (drive.searchElement(1, "//*[text()[contains(.,'Ingresa el código a continuación')]]") != 0) {
			System.out.println("El usuario pide verificación por catcha");
			userBlock("El usuario pide verificación por catcha");
			addPostMadurate(idlistTask);
			return true;
		}else if(drive.searchElement(1, "//*[text()[contains(.,'Confirma tu identidad')]]") != 0) {
			System.out.println("El usuario pide confirmación de identidad");
			userBlock("El usuario pide confirmación de identidad");
			addPostMadurate(idlistTask);
			return true;
		}else if(drive.searchElement(1, "//*[text()[contains(.,'Control de seguridad')]]") != 0) {
			System.out.println("El usuario pide un control de seguridad");
			userBlock("El usuario pide un control de seguridad");
			addPostMadurate(idlistTask);
			return true;
		}else if(drive.searchElement(1, "//*[text()[contains(.,'No puedes usar esta función en este momento.')]]") != 0
				|| drive.searchElement(1, "//h2[text()[contains(.,'No puedes usar esta función en este momento.')]]") != 0
				|| drive.searchElement(1, "/html/body/div/div/div[2]/div[1]/h2") != 0) {
			System.out.println("No puedes usar esta función en este momento");
			addPostMadurate(idlistTask);
			return true;
		}else if(drive.searchElement(1, "/html/body/div/div/div/div/table[1]/tbody/tr/td[2]/div") != 0
				|| drive.searchElement(1, "//*[text()[contains(.,'Reconocimiento facial en Facebook')]]") != 0) {
			userBlock("Reconocimiento facial en facebook");
			System.out.println("El usuario pide reconocimiento facial");
			addPostMadurate(idlistTask);
			return true;
		}else if(drive.searchElement(1, "/html/body/div/div/div[2]/div/form/div/div[2]/div[1]/div[2]/div/div[1]/div") != 0 
				|| drive.searchElement(1, "//*[text()[contains(.,'Completa los siguientes pasos para iniciar sesión')]]") != 0) {
			userBlock("Completa los siguientes pasos para iniciar sesión");
			System.out.println("Completa los siguientes pasos para iniciar sesión");
			addPostMadurate(idlistTask);
			return true;
		}else if(drive.searchElement(1, "/html/body/div/div/div[2]/div/form/div/div[2]/div[1]/div[2]/span/div") != 0) {
			userBlock("La cuenta esta inhabilitada");
			System.out.println("La cuenta esta inhabilitada");
			addPostMadurate(idlistTask);
			return true;
		}else if(drive.searchElement(1, "//*[text()[contains(.,'Has usado una contraseña antigua. Si has olvidado tu contraseña actual, puedes solicitar nueva.')]]") != 0
				|| drive.searchElement(1, "/html/body/div/div/div[2]/div/table/tbody/tr/td/div[1]/div") != 0) {
			System.out.println("La contraseña es una antigua");
			userBlock("La contraseña es una antigua");
			return true;
		}else if(drive.searchElement(1, "//*[text()[contains(.,'Just a few more steps before you log in')]]") != 0) {
			System.out.println("La cuenta esta bloqueada");
			userBlock("La cuenta esta bloqueada");
			return true;
		}
		return false;
	}
	
	private void validateOpcionBeforeInitSesion() throws InterruptedException {
		Thread.sleep(700);
		if (drive.searchElement(1, "/html/body/div/div/div/div/table/tbody/tr/td/div/div[3]/a") != 0) {
			drive.clickButton(1, "/html/body/div/div/div/div/table/tbody/tr/td/div/div[3]/a","Ahora no");
		}

		if (drive.searchElement(1, "/html/body/div/div/div[2]/div/table/tbody/tr/td/div/div[3]/a") != 0) {
			drive.clickButton(1, "/html/body/div/div/div[2]/div/table/tbody/tr/td/div/div[3]/a","Ahora no");
		}
		
		if(drive.searchElement(1, "//*[text()[contains(.,'Siguiente')]]") != 0) {
			try {
				drive.clickButton(1, "//*[text()[contains(.,'Siguiente')]]", "Siguiente");
			}catch(ElementClickInterceptedException e) {
				System.out.println("No se puede hacer click en este elemento");
			}
		}
		
		disabledFacialRecognitive();
		
		acceptInitSession();
	}
	
	private void disabledFacialRecognitive() throws InterruptedException {
		if(drive.searchElement(1, "//*[text()[contains(.,'Reconocimiento facial en Facebook')]]") != 0) {
			Thread.sleep(850);
			if(drive.searchElement(1, "/html/body/div/div/div/div/table[2]/tbody/tr/td/div[2]/a") != 0) {
				
				drive.clickButton(1, "/html/body/div/div/div/div/table[2]/tbody/tr/td/div[2]/a", "Continuar xPath");
			}else if(drive.searchElement(1, "//*[text()[contains(.,'Continuar')]]") != 0) {
				drive.clickButton(1, "//*[text()[contains(.,'Continuar')]]", "Continuar xPath");
				
			}
			
			Thread.sleep(850);
			
			if(drive.searchElement(1, "//*[text()[contains(.,'Mantener desactivada')]]")!= 0) {
				drive.clickButton(1, "//*[text()[contains(.,'Mantener desactivada')]]","Mantener desactivada");
				
			}else if(drive.searchElement(1, "/html/body/div/div/div/div/table[2]/tbody/tr/td/div[2]/div[2]/a") != 0) {
				drive.clickButton(1, "/html/body/div/div/div/div/table[2]/tbody/tr/td/div[2]/div[2]/a","Mantener desactivada xpath");
			}

			if(drive.searchElement(1, "//*[text()[contains(.,'Cerrar')]]") != 0) {
				drive.clickButton(1, "//*[text()[contains(.,'Cerrar')]]", "Cerrar xPath");
			}

		}
	}
	
	private void acceptInitSession() throws InterruptedException {
		if(drive.searchElement(3,"checkpointSubmitButton-actual-button") !=0) {
			System.out.println("Pulsar Si 1");
			drive.clickButton(3, "checkpointSubmitButton-actual-button", "Si Id");
			Thread.sleep(2500);
			
			validateIfExistChangePassword();
		}else if(drive.searchElement(2, "submit[Yes]") != 0) {
			System.out.println("Pulsar Si 2");
			drive.clickButton(2, "submit[Yes]","Si name");
			Thread.sleep(2500);
			validateIfExistChangePassword();
		}else if(drive.searchElement(1, "//*[text()[contains(.,'Sí')]]") != 0) {
			System.out.println("Pulsar Si 3");
			drive.clickButton(1, "//*[text()[contains(.,'Sí')]]", "Si xPath 1");
			Thread.sleep(2500);
			validateIfExistChangePassword();
		}else if(drive.searchElement(1, "/html/body/div/div/div[2]/div/form/div/div[2]/div[2]/table/tbody/tr/td[2]/input") != 0) {
			System.out.println("Pulsar Si 4");
			drive.clickButton(1, "/html/body/div/div/div[2]/div/form/div/div[2]/div[2]/table/tbody/tr/td[2]/input", "Si xPath 2");
			Thread.sleep(2500);
			validateIfExistChangePassword();
		}
	}
	
	private void validateIfExistChangePassword() throws InterruptedException {
		String[] nm = fullName.trim().split(" ");
		String passwordNew = generatePassword(nm[0]+nm[1]);
		if(drive.searchElement(2, "password_new") != 0) {
			System.out.println("Escribir password 1");
			drive.inputWrite(2, "password_new", passwordNew, 99);
			Thread.sleep(1500);
			
			clickNext(passwordNew);
		}else if(drive.searchElement(1, "/html/body/div/div/div[2]/div/form/div/div[2]/div[1]/fieldset/div[2]/input") != 0) {
			System.out.println("Escribir password 2");
			drive.inputWrite(1, "/html/body/div/div/div[2]/div/form/div/div[2]/div[1]/fieldset/div[2]/input", passwordNew, 99);
			Thread.sleep(1500);
			clickNext(passwordNew);
		}
	}
	
	private String generatePassword(String input) {
		List<Character> characters = new ArrayList<>();
        for(char c:input.toCharArray()){
            characters.add(c);
        }
        StringBuilder output = new StringBuilder(input.length());
        while(!characters.isEmpty()){
            int randPicker = (int)(Math.random()*characters.size());
            output.append(characters.remove(randPicker));
        }
        return output.toString();
	}
	
	private void clickNext(String passwordNew) throws InterruptedException {
		if(drive.searchElement(2, "submit[Next]") != 0) {
			System.out.println("Pulsar next 1");
			drive.clickButton(2, "submit[Next]", "siguiente Next");
		}else if(drive.searchElement(3, "checkpointSubmitButton-actual-button") != 0) {
			System.out.println("Pulsar next 2");
			drive.clickButton(3, "checkpointSubmitButton-actual-button","Siguiente id");
		}else if(drive.searchElement(1, "//*[text()[contains(.,'Siguiente')]]") != 0) {
			System.out.println("Pulsar next 3");
			drive.clickButton(1, "//*[text()[contains(.,'Siguiente')]]", "Siguiente xPath 1");
		}else if(drive.searchElement(1, "/html/body/div/div/div[2]/div/form/div/div[2]/div[2]/table/tbody/tr/td/input") != 0) {
			System.out.println("Pulsar next 4");
			drive.clickButton(1, "/html/body/div/div/div[2]/div/form/div/div[2]/div[2]/table/tbody/tr/td/input", "Siguiente xPath 2");
		}
		
		Thread.sleep(2500);
		
		User user = new User();
		user.setUsers_id(idUser);
		user.setActive(true);
		user.setUsername(users.getUsername());
		user.setEmail(users.getEmail());
		user.setPassword(passwordNew);
		user.setVpn_id(users.getVpn_id());
		try {
			user.update();
			System.out.println("Actualizada la contraseña correctamente");
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		
		
	}

	/**
	 * Inico del proceso de instagram luego que el inicio de sesión sea exitoso
	 * 
	 * @throws InterruptedException
	 * @throws SQLException
	 */
	private void startProgram(int listTaskId) throws InterruptedException, SQLException {

		System.out.println("Usuario logueado");

		
		Task_Model_Detail tmd = new Task_Model_Detail();
		tmd.setTasks_model_id(listTaskId);
		List<Integer> listTask = tmd.getTaskModelDetailDiferent();
		System.out.println("Buscando tarea para que el usuario realice");
		random(listTask, listTaskId);

		
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
			
			if(!isFanPage && !isGroups) {
				addPostMadurate(listTaskId);
			}
		}

		System.out.println("Se cerro la sesión del usuario " + users.getUsername());
	}
	
	private void addPostMadurate(int listTaskId) {
		Post pt = new Post();
		pt.setCategories_id(categoria_id);
		pt.setFanPage(false);
		pt.setGroups("0");
		pt.setMaduration(true);
		pt.setTasks_model_id(listTaskId);
		pt.setTasks_grid_id(tasks_grid_id);
		pt.setUsers_id(idUser);
		pt.insert();
	}
	
	
	private void random(List<Integer> listTask, int listTaskId) throws InterruptedException, SQLException {
		for (Integer li : listTask) {
			drive.goPage(PAGE);
			Thread.sleep(getNumberRandomForSecond(2501, 2654));
			int randomScroll = getNumberRandomForSecond(15, 30);
			robot.mouseScroll(randomScroll);
			Thread.sleep(getNumberRandom(940, 1130));
			likePublication();
			robot.mouseScroll(randomScroll * (-1));
			Thread.sleep(getNumberRandom(940, 1130));
			taskAthand(li, listTaskId);
			
		} // Fin del For

	}

	private void taskAthand(Integer task,int listTaskId) throws InterruptedException, SQLException {
		switch (task) {
		
			case 1:
				// Entrar en Editar Perfil
				System.out.println("ENTRAR EN PERFIL Y DAR LIKE A FOTO");
				likeUsers();
				break;
			case 2:
				System.out.println("SUBIR IMAGEN NORMAL O POST");
				addPostNormal();
				break;
			case 3:
				// Revisar los grupos
				// Ingresar en la seccion de grupos
				if(isAddGroups) {
					System.out.println("BUSCAR GRUPO Y AGREGARSE");
					addGroup();
				}
				break;
			case 4:
				// Publicar en Grupo
				// Ingresar en la seccion de grupos
				ifPublicationInFanPage(listTaskId);
				
				ifPublicationInGroup(listTaskId);
					
				break;
			case 5:
				// Publicacion final
				System.out.println("HACER PUBLICACION FINAL");
				Genere gene1 = new Genere();
				gene1.setGeneres_id(idGenere);
				gene1 = gene1.getGenereWithPhrasesPhotosHashtag();
				if(gene1 != null) {
					publicFinal();
				}else {
					System.out.println("NO TIENE FRASE, FOTO O HASHTAG PARA PUBLICAR");
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
				System.out.println("LEER TODOS LOS GRUPOS DEL USUARIO");
				reviewGroups();
				break;
			case 9:
				if(isAddFriends) {
					System.out.println("AGREGAR Y ACEPTAR NUEVOS USUARIOS");
					addAndAceptedNewUsers();
				}
				break;
			case 10:
				commentsPost();
			default:
				break;
		}
	}
	
	
	private void likePublication() throws InterruptedException {
		if(drive.searchElement(1, "//*[text()[contains(.,'Me gusta')]]") != 0) {
			drive.clickButton(1, "//*[text()[contains(.,'Me gusta')]]", "Me gusta xPath");
			Thread.sleep(800);
			if(drive.searchElementAppium(1, "/html/body/div/div/div[1]/table/tbody/tr/td[1]/a") != 0) {
				drive.goPage(PAGE);
			}else if(drive.searchElement(1, "//*[text()[contains(.,'Elige una reacción')]]") != 0) {
				drive.goPage(PAGE);
			}
		}
	}

	private void ifPublicationInFanPage(int listTaskId) throws InterruptedException {
		if(isFanPage) {
			System.out.println("INGRESAR EN FAN PAGE Y COMPARTIR");
			Genere gene = new Genere();
			gene.setGeneres_id(idGenere);
			gene = gene.getFanPage();
	
			if(gene != null) {
				boolean trash = gene.isTrash();
				for(int i =1; i<= quantity_groups; i++) {
					goFanPage(gene.getFan_page());
					publicGroup(listTaskId,trash);
				}
				
			}
		}
	}
	
	
	private void ifPublicationInGroup(int listTaskId) throws InterruptedException, SQLException {
		if(isGroups) {
			Genere gene = new Genere();
			gene.setGeneres_id(idGenere);
			gene = gene.getGenere();
			
			SftpController sftp = new SftpController();
			System.out.println("Descagando imagen "+image);
			sftp.downloadFileSftp(image);
			//Si el genero esta clasificado como basura ingresar entre 9 y 14 
			//grupos y publicar
			if(gene != null && gene.isTrash()) {
				System.out.println("PUBLICAR EN GRUPOS RANDOM");
				publicGroupTrash(listTaskId);
			}else {
				System.out.println("PUBLICAR EN GRUPOS SEGUN CATEGORIA");
				publicGroupPublicity(listTaskId);
			}
		}
	}
	
	private void uploadImageFinal() throws InterruptedException {

		System.out.println("Darle click a la opción de fotos");
		if (drive.searchElement(2, "view_photo") != 0) {
			System.out.println("Subir foto view_photo");
			drive.clickButton(2, "view_photo","view_photo para subir foto");
			publicIfExistElementFoto();
		} else if (drive.searchElement(1, "//*[text()[contains(.,'Foto')]]") != 0) {
			System.out.println("Subir foto xPath");
			drive.clickButton(2, "//*[text()[contains(.,'Foto')]]","Subir foto");
			publicIfExistElementFoto();
		}else {
			System.out.println("No se puede publicar en este grupo");
			
		}
		
	}
	
	private String getIdGroup() {
		String urlImage = drive.getCurrentUrl();
		StringBuilder idGroup = new StringBuilder();
		urlImage = urlImage.substring(35);
		for(int j = 0; j<urlImage.length(); j++) {
			if(urlImage.substring(j,j+1).equals("?")) {
				break;
			}else {
				idGroup.append(urlImage.substring(j,j+1));
			}
		}
		
		return idGroup.toString();
	}
	
	private void publicGroupTrash(int listTaskId) throws InterruptedException, SQLException {
		User_Group gro = new User_Group();
		gro.setUsers_id(idUser);
		int cantGrupo = gro.getCountGroups();
		
		if(cantGrupo < 1) {
			System.out.println("El usuario no tiene grupos agregados en la base de datos ");
		}else {
			System.out.println("El usuario tiene "+cantGrupo+" registrados.");
			int groupsPublication = quantity_groups;
			System.out.println("Buscando "+groupsPublication+" para publicar");
			List<Group> listGroups = gro.getGroupNotPublication(groupsPublication);
			System.out.println("La cantidad de grupos son "+listGroups.size());
			if(listGroups.isEmpty()) {
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
					
					uploadImageFinal();
					System.out.println("Registrando post");
					
					po.setCategories_id(categoria_id);
					po.setTasks_model_id(listTaskId);
					po.setTasks_grid_id(tasks_grid_id);
					po.setUsers_id(idUser);
					po.setMaduration(false);
					po.setGroups(idGroups);
					po.setFanPage(false);
					searchLinkPublication(idGroups);

						System.out.println("El usuario publico correctamente");
				}
			}
		}
	}
	
	private void addPostNormal() throws InterruptedException, SQLException {
		int random = getNumberRandomForSecond(1, 2);
		if(random == 1) {
			uploadImage();
		}else {
			addPostWrite();
		}
	}

	private void uploadImage() throws InterruptedException, SQLException {
		
		if(ifElementPhotoExist()) {
			Thread.sleep(getNumberRandom(2560, 4980));
			Categorie ca = new Categorie();
			List<Integer> la = ca.getSubCategorieConcat();
			Path_Photo pathPhoto = new Path_Photo();
			pathPhoto.setCategories_id(la.get(0));
			pathPhoto.setSub_categories_id(la.get(1));
			System.out.println("Extraer el path de la foto");
			String path = pathPhoto.getPathPhotos();
			System.out.println("Colocar el path de la foto en el file");
			try {
				drive.inputWriteFile(2, "file1", path);
			}catch (org.openqa.selenium.NoSuchElementException e) {
				drive.inputWrite(1, "/html/body/div/div/div[2]/div/table/tbody/tr/td/form/div[1]/div/input[1]", path, 115);
			}
			Thread.sleep(getNumberRandomForSecond(1201, 1899));
			System.out.println("Darle click al boton siguiente para escribir el pie de foto");
			if(drive.searchElement(2, "add_photo_done") != 0) {
				drive.clickButton(2, "add_photo_done","Darle click a siguiente para subir foto");
			}else if(drive.searchElement(1, "//*[text()[contains(.,'Previsualizar')]]") != 0) {
				drive.clickButton(1, "//*[text()[contains(.,'Previsualizar')]]","Darle click a siguiente para subir foto");
				
			}
			

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
			
			skipAddComplementPhoto();
		}else {
			System.out.println("No se le puede dar click a la opcion de fotos");
		}
		
	}
	
	private boolean ifElementPhotoExist() {
		if (drive.searchElement(2, "view_photo") != 0) {
			System.out.println("Subir foto view_photo");
			drive.clickButton(2, "view_photo","view_photo para subir foto");
			return true;
		} else if (drive.searchElement(1, "//*[text()[contains(.,'Foto')]]") != 0) {
			System.out.println("Subir foto xpath");
			drive.clickButton(1, "//*[text()[contains(.,'Foto')]]","Subir foto");
			return true;
		}
		return false;
	}
	
	private void skipAddComplementPhoto() {
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
	
	private void addPostWrite() throws InterruptedException {
		System.out.println("ESCRIBIR UN POST NORMAL");
		String[] array = {"Que bonito día!","Los politicos no sirven -.-","No me lo creo","Que genial idea jajaja",
				"Genocidio y patriarcado","¿Quien me quiere y para que?","Alguién por aquí?","Que agradable sujeto","Esa noticia me dejo en shock",
				"Jajajajajajajajajajajaj","Todos vamos al mismo camino.","Es irrelevante eso que me dices","Si no te agrado te vas",
				"Para que todo este reguero","Tusa","Really?","Cantaclaro","Simon Diaz","No es copia, jamas.",
				"Soy de las personas que prefieren netflix que estar en una rumba","¿Tu mamá sabe que usas el internet para dejarme en visto?",
				"Amo la lluvia","Odio los climas calientes","Perreo catolico","El celular nunca puede ser mas importante que la persona con la que estas comiendo.",
				"No!","O quizás si jejeje","Que cancion tan genial","Sos la hostia macho","¿Que te pasa?",
				"¿Pensando en mi o ke ase?","Choca, choca, choca, choca","Fantasmas del pasado viven en mi presente, pero no atormentaran mi futuro",
				"Casi lo logro","Es inminente","Like y te escribo al privado","Like y te paso una foto","Like si like",
			    "Never ever","Nunca digas nunca","Preguntale a las familias de Libia si alivia leer la biblia ahora que lidian con la desidia",
			    "La única ex que vuelve contigo es la excusa"};
		
		if(drive.searchElement(2, "xc_message") != 0) {
			drive.inputWrite(2, "xc_message", array[getNumberRandomForSecond(0, array.length -1)],114);
			
			Thread.sleep(1000);
			
			if(drive.searchElement(2, "view_post") != 0) {
				drive.clickButton(2, "view_post", "Publicar name");
			}else if(drive.searchElement(1, "//*[text()[contains(.,'Publicar')]]") != 0) {
				drive.clickButton(1, "//*[text()[contains(.,'Publicar')]]", "Publicar xPath");
			}
			
			Thread.sleep(1000);
		}
	}
	
	private void publicIfExistElementFoto() throws InterruptedException {
		Thread.sleep(getNumberRandom(2562, 3279));
		System.out.println("Extraer el path de la foto");
		String path = PATH_IMAGE_DOWNLOAD_FTP+image;
		System.out.println("Colocar el path de la foto en el file");
		try {
			drive.inputWriteFile(2, "file1", path);
		}catch (org.openqa.selenium.NoSuchElementException e) {
			drive.inputWrite(1, "/html/body/div/div/div[2]/div/table/tbody/tr/td/form/div[1]/div/input[1]", path, 115);
		}
		
		Thread.sleep(getNumberRandomForSecond(1201, 1899));
		System.out.println("Darle click al boton siguiente para escribir el pie de foto");
		if(drive.searchElement(2, "add_photo_done") != 0) {
			drive.clickButton(2, "add_photo_done","Darle click a siguiente para subir foto");
		}else if(drive.searchElement(1, "//*[text()[contains(.,'Previsualizar')]]") != 0) {
			drive.clickButton(1, "//*[text()[contains(.,'Previsualizar')]]","Darle click a siguiente para subir foto");
			
		}

		// Esperar que aparezca el boton de publicar
		while (drive.searchElement(2, "view_post") == 0);

		System.out.println("Escribir el pie de la foto");
		drive.inputWrite(2, "xc_message", phrase,150);

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
	}
		
	private void likeUsers() throws InterruptedException {
		if (drive.searchElement(1, "//*[text()[contains(.,'Editar perfil')]]") != 0) {
			drive.clickButton(1, "//*[text()[contains(.,'Editar perfil')]]","Editar perfil");
		} else if (drive.searchElement(1, "/html/body/div/div/div[1]/div/div/a[2]") != 0) {
			drive.clickButton(1, "/html/body/div/div/div[1]/div/div/a[2]","Editar perfil xpath");
		}
		Thread.sleep(getNumberRandom(1253, 1754));

		// Darle click a Amigos
		if(drive.searchElement(1, "//*[text()[contains(.,'Amigos')]]") != 0) {
			System.out.println("Entrar en amigos");
			drive.clickButton(1, "//*[text()[contains(.,'Amigos')]]", "Amigos xPath");
			quantityFriends();
		}else if(drive.searchElement(1, "//*[text()[contains(.,'Friends')]]") != 0) {
			System.out.println("Entrar en amigos");
			drive.clickButton(1, "//*[text()[contains(.,'Friends')]]", "Friend xPath");
			quantityFriends();
		}else {
			System.out.println("El usuario no tiene amigos");
		}
	}
	
	private void quantityFriends() throws InterruptedException {
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

			validateAccountDisabled();
		}
	}
	
	private void validateAccountDisabled() throws InterruptedException {
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
					drive.goPage(PAGE);
				}
				System.out.println("Volver al inicio");
			}
		}
	}
	
	private void addGroup() throws InterruptedException {
		Group_Categorie gp = new Group_Categorie();
		gp.setCategories_id(categoria_id);
		List<Group_Categorie> listG = gp.getGroupCategorie();
		for (Group_Categorie group_Categorie : listG) {
			if(cantGroupsAdd <= quantity_groups) {
				String name = "";
				name = group_Categorie.getName();
				
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
					}
					countQuantityGroup();

				}
				
				if(drive.searchElement(1, "/html/body/div/div/div[1]/div/div/a[1]") != 0) {
					drive.clickButton(1, "/html/body/div/div/div[1]/div/div/a[1]", "Volver Inicio xpath");
				}else {
					drive.goPage(PAGE);
				}
				
			}
		}
		//Si la campaña no tiene grupos respaldados buscar un grupo random
		if(listG.isEmpty()) {
			categoryWithoutGroupCategory();
		}
	}
	
	private void countQuantityGroup() throws InterruptedException {
		Thread.sleep(getNumberRandomForSecond(1980, 2460));
		System.out.println("Contar cantidad de grupos");
		int quantityGroups = drive.getQuantityElements(1,"/html/body/div/div/div[2]/div[2]/div[1]/div[2]/div[1]/div/div/div") - 1;
		
		if (quantityGroups > 0) {
			System.out.println("Elegir un grupo random");
			for(int i = 1; i <= quantityGroups; i++) {
				if(cantGroupsAdd <= quantity_groups) {
					//Si hay mas resultados en la busqueda de grupos
					
					String urlCurrent = drive.getCurrentUrl();
					//Si tiene un boton el grupo
					if(drive.searchElement(1, "/html/body/div/div/div[2]/div[2]/div[1]/div[2]/div[1]/div/div/div["+i+"]/table/tbody/tr/td[3]/div/div/table/tbody/tr/td[2]/a") != 0) {
						//Si dice Cancelar solicitud volver atras
						if(!drive.getText(1, "/html/body/div/div/div[2]/div[2]/div[1]/div[2]/div[1]/div/div/div["+i+"]/table/tbody/tr/td[3]/div/div/table/tbody/tr/td[2]/a/span").equals("Cancelar solicitud para unirte")) {
							drive.clickButton(1, "/html/body/div/div/div[2]/div[2]/div[1]/div[2]/div[1]/div/div/div["+i+"]/table/tbody/tr/td[2]/a", "Entrar a grupo "+i);
							//Si la cantidad de miembros es mayor o igual a la minima solicitada
							int cantMembers = countQuantityMembers();
							System.out.println("La cantidad de miembro son "+cantMembers);
							if(cantMembers >= quantity_min) {
								//Si existe el boton de Unirse agrupo
								String groupId = getIdGroup();
								Group group = new Group();
								group.setGroups_id(groupId);
								
								//Si el grupo no existe en la base de datos, no se agrega al grupo
								if(group.getCountUsersForGroups() < 6) {
									if(drive.searchElement(1, "/html/body/div/div/div[2]/div/div[1]/form/input[3]") != 0) {
										drive.clickButton(1, "/html/body/div/div/div[2]/div/div[1]/form/input[3]", "Unirse grupo xPath");
										Thread.sleep(1450);
										//Si existen preguntas 
										if(drive.searchElement(1, "//*[text()[contains(.,'Error al agregar el miembro')]]") !=0) {
											drive.goPage(urlCurrent);
										}
										if(drive.searchElement(1, "/html/body/div/div/div[2]/div/table/tbody/tr/td/form/div[2]/div[3]/div[1]/div[1]/span") != 0) {
											responseQuestions();
										}else {
											cantGroupsAdd++;
											drive.goPage(urlCurrent);
										}
									//Si no existe el boton de unirse a grupo
									}else {
										drive.goPage(urlCurrent);
									}
								}
							//Si la cantidad de miembros es menor que la solicitada
							}else {
								drive.goPage(urlCurrent);
							}
						}
					}
					
					if(i == quantityGroups - 1) {
						if(drive.searchElement(1,"//*[text()[contains(.,'Ver más resultados')]]") != 0) {
							pressSeeAll("//*[text()[contains(.,'Ver más resultados')]]");
							quantityGroups = drive.getQuantityElements(1,"/html/body/div/div/div[2]/div[2]/div[1]/div[2]/div[1]/div/div/div") - 1;
							i = 1;
						}else if(drive.searchElement(1, "//*[text()[contains(.,'See all')]]") != 0) {
							pressSeeAll("//*[text()[contains(.,'See all')]]");
							quantityGroups = drive.getQuantityElements(1,"/html/body/div/div/div[2]/div[2]/div[1]/div[2]/div[1]/div/div/div") - 1;
							i = 1;
						}
					}
				}
				if(cantGroupsAdd >= 2) {
					break;
				}
			}//Fin del for quantity de grupos
				
		}
	}
	
	private void responseQuestions() {

		System.out.println("Contando cantidad de preguntas");
		int quantiQuestion = drive.getQuantityElements(1, "/html/body/div/div/div[2]/div/table/tbody/tr/td/form/div[2]/div[3]/div");
		System.out.println("Hay "+quantiQuestion+" pregunta(s)");
		//Si es solo una pregunta
		if(quantiQuestion <2) {
			String pregunta = drive.getText(1, "/html/body/div/div/div[2]/div/table/tbody/tr/td/form/div[2]/div[3]/div[1]/div[1]/span");
			String preguntaModificada = modifyQuestion(pregunta);
				
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
			System.out.println("Darle click al boton de enviar solicitud");	
			drive.clickButton(1, "/html/body/div/div/div[2]/div/table/tbody/tr/td/form/div[2]/div[4]/input", "Boton de enviar solicitud");
			cantGroupsAdd++;
		//Si es mas de una pregunta
		}else {
			for(int i1 = 1; i1<= quantiQuestion;i1++) {
				String pregunta = drive.getText(1, "/html/body/div/div/div[2]/div/table/tbody/tr/td/form/div[2]/div[3]/div["+i1+"]/div[1]/span");
				String preguntaModificada = modifyQuestion(pregunta);	
				
				Question question = new Question();
				question.setQuestion(preguntaModificada);
				String answerB = question.getAnswerQuestion();
				if(answerB.isEmpty()) {
					System.out.println("La pregunta no se encuentra en la base de datos, se debe responder manual");
					String respuesta = JOptionPane.showInputDialog("Ingrese la respuesta para la pregunta "+i1+" y pulse aceptar");
					
					while(respuesta == null || respuesta.isEmpty()) respuesta = JOptionPane.showInputDialog("Ingrese la respuesta para la pregunta "+i1+" y pulse aceptar");
					
					System.out.println("Se ingresará la respuesta ingresada en el cuadro de texto");
					drive.inputWrite(1, "/html/body/div/div/div[2]/div/table/tbody/tr/td/form/div[2]/div[3]/div["+i1+"]/div[2]/div/textarea", respuesta,150);
					
					question.setAnswer(respuesta);
					try {
						question.insert();
						System.out.println("Se agrego el usuario y la respuesta a la base de datos");
					} catch (SQLException e) {
						e.printStackTrace();
					}
				}else {
					System.out.println("Se ingresará la respuesta ingresada en el cuadro de texto");
					drive.inputWrite(1, "/html/body/div/div/div[2]/div/table/tbody/tr/td/form/div[2]/div[3]/div["+i1+"]/div[2]/div/textarea", answerB,150);
					
				}
			}	
			System.out.println("Darle click al boton de enviar solicitud");	
			drive.clickButton(1, "/html/body/div/div/div[2]/div/table/tbody/tr/td/form/div[2]/div[4]/input", "Boton de enviar solicitud");
			cantGroupsAdd++;
		}
	//Si no hay preguntas por responder
	
	}
	
	private void categoryWithoutGroupCategory() throws InterruptedException {
		Group_Categorie gp = new Group_Categorie();
		gp = gp.getGroupSearchRandom();
		String name = gp.getName();
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
			}
			countQuantityGroup();
		}
		
		drive.clickButton(1, "/html/body/div/div/div[1]/div/div/a[1]", "Volver Inicio xpath");
	}
	
	
	
	private String modifyQuestion(String question) {
		StringBuilder preguntaModificada = new StringBuilder();
		for(int j = 0;j<question.length();j++) {
			if(question.substring(j, j+1).equals("­")) {
				preguntaModificada.append(""); 
			}else {
				preguntaModificada.append(question.subSequence(j, j+1));
			}
		}
		
		return preguntaModificada.toString();
	}
	
	private int countQuantityMembers() {
		for(int i = 0; i< 8; i++) {
			try {
				if(drive.getText(3, "u_0_"+i).equals("Miembros") || drive.getText(3, "u_0_"+i).equals("Members")) {
					return Integer.parseInt(drive.getText(3, "u_0_"+(i-1)));
				}
			}catch(NoSuchElementException e) {
				// NONE
			}
		}
		return 0;
	}
	private void goFanPage(String urlFanPage) throws InterruptedException {
		System.out.println("Ingresar en la fan page "+urlFanPage);
		drive.goPage(urlFanPage);
		Thread.sleep(getNumberRandomForSecond(1545, 2456));
		System.out.println("Actualizar pagina");
		drive.refresh();
		Thread.sleep(getNumberRandomForSecond(1945, 2456));
		drive.scrollDown(15);
		Thread.sleep(getNumberRandomForSecond(2845, 3996));
		drive.scrollUp(15);
		Thread.sleep(getNumberRandomForSecond(3047, 3396));

	}
	
	private void publicGroup(int taskModelId, boolean trash) throws InterruptedException {
		if(drive.searchElement(1, "//*[text()[contains(.,'Me gusta')]]") != 0) {
			System.out.println("Darle like");
			drive.clickButton(1, "//*[text()[contains(.,'Me gusta')]]","Me gusta");
		}else if(drive.searchElement(0, "likeButton") != 0) {
			System.out.println("Darle like 1");
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
		robot.pressShiftTabulador();
		Thread.sleep(getNumberRandomForSecond(1645, 1987));
		robot.enter();
		Thread.sleep(10000);
		
		System.out.println("Compartir en un grupo");
		robot.pressDown();
		Thread.sleep(1000);
		robot.enter();
		Thread.sleep(7000);
		robot.enter();
		Thread.sleep(4000);
		robot.pressDown();
		Thread.sleep(getNumberRandomForSecond(478,645));
		robot.pressDown();
		Thread.sleep(getNumberRandomForSecond(478,645));
		robot.enter();
		Thread.sleep(getNumberRandomForSecond(478,645));
		writeGroupAndPublic(taskModelId, trash);
		
		

		drive.goPage(PAGE);
	}
	
	private void writeGroupAndPublic(int taskModelId, boolean trash) throws InterruptedException {
		if(trash) {
			Group_Categorie gp = new Group_Categorie();
			gp.setCategories_id(categoria_id);
			gp.setSpanish(isGroupsInSpanish);
			Group groupC = new Group();
			List<Group_Categorie> list = gp.getGroupCategorie();
			System.out.println("Buscar grupos segun categoria ");
			if(!list.isEmpty()) {
				
				User_Group userGroup = new User_Group();
				userGroup.setUsers_id(idUser);
				for(Group_Categorie group : list) {
					
					String[] values = group.getName().trim().split(" ");
					System.out.println("Buscando grupo por categoria "+values[0]+" "+values[1]);
					groupC = userGroup.getOneGroupNotPublicationTrash(values[0], values[1]);
					if(groupC != null) {
						System.out.println("se consiguio grupo "+groupC.getName());
						break;
					}else {
						System.out.println("No se consiguio grupo para esta categoria");
					}
				}
				
				
				if(groupC != null) {
					ifThereIsAGroupToBePublished(groupC,taskModelId);
				}else {
					System.out.println("No hay grupos para publicar");
				}
				
			}
		}else {
			Group_Categorie gp = new Group_Categorie();
			gp.setCategories_id(categoria_id);
			Group group1 = new Group();
			List<Group_Categorie> list = gp.getGroupCategorie();
			System.out.println("Buscar grupos segun categoria ");
			if(!list.isEmpty()) {
				User_Group userG = new User_Group();
				userG.setUsers_id(idUser);
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
					group1 = userG.getOneGroupNotPublication(string1, string2);
					if(group1 != null) {
						System.out.println("se consiguio grupo "+group1.getName());
						break;
					}else {
						System.out.println("No se consiguio grupo para esta categoria");
					}
				}
				
				
				
				
				if(group1 != null) {
					System.out.println("El grupo a publicar es "+group1.getName());
					int aux = 0;
					for(int i =1; i< 100; i++) {
						
						if(drive.searchElement(1, "/html/body/div["+i+"]/div[2]/div/div/div/div/div/div[2]/div[2]/table/tbody/tr/td[2]/span/span/label/input") != 0) {
							try {
								drive.inputWrite(1, "/html/body/div["+i+"]/div[2]/div/div/div/div/div/div[2]/div[2]/table/tbody/tr/td[2]/span/span/label/input", group1.getName(), 125);
								System.out.println("Se escribio el nombre del grupo");
								Thread.sleep(getNumberRandomForSecond(1560, 1789));
								robot.pressDown();
								Thread.sleep(getNumberRandomForSecond(456, 789));
								robot.enter();
								Thread.sleep(getNumberRandomForSecond(1560, 1789));
								aux = i;
								break;
							}catch (Exception e) {
								//No se imprime nada si da error
							}
						}
						
					}
					Thread.sleep(getNumberRandomForSecond(895, 985));
					System.out.println(aux);
					if(aux != 0) {
						if(drive.searchElement(1, "/html/body/div["+aux+"]/div[2]/div/div/div/div/div/div[3]/div/div/div[3]/label/input") != 0) {
							System.out.println("Compartir publicacion completa");
							robot.pressTab();
							Thread.sleep(789);
							robot.pressTab();
							Thread.sleep(789);
							robot.pressTab();
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
							robot.pressShiftTabulador();
							Thread.sleep(789);
							robot.pressShiftTabulador();
							Thread.sleep(789);
							robot.pressShiftTabulador();
							Thread.sleep(789);
							robot.pressShiftTabulador();
							Thread.sleep(789);
							robot.pressShiftTabulador();
							Thread.sleep(789);
							robot.enter();
						}
						
						po.setGroups(group1.getGroups_id());
						po.setTasks_model_id(taskModelId);
						po.setCategories_id(categoria_id);
						po.setTasks_grid_id(tasks_grid_id);
						po.setMaduration(false);
						po.setUsers_id(idUser);
						po.setFanPage(true);
						searchLinkPublication(group1.getGroups_id());
						System.out.println("Guardada la publicacion en la fan page correctamente");
					}else {
						System.out.println("No se encuentra el elemento para escribir");
					}
					
				}else {
					System.out.println("No hay grupos para publicar");
				}
				
			}
		}
		
	}
	
	private void ifThereIsAGroupToBePublished(Group groupC, int taskModelId) throws InterruptedException {
		System.out.println("El grupo a publicar es "+groupC.getName());
		int aux = 0;
		for(int i =1; i< 100; i++) {
			
			if(drive.searchElement(1, "/html/body/div["+i+"]/div[2]/div/div/div/div/div/div[2]/div[2]/table/tbody/tr/td[2]/span/span/label/input") != 0) {
				try {
					drive.inputWrite(1, "/html/body/div["+i+"]/div[2]/div/div/div/div/div/div[2]/div[2]/table/tbody/tr/td[2]/span/span/label/input", groupC.getName(), 125);
					System.out.println("Se escribio el nombre del grupo");
					Thread.sleep(getNumberRandomForSecond(1560, 1789));
					robot.pressDown();
					Thread.sleep(getNumberRandomForSecond(456, 789));
					robot.enter();
					Thread.sleep(getNumberRandomForSecond(1560, 1789));
					aux = i;
					break;
				}catch (Exception e) {
					//No hacer nada si da exception
				}
			}
			
		}
		Thread.sleep(getNumberRandomForSecond(895, 985));
		System.out.println(aux);
		if(aux != 0) {
			if(drive.searchElement(1, "/html/body/div["+aux+"]/div[2]/div/div/div/div/div/div[3]/div/div/div[3]/label/input") != 0) {
				System.out.println("Compartir publicacion completa");
				robot.pressTab();
				Thread.sleep(789);
				robot.pressTab();
				Thread.sleep(789);
				robot.pressTab();
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
				robot.pressShiftTabulador();
				Thread.sleep(789);
				robot.pressShiftTabulador();
				Thread.sleep(789);
				robot.pressShiftTabulador();
				Thread.sleep(789);
				robot.pressShiftTabulador();
				Thread.sleep(789);
				robot.pressShiftTabulador();
				Thread.sleep(789);
				robot.enter();
			}
			
			
			po.setGroups(groupC.getGroups_id());
			po.setTasks_model_id(taskModelId);
			po.setCategories_id(categoria_id);
			po.setTasks_grid_id(tasks_grid_id);
			po.setUsers_id(idUser);
			po.setMaduration(false);
			po.setFanPage(true);
			
			searchLinkPublication(groupC.getGroups_id());
			System.out.println("Guardada la publicacion en la fan page correctamente");
		}else {
			System.out.println("No se encuentra el elemento para escribir");
		}
		
	
	}
	
	private void searchLinkPublication(String groupId) throws InterruptedException {
		System.out.println("Ingresar al registro de actividad");
		drive.goPage(PAGE+"profile");
		
		Thread.sleep(1250);
		
		if(drive.searchElement(1, "//*[text()[contains(.,'Registro de actividad')]]") != 0) {
			drive.clickButton(1, "//*[text()[contains(.,'Registro de actividad')]]", "Registro de Actividad");
			
			Thread.sleep(1250);
			
			if(drive.searchElement(1, "/html/body/div/div/div[2]/div/div[1]/div[2]/div/div[1]/div/div[1]/div/div/div[2]") != 0) {
				String link_publication = "";
				if(drive.searchElement(1, "/html/body/div/div/div[2]/div/div[1]/div[2]/div/div[1]/div/div[1]/div/div/div[3]/div/div/a") != 0) {
				
					link_publication = drive.getHref(1, "/html/body/div/div/div[2]/div/div[1]/div[2]/div/div[1]/div/div[1]/div/div/div[3]/div/div/a");
				}else if(drive.searchElement(1, "/html/body/div/div/div[2]/div/div[1]/div[2]/div/div[1]/div/div[1]/div/div/div[2]/div/div/a") != 0) {
				
					link_publication = drive.getHref(1, "/html/body/div/div/div[2]/div/div[1]/div[2]/div/div[1]/div/div[1]/div/div/div[2]/div/div/a");
				}
				
				System.out.println(link_publication);
				System.out.println("groups/"+groupId);
				if(link_publication.contains("groups/"+groupId)) {
					po.setLink_post(link_publication);
				}
				
			}
		}
		
		po.insert();
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
	
	private void publicGroupPublicity(int taskModelId) throws InterruptedException {
		User_Group gro = new User_Group();
		gro.setUsers_id(idUser);
		int cantGrupo = gro.getCountGroups();
		if(cantGrupo < 1) {
			System.out.println("El usuario no tiene grupos agregados en la base de datos ");
		}else {
			Group_Categorie groC = new Group_Categorie();
			groC.setCategories_id(categoria_id);
			List<Group_Categorie> list = groC.getGroupCategorie();
				
			System.out.println("Buscar grupos segun categoria ");
			if(!list.isEmpty()) {
				
				groupCategories(list, taskModelId);
			}
		}
	}
	
	private void groupCategories(List<Group_Categorie> list, int taskModelId) throws InterruptedException {
		int cantMaxPublications = quantity_groups;
		int cantiPublications = 0;
		List<Group> listG;
		Group gr = new Group();
		for(Group_Categorie group : list) {
			if(cantiPublications <= cantMaxPublications) {
				String[] values = group.getName().trim().split(" ");
				
				System.out.println("Buscando grupo por categoria "+values[0]+" "+values[1]);
				listG = gr.getGroupNotPublication(values[0], values[1], idUser);
				if(listG.isEmpty()) {
					System.out.println("No hay grupos a publicar para la categoria "+group.getName());
				}else {
					for(Group g : listG) {
						String urlGroup = PAGE+"groups/"+g.getGroups_id();
						String idGroups = g.getGroups_id();
						System.out.println("Publicar en el grupo "+g.getName()+ " id: "+idGroups);
						drive.goPage(urlGroup);
						
						Thread.sleep(getNumberRandomForSecond(1234, 1456));
						
						uploadImageFinal();
						cantiPublications++;
						System.out.println("El usuario hizo su publicación "+cantiPublications);
						System.out.println("Registrando post");
						
						po.setCategories_id(categoria_id);
						po.setTasks_model_id(taskModelId);
						po.setUsers_id(idUser);
						po.setGroups(idGroups);
						po.setMaduration(false);
						po.setFanPage(false);
						searchLinkPublication(idGroups);

					
				

						System.out.println("El usuario publico correctamente");

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
			readGroups("/html/body/div/div/div[2]/div/table/tbody/tr/td/div[2]/ul/li[6]/div/a");
		}else if(drive.searchElement(1, "//*[text()[contains(.,'Ver todos')]]") != 0) {
			System.out.println("Ver todos los grupos");
			pressSeeAll("//*[text()[contains(.,'Ver todos')]]");
			readGroups("//*[text()[contains(.,'Ver todos')]]");
		}else if(drive.searchElement(1, "//*[text()[contains(.,'See all')]]") != 0) {
			System.out.println("Ver todos los grupos");
			pressSeeAll("//*[text()[contains(.,'See all')]]");
			readGroups("//*[text()[contains(.,'See all')]]");
				
		}else {
			readGroups("");
		}
		
		if(drive.searchElement(1, "/html/body/div/div/div[1]/div/div/a[1]") != 0) {
			drive.clickButton(1, "/html/body/div/div/div[1]/div/div/a[1]", "Inicio xPath");
		}
	}
	
	private void pressSeeAll(String element) throws InterruptedException {
		drive.clickButton(1, element, "elemento der Ver todos");
		Thread.sleep(getNumberRandomForSecond(1265, 1980));
	}
	
	private void readGroups(String element) throws InterruptedException, SQLException {
		
		int quantityGroups = drive.getQuantityElements(1, "/html/body/div/div/div[2]/div/table/tbody/tr/td/div[2]/ul/li");
		if(quantityGroups < 1) {
			System.out.println("No hay grupos para este usuario");
		}else {
			User_Group ugro = new User_Group();
			ugro.setUsers_id(idUser);
			ugro.deleteGroups();
			Group gp = new Group();
			List<String> listHref = new ArrayList<>();
			for(int i = 1; i<= quantityGroups;i++) {
				if(!element.isEmpty() && drive.searchElement(1, element) != 0) {
					drive.clickButton(1, element, "Elemento ver todos");
				}
				String href = drive.getHref(1, "/html/body/div/div/div[2]/div/table/tbody/tr/td/div[2]/ul/li["+i+"]/table/tbody/tr/td[1]/a");
				if(href != null) listHref.add(href);
			}
			
			for(String list: listHref) {
				drive.goPage(list);
				Thread.sleep(890);
				if(validateElementViewPost()) {
					String idGroup = getIdGroup();
					gp.setGroups_id(idGroup);
					String nameGroup = drive.getText(1, "/html/body/div/div/div[2]/div/div[1]/div[2]/table/tbody/tr/td[1]/a/table/tbody/tr/td[2]/h1/div");
					System.out.println("Grupo a agregar "+nameGroup);
					gp = gp.find();
					//Si el grupo no esta en la base de datos ingresarlo
					if(gp == null) {
						gp = new Group();
						gp.setGroups_id(idGroup);
						System.out.println("Ingresar en los miembros");
						int cantMiembros = 0;
						cantMiembros = countQuantityMembers();
						System.out.println("La cantidad de miembros son "+cantMiembros);
						if(drive.searchElement(1, "/html/body/div/div/div[2]/div/div[1]/div[2]/table/tbody/tr/td[2]/a") != 0 && cantMiembros != 0) {
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
							drive.back();
						}else if(drive.searchElement(1, "//*[text()[contains(.,'Miembro')]]") != 0  && cantMiembros != 0) {
							drive.clickButton(1, "//*[text()[contains(.,'Miembro')]]", "Miembro");
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
							drive.back();
						}
						gp.setCant_miembros(cantMiembros);
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
			
				
		}
	}
	
	private boolean validateElementViewPost() {
		
		if (drive.searchElement(2, "xc_message") != 0) {
			return true;
		}
		if(drive.searchElement(2, "view_post") != 0) {
			return true;
		}
		return false;
	}
	
	
	
	private void publicFinal() throws InterruptedException {
		uploadImageFinal();

	}
	
	private void reviewMessage() throws InterruptedException {
		Thread.sleep(getNumberRandomForSecond(1250, 2563));
		// Ingresar en mensajes
		if(drive.searchElement(1, "/html/body/div/div/div[1]/div/div/a[3]") != 0) {
			drive.clickButton(1, "/html/body/div/div/div[1]/div/div/a[3]","Mensajes xpath");
			Thread.sleep(getNumberRandomForSecond(4530, 5600));
			robot.mouseScroll(5);
			Thread.sleep(getNumberRandomForSecond(256, 1024));
			robot.mouseScroll(-5);
			Thread.sleep(getNumberRandomForSecond(256, 1024));
			if (drive.searchElement(2, "body") != 0) {
				if(drive.searchElement(1, "/html/body/div/div/div[1]/div/div/a[3]") != 0) {
					drive.clickButton(1, "/html/body/div/div/div[1]/div/div/a[3]","Entro en mensaje directo");	
				}else if(drive.searchElement(1, "//*[text()[contains(.,'Mensajes')]]") != 0) {
					drive.clickButton(1, "//*[text()[contains(.,'Mensajes')]]","Entro en mensaje directo");
				}
			}
			robot.mouseScroll(5);
			Thread.sleep(getNumberRandomForSecond(256, 1024));
			robot.mouseScroll(-5);
			Thread.sleep(getNumberRandomForSecond(256, 1024));
			System.out.println("Contar cantidad de mensajes");
			int quantityMessage = drive.getQuantityElements(1,
					"/html/body/div/div/div[2]/div[2]/div[1]/div[2]/div[1]/table");
    		quantityMessage = quantityMessage == 0 ? drive.getQuantityElements(1,"/html/body/div/div/div[2]/div[2]/div[2]/div[2]/div[1]/table") : quantityMessage;
			if (quantityMessage > 1) {
				System.out.println("Elegir un mensajes randon para ingresar");
				int randomMessage = getNumberRandomForSecond(1, quantityMessage);
				// Entrar en un mensaje random
				System.out.println("Ingresar en mensaje random");
				drive.clickButton(1, "/html/body/div/div/div[2]/div[2]/div[2]/div[2]/div[1]/table[" + randomMessage
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
		}else if(drive.searchElement(1, "//*[text()[contains(.,'Mensajes')]]") != 0) {
			drive.clickButton(1, "//*[text()[contains(.,'Mensajes')]]","Mensajes xpath");
			Thread.sleep(getNumberRandomForSecond(4530, 5600));
			robot.mouseScroll(5);
			Thread.sleep(getNumberRandomForSecond(256, 1024));
			robot.mouseScroll(-5);
			Thread.sleep(getNumberRandomForSecond(256, 1024));
			if (drive.searchElement(2, "body") != 0) {
				if(drive.searchElement(1, "/html/body/div/div/div[1]/div/div/a[3]") != 0) {
					drive.clickButton(1, "/html/body/div/div/div[1]/div/div/a[3]","Entro en mensaje directo");	
				}else if(drive.searchElement(1, "//*[text()[contains(.,'Mensajes')]]") != 0) {
					drive.clickButton(1, "//*[text()[contains(.,'Mensajes')]]","Entro en mensaje directo");
				}
				
			}
			robot.mouseScroll(5);
			Thread.sleep(getNumberRandomForSecond(256, 1024));
			robot.mouseScroll(-5);
			Thread.sleep(getNumberRandomForSecond(256, 1024));
			System.out.println("Contar cantidad de mensajes");
			int quantityMessage = drive.getQuantityElements(1,
					"/html/body/div/div/div[2]/div[2]/div[1]/div[2]/div[1]/table");
			if (quantityMessage > 1) {
			
				System.out.println("Elegir un mensajes randon para ingresar");
				int randomMessage = getNumberRandomForSecond(1, quantityMessage);
				// Entrar en un mensaje random
				System.out.println("Ingresar en mensaje random");
				drive.clickButton(1, "/html/body/div/div/div[2]/div[2]/div[1]/div[2]/div[1]/table[" + randomMessage
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
	
	private void addAndAceptedNewUsers() throws InterruptedException {
		drive.goPage("https://mbasic.facebook.com/friends/center/requests/");
		Thread.sleep(1250);
		int quantityUsers = 0;												    
		System.out.println("Validar los usuarios que enviaron solicitud");
		if(drive.searchElement(1, "//*[text()[contains(.,'No hay solicitudes')]]") != 0) {
			System.out.println("No hay solicitudes pendiente");
		}else {
			int quantityUsersRequest = drive.getQuantityElements(1, "/html/body/div/div/div[2]/div/div[1]/div[1]/div");
			
			if(quantityUsersRequest < 2) {
				System.out.println("No hay usuarios para confirmar");
			}else {
				
				System.out.println("Hay "+quantityUsersRequest+" usuarios para confirmar");
				for(int i = 1; i <= quantityUsersRequest; i++) {
					int numRandom = getNumberRandomForSecond(1, 4);
					if(numRandom % 2 == 0) {
						drive.clickButton(1, "/html/body/div/div/div[2]/div/div[1]/div[1]/div["+i+"]/table/tbody/tr/td[2]/div[2]/a[1]", "Confirmar xPath");
						Thread.sleep(854);
						System.out.println("Se ha aceptado un usuario");
						quantityUsers++;
					}
					if(quantityUsers >= 2) {
						break;
					}
				}
			}
		}
		
		
		System.out.println("Validar usuarios a enviarles solicitud");
		drive.goPage("https://mbasic.facebook.com/friends/center/suggestions/");
		int quantityUsersSugestion = drive.getQuantityElements(1, "/html/body/div/div/div[2]/div/div[1]/div[1]/div");
		                                                    
		if(quantityUsersSugestion < 2) {
			System.out.println("No hay usuarios para agregar");
		}else {
			System.out.println("Hay "+quantityUsersSugestion+" para agregar");
			for(int i = 1; i <= quantityUsersSugestion; i++) {
				int numRandom = getNumberRandomForSecond(1, 4);
				if(quantityUsers >= 2) {
					break;
				}
				if(numRandom % 2 == 0) {
					drive.clickButton(1, "/html/body/div/div/div[2]/div/div[1]/div[1]/div["+i+"]/table/tbody/tr/td[2]/a", "Perfil usuario xPath");			
					Thread.sleep(854);
					if(drive.searchElement(1, "/html/body/div/div/div[2]/div/table/tbody/tr/td/div/div[4]/a") != 0) {
						drive.clickButton(1, "/html/body/div/div/div[2]/div/table/tbody/tr/td/div/div[4]/a", "Ver Perfil xPath");
						
						if(drive.searchElement(1, "/html/body/div/div/div[2]/div/div[1]/div[1]/div[3]/table/tbody/tr/td[1]/a") != 0) {
							drive.clickButton(1, "/html/body/div/div/div[2]/div/div[1]/div[1]/div[3]/table/tbody/tr/td[1]/a", "Añadir xPath");
							quantityUsers++;
						}else if(drive.searchElement(1, "//*[text()[contains(.,'Agregar')]]") != 0) {
							drive.clickButton(1, "//*[text()[contains(.,'Agregar')]]", "Añadir xPath");
							quantityUsers++;
						}
					}else if(drive.searchElement(1, "//*[text()[contains(.,'Ver perfil')]]") != 0) {
						drive.clickButton(1, "//*[text()[contains(.,'Ver perfil')]]", "Ver perfil xPath");
						
						if(drive.searchElement(1, "/html/body/div/div/div[2]/div/div[1]/div[1]/div[3]/table/tbody/tr/td[1]/a") != 0) {
							drive.clickButton(1, "/html/body/div/div/div[2]/div/div[1]/div[1]/div[3]/table/tbody/tr/td[1]/a", "Añadir xPath");
							quantityUsers++;
						}else if(drive.searchElement(1, "//*[text()[contains(.,'Agregar')]]") != 0) {
							drive.clickButton(1, "//*[text()[contains(.,'Agregar')]]", "Añadir xPath");
							quantityUsers++;
						}
					}
					
					Thread.sleep(854);
					System.out.println("Se ha agregado un usuario");
				}
				
				drive.goPage("https://mbasic.facebook.com/friends/center/suggestions/");
				
			}
		}
		
		System.out.println("Volver al inicio");
		drive.goPage(PAGE);
	}
	
	private void commentsPost() {
		System.out.println("COMENTAR POST");
		
		System.out.println("Buscar Post que no se hayan comentado");
		
		Post pos = new Post();
		pos.setUsers_id(idUser);

		List<Post> listP = pos.getPostForComments();
		for(Post post : listP){
			System.out.println(post);
		}
	}
	
	private void userBlock(String name) {
		User_Block userB = new User_Block();
		userB.setUsers_id(idUser);
		userB.setComentario(name);
		if (userB.getIdUser() == 0) {
			userB.insert();
		}
	}

	private String validateIP(String ip) throws IOException {

		BufferedReader in = null;
            try {
            	URL myIP = new URL("http://checkip.amazonaws.com");
            	in = new BufferedReader(new InputStreamReader(myIP.openStream()));
                return in.readLine();
            } catch (Exception e1) {
            	return ip;
            }
        
	}

	private static int getNumberRandomForSecond(int min, int max) {
		return ThreadLocalRandom.current().nextInt(min, max + 1);
	}

	private static int getNumberRandom(int min, int max) {
		return (int) (Math.random() * min) + max;
	}

}
