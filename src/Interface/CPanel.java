package Interface;

import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;

import javax.swing.JPanel;

public class CPanel {
	
	JPanel panel = new JPanel();
	
	public CPanel(int width,int height) {
		
		setSize(width,height);
		panel.setLayout(null);
	}
	
	public void add(Component component,int x,int y) {
		component.setBounds(x,y,component.getWidth(),component.getHeight());
		panel.add(component);
	}
	
	public Component component() {
		return panel;
	}

	public void setSize(int width,int height) {
		Dimension d = new Dimension(width,height);
		
		panel.setPreferredSize(d);
		panel.setSize(d);
		
	}
}
