import java.awt.Graphics;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.HeadlessException;
import java.awt.Image;
import java.awt.Transparency;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.PixelGrabber;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipOutputStream;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;

public class Compile {
	Main main;
	
	ZipFile zf;
	Enumeration entries;
	BufferedReader input;
	
	public Compile(Main main) {
		this.main = main;
		try {
			zf = new ZipFile(main.config.getValue("gamepath","FAILURE")+File.separator+"game"+File.separator+"resources0.s2z");
			entries = zf.entries();
			input = new BufferedReader(new InputStreamReader(System.in));
		} catch (IOException e) {
			JOptionPane.showMessageDialog(null,"Could not read "+main.config.getValue("gamepath","FAILURE")+File.separator+"game"+File.separator+"resources0.s2z");
			main.gui.compile_compile.setEnabled(false);
			e.printStackTrace();
		}
	}
	public void writeFile(String path,String body) {
		File file = new File(path);
		DataOutputStream out = null;
		if(file.exists() == false) {
			new File(System.getenv("APPDATA")+""+File.separator+"Lindholm"+File.separator+"Customization_of_Newerth"+File.separator+"").mkdirs();
			try {
				file.createNewFile();
			} catch (IOException e) {
			}
		}
		try {
			out = new DataOutputStream(new FileOutputStream(file));
			out.writeBytes(body);
			out.flush();
			out.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	public void get() {
		Gui gui = main.gui;
		String mod = "";
		boolean y = false;
		boolean x = false;
		String dis = "";
		//Checking what to compile;
		if(gui.compile_althero.getSelectedObjects() != null) {
			String string = "";
			//Generating couriers;
			Hero hero = main.config.hero[0];
			for(int cur = 0; cur <= hero.avatar;cur += 1) {
				main.gui.frame.validate();
				//Checking if the avatar should be modified; | If a earlier courier has been modified the following must too;
				if(main.config.getValue(""+hero.key[0]+cur,cur) != cur | x == true) {
					x = true;
					int alt = main.config.getValue(""+hero.key[0]+cur,cur);
					string += "	<editfile name='items/basic/ground_familiar/pet_courier.entity'>\n";
					if(cur == 0) {
						string += "	<find><![CDATA[icon='icon.tga'\n	portrait='icon.tga'\n	model='model.mdf']]></find><delete/><find position='start'/>\n" +
								"	<find><![CDATA[preglobalscale='1.33']]></find><delete/><find position='start'/>\n" +
								"	<find><![CDATA[passiveeffect='']]></find><delete/><find position='start'/>\n" +
								"	<find><![CDATA[previewmodel='model.mdf']]></find><delete/><find position='start'/>\n" +
								"	<find><![CDATA[previewscale='1.33']]></find><delete/><find position='start'/>\n" +
								"	<find><![CDATA[previewpos='0 0 -50']]></find><delete/><find position='start'/>\n" +
								"	<find><![CDATA[skin='']]></find><insert position='after'><![CDATA[ "+hero.ground[alt]+"]]></insert><find position='start'/>\n";
					}
					else {
						string += "		<find><![CDATA[<modifier key='"+hero.key[cur]+"' mod]]></find><replace><![CDATA[<!-- Customization of Newerth was here! \n<modifier key='"+hero.key[cur]+"']]></replace>\n" +
						"		<find><![CDATA[</modifier>]]></find><replace><![CDATA[</modifier>-->]]></replace>\n" +
						"		<insert position='after'><![CDATA[\n	<modifier key='"+hero.key[cur]+"' modpriority='1' altavatar='true' dynamicavatar='true'\n"+hero.ground[alt]+"\n >\n	</modifier>]]></insert>\n";
					}
					string += "	</editfile>\n" +
								"	<editfile name='items/recipes/flying_courier/pet_flying_courier.entity'>\n";
					if(cur == 0) {
						string += "	<find><![CDATA[icon='icon.tga'\n	portrait='icon.tga'\n	model='model.mdf']]></find><delete/><find position='start'/>\n" +
						"	<find><![CDATA[preglobalscale='1.0']]></find><delete/><find position='start'/>\n" +
						"	<find><![CDATA[passiveeffect='']]></find><delete/><find position='start'/>\n" +
						"	<find><![CDATA[previewmodel='model.mdf']]></find><delete/><find position='start'/>\n" +
						"	<find><![CDATA[previewscale='1.33']]></find><delete/><find position='start'/>\n" +
						"	<find><![CDATA[previewpos='0 0 -10']]></find><delete/><find position='start'/>\n" +
						"	<find><![CDATA[skin='']]></find><insert position='after'><![CDATA[ "+hero.flying[alt]+"]]></insert><find position='start'/>\n";
					}
					else {
						string += "		<find><![CDATA[<modifier key='"+hero.key[cur]+"' mod]]></find><replace><![CDATA[<!-- Customization of Newerth was here! \n<modifier key='"+hero.key[cur]+"']]></replace>\n" +
						"		<find><![CDATA[</modifier>]]></find><replace><![CDATA[</modifier>-->]]></replace>\n" +
						"		<insert position='after'><![CDATA[\n	<modifier key='"+hero.key[cur]+"' modpriority='1' altavatar='true' dynamicavatar='true'\n"+hero.flying[alt]+"\n >\n	</modifier>]]></insert>\n";
					}
					string += "	</editfile>\n";
				}
			}
			//Generating heroes;
			for(int h = 1; h <= main.config.h;h += 1) {
				main.gui.frame.validate();
				string += main.compile.getHero(main.config.hero[h]);
			}
			if(string.equals("") == false) {
				mod += string;
				dis += "*Hero Avatars\n";
			}
		}
		if(gui.compile_recommended.getSelectedObjects() != null) {
		}
		if(gui.compile_indicator.getSelectedObjects() != null) {
		}
		if(gui.compile_announcers.getSelectedObjects() != null) {
			String string = "";
			int sell = main.config.sell;
			x = false;
			//Checking if announcers have been modified;
			for(int i = 0; i <= main.config.a;i++) {
				if(main.config.getValue(main.config.announcer[i].voice,i) != i) {
					x = true;
					if(main.config.getValue(main.config.announcer[i].voice,i) == sell) {
						y = true;
						i = main.config.a+1;
					}
				}
			}
			if(y == true) {
				
			}
			//If any announcer has been modified all have to be;
			if(x == true) {
				//Installing default package & normal arcade text
				for(int i = 0;i <= 37;i++) {
					string += "	<copyfile name='"+main.config.announcer[0].sound[i].replace("/announcer/","/announcer/default/")+"' source='"+main.config.announcer[0].sound[i]+"' overwrite='yes' fromresource='true' />\n";
				}
				for(int i = 0;i <= 19;i++) {
					string += "	<copyfile name='"+main.config.announcer[0].text[i].replace("/arcade_text/","/normal/")+".mdf' source='"+main.config.announcer[0].text[i]+".mdf' overwrite='yes' fromresource='true' />\n";
					string += "	<copyfile name='"+main.config.announcer[0].text[i].replace("/arcade_text/","/normal/")+".model' source='"+main.config.announcer[0].text[i]+".model' overwrite='yes' fromresource='true' />\n";
					string += "	<copyfile name='"+main.config.announcer[0].text[i].replace("/arcade_text/","/normal/")+".clip' source='"+main.config.announcer[0].text[i]+".clip' overwrite='yes' fromresource='true' />\n";
				}
				string += "	<copyfile name='ui/common/models/normal/arrow2.clip' source='ui/common/models/arcade_text/arrow2.clip' overwrite='yes' fromresource='true' />\n";
				string += "	<copyfile name='ui/common/models/normal/material.material' source='ui/common/models/arcade_text/material.material' overwrite='yes' fromresource='true' />\n";
				string += "	<editfile name='ui/common/models/normal/material.material'><findall><![CDATA[color.tga]]></findall><replace><![CDATA[/ui/common/models/arcade_text/color.tga]]></replace></editfile>";
				string += "	<copyfile name='ui/common/models/normal/material2.material' source='ui/common/models/arcade_text/material2.material' overwrite='yes' fromresource='true' />\n";
				string += "	<editfile name='ui/common/models/normal/material.material'><findall><![CDATA[color.tga]]></findall><replace><![CDATA[/ui/common/models/arcade_text/color.tga]]></replace></editfile>";
				//Generating announcer;
				for(int i = 0; i <= main.config.a;i++) {
					string += main.compile.getAnnouncer(i);
				}
				mod += string;
				dis += "*Announcers\n";
			}
		}
		if(gui.compile_taunt.getSelectedObjects() != null) {
			String string = "";
			for(int i = 0;i <= main.config.t;i++) {
				if(main.config.getValue(main.config.taunt[i].key,i) != i) {
					int cur = i;
					int alt = main.config.getValue(main.config.taunt[i].key,i);
					Taunt[] taunt = main.config.taunt;
					String[] path = new String[10];
					path = taunt[alt].file.split("/");
					String dir0 = "/";
					String dir1 = "/";
					String dir2 = "/";
					String dir3 = "/";
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
					string += 	"\n	<copyfile name='"+taunt[cur].file+"' source='"+taunt[alt].file+"' overwrite='yes' fromresource='true' />\n" +
								"	<editfile name='"+taunt[cur].file+"'>\n" +
								"		<findall><![CDATA[sample=']]></findall><replace><![CDATA[sample='"+dir0+"]]></replace><find position='start'/>\n" +
								"		<findall><![CDATA[material=']]></findall><replace><![CDATA[material='"+dir0+"]]></replace><find position='start'/>\n" +
								"		<findall><![CDATA[model=']]></findall><replace><![CDATA[model='"+dir0+"]]></replace><find position='start'/>\n" +
								"		<findall><![CDATA[icon=']]></findall><replace><![CDATA[icon='"+dir0+"]]></replace><find position='start'/>\n" +
								"		<findall><![CDATA[portrait=']]></findall><replace><![CDATA[portrait='"+dir0+"]]></replace><find position='start'/>\n" +
								"		<findall><![CDATA[effect=']]></findall><replace><![CDATA[effect='"+dir0+"]]></replace><find position='start'/>\n" +
								"		<findall><![CDATA[Linear ]]></findall><replace><![CDATA[Linear "+dir0+"]]></replace><find position='start'/>\n" +
								"		<findall><![CDATA[Effect ]]></findall><replace><![CDATA[Effect "+dir0+"]]></replace><find position='start'/>\n" +
								//models
								"		<findall><![CDATA[file=']]></findall><replace><![CDATA[file='"+dir0+"]]></replace><find position='start'/>\n" +
								"		<findall><![CDATA[low=']]></findall><replace><![CDATA[low='"+dir0+"]]></replace><find position='start'/>\n" +
								"		<findall><![CDATA[med=']]></findall><replace><![CDATA[med='"+dir0+"]]></replace><find position='start'/>\n" +
								"		<findall><![CDATA[high=']]></findall><replace><![CDATA[high='"+dir0+"]]></replace><find position='start'/>\n" +
								"		<findall><![CDATA[clip=']]></findall><replace><![CDATA[clip='"+dir0+"]]></replace><find position='start'/>\n" +
								//remove failure's
								"		<findall><![CDATA["+dir0+"/heroes]]></findall><replace><![CDATA[/heroes]]></replace><find position='start'/>\n" +
								"		<findall><![CDATA["+dir0+"/shared]]></findall><replace><![CDATA[/shared]]></replace><find position='start'/>\n" +
								"		<findall><![CDATA["+dir0+"/ui]]></findall><replace><![CDATA[/ui]]></replace><find position='start'/>\n" +
								//"../"
								"		<findall><![CDATA["+dir0+"../../../]]></findall><replace><![CDATA["+dir3+"]]></replace><find position='start'/>\n" +
								"		<findall><![CDATA["+dir0+"../../]]></findall><replace><![CDATA["+dir2+"]]></replace><find position='start'/>\n" +
								"		<findall><![CDATA["+dir0+"../]]></findall><replace><![CDATA["+dir1+"]]></replace><find position='start'/>\n" +
								"	</editfile>\n";
				}
			}
			if(string.equals("") == false) {
				mod += string;
				dis += "*Taunts\n";
			}
		}
		//If enough data for a compile;
		if(dis.equals("") == false) {
			mod = "<?xml version='1.0' encoding='UTF-8'?>\n" +
					"<!--This file belongs to Customization of Newerth and before going any further I would recommended you to read the Terms of Service-->\n" +
					"<modification \n" +
					"	application='Heroes of Newerth'\n" +
					"	appversion='*'\n" +
					"	mmversion='1.3.6'\n" +
					"	name='Customization of Newerth'\n" +
					"	version='"+main.config.version+"'\n" +
					"	date='"+new SimpleDateFormat("dd/MM/yyyy").format(new Date())+"'\n" +
					"	description='This is a premade mod created by CoN.\nModifies:\n"+dis+"'" +
					"	author='Customization of Newerth'\n" +
					"	weblink='http://forums.heroesofnewerth.com/showthread.php?t=338461'" +
					"	>\n" +
						mod +
					"</modification>";
			mod = mod.replaceAll("'",'"'+"");
			//Writing mod.xml file;
			main.compile.writeFile(main.config.App_Data+File.separator+"Lindholm"+File.separator+"Customization_of_Newerth"+File.separator+"mod.xml",mod);
			//Zipping everything together at the chosen location;
			main.compile.zip(gui.dir[gui.compile_moddir.getSelectedIndex()]+gui.compile_modname.getText()+".honmod");
			if(new File(gui.dir[gui.compile_moddir.getSelectedIndex()]+gui.compile_modname.getText()+".honmod").exists() == false) {
				JOptionPane.showMessageDialog(null,"Dir=\""+gui.dir[gui.compile_moddir.getSelectedIndex()]+"\" could not be found");
			}
			else {
				JOptionPane.showMessageDialog(null,"Great success!");
			}
		}
		else {
			JOptionPane.showMessageDialog(null,"Nothing to compile!");
		}
	}
	public void zip(String outFilename) {
		//image saving
		File file = new File(System.getenv("APPDATA")+""+File.separator+"Lindholm"+File.separator+"Customization_of_Newerth"+File.separator+"icon.png");
		if(file.exists() == false) {
			try {
				ImageIO.write(toBufferedImage(this.main.gui.image.getImage()),"png",file);
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
		String[] filenames = new String[]{"mod.xml","icon.png"};
		// Create a buffer for reading the files
		byte[] buf = new byte[1024];

		try {
		    // Create the ZIP file
		    ZipOutputStream out = new ZipOutputStream(new FileOutputStream(outFilename));

		    // Compress the files
		    for (int i=0; i<filenames.length; i++) {
		        FileInputStream in = new FileInputStream(System.getenv("APPDATA")+""+File.separator+"Lindholm"+File.separator+"Customization_of_Newerth"+File.separator+""+filenames[i]);

		        // Add ZIP entry to output stream.
		        out.putNextEntry(new ZipEntry(filenames[i]));

		        // Transfer bytes from the file to the ZIP file
		        int len;
		        while ((len = in.read(buf)) > 0) {
		            out.write(buf, 0, len);
		        }

		        // Complete the entry
		        out.closeEntry();
		        in.close();
		    }

		    // Complete the ZIP file
		    out.close();
		} catch (IOException e) {
		}
	}
	public static BufferedImage toBufferedImage(Image image) {
	    if (image instanceof BufferedImage) {
	        return (BufferedImage)image;
	    }

	    // This code ensures that all the pixels in the image are loaded
	    image = new ImageIcon(image).getImage();

	    // Determine if the image has transparent pixels; for this method's
	    // implementation, see Determining If an Image Has Transparent Pixels
	    boolean hasAlpha = hasAlpha(image);

	    // Create a buffered image with a format that's compatible with the screen
	    BufferedImage bimage = null;
	    GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
	    try {
	        // Determine the type of transparency of the new buffered image
	        int transparency = Transparency.OPAQUE;
	        if (hasAlpha) {
	            transparency = Transparency.BITMASK;
	        }

	        // Create the buffered image
	        GraphicsDevice gs = ge.getDefaultScreenDevice();
	        GraphicsConfiguration gc = gs.getDefaultConfiguration();
	        bimage = gc.createCompatibleImage(
	            image.getWidth(null), image.getHeight(null), transparency);
	    } catch (HeadlessException e) {
	        // The system does not have a screen
	    }

	    if (bimage == null) {
	        // Create a buffered image using the default color model
	        int type = BufferedImage.TYPE_INT_RGB;
	        if (hasAlpha) {
	            type = BufferedImage.TYPE_INT_ARGB;
	        }
	        bimage = new BufferedImage(image.getWidth(null), image.getHeight(null), type);
	    }

	    // Copy image to buffered image
	    Graphics g = bimage.createGraphics();

	    // Paint the image onto the buffered image
	    g.drawImage(image, 0, 0, null);
	    g.dispose();

	    return bimage;
	}
	// This method returns true if the specified image has transparent pixels
	public static boolean hasAlpha(Image image) {
	    // If buffered image, the color model is readily available
	    if (image instanceof BufferedImage) {
	        BufferedImage bimage = (BufferedImage)image;
	        return bimage.getColorModel().hasAlpha();
	    }

	    // Use a pixel grabber to retrieve the image's color model;
	    // grabbing a single pixel is usually sufficient
	     PixelGrabber pg = new PixelGrabber(image, 0, 0, 1, 1, false);
	    try {
	        pg.grabPixels();
	    } catch (InterruptedException e) {
	    }

	    // Get the image's color model
	    ColorModel cm = pg.getColorModel();
	    return cm.hasAlpha();
	}
	//Alt hero
	public String getHero(Hero hero) {
		String string = "";
		boolean x = false;
		for(int cur = 0; cur <= hero.avatar;cur += 1) {
			String mod = "";
			String sub = "";
			if(this.main.config.getValue(""+hero.key[0]+cur,cur) != cur | x == true) {
				//If no data collected;
				String find;
				if(x == false) {
					//Reading the hero entity;
					ZipEntry ze = zf.getEntry("heroes"+"/"+hero.key[0]+"/"+hero.alt[0]+".entity");
					long size = ze.getSize();
					if (size > 0) {
						BufferedReader br;
						try {
							br = new BufferedReader(
							new InputStreamReader(zf.getInputStream(ze)));
						String linebreak;
						while ((linebreak = br.readLine()) != null) {
							hero.file += linebreak+"\n";
						}
						br.close();
						} catch (IOException e) {
							e.printStackTrace();
						}
					}
					//Collecting data for each alt;
					for(int i = 0;i <= hero.avatar;i++) {
						if(i == 0) {
							find = "<hero";
						}
						else {
							find = "<modifier key=\""+hero.key[i]+"\" mod";
						}
						//Removes uneeded data to avoid failure's while collecting data;
						int pos = hero.file.indexOf(find);
						hero.subfile = hero.file.substring(pos,hero.file.indexOf(">",pos)+">".length());
						//JOptionPane.showMessageDialog(null,hero.subfile);
						//Collecting the data;
						hero.icon[i] = getValue("icon=\"",hero.subfile,hero.icon[0]);
						hero.portrait[i] = getValue("portrait=\"",hero.subfile,hero.portrait[0]);
						hero.model[i] = getValue("model=\"",hero.subfile,hero.model[0]);
						hero.previewmodel[i] = getValue("previewmodel=\"",hero.subfile,hero.previewmodel[0]);
						hero.modelscale[i] = getValue("modelscale=\"",hero.subfile,hero.modelscale[0]);
						hero.effectscale[i] = getValue("effectscale=\"",hero.subfile,hero.effectscale[0]);
						hero.selectedsound[i] = getValue("selectedsound=\"",hero.subfile,hero.selectedsound[0]);
						hero.selectedflavorsound[i] = getValue("selectedflavorsound=\"",hero.subfile,hero.selectedflavorsound[0]);
						hero.confirmmovesound[i] = getValue("confirmmovesound=\"",hero.subfile,hero.confirmmovesound[0]);
						hero.confirmattacksound[i] = getValue("confirmattacksound=\"",hero.subfile,hero.confirmattacksound[0]);
						hero.nomanasound[i] = getValue("nomanasound=\"",hero.subfile,hero.nomanasound[0]);
						hero.cooldownsound[i] = getValue("cooldownsound=\"",hero.subfile,hero.cooldownsound[0]);
						hero.tauntedsound[i] = getValue("tauntedsound=\"",hero.subfile,hero.tauntedsound[0]);
						hero.tauntkillsound[i] = getValue("tauntkillsound=\"",hero.subfile,hero.tauntkillsound[0]);
						hero.preglobalscale[i] = getValue("preglobalscale=\"",hero.subfile,hero.preglobalscale[0]);
						hero.passiveeffect[i] = getValue("passiveeffect=\"",hero.subfile,hero.passiveeffect[0]);
						hero.infoheight[i] = getValue("infoheight=\"",hero.subfile,hero.infoheight[0]);
						hero.tiltfactor[i] = getValue("tiltfactor=\"",hero.subfile,hero.tiltfactor[0]);
						hero.tiltspeed[i] = getValue("tiltspeed=\"",hero.subfile,hero.tiltspeed[0]);
						hero.attackoffset[i] = getValue("attackoffset=\"",hero.subfile,hero.attackoffset[0]);
						hero.attackprojectile[i] = getValue("attackprojectile=\"",hero.subfile,hero.attackprojectile[0]);
						hero.attackstarteffect[i] = getValue("attackstarteffect=\"",hero.subfile,hero.attackstarteffect[0]);
						hero.attackactioneffect[i] = getValue("attackactioneffect=\"",hero.subfile,hero.attackactioneffect[0]);
						hero.attackimpacteffect[i] = getValue("attackimpacteffect=\"",hero.subfile,hero.attackimpacteffect[0]);
						hero.previewpos[i] = getValue("previewpos=\"",hero.subfile,hero.previewpos[0]);
						hero.previewscale[i] = getValue("previewscale=\"",hero.subfile,hero.previewscale[0]);
						hero.storepos[i] = getValue("storepos=\"",hero.subfile,hero.storepos[0]);
						hero.storescale[i] = getValue("storescale=\"",hero.subfile,hero.storescale[0]);
					}
				}
				hero.file = "";
				hero.subfile = "";
				x = true;
				int alt = this.main.config.getValue(""+hero.key[0]+cur,cur);
				String find1;
				String find2;
				String find3;
				String mod1;
				String mod2;
				String mod3;
				if(cur == 0) {
					find1 = "<find><![CDATA[";
					find2 = "]]></find><replace><![CDATA[";
					find3 = "]]></replace><find position='start'/>";
					mod1 = "";
					mod2 = "";
					mod3 = "";
				}
				else {
					find1 = "";
					find2 = "";
					find3 = "";
					mod1 = "		<find><![CDATA[<modifier key='"+hero.key[cur]+"' mod]]></find><replace><![CDATA[<!-- Customization of Newerth was here! \n<modifier key='"+hero.key[cur]+"']]></replace>\n" +
							"		<find><![CDATA[</modifier>]]></find><replace><![CDATA[</modifier>-->]]></replace>\n" +
							"		<insert position='after'><![CDATA[\n	<modifier key='"+hero.key[cur]+"' modpriority='1'\n";
					if(this.main.config.getValue(hero.key[0]+"alt",true) == true) {
						mod1 += "		altavatar='true'\n";
					}
							
					mod2 = ">\n	</modifier>";
					mod3 =  "]]></insert>\n";
				}
				mod +=  "\n" +
						"	<editfile name='heroes/"+hero.key[0]+"/"+hero.alt[0]+".entity'>\n" +
						mod1;
						//general
				mod +=  "		"+find1+getStr("icon",hero.icon[0],cur)+find2+"icon='"+hero.icon[alt]+"'"+find3+"\n" +
						"		"+find1+getStr("portrait",hero.portrait[0],cur)+find2+"portrait='"+hero.portrait[alt]+"'"+find3+"\n" +
						"		"+find1+getStr("selectedsound",hero.selectedsound[0],cur)+find2+"selectedsound='"+hero.selectedsound[alt]+"'"+find3+"\n" +
						"		"+find1+getStr("selectedflavorsound",hero.selectedflavorsound[0],cur)+find2+"selectedflavorsound='"+hero.selectedflavorsound[alt]+"'"+find3+"\n" +
						"		"+find1+getStr("confirmmovesound",hero.confirmmovesound[0],cur)+find2+"confirmmovesound='"+hero.confirmmovesound[alt]+"'"+find3+"\n" +
						"		"+find1+getStr("confirmattacksound",hero.confirmattacksound[0],cur)+find2+"confirmattacksound='"+hero.confirmattacksound[alt]+"'"+find3+"\n" +
						"		"+find1+getStr("nomanasound",hero.nomanasound[0],cur)+find2+"nomanasound='"+hero.nomanasound[alt]+"'"+find3+"\n" +
						"		"+find1+getStr("cooldownsound",hero.cooldownsound[0],cur)+find2+"cooldownsound='"+hero.cooldownsound[alt]+"'"+find3+"\n" +
						"		"+find1+getStr("tauntedsound",hero.tauntedsound[0],cur)+find2+"tauntedsound='"+hero.tauntedsound[alt]+"'"+find3+"\n" +
						"		"+find1+getStr("tauntkillsound",hero.tauntkillsound[0],cur)+find2+"tauntkillsound='"+hero.tauntkillsound[alt]+"'"+find3+"\n";
				if(hero.editmodel == false) {
				mod +=  "		"+find1+getStr("model",hero.model[0],cur)+find2+"model='"+hero.model[alt]+"'"+find3+"\n" +
				"		"+find1+getStr("previewmodel",hero.previewmodel[0],cur)+find2+"previewmodel='"+hero.previewmodel[alt]+"'"+find3+"\n";
				}
				else {
				mod +=  "		"+find1+getStr("model",hero.model[0],cur)+find2+"model='"+hero.model[cur]+"'"+find3+"\n" +
						"		"+find1+getStr("previewmodel",hero.previewmodel[0],cur)+find2+"previewmodel='"+hero.previewmodel[alt]+"'"+find3+"\n";
					sub += getModel(hero,cur,alt);
				}
						//scales
				mod +=  "		"+find1+getStr("preglobalscale",hero.preglobalscale[0],cur)+find2+"preglobalscale='"+hero.preglobalscale[alt]+"'"+find3+"\n" +
						"		"+find1+getStr("passiveeffect",hero.passiveeffect[0],cur)+find2+"passiveeffect='"+hero.passiveeffect[alt]+"'"+find3+"\n" +
						"		"+find1+getStr("modelscale",hero.modelscale[0],cur)+find2+"modelscale='"+hero.modelscale[alt]+"'"+find3+"\n" +
						"		"+find1+getStr("effectscale",hero.effectscale[0],cur)+find2+"effectscale='"+hero.effectscale[alt]+"'"+find3+"\n";
				if(cur == 0) {
					if(hero.infoheight[cur] == null) {
						mod += "		<find><![CDATA[skin='']]></find><replace><![CDATA[skin=''\n	infoheight='"+hero.infoheight[0]+"']]></replace><find position='start'/>\n";
					}
					else {
						mod += "		"+find1+getStr("infoheight",hero.infoheight[0],cur)+find2+"infoheight='"+hero.infoheight[alt]+"'"+find3+"\n";
					}
					if(hero.tiltfactor[cur] == null) {
						mod += "		<find><![CDATA[skin='']]></find><replace><![CDATA[skin=''\n	tiltfactor='"+hero.tiltfactor[0]+"']]></replace><find position='start'/>\n";
					}
					else {
						mod += "		"+find1+getStr("tiltfactor",hero.tiltfactor[0],cur)+find2+"tiltfactor='"+hero.tiltfactor[alt]+"'"+find3+"\n";
					}
					if(hero.tiltspeed[cur] == null) {
						mod += "		<find><![CDATA[skin='']]></find><replace><![CDATA[skin=''\n	tiltspeed='"+hero.tiltspeed[0]+"']]></replace><find position='start'/>\n";
					}
					else {
						mod += "		"+find1+getStr("tiltspeed",hero.tiltspeed[0],cur)+find2+"tiltspeed='"+hero.tiltspeed[alt]+"'"+find3+"\n";
					}
				}
				else {
					mod +=  "		"+find1+getStr("infoheight",hero.infoheight[0],cur)+find2+"infoheight='"+hero.infoheight[alt]+"'"+find3+"\n" +
					"		"+find1+getStr("tiltfactor",hero.tiltfactor[0],cur)+find2+"tiltfactor='"+hero.tiltfactor[alt]+"'"+find3+"\n" +
							"		"+find1+getStr("tiltspeed",hero.tiltspeed[0],cur)+find2+"tiltspeed='"+hero.tiltspeed[alt]+"'"+find3+"\n";
				}
						//attack
				mod += "		"+find1+getStr("attackoffset",hero.attackoffset[0],cur)+find2+"attackoffset='"+hero.attackoffset[alt]+"'"+find3+"\n";
				if(hero.attackprojectile[cur] != null) {
					if(cur != 0) {
						mod += "		"+find1+getStr("attackprojectile",hero.attackprojectile[0],cur)+find2+"attackprojectile='"+hero.attackprojectile[alt]+"'"+find3+"\n";
					}
				}
				mod +=  "		"+find1+getStr("attackstarteffect",hero.attackstarteffect[0],cur)+find2+"attackstarteffect='"+hero.attackstarteffect[alt]+"'"+find3+"\n";
				if(hero.attackactioneffect[cur] != null) {
					mod += "		"+find1+getStr("attackactioneffect",hero.attackactioneffect[0],cur)+find2+"attackactioneffect='"+hero.attackactioneffect[alt]+"'"+find3+"\n";
				}
				if(hero.attackimpacteffect[cur] != null) {
					mod += "		"+find1+getStr("attackimpacteffect",hero.attackimpacteffect[0],cur)+find2+"attackimpacteffect='"+hero.attackimpacteffect[alt]+"'"+find3+"\n";
				}
						//Store & preview
				mod +=  "		"+find1+getStr("previewpos",hero.previewpos[0],cur)+find2+"previewpos='"+hero.previewpos[alt]+"'"+find3+"\n" +
						//"		"+find1+getStr("previewangles","0 0 0",cur)+find2+"previewpos='"+"0 0 0"+"'"+find3+"\n" +
						"		"+find1+getStr("previewscale",hero.previewscale[0],cur)+find2+"previewscale='"+hero.previewscale[alt]+"'"+find3+"\n" +
						"		"+find1+getStr("storepos",hero.storepos[0],cur)+find2+"storepos='"+hero.storepos[alt]+"'"+find3+"\n" +
						//"		"+find1+getStr("storeangles","0 0 0",cur)+find2+"previewpos='"+"0 0 0"+"'"+find3+"\n" +
						"		"+find1+getStr("storescale",hero.storescale[0],cur)+find2+"storescale='"+hero.storescale[alt]+"'"+find3+"\n" +
						mod2 +
						mod3 +
						"	</editfile>\n";
						//spells
				for(int i = 0; i <= hero.spellnum;i += 1) {
					boolean z = false;
					Spell spell = hero.spell[i];
					if(spell.filepath[cur] != null & spell.filepath[alt] != null) {
						if(spell.body[cur] != null) {
							mod += "	<editfile name='heroes/"+hero.key[0]+"/"+spell.filepath[0]+"'>\n";
							if(cur != 0) {
								mod += "		<find><![CDATA[<modifier key='"+hero.key[cur]+"' mod]]></find>\n";
							}
							mod +=  "		<find><![CDATA["+spell.body[cur]+"]]></find><replace><![CDATA["+spell.body[alt]+"]]></replace>\n" +
									"	</editfile>\n";
						}
						else {
							String[] path = new String[10];
							path = spell.filepath[alt].split("/");
							String dir0 = "/heroes/"+hero.key[0]+"/";
							String dir1 = "/heroes/"+hero.key[0]+"/";
							String dir2 = "/heroes/"+hero.key[0]+"/";
							String dir3 = "/heroes/"+hero.key[0]+"/";
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
							mod +=  "	<copyfile name='heroes/"+hero.key[0]+"/"+spell.filepath[cur]+"' source='heroes/"+hero.key[0]+"/"+spell.filepath[alt]+"' overwrite='yes' fromresource='true' />\n" +
									"	<editfile name='heroes/"+hero.key[0]+"/"+spell.filepath[cur]+"'>\n" +
									"		<findall><![CDATA[sample=']]></findall><replace><![CDATA[sample='"+dir0+"]]></replace><find position='start'/>\n" +
									"		<findall><![CDATA[material=']]></findall><replace><![CDATA[material='"+dir0+"]]></replace><find position='start'/>\n" +
									"		<findall><![CDATA[model=']]></findall><replace><![CDATA[model='"+dir0+"]]></replace><find position='start'/>\n" +
									"		<findall><![CDATA[icon=']]></findall><replace><![CDATA[icon='"+dir0+"]]></replace><find position='start'/>\n" +
									"		<findall><![CDATA[portrait=']]></findall><replace><![CDATA[portrait='"+dir0+"]]></replace><find position='start'/>\n" +
									"		<findall><![CDATA[effect=']]></findall><replace><![CDATA[effect='"+dir0+"]]></replace><find position='start'/>\n" +
									"		<findall><![CDATA[PlaySoundLinear ]]></findall><replace><![CDATA[PlaySoundLinear "+dir0+"]]></replace><find position='start'/>\n" +
									"		<findall><![CDATA[StartEffect ]]></findall><replace><![CDATA[StartEffect "+dir0+"]]></replace><find position='start'/>\n" +
									//models
									"		<findall><![CDATA[file=']]></findall><replace><![CDATA[file='"+dir0+"]]></replace><find position='start'/>\n" +
									"		<findall><![CDATA[low=']]></findall><replace><![CDATA[low='"+dir0+"]]></replace><find position='start'/>\n" +
									"		<findall><![CDATA[med=']]></findall><replace><![CDATA[med='"+dir0+"]]></replace><find position='start'/>\n" +
									"		<findall><![CDATA[high=']]></findall><replace><![CDATA[high='"+dir0+"]]></replace><find position='start'/>\n" +
									"		<findall><![CDATA[clip=']]></findall><replace><![CDATA[clip='"+dir0+"]]></replace><find position='start'/>\n" +
									//remove failure's
									"		<findall><![CDATA[\""+dir0+"\"]]></findall><replace><![CDATA[\"\"]]></replace><find position='start'/>\n" +
									"		<findall><![CDATA["+dir0+"/heroes]]></findall><replace><![CDATA[/heroes]]></replace><find position='start'/>\n" +
									"		<findall><![CDATA["+dir0+"/shared]]></findall><replace><![CDATA[/shared]]></replace><find position='start'/>\n" +
									"		<findall><![CDATA["+dir0+"/ui]]></findall><replace><![CDATA[/ui]]></replace><find position='start'/>\n" +
									//"../"
									"		<findall><![CDATA["+dir0+"../../../]]></findall><replace><![CDATA["+dir3+"]]></replace><find position='start'/>\n" +
									"		<findall><![CDATA["+dir0+"../../]]></findall><replace><![CDATA["+dir2+"]]></replace><find position='start'/>\n" +
									"		<findall><![CDATA["+dir0+"../]]></findall><replace><![CDATA["+dir1+"]]></replace><find position='start'/>\n";
							if(spell.projectile == true) {
								if(z == false) {
									for(int ii = 0;ii <= hero.avatar;ii++) {
										if(spell.filepath[ii] != null) {
											String spellfile = null;
											ZipEntry ze = zf.getEntry("heroes"+"/"+hero.key[0]+"/"+spell.filepath[ii]);
											long size = ze.getSize();
											if (size > 0) {
												BufferedReader br;
												try {
													br = new BufferedReader(
													new InputStreamReader(zf.getInputStream(ze)));
												String linebreak;
												while ((linebreak = br.readLine()) != null) {
													spellfile += linebreak+"\n";
												}
												br.close();
												} catch (IOException e) {
													e.printStackTrace();
												}
											}
											int pos = spellfile.indexOf("name=\"")+"name=\"".length();
											spell.name[ii] = spellfile.substring(pos,spellfile.indexOf("\"",pos));
										}
									}
									z = true;
								}
								mod += "		<find><![CDATA["+spell.name[alt]+"]]></find><replace><![CDATA["+spell.name[cur]+"]]></replace><find position='start'/>\n";
							}
							mod +=  "	</editfile>\n";
						}
					}
				}
			}
			mod += sub;
			string += mod;
		}
		return string;
	}
	public String getValue(String value,String father,String backup) {
		if(father.contains(value) == false) {
			return backup;
		}
		else {
			int pos = father.indexOf(value)+value.length();
			return father.substring(pos,father.indexOf("\"",pos));
		}
	}
	public String getStr(String value,String string,int cur) {
		if(cur != 0) {
			return "";
		}
		else {
			return value+"='"+string+"'";
		}
	}
	public String getModel(Hero hero,int cur,int alt) {
		String[] s = new String[2];
		s[0] = "";
		s[1] = "";
		//Reading the hero entity;
		ZipEntry ze = zf.getEntry("heroes"+"/"+hero.key[0]+"/"+hero.model[cur]);
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
		ze = zf.getEntry("heroes"+"/"+hero.key[0]+"/"+hero.model[alt]);
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
		path = hero.model[alt].split("/");
		String dir0 = "/heroes/"+hero.key[0]+"/";
		String dir1 = "/heroes/"+hero.key[0]+"/";
		String dir2 = "/heroes/"+hero.key[0]+"/";
		String dir3 = "/heroes/"+hero.key[0]+"/";
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
		s[1] = s[1].replaceAll("sample=\"","sample=\""+dir0).replaceAll("material=\"","material=\""+dir0).replaceAll("model=\"","model=\""+dir0).replaceAll("icon=\"","icon=\""+dir0).replaceAll("portrait=\"","portrait=\""+dir0).replaceAll("effect=\"","effect=\""+dir0).replaceAll("PlaySoundLinear ","PlaySoundLinear "+dir0).replaceAll("StartEffect ","StartEffect "+dir0).replaceAll("file=\"","file=\""+dir0).replaceAll("low=\"","low=\""+dir0).replaceAll("med=\"","med=\""+dir0).replaceAll("high=\"","high=\""+dir0).replaceAll("clip=\"","clip=\""+dir0).replaceAll(dir0+"\""+dir0+"\"","\"\"").replaceAll(dir0+"/heroes","/heroes").replaceAll(dir0+"/shared","/shared").replaceAll(dir0+"/ui","/ui").replaceAll(dir0+"../../../",dir3).replaceAll(dir0+"../../",dir2).replaceAll(dir0+"../",dir1);
		for(int i = 0;i <= 1;i++) {
			int length = s[i].split("<anim").length-2;
			int position = 0;
			for(int ii = 0;ii <= length;ii++) {
				position = s[i].indexOf("<anim",position+"<anim".length());
				int index = s[i].indexOf("/>",position);
				if(s[i].indexOf(">",position) > s[i].indexOf("/>",position)) {
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
		String re = 	"	<editfile name='heroes"+"/"+hero.key[0]+"/"+hero.model[cur]+"'>\n" +
						"		<findall><![CDATA[<!--]]></findall><delete/><find position='start'/><findall><![CDATA[-->]]></findall><delete/><find position='start'/>\n" +
						"		<find><![CDATA[<model]]></find><replace><![CDATA["+s[0]+"\n\n<!--<model]]></replace>\n" +
						"		<find><![CDATA[</model>]]></find><replace><![CDATA[</model>-->]]></replace>\n" +
						"	</editfile>\n";
		return re.replace("<?xml version=\"1.0\" encoding=\"UTF-8\"?>","").replaceAll("file=\"","file=\""+dir0).replaceAll("low=\"","low=\""+dir0).replaceAll("med=\"","med=\""+dir0).replaceAll("high=\"","high=\""+dir0);
	}
	//Announcer
	public String getAnnouncer(int cur) {
		String string = "";
		String mod = "";
		Announcer[] anno = this.main.config.announcer;
		int alt = this.main.config.getValue(anno[cur].voice,cur);
		if(anno[cur].soundmove == false) {
			String arcade2 = null;
			if(anno[alt].arcade.equals("arcade_text")) {
				arcade2 = "normal";
			}
			else {
				arcade2 = anno[alt].arcade;
			}
			mod += "	<editfile name='base.upgrades'>\n" +
					"		<find><![CDATA[<announcervoice name='"+anno[cur].key+"' voiceset='"+anno[cur].voice+"' arcadetext='"+anno[cur].arcade+"' />]]></find><replace><![CDATA[<announcervoice name='"+anno[cur].key+"' voiceset='"+anno[alt].voice+"' arcadetext='"+arcade2+"' />]]></replace><find position='start'/>\n" +
					"	</editfile>\n";
		}
		else {
			for(int i = 0;i <= 37;i++) {
				string += "	<copyfile name='"+anno[cur].sound[i]+"' source='"+anno[alt].sound[i]+"' overwrite='yes' fromresource='true' />\n";
			}
			for(int i = 0;i <= 19;i++) {
				string += "	<copyfile name='"+anno[cur].text[i]+".mdf' source='"+anno[alt].text[i]+".mdf' overwrite='yes' fromresource='true' />\n";
				string += "	<copyfile name='"+anno[cur].text[i]+".model' source='"+anno[alt].text[i]+".model' overwrite='yes' fromresource='true' />\n";
				string += "	<copyfile name='"+anno[cur].text[i]+".clip' source='"+anno[alt].text[i]+".clip' overwrite='yes' fromresource='true' />\n";
			}
			string += 	"	<copyfile name='ui/common/models/"+anno[cur].arcade+"/material.material' source='ui/common/models/"+anno[alt].arcade+"/material.material' overwrite='yes' fromresource='true' />\n" +
						"	<editfile name='ui/common/models/"+anno[cur].arcade+"/material.material'><findall><![CDATA[color.tga]]></findall><replace><![CDATA[/ui/common/models/"+anno[alt].arcade+"/color.tga]]></replace></editfile>" +
						"	<copyfile name='ui/common/models/"+anno[cur].arcade+"/material2.material' source='ui/common/models/"+anno[alt].arcade+"/material.material' overwrite='yes' fromresource='true' />\n" +
						"	<editfile name='ui/common/models/"+anno[cur].arcade+"/material2.material'><findall><![CDATA[color.tga]]></findall><replace><![CDATA[/ui/common/models/"+anno[alt].arcade+"/color.tga]]></replace></editfile>" +
						"	<copyfile name='ui/common/models/"+anno[cur].arcade+"/arrow2.clip' source='ui/common/models/"+anno[cur].arcade+"/arrow2.clip' overwrite='yes' fromresource='true' />\n";
		}
		string += mod;
		return string;
	}
}