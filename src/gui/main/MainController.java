package gui.main;

import gui.*;
import gui.util.*;
import gui.preferences.PreferencesController;
import gui.project.Project;
import gui.project.ProjectController;

import java.awt.Desktop;
import java.io.*;
import java.util.zip.ZipFile;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.image.Image;
import javafx.scene.layout.VBox;
import javafx.scene.control.*;
import javafx.stage.*;
import javafx.stage.FileChooser.ExtensionFilter;

import util.ResourceLoader;
import util.ent.Hero;

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
		File projectFile = new File(theModel.getString("projectFile", ""));
		if(projectFile.exists()) { //For autosave the file is required to exist.
			saveProject(projectFile);
		}
		else {
			handleSaveAs();
		}
		
		//Now to the action...
		View view = new View("gui/project/ProjectView.fxml", new ProjectController(theModel));
		view.initModality(Modality.WINDOW_MODAL);
		view.setOwner(theView);
		if(view.showDialog()) {
			createProject(theModel.getInt("projectScheme", Project.SCHEME_NONE));
		}
	}
	private void createProject(int scheme) {
		theModel.project = new Project(scheme);
		theModel.set("projectFile", "");
		
		//Load in the new proeprties...
		loadMap(heroList.getSelectionModel().getSelectedItem());
	}
	@FXML
	public void handleOpen() {
		//Saving the current project...
		File projectFile = new File(theModel.getString("projectFile", ""));
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
			theModel.project = new Project(new FileInputStream(projectFile));
			theModel.set("projectFile", projectFile.getPath());
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
			theModel.set("projectFile", projectFile.getPath());
		} catch (IOException e) {
			throw new RuntimeException("Could not write to or access " + projectFile.getPath(), e);
		}
	}
	@FXML
	public void handleViewGameFolder() throws IOException {
		Desktop explorer = Desktop.getDesktop();
		File resourceFile = new File(theModel.getString("resourceFile", ""));
		File gameFolder = resourceFile.getParentFile();
		
		//explorer.open(gameFolder); removed due to sometimes trying to run files with similar names to the gameFolder.
		explorer.browse(gameFolder.toURI());
	}
	@FXML
	public void handleRefreshResources() throws IOException {
		ResourceLoader loader = new ResourceLoader(new ZipFile(theModel.getString("resourceFile", "")));
		loader.toString(); //Removes the Warning of the loader being useless, for the time being.
		
		
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
		theModel.set("mainWidth", "" + ((int) theView.getWidth()));
		theModel.set("mainHeight", "" + ((int) theView.getHeight()));
		
		//Saving the Config
		theModel.store(new File("config.properties"), null);
		
		//Saving the project...
		File projectFile = new File(theModel.getString("projectFile", ""));
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
		File projectFile = new File(theModel.getString("projectFile", ""));
		if(projectFile.exists()) {
			loadProject(projectFile);
		}
		else {
			createProject(Project.SCHEME_NONE);
		}
		
		//MainView(Window)
		view.setTitle(TITLE);
		view.getIcons().add(new Image(ClassLoader.getSystemResourceAsStream("gui/res/icon.png")));
		
		view.setWidth(theModel.getInt("mainWidth", (int) rootPanel.getWidth()));
		view.setHeight(theModel.getInt("mainHeight", (int) rootPanel.getHeight()));
		
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
