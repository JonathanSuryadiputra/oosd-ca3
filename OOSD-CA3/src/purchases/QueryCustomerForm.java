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
import javax.swing.RowFilter;
import javax.swing.JScrollPane;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import javax.swing.border.Border;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;

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
	private JPanel searchPanel = new JPanel();
	final JTextField searchBarField;

	private JTable jtable;
	private DefaultTableModel model;

	// constructor
	public QueryCustomerForm() {

		getQuery();

		JLabel topicLabel = new JLabel("           Query Customer Table           ");
		JButton addButton = new JButton("Create");
		JButton updateButton = new JButton("Update");
		JButton deleteButton = new JButton("Delete");

		addButtonHandler addHandler = new addButtonHandler();
		addButton.addActionListener(addHandler);

		updateButtonHandler updateHandler = new updateButtonHandler();
		updateButton.addActionListener(updateHandler);

		deleteButtonHandler deleteHandler = new deleteButtonHandler();
		deleteButton.addActionListener(deleteHandler);

		// set up table search bar and sorter
		final TableRowSorter<TableModel> sorter = new TableRowSorter<TableModel>(model);
		jtable.setRowSorter(sorter);

		searchPanel.setLayout(new FlowLayout());
		searchPanel.setBackground(Color.WHITE);

		searchBarField = new JTextField(
				"Enter query here (Click on the field to clear it, then press Enter to clear query)");
		searchBarField.setHorizontalAlignment(JTextField.CENTER);
		searchBarField.setColumns(80);

		searchBarField.addKeyListener(new KeyListener() {
			String query;

			@Override
			public void keyTyped(KeyEvent e) {
				// TODO Auto-generated method stub

			}

			@Override
			public void keyPressed(KeyEvent event) {
				if (event.getKeyCode() == KeyEvent.VK_ENTER) {
					query = searchBarField.getText();
					if (query.length() == 0) {
						sorter.setRowFilter(null);
					} else {
						sorter.setRowFilter(RowFilter.regexFilter(query));
					}
				}
			}

			@Override
			public void keyReleased(KeyEvent e) {
				// TODO Auto-generated method stub

			}

		});

		ClearFieldHandler clickClear = new ClearFieldHandler();
		searchBarField.addMouseListener(clickClear);
		searchPanel.add(searchBarField);

		/* add components */
		topPanel.add(topicLabel);
		panel.add(jtablePanel);
		bottomPanel.add(addButton);
		bottomPanel.add(new JLabel("                   "));
		bottomPanel.add(updateButton);
		bottomPanel.add(new JLabel("                   "));
		bottomPanel.add(deleteButton);
		bottomPanel.add(new JLabel("                   "));
		queryCustomerFormPanel.add(topPanel);
		queryCustomerFormPanel.add(searchPanel);
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


		addButton.setBackground(Color.lightGray);
		updateButton.setBackground(Color.lightGray);
		deleteButton.setBackground(Color.lightGray);


		addButton.setPreferredSize(buttonSize);
		updateButton.setPreferredSize(buttonSize);
		deleteButton.setPreferredSize(buttonSize);


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

		String[] columnNames = { "Customer ID", "First Name", "Surname", "Address", "Phone Number" };

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

		// JFrame class
		public class AddCustomerForm extends JFrame {
			
			
			// initialize variables

			static final String DATABASE_URL = "jdbc:mysql://localhost/purchases";
			static final String UserName_SQL = "root";
			static final String Password_SQL = "password";
			
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

			// Constructor
			public AddCustomerForm(String title) {
				// Set title for frame and choose layout
				super(title);

				getContentPane().setLayout(new BorderLayout());

				form.setLayout(new GridLayout(11, 1));
				padding1 = BorderFactory.createEmptyBorder(10, 30, -40, 30); // form panel padding
				form.setBorder(padding1);

				// Add first name label and text field to frame
				form.add(new JLabel("First Name"));
				form.add(firstNameField);

				// Add last name label and text field to frame
				form.add(new JLabel("Last Name"));
				form.add(lastNameField);

				// Add address label and text field to frame
				form.add(new JLabel("Address"));
				form.add(addressField);

				// Add phone number label and text field to frame
				form.add(new JLabel("Phone Number"));
				form.add(phoneNumField);

				// Add submit button to frame
				padding2 = BorderFactory.createEmptyBorder(0, 0, 20, 0); // button panel padding
				buttons.setBorder(padding2);
				buttons.add(submitButton);

				// Submit Button
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

				String firstName;
				String lastName;
				String address;
				String phoneNum;

				final String DATABASE_URL = "jdbc:mysql://localhost/purchases";
				final String UserName_SQL = "root";
				final String Password_SQL = "password";

				Connection connection = null;
				Statement statement = null;

				@Override
				public void actionPerformed(ActionEvent event) {
					//get values from form
					firstName = firstNameField.getText();
					lastName = lastNameField.getText();
					address = addressField.getText();
					phoneNum = phoneNumField.getText();

					if (firstName.isEmpty() || lastName.isEmpty() || address.isEmpty() || phoneNum.isEmpty()) {
						JOptionPane.showMessageDialog(AddCustomerForm.this, String.format(
								"One or more empty field(s), will not add to database", event.getActionCommand()));
					}
					else if (firstName.matches("^[a-zA-Z-'.\\s]+") == false
							|| lastName.matches("^[a-zA-Z-'.\\s]+") == false
							|| phoneNum.matches("^[+0-9\\-s]+") == false) {
						JOptionPane.showMessageDialog(AddCustomerForm.this, String
								.format("One or more invalid input(s), please try again", event.getActionCommand()));
					}
					else {
						try {

							// establish connection to database
							connection = DriverManager.getConnection(DATABASE_URL, UserName_SQL, Password_SQL);

							// create Statement for querying database
							statement = connection.createStatement();

							// Insert data into database
							PreparedStatement pstat = connection.prepareStatement(
									"INSERT INTO Customer (firstName, lastName, address, phoneNumber) VALUES(?,?,?,?)");
							pstat.setString(1, firstName);
							pstat.setString(2, lastName);
							pstat.setString(3, address);
							pstat.setString(4, phoneNum);
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

						JOptionPane.showMessageDialog(AddCustomerForm.this,
								String.format("Added to Database", event.getActionCommand()));
						firstNameField.setText("");
						lastNameField.setText("");
						addressField.setText("");
						phoneNumField.setText("");
						refreshJTable();
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

		}// end JFrame class

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
		}// end actionPerformed
	}// end addActionListener

	/*-----------------------------------------------------------update Button----------------------------------------------------------*/
	private class updateButtonHandler implements ActionListener {
		// JFrame class
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
			String customerId = model.getValueAt(selectedRow, 0).toString();
			String firstName = model.getValueAt(selectedRow, 1).toString();
			String lastName = model.getValueAt(selectedRow, 2).toString();
			String address = model.getValueAt(selectedRow, 3).toString();
			String phoneNum = model.getValueAt(selectedRow, 4).toString();

			// Constructor
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

						// check if there are no changes made
						if (newFirstName.equalsIgnoreCase(firstName) && newLastName.equalsIgnoreCase(lastName)
								&& newAddress.equalsIgnoreCase(address) && newPhoneNum.equalsIgnoreCase(phoneNum)) {
							JOptionPane.showMessageDialog(UpdateCustomerForm.this,
									String.format("No changes were made", event.getActionCommand()));
						}
						// check if there are any empty fields
						else if (newFirstName.isEmpty() || newLastName.isEmpty() || newAddress.isEmpty() || newPhoneNum.isEmpty()) {
							JOptionPane.showMessageDialog(UpdateCustomerForm.this, String.format(
									"One or more empty field(s), will not save changes", event.getActionCommand()));
						}
						// check if inputs are valid
						else if (newFirstName.matches("^[a-zA-Z-'.\\s]+") == false
									|| newLastName.matches("^[a-zA-Z-'.\\s]+") == false
									|| newPhoneNum.matches("^[+0-9\\-s]+") == false) {
								JOptionPane.showMessageDialog(UpdateCustomerForm.this, String
										.format("One or more invalid change(s), please try again", event.getActionCommand()));
						} // input validation if statement
						// execute insert record
						else {
							try {
								// establish connection to database
								connection = DriverManager.getConnection(DATABASE_URL, UserName_SQL, Password_SQL);

								// create Statement for updating database
								statement = connection.createStatement();
								statement.executeUpdate(
										"UPDATE Customer SET firstName = '" + newFirstName + "', lastName = '" 
								+ newLastName + "', address = '" + newAddress + "', phoneNumber = '" 
												+ newPhoneNum + "' WHERE customerId = '" + customerId + "' ");
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
							JOptionPane.showMessageDialog(UpdateCustomerForm.this,
									String.format("Record Updated", event.getActionCommand()));
							refreshJTable();
						} // end else
					}
				});
				padding2 = BorderFactory.createEmptyBorder(10, 50, 25, 50);
				button.setBorder(padding2);
				button.add(save);

				add(form, BorderLayout.CENTER);
				add(button, BorderLayout.SOUTH);

			} // end constructor

		} // end update customer form class

		public void actionPerformed(ActionEvent e) {
			if (jtable.getSelectedRow() != -1) {
				UpdateCustomerForm customerForm = new UpdateCustomerForm();
				customerForm.setSize(300, 400);
				customerForm.setLocation(400, 300);
				customerForm.setLocationRelativeTo(null);
				customerForm.setVisible(true);
			} else {
				JOptionPane.showMessageDialog(null, "Please select a record");
			}
		}// end actionPerformed

	}// end update button handler
	
	/*-----------------------------------------------------------Refresh JTable----------------------------------------------------------*/
	
	/*-----------------------------------------------------------Refresh JTABLE----------------------------------------------------------*/
	private void refreshJTable() {

		Connection connection = null;
		Statement statement = null;

		try {
			connection = DriverManager.getConnection(DATABASE_URL, UserName_SQL, Password_SQL);
			statement = connection.createStatement();
			ResultSet resultSet = statement.executeQuery(
					"SELECT customerId, firstName, lastName, address, phoneNumber FROM customer ORDER BY customerId ASC");
			ResultSetMetaData metaData = resultSet.getMetaData(); /* create for the columns count */
			int numberOfColumns = metaData.getColumnCount(); /* get the number of columns for Query Table */
			int numberOfRows = getJTableNumberOfRows(); /* get the number of rows for Query Table */
			Object[][] data = new Object[numberOfRows][numberOfColumns]; /* create a storage for the database */

			model.getDataVector().removeAllElements(); /* remove JTable all elements */

			/* While loop for getting all database into object */
			int j = 0, k = 0;
			while (resultSet.next()) {
				for (int i = 1; i <= numberOfColumns; i++) {
					data[j][k] = resultSet.getObject(i);
					k++;
				}
				Object[] addRow = { data[j][0], data[j][1], data[j][2], data[j][3], data[j][4] };
				model.addRow(addRow);
				k = 0;
				j++;
			} // end while
				// model.fireTableDataChanged(); /* no use at the moment*/
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
	}// end refreshTable
	
	/*---------------------------------------------get Number of Rows from Database------------------------------------------------------*/
	private int getJTableNumberOfRows() {

		int count = 0; /* create a integer object for rows count */
		Connection connection = null;
		Statement statement = null;
		try {
			connection = DriverManager.getConnection(DATABASE_URL, UserName_SQL, Password_SQL);
			statement = connection.createStatement();
			ResultSet resultSet = statement.executeQuery("SELECT COUNT(*) as numberOfRows FROM customer");
			resultSet.next();
			count = resultSet.getInt("numberOfRows");
			resultSet.close();
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
		return count; /* return the result of rows count */
	}// end getJTableNumberOfRows
	
	/*---------------------------------------------Clear Button Handler for Clear Button on Add Form-------------------------------------*/
	
	// Clear Field Handler for query text
	public class ClearFieldHandler implements MouseListener {

		@Override
		public void mouseClicked(MouseEvent event) {
			// TODO Auto-generated method stub

		}

		@Override
		public void mousePressed(MouseEvent e) {
			searchBarField.setText("");

		}

		@Override
		public void mouseReleased(MouseEvent e) {
			// TODO Auto-generated method stub

		}

		@Override
		public void mouseEntered(MouseEvent e) {
			// TODO Auto-generated method stub

		}

		@Override
		public void mouseExited(MouseEvent e) {
			// TODO Auto-generated method stub

		}

	}

}// end class
