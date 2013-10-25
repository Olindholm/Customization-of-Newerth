package Engine.Attributes;

public class Spell {
	public Spell(String filepath,String body,boolean projectile) {
		this.filepath[0] = filepath;
		this.body[0] = body;
		this.projectile = projectile;
	}
	//general
	public String[] filepath = new String[10];
	public String[] body = new String[10];
	//Other
	public String[] name = new String[10];
	public boolean projectile;
}
