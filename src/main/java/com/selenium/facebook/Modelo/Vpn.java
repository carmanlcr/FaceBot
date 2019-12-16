package com.selenium.facebook.Modelo;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.selenium.facebook.Interface.Model;


public class Vpn implements Model{
	
	private final String TABLE_NAME = "vpn";
	private String name;
	private boolean activo;
	private static Conexion conn = new Conexion();
	
	public List<String> getAllActive() throws SQLException {

		Connection conexion = conn.conectar();
		List<String> list = new ArrayList<String>();
		Statement st = null;
	    ResultSet rs = null;
		try {
			st = (Statement) conexion.createStatement();
			
			
			rs = st.executeQuery("SELECT name FROM "+TABLE_NAME+" WHERE active = 1 ORDER BY name ASC");
			
			list.add("Seleccione");
			while (rs.next() ) {
				list.add(rs.getString("name"));
               
			}
			conexion.close();
		}catch(Exception e) {
			System.err.println(e);
		}finally {
			st.close();
			conexion.close();
		}
		
		return list;
	}
	
	public String getNameVPN(String name) throws SQLException {
		String nameVpn = "";
		Statement st = null;
		ResultSet rs = null;
		Connection conexion = conn.conectar();
		try {
			
			st = (Statement) conexion.createStatement();
			
			
			rs = st.executeQuery("SELECT * FROM "+TABLE_NAME+" WHERE name = '"+nameVpn+"';");
			
			while (rs.next() ) {
				nameVpn = rs.getString("name");
               
			}
			conexion.close();
		}catch(Exception e) {
			System.err.println(e);
		}finally{
			st.close();
			conexion.close();
		}
		
		return nameVpn;
		
	}
	
	public int getFind(String name) throws SQLException {
		int idVpn = 0;
		ResultSet rs = null;
		Statement st = null;
		Connection conexion = conn.conectar();
		try {
			
			st = (Statement) conexion.createStatement();
			String query = "SELECT * FROM "+TABLE_NAME+" WHERE name = '"+name+"';";
			
			rs = st.executeQuery(query);
			
			while (rs.next() ) {
				idVpn = rs.getInt("vpn_id");
               
			}
			conexion.close();
		}catch(Exception e) {
			System.err.println(e);
		}finally {
			rs.close();
			conexion.close();
		}
			
		
		
		return idVpn;
	}
	
	public int findOrCreate(String name) throws SQLException {
		int idVpn = 0;
		ResultSet rs = null;
		Statement st = null;
		Connection conexion = conn.conectar();
		try {
			
			st = (Statement) conexion.createStatement();
			String query = "SELECT * FROM "+TABLE_NAME+" WHERE UPPER(name) = '"+name.toUpperCase()+"';";
			
			rs = st.executeQuery(query);
			
			while (rs.next() ) {
				idVpn = rs.getInt("vpn_id");
               
			}
			conexion.close();
		}catch(Exception e) {
			System.err.println(e);
		}finally {
			rs.close();
			conexion.close();
		}
			
		if(idVpn==0) {
			setName(name);
			insert();
		}
		
		return idVpn;
	}
	
	public void insert() throws SQLException {
		Connection conexion = conn.conectar();

		try {
			String insert = "INSERT INTO "+TABLE_NAME+"(name) VALUES (?);";
			PreparedStatement exe = conexion.prepareStatement(insert);
			exe.setString(1, getName());
			
			exe.executeUpdate();
			conexion.close();
		} catch(Exception e)  {
			System.err.println(e);
		}
			

		
	}
	
	@Override
	public void update() throws SQLException {
		
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public boolean isActivo() {
		return activo;
	}

	public void setActivo(boolean activo) {
		this.activo = activo;
	}

	
}
