package Doodads;

import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.Vector;

import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;

public class LLFile extends File {
	//STATIC variables;
	
	//Variables;
	
	//Setup;
	
	//Constructor;
	public LLFile(String path) throws IOException {
		super(path);
		
		if(!exists()) {
			if(new File(path).getParentFile() != null) {
				new File(path).getParentFile().mkdirs();
			}
			createNewFile();
		}
	}
	public LLFile(String path,boolean newfile) throws IOException {
		super(path);
		if(newfile) {
			if(new File(path).getParentFile() != null) {
				new File(path).getParentFile().mkdirs();
			}
			//This is technically incorrect if I've understood the documentation correctly, I should first check if it exists and then delete it if it does before creating a new one.
			//However I found that to be very buggy and just throwing expections everything and this still works(or atleast doesn't seem to fuck up) so...
			
			boolean isCreated = false;
			
			try {
				//System.out.println(exists());
				isCreated = createNewFile();
				
				//System.out.println(isCreated); 
				//System.out.println(exists());
			} catch (IOException e) {
				e.printStackTrace();
				throw e;
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
		if((System.getProperty("os.arch").indexOf("64") != -1)) {
			return new String[] {System.getenv("ProgramFiles(x86)")+File.separator,System.getenv("ProgramFiles")+File.separator};
		}
		else {
			return new String[] {System.getenv("ProgramFiles")+File.separator};
		}
	}
	
	//Add;
	
	//Remove;
	
	//Do;
	public static File chooseFile(String title,String[] filters,File directory) {
		JFileChooser chooser = new JFileChooser();
		chooser.setAcceptAllFileFilterUsed(false);
		chooser.setDialogTitle(title);
		chooser.setCurrentDirectory(directory);
		
		if(filters != null) {
			for(int i = 0;i <= filters.length-1;i++) {
				if(filters.equals("*")) {
					chooser.setAcceptAllFileFilterUsed(true);
				}
				else {
					chooser.addChoosableFileFilter(new FileNameExtensionFilter(filters[i],filters[i+1]));
					i++;
				}
			}
		}
		else {
			chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		}
		
		int option;
		while((option = chooser.showOpenDialog(null)) == JFileChooser.APPROVE_OPTION) {
	    	File f = chooser.getSelectedFile();
	    	if(f.exists()) {
	    		return f;
		    }
		}
		return null;
	}
	
	//Other;
	public File[] getAllFiles() {
		Vector<File> files = new Vector<File>();
		String[] array = list();
		
		for(int i = 0;i <= array.length-1;i++) {
			LLFile file = null;
			try {
				file = new LLFile(getAbsolutePath()+File.separator+array[i],false);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			if(!file.isDirectory()) {
				files.add(file);
			}
			else {
				Collections.addAll(files,file.getAllFiles());
			}
		}
		
		return files.toArray(new File[files.size()]);
	}
	
	//Implements;
	
}
