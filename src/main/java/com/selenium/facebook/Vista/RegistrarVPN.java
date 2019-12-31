package com.selenium.facebook.Vista;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import com.selenium.facebook.Modelo.Vpn;

import javax.swing.GroupLayout;
import javax.swing.ImageIcon;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import java.awt.Font;
import javax.swing.JTextField;
import javax.swing.JButton;
import javax.swing.LayoutStyle.ComponentPlacement;
import java.awt.Color;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.awt.event.ActionEvent;

public class RegistrarVPN extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5448326576339838922L;
	private JPanel contentPane;
	private JTextField textField;

	/**
	 * Launch the application.
	 */
	public void inicio() {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					RegistrarVPN frame = new RegistrarVPN();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public RegistrarVPN() {
		setTitle("Registrar VPN");
		setBounds(100, 100, 450, 222);
		setIconImage(new ImageIcon("C:\\Users\\Usuario\\workspace\\TwitterBot\\src\\main\\java\\com\\selenium\\twitter\\Imagen\\iconotwitter.png").getImage());
		contentPane = new JPanel();
		contentPane.setBackground(new Color(255, 255, 255));
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		JLabel lblNombreDeVpn = new JLabel("Nombre de VPN");
		lblNombreDeVpn.setFont(new Font("Arial", Font.PLAIN, 12));
		
		textField = new JTextField();
		textField.setColumns(10);
		
		JButton btnRegistrar = new JButton("Registrar");
		btnRegistrar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Vpn vpn = new Vpn();
				vpn.setName(textField.getText().trim());
				try {
					vpn.insert();
					JOptionPane.showMessageDialog(null, "Registrado con exito");
					
				} catch (SQLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		});
		btnRegistrar.setFont(new Font("Arial", Font.PLAIN, 12));
		GroupLayout gl_contentPane = new GroupLayout(contentPane);
		gl_contentPane.setHorizontalGroup(
			gl_contentPane.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_contentPane.createSequentialGroup()
					.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_contentPane.createSequentialGroup()
							.addGap(39)
							.addComponent(lblNombreDeVpn)
							.addGap(34)
							.addComponent(textField, GroupLayout.PREFERRED_SIZE, 160, GroupLayout.PREFERRED_SIZE))
						.addGroup(gl_contentPane.createSequentialGroup()
							.addGap(153)
							.addComponent(btnRegistrar)))
					.addContainerGap(102, Short.MAX_VALUE))
		);
		gl_contentPane.setVerticalGroup(
			gl_contentPane.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_contentPane.createSequentialGroup()
					.addGap(42)
					.addGroup(gl_contentPane.createParallelGroup(Alignment.BASELINE)
						.addComponent(lblNombreDeVpn)
						.addComponent(textField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addPreferredGap(ComponentPlacement.RELATED, 61, Short.MAX_VALUE)
					.addComponent(btnRegistrar)
					.addGap(28))
		);
		contentPane.setLayout(gl_contentPane);
	}

}
