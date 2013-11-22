package gui;

import java.io.IOException;
import java.io.InputStream;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;

public abstract class StandardController implements Controller {
	// STATIC Variables

	// STATIC Methods

	// Variables
	boolean theDialog;
	
	protected View	theView;
	protected Model	theModel;
	
	// Constructors
	public StandardController(View view, Model model, String fxmlPath) {
		theView = view;
		theModel = model;
		
		FXMLLoader loader = new FXMLLoader();
		loader.setController(this);
		Parent root = loadRoot(loader, fxmlPath);
		
		Scene scene = new Scene(root);
		theView.setScene(scene);
	}
	// Try to hide the ugly details of try/catch and the likes behind a function with a descriptive name.
	private Parent loadRoot(FXMLLoader loader, String fxmlPath) {
		try {
			InputStream fxmlStream = ClassLoader.getSystemResourceAsStream(fxmlPath);
			return (Parent) loader.load(fxmlStream);
		} catch(IOException e) {
			throw new RuntimeException("Could not read or access " + fxmlPath, e);
		}
	}

	// Setters

	// Getters
	@Override
	public View getView() {
		return theView;
	}
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
