package com.selenium.facebook.Modelo;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.selenium.facebook.Interface.Model;


public class Question implements Model {
	
	
	private final String TABLE_NAME = "questions";
	private String question;
	private String answer;
	private String created_at;
	private Date date = new Date();
	private DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd H:m:s");
	private static Conexion conn = new Conexion();
	private ResultSet rs;
	
	@Override
	public void insert() throws SQLException {
		setCreated_at(dateFormat.format(date));
		
		try (Connection conexion = conn.conectar();){
			String insert = "INSERT INTO "+TABLE_NAME+""
					+ "(question, answer, created_at) "
					+ " VALUE (?,?,?);";
			
			PreparedStatement  query = (PreparedStatement) conexion.prepareStatement(insert);
			query.setString(1, getQuestion());
			query.setString(2, getAnswer());
			query.setString(3, getCreated_at());
			
			query.executeUpdate();
			
		}catch(SQLException e) {
			System.err.println(e);
		}
	}
	
	
	public String getAnswerQuestion() {
		String questionA = "";
		
		try (Connection conexion = conn.conectar();){
			String select = "SELECT * FROM "+TABLE_NAME+""
					+ "WHERE question = ?;";
			
			PreparedStatement  query = (PreparedStatement) conexion.prepareStatement(select);
			query.setString(1, getQuestion());
			
			rs = query.executeQuery();
			while(rs.next()) {
				questionA = rs.getString("answer");
			}
			
		}catch(SQLException e) {
			System.err.println(e);
		}
		return questionA;
		
	}

	@Override
	public void update() throws SQLException {
		//SIn update
	}

	public String getQuestion() {
		return question;
	}

	public void setQuestion(String question) {
		this.question = question;
	}

	public String getAnswer() {
		return answer;
	}

	public void setAnswer(String answer) {
		this.answer = answer;
	}

	public String getCreated_at() {
		return created_at;
	}

	public void setCreated_at(String created_at) {
		this.created_at = created_at;
	}

}
