package gui;

import java.io.IOException;
import java.io.InputStream;

import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

public class View extends Stage {
	// STATIC Variables

	// STATIC Methods

	// Variables
	private boolean dialog = false;

	// Constructors
	public View(String pathname) throws Exception {
		this(pathname, null);
	}
	public View(String fxmlPath, final Controller controller) {
		// As a Precondition, I'm declaring controller as not null. If you think you will need a null value for the controller,
		// then just comment this line out.
		assert (controller != null): "Controller should not be null! Please pass a valid controller instance.";
		
		FXMLLoader loader = new FXMLLoader();
		loader.setController(controller);
		Parent root = loadRoot(loader, fxmlPath);
		
		controller.initialize(this);
		setOnCloseRequest(new EventHandler<WindowEvent>() {
			
			@Override
			public void handle(WindowEvent we) {
				controller.handleClose();
				we.consume();
			}
			
		});
		
		Scene scene = new Scene(root);
		setScene(scene);
	}
	
	// Try to hide the ugly details of try/catch and the likes behind a function with a descriptive name.
	private Parent loadRoot(FXMLLoader loader, String fxmlPath) {
		try {
			InputStream fxmlStream = ClassLoader.getSystemResourceAsStream(fxmlPath);
			return (Parent) loader.load(fxmlStream);
		} catch(IOException e) {
			throw new RuntimeException("Could not read or access " + fxmlPath, e);
		}
	}
	// Setters

	// Getters
	public boolean isClosed() {
		return !isShowing();
	}

	// Adders

	// Removers

	// Others Methods
	public void setOwner(Stage owner) {
		super.getIcons().addAll(owner.getIcons());
		
		initOwner(owner);
	}
	
	public void close(boolean dialog) {
		this.dialog = dialog;
		
		super.close();
	}
	
	public boolean showDialog() {
		super.showAndWait();
		
		return dialog;
	}

	// Implementation Methods

	// Internal Classes

}
