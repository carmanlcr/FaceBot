package com.selenium.facebook.Vista;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;

import com.selenium.facebook.Modelo.User;
import com.selenium.facebook.Modelo.User_Block;
import com.selenium.facebook.Modelo.Vpn;

import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.SortedSet;
import java.util.TreeSet;
import java.awt.event.ActionEvent;

public class UpdateUser {

	private JFrame frmActualizarUsuario;
	private JTextField searchUser;
	private JTextField email;
	private JTextField username;
	private JTextField password;
	private HashMap<String, Integer> mapVpn;
	private Vpn vp = new Vpn();
	private int users_id;
	/**
	 * Launch the application.
	 */
	public void init() {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					UpdateUser window = new UpdateUser();
					window.frmActualizarUsuario.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public UpdateUser() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frmActualizarUsuario = new JFrame();
		frmActualizarUsuario.setTitle("Actualizar Usuario");
		frmActualizarUsuario.setResizable(false);
		frmActualizarUsuario.setBounds(100, 100, 418, 522);
		
		JLabel lblNewLabel = new JLabel("Usuario o Email");
		
		searchUser = new JTextField();
		searchUser.setColumns(10);
		
		JLabel lblNewLabel_1 = new JLabel("Email");
		
		email = new JTextField();
		email.setEnabled(false);
		email.setColumns(10);
		
		JLabel lblUsername = new JLabel("Username");
		
		username = new JTextField();
		username.setEnabled(false);
		username.setColumns(10);
		
		JLabel lblContrasea = new JLabel("Contraseña");
		
		password = new JTextField();
		password.setEnabled(false);
		password.setColumns(10);
		
		final JComboBox<String> vpn = new JComboBox<String>();
		
		
		
		JLabel lblActivo = new JLabel("Activo");
		
		final JCheckBox activo = new JCheckBox("");
		activo.setEnabled(false);
		
		JLabel lblVpn = new JLabel("Vpn");
		final JCheckBox block = new JCheckBox("");
		block.setEnabled(false);
		
		vpn.setEnabled(false);
		
		final JButton btnNewButton = new JButton("Actualizar");
		btnNewButton.setEnabled(false);
		mapVpn = vp.getAllVpn();
		JButton search = new JButton("Buscar");
		search.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				String usuario = searchUser.getText().trim();
				if(usuario.isEmpty()) {
					JOptionPane.showMessageDialog(null, "El campo no puede quedar vacio");
				}else {
					User us = new User();
					us.setUsername(usuario);
					us.setEmail(usuario);
					
					try {
						us = us.getUser();
					} catch (SQLException e1) {
						System.err.println(e1);
					}
					if(us != null) {
						users_id = us.getUsers_id();
						username.setText(us.getUsername());
						username.setEditable(true);
						username.setEnabled(true);
						email.setText(us.getEmail());
						email.setEditable(true);
						email.setEnabled(true);
						password.setText(us.getPassword());
						password.setEditable(true);
						password.setEnabled(true);
						activo.setEnabled(true);
						if(us.isActive()) activo.setSelected(true);
						vpn.setEnabled(true);
						SortedSet<String> keys = new TreeSet<>(mapVpn.keySet());
						for(String st: keys) {
							vpn.addItem(st);
						}
						for(Entry<String, Integer> in : mapVpn.entrySet()) {
							if(in.getValue() == us.getVpn_id()) {
								vpn.setSelectedItem(in.getKey());
							}
						}
						block.setEnabled(true);
						block.setSelected(us.isBlock());
						btnNewButton.setEnabled(true);
					}else {
						JOptionPane.showMessageDialog(null, "Este usuario o correo no se encuentra en la base de datos");
					}
						
				}
			}
		});
		
		
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if(username.getText().isEmpty()) {
					JOptionPane.showMessageDialog(null, "El campo de username no puede estar vacio");
				}else if(password.getText().isEmpty()) {
					JOptionPane.showMessageDialog(null, "El campo de password no puede estar vacio");
				}else if(email.getText().isEmpty()) {
					JOptionPane.showMessageDialog(null, "El campo de email no puede estar vacio");
				}else {
					boolean active = false;
					int vpn_id = Integer.parseInt(mapVpn.get(vpn.getSelectedItem().toString()).toString());
					
					if(activo.isSelected()) active = true;
					User us = new User();
					us.setUsers_id(users_id);
					us.setUsername(username.getText());
					us.setPassword(password.getText());
					us.setEmail(email.getText());
					us.setActive(active);
					us.setVpn_id(vpn_id);
					try {
						us.update();
						if(!block.isSelected()) {
							User_Block usB = new User_Block();
							usB.desblockUser(username.getText());
						}
						email.setText("");
						email.setEditable(false);
						email.setEnabled(false);
						username.setText("");
						username.setEditable(false);
						username.setEnabled(false);
						password.setText("");
						password.setEditable(false);
						password.setEnabled(false);
						users_id = 0;
						activo.setSelected(false);
						activo.setEnabled(false);
						block.setSelected(false);
						block.setEnabled(false);
						vpn.removeAllItems();
						vpn.setEnabled(false);
						btnNewButton.setEnabled(false);
						JOptionPane.showMessageDialog(null, "Se actualizo con exito");
						
					} catch (SQLException e) {
						e.printStackTrace();
					}
				}
				
			}
		});
		
		JLabel lblBloqueado = new JLabel("Bloqueado");
		
		
		GroupLayout groupLayout = new GroupLayout(frmActualizarUsuario.getContentPane());
		groupLayout.setHorizontalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(groupLayout.createSequentialGroup()
					.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
						.addGroup(groupLayout.createSequentialGroup()
							.addGap(146)
							.addComponent(search))
						.addGroup(groupLayout.createSequentialGroup()
							.addGap(30)
							.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
								.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
									.addGroup(groupLayout.createParallelGroup(Alignment.LEADING, false)
										.addComponent(lblNewLabel_1, GroupLayout.PREFERRED_SIZE, 39, GroupLayout.PREFERRED_SIZE)
										.addComponent(lblActivo, GroupLayout.PREFERRED_SIZE, 58, GroupLayout.PREFERRED_SIZE)
										.addComponent(lblContrasea, GroupLayout.DEFAULT_SIZE, 67, Short.MAX_VALUE)
										.addComponent(lblUsername, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
									.addComponent(lblVpn, GroupLayout.PREFERRED_SIZE, 58, GroupLayout.PREFERRED_SIZE)
									.addComponent(lblBloqueado, GroupLayout.DEFAULT_SIZE, 93, Short.MAX_VALUE))
								.addComponent(lblNewLabel, GroupLayout.PREFERRED_SIZE, 87, GroupLayout.PREFERRED_SIZE))
							.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
								.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
									.addGroup(groupLayout.createSequentialGroup()
										.addGap(25)
										.addGroup(groupLayout.createParallelGroup(Alignment.TRAILING)
											.addComponent(searchUser, GroupLayout.PREFERRED_SIZE, 143, GroupLayout.PREFERRED_SIZE)
											.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
												.addComponent(username, GroupLayout.DEFAULT_SIZE, 196, Short.MAX_VALUE)
												.addComponent(password, GroupLayout.DEFAULT_SIZE, 196, Short.MAX_VALUE)
												.addComponent(email, GroupLayout.DEFAULT_SIZE, 196, Short.MAX_VALUE))))
									.addGroup(groupLayout.createSequentialGroup()
										.addGap(61)
										.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
											.addComponent(block, GroupLayout.PREFERRED_SIZE, 21, GroupLayout.PREFERRED_SIZE)
											.addComponent(activo))))
								.addGroup(groupLayout.createSequentialGroup()
									.addGap(69)
									.addComponent(vpn, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)))))
					.addGap(45))
				.addGroup(Alignment.TRAILING, groupLayout.createSequentialGroup()
					.addContainerGap(181, Short.MAX_VALUE)
					.addComponent(btnNewButton)
					.addGap(152))
		);
		groupLayout.setVerticalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(groupLayout.createSequentialGroup()
					.addGap(30)
					.addGroup(groupLayout.createParallelGroup(Alignment.BASELINE)
						.addComponent(lblNewLabel)
						.addComponent(searchUser, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addGap(30)
					.addComponent(search)
					.addGap(31)
					.addGroup(groupLayout.createParallelGroup(Alignment.BASELINE)
						.addComponent(lblNewLabel_1)
						.addComponent(email, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addGap(28)
					.addGroup(groupLayout.createParallelGroup(Alignment.BASELINE)
						.addComponent(lblUsername)
						.addComponent(username, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addGap(35)
					.addGroup(groupLayout.createParallelGroup(Alignment.BASELINE)
						.addComponent(lblContrasea)
						.addComponent(password, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addGap(32)
					.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
						.addGroup(groupLayout.createSequentialGroup()
							.addComponent(lblActivo)
							.addGap(27)
							.addComponent(lblBloqueado))
						.addGroup(groupLayout.createSequentialGroup()
							.addComponent(activo, GroupLayout.PREFERRED_SIZE, 21, GroupLayout.PREFERRED_SIZE)
							.addGap(18)
							.addComponent(block, GroupLayout.PREFERRED_SIZE, 21, GroupLayout.PREFERRED_SIZE)))
					.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
						.addGroup(groupLayout.createSequentialGroup()
							.addGap(20)
							.addComponent(lblVpn))
						.addGroup(groupLayout.createSequentialGroup()
							.addGap(18)
							.addComponent(vpn, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)))
					.addGap(48)
					.addComponent(btnNewButton)
					.addGap(36))
		);
		frmActualizarUsuario.getContentPane().setLayout(groupLayout);
	}
}
