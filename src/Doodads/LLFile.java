package Doodads;

import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.Vector;

import javax.swing.JFileChooser;

public class LLFile extends File {
	//STATIC variables;
	
	//Variables;
	
	//Setup;
	
	//Constructor;
	public LLFile(String path) {
		super(path);
		if(!exists()) {
			if(new File(path).getParentFile() != null) {
				new File(path).getParentFile().mkdirs();
			}
			try {
				createNewFile();
			} catch (IOException e) {
			}
		}
	}
	public LLFile(String path,boolean newfile) {
		super(path);
		if(newfile) {
			if(new File(path).getParentFile() != null) {
				new File(path).getParentFile().mkdirs();
			}
			try {
				createNewFile();
			} catch (IOException e) {
			}
		}
	}
	//Set;
	
	//Get;
	public static String getAppData() {
		return System.getenv("APPDATA")+File.separator;
	}
	public static String getDesktop() {
		return new JFileChooser().getFileSystemView().getHomeDirectory().toString()+File.separator;
	}
	public static String getUser() {
		return new JFileChooser().getFileSystemView().getHomeDirectory().getParent().toString()+File.separator;
	}
	public static String getPublic() {
		return System.getenv("PUBLIC")+File.separator;
	}
	public static String[] getProgram() {
		if(new LLFile(System.getenv("ProgramFiles(x86)")+"",false).exists()) {
			return new String[] {System.getenv("ProgramFiles(x86)")+File.separator,System.getenv("ProgramFiles")+File.separator};
		}
		else {
			return new String[] {System.getenv("ProgramFiles")+File.separator};
		}
	}
	
	//Add;
	
	//Remove;
	
	//Do;
	
	//Other;
	public File[] getAllFiles() {
		Vector<File> files = new Vector<File>();
		String[] array = list();
		
		for(int i = 0;i <= array.length-1;i++) {
			LLFile file = new LLFile(getAbsolutePath()+File.separator+array[i],false);
			
			if(!file.isDirectory()) {
				files.add(file);
			}
			else {
				Collections.addAll(files,file.getAllFiles());
			}
		}
		
		return files.toArray(new File[files.size()]);
	}
	/*private File[] getAllFiles(File file) {
		File[] files = 
	}*/
	
	//Implements;
	
}
