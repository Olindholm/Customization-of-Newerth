package Interface;

import java.awt.Component;
import java.awt.Dimension;

import javax.swing.JTabbedPane;

public class CTabPane {
	
	JTabbedPane tabpane = new JTabbedPane();
	
	public CTabPane(int width,int height) {
		tabpane.setSize(new Dimension(width,height));
	}
	
	public void addTab(String title,Component component) {
		tabpane.addTab(title,component);
	}
	
	public void setEnabledTab(int index,boolean enabled) {
		tabpane.setEnabledAt(index,enabled);
	}
	
	public Component component() {
		return tabpane;
	}
	
}
