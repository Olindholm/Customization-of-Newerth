package Doodads;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Collections;
import java.util.Comparator;
import java.util.Properties;
import java.util.Vector;

public class LLProperty {
	//STATIC variables;
	
	//Variables;
	String filepath = "";;
	Vector<String[]> properties = new Vector<String[]>();
	private boolean inorder = true;
	
	//Setup;
	
	//Constructor;
	public LLProperty(String filepath) throws FileNotFoundException, IOException {
		this.filepath = filepath;
		
		StringBuilder sb = new StringBuilder();
		LLInputStream in = new LLInputStream(new FileInputStream(new LLFile(filepath)));
		while(in.available() > 0) {
			sb.append(in.readString(4*4*1024));
		}
		in.close();
		
		String[] lines = sb.toString().split("[\n]");
		for(String line:lines) {
			if(!line.startsWith("#")) {
				if(LLAccessories.containsCount(line,"=")-LLAccessories.containsCount(line,"\\=") == 1) {
					
					String[] lineparts = line.split("=",2);
					
					setProperty(lineparts[0],lineparts[1].replace("\\n","\n"));
				}
			}
		}
	}
	//Set;
	public void setProperty(String key,String value) {
		if(!key.contains("=") && !key.contains("\n")) {
			String[] property = getProperty(key);
			if(property != null) {
				property[1] = value;
				return;
			}
			String[] property2 = {key,value};
			properties.add(property2);
			inorder = false;
		}
	}
	
	//Get;
	private String[] getProperty(String key) {
		sort();
		
		int area = properties.size();
		int index = (int) (-1+Math.ceil(area/2.0));
		
		while(area > 0) {
			//System.out.println("area="+area);
			//System.out.println("index="+index);
			
			String[] asd = properties.get(index);
			int compare = key.compareToIgnoreCase(asd[0]);
			//System.out.println(key+" vs "+properties.get(index)[0]);
			//System.out.println("compare="+compare);
			
			if(compare > 0) {
				area = area/2;
				index = (int) (index+Math.ceil(area/2.0));
			}
			else if (compare < 0) {
				area = (int) (Math.ceil(area/2.0)-1);
				index = index-area/2-1;
			}
			else {
				return asd;
			}
		}
		
		return null;
	}
	public String getProperty(String key,String property) {
		if(getProperty(key) == null) {
			setProperty(key,property+"");
			return property;
		}
		if(getProperty(key).equals("null")) {
			return null;
		}
		return getProperty(key)[1];
	}
	public int getProperty(String key,int property) {
		if(getProperty(key) != null) {
			try {
				return Integer.parseInt(getProperty(key)[1]);
			} catch(NumberFormatException e) {
			}
		}
		setProperty(key,property+"");
		return property;
	}
	public boolean getProperty(String key,boolean property) {
		if(getProperty(key) == null) {
			setProperty(key,property+"");
			return property;
		}
		//stuff
		String p = getProperty(key)[1];
		if(p.equalsIgnoreCase("true") || p.equalsIgnoreCase("yes") || p.equalsIgnoreCase("1")) {
			return true;
		}
		else if(p.equalsIgnoreCase("false") || p.equalsIgnoreCase("no") || p.equalsIgnoreCase("0")) {
			return false;
		}
		else {
			return property;
		}
	}
	//Add;
	
	//Remove;
	public String removeLastChar(String s) {
	    if (s == null || s.length() == 0 || s.endsWith("\n") == true) {
	        return s;
	    }
	    return s.substring(0,s.length()-1);
	}


	
	//Do;
	public void sort() {
		if(!inorder) {
			Collections.sort(properties,
				new Comparator<String[]>() {
					@Override
					public int compare(String[] a, String[] b) {
						return a[0].compareToIgnoreCase(b[0]);
					}
				}
			);
		}
	}
	
	//Other;
	public void save(String message) throws FileNotFoundException, IOException {
		sort();
		
		LLOutputStream ut = new LLOutputStream(new FileOutputStream(new LLFile(filepath)));
		
		ut.writeString("# "+message.replace("\n",""));
		
		for(int i = 0;i <= properties.size()-1;i++) {
			String[] property = properties.get(i);
			
			ut.writeString("\n"+property[0]+"="+property[1]+"".replace("\n","\\n"));
		}
		
		ut.close();
	}
	
	//Implements;
	
}
