package Main;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import javax.swing.JOptionPane;

import Engine.Config;
import Engine.Gui;
import Engine.Update;
import Lindholm.com.LLExplorer;
import Lindholm.com.LLLogPrinter;
import Lindholm.graphic.LLImageHandler;

public class Main {
	public static void main(String[] args) {
		new Main();
	}
	//Variables;
	public LLLogPrinter		log;
	public Config 			config;
	public LLImageHandler	images;
	public Gui				gui;
	public Update			update;
	//Engine;
	public	String	EngNme = "Customization of Newerth";
	public	String	EngVer = "2.07";
	public	String	EngSte = "Unleashing the beast";
	public	String	EngDir = LLExplorer.getAppData()+"Lindholm"+File.separator+"customization-of-newerth"+File.separator;
	public	String	EngUrl = "http://customization-of-newerth.googlecode.com/svn/tags/";
	public	String	EngSrc = "";
	//Jars
	public	String	JarUrl = "http://customization-of-newerth.googlecode.com/files/";
	//Credits;
	public	String credit = "" +
			"Xplitter		-	For teaching me the basic's about mod applying.\n" +
			"saTo			-	For his All alter. application, I learned alot from his code.\n" +
			"SHiRKiT			-	For helping me out with better java methods, also for his cooperation the with HoNMoDManager.\n" +
			"SoulBeaver		-	For helping me with some debugging and keeping me on track.\n" +
			"Lanboost		-	For teaching me the basic's about Java.\n" +
			"Lindholm-Library	-	For letting me use their library.\n" +
			"Rean			-	For explaining how the models/animations work.\n" +
			"Redhay			-	For helping out importing entities in the CoN Handler.\n" +
			"Coke			-	For keeping my brain work properly.\n" +
			"S2Games			-	For launching Heroes of Newerth.\n" +
			"Community		-	Not to forget all the people who are using this and supporting this project.\n" +
			"To all people above I am very grateful.";
	//Constructors;
	public Main() {
		try {
			//Creating Eng folders;
			new File(EngDir).mkdirs();
			//Setting up a ExpetionLogger;
			log = new LLLogPrinter(EngDir+"exception.log");
			//Booting up;
			config = new Config(this);
			images = new LLImageHandler(EngDir+"textures.dat");
			gui = new Gui(this);
			update = new Update(this);
		} catch (FileNotFoundException e) {
			report(1,e);
		} catch (IOException e) {
			report(2,e);
		}
	}
	public void reboot() {
		String[] cmd = {"java","-jar",EngSrc+"(CoN).jar"};
		try {
			Runtime.getRuntime().exec(cmd);
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		System.exit(0);
	}
	public void log(String str) {
		try {
			log.printString(str);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	public void report(int i,Exception e) {
		try {
			log.printException(e);
		} catch (IOException e1) {
			JOptionPane.showMessageDialog(null,	"An error has occured during runtime of this software.\n" +
					"Error code: #"+i+"\n" +
					"\n" +
					"Exception slipped away and could not be logged.",EngNme+" - Error Report",JOptionPane.ERROR_MESSAGE);
			return;
		}
		JOptionPane.showMessageDialog(null,	"An error has occured during runtime of this software.\n" +
											"Error code: #"+i+"\n" +
											"\n" +
											"Exception caught and logged.",EngNme+" - Error Report",JOptionPane.ERROR_MESSAGE);
	}
	/** eRRoR log;
	 * 1	= Could not create/find the log file;
	 * 2	= Could not read the textures.dat;						File not found?;	Access denied?;	Corrupted file?;
	 * 3	= Could not read HoN resources.s2z;						File not found?;	Access denied?;	Corrupted file?;
	 * 4	= Could not download/read resources_version.sht;											Corrupted file?;	Connection timed out?;
	 * 5	= Could not download/read resources.dat | textures.dat;										Corrupted file?;	Connection timed out?;
	 * 6	= Could not read the resources.dat;						File not found?;	Access denied?;	Corrupted file?;
	 * 7	= Could not write to name.honmod;											Access denied?;
	 * 8	= Could not download/read version.str;														Corrupted file?;	Connection timed out?;
	 * 9	= Could not download/read (CoN).jar;					File not found?;										Connection timed out?;
	 * 10	= Could not download/read changelog.dat;													Corrupted file?;	Connection timed out?;
	 * 11	= Could not download/read buglog.str;														Corrupted file?;	Connection timed out?;
	 * 12	= Could not read specified file inside resources.s2z;	File not found?;
	 * 13	= Could not read specified parts from read file;																						Specified string not found;
	 * 14	= Could not read specified parts from read file;		File not found?;	Access denied?;
	 */
}
