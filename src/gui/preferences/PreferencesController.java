package gui.preferences;

import java.io.File;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;

import gui.*;

public class PreferencesController extends StandardController {
	// STATIC Variables
	public static final String TITLE = "Preferences";

	// STATIC Methods

	// Variables
	
	//General
	//@FXML private ComboBox language;
	@FXML private CheckBox	autoUpdate;
	
	//Resources
	@FXML private TextField	resourceFile;
	@FXML private Slider	threading;
	@FXML private CheckBox	autoRefresh;
	
	// Constructors
	public PreferencesController(View view, Model model) {
		super(view, model, "gui/preferences/PreferencesView.fxml");
		
		//Inserting the correct values into it's corresponding place...
		autoUpdate.setSelected(theModel.getBoolean("autoUpdate"));
		
		resourceFile.setText(theModel.getString("resourceFile"));
		threading.setValue(theModel.getInt("threading"));
		autoRefresh.setSelected(theModel.getBoolean("autoRefresh"));
		
		//PreferencesView(Window)
		theView.setTitle(TITLE);
		theView.setResizable(false);
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
		theModel.setBoolean("autoUpdate", autoUpdate.isSelected());
		
		theModel.setString("resourceFile", resourceFile.getText());
		theModel.setInt("threading", (int) threading.getValue());
		theModel.setBoolean("autoRefresh", autoRefresh.isSelected());
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
	@Override
	public void handleClose() {
		theView.close();
	}

	// Implementation Methods

	// Internal Classes

}
