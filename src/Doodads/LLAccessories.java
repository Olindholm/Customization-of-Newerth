package Doodads;

public class LLAccessories {
	public static boolean getOpposite(boolean b) {
		
		if(b == false) {
			return true;
		}
		
		return false;
		
	}
	public static int containsCount(String s,String substring) {
		
		int count = 0;
		
		while(s.contains(substring)) {
			s = s.substring(s.indexOf(substring)+substring.length());
			count++;
		}
		
		return count;
		
	}
	public static String toStringNumber(String number) {
		
		String[] sp;
		//Reverse
		sp = number.split("");
		number = "";
		
		for(int i = sp.length-1;i >= 0;i--) {
			number += sp[i];
		}
		
		
		int index = 0;
		if(number.contains(".")) {
			index = number.indexOf(".")+".".length();
		}
		
		while(number.substring(index).length() > 3) {
			number = number.substring(0,index+3)+","+number.substring(index+3);
			index = index+3+",".length();
		}
		
		//Reverse
		sp = number.split("");
		number = "";
		
		for(int i = sp.length-1;i >= 0;i--) {
			number += sp[i];
		}
		
		return number;
	}
	public static int indexOf(String s,String regex,int fromindex) {
		s = s.substring(fromindex);
		
		String[] split = s.split(regex,2);
		
		if(split.length == 1) {
			return -1;
		}
		
		return fromindex+split[0].length();
	}
	public static String getString(String searchstring,String regex) {
		return getString(searchstring,regex,0);
	}
	public static String getString(String searchstring,String regex,int fromindex) {
		searchstring = searchstring.substring(fromindex);
		
		String[] split = searchstring.split(regex,2);
		
		if(split.length == 1) {
			return null;
		}
		
		return searchstring.substring(split[0].length(),searchstring.length()-split[1].length());
	}
}
