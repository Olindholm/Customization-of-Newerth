import java.io.*;
import java.lang.Thread.UncaughtExceptionHandler;

import gui.*;
import gui.main.*;
import gui.exception.ExceptionController;

import javafx.stage.*;
import javafx.application.Application;

public class Main extends Application {
	public static void main(String[] args) {
		launch(args);
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
		Thread.setDefaultUncaughtExceptionHandler(new ExceptionHandler());
		
		try {
			MainModel model = new MainModel();
			File file = new File("config.properties");
			if(!file.createNewFile()) model.load(new FileInputStream(file));
			
			Controller controller = new MainController(new View(), model);
			controller.getView().show();
		} catch(Exception e) {//I have to catch this Exception or else the Application thread will die & won't be able to report the Exception to the user.
			//If this throws and exception it will then been caught in the main method.
			Thread.getDefaultUncaughtExceptionHandler().uncaughtException(Thread.currentThread(), e);
		}
	}

	// Internal Classes
	class ExceptionHandler implements UncaughtExceptionHandler {
	
		@Override
		public void uncaughtException(Thread thread, Throwable throwable) {
			try {
				
				Controller controller = new ExceptionController(new View(), "Obs!?", throwable);
				View view = controller.getView();
				view.initModality(Modality.APPLICATION_MODAL);
				view.show();
				
				throwable.printStackTrace();
			} catch(Exception e) {
				e.printStackTrace();
				throwable.printStackTrace();
			}
		}
	}
}
