import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;
import java.util.Random;
import javax.swing.*;

/** class that holds all information for how a shape should be drawn */
public class ShapeHolder {
    private String name;
    private BasicStroke stroke;
    private Color color;
    private Shape shape;
    private boolean filled;
    private AffineTransform affT;
    private Shape strokeShape;
    private boolean selected;
    private double theta;            //rotation 
    private double tx;               //translate-x 
    private double ty;               //translate-y
    private double sx;               //scale-x
    private double sy;               //scale-y
    private double shx;              //shear-x
    private double shy;              //shear-y
    
    public ShapeHolder(String name, BasicStroke stroke, Color color, 
                       Shape shape, boolean filled, AffineTransform affT){
        this.name = name;
        this.stroke = stroke;
        this.color = color;
        this.shape = shape;
        this.filled = filled;
        this.affT = affT;
        this.selected = false;
        this.strokeShape = stroke.createStrokedShape(shape);
        this.tx = affT.getTranslateX();
        this.ty = affT.getTranslateY(); 
        this.theta = Math.atan2(affT.getShearY(), affT.getShearX());
        this.sx = affT.getScaleX();
        this.sy = affT.getScaleY();
        this.shx = affT.getShearX();
        this.shy = affT.getShearY();
    }
    
    /** accessor methods */
    
    public String getName(){
        return this.name;
    }
    
    public BasicStroke getStroke(){
        return this.stroke;
    }
    
    public Color getColor(){
        return this.color;
    }
    
    public Shape getShape(){
        return this.shape;
    }
    
    public boolean getFilled(){
        return this.filled;
    }
    
    public AffineTransform getAffineT(){
        return this.affT;
    }
    
    public boolean isSelected(){
    	return this.selected;
    }
    
    /** end accessor methods */
    
    /** mutation methods */
    
    public void setName(String s){
        this.name = s;
    }
    
    public void setStroke(BasicStroke bs){
        this.stroke = bs;
    }
    
    public void setColor(Color c){
        this.color = c;
    }
    
    public void setShape(Shape r){
        this.shape = r;
    }
    
    public void setFilled(boolean f){
        this.filled = f;
    }
    
    public void setAffineT(AffineTransform a){
        this.affT = a;
    }
    
    public void setSelected(boolean b){
    	this.selected = b;
    }
    
    /** end mutation methods */
    
    /**checks if given point is contained in this ShapeHolder's
     * shape field
     */
    public boolean containsPt(Point2D p){
        Shape temp = this.affT.createTransformedShape(this.shape);
        Shape tempStroke = this.affT.createTransformedShape(this.strokeShape);
        return temp.contains(p) || tempStroke.contains(p);
    }
    
    /** checks if given coordinates are contained in this ShapeHolder's
     *  shape field
     */
    public boolean containsPt(double x, double y){
        return this.shape.contains(x, y);
    }   
    
    /** updates rotation factor */
    public void rotate(double rot){
        this.theta += rot;
        updateAffT();
    }
    
    /** update shear factors */
    public void shear(double x, double y){
        this.shx += x;
        this.shy += y;
        updateAffT();
    }
    
    /** update translate factors */
    public void translate(double x, double y){
        this.tx += x;
        this.ty += y;
        updateAffT();
    }
    
    /** update scaling factors */
    public void scale(double x, double y){
        this.sx += x;
        this.sy += y;
        updateAffT();
    }
    
    /** recalculate transformation matrix */
    protected synchronized void updateAffT(){
    	double c = (double) Math.cos(this.theta);
    	double s = (double) Math.sin(this.theta);
    	affT.setTransform(c, s, -s, c, tx, ty);
    }
    
    /** gets the origin of the transformed shapes bounding box */
    public Point2D getOrigin(){
    	double x = this.affT.createTransformedShape(shape).getBounds().x;
    	double y = this.affT.createTransformedShape(shape).getBounds().y;
    	return new Point2D.Double(x, y);
    }
    
    /** gets the center of the transformed shapes bounding box */
    public Point2D getCenter(){
    	Rectangle2D rec = this.affT.createTransformedShape(shape).getBounds();
    	double x = rec.getCenterX();
    	double y = rec.getCenterY();
    	return new Point2D.Double(x, y);
    }
}
