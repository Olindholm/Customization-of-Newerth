package gui.util;

import java.util.Vector;

import javafx.beans.property.SimpleListProperty;
import javafx.collections.ObservableList;

/*
 * public class FilterableObservableList<E> extends SimpleListProperty<E> {
 * caused difficulties with incombablity with Java 8 & Eclipse...
 * For details... http://stackoverflow.com/questions/19137261/java-duplicate-methods-named-xxx-issue-when-using-eclipse-compiler-in-idea-wi
 */
public class FilterableObservableList<E> extends SimpleListProperty {
	// STATIC Variables

	// STATIC Methods

	// Variables
	private Vector<E> hidden = new Vector<E>();
	private String	filter = "";

	// Constructors
	public FilterableObservableList(ObservableList<E> observableList) {
		super(observableList);
	}

	// Setters
	public void setFilter(String filter) {
		
		if(!this.filter.equalsIgnoreCase(filter)) {
			this.filter = filter.toLowerCase();
			//super.clear();
			
			filterList();
			filterHidden();
		}
	}

	// Getters
	public String getFilter() {
		return this.filter;
	}
	
	// Adders
	@Override
	public boolean add(Object e) {
		String str = e.toString().toLowerCase();
		String filter = this.getFilter().toLowerCase();
		
		if(str.contains(filter)) {
			super.add(e);
		}
		else {
			hidden.add((E) e);
		}
		
		return true;
	}

	// Removers

	// Others Methods
	private void filterList() {
			for(int i = 0; i < hidden.size(); i++) {
			Object e = hidden.get(i);
			
			
			String str = e.toString().toLowerCase();
			String filter = this.getFilter().toLowerCase();
			
			if(str.contains(filter)) {
				super.add(hidden.remove(i));
				i--;
			}
		}
	}
	private void filterHidden() {
		for(int i = 0; i < super.getSize(); i++) {
			Object e = super.get(i);
			
			
			String str = e.toString().toLowerCase();
			String filter = this.getFilter().toLowerCase();
			
			if(!str.contains(filter)) {
				hidden.add((E) super.remove(i));
				i--;
			}
		}
	}
	@Override
	public void clear() {
		hidden.clear();
		super.clear();
	}

	// Implementation Methods

	// Internal Classes

}
