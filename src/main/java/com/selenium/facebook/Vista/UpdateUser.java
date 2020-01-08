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
import javax.swing.LayoutStyle.ComponentPlacement;

import com.selenium.facebook.Modelo.User;
import com.selenium.facebook.Modelo.Vpn;

import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map.Entry;
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
		frmActualizarUsuario.setBounds(100, 100, 395, 477);
		
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
		
		JLabel lblVpn = new JLabel("Vpn");
		
		
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
						if(us.isActive()) activo.setSelected(true);
						vpn.setEnabled(true);
						for(String st: mapVpn.keySet()) {
							vpn.addItem(st);
						}
						for(Entry<String, Integer> in : mapVpn.entrySet()) {
							if(in.getValue() == us.getVpn_id()) {
								vpn.setSelectedItem(in.getKey());
							}
						}
						btnNewButton.setEnabled(true);
					} catch (SQLException e) {
						e.printStackTrace();
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
								.addGroup(groupLayout.createSequentialGroup()
									.addComponent(lblNewLabel, GroupLayout.PREFERRED_SIZE, 87, GroupLayout.PREFERRED_SIZE)
									.addGap(62)
									.addComponent(searchUser, GroupLayout.PREFERRED_SIZE, 143, GroupLayout.PREFERRED_SIZE))
								.addGroup(groupLayout.createSequentialGroup()
									.addGroup(groupLayout.createParallelGroup(Alignment.LEADING, false)
										.addComponent(lblNewLabel_1, GroupLayout.PREFERRED_SIZE, 39, GroupLayout.PREFERRED_SIZE)
										.addComponent(lblActivo, GroupLayout.PREFERRED_SIZE, 58, GroupLayout.PREFERRED_SIZE)
										.addComponent(lblContrasea, GroupLayout.DEFAULT_SIZE, 67, Short.MAX_VALUE)
										.addComponent(lblUsername, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
										.addComponent(lblVpn, GroupLayout.PREFERRED_SIZE, 58, GroupLayout.PREFERRED_SIZE))
									.addPreferredGap(ComponentPlacement.RELATED)
									.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
										.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
											.addGroup(groupLayout.createSequentialGroup()
												.addGap(86)
												.addComponent(activo))
											.addGroup(groupLayout.createSequentialGroup()
												.addGap(47)
												.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
													.addComponent(username, GroupLayout.DEFAULT_SIZE, 178, Short.MAX_VALUE)
													.addComponent(email, GroupLayout.DEFAULT_SIZE, 178, Short.MAX_VALUE)
													.addComponent(password, GroupLayout.DEFAULT_SIZE, 178, Short.MAX_VALUE))))
										.addGroup(Alignment.TRAILING, groupLayout.createSequentialGroup()
											.addComponent(vpn, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
											.addGap(78)))))))
					.addGap(67))
				.addGroup(Alignment.TRAILING, groupLayout.createSequentialGroup()
					.addContainerGap(156, Short.MAX_VALUE)
					.addComponent(btnNewButton)
					.addGap(154))
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
						.addComponent(lblActivo)
						.addComponent(activo, GroupLayout.PREFERRED_SIZE, 21, GroupLayout.PREFERRED_SIZE))
					.addGap(28)
					.addGroup(groupLayout.createParallelGroup(Alignment.BASELINE)
						.addComponent(lblVpn)
						.addComponent(vpn, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addGap(39)
					.addComponent(btnNewButton)
					.addGap(67))
		);
		frmActualizarUsuario.getContentPane().setLayout(groupLayout);
	}
}
