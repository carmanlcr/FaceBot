package com.selenium.facebook.Modelo;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.selenium.facebook.Interface.Model;

import configurations.connection.ConnectionFB;

public class Post_Comment implements Model {

	private static final String TABLE_NAME = "posts_comments";
	private int posts_comments_id;
	private int posts_id;
	private int comments_id;
	private int users_id;
	private boolean active;
	private String created_at;
	private String updated_at;
	private Date date = new Date();
	private DateFormat dateFormatDateTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	private static ConnectionFB conn = new ConnectionFB();
	
	
	@Override
	public void insert() throws SQLException {
		date = new Date();
		setCreated_at(dateFormatDateTime.format(date));
		setUpdated_at(dateFormatDateTime.format(date));
		
		StringBuilder query = new StringBuilder();
		
		query.append("INSERT INTO "+TABLE_NAME);
		query.append("(posts_id,comments_id,users_id,created_at,updated_at) ");
		query.append("VALUES(?,?,?,?,?);");
		
		try(Connection conexion = conn.conectar();
				PreparedStatement pre = conexion.prepareStatement(query.toString())){
					pre.setInt(1, getPosts_id());
					pre.setInt(2, getComments_id());
					pre.setInt(3, getUsers_id());
					pre.setString(4, getCreated_at());
					pre.setString(5, getUpdated_at());
					
					pre.executeUpdate();
		}catch(SQLException e){
			e.printStackTrace();
		}
	}

	@Override
	public void update() throws SQLException {
		// None

	}
	
	public int getPosts_comments_id() {
		return posts_comments_id;
	}

	public void setPosts_comments_id(int posts_comments_id) {
		this.posts_comments_id = posts_comments_id;
	}

	public int getPosts_id() {
		return posts_id;
	}

	public void setPosts_id(int posts_id) {
		this.posts_id = posts_id;
	}

	public int getComments_id() {
		return comments_id;
	}

	public void setComments_id(int comments_id) {
		this.comments_id = comments_id;
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


}
