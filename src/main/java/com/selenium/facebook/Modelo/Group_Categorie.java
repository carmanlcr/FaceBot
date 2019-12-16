package com.selenium.facebook.Modelo;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.selenium.facebook.Interface.Model;

public class Group_Categorie implements Model {

	private final String TABLE_NAME = "groups_categories";
	private int groups_categories_id;
	private String name;
	private String created_at;
	private int categories_id;
	
	private Conexion conn = new Conexion();
	ResultSet rs;
	@Override
	public void insert() throws SQLException {
		// TODO Auto-generated method stub

	}

	@Override
	public void update() throws SQLException {
		// TODO Auto-generated method stub

	}

	public Group_Categorie getGroupSearch() {
		Group_Categorie groCa = new Group_Categorie();
		String query = "SELECT * FROM "+TABLE_NAME+" WHERE categories_id = ? ORDER BY RAND() LIMIT 1";
		Connection conexion = conn.conectar();
		try {
			PreparedStatement  queryE = (PreparedStatement) conexion.prepareStatement(query);
			queryE.setInt(1, getCategories_id());
			
			rs = queryE.executeQuery();
			
			if(rs.next()) {
				groCa.setGroups_categories_id(rs.getInt("groups_categories_id"));
				groCa.setName(rs.getString("name"));
				groCa.setCreated_at(rs.getString("created_at"));
				groCa.setCategories_id(rs.getInt("categories_id"));
			}
			conexion.close();
		}catch(SQLException e) {
			e.getStackTrace();
		}
		return groCa;
	}
	public int getGroups_categories_id() {
		return groups_categories_id;
	}

	public void setGroups_categories_id(int groups_categories_id) {
		this.groups_categories_id = groups_categories_id;
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

	public int getCategories_id() {
		return categories_id;
	}

	public void setCategories_id(int categories_id) {
		this.categories_id = categories_id;
	}

}
