import java.awt.Image;
import javax.swing.ImageIcon;


public class Taunt {
	public Taunt(String name,String key) {
		this.name = name;
		this.key = key;
		this.file = "shared/effects/taunts/"+key+"/death.effect";
		
		ImageIcon image = new ImageIcon(getClass().getResource(key+".png"));
		this.image = image.getImage();
	}
	//CoN
	Image image;
	String name;
	String key;
	String taunt;
	//General
	String file;
}
