import java.awt.*;
import javax.swing.*;
public class About extends JPanel{

	public About(){
		/** sets the grid layout */
		super(new GridLayout(4, 1));
		
		/** creates the labels */
		JLabel label1 = new JLabel("About:", JLabel.CENTER);
		JLabel label2 = new JLabel("Authors: Kyle Billemeyer, Phil Quinn", JLabel.CENTER);
		JLabel label3 = new JLabel("Version: 1.0", JLabel.CENTER);
		JLabel label4 = new JLabel("Last Update: 2/24/2010", JLabel.CENTER);
		
		/** adds the labels */
		add(label1);
		add(label2);
		add(label3);
		add(label4);
	}
	
	/** creates and shows the frame */
	public JInternalFrame createAndShow() {
        /** Create and set up the window. */
        JInternalFrame frame = new JInternalFrame("About");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        
        frame.putClientProperty("JInternalFrame.isPallete", Boolean.TRUE);

        /** Add content to the window. */
        frame.add(new About());
        
        frame.setClosable(true);

        /** Display the window. */
        frame.pack();
        frame.setVisible(true);
        
        return frame;
    }
}
