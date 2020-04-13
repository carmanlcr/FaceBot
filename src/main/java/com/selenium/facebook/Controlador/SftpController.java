package com.selenium.facebook.Controlador;

import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import com.jcraft.jsch.SftpException;


/**
 * Connecion SFTP para la descarga de imagenes en el servidor
 * 
 * @author Luis Morales
 * @deprecated
 *
 */
public class SftpController {

	private static final String URL_SFTP = "192.168.2.6";
	private static final String USERNAME = "root";
	private static final String PASSWORD = "R315T4R*";
	private JSch jsch;
	
	
	protected ChannelSftp connectionSftp() {
		jsch = new JSch();
	    try {
			jsch.setKnownHosts("/var/www/generate");
			Session jschSession = jsch.getSession(USERNAME, URL_SFTP);
			java.util.Properties config = new java.util.Properties(); 
		    config.put("StrictHostKeyChecking", "no");
		    jschSession.setConfig(config);
		    jschSession.setPassword(PASSWORD);
		    jschSession.connect();
			return (ChannelSftp) jschSession.openChannel("sftp");
			
		} catch (JSchException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	protected void downloadFileSftp(String name_image) {
		ChannelSftp channelSftp = connectionSftp();
		try {
			channelSftp.connect();
			
			String remoteFile = "/var/www/generate/public/images/"+name_image;
		    String localDir = InicioController.PATH_IMAGE_DOWNLOAD_FTP;
		    try {
				channelSftp.get(remoteFile, localDir);
			} catch (SftpException e) {
				e.printStackTrace();
			}
		  
		    channelSftp.exit();
		} catch (JSchException e) {
			e.printStackTrace();
		}
	}
}
