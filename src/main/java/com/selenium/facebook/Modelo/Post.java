package com.selenium.facebook.Modelo;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import com.mysql.jdbc.Statement;
import com.selenium.facebook.Interface.Model;


public class Post implements Model{
	
	private final String TABLE_NAME ="posts";
	private int posts_id;
	private int users_id;
	private int categories_id;
	private String link_post;
	private String created_at; 
	private String groups;
	private int tasks_model_id;
	private Calendar c = Calendar.getInstance();
	private Date date = c.getTime();
	private DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd H:m:s");
	private DateFormat dateFormat1 = new SimpleDateFormat("yyyy-MM-dd");
	
	private static Conexion conn = new Conexion();
	Statement st;
	ResultSet rs;

	public void insert() {
		setCreated_at(dateFormat.format(date));
		Connection conexion = conn.conectar();
		try {
			String insert = "INSERT INTO "+TABLE_NAME+"(users_id,categories_id,tasks_model_id,link_post,groups,created_at) "
					+ " VALUE (?,?,?,?,?,?);";
			PreparedStatement  query = (PreparedStatement) conexion.prepareStatement(insert);
			query.setInt(1, getUsers_id());
			query.setInt(2, getCategories_id());
			query.setInt(3, getTasks_model_id());
			query.setString(4, getLink_post());
			query.setString(5, getGroups());
			query.setString(6, getCreated_at());
			
			query.executeUpdate();
			
			conexion.close();
			conexion.close();
		}catch(SQLException e) {
			System.err.println(e);
		}
		
		
	}
	
	public List<String[]> getCountPostUsers(int categories_id){
		List<String[]> list = new ArrayList<String[]>();
		String[] array = null;
		int increment = 0;
		String created_at = dateFormat1.format(date);
		String query = " SELECT c.username usuario, COUNT(*) cuenta FROM "
				+ " (SELECT us.username, pt.created_at "
				+ " FROM users us "
				+ " LEFT JOIN "+TABLE_NAME+" pt ON pt.users_id = us.users_id AND categories_id =  ? "
				+ " WHERE DATE(pt.created_at) = ?) AS c "
				+ " GROUP BY c.username; ";
		ResultSet rs = null;
		Connection conexion = conn.conectar();
		
		try {
			PreparedStatement pst = (PreparedStatement) conexion.prepareStatement(query);
			pst.setInt(1, categories_id);
			pst.setString(2, created_at);
			rs = pst.executeQuery();

			
			while (rs.next() ) {
				array = new String[2];
				array[0] = rs.getString("usuario");
				array[1] = rs.getString("cuenta");
				list.add(increment, array);
				increment++;
			}
			conexion.close();
		}catch(Exception e) {
			System.err.println(e);
		}
		
		return list;
	}
	
	public int getCountPostUser() {
		int post = 0;
		String date1 = dateFormat1.format(date);
		String query = "SELECT count(*) cuenta FROM "+TABLE_NAME + 
				" WHERE users_id = ? AND DATE(created_at) = ?;";
		Connection conexion = conn.conectar();
		try {
			PreparedStatement pst = (PreparedStatement) conexion.prepareStatement(query);
			pst.setInt(1, getUsers_id());
			pst.setString(2, date1);
			rs = pst.executeQuery();
			
			if(rs.next()) post = rs.getInt("cuenta");
			
			conexion.close();
		}catch (SQLException e) {
			e.getStackTrace();
		}
		return post;
	}
	
	public int getLast() {
		int id = 0;
		Connection conexion = conn.conectar();
		
		
		try {
			
			st = (Statement) conexion.createStatement();
			rs = st.executeQuery("SELECT po.posts_id FROM "+TABLE_NAME+" po ORDER BY po.posts_id DESC LIMIT 1");

			
			while (rs.next() ) {
               id =  rs.getInt("po.posts_id");
			}
			conexion.close();
		}catch(SQLException e) {
			System.err.println(e);
		}
		
		return id;
	} 
	
	
	public int getLastsTasktPublic(){
		int idTask = 0;
		Connection conexion = conn.conectar();
		
		try {
			String queryExce = "SELECT * FROM tasks_model tm " + 
					"WHERE tm.tasks_model_id NOT IN (SELECT pt.tasks_model_id FROM "+TABLE_NAME+" pt WHERE users_id = ? AND DATE(pt.created_at) BETWEEN ? AND ?) " + 
					"ORDER BY RAND() LIMIT 1;";
	
		PreparedStatement  query = (PreparedStatement) conexion.prepareStatement(queryExce);
		query.setInt(1, getUsers_id());
		query.setString(2, dateFormat1.format(new Date( date.getTime()-86400000)));
		query.setString(3, dateFormat1.format(date));
		
		rs = query.executeQuery();
			
		if(rs.next()) {
			idTask = rs.getInt("tm.tasks_model_id");
		}
		conexion.close();
		}catch(SQLException e) {
			e.getStackTrace();
		}
		return idTask;
	}
	
	public Post getPostForGroup() {
		Post po = new Post();
		c.add(Calendar.DAY_OF_MONTH, -1);
		Date date1 = c.getTime();
		Connection conexion = conn.conectar();
		String query = "SELECT * FROM posts po WHERE DATE(created_at) BETWEEN ? AND ? AND po.groups = ?;";
		try {
			PreparedStatement exe = (PreparedStatement) conexion.prepareStatement(query);
			exe.setString(1, dateFormat1.format(date1));
			exe.setString(2, dateFormat1.format(date));
			exe.setString(3, getGroups());
			
			rs = exe.executeQuery();
			
			if(rs.next()) {
				po.setPosts_id(rs.getInt("po.posts_id"));
				po.setCategories_id(rs.getInt("po.categories_id"));
				po.setCreated_at(rs.getString("po.created_at"));
				po.setGroups(rs.getString("po.groups"));
				po.setLink_post(rs.getString("po.link_post"));
				po.setTasks_model_id(rs.getInt("po.tasks_model_id"));
				po.setUsers_id(rs.getInt("po.users_id"));
			}
			conexion.close();
		}catch(SQLException e) {
			e.getStackTrace();
		}
		
		return po;
	}
	
	@Override
	public void update() throws SQLException {
		// TODO Auto-generated method stub
		
	}
	
	public int getPosts_id() {
		return posts_id;
	}

	public void setPosts_id(int posts_id) {
		this.posts_id = posts_id;
	}

	public int getCategories_id() {
		return categories_id;
	}

	public void setCategories_id(int categories_id) {
		this.categories_id = categories_id;
	}

	public String getCreated_at() {
		return created_at;
	}

	public void setCreated_at(String created) {
		this.created_at = created;
	}

	public int getUsers_id() {
		return users_id;
	}

	public void setUsers_id(int users_id) {
		this.users_id = users_id;
	}

	public int getTasks_model_id() {
		return tasks_model_id;
	}

	public void setTasks_model_id(int tasks_model_id) {
		this.tasks_model_id = tasks_model_id;
	}

	public String getLink_post() {
		return link_post;
	}

	public void setLink_post(String link_post) {
		this.link_post = link_post;
	}

	public String getGroups() {
		return groups;
	}

	public void setGroups(String groups) {
		this.groups = groups;
	}

	
	
}
