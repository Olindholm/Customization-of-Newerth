
public class Spell {
	public Spell(String filepath,String body,boolean projectile) {
		this.filepath[0] = filepath;
		this.body[0] = body;
		this.projectile = projectile;
	}
	//general
	String[] filepath = new String[10];
	String[] body = new String[10];
	//Other
	boolean projectile;
	String[] name = new String[10];
}
