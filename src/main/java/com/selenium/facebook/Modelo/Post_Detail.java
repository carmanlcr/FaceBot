package com.selenium.facebook.Modelo;

import java.sql.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.selenium.facebook.Interface.Model;


public class Post_Detail implements Model {

	private int posts_id;
	private int hashtag_id;
	private String created_at;
	private static Conexion conn = new Conexion();
	private Date date = new Date();
	private DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd H:m:s");
	Statement st;
	ResultSet rs;
	
	public void insert() throws SQLException {
		setCreated_at(dateFormat.format(date));
		Connection conexion = conn.conectar();
		try {
			String insert = "INSERT INTO posts_detail(posts_id,hashtag_id,created_at) "
					+ " VALUE ("+getPosts_id()+", "+getHashtag_id()+", '"+getCreated_at()+"');";
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
