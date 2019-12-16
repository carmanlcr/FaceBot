package com.selenium.facebook.Modelo;

import java.sql.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.selenium.facebook.Interface.Model;


public class Task implements Model{
	
	private String name;
	private String created_at;
	private boolean active;
	private Date date = new Date();
	private DateFormat dateFormatDateTime = new SimpleDateFormat("yyyy-MM-dd H:m:s");
	private static Conexion conn = new Conexion();
	Statement st;
	ResultSet rs;
	
	public void insert() throws SQLException {
		Connection conexion = conn.conectar();
		setCreated_at(dateFormatDateTime.format(date));
		
		try {
			String insert = "INSERT INTO tasks(name,created_at) VALUE "
					+ " ('"+getName()+"','"+getCreated_at()+"');";
			st = (Statement) conexion.createStatement();
			st.executeUpdate(insert);
			conexion.close();
		}catch(SQLException e) {
			System.err.println(e);
		}
		
	}
	
	@Override
	public void update() throws SQLException {
		
	}

	public List<String> getTasksActive(){
		List<String> list = new ArrayList<String>();
		
		Connection conexion = conn.conectar();
		String query = "SELECT t.name FROM tasks t " + 
				"WHERE active = ?;"; 
		try {
			PreparedStatement pst = conexion.prepareStatement(query);
			pst.setInt(1, 1);
			rs = pst.executeQuery();
			
			while (rs.next() ) {
				list.add(rs.getString("t.name"));
			}
			conexion.close();
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
