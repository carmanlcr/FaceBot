package com.selenium.facebook.Modelo;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import com.selenium.facebook.Interface.Model;

import configurations.connection.ConnectionFB;


public class Post implements Model{
	
	private static final String TABLE_NAME ="posts";
	private int posts_id;
	private int users_id;
	private int categories_id;
	private int tasks_model_id;
	private int tasks_grid_id;
	private int tasks_maduration_id;
	private String link_post;
	private boolean isFanPage;
	private boolean isMaduration;
	private String created_at;
	private String updated_at; 
	private String groups;
	private Calendar c = Calendar.getInstance();
	private Date date;
	private DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd H:m:s");
	private DateFormat dateFormat1 = new SimpleDateFormat("yyyy-MM-dd");
	
	private static ConnectionFB conn = new ConnectionFB();
	Statement st;
	ResultSet rs;

	public void insert() {
		date = new Date();
		setCreated_at(dateFormat.format(date));
		setUpdated_at(dateFormat.format(date));
		String insert = "INSERT INTO "+TABLE_NAME+"(users_id,categories_id,tasks_model_id,tasks_grid_id,tasks_maduration_id,link_post,isFanPage,isMaduration,groups,created_at,updated_at) "
				+ " VALUE (?,?,?,?,?,?,?,?,?,?,?);";
		try (Connection conexion = conn.conectar();
				PreparedStatement  query = conexion.prepareStatement(insert);){
			
			
			query.setInt(1, getUsers_id());
			query.setInt(2, getCategories_id());
			query.setInt(3, getTasks_model_id());
			query.setInt(4, getTasks_grid_id());
			query.setInt(5, getTasks_maduration_id());
			query.setString(6, getLink_post());
			query.setBoolean(7, isFanPage());
			query.setBoolean(8, isMaduration());
			query.setString(9, getGroups());
			query.setString(10, getCreated_at());
			query.setString(11, getUpdated_at());
			query.executeUpdate();
			
		}catch(SQLException e) {
			System.err.println(e);
		}
		
		
	}
	
	public List<String[]> getCountPostUsers(int categories_id){
		List<String[]> list = new ArrayList<>();
		String[] array = null;
		int increment = 0;
		date = c.getTime();
		String created_at = dateFormat1.format(date);
		String query = " SELECT c.username usuario, COUNT(DISTINCT(c.groups)) cuenta FROM "
				+ " (SELECT us.username, pt.created_at, pt.groups "
				+ " FROM users us "
				+ " LEFT JOIN "+TABLE_NAME+" pt ON pt.users_id = us.users_id AND categories_id =  ? "
				+ " WHERE DATE(pt.created_at) = ?) AS c "
				+ " GROUP BY c.username; ";
		ResultSet rs = null;
		try (Connection conexion = conn.conectar();
				PreparedStatement pst = conexion.prepareStatement(query);){
			
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
			rs.close();
		}catch(Exception e) {
			System.err.println(e);
		}
		
		return list;
	}
	
	public int getCountPostUser() {
		int post = 0;
		 date = c.getTime();
		String date1 = dateFormat1.format(date);
		String query = "SELECT count(*) cuenta FROM "+TABLE_NAME + 
				" WHERE users_id = ? AND DATE(created_at) = ?;";
		
		try (Connection conexion = conn.conectar();
				PreparedStatement pst = conexion.prepareStatement(query);) {
			
			pst.setInt(1, getUsers_id());
			pst.setString(2, date1);
			rs = pst.executeQuery();
			
			if(rs.next()) post = rs.getInt("cuenta");
			
		}catch (SQLException e) {
			e.getStackTrace();
		}finally {
			if(rs != null) try {rs.close(); } catch(SQLException e) {e.getStackTrace(); }
		}
		return post;
	}
	
	public int getLast() {
		int id = 0;
		
		String query = "SELECT po.posts_id FROM "+TABLE_NAME+" po ORDER BY po.posts_id DESC LIMIT 1";
		
		try (Connection conexion = conn.conectar();
				Statement st = conexion.createStatement();
				ResultSet rs = st.executeQuery(query)){
			
			while (rs.next() ) {
               id =  rs.getInt("po.posts_id");
			}
		}catch(SQLException e) {
			System.err.println(e);
		}
		
		return id;
	} 
	
	
	public int getLastsTasktPublic(){
		int idTask = 0;
		date = c.getTime();
		String queryExce = "SELECT * FROM tasks_model tm " + 
				"WHERE tm.tasks_model_id NOT IN (SELECT pt.tasks_model_id FROM "+TABLE_NAME+" pt WHERE users_id = ? AND DATE(pt.created_at) BETWEEN ? AND ? AND isMaduration = 0)" + 
				"ORDER BY RAND() LIMIT 1;";
		try (Connection conexion = conn.conectar();
				PreparedStatement  query = conexion.prepareStatement(queryExce);){
			
	
		
		query.setInt(1, getUsers_id());
		query.setString(2, dateFormat1.format(new Date( date.getTime()-86400000)));
		query.setString(3, dateFormat1.format(date));
		
		rs = query.executeQuery();
			
		if(rs.next()) {
			idTask = rs.getInt("tm.tasks_model_id");
		}
		}catch(SQLException e) {
			e.getStackTrace();
		}
		return idTask;
	}
	
	public Post getPostForGroup() {
		Post po = new Post();
		c.add(Calendar.DAY_OF_MONTH, -1);
		Date date1 = c.getTime();
		date = c.getTime();
		String query = "SELECT * FROM posts po WHERE DATE(created_at) BETWEEN ? AND ? AND po.groups = ?;";
		try (Connection conexion = conn.conectar();
				PreparedStatement exe = conexion.prepareStatement(query);){
			
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
				po.setTasks_maduration_id(rs.getInt("po.tasks_maduration_id"));
				po.setUsers_id(rs.getInt("po.users_id"));
			}
		}catch(SQLException e) {
			e.getStackTrace();
		}
		
		return po;
	}
	
	public List<Post> getPostForComments() {
		List<Post> listPost = new ArrayList<>();
		date = new Date();
		StringBuilder query = new StringBuilder();
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.add(Calendar.DAY_OF_YEAR, -3); 
		Date date1 = c.getTime();
		
		query.append("SELECT p.* FROM posts p ");
		query.append("INNER JOIN users_groups ug ON ug.groups_id = p.groups AND ug.users_id = ? ");
		query.append("WHERE p.posts_id NOT IN (SELECT pc.posts_id FROM posts_comments pc  ");
		query.append("INNER JOIN comments c ON c.comments_id = pc.comments_id AND c.isNormal = 1 ");
		query.append("WHERE pc.users_id = ?) ");
		query.append("AND p.users_id <> ? AND p.link_post IS NOT NULL AND DATE(p.created_at)>=?;");
		
		try (Connection conexion = conn.conectar();
				PreparedStatement exe = conexion.prepareStatement(query.toString());){
			exe.setInt(1, getUsers_id());
			exe.setInt(2, getUsers_id());
			exe.setInt(3, getUsers_id());
			exe.setString(4, dateFormat1.format(date1));
			ResultSet result = exe.executeQuery();
			
			while(result.next()){
				Post post = new Post();
				post.setPosts_id(result.getInt("p.posts_id"));
				post.setUsers_id(result.getInt("p.users_id"));
				post.setCategories_id(result.getInt("p.categories_id"));
				post.setTasks_grid_id(result.getInt("p.tasks_grid_id"));
				post.setTasks_maduration_id(result.getInt("p.tasks_maduration_id"));
				post.setLink_post(result.getString("p.link_post"));
				post.setMaduration(result.getBoolean("p.isMaduration"));
				post.setFanPage(result.getBoolean("p.isFanPage"));
				post.setGroups(result.getString("p.groups"));
				listPost.add(post);
			}
			
			result.close();
		}catch(SQLException e) {
			e.getStackTrace();
		}
		return listPost;
	}
	
	public List<Post> getMyPostforComment() {
		List<Post> listPost = new ArrayList<>();
		date = new Date();
		StringBuilder query = new StringBuilder();
		Date date1 = new Date();
		
		query.append("SELECT p.* FROM posts p ");
		query.append("WHERE p.posts_id NOT IN (SELECT pc.posts_id FROM posts_comments pc ");
		query.append("INNER JOIN comments c ON c.comments_id = pc.comments_id AND c.isNormal = 0 ");
		query.append("WHERE pc.users_id = ?) ");
		query.append("AND p.users_id = ? AND p.link_post IS NOT NULL AND DATE(p.created_at)=DATE_SUB(?, INTERVAL 1 DAY);");
		
		try (Connection conexion = conn.conectar();
				PreparedStatement exe = conexion.prepareStatement(query.toString());){
			exe.setInt(1, getUsers_id());
			exe.setInt(2, getUsers_id());
			exe.setString(3, dateFormat1.format(date1));
			ResultSet result = exe.executeQuery();
			
			while(result.next()){
				Post post = new Post();
				post.setPosts_id(result.getInt("p.posts_id"));
				post.setUsers_id(result.getInt("p.users_id"));
				post.setCategories_id(result.getInt("p.categories_id"));
				post.setTasks_grid_id(result.getInt("p.tasks_grid_id"));
				post.setTasks_maduration_id(result.getInt("p.tasks_maduration_id"));
				post.setLink_post(result.getString("p.link_post"));
				post.setMaduration(result.getBoolean("p.isMaduration"));
				post.setFanPage(result.getBoolean("p.isFanPage"));
				post.setGroups(result.getString("p.groups"));
				listPost.add(post);
			}
			
			result.close();
		}catch(SQLException e) {
			e.getStackTrace();
		}
		return listPost;
	}
	@Override
	public void update() throws SQLException {
		// None
		
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

	public String getUpdated_at() {
		return updated_at;
	}

	public void setUpdated_at(String updated_at) {
		this.updated_at = updated_at;
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

	public int getTasks_grid_id() {
		return tasks_grid_id;
	}

	public void setTasks_grid_id(int tasks_grid_id) {
		this.tasks_grid_id = tasks_grid_id;
	}

	public int getTasks_maduration_id() {
		return tasks_maduration_id;
	}

	public void setTasks_maduration_id(int tasks_maduration_id) {
		this.tasks_maduration_id = tasks_maduration_id;
	}

	public String getLink_post() {
		return link_post;
	}

	public void setLink_post(String link_post) {
		this.link_post = link_post;
	}

	public boolean isFanPage() {
		return isFanPage;
	}

	public void setFanPage(boolean isFanPage) {
		this.isFanPage = isFanPage;
	}

	public boolean isMaduration() {
		return isMaduration;
	}

	public void setMaduration(boolean isMaduration) {
		this.isMaduration = isMaduration;
	}

	public String getGroups() {
		return groups;
	}

	public void setGroups(String groups) {
		this.groups = groups;
	}

	
	
}
