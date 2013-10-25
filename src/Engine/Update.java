package Engine;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.net.MalformedURLException;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.net.URLConnection;
import java.util.Collections;
import java.util.Vector;

import javax.swing.JOptionPane;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import Doodads.CElement;
import Doodads.LLFile;
import Doodads.LLInputStream;


public class Update {
	//STATIC variables;
	
	//Variables;
	
	//Setup;
	
	//Constructor;
	public Update(Main main) {
		main.gui.progressbar.setValue(0);
		main.gui.progresslabel.setText("Connecting to update server...");
		
		String xml;

		try {
			URLConnection connection = new URL(main.config.property.getProperty("url","https://dl.dropboxusercontent.com/u/38414202/Customization%20of%20Newerth/changeset.xml")).openConnection();
			connection.setConnectTimeout(5000);
			
			StringBuilder sb = new StringBuilder();
			LLInputStream in = new LLInputStream(connection.getInputStream());
			main.log.print("Successfully establish a connection with \""+main.config.property.getProperty("url","https://dl.dropboxusercontent.com/u/38414202/Customization%20of%20Newerth/changeset.xml")+"\"");
			
			while(in.available() > 0) {
				sb.append(in.readString(1024*4*4));
			}
			xml = sb.toString();
			
		} catch (MalformedURLException e) {
			main.log.print(e,"Failed to recognize URL protocol \""+main.config.property.getProperty("url","https://dl.dropboxusercontent.com/u/38414202/Customization%20of%20Newerth/changeset.xml")+"\"");
			return;
		} catch (SocketTimeoutException e) {
			main.log.print(e,"Failed to connect to \""+main.config.property.getProperty("url","https://dl.dropboxusercontent.com/u/38414202/Customization%20of%20Newerth/changeset.xml")+"\", connection timed out");
			return;
		} catch (IOException e) {
			main.log.print(e,"Failed to read data from \""+main.config.property.getProperty("url","https://dl.dropboxusercontent.com/u/38414202/Customization%20of%20Newerth/changeset.xml")+"\"");
			return;
		}
		
		String url;
		Vector<String> updates = new Vector<String>();
		Vector<String[]> changelist = new Vector<String[]>();
		
		try {
			Element changeset = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(new InputSource(new StringReader(xml))).getDocumentElement();
			main.log.print("Successfully parsed xml changeset");
			
			int length = Integer.parseInt(changeset.getAttribute("length"));
			int curver = (int) (Integer.parseInt(Main.VERSION.replace(".",""))*Math.pow(10,length-Main.VERSION.replace(".","").replace(".","").length()));
			url = changeset.getAttribute("url");
			
			NodeList changesets = changeset.getElementsByTagName("Change");
			main.gui.progressbar.setMaximum(changesets.getLength());
			
			for(int i = 0;i <= changesets.getLength()-1;i++) {
				Element change = (Element) changesets.item(i);
				
				if(((int) (Integer.parseInt(change.getAttribute("version").replace(".",""))*Math.pow(10,length-change.getAttribute("version").replace(".","").length()))) > curver) {
					updates.add(change.getAttribute("version"));
					
					NodeList changes = change.getChildNodes();
					main.gui.progressbar.setMaximum(main.gui.progressbar.getMaximum()+changes.getLength());
					
					for(int ii = 0;ii <= changes.getLength()-1;ii++) {
						if(changes.item(ii).getNodeType() == Node.ELEMENT_NODE) {
							Element elem = (Element) changes.item(ii);
							/*
							 * 0 = version
							 * 1 = tagname
							 * 2 = file
							 */
							boolean add = true;
							for(int iii = 0;iii <= changelist.size()-1;iii++) {
								if(changelist.get(iii)[2].equals(change.getAttribute("file"))) {
									if(((int) (Integer.parseInt(elem.getAttribute("version").replace(".",""))*Math.pow(10,length-elem.getAttribute("version").replace(".","").length()))) > ((int) (Integer.parseInt(changelist.get(iii)[0].replace(".",""))*Math.pow(10,length-changelist.get(iii)[0].replace(".","").length())))) {
										changelist.remove(iii);
										break;
									}
									else {
										add = false;
										break;
									}
								}
							}
							
							String[] s = {change.getAttribute("version"),elem.getTagName(),elem.getAttribute("file")};
							changelist.add(s);
						}
						main.gui.progressbar.setValue(main.gui.progressbar.getValue()+1);
					}
				}
				main.gui.progressbar.setValue(main.gui.progressbar.getValue()+1);
			}
			
			if(!(updates.size() > 0)) {
				main.log.print("No updates were found");
				return;
			}
			
			String print = "";
			for(int i = 0;i <= updates.size()-1;i++) {
				if(i == updates.size()-1 && i != 0) {
					print += " and "+updates.get(i);
					continue;
				}
				
				print += ", "+updates.get(i);
			}
			main.log.print("Found "+updates.size()+" out of "+changesets.getLength()+" valid updates"+print);
			main.gui.progressbar.setMaximum(main.gui.progressbar.getMaximum()+(changelist.size()*2));
			
		} catch (SAXException | IOException | ParserConfigurationException e) {
			main.log.print(e,"Failed to parse xml changeset");
			return;
		}

		String[] change = null;
		try {
			for(int i = 0;i <= changelist.size()-1;i++) {
				change = changelist.get(i);
				
				if(change[1].equals("Add")) {
					main.gui.progresslabel.setText("Downloading "+change[2]+"...");
					
					URLConnection connection = new URL(url+change[0]+"/"+change[2]).openConnection();
					connection.setConnectTimeout(5000);
					
					LLFile file = new LLFile(Main.PATH+"update"+File.separator+change[2],true);
					LLInputStream in = new LLInputStream(connection.getInputStream());
					main.log.print("Successfully establish a connection with \""+url+change[0]+"/"+change[2]+"\"");
					
					FileOutputStream ut = new FileOutputStream(file);
					byte[] buffer = new byte[4*4*1024];
					int len;
					
					while((len = in.read(buffer)) >= 0) {
						ut.write(buffer,0,len);
					}
					
					in.close();
					ut.close();
				}
				main.gui.progressbar.setValue(main.gui.progressbar.getValue()+1);
			}
		} catch (MalformedURLException e) {
			main.log.print(e,"Failed to recognize URL protocol \""+url+change[0]+"/"+change[2]+"\"");
			return;
		} catch (IOException e) {
			main.log.print(e,"Failed to read or write data from \""+url+change[0]+"/"+change[2]+"\" to "+Main.PATH+"update"+File.separator+change[2]);
			return;
		}
		
		try {
			for(int i = 0;i <= changelist.size()-1;i++) {
				change = changelist.get(i);
				
				if(change[1].equals("Add")) {
					main.gui.progresslabel.setText("Replacing "+change[2]+"...");

					LLFile file = new LLFile(Main.PATH+change[2],true);
					LLInputStream in = new LLInputStream(new FileInputStream(new LLFile(Main.PATH+"update"+File.separator+change[2],true)));
					
					FileOutputStream ut = new FileOutputStream(file);
					byte[] buffer = new byte[4*4*1024];
					int len;
					
					while((len = in.read(buffer)) >= 0) {
						ut.write(buffer,0,len);
					}
					
					in.close();
					ut.close();
				}
				else if(change[1].equals("Remove")) {
					main.gui.progresslabel.setText("Removing "+change[2]+"...");

					new LLFile(Main.PATH+change[2],true).delete();
				}
				main.log.print("["+change[0]+"]["+change[1]+"]: "+change[2]);
				main.gui.progressbar.setValue(main.gui.progressbar.getValue()+1);
			}
		} catch (FileNotFoundException e) {
			main.log.print(e,"Failed to located or create \""+Main.PATH+"update"+File.separator+change[2]+"\", "+Main.PATH+change[2]);
			return;
		} catch (IOException e) {
			main.log.print(e,"Failed to read or write data from \""+Main.PATH+"update"+File.separator+change[2]+"\" to "+Main.PATH+change[2]);
			return;
		}
	}
	//Set;
	
	//Get;
	
	//Add;
	
	//Remove;
	
	//Do;
	
	//Other;
	
	//Implements;
	
}
