package com.selenium.facebook.Modelo;

import java.sql.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.selenium.facebook.Interface.Model;

import configurations.connection.ConnectionFB;


public class Task implements Model{
	
	private static final String TABLE_NAME = "tasks";
	private String name;
	private String created_at;
	private boolean active;
	private Date date = new Date();
	private DateFormat dateFormatDateTime = new SimpleDateFormat("yyyy-MM-dd H:m:s");
	private static ConnectionFB conn = new ConnectionFB();
	Statement st;
	ResultSet rs;
	
	public void insert() throws SQLException {
		
		setCreated_at(dateFormatDateTime.format(date));
		String insert = "INSERT INTO "+TABLE_NAME+"(name,created_at) VALUE "
				+ " (?,?);";
		try (Connection conexion = conn.conectar();
				PreparedStatement exe = conexion.prepareStatement(insert);){
			
			
			exe.setString(1, getName());
			exe.setString(2, getCreated_at());
			exe.executeUpdate();
		}catch(SQLException e) {
			System.err.println(e);
		}
		
	}
	
	@Override
	public void update() throws SQLException {
		//None
	}

	public List<String> getTasksActive(){
		List<String> list = new ArrayList<>();
		
		
		String query = "SELECT t.name FROM "+TABLE_NAME+" t " + 
				"WHERE active = ?;"; 
		try (Connection conexion = conn.conectar();
				PreparedStatement pst = conexion.prepareStatement(query);){
			
			pst.setInt(1, 1);
			rs = pst.executeQuery();
			
			while (rs.next() ) {
				list.add(rs.getString("t.name"));
			}
		}catch(SQLException e) {
			System.err.println(e);
		}
		
		return list;
		
	}
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCreated_at() {
		return created_at;
	}

	public void setCreated_at(String created_at) {
		this.created_at = created_at;
	}

	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}

	
	

}
