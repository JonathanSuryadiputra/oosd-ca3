package purchases;

import java.sql.Connection;

import java.sql.Statement;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
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
import java.awt.Color;
import java.awt.Dimension; 
import javax.swing.border.Border;
import javax.swing.table.DefaultTableModel;

import java.text.SimpleDateFormat;
import java.util.Date;
import purchases.AddCustomerForm;

public class QueryInvoiceForm {
	
	private JPanel queryInvoiceFormPanel = new JPanel();
	
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
	public QueryInvoiceForm() {
	   
		getQuery();
		JLabel topicLabel = new JLabel("           Query Invoice Table           ");
		JButton addButton = new JButton("Create");
		JButton updateButton = new JButton("Update");
		JButton deleteButton = new JButton("Delete");
		
		addButtonHandler addHandler = new addButtonHandler();
		addButton.addActionListener(addHandler);
		updateButtonHandler updateHandler = new updateButtonHandler();
		updateButton.addActionListener(updateHandler);
		deleteButtonHandler deleteHandler = new deleteButtonHandler();
		deleteButton.addActionListener(deleteHandler);

		/* add components */
		topPanel.add(topicLabel);
		panel.add(jtablePanel);
		bottomPanel.add(addButton);
		bottomPanel.add(new JLabel("                   "));
		bottomPanel.add(updateButton);
		bottomPanel.add(new JLabel("                   "));
		bottomPanel.add(deleteButton);
		queryInvoiceFormPanel.add(topPanel);
		queryInvoiceFormPanel.add(panel);
		queryInvoiceFormPanel.add(bottomPanel);   
	   
		/* set Style */
		Font topicFont = new Font("Serif",Font.BOLD,25);
		Font buttonFont = new Font("Serif",Font.BOLD,15);
		Border formBorder = BorderFactory.createLineBorder(Color.LIGHT_GRAY, 3);
		Dimension buttonSize = new Dimension(200,50);
	   
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
		jtablePanel.setPreferredSize(new Dimension(1260,500));
		jtablePanel.setBorder(formBorder);		
		topPanel.setLayout(new FlowLayout(1,0,30));
		topPanel.setPreferredSize(new Dimension(1260,100)); 
		topPanel.setOpaque(false);
		panel.setPreferredSize(new Dimension(1280,520));
		panel.setOpaque(false);
		bottomPanel.setLayout(new FlowLayout(1,0,10));
		bottomPanel.setPreferredSize(new Dimension(1260,80));
		bottomPanel.setOpaque(false);
		queryInvoiceFormPanel.setBackground(Color.WHITE);
   }//end constructor
   
   public JPanel getJPanel() {
	   return queryInvoiceFormPanel;
   }
   
   public void getQuery() {
	  
	   String[] columnNames = {"customerId","productId","qtyProduct","invoiceDate","invoiceTime"};
	   
	   Connection connection = null;
	   Statement statement = null;
	   
	   try {
		   connection = DriverManager.getConnection( DATABASE_URL, UserName_SQL, Password_SQL );
		   statement = connection.createStatement();
		   ResultSet resultSet = statement.executeQuery("SELECT customerID, productId, qtyProduct, invoiceDate, invoiceTime FROM invoice");
		   
		   ResultSetMetaData metaData = resultSet.getMetaData();
		   
		   int numberOfColumns = metaData.getColumnCount(); 
		   Object[][] data = new Object[50][numberOfColumns];
		   
		   int j=0,k=0;
		   while (resultSet.next() ) {
			   for (int i=1;i <=numberOfColumns; i++){
				   data[j][k] = resultSet.getObject( i );
				   k++;
			   }
			   k=0; j++;
		   } // end while
		   
		   model = new DefaultTableModel(data, columnNames);
		   
		   /* create non-editable JTable */
		   jtable = new JTable(model){ public boolean isCellEditable(int row, int column){ return false; }};; 
		   jtable.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
		   jtable.setPreferredScrollableViewportSize(new Dimension(1230, 450));
		   JScrollPane scrollPane = new JScrollPane(jtable);

		   jtablePanel.add(scrollPane); 
		   jtable.getColumnModel().getColumn(0).setPreferredWidth(250);
		   jtable.getColumnModel().getColumn(1).setPreferredWidth(250);
		   jtable.getColumnModel().getColumn(2).setPreferredWidth(250);
		   jtable.getColumnModel().getColumn(3).setPreferredWidth(400);
		   jtable.getColumnModel().getColumn(4).setPreferredWidth(400);
		   jtable.setAutoResizeMode(jtable.AUTO_RESIZE_LAST_COLUMN);
		   jtable.setBackground(Color.white);
		   jtable.setForeground(Color.black);
		   jtable.setRowHeight(30);
		   jtable.setFont(new Font("Serif",Font.PLAIN,15));	
		   
	   }  // end try
	   catch (SQLException sqlException)  {
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
   }//end getQuery
   
   /* -----------------------------------------------------------add Button----------------------------------------------------------*/
   private class addButtonHandler implements ActionListener {
	   @Override
	   public void actionPerformed(ActionEvent e) {
	   	AddInvoiceForm InvoiceForm = new AddInvoiceForm("Invoice Form");
	   	InvoiceForm.setSize(275, 350);
	   	InvoiceForm.setLocation(400, 300);
	   	InvoiceForm.setLocationRelativeTo(null);
	   	InvoiceForm.setVisible(true);
	   }//end actionPerformed
   }//end addActionListener
   
   /* -----------------------------------------------------------update Button----------------------------------------------------------*/
   private class updateButtonHandler implements ActionListener {
	   @Override
		public void actionPerformed(ActionEvent e) {
		   if(jtable.getSelectedRow() != -1) {
			   
			   int selectedRowIndex = jtable.getSelectedRow();
			   String customerId = model.getValueAt(selectedRowIndex,0).toString();
			   String productId = model.getValueAt(selectedRowIndex,1).toString();
			   String qtyProduct = model.getValueAt(selectedRowIndex,2).toString();
			   String invoiceDate = model.getValueAt(selectedRowIndex,3).toString();
			   String invoiceTime = model.getValueAt(selectedRowIndex,4).toString();
			   String firstName = new String();
			   String lastName = new String();
			   String productName = new String();
			   
			   /*    looking for customer firstName & lastName by giving customerId */
			   Connection connection = null;
			   Statement statement = null;
			   
			   try{
					connection = DriverManager.getConnection(DATABASE_URL, UserName_SQL, Password_SQL);
					statement = connection.createStatement();
					ResultSet customerResultSet = statement.executeQuery("select firstName, lastName from customer where customerId =" + customerId);
					while(customerResultSet.next() ) {
						firstName = customerResultSet.getString("firstName");
						lastName = customerResultSet.getString("lastName");
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
			   
			   String customerFullName = firstName + " " + lastName;
			   	
			   /* looking for productName by giving productId */
			   connection = null;
			   statement = null;
			   
			   try{
					connection = DriverManager.getConnection(DATABASE_URL, UserName_SQL, Password_SQL);
					statement = connection.createStatement();
					ResultSet productResultSet = statement.executeQuery("select productName from product where productId =" + productId);
					while(productResultSet.next() ) {
						productName = productResultSet.getString("productName");
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
			   
			   JTextField qtyProductField = new JTextField();
			   qtyProductField.setText(qtyProduct);

			   JPanel panel = new JPanel();
			   panel.setPreferredSize(new Dimension(200,220));
			   panel.setLayout(new GridLayout(8, 1));
			   panel.add(new JLabel("Date & Time: "+ invoiceDate + " " + invoiceTime));
			   panel.add(new JLabel("Customer ID: " + customerId));
			   panel.add(new JLabel("Customer Name: " + customerFullName));
			   panel.add(new JLabel("Product ID: " + productId));
			   panel.add(new JLabel("Product Name: " + productName));
			   panel.add(new JLabel("                                      "));
			   panel.add(new JLabel("Product Quantity"));
			   panel.add(qtyProductField);
			  
			   int result = JOptionPane.showConfirmDialog(null, panel,"Update Record", JOptionPane.OK_CANCEL_OPTION);
			   if (result == JOptionPane.OK_OPTION) {
				   
				   int newQtyProduct = Integer.parseInt(qtyProductField.getText());
				   
				   if (newQtyProduct < 1 || newQtyProduct > 99) {
			    		  JOptionPane.showMessageDialog(null,"Invalid Quantity input");
			    	  }//end if
				   
				   else {

					   try{
		  					connection = DriverManager.getConnection(DATABASE_URL, UserName_SQL, Password_SQL);
		  					statement = connection.createStatement();
		  					statement.executeUpdate("UPDATE invoice SET qtyProduct =" + newQtyProduct + " WHERE (customerId,productId,invoiceDate,invoiceTime) = (" + customerId + "," + productId + ", '" + invoiceDate + "', '" + invoiceTime + "')");
		  				 }//end try
		  				 
		  				catch(SQLException sqlException) {
		  					sqlException.printStackTrace();
		  				}//end catch
		  					
		  				finally {
		  					try {
		  						statement.close();
		  						connection.close();
		  					}//end try
		  						
		  					catch ( Exception exception ) {
		  						exception.printStackTrace();
		  					}//end catch
		  				}//end finally		   
		  			  JOptionPane.showMessageDialog(null,"Record Updated");	
				   }//end else
		   
			   }//end OK if
			   }//end outer if
		}//end actionPerformed
   }//end updateActionListener
   
   /* -----------------------------------------------------------delete Button----------------------------------------------------------*/
   private class deleteButtonHandler implements ActionListener {
	    @Override
		public void actionPerformed(ActionEvent e) {
			if(jtable.getSelectedRow() != -1) {
				
				Connection connection = null;
				Statement statement = null;
			
				int selectedRowIndex = jtable.getSelectedRow();
				String customerId = model.getValueAt(selectedRowIndex,0).toString();
				String productId = model.getValueAt(selectedRowIndex,1).toString();
				String invoiceDate = model.getValueAt(selectedRowIndex,3).toString();
				String invoiceTime = model.getValueAt(selectedRowIndex,4).toString();
				
				try {
				connection = DriverManager.getConnection(DATABASE_URL, UserName_SQL, Password_SQL);
				statement = connection.createStatement();
				statement.executeUpdate ("DELETE FROM invoice WHERE (customerId, productId, invoiceDate, invoiceTime) = ('" + customerId + "', '" + productId + "', '" + invoiceDate + "', '" + invoiceTime + "')");
				}//end try

				catch(SQLException sqlException ) {
					sqlException . printStackTrace () ;
				}//end catch
				
				finally {
					try {
						statement. close () ;
						connection. close () ;
					}//end try
					
					catch (Exception exception) {
						exception . printStackTrace () ;
					}//end catch
				}//end finally
				model.removeRow(jtable.getSelectedRow());
				JOptionPane.showMessageDialog(null,"Deleted successfully");	
			}//end if
			else {
				JOptionPane.showMessageDialog(null,"Please select a record");
			}//end else
		}//end actionPerformed
   }//end deleteActionListener
}// end class
