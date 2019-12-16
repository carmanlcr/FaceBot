package com.selenium.facebook.Controlador;

import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;

public class RobotClick {
	
	private static Robot robot;
	private static int mask = InputEvent.BUTTON1_DOWN_MASK;
	private String vpn = "";
	Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
	
	/**
	 * Instancia del robot sin parametros
	 * 
	 * @author Luis Morales
	 * @version 1.0.0
	 */
	public RobotClick() {
		try {
			robot = new Robot();
		}catch (Exception e) {
			System.err.println("Error al iniciar el robot");
		}
	}
	
	/**
	 * Instancia del robot recibiendo como parametro el nombre de la vpn
	 * 
	 * @author Luis Morales
	 * @version 1.0.0
	 * @param vpn Nombre de la vpn
	 */
	public RobotClick(String vpn) {
		this.vpn = vpn;
		try {
			robot = new Robot();
		}catch (Exception e) {
			System.err.println("Error al iniciar el robot");
		}
	}
	
	/**
	 * Colocar mouse en una posición de la pantalla en especifica
	 * 
	 * @author Luis Morales
	 * @version 1.0.0
	 * @param x posición en equis(x) del mouse
	 * @param y posición en y del mouse
	 */
	public void dimensions(int x, int y) {
		robot.mouseMove(x, y);
	}
	
	/**
	 * Presionar el boton izquierdo del mouse
	 * 
	 * @author Luis Morales
	 * @version 1.0.0
	 */
	public void clickPressed() {
		robot.mousePress(mask);
		robot.mouseRelease(mask);
	}

	/**
	 * Hacer una copia en el portapapeles de la vpn
	 * 
	 * @author Luis Morales
	 * @version 1.0.0
	 */
	public void copy() {
		 StringSelection stringSelection = new StringSelection(this.vpn);
		 clipboard.setContents(stringSelection, null);
	}
	
	/**
	 * Se copia en el portapapeles la direccion de la imagen a publicar
	 * 
	 * @author Luis Morales
	 * @version 1.0.0
	 * @param imagePath path de la imagen a publicar
	 */
	public void copy(String imagePath) {
		 StringSelection stringSelection = new StringSelection(imagePath);
		 try {
			 clipboard.setContents(stringSelection, null);
		 }catch(IllegalStateException e) {
			 System.err.println("No puede copiarse en el portapapeles");
		 }
		 
		
		robot.keyRelease(KeyEvent.VK_CONTROL);
		robot.keyRelease(KeyEvent.VK_C);
		 
	}
	
	/**
	 * Hace un ctrl+v o pega en donde este posicionado el mouse y pueda escribir
	 * 
	 * @author Luis Morales
	 * @version 1.0.0
	 */
	public void paste() {
		robot.keyPress(KeyEvent.VK_CONTROL);
		robot.keyPress(KeyEvent.VK_V);
		robot.keyRelease(KeyEvent.VK_CONTROL);
		robot.keyRelease(KeyEvent.VK_V);
	}
	
	/**
	 * Hacer (enter) desde codigo
	 * 
	 * @author Luis Morales
	 * @version 1.0.0
	 */
	public void enter() {
		robot.keyPress(KeyEvent.VK_ENTER);
		robot.keyRelease(KeyEvent.VK_ENTER);
	}
	
	/**
	 * Cerrar una ventana con el comando Alt+F4
	 * 
	 * @author Luis Morales
	 * @version 1.0.0
	 */
	public void close() {
		robot.keyPress(KeyEvent.VK_ALT);
		robot.keyPress(KeyEvent.VK_F4);
		robot.keyRelease(KeyEvent.VK_ALT);
		robot.keyRelease(KeyEvent.VK_F4);
	}
	
	/**
	 * Maximizar una ventana de windows con el comando Windows+Up o Windows+Flecha Arriba
	 * 
	 * @author Luis Morales
	 * @version 1.0.0
	 */
	public void maximixar() {
		robot.keyPress(KeyEvent.VK_WINDOWS);
		robot.keyPress(KeyEvent.VK_UP);
		robot.keyRelease(KeyEvent.VK_WINDOWS);
		robot.keyRelease(KeyEvent.VK_UP);
	}
	
	/**
	 * Hacer scroll de manera random, valores negativo indica movimiento hacía arriba,
	 * valores positivos indica valores hacía abajo
	 * 
	 * @author Luis Morales
	 * @version 1.0.0
	 * @param value
	 */
	public void mouseScroll(int value){
		robot.mouseWheel(value);
	}
	
	/**
	 * Maximizar una pantalla a la izquierda con el comando Windows+Bloq Mayus+Left o Windows+Bloq Mayus+Flecha Izquierda
	 * 
	 * 
	 * @author Luis Morales
	 * @version 1.0.0
	 */
	public void maxiIzquierda() {
		robot.keyPress(KeyEvent.VK_WINDOWS);
		robot.keyPress(KeyEvent.VK_CAPS_LOCK);
		robot.keyPress(KeyEvent.VK_LEFT);
		robot.keyRelease(KeyEvent.VK_WINDOWS);
		robot.keyRelease(KeyEvent.VK_CAPS_LOCK);
		robot.keyRelease(KeyEvent.VK_LEFT);
	}
	
	/**
	 *  Pulsar tecla tabulador
	 *  
	 *  @author Luis Morales
	 *  
	 */
	public void pulsarTabulador() {
		robot.keyPress(KeyEvent.VK_TAB);
		robot.keyRelease(KeyEvent.VK_TAB);
	}
	
	/**
	 *  Pulsar tecla Shift + Tabulador
	 *  
	 *  @author Luis Morales
	 *  
	 */
	public void pulsarShiftTabulador() {
		robot.keyPress(KeyEvent.VK_SHIFT);
		robot.keyPress(KeyEvent.VK_TAB);
		robot.keyRelease(KeyEvent.VK_SHIFT);
		robot.keyRelease(KeyEvent.VK_TAB);
	}
	
	/**
	 * Seleccionar todo Ctrl + A y Eliminar Delete
	 */
	public void selectAllAndDelete() {
		robot.keyPress(KeyEvent.VK_CONTROL);
		robot.keyPress(KeyEvent.VK_A);
		robot.keyRelease(KeyEvent.VK_CONTROL);
		robot.keyRelease(KeyEvent.VK_A);
		robot.keyPress(KeyEvent.VK_DELETE);
		robot.keyRelease(KeyEvent.VK_DELETE);
	}
	
	/**
	 * Darle click al boton de Escape
	 * 
	 */
	public void pressEsc() {
		robot.keyPress(KeyEvent.VK_ESCAPE);
		robot.keyRelease(KeyEvent.VK_ESCAPE);
	}
}

