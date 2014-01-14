package Engine;

import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.io.StringWriter;
import java.net.MalformedURLException;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.net.URLConnection;
import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.Collections;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.Vector;
import java.util.zip.ZipEntry;
import java.util.zip.ZipException;
import java.util.zip.ZipFile;
import java.util.zip.ZipOutputStream;

import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.TransformerFactoryConfigurationError;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;


import Doodads.DDS;
import Doodads.LLAccessories;
import Doodads.LLFile;
import Doodads.LLInputStream;
import Doodads.LLOutputStream;
import Engine.Attributes.Avatar;
import Engine.Attributes.Hero;
import Interface.LLGui;
import Doodads.LLProperty;

public class Config {
	//STATIC variables;
	
	//Variables;
	private	Main main;

	ZipFile textures = null;
	ZipFile resources = null;
	
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
			main.log.print(e,"Failed to establish config from \""+Main.PATH+"config.ini"+"\"",true);
			return;
		}
		
		//Terms of Argement;
		if(!property.getProperty("Setting_version","").equalsIgnoreCase(Main.VERSION)) {
			try {
				LLInputStream in;
				StringBuilder data = new StringBuilder();
				
				in = new LLInputStream(new FileInputStream(new LLFile(Main.PATH+"license.txt",false)));
				while(in.available() > 0) {
					data.append(in.readString(1024*16));
				}
				in.close();

				if(LLGui.show("License Agreement","Please read the following license agreement carefully",data.toString(),new String[] {"I Agree","I Disagree"}) != 0) {
					System.exit(0);
				}
				
				
				property.setProperty("Setting_version",Main.VERSION);
			} catch (IOException e) {
				main.log.print(e,"Failed to display the license, check out the licesse at: https://plus.google.com/104710093754514390547/about",true);
			}
		}
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
		if(textures != null) {
			try {
				
				ZipEntry ze = textures.getEntry(path);
				//System.out.println(path);
				
				if(ze != null) {
					InputStream in = textures.getInputStream(ze);
					
					ByteBuffer b = ByteBuffer.allocate(in.available());
					//System.out.println(in.available());
					while(in.available() > 0) {
						b.put((byte) in.read());
						
					}
					BufferedImage img = DDS.readDxt(b);
					
					img = flipImage(img);
					
					return new ImageIcon(img.getScaledInstance(32,32,java.awt.Image.SCALE_SMOOTH));
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return new ImageIcon(ClassLoader.getSystemResource("Images/error.png"));
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
	public int getAvatarIndex(Hero hero,int index) {
		if(index >= 0) {
			String value = property.getProperty(hero.getName()+"."+hero.getAvatar(index).getKey(),hero.getAvatar(index).getKey());
			
			for(int i = 0;i <= hero.getAvatarCount()-1;i++) {
				if(hero.getAvatar(i).getKey().equalsIgnoreCase(value)) {
					return i;
				}
			}
		}
		else {
			String value = property.getProperty(hero.getName(),null);
			if(value != null) {
				for(int i = 0;i <= hero.getAvatarCount()-1;i++) {
					if(hero.getAvatar(i).getKey().equalsIgnoreCase(value)) {
						return i;
					}
				}
			}
		}
		return index;
	}
	
	//Add;
	
	//Remove;
	
	//Do;
	
	//Other;
	public boolean refresh() {
		Gui gui = main.gui;
		
		//Clearing out old resources,
		heroes.clear();
		gui.herolist.clear();
		
		main.gui.progresslabel.setText("Locating resources...");
		File file = new File(property.getProperty("Setting_Resources",LLFile.getProgram()[0]+"Heroes of Newerth"+File.separator+"game"+File.separator+"resources0.s2z"));
		if(!file.exists()) {
			String[] filters = {"Compressed S2Games file","s2z"};
			file = LLFile.chooseFile("Open - Heroes of Newerth resources",filters,new File(LLFile.getProgram()[0]));
			if(file == null) {
				return false;
			}
			property.setProperty("Setting_Resources",file.getAbsolutePath());
		}
		
		try {
			resources = new ZipFile(file);
		} catch (IOException e) {
			main.log.print(e,"Failed to establish and read from "+file.getAbsolutePath(),true);
			return false;
		}
		Enumeration en = resources.entries();
		String stringtable;

		try {
			LLInputStream in;
			StringBuilder data = new StringBuilder();
			
			//Fetching the entities name so it'll be ready to be read when needed;
			in = new LLInputStream(resources.getInputStream(resources.getEntry("stringtables/interface_en.str")));
			while(in.available() > 0) {
				data.append(in.readString(1024*16));
			}
			in.close();
			
			stringtable = data.toString();
		} catch (IOException e) {
			main.log.print(e,"Failed to establish and read from "+"stringtables/interface_en.str",true);
			return false;
		}
			
		//Locating all the heroes;
		while(en.hasMoreElements()) {
			String entry = en.nextElement().toString();
			
			if(entry.startsWith("heroes/") && LLAccessories.containsCount(entry,"/") == 2 && entry.endsWith(".entity")) {
				int index1 = "heroes/".length();
				int index2 = entry.substring(index1).indexOf("/")+index1;
				int index3 = index2+1;
				int index4 = entry.substring(index3).indexOf(".")+index3;
				
				if(!entry.endsWith("/hero.entity") && !entry.substring(index1,index2).equals(entry.substring(index3,index4))) {
					continue;
				}
				
				try {
					Element hero = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(resources.getInputStream(resources.getEntry(entry))).getDocumentElement();
					String srcname = hero.getAttribute("name");

					//Reciveing Avatar0 name;
					index1 = LLAccessories.indexOf(stringtable,"mstore_"+hero.getAttribute("name")+"_name",0);
					index1 = LLAccessories.indexOf(stringtable,"\\s[0-9a-zA-Z-][0-9a-zA-Z\\s-]+\n",index1)+1;
					index2 = stringtable.indexOf("\n",index1);
					String name = stringtable.substring(index1,index2);
					
					heroes.add(new Hero(srcname,entry,name));

					gui.progresslabel.setText("Locating heroes... "+name);
					//System.out.println("Found "+name+" at "+entry+" with the keyname of "+srcname);
				} catch (SAXException | ParserConfigurationException | IOException e) {
					main.log.print(e,"Failed to read or parse xml "+entry,false);
				}
			}
		}
		Collections.sort(heroes);
		
		//Now importing all the avatars;
		try {
			Element mstore = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(resources.getInputStream(resources.getEntry("content/store_avatars.package"))).getDocumentElement();
			NodeList list = mstore.getElementsByTagName("instance");
			
			gui.progressbar.setMaximum(list.getLength()+heroes.size()+heroes.size());
			
			instances:for(int i = 0;i <= list.getLength()-1;i++) {
				Element instance = (Element) list.item(i);
				
				if(instance.getAttribute("name").equalsIgnoreCase("altAvatarPreviewPanel")) {
					String key = instance.getAttribute("product");
					
					//Figuring out it's name
					int index1 = LLAccessories.indexOf(stringtable,"mstore_product"+instance.getAttribute("id")+"_name",0);
					index1 = LLAccessories.indexOf(stringtable,"\\s[a-zA-Z][a-zA-Z\\s]+\n",index1)+1;
					int index2 = stringtable.indexOf("\n",index1);
					
					String name = stringtable.substring(index1,index2);
					gui.progresslabel.setText("Locating avatars... "+name);
					
					//Finding which hero it belongs to?
					for(int ii = 0;ii <= heroes.size()-1;ii++) {
						if(instance.getAttribute("heroName").equals(heroes.get(ii).getName())) {
							for(int iii = 0;iii <= heroes.get(ii).getAvatarCount()-1;iii++) {
								if(heroes.get(ii).getAvatar(iii).getKey().equalsIgnoreCase(key)) {
									main.log.print("Ignoring dublicated-key avatar "+name);
									gui.progressbar.setValue(gui.progressbar.getValue()+1);
									continue instances;
								}
							}
							for(int iii = 0;iii <= heroes.get(ii).getUltimateCount()-1;iii++) {
								if(heroes.get(ii).getUltimate(iii).getKey().equalsIgnoreCase(key)) {
									main.log.print("Ignoring dublicated-key avatar "+name);
									gui.progressbar.setValue(gui.progressbar.getValue()+1);
									continue instances;
								}
							}
							
							//Checking if it's ultimate, then ignore it.
							if(instance.getAttribute("ultimateAvatar").equalsIgnoreCase("true")) {
								main.log.print("Ignoring ultimate avatar "+name);
								heroes.get(ii).addUltimate(new Avatar(name,key));
							}
							else {
								heroes.get(ii).addAvatar(new Avatar(name,key));
							}
							break;
						}
					}
				}
				gui.progressbar.setValue(gui.progressbar.getValue()+1);
			}
		} catch (SAXException | ParserConfigurationException | IOException e) {
			main.log.print(e,"Failed to establish and read from "+"content/store_avatars.package",true);
			return false;
		}
		
		for(int i = 0;i <= heroes.size()-1;i++) {
			try {
				Element hero = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(resources.getInputStream(resources.getEntry(heroes.get(i).getEntry()))).getDocumentElement();
				NodeList list = hero.getElementsByTagName("altavatar");
				
				for(int ii = 0;ii <= heroes.get(i).getAvatarCount()-1;ii++) {
					if(ii == 0) {
						heroes.get(i).getAvatar(ii).setIcon("00000000"+getPath(hero.getAttribute("icon").replace("tga","dds"),heroes.get(i).getEntry()));
					}
					else {
						for(int iii = 0;iii <= list.getLength()-1;iii++) {
							Element alt = (Element) list.item(iii);
							
							if(heroes.get(i).getAvatar(ii).getKey().equalsIgnoreCase(alt.getAttribute("key")) || heroes.get(i).getAvatar(ii).getFullKey().equalsIgnoreCase(alt.getAttribute("key"))) {
								heroes.get(i).getAvatar(ii).setIcon("00000000"+getPath(alt.getAttribute("icon2").replace("tga","dds"),heroes.get(i).getEntry()));
								break;
							}
						}
						if(heroes.get(i).getAvatar(ii).getIcon() == null) {
							main.log.print("Ignoring non-confirmed avatar "+heroes.get(i).getAvatar(ii).getName());
							
							heroes.get(i).removeAvatar(ii);
							ii--;
						}
					}
				}
				
			} catch (SAXException | ParserConfigurationException | IOException e) {
				main.log.print(e,"Failed to read or parse xml "+heroes.get(i).getEntry(),false);
				heroes.remove(i);
			}
			
			gui.progresslabel.setText("Confirming avatars existence... "+heroes.get(i).getAvatar(0).getName());
			gui.progressbar.setValue(gui.progressbar.getValue()+1);
		}
		
		//Fetching list;
		for(int i = 0;i <= heroes.size()-1;i++) {
			gui.herolist.add(heroes.get(i).getAvatar(0).getName());
			
			gui.progresslabel.setText("Fetching hero list... "+heroes.get(i).getAvatar(0).getName());
			gui.progressbar.setValue(gui.progressbar.getValue()+1);
		}
		
		//Textures;
		try {
			textures = new ZipFile(file.getParentFile().getAbsolutePath()+File.separator+"textures.s2z");
		} catch (IOException e) {
			main.log.print(e,file.getParentFile().getAbsolutePath()+File.separator+"textures.s2z",false);
			return false;
		}
		
		return true;
	}
	
	//UPDATE
	public boolean update() {
		main.gui.progressbar.setValue(0);
		main.gui.progresslabel.setText("Connecting to update server...");
		
		String xml;

		try {
			URLConnection connection = new URL(main.config.property.getProperty("Setting_Changeset","https://dl.dropboxusercontent.com/u/38414202/Customization%20of%20Newerth/changeset.xml")).openConnection();
			connection.setConnectTimeout(5000);
			
			StringBuilder sb = new StringBuilder();
			LLInputStream in = new LLInputStream(connection.getInputStream());
			main.log.print("Successfully establish a connection with \""+main.config.property.getProperty("Setting_Changeset","https://dl.dropboxusercontent.com/u/38414202/Customization%20of%20Newerth/changeset.xml")+"\"");
			
			while(in.available() > 0) {
				sb.append(in.readString(1024*4*4));
			}
			xml = sb.toString();
			
		} catch (MalformedURLException e) {
			main.log.print(e,"Failed to recognize URL protocol \""+main.config.property.getProperty("Setting_Changeset","https://dl.dropboxusercontent.com/u/38414202/Customization%20of%20Newerth/changeset.xml")+"\"",false);
			return false;
		} catch (SocketTimeoutException e) {
			main.log.print(e,"Failed to connect to \""+main.config.property.getProperty("Setting_Changeset","https://dl.dropboxusercontent.com/u/38414202/Customization%20of%20Newerth/changeset.xml")+"\", connection timed out",false);
			return false;
		} catch (IOException e) {
			main.log.print(e,"Failed to read data from \""+main.config.property.getProperty("Setting_Changeset","https://dl.dropboxusercontent.com/u/38414202/Customization%20of%20Newerth/changeset.xml")+"\"",false);
			return false;
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
								if(changelist.get(iii)[2].equals(elem.getAttribute("file"))) {
									if(((int) (Integer.parseInt(change.getAttribute("version").replace(".",""))*Math.pow(10,length-elem.getAttribute("version").replace(".","").length()))) > ((int) (Integer.parseInt(changelist.get(iii)[0].replace(".",""))*Math.pow(10,length-changelist.get(iii)[0].replace(".","").length())))) {
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
				return false;
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
			main.log.print(e,"Failed to parse xml changeset",false);
			return false;
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
			main.log.print(e,"Failed to recognize URL protocol \""+url+change[0]+"/"+change[2]+"\"",false);
			return false;
		} catch (IOException e) {
			main.log.print(e,"Failed to read or write data from \""+url+change[0]+"/"+change[2]+"\" to "+Main.PATH+"update"+File.separator+change[2],false);
			return false;
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
			main.log.print(e,"Failed to located or create \""+Main.PATH+"update"+File.separator+change[2]+"\", "+Main.PATH+change[2],false);
			return false;
		} catch (IOException e) {
			main.log.print(e,"Failed to read or write data from \""+Main.PATH+"update"+File.separator+change[2]+"\" to "+Main.PATH+change[2],false);
			return false;
		}
		return true;
	}
	
		//COMPILE!
	public boolean compile() {
		this.main = main;
	
		main.gui.progresslabel.setText("Applying modifications...");
		main.gui.progressbar.setMaximum(1);
		for(int i = 0;i <= main.config.heroes.size()-1;i++) {
			main.gui.progressbar.setMaximum(main.gui.progressbar.getMaximum()+main.config.heroes.get(i).getAvatarCount());
		}
		
		String folder = Main.PATH+"instances"+File.separator+System.currentTimeMillis()+File.separator;
		
		//Compiling heroes...
		heroes:for(int i = 0;i <= main.config.heroes.size()-1;i++) {
			Hero hero = main.config.heroes.get(i);
	
			main.gui.progresslabel.setText("Compiling "+hero.getAvatar(0).getName()+"...");
			main.log.print("Compiling "+hero.getAvatar(0).getName()+"...");
			int allforce = main.config.getAvatarIndex(hero,-1);
			for(int ii = 0;ii <= hero.getAvatarCount()-1;ii++) {
				int index = allforce;
				if(index < 0) {
					index = main.config.getAvatarIndex(hero,ii);
				}
				
				if(ii == index) {
					if(ii == hero.getAvatarCount()-1) {
						main.gui.progressbar.setValue(main.gui.progressbar.getValue()+hero.getAvatarCount());
						continue heroes;
					}
					continue;
				}
				break;
			}
			//Global things;
			Element orgheroentity = null, modheroentity = null;
			try {
				//Hero Entity;
				orgheroentity = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(main.config.resources.getInputStream(main.config.resources.getEntry(hero.getEntry()))).getDocumentElement();
				orgheroentity.setAttribute("key",hero.getAvatar(0).getKey()); //Setting a key so later looks for modifers will be found;
				modheroentity = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(main.config.resources.getInputStream(main.config.resources.getEntry(hero.getEntry()))).getDocumentElement();
				modheroentity.setAttribute("key",hero.getAvatar(0).getKey()); //Setting a key so later looks for modifers will be found;
			} catch (IOException | SAXException | ParserConfigurationException e) {
				main.log.print(e,"Failed to load or parse "+hero.getEntry(),true);
				continue heroes;
			}
	
			Vector<String> projectiles = new Vector<String>();
			Vector<String> abilities = new Vector<String>();
			
			Enumeration entries = main.config.resources.entries();
			while(entries.hasMoreElements()) {
				String entry = entries.nextElement().toString();
				
				if(entry.startsWith("heroes/"+hero.getFolder()) && entry.endsWith(".entity")) {
					//Projcetiles;
					if(entry.contains("projectile")) {
						projectiles.add(entry);
					}
					//Abilities
					if(entry.contains("ability")) {
						abilities.add(entry);
					}
				}
			}
			//Abilities
			String[]	ability = new String[4];
			Element[]	orgabilityentity = new Element[4];
			Element[]	modabilityentity = new Element[4];
			for(int ii = 0;ii <= abilities.size()-1;ii++) {
				Element abilityelement;
				try {
					abilityelement = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(main.config.resources.getInputStream(main.config.resources.getEntry(abilities.get(ii)))).getDocumentElement();
					for(int iii = 0;iii <= ability.length-1;iii++) {
						if(abilityelement.getAttribute("name").equalsIgnoreCase(orgheroentity.getAttribute("inventory"+iii))) {
							ability[iii] = abilities.get(ii);
							orgabilityentity[iii] = abilityelement;
							orgabilityentity[iii].setAttribute("key",hero.getAvatar(0).getKey()); //Setting a key so later looks for modifers will be found;
							modabilityentity[iii] = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(main.config.resources.getInputStream(main.config.resources.getEntry(abilities.get(ii)))).getDocumentElement();
							modabilityentity[iii].setAttribute("key",hero.getAvatar(0).getKey()); //Setting a key so later looks for modifers will be found;
							
							break;
						}
					}
				} catch (IOException | SAXException | ParserConfigurationException e) {
					main.log.print(e,"Failed to load or parse "+abilities.get(ii),true);
				}
			}
			
			for(int ii = 0;ii <= hero.getAvatarCount()-1;ii++) {
				main.gui.progresslabel.setText("Compiling "+hero.getAvatar(ii).getName()+"...");
				main.log.print("Compiling "+hero.getAvatar(ii).getName()+"...");
				
				int index = allforce;
				if(index < 0) {
					index = main.config.getAvatarIndex(hero,ii);
				}
				
				//Modifying heroentity;
				Element orgheromodifier = getModifier(hero.getAvatar(index),orgheroentity);
				Element modheromodifier = getModifier(hero.getAvatar(ii),modheroentity);
				
				//Changing the attributes;
				//Models & Effects;
				modheromodifier.setAttribute("portrait",getAttribute(orgheromodifier.getAttribute("portrait"),getAttribute(orgheroentity.getAttribute("portrait"),"")));
				
				modheromodifier.setAttribute("passiveeffect",getAttribute(orgheromodifier.getAttribute("passiveeffect"),getAttribute(orgheroentity.getAttribute("passiveeffect"),""))); //Doing this after the others due to "storepassiveeffect" 'might' need it and then it has to be with the original value;
				modheromodifier.setAttribute("previewpassiveeffect",getAttribute(orgheromodifier.getAttribute("previewpassiveeffect"),getAttribute(orgheroentity.getAttribute("previewpassiveeffect"),getAttribute(orgheromodifier.getAttribute("passiveeffect"),orgheroentity.getAttribute("passiveeffect")))));
				modheromodifier.setAttribute("storepassiveeffect","");
				
				modheromodifier.setAttribute("previewmodel",getAttribute(orgheromodifier.getAttribute("previewmodel"),getAttribute(orgheroentity.getAttribute("previewmodel"),"")));
				modheromodifier.setAttribute("storepos",getAttribute(orgheromodifier.getAttribute("storepos"),getAttribute(orgheroentity.getAttribute("storepos"),"")));
				modheromodifier.setAttribute("storescale",getAttribute(orgheromodifier.getAttribute("storescale"),getAttribute(orgheroentity.getAttribute("storescale"),"")));
				
				//Scales;
				modheromodifier.setAttribute("preglobalscale",getAttribute(orgheromodifier.getAttribute("preglobalscale"),getAttribute(orgheroentity.getAttribute("preglobalscale"),"")));
				modheromodifier.setAttribute("modelscale",getAttribute(orgheromodifier.getAttribute("modelscale"),getAttribute(orgheroentity.getAttribute("modelscale"),"")));
				modheromodifier.setAttribute("effectscale",getAttribute(orgheromodifier.getAttribute("effectscale"),getAttribute(orgheroentity.getAttribute("effectscale"),"")));
				modheromodifier.setAttribute("infoheight",getAttribute(orgheromodifier.getAttribute("infoheight"),getAttribute(orgheroentity.getAttribute("infoheight"),"")));
				modheromodifier.setAttribute("tiltfactor",getAttribute(orgheromodifier.getAttribute("tiltfactor"),getAttribute(orgheroentity.getAttribute("tiltfactor"),"")));
				modheromodifier.setAttribute("tiltspeed",getAttribute(orgheromodifier.getAttribute("tiltspeed"),getAttribute(orgheroentity.getAttribute("tiltspeed"),"")));
				
				//Sounds;
				modheromodifier.setAttribute("selectedsound",getAttribute(orgheromodifier.getAttribute("selectedsound"),getAttribute(orgheroentity.getAttribute("selectedsound"),"")));
				modheromodifier.setAttribute("selectedflavorsound",getAttribute(orgheromodifier.getAttribute("selectedflavorsound"),getAttribute(orgheroentity.getAttribute("selectedflavorsound"),"")));
				modheromodifier.setAttribute("confirmmovesound",getAttribute(orgheromodifier.getAttribute("confirmmovesound"),getAttribute(orgheroentity.getAttribute("confirmmovesound"),"")));
				modheromodifier.setAttribute("confirmattacksound",getAttribute(orgheromodifier.getAttribute("confirmattacksound"),getAttribute(orgheroentity.getAttribute("confirmattacksound"),"")));
				modheromodifier.setAttribute("nomanasound",getAttribute(orgheromodifier.getAttribute("nomanasound"),getAttribute(orgheroentity.getAttribute("nomanasound"),"")));
				modheromodifier.setAttribute("cooldownsound",getAttribute(orgheromodifier.getAttribute("cooldownsound"),getAttribute(orgheroentity.getAttribute("cooldownsound"),"")));
				modheromodifier.setAttribute("tauntedsound",getAttribute(orgheromodifier.getAttribute("tauntedsound"),getAttribute(orgheroentity.getAttribute("tauntedsound"),"")));
				modheromodifier.setAttribute("tauntkillsound",getAttribute(orgheromodifier.getAttribute("tauntkillsound"),getAttribute(orgheroentity.getAttribute("tauntkillsound"),"")));
				
				//Attacks;
				modheromodifier.setAttribute("attackoffset",getAttribute(orgheromodifier.getAttribute("attackoffset"),getAttribute(orgheroentity.getAttribute("attackoffset"),"")));
				modheromodifier.setAttribute("attackstarteffect",getAttribute(orgheromodifier.getAttribute("attackstarteffect"),getAttribute(orgheroentity.getAttribute("attackstarteffect"),"")));
				modheromodifier.setAttribute("attackactioneffect",getAttribute(orgheromodifier.getAttribute("attackactioneffect"),getAttribute(orgheroentity.getAttribute("attackactioneffect"),"")));
				modheromodifier.setAttribute("attackimpacteffect",getAttribute(orgheromodifier.getAttribute("attackimpacteffect"),getAttribute(orgheroentity.getAttribute("attackimpacteffect"),"")));
				
				//Model;
				try {
					String model = getModel(modheromodifier.getAttribute("model"),orgheromodifier.getAttribute("model"),hero.getAvatar(0).getKey());
					
					LLOutputStream out = new LLOutputStream(new FileOutputStream(new LLFile(folder+"heroes/"+hero.getFolder()+"/"+modheromodifier.getAttribute("model"))));
					out.writeString(stamp(model));
					out.close();
					
				} catch (IOException e) {
					main.log.print(e,"Failed to write to "+modheromodifier.getAttribute("model"),true);
				}
				//Projectile;
				if(!orgheroentity.getAttribute("attackprojectile").isEmpty()) {
					
					String[] keys = {hero.getAvatar(index).getFullKey(),hero.getAvatar(ii).getFullKey()};
					getProjectile(getAttribute(modheromodifier.getAttribute("attackprojectile"),orgheroentity.getAttribute("attackprojectile")),getAttribute(orgheromodifier.getAttribute("attackprojectile"),orgheroentity.getAttribute("attackprojectile")),projectiles.toArray(new String[projectiles.size()]),keys,folder);
				}
				//Abilities
				for(int iii = 0;iii <= ability.length-1;iii++) {
					if(ability[iii] != null) {
						Element orgabilitymodifier = getModifier(hero.getAvatar(index),orgabilityentity[iii]);
						Element modabilitymodifier = getModifier(hero.getAvatar(ii),modabilityentity[iii]);
						
						if(modabilitymodifier != null) {
							if(orgabilitymodifier == null) {
								orgabilitymodifier = orgabilityentity[iii];
							}
							
							//Change attibutes;
							modabilitymodifier.setAttribute("icon",getAttribute(orgabilitymodifier.getAttribute("icon"),getAttribute(orgabilityentity[iii].getAttribute("icon"),"")));
							modabilitymodifier.setAttribute("casteffect",getAttribute(orgabilitymodifier.getAttribute("casteffect"),getAttribute(orgabilityentity[iii].getAttribute("casteffect"),"")));
							modabilitymodifier.setAttribute("passiveeffect",getAttribute(orgabilitymodifier.getAttribute("passiveeffect"),getAttribute(orgabilityentity[iii].getAttribute("passiveeffect"),"")));
							
							//if(orgabilitymodifier.getElementsByTagName("hasmodifier").getLength() > 0 && !orgabilitymodifier.getAttribute("key").equalsIgnoreCase(hero.getAvatar(0).getKey())) System.out.println(ability[iii]);
							
							//Projectile;
							if(!orgabilityentity[iii].getAttribute("projectile").isEmpty()) {
								
								String[] keys = {hero.getAvatar(index).getFullKey(),hero.getAvatar(ii).getFullKey()};
								getProjectile(getAttribute(modabilitymodifier.getAttribute("projectile"),orgabilityentity[iii].getAttribute("projectile")),getAttribute(orgabilitymodifier.getAttribute("projectile"),orgabilityentity[iii].getAttribute("projectile")),projectiles.toArray(new String[projectiles.size()]),keys,folder);
							}
						}
						
						//Onimpact etc... private void On(Element[] element,String[] filepath,String[] keys,String folder) {
						Element[] one = {orgabilityentity[iii],orgabilitymodifier,modabilitymodifier};
						String[] onpath = {ability[iii],ability[iii]};
						String[] onkey = {hero.getAvatar(index).getFullKey(),hero.getAvatar(ii).getFullKey()};
						On(one,onpath,onkey,folder,projectiles.toArray(new String[projectiles.size()]));
						
					}
				}
				main.gui.progressbar.setValue(main.gui.progressbar.getValue()+1);
			}
			
			//Hero Entity;
			try {
				
				LLOutputStream out = new LLOutputStream(new FileOutputStream(new LLFile(folder+hero.getEntry())));
				out.writeString(stamp(xmlToString(modheroentity)));
				out.close();
				
			} catch (IOException e) {
				main.log.print(e,"Failed to write to "+hero.getEntry(),true);
			}
			
			for(int ii = 0;ii <= ability.length-1;ii++) {
				try {
					if(ability[ii] != null) {
						LLOutputStream out = new LLOutputStream(new FileOutputStream(new LLFile(folder+ability[ii])));
						String abilityxml = stamp(xmlToString(modabilityentity[ii]));
						out.writeString(abilityxml);
						out.close();
						
						/*if(abilityxml.contains("hasmodifier")) {
							System.out.println(ability[ii]);
						}*/
						
						
					}
				} catch (IOException e) {
					main.log.print(e,"Failed to write to "+ability[ii],true);
				}
			}
		}
		//Zip...
		LLFile filefolder;
		try {
			filefolder = new LLFile(folder,false);
			if(filefolder.exists()) {
				main.gui.progresslabel.setText("Zipping resources...");
				ZipOutputStream zut = new ZipOutputStream(new FileOutputStream(new LLFile(new LLFile(property.getProperty("Setting_Resources",""),false).getParentFile().getAbsolutePath()+File.separator+"resourcesCoN.s2z",true)));
				
				File[] files = filefolder.getAllFiles();
				for(File file : files) {
					LLInputStream zin = new LLInputStream(new FileInputStream(file));
					String content = "";
					
					while(zin.available() > 0) {
						content += zin.readString(4*4*1024);
					}
					zin.close();
					
					String path = file.getAbsolutePath().substring(folder.length()).replaceAll("\\\\","/");
					
					zut.putNextEntry(new ZipEntry(path));
					for(int i = 0;i <= content.length()-1;i++) {
						zut.write((int) content.charAt(i));
					}
					zut.closeEntry();
				}
	
				LLInputStream zin = new LLInputStream(new FileInputStream(main.PATH+"console.log"));
				String content = "";
				
				while(zin.available() > 0) {
					content += zin.readString(4*4*1024);
				}
				zin.close();
				
				zut.putNextEntry(new ZipEntry("console.log"));
				for(int i = 0;i <= content.length()-1;i++) {
					zut.write((int) content.charAt(i));
				}
				zut.closeEntry();
				zut.close();
			}
			else {
				new LLFile(new LLFile(property.getProperty("Setting_Resources",""),false).getParentFile().getAbsolutePath()+File.separator+"resourcesCoN.s2z",true).delete();
			}
		} catch (IOException e) {
			try {
				main.log.print(e,"Failed to write to "+new LLFile(property.getProperty("Setting_Resources",""),false).getParentFile().getAbsolutePath()+File.separator+"resourcesCoN.s2z",true);
				return false;
			} catch (IOException e1) {
				e1.printStackTrace();
				return false;
			}
		}
		return true;
	}
	
	private Vector<Element> runOn(Element element,String key) {
		Vector<Element> vector = new Vector<Element>();
		Element[] children = getChildTags(element,"*");
		
		boolean elsi = false;
		
		for(Element child : children) {
			if(elsi) { //If the last tag was hasmodifier with a acceptable altkey the else tag should be denied.
				if(child.getTagName().equalsIgnoreCase("else")) {
					continue;
				}
				
				elsi = false;
			}
			
			if(child.getTagName().equalsIgnoreCase("hasavatarkey")) {
				if(child.getAttribute("name").toLowerCase().contains("alt")
				|| child.getAttribute("name").toLowerCase().contains("female")
				|| child.getAttribute("name").toLowerCase().contains("reskin")
				|| child.getAttribute("name").toLowerCase().contains("classic")) {
					//If the modifier is an alt modifier
					if(child.getAttribute("name").equalsIgnoreCase(key)) { //If this is the avatars modifier(specified with the key(name attribute))
						vector.addAll(runOn(child,key));
						elsi = true;
					}
				}
				else { //Else it's another kind of modifier, let's look inside!
					vector.addAll(runOn(child,key));
				}
			}
			else if(child.getTagName().equalsIgnoreCase("playeffect")
				 || child.getTagName().equalsIgnoreCase("applystate")
				 || child.getTagName().equalsIgnoreCase("spawnunit")
				 || child.getTagName().equalsIgnoreCase("spawnprojectile")
				 || child.getTagName().equalsIgnoreCase("spawnaffector")) {
				vector.add(child);
			}
			else {
				vector.addAll(runOn(child,key));
			}
		}
		
		return vector;
	}
	/**
	 * 
	 * @param element
	 * @param tag The specific name of the tags you want. * will return all children.
	 * @return
	 */
	private Element[] getChildTags(Element element,String tag) {
		int l = 0;
		NodeList list = element.getChildNodes();
		for(int i = 0;i <= list.getLength()-1;i++) {
			if(list.item(i).getNodeType() == Node.ELEMENT_NODE) {
				if(list.item(i).getNodeName().equalsIgnoreCase(tag) || tag.equals("*")) {
					l++;
				}
			}
		}
		
		Element[] array = new Element[l];
		l = 0;
		for(int i = 0;i <= list.getLength()-1;i++) {
			if(list.item(i).getNodeType() == Node.ELEMENT_NODE) {
				if(list.item(i).getNodeName().equalsIgnoreCase(tag) || tag.equals("*")) {
					array[l] = (Element) list.item(i);
					l++;
				}
			}
		}
		
		return array;
	}
	
	private void getProjectile(String modprojectile, String orgprojectile,String[] projectiles,String[] keys,String folder) {
		Element orgprojectileentity = null,	modprojectileentity = null;
		String	orgprojectilepath	= null,	modprojectilepath	= null;
		
		//Finding the right projectiles;
		for(String projectile:projectiles) {
			Element element;
			try {
				element = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(main.config.resources.getInputStream(main.config.resources.getEntry(projectile))).getDocumentElement();
			} catch (SAXException | IOException | ParserConfigurationException e) {
				e.printStackTrace();
				return;
			}
			
			if(orgprojectile.equalsIgnoreCase(element.getAttribute("name"))) {
				orgprojectilepath = projectile;
				orgprojectileentity = element;
			}
			if(modprojectile.equalsIgnoreCase(element.getAttribute("name"))) {
				modprojectilepath = projectile;
				modprojectileentity = element;
			}
			
			if(orgprojectileentity != null && modprojectileentity != null) {
				break;
			}
		}
		if(orgprojectileentity == null || modprojectileentity == null) {
			return;
		}
		
		
		//Changing the attributes;
		//Scales;
		modprojectileentity.setAttribute("gravity",getAttribute(orgprojectileentity.getAttribute("gravity"),""));
		modprojectileentity.setAttribute("modelscale",getAttribute(orgprojectileentity.getAttribute("modelscale"),"1.0"));
		
		//Effects;
		modprojectileentity.setAttribute("model",fixPath(getAttribute(orgprojectileentity.getAttribute("model"),""),orgprojectilepath));
		modprojectileentity.setAttribute("impacteffect",fixPath(getAttribute(orgprojectileentity.getAttribute("impacteffect"),""),orgprojectilepath));
		modprojectileentity.setAttribute("traileffect",fixPath(getAttribute(orgprojectileentity.getAttribute("traileffect"),""),orgprojectilepath));

		//Onimpact etc... private void On(Element[] element,String[] filepath,String[] keys,String folder) {
		Element[] one = {orgprojectileentity,orgprojectileentity,modprojectileentity};
		String[] onpath = {orgprojectilepath,modprojectilepath};
		On(one,onpath,keys,folder,projectiles);
		
		try {
			if(!new LLFile(folder+modprojectilepath,false).exists()) {
			
				LLOutputStream out = new LLOutputStream(new FileOutputStream(new LLFile(folder+modprojectilepath)));
				out.writeString(stamp(xmlToString(modprojectileentity)));
				out.close();
			}
		} catch (IOException e) {
			main.log.print(e,"Failed to write to "+modprojectilepath,true);
		}
	}
	public Element getModifier(Avatar avatar, Element entity) {
		if(entity.getAttribute("key").equalsIgnoreCase(avatar.getKey()) || entity.getAttribute("key").equalsIgnoreCase(avatar.getFullKey())) {
			return entity;
		}
		
		NodeList list = entity.getElementsByTagName("altavatar");
		for(int i = 0;i <= list.getLength()-1;i++) {
			Element element = (Element) list.item(i);
			
			if(element.getAttribute("key").equalsIgnoreCase(avatar.getKey()) || element.getAttribute("key").equalsIgnoreCase(avatar.getFullKey())) {
				return element;
			}
		}
		return null;
	}
	public Element getTag(String tag,Element entity,Element backup) {
		if(entity != null) {
			NodeList list = entity.getChildNodes();
			
			for(int i = 0;i <= list.getLength()-1;i++) {
				if(list.item(i).getNodeType() == Node.ELEMENT_NODE && list.item(i).getNodeName().equalsIgnoreCase(tag)) {
					return (Element) list.item(i);
				}
			}
		}
		
		return backup;
	}
	private String fixPath(String fix,String location) {
		if(!fix.startsWith("/") && !fix.isEmpty()) {
			fix = "/"+location.replaceFirst("/\\w+\\.{1}entity","/")+fix;
			
			while(fix.contains("/../")) {
				fix = fix.replaceFirst("/\\w+/\\.{2}/","/");
			}
		}
		return fix;
	}
	public String getAttribute(String attribute,String backup) {
		if(attribute.isEmpty()) {
			return backup;
		}
		return attribute;
	}
	private String xmlToString(Element xml) {
		StringWriter sw = new StringWriter();
		try {
			TransformerFactory.newInstance().newTransformer().transform(new DOMSource(xml),new StreamResult(sw));
		} catch (TransformerException | TransformerFactoryConfigurationError e) {
			e.printStackTrace();
			return null;
		}
		
		return sw.toString()/*.replace("?>","?>\n").replace("\"","\"\n").replace("=\"\n","=\"")*/;
	}
	
	private String getModel(String modeli,String modelii,String key0) {
		String[] s = new String[2];
		s[0] = "";
		s[1] = "";
		//Reading the hero entity;
		ZipEntry ze = main.config.resources.getEntry("heroes"+"/"+key0+"/"+modeli);
		long size = ze.getSize();
		if (size > 0) {
			BufferedReader br;
			try {
				br = new BufferedReader(
				new InputStreamReader(main.config.resources.getInputStream(ze)));
			String linebreak;
			while ((linebreak = br.readLine()) != null) {
				s[0] += linebreak+"\n";
			}
			br.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		//Reading the hero entity;
		ze = main.config.resources.getEntry("heroes"+"/"+key0+"/"+modelii);
		size = ze.getSize();
		if (size > 0) {
			BufferedReader br;
			try {
				br = new BufferedReader(
				new InputStreamReader(main.config.resources.getInputStream(ze)));
			String linebreak;
			while ((linebreak = br.readLine()) != null) {
				s[1] += linebreak+"\n";
			}
			br.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		String[] path = new String[10];
		path = modelii.split("/");
		String dir0 = "/heroes/"+key0+"/";
		String dir1 = "/heroes/"+key0+"/";
		String dir2 = "/heroes/"+key0+"/";
		String dir3 = "/heroes/"+key0+"/";
		//making dir's
		for(int ii = 0;ii <= path.length-2;ii++) {
			dir0 += path[ii]+"/";
		}
		for(int ii = 0;ii <= path.length-3;ii++) {
			dir1 = path[ii]+"/";
		}
		for(int ii = 0;ii <= path.length-4;ii++) {
			dir2 += path[ii]+"/";
		}
		for(int ii = 0;ii <= path.length-5;ii++) {
			dir3 += path[ii]+"/";
		}
		
		
		//Making the dir's correct
		s[1] = s[1].replaceAll("sample=\"","sample=\""+dir0).replaceAll("material=\"","material=\""+dir0).replaceAll("model=\"","model=\""+dir0).replaceAll("icon=\"","icon=\""+dir0).replaceAll("portrait=\"","portrait=\""+dir0).replaceAll("effect=\"","effect=\""+dir0).replaceAll("PlaySoundLinear ","PlaySoundLinear "+dir0).replaceAll("StartEffect ","StartEffect "+dir0).replaceAll("file=\"","file=\""+dir0).replaceAll("low=\"","low=\""+dir0).replaceAll("med=\"","med=\""+dir0).replaceAll("high=\"","high=\""+dir0).replaceAll("clip=\"","clip=\""+dir0).replaceAll(/*If model bug accurs this might be the error: before bug this == dir0+"\""+dir0+"\"","\"\"")*/"\""+dir0+"\"","\"\"").replaceAll(dir0+"/heroes","/heroes").replaceAll(dir0+"/shared","/shared").replaceAll(dir0+"/ui","/ui").replaceAll(dir0+"../../../",dir3).replaceAll(dir0+"../../",dir2).replaceAll(dir0+"../",dir1);
		//System.out.println(s[1]);
		for(int i = 0;i <= 1;i++) {
			int length = s[i].split("<anim").length-2;
			int position = 0;
			for(int ii = 0;ii <= length;ii++) {
				position = s[i].indexOf("<anim",position+"<anim".length());
				int index = s[i].indexOf("/>",position);
				if(s[i].indexOf(">",position) > s[i].indexOf("/>",position) && s[i].indexOf("/>",position) != -1) {
					s[i] = s[i].substring(0,index) + s[i].substring(index).replaceFirst("/>","></anim>");
				}
			}
		}
		
		
		int pos = s[0].indexOf("<anim name=\"",0);
		int length = s[0].split("<anim").length-2;
		for(int i = 0;i <= length;i++) {
			pos = s[0].indexOf("<anim name=\"",pos)+"<anim name=\"".length();
			String find = s[0].substring(pos-"<anim name=\"".length(),s[0].indexOf("\"",pos))+"\"";
			if(s[1].contains(find) == true) {
				int pos1;
				s[0] = s[0].replace(s[0].substring(pos-"<anim name=\"".length(),s[0].indexOf("</anim>",pos)+"</anim>".length()),s[1].substring(pos1 = s[1].indexOf(find),s[1].indexOf("</anim>",pos1)+"</anim>".length()));
			}
		}
		
		
		
		return s[0].replaceAll("file=\"","file=\""+dir0).replaceAll("low=\"","low=\""+dir0).replaceAll("med=\"","med=\""+dir0).replaceAll("high=\"","high=\""+dir0);
	}
	private String stamp(String str) {
		return str+"\n<!-- Customization of Newerth -->";
	}
	private void On(Element[] element,String[] filepath,String[] keys,String folder,String[] projectiles) {
		int mal = 0;
		int org = 1;
		int mod = 2;
		
		//Onimpact etc...  THIS WILL BE TRASH CODE!
		NodeList list = element[mal].getChildNodes();
		for(int n = 0;n <= list.getLength()-1;n++) {
			if(list.item(n).getNodeType() == Node.ELEMENT_NODE) {
				Element mall = (Element) list.item(n);
				
				if(mall.getNodeName().equalsIgnoreCase("altavatar")) {
					//Possibly do something;
				}
				else {
					Element orgabilityon = getTag(mall.getTagName(),element[org],mall);
					Element modabilityon = getTag(mall.getTagName(),element[mod],mall);
					
					Vector<Element> orgelements = runOn(orgabilityon,keys[0]);
					Vector<Element> modelements = runOn(modabilityon,keys[1]);
					
					String asd = orgelements.size()+":"+modelements.size();
					for(int nn = 0;nn <= orgelements.size()-1 && nn <= modelements.size()-1;nn++) {
						
						if(!orgelements.get(nn).getTagName().equalsIgnoreCase(modelements.get(nn).getTagName())) {
							int max = Math.max(orgelements.size(),modelements.size());
							
							if(max == orgelements.size()) {
								orgelements.remove(nn);
							}
							else {
								modelements.remove(nn);
							}
							nn--;
						}
					}
					
					if(orgelements.size() == 0 || modelements.size() == 0) {
						orgelements.clear();
						modelements.clear();
					}
					else if(orgelements.size() != modelements.size()) {
						for(int nn = Math.min(orgelements.size(),modelements.size());nn <= Math.max(orgelements.size(),modelements.size())-1;) {
							if(nn == orgelements.size()) {
								modelements.remove(nn);
							}
							else {
								orgelements.remove(nn);
							}
						}
					}
					//System.out.println((iii+1)+":"+n+" = "+orgelements.size()+":"+modelements.size()+"("+asd+")");

					for(int nn = 0;nn <= orgelements.size()-1;nn++) {
						if(orgelements.get(nn).getTagName().equalsIgnoreCase("playeffect")) {
							String orgeffect = fixPath(orgelements.get(nn).getAttribute("effect"),filepath[0]);
							String modeffect = fixPath(modelements.get(nn).getAttribute("effect"),filepath[1]);
							
							try {
								LLFile file = new LLFile(folder+modeffect.substring(1));
								if(!(file.length() > 0)) {
									StringBuilder sb = new StringBuilder();
									ZipEntry zipentry = resources.getEntry(orgeffect.substring(1));
									
									if(zipentry == null) {
										main.log.print("Failed to find the specific resource "+orgeffect);
										continue;
									}
									
									LLInputStream in2 = new LLInputStream(resources.getInputStream(zipentry)); //Same case as for 3 lines above;
									while(in2.available() > 0) {
										sb.append(in2.readString(4*4*1024));
									}
									String effect = sb.toString();
									
									//Fixing the paths...
									String path = orgeffect.replaceFirst("/\\w+\\.{1}effect","/");
									
									effect = effect.replace("sample=\"","sample=\""+path);
									effect = effect.replace("material=\"","material=\""+path);
									effect = effect.replace("model=\"","model=\""+path);
									
									effect = effect.replaceAll(path+"/","/");
									while(effect.contains("/../")) {
										effect = effect.replaceFirst("/\\w+/\\.{2}/","/");
									}
									
									//And now finally, time to output!
									LLOutputStream ut = new LLOutputStream(new FileOutputStream(file));
									ut.writeString(stamp(effect));
									ut.close();
								}
							} catch (IOException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						}
					/*	else if(orgelements.get(nn).getTagName().equalsIgnoreCase("spawnprojectile")) {
							
							if(!modelements.get(nn).getAttribute("name").equalsIgnoreCase(element[mal].getAttribute("name"))
							&& !orgelements.get(nn).getAttribute("name").equalsIgnoreCase(element[mal].getAttribute("name"))) {
								
								getProjectile(modelements.get(nn).getAttribute("name"),orgelements.get(nn).getAttribute("name"),projectiles,keys,folder);
							}
						}*/
					}
					
					
					
					
				}
				
				
			}
		}
	}
	
	//Implements;
	
}


