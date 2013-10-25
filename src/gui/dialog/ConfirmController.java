package gui.dialog;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import gui.Controller;
import gui.Model;
import gui.View;

public class ConfirmController implements Controller {
	
	Model	theModel;
	View	theView;
	
	private String message;
	@FXML private Label messageLabel;

	public ConfirmController(String message) {
		this.message = message;
	}
	
	@FXML
	public void handleYes() {
		theView.close(true);
	}

	@Override
	public void initialize(View view) {
		this.theView = view;
		
		messageLabel.setWrapText(true);
		messageLabel.setText(message);
		
		view.setTitle("Message");
		view.setResizable(false);
	}

	@Override
	@FXML
	public void handleClose() {
		theView.close();
	}
}
