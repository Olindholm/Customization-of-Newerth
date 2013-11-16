package gui.map;

import javafx.scene.layout.VBox;

import gui.Model;

public abstract class AbstractMap extends VBox implements Map {
	// STATIC Variables

	// STATIC Methods

	// Variables
	Model theModel;

	// Constructors
	public AbstractMap(Model model) {
		setModel(model);
	}

	// Setters
	@Override
	public void setModel(Model model) {
		theModel = model;
	}

	// Getters
	@Override
	public Model getModel() {
		return theModel;
	}

	// Adders

	// Removers

	// Others Methods

	// Implementation Methods

	// Internal Classes

}
