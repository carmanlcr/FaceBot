package com.selenium.facebook.Modelo;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.selenium.facebook.Interface.Model;

import configurations.connection.ConnectionFB;


public class HashTag implements Model{

	private static final String TABLE_NAME = "hashtag";
	private String name;
	private boolean active;
	private int created_at;
	private int categories_id;
	private int sub_categories_id;
	private int generes_id;
	private static ConnectionFB conn = new ConnectionFB();
	
	public void insert() throws SQLException {
		
		Date date = new Date();
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd H:m:s");
		String strDate= formatter.format(date);
		String insert = "";
			try (Connection conexion = conn.conectar();
					Statement st = conexion.createStatement();){
				if(getGeneres_id() == 0) {
					insert = "INSERT INTO "+TABLE_NAME+"(name,created_at,categories_id,sub_categories_id) "
							+ "VALUE ('"+getName()+"','"+strDate+"',"+getCategories_id()+","+getSub_categories_id()+");";
				}else if(getSub_categories_id() == 0) {
					insert = "INSERT INTO "+TABLE_NAME+"(name,created_at,categories_id,generes_id) "
							+ "VALUE ('"+getName()+"','"+strDate+"',"+getCategories_id()+","+getGeneres_id()+");";
				}else {
					insert = "INSERT INTO "+TABLE_NAME+"(name,created_at,categories_id,sub_categories_id,generes_id) "
							+ "VALUE ('"+getName()+"','"+strDate+"',"+getCategories_id()+","+getSub_categories_id()+","
							+getGeneres_id()+");";
				}
				st.executeUpdate(insert);
			} catch(Exception e)  {
				System.err.println(e);
			} 
			
		
	}
	
	@Override
	public void update() throws SQLException {
		//None
	}
	
	public String[] getHashTagRandom() throws SQLException{
		
		String[] list = new String[4];
		int indice = 0;
		
		ResultSet rs = null;
		String queryExce = "SELECT ht.name FROM "+TABLE_NAME+" ht "
				+ "WHERE ht.active = ? AND ht.generes_id = ? "
				+ "AND ht.categories_id = ? "
				+ "ORDER BY RAND() LIMIT 4;";
		try (Connection conexion = conn.conectar();
				PreparedStatement  query =  conexion.prepareStatement(queryExce);){
			
			
			
			query.setInt(1, 1);
			query.setInt(2, getGeneres_id());
			query.setInt(3, getCategories_id());
			rs = query.executeQuery();
			conexion.close();
			while (rs.next() ) {
               list[indice] =  rs.getString("phrase");
               indice++;
			}
		}catch(Exception e) {
			System.err.println(e);
		}
		
		return list;
 	}
	
	public List<String> getHashTagForCategories(){
		List<String> list = new ArrayList<>();
		
		String queryExce = "SELECT ht.name FROM "+TABLE_NAME+" ht "
				+ "WHERE ht.active = ? AND ht.generes_id = ? "
				+ "AND ht.categories_id = ? ; ";
		ResultSet rs = null;
		try (Connection conexion = conn.conectar();
				PreparedStatement  query =  conexion.prepareStatement(queryExce);){
			
			
			
			query.setInt(1, 1);
			query.setInt(2, getGeneres_id());
			query.setInt(3, getCategories_id());
			rs = query.executeQuery();

			while (rs.next() ) {
               list.add(rs.getString("name"));
			}
		}catch(SQLException e) {
			System.err.println(e);
		}
		
		return list;
	}
	
	public int getIdCategorieHashTag() throws SQLException {
		int id = 0;
		ResultSet rs = null;
		String queryExce = "SELECT ht.hashtag_id "
		         + "FROM "+TABLE_NAME+" ht "
		         + "WHERE ht.active = ? AND ht.name = ? "
		         + "AND ht.categories_id = ? AND generes_id = ?;";
		
		try (Connection conexion = conn.conectar();
				PreparedStatement  query =  conexion.prepareStatement(queryExce);){
			
			
			query.setInt(1, 1);
			query.setString(2, getName());
			query.setInt(3, getCategories_id());
			query.setInt(4, getGeneres_id());
			
			rs = query.executeQuery();
			
			while (rs.next() ) {
				id = rs.getInt("hashtag_id");
               
			}
		}catch(Exception e) {
			System.err.println(e);
		}
		
		return id;
		
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}

	public int getCreated_at() {
		return created_at;
	}

	public void setCreated_at(int created_at) {
		this.created_at = created_at;
	}

	public int getCategories_id() {
		return categories_id;
	}

	public void setCategories_id(int categories_id) {
		this.categories_id = categories_id;
	}

	public int getSub_categories_id() {
		return sub_categories_id;
	}

	public void setSub_categories_id(int sub_categories_id) {
		this.sub_categories_id = sub_categories_id;
	}

	public int getGeneres_id() {
		return generes_id;
	}

	public void setGeneres_id(int generes_id) {
		this.generes_id = generes_id;
	}

}
