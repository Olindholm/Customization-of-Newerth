import java.io.File;
import java.io.FileInputStream;
import java.lang.Thread.UncaughtExceptionHandler;

import javax.swing.JOptionPane;

import gui.View;
import gui.dialog.ExceptionController;
import gui.main.MainController;
import gui.main.MainModel;

import javafx.application.Application;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class Main extends Application {
	public static void main(String[] args) {
		try {
			launch(args);
		} catch(Exception e) {
			JOptionPane.showMessageDialog(null, "Something went teribly wrong and we failed to catch the error");
		}
	}
	// STATIC Variables
	
	// STATIC Methods

	// Variables

	// Constructors

	// Setters

	// Getters

	// Adders

	// Removers

	// Others Methods
	
	// Implementation Methods
	@Override
	public void start(Stage stage) {
		Thread.setDefaultUncaughtExceptionHandler(new UncaughtExceptionHandler() {

			@Override
			public void uncaughtException(Thread thread, Throwable t) {
				View view = new View("gui/dialog/ExceptionView.fxml", new ExceptionController("Well, now this is embarrasing!", t));
				view.initModality(Modality.APPLICATION_MODAL);
				view.show();
				
				t.printStackTrace();
			}
			
		});
		
		try {
			MainModel model = new MainModel();
			File file = new File("config.properties");
			//If the system is unable to create the file this must mean it's already existing, then load properties.
			if(!file.createNewFile()) model.load(new FileInputStream(file));
			
			View view = new View("gui/main/MainView.fxml", new MainController(model));
			view.show();
		} catch(Exception e) {//I have to catch this Exception or else the Application thread will die & won't be able to report the Exception to the user.
			//If this throws and exception it will then been caught in the main method.
			Thread.getDefaultUncaughtExceptionHandler().uncaughtException(Thread.currentThread(), e);
		}
	}

	// Internal Classes
}
