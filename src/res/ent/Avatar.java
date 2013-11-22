package res.ent;

import org.w3c.dom.*;

public class Avatar extends AbstractEntity implements Product {
	//STATIC variables;
	String[] ATTRIBUTE_NAMES = {
		//Models & Effects
		"portrait",
		"passiveeffect",
		"previewpassiveeffect",
		"storepassiveeffect",
		
		"previewmodel",
		"storepos",
		"storescale",
		
		//Scales
		"preglobalscale",
		"modelscale",
		"effectscale",
		"infoheight",
		"tiltfactor",
		"tiltspeed",
		
		//Attacks
		"attackoffset",
		"attackstarteffect",
		"attackactioneffect",
		"attackimpacteffect",
		
		//Sounds
		"selectedsound",
		"selectedflavorsound",
		"confirmmovesound",
		"confirmattacksound",
		"nomanasound",
		"cooldownsound",
		"tauntedsound",
		"tauntkillsound",
	};
	
	//Variables;
	String	name, product, icon;
	boolean	ultimate;
	
	Hero	hero;
	
	//Constructor;
	public Avatar(String entryString, Element element, String name, Hero hero) {
		super(entryString, element);
		this.name = name;
		this.product = element.getAttribute("key"); //Assuming key="Hero_*.*"
		this.icon = !(element.getAttribute("icon").isEmpty()) ? element.getAttribute("icon") : element.getAttribute("icon2");
		hero.addAvatar(this);
		
		//Attributes
		attributes = new String[ATTRIBUTE_NAMES.length];
		setAttributes(ATTRIBUTE_NAMES);
	}
	
	//Set;
	
	//Get;
	@Override
	public String getProduct() {
		return product;
	}
	@Override
	public void getIcon() {
		// TODO Auto-generated method stub
		
	}
	
	//Add;
	
	//Remove;
	
	//Is;
	
	//Do;
	
	//Other;
	@Override
	public String toString() {
		return name;
	}
	
	//Implements;
	@Override
	public String[] replicate(Entity e) {
		if(!(e instanceof Avatar)) throw new RuntimeException(e.toString() + "is not an instance of Avatar");
		
		
		
		return null;
	}
	
}

