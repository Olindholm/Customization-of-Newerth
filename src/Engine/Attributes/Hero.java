package Engine.Attributes;

import java.util.Vector;

public class Hero implements Comparable<Hero> {
	//STATIC variables;
	//Variables
	
	//Setup;
	String name;
	String folder;
	String entry;
	public	Vector<Avatar>	avatars		= new Vector<Avatar>();
	public	Vector<Avatar>	ultimate	= new Vector<Avatar>();
	
	//Constructor;
	public Hero(String srcname,String entry,String name) {
		this.name = srcname;
		this.entry = entry;
		String[] split = entry.split("/"); this.folder = split[split.length-2];
		
		this.addAvatar(new Avatar(name,folder));
	}
	
	//Set;
	
	//Get;
	public String getName() {
		return name;
	}
	public String getFolder() {
		return folder;
	}
	public String getEntry() {
		return entry;
	}
	public Avatar getAvatar(int index) {
		return avatars.get(index);
	}
	public int getAvatarCount() {
		return avatars.size();
	}
	public Avatar getUltimate(int index) {
		return ultimate.get(index);
	}
	public int getUltimateCount() {
		return ultimate.size();
	}
	
	//Add;
	public void addAvatar(Avatar avatar) {
		avatars.add(avatar);
	}
	public void addUltimate(Avatar avatar) {
		ultimate.add(avatar);
	}

	@Override
	public int compareTo(Hero hero) {
		return getAvatar(0).getName().compareToIgnoreCase(hero.getAvatar(0).getName());
	}
	
	//Remove;
	public void removeAvatar(int index) {
		avatars.remove(index);
	}
	public void removeUltimate(int index) {
		ultimate.remove(index);
	}
	
	//Do;
	
	//Other;
	
	//Implements;
	
}
