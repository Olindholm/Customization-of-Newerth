package Engine;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URLConnection;

import javax.swing.JOptionPane;
import javax.swing.JTextArea;

import Lindholm.data.LLInputStream;
import Lindholm.data.LLOutputStream;
import Lindholm.com.LLFile;
import Lindholm.com.LLUrl;
import Lindholm.ui.LLGui;

import Main.Main;

public class Update {
	//Variables;
	private	Main			main;
	public	LLInputStream	cin;
	//Constructors;
	public Update(Main main) {
		//Saving the main parameter;
		this.main = main;
		//Checking for update
		String version = "";
		try {
			version = getLatestVersion();
		} catch (MalformedURLException e) {
			//This Exception shouldn't get caught;
		} catch (IOException e) {
			main.report(8,e);
		}
		if(Integer.parseInt(version.replace(".","")) > Integer.parseInt(main.EngVer.replace(".",""))) {
			String[] option = {"Yes","No"};
			if(JOptionPane.showOptionDialog(main.gui,version+" is out!\nWould you like to download and install it?","Update available!",JOptionPane.YES_NO_OPTION,-1,null,option,option[1]) == JOptionPane.YES_OPTION) {
				try {
					LLGui gui = new LLGui(); 
					URLConnection uc = LLUrl.connect(main.JarUrl+"(CoN)%20-%20"+version+".jar",10000);
					
					gui.loadProgress(uc.getContentLength()*2);
					LLInputStream in;
					LLOutputStream out;
					in = new LLInputStream(uc.getInputStream());
					out = new LLOutputStream(new FileOutputStream(LLFile.getNewFile(main.EngDir+"(CoN).temp")));
					byte[] buffer = new byte[1024];
					int length;
					while((length = in.read(buffer)) >= 0) {
						out.write(buffer,0,length);
						gui.setProgressValue(gui.getProgressValue()+length);
					}
					in.close();
					out.close();
					in = new LLInputStream(new FileInputStream(main.EngDir+"(CoN).temp"));
					out = new LLOutputStream(new FileOutputStream(main.EngSrc+"(CoN).jar"));
					while((length = in.read(buffer)) >= 0) {
						out.write(buffer,0,length);
						gui.setProgressValue(gui.getProgressValue()+length);
					}
					in.close();
					out.close();
					new File(main.EngDir+"(CoN).temp").delete();
					main.reboot();
				} catch (MalformedURLException e) {
					//This Exception shouldn't get caught;
				} catch (IOException e) {
					main.report(9,e);
				}
			}
		}
		//Changelog download;
		try {
			cin = new LLInputStream(LLUrl.connect(main.EngUrl+"changelog.dat",10000).getInputStream());
			getNextChangelog(main.gui.command_log[0]);
		} catch (MalformedURLException e) {
			//This Exception shouldn't get caught;
		} catch (IOException e) {
			main.report(10,e);
		}
		//Buglog download;
		try {
			URLConnection uc = LLUrl.connect(main.EngUrl+"buglog.str",10000);
			int length = uc.getContentLength();
			LLInputStream in = new LLInputStream(uc.getInputStream());
			main.gui.command_log[1].setText(in.readString(length));
		} catch (MalformedURLException e) {
			//This Exception shouldn't get caught;
		} catch (IOException e) {
			main.gui.command_log[1].setText("Couldn't not download the buglog file.");
			main.report(11,e);
		}
	}
	//get;
	public String getLatestVersion() throws MalformedURLException, IOException {
		URLConnection uc;
		uc = LLUrl.connect(main.EngUrl+"major.str",10000);
		LLInputStream in = new LLInputStream(uc.getInputStream());
		return in.readString(uc.getContentLength());
	}
	public void getNextChangelog(JTextArea log) {
		try {
			if(cin.available() > 0) {
				String s = log.getText().replace("\n\nPress any key to download further changelog history.","");
				log.setText(s+"\n\nDownloading...");
				int length = cin.readShort();
				log.setText(s+cin.readString(length)+"\n\nPress any key to download further changelog history.");
			}
			else {
				log.setText(log.getText().replace("\n\nPress any key to download further changelog history.","\n\nNo further changelog history exists."));
			}
		} catch (MalformedURLException e) {
		} catch (IOException e) {
			main.report(10,e);
		}
	}
	//set;
	//add;
	//rem;
	//other;
}
