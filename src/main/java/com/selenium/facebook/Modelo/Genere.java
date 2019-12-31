package com.selenium.facebook.Modelo;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import com.selenium.facebook.Interface.Model;


public class Genere implements Model{
	private final String TABLE_NAME = "generes";
	private int generes_id;
	private String name;
	private String fan_page;
	private String created_at;
	private int categories_id;
	private boolean active;
	private Date date = new Date();
	private DateFormat dateFormatDateTime = new SimpleDateFormat("yyyy-MM-dd H:m:s");
	private static Conexion conn = new Conexion();
	Statement st;
	ResultSet rs;
	
	public void insert() {
		
		setCreated_at(dateFormatDateTime.format(date));
		String insert = "INSERT INTO "+TABLE_NAME+"(name,fan_page,created_at,categories_id) VALUE "
				+ " (?,?,?,?);";
		try (Connection conexion = conn.conectar();
				PreparedStatement exe = (PreparedStatement) conexion.prepareStatement(insert);){
			exe.setString(1, getName());
			exe.setString(2, getFan_page());
			exe.setString(3, getCreated_at());
			exe.setInt(4, getCategories_id());
			exe.executeUpdate();
			
		}catch(SQLException e) {
			System.err.println(e);
		}
		
	}
	
	@Override
	public void update() throws SQLException {
		
	}
	
	public List<String> getGeneresActive(){
		List<String> list = new ArrayList<String>();
		
		String query = "SELECT g.generes_id, g.name FROM "+TABLE_NAME+" g " + 
				"WHERE categories_id = ? AND active = ?;"; 
		try (Connection conexion = conn.conectar();){
			PreparedStatement pst = conexion.prepareStatement(query);
			pst.setInt(1, getCategories_id());
			pst.setInt(2, 1);
			rs = pst.executeQuery();
			while (rs.next() ) {
				list.add(rs.getString("g.name"));
			}
		}catch(SQLException e) {
			System.err.println(e);
		}
		
		
		
		return list;
	}
	
	public HashMap<String,Integer> getGeneresForCategorieActive(){
		HashMap<String,Integer> mapGe = new HashMap<String,Integer>();
		
		String query = "SELECT g.generes_id, g.name FROM "+TABLE_NAME+" g " + 
				"INNER JOIN path_photos pp ON pp.generes_id = g.generes_id AND pp.active = 1 " + 
				"INNER JOIN phrases ph ON ph.generes_id = g.generes_id AND ph.active = 1 " + 
				"INNER JOIN hashtag ht ON ht.generes_id = g.generes_id AND ht.active = 1 " + 
				"WHERE g.categories_id = ? AND g.active = ?;";
		
		try (Connection conexion = conn.conectar();){
			PreparedStatement pst = conexion.prepareStatement(query);
			pst.setInt(1, getCategories_id());
			pst.setInt(2, 1);
			rs = pst.executeQuery();
			while (rs.next() ) {
				mapGe.put(rs.getString("g.name"), rs.getInt("g.generes_id"));
			}
		}catch(SQLException e) {
			System.err.println(e);
		}
		
		
		
		return mapGe;
	}
	
	public List<String> getGeneresActiveWithPhrasesHashTagPhoto(){
		List<String> list = new ArrayList<String>();
		
		String query = "SELECT DISTINCT(g.generes_id), g.name FROM (" + 
				"SELECT g.generes_id, g.name FROM "+TABLE_NAME+" g " + 
				"INNER JOIN path_photos pp ON pp.generes_id = g.generes_id AND pp.active = ? " + 
				"INNER JOIN phrases ph ON ph.generes_id = g.generes_id AND ph.active = ? " + 
				"INNER JOIN hashtag ht ON ht.generes_id = g.generes_id AND ht.active = ? " + 
				"WHERE g.categories_id = ? AND g.active = ?) g ;"; 
		try (Connection conexion = conn.conectar();){
			PreparedStatement pst = conexion.prepareStatement(query);
			pst.setInt(1, 1);
			pst.setInt(2, 1);
			pst.setInt(3, 1);
			pst.setInt(4, getCategories_id());
			pst.setInt(5, 1);
			rs = pst.executeQuery();
			while (rs.next() ) {
				list.add(rs.getString("g.name"));
			}
		}catch(SQLException e) {
			System.err.println(e);
		}
		
		
		
		return list;
	}
	
	public int getIdGenere() {
		int idGenere = 0;
		
		String query = "SELECT g.generes_id FROM "+TABLE_NAME+" g " + 
				"WHERE g.active = ? AND g.name = ?;"; 
		try (Connection conexion = conn.conectar();){
			PreparedStatement pst = conexion.prepareStatement(query);
			pst.setInt(1, 1);
			pst.setString(2, getName());
			rs = pst.executeQuery();
			
			while (rs.next() ) {
				idGenere = rs.getInt("g.generes_id");
			}
		}catch(SQLException e) {
			System.err.println(e);
		}
		
		return idGenere;
	}
	
	public Genere getFanPage() {
		Genere gene = null;
		
		String query = "SELECT * FROM "+TABLE_NAME+" g "
				+ "WHERE fan_page IS NOT NULL AND generes_id = ? ORDER BY RAND() LIMIT 1; ";
		try(Connection conexion = conn.conectar();
				PreparedStatement pst = conexion.prepareStatement(query);){
			pst.setInt(1, getGeneres_id());
			
			rs = pst.executeQuery();
			
			if(rs.next()) {
				gene = new Genere();
				gene.setGeneres_id(getGeneres_id());
				gene.setName(rs.getString("g.name"));
				gene.setFan_page(rs.getString("g.fan_page"));
				gene.setActive(rs.getBoolean("g.active"));
				gene.setCategories_id(rs.getInt("g.categories_id"));
				gene.setCreated_at(rs.getString("g.created_at"));
			}
		}catch (SQLException e) {
			e.getStackTrace();
		}
		return gene;
		
	}
	
	public int getGeneres_id() {
		return generes_id;
	}

	public void setGeneres_id(int generes_id) {
		this.generes_id = generes_id;
	}

	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getFan_page() {
		return fan_page;
	}

	public void setFan_page(String fan_page) {
		this.fan_page = fan_page;
	}

	public String getCreated_at() {
		return created_at;
	}
	public void setCreated_at(String created_id) {
		this.created_at = created_id;
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

	public void setCategories_id(int categories_id) {
		this.categories_id = categories_id;
	}

	
}
