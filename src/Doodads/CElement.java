package Doodads;

import org.w3c.dom.Element;

public class CElement implements Comparable<CElement> {
	
	Element element;
	int length;
	
	public CElement(Element e) {
		element = e;
	}
	
	public Element getElement() {
		return element;
	}
	public void setLength(int l) {
		length = l;
	}

	@Override
	public int compareTo(CElement e) {
		int thisv = (int) Math.pow(Integer.parseInt(element.getAttribute("version").replace(".","")),length-element.getAttribute("version").replace(".","").length());
		int ev = (int) Math.pow(Integer.parseInt(e.getElement().getAttribute("version").replace(".","")),length-e.getElement().getAttribute("version").replace(".","").length());
		
		if(thisv > ev) {
			return 1;
		}
		else if(thisv < ev) {
			return -1;
		}
		return 0;
	}
}
