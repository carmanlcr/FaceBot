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
	private String updated_at; 
	private static Conexion conn = new Conexion();
	private Date date = new Date();
	private DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	Statement st;
	ResultSet rs;
	
	public void insert() {
		date = new Date();
		setCreated_at(dateFormat.format(date));
		setUpdated_at(dateFormat.format(date));
		try (Connection conexion = conn.conectar();){
			String insert = "INSERT INTO "+TABLE_NAME+"(users_id,categories_id,created_at,updated_at) "
					+ " VALUE (?,?,?,?);";
			PreparedStatement exe = conexion.prepareStatement(insert);
			exe.setInt(1, getUsers_id());
			exe.setInt(2, getCategories_id());
			exe.setString(3, getCreated_at());
			exe.setString(4, getUpdated_at());
			exe.executeUpdate();

		}catch(SQLException e) {
			System.err.println(e);
		}
		
	}
	
	@Override
	public void update() throws SQLException {
		
	}
	
	public void inserts(List<Integer> list) {
		date = new Date();
		setCreated_at(dateFormat.format(date));
		setUpdated_at(dateFormat.format(date));
		try (Connection conexion = conn.conectar();){
			String insert = "INSERT INTO "+TABLE_NAME+"(users_id,categories_id,created_at) "
					+ " VALUES ";
			for(Integer usercate : list) {
				insert += "("+getUsers_id()+", "+usercate+", '"+getCreated_at()+"','"+getUpdated_at()+"'),";
			}
			String sinUltimoCaracter = insert.substring(0, insert.length()-1);
			sinUltimoCaracter += ";";
			st = (Statement) conexion.createStatement();
			st.executeUpdate(sinUltimoCaracter);
			
		}catch(SQLException e) {
			System.err.println(e);
		}
		
	}
	
	public void updateCategories() throws SQLException{
		date = new Date();
		setUpdated_at(dateFormat.format(date));
		setCreated_at(dateFormat.format(date));
		try (Connection conexion = conn.conectar();
				Statement st = conexion.createStatement()){
			String update = "UPDATE "+TABLE_NAME+" SET categories_id ="+getCategories_id()+", updated_at = "+getUpdated_at()+" WHERE users_id = "+getUsers_id();
			st.executeUpdate(update);
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

	public String getUpdated_at() {
		return updated_at;
	}

	public void setUpdated_at(String updated_at) {
		this.updated_at = updated_at;
	}
}
