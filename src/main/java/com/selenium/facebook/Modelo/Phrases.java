package com.selenium.facebook.Modelo;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.selenium.facebook.Interface.Model;


public class Phrases implements Model{
	
	
	private String phrase;
	private boolean active;
	private int categories_id;
	private int sub_categories_id;
	private int generes_id;
	private static Conexion conn = new Conexion();
	
	
	public void insert() throws SQLException {
		Statement st = null;
		Connection conexion = conn.conectar();
		Date date = new Date();
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy/MM/dd H:m:s");
		String strDate= formatter.format(date);
		String insert = "";
			try {
				if(getGeneres_id() == 0) {
					
					
					insert = "INSERT INTO phrases(phrase,created_at,updated_at,categories_id,sub_categories_id) "
							+ "VALUE ('"+getPhrase()+"','"+strDate+"', '"+strDate+"',"+getCategories_id()+","+getSub_categories_id()+");";
				}else if(getSub_categories_id() == 0) {
					insert = "INSERT INTO phrases(phrase,created_at,updated_at,categories_id,generes_id) "
							+ "VALUE ('"+getPhrase()+"','"+strDate+"','"+strDate+"',"+getCategories_id()+","+getGeneres_id()+");";
				}else {
					insert = "INSERT INTO phrases(phrase,created_at,updated_at,categories_id,sub_categories_id,generes_id) "
							+ "VALUE ('"+getPhrase()+"','"+strDate+"','"+strDate+"',"+getCategories_id()+","+getSub_categories_id()+","
							+getGeneres_id()+");";
				}
				st = (Statement) conexion.createStatement();
				st.executeUpdate(insert);
				
				conexion.close();
			} catch(SQLException e)  {
				System.err.println(e);
			} catch(Exception e){
				System.err.println(e);
				
			}finally {
				st.close();
				conexion.close();
			}
			
	}
	
	@Override
	public void update() throws SQLException {
		
	}
	 
	public String getPhraseRandom() throws SQLException{
		
		String list = "";
		Connection conexion = conn.conectar();
		Statement st = (Statement) conexion.createStatement();
		ResultSet rs = null;
		try {
			
			String queryExce = "SELECT ph.phrase FROM phrases ph "
					+ "WHERE ph.active = ? AND ph.categories_id = ? "
					+ "AND ph.generes_id = ? "
					+ "ORDER BY RAND() LIMIT 1;";
			PreparedStatement  query = (PreparedStatement) conexion.prepareStatement(queryExce);
			query.setInt(1, 1);
			query.setInt(2, getCategories_id());
			query.setInt(3,getGeneres_id());
			rs = query.executeQuery();

			while (rs.next() ) {
               list +=  rs.getString("phrase");
			}
		}catch(Exception e) {
			System.err.println(e);
		}finally {
			st.close();
			conexion.close();
		}
		
		return list;
 	}
	
	public String getPhraseRandomSubCategorie() throws SQLException{
		
		String list = "";
		Connection conexion = conn.conectar();
		Statement st = (Statement) conexion.createStatement();
		ResultSet rs = null;
		try {
			
			String queryExce = "SELECT ph.phrase FROM phrases ph "
					+ "WHERE ph.active = ? AND ph.categories_id = ? "
					+ "AND ph.sub_categories_id = ? "
					+ "ORDER BY RAND() LIMIT 1;";
			PreparedStatement  query = (PreparedStatement) conexion.prepareStatement(queryExce);
			query.setInt(1, 1);
			query.setInt(2, getCategories_id());
			query.setInt(3, getSub_categories_id());
			rs = query.executeQuery();

			while (rs.next() ) {
               list +=  rs.getString("phrase");
			}
		}catch(Exception e) {
			System.err.println(e);
		}finally {
			st.close();
			conexion.close();
		}
		
		return list;
 	}
	
	public String getPhrase() {
		return phrase;
	}
	public void setPhrase(String phrase) {
		this.phrase = phrase;
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

	public void setCategories_id(int campaings_id) {
		this.categories_id = campaings_id;
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
