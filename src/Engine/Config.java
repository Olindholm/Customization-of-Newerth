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
import Doodads.LLProperty;
import Engine.Attributes.Avatar;
import Engine.Attributes.Hero;

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
			resources = new ZipFile(new File(property.getProperty("game","")+"resources0.s2z"));
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
		} catch (ZipException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
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
			main.log.print(e,"Failed to recognize URL protocol \""+main.config.property.getProperty("Setting_Changeset","https://dl.dropboxusercontent.com/u/38414202/Customization%20of%20Newerth/changeset.xml")+"\"");
			return false;
		} catch (SocketTimeoutException e) {
			main.log.print(e,"Failed to connect to \""+main.config.property.getProperty("Setting_Changeset","https://dl.dropboxusercontent.com/u/38414202/Customization%20of%20Newerth/changeset.xml")+"\", connection timed out");
			return false;
		} catch (IOException e) {
			main.log.print(e,"Failed to read data from \""+main.config.property.getProperty("Setting_Changeset","https://dl.dropboxusercontent.com/u/38414202/Customization%20of%20Newerth/changeset.xml")+"\"");
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
			main.log.print(e,"Failed to parse xml changeset");
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
			main.log.print(e,"Failed to recognize URL protocol \""+url+change[0]+"/"+change[2]+"\"");
			return false;
		} catch (IOException e) {
			main.log.print(e,"Failed to read or write data from \""+url+change[0]+"/"+change[2]+"\" to "+Main.PATH+"update"+File.separator+change[2]);
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
			main.log.print(e,"Failed to located or create \""+Main.PATH+"update"+File.separator+change[2]+"\", "+Main.PATH+change[2]);
			return false;
		} catch (IOException e) {
			main.log.print(e,"Failed to read or write data from \""+Main.PATH+"update"+File.separator+change[2]+"\" to "+Main.PATH+change[2]);
			return false;
		}
		return true;
	}
	
	//COMPILE!
	public boolean compile() {

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
				main.log.print(e,"Failed to load or parse "+hero.getEntry());
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
					main.log.print(e,"Failed to load or parse "+abilities.get(ii));
					continue heroes;
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
				if(true) { //Creating a chamber to lock in a few varibles that I don't want to have in the future;
					Element orgheromodifier = getModifier(hero.getAvatar(index).getKey(),orgheroentity);
					Element modheromodifier = getModifier(hero.getAvatar(ii).getKey(),modheroentity);
					
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
						out.writeString(model);
						out.close();
						
					} catch (IOException e) {
						main.log.print(e,"Failed to create or write to "+"heroes/"+hero.getFolder()+"/"+modheromodifier.getAttribute("model"));
					}
					//Projectile;
					if(!orgheroentity.getAttribute("attackprojectile").isEmpty()) {
						String[] projectile = getProjectile(getAttribute(modheromodifier.getAttribute("attackprojectile"),orgheroentity.getAttribute("attackprojectile")),getAttribute(orgheromodifier.getAttribute("attackprojectile"),orgheroentity.getAttribute("attackprojectile")),projectiles.toArray(new String[projectiles.size()]));
						
						try {
							if(!new LLFile(folder+projectile[0],false).exists()) {
							
								LLOutputStream out = new LLOutputStream(new FileOutputStream(new LLFile(folder+projectile[0])));
								out.writeString(projectile[1]);
								out.close();
							}
						} catch (IOException e) {
							main.log.print(e,"Failed to create or write to "+projectile[0]);
						}
					}
					//Abilities
					for(int iii = 0;iii <= ability.length-1;iii++) {
						if(ability[iii] != null) {
							Element orgabilitymodifier = getModifier(hero.getAvatar(index).getKey(),orgabilityentity[iii]);
							Element modabilitymodifier = getModifier(hero.getAvatar(ii).getKey(),modheroentity);
							
							if(modabilitymodifier != null) {
								if(orgabilitymodifier == null) {
									orgabilitymodifier = orgabilityentity[iii];
								}
								
								//Change attibutes...
								modabilitymodifier.setAttribute("icon",getAttribute(orgabilitymodifier.getAttribute("icon"),getAttribute(orgabilityentity[iii].getAttribute("icon"),"")));
								modabilitymodifier.setAttribute("casteffect",getAttribute(orgabilitymodifier.getAttribute("casteffect"),getAttribute(orgabilityentity[iii].getAttribute("casteffect"),"")));
								modabilitymodifier.setAttribute("passiveeffect",getAttribute(orgabilitymodifier.getAttribute("passiveeffect"),getAttribute(orgabilityentity[iii].getAttribute("passiveeffect"),"")));
							}
						}
					}
				}
				main.gui.progressbar.setValue(main.gui.progressbar.getValue()+1);
			}
			
			//Hero Entity;
			try {
				
				LLOutputStream out = new LLOutputStream(new FileOutputStream(new LLFile(folder+hero.getEntry())));
				out.writeString(xmlToString(modheroentity));
				out.close();
				
			} catch (IOException e) {
				main.log.print(e,"Failed to create or write to "+hero.getEntry());
			}
			//Abilities;
			for(int ii = 0;ii <= ability.length-1;ii++) {
				try {
					if(ability[ii] != null) {
						LLOutputStream out = new LLOutputStream(new FileOutputStream(new LLFile(folder+ability[ii])));
						out.writeString(xmlToString(modabilityentity[ii]));
						out.close();
					}
				} catch (IOException e) {
					main.log.print(e,"Failed to create or write to "+ability[ii]);
				}
			}
		}
		//Zipping;
		LLFile filefolder;
		try {
			filefolder = new LLFile(folder,false);
			if(filefolder.exists()) {
				main.gui.progresslabel.setText("Zipping resources...");
				ZipOutputStream zut = new ZipOutputStream(new FileOutputStream(new LLFile(main.config.property.getProperty("Setting_resourcesCoN",main.config.property.getProperty("Setting_resources","")),true)));
				
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
				zut.close();
			}
			else {
				new LLFile(main.config.property.getProperty("game","")+"resourcesCoN.s2z",true).delete();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
				/*
						
						//Extracting to see if required a modification;
						for(int iii = 0;iii <= abilities.length-1;iii++) {
							//System.out.println(mode.getAttribute("key")+" to "+e.getAttribute("key")+" = "+abilities[iii]);
							Element ability = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(resources.getInputStream(resources.getEntry(abilities[iii]))).getDocumentElement();
							Element modability = figureFile(folder+abilities[iii]);
							if(modability == null) {
								modability = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(resources.getInputStream(resources.getEntry(abilities[iii]))).getDocumentElement();
							}
							
							Element a = figureModifer(hero.getAvatar(index).getKey(),ability);
							Element moda = figureModifer(hero.getAvatar(ii).getKey(),modability);
							if(a == null || moda ==null) {
								continue;
							}
							//Change attibutes...
							moda.setAttribute("icon",getAttribute(a.getAttribute("icon"),getAttribute(ability.getAttribute("icon"),"")));
							moda.setAttribute("casteffect",getAttribute(a.getAttribute("casteffect"),getAttribute(ability.getAttribute("casteffect"),"")));
							moda.setAttribute("passiveeffect",getAttribute(a.getAttribute("passiveeffect"),getAttribute(ability.getAttribute("passiveeffect"),"")));
							
							//Exporting it because we wont be changing the content of it no more atleast...
							transformer = TransformerFactory.newInstance().newTransformer();
							transformer.transform(new DOMSource(modability),new StreamResult(new LLFile(folder+abilities[iii])));
							
							//Path location for the file directory;
							path = "/"+abilities[iii].replaceFirst("/\\w+\\.{1}entity","/");
							while(path.contains("\\")) {
								path.replace("\\","/");
							}
							
							//Change effects,
							NodeList al = a.getChildNodes();
							NodeList modal = moda.getChildNodes();
							
							for(int iiii = 0;iiii <= al.getLength()-1;iiii++) {
								if(al.item(iiii).getNodeType() == Node.ELEMENT_NODE) {
									Element aelement = (Element) al.item(iiii);
									
									if(!aelement.getTagName().equals("modifier")) {
										for(int iiiii = 0;iiiii <= modal.getLength()-1;iiiii++) {
											if(modal.item(iiiii).getNodeType() == Node.ELEMENT_NODE) {
												Element modaelement = (Element) modal.item(iiiii);
												
												if(aelement.getTagName().equals(modaelement.getTagName())) {
													//If everything matches means we're in both the same elements, Now let's look for keytags like playeffect...
													NodeList al2 = aelement.getElementsByTagName("playeffect");
													NodeList modal2 = modaelement.getElementsByTagName("playeffect");
													
													for(int iiiiii = 0;iiiiii <= al2.getLength()-1;iiiiii++) {
														if(iiiiii <= modal2.getLength()-1) {
															String ae = ((Element) al2.item(iiiiii)).getAttribute("effect");
															String modae = ((Element) modal2.item(iiiiii)).getAttribute("effect");
															//System.out.println(modaelement.getTagName()+" -> "+mode.getAttribute("key")+" to "+e.getAttribute("key")+" = "+modae+" to "+ae);
															
															//Will be have do done twice, for both ae and modae;
															if(!ae.startsWith("/") && !ae.isEmpty()) {
																ae = path+ae;
																
																while(ae.contains("/../")) {
																	ae = ae.replaceFirst("/\\w+/\\.{2}/","/");
																	System.out.println("stuck1");
																}
															}if(!modae.startsWith("/") && !modae.isEmpty()) {
																modae = path+modae;

																while(modae.contains("/../")) {
																	modae = modae.replaceFirst("/\\w+/\\.{2}/","/");
																	System.out.println("stuck2");
																}
															}
															
															//Now let's change them!
															LLFile file = new LLFile(folder+modae.substring(1)); //Remove first character since it'll be "/" to prevent "*//*";
															if(!(file.length() > 0)) {
																StringBuilder sb = new StringBuilder();
																
																ZipEntry zipentry =main.config.resources.getEntry(ae.substring(1));
																if(zipentry == null) {
																	System.out.println("NullPointerException: "+ae);
																	continue;
																}
																
																LLInputStream in2 = new LLInputStream(resources.getInputStream(zipentry)); //Same case as for 3 lines above;
																while(in2.available() > 0) {
																	sb.append(in2.readString(4*4*1024));
																}
																
																//Need to fix all the paths now;
																String effect = sb.toString();
																
																String path2 = ae.replaceFirst("/\\w+\\.{1}effect","/");

																effect = effect.replace("sample=\"","sample=\""+path2);
																effect = effect.replace("material=\"","material=\""+path2);
																effect = effect.replace("model=\"","model=\""+path2);
																
																effect = effect.replaceAll(path2+"/","/");
																while(effect.contains("/../")) {
																	effect = effect.replaceFirst("/\\w+/\\.{2}/","/");
																	System.out.println("stuck3");
																}
																
																//And now finally output!
																LLOutputStream ut = new LLOutputStream(new FileOutputStream(file));
																ut.writeString(effect);
																ut.close();
																System.out.println(hero.getAvatar(ii).getKey());
															}
														}
													}
												}
											}
										}
									}
								}
							}
						}
					}
				}
			}*/
		return true;
	}
	
	private String[] getProjectile(String modprojectile, String orgprojectile,String[] projectiles) {
		Element orgprojectileentity = null,	modprojectileentity = null;
		String	orgprojectilepath	= null,	modprojectilepath	= null;
		
		//Finding the right projectiles;
		for(String projectile:projectiles) {
			Element element;
			try {
				element = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(main.config.resources.getInputStream(main.config.resources.getEntry(projectile))).getDocumentElement();
			} catch (SAXException | IOException | ParserConfigurationException e) {
				e.printStackTrace();
				return null;
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
			return null;
		}
		
		
		//Changing the attributes;
		//Scales;
		modprojectileentity.setAttribute("gravity",getAttribute(orgprojectileentity.getAttribute("gravity"),""));
		modprojectileentity.setAttribute("modelscale",getAttribute(orgprojectileentity.getAttribute("modelscale"),"1.0"));
		
		//Effects;
		modprojectileentity.setAttribute("model",fixPath(getAttribute(orgprojectileentity.getAttribute("model"),""),orgprojectilepath));
		modprojectileentity.setAttribute("impacteffect",fixPath(getAttribute(orgprojectileentity.getAttribute("impacteffect"),""),orgprojectilepath));
		modprojectileentity.setAttribute("traileffect",fixPath(getAttribute(orgprojectileentity.getAttribute("traileffect"),""),orgprojectilepath));
		
		String[] re = {modprojectilepath,xmlToString(modprojectileentity)};
		return re;
	}
	public Element getModifier(String key,Element entity,String tag) {
		if(entity.getAttribute("key").equalsIgnoreCase(key)) {
			return entity;
		}
		
		NodeList list = entity.getElementsByTagName(tag);
		for(int i = 0;i <= list.getLength()-1;i++) {
			Element element = (Element) list.item(i);
			
			if(element.getAttribute("key").equalsIgnoreCase(key)) {
				return element;
			}
		}
		return null;
	}
	public Element getModifier(String key,Element entity) {
		return getModifier(key,entity,"modifier");
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
	
	//Implements;
	
}


