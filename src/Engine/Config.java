package Engine;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URLConnection;
import java.util.Vector;
import java.util.zip.ZipFile;

import Lindholm.LLMethod;
import Lindholm.data.LLInputStream;
import Lindholm.data.LLOutputStream;
import Lindholm.com.LLFile;
import Lindholm.com.LLProperty;
import Lindholm.com.LLRegistry;
import Lindholm.com.LLUrl;
import Lindholm.ui.LLGui;

import Engine.Attributes.Announcer;
import Engine.Attributes.Hero;
import Engine.Attributes.Taunt;

import Main.Main;

public class Config {
	//Variables;
	private	Main		main;
	public 	LLProperty	property;
	public	ZipFile		resources;
	//Passive.effect;
	public String passive = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
							"<effect name=\"\" deferred=\"true\" useentityeffectscale=\"true\">\n" +
							"	<definitions>\n" +
							"	</definitions>\n" +
							"	<thread>\n" +
							"	</thread>\n" +
							"</effect>";
	public String material ="<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
							"<material>\n" +
							"	<parameters />\n" +
							"	<phase\n" +
							"		name=\"color\"\n" +
							"		vs=\"effect\"\n" +
							"		ps=\"effect\"\n" +
							"		srcblend=\"BLEND_SRC_ALPHA\"\n" +
							"		dstblend=\"BLEND_ONE_MINUS_SRC_ALPHA\"\n" +
							"		cull=\"CULL_NONE\"\n" +
							"		translucent=\"true\"\n" +
							"		depthwrite=\"false\"\n" +
							"		depthbias=\"1\"\n" +
							"		depthslopebias=\"true\"\n" +
							"		alphawrite=\"false\"\n" +
							"		layer=\"1\"\n" +
							"	>\n" +
							"		<sampler\n" +
							"			name=\"diffuse\"\n" +
							"			texture=\"cic.tga\"\n" +
							"			repeat_u=\"false\"\n" +
							"			repeat_v=\"false\"\n" +
							"			mipmaps=\"true\"\n" +
							"			fullquality=\"true\"\n" +
							"			nocompress=\"false\"\n" +
							"			filtering=\"true\"\n" +
							"		/>\n" +
							"	</phase>\n" +
							"</material>";
	//Attributes;
	public Vector<Hero> hero = new Vector<Hero>();
	public	int			h = -1;
	public Vector<Announcer> announcer = new Vector<Announcer>();
	public	int			a = -1;
	public Vector<Taunt> taunt = new Vector<Taunt>();
	public	int			t = -1;
	//Constructors;
	public Config(Main main) {
		//Saving the main parameter;
		this.main = main;
		//Loading Properties;
		property = new LLProperty(LLFile.getFile(main.EngDir+"config.ini"));
		//Loading HoN resourcess;
		try {
			property.setProperty("hondir",getHonDir(property));
			resources = new ZipFile(property.getValue("hondir","")+File.separator+"game"+File.separator+"resources0.s2z");
		} catch (IOException e) {
			main.report(3,e);
		}
		//Checking for Attribute updates;
		int v = 0;
		try {
			v = new LLInputStream(LLUrl.connect(main.EngUrl+"minor.sht",10000).getInputStream()).readShort();
		} catch (MalformedURLException e) {
		} catch (IOException e) {
			main.report(4,e);
		}
		if(v > property.getValue("minor",0) | new File(main.EngDir+"resources.dat").exists() == false) {
			LLGui gui = new LLGui();
			try {
				URLConnection uc = LLUrl.connect(main.EngUrl+"resources.dat",10000);
				URLConnection uc2 = LLUrl.connect(main.EngUrl+"textures.dat",10000);
				gui.loadProgress(uc.getContentLength()+uc2.getContentLength());
				LLInputStream in;
				LLOutputStream out;
				in = new LLInputStream(uc.getInputStream());
				out = new LLOutputStream(new FileOutputStream(LLFile.getNewFile(main.EngDir+"resources.dat")));
				byte[] buffer = new byte[1024];
				int length;
				while((length = in.read(buffer)) >= 0) {
					out.write(buffer,0,length);
					gui.setProgressValue(gui.getProgressValue()+length);
				}
				in.close();
				out.close();
				in = new LLInputStream(uc2.getInputStream());
				out = new LLOutputStream(new FileOutputStream(LLFile.getNewFile(main.EngDir+"textures.dat")));
				while((length = in.read(buffer)) >= 0) {
					out.write(buffer,0,length);
					gui.setProgressValue(gui.getProgressValue()+length);
				}
				in.close();
				out.close();
				gui.remove();
			} catch (MalformedURLException e) {
			} catch (IOException e) {
				main.report(5,e);
			}
			property.setProperty("minor",v+"");
		}
		//Loading resources;
		LLInputStream in;
		try {
			in = new LLInputStream(new FileInputStream(main.EngDir+"resources.dat"));
			//Binding the methods
			Method[] cm = new Method[13];
			for(int i = 0;i <= 12;i++) {
				cm[i] = LLMethod.getMethod(this,"cm"+i,LLInputStream.class);
			}
			/***
			 * 0	= hero						(name			,key		);
			 * 1	= alt						(name			,key		,alt		);
			 * 2	= spell						(filepath		,body		,property	);
			 * 3	= attribute-spell-filepath	(spellnum		,filepathnum,filepath	);
			 * 4	= attribute-spell-body		(spellnum		,bodynum	,body		);
			 * 5	= attribute-editmodel		(editmodel		);
			 * 6	= attribute-alt				(alt			);
			 * 7	= announcer					(name			,key		,voice		,	text);
			 * 8	= attribute-sound			(soundnum		,soundpath);
			 * 9	= attribute-soundmove		(soundmove		);
			 * 10	= taunt						(mame			,key		);
			 * 11	= attribute-tauntfile		(filepath		);
			 * 12	= Indicator					(key			,filepath	);
			 * 13	= PreFile					(indicnum		,data		);
			 * 14	= PreRange					(indicnum		,range		);
			 */
			while(in.available() > 0) {
				cm[in.readShort()].invoke(this,in);
			}
		} catch (IllegalAccessException e) {
			//This Exception shouldn't get caught;
		} catch (IllegalArgumentException e) {
			//This Exception shouldn't get caught;
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			//This Exception shouldn't get caught
			e.printStackTrace();
		} catch (IOException e) {
			main.report(6,e);
		}
		//Accursed1
		hero.get(1).addIndicator(1,"ability_01/ability.entity");
		hero.get(1).addIndicator(2,"ability_02/state.entity");
		hero.get(1).indic[1].range = "700";
		hero.get(1).addIndicator(3,"ability_02/ability.entity");
		//AccursedATK
		String[] teeee = hero.get(1).alt[0].split("/");
		hero.get(1).addIndicator(8,teeee[teeee.length-1]+".entity");
		//Magmus1
		hero.get(48).addIndicator(1,"ability_01/ability.entity");
		String[] teeeee = hero.get(2).alt[0].split("/");
		hero.get(2).addIndicator(8,teeeee[teeeee.length-1]+".entity");
		property.save();
	}
	//Config methods()
	public void cm0(LLInputStream in) throws IOException {
		String var1 = in.readString(in.readShort());
		String var2 = in.readString(in.readShort());
		hero.add(new Hero(var1,var2));
		h++;
	}
	public void cm1(LLInputStream in) throws IOException {
		String var1 = in.readString(in.readShort());
		String var2 = in.readString(in.readShort());
		String var3 = in.readString(in.readShort());
		hero.get(h).addAlt(var1,var2,var3);
	}
	public void cm2(LLInputStream in) throws IOException {
		String var1 = in.readString(in.readShort());
		String var2 = in.readString(in.readShort());
		hero.get(h).addSpell(var1,var2,in.readBoolean());
	}
	public void cm3(LLInputStream in) throws IOException {
		int var1 = in.readShort();
		int var2 = in.readShort();
		hero.get(h).spell[var1].filepath[var2] = in.readString(in.readShort());
	}
	public void cm4(LLInputStream in) throws IOException {
		int var1 = in.readShort();
		int var2 = in.readShort();
		hero.get(h).spell[var1].body[var2] = in.readString(in.readShort());
	}
	public void cm5(LLInputStream in) throws IOException {
		hero.get(h).editmodel = in.readBoolean();
	}
	public void cm6(LLInputStream in) throws IOException {
		hero.get(h).alt[0] = in.readString(in.readShort());
	}
	public void cm7(LLInputStream in) throws IOException {
		String var1 = in.readString(in.readShort());
		String var2 = in.readString(in.readShort());
		String var3 = in.readString(in.readShort());
		String var4 = in.readString(in.readShort());
		announcer.add(new Announcer(var1,var2,var3,var4));
		a++;
	}
	public void cm8(LLInputStream in) throws IOException {
		int var1 = in.readShort();
		announcer.get(a).sound[var1] = in.readString(in.readShort());
	}
	public void cm9(LLInputStream in) throws IOException {
		announcer.get(a).movesound = in.readBoolean();
	}
	public void cm10(LLInputStream in) throws IOException {
		String var1 = in.readString(in.readShort());
		String var2 = in.readString(in.readShort());
		taunt.add(new Taunt(var1,var2));
		t++;
	}
	public void cm11(LLInputStream in) throws IOException {
		taunt.get(t).file = in.readString(in.readShort());
	}
	public void cm12(LLInputStream in) throws IOException {
		
	}
	//Gamepath method;
	public String getHonDir(LLProperty prop) {
		if(prop.getValue("hondir","").isEmpty() == false) {
			return prop.getValue("hondir","");
		}
		//Else
		if(System.getProperty("os.name").toLowerCase().contains("windows")) {
			String[] honFolder = {"C:\\Program Files\\Heroes of Newerth\\","C:\\Program Files (x86)\\Heroes of Newerth\\"};
			for (int i = 0; i < honFolder.length; i++) {
				File f = new File(honFolder[i]);
				if (f.exists()) {
					return f.getAbsolutePath();
				}
			}
			// Get the folder from uninstall info in windows registry saved by HoN
	        String registryData = LLRegistry.readRegistry("SOFTWARE\\Microsoft\\Windows\\CurrentVersion\\Uninstall\\hon", "InstallLocation");
	        if (registryData != null && !registryData.isEmpty()) {
	            return registryData;
	        }
	        registryData = LLRegistry.readRegistry("SOFTWARE\\Notausgang\\HoN_ModMan", "hondir");
	        if (registryData != null && !registryData.isEmpty()) {
	            return registryData;
	        }
	        registryData = LLRegistry.readRegistry("SOFTWARE\\Wow6432Node\\Microsoft\\Windows\\CurrentVersion\\Uninstall\\hon", "InstallLocation");
	        if (registryData != null && !registryData.isEmpty()) {
	            return registryData;
	        }
	        // From Notausgang's ModManager
	        registryData = LLRegistry.readRegistry("SOFTWARE\\Notausgang\\HoN_ModMan", "hondir");
	        if (registryData != null && !registryData.isEmpty()) {
	            return registryData;
	        }
		}
		if (System.getProperty("os.name").toLowerCase().contains("linux")) {
            // Try to find HoN in its usual location:
            String[] honFolder = {"~/Heroes of Newerth/", "~/HoN/"};
            for (int i = 0; i < honFolder.length; i++) {
                File f = new File(honFolder[i]);
                if (f.exists()) {
                    return f.getAbsolutePath();
                }
            }
        }
		if (System.getProperty("os.name").toLowerCase().contains("mac")) {
            File a = new File(System.getProperty("user.home") + File.separator + "Applications/Heroes of Newerth.app");
            File b = new File(System.getProperty("user.home") + "/Applications/Heroes of Newerth.app");
            File c = new File("/Applications/Heroes of Newerth.app");
            File d = new File("~/Applications/Heroes of Newerth.app");
            if (a.exists()) {
                return a.getAbsolutePath();
            } else if (b.exists()) {
                return b.getAbsolutePath();
            } else if (c.exists()) {
                return c.getAbsolutePath();
            } else if (d.exists()) {
                return d.getAbsolutePath();
            }
        }
        return "";
	}
}
