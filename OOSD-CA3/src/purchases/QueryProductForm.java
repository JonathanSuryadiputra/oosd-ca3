package purchases;

import java.sql.Connection;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Date;
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

public class QueryProductForm {
	
	private JPanel queryProductFormPanel = new JPanel();
	
   private final String JDBC_DRIVER = "com.mysql.jdbc.Driver";        
   private final String DATABASE_URL = "jdbc:mysql://localhost/purchases";
   private final String UserName_SQL = "root";
   private final String Password_SQL = "Lwhzyy520";
   
   private JPanel topPanel = new JPanel();
   private JPanel panel = new JPanel();
   private JPanel bottomPanel = new JPanel();
   private JPanel jtablePanel = new JPanel();
   
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
	   
	   /* add components */
	   topPanel.add(topicLabel);
	   panel.add(jtablePanel);
	   bottomPanel.add(addButton);
	   bottomPanel.add(new JLabel("                   "));
	   bottomPanel.add(updateButton);
	   bottomPanel.add(new JLabel("                   "));
	   bottomPanel.add(deleteButton);
	   queryProductFormPanel.add(topPanel);
	   queryProductFormPanel.add(panel);
	   queryProductFormPanel.add(bottomPanel);   
	   
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
	   
	   queryProductFormPanel.setBackground(Color.WHITE);
   }
   
   public JPanel getJPanel() {
	   return queryProductFormPanel;
   }
   
   public void getQuery() {
	   
	   String[] columnNames = {"Product ID","Product Name","Description","Price"};
	   
	   Connection connection = null;
	   Statement statement = null;
	   
	   try {
		   connection = DriverManager.getConnection( DATABASE_URL, UserName_SQL, Password_SQL );
		   statement = connection.createStatement();
		   ResultSet resultSet = statement.executeQuery("SELECT productId, productName, description, price FROM product;");
		   
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
		   jtable.getColumnModel().getColumn(1).setPreferredWidth(350);
		   jtable.getColumnModel().getColumn(2).setPreferredWidth(600);
		   jtable.getColumnModel().getColumn(3).setPreferredWidth(150);
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
		      JTextField ProductNameField = new JTextField(10);
		      JTextField DescriptionField = new JTextField(10);
		      JTextField PriceField = new JTextField(100);
		      
		      JPanel panel = new JPanel();
		      panel.setPreferredSize(new Dimension(200,250));
		      panel.setLayout(new GridLayout(10, 1));
		      panel.add(new JLabel("Product Name:"));
		      panel.add(ProductNameField);
		      panel.add(new JLabel("Description:"));
		      panel.add(DescriptionField);
		      panel.add(new JLabel("Price:"));
		      panel.add(PriceField);

		      int result = JOptionPane.showConfirmDialog(null, panel,"Create new Record", JOptionPane.OK_CANCEL_OPTION);
		      
		      /* click OK and go */
		      if (result == JOptionPane.OK_OPTION) {
		    	  
		      /* ***************************************************************************************** */  	  
		    	  if (!ProductNameField.getText().equals("") && !DescriptionField.getText().equals("") && !PriceField.getText().equals("")) {
						
		    		  	String productName = ProductNameField.getText();
					  	String description = DescriptionField.getText();
					  	String price = PriceField.getText();
		    		  
						Connection connection = null;
						Statement statement = null;

						try {
							connection = DriverManager.getConnection( DATABASE_URL, UserName_SQL, Password_SQL );
							statement = connection.createStatement();
							statement.executeUpdate("INSERT INTO product ( productName , description, price)" + " VALUES " + "('" + productName + "','" + description + "','" + price + "')");
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
   
   /* -----------------------------------------------------------update Button----------------------------------------------------------*/
   
   private class updateButtonHandler implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			JOptionPane.showMessageDialog(null,"under construction");	
		}//end actionPerformed
   }//end updateActionListener
   
   /* -----------------------------------------------------------delete Button----------------------------------------------------------*/
   
   private class deleteButtonHandler implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			JOptionPane.showMessageDialog(null,"under construction");	
		}//end actionPerformed
   }//end deleteActionListener
   
}// end class
