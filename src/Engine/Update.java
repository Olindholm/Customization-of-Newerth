package Engine;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
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
import org.xml.sax.SAXException;

import Doodads.CElement;
import Doodads.LLFile;


public class Update {
	//STATIC variables;
	
	//Variables;
	
	//Setup;
	
	//Constructor;
	public Update(Main main) {
		main.gui.progressbar.setValue(0);
		main.gui.progresslabel.setText("Connecting to update server...");
		
		try {
			URLConnection c = new URL(main.config.property.getProperty("updateurl","https://dl.dropboxusercontent.com/u/38414202/Customization%20of%20Newerth/changeset.xml")).openConnection();
			c.setConnectTimeout(10000);
			
			Element changeset = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(c.getInputStream()).getDocumentElement();
			int length = Integer.parseInt(changeset.getAttribute("length"));
			
			NodeList changesets = changeset.getElementsByTagName("Change");
			main.gui.progressbar.setMaximum(changesets.getLength());
			
			Vector<CElement> changes = new Vector<CElement>();
			
			int cv = (int) (Integer.parseInt(Main.VERSION.replace(".",""))*Math.pow(10,length-Main.VERSION.replace(".","").replace(".","").length()));
			System.out.println(cv);
			
			for(int i = 0;i <= changesets.getLength()-1;i++) {
				Element e = (Element) changesets.item(i);
				int ev = (int) (Integer.parseInt(e.getAttribute("version").replace(".",""))*Math.pow(10,length-e.getAttribute("version").replace(".","").length()));
				System.out.println(ev);
				
				if(ev > cv) {
					CElement ce = new CElement(e);
					ce.setLength(length);
					
					changes.add(ce);
				}
				main.gui.progressbar.setValue(main.gui.progressbar.getValue()+1);
			}
			main.gui.progressbar.setMaximum(main.gui.progressbar.getMaximum()+changes.size());
			Collections.sort(changes);
			
			if(changes.size() > 0) {
				main.gui.progresslabel.setText("Update "+changes.get(changes.size()-1).getElement().getAttribute("version")+" found...");

				Vector<String[]> changelist = new Vector<String[]>();
				for(int i = 0;i <= changes.size()-1;i++) {
					main.gui.progresslabel.setText("Calculating data from "+changes.get(i).getElement().getAttribute("version")+"...");
					NodeList list = changes.get(i).getElement().getChildNodes();
					
					for(int ii = 0;ii <= list.getLength()-1;ii++) {
						if(list.item(ii).getNodeType() == Node.ELEMENT_NODE) {
							Element change = (Element) list.item(ii);
							
							for(int iii = 0;iii <= changelist.size()-1;iii++) {
								if(changelist.get(iii)[2].equals(change.getAttribute("file"))) {
									changelist.remove(iii);
									
									break;
								}
							}
							/*
							 * 0 = version
							 * 1 = tagname
							 * 2 = file
							 */
							String[] s = {changes.get(i).getElement().getAttribute("version"),change.getTagName(),change.getAttribute("file")};
							changelist.add(s);
						}
					}
					main.gui.progressbar.setValue(main.gui.progressbar.getValue()+1);
				}
				
				//Now for the downloading...
				main.gui.progressbar.setMaximum(main.gui.progressbar.getMaximum()+(changelist.size()*2));
				
				for(int i = 0;i <= changelist.size()-1;i++) {
					String[] s = changelist.get(i);
					
					if(s[1].equals("Add")) {
						main.gui.progresslabel.setText("Downloading "+s[2]+"...");
						
						URLConnection connection = new URL(changeset.getAttribute("url")+s[0]+"/"+s[2]).openConnection();
						connection.setConnectTimeout(10000);

						LLFile file = new LLFile(Main.PATH+"update"+File.separator+s[2],true);
						
						InputStream in = connection.getInputStream();
						FileOutputStream ut = new FileOutputStream(file);
						byte[] buffer = new byte[1024];
						int len;
						
						while((len = in.read(buffer)) >= 0) {
							ut.write(buffer,0,len);
						}
						
						in.close();
						ut.close();
					}
					
					System.out.println("["+s[0]+"]["+s[1]+"]: "+s[2]);
					main.gui.progressbar.setValue(main.gui.progressbar.getValue()+1);
				}
				for(int i = 0;i <= changelist.size()-1;i++) {
					String[] s = changelist.get(i);
					
					if(s[1].equals("Add")) {
						main.gui.progresslabel.setText("Adding/Replacing "+s[2]+"...");
						

						LLFile infile = new LLFile(Main.PATH+"update"+File.separator+s[2]);
						LLFile file = new LLFile(Main.PATH+s[2],true);
						
						InputStream in = new FileInputStream(infile);
						FileOutputStream ut = new FileOutputStream(file);
						byte[] buffer = new byte[1024];
						int len;
						
						while((len = in.read(buffer)) >= 0) {
							ut.write(buffer,0,len);
						}
						
						in.close();
						ut.close();
						infile.delete();
					}
					else if(s[1].equals("Remove")) {
						main.gui.progresslabel.setText("Removing "+s[2]+"...");
						new LLFile(Main.PATH+s[2]).delete();
					}
					
					System.out.println("["+s[0]+"]["+s[1]+"]: "+s[2]);
					main.gui.progressbar.setValue(main.gui.progressbar.getValue()+1);
				}
				
				String[] cmd = {"java","-jar",Main.PATH+"jcustom.jar"};
				Runtime.getRuntime().exec(cmd);
				System.exit(0);
				
			}
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (SAXException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ParserConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
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
