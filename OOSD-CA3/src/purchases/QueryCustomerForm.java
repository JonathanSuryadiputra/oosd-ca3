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
	   queryCustomerFormPanel.add(topPanel);
	   queryCustomerFormPanel.add(panel);
	   queryCustomerFormPanel.add(bottomPanel);   
	   
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
	   
	   queryCustomerFormPanel.setBackground(Color.WHITE);
   }
   
   
   public JPanel getJPanel() {
	   return queryCustomerFormPanel;
   }
   
   public void getQuery() {
	   
	   String[] columnNames = {"Customer ID","First Name","LastName","Address","Phone Number"};
	   
	   Connection connection = null;
	   Statement statement = null;
	   
	   try {
		   connection = DriverManager.getConnection( DATABASE_URL, UserName_SQL, Password_SQL );
		   statement = connection.createStatement();
		   ResultSet resultSet = statement.executeQuery("SELECT customerID, firstName, lastName, address, phoneNumber FROM Customer");
		   
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
		   jtable.getColumnModel().getColumn(0).setPreferredWidth(150);
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
			
		      JTextField firstNameField = new JTextField(10);
		      JTextField lastNameField = new JTextField(10);
		      JTextField addressField = new JTextField(50);
		      JTextField phoneNumField = new JTextField(10);
		      
		      JPanel panel = new JPanel();
		      panel.setPreferredSize(new Dimension(200,250));
		      panel.setLayout(new GridLayout(10, 1));
		      panel.add(new JLabel("First Name:"));
		      panel.add(firstNameField);
		      panel.add(new JLabel("Last Name:"));
		      panel.add(lastNameField);
		      panel.add(new JLabel("Address:"));
		      panel.add(addressField);
		      panel.add(new JLabel("Phone Number:"));
		      panel.add(phoneNumField);

		      int result = JOptionPane.showConfirmDialog(null, panel,"Create new Record", JOptionPane.OK_CANCEL_OPTION);
		      
		      /* click OK and go */
		      if (result == JOptionPane.OK_OPTION) {
		    	  
		      /* ***************************************************************************************** */  	  
		    	  if (!firstNameField.getText().equals("") && !lastNameField.getText().equals("") && !addressField.getText().equals("") && !phoneNumField.getText().equals("")) {
						
		    		  	String firstName = firstNameField.getText();
					  	String lastName = lastNameField.getText();
					  	String address = addressField.getText();
					  	String phoneNum = phoneNumField.getText();
		    		  
						Connection connection = null;
						Statement statement = null;

						try {
							connection = DriverManager.getConnection( DATABASE_URL, UserName_SQL, Password_SQL );
							statement = connection.createStatement();
							statement.executeUpdate("INSERT INTO customer (firstName, lastName, address, phoneNumber)" + " VALUES " + "('" + firstName + "','" + lastName + "','" + address + "','" + phoneNum + "')");
						}//end try
							
						catch(SQLException sqlException) {
							sqlException.printStackTrace();
						}//catch
							
						finally {
							try {
								statement.close();
								connection.close();
							}//end try
								
							catch ( Exception exception ) {
								exception.printStackTrace();
							}//end catch
							
						}//end finally		
						
						JOptionPane.showMessageDialog(null,"Added to Database");
					}//end if

		      }//end outer if
		}//end actionPerformed
	}//end addActionListener
   
   /* -----------------------------------------------------------delete Button----------------------------------------------------------*/
   private class deleteButtonHandler implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			if(jtable.getSelectedRow() != -1) {
				
				Connection connection = null;
				Statement statement = null;
			
				int selectedRowIndex = jtable.getSelectedRow();
				String deleteId = model.getValueAt(selectedRowIndex,0).toString();

				
				try {
				connection = DriverManager.getConnection(DATABASE_URL, UserName_SQL, Password_SQL);
				statement = connection.createStatement();
				statement.executeUpdate ("DELETE FROM Customer WHERE customerId = '" + deleteId + "' ");
				
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
			}
		}//end actionPerformed
	}//end addActionListener
   
   /* -----------------------------------------------------------update Button----------------------------------------------------------*/
   private class updateButtonHandler implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			if(jtable.getSelectedRow() != -1) {
				JTextField firstNameField = new JTextField(10);
				JTextField lastNameField = new JTextField(10);
				JTextField addressField = new JTextField(50);
				JTextField phoneNumField = new JTextField(10);
				
				int selectedRow = jtable.getSelectedRow();
				
				String customerId = model.getValueAt(selectedRow,0).toString();
				String firstName = model.getValueAt(selectedRow,1).toString();
				String lastName = model.getValueAt(selectedRow,2).toString();
				String address = model.getValueAt(selectedRow,3).toString();
				String phoneNum = model.getValueAt(selectedRow,4).toString();
				
				firstNameField.setText(firstName);
				lastNameField.setText(lastName);
				addressField.setText(address);
				phoneNumField.setText(phoneNum);
				
			    JPanel panel = new JPanel();
			    panel.setPreferredSize(new Dimension(200,250));
			    panel.setLayout(new GridLayout(10, 1));
			    panel.add(new JLabel("First Name:"));
			    panel.add(firstNameField);
			      
			    panel.add(new JLabel("Last Name:"));
			    panel.add(lastNameField);
			    panel.add(new JLabel("Address:"));
			    panel.add(addressField);
			    panel.add(new JLabel("Phone Number:"));
			    panel.add(phoneNumField);
			    
			    int result = JOptionPane.showConfirmDialog(null, panel,"Update Customer Record", JOptionPane.OK_CANCEL_OPTION);
			      if (result == JOptionPane.OK_OPTION) {
			    	  if (true) {
			    		 
			    		  String newFirstName = firstNameField.getText();
			    		  String newLastName = lastNameField.getText();
			    		  String newAddress = addressField.getText();
			    		  String newPhoneNum = phoneNumField.getText();
			    		  
			    		  Connection connection = null;
			    		  Statement statement = null;
			    		  
			    		try {
			    			connection = DriverManager.getConnection(DATABASE_URL, UserName_SQL, Password_SQL);
			  				statement = connection.createStatement();
			  				statement.executeUpdate("UPDATE customer SET firstName = '" + newFirstName + "', lastName = '" + newLastName + "', address = '" + newAddress + "', phoneNumber = '" + newPhoneNum + "' WHERE customerId = '" + customerId + "'");
			  			}//end try
			    		  
			    		catch(SQLException sqlException ) {
			    			sqlException . printStackTrace () ;
			  			}//end catch
			  						
			  			finally {
			  				try {
			  					statement. close () ;
			  					connection. close () ;
			  				}//end try
			  								
			  				catch ( Exception exception ) {
			  					exception . printStackTrace () ;
			  				}//end catch
			  			}//end finally
			    		JOptionPane.showMessageDialog(null,"Record Updated");
			    	  }//end if
			    	  else {
			    		  JOptionPane.showMessageDialog(null,"All Record is the same as Old one");
			    	  }//end else
			      }//end if
				
			}
			else {
				JOptionPane.showMessageDialog(null,"Please select a Record");
			}
				
				
				
		}//end actionPerformed
	}//end addActionListener
   
}// end class
