package gui.dialog;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import gui.Controller;
import gui.Model;
import gui.View;

public class MessageController implements Controller {
	
	Model	model;
	View	view;
	
	private String message;
	@FXML private Label messageLabel;

	public MessageController(String message) {
		this.message = message;
	}

	@Override
	public void initialize(View view) {
		this.view = view;
		
		messageLabel.setWrapText(true);
		messageLabel.setText(message);
		
		view.setTitle("Message");
		view.setResizable(false);
	}

	@Override
	@FXML
	public void handleClose() {
		view.close();
	}
}
