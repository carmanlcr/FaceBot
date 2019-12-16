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
		Connection conexion = conn.conectar();
			try {
				String insert = "INSERT INTO "+TABLE_NAME+"(name) "
						+ "VALUES (?);";
				PreparedStatement exe = (PreparedStatement) conexion.prepareStatement(insert);
				exe.setString(1, getName());
				
				exe.executeUpdate();
				
				conexion.close();
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
		Connection conexion = conn.conectar();
		ResultSet rs;
		try {
			String queryExce = "SELECT * FROM "+TABLE_NAME+" WHERE active = ? ;";
			PreparedStatement  query = (PreparedStatement) conexion.prepareStatement(queryExce);
			query.setInt(1, 1);
			rs = query.executeQuery();


			while (rs.next() ) {
				list.add(rs.getString("name"));
               
			}
			conexion.close();
		}catch(SQLException e) {
			System.err.println(e);
		}
		
		return list;
	}

	public int getIdCategories(String name) throws SQLException {
		int id = 0;
		Statement st = null;
		ResultSet rs = null;
		Connection conexion = conn.conectar();
		try {
			
			st = (Statement) conexion.createStatement();
			
			
			rs = st.executeQuery("SELECT * FROM "+TABLE_NAME+" WHERE name = '"+name+"' AND active = 1;");
			
			while (rs.next() ) {
				id = rs.getInt("categories_id");
               
			}
			conexion.close();
		}catch(Exception e) {
			System.err.println(e);
		}finally{
			st.close();
			conexion.close();
		}
		
		return id;
		
	}
	
	public int getIdCategorieHashTag(String name) throws SQLException {
		int id = 0;
		Statement st = null;
		ResultSet rs = null;
		Connection conexion = conn.conectar();
		try {
			
			st = (Statement) conexion.createStatement();
			
			
			rs = st.executeQuery("SELECT * FROM "+TABLE_NAME+" WHERE name = '"+name+"' AND active = 0;");
			
			while (rs.next() ) {
				id = rs.getInt("categories_id");
               
			}
			conexion.close();
		}catch(Exception e) {
			System.err.println(e);
		}finally{
			st.close();
			conexion.close();
		}
		
		return id;
		
	}
	
	public String getNameCategories(int id) throws SQLException {
		String name = "";
		Statement st = null;
		ResultSet rs = null;
		Connection conexion = conn.conectar();
		try {
			
			st = (Statement) conexion.createStatement();
			
			
			rs = st.executeQuery("SELECT * FROM "+TABLE_NAME+" WHERE categories_id = "+id+";");
			
			while (rs.next() ) {
				name = rs.getString("name");
               
			}
			conexion.close();
		}catch(Exception e) {
			System.err.println(e);
		}finally{
			st.close();
			conexion.close();
		}
		
		return name;
		
	}
	
	public List<Integer> getSubCategorieConcat() throws SQLException {
		List<Integer> concat = new ArrayList<Integer>();
		Statement st = null;
		ResultSet rs = null;
		Connection conexion = conn.conectar();
		try {
			
			st = (Statement) conexion.createStatement();
			
			
			rs = st.executeQuery("SELECT ca.categories_id, ca.name, sb.sub_categories_id, sb.name "
					+"FROM "+TABLE_NAME+" ca " + 
					"INNER JOIN sub_categories sb ON ca.categories_id = sb.categories_id " + 
					"ORDER BY RAND() LIMIT 1;");
			
			if(rs.next()) {
				concat.add(rs.getInt("ca.categories_id"));
				concat.add(rs.getInt("sb.sub_categories_id"));
			}
			conexion.close();
		}catch(Exception e) {
			System.err.println(e);
		}finally{
			st.close();
			conexion.close();
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
