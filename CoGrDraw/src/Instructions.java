import java.awt.*;
import javax.swing.*;
public class Instructions extends JPanel{
	/** constructor */
	public Instructions(){
		/** sets up the gridlayout */
		super(new GridLayout(40, 1));
		
		/** creates the JLabels */
		JLabel label1 = new JLabel("Instructions", JLabel.CENTER);
		JLabel label2 = new JLabel("Selection Tool - Toggles Selection Mode", JLabel.CENTER);
		JLabel label3 = new JLabel("Selection mode allows you to: ");
		JLabel label4 = new JLabel("  -Click and Drag on a shape to move it.");
		JLabel label5 = new JLabel("  -Click and Drag while holding control to rotate.");
		JLabel label6 = new JLabel("Line Tool - Toggles Line Draw mode", JLabel.CENTER);
		JLabel label7 = new JLabel("To draw a line: ");
		JLabel label8 = new JLabel("  -Click where you want the two endpoints of the line to be.");
		JLabel label9 = new JLabel("Circle Tool - Toggles Circle Draw mode", JLabel.CENTER);
		JLabel label10 = new JLabel("To draw a circle: ");
		JLabel label11 = new JLabel("  -Click first where you want the center to be.");
		JLabel label12 = new JLabel("  -Next, click the distance from the center that you want the radius to be");
		JLabel label13 = new JLabel("Ellipse Tool - Toggles Elipse Draw mode", JLabel.CENTER);
		JLabel label14 = new JLabel("To Draw an Ellipse: ");
		JLabel label15 = new JLabel("  -Click where you want the top left corner of the bounding box to be.");
		JLabel label16 = new JLabel("  -Next, click where you want the bottom left corner of the bounding box to be.");
		JLabel label17 = new JLabel("Rectangle Tool - Toggles Rectangle Draw mode", JLabel.CENTER);
		JLabel label18 = new JLabel("To Draw a Rectangle: ");
		JLabel label19 = new JLabel("  -Click where you want the top left vertex to be.");
		JLabel label20 = new JLabel("  -Next, click where you want the bottom right vertex to be.");
		JLabel label21 = new JLabel("Ruler Tool - Toggles Ruler mode", JLabel.CENTER);
		JLabel label22 = new JLabel("Measures the distance between two points.");
		JLabel label23 = new JLabel("Distance is displayed (in pixels) on the status bar.");
		JLabel label24 = new JLabel("Delete Tool - Toggles Delete mode", JLabel.CENTER);
		JLabel label25 = new JLabel("To Delete a shape, just click on it. ");
		JLabel label26 = new JLabel("If you click on multiple shapes, it will delete them all.");
		JLabel label27 = new JLabel("Color Tool", JLabel.CENTER);
		JLabel label28 = new JLabel("Allows you to change the color of the currently selected shape.");
		JLabel label29 = new JLabel("Also changes the color of future shapes.");
		JLabel label30 = new JLabel("Stroke Slider", JLabel.CENTER);
		JLabel label31 = new JLabel("Changes the stroke of the currently selected shape.");
		JLabel label32 = new JLabel("Fill Type", JLabel.CENTER);
		JLabel label33 = new JLabel("Changes the fill type of the currently selected shape.");
		JLabel label34 = new JLabel("Also changes the fill type for future shapes.");
		JLabel label35 = new JLabel("");
		JLabel label36 = new JLabel("Export saves the current frame as a .JPG");
		
		
		/** adds them to the panel */
		add(label1);
		add(label2);
		add(label3);
		add(label4);
		add(label5);
		add(label6);
		add(label7);
		add(label8);
		add(label9);
		add(label10);
		add(label11);
		add(label12);
		add(label13);
		add(label14);
		add(label15);
		add(label16);
		add(label17);
		add(label18);
		add(label19);
		add(label20);
		add(label21);
		add(label22);
		add(label23);
		add(label24);
		add(label25);
		add(label26);
		add(label27);
		add(label28);
		add(label29);
		add(label30);
		add(label31);
		add(label32);
		add(label33);
		add(label34);
		add(label35);
		add(label36);
		
	}
	 /** creates and shows the panel */
	public JInternalFrame createAndShow() {
        /** Create and set up the window. */
        JInternalFrame frame = new JInternalFrame("Instructions");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        
        frame.putClientProperty("JInternalFrame.isPallete", Boolean.TRUE);

        /** Add content to the window. */
        frame.add(new Instructions());
        
        frame.setClosable(true);

        /** Display the window. */
        frame.pack();
        frame.setVisible(true);
        
        return frame;
    }
}
