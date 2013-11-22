package gui.progress;

import javafx.fxml.FXML;
import javafx.scene.control.*;

import gui.*;

public class ProgressController extends StandardController {
	// STATIC Variables
	public static final String TITLE = "Progress";

	// STATIC Methods

	// Variables
	@FXML private ProgressBar	progressBar;
	@FXML private Label			progressLabel;
	
	int max		= 100;
	
	// Constructors
	public ProgressController(View view) {
		super(view, null, "gui/progress/ProgressView.fxml");
		
		theView.setTitle(TITLE);
		theView.setResizable(false);
	}
	
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

	// Internal Classes

}
