package purchases;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.Border;

public class AddInvoiceForm extends JFrame {
	
	static final String DATABASE_URL = "jdbc:mysql://localhost/purchases";
	static final String UserName_SQL = "root";
	static final String Password_SQL = "password";
	
	JComboBox customerList;
	JComboBox productList;
	JTextField qtyProductField = new JTextField();
	JButton submitButton = new JButton("Add Record");
	JButton clearButton = new JButton("Clear Form");
	JPanel form = new JPanel();
	JPanel button = new JPanel();
	Border padding1;
	Border padding2;
	
	//get customer list for Customer JComboBox
	public JComboBox getCustomerList() {
		JComboBox customerSelectBox = new JComboBox();
		Connection connection = null;
		Statement statement = null;
		
		try {
			connection = DriverManager.getConnection(DATABASE_URL, UserName_SQL, Password_SQL);
			statement = connection.createStatement();
			ResultSet customerResultSet = statement.executeQuery("select customerId, firstName, lastName from customer;");
			ResultSetMetaData customerMetaData = customerResultSet.getMetaData();
			int numberOfColumns = customerMetaData.getColumnCount();
			while(customerResultSet.next() ) {
				for ( int i = 1; i <= numberOfColumns; i=i+3 ) {
					customerSelectBox.addItem("ID (" + customerResultSet.getObject(i) + ")   " + customerResultSet.getObject(i+1) + " " + customerResultSet.getObject(i+2));
				}
			}//end while
		}//end try
			
		catch(SQLException sqlException ) {
			sqlException . printStackTrace ();
		}//end catch
			
		finally {
			try{
				statement. close ();	
				connection. close ();
			}//end try
				
			catch ( Exception exception ){	
				exception . printStackTrace ();
			}//end catch
		}//end finally
		
		return customerSelectBox;
	}
	
	//get Product list for Product JComboBox
	public JComboBox getProductList() {
		JComboBox productSelectBox = new JComboBox();
		
		Connection connection = null;
		Statement statement = null;
		   
		   
		try {
			connection = DriverManager.getConnection(DATABASE_URL, UserName_SQL, Password_SQL);	
			statement = connection.createStatement();	
			ResultSet productResultSet = statement.executeQuery("select productId, productName, price from product;");	
			ResultSetMetaData productMetaData = productResultSet.getMetaData();	
			int numberOfColumns = productMetaData.getColumnCount();
			while(productResultSet.next() ) {	
				for ( int i = 1; i <= numberOfColumns; i=i+3 )	
					productSelectBox.addItem("ID (" + productResultSet.getObject(i) + ")   " + productResultSet.getObject(i+1) + " - \u20ac" + productResultSet.getObject(i+2));
			}//end while
		}//end try
				
			
		catch(SQLException sqlException ) {	
			sqlException . printStackTrace ();
		}//end catch
				
			
		finally {
			try{		
				statement. close ();
				connection. close ();
			}//end try
	
			catch ( Exception exception ){	
				exception . printStackTrace ();
			}//end catch
		}//end finally
		
		return productSelectBox;
	}
	
	//Constructor
	public AddInvoiceForm(String title) {
		super(title);
		
		getContentPane().setLayout(new BorderLayout());
		
		form.setLayout(new GridLayout(6, 1));
		padding1 = BorderFactory.createEmptyBorder(10, 30, 10, 30); //padding for main form panel
		form.setBorder(padding1);
		
		form.add(new JLabel("Customer ID"));
		customerList = getCustomerList();
		form.add(customerList); 
		form.add(new JLabel("Product ID"));
		productList = getProductList();
		form.add(productList); 
		form.add(new JLabel("Quantity"));  
		form.add(qtyProductField);
		
		button.setLayout(new GridLayout(1, 2));
		padding2 = BorderFactory.createEmptyBorder(0, 10, 20, 10); //padding for button panel
		button.setBorder(padding2);
		
		SubmitButtonHandler submitHandler = new SubmitButtonHandler();
		submitButton.addActionListener(submitHandler);
		button.add(submitButton);
		
		ClearButtonHandler clearHandler = new ClearButtonHandler();
		clearButton.addActionListener(clearHandler);
		button.add(clearButton);
		
		add(form, BorderLayout.CENTER);
		add(button, BorderLayout.SOUTH);
	}
	
	//TextField handler
	public class SubmitButtonHandler implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			//get customer value
			String customerIdstr = customerList.getSelectedItem().toString();
			customerIdstr = customerIdstr.substring(customerIdstr.indexOf("(")+1,customerIdstr.indexOf(")"));
			int customerId = Integer.parseInt(customerIdstr);
			
			//get product value
			String productIdstr = productList.getSelectedItem().toString();
			productIdstr = productIdstr.substring(productIdstr.indexOf("(")+1,productIdstr.indexOf(")"));
			int productId = Integer.parseInt(productIdstr);
			
			//get quantity value
			int qtyProduct = Integer.parseInt(qtyProductField.getText());
			
			if (qtyProduct < 1 || qtyProduct > 99) {
				JOptionPane.showMessageDialog(null,"Invalid Quantity input");
			}
		 	
			else {
				
				SimpleDateFormat formatDate = new SimpleDateFormat("yyyy-MM-dd");
				SimpleDateFormat formatTime = new SimpleDateFormat("HH:mm:ss");
				Date d = new Date();
				
				Connection connection = null;
				Statement statement = null;
					    
				try {
					connection = DriverManager.getConnection(DATABASE_URL, UserName_SQL, Password_SQL);
					statement = connection.createStatement();
					statement.executeUpdate("INSERT INTO invoice (customerId, productId, qtyProduct, invoiceDate, invoiceTime)" + " VALUES " + "(" + customerId + "," + productId + "," + qtyProduct + ",'" + formatDate.format(d) + "','" + formatTime.format(d) + "')");
				}
				
				catch(SQLException sqlException) {
					sqlException.printStackTrace();
				}
							
						
				finally {
					try {		
						statement.close();		
						connection.close();
					}
					
					catch ( Exception exception ) {
						exception.printStackTrace();
					}
				}
				
				JOptionPane.showMessageDialog(null,"Created New Record");
			}//end else
		}
	}
	
	//Clear button handler
	public class ClearButtonHandler implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			customerList.setSelectedIndex(-1);
			productList.setSelectedIndex(-1);
			qtyProductField.setText("");
		}
	}
}