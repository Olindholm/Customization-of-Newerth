package Doodads;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import javax.swing.JOptionPane;

public class LLLogger {
	private LLOutputStream ut = null;
	private String prefix;
	
	public LLLogger(String prefix) {
		this.prefix = prefix;
	}
	public LLLogger(String prefix,File file) throws FileNotFoundException {
		this.prefix = prefix;
		ut = new LLOutputStream(new FileOutputStream(file));
	}
	
	public void print(String message) {
		StackTraceElement[] stackTraceElements = Thread.currentThread().getStackTrace();
		String print = prefix+" "+stackTraceElements[2]+": "+message;
		
		if(ut != null) {
			try {
				ut.writeString(print+"\n");
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		System.out.println(print);
	}
	public void print(Exception exception,String message,boolean messageuser) {
		StackTraceElement[] stackTraceElements = Thread.currentThread().getStackTrace();
		String print =	prefix+" "+stackTraceElements[2]+": "+message+", Stack Trace:\n"+
						"	A "+exception.toString().split("\\s")[0].replace(":","")+" caught in \""+Thread.currentThread().getName()+"\" thread";
		
		for(StackTraceElement stacktrace:exception.getStackTrace()) {
			print += "\n		at "+stacktrace.toString();
		}
		
		if(ut != null) {
			try {
				ut.writeString(print+"\n");
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		System.out.println(print);
		if(messageuser) {
			JOptionPane.showMessageDialog(null,message);
		}
	}
}
