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

public class RecentPurchasesForm {
	
	private JPanel RecentPurchasesFormPanel = new JPanel();
	
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
   public RecentPurchasesForm() {
	   
	   getQuery();
	   
	   JLabel topicLabel = new JLabel("           Recently Purchases Table           ");
	   JButton refreshButton = new JButton("Refresh");
	   	
	  
	   refreshButtonHandler refreshHandler = new refreshButtonHandler();
	   refreshButton.addActionListener(refreshHandler);
	   
	   /* add components */
	   topPanel.add(topicLabel);
	   panel.add(jtablePanel);
	   bottomPanel.add(refreshButton);
	   RecentPurchasesFormPanel.add(topPanel);
	   RecentPurchasesFormPanel.add(panel);
	   RecentPurchasesFormPanel.add(bottomPanel);   
	   
	   /* set Style */
	   Font topicFont = new Font("Serif",Font.BOLD,25);
	   Font buttonFont = new Font("Serif",Font.BOLD,15);
	   Border formBorder = BorderFactory.createLineBorder(Color.LIGHT_GRAY, 3);
	   Dimension buttonSize = new Dimension(200,50);
	   
	   topicLabel.setFont(topicFont);
	   refreshButton.setFont(buttonFont);
	   refreshButton.setBackground(Color.LIGHT_GRAY);
	   refreshButton.setPreferredSize(buttonSize);

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
	   
	   RecentPurchasesFormPanel.setBackground(Color.WHITE);
   }
   
   
   public JPanel getJPanel() {
	   return RecentPurchasesFormPanel;
   }
   
   public void getQuery() {
	   
	   String[] columnNames = {"firstName", "lastName", "productName", "qtyProduct"};
	   
	   Connection connection = null;
	   Statement statement = null;
	   
	   try {
		   connection = DriverManager.getConnection( DATABASE_URL, UserName_SQL, Password_SQL );
		   statement = connection.createStatement();
		   ResultSet resultSet = statement.executeQuery("select firstName, lastName, productName, qtyProduct from customer inner join invoice on customer.customerId = invoice.customerId inner join product on invoice.productId = product.productId;");
		   
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
   
//   ---------------------------------------------------Refresh Button-----------------------------------------------------------------
   private class refreshButtonHandler implements ActionListener {

	   	@Override
		public void actionPerformed(ActionEvent e) {
	   	getQuery();
	   	getJPanel();
	   	}// end actionPerformed	
   }//end ActionListener
   
}// end class