package com.selenium.facebook.Vista;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.EmptyBorder;

import com.mysql.jdbc.MysqlDataTruncation;
import com.mysql.jdbc.exceptions.jdbc4.MySQLIntegrityConstraintViolationException;
import com.selenium.facebook.Modelo.*;

import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.math.BigInteger;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.SortedSet;
import java.util.TreeSet;

import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.JComboBox;
import javax.swing.JButton;

public class RegistrarUsuario extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JTextField NameField = new JTextField();
	private JTextField usernameField = new JTextField();
	private JTextField emailField = new JTextField();
	private JTextField telefonoField = new JTextField();
	private JTextField passwordField = new JTextField();
	private JTextField creadorField = new JTextField();
	private JTextField fdnField = new JTextField();
	private JTextField simCardField = new JTextField();
	private Vpn v = new Vpn();
	private HashMap<String, Integer> vpn = v.getAllVpn();
	private JComboBox<String> comboBoxvPN_1 = new JComboBox<>();
	private Categorie cate = new Categorie();
	private HashMap<String, Integer> list = cate.getComboBox();
	private JComboBox<String> comboBoxCategori_1  = new JComboBox<>();
	private JButton btnRegistrar = new JButton("Registrar");
	private RegistrarUsuario frame;
	
	/**
	 * Launch the application.
	 */
	public void inicio() {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					frame = new RegistrarUsuario();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 * @throws SQLException 
	 */
	public RegistrarUsuario() throws SQLException {
		setTitle("Registrar nuevo usuario");
		setResizable(false);
		setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
		setBounds(100, 100, 555, 576);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		
		JLabel labelName = new JLabel("Nombre Completo");
		labelName.setFont(new Font("Arial", Font.PLAIN, 12));
		
		NameField.setColumns(10);
		NameField.setFocusable(true);
		
		JLabel labelUsername = new JLabel("Username");
		labelUsername.setFont(new Font("Arial", Font.PLAIN, 12));
		
		usernameField.setColumns(10);
		
		JLabel labelEmail = new JLabel("Email");
		labelEmail.setFont(new Font("Arial", Font.PLAIN, 12));
		
		emailField.setColumns(10);
		
		JLabel labelPhone = new JLabel("Telefono");
		labelPhone.setFont(new Font("Arial", Font.PLAIN, 12));
		
		telefonoField.setColumns(10);
		
		JLabel labelPassword = new JLabel("Password");
		labelPassword.setFont(new Font("Arial", Font.PLAIN, 12));
		
		passwordField.setColumns(10);
		
		JLabel labelCreador = new JLabel("Creador");
		labelCreador.setFont(new Font("Arial", Font.PLAIN, 12));
		
		creadorField.setColumns(10);
		
		JLabel labelFechaDeNacimiento = new JLabel("Fecha de Nacimiento");
		labelFechaDeNacimiento.setFont(new Font("Arial", Font.PLAIN, 12));
		
		fdnField.setColumns(10);
		
		JLabel lblyyyymmdd = new JLabel("(YYYY-mm-dd)");
		lblyyyymmdd.setFont(new Font("Arial", Font.PLAIN, 12));
		
		JLabel lblNmeroDeSim = new JLabel("N\u00FAmero de Sim Card");
		lblNmeroDeSim.setFont(new Font("Arial", Font.PLAIN, 12));
		
		simCardField.setColumns(10);
		
		JScrollPane scroll = new JScrollPane();
		JPanel panel = new JPanel();
		
		scroll.add(panel);
		
		SortedSet<String> keys = new TreeSet<>(list.keySet());
		comboBoxCategori_1 = setComboBoxCategorias(keys);
		SortedSet<String> keysVpn = new TreeSet<>(vpn.keySet());
		comboBoxvPN_1 = setComboBoxVpn(keysVpn);
		
		JLabel lblVpn = new JLabel("VPN");
		lblVpn.setFont(new Font("Arial", Font.PLAIN, 12));
		
		JLabel lblCategories = new JLabel("Categorias");
		lblCategories.setFont(new Font("Arial", Font.PLAIN, 12));
		actionListenerRegister();
		GroupLayout gl_contentPane = new GroupLayout(contentPane);
		gl_contentPane.setHorizontalGroup(
			gl_contentPane.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_contentPane.createSequentialGroup()
					.addGap(41)
					.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
						.addComponent(lblNmeroDeSim)
						.addGroup(gl_contentPane.createSequentialGroup()
							.addGroup(gl_contentPane.createParallelGroup(Alignment.TRAILING)
								.addGroup(gl_contentPane.createSequentialGroup()
									.addComponent(labelFechaDeNacimiento)
									.addGap(188))
								.addGroup(gl_contentPane.createSequentialGroup()
									.addComponent(labelPassword)
									.addPreferredGap(ComponentPlacement.RELATED, 102, Short.MAX_VALUE)
									.addComponent(passwordField, GroupLayout.PREFERRED_SIZE, 147, GroupLayout.PREFERRED_SIZE))
								.addGroup(gl_contentPane.createSequentialGroup()
									.addComponent(labelPhone)
									.addPreferredGap(ComponentPlacement.RELATED, 110, Short.MAX_VALUE)
									.addComponent(telefonoField, GroupLayout.PREFERRED_SIZE, 147, GroupLayout.PREFERRED_SIZE))
								.addGroup(gl_contentPane.createSequentialGroup()
									.addComponent(labelEmail)
									.addPreferredGap(ComponentPlacement.RELATED, 126, Short.MAX_VALUE)
									.addComponent(emailField, GroupLayout.PREFERRED_SIZE, 147, GroupLayout.PREFERRED_SIZE))
								.addGroup(gl_contentPane.createSequentialGroup()
									.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
										.addGroup(gl_contentPane.createSequentialGroup()
											.addComponent(labelName)
											.addGap(46))
										.addGroup(gl_contentPane.createSequentialGroup()
											.addComponent(labelUsername, GroupLayout.DEFAULT_SIZE, 154, Short.MAX_VALUE)
											.addPreferredGap(ComponentPlacement.RELATED)))
									.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING, false)
										.addComponent(usernameField)
										.addComponent(NameField, GroupLayout.DEFAULT_SIZE, 147, Short.MAX_VALUE)))
								.addGroup(gl_contentPane.createSequentialGroup()
									.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
										.addComponent(labelCreador)
										.addComponent(lblVpn)
										.addComponent(lblCategories))
									.addPreferredGap(ComponentPlacement.RELATED, 97, Short.MAX_VALUE)
									.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING, false)
										.addComponent(fdnField)
										.addComponent(creadorField, GroupLayout.DEFAULT_SIZE, 147, Short.MAX_VALUE)
										.addComponent(simCardField)
										.addComponent(comboBoxCategori_1, 0, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
										.addComponent(comboBoxvPN_1, 0, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
							.addPreferredGap(ComponentPlacement.UNRELATED)
							.addComponent(lblyyyymmdd)
							.addGap(368))))
				.addGroup(gl_contentPane.createSequentialGroup()
					.addGap(163)
					.addComponent(btnRegistrar)
					.addContainerGap(564, Short.MAX_VALUE))
		);
		gl_contentPane.setVerticalGroup(
			gl_contentPane.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_contentPane.createSequentialGroup()
					.addGap(22)
					.addGroup(gl_contentPane.createParallelGroup(Alignment.BASELINE)
						.addComponent(labelName, GroupLayout.PREFERRED_SIZE, 22, GroupLayout.PREFERRED_SIZE)
						.addComponent(NameField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addGap(26)
					.addGroup(gl_contentPane.createParallelGroup(Alignment.BASELINE)
						.addComponent(usernameField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addComponent(labelUsername, GroupLayout.PREFERRED_SIZE, 23, GroupLayout.PREFERRED_SIZE))
					.addGap(26)
					.addGroup(gl_contentPane.createParallelGroup(Alignment.BASELINE)
						.addComponent(emailField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addComponent(labelEmail))
					.addGap(27)
					.addGroup(gl_contentPane.createParallelGroup(Alignment.BASELINE)
						.addComponent(labelPhone)
						.addComponent(telefonoField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addGap(27)
					.addGroup(gl_contentPane.createParallelGroup(Alignment.BASELINE)
						.addComponent(labelPassword)
						.addComponent(passwordField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addGap(29)
					.addGroup(gl_contentPane.createParallelGroup(Alignment.BASELINE)
						.addComponent(labelCreador)
						.addComponent(creadorField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addGap(27)
					.addGroup(gl_contentPane.createParallelGroup(Alignment.BASELINE)
						.addComponent(labelFechaDeNacimiento)
						.addComponent(fdnField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addComponent(lblyyyymmdd))
					.addGap(28)
					.addGroup(gl_contentPane.createParallelGroup(Alignment.BASELINE)
						.addComponent(lblNmeroDeSim)
						.addComponent(simCardField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addGap(39)
					.addGroup(gl_contentPane.createParallelGroup(Alignment.BASELINE)
						.addComponent(comboBoxvPN_1, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addComponent(lblVpn))
					.addGap(22)
					.addGroup(gl_contentPane.createParallelGroup(Alignment.BASELINE)
						.addComponent(comboBoxCategori_1, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addComponent(lblCategories))
					.addPreferredGap(ComponentPlacement.RELATED, 15, Short.MAX_VALUE)
					.addComponent(btnRegistrar)
					.addGap(22))
		);
		contentPane.setLayout(gl_contentPane);
		

	}
	

	private JComboBox<String> setComboBoxVpn(final SortedSet<String> keysVpn) {
		comboBoxvPN_1 = new JComboBox<>();
	
		for (String jugador : keysVpn){
			
			comboBoxvPN_1.addItem(jugador);
		}
	    return comboBoxvPN_1;
	}
	
	private JComboBox<String> setComboBoxCategorias(SortedSet<String> keys) {
		comboBoxCategori_1 = new JComboBox<>();
		
		for (String string : keys) {
			comboBoxCategori_1.addItem(string);
		}
	    return comboBoxCategori_1;
	}
	
	private void actionListenerRegister(){
		btnRegistrar.addActionListener(new ActionListener() {
			
			public void actionPerformed(ActionEvent e) {

			BigInteger telefono = new BigInteger(telefonoField.getText().trim());

			User usuario = new User();
			usuario.setFull_name(NameField.getText().trim());
			usuario.setUsername(usernameField.getText().trim());
			usuario.setEmail(emailField.getText().trim());
			usuario.setPassword(passwordField.getText().trim());
			usuario.setPhone(telefono);
			usuario.setCreator(creadorField.getText().trim());
			usuario.setDate_of_birth(fdnField.getText().trim());
			usuario.setSim_card_number(Integer.parseInt(simCardField.getText().trim()));
			usuario.setVpn_id(Integer.parseInt(vpn.get(comboBoxvPN_1.getSelectedItem().toString()).toString()));
			usuario.setCategories_id(Integer.parseInt(list.get(comboBoxCategori_1.getSelectedItem().toString()).toString()));
			contentPane.setEnabled(false);
			try {
				usuario.insert();
				JOptionPane.showMessageDialog(null,"Usuario agregado con exito");
				NameField.setText("");
				NameField.setFocusable(true);
				telefonoField.setText("");
				usernameField.setText("");
				emailField.setText("");
				passwordField.setText("");
				creadorField.setText("");
				fdnField.setText("");
				simCardField.setText("");
				comboBoxvPN_1.setSelectedIndex(0);
				comboBoxCategori_1.setSelectedIndex(0);
			}catch(MysqlDataTruncation e3) {
				JOptionPane.showMessageDialog(null, "Hay error en uno de los datos ingresados, por favor validar","Failed",JOptionPane.ERROR_MESSAGE);
			}catch(MySQLIntegrityConstraintViolationException e2) {
				JOptionPane.showMessageDialog(null, "Usuario o correo repetido, por favor validar","Failed",JOptionPane.ERROR_MESSAGE);
			}catch (SQLException e1) {
				JOptionPane.showMessageDialog(null, "Error al ingresar usuario, por favor validar los campos","Failed",JOptionPane.ERROR_MESSAGE);
				}
				
			}
				
		});
	}
}
