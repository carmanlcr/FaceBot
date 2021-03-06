package com.selenium.facebook.Controlador;

import com.selenium.facebook.Controlador.DriverController;

public class IniciaSesion {
	
	private String username;
	private String password;
	private DriverController dr;
	private String URL_LOGIN_FACEBOOK = "https://mbasic.facebook.com/";
	
	
	
	public IniciaSesion(DriverController dr,String username, String password) {
		this.dr = dr;
		this.username = username;
		this.password = password;
	}
	
	/**
	 * Iniciar sesión en twitter, ingresando el usuario y la contraseña y presionando el boton
	 * 
	 * @author Luis Morales
	 * @version 1.0.0
	 * @throws InterruptedException
	 */
	public void init() throws InterruptedException {
		dr.goPage(URL_LOGIN_FACEBOOK);
		System.out.println("Ingreso en la pagina para iniciar sesión");
		Thread.sleep(1250);
			
		while(dr.searchElement(2, "email") == 0);
		//Insertar el usuario
		if(dr.searchElement(2, "email") != 0) {
			System.out.println("Escribir el usuario");
			dr.inputWrite(2, "email", username,110);
			//Insertar el password
			Thread.sleep(1000);
			System.out.println("Escribir la contraseña");
			dr.inputWrite(2, "pass", password ,110);
			//Presionar el boton de sesion
			Thread.sleep(1000);
			System.out.println("Dar Click al boton de iniciar sesión");
			dr.clickButton(2, "login","Login inicio de sesion");
			
		}else if(dr.searchElement(2, "email") != 0) {
			//Insertar el usuario
			System.out.println("Escribir el usuario");
			dr.inputWrite(2, "email", username ,110);
			//Insertar la contraseña
			Thread.sleep(1000);
			System.out.println("Escribir la contraseña");
			dr.inputWrite(2, "pass", password,110);
			//Presionar boton de aceptar
			Thread.sleep(1000);
			System.out.println("Dar Click al boton de iniciar sesión");
			dr.clickButton(2, "login","Login inicio de sesion");
		}
		

		
	}

}
