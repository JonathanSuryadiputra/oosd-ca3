package purchases;

import javax.swing.JFrame;

public class SystemDriver {

	public static void main( String args[] ) {
		MainFrame o = new MainFrame();
		o.setExtendedState(JFrame.MAXIMIZED_BOTH);
		o.setLocationRelativeTo(null);
		o.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		o.setVisible(true);	

	}

}
