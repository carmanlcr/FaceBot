package com.selenium.facebook.Modelo;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.selenium.facebook.Interface.Model;

import configurations.connection.ConnectionFB;

public class User_Group implements Model {

	private static final String TABLE_NAME = "users_groups";
	private int users_grouops_id;
	private int users_id;
	private String groups_id;
	private String created_at;
	private Date date;
	private SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	private DateFormat dateFormat1 = new SimpleDateFormat("yyyy-MM-dd");
	private static ConnectionFB conn = new ConnectionFB();
	
	@Override
	public void insert() throws SQLException {
		date = new Date();
		setCreated_at(dateFormat.format(date));
		String insert = "INSERT INTO "+TABLE_NAME+"(users_id,groups_id,created_at) "
				+ "VALUES (?,?,?);";
		
		try(Connection conexion = conn.conectar();
				PreparedStatement pre = conexion.prepareStatement(insert)){
			pre.setInt(1, getUsers_id());
			pre.setString(2, getGroups_id());
			pre.setString(3, getCreated_at());
			
			pre.executeUpdate();
			
		}catch (SQLException e) {
			System.err.println(e);
		}
		

	}

	public User_Group find() {
		User_Group ug = null;
		String query = "SELECT * FROM "+TABLE_NAME+" ug "
				+ "WHERE ug.users_id = ? AND ug.groups_id = ?;";
		try(Connection conexion = conn.conectar();
				PreparedStatement pre = conexion.prepareStatement(query);){
			pre.setInt(1, getUsers_id());
			pre.setString(2, getGroups_id());
			
			ResultSet rs = pre.executeQuery();
			if(rs.next()) {
				ug = new User_Group();
				ug.setUsers_grouops_id(rs.getInt("ug.users_groups_id"));
				ug.setUsers_id(rs.getInt("ug.users_id"));
				ug.setGroups_id(rs.getString("ug.groups_id"));
				ug.setCreated_at(rs.getString("ug.created_at"));
			}
		}catch (SQLException e) {
			System.err.println(e);
		}
		
		return ug;
	}
	
	@Override
	public void update() throws SQLException {
		//None
	}
	
	public int getCountGroups() {
		int cant = 0;
		String query = "SELECT COUNT(*) cant FROM "+TABLE_NAME+ " WHERE users_id = ?;";
		ResultSet rs = null;
		try (Connection conexion = conn.conectar();
				PreparedStatement pre = conexion.prepareStatement(query)){
			pre.setInt(1, getUsers_id());
			
			rs = pre.executeQuery();
			
			if(rs.next()) {
				cant = rs.getInt("cant");
			}
			
		}catch(SQLException e) {
			e.getStackTrace();
		}
		
		return cant;
	}
	
	public List<Group> getGroupNotPublication(String string1, String string2, int users_id) {
		date = new Date();
		List<Group> listG = new ArrayList<>();
		Group gp = null;
		String query = "SELECT gp.name, gp.active, gp.groups_id, gp.created_at, ug.users_id " + 
				"FROM "+TABLE_NAME+" ug " + 
				"INNER JOIN (select * from groups where name like ?) gp ON ug.groups_id = gp.groups_id " + 
				"INNER JOIN (select * from groups where name like ?) C ON C.groups_id = gp.groups_id " + 
				"WHERE ug.groups_id NOT IN (SELECT pt.groups FROM posts pt WHERE pt.groups IS NOT NULL " + 
				"AND DATE(pt.created_at) = ?) " + 
				"AND ug.users_id = ? " + 
				"GROUP BY gp.name, gp.active, gp.groups_id, gp.created_at, ug.users_id " + 
				"ORDER BY RAND();";
		try(Connection conexion = conn.conectar();
				PreparedStatement exe = conexion.prepareStatement(query);
				){
			exe.setString(1, "%"+string1+"%");
			exe.setString(2, "%"+string2+"%");
			exe.setInt(3, users_id);
			exe.setString(4, dateFormat1.format(date));
			ResultSet rs = exe.executeQuery();
			
			while(rs.next()) {
				gp = new Group();
				gp.setName(rs.getString("gp.name"));
				gp.setActive(rs.getBoolean("gp.active"));
				gp.setGroups_id(rs.getString("gp.groups_id"));
				gp.setCreated_at(rs.getString("gp.created_at"));
				listG.add(gp);
			}
		}catch (SQLException e) {
			e.getStackTrace();
		}
		
		return listG;
	}
	
	public Group getOneGroupNotPublication(String string1, String string2) {
		Group gp = null;
		String query = "SELECT gp.name, gp.active, gp.groups_id, gp.created_at, ug.users_id " + 
				"FROM "+TABLE_NAME+" ug " + 
				"INNER JOIN (select * from groups where name like ? AND cant_miembros >= 3000) gp ON ug.groups_id = gp.groups_id " + 
				"INNER JOIN (select * from groups where name like ?) C ON C.groups_id = gp.groups_id " + 
				"WHERE ug.groups_id NOT IN (SELECT pt.groups FROM posts pt WHERE pt.groups IS NOT NULL " + 
				"AND DATE(pt.created_at) = ?) " + 
				"AND ug.users_id = ? " + 
				"GROUP BY gp.name, gp.active, gp.groups_id, gp.created_at, ug.users_id " + 
				"ORDER BY RAND() LIMIT 1;";
		date = new Date();
		try(Connection conexion = conn.conectar();
				PreparedStatement exe = conexion.prepareStatement(query);
				){
			exe.setString(1, "%"+string1+"%");
			exe.setString(2, "%"+string2+"%");
			exe.setString(3, dateFormat1.format(date));
			exe.setInt(4, getUsers_id());
			ResultSet rs = exe.executeQuery();
			
			while(rs.next()) {
				gp = new Group();
				gp.setName(rs.getString("gp.name"));
				gp.setActive(rs.getBoolean("gp.active"));
				gp.setGroups_id(rs.getString("gp.groups_id"));
				gp.setCreated_at(rs.getString("gp.created_at"));
			}
		}catch (SQLException e) {
			e.getStackTrace();
		}
		
		return gp;
	}
	
	public Group getOneGroupNotPublicationTrash(String string1, String string2) {
		Group gp =  null;
		String query = "SELECT gp.name, gp.active, gp.groups_id, gp.created_at, ug.users_id " + 
				"FROM "+TABLE_NAME+" ug " + 
				"INNER JOIN (select * from groups where name like ? AND cant_miembros >= 3000) gp ON ug.groups_id = gp.groups_id " + 
				"LEFT JOIN (select * from groups where name like ?) C ON C.groups_id = gp.groups_id " + 
				"WHERE ug.groups_id NOT IN (SELECT pt.groups FROM posts pt WHERE pt.groups IS NOT NULL " + 
				"AND DATE(pt.created_at) = ?) " + 
				"AND ug.users_id = ? " + 
				"GROUP BY gp.name, gp.active, gp.groups_id, gp.created_at, ug.users_id " + 
				"ORDER BY RAND() LIMIT 1;";
		date = new Date();
		try(Connection conexion = conn.conectar();
				PreparedStatement exe = conexion.prepareStatement(query);
				){
			exe.setString(1, "%"+string1+"%");
			exe.setString(2, "%"+string2+"%");
			exe.setString(3, dateFormat1.format(date));
			exe.setInt(4, getUsers_id());
			ResultSet rs = exe.executeQuery();
			
			if(rs.next()) {
				gp= new Group();
				gp.setName(rs.getString("gp.name"));
				gp.setActive(rs.getBoolean("gp.active"));
				gp.setGroups_id(rs.getString("gp.groups_id"));
				gp.setCreated_at(rs.getString("gp.created_at"));
			}
		}catch (SQLException e) {
			e.getStackTrace();
		}
		
		return gp;
	}
	
	public List<Group> getGroupNotPublication(int quantityGroups) {
		List<Group> listG = new ArrayList<>();
		Group gp = null;
		date = new Date();
		String query = "SELECT gp.name, gp.active, gp.groups_id, gp.created_at, ug.users_id "
				+ "FROM "+TABLE_NAME+" ug "
				+ "INNER JOIN groups gp ON ug.groups_id = gp.groups_id "
				+"WHERE ug.groups_id NOT IN "
				+ "(SELECT pt.groups FROM posts pt WHERE pt.groups IS NOT NULL AND DATE(pt.created_at) = ?) "  
				+ "AND ug.users_id = ? "
				+ "GROUP BY gp.name, gp.active, gp.groups_id, gp.created_at, ug.users_id "
				+ "ORDER BY RAND() LIMIT ?; ";
		try(Connection conexion = conn.conectar();
				PreparedStatement exe = conexion.prepareStatement(query);
				){
			
			exe.setString(1, dateFormat1.format(date));
			exe.setInt(2, getUsers_id());
			exe.setInt(3, quantityGroups);
			ResultSet rs = exe.executeQuery();
			while(rs.next()) {
				gp = new Group();
				gp.setName(rs.getString("gp.name"));
				gp.setActive(rs.getBoolean("gp.active"));
				gp.setGroups_id(rs.getString("gp.groups_id"));
				gp.setCreated_at(rs.getString("gp.created_at"));
				listG.add(gp);
			}
		}catch (SQLException e) {
			System.out.println(e);
		}
		
		return listG;
	}
	
	public void deleteGroups() {
		String query = "DELETE FROM "+TABLE_NAME+" WHERE users_id = ?";
		try(Connection conexion = conn.conectar();
				PreparedStatement e = conexion.prepareStatement(query)){
			e.setInt(1, getUsers_id());
			e.executeUpdate();
		}catch(SQLException e) {
			System.err.println("Error al eliminar");
		}
	}

	public int getUsers_grouops_id() {
		return users_grouops_id;
	}

	public void setUsers_grouops_id(int users_grouops_id) {
		this.users_grouops_id = users_grouops_id;
	}

	public int getUsers_id() {
		return users_id;
	}

	public void setUsers_id(int users_id) {
		this.users_id = users_id;
	}

	public String getGroups_id() {
		return groups_id;
	}

	public void setGroups_id(String groups_id) {
		this.groups_id = groups_id;
	}

	public String getCreated_at() {
		return created_at;
	}

	public void setCreated_at(String created_at) {
		this.created_at = created_at;
	}

}

