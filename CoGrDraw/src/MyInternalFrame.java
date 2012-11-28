import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Random;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.image.*;
import com.sun.image.codec.jpeg.*;
import java.io.*;
 


public class MyInternalFrame extends JInternalFrame{
    static int frameCount = 0;
    static final int xOff = 20, yOff = 20;
    private int shapeCount = 0;
    private JInternalFrame frame;
    private ShapeHolder selected;
    
    private ArrayList<ShapeHolder> shapeList; //holds the shapes to be drawn
    
    public MyInternalFrame(ArrayList<ShapeHolder> shapeList){
        super("Canvas " + (++frameCount), false, false, false, false);
        this.setName("Canvas " + frameCount);
        setLocation(xOff * frameCount, yOff * frameCount);
        this.shapeList = shapeList;
        this.setContentPane(panel);
    }
    
    /** create an inner panel to draw on */
    private JPanel panel = new JPanel(){
        /** iterates over the ArrayList<ShapeHolders> and draws 
         *   each shape based on the instructions given by ShapeHolder
         */
        public void paintComponent(Graphics g){
            super.paintComponent(g);
        
            Graphics2D g2d = (Graphics2D) g;
        
            RenderingHints rh = g2d.getRenderingHints ();
            rh.put (RenderingHints.KEY_ANTIALIASING,
                    RenderingHints.VALUE_ANTIALIAS_ON);
            g2d.setRenderingHints(rh);
        
            /** iterate over the shape list */
            for(ShapeHolder s : shapeList){
                Shape shape = s.getAffineT().createTransformedShape(
                        s.getShape());
            
                /** filled shapes */
                if(s.getFilled()){
                    g2d.setStroke(s.getStroke());
                    g2d.setPaint(s.getColor());
                    g2d.fill(shape);
                }
                /** non filled shapes */
                else{
                    g2d.setStroke(s.getStroke());
                    g2d.setColor(s.getColor());
                    g2d.draw(shape);
                }
                /** draws bounding box */
                if(s.isSelected()){
                    g2d.setStroke(new BasicStroke(1));
                    g2d.draw(shape.getBounds2D());
                }
            }
        }
    };
    
    /** accessor for shapeList field */
    public ArrayList<ShapeHolder> getShapeList(){
    	return this.shapeList;
    }
    
    /** accessor for the frame field */
    public JInternalFrame getFrame(){
    	return this.frame;
    }
    
    /** get the count of the shapes in this frame */
    public int getCount(){
    	return this.shapeCount;
    }
    
    /** return the ShapeHolder with the specified name from this frame
     *  the method should not be used before checking to see if such
     *  ShapeHolder exists in the list
     */
    public ShapeHolder getShape(String name){
        for(ShapeHolder s : shapeList){
            if(s.getName()  == name)
                return s;
        }
        throw new IllegalArgumentException("Name is not in the array");
    }
    
    /** checks if there is a ShapeHolder with the given name in the list */
    public boolean containsShape(String name){
        for(ShapeHolder s : shapeList){
            if(s.getName() == name)
                return true;
        }
        return false;
    }
    
    /** append a shapeholder to the end of the of arraylist */
    public void addShape(ShapeHolder sh){
    	shapeList.add(sh);
    	shapeCount++;
    }
    
    /** instert a shapeholder into the arraylist at i */
    public void addShape(ShapeHolder sh, int i){
    	shapeList.add(i, sh);
    	shapeCount++;
    }
    
    /** remove the ShapeHolder with the specified name from this frame */
    public void removeShape(String name){
        int i = 0;
        while(i < shapeList.size()){
            if(shapeList.get(i).getName()  == name)
                shapeList.remove(i);
            i++;
        }
    }
    
    /** deletes the list of shapes clearing the canvas */
    public void clearShapes(){
    	this.shapeList = new ArrayList<ShapeHolder>();
    	this.shapeCount = 0;
    }
    
    /** gets a list of all ShapeHolders that contain the given point */
    public ArrayList<ShapeHolder> selected(Point2D p){
        ArrayList<ShapeHolder> temp = new ArrayList<ShapeHolder>();
        
        for(ShapeHolder s : shapeList){
            if(s.containsPt(p))
                temp.add(s);
        }
        return temp;
    }
    
    /** checks if any shape contains the point */
    public boolean shapesContainPt(Point2D p){
    	for(ShapeHolder s : shapeList){
            if(s.containsPt(p))
                return true;
        }
    	return false;
    }
    
    /** set selected to false for all item in the list */
    public void deselectAll(){
    	for(ShapeHolder s : shapeList){
    		s.setSelected(false);
    	}
    }
    
    /** changes the background color of the panel */
    public void changeBackgroundColor(Color color){
    	this.panel.setBackground(color);
    }
    
    /** exports this frame as a jpeg */
    public void export(double height, double width, String fileName){
    	try {
    		BufferedImage image = new BufferedImage((int) height,
    						                        (int) width,
    						                        BufferedImage.TYPE_INT_RGB);
    		Graphics2D g = image.createGraphics();
    		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
    				RenderingHints.VALUE_ANTIALIAS_ON);
    	    this.paint(g);
    	    
    	    File file = new File(fileName + ".jpeg");
    	    ImageIO.write(image, "jpeg", file);
    	    image.flush();
    	    
    	} catch(Exception e){
    		System.out.println("Failed to create file");
    	}
    }  
}
