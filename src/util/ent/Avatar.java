package util.ent;

import org.w3c.dom.Element;

public class Avatar {
	//STATIC variables;
	//Variables;
	
	//Setup;
	private String	heroName;
	private String	id;
	private Element	element;
	
	private String	name;
	private String	key;
	private boolean	enabled = true;
	
	//Models & Effects;
	public static String[][] DEFAULTATTRIBUTES = {
		//Models & Effects;
		{"portrait",				"Insert_Default_Value"},
		
		{"passiveeffect",			""},
		{"previewpassiveeffect",	""},
		{"storepassiveeffect",		""},
		
		{"previewmodel",			"1.0"},
		{"storepos",				"0 0 0"},
		{"storescale",				"1.0"},
		
		//Scales;
		{"preglobalscale",			"1.0"},
		{"modelscale",				"1.0"},
		{"effectscale",				"1.0"},
		{"infoheight",				""},
		{"tiltfactor",				""},
		{"tiltspeed",				""},
		
		//Sounds;
		{"selectedsound",			"Insert_Default_Value"},
		{"selectedflavorsound",		"Insert_Default_Value"},
		{"confirmmovesound",		"Insert_Default_Value"},
		{"confirmattacksound",		"Insert_Default_Value"},
		{"nomanasound",				"Insert_Default_Value"},
		{"cooldownsound",			"Insert_Default_Value"},
		{"tauntedsound",			"Insert_Default_Value"},
		{"tauntkillsound",			"Insert_Default_Value"},
		
		//Attacks;
		{"attackoffset",			"0 0 0"},
		{"attackstarteffect",		""},
		{"attackactioneffect",		""},
		{"attackimpacteffect",		""},
	};
	private String[] attributes = new String[DEFAULTATTRIBUTES.length];
	
	//private Projectile attackprojectile;
	
	//Constructor;
	public Avatar(String product, String id) {
		String[] array = product.split("\\.");
		
		this.heroName = array[0];
		this.key = array[1];
		this.id = id;
	}
	
	//Set;
	public void setName(String name) {
		this.name = name;
	}
	public void setElement(Element element) {
		this.element = element;
	}
	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}
	
	//Get;
	public String getHeroName() {
		return heroName;
	}
	public String getName() {
		return name;
	}
	public String getKey() {
		return key;
	}
	public String getId() {
		return id;
	}
	public Element getElement() {
		return element;
	}
	public String[] getAttributes() {
		return attributes;
	}
	
	//Add;
	
	//Remove;
	
	//Is;
	public boolean isEnabled() {
		return this.enabled;
	}
	
	//Do;
	
	//Other;
	@Override
	public String toString() {
		return this.getName();
	}
	
	//Implements;
	
}

