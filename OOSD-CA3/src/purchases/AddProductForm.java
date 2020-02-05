
package purchases;

import java.awt.BorderLayout;


//Insert contents to the Authors table .
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.Statement;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.Border;
import purchases.AddProductForm;




public class AddProductForm extends JFrame {
	private JTextField ProductNameField = new JTextField();
	private JTextField DescriptionField = new JTextField();
	private JTextField PriceField = new JTextField();
	private JButton submitButton = new JButton("Add Product");
	private JButton clearButton = new JButton("Clear Form");
	JPanel form = new JPanel();
	JPanel buttons = new JPanel();
	Border padding1;
	Border padding2;


		// Constructor 
		public AddProductForm(String title) {
			// Set title for frame and choose layout
			super(title);
			getContentPane().setLayout(new BorderLayout());
			form.setLayout(new GridLayout(8, 2));
			
			padding1 = BorderFactory.createEmptyBorder(10, 30, -40, 30);
			form.setBorder(padding1);
			
			// Add Product Name label and text field to frame
			form.add(new JLabel("   Product Name:"));
			form.add(ProductNameField);

			// Add Description label and text field to frame
			form.add(new JLabel("   Decription:"));
			form.add(DescriptionField);

			// Add Price label and text field to frame
			form.add(new JLabel("   Price "+"\u20ac"));
			form.add(PriceField);

			padding2 = BorderFactory.createEmptyBorder(0, 30, 20, 30);
			buttons.setBorder(padding2);
			buttons.setLayout(new GridLayout(1, 2));
			// Add submit button to frame
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
			
			String productName;
			String description;
			String price;
			
			final String DATABASE_URL = "jdbc:mysql://localhost/purchases";
			
			Connection connection = null;
			Statement statement = null;

			@Override
			public void actionPerformed(ActionEvent event) {
				productName = ProductNameField.getText();
				description = DescriptionField.getText();
				price = PriceField.getText();
				
				if (productName.isEmpty() || description.isEmpty() || price.isEmpty()) {
					JOptionPane.showMessageDialog(AddProductForm.this, String.format("One or more empty field(s), will not add to database", event.getActionCommand()));
				}
				else if (productName.matches("^[a-zA-Z-'.\\s]+") == false || description.matches("^[a-zA-Z-'.\\s]+") == false || price.matches("^[+0-9,\\s]+") == false) {
					JOptionPane.showMessageDialog(AddProductForm.this, String.format("One or more invalid input(s), please try again", event.getActionCommand()));
				}
				else {
				
				try {
					
					// establish connection to database
					connection = DriverManager.getConnection(DATABASE_URL, "root", "password");
						
					// create Statement for querying database
					statement = connection.createStatement();
					
					// Insert data into database
					// statement.executeUpdate("INSERT INTO Product (productName, description, price)" + "VALUES" + "('" + productName + "','" + description + "','" +price)");
					PreparedStatement pstat = connection.prepareStatement("INSERT INTO Product (productName, description, price) VALUES(?,?,?)");
					pstat.setString(1, productName);
					pstat.setString(2, description);
					pstat.setString(3, price);
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
				
				JOptionPane.showMessageDialog(AddProductForm.this, String.format("Added to Database", event.getActionCommand()));
				ProductNameField.setText("");
				DescriptionField.setText("");
				PriceField.setText("");
			}
			}
			
			
		}
		// clear button handler
			private class ClearButtonHandler implements ActionListener {

				@Override
				public void actionPerformed(ActionEvent event) {
					ProductNameField.setText("");
					DescriptionField.setText("");
					PriceField.setText("");
				}
				
			}
} // end class