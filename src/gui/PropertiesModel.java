package gui;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

public abstract class PropertiesModel extends Properties implements Model {
	// STATIC Variables

	// STATIC Methods

	// Variables

	// Constructors
	public PropertiesModel() {
	}
	public PropertiesModel(String defaultsPath) {
		try {
			super.defaults.load(ClassLoader.getSystemResourceAsStream(defaultsPath));
		} catch (IOException e) {
			throw new RuntimeException("Could not read or access " + defaultsPath, e);
		}
	}

	// Setters

	// Getters

	// Adders

	// Removers

	// Others Methods

	// Implementation Methods
	@Override
	public void setString(String key, String value) {
		super.setProperty(key, value);
	}
	@Override
	public void setInt(String key, int value) {
		super.setProperty(key, value + "");
	}
	@Override
	public void setBoolean(String key, boolean value) {
		super.setProperty(key, value + "");
	}
	@Override
	public String getString(String key) {
		String property = super.getProperty(key);
		return property.equals("null") ? null : property;
	}
	@Override
	public int getInt(String key) {
		String property = super.getProperty(key);
		return Integer.parseInt(property);
	}
	@Override
	public boolean getBoolean(String key) {
		String property = super.getProperty(key);
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
