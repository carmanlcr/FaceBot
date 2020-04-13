package com.selenium.facebook.Modelo;

import java.sql.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.selenium.facebook.Interface.Model;

import configurations.connection.ConnectionFB;


public class Task_Model implements Model {

	private static final String TABLE_NAME = "tasks_model";
	private String name;
	private String created_at;
	private Date date = new Date();
	private DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd H:m:s");
	private static ConnectionFB conn = new ConnectionFB();
	Statement st;
	ResultSet rs;
	
	public void insert() throws SQLException {
		
		setCreated_at(dateFormat.format(date));
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
	
	public int getLast() {
		int id = 0;
		
		try (Connection conexion = conn.conectar();
				Statement st = conexion.createStatement();
				ResultSet rs = st.executeQuery("SELECT tk.tasks_model_id FROM "+TABLE_NAME+" tk ORDER BY tk.tasks_model_id DESC LIMIT 1")){
			
			while (rs.next() ) {
               id =  rs.getInt("tk.tasks_model_id");
			}
		}catch(SQLException e) {
			System.err.println(e);
		}
		
		return id;
	}

	public int getTaskModelDetailDiferent(String values){
		int list = 0;
		String queryExce = "SELECT tm.tasks_model_id FROM "+TABLE_NAME+" tm " + 
				"WHERE tm.tasks_model_id NOT IN ("+values+") ORDER BY rand() LIMIT 1;";
		try (Connection conexion = conn.conectar();
				PreparedStatement  query =  conexion.prepareStatement(queryExce);
				ResultSet rs = query.executeQuery();){
			
			
			
			
			while (rs.next() ) {
				list = rs.getInt("tm.tasks_model_id");
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

}
