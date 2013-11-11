package gui.util;

import javafx.beans.property.SimpleListProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/*
 * public class SortedObservableList<E> extends SimpleListProperty<E> {
 * caused difficulties with incombablity with Java 8 & Eclipse...
 * For details... http://stackoverflow.com/questions/19137261/java-duplicate-methods-named-xxx-issue-when-using-eclipse-compiler-in-idea-wi
 */
@SuppressWarnings("rawtypes")
public class SortedObservableList<E> extends SimpleListProperty {

	// STATIC Variables
	public static <e> SortedObservableList<e> newInstance() {
		ObservableList<e> ol = FXCollections.observableArrayList();
		SortedObservableList<e> sol = new SortedObservableList<e>(ol);
		
		return sol;
	}

	// STATIC Methods

	// Variables

	// Constructors
	@SuppressWarnings("unchecked")
	public SortedObservableList(ObservableList<E> observableList) {
		super(observableList);
	}

	// Setters

	// Getters

	// Adders
	@SuppressWarnings("unchecked")
	@Override
	public boolean add(Object o) {
		String e = o.toString();
		
		for(int i = 0;i < super.size();i++) {
			
			if(e.compareToIgnoreCase(super.get(i).toString()) <= 0) {
				//Less than or equal;
				super.add(i,o);
				return true;
			}
		}
		
		return super.add(o);
	}

	// Removers

	// Others Methods

	// Implementation Methods

	// Internal Classes

}
