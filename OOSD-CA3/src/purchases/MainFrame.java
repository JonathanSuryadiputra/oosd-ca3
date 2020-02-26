package purchases;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

/**
 * This class is the main menu frame for the purchase system
 * @author Weihao, Jonathan, Chi
 *
 */
public class MainFrame extends JFrame {

	private JPanel topPanel = new JPanel();
	private JPanel mainPanel = new JPanel();
	private JPanel contentPanel = new JPanel();
	private JPanel alterPanel = new JPanel();

	private JPanel queryCustomerPanel = new JPanel();
	private JPanel queryProductPanel = new JPanel();
	private JPanel queryInvoicePanel = new JPanel();
	private JPanel purchaseRecordPanel = new JPanel();

	private CardLayout cardlayout = new CardLayout();

	final String[] ComboBoxString = new String[] { "Select Option", "Query Customer Table", "Query Product Table",
			"Query Invoice Table", "Purchases Record Table" };
	final JComboBox<String> comboBox = new JComboBox<>(ComboBoxString);

	/**
	 * Main Structure of System
	 */
	public MainFrame() { /* full screen: max-width: 1360, max-height: 841 */

		/* JFrame setup */
		setPanel();
		setTitle("Purchase System Demo : OOSD Y2 Project");

		/* variable */
		Font topicFont = new Font("Calibri", Font.BOLD, 22);
		Font comboBoxFont = new Font("Calibri", Font.PLAIN, 18);
		Font someTextFont = new Font("Calibri", Font.BOLD, 30);
		Dimension comboBoxSize = new Dimension(500, 30);
		final JLabel topic = new JLabel("Purchases System");

		JLabel imageLabel = new JLabel();
		ImageIcon image = new ImageIcon(getClass().getResource("imageIcon.png"));
		imageLabel.setIcon(image);
		imageLabel.setHorizontalTextPosition(SwingConstants.CENTER);
		imageLabel.setVerticalTextPosition(SwingConstants.BOTTOM);

		/* comboBox */
		comboBoxHandler selection = new comboBoxHandler();
		comboBox.addActionListener(selection);

		/* CardLayout Selection */
		alterPanel.setLayout(cardlayout);
		alterPanel.add(mainPanel, "1");
		alterPanel.add(queryCustomerPanel, "2");
		alterPanel.add(queryProductPanel, "3");
		alterPanel.add(queryInvoicePanel, "4");
		alterPanel.add(purchaseRecordPanel, "5");

		/* add Components */
		topPanel.add(topic);
		topPanel.add(comboBox);
		mainPanel.add(contentPanel);
		contentPanel.add(imageLabel);

		/* Set Style */
		topic.setFont(topicFont);

		comboBox.setPreferredSize(comboBoxSize);
		comboBox.setFont(comboBoxFont);

		topPanel.setLayout(new FlowLayout(1, 100, 10));
		topPanel.setPreferredSize(new Dimension(1360, 50));
		topPanel.setBackground(Color.lightGray);

		mainPanel.setLayout(new FlowLayout(1, 0, 0));
		mainPanel.setBackground(Color.WHITE);

		contentPanel.setPreferredSize(new Dimension(1200, 700));
		contentPanel.setBackground(Color.WHITE);

		/* ContentPane */
		getContentPane().add(topPanel, BorderLayout.NORTH);
		getContentPane().add(alterPanel, BorderLayout.CENTER);

	}// end main
	
	/**
	 * get the panel from other class
	 */
	private void setPanel() { /* set this JPanel equal to other class JPanel */

		QueryCustomerForm queryCustomerObj = new QueryCustomerForm();
		queryCustomerPanel = queryCustomerObj.getJPanel();
		QueryProductForm queryProductObj = new QueryProductForm();
		queryProductPanel = queryProductObj.getJPanel();
		QueryInvoiceForm queryInvoiceObj = new QueryInvoiceForm();
		queryInvoicePanel = queryInvoiceObj.getJPanel();
		PurchasesRecordForm purchasesRecord = new PurchasesRecordForm();
		purchaseRecordPanel = purchasesRecord.getJPanel();

	}
	
	/**
	 * Define the action of combo Box
	 */
	private class comboBoxHandler implements ActionListener {
		public void actionPerformed(ActionEvent e) {

			String s = (String) comboBox.getSelectedItem();
			switch (s) {
			case "Query Customer Table":
				cardlayout.show(alterPanel, "2");
				break;
			case "Query Product Table":
				cardlayout.show(alterPanel, "3");
				break;
			case "Query Invoice Table":
				cardlayout.show(alterPanel, "4");
				break;
			case "Purchases Record Table":
				cardlayout.show(alterPanel, "5");
				break;

			}// end switch
		}// end actionPerformed
	}// end comboBoxHandler

}// end class
