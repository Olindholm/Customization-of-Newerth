package gui.main;

import gui.*;
import gui.util.*;
import gui.preferences.PreferencesController;
import gui.progress.ProgressController;
import gui.project.ProjectModel;
import gui.project.ProjectController;

import java.awt.Desktop;
import java.io.*;
import java.util.zip.ZipFile;

import res.ResourceExtractor;
import res.ResourceTransformer;
import res.ent.Hero;

import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.image.Image;
import javafx.scene.layout.VBox;
import javafx.scene.control.*;
import javafx.stage.*;
import javafx.stage.FileChooser.ExtensionFilter;


public class MainController implements Controller, ChangeListener<Hero> {
	// STATIC Variables
	public static final String TITLE = "Customization of Newerth";
	
	// STATIC Methods
	
	// Variables
	MainModel	theModel;
	View		theView;
	
	@FXML private VBox				rootPanel;
	
	@FXML private Menu				projectMenu;
	
	@FXML private TextField			heroFilter;
	@FXML private ListView<Hero>	heroList;
	
	// Constructors
	public MainController(MainModel model) {
		theModel = model;
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
	public void handleHotkey() {
		//Ctrl+A		= Apply
		//Ctrl+Shift+A	= Apply & Launch
		//Alt+F4		= Close
		
		//Ctrl+N		= New
		//Ctrl+O		= Open
		//Ctrl+E		= Edit
		//Ctrl+S		= Save As...
		
		//F5			= Refresh
		
		//F8			= Preferences
		
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
		ProjectModel model = new ProjectModel();
		View view = new View("gui/project/ProjectView.fxml", new ProjectController(model));
		view.initModality(Modality.WINDOW_MODAL);
		view.setOwner(theView);
		
		if(view.showDialog()) {
			setProject(model);
		}
	}
	private void setProject(ProjectModel model) {
		theModel.project = model;
		theModel.setString("projectFile", "");
		
		//Load in the new proeprties...
		loadMap(heroList.getSelectionModel().getSelectedItem());
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
			theModel.project = new ProjectModel(new FileInputStream(projectFile));
			theModel.setString("projectFile", projectFile.getPath());
		} catch (IOException e) {
			throw new RuntimeException("Could not read or access " + projectFile.getPath(), e);
		}
		
		//Load in the new proeprties...
		loadMap(heroList.getSelectionModel().getSelectedItem());
	}
	@FXML
	public void handleEdit() {
		//Saving current Map to Project in case the user wants to modify the project.
		saveMap(heroList.getSelectionModel().getSelectedItem());
		
		View view = new View("gui/project/ProjectView.fxml", new ProjectController(theModel.project));
		view.initModality(Modality.WINDOW_MODAL);
		view.setOwner(theView);
		view.showAndWait();
		
		loadMap(heroList.getSelectionModel().getSelectedItem());
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
		saveMap(heroList.getSelectionModel().getSelectedItem());
		
		try {
			theModel.project.store(new FileOutputStream(projectFile), null);
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
	public void handleRefreshResources() throws IOException {
		//Creating a progress gui;
		final ProgressController progress = new ProgressController();
		final View view = new View("gui/Progress/ProgressView.fxml", progress);
		view.initModality(Modality.WINDOW_MODAL);
		view.setOwner(theView);
		view.show();
		
		//Flushing current resources
		heroList.getItems().clear();
		
		//Collecting new resources...
		ResourceExtractor extractor = new ResourceExtractor(new ZipFile(theModel.getString("resourceFile")));
		final ResourceTransformer transformer = new ResourceTransformer(extractor);
		progress.setMaxiumum(transformer.totalElements());
		
		int threads = theModel.getInt("threading");
		for(int i = 0;i < threads;i++) {
			new Thread(new Runnable() {
				
				@Override
				public void run() {
					while(transformer.remainingElements() > 0 && !view.isClosed()) {
						final Hero hero = transformer.nextElement();
						
						Platform.runLater(new Runnable() {
							
							@Override
							public void run() {
								heroList.getItems().add(hero);
								progress.setValue(progress.getValue()+1, hero.toString() + "...");
							}
							
						});
					}
				}
				
			}).start();
		}
	}
	@FXML
	public void handlePreferences() {
		View view = new View("gui/preferences/PreferencesView.fxml", new PreferencesController(theModel));
		view.initModality(Modality.WINDOW_MODAL);
		view.setOwner(theView);
		view.showAndWait();
	}
	@Override
	@FXML
	public void handleClose() {
		//Saving properties for next run
		theModel.setInt("mainWidth", (int) theView.getWidth());
		theModel.setInt("mainHeight", (int) theView.getHeight());
		
		//Saving the Config
		theModel.store(new File("config.properties"), null);
		
		//Saving the project...
		File projectFile = new File(theModel.getString("projectFile"));
		if(projectFile.exists()) { //For autosave the file is required to exist.
			saveProject(projectFile);
		}
		else {
			handleSaveAs();
		}
		//... and the config
		theModel.store(new File("config.properties"), null);
		
		//Finally closing the stage resulting the progam to exit.
		theView.close();
	}
	
	// Implementation Methods
	@SuppressWarnings("unchecked")
	@Override
	public void initialize(View view) {
		this.theView = view;
		
		//ListView(Heroes)
		SortedObservableList<Hero> sol = SortedObservableList.newInstance();
		FilterableObservableList<Hero> fol = new FilterableObservableList<Hero>(sol);
		
		heroList.setItems(fol);
		heroList.getSelectionModel().selectedItemProperty().addListener(this);
		
		//Project
		File projectFile = new File(theModel.getString("projectFile"));
		if(projectFile.exists()) {
			loadProject(projectFile);
		}
		else {
			setProject(new ProjectModel());
		}
		
		//MainView(Window)
		view.setTitle(TITLE);
		view.getIcons().add(new Image(ClassLoader.getSystemResourceAsStream("gui/main/icon.png")));
		
		view.setWidth(theModel.getInt("mainWidth"));
		view.setHeight(theModel.getInt("mainHeight"));
		
		view.setMinWidth(rootPanel.getMinWidth());
		view.setMinHeight(rootPanel.getMinHeight());
	}
	@Override
	public void changed(ObservableValue<? extends Hero> observable, Hero oldHero, Hero newHero) {
		saveMap(oldHero);
		loadMap(newHero);
	}
	public void saveMap(Hero hero) {
		//if(hero != null) {
			System.out.println("Saving " + hero);
		//}
	}
	public void loadMap(Hero hero) {
		//if(hero != null) {
			System.out.println("Loading " + hero);
		//}
	}
	
	// Internal Classes
	
}
