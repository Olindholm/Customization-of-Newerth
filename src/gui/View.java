package gui;

import javafx.stage.*;

public class View extends Stage {
	// STATIC Variables

	// STATIC Methods

	// Variables
	boolean theDialog;

	// Constructors
	
	// Setters

	// Getters
	
	// Adders

	// Removers

	// Others Methods
	public boolean showDialog() {
		theDialog = false;
		
		showAndWait();
		return theDialog;
	}
	public void closeAndRespond() {
		theDialog = true;
	}

	// Implementation Methods

	// Internal Classes

}
