package gui.map;

import res.ent.Hero;
import gui.Model;

public interface Map {
	// STATIC Variables

	// STATIC Methods

	// Variables

	// Constructors

	// Setters
	public void setModel(Model model);

	// Getters
	public Model getModel();

	// Adders

	// Removers

	// Others Methods
	public void load(Hero hero);
	public void save();

	// Implementation Methods

	// Internal Classes

}
