package com.selenium.facebook.Modelo;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.selenium.facebook.Interface.Model;

public class Task_Grid implements Model {
	
	private final String TABLE_NAME = "tasks_grid";
	private int tasks_grid_id;
	private int users_id;
	private int generes_id;
	private String date_publication;
	private boolean fan_page_publication;
	private boolean groups_publication;
	private String created_at;
	private Conexion conn = new Conexion();
	private SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	
	@Override
	public void insert() throws SQLException {
		Date date = new Date();
		setCreated_at(format.format(date));
		String insert = "INSERT INTO "+TABLE_NAME+ "(users_id,generes_id,date_publication,fan_page_publication,groups_publication,created_at) "
				+ "VALUES (?,?,?,?,?,?)";
		
		try(Connection conexion = conn.conectar();
				PreparedStatement pre = conexion.prepareStatement(insert)){
			pre.setInt(1, getUsers_id());
			pre.setInt(2, getGeneres_id());
			pre.setString(3, getDate_publication());
			pre.setBoolean(4, isFan_page_publication());
			pre.setBoolean(5, isGroups_publication());
			pre.setString(6, getCreated_at());
			
			pre.executeUpdate();
		}catch (SQLException e) {
			e.getStackTrace();
		}
	}

	@Override
	public void update() throws SQLException {
		// TODO Auto-generated method stub

	}

	public int getTasks_grid_id() {
		return tasks_grid_id;
	}

	public void setTasks_grid_id(int tasks_grid_id) {
		this.tasks_grid_id = tasks_grid_id;
	}

	public int getUsers_id() {
		return users_id;
	}

	public void setUsers_id(int users_id) {
		this.users_id = users_id;
	}

	public int getGeneres_id() {
		return generes_id;
	}

	public void setGeneres_id(int generes_id) {
		this.generes_id = generes_id;
	}

	public String getDate_publication() {
		return date_publication;
	}

	public void setDate_publication(String date_publication) {
		this.date_publication = date_publication;
	}

	public boolean isFan_page_publication() {
		return fan_page_publication;
	}

	public void setFan_page_publication(boolean fan_page_publication) {
		this.fan_page_publication = fan_page_publication;
	}

	public boolean isGroups_publication() {
		return groups_publication;
	}

	public void setGroups_publication(boolean groups_publication) {
		this.groups_publication = groups_publication;
	}

	public String getCreated_at() {
		return created_at;
	}

	public void setCreated_at(String created_at) {
		this.created_at = created_at;
	}

}
