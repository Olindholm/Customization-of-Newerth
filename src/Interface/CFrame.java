package Interface;

import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.event.WindowListener;

import javax.swing.JFrame;
import javax.swing.JMenuBar;

public class CFrame {
	
	JFrame frame = new JFrame();
	
	public void setTitle(String title) {
		frame.setTitle(title);
	}
	public void setIconImage(Image image) {
		frame.setIconImage(image);
	}
	public void setResizable(boolean resizable) {
		frame.setResizable(resizable);
	}
	public void add(Component component) {
		frame.add(component);
	}
	public void setJMenuBar(JMenuBar menubar) {
		frame.setJMenuBar(menubar);
	}
	public void finish() {
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.pack();
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
	}
	public void addWindowListener(WindowListener windowlistener) {
		frame.addWindowListener(windowlistener);
	}
	public JFrame component() {
		return frame;
	}
	
	public Container getContentPane() {
		return frame.getContentPane();
	}
	
	public void setContentPane(Container panel) {
		frame.setContentPane(panel);
	}
}
