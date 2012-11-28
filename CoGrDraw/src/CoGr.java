/*
 * Drawing program.
 * @author Phil Quinn & Kyle Billemeyer
 */

import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;
import java.util.ArrayList;
import java.util.Random;
import javax.swing.*;

import javax.swing.*; 
import javax.swing.event.*; 
import javax.swing.text.*; 
import javax.swing.border.*; 
import javax.swing.colorchooser.*; 
import javax.swing.filechooser.*; 
import javax.accessibility.*; 

import java.awt.*; 
import java.awt.event.*; 
import java.beans.*; 
import java.util.*; 
import java.io.*; 
import java.applet.*; 
import java.net.*; 

import javax.swing.JInternalFrame;
import javax.swing.JDesktopPane;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JMenuBar;
import javax.swing.JFrame;
import javax.swing.KeyStroke;
import javax.swing.RootPaneContainer;

import java.awt.event.*;
import java.awt.*;


public class CoGr extends JFrame implements ActionListener,
											ChangeListener,
											MouseListener,
											MouseMotionListener,
											ItemListener{
	
	/** frame lock boolean **/
    boolean frameLock = false;
    /** shape fill **/
    boolean filled = false;
    /** internal frame width **/
    int WIDTH = 400;
    /** internal frame height **/
    int HEIGHT = 400;
    /** x offset of JInternalFrame and inner JPanel **/
    int OFFSET_X = 5;
    /** x offset of JInternalFrame and inner JPanel **/
    int OFFSET_Y = 29;
    /** shape color **/
    Color shapeColor;
    /** stroke float **/
    float STROKE = 10;
    /** main window **/
    JDesktopPane desktop;
    /** cursor **/
    Cursor cursor;
    /** clicked variable for shape modes **/
    boolean clicked = false;
    /** stored click point for tools **/
    Point2D click = null;
    /** status bar that is displayed  **/
    boolean pressedCtrl = false;
    static StatusBar statusBar = new StatusBar();
    /** toolbar **/
    JInternalFrame toolbar = new JInternalFrame("");
    
    int printNum = 0;
    
    /** a global variable for the currently selected shape */
    ShapeHolder selected = null;
    /** an array list of shape holders that 
     * represents all the currently selected shapes
     */
    ArrayList<ShapeHolder> selectMult = new ArrayList<ShapeHolder>();
    /** the index of the currently selected shape */
    int selectIndex = 0;
    /** the array list of the copied shapes */
    ArrayList<ShapeHolder> copy = new ArrayList<ShapeHolder>();
    /** the drilled shape */
    ShapeHolder drilled = null;
    
    /** desktop layer that the toolbar is on **/
    public Integer TOOLBAR_LAYER = new Integer(2);
    /** desktop layer that the frames are on **/
    public Integer FRAME_LAYER = new Integer(1);
    
    /** dimension that represents the size of the users screen **/
    Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
    
    /** toggle button declarations
     *  We had to make this global because we needed a method to toggle
     *  the buttons off.
     */
    JToggleButton selectionTool = new JToggleButton();
    JToggleButton lineButton = new JToggleButton();
    JToggleButton circleButton = new JToggleButton();
    JToggleButton ellipseButton = new JToggleButton();
    JToggleButton rectButton = new JToggleButton();
    JToggleButton rulerButton = new JToggleButton();
    JToggleButton deleteButton = new JToggleButton();
    
    /** variables for the select tool **/
	protected AffineTransform navXForm = new AffineTransform();
	protected ArrayList<ShapeHolder> dragShape = new ArrayList<ShapeHolder>();
	protected int lastX, lastY;
	protected boolean navigating = false;
	
	/** controls the amount of degrees object rotate per pixel dragged*/
	float ROT_DEG_PER_PIXEL = 1.0f;
	
    /** default constructor */
    public CoGr(){
    	/** Names the window */
    	super("Desktop");
    	/** set the inset */
    	int inset = 30;
    	/** sets the size of the main frame */
    	setBounds(inset, 
    			  inset, 
    			  screenSize.width - inset*2, 
    			  screenSize.height - inset*2);
    	
    	/** creates the main frame */
    	desktop = new JDesktopPane();
    	/** creates one internal frame on the frame layer */
    	try {
            createFrame(FRAME_LAYER);
        } catch (PropertyVetoException e) {
            e.printStackTrace();
        }
    	/** sets the content pane to the main window */
    	setContentPane(desktop);
    	/** sets the menu bar of the main frame */
    	setJMenuBar(createMenuBar());
    	/** adds the listeners */
    	desktop.addMouseListener(this);
    	desktop.addMouseMotionListener(this);
    	
    	/** sets the drag mode of the main window */
    	desktop.setDragMode(JDesktopPane.OUTLINE_DRAG_MODE);
    }
    /** creates the menu bar for the main window */
    protected JMenuBar createMenuBar(){
    	/** instantiate a new JMenuBar */
    	JMenuBar menuBar = new JMenuBar();
    	
    	/** creates the file sub menu */
    	JMenu file = new JMenu("File");
    	/** adds the mnemonic to the file sub menu */
    	file.setMnemonic(KeyEvent.VK_F);
    	/** adds the file sub menu to the Menu Bar */
    	menuBar.add(file);
    	
    	/** creates the new menu item */
    	JMenuItem menuItem = new JMenuItem("New");
    	/** sets the mnemonic */
    	menuItem.setMnemonic(KeyEvent.VK_N);
    	/** adds the alt mask to the menu item */
    	menuItem.setAccelerator(KeyStroke.getKeyStroke(
    			KeyEvent.VK_N, ActionEvent.ALT_MASK));
    	/** sets the action command */
    	menuItem.setActionCommand("new");
    	/** adds the action listener */
    	menuItem.addActionListener(this);
    	/** adds the menu item to the file sub menu */
    	file.add(menuItem);
    	
    	/** creates the save menu item */
    	menuItem = new JMenuItem("Export");
    	/** sets the mnemonic */
    	menuItem.setMnemonic(KeyEvent.VK_S);
    	/** adds the alt mask to the menu item */
    	menuItem.setAccelerator(KeyStroke.getKeyStroke(
    			KeyEvent.VK_S, ActionEvent.ALT_MASK));
    	/** sets the action command */
    	menuItem.setActionCommand("export");
    	/** adds the action listener */
    	menuItem.addActionListener(this);
    	/** adds the menu item to the file sub menu */
    	file.add(menuItem);
    	
    	/** creates the quit menu item */
    	menuItem = new JMenuItem("Quit");
    	/** sets the mnemonic */
    	menuItem.setMnemonic(KeyEvent.VK_Q);
    	/** adds the alt mask to the menu item */
    	menuItem.setAccelerator(KeyStroke.getKeyStroke(
    			KeyEvent.VK_Q, ActionEvent.ALT_MASK));
    	/** sets the action command */
    	menuItem.setActionCommand("quit");
    	/** adds the action listener */
    	menuItem.addActionListener(this);
    	/** adds the menu item to the file sub menu */
    	file.add(menuItem);
    	
    	/** creates a new sub menu */
    	JMenu options = new JMenu("Options");
    	/** sets the mnemonic */
    	options.setMnemonic(KeyEvent.VK_O);
    	/** adds the sub menu to the menu bar */
    	menuBar.add(options);
    	
    	/** creates the frame size menu item */
    	menuItem = new JMenuItem("Frame Size");
    	/** sets the mnemonic */
    	menuItem.setMnemonic(KeyEvent.VK_I);
    	/** adds the alt mask */
    	menuItem.setAccelerator(KeyStroke.getKeyStroke(
    			KeyEvent.VK_I, ActionEvent.ALT_MASK));
    	/** sets the action command */
    	menuItem.setActionCommand("size");
    	/** adds the action listener */
    	menuItem.addActionListener(this);
    	/** adds the menu item to the options sub menu */
    	options.add(menuItem);
    	
    	/** creates the clear frame menu item */
    	menuItem = new JMenuItem("Clear Frame");
    	/** sets the mnemonic */
    	menuItem.setMnemonic(KeyEvent.VK_C);
    	/** adds the alt mask */
    	menuItem.setAccelerator(KeyStroke.getKeyStroke(
    			KeyEvent.VK_C, ActionEvent.ALT_MASK));
    	/** sets the action command */
    	menuItem.setActionCommand("clear");
    	/** adds the action listener */
    	menuItem.addActionListener(this);
    	/** adds the menu item to the options sub menu */
    	options.add(menuItem);
    	
    	/** creates the background color menu item */
    	menuItem = new JMenuItem("Background Color");
    	/** sets the mnemonic */
    	menuItem.setMnemonic(KeyEvent.VK_B);
    	/** adds the alt mask */
    	menuItem.setAccelerator(KeyStroke.getKeyStroke(
    			KeyEvent.VK_B, ActionEvent.ALT_MASK));
    	/** sets the action command */
    	menuItem.setActionCommand("backColor");
    	/** adds the action listener */
    	menuItem.addActionListener(this);
    	/** adds the menu item to the options sub menu */
    	options.add(menuItem);
    	
    	/** creates the Cut menu item */
    	menuItem = new JMenuItem("Cut");
    	/** adds the ctrl mask */
    	menuItem.setAccelerator(KeyStroke.getKeyStroke(
    			KeyEvent.VK_X, ActionEvent.CTRL_MASK));
    	/** sets the action command */
    	menuItem.setActionCommand("cut");
    	/** adds the action listener */
    	menuItem.addActionListener(this);
    	/** adds the menu item to options sub menu */
    	options.add(menuItem);
    	
    	/** creates the copy menu item */
    	menuItem = new JMenuItem("Copy");
    	/** adds the ctrl mask */
    	menuItem.setAccelerator(KeyStroke.getKeyStroke(
    			KeyEvent.VK_C, ActionEvent.CTRL_MASK));
    	/** sets the action command */
    	menuItem.setActionCommand("copy");
    	/** adds the action listener */
    	menuItem.addActionListener(this);
    	/** adds the menu item to options sub menu */
    	options.add(menuItem);
    	
    	/** creates the paste menu item */
    	menuItem = new JMenuItem("Paste");
    	/** adds the ctrl mask */
    	menuItem.setAccelerator(KeyStroke.getKeyStroke(
    			KeyEvent.VK_V, ActionEvent.CTRL_MASK));
    	/** sets the action command */
    	menuItem.setActionCommand("paste");
    	/** adds the action listener */
    	menuItem.addActionListener(this);
    	/** adds the menu item to options sub menu */
    	options.add(menuItem);
    	
    	/** creates the duplicate menu item */
    	menuItem = new JMenuItem("Drill");
    	/** adds the ctrl mask */
    	menuItem.setAccelerator(KeyStroke.getKeyStroke(
    			KeyEvent.VK_SPACE, ActionEvent.CTRL_MASK));
    	/** sets the action command */
    	menuItem.setActionCommand("drill");
    	/** adds the action listener */
    	menuItem.addActionListener(this);
    	/** adds the menu item to options sub menu */
    	options.add(menuItem);
    	
    	/** creates the help sub menu */
    	JMenu help = new JMenu("Help");
    	/** sets the mnemonic */
    	help.setMnemonic(KeyEvent.VK_H);
    	/** adds the sub menu to the menu bar */
    	menuBar.add(help);
    	
    	/** creates the instructions menu item */
    	menuItem = new JMenuItem("Instructions");
    	/** sets the mnemonic */
    	menuItem.setMnemonic(KeyEvent.VK_C);
    	/** adds the alt mask */
    	menuItem.setAccelerator(KeyStroke.getKeyStroke(
    			KeyEvent.VK_C, ActionEvent.ALT_MASK));
    	/** sets the action command */
    	menuItem.setActionCommand("inst");
    	/** adds the action listener */
    	menuItem.addActionListener(this);
    	/** adds the menu item to the help sub menu */
    	help.add(menuItem);
    	
    	/** creates the instructions menu item */
    	menuItem = new JMenuItem("About");
    	/** sets the mnemonic */
    	menuItem.setMnemonic(KeyEvent.VK_U);
    	/** adds the alt mask */
    	menuItem.setAccelerator(KeyStroke.getKeyStroke(
    			KeyEvent.VK_U, ActionEvent.ALT_MASK));
    	/** sets the action command */
    	menuItem.setActionCommand("about");
    	/** adds the action listener */
    	menuItem.addActionListener(this);
    	/** adds the menu item to the help sub menu */
    	help.add(menuItem);
    	
    	return menuBar;
    }
    
    /** handles action events */
    public void actionPerformed(ActionEvent e){
    	/** takes the action command and performs the appropriate action */
    	if("new".equals(e.getActionCommand())){
    		try {
                createFrame(MyInternalFrame.frameCount);
            } catch (PropertyVetoException e1) {
                e1.printStackTrace();
            }
    	} else if ("quit".equals(e.getActionCommand())){
    		quit();
    	} else if ("size".equals(e.getActionCommand())){
    		changeFrameSize();
    	} else if ("selectTool".equals(e.getActionCommand())){
    		toggleSelectionMode();
    	} else if ("drawLine".equals(e.getActionCommand())){
    		if(lineButton.isSelected()){
    			toggleAllModesFalse();
           		lineButton.setSelected(!lineButton.isSelected());
    		} else
    			lineButton.setSelected(false);
    	} else if ("drawCircle".equals(e.getActionCommand())){
    		if(circleButton.isSelected()){
    			toggleAllModesFalse();
    			circleButton.setSelected(!circleButton.isSelected());
    		} else
    			circleButton.setSelected(false);
    	} else if ("drawEllipse".equals(e.getActionCommand())){
    		if(ellipseButton.isSelected()){
    			toggleAllModesFalse();
    			ellipseButton.setSelected(!ellipseButton.isSelected());
    		} else
    			ellipseButton.setSelected(false);
    	} else if ("drawRect".equals(e.getActionCommand())){
    		if(rectButton.isSelected()){
            	toggleAllModesFalse();
            	rectButton.setSelected(true);
    		} else
    			rectButton.setSelected(false);
    	} else if ("delete".equals(e.getActionCommand())){
    		if(deleteButton.isSelected()){
    			toggleAllModesFalse();
    			deleteButton.setSelected(true);
    		} else
    			deleteButton.setSelected(false);
    	} else if ("ruler".equals(e.getActionCommand())){
    		if(rulerButton.isSelected()){
    			toggleAllModesFalse();
    			rulerButton.setSelected(true);
    		} else
    			rulerButton.setSelected(false);
    	} else if ("shapeColor".equals(e.getActionCommand())){
    		shapeColor = JColorChooser.showDialog(this, 
    				"Choose Shape Color", shapeColor);
    		if(selected != null){
    			selected.setColor(shapeColor);
    		}
    	} else if ("backColor".equals(e.getActionCommand())){
    		changeFrameBackgroundColor();
    	} else if ("clear".equals(e.getActionCommand())){
    		clearFrame();
    	} else if ("export".equals(e.getActionCommand())){
    		double x = getCurrentFrame().getWidth();
    		double y = getCurrentFrame().getHeight();
    		getCurrentFrame().export(x, y, "CoGrPrint" + printNum);
    		printNum++;
    	} else if("inst".equals(e.getActionCommand())){
    		showInstructions();
    	} else if("about".equals(e.getActionCommand())){
    		showAbout();
    	} else if("cut".equals(e.getActionCommand())){
    		cut();
    	} else if("copy".equals(e.getActionCommand())){
    		copy();
    	} else if("paste".equals(e.getActionCommand())){
    		paste();
    	} else if("drill".equals(e.getActionCommand())){
    		if(selectMult.size() > 1)
    			drilled = selectMult.get(0);
    	}
    }
    
    /** quits the program */
    protected void quit(){
    	int response = JOptionPane.showOptionDialog(desktop, 
				"Are you sure you want to close the program?",
				"Are you sure?",
				JOptionPane.YES_NO_CANCEL_OPTION,
				JOptionPane.INFORMATION_MESSAGE, null, null, null);
    	if(response == 0)
    		System.exit(0);
    }
    
    /** creates the toolbar */
    public JInternalFrame createToolbar(){
    	
    	/** creates the toolbar */
    	toolbar.putClientProperty("JInternalFrame.isPallete", Boolean.TRUE);
    	toolbar.getContentPane().setLayout(new BorderLayout());
    	toolbar.setBounds(screenSize.width - 169, 0, 100, 500);
    	toolbar.setResizable(false);
    	toolbar.setIconifiable(true);
    	desktop.add(toolbar, TOOLBAR_LAYER);
    	toolbar.show();
    	
    	
    	/**adds the buttons and sets the action commands/listeners */
    	ImageIcon selectionIcon = createImageIcon("images/hand.GIF");
    	selectionTool.setIcon(selectionIcon);
    	selectionTool.setActionCommand("selectTool");
    	selectionTool.setToolTipText("Selection Tool");
    	selectionTool.addActionListener(this);
    	
    	ImageIcon lineIcon = createImageIcon("images/line.GIF");
    	lineButton.setIcon(lineIcon);
    	lineButton.setActionCommand("drawLine");
    	lineButton.setToolTipText("Line Tool");
    	lineButton.addActionListener(this);
    	
    	ImageIcon circleIcon = createImageIcon("images/circle.GIF");
    	circleButton.setIcon(circleIcon);
    	circleButton.setActionCommand("drawCircle");
    	circleButton.setToolTipText("Circle Tool");
    	circleButton.addActionListener(this);
    	
    	ImageIcon ellipseIcon = createImageIcon("images/ellipse.GIF");
    	ellipseButton.setIcon(ellipseIcon);
    	ellipseButton.setActionCommand("drawEllipse");
    	ellipseButton.setToolTipText("Ellipse Tool");
    	ellipseButton.addActionListener(this);
    	
    	ImageIcon rectIcon = createImageIcon("images/rectangle.GIF");
    	rectButton.setIcon(rectIcon);
    	rectButton.setActionCommand("drawRect");
    	rectButton.setToolTipText("Rectangle Tool");
    	rectButton.addActionListener(this);
    	
    	ImageIcon rulerIcon = createImageIcon("images/ruler.GIF");
    	rulerButton.setIcon(rulerIcon);
    	rulerButton.setActionCommand("ruler");
    	rulerButton.setToolTipText("Ruler Tool");
    	rulerButton.addActionListener(this);
    	
    	ImageIcon deleteIcon = createImageIcon("images/delete.GIF");
    	deleteButton.setIcon(deleteIcon);
    	deleteButton.setActionCommand("delete");
    	deleteButton.setToolTipText("Delete Tool");
    	deleteButton.addActionListener(this);
    	
    	ImageIcon colorIcon = createImageIcon("images/color.GIF");
    	JButton colorButton = new JButton(colorIcon);
    	colorButton.setActionCommand("shapeColor");
    	colorButton.setToolTipText("Color Tool");
    	colorButton.addActionListener(this);
    	
    	/** creates the stroke slider */
    	JLabel strokeLabel = new JLabel("Stroke", JLabel.CENTER);
    	strokeLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
    	JSlider strokeSlider = new JSlider(JSlider.HORIZONTAL, 0, 20, 10);
    	strokeSlider.addChangeListener(this);
    	Font font = new Font("Serif", Font.ITALIC, 15);
    	strokeSlider.setFont(font);
    	strokeSlider.setMajorTickSpacing(10);
    	strokeSlider.setMinorTickSpacing(2);
    	strokeSlider.setPaintTicks(true);
    	strokeSlider.setPaintLabels(true);
    	
    	/** creates the fill check box */
    	JCheckBox fill = new JCheckBox("Filled");
    	fill.setSelected(false);
    	fill.addItemListener(this);
    	
    	/** adds all the items to the pane */
    	Container pane = toolbar.getContentPane();
    	pane.setLayout(new BoxLayout(pane, BoxLayout.Y_AXIS));
    	
    	selectionTool.setAlignmentX(Component.CENTER_ALIGNMENT);
    	pane.add(selectionTool);
    	lineButton.setAlignmentX(Component.CENTER_ALIGNMENT);
    	pane.add(lineButton);
    	circleButton.setAlignmentX(Component.CENTER_ALIGNMENT);
    	pane.add(circleButton);
    	ellipseButton.setAlignmentX(Component.CENTER_ALIGNMENT);
    	pane.add(ellipseButton);
    	rectButton.setAlignmentX(Component.CENTER_ALIGNMENT);
    	pane.add(rectButton);
    	rulerButton.setAlignmentX(Component.CENTER_ALIGNMENT);
    	pane.add(rulerButton);
    	deleteButton.setAlignmentX(Component.CENTER_ALIGNMENT);
    	pane.add(deleteButton);
    	colorButton.setAlignmentX(Component.CENTER_ALIGNMENT);
    	pane.add(colorButton);
    	pane.add(strokeLabel);
    	strokeLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
    	pane.add(strokeSlider);
    	strokeSlider.setAlignmentX(Component.CENTER_ALIGNMENT);
    	pane.add(fill);
    	fill.setAlignmentX(Component.CENTER_ALIGNMENT);
    	
    	return toolbar;
    	
    }
    
    /** handles the changes to the fill button */
    public void itemStateChanged(ItemEvent e) {
    	boolean empty = selectMult.isEmpty();
    	if(e.getStateChange() == ItemEvent.DESELECTED){
    		filled = false;
    		if(!empty){
    			for(ShapeHolder s : selectMult){
    				s.setFilled(filled);
    			}
    		}
    	} else { 
    		filled = true;
    		if(!empty){
    			for(ShapeHolder s : selectMult){
    				s.setFilled(filled);
    			}
    		}
    	}
    	repaint();
    }
    
    /** handles the changes to the slider  */
    public void stateChanged(ChangeEvent e){
    	JSlider source = (JSlider)e.getSource();
    	if(!source.getValueIsAdjusting()){
    		STROKE = (float)source.getValue();
    		if(!selectMult.isEmpty()){
    			for(ShapeHolder s : selectMult){
    				s.setStroke(new BasicStroke(STROKE));
    			}
    		}
    	}
    	repaint();
    }
    
    /** creates the status bar */
    public void createStatusBar(){
    	StatusBar statusBar = new StatusBar();
    	getContentPane().add(statusBar, java.awt.BorderLayout.SOUTH);
    }
    
    /** creates a new frame */
    public void createFrame(Integer layer) throws java.beans.PropertyVetoException{
        MyInternalFrame tempFrame 
        	= new MyInternalFrame(new ArrayList<ShapeHolder>());
         
        tempFrame.setClosable(true);
        tempFrame.setMaximizable(true);
        tempFrame.setIconifiable(true);
        tempFrame.setResizable(true);
        
        tempFrame.changeBackgroundColor(Color.WHITE);
        
        tempFrame.setBounds(20*(MyInternalFrame.frameCount%10), 
        					20*(MyInternalFrame.frameCount%10), 
        					WIDTH, 
        					HEIGHT);
        //tempFrame.pack();
        
        desktop.add(tempFrame, layer);
        
        tempFrame.addMouseMotionListener(this);
        tempFrame.addMouseListener(this);
        
        tempFrame.setVisible(true);
        
        tempFrame.grabFocus();
    }
    
    /** toggles the selection mode cursor */
    public void toggleSelectionMode(){
    	Cursor hand = Cursor.getPredefinedCursor(Cursor.HAND_CURSOR);
    	Cursor def = Cursor.getDefaultCursor();
    	if(selectionTool.isSelected()){
    		toggleAllModesFalse();
    		selectionTool.setSelected(true);
    		desktop.getSelectedFrame().setCursor(hand);
			setCursor(hand);
    	} else {
    		selectionTool.setSelected(false);
    		desktop.getSelectedFrame().setCursor(def);
    		setCursor(def);
    	}
		
    }	
    
    /** changes the background color of the currently selected frame
     *  shows an error dialog if no frame is selected of if its the toolbar
     */
    public void changeFrameBackgroundColor(){
    	if(desktop.getSelectedFrame() == null || desktop.getSelectedFrame() == toolbar){
    		JOptionPane.showMessageDialog(desktop, "No Frame Is Selected.",
    				"Error", JOptionPane.ERROR_MESSAGE);
    	} else {
    		Color frameBackgroundColor = JColorChooser.showDialog(this,
    				"Choose Background Color",
    				getBackground());
    		getCurrentFrame().changeBackgroundColor(frameBackgroundColor);
    	}
    }
    
    /** changes the width and height of future frames */
    public void changeFrameSize(){
    	String newWidth = JOptionPane.showInputDialog("Input a new Width:");
    	WIDTH = Integer.parseInt(newWidth);
    	String newHeight = JOptionPane.showInputDialog("Input a new Height:");
    	HEIGHT = Integer.parseInt(newHeight);
    	//getCurrentFrame().setPreferredSize(new Dimension(WIDTH, HEIGHT));
    }
    
    /** makes an ImageIcon from the specified path */
    protected static ImageIcon createImageIcon(String path) {
    	java.net.URL imgURL = CoGr.class.getResource(path);
    	if (imgURL != null) {
            return new ImageIcon(imgURL);
        } else {
            System.err.println("Couldn't find file: " + path);
            return null;
        }
    }
    
    /** handles events when the mouse is clicked */
	public void mouseClicked(MouseEvent e) {}
	
	/** adds a line to the current frame's array of shapes */
	public void drawLine(Point2D p){
		/** reset selected shapes */
		getCurrentFrame().deselectAll();
		selected = null;
		
		/** creates vector components of stored point and new click point */
		double x = p.getX() - this.click.getX();
		double y = p.getY() - this.click.getY();
		MyInternalFrame frame = getCurrentFrame();
		Point2D p2 = new Point2D.Double(x, y);
		
		/** set local frame to the origin of the the frame */
		Shape tempShape = new Line2D.Double(new Point2D.Double(0, 0), p2 );
		String tempName = "shape" + frame.getCount();
		BasicStroke tempStroke = new BasicStroke(STROKE);
		
		/** translate to the first click */
		ShapeHolder tempSH = new ShapeHolder(tempName, 
											 tempStroke,
											 shapeColor,
											 tempShape, 
											 filled, 
											 AffineTransform.getTranslateInstance(
													 this.click.getX(), 
													 this.click.getY()));
		frame.addShape(tempSH);
		
		clicked = false;
		removeFirstClick();
	}
	
	/** adds a circle to the current frame's array of shapes */
	public void drawCircle(Point2D p){
		/** reset selected shapes */
		getCurrentFrame().deselectAll();
		selectMult = new ArrayList<ShapeHolder>();
		selected = null;
		MyInternalFrame frame = getCurrentFrame();
		
		/** sets radius to the distance between stored and new clicks */
		double dist = p.distance(this.click);
		
		/** set center of circle to origin of frame */
		Shape tempShape = new Ellipse2D.Double(-dist, -dist, 2 * dist, 2 * dist);
		String tempName = "shape" + frame.getCount();
		BasicStroke tempStroke = new BasicStroke(STROKE);
		
		/** translate to stored point */
		ShapeHolder tempSH = new ShapeHolder(tempName, 
											 tempStroke,
											 shapeColor,
											 tempShape,
											 filled,
											 AffineTransform.getTranslateInstance(
													 this.click.getX(), 
													 this.click.getY()));
		frame.addShape(tempSH);
		
		clicked = false;
		removeFirstClick();
	}
	
	/** adds a circle to the current window to represent
	 *  the first click
	 */
	public void drawCircle(Point2D p, int r){
		Shape tempShape = new Ellipse2D.Double(p.getX(), p.getY(), r / 2, r / 2);
		ShapeHolder tempSH = new ShapeHolder("click", new BasicStroke(1),
				Color.black, tempShape, true, new AffineTransform());
		MyInternalFrame frame = getCurrentFrame();
		frame.addShape(tempSH);
	}
	
	/** adds an ellipse to the current frame's array of shapes */
	public void drawEllipse(Point2D p){
		/** reset selected shapes */
		getCurrentFrame().deselectAll();
		selectMult = new ArrayList<ShapeHolder>();
		selected = null;
		
		MyInternalFrame frame = getCurrentFrame();
	
		/** width and height are equal to the dx, dy of the two clicks */
		double x = p.getX() - this.click.getX();
		double y = p.getY() - this.click.getY();
		
		/** set center to origin of the frame */
		Shape tempShape = new Ellipse2D.Double(-x / 2, -y / 2, x, y);
		String tempName = "shape" + frame.getCount();
		BasicStroke tempStroke = new BasicStroke(STROKE);
		
		/** Translate to the stored point */
		ShapeHolder tempSH = new ShapeHolder(tempName, 
											 tempStroke,
											 shapeColor, 
											 tempShape,
											 filled, 
											 AffineTransform.getTranslateInstance(
													 this.click.getX() + x / 2,
													 this.click.getY() + y / 2));
		frame.addShape(tempSH);
		
		clicked = false;
		removeFirstClick();
	}
	
	/** adds a rectangle to the current frame's array of shapes */
	public void drawRect(Point2D p){
		/** reset selected shapes */
		getCurrentFrame().deselectAll();
		selectMult = new ArrayList<ShapeHolder>();
		selected = null;
		
		MyInternalFrame frame = getCurrentFrame();
		
		/** set center to origin of the frame */
		double x = p.getX() - this.click.getX();
		double y = p.getY() - this.click.getY();
		Shape tempShape = new Rectangle2D.Double(-x / 2, -y / 2, x, y);
		String tempName = "shape" + frame.getCount();
		BasicStroke tempStroke = new BasicStroke(STROKE);
		
		/** Translate to the stored point */
		ShapeHolder tempSH = new ShapeHolder(tempName, tempStroke,
				shapeColor, tempShape, filled, 
				AffineTransform.getTranslateInstance(this.click.getX() + x / 2, 
						this.click.getY() + y / 2));
		frame.addShape(tempSH);
		
		clicked = false;
		removeFirstClick();
	}	
	
	/** checks if a pt is in a frame on the desktop */
	public boolean frameContainsPt(Point2D p){
	    for(Component c : desktop.getComponents()){
	        if(c.contains((Point) p)){
	            return true;
	        }
	    }
	    return false;
	}

	/** unused mouse handler */
	public void mouseEntered(MouseEvent e) {}
	
	/** handles the event when the mouse leaves the window */
	public void mouseExited(MouseEvent arg0) {
		dragShape =  new ArrayList<ShapeHolder>(); 
		navigating = false;
	}

	/** handles mouse pressed events */
	public void mousePressed(MouseEvent e) {
	    if(!frameContainsPt(e.getPoint())){ 
	        /** if there was a first mouse click for a multi-click
	        *	function, reset
	        */
	        if(getCurrentFrame().containsShape("click"))
                removeFirstClick();
	        this.clicked = false;
	        this.click = null;
	        
	        /** deselect all shapes and reset selected fields */
	        for(Component c : desktop.getComponents()){
	            if(c instanceof MyInternalFrame)
	                ((MyInternalFrame) c).deselectAll();
	        }
	        selectMult = new ArrayList<ShapeHolder>();
	        selected = null;
	    } else{
    		java.util.List<ShapeHolder> shapes =
    		getCurrentFrame().getShapeList();
    		
    		/** reset selected shapes */
    		getCurrentFrame().deselectAll();
    		selected = null;
    		selectMult = new ArrayList<ShapeHolder>();
    		
    		/** remove click representation if it exists */
    		if(getCurrentFrame().containsShape("click"))
    			removeFirstClick();
    		
    		/** account for frame/panel offset */
    		Point2D offsetPt = new Point2D.Double(e.getPoint().getX() - OFFSET_X,
                    e.getPoint().getY() - OFFSET_Y);
    		
    		/** drawLine if a click has already been stored, otherwise store
    		 *  the point
    		 */
    		if(lineButton.isSelected()){
    			if(clicked){
    				drawLine(offsetPt);
    			} else {
    				this.click = offsetPt;
    				this.clicked = true;
    				firstClick(this.click);
    			}
    			
    		/** drawCircle if a click has already been stored, otherwise store
    		*   the point
    		*/
    		} else if(circleButton.isSelected()){
    			if(clicked){
    				drawCircle(offsetPt);
    			} else {
    				this.click = offsetPt;
    				this.clicked = true;
    				firstClick(this.click);
    			}
    			
    		/** drawEllipse if a click has already been stored, otherwise store
    		*   the point
    		*/
    		} else if(ellipseButton.isSelected()){
    			if(clicked){
    				drawEllipse(offsetPt);
    			} else {
    				this.click = offsetPt;
    				this.clicked = true;
    				firstClick(this.click);
    			}
    		/** drawRect if a click has already been stored, otherwise store
    		 *  the point
    		 */
    		} else if(rectButton.isSelected()){
    			if(clicked){
    				drawRect(offsetPt);
    			} else {
    				this.click = offsetPt;
    				this.clicked = true;
    				firstClick(this.click);
    			}
    			
    		/** selection operations */
    		} else if(selectionTool.isSelected()){
    			selectMult = getCurrentFrame().selected(offsetPt);
    			boolean empty = selectMult.isEmpty();
    			if(!empty)
    				selected = selectMult.get(0);
    			if(!empty){
    				for(ShapeHolder s : selectMult){
    					s.setSelected(true);
    				}
    				System.out.println(selectMult.size());
    			}
    			
    			
    			/** store last point */
    			lastX = e.getX() - OFFSET_X;  //offset
    			lastY = e.getY() - OFFSET_Y; //offset
    			Point2D.Float p = new Point2D.Float(lastX, lastY);
    			
    			try {
    				navXForm.inverseTransform(p, p);
    			} catch (NoninvertibleTransformException ex) { assert(false); }
    			
    			synchronized(shapes){
    				for(Iterator<ShapeHolder> it
    						= shapes.iterator(); it.hasNext();){
    					ShapeHolder sh = it.next();
    					if(sh.containsPt(p)){
    						dragShape.add(sh);
    						break;
    					}
    				}
    			}
    		
    		/** ruler operations */
    		} else if(rulerButton.isSelected()){
    			if(clicked){ //find distance between the two clicks
    				Point2D current = offsetPt;
    				double dist = click.distance(current);
    				statusBar.setText("X: " + (e.getX() + OFFSET_X) + 
    						" Y : " + (e.getY() + OFFSET_Y +
    						" Distance : " + dist + " Pixels"));
    				clicked = false;
    				
    			} else {    //store point
    				this.click = offsetPt;
    				this.clicked = true;
    				firstClick(this.click);
    			}
    			
    		/** delete any shape this point is contained in */
    		} else if(deleteButton.isSelected()){
    			synchronized(shapes){
    				for(Iterator<ShapeHolder> it 
    						= shapes.iterator(); it.hasNext();){
    					ShapeHolder sh = it.next();
    					if(sh.containsPt(offsetPt)){
    						dragShape.add(sh);
    					}
    				}
    			}
    			for(ShapeHolder s : dragShape){
    				if(getCurrentFrame().containsShape(s.getName()))
    					getCurrentFrame().removeShape(s.getName());
    			}
    		}
    		if (!dragShape.isEmpty())
    			navigating = true;
    		repaint();
	    }
	}
	
	/** graphic representation of the first click in a multi-click operation */
	public void firstClick(Point2D p){
		drawCircle(p, 15);
	}
	
	/** remove the graphic representation of the first click in a 
	 *  multi-click operation
	*/
	public void removeFirstClick(){
		getCurrentFrame().removeShape("click");
	}
	
	/** mouse handler for mouse release */
	public void mouseReleased(MouseEvent e) {
		/** reset dragging information */
		dragShape =  new ArrayList<ShapeHolder>(); 
		navigating = false;
		repaint();
	}

	/** mouse handler for mouse dragging */
	public void mouseDragged(MouseEvent e) {
		if(selectionTool.isSelected()){
			int x = e.getX() - OFFSET_X;
			int y = e.getY() - OFFSET_Y;
					
			float dx = (x - lastX);
			float dy = (y - lastY);
			
			lastX = x;
			lastY = y;
			
			/** perform tranformation as per button presses */
			if(!selectMult.isEmpty()){
				if (e.isShiftDown()){
					for(ShapeHolder s : selectMult){
						s.rotate(Math.toRadians(dx*ROT_DEG_PER_PIXEL));
					}
				} else{
					for(ShapeHolder s : selectMult){
						s.translate(dx, dy);
					}
				}
				repaint();
			} else if(navigating){
				repaint();
			}
		}
	}

	/** mouse handler for mouse movement */
	public void mouseMoved(MouseEvent e) {
		statusBar.setText("X: " + (e.getX() - OFFSET_X) 
				        + " Y: " + (e.getY() - OFFSET_Y));	
	}
	
	/** deselect all toggle button */
	public void toggleAllModesFalse(){
		selectionTool.setSelected(false);
		lineButton.setSelected(false);
		circleButton.setSelected(false);
		ellipseButton.setSelected(false);
		rectButton.setSelected(false);
		rulerButton.setSelected(false);
		deleteButton.setSelected(false);
		
	}
	
	/** creates the instruction frame and shows it */
	public void showInstructions(){
		Instructions inst = new Instructions();
		desktop.add(inst.createAndShow(), TOOLBAR_LAYER);
	}
	
	/** creates the about frame and shows it */
	public void showAbout(){
		About about = new About();
		desktop.add(about.createAndShow(), TOOLBAR_LAYER);
	}
	/** clear the entire contents of a frame
	 *  presents confirmation options
	 */
	public void clearFrame(){
		int response = JOptionPane.showOptionDialog(desktop, 
					"Are you sure you want to clear the frame?",
					"Are you sure?",
					JOptionPane.YES_NO_CANCEL_OPTION,
					JOptionPane.INFORMATION_MESSAGE, null, null, null);
		if(response == 0)
			getCurrentFrame().clearShapes();
		repaint();
	}
	
	/** access this frame */
	public MyInternalFrame getCurrentFrame(){
	    return (MyInternalFrame) desktop.getSelectedFrame();
	}
	
	/** duplicates selected shape to be pasted in same position in any frame */
	public void copy(){
		if(!selectMult.isEmpty()){
			for(ShapeHolder s : selectMult){
				ShapeHolder temp = new ShapeHolder(s.getName() + " copy",
												   s.getStroke(),
												   s.getColor(),
												   s.getShape(),
												   s.getFilled(),
												   s.getAffineT());
				copy.add(temp);
			}
		}
	}

	/** duplicate selected shape to be pasted in same position in any frame
	 *  also deletes copied shape
	 */
	public void cut(){
		if(!selectMult.isEmpty()){
			for(ShapeHolder s : selectMult){
				ShapeHolder temp = new ShapeHolder(s.getName() + " copy",
												   s.getStroke(), 
												   s.getColor(),
												   s.getShape(),
												   s.getFilled(),
												   s.getAffineT());
				copy.add(temp);
				getCurrentFrame().removeShape(s.getName());
			}
			repaint();
		}
	}
	
	/** add copied or cut shape to the currently selected frame */
	public void paste(){
		for(ShapeHolder s : copy){
			getCurrentFrame().addShape(s);
		}
		repaint();
	}
	
	/** sends to front of ArrayList (drawn first) */
	public void sendToBack(ShapeHolder sh){
		if(getCurrentFrame().containsShape(sh.getName())){
			getCurrentFrame().removeShape(sh.getName());
			getCurrentFrame().addShape(sh, 0);
		}
	}
	
	/** sends to end of ArrayList (drawn last) */
	public void sendToFront(ShapeHolder sh){
		if(getCurrentFrame().containsShape(sh.getName())){
			getCurrentFrame().addShape(sh);
		}
	}

	/** creates and shows the gui */
	private static void createAndShow(){
		JFrame.setDefaultLookAndFeelDecorated(true);
	
		CoGr frame = new CoGr();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	
		frame.createToolbar();
	
		frame.getContentPane().add(statusBar, BorderLayout.SOUTH);
	
		frame.setVisible(true);
	
	}

	/** main method */
	public static void main(String[] args){
		javax.swing.SwingUtilities.invokeLater(new Runnable(){
			public void run(){
				createAndShow();
			}
		});
	}
}

//MOST IMPORTANT
//-space tabbing through selected objects is probably not going to work perfectly but i
//	can't tell for sure w/o KeyListeners


