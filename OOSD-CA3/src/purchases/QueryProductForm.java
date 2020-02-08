package purchases;

import java.sql.Connection;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
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

public class QueryProductForm {

	private JPanel queryProductFormPanel = new JPanel();

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
	public QueryProductForm() {

		getQuery();

		JLabel topicLabel = new JLabel("           Query Product Table           ");
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
		queryProductFormPanel.add(topPanel);
		queryProductFormPanel.add(searchPanel);
		queryProductFormPanel.add(panel);
		queryProductFormPanel.add(bottomPanel);

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

		queryProductFormPanel.setBackground(Color.WHITE);
	}

	public JPanel getJPanel() {
		return queryProductFormPanel;
	}

	public void getQuery() {

		String[] columnNames = { "Product ID", "Product Name", "Description", "Price" };

		Connection connection = null;
		Statement statement = null;

		try {
			connection = DriverManager.getConnection(DATABASE_URL, UserName_SQL, Password_SQL);
			statement = connection.createStatement();
			ResultSet resultSet = statement
					.executeQuery("SELECT productId, productName, description, price FROM product;");

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
			jtable.getColumnModel().getColumn(1).setPreferredWidth(350);
			jtable.getColumnModel().getColumn(2).setPreferredWidth(600);
			jtable.getColumnModel().getColumn(3).setPreferredWidth(150);
			jtable.setAutoResizeMode(jtable.AUTO_RESIZE_LAST_COLUMN);
			jtable.setBackground(Color.white);
			jtable.setForeground(Color.black);
			jtable.setRowHeight(30);
			jtable.setFont(new Font("Serif", Font.PLAIN, 15));

			jtable.repaint();
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
		public class AddProductForm extends JFrame {
			private JTextField productNameField = new JTextField();
			private JTextField descriptionField = new JTextField();
			private JTextField priceField = new JTextField();
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
				form.add(new JLabel("Product Name"));
				form.add(productNameField);

				// Add Description label and text field to frame
				form.add(new JLabel("Decription"));
				form.add(descriptionField);

				// Add Price label and text field to frame
				form.add(new JLabel("Price " + "\u20ac"));
				form.add(priceField);

				padding2 = BorderFactory.createEmptyBorder(0, 30, 20, 30);
				buttons.setBorder(padding2);
				buttons.setLayout(new GridLayout(1, 2));
				// Add submit button to frame
				buttons.add(submitButton);

				SubmitButtonHandler submitHandler = new SubmitButtonHandler();
				submitButton.addActionListener(submitHandler);

				// Add clear button to frame
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
				final String UserName_SQL = "root";
				final String Password_SQL = "password";

				Connection connection = null;
				Statement statement = null;

				@Override
				public void actionPerformed(ActionEvent event) {
					productName = productNameField.getText();
					description = descriptionField.getText();
					price = priceField.getText();

					if (productName.isEmpty() || description.isEmpty() || price.isEmpty()) {
						JOptionPane.showMessageDialog(AddProductForm.this, String.format(
								"One or more empty field(s), will not add to database", event.getActionCommand()));
					}
					else if (productName.matches("^[a-zA-Z-'.\\s0-9]+") == false
							|| description.matches("^[a-zA-Z-' .\\s0-9]+") == false
							|| price.matches("^[0-9.]+") == false) {
						JOptionPane.showMessageDialog(AddProductForm.this, String
								.format("One or more invalid input(s), please try again", event.getActionCommand()));
					}
					else {

						try {

							// establish connection to database
							connection = DriverManager.getConnection(DATABASE_URL, UserName_SQL, Password_SQL);

							// create Statement for querying database
							statement = connection.createStatement();

							// Insert data into database
							// statement.executeUpdate("INSERT INTO Product (productName, description,
							// price)" + "VALUES" + "('" + productName + "','" + description + "','"
							// +price)");
							PreparedStatement pstat = connection.prepareStatement(
									"INSERT INTO Product (productName, description, price) VALUES(?,?,?)");
							pstat.setString(1, productName);
							pstat.setString(2, description);
							pstat.setString(3, price);
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

						JOptionPane.showMessageDialog(AddProductForm.this,
								String.format("Added to Database", event.getActionCommand()));
						refreshJTable();
						productNameField.setText("");
						descriptionField.setText("");
						priceField.setText("");
					}
				}

			}

			// clear button handler
			private class ClearButtonHandler implements ActionListener {

				@Override
				public void actionPerformed(ActionEvent event) {
					productNameField.setText("");
					descriptionField.setText("");
					priceField.setText("");
				}

			}
		} // end JFrame class

		@Override
		public void actionPerformed(ActionEvent e) {
			AddProductForm productForm = new AddProductForm("Product Form");
			productForm.setSize(275, 350);
			productForm.setLocation(400, 300);
			productForm.setLocationRelativeTo(null);
			productForm.setVisible(true);

		}// end actionPerformed
	}// end addActionListener

	/*-----------------------------------------------------------update Button----------------------------------------------------------*/

	private class updateButtonHandler implements ActionListener {
		class UpdateProductForm extends JFrame {
			JTextField productNameField = new JTextField(10);
			JTextField descriptionField = new JTextField(10);
			JTextField priceField = new JTextField(10);
			JButton save = new JButton("Save");
			JPanel form = new JPanel();
			JPanel buttonPanel = new JPanel();
			Border padding1;
			Border padding2;

			int selectedRow = jtable.getSelectedRow();

			String productId = model.getValueAt(selectedRow, 0).toString();
			String productName = model.getValueAt(selectedRow, 1).toString();
			String description = model.getValueAt(selectedRow, 2).toString();
			String price = model.getValueAt(selectedRow, 3).toString();

			public UpdateProductForm() {
				super("Update Product");

				getContentPane().setLayout(new BorderLayout());

				productNameField.setText(productName);
				descriptionField.setText(description);
				priceField.setText(price);

				padding1 = BorderFactory.createEmptyBorder(10, 30, 15, 30); // padding for main form panel
				form.setBorder(padding1);
				form.setLayout(new GridLayout(6, 1));
				form.add(new JLabel("Product Name"));
				form.add(productNameField);
				form.add(new JLabel("Description"));
				form.add(descriptionField);
				form.add(new JLabel("Price " + "\u20ac"));
				form.add(priceField);

				buttonPanel.setLayout(new GridLayout(1, 1));
				save.addActionListener(new ActionListener() {
					String newProductName;
					String newDescription;
					String newPrice;

					Connection connection = null;
					Statement statement = null;

					@Override
					public void actionPerformed(ActionEvent event) {
						newProductName = productNameField.getText();
						newDescription = descriptionField.getText();
						newPrice = priceField.getText();

						if (newProductName.equalsIgnoreCase(productName) && newDescription.equalsIgnoreCase(description)
								&& newPrice.equalsIgnoreCase(price)) {
							JOptionPane.showMessageDialog(UpdateProductForm.this,
									String.format("No changes were made", event.getActionCommand()));
						}
						else if (newProductName.isEmpty() || newDescription.isEmpty() || newPrice.isEmpty()) {
							JOptionPane.showMessageDialog(UpdateProductForm.this, String.format(
									"One or more empty field(s), will not save changes", event.getActionCommand()));
						}
						else if (newProductName.matches("^[a-zA-Z-'.\\s0-9]+") == false
								|| newDescription.matches("^[a-zA-Z-' .\\s0-9]+") == false
								|| newPrice.matches("^[0-9.]+") == false) {
							JOptionPane.showMessageDialog(UpdateProductForm.this, String
									.format("One or more invalid change(s), please try again", event.getActionCommand()));
						}
						else {
							try {
								connection = DriverManager.getConnection(DATABASE_URL, UserName_SQL, Password_SQL);
								statement = connection.createStatement();
								statement.executeUpdate("UPDATE product SET productName = '" + newProductName
										+ "', description = '" + newDescription + "', price = '" + newPrice
										+ "' WHERE productId = '" + productId + "'");
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
							JOptionPane.showMessageDialog(UpdateProductForm.this,
									String.format("Record Updated", event.getActionCommand()));
							refreshJTable();
						}
					}

				});
				padding2 = BorderFactory.createEmptyBorder(20, 40, 20, 40); // padding for button panel
				buttonPanel.setBorder(padding2);
				buttonPanel.add(save);

				add(form, BorderLayout.CENTER);
				add(buttonPanel, BorderLayout.SOUTH);

			}
		}

		public void actionPerformed(ActionEvent e) {
			if (jtable.getSelectedRow() != -1) {
				UpdateProductForm productForm = new UpdateProductForm();
				productForm.setSize(275, 350);
				productForm.setLocation(250, 250);
				productForm.setLocationRelativeTo(null);
				productForm.setVisible(true);
			} else {
				JOptionPane.showMessageDialog(null, "Please select a Record");
			}

		}// end actionPerformed
	}// end updateActionListener

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
					statement.executeUpdate("DELETE FROM Product WHERE productId = '" + deleteId + "' ");

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
	}// end deleteActionListener

	/*-----------------------------------------------------------Refresh JTABLE----------------------------------------------------------*/
	private void refreshJTable() {

		Connection connection = null;
		Statement statement = null;

		try {
			connection = DriverManager.getConnection(DATABASE_URL, UserName_SQL, Password_SQL);
			statement = connection.createStatement();
			ResultSet resultSet = statement.executeQuery(
					"SELECT productId, productName, description, price FROM product ORDER BY productId ASC");
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
				Object[] addRow = { data[j][0], data[j][1], data[j][2], data[j][3] };
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

	/*-----------------------------------------------------------get Number of Rows from Database----------------------------------------------------------*/
	private int getJTableNumberOfRows() {

		int count = 0; /* create a integer object for rows count */
		Connection connection = null;
		Statement statement = null;
		try {
			connection = DriverManager.getConnection(DATABASE_URL, UserName_SQL, Password_SQL);
			statement = connection.createStatement();
			ResultSet resultSet = statement.executeQuery("SELECT COUNT(*) as numberOfRows FROM product");
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

	// Clear field handler for query field
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
