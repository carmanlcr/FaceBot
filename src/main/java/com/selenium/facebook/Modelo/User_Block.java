package com.selenium.facebook.Modelo;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.selenium.facebook.Interface.Model;


public class User_Block extends User implements Model{

	private final String TABLE_NAME = "users_block";
	private int users_id;
	private boolean active;
	private String comentario;
	private String created_at;
	private String updated_at;
	private Date date = new Date();
	private DateFormat dateFormatDate = new SimpleDateFormat("yyyy-MM-dd");
	private DateFormat dateFormatDateTime = new SimpleDateFormat("yyyy-MM-dd H:m:s");
	private static Conexion conn = new Conexion();
	Statement st;
	ResultSet rs;
	
	
	public void insert() {
		setCreated_at(dateFormatDate.format(date));
		setUpdated_at(dateFormatDateTime.format(date));
		
		try (Connection conexion = conn.conectar();){
			String insert = "INSERT INTO "+TABLE_NAME+"(users_id,comentario,created_at,updated_at) VALUE "
					+ " (?,?,?,?);";
			PreparedStatement exe = conexion.prepareStatement(insert);
			exe.setInt(1, getUsers_id());
			exe.setString(2, getComentario());
			exe.setString(3, getCreated_at());
			exe.setString(4, getUpdated_at());
			exe.executeUpdate();
		}catch(SQLException e) {
			System.err.println(e);
		}
	}
	
	public int getIdUser(){
		int id = 0;
		String query = "SELECT us.users_id FROM "+TABLE_NAME+" us WHERE users_id = "+getUsers_id()+" AND active = 1;";
		try (Connection conexion = conn.conectar();
				Statement st = conexion.createStatement();
				ResultSet rs = st.executeQuery(query)){
			
			while (rs.next() ) {
               id =  rs.getInt("us.users_id");
			}
		}catch(SQLException e) {
			System.err.println(e);
		}
		
		return id;
		
	}
	
	public boolean desblockUser(String username) {
		String query = "UPDATE "+TABLE_NAME+" SET active = ?, updated_at = ?  WHERE users_id = ? ";
		
		setUpdated_at(dateFormatDateTime.format(date));
		User user = new User();
		user.setUsername(username);
		
		try (Connection conexion = conn.conectar();){
			PreparedStatement pst = conexion.prepareStatement(query);
			pst.setBoolean(1,false);
			pst.setString(2, getUpdated_at());
			pst.setInt(3, user.getIdUser());
			
			pst.executeUpdate();
			return true;
		}catch(SQLException e ) {
			System.err.println(e);
		}
		return false;
	}
	
	public int getUsers_id() {
		return users_id;
	}


	public void setUsers_id(int users_id) {
		this.users_id = users_id;
	}


	public boolean isActive() {
		return active;
	}


	public void setActive(boolean active) {
		this.active = active;
	}


	public String getCreated_at() {
		return created_at;
	}


	public void setCreated_at(String created_at) {
		this.created_at = created_at;
	}

	public String getUpdated_at() {
		return updated_at;
	}

	public void setUpdated_at(String updated_at) {
		this.updated_at = updated_at;
	}

	public String getComentario() {
		return comentario;
	}

	public void setComentario(String comentario) {
		this.comentario = comentario;
	}
}
