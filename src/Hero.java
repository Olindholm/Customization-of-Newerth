import java.awt.Image;
import javax.swing.ImageIcon;

public class Hero {
	public Hero(String name,String key) {
		this.name[0] = name;
		this.key[0] = key;
		this.alt[0] = "hero";
		
		ImageIcon image = new ImageIcon(getClass().getResource(
				this.key[0] + this.avatar + ".png"));
		this.image[this.avatar] = image.getImage();
	}
	public void addAlt(String name, String key, String alt) {
		this.avatar += 1;
		this.name[this.avatar] = name;
		this.key[this.avatar] = key;
		this.alt[this.avatar] = alt;
		
		for(int i = 0;i <= spellnum;i++) {
			spell[i].filepath[avatar] = alt+"/"+spell[i].filepath[0];
			spell[i].body[avatar] = spell[i].body[0];
		}
		
		ImageIcon image = new ImageIcon(getClass().getResource(
				this.key[0] + this.avatar + ".png"));
		this.image[this.avatar] = image.getImage();
	}
	public void addSpell(String filepath, String body,boolean projectile) {
		spellnum += 1;
		spell[spellnum] = new Spell(filepath, body,projectile);
	}
	//CoN info;
	Image[] image = new Image[10];
	String file = "";
	String subfile = "";
	int avatar = 0;
	int spellnum = -1;
	Spell[] spell = new Spell[20];

	//General;
	String[] name = new String[10];
	String[] key = new String[10];
	String[] alt = new String[10];

	String[] icon = new String[10];
	String[] portrait = new String[10];
	String[] model = new String[10];
	String[] previewmodel = new String[10];
	String[] modelscale = new String[10];
	String[] effectscale = new String[10];

	//Sounds;
	String selectedsound[] = new String[10];
	String selectedflavorsound[] = new String[10];
	String confirmmovesound[] = new String[10];
	String confirmattacksound[] = new String[10];
	String nomanasound[] = new String[10];
	String cooldownsound[] = new String[10];
	String tauntedsound[] = new String[10];
	String tauntkillsound[] = new String[10];

	//Scales;
	String[] preglobalscale = new String[10];
	String[] passiveeffect = new String[10];
	String[] infoheight = new String[10];
	String[] tiltfactor = new String[10];
	String[] tiltspeed = new String[10];

	//Attack;
	String[] attackoffset = new String[10];
	String[] attackprojectile = new String[10];
	String[] attackstarteffect = new String[10];
	String[] attackactioneffect = new String[10];
	String[] attackimpacteffect = new String[10];

	//Preview;
	String[] previewpos = new String[10];
	String[] previewscale = new String[10];
	String[] storepos = new String[10];
	String[] storescale = new String[10];
	
	//Modeledit;
	boolean editmodel = false;
	
	//Courier
	String[] ground = new String[10];
	String[] flying = new String[10];
}
