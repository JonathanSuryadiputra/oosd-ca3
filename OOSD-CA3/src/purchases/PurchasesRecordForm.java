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

/**
 * This class is for the purchases record table frame
 * @author weihao, jonathan, chi
 *
 */
public class PurchasesRecordForm {

	// initilize variable
	private JPanel RecentPurchasesFormPanel = new JPanel();

	private final String JDBC_DRIVER = "com.mysql.jdbc.Driver";
	private final String DATABASE_URL = "jdbc:mysql://localhost/purchases";
	private final String UserName_SQL = "root";
	private final String Password_SQL = "password";

	private JPanel topPanel = new JPanel();
	private JPanel panel = new JPanel();
	private JPanel bottomPanel = new JPanel();
	private JPanel jtablePanel = new JPanel();
	final JTextField searchBarField;

	private JTable jtable;
	private DefaultTableModel model;
	Border topPadding;

	// constructor
	/**
	 * the structure of the form
	 */
	public PurchasesRecordForm() {

		getQuery();

		JLabel topicLabel = new JLabel("Purchases Record Table     ");
		JButton refreshButton = new JButton("Refresh");
		
		refreshButtonHandler refreshHandler = new refreshButtonHandler();
		refreshButton.addActionListener(refreshHandler);
		
		// set up table search bar and sorter
				final TableRowSorter<TableModel> sorter = new TableRowSorter<TableModel>(model);
				jtable.setRowSorter(sorter);

				searchBarField = new JTextField(
						"Enter query here (Click on the field to clear it, then press Enter to clear query)");
				searchBarField.setHorizontalAlignment(JTextField.CENTER);
				searchBarField.setPreferredSize(new Dimension(500, 40));

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
				
				topPadding = BorderFactory.createEmptyBorder(-15, 0, 0, 0);
				topPanel.setLayout(new BorderLayout());
				topPanel.setBorder(topPadding);

		/* add components */
		topPanel.add(topicLabel);
		topPanel.add(searchBarField);
		panel.add(jtablePanel);
		bottomPanel.add(refreshButton);
		RecentPurchasesFormPanel.add(topPanel);
		RecentPurchasesFormPanel.add(panel);
		RecentPurchasesFormPanel.add(bottomPanel);

		/* set Style */
		Font topicFont = new Font("Calibri", Font.BOLD, 25);
		Font buttonFont = new Font("Calibri", Font.BOLD, 15);
		Border formBorder = BorderFactory.createLineBorder(Color.LIGHT_GRAY, 3);
		Dimension buttonSize = new Dimension(200, 50);

		topicLabel.setFont(topicFont);
		
		refreshButton.setFont(buttonFont);
		refreshButton.setBackground(Color.lightGray);
		refreshButton.setPreferredSize(buttonSize);

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

		RecentPurchasesFormPanel.setBackground(Color.WHITE);
	}
	//getJPanel
	/**
	 * @return JPanel for Purchases Record Form mainframe
	 */
	public JPanel getJPanel() {
		return RecentPurchasesFormPanel;
	}
	//getQuery
	/**
	 * generate the JTable and list the data
	 */
	public void getQuery() {

		String[] columnNames = { "First Name", "Surname", "Product Name", "Price (\u20ac)", "Quantity", "Total Price (\u20ac)", "Purchase Date", "Purchase Time" };

		Connection connection = null;
		Statement statement = null;

		try {
			connection = DriverManager.getConnection(DATABASE_URL, UserName_SQL, Password_SQL);
			statement = connection.createStatement();
			ResultSet resultSet = statement.executeQuery(
					"select firstName, lastName, productName, price, qtyProduct, (price * qtyProduct), invoiceDate, invoiceTime from"
					+ " customer inner join invoice on customer.customerId = invoice.customerId inner join product on invoice.productId = product.productId ORDER BY invoiceDate DESC, invoiceTime DESC;");

			ResultSetMetaData metaData = resultSet.getMetaData();

			int numberOfColumns = metaData.getColumnCount();
			int numberOfRows = getJTableNumberOfRows();
			Object[][] data = new Object[numberOfRows][numberOfColumns];

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
			// set up the interface
			jtable.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
			jtable.setPreferredScrollableViewportSize(new Dimension(1230, 450));
			JScrollPane scrollPane = new JScrollPane(jtable);

			jtablePanel.add(scrollPane);
			jtable.getColumnModel().getColumn(0).setPreferredWidth(250);
			jtable.getColumnModel().getColumn(1).setPreferredWidth(250);
			jtable.getColumnModel().getColumn(2).setPreferredWidth(250);
			jtable.getColumnModel().getColumn(3).setPreferredWidth(200);
			jtable.getColumnModel().getColumn(4).setPreferredWidth(200);
			jtable.getColumnModel().getColumn(5).setPreferredWidth(200);
			jtable.getColumnModel().getColumn(6).setPreferredWidth(250);
			jtable.getColumnModel().getColumn(7).setPreferredWidth(250);
			jtable.setAutoResizeMode(jtable.AUTO_RESIZE_LAST_COLUMN);
			jtable.setBackground(Color.white);
			jtable.setForeground(Color.black);
			jtable.setRowHeight(30);
			jtable.getTableHeader().setFont(new Font("Calibri", Font.BOLD, 15));
			jtable.setFont(new Font("Calibri", Font.PLAIN, 13));

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
	
	/*-----------------------------------------------------------refresh Button----------------------------------------------------------*/
	/**
	 * define the action of button to perform the refresh JTable function
	 */
	private class refreshButtonHandler implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent event) {
			// TODO Auto-generated method stub
			refreshJTable();
		}
		
	}
	
	/*-----------------------------------------------------------Refresh JTABLE----------------------------------------------------------*/
	/**
	 * refresh the Jtable to show the most updated data to user
	 */
	private void refreshJTable() {

		Connection connection = null;
		Statement statement = null;

		try {
			connection = DriverManager.getConnection(DATABASE_URL, UserName_SQL, Password_SQL);
			statement = connection.createStatement();
			ResultSet resultSet = statement.executeQuery(
					"select firstName, lastName, productName, price, qtyProduct, (price * qtyProduct), invoiceDate, invoiceTime from"
							+ " customer inner join invoice on customer.customerId = invoice.customerId inner join product on invoice.productId = product.productId ORDER BY invoiceDate DESC, invoiceTime DESC;");
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
				Object[] addRow = { data[j][0], data[j][1], data[j][2], data[j][3], data[j][4], data[j][5], data[j][6], data[j][7]};
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
	/**
	 * count the current data and return the number of rows
	 */
	private int getJTableNumberOfRows() {

		int count = 0; /* create a integer object for rows count */
		Connection connection = null;
		Statement statement = null;
		try {
			connection = DriverManager.getConnection(DATABASE_URL, UserName_SQL, Password_SQL);
			statement = connection.createStatement();
			ResultSet resultSet = statement.executeQuery("SELECT COUNT(*) as numberOfRows FROM "
					+ "customer inner join invoice on customer.customerId = invoice.customerId inner join product on invoice.productId = product.productId ORDER BY invoiceDate DESC, invoiceTime DESC;");
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

}// end class
