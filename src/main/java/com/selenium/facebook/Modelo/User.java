package com.selenium.facebook.Modelo;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

import com.selenium.facebook.Interface.Model;


public class User implements Model{
	
	private final String TABLE_NAME = "users";
	private int users_id;
	private String username;
	private String email;
	private String full_name;
	private BigInteger phone;
	private String password;
	private String creator;
	private String date_of_birth;
	private int categories_id;
	private int sim_card_number;
	private int vpn_id;
	private boolean activo;
	private static Conexion conn = new Conexion();
	private Date date = new Date();
	private DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
	private String created = dateFormat.format(date);
	Statement st;
	ResultSet rs;
	
	
	public String[] getUser() throws SQLException{
		String[] list = null;
		Connection conexion = conn.conectar();
		try {
			
			st = (Statement) conexion.createStatement();
			String query = "SELECT * FROM "+TABLE_NAME+" us "
					+ "INNER JOIN vpn vp ON vp.vpn_id = us.vpn_id "
					+ "INNER JOIN users_categories uc ON uc.users_id = us.users_id "
					+ "INNER JOIN categories ca ON ca.categories_id = uc.categories_id "
					+ "WHERE us.email = '"+getEmail()+"' OR us.username= '"+getUsername()+"';";
			rs = st.executeQuery(query);

			
			list = new String[8];
			while (rs.next() ) {
               list[0] =  rs.getString("us.users_id");
               list[1] = rs.getString("us.username");
               list[2] = rs.getString("us.phone");
               list[3] = rs.getString("us.password");
               list[4] = rs.getString("vp.name");
               list[5] = rs.getString("us.email");
               list[6] = rs.getString("uc.categories_id");
               list[7] = rs.getString("ca.name");
               
			}
			
			conexion.close();
		}catch(Exception e) {
			System.err.println(e);
		}
		
		return list;
 	}
	
	
	public String[] getOneRandom() throws SQLException{
		String[] list = null;
		Connection conexion = conn.conectar();
		
		
		try {
			
			st = (Statement) conexion.createStatement();
			rs = st.executeQuery("SELECT * FROM "+TABLE_NAME+" us "
					+ "INNER JOIN vpn vp ON vp.vpn_id = us.vpn_id "
					+ "ORDER BY rand() LIMIT 1");

			
			list = new String[6];
			while (rs.next() ) {
               list[0] =  rs.getString("us.users_id");
               list[1] = rs.getString("us.username");
               list[2] = rs.getString("us.phone");
               list[3] = rs.getString("us.password");
               list[4] = rs.getString("vp.name");
               list[5] = rs.getString("us.email");
               
			}
			conexion.close();
		}catch(Exception e) {
			System.err.println(e);
		}
		
		return list;
		
	}
	
	public int getIdUser(){
		int id = 0;
		Connection conexion = conn.conectar();
		
		
		try {
			
			st = (Statement) conexion.createStatement();
			rs = st.executeQuery("SELECT us.users_id FROM "+TABLE_NAME+" us WHERE username = '"+getUsername()+"' GROUP BY us.users_id;");

			
			while (rs.next() ) {
               id =  rs.getInt("us.users_id");
			}
			conexion.close();
		}catch(SQLException e) {
			System.err.println(e);
		}
		
		return id;
		
	}
	
	public void insert() {
		Connection conexion = conn.conectar();
		try {
			String insert = "INSERT INTO "+TABLE_NAME+"(username,email,full_name,phone,password,creator,date_of_birth,created,sim_card_number,vpn_id)"
					+ " VALUES (?, ?, ?,?, ?, ? , ?, ?, ?,?);";
			PreparedStatement exe = conexion.prepareStatement(insert);
			exe.setString(1, getUsername());
			exe.setString(2, getEmail());
			exe.setString(3, getFull_name());
			exe.setBigDecimal(4, new BigDecimal(getPhone()));
			exe.setString(5, getPassword());
			exe.setString(6, getCreator());
			exe.setString(7, getDate_of_birth());
			exe.setString(8, getCreated());
			exe.setInt(9, getSim_card_number());
			exe.setInt(10, getVpn_id());
			
			exe.executeUpdate();
			
			User_Categorie usercate = new User_Categorie();
			usercate.setCategories_id(getCategories_id());
			usercate.setUsers_id(getIdUser());
			usercate.insert();
			
			conexion.close();
		}catch(SQLException e) {
			System.err.println(e);
		}
		
		
	}
	
	@Override
	public void update() throws SQLException {
		
	}
	
	public String getDifferentRandomUser(String username) throws SQLException{
		String list = null;
		
		Connection conexion = conn.conectar();
		
		try {
			String query = "SELECT * FROM "+TABLE_NAME+" us" 
					+ " WHERE NOT us.username = '"+username+"' "
					+ " GROUP BY RAND() LIMIT 1;";
			st = (Statement) conexion.createStatement();
			rs = st.executeQuery(query);

			
			
			while (rs.next() ) {
				list = rs.getString("us.username");
			}
			conexion.close();
		}catch(Exception e) {
			System.err.println(e);
		}
		
		return list;
		
	}
	
	public List<String[]> getUserCategorie(int id) throws SQLException{
		String[] list ;
		ArrayList<String[]> lista = new ArrayList<String[]>();
		Connection conexion = conn.conectar();
		int io = 0;
		try {
			
			st = (Statement) conexion.createStatement();
			String query = "SELECT us.users_id,us.username,us.phone,us.password,vp.name,us.email,uc.categories_id,ub.users_block_id, count(*) as canpost "
					+ "FROM "+TABLE_NAME+" us "
					+ "INNER JOIN vpn vp ON vp.vpn_id = us.vpn_id "
					+ "INNER JOIN users_categories uc ON uc.users_id = us.users_id "
					+ "LEFT JOIN users_block ub ON ub.users_id = us.users_id AND ub.active = 1 "
					+ "LEFT JOIN posts po ON po.users_id = us.users_id AND po.created_at BETWEEN "
					+ "'"+dateFormat.format(date) + " 00:00:00' AND '"+dateFormat.format(date) + " 23:59:59' "
					+ "WHERE uc.categories_id = "+id+" "
					+ "GROUP BY us.users_id,us.username,us.phone,us.password,vp.name,us.email,uc.categories_id,ub.users_block_id;";
			rs = st.executeQuery(query);

			
			
			while (rs.next() ) {
			   list = new String[9];
               list[0] = rs.getString("us.users_id");
               list[1] = rs.getString("us.username");
               list[2] = rs.getString("us.phone");
               list[3] = rs.getString("us.password");
               list[4] = rs.getString("vp.name");
               list[5] = rs.getString("us.email");
               list[6] = rs.getString("uc.categories_id");
               list[7] = rs.getString("ub.users_block_id");
               list[8] = rs.getString("canpost");
               lista.add(io, list);
               io++;
			}
			
			conexion.close();
		}catch(Exception e) {
			System.err.println(e);
		}
		
		return lista;
 	}
	
	public List<String> getUserCategories() throws SQLException{
		List<String> list = new ArrayList<String>();
		Connection conexion = conn.conectar();
		try {
			
			st = (Statement) conexion.createStatement();
			String query = "SELECT us.username "
					+ "FROM "+TABLE_NAME+" us "
					+ "INNER JOIN users_categories uc ON uc.users_id = us.users_id "
					+ "WHERE uc.categories_id = "+getCategories_id()+" "
					+ "GROUP BY us.username";
			rs = st.executeQuery(query);

			
			
			while (rs.next() ) {
			   list.add(rs.getString("us.username"));
			}
			conexion.close();
		}catch(Exception e) {
			System.err.println(e);
		}
		return list;
	}

	public int getUsers_id() {
		return users_id;
	}


	public void setUsers_id(int users_id) {
		this.users_id = users_id;
	}


	public String getUsername() {
		return username;
	}



	public void setUsername(String username) {
		this.username = username;
	}



	public String getEmail() {
		return email;
	}



	public void setEmail(String email) {
		this.email = email;
	}



	public String getFull_name() {
		return full_name;
	}



	public void setFull_name(String full_name) {
		this.full_name = full_name;
	}



	public BigInteger getPhone() {
		return phone;
	}



	public void setPhone(BigInteger phone) {
		this.phone = phone;
	}



	public String getPassword() {
		return password;
	}



	public void setPassword(String password) {
		this.password = password;
	}



	public String getCreator() {
		return creator;
	}



	public int getCategories_id() {
		return categories_id;
	}

	public void setCategories_id(int categories_id) {
		this.categories_id = categories_id;
	}

	public void setCreator(String creator) {
		this.creator = creator;
	}



	public String getDate_of_birth() {
		return date_of_birth;
	}



	public void setDate_of_birth(String date_of_birth) {
		this.date_of_birth = date_of_birth;
	}



	public String getCreated() {
		return created;
	}



	public void setCreated(String created) {
		this.created = created;
	}



	public int getSim_card_number() {
		return sim_card_number;
	}



	public void setSim_card_number(int sim_card_number) {
		this.sim_card_number = sim_card_number;
	}



	public int getVpn_id() {
		return vpn_id;
	}



	public void setVpn_id(int vpn_id) {
		this.vpn_id = vpn_id;
	}
	
	public boolean isActivo() {
		return activo;
	}

	public void setActivo(boolean activo) {
		this.activo = activo;
	}

	
	

}
