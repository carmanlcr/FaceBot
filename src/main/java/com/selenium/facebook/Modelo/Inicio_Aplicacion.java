package com.selenium.facebook.Modelo;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.selenium.facebook.Interface.Model;

import configurations.connection.ConnectionFB;


public class Inicio_Aplicacion implements Model{
	
	private static final String TABLE_NAME = "inicio_aplicacion";
	private String created_at;
	private String updated_at;
	private String version;
	private int categories_id;
	private int generes_id;
	private static ConnectionFB conn = new ConnectionFB();
	private Date date = new Date();
	private DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	
	public void insert() {
		date = new Date();
		setCreated_at(dateFormat.format(date));
		setUpdated_at(dateFormat.format(date));
		String insert = "INSERT INTO "+TABLE_NAME+"(version,created_at,updated_at,categories_id,generes_id) "
				+ " VALUE (?,?,?,?,?);";
		try (Connection conexion = conn.conectar();
				PreparedStatement exe = conexion.prepareStatement(insert);){
			
			
			exe.setString(1, getVersion());
			exe.setString(2, getCreated_at());
			exe.setString(3, getUpdated_at());
			exe.setInt(4, getCategories_id());
			exe.setInt(5, getGeneres_id());
			exe.executeUpdate();
		}catch(SQLException e) {
			System.err.println(e);
		}
		
	}
	
	@Override
	public void update() throws SQLException {
		//None
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

	public String getUpdated_at() {
		return updated_at;
	}

	public void setUpdated_at(String updated_at) {
		this.updated_at = updated_at;
	}

	public int getCategories_id() {
		return categories_id;
	}

	public void setCategories_id(int categories_id) {
		this.categories_id = categories_id;
	}

	public int getGeneres_id() {
		return generes_id;
	}

	public void setGeneres_id(int generes_id) {
		this.generes_id = generes_id;
	}
}
