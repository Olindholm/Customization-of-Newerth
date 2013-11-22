package res.ent;

import java.util.Vector;

import org.w3c.dom.Element;

public class Hero {
	// STATIC Variables

	// STATIC Methods

	// Variables
	
	Vector<Avatar> avatars = new Vector<Avatar>();

	// Constructors
	public Hero(String entryString, Element element, String name) {
		element.setAttribute("key", element.getAttribute("name"));
		new Avatar(entryString, element, name, this);
	}

	// Setters
	
	// Getters
	public String getName() {
		return avatars.get(0).getProduct();
	}
	public Avatar getAvatar(int i) {
		return avatars.get(i);
	}
	public Avatar[] getAvatars() {
		return avatars.toArray(new Avatar[avatars.size()]);
	}
	public int getAvatarCount() {
		return avatars.size();
	}

	// Adders
	public void addAvatar(Avatar avatar) {
		avatars.add(avatar);
	}

	// Removers

	// Others Methods
	@Override
	public String toString() {
		return avatars.get(0).toString();
	}
	
	// Implementation Methods

	// Internal Classes

}
