package com.selenium.facebook.Vista;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import com.selenium.facebook.Modelo.Task;
import com.selenium.facebook.Modelo.Task_Model;
import com.selenium.facebook.Modelo.Task_Model_Detail;

import javax.swing.GroupLayout;
import javax.swing.ImageIcon;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.LayoutStyle.ComponentPlacement;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.awt.event.ActionEvent;
import java.awt.GridLayout;
import javax.swing.JTextField;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

public class RegistrarTarea extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2971396123919213008L;
	private JPanel contentPane;
	private List<JComboBox<String>> list = new ArrayList<JComboBox<String>>();
	private JTextField textField;

	/**
	 * Launch the application.
	 */
	public void init() {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					RegistrarTarea frame = new RegistrarTarea();
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
	public RegistrarTarea() {
		setTitle("Crear Tarea");
		setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
		setIconImage(new ImageIcon("C:\\Users\\Usuario\\workspace\\TwitterBot\\src\\main\\java\\com\\selenium\\twitter\\Imagen\\iconotwitter.png").getImage());
		setBounds(100, 100, 393, 417);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		
		JScrollPane scrollPane = new JScrollPane();
		
		JButton btnAgregar = new JButton("Agregar");
		Task tarea = new Task();
		final List<String> listTask = tarea.getTasksActive(); 
		final JPanel panel = new JPanel();
		scrollPane.setViewportView(panel);
		panel.setLayout(new GridLayout(0, 1, 0, 0));
		
		btnAgregar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JComboBox<String> combo = new JComboBox<String>();
				JSeparator sepa = new JSeparator();
				sepa.setEnabled(false);
				for(String st : listTask) combo.addItem(st);
				list.add(combo);
				panel.add(combo);
				panel.add(sepa);
				panel.updateUI();
				
			}
		});
		
		JButton btnRegistrar = new JButton("Registrar");
		btnRegistrar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if(list.isEmpty()) {
					JOptionPane.showMessageDialog(null, "Tiene que agregar al menos 3 tareas","Failed",JOptionPane.ERROR_MESSAGE);
				}else if(list.size() < 3) {
					JOptionPane.showMessageDialog(null, "Tiene que agregar al menos 3 tareas","Failed",JOptionPane.ERROR_MESSAGE);
				}else if(textField.getText().equals("")) {
					JOptionPane.showMessageDialog(null, "Tiene que agregarle un nombre a la tarea","Failed",JOptionPane.ERROR_MESSAGE);
				}else {
					Task_Model taskModel = new Task_Model();
					taskModel.setName(textField.getText());
					try {
						taskModel.insert();
					} catch (SQLException e) {
						e.printStackTrace();
					}
					
					Task_Model_Detail taskModelDetail = new Task_Model_Detail();
					taskModelDetail.setTasks_model_id(taskModel.getLast());
					
					for(JComboBox<String> in : list) {
						taskModelDetail.setTasks_id(in.getSelectedIndex() +1 );
						try {
							taskModelDetail.insert();
						} catch (SQLException e) {
							e.printStackTrace();
						}
					}
					
					JOptionPane.showMessageDialog(null, "Se creo la tarea correctamente", "Exito", JOptionPane.INFORMATION_MESSAGE);
					panel.removeAll();
					panel.updateUI();
					textField.setText("");
				}
			}
		});
		
		textField = new JTextField();
		textField.setColumns(10);
		
		JLabel lblNombreDeTarea = new JLabel("Nombre de Tarea");
		GroupLayout gl_contentPane = new GroupLayout(contentPane);
		gl_contentPane.setHorizontalGroup(
			gl_contentPane.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_contentPane.createSequentialGroup()
					.addContainerGap()
					.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_contentPane.createSequentialGroup()
							.addComponent(scrollPane, GroupLayout.DEFAULT_SIZE, 347, Short.MAX_VALUE)
							.addContainerGap())
						.addGroup(Alignment.TRAILING, gl_contentPane.createSequentialGroup()
							.addComponent(btnRegistrar)
							.addGap(152))
						.addGroup(gl_contentPane.createSequentialGroup()
							.addComponent(lblNombreDeTarea)
							.addPreferredGap(ComponentPlacement.UNRELATED)
							.addComponent(textField, GroupLayout.PREFERRED_SIZE, 142, GroupLayout.PREFERRED_SIZE)
							.addPreferredGap(ComponentPlacement.RELATED, 30, Short.MAX_VALUE)
							.addComponent(btnAgregar)
							.addGap(21))))
		);
		gl_contentPane.setVerticalGroup(
			gl_contentPane.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_contentPane.createSequentialGroup()
					.addGap(4)
					.addGroup(gl_contentPane.createParallelGroup(Alignment.BASELINE)
						.addComponent(lblNombreDeTarea)
						.addComponent(textField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addComponent(btnAgregar))
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(scrollPane, GroupLayout.PREFERRED_SIZE, 227, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.RELATED, 60, Short.MAX_VALUE)
					.addComponent(btnRegistrar)
					.addGap(26))
		);
		
		
		contentPane.setLayout(gl_contentPane);
	}
}
