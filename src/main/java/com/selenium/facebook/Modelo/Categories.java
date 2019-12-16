package com.selenium.facebook.Modelo;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.selenium.facebook.Interface.Model;


public class Categories implements Model{

	private final String TABLE_NAME = "categories";
	private String name;
	private static Conexion conn = new Conexion();
	
	
	public void insert() {
		String insert = "INSERT INTO "+TABLE_NAME+"(name) "
				+ "VALUES (?);";
			try (Connection conexion = conn.conectar();
				PreparedStatement exe = conexion.prepareStatement(insert);){
				exe.setString(1, getName());
				exe.executeUpdate();
				
			} catch(SQLException e)  {
				System.err.println(e);
			} catch(Exception e){
				System.err.println(e);
			}
			
	}
	
	public void update() throws SQLException {
		
	}
	
	public List<String> getAllActive()  {
		ArrayList<String> list = new ArrayList<String>();

		String queryExce = "SELECT * FROM "+TABLE_NAME+" WHERE active = ? ;";
		
		ResultSet rs;
		try (Connection conexion = conn.conectar()){
			PreparedStatement  query = conexion.prepareStatement(queryExce);
			query.setInt(1, 1);
			rs = query.executeQuery();
			while (rs.next() ) {
				list.add(rs.getString("name"));
			}
			rs.close();
		}catch(SQLException e) {
			System.err.println(e);
		}
		
		return list;
	}

	public int getIdCategories(String name) throws SQLException {
		int id = 0;
		String query = "SELECT * FROM "+TABLE_NAME+" WHERE name = '"+name+"' AND active = 1;";
		try(Connection conexion = conn.conectar();
				Statement st = (Statement) conexion.createStatement();
				ResultSet rs = st.executeQuery(query);) {
		
			while (rs.next() ) {
				id = rs.getInt("categories_id");
               
			}
			
		}catch(Exception e) {
			System.err.println(e);
		}
		
		return id;
		
	}
	
	public int getIdCategorieHashTag(String name) throws SQLException {
		int id = 0;
		String query = "SELECT * FROM "+TABLE_NAME+" WHERE name = '"+name+"' AND active = 0;";
		try (Connection conexion = conn.conectar();
				Statement st = conexion.createStatement();
				ResultSet rs = st.executeQuery(query)){
			
			while (rs.next() ) {
				id = rs.getInt("categories_id");
               
			}
		}catch(Exception e) {
			System.err.println(e);
		}
		
		return id;
		
	}
	
	public String getNameCategories(int id) throws SQLException {
		String name = "";
		String query = "SELECT * FROM "+TABLE_NAME+" WHERE categories_id = "+id+";";
		
		try (Connection conexion = conn.conectar();
				Statement st = conexion.createStatement();
				ResultSet rs = st.executeQuery(query)){
			
			
			while (rs.next() ) {
				name = rs.getString("name");
			}
		}catch(Exception e) {
			System.err.println(e);
		}
		
		return name;
		
	}
	
	public List<Integer> getSubCategorieConcat() throws SQLException {
		List<Integer> concat = new ArrayList<Integer>();
		String query = "SELECT ca.categories_id, ca.name, sb.sub_categories_id, sb.name "
				+"FROM "+TABLE_NAME+" ca " + 
				"INNER JOIN sub_categories sb ON ca.categories_id = sb.categories_id " + 
				"ORDER BY RAND() LIMIT 1;";
		try (Connection conexion = conn.conectar();
				Statement st = conexion.createStatement();
				ResultSet rs = st.executeQuery(query)){
			
			if(rs.next()) {
				concat.add(rs.getInt("ca.categories_id"));
				concat.add(rs.getInt("sb.sub_categories_id"));
			}
		}catch(Exception e) {
			System.err.println(e);
		}	
				
		return concat;
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	
	
	
}
