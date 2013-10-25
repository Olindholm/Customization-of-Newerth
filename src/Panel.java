import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import javax.swing.JPanel;
import javax.swing.SpringLayout;

public class Panel extends JPanel {
	public Panel(Main main) {
		this.main = main;
		this.setLayout(layout);
	}
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		int x = 5;
		if(this.state == 1) {
			//g.setColor(new Color(this.main.config.random.nextInt(255),this.main.config.random.nextInt(255),this.main.config.random.nextInt(255)));
			for(int i = 0; i <= this.main.config.hero[this.main.gui.last].avatar; i += 1) {
				g.drawImage(this.main.config.hero[this.main.gui.last].image[i],x,5,32,32,null);
				x += 32+5;
			}
			for(int i = this.main.config.hero[this.main.gui.last].avatar+1; i <= 9; i += 1) {
				g.drawImage(this.main.config.image,x,5,32,32,null);
				x += 32+5;
			}
		}
		else if(this.state == 2) {
			g.drawImage(this.main.gui.shopbg,5,5,310,275,null);
		}
		else if(this.state == 3) {
			for(int i = 0; i <= this.main.config.a; i += 1) {
				g.drawImage(this.main.config.announcer[i].image,x,5,32,32,null);
				x += 32+5;
			}
		}
		else if(this.state == 4) {
			for(int i = 0; i <= this.main.config.t; i += 1) {
				g.drawImage(this.main.config.taunt[i].image,x,5,32,32,null);
				x += 32+5;
			}
		}
	}
	public void add(Component comp, int x, int y) {
		this.layout.putConstraint(SpringLayout.WEST, comp, x,SpringLayout.WEST, this);
		this.layout.putConstraint(SpringLayout.NORTH, comp, y, SpringLayout.NORTH, this);
		this.add(comp);
	}
	Main main;
	int state = 0;
	
	SpringLayout layout = new SpringLayout();
}
