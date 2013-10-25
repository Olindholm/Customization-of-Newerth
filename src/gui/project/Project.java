package gui.project;

import gui.Model;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class Project extends Properties implements Model {
	// STATIC Variables
	public static final int SCHEME_NONE = -1;

	// STATIC Methods

	// Variables

	// Constructors
	public Project(int scheme) {
		set("projectScheme", "" + scheme);
	}
	public Project(InputStream in) throws IOException {
		load(in);
	}

	// Setters

	// Getters

	// Adders

	// Removers

	// Others Methods

	// Implementation Methods
	public void set(String key, String value) {
		super.setProperty(key, value);
	}
	public String getString(String key, String defaultValue) {
		String property = super.getProperty(key, defaultValue);
		
		if(property == null || property.equals("null")) {
			return null;
		}
		else {
			return property;
		}
	}
	public int getInt(String key, int defaultValue) {
		String property = super.getProperty(key, defaultValue + "");
		
		try {
			return Integer.parseInt(property);
		} catch(NumberFormatException e) {
			super.setProperty(key, defaultValue + "");
			return defaultValue;
		}
	}
	public boolean getBoolean(String key, boolean defaultValue) {
		String property = super.getProperty(key, defaultValue + "");
		
		return Boolean.parseBoolean(property);
	}

	// Internal Classes

}
