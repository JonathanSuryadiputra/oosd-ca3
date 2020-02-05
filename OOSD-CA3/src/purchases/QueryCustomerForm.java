package purchases;

import java.sql.Connection;

import java.sql.Statement;
import java.util.Vector;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.JScrollPane;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import javax.swing.border.Border;
import javax.swing.table.DefaultTableModel;

public class QueryCustomerForm {

	private JPanel queryCustomerFormPanel = new JPanel();

	private final String JDBC_DRIVER = "com.mysql.jdbc.Driver";
	private final String DATABASE_URL = "jdbc:mysql://localhost/purchases";
	private final String UserName_SQL = "root";
	private final String Password_SQL = "password";

	private JPanel topPanel = new JPanel();
	private JPanel panel = new JPanel();
	private JPanel bottomPanel = new JPanel();
	private JPanel jtablePanel = new JPanel();

	private JTable jtable;
	private DefaultTableModel model;
   
	// constructor
	public QueryCustomerForm() {

		getQuery();

		JLabel topicLabel = new JLabel("           Query Customer Table           ");
		JButton addButton = new JButton("Create");
		JButton updateButton = new JButton("Update");
		JButton deleteButton = new JButton("Delete");
		JButton RecentPurchasesButton = new JButton("Recent Purchases");

		addButtonHandler addHandler = new addButtonHandler();
		addButton.addActionListener(addHandler);

		updateButtonHandler updateHandler = new updateButtonHandler();
		updateButton.addActionListener(updateHandler);

		deleteButtonHandler deleteHandler = new deleteButtonHandler();
		deleteButton.addActionListener(deleteHandler);

		RecentPurchasesHandler RecentPurchaseHandler = new RecentPurchasesHandler();
		RecentPurchasesButton.addActionListener(RecentPurchaseHandler);

		/* add components */
		topPanel.add(topicLabel);
		panel.add(jtablePanel);
		bottomPanel.add(addButton);
		bottomPanel.add(new JLabel("                   "));
		bottomPanel.add(updateButton);
		bottomPanel.add(new JLabel("                   "));
		bottomPanel.add(deleteButton);
		bottomPanel.add(new JLabel("                   "));
		bottomPanel.add(RecentPurchasesButton);
		queryCustomerFormPanel.add(topPanel);
		queryCustomerFormPanel.add(panel);
		queryCustomerFormPanel.add(bottomPanel);

		/* set Style */
		Font topicFont = new Font("Serif", Font.BOLD, 25);
		Font buttonFont = new Font("Serif", Font.BOLD, 15);
		Border formBorder = BorderFactory.createLineBorder(Color.LIGHT_GRAY, 3);
		Dimension buttonSize = new Dimension(200, 50);

		topicLabel.setFont(topicFont);

		addButton.setFont(buttonFont);
		updateButton.setFont(buttonFont);
		deleteButton.setFont(buttonFont);
		RecentPurchasesButton.setFont(buttonFont);

		addButton.setBackground(Color.lightGray);
		updateButton.setBackground(Color.lightGray);
		deleteButton.setBackground(Color.lightGray);
		RecentPurchasesButton.setBackground(Color.lightGray);

		addButton.setPreferredSize(buttonSize);
		updateButton.setPreferredSize(buttonSize);
		deleteButton.setPreferredSize(buttonSize);
		RecentPurchasesButton.setPreferredSize(buttonSize);

		jtablePanel.setPreferredSize(new Dimension(1260, 500));
		jtablePanel.setBorder(formBorder);

		topPanel.setLayout(new FlowLayout(1, 0, 30));
		topPanel.setPreferredSize(new Dimension(1260, 100));
		topPanel.setOpaque(false);

		panel.setPreferredSize(new Dimension(1280, 520));
		panel.setOpaque(false);

		bottomPanel.setLayout(new FlowLayout(1, 0, 10));
		bottomPanel.setPreferredSize(new Dimension(1260, 80));
		bottomPanel.setOpaque(false);

		queryCustomerFormPanel.setBackground(Color.WHITE);
	}

	public JPanel getJPanel() {
		return queryCustomerFormPanel;
	}

	public void getQuery() {

		String[] columnNames = { "Customer ID", "First Name", "LastName", "Address", "Phone Number" };

		Connection connection = null;
		Statement statement = null;

		try {
			connection = DriverManager.getConnection(DATABASE_URL, UserName_SQL, Password_SQL);
			statement = connection.createStatement();
			ResultSet resultSet = statement
					.executeQuery("SELECT customerID, firstName, lastName, address, phoneNumber FROM Customer");

			ResultSetMetaData metaData = resultSet.getMetaData();

			int numberOfColumns = metaData.getColumnCount();
			Object[][] data = new Object[50][numberOfColumns];

			int j = 0, k = 0;
			while (resultSet.next()) {
				for (int i = 1; i <= numberOfColumns; i++) {
					data[j][k] = resultSet.getObject(i);
					k++;
				}
				k = 0;
				j++;
			} // end while

			model = new DefaultTableModel(data, columnNames);

			/* create non-editable JTable */
			jtable = new JTable(model) {
				public boolean isCellEditable(int row, int column) {
					return false;
				}
			};
			;
			jtable.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
			jtable.setPreferredScrollableViewportSize(new Dimension(1230, 450));
			JScrollPane scrollPane = new JScrollPane(jtable);

			jtablePanel.add(scrollPane);
			jtable.getColumnModel().getColumn(0).setPreferredWidth(150);
			jtable.getColumnModel().getColumn(1).setPreferredWidth(250);
			jtable.getColumnModel().getColumn(2).setPreferredWidth(250);
			jtable.getColumnModel().getColumn(3).setPreferredWidth(400);
			jtable.getColumnModel().getColumn(4).setPreferredWidth(400);
			jtable.setAutoResizeMode(jtable.AUTO_RESIZE_LAST_COLUMN);
			jtable.setBackground(Color.white);
			jtable.setForeground(Color.black);
			jtable.setRowHeight(30);
			jtable.setFont(new Font("Serif", Font.PLAIN, 15));

		} // end try
		catch (SQLException sqlException) {
			sqlException.printStackTrace();
			System.exit(1);
		} // end catch
		finally // ensure statement and connection are closed properly
		{
			try {
				statement.close();
				connection.close();
			} // end try
			catch (Exception exception) {
				exception.printStackTrace();
				System.exit(1);
			} // end catch
		} // end finally
	}// end getQuery

	/*-----------------------------------------------------------add Button----------------------------------------------------------*/
	private class addButtonHandler implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			AddCustomerForm customerForm = new AddCustomerForm("Customer Form");
			customerForm.setSize(275, 350);
			customerForm.setLocation(400, 300);
			customerForm.setLocationRelativeTo(null);
			customerForm.setVisible(true);
		}// end actionPerformed
	}// end addActionListener

	/*-----------------------------------------------------------delete Button----------------------------------------------------------*/
	private class deleteButtonHandler implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			if (jtable.getSelectedRow() != -1) {

				Connection connection = null;
				Statement statement = null;

				int selectedRowIndex = jtable.getSelectedRow();
				String deleteId = model.getValueAt(selectedRowIndex, 0).toString();

				try {
					connection = DriverManager.getConnection(DATABASE_URL, UserName_SQL, Password_SQL);
					statement = connection.createStatement();
					statement.executeUpdate("DELETE FROM Customer WHERE customerId = '" + deleteId + "' ");

				} // end try

				catch (SQLException sqlException) {
					sqlException.printStackTrace();
				} // end catch

				finally {
					try {
						statement.close();
						connection.close();
					} // end try

					catch (Exception exception) {
						exception.printStackTrace();
					} // end catch
				} // end finally
				model.removeRow(jtable.getSelectedRow());
				JOptionPane.showMessageDialog(null, "Deleted successfully");
			} // end if
			else {
				JOptionPane.showMessageDialog(null, "Please select a record");
			}
		}//end actionPerformed
	}//end addActionListener
   
   /* -----------------------------------------------------------update Button----------------------------------------------------------*/
   private class updateButtonHandler implements ActionListener {
	 //JFrame class
	   class UpdateCustomerForm extends JFrame {
			
			static final String DATABASE_URL = "jdbc:mysql://localhost/purchases";
			static final String UserName_SQL = "root";
			static final String Password_SQL = "password";
			
			JTextField firstNameField;
			JTextField lastNameField;
			JTextField addressField;
			JTextField phoneNumField;
			JButton save;
			JPanel form;
			JPanel button;
			Border padding1;
			Border padding2;
			
			int selectedRow = jtable.getSelectedRow();
			String customerId = model.getValueAt(selectedRow,0).toString();
			String firstName = model.getValueAt(selectedRow,1).toString();
			String lastName = model.getValueAt(selectedRow,2).toString();
			String address = model.getValueAt(selectedRow,3).toString();
			String phoneNum = model.getValueAt(selectedRow,4).toString();
				
			
			//Constructor
			public UpdateCustomerForm() {
				super("Update Customer");
				
				getContentPane().setLayout(new BorderLayout());
				
				form = new JPanel();
				padding1 = BorderFactory.createEmptyBorder(10, 30, 10, 30);
				form.setBorder(padding1);
				
				form.setLayout(new GridLayout(8, 1));
				
				form.add(new JLabel("First Name"));
				firstNameField = new JTextField();
				firstNameField.setText(firstName);
				form.add(firstNameField);
				
				form.add(new JLabel("Last Name"));
				lastNameField = new JTextField();
				lastNameField.setText(lastName);
				form.add(lastNameField);
				
				form.add(new JLabel("Address"));
				addressField = new JTextField();
				addressField.setText(address);
				form.add(addressField);
				
				form.add(new JLabel("Phone Number"));
				phoneNumField = new JTextField();
				phoneNumField.setText(phoneNum);
				form.add(phoneNumField);
				
				button = new JPanel();
				button.setLayout(new GridLayout(1, 1));
				save = new JButton("Save");
				save.addActionListener(new ActionListener() {
					String newFirstName;
					String newLastName;
					String newAddress;
					String newPhoneNum;
					
					Connection connection = null;
					Statement statement = null;

					@Override
					public void actionPerformed(ActionEvent event) {
						// TODO Auto-generated method stub
						newFirstName = firstNameField.getText();
						newLastName = lastNameField.getText();
						newAddress = addressField.getText();
						newPhoneNum = phoneNumField.getText();
						
						if (newFirstName.equalsIgnoreCase(firstName) && newLastName.equalsIgnoreCase(lastName) && newAddress.equalsIgnoreCase(address) && newPhoneNum.equalsIgnoreCase(phoneNum)) {
							JOptionPane.showMessageDialog(UpdateCustomerForm.this, String.format("No changes were made", event.getActionCommand()));
						}//end if
						
						else {
							try {
								// establish connection to database
								connection = DriverManager.getConnection(DATABASE_URL, UserName_SQL, Password_SQL);
									
								// create Statement for updating database
								statement = connection.createStatement();
								statement.executeUpdate("UPDATE Customer SET firstName = '" + newFirstName + "', lastName = '" + newLastName + "', address = '" + newAddress + "', phoneNumber = '" + newPhoneNum + "' WHERE customerId = '" + customerId + "' ");
							}
									
							catch(SQLException sqlException ) {
								sqlException . printStackTrace () ;
							}
									
							finally {
								try {
									statement. close () ;
									connection. close () ;
								}
											
								catch ( Exception exception ) {
										exception . printStackTrace () ;
								}
							}
							JOptionPane.showMessageDialog(UpdateCustomerForm.this, String.format("Record Updated", event.getActionCommand()));
						}//end else
					}
				});
				padding2 = BorderFactory.createEmptyBorder(10, 50, 25, 50);
				button.setBorder(padding2);
				button.add(save);
				
				add(form, BorderLayout.CENTER);
				add(button, BorderLayout.SOUTH);
				
			} //end constructor

		} //end update customer form class
	   
		public void actionPerformed(ActionEvent e) {
			if(jtable.getSelectedRow() != -1) {
				UpdateCustomerForm customerForm = new UpdateCustomerForm();
				customerForm.setSize(300, 400);
				customerForm.setLocation(400, 300);
				customerForm.setLocationRelativeTo(null);
				customerForm.setVisible(true);
			}
			else {
				JOptionPane.showMessageDialog(null,"Please select a record");
			}	
		}// end actionPerformed
		
	}// end update button handler
   
   /* -----------------------------------------------------------Recent purchases button----------------------------------------------------------*/

	private class RecentPurchasesHandler implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			if (jtable.getSelectedRow() != -1) {

				Connection connection = null;
				Statement statement = null;

				int selectedRowIndex = jtable.getSelectedRow();
				String RenctlyPurchasesId = model.getValueAt(selectedRowIndex, 0).toString();

				try {
					connection = DriverManager.getConnection(DATABASE_URL, UserName_SQL, Password_SQL);
					statement = connection.createStatement();
					statement.executeUpdate("select firstName, lastName, productName, qtyProduct from customer inner join invoice on customer.customerId = invoice.customerId inner join product on invoice.productId = product.productId;");

				} // end try

				catch (SQLException sqlException) {
					sqlException.printStackTrace();
				} // end catch

				finally {
					try {
						statement.close();
						connection.close();
					} // end try

					catch (Exception exception) {
						exception.printStackTrace();
					} // end catch
				} // end finally
				JOptionPane.showMessageDialog(null, "True");
			}
		}// end actionPerformed
	}// end addActionListener

}// end class
