package purchases;

import javax.swing.JFrame;

/**
 * This class accesses the other frames as a driver class
 * @author Weihao, Jonathan, Chi
 *
 */
public class SystemDriver {
	
	/**
	 * 
	 * @param args -String main
	 */
	public static void main( String args[] ) {
		MainFrame o = new MainFrame();
		o.setExtendedState(JFrame.MAXIMIZED_BOTH);
		o.setLocationRelativeTo(null);
		o.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		o.setVisible(true);	

	}

}
