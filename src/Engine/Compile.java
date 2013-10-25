package Engine;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringWriter;
import java.util.Enumeration;
import java.util.Vector;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipOutputStream;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.TransformerFactoryConfigurationError;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.ls.DOMImplementationLS;
import org.w3c.dom.ls.LSSerializer;
import org.xml.sax.SAXException;

import Doodads.LLFile;
import Doodads.LLInputStream;
import Doodads.LLOutputStream;
import Engine.Attributes.Hero;

public class Compile {
	
	Main main;
	
	public Compile(Main main) {
		this.main = main;
		Gui gui = main.gui;
		
		int size = 0;
		for(int i = 0;i <= main.config.heroes.size()-1;i++) {
			size += main.config.heroes.get(i).getAvatarCount();
		}
		
		gui.progressbar.setValue(0);
		gui.progressbar.setMaximum(size+1);
		gui.progresslabel.setText("Applying modifications...");
		try {
			LLInputStream in;
			StringBuilder data = new StringBuilder();
			ZipFile resources = new ZipFile(new File(main.config.property.getProperty("game","")+"resources0.s2z"));
			
			Transformer transformer;
			
			String folder = Main.PATH+"instances"+File.separator+System.currentTimeMillis()+File.separator;
			
			//Compiling heroes...
			for(int i = 0;i <= main.config.heroes.size()-1;i++) {
				Hero hero = main.config.heroes.get(i);
				
				boolean dohero = false;
				for(int ii = 0;ii <= hero.getAvatarCount()-1;ii++) {
					int index = figureIndex(i,ii);
					
					if(ii != index) {
						dohero = true;
						break;
					}
				}
				
				if(dohero) {
					Element entity = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(resources.getInputStream(resources.getEntry(hero.getEntry()))).getDocumentElement();
					
					for(int ii = 0;ii <= hero.getAvatarCount()-1;ii++) {
						gui.progresslabel.setText("Compiling "+hero.getAvatar(ii).getName()+"...");
						
						int index = figureIndex(i,ii);
						
						//Reading the modfied here and if there is none then make one by reading from the original!
						Element modentity = figureFile(folder+hero.getEntry());
						if(modentity == null) {
							modentity = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(resources.getInputStream(resources.getEntry(hero.getEntry()))).getDocumentElement();
						}
						
						Element e = figureModifer(hero.getAvatar(index).getKey(),entity);
						Element mode = figureModifer(hero.getAvatar(ii).getKey(),modentity);
						
						//Changing the attributes;
						
						//Models & Effects;
						mode.setAttribute("portrait",getAttribute(e.getAttribute("portrait"),getAttribute(entity.getAttribute("portrait"),"")));
						
						mode.setAttribute("passiveeffect",getAttribute(e.getAttribute("passiveeffect"),getAttribute(entity.getAttribute("passiveeffect"),""))); //Doing this after the others due to "storepassiveeffect" 'might' need it and then it has to be with the original value;
						mode.setAttribute("previewpassiveeffect",getAttribute(e.getAttribute("previewpassiveeffect"),getAttribute(entity.getAttribute("previewpassiveeffect"),getAttribute(e.getAttribute("passiveeffect"),entity.getAttribute("passiveeffect")))));
						mode.setAttribute("storepassiveeffect","");
						
						mode.setAttribute("previewmodel",getAttribute(e.getAttribute("previewmodel"),getAttribute(entity.getAttribute("previewmodel"),"")));
						mode.setAttribute("storepos",getAttribute(e.getAttribute("storepos"),getAttribute(entity.getAttribute("storepos"),"")));
						mode.setAttribute("storescale",getAttribute(e.getAttribute("storescale"),getAttribute(entity.getAttribute("storescale"),"")));
						
						//Scales;
						mode.setAttribute("preglobalscale",getAttribute(e.getAttribute("preglobalscale"),getAttribute(entity.getAttribute("preglobalscale"),"")));
						mode.setAttribute("modelscale",getAttribute(e.getAttribute("modelscale"),getAttribute(entity.getAttribute("modelscale"),"")));
						mode.setAttribute("effectscale",getAttribute(e.getAttribute("effectscale"),getAttribute(entity.getAttribute("effectscale"),"")));
						mode.setAttribute("infoheight",getAttribute(e.getAttribute("infoheight"),getAttribute(entity.getAttribute("infoheight"),"")));
						mode.setAttribute("tiltfactor",getAttribute(e.getAttribute("tiltfactor"),getAttribute(entity.getAttribute("tiltfactor"),"")));
						mode.setAttribute("tiltspeed",getAttribute(e.getAttribute("tiltspeed"),getAttribute(entity.getAttribute("tiltspeed"),"")));
						
						//Sounds;
						mode.setAttribute("selectedsound",getAttribute(e.getAttribute("selectedsound"),getAttribute(entity.getAttribute("selectedsound"),"")));
						mode.setAttribute("selectedflavorsound",getAttribute(e.getAttribute("selectedflavorsound"),getAttribute(entity.getAttribute("selectedflavorsound"),"")));
						mode.setAttribute("confirmmovesound",getAttribute(e.getAttribute("confirmmovesound"),getAttribute(entity.getAttribute("confirmmovesound"),"")));
						mode.setAttribute("confirmattacksound",getAttribute(e.getAttribute("confirmattacksound"),getAttribute(entity.getAttribute("confirmattacksound"),"")));
						mode.setAttribute("nomanasound",getAttribute(e.getAttribute("nomanasound"),getAttribute(entity.getAttribute("nomanasound"),"")));
						mode.setAttribute("cooldownsound",getAttribute(e.getAttribute("cooldownsound"),getAttribute(entity.getAttribute("cooldownsound"),"")));
						mode.setAttribute("tauntedsound",getAttribute(e.getAttribute("tauntedsound"),getAttribute(entity.getAttribute("tauntedsound"),"")));
						mode.setAttribute("tauntkillsound",getAttribute(e.getAttribute("tauntkillsound"),getAttribute(entity.getAttribute("tauntkillsound"),"")));
						
						//Attacks;
						mode.setAttribute("attackoffset",getAttribute(e.getAttribute("attackoffset"),getAttribute(entity.getAttribute("attackoffset"),"")));
						mode.setAttribute("attackstarteffect",getAttribute(e.getAttribute("attackstarteffect"),getAttribute(entity.getAttribute("attackstarteffect"),"")));
						mode.setAttribute("attackactioneffect",getAttribute(e.getAttribute("attackactioneffect"),getAttribute(entity.getAttribute("attackactioneffect"),"")));
						mode.setAttribute("attackimpacteffect",getAttribute(e.getAttribute("attackimpacteffect"),getAttribute(entity.getAttribute("attackimpacteffect"),"")));
						
						//Model;
						String path;
						if(mode.getAttribute("model").startsWith("/")) {
							path = mode.getAttribute("model");
						}
						else {
							path = "heroes/"+hero.getFolder()+"/"+mode.getAttribute("model");
						}
						
						LLOutputStream out = new LLOutputStream(new FileOutputStream(new LLFile(folder+path)));
						out.writeString(getModel(mode.getAttribute("model"),e.getAttribute("model"),resources,hero.getFolder()));
						out.close();
						
						//Projectile;
						if(!entity.getAttribute("attackprojectile").isEmpty()) {
							Vector<String> projectiles = new Vector<String>();
							
							//Fetching projectiles;
							Enumeration entries = resources.entries();
							while(entries.hasMoreElements()) {
								String entry = entries.nextElement().toString();
								
								if(entry.startsWith("heroes/"+hero.getFolder()) && entry.endsWith(".entity") && entry.contains("projectile")) {
									projectiles.add(entry);
								}
							}
							
							String projectile = "";
							String modprojectile = "";
							
							//Finding the right projectiles;
							for(int iii = 0;iii <= projectiles.size()-1;iii++) {
								String name = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(resources.getInputStream(resources.getEntry(projectiles.get(iii)))).getDocumentElement().getAttribute("name");
								
								if(getAttribute(e.getAttribute("attackprojectile"),getAttribute(entity.getAttribute("attackprojectile"),"")).equals(name)) {
									projectile = projectiles.get(iii);
								}
								if(getAttribute(mode.getAttribute("attackprojectile"),getAttribute(entity.getAttribute("attackprojectile"),"")).equals(name)) {
									modprojectile = projectiles.get(iii);
								}
								
								if(!projectile.isEmpty() && !modprojectile.isEmpty()) {
									break;
								}
							}
							if(projectile.isEmpty() || modprojectile.isEmpty()) {
								System.out.println("This shouldn't have happend and now you've got to report this so I can fix it because else your fucked!");
								return;
							}
							
							//Time to modify UNLESS it's already been modified that is;
							LLFile projectfile = new LLFile(folder+modprojectile);
							if(!(projectfile.length() > 0)) {
								Element p = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(resources.getInputStream(resources.getEntry(projectile))).getDocumentElement();
								Element modp = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(resources.getInputStream(resources.getEntry(modprojectile))).getDocumentElement();
								
								//Changing the attributes;
								
								//Scales;
								modp.setAttribute("gravity",getAttribute(p.getAttribute("gravity"),""));
								modp.setAttribute("modelscale",getAttribute(p.getAttribute("modelscale"),"1.0"));
								
								//Effects;
								path = "/"+projectile.replaceFirst("/[0-9a-zA-Z-]+.entity","/");
								while(path.contains("\\")) {
									path.replace("\\","/");
								}
								
								String[] attributes = {"model","impacteffect","traileffect"};
								for(int iii = 0;iii <= attributes.length-1;iii++) {
									String fixedpath = p.getAttribute(attributes[iii]);
									
									if(!fixedpath.startsWith("/") && !fixedpath.isEmpty()) {
										fixedpath = path+fixedpath;
										
										while(fixedpath.contains("/../")) {
											fixedpath = fixedpath.replaceFirst("/\\w+/\\.{2}/","/");
										}
									}
									
									modp.setAttribute(attributes[iii],fixedpath);
								}
								
								//Write it;
								transformer = TransformerFactory.newInstance().newTransformer();
								transformer.transform(new DOMSource(modp),new StreamResult(projectfile));
							}
						}
						//Abilities;
						Vector<String> abilitiesfiles = new Vector<String>();
						
						//Fetching abilities;
						Enumeration entries = resources.entries();
						while(entries.hasMoreElements()) {
							String entry = entries.nextElement().toString();
							
							if(entry.startsWith("heroes/"+hero.getFolder()) && entry.endsWith(".entity") && entry.contains("ability")) {
								abilitiesfiles.add(entry);
							}
						}
						
						String[] abilities = {"","","",""};
						
						//Finding the right abilities;
						for(int iii = 0;iii <= abilities.length-1;iii++) {
							for(int iiii = 0;iiii <= abilitiesfiles.size()-1;iiii++) {
								String name = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(resources.getInputStream(resources.getEntry(abilitiesfiles.get(iiii)))).getDocumentElement().getAttribute("name");
								
								if(entity.getAttribute("inventory"+iii).equals(name)) {
									abilities[iii] = abilitiesfiles.get(iiii);
									break;
								}
							}
							
							if(abilities[iii].isEmpty()) {
								System.out.println("Fuck me,");
								return;
							}
						}
						
						//Extracting to see if required a modification;
						for(int iii = 0;iii <= abilities.length-1;iii++) {
							System.out.println(mode.getAttribute("key")+" to "+e.getAttribute("key")+" = "+abilities[iii]);
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
							path = "/"+abilities[iii].replaceFirst("/[0-9a-zA-Z-]+.entity","/");
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
															System.out.println(modaelement.getTagName()+" -> "+mode.getAttribute("key")+" to "+e.getAttribute("key")+" = "+modae+" to "+ae);
															
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
																
																ZipEntry zipentry = resources.getEntry(ae.substring(1));
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

																effect = effect.replace("sample=\"","sample=\""+path);
																effect = effect.replace("material=\"","material=\""+path);
																effect = effect.replace("model=\"","model=\""+path);
																
																effect = effect.replaceAll(path+"/","/");
																while(effect.contains("/../")) {
																	effect = effect.replaceFirst("/\\w+/\\.{2}/","/");
																	System.out.println("stuck3");
																}
																
																//And now finally output!
																LLOutputStream ut = new LLOutputStream(new FileOutputStream(file));
																ut.writeString(effect);
																ut.close();
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
						
						//Output
						transformer = TransformerFactory.newInstance().newTransformer();
						transformer.transform(new DOMSource(modentity),new StreamResult(new File(folder+hero.getEntry())));
						
						gui.progressbar.setValue(gui.progressbar.getValue()+1);
					}
				}
			}
			
			LLFile filefolder = new LLFile(folder,false);
			if(filefolder.exists()) {
				gui.progresslabel.setText("Zipping resources...");
				ZipOutputStream zut = new ZipOutputStream(new FileOutputStream(new LLFile(main.config.property.getProperty("game")+"resourcesCoN.s2z",true)));
				
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
					
					//System.out.println(path);
				}
				
				zut.close();
			}
			else {
				new LLFile(main.config.property.getProperty("game")+"resourcesCoN.s2z",true).delete();
			}
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (SAXException e) {
			e.printStackTrace();
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		} catch (TransformerConfigurationException e) {
			e.printStackTrace();
		} catch (TransformerFactoryConfigurationError e) {
			e.printStackTrace();
		} catch (TransformerException e) {
			e.printStackTrace();
		}
	}
	
	public int figureIndex(int i,int ii) {
		String name = main.config.heroes.get(i).getName();
		String key	= main.config.heroes.get(i).getAvatar(ii).getKey();
		
		if(main.config.property.getProperty(name,-1) > -1) {
			return main.config.property.getProperty(name,-1);
		}
		else {
			return main.config.property.getProperty(name+"."+key,ii);
		}
	}
	public Element figureFile(String filepath) throws FileNotFoundException, SAXException, IOException, ParserConfigurationException {
		LLFile file = new LLFile(filepath);
		
		if(file.length() > 0) {
			return DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(new FileInputStream(file)).getDocumentElement();
		}
		else {
			return null;
		}
	}
	public Element figureModifer(String key,Element entity) {
		if(key == null) {
			return entity;
		}
		
		NodeList list = entity.getElementsByTagName("modifier");
		
		for(int i = 0;i <= list.getLength()-1;i++) {
			Element element = (Element) list.item(i);
			
			if(element.getAttribute("key").toLowerCase().equals(key.toLowerCase())) {
				return element;
			}
		}
		
		//System.out.println("Shits about to happen, "+entity.getAttribute("name")+" "+key);
		return null;
	}
	public String getAttribute(String attribute,String backup) {
		if(attribute.isEmpty()) {
			return backup;
		}
		return attribute;
	}
	
	public String getModel(String modeli,String modelii,ZipFile zf,String key0) {
		String[] s = new String[2];
		s[0] = "";
		s[1] = "";
		//Reading the hero entity;
		ZipEntry ze = zf.getEntry("heroes"+"/"+key0+"/"+modeli);
		long size = ze.getSize();
		if (size > 0) {
			BufferedReader br;
			try {
				br = new BufferedReader(
				new InputStreamReader(zf.getInputStream(ze)));
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
		ze = zf.getEntry("heroes"+"/"+key0+"/"+modelii);
		size = ze.getSize();
		if (size > 0) {
			BufferedReader br;
			try {
				br = new BufferedReader(
				new InputStreamReader(zf.getInputStream(ze)));
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
	
}