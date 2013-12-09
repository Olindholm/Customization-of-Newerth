package Engine.Attributes;

import Engine.Main;

public class Avatar {
	//STATIC variables;
	//Variables;
	
	//Setup;
	String name;
	String key;
	String icon;
	
	//Constructor;
	public Avatar(String name,String key) {
		this.name = name;
		this.key = key;
	}
	
	//Set;
	public void setIcon(String icon) {
		this.icon = icon;
	}
	
	//Get;
	public String getName() {
		return name;
	}
	public String getFullKey() {
		return key;
	}
	public String getKey() {
		return key.substring(key.indexOf(".")+1);
	}
	public String getIcon() {
		return icon;
	}
	
	//Add;
	
	//Remove;
	
	//Do;
	
	//Other;

	//Implements;
	
}

