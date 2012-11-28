import java.awt.Dimension;
import javax.swing.*;

public class StatusBar extends JLabel{
	
	public StatusBar(){
		super();
		super.setSize(new Dimension(500, 20));
	}
	
	public void setMessage(String message){
		setText(message);
	}
}
