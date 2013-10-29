package util.ent;

import java.util.Vector;

import org.w3c.dom.Element;

public class Hero {
	// STATIC Variables

	// STATIC Methods

	// Variables
	String	entry;
	Element	element;

	Vector<Avatar>	avatars		= new Vector<Avatar>();

	// Constructors
	public Hero(String entry, Element element) {
		this.entry = entry;
		this.element = element;
		
		String key = entry.split("\\/")[1]; // entry = heroes/{key}/(hero/{key}).entity
		this.element.setAttribute("key",key);
	}

	public String getName() {
		return element.getAttribute("name");
	}
	public String getEntry() {
		return entry;
	}
	

	// Setters
	
	// Getters
	public int getAvatarCount() {
		return avatars.size();
	}
	public Avatar getAvatar(int n) {
		return avatars.get(n);
	}
	public Element getElement() {
		return element;
	}

	// Adders
	public void addAvatar(Avatar avatar) {
		avatars.add(avatar);
	}

	// Removers

	// Others Methods
	@Override
	public String toString() {
		if(getAvatarCount() > 0) return getAvatar(0).getName(); return getName();
	}

	// Implementation Methods

	// Internal Classes

}
