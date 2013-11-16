package gui.exception;

import java.io.PrintWriter;
import java.io.StringWriter;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.layout.VBox;

import gui.*;

public class ExceptionController extends StandardController {
	
	Model	theModel;
	View	theView;
	
	@FXML private VBox				rootPanel;
	
	private String theMessage;
	@FXML private Label messageLabel;
	
	private Throwable theTrowable;
	@FXML private TextArea exceptionArea;

	public ExceptionController(View view, String message, Throwable trowable) {
		super(view, null, "gui/exception/ExceptionView");
		theMessage = message;
		theTrowable = trowable;
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
	public void initialize() {
		messageLabel.setWrapText(true);
		messageLabel.setText(theMessage);
		
		exceptionArea.setText(getPrintStackTraceAsString(theTrowable));
		
		theView.setTitle("Message");
		theView.getIcons().add(new Image(ClassLoader.getSystemResourceAsStream("gui/exception/icon.png")));
		theView.setResizable(false);
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
