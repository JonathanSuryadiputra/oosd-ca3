package purchases;

import java.awt.BorderLayout;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.Border;

public class AddInvoiceForm extends JFrame {
	
	// initialize variables
	JTextField customerIdField = new JTextField();
	JTextField productIdField = new JTextField();
	JTextField qtyProductField = new JTextField();
	JTextField invoiceDateField = new JTextField();
	JTextField invoiceTimeField = new JTextField();

	SimpleDateFormat formatDate = new SimpleDateFormat("yyyy-MM-dd");
	SimpleDateFormat formatTime = new SimpleDateFormat("HH:mm:ss");
	Date d = new Date();

	JButton submitButton = new JButton("Add Invoice");
	JButton clearButton = new JButton("Clear Form");
	JPanel form = new JPanel();
	JPanel buttons = new JPanel();
	Border padding1;
	Border padding2;

	// Constructor
	public AddInvoiceForm(String title) {
		// Set title for frame and choose layout
		super(title);

		getContentPane().setLayout(new BorderLayout());

		form.setLayout(new GridLayout(8, 1));

		padding1 = BorderFactory.createEmptyBorder(10, 30, -20, 30);
		form.setBorder(padding1);

		// Add Customer Id label and text field to frame
		form.add(new JLabel("Customer Id"));
		form.add(customerIdField);

		// Add Product Id label and text field to frame
		form.add(new JLabel("Product Id"));
		form.add(productIdField);

		// Add quantity Product label and text field to frame
		form.add(new JLabel("qty Product"));
		form.add(qtyProductField);

		// Button padding
		padding2 = BorderFactory.createEmptyBorder(0, 30, 40, 30);
		buttons.setBorder(padding2);
		buttons.setLayout(new GridLayout(1, 2));
		// Add submit button to frame
		buttons.add(submitButton);

		// Submit button
		SubmitButtonHandler submitHandler = new SubmitButtonHandler();
		submitButton.addActionListener(submitHandler);

		// Add clear button to frame
		buttons.add(clearButton);
		
		// Clear Button
		ClearButtonHandler clearHandler = new ClearButtonHandler();
		clearButton.addActionListener(clearHandler);

		add(form, BorderLayout.CENTER);
		add(buttons, BorderLayout.SOUTH);

	}// end constructor

	// TextField handler
	private class SubmitButtonHandler implements ActionListener {

		// Initialize variable
		String customerId;
		String productId;
		String qtyProduct;
		String InvoiceDate;
		String InvoiceTime;

		// initialize the databases link
		final String DATABASE_URL = "jdbc:mysql://localhost/purchases";

		Connection connection = null;
		Statement statement = null;

		@Override
		public void actionPerformed(ActionEvent event) {
			customerId = customerIdField.getText();
			productId = productIdField.getText();
			qtyProduct = qtyProductField.getText();
			InvoiceDate =formatDate.format(d);
			InvoiceTime =  formatTime.format(d);

			try {

				// establish connection to database
				connection = DriverManager.getConnection(DATABASE_URL, "root", "password");

				// create Statement for querying database
				statement = connection.createStatement();

				// Insert data into database
				PreparedStatement pstat = connection.prepareStatement(
						"INSERT INTO invoice (customerId, productId, qtyProduct, invoiceDate, invoiceTime)VALUES(?,?,?,?,?)");
				pstat.setString(1, customerId);
				pstat.setString(2, productId);
				pstat.setString(3, qtyProduct);
				pstat.setString(4, formatDate.format(d));
				pstat.setString(5, formatTime.format(d));
				pstat.executeUpdate();

			}

			catch (SQLException sqlException) {
				sqlException.printStackTrace();
			}

			finally {
				try {
					statement.close();
					connection.close();
				}

				catch (Exception exception) {
					exception.printStackTrace();
				}
			}

					
			// Add data and reset textField
			JOptionPane.showMessageDialog(AddInvoiceForm.this,
					String.format("Added to Database", event.getActionCommand()));
			customerIdField.setText("");
			productIdField.setText("");
			qtyProductField.setText("");
		}

	}

	// clear button handler
	private class ClearButtonHandler implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent event) {
			customerIdField.setText("");
			productIdField.setText("");
			qtyProductField.setText("");
		} //

	} // end of Clear Button 

}// end class
