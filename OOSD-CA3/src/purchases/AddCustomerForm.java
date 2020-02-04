package purchases;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.Border;

public class AddCustomerForm extends JFrame {
	
	JTextField firstNameField = new JTextField();
	JTextField lastNameField = new JTextField();
	JTextField addressField = new JTextField();
	JTextField phoneNumField = new JTextField();
	JButton submitButton = new JButton("Add Customer");
	JButton clearButton = new JButton("Clear Form");
	JPanel form = new JPanel();
	JPanel buttons = new JPanel();
	Border padding1;
	Border padding2;
	
	//Constructor
	public AddCustomerForm(String title) {
		//Set title for frame and choose layout
		super(title);
		
		getContentPane().setLayout(new BorderLayout());
		
		form.setLayout(new GridLayout(10, 1));

		padding1 = BorderFactory.createEmptyBorder(10, 30, -50, 30);
		form.setBorder(padding1);
		
		//Add first name label and text field to frame
		form.add(new JLabel("First Name: "));
		form.add(firstNameField);
		
		//Add last name label and text field to frame
		form.add(new JLabel("Last Name: "));
		form.add(lastNameField);
		
		//Add address label and text field to frame
		form.add(new JLabel("Address:"));
		form.add(addressField);
		
		//Add phone number label and text field to frame
		form.add(new JLabel("Phone Number:"));
		form.add(phoneNumField);
		
		//Add submit button to frame
		
		padding2 = BorderFactory.createEmptyBorder(0, 30, 20, 30);
		buttons.setBorder(padding2);
		buttons.add(submitButton);
		
		SubmitButtonHandler submitHandler = new SubmitButtonHandler();
		submitButton.addActionListener(submitHandler);
		
		//Add clear button to frame
		buttons.add(clearButton);
		
		ClearButtonHandler clearHandler = new ClearButtonHandler();
		clearButton.addActionListener(clearHandler);
		
		add(form, BorderLayout.CENTER);
		add(buttons, BorderLayout.SOUTH);
		
	}// end constructor
	
	// TextField handler
	private class SubmitButtonHandler implements ActionListener {
		
		String firstName;
		String lastName;
		String address;
		String phoneNum;
		
		final static String DATABASE_URL = "jdbc:mysql://localhost/purchases";
		final static String USERNAME = "root";
		final static String PASSWORD = "password";
		
		Connection connection = null;
		Statement statement = null;

		@Override
		public void actionPerformed(ActionEvent event) {
			firstName = firstNameField.getText();
			lastName = lastNameField.getText();
			address = addressField.getText();
			phoneNum = phoneNumField.getText();
			
			if (firstName.isEmpty() || lastName.isEmpty() || address.isEmpty() || phoneNum.isEmpty()) {
				JOptionPane.showMessageDialog(AddCustomerForm.this, String.format("One or more empty field(s), will not add to database", event.getActionCommand()));
			}
			else if (firstName.matches("^[a-zA-Z-'.\\s]+") == false || lastName.matches("^[a-zA-Z-'.\\s]+") == false || phoneNum.matches("^[+0-9\\s]+") == false) {
				JOptionPane.showMessageDialog(AddCustomerForm.this, String.format("One or more invalid input(s), please try again", event.getActionCommand()));
			}
			else {
				try {
					
					// establish connection to database
					connection = DriverManager.getConnection(DATABASE_URL, USERNAME, PASSWORD);
						
					// create Statement for querying database
					statement = connection.createStatement();
					
					// Insert data into database
					PreparedStatement pstat = connection.prepareStatement("INSERT INTO Customer (firstName, lastName, address, phoneNumber) VALUES(?,?,?,?)");
					pstat.setString(1, firstName);
					pstat.setString(2, lastName);
					pstat.setString(3, address);
					pstat.setString(4, phoneNum);
					pstat.executeUpdate();
						
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
				
				JOptionPane.showMessageDialog(AddCustomerForm.this, String.format("Added to Database", event.getActionCommand()));
				firstNameField.setText("");
				lastNameField.setText("");
				addressField.setText("");
				phoneNumField.setText("");
			}
			
		}
		
	}
	
	// clear button handler
		private class ClearButtonHandler implements ActionListener {

			@Override
			public void actionPerformed(ActionEvent event) {
				firstNameField.setText("");
				lastNameField.setText("");
				addressField.setText("");
				phoneNumField.setText("");
			}
			
		}

}// end class
