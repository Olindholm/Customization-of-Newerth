package Engine;

import java.awt.event.ActionEvent;
import java.io.File;
import java.io.File;


public class Main {
	public static void main(String[] args) {
		new Main();
	}
	//STATIC variables;
	public static final String NAME		= "Customization of Newerth";
	public static final String BRANCH	= "The New Age";
	public static final String VERSION	= "3.0.0.1";
	public static final String PATH		= System.getenv("APPDATA")+File.separator+"Lindholm"+File.separator+NAME+File.separator;
	
	//Variables;
	public	Config	config;
	public	Gui		gui;
	
	//Setup;
	
	//Constructor;
	public Main() {
		this.println(NAME+" - "+BRANCH+" - "+VERSION);
		
		config = new Config(this);
		gui = new Gui(this);
		
		if(config.property.getProperty("update",true)) {
			gui.actionPerformed(new ActionEvent(new Object(),0,Gui.ACTION_UPDATE+""));
		}
		while(gui.run.isAlive()) {
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		if(config.property.getProperty("refresh",true)) {
			gui.actionPerformed(new ActionEvent(new Object(),0,Gui.ACTION_REFRESH+""));
		}
	}
	//Set;
	
	//Get;
	
	//Add;
	
	//Remove;
	
	//Do;
	
	//Other;
	public void println(String s) {
		StackTraceElement[] stackTraceElements = Thread.currentThread().getStackTrace();
		System.out.println("[Info]["+stackTraceElements[2].getClassName()+"]["+stackTraceElements[2].getLineNumber()+"]: "+s);
	}
	public void println(Exception e) {
		StackTraceElement[] stackTraceElements = Thread.currentThread().getStackTrace();
		System.out.println("[Error]["+stackTraceElements[2].getClassName()+"]["+stackTraceElements[2].getLineNumber()+"]: "+e.toString());
	}
	public void exit() {
		config.property.save("test");
		System.exit(0);
	}
	
	//Implements;
	
}