package com.selenium.facebook.Modelo;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import com.selenium.facebook.Interface.Model;

public class Task_Grid implements Model {
	
	private final String TABLE_NAME = "tasks_grid";
	private int tasks_grid_id;
	private int categories_id;
	private int generes_id;
	private String phrase;
	private String image;
	private String date_publication;
	private boolean isFanPage;
	private boolean isGroups;
	private boolean isAddGroups;
	private int quantity_groups;
	private int quantity_min;
	private boolean active;
	private String created_at;
	private String updated_at;
	private int db_admin_tasks_id;
	private Date date;
	private Conexion conn = new Conexion();
	private SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd");
	
	@Override
	public void insert() throws SQLException {

	}

	@Override
	public void update() throws SQLException {

	}
	
	public HashMap<String, Integer> getCategoriesToday(){
		HashMap<String, Integer> hash = new HashMap<String, Integer>();
		String query = "SELECT DISTINCT(ca.categories_id) categories_id,ca.name FROM "+TABLE_NAME+" tg " +
				"INNER JOIN tasks_grid_detail tgd ON tgd.tasks_grid_id = tg.tasks_grid_id " +
				"INNER JOIN categories ca ON ca.categories_id = tg.categories_id " + 
				"WHERE DATE(tg.date_publication) = ? "
				+"AND tgd.users_id NOT IN (SELECT pt.users_id FROM posts pt WHERE pt.tasks_grid_id IS NOT NULL AND DATE(pt.created_at) = ?);";
		date = new Date();
		try(Connection conexion = conn.conectar();
				PreparedStatement pre = conexion.prepareStatement(query)){
			
			pre.setString(1, format1.format(date));
			pre.setString(2, format1.format(date));
			ResultSet rs = pre.executeQuery();
			
			while(rs.next()) {
				hash.put(rs.getString("name"), rs.getInt("categories_id"));
			}
			
		}catch(SQLException e) {
			System.err.println(e);
		}
		
		return hash;
	}
	
	public HashMap<String, Integer> getCategoriesAndGeneresToday(){
		HashMap<String, Integer> hash = new HashMap<String, Integer>();
		String query = "SELECT DISTINCT(ge.generes_id) generes_id,ge.name FROM "+TABLE_NAME+" tg " +
				"INNER JOIN tasks_grid_detail tgd ON tgd.tasks_grid_id = tg.tasks_grid_id " +
				"INNER JOIN generes ge ON ge.generes_id = tg.generes_id " + 
				"WHERE DATE(tg.date_publication) = ? " +
				"AND tgd.users_id NOT IN (SELECT pt.users_id FROM posts pt WHERE pt.tasks_grid_id IS NOT NULL AND DATE(pt.created_at) = ?) " +
				"AND tg.categories_id = ?;";
		date = new Date();
		try(Connection conexion = conn.conectar();
				PreparedStatement pre = conexion.prepareStatement(query)){
			
			pre.setString(1, format1.format(date));
			pre.setString(2, format1.format(date));
			pre.setInt(3, getCategories_id());
			ResultSet rs = pre.executeQuery();
			
			while(rs.next()) {
				hash.put(rs.getString("name"), rs.getInt("generes_id"));
			}
			
		}catch(SQLException e) {
			System.err.println(e);
		}
		
		return hash;
	}
	
	
	public List<Task_Grid> getTaskGridToday() throws SQLException{
		List<Task_Grid> list = new ArrayList<Task_Grid>();
		Task_Grid taskG = null;
		String query = "SELECT * FROM "+TABLE_NAME+" tg " + 
				"INNER JOIN tasks_grid_detail tgd ON tg.tasks_grid_id = tgd.tasks_grid_id " + 
				"INNER JOIN users u ON u.users_id = tgd.users_id "+
				"WHERE tgd.users_id NOT IN (SELECT pt.users_id FROM posts pt WHERE DATE(pt.created_at) = ?) " + 
				"AND tg.categories_id = ? AND tg.active = ? AND DATE(tg.date_publication) = ? " + 
				"ORDER BY tg.date_publication ASC;";
		date = new Date();
		try (Connection conexion = conn.conectar();
				PreparedStatement pre = conexion.prepareStatement(query);){
			pre.setString(1, format1.format(date));
			pre.setInt(2, getCategories_id());
			pre.setInt(3, 1);
			pre.setString(4, format1.format(date));
			ResultSet rs = pre.executeQuery();
			while (rs.next() ) {
				taskG = new Task_Grid();
				taskG.setTasks_grid_id(rs.getInt("tg.tasks_grid_id"));
				taskG.setCategories_id(rs.getInt("tg.categories_id"));
				taskG.setGeneres_id(rs.getInt("tg.generes_id"));
				taskG.setPhrase(rs.getString("tg.phrase"));
				taskG.setImage(rs.getString("tg.image"));
				taskG.setGroups(rs.getBoolean("tg.isGroups"));
				taskG.setFanPage(rs.getBoolean("tg.isFanPage"));
				taskG.setAddGroups(rs.getBoolean("tg.isAddGroups"));
				taskG.setActive(rs.getBoolean("tg.active"));
				taskG.setQuantity_groups(rs.getInt("tg.quantity_groups"));
				taskG.setQuantity_min(rs.getInt("tg.quantity_min"));
				taskG.setDate_publication(rs.getString("tg.date_publication"));
				taskG.setDb_admin_tasks_id(rs.getInt("tg.db_admin_tasks_id"));
				list.add(taskG);
			}
			
		}catch(Exception e) {
			System.err.println(e);
		}
		
		return list;
 	}
	
	public Task_Grid getTaskForUser(int users_id) throws SQLException{
		Task_Grid taskG = null;
		date = new Date();
		String query = "SELECT * FROM "+TABLE_NAME +" tg "
				+" INNER JOIN tasks_grid_detail tgd ON tgd.tasks_grid_id = tg.tasks_grid_id "
				+" INNER JOIN users u ON u.users_id = tgd.users_id AND u.users_id = ? "
				+ "WHERE tgd.users_id NOT IN (SELECT pt.users_id FROM posts pt WHERE pt.tasks_grid_id IS NOT NULL AND pt.tasks_grid_id <> 0\r\n" + 
				"AND DATE(pt.created_at) = ?) "
				+ "AND DATE(tg.date_publication) = ?;";
		try(Connection conexion = conn.conectar();
				PreparedStatement pre = conexion.prepareStatement(query);){
			
			pre.setInt(1, users_id);
			pre.setString(2,format1.format(date));
			pre.setString(3, format1.format(date));
			ResultSet rs = pre.executeQuery();
			if(rs.next()) {		
				taskG = new Task_Grid();
				taskG.setTasks_grid_id(rs.getInt("tg.tasks_grid_id"));
				taskG.setCategories_id(rs.getInt("tg.categories_id"));
				taskG.setGeneres_id(rs.getInt("tg.generes_id"));
				taskG.setPhrase(rs.getString("tg.phrase"));
				taskG.setImage(rs.getString("tg.image"));
				taskG.setGroups(rs.getBoolean("tg.isGroups"));
				taskG.setFanPage(rs.getBoolean("tg.isFanPage"));
				taskG.setAddGroups(rs.getBoolean("tg.isAddGroups"));
				taskG.setActive(rs.getBoolean("tg.active"));
				taskG.setQuantity_groups(rs.getInt("tg.quantity_groups"));
				taskG.setQuantity_min(rs.getInt("tg.quantity_min"));
				taskG.setDate_publication(rs.getString("tg.date_publication"));
				taskG.setDb_admin_tasks_id(rs.getInt("tg.db_admin_tasks_id"));
			}
				
		}catch(SQLException e) {
			System.err.println(e);
		}
		
		return taskG;
	}
	
	public int getTasks_grid_id() {
		return tasks_grid_id;
	}

	public void setTasks_grid_id(int tasks_grid_id) {
		this.tasks_grid_id = tasks_grid_id;
	}

	public int getCategories_id() {
		return categories_id;
	}

	public void setCategories_id(int categories_id) {
		this.categories_id = categories_id;
	}

	public int getGeneres_id() {
		return generes_id;
	}

	public void setGeneres_id(int generes_id) {
		this.generes_id = generes_id;
	}

	public String getPhrase() {
		return phrase;
	}

	public void setPhrase(String phrase) {
		this.phrase = phrase;
	}

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}

	public String getDate_publication() {
		return date_publication;
	}

	public void setDate_publication(String date_publication) {
		this.date_publication = date_publication;
	}

	public boolean isFanPage() {
		return isFanPage;
	}

	public void setFanPage(boolean isFanPage) {
		this.isFanPage = isFanPage;
	}

	public boolean isGroups() {
		return isGroups;
	}

	public void setGroups(boolean isGroups) {
		this.isGroups = isGroups;
	}

	public boolean isAddGroups() {
		return isAddGroups;
	}

	public void setAddGroups(boolean isAddGroups) {
		this.isAddGroups = isAddGroups;
	}

	public int getQuantity_groups() {
		return quantity_groups;
	}

	public void setQuantity_groups(int quantity_groups) {
		this.quantity_groups = quantity_groups;
	}

	public int getQuantity_min() {
		return quantity_min;
	}

	public void setQuantity_min(int quantity_min) {
		this.quantity_min = quantity_min;
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

	public int getDb_admin_tasks_id() {
		return db_admin_tasks_id;
	}

	public void setDb_admin_tasks_id(int db_admin_tasks_id) {
		this.db_admin_tasks_id = db_admin_tasks_id;
	}


}
