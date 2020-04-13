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

import configurations.connection.ConnectionFB;


public class User_Categorie implements Model{

	private static final String TABLE_NAME = "users_categories";
	private int users_id;
	private int categories_id;
	private String created_at;
	private static ConnectionFB conn = new ConnectionFB();
	private Date date = new Date();
	private DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	Statement st;
	ResultSet rs;
	
	public void insert() {
		
		date = new Date();
		setCreated_at(dateFormat.format(date));
		String insert = "INSERT INTO "+TABLE_NAME+"(users_id,categories_id,created_at) "
				+ " VALUE (?,?,?);";
		try (Connection conexion = conn.conectar();
				PreparedStatement exe = conexion.prepareStatement(insert);){
			
			
			exe.setInt(1, getUsers_id());
			exe.setInt(2, getCategories_id());
			exe.setString(3, getCreated_at());
			exe.executeUpdate();

		}catch(SQLException e) {
			System.err.println(e);
		}
		
	}
	
	@Override
	public void update() throws SQLException {
		//none
	}
	
	public void inserts(List<Integer> list) {
		
		setCreated_at(dateFormat.format(date));
		try (Connection conexion = conn.conectar();){
			String insert = "INSERT INTO "+TABLE_NAME+"(users_id,categories_id,created_at) "
					+ " VALUES ";
			for(Integer usercate : list) {
				insert += "("+getUsers_id()+", "+usercate+", '"+getCreated_at()+"'),";
			}
			String sinUltimoCaracter = insert.substring(0, insert.length()-1);
			sinUltimoCaracter += ";";
			st =  conexion.createStatement();
			st.executeUpdate(sinUltimoCaracter);
			
		}catch(SQLException e) {
			System.err.println(e);
		}
		
	}
	
	public void updateCategories() throws SQLException{
		
		setCreated_at(dateFormat.format(date));
		try (Connection conexion = conn.conectar();
				Statement st = conexion.createStatement()){
			String update = "UPDATE "+TABLE_NAME+" SET categories_id ="+getCategories_id()+" WHERE users_id = "+getUsers_id();
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
}
