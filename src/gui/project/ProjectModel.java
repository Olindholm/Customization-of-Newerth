package gui.project;

import gui.PropertiesModel;

import java.io.IOException;
import java.io.InputStream;

public class ProjectModel extends PropertiesModel {
	// STATIC Variables

	// STATIC Methods

	// Variables

	// Constructors
	public ProjectModel() {
		super("gui/project/defaults.properties");
	}
	public ProjectModel(InputStream in) throws IOException {
		this();
		super.load(in);
	}

	// Setters

	// Getters

	// Adders

	// Removers

	// Others Methods

	// Implementation Methods

	// Internal Classes

}
