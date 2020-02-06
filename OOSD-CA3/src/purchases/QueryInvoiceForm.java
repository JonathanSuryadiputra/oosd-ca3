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

import java.text.SimpleDateFormat;
import java.util.Date;

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
	private JPanel searchPanel = new JPanel();
	final JTextField searchBarField;
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
		
		//set up table search bar and sorter
		final TableRowSorter<TableModel> sorter = new TableRowSorter<TableModel>(model);
		jtable.setRowSorter(sorter);
				
		searchPanel.setLayout(new FlowLayout());
		searchPanel.setBackground(Color.WHITE);
				
		searchBarField = new JTextField("Enter query here (Click on the field to clear it, then press Enter to clear query)");
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
					}
					else {
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
		queryInvoiceFormPanel.add(topPanel);
		queryInvoiceFormPanel.add(searchPanel);
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
	   
	   //JFrame class
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
		}// end JFrame class

	   	@Override
		public void actionPerformed(ActionEvent e) {
	   		AddInvoiceForm invoiceForm = new AddInvoiceForm("Invoice Form");
	   		invoiceForm.setSize(275, 350);
			invoiceForm.setLocation(400, 300);
			invoiceForm.setLocationRelativeTo(null);
			invoiceForm.setVisible(true);
		}//end actionPerformed
   }//end addActionListener
   
   /* -----------------------------------------------------------update Button----------------------------------------------------------*/
   private class updateButtonHandler implements ActionListener {
	   class UpdateInvoiceForm extends JFrame {
		   
		   int selectedRowIndex = jtable.getSelectedRow();
		   
		   String customerId = model.getValueAt(selectedRowIndex,0).toString();
		   String productId = model.getValueAt(selectedRowIndex,1).toString();
		   String qtyProduct = model.getValueAt(selectedRowIndex,2).toString();
		   String invoiceDate = model.getValueAt(selectedRowIndex,3).toString();
		   String invoiceTime = model.getValueAt(selectedRowIndex,4).toString();
		   
		   JTextField qtyProductField;
		   JPanel form;
		   JPanel buttonPanel;
		   JButton save;
		   Border padding1;
		   Border padding2;
		   
		   public String getCustomerName() {
			   String firstName = "";
			   String lastName = "";
			   String customerFullName = "";
			   
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
			   
			   customerFullName = firstName + " " + lastName;
			   
			   return customerFullName;
		   }
		   
		   public String getProductName() {
			   String productName = "";
			   
			   /* looking for productName by giving productId */
			   Connection connection = null;
			   Statement statement = null;
			   
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
			   
			   return productName;
		   }
		   
		   //Constructor
		   public UpdateInvoiceForm() {
			   super("Update Invoice");
			   
			   getContentPane().setLayout(new BorderLayout());

			   form = new JPanel();
			   form.setLayout(new GridLayout(7, 1));
			   
			   form.add(new JLabel("Date & Time : "+ invoiceDate + " " + invoiceTime + ""));
			   form.add(new JLabel("Customer ID : " + customerId + ""));
			   form.add(new JLabel("Customer Name : " + getCustomerName() + ""));
			   form.add(new JLabel("Product ID : " + productId + ""));
			   form.add(new JLabel("Product Name : " + getProductName() + ""));
			   
			   form.add(new JLabel("Product Quantity"));
			   qtyProductField = new JTextField();
			   qtyProductField.setText(qtyProduct);
			   form.add(qtyProductField);
			   
			   padding1 = BorderFactory.createEmptyBorder(10, 30, 10, 30); // padding for main form panel
			   form.setBorder(padding1);
			   
			   save = new JButton("Save");
			   save.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					// TODO Auto-generated method stub
					int newQtyProduct = Integer.parseInt(qtyProductField.getText());
					
					Connection connection = null;
					Statement statement = null;
					   
					if (newQtyProduct < 1 || newQtyProduct > 99) {
						JOptionPane.showMessageDialog(null,"Invalid quantity input");
					}// end if
					else if (newQtyProduct == Integer.parseInt(qtyProduct)) {
						JOptionPane.showMessageDialog(null,"No changes were made");
					}
					else {
						try {
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
						}
			  		}
			   });
			   buttonPanel = new JPanel();
			   buttonPanel.setLayout(new GridLayout(1, 1));
			   
			   buttonPanel.add(save);
			   padding2 = BorderFactory.createEmptyBorder(0, 40, 10, 40);
			   buttonPanel.setBorder(padding2);
			   
			   add(form, BorderLayout.CENTER);
			   add(buttonPanel, BorderLayout.SOUTH);
		   }
	   }//end UpdateInvoiceForm

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		if(jtable.getSelectedRow() != -1) {
			UpdateInvoiceForm invoiceForm = new UpdateInvoiceForm();
			invoiceForm.setSize(300, 400);
			invoiceForm.setLocation(400, 300);
			invoiceForm.setLocationRelativeTo(null);
			invoiceForm.setVisible(true);
		}
		else {
			JOptionPane.showMessageDialog(null,"Please select a record");
		}
	}
   }
   
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
}// end QueryCustomerForm class
