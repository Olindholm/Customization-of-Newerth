package gui.project;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import gui.*;

public class ProjectController implements Controller, ChangeListener<Number> {
	public static final String TITLE = "New Project";
	
	public static final String SCHEMESTART = "Force All to ";
	public static final String SCHEMEEND = " Avatar";
	public static final String[] SCHEME = {"No Scheme", SCHEMESTART + "Default" + SCHEMEEND, SCHEMESTART + "1st" + SCHEMEEND, SCHEMESTART + "2nd" + SCHEMEEND, SCHEMESTART + "3rd" + SCHEMEEND};
	
	Model	theModel;
	View	theView;
	
	@FXML public TextField		name;
	
	@FXML public RadioButton	emptyProject;
	@FXML public RadioButton	copyProject;
	
	@FXML public Label			schemeLabel;
	@FXML public Slider			scheme;
	
	public ProjectController(Model model) {
		theModel = model;
	}
	
	@FXML
	public void handleOK() {
		//Saving preferences...
		theModel.set("projectScheme", "" + ((int) scheme.getValue()));
		
		theView.close(true);
	}
	
	@Override
	public void initialize(View view) {
		this.theView = view;
		
		//Inserting the correct values into it's corresponding place...
		scheme.setValue(theModel.getInt("projectScheme", Project.SCHEME_NONE));
		changed(null, null, scheme.getValue());
		
		//Adding some listeners...
		scheme.valueProperty().addListener(this);
		name.textProperty().addListener(new ChangeListener<String>() {

			@Override 
			public void changed(ObservableValue<? extends String> arg0, String oldText, String newText) {
				if(!newText.matches("[0-9a-zA-Z]*")) {
					name.setText(oldText);
				}
			}
			
		});
		
		//PreferencesView(Window)
		view.setTitle(TITLE);
		view.getIcons().add(new Image(ClassLoader.getSystemResourceAsStream("gui/res/icon.png")));
		view.setResizable(false);
	}
	@Override
	@FXML
	public void handleClose() {
		theView.close();
	}
	
	
	@Override
	public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
		//Figure index;
		int index;
		if(newValue.floatValue() > 0.0) {
			index = (int) (newValue.floatValue()+0.5);
		}
		else {
			index = (int) (newValue.floatValue()-0.5);
		}
		
		//Set label
		if((index+1) < SCHEME.length) {
			schemeLabel.setText(SCHEME[index+1]);
		}
		else {
			schemeLabel.setText(SCHEMESTART + index+"th" + SCHEMEEND);
		}
	}
}
