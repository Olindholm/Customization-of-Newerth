package res;

import java.io.IOException;
import java.io.InputStream;
import java.util.Scanner;

public class NameTable {
	// STATIC Variables

	// STATIC Methods

	// Variables
	String stringtable;

	// Constructors
	public NameTable(InputStream input) throws IOException {
		stringtable = convertStreamToString(input);
	}

	// Setters

	// Getters
	public String getName(String name) {
		int index1 = indexOf(stringtable, "\\s[0-9a-zA-Z-][0-9a-zA-Z\\s-]+\n", stringtable.indexOf(name))+1;
		int index2 = stringtable.indexOf("\n", index1);
		
		return stringtable.substring(index1, index2);
	}
	
	public String getHeroName(String heroName) {
		return getName("mstore_" + heroName + "_name");
	}
	public String getProductName(String productId) {
		return getName("mstore_product" + productId + "_name");
	}

	// Adders

	// Removers

	// Others Methods
	private int indexOf(String s,String regex,int fromindex) {
		s = s.substring(fromindex);
		String[] split = s.split(regex,2);
		
		if(split.length == 1) return -1; return fromindex+split[0].length();
	}
	
	private String convertStreamToString(InputStream is) {
		try (Scanner s = new Scanner(is, "UTF-8")) {
			s.useDelimiter("\\A");
			return s.hasNext() ? s.next() : "";
		}
	}

	// Implementation Methods

	// Internal Classes

}
