package com.selenium.facebook.Modelo;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.selenium.facebook.Interface.Model;

import configurations.connection.ConnectionFB;


public class Phrases implements Model{
	
	private static final String TABLE_NAME = "phrases";
	private String phrase;
	private boolean active;
	private int categories_id;
	private int sub_categories_id;
	private int generes_id;
	private ConnectionFB conn = new ConnectionFB();
	
	
	public void insert() throws SQLException {
		Date date = new Date();
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy/MM/dd H:m:s");
		String strDate= formatter.format(date);
		String insert = "";
			try (Connection conexion = conn.conectar();
					Statement st = conexion.createStatement();){
				if(getGeneres_id() == 0) {
					
					
					insert = "INSERT INTO "+TABLE_NAME+"(phrase,created_at,updated_at,categories_id,sub_categories_id) "
							+ "VALUE ('"+getPhrase()+"','"+strDate+"', '"+strDate+"',"+getCategories_id()+","+getSub_categories_id()+");";
				}else if(getSub_categories_id() == 0) {
					insert = "INSERT INTO "+TABLE_NAME+"(phrase,created_at,updated_at,categories_id,generes_id) "
							+ "VALUE ('"+getPhrase()+"','"+strDate+"','"+strDate+"',"+getCategories_id()+","+getGeneres_id()+");";
				}else {
					insert = "INSERT INTO "+TABLE_NAME+"(phrase,created_at,updated_at,categories_id,sub_categories_id,generes_id) "
							+ "VALUE ('"+getPhrase()+"','"+strDate+"','"+strDate+"',"+getCategories_id()+","+getSub_categories_id()+","
							+getGeneres_id()+");";
				}
				st.executeUpdate(insert);
				
				conexion.close();
			} catch(SQLException e)  {
				System.err.println(e);
			} 
			
			
	}
	
	@Override
	public void update() throws SQLException {
		//None
	}
	 
	public Phrases getPhraseRandom() throws SQLException{
		Phrases ph = null;
		
		
		ResultSet rs = null;
		try (Connection conexion = conn.conectar();
				Statement st = conexion.createStatement();){
			
			String queryExce = "SELECT * FROM "+TABLE_NAME+" ph "
					+ "WHERE ph.active = ? AND ph.categories_id = ? "
					+ "AND ph.generes_id = ? "
					+ "ORDER BY RAND() LIMIT 1;";
			PreparedStatement  query =  conexion.prepareStatement(queryExce);
			query.setInt(1, 1);
			query.setInt(2, getCategories_id());
			query.setInt(3,getGeneres_id());
			rs = query.executeQuery();

			if(rs.next() ) {
				ph = new Phrases();
				ph.setPhrase(rs.getString("ph.phrase"));
				ph.setCategories_id(rs.getInt("ph.categories_id"));
				ph.setGeneres_id(rs.getInt("ph.generes_id"));
				ph.setSub_categories_id(rs.getInt("ph.sub_categories_id"));
				ph.setActive(rs.getBoolean("ph.active"));
			}
		}catch(Exception e) {
			System.err.println(e);
		}
		
		return ph;
 	}
	
	public String getPhraseRandomSubCategorie() throws SQLException{
		
		String list = "";
		
		
		ResultSet rs = null;
		try (Connection conexion = conn.conectar();
				Statement st = conexion.createStatement();){
			
			String queryExce = "SELECT ph.phrase FROM "+TABLE_NAME+" ph "
					+ "WHERE ph.active = ? AND ph.categories_id = ? "
					+ "AND ph.sub_categories_id = ? "
					+ "ORDER BY RAND() LIMIT 1;";
			PreparedStatement  query = conexion.prepareStatement(queryExce);
			query.setInt(1, 1);
			query.setInt(2, getCategories_id());
			query.setInt(3, getSub_categories_id());
			rs = query.executeQuery();

			while (rs.next() ) {
               list +=  rs.getString("phrase");
			}
		}catch(Exception e) {
			System.err.println(e);
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
