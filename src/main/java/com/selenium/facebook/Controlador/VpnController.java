package com.selenium.facebook.Controlador;

import java.io.IOException;


public class VpnController {
	private static RobotClick robot;
	
	
	void iniciarVpn(String vpn, boolean bandera) throws InterruptedException {
		robot = new RobotClick(vpn);
		//Abrir la aplicación OpenVPN
		try {
			openVPN();
			Thread.sleep(2000);
			//Maximizar ventana
			robot.maximixar();
			//Seleccionar el buscador de la vpn
			if(bandera) {
				robot.pulsarTabulador();
				Thread.sleep(256);
				robot.pulsarTabulador();
				Thread.sleep(256);
				robot.pulsarTabulador();
				Thread.sleep(256);
				robot.pulsarTabulador();
				Thread.sleep(256);
				robot.pulsarTabulador();
				Thread.sleep(256);
				robot.pulsarTabulador();
				Thread.sleep(256);
				robot.pulsarTabulador();
				Thread.sleep(256);
				robot.pulsarTabulador();
				Thread.sleep(256);
				robot.pulsarTabulador();
				Thread.sleep(256);
				robot.pulsarTabulador();
				Thread.sleep(256);
				robot.pulsarTabulador();
				Thread.sleep(256);
				robot.pulsarTabulador();
				Thread.sleep(256);
			}else {
				robot.pulsarTabulador();
				Thread.sleep(256);
				robot.pulsarTabulador();
				Thread.sleep(256);
				robot.pulsarTabulador();
				Thread.sleep(256);
				robot.pulsarTabulador();
				Thread.sleep(256);
			}
			//copiar la vpn en el portapapeles
			robot.copy();
			Thread.sleep(2000);
			//Pegar el nombre de la VPN
			robot.paste();
			Thread.sleep(2000);
			//Presionar enter para que busque la vpn
			robot.enter();
			//Presionar enter para que busque la vpn
			Thread.sleep(35000);
			//Borrar la vpn escrita
			robot.selectAllAndDelete();
			//Cerrar la pestaña
			robot.close();
		} catch (IOException e) {
			System.out.println("Error al conseguir archivo");
		}
		
	}
	
	private static void openVPN() throws IOException {
		String path = "C:\\Program Files (x86)\\NordVPN\\NordVPN.exe";
		//String path = "C:\\Program Files\\NordVPN\\NordVPN.exe";
		Runtime objrun = Runtime.getRuntime();
		try {
			objrun.exec(path);
		} catch(IOException e) {				
			System.err.println("No consigue el archivo a ejecutar");
		}
		objrun.freeMemory();
	}
	
	void desconectVpn() throws IOException, InterruptedException {
		robot = new RobotClick();
		//Abrir la aplicacion OpenVPN
		openVPN();
		Thread.sleep(2015);
		//Maximizar ventana
		robot.maximixar();
		//Seleccionar la opcion de desconectar
		
		robot.pulsarShiftTabulador();
		Thread.sleep(290);
		robot.pulsarShiftTabulador();
		Thread.sleep(290);
		robot.pulsarShiftTabulador();
		Thread.sleep(290);
		robot.pulsarShiftTabulador();
		Thread.sleep(290);
		robot.pulsarShiftTabulador();
		Thread.sleep(290);
		robot.pulsarShiftTabulador();
		Thread.sleep(290);
		robot.pulsarShiftTabulador();
		Thread.sleep(290);
		robot.pulsarShiftTabulador();
		Thread.sleep(290);
		robot.pulsarShiftTabulador();
		Thread.sleep(290);
		robot.pulsarShiftTabulador();
		Thread.sleep(500);
		robot.enter();
		Thread.sleep(500);
		//Cerrar el vpn
		robot.close();
		
		Thread.sleep(1054);
	}
	
	
	
}
