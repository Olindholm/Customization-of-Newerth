package res.ent;

import java.util.Vector;

import org.w3c.dom.Element;

public abstract class AbstractEntity implements Entity {
	// STATIC Variables

	// STATIC Methods

	// Variables
	String		entryString;
	String[]	attributes;
	Element		element;
	
	Vector<Entity> subEntities = new Vector<Entity>();

	// Constructors
	public AbstractEntity(String entryString, Element element) {
		this.entryString = entryString;
		this.element = element;
	}

	// Setters
	public void setAttributes(String[] keywords) {
		for (int i = 0; i < keywords.length; i++) {
			String attribute = element.getAttribute(keywords[i]);
			attributes[i] = attribute;
		}
	}

	// Getters
	public String[] getAttributes() {
		//Cloning the object to remove any chance of an outside class changing the values
		return attributes.clone();
	}

	// Adders

	// Removers

	// Others Methods
	@Override
	public String toString() {
		return element.getAttribute("name");
	}

	// Implementation Methods

	// Internal Classes

}
