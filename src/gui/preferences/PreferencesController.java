package gui.preferences;

import java.io.File;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;

import gui.Controller;
import gui.Model;
import gui.View;

public class PreferencesController implements Controller {
	// STATIC Variables
	public static final String TITLE = "Preferences";

	// STATIC Methods

	// Variables
	Model	theModel;
	View	theView;
	
	//General
	//@FXML private ComboBox language;
	@FXML private CheckBox	autoUpdate;
	
	//Resources
	@FXML private TextField	resourceFile;
	@FXML private Slider	threading;
	@FXML private CheckBox	autoRefresh;
	
	// Constructors
	public PreferencesController(Model model) {
		theModel = model;
	}

	// Setters
	
	// Getters

	// Adders

	// Removers

	// Others Methods
	@FXML
	public void handleOK() {
		handleApply();
		handleClose();
		
	}
	@FXML
	public void handleApply() {
		theModel.set("autoUpdate", "" + autoUpdate.isSelected());
		
		theModel.set("resourceFile", resourceFile.getText());
		theModel.set("threading", "" + ((int) threading.getValue()));
		theModel.set("autoRefresh", "" + autoRefresh.isSelected());
	}
	@FXML
	public void handleBrowse() {
		FileChooser fileChooser = new FileChooser();
		fileChooser.getExtensionFilters().add(new ExtensionFilter("S2Games Resource File","resources0.s2z"));
		File chosenFile = fileChooser.showOpenDialog(theView);
		
		if(chosenFile != null) {
			resourceFile.setText(chosenFile.toString());
		}
	}
	@FXML
	public void handleClose() {
		theView.close();
	}

	// Implementation Methods
	@Override
	public void initialize(View view) {
		theView = view;
		
		//Inserting the correct values into it's corresponding place...
		autoUpdate.setSelected(theModel.getBoolean("autoUpdate", true));
		
		resourceFile.setText(theModel.getString("resourceFile", ""));
		threading.setValue(theModel.getInt("threading", 3));
		autoRefresh.setSelected(theModel.getBoolean("autoRefresh", true));
		
		//PreferencesView(Window)
		view.setTitle(TITLE);
		view.getIcons().add(new Image(ClassLoader.getSystemResourceAsStream("gui/res/icon.png")));
		view.setResizable(false);
	}

	// Internal Classes

}
