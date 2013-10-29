package util;

import java.io.IOException;
import java.io.InputStream;

public class NameTable {
	// STATIC Variables

	// STATIC Methods

	// Variables
	String stringtable;

	// Constructors
	public NameTable(InputStream input) throws IOException {
		stringtable = ResourceLoader.convertStreamToString(input);
	}

	// Setters

	// Getters
	public String getName(String name) {
		int index1 = indexOf(stringtable, "\\s[0-9a-zA-Z-][0-9a-zA-Z\\s-]+\n", stringtable.indexOf(name))+1;
		int index2 = stringtable.indexOf("\n", index1);
		
		return stringtable.substring(index1, index2);
	}

	// Adders

	// Removers

	// Others Methods
	private int indexOf(String s,String regex,int fromindex) {
		s = s.substring(fromindex);
		String[] split = s.split(regex,2);
		
		if(split.length == 1) return -1; return fromindex+split[0].length();
	}

	// Implementation Methods

	// Internal Classes

}
