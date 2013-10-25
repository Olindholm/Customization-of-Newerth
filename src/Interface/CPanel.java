package Interface;

import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;

import javax.swing.JPanel;

public class CPanel {
	
	private JPanel panel = new JPanel();
	
	public CPanel(int width,int height) {
		
		setSize(width,height);
		panel.setLayout(null);
	}
	
	public Color getBackgroundColor() {
		return panel.getBackground();
	}

	public void add(Component component,int x,int y) {
		component.setBounds(x,y,component.getWidth(),component.getHeight());
		panel.add(component);
	}
	public void add(Component component,int x,int y,boolean preffered) {
		component.setBounds(x,y,component.getPreferredSize().width,component.getPreferredSize().height);
		panel.add(component);
	}
	
	public JPanel component() {
		return panel;
	}

	public void setSize(int width,int height) {
		Dimension d = new Dimension(width,height);
		
		panel.setPreferredSize(d);
		panel.setSize(d);
		
	}
}
