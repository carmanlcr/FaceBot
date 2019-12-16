package com.selenium.facebook.Modelo;

import java.sql.DriverManager;
import java.sql.SQLException;

import java.sql.Connection;

public class Conexion {
	private final String URL = "jdbc:mysql://HOST:3306/"; // Ubicaci�n de la BD.
    private final String BD = "facebook"; // Nombre de la BD.
    private final String USER = "username"; //Nomber del usuario
    private final String PASSWORD = "password"; //contrase�a

    public Connection connect = null;

    @SuppressWarnings("finally")
	public Connection conectar() {
        try {
        	Class.forName("com.mysql.cj.jdbc.Driver");
            connect = (Connection) DriverManager.getConnection(URL + BD, USER, PASSWORD);
            
        }catch(SQLException e1) {
        	System.err.println("Error al conectarse a la base de datos "+e1);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            return connect;
        }
    }
}
