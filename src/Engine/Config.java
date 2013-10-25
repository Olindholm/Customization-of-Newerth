package Engine;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.util.Collections;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.Vector;
import java.util.zip.ZipEntry;
import java.util.zip.ZipException;
import java.util.zip.ZipFile;

import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;


import Doodads.DDS;
import Doodads.LLAccessories;
import Doodads.LLFile;
import Doodads.LLInputStream;
import Doodads.LLProperty;
import Engine.Attributes.Avatar;
import Engine.Attributes.Hero;

public class Config {
	//STATIC variables;
	
	//Variables;
	private	Main main;
	
	ZipFile textures = null;
	
	public	LLProperty		property;
	public	Vector<Hero>	heroes		= new Vector<Hero>();
	
	//Setup;
	
	//Constructor;
	public Config(Main main) {
		this.main = main;
		
		try {
			property = new LLProperty(Main.PATH+"config.ini");
			main.log.print("Successfully establish config from \""+Main.PATH+"config.ini"+"\"");
		} catch (IOException e) {
			main.log.print(e,"Failed to establish config from \""+Main.PATH+"config.ini"+"\"");
			return;
		}
		
		//Terms of Argement;
	}
	//Set;
	
	//Get;
	public String getPath(String path,String location) {
		if(!path.startsWith("/")) {
			String[] paths = location.split("/");
			String cpath = "/";
			
			for(int i = 0;i <= paths.length-2;i++) {
				cpath += paths[i]+"/";
			}
			path = cpath+path;
		}
		return path;
	}
	public ImageIcon getImage(String path) {
		try {
			InputStream in = textures.getInputStream(textures.getEntry(path));
			
			ByteBuffer b = ByteBuffer.allocate(in.available());
			//System.out.println(in.available());
			while(in.available() > 0) {
				b.put((byte) in.read());
				
			}
			BufferedImage img = DDS.readDxt(b);
			
			img = flipImage(img);
			
			return new ImageIcon(img.getScaledInstance(32,32,java.awt.Image.SCALE_SMOOTH));
			
		} catch (IOException e) {
			e.printStackTrace();
		};
		return null;
	}
	private static BufferedImage flipImage(BufferedImage img) {
		BufferedImage result = new BufferedImage(img.getWidth(), img.getHeight(), BufferedImage.TYPE_INT_ARGB_PRE);
		
		for(int y=0; y<img.getHeight(); y++) {
			for(int x=0; x<img.getWidth(); x++) {
				result.setRGB(x, img.getHeight()-y-1, img.getRGB(x, y));
			}
		}
		
		return result;
	}
	
	//Add;
	
	//Remove;
	
	//Do;
	
	//Other;
	public void refresh() {
		Gui gui = main.gui;
		
		//Clearing out old resources,
		heroes.clear();
		gui.herolist.clear();
		//gui.globalcombobox.clear();
		
		
		gui.progressbar.setValue(0);
		while(!new File(property.getProperty("game","")+"resources0.s2z").exists()) {
			gui.progresslabel.setText("Locating resources... "+property.getProperty("game","")+"resources0.s2z");
			
			JFileChooser chooser = new JFileChooser();
			chooser.setDialogTitle("Open - Failed to locate resources");
			chooser.setAcceptAllFileFilterUsed(false);
			chooser.setFileFilter(new FileNameExtensionFilter("Compressed S2Games file","s2z"));
			try {
				chooser.setCurrentDirectory(new LLFile(LLFile.getProgram()[0],false));
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			int option = chooser.showOpenDialog(null);
		    if(option == JFileChooser.APPROVE_OPTION) {
		    	property.setProperty("game",chooser.getSelectedFile().getParentFile().getAbsolutePath()+File.separator);
		    }
		    else {
		    	return;
		    //	new LLException(new FileNotFoundException(property.getProperty("resources","")+" (The system cannot find the file specified)"));
		    }
		}
		try {
			LLInputStream in;
			StringBuilder data = new StringBuilder();
			ZipFile resources = new ZipFile(new File(property.getProperty("game","")+"resources0.s2z"));
			Enumeration en = resources.entries();
			
			//Fetching the entities name so it'll be ready to be read when needed;
			in = new LLInputStream(resources.getInputStream(resources.getEntry("stringtables/interface_en.str")));
			while(in.available() > 0) {
				data.append(in.readString(1024*16));
			}
			
			//Locating all the heroes;
			while(en.hasMoreElements()) {
				String entry = en.nextElement().toString();
				boolean pass = false;
				
				if(entry.startsWith("heroes/")) {
					if(entry.endsWith("/hero.entity")) {
						pass = true;
					}
					else if(LLAccessories.containsCount(entry,"/") == 2 && entry.endsWith(".entity")) {
						int index1 = "heroes/".length();
						int index2 = entry.substring(index1).indexOf("/")+index1;
						int index3 = index2+1;
						int index4 = entry.substring(index3).indexOf(".")+index3;
						if(entry.substring(index1,index2).equals(entry.substring(index3,index4))) {
							pass = true;
						}
					}
				}
				
				if(pass) {
					//System.out.println(entry);
					
					try {
						int index1;
						int index2;
						
						Element hero = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(resources.getInputStream(resources.getEntry(entry))).getDocumentElement();
						
						//Reciveing srcname;
						/*index1 = "heroes/".length();
						index2 = entry.substring(index1).indexOf("/")+index1;
						String srcname = entry.substring(index1,index2);*/
						String srcname = hero.getAttribute("name");

						//Reciveing Avatar0 name;
						index1 = LLAccessories.indexOf(data.toString(),"mstore_"+hero.getAttribute("name")+"_name",0);
						index1 = LLAccessories.indexOf(data.toString(),"\\s[0-9a-zA-Z-][0-9a-zA-Z\\s-]+\n",index1)+1;
						index2 = data.indexOf("\n",index1);
						String name = data.substring(index1,index2);
						
						heroes.add(new Hero(srcname,entry,name));

						gui.progresslabel.setText("Locating heroes... "+name);
						//System.out.println("Found "+name+" at "+entry+" with the keyname of "+srcname);
						
					} catch (SAXException e) {
						e.printStackTrace();
					} catch (ParserConfigurationException e) {
						e.printStackTrace();
					}
				}
			}
			
			Collections.sort(heroes);
			
			//Now importing all the avatars;
			try {
				Element mstore = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(resources.getInputStream(resources.getEntry("content/store_avatars.package"))).getDocumentElement();
				NodeList list = mstore.getElementsByTagName("instance");
				
				gui.progressbar.setMaximum(list.getLength()-1+heroes.size()-1+heroes.size()-1);
				
				for(int i = 0;i <= list.getLength()-1;i++) {
					Element instance = (Element) list.item(i);
					
					if(instance.getAttribute("name").equals("altAvatarPreviewPanel")) {
						String key = instance.getAttribute("product").substring(instance.getAttribute("product").indexOf(".")+1);
						
						int index1 = LLAccessories.indexOf(data.toString(),"mstore_product"+instance.getAttribute("id")+"_name",0);
						index1 = LLAccessories.indexOf(data.toString(),"\\s[a-zA-Z][a-zA-Z\\s]+\n",index1)+1;
						int index2 = data.indexOf("\n",index1);
						String name = data.substring(index1,index2);

						gui.progresslabel.setText("Locating avatars... "+name);
						
						for(int ii = 0;ii <= heroes.size()-1;ii++) {
							if(instance.getAttribute("heroName").equals(heroes.get(ii).getName())) {
								heroes.get(ii).addAvatar(new Avatar(name,key));
								//System.out.println("Found "+name+" with key "+key+" for "+heroes.get(ii).getName());
								break;
							}
						}
					}
					gui.progressbar.setValue(gui.progressbar.getValue()+1);
				}
			} catch (SAXException e) {
				e.printStackTrace();
			} catch (ParserConfigurationException e) {
				e.printStackTrace();
			}
			
			for(int i = 0;i <= heroes.size()-1;i++) {

				try {
					Element hero = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(resources.getInputStream(resources.getEntry(heroes.get(i).getEntry()))).getDocumentElement();
					
					for(int ii = 0;ii <= heroes.get(i).getAvatarCount()-1;ii++) {
						NodeList list = hero.getElementsByTagName("modifier");
						if(ii == 0) {
							heroes.get(i).getAvatar(ii).setIcon("00000000"+getPath(hero.getAttribute("icon").replace("tga","dds"),heroes.get(i).getEntry()));
							//System.out.println(heroes.get(i).getAvatar(0).getName()+"."+heroes.get(i).getAvatar(ii).getKey()+"="+heroes.get(i).getAvatar(ii).getIcon());
						}
						else {
							boolean pass = false;
							for(int iii = 0;iii <= list.getLength()-1;iii++) {
								Element alt = (Element) list.item(iii);
								
								if(heroes.get(i).getAvatar(ii).getKey().equalsIgnoreCase(alt.getAttribute("key"))) {
									hero.removeChild(list.item(iii));
									pass = true;
									heroes.get(i).getAvatar(ii).setIcon("00000000"+getPath(alt.getAttribute("icon2").replace("tga","dds"),heroes.get(i).getEntry()));
									//System.out.println(heroes.get(i).getAvatar(0).getName()+"."+heroes.get(i).getAvatar(ii).getKey()+"="+heroes.get(i).getAvatar(ii).getIcon());
									break;
								}
							}
							if(pass == false) {
								System.out.println(heroes.get(i).getAvatar(ii).getName());
								heroes.get(i).removeAvatar(ii);
								ii--;
							}
						}
					}
					
				} catch (SAXException e) {
					e.printStackTrace();
				} catch (ParserConfigurationException e) {
					e.printStackTrace();
				}
				
				gui.progresslabel.setText("Confirming avatars existence... "+heroes.get(i).getAvatar(0).getName());
				gui.progressbar.setValue(gui.progressbar.getValue()+1);
			}
			
			
			for(int i = 0;i <= heroes.size()-1;i++) {
				gui.herolist.add(heroes.get(i).getAvatar(0).getName());
				
				gui.progresslabel.setText("Fetching hero list... "+heroes.get(i).getAvatar(0).getName());
				gui.progressbar.setValue(gui.progressbar.getValue()+1);
			}
			
			
			resources.close();
		} catch (ZipException e) {
		} catch (IOException e) {
		}
		
		if(textures == null) {
			try {
				textures = new ZipFile(new File(property.getProperty("game","")+"textures.s2z"));
			} catch (ZipException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
			
		}
		
	}

	//Implements;
	
}


