package com.selenium.facebook.Modelo;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.selenium.facebook.Interface.Model;


public class Inicio_Aplicacion implements Model{
	
	private final String TABLE_NAME = "inicio_aplicacion";
	private String created_at;
	private String version;
	private static Conexion conn = new Conexion();
	private Date date = new Date();
	private DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	
	public void insert() {
		
		setCreated_at(dateFormat.format(date));
		try (Connection conexion = conn.conectar();){
			String insert = "INSERT INTO "+TABLE_NAME+"(version,created_at) "
					+ " VALUE (?,?);";
			PreparedStatement exe = (PreparedStatement) conexion.prepareStatement(insert);
			exe.setString(1, getVersion());
			exe.setString(2, getCreated_at());
			exe.executeUpdate();
		}catch(SQLException e) {
			System.err.println(e);
		}
		
	}
	
	@Override
	public void update() throws SQLException {
		
	}
	
	public String getCreated_at() {
		return created_at;
	}

	public void setCreated_at(String created_at) {
		this.created_at = created_at;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}
}
