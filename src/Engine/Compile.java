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
import java.util.zip.ZipException;
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
					main.log.print(e,"Failed to load or parse "+abilities.get(ii));
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
						main.log.print(e,"Failed to write to "+modheromodifier.getAttribute("model"));
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
							main.log.print(e,"Failed to write to "+projectile[0]);
						}
					}
					//Abilities
					for(int iii = 0;iii <= ability.length-1;iii++) {
						if(ability[iii] != null) {
							Element orgabilitymodifier = getModifier(hero.getAvatar(index).getKey(),orgabilityentity[iii]);
							Element modabilitymodifier = getModifier(hero.getAvatar(ii).getKey(),modabilityentity[iii]);
							
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
				main.log.print(e,"Failed to write to "+hero.getEntry());
			}
			
			for(int ii = 0;ii <= ability.length-1;ii++) {
				try {
					if(ability[ii] != null) {
						LLOutputStream out = new LLOutputStream(new FileOutputStream(new LLFile(folder+ability[ii])));
						out.writeString(xmlToString(modabilityentity[ii]));
						out.close();
					}
				} catch (IOException e) {
					main.log.print(e,"Failed to write to "+ability[ii]);
				}
			}
		}
		//Zip...
		LLFile filefolder;
		try {
			filefolder = new LLFile(folder,false);
			if(filefolder.exists()) {
				main.gui.progresslabel.setText("Zipping resources...");
				ZipOutputStream zut = new ZipOutputStream(new FileOutputStream(new LLFile(main.config.property.getProperty("Setting_ResourcesCoN",main.config.property.getProperty("Setting_Resources","").replaceFirst("\\"+File.separator+"\\w+\\.{1}s2z","\\"+File.separator+"resourcesCoN.s2z")),true)));
				
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
				new LLFile(main.config.property.getProperty("game","")+"resourcesCoN.s2z",true).delete();
			}
		} catch (IOException e) {
			main.log.print(e,"Failed to write to "+main.config.property.getProperty("Setting_ResourcesCoN",main.config.property.getProperty("Setting_Resources","").replaceFirst("/\\w+\\.{1}s2z","resourcesCoN.s2z")));
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
	
}