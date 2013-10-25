package Doodads;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

public class LLProperty extends Properties {
	//STATIC variables;
	
	//Variables;
	File file;
	
	//Setup;
	
	//Constructor;
	public LLProperty() {
	}
	public LLProperty(File file) {
		this.file = file;
		LLInputStream in = null;
		try {
			in = new LLInputStream(new FileInputStream(file));
		} catch (FileNotFoundException e) {
		}
		try {
			load(in);
		} catch (IOException e) {
		}
	}
	//Set;
	public void setFile(File file) {
		this.file = file;
		LLInputStream in = null;
		try {
			in = new LLInputStream(new FileInputStream(file));
		} catch (FileNotFoundException e) {
		}
	}
	
	//Get;
	public String getProperty(String key,String property) {
		if(getProperty(key) == null) {
			setProperty(key,property+"");
		}
		if(getProperty(key).equals("null")) {
			return null;
		}
		return getProperty(key);
	}
	public int getProperty(String key,int property) {
		if(getProperty(key) == null) {
			setProperty(key,property+"");
		}
		return Integer.parseInt(getProperty(key));
	}
	public boolean getProperty(String key,boolean property) {
		if(getProperty(key) == null) {
			setProperty(key,property+"");
		}
		return Boolean.parseBoolean(getProperty(key));
	}
	//Add;
	
	//Remove;
	
	//Do;
	
	//Other;
	public void save(String message) {
		try {
			store(new LLOutputStream(new FileOutputStream(file)),message);
		} catch (IOException e) {
		}
	}
	
	//Implements;
	
}
