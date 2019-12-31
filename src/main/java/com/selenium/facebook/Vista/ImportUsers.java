package com.selenium.facebook.Vista;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.selenium.facebook.Modelo.User;
import com.selenium.facebook.Modelo.Vpn;

import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JTextField;
import javax.swing.JButton;
import javax.swing.LayoutStyle.ComponentPlacement;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileInputStream;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.awt.event.ActionEvent;
import javax.swing.JLabel;

public class ImportUsers extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6539691741739065805L;
	private JPanel contentPane;
	private JTextField textField;

	/**
	 * Launch the application.
	 */
	public void inicio() {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					ImportUsers frame = new ImportUsers();
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
	public ImportUsers() {
		setTitle("Importacion masiva de usuarios");
		setResizable(false);
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		
		textField = new JTextField();
		textField.setColumns(10);
		
		JButton btnNewButton = new JButton("Ingresar");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				File file = new File(textField.getText());
				if(file.exists()) {
					LeerArchivosDeExcel(file);
				}else {
					System.out.println("El archivo no existe");
				}
				
				
			}
		});
		
		JLabel lblDireccinDelArchivo = new JLabel("Direcci\u00F3n del archivo");
		GroupLayout gl_contentPane = new GroupLayout(contentPane);
		gl_contentPane.setHorizontalGroup(
			gl_contentPane.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_contentPane.createSequentialGroup()
					.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_contentPane.createSequentialGroup()
							.addGap(50)
							.addComponent(textField, GroupLayout.PREFERRED_SIZE, 296, GroupLayout.PREFERRED_SIZE))
						.addGroup(gl_contentPane.createSequentialGroup()
							.addGap(139)
							.addComponent(btnNewButton))
						.addGroup(gl_contentPane.createSequentialGroup()
							.addGap(146)
							.addComponent(lblDireccinDelArchivo)))
					.addContainerGap(78, Short.MAX_VALUE))
		);
		gl_contentPane.setVerticalGroup(
			gl_contentPane.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_contentPane.createSequentialGroup()
					.addGap(33)
					.addComponent(lblDireccinDelArchivo)
					.addGap(18)
					.addComponent(textField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.RELATED, 73, Short.MAX_VALUE)
					.addComponent(btnNewButton)
					.addGap(71))
		);
		contentPane.setLayout(gl_contentPane);
	}
	
	public void LeerArchivosDeExcel(File fileName) {
		List<List<XSSFCell>> cellData = new ArrayList<List<XSSFCell>>();
		
		try {
			FileInputStream fis = new FileInputStream(fileName);
			@SuppressWarnings("resource")
			XSSFWorkbook woorkBook = new XSSFWorkbook(fis);
			
			XSSFSheet sheet = woorkBook.getSheetAt(0);
			
			Iterator<?> rowIterator = sheet.rowIterator();
			
			while(rowIterator.hasNext()) {
				XSSFRow row = (XSSFRow) rowIterator.next();
				
				Iterator<?> iterator = row.cellIterator();
				
				List<XSSFCell> cellTemporal = new ArrayList<XSSFCell>();
				
				while(iterator.hasNext()) {
					XSSFCell cell = (XSSFCell) iterator.next();
					
					cellTemporal.add(cell);
				}
				
				cellData.add(cellTemporal);
			}
			
			for(int i = 0; i< cellData.size(); i++) {
				System.out.println("*************************************************");
				List<?> celltemporalList = (List<?>) cellData.get(i);
				//C:\Users\Usuario\Downloads\facebook nuevos.xlsx
				User user = new User();
				for(int j = 0;j < celltemporalList.size(); j++) {
					
					XSSFCell cell = (XSSFCell) celltemporalList.get(j);
					
					String stringcellValue = (String) cell.toString();
					if(j==0) {
						String dateOfBirth = "";
						int anio = 0;
						int mes = 0;
						int dia = 0;
						anio = Integer.parseInt(stringcellValue.substring(7, 11));
						switch (stringcellValue.substring(3,6)) {
							case "ene":
								mes = 1;
								break;
							case "feb":
								mes = 2;
								break;
							case "mar":
								mes = 3;
								break;
							case "abr":
								mes = 4;
								break;
							case "may":
								mes = 5;
								break;
							case "jun":
								mes = 6;
								break;
							case "jul":
								mes = 7;
								break;
							case "ago":
								mes = 8;
								break;
							case "sep":
								mes = 9;
								break;
							case "oct":
								mes = 10;
							case "nov":
								mes = 11;
							case "dic":
								mes = 12;
							}
						dia = Integer.parseInt(stringcellValue.substring(0,2));
						if(mes < 10) {
							if(dia < 10) {
								dateOfBirth = anio + "-0"+mes+"-0"+dia;
								
							}else {
								dateOfBirth =  anio + "-0"+mes+"-"+dia;
							}
								
							
						}else {
							if(dia < 10) {
								dateOfBirth = anio + "-"+mes+"-0"+dia;
							}else {
								dateOfBirth = anio + "-"+mes+"-"+dia;
							}
						}
						//Setear la fecha de nacimiento
						user.setDate_of_birth(dateOfBirth);
						System.out.println(user.getDate_of_birth());
					}else if (j==1) {
						//setear el full name
						user.setFull_name(stringcellValue);
						System.out.println(user.getFull_name());
					}else if (j==2) {
						long value = (long) cell.getNumericCellValue();
						BigInteger bigin = new BigInteger(String.valueOf(value));
						user.setPhone(bigin);
				
						System.out.println(user.getPhone());
					}else if(j==3) {
						double numDouble = 0;
						try {
							if(!stringcellValue.equals("")) {
								numDouble = Double.parseDouble(stringcellValue);
								
							}
//							System.out.println(numDouble);
							user.setSim_card_number((int)numDouble);
						}catch (NumberFormatException e) {
							System.out.println("Error: "+stringcellValue);
						}
						
						System.out.println(user.getSim_card_number());
					}else if(j==4) {
						user.setEmail(stringcellValue);
						System.out.println(user.getEmail());
					}else if(j==5) {
						user.setPassword(stringcellValue);
						System.out.println(user.getPassword());
					}else if(j==6) {
						Vpn vpn = new Vpn();
						int vpnId = vpn.findOrCreate(stringcellValue);
						if(vpnId == 0) {
							vpnId = vpn.getFind(stringcellValue);
						}
						user.setVpn_id(vpnId);
						System.out.println(user.getVpn_id());
					}else if(j== 7) {
						user.setUsername(stringcellValue);
						System.out.println(user.getUsername());
						user.setCategories_id(2);
						user.insert();
					}else if(j > 7) {
						
						break;
					}else {
						System.out.println(stringcellValue);
					}
					
					
					
				}
			}
			
		} catch (Exception e) {
			System.err.println("error:" +e);
		}
	}
}