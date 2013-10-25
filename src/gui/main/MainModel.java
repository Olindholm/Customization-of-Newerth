package gui.main;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;


import gui.Model;
import gui.project.Project;

public class MainModel extends Properties implements Model {
	// STATIC Variables

	// STATIC Methods

	// Variables
	public Project project;

	// Constructors

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
		
		if(property.equals("null")) {
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
	
	public void store(File file, String comment) {
		try {
			super.store(new FileOutputStream(file), comment);
		} catch (IOException e) {
			throw new RuntimeException("Could not write to or access " + file.getPath(), e);
		}
	}

	// Internal Classes

}
