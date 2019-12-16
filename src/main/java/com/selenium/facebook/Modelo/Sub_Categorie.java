package com.selenium.facebook.Modelo;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.selenium.facebook.Interface.Model;


public class Sub_Categorie implements Model {
	
	private final String TABLE_NAME = "sub_categories";
	private String name;
	private int categories_id;
	private static Conexion conn = new Conexion();
	
	public void insert() throws SQLException {
		
		Date date = new Date();
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd H:m:s");
		String strDate= formatter.format(date);
			try (Connection conexion = conn.conectar();){
				String insert = "INSERT INTO "+TABLE_NAME+"(name,categories_id,created_at,updated_at) "
						+ "VALUES (?,?,?,?);";
				PreparedStatement exe = conexion.prepareStatement(insert);
				exe.setString(1, getName());
				exe.setInt(2, getCategories_id());
				exe.setString(3, strDate);
				exe.setString(4, strDate);
				exe.executeUpdate();
				
			} catch(SQLException e)  {
				System.err.println(e);
			} catch(Exception e){
				System.err.println(e);
				
			}
	}
	
	@Override
	public void update() throws SQLException {
		
	}
	
	public List<String> getSubCategories(){
		List<String> list = new ArrayList<String>();

		
		ResultSet rs = null;
		try (Connection conexion = conn.conectar();){
			
			String queryExce = "SELECT sca.name FROM "+TABLE_NAME+" sca "
					+ "WHERE sca.categories_id = ? ; ";
			
			PreparedStatement  query = (PreparedStatement) conexion.prepareStatement(queryExce);
			query.setInt(1, getCategories_id());
			rs = query.executeQuery();

			while (rs.next() ) {
               list.add(rs.getString("sca.name"));
			}
		}catch(Exception e) {
			System.err.println(e);
		}
		
		return list;
	}

	public int getIdPhraseSubCategories(String name) throws SQLException {

		int indice = 0;
		
		ResultSet rs = null;
		try (Connection conexion = conn.conectar();){
			
			String queryExce = "SELECT sca.sub_categories_id FROM "+TABLE_NAME+" sca "
					+ "WHERE sca.name = ? LIMIT 1; ";
			
			PreparedStatement  query = (PreparedStatement) conexion.prepareStatement(queryExce);
			query.setString(1, name);
			rs = query.executeQuery();

			while (rs.next() ) {
               indice =  rs.getInt("sca.sub_categories_id");
			}
		}catch(Exception e) {
			System.err.println(e);
		}
		
		return indice;
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getCategories_id() {
		return categories_id;
	}

	public void setCategories_id(int categories_id) {
		this.categories_id = categories_id;
	}

}
