package com.selenium.facebook.Modelo;

import java.sql.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.selenium.facebook.Interface.Model;


public class Task_Model implements Model {

	private String name;
	private String created_at;
	private Date date = new Date();
	private DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd H:m:s");
	private static Conexion conn = new Conexion();
	Statement st;
	ResultSet rs;
	
	public void insert() throws SQLException {
		Connection conexion = conn.conectar();
		setCreated_at(dateFormat.format(date));
		
		try {
			String insert = "INSERT INTO tasks_model(name,created_at) VALUE "
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
	
	public int getLast() {
		int id = 0;
		Connection conexion = conn.conectar();
		
		
		try {
			
			st = (Statement) conexion.createStatement();
			rs = st.executeQuery("SELECT tk.tasks_model_id FROM tasks_model tk ORDER BY tk.tasks_model_id DESC LIMIT 1");

			
			while (rs.next() ) {
               id =  rs.getInt("tk.tasks_model_id");
			}
			conexion.close();
		}catch(SQLException e) {
			System.err.println(e);
		}
		
		return id;
	}

	public int getTaskModelDetailDiferent(String values){
		int list = 0;
		Connection conexion = conn.conectar();
		try {
			
			String queryExce = "SELECT tm.tasks_model_id FROM tasks_model tm " + 
							"WHERE tm.tasks_model_id NOT IN ("+values+") ORDER BY rand() LIMIT 1;";
			
			PreparedStatement  query = (PreparedStatement) conexion.prepareStatement(queryExce);
			rs = query.executeQuery();
			
			while (rs.next() ) {
				list = rs.getInt("tm.tasks_model_id");
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

}
