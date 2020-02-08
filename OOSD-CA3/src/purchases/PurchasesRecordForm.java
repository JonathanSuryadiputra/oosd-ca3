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
import java.awt.Color;
import java.awt.Dimension;
import javax.swing.border.Border;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;

public class PurchasesRecordForm {

	private JPanel RecentPurchasesFormPanel = new JPanel();

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
	public PurchasesRecordForm() {

		getQuery();

		JLabel topicLabel = new JLabel("           Purchase Records Table           ");
		
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
		RecentPurchasesFormPanel.add(topPanel);
		RecentPurchasesFormPanel.add(searchPanel);
		RecentPurchasesFormPanel.add(panel);
		RecentPurchasesFormPanel.add(bottomPanel);

		/* set Style */
		Font topicFont = new Font("Serif", Font.BOLD, 25);
		Font buttonFont = new Font("Serif", Font.BOLD, 15);
		Border formBorder = BorderFactory.createLineBorder(Color.LIGHT_GRAY, 3);
		Dimension buttonSize = new Dimension(200, 50);

		topicLabel.setFont(topicFont);

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

	public JPanel getJPanel() {
		return RecentPurchasesFormPanel;
	}

	public void getQuery() {

		String[] columnNames = { "firstName", "lastName", "productName", "qtyProduct" };

		Connection connection = null;
		Statement statement = null;

		try {
			connection = DriverManager.getConnection(DATABASE_URL, UserName_SQL, Password_SQL);
			statement = connection.createStatement();
			ResultSet resultSet = statement.executeQuery(
					"select firstName, lastName, productName, qtyProduct from customer inner join invoice on customer.customerId = invoice.customerId inner join product on invoice.productId = product.productId;");

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