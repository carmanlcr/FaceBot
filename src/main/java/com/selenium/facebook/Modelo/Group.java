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

public class Group implements Model {

	private final String TABLE_NAME = "groups";
	private String groups_id;
	private String name;
	private int cant_miembros;
	private boolean active;
	private String created_at;
	private String updated_at;
	private int users_id;
	private static Conexion conn = new Conexion();
	private Date date = new Date();
	private DateFormat dateFormatDateTime = new SimpleDateFormat("yyyy-MM-dd H:m:s");
	private DateFormat dateFormat1 = new SimpleDateFormat("yyyy-MM-dd");
	
	@Override
	public void insert() throws SQLException {
		date = new Date();
		setCreated_at(dateFormatDateTime.format(date));
		setUpdated_at(dateFormatDateTime.format(date));
		String insert = "INSERT INTO "+TABLE_NAME+"(groups_id,name,cant_miembros,created_at,updated_at) "
				+ "VALUES (?,?,?,?,?);";
			try (Connection conexion = conn.conectar();
				PreparedStatement exe = conexion.prepareStatement(insert);){
				exe.setString(1, getGroups_id());
				exe.setString(2, getName());
				exe.setInt(3, getCant_miembros());
				exe.setString(4, getCreated_at());
				exe.setString(5, getUpdated_at());
				exe.executeUpdate();
				
			} catch(SQLException e)  {
				System.err.println(e);
			} catch(Exception e){
				System.err.println(e);
			}
	}

	@Override
	public void update() throws SQLException {
		// TODO Auto-generated method stub

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
		List<Group> listG = new ArrayList<Group>();
		Group gp = null;
		String query = "SELECT gp.name, gp.active, gp.groups_id, gp.created_at, ug.users_id "
				+ "FROM users_groups ug "
				+ "INNER JOIN (select * from "+TABLE_NAME+" where name like ?) gp ON gp.groups_id = ug.groups_id " 
				+ "INNER JOIN (select * from "+TABLE_NAME+" where name like ?) C "  
				+ "ON C.groups_id = gp.groups_id "  
				+ "WHERE ug.users_id = ? "
				+ "AND gp.groups_id NOT IN (SELECT pt.groups FROM posts pt WHERE pt.groups IS NOT NULL "
				+ "AND DATE(pt.created_at) = ?) "
				+ "GROUP BY gp.name, gp.active, gp.groups_id, gp.created_at, ug.users_id "
				+ "ORDER BY RAND(); ";
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
				gp.setUsers_id(rs.getInt("gp.users_id"));
				listG.add(gp);
			}
		}catch (SQLException e) {
			e.getStackTrace();
		}
		
		return listG;
	}
	
	public List<Group> getGroupNotPublication(int quantityGroups) {
		List<Group> listG = new ArrayList<Group>();
		Group gp = null;
		String query = "SELECT gp.name, gp.active, gp.groups_id, gp.created_at, gp.users_id "
				+ "FROM "+TABLE_NAME+" gp "
				+"WHERE gp.users_id = ? "
				+ "AND gp.groups_id NOT IN (SELECT pt.groups FROM posts pt WHERE pt.groups IS NOT NULL "  
				+ "AND DATE(pt.created_at) = ?) "
				+ "GROUP BY gp.name, gp.active, gp.groups_id, gp.created_at, gp.users_id "
				+ "ORDER BY RAND() LIMIT ?; ";
		try(Connection conexion = conn.conectar();
				PreparedStatement exe = conexion.prepareStatement(query);
				){
			exe.setInt(1, getUsers_id());
			exe.setString(2, dateFormat1.format(date));
			exe.setInt(3, quantityGroups);
			ResultSet rs = exe.executeQuery();
			while(rs.next()) {
				gp = new Group();
				gp.setName(rs.getString("gp.name"));
				gp.setActive(rs.getBoolean("gp.active"));
				gp.setGroups_id(rs.getString("gp.groups_id"));
				gp.setCreated_at(rs.getString("gp.created_at"));
				gp.setUsers_id(rs.getInt("gp.users_id"));
				listG.add(gp);
			}
		}catch (SQLException e) {
			System.out.println(e);
		}
		
		return listG;
	}
	
	public Group find() {
		Group gro = null;
		String query = "SELECT * FROM "+TABLE_NAME+" gr "
				+ "WHERE gr.groups_id = ?;" ;
		try(Connection conexion = conn.conectar();
				PreparedStatement exe = conexion.prepareStatement(query);){
			exe.setString(1, getGroups_id());
			ResultSet rs = exe.executeQuery();
			
			if(rs.next()) {
				gro = new Group();
				gro.setGroups_id(rs.getString("gr.groups_id"));
				gro.setName(rs.getString("gr.name"));
				gro.setActive(rs.getBoolean("gr.active"));
				gro.setCant_miembros(rs.getInt("gr.cant_miembros"));
			}
			
		}catch (SQLException e) {
			System.err.println(e);
		}
		return gro;
	}

	public String getGroups_id() {
		return groups_id;
	}

	public void setGroups_id(String groups_id) {
		this.groups_id = groups_id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getCant_miembros() {
		return cant_miembros;
	}

	public void setCant_miembros(int cant_miembros) {
		this.cant_miembros = cant_miembros;
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

	public int getUsers_id() {
		return users_id;
	}

	public void setUsers_id(int users_id) {
		this.users_id = users_id;
	}

}
