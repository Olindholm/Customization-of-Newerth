package gui.main;

import gui.*;
import gui.util.*;
import gui.project.*;
import gui.progress.ProgressController;
import gui.preferences.PreferencesController;

import java.io.*;
import java.awt.Desktop;
import java.util.zip.ZipFile;

import res.*;
import res.ent.*;

import javafx.fxml.FXML;
import javafx.stage.*;
import javafx.beans.value.*;
import javafx.scene.layout.*;
import javafx.scene.control.*;
import javafx.scene.input.*;
import javafx.application.Platform;
import javafx.scene.image.Image;
import javafx.stage.FileChooser.ExtensionFilter;

public class MainController extends StandardController implements ChangeListener<Toggle> {
	// STATIC Variables
	
	// STATIC Methods
	
	// Variables
	@FXML private VBox				rootPanel;
	
	@FXML private TextField			heroFilter;
	@FXML private ListView<Hero>	heroList;
	
	// Constructors
	public MainController(View view, MainModel model) {
		super(view, model, "gui/main/MainView.fxml");
	}
	
	// Setters
	
	// Getters
	
	// Adders
	
	// Removers
	
	// Is
	
	// Others Methods
	@SuppressWarnings("unchecked")
	@FXML
	public void handleHeroFilter() {
		FilterableObservableList<Hero> fol = (FilterableObservableList<Hero>) heroList.getItems();
		fol.setFilter(heroFilter.getText());
	}
	@FXML
	public void handleHotkey(KeyEvent event) throws IOException {
		//Ctrl+A		= Apply
		//Ctrl+Shift+A	= Apply & Launch
		//Alt+F4		= Close
		
		//Ctrl+N		= New
		//Ctrl+O		= Open
		//Ctrl+E		= Edit
		//Ctrl+S		= Save As...
		
		//F5			= Refresh
		if(event.getCode() == KeyCode.F5) {
			handleRefreshResources();
		}
		//F8			= Preferences
		else if(event.getCode() == KeyCode.F8) {
			handlePreferences();
		}
		//F1			= Help Instructions
	}
	@FXML
	public void handleApply() {
		//TODO;
	}
	@FXML
	public void handleApplyNLaunch() {
		handleApply();
		//Launch;
		//TODO;
	}
	@FXML
	public void handleUnapply() {
		//TODO;
	}
	@FXML
	public void handleNew() {
		//Saving the current project...
		File projectFile = new File(theModel.getString("projectFile"));
		if(projectFile.exists()) { //For autosave the file is required to exist.
			saveProject(projectFile);
		}
		else {
			handleSaveAs();
		}
		
		//Now to the action...
		Controller controller = new ProjectController(new View(), new ProjectModel());
		View view = controller.getView();
		view.initModality(Modality.WINDOW_MODAL);
		view.initOwner(theView);
		
		if(view.showDialog()) {
			setProject((ProjectModel) controller.getModel());
		}
	}
	private void setProject(ProjectModel model) {
		((MainModel) theModel).project = model;
		theModel.setString("projectFile", "");
		
		//Load in the new proeprties...
		reloadMaps();
	}
	@FXML
	public void handleOpen() {
		//Saving the current project...
		File projectFile = new File(theModel.getString("projectFile"));
		if(projectFile.exists()) { //For autosave the file is required to exist.
			saveProject(projectFile);
		}
		else {
			handleSaveAs();
		}
		
		//Now to the action...
		FileChooser fileChooser = new FileChooser();
		fileChooser.getExtensionFilters().add(new ExtensionFilter("Project File","*.ini"));
		File chosenFile = fileChooser.showOpenDialog(theView);
		
		if(chosenFile != null) {
			loadProject(chosenFile);
		}
	}
	private void loadProject(File projectFile) {
		try {
			((MainModel) theModel).project = new ProjectModel(new FileInputStream(projectFile));
			theModel.setString("projectFile", projectFile.getPath());
		} catch (IOException e) {
			throw new RuntimeException("Could not read or access " + projectFile.getPath(), e);
		}
		
		//Load in the new proeprties...
		reloadMaps();
	}
	@FXML
	public void handleEdit() {
		//Saving current Map to Project in case the user wants to modify the project.
		saveMaps();
		
		Controller controller = new ProjectController(new View(), ((MainModel) theModel).project);
		View view = controller.getView();
		view.initModality(Modality.WINDOW_MODAL);
		view.initOwner(theView);
		view.showAndWait();
		
		reloadMaps();
	}
	@FXML
	public void handleSaveAs() {
		FileChooser fileChooser = new FileChooser();
		fileChooser.getExtensionFilters().add(new ExtensionFilter("Project File","*.ini"));
		File chosenFile = fileChooser.showSaveDialog(theView);
		
		if(chosenFile != null) {
			saveProject(chosenFile);
		}
	}
	private void saveProject(File projectFile) {
		//Saving the current properties
		saveMaps();
		
		try {
			((MainModel) theModel).project.store(new FileOutputStream(projectFile), null);
			theModel.setString("projectFile", projectFile.getPath());
		} catch (IOException e) {
			throw new RuntimeException("Could not write to or access " + projectFile.getPath(), e);
		}
	}
	@FXML
	public void handleViewGameFolder() throws IOException {
		Desktop explorer = Desktop.getDesktop();
		File resourceFile = new File(theModel.getString("resourceFile"));
		File gameFolder = resourceFile.getParentFile();
		
		//explorer.open(gameFolder); removed due to sometimes trying to run files with similar names to the gameFolder.
		explorer.browse(gameFolder.toURI());
	}
	@FXML
	public void handleRefreshResources() {
		//Creating a progress gui;
		final ProgressController controller = new ProgressController(new View());
		View view = controller.getView();
		view.initModality(Modality.WINDOW_MODAL);
		view.initOwner(theView);
		view.show();
		
		//Flushing current resources
		heroList.getItems().clear();
		
		//Collecting new resources...
		try {
			ResourceExtractor extractor = new ResourceExtractor(new ZipFile(theModel.getString("resourceFile")));
			final ResourceTransformer transformer = new ResourceTransformer(extractor);
			controller.setMaxiumum(transformer.totalElements());
			
			int threads = theModel.getInt("threading");
			for(int i = 0;i < threads;i++) {
				new Thread(new Runnable() {
					
					@Override
					public void run() {
						while(transformer.remainingElements() > 0 && controller.getView().isShowing()) {
							final Hero hero = transformer.nextElement();
							
							Platform.runLater(new Runnable() {
								
								@Override
								public void run() {
									heroList.getItems().add(hero);
									controller.setValue(controller.getValue()+1, hero.toString() + "...");
								}
								
							});
						}
					}
					
				}).start();
			}
		} catch(IOException e) {
			throw new RuntimeException("Could not read to or access " + theModel.getString("resourceFile"), e);
		}
	}
	@FXML
	public void handlePreferences() {
		Controller controller = new PreferencesController(new View(), theModel);
		View view = controller.getView();
		view.initModality(Modality.WINDOW_MODAL);
		view.initOwner(theView);
		view.showAndWait();
	}
	@FXML
	@Override
	public void handleClose() {
		//Saving properties for next run
		theModel.setInt("mainWidth", (int) theView.getWidth());
		theModel.setInt("mainHeight", (int) theView.getHeight());
		
		//Saving the project...
		File projectFile = new File(theModel.getString("projectFile"));
		if(projectFile.exists()) { //For autosave the file is required to exist.
			saveProject(projectFile);
		}
		else {
			handleSaveAs();
		}
		//... and the config
		((MainModel) theModel).store(new File("config.properties"), null);
		
		//Finally closing the stage resulting the progam to exit.
		theView.close();
	}
	
	// Implementation Methods
	@SuppressWarnings("unchecked")
	@Override
	public void initialize() {
		//ListView(Heroes)
		SortedObservableList<Hero> sol = SortedObservableList.newInstance();
		FilterableObservableList<Hero> fol = new FilterableObservableList<Hero>(sol);
		
		heroList.setItems(fol);
		heroList.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Hero>() {

			@Override
			public void changed(ObservableValue<? extends Hero> observable, Hero oldHero, Hero newHero) {
				
			}
			
		});
		
		//Map
		
		//Project
		File projectFile = new File(theModel.getString("projectFile"));
		if(projectFile.exists()) {
			loadProject(projectFile);
		}
		else {
			setProject(new ProjectModel());
		}
		
		//MainView(Window)
		theView.setTitle("Customization of Newerth");
		theView.getIcons().add(new Image(ClassLoader.getSystemResourceAsStream("gui/main/icon.png")));
		
		theView.setWidth(theModel.getInt("mainWidth"));
		theView.setHeight(theModel.getInt("mainHeight"));
		
		theView.setMinWidth(rootPanel.getMinWidth());
		theView.setMinHeight(rootPanel.getMinHeight());
	}
	@Override
	public void changed(ObservableValue<? extends Toggle> observable, Toggle oldToggle, Toggle newToggle) {
		/*
		 * When the toggle is changed the behaviour is really odd,
		 * instead of calling this with the oldToggle and the newToggle
		 * it is called twice...
		 * 
		 * 1. changed(..., oldToggle, null);
		 * 2. changed(..., null, newToggle);
		 * 
		 * To this behaviour a check of the newToggle value is required...
		 */
		if(newToggle != null) {
			//avatarField.
		}
	}
	public void saveMaps() {
	}
	public void reloadMaps() {
	}
	
	// Internal Classes
	
}
