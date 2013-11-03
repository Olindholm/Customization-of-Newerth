package gui.progress;

import javafx.fxml.FXML;
import javafx.scene.control.*;

import gui.Controller;
import gui.Model;
import gui.View;

public class ProgressController implements Controller {
	// STATIC Variables
	public static final String TITLE = "Progress";

	// STATIC Methods

	// Variables
	Model	theModel;
	View	theView;
	
	@FXML private ProgressBar	progressBar;
	@FXML private Label			progressLabel;
	
	int max		= 100;
	
	// Constructors
	
	// Setters
	public void setMaxiumum(int value) {
		max = value;
	}
	/**
	 * Sets the current value of the progress bar.
	 * Value/Max = Progress
	 * 
	 * If the Progress comes out to be 1(100%) the window will close.
	 * 
	 * @param value
	 * @param str
	 * 
	 */
	public void setValue(int value, String str) {
		progressBar.setProgress((double) value/max);
		progressLabel.setText(str);
		
		if(progressBar.getProgress() >= 1.0) {
			theView.close();
		}
	}
	
	// Getters
	public int getValue() {
		//You have to add 0.5 to make the rounding correct.
		//Happened in one case where 60.999999999 kept rounding to 60, creating major issue's.
		return (int) (progressBar.getProgress()*max+0.5);
	}

	// Adders

	// Removers

	// Others Methods
	@FXML
	public void handleClose() {
		theView.close();
	}

	// Implementation Methods
	@Override
	public void initialize(View view) {
		theView = view;
		
		//Inserting the correct values into it's corresponding place...
		
		//PreferencesView(Window)
		view.setTitle(TITLE);
		view.setResizable(false);
	}

	// Internal Classes

}
