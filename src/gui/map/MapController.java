package gui.map;

import res.ent.Product;
import javafx.scene.layout.VBox;

import gui.Model;

public class MapController {
	// STATIC Variables

	// STATIC Methods

	// Variables
	Model	theModel;
	VBox	theBox;
	
	Map		theMap;

	// Constructors
	public MapController(VBox box, Model model, Map map) {
		theBox = box;
		theModel = model;
		theMap = map;
	}

	// Setters
	public void setModel(Model model) {
		theModel = model;
	}
	public void setMap(Map map) {
		theMap = map;
	}

	// Getters
	public Model getModel() {
		return theModel;
	}
	public Map getMap() {
		return theMap;
	}

	// Adders

	// Removers

	// Others Methods
	public void load(Product[] product) {
		
	}
	public void save() {
	}

	// Implementation Methods

	// Internal Classes

}
