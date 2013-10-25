package gui.dialog;

import java.io.PrintWriter;
import java.io.StringWriter;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.VBox;
import gui.Controller;
import gui.Model;
import gui.View;

public class ExceptionController implements Controller {
	
	Model	theModel;
	View	theView;
	
	@FXML private VBox				rootPanel;
	
	private String message;
	@FXML private Label messageLabel;
	
	private Throwable t;
	@FXML private TextArea exceptionArea;

	public ExceptionController(String message, Throwable t) {
		this.message = message;
		this.t = t;
	}
	
	@FXML
	public void handleReport() {
		// TODO Auto-generated method stub
	}
	
	@FXML
	public void handleExit() {
		System.exit(0);
	}

	@Override
	public void initialize(View view) {
		this.theView = view;
		
		messageLabel.setWrapText(true);
		messageLabel.setText(message);
		
		exceptionArea.setText(getPrintStackTraceAsString(t));
		
		view.setTitle("Message");
		view.setResizable(false);
	}
	
	private String getPrintStackTraceAsString(Throwable t) {
		StringWriter sw = new StringWriter();
		t.printStackTrace(new PrintWriter(sw));
		
		return sw.toString();
	}

	@Override
	@FXML
	public void handleClose() {
		theView.close();
	}
}
