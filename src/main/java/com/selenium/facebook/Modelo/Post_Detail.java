package com.selenium.facebook.Modelo;

import java.sql.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.selenium.facebook.Interface.Model;

import configurations.connection.ConnectionFB;


public class Post_Detail implements Model {

	private static final String TABLE_NAME = "posts_detail";
	private int posts_id;
	private int hashtag_id;
	private String created_at;
	private static ConnectionFB conn = new ConnectionFB();
	private Date date = new Date();
	private DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd H:m:s");
	Statement st;
	ResultSet rs;
	
	public void insert() throws SQLException {
		setCreated_at(dateFormat.format(date));
		String insert = "INSERT INTO "+TABLE_NAME+"(posts_id,hashtag_id,created_at) "
				+ " VALUE (?,?,?);";
		
		try (Connection conexion = conn.conectar();
				PreparedStatement exe = conexion.prepareStatement(insert);){
			
			
			exe.setInt(1, getPosts_id());
			exe.setInt(2, getHashtag_id());
			exe.setString(3, getCreated_at());
			exe.executeUpdate();
		}catch(SQLException e) {
			System.err.println(e);
		}
	}
	
	@Override
	public void update() throws SQLException {
		//None
	}
	
	
	public int getPosts_id() {
		return posts_id;
	}

	public void setPosts_id(int posts_id) {
		this.posts_id = posts_id;
	}

	public int getHashtag_id() {
		return hashtag_id;
	}

	public void setHashtag_id(int hashtag_id) {
		this.hashtag_id = hashtag_id;
	}

	public String getCreated_at() {
		return created_at;
	}

	public void setCreated_at(String created_at) {
		this.created_at = created_at;
	}
	
	

}
