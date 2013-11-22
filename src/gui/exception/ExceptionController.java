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
	
	@FXML private VBox				rootPanel;
	
	@FXML private Label messageLabel;
	@FXML private TextArea exceptionArea;

	public ExceptionController(View view, String message, Throwable trowable) {
		super(view, null, "gui/exception/ExceptionView.fxml");
		
		//Init settings
		messageLabel.setWrapText(true);
		messageLabel.setText(message);
		
		exceptionArea.setText(getPrintStackTraceAsString(trowable));
		
		theView.setTitle("Message");
		theView.getIcons().add(new Image(ClassLoader.getSystemResourceAsStream("gui/exception/icon.png")));
		theView.setResizable(false);
	}
	
	@FXML
	public void handleReport() {
		// TODO Auto-generated method stub
	}
	
	@FXML
	public void handleExit() {
		System.exit(0);
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
