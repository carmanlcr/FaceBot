package com.selenium.facebook.Modelo;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.selenium.facebook.Interface.Model;

import configurations.connection.ConnectionFB;

public class Comment implements Model {
	private static final String TABLE_NAME = "comments";
	private int comments_id;
	private String comment;
	private boolean active;
	private int categories_id;
	private String created_at;
	private String updated_at;
	private static ConnectionFB conn = new ConnectionFB();
	
	
	@Override
	public void insert() throws SQLException {
		// NONE
		
	}

	@Override
	public void update() throws SQLException {
		// None

	}
	
	public Comment getCommentCategorie(int postId) {
		Comment comment = null;
		StringBuilder query = new StringBuilder();
		query.append("SELECT * FROM facebook.comments c ");
		query.append("WHERE c.comments_id NOT IN ");
		query.append("(SELECT pc.comments_id FROM posts_comments pc WHERE pc.posts_id = ? ) ");
		query.append("AND c.categories_id = ? AND c.active = 1 AND c.isNormal = 1 ");
		query.append("ORDER BY RAND() LIMIT 1;"); 
		try(Connection conexion = conn.conectar();
				PreparedStatement exe = conexion.prepareStatement(query.toString())){
			exe.setInt(1, postId);
			exe.setInt(2, getCategories_id());
			
			ResultSet rs = exe.executeQuery();
			if(rs.next()) {
				comment = new Comment();
				comment.setComments_id(rs.getInt("comments_id"));
				comment.setComment(rs.getString("comment"));
				comment.setActive(rs.getBoolean("active"));
				comment.setCategories_id(rs.getInt("categories_id"));
			}
			
		}catch(SQLException e) {
			e.printStackTrace();
		}
		return comment;
	}
	
	public Comment getCommentisNotNormal() {
		Comment comment = null;
		StringBuilder query = new StringBuilder();
		query.append("SELECT * FROM facebook.comments c ");
		query.append("WHERE c.categories_id = ? AND c.active = 1 AND c.isNormal = 0 ");
		query.append("ORDER BY RAND() LIMIT 1;"); 
		try(Connection conexion = conn.conectar();
				PreparedStatement exe = conexion.prepareStatement(query.toString())){
			exe.setInt(1, getCategories_id());
			
			ResultSet rs = exe.executeQuery();
			if(rs.next()) {
				comment = new Comment();
				comment.setComments_id(rs.getInt("comments_id"));
				comment.setComment(rs.getString("comment"));
				comment.setActive(rs.getBoolean("active"));
				comment.setCategories_id(rs.getInt("categories_id"));
			}
			
		}catch(SQLException e) {
			e.printStackTrace();
		}
		return comment;
	}
	
	public Comment validateComment() {
		Comment comment = null;
		StringBuilder query = new StringBuilder();
		query.append("SELECT * FROM comments c ");
		query.append("WHERE c.comment = ?;");
		try(Connection conexion = conn.conectar();
				PreparedStatement exe = conexion.prepareStatement(query.toString())){
			exe.setString(1, getComment());
			
			ResultSet rs = exe.executeQuery();
			if(rs.next()) {
				comment = new Comment();
				comment.setComments_id(rs.getInt("comments_id"));
				comment.setComment(rs.getString("comment"));
				comment.setActive(rs.getBoolean("active"));
				comment.setCategories_id(rs.getInt("categories_id"));
			}
			
		}catch(SQLException e) {
			e.printStackTrace();
		}
		return comment;
	}
	

	public int getComments_id() {
		return comments_id;
	}

	public void setComments_id(int comments_id) {
		this.comments_id = comments_id;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
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
