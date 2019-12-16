package com.selenium.facebook.Modelo;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import com.selenium.facebook.Interface.Model;


public class User_Categorie implements Model{

	private final String TABLE_NAME = "users_categories";
	private int users_id;
	private int categories_id;
	private String created_at;
	private static Conexion conn = new Conexion();
	private Date date = new Date();
	private DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	Statement st;
	ResultSet rs;
	
	public void insert() {
		Connection conexion = conn.conectar();
		setCreated_at(dateFormat.format(date));
		try {
			String insert = "INSERT INTO "+TABLE_NAME+"(users_id,categories_id,created_at) "
					+ " VALUE (?,?,?);";
			PreparedStatement exe = conexion.prepareStatement(insert);
			exe.setInt(1, getUsers_id());
			exe.setInt(2, getCategories_id());
			exe.setString(3, getCreated_at());
			exe.executeUpdate();

			conexion.close();
		}catch(SQLException e) {
			System.err.println(e);
		}
		
	}
	
	@Override
	public void update() throws SQLException {
		
	}
	
	public void inserts(List<Integer> list) {
		Connection conexion = conn.conectar();
		setCreated_at(dateFormat.format(date));
		try {
			String insert = "INSERT INTO "+TABLE_NAME+"(users_id,categories_id,created_at) "
					+ " VALUES ";
			for(Integer usercate : list) {
				insert += "("+getUsers_id()+", "+usercate+", '"+getCreated_at()+"'),";
			}
			String sinUltimoCaracter = insert.substring(0, insert.length()-1);
			sinUltimoCaracter += ";";
			st = (Statement) conexion.createStatement();
			st.executeUpdate(sinUltimoCaracter);
			
			conexion.close();
		}catch(SQLException e) {
			System.err.println(e);
		}
		
	}
	
	public void updateCategories() throws SQLException{
		Connection conexion = conn.conectar();
		setCreated_at(dateFormat.format(date));
		try {
			String update = "UPDATE "+TABLE_NAME+" SET categories_id ="+getCategories_id()+" WHERE users_id = "+getUsers_id();
			st = (Statement) conexion.createStatement();
			st.executeUpdate(update);

			conexion.close();
		}catch(SQLException e) {
			System.err.println(e);
		}
	}
	
	
	public int getUsers_id() {
		return users_id;
	}
	public void setUsers_id(int users_id) {
		this.users_id = users_id;
	}
	public int getCategories_id() {
		return categories_id;
	}
	public void setCategories_id(int categories_id) {
		this.categories_id = categories_id;
	}
	public String getCreated_at() {
		return created_at;
	}
	public void setCreated_at(String date) {
		this.created_at = date;
	}
}
