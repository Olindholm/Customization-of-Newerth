package Engine.Attributes;

import java.awt.Image;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import Lindholm.LLInteger;
import Lindholm.LLString;
import Lindholm.com.LLProperty;
import Lindholm.com.LLZipFile;

public class Hero {
	//Variables;
	//CoN info;
	public Image[] image = new Image[10];
	public int avatar = 0;
	public int spellnum = -1;
	public Spell[] spell = new Spell[20];
	public int indicnum = -1;
	public Indicator[] indic = new Indicator[20];

	//General;
	public String[] name = new String[10];
	public String[] key = new String[10];
	public String[] alt = new String[10];
	
	//Modeledit;
	public boolean editmodel = false;
	
	public Hero(String name,String key) {
		this.name[0] = name;
		this.key[0] = key;
		this.alt[0] = "heroes/"+key+"/hero";
	}
	public void addAlt(String name, String key, String alt) {
		this.avatar += 1;
		this.name[this.avatar] = name;
		this.key[this.avatar] = key;
		this.alt[this.avatar] = alt;
		
		for(int i = 0;i <= spellnum;i++) {
			spell[i].filepath[avatar] = alt+"/"+spell[i].filepath[0];
			spell[i].body[avatar] = spell[i].body[0];
		}
	}
	public void addSpell(String filepath, String body,boolean projectile) {
		spellnum += 1;
		spell[spellnum] = new Spell(filepath, body,projectile);
	}
	public void addIndicator(int key,String filepath) {
		indicnum += 1;
		indic[indicnum] = new Indicator(key,filepath);
	}
	//____________________________________________________________\\
	public String getCourierXAML(LLProperty yp,ZipFile zf) {
		String str = "";
		String alt = this.alt[0];
		String[] altalt = this.alt[0].split(":");
		for(int i = 0;i <= altalt.length-1;i++) {
			this.alt[0] = altalt[i];
			str += getXAML(yp,zf);
		}
		this.alt[0] = alt;
		return str;
	}
	public String getXAML(LLProperty yp,ZipFile zf) {
		//Creating a return string due we need to return lots of data;
		String re = "";
		//Creating avatar(0) variables;
		//Models;
		String icon = null;
		String portrait = null;
		String model = null;
		String previewmodel = null;
		String storemodel = null;
		String modelscale = null;
		String effectscale = null;
		//Scales;
		String preglobalscale = null;
		String passiveeffect = null;
		String infoheight = null;
		String tiltfactor = null;
		String tiltspeed = null;
		//Sound;
		String selectedsound = null;
		String selectedflavorsound = null;
		String confirmmovesound = null;
		String confirmattacksound = null;
		String nomanasound = null;
		String cooldownsound = null;
		String tauntedsound = null;
		String tauntkillsound = null;
		//Attack;
		String attackoffset = null;
		String attackprojectile = null;
		String attackstarteffect = null;
		String attackactioneffect = null;
		String attackimpacteffect = null;
		//Preview;
		String previewpos = null;
		String previewscale = null;
		String storepos = null;
		String storescale = null;
		//Checking if any avatar have been modified;
		for(int i = 0;i <= avatar+1;i++) {
			//Checking if it's still looping(It would mean no avatar was modified);
			if(i == avatar+1) {
				//Then quit and return empty string;
				return re;
			}
			//Checking if avatar have been modifed;
			else if(yp.getValue(key[0]+i,i) != i) {
				break;
			}
		}
		//Collecting the entity data;
		String data = LLZipFile.getData(alt[0]+".entity",zf);
		//Generating the code;
		for(int i = 0;i <= avatar;i++) {
			//Saving the become avatar number;
			int ii = yp.getValue(key[0]+i,i);
			//Creating the find string for the entity data collection;
			String find;
			String find1;
			String find2;
			String find3;
			String mod1;
			String mod2;
			String mod3;
			String modelstr;
			String modelssttrr;
			String infoheightstr;
			String tiltfactorstr;
			String tiltspeedstr;
			String attackprojectilestr;
			String attackactioneffectstr;
			String attackimpacteffectstr;
			String storeposstr;
			String storescalestr;
			String storemodelstr;
			String soundstr;
			String attackoffsetstr;
			String attackstarteffectstr;
			if(ii == 0) {
				find = "<hero";
			}
			else {
				find = "<modifier key=\""+key[ii]+"\" mod";
			}
			//Collecting the avatar data;
			int pos = data.indexOf(find);
			if(pos == -1) {
				pos = data.indexOf("<pet");
			}
			String subdata = data.substring(pos,data.indexOf(">",pos)+">".length());
			if(i == 0) {
				find1 = "<find><![CDATA[";
				find2 = "]]></find><replace><![CDATA[";
				find3 = "]]></replace><find position='start'/>";
				mod1 = "";
				mod2 = "";
				mod3 = "";
				//Collecting basic data;
				//Models;
				pos = data.indexOf("<hero");
				if(pos == -1) {
					pos = data.indexOf("<pet");
				}
				String cdata = data.substring(pos,data.indexOf(">",pos)+">".length());
				icon = getValueData("icon=\"",cdata,icon);
				portrait = getValueData("portrait=\"",cdata,portrait);
				model = getValueData("model=\"",cdata,model);
				previewmodel = getValueData("previewmodel=\"",cdata,previewmodel);
				storemodel = getValueData("storemodel=\"",cdata,storemodel);
				modelscale = getValueData("modelscale=\"",cdata,modelscale);
				effectscale = getValueData("effectscale=\"",cdata,effectscale);
				//Scales;
				preglobalscale = getValueData("preglobalscale=\"",cdata,preglobalscale);
				passiveeffect = getValueData("passiveeffect=\"",cdata,passiveeffect);
				infoheight = getValueData("infoheight=\"",cdata,infoheight);
				tiltfactor = getValueData("tiltfactor=\"",cdata,tiltfactor);
				tiltspeed = getValueData("tiltspeed=\"",cdata,tiltspeed);
				//Sound;
				selectedsound = getValueData("selectedsound=\"",cdata,selectedsound);
				selectedflavorsound = getValueData("selectedflavorsound=\"",cdata,selectedflavorsound);
				confirmmovesound = getValueData("confirmmovesound=\"",cdata,confirmmovesound);
				confirmattacksound = getValueData("confirmattacksound=\"",cdata,confirmattacksound);
				nomanasound = getValueData("nomanasound=\"",cdata,nomanasound);
				cooldownsound = getValueData("cooldownsound=\"",cdata,cooldownsound);
				tauntedsound = getValueData("tauntedsound=\"",cdata,tauntedsound);
				tauntkillsound = getValueData("tauntkillsound=\"",cdata,tauntkillsound);
				//Attack;
				attackoffset = getValueData("attackoffset=\"",cdata,attackoffset);
				attackprojectile = getValueData("attackprojectile=\"",cdata,attackprojectile);
				attackstarteffect = getValueData("attackstarteffect=\"",cdata,attackstarteffect);
				attackactioneffect = getValueData("attackactioneffect=\"",cdata,attackactioneffect);
				attackimpacteffect = getValueData("attackimpacteffect=\"",cdata,attackimpacteffect);
				//Preview;
				previewpos = getValueData("previewpos=\"",cdata,previewpos);
				previewscale = getValueData("previewscale=\"",cdata,previewscale);
				storepos = getValueData("storepos=\"",cdata,storepos);
				storescale = getValueData("storescale=\"",cdata,storescale);
				//Checking if some data is null, then we need to make some changes in the generation;
				if(infoheight == null) {	infoheightstr = "		<find><![CDATA[skin='']]></find><replace><![CDATA[skin=''\n	infoheight='"+infoheight+"']]></replace><find position='start'/>\n";	}
				else {						infoheightstr = "		"+find1+getStr("infoheight",infoheight,i)+find2+"infoheight='"+getValueData("infoheight=\"",subdata,infoheight)+"'"+find3+"\n";	}
				if(tiltfactor == null) {	tiltfactorstr = "		<find><![CDATA[skin='']]></find><replace><![CDATA[skin=''\n	tiltfactor='"+tiltfactor+"']]></replace><find position='start'/>\n";	}
				else {						tiltfactorstr = "		"+find1+getStr("tiltfactor",tiltfactor,i)+find2+"tiltfactor='"+getValueData("tiltfactor=\"",subdata,tiltfactor)+"'"+find3+"\n";	}
				if(tiltspeed == null) {		tiltspeedstr = "		<find><![CDATA[skin='']]></find><replace><![CDATA[skin=''\n	tiltspeed='"+tiltspeed+"']]></replace><find position='start'/>\n";	}
				else {						tiltspeedstr = "		"+find1+getStr("tiltspeed",tiltspeed,i)+find2+"tiltspeed='"+getValueData("tiltspeed=\"",subdata,tiltspeed)+"'"+find3+"\n";	}
				//Attackcheck
				attackprojectilestr = "";
				if(attackactioneffect == null) {	attackactioneffectstr = "";	}
				else {								attackactioneffectstr = "		"+find1+getStr("attackactioneffect",attackactioneffect,i)+find2+"attackactioneffect='"+getValueData("attackactioneffect=\"",subdata,attackactioneffect)+"'"+find3+"\n";	}
				if(attackimpacteffect == null) {	attackimpacteffectstr = "";	}
				else {								attackimpacteffectstr = "		"+find1+getStr("attackimpacteffect",attackimpacteffect,i)+find2+"attackimpacteffect='"+getValueData("attackimpacteffect=\"",subdata,attackimpacteffect)+"'"+find3+"\n";	}
				if(attackoffset == null) {			attackoffsetstr = "";	}
				else {								attackoffsetstr = "		"+find1+getStr("attackoffset",attackoffset,i)+find2+"attackoffset='"+getValueData("attackoffset=\"",subdata,attackoffset)+"'"+find3+"\n";	}
				if(attackstarteffect == null) {		attackstarteffectstr = "";	}
				else {								attackstarteffectstr = "		"+find1+getStr("attackstarteffect",attackstarteffect,i)+		find2+"attackstarteffect='"+	getValueData("attackstarteffect=\"",subdata,attackstarteffect)+		"'"+find3+"\n";	}
				//Storecheck
				if(storemodel == null) {			storemodelstr = "";	}
				else {								storemodelstr = "		"+find1+getStr("storemodel",storemodel,i)+find2+"storemodel='"+getValueData("storemodel=\"",subdata,storemodel)+"'"+find3+"\n";	}
				if(storepos == null) {				storeposstr = "";	}
				else {								storeposstr = "		"+find1+getStr("storepos",storepos,i)+find2+"storepos='"+getValueData("storepos=\"",subdata,storepos)+"'"+find3+"\n";	}
				if(storescale == null) {			storescalestr = "";	}
				else {								storescalestr = "		"+find1+getStr("storescale",storescale,i)+find2+"storescale='"+getValueData("storescale=\"",subdata,storescale)+"'"+find3+"\n";	}
				if(selectedsound == null) {			soundstr = "";	}
				else {								soundstr =	"		"+find1+getStr("selectedsound",selectedsound,i)+				find2+"selectedsound='"+		getValueData("selectedsound=\"",subdata,selectedsound)+				"'"+find3+"\n" +
																"		"+find1+getStr("selectedflavorsound",selectedflavorsound,i)+	find2+"selectedflavorsound='"+	getValueData("selectedflavorsound=\"",subdata,selectedflavorsound)+	"'"+find3+"\n" +
																"		"+find1+getStr("confirmmovesound",confirmmovesound,i)+			find2+"confirmmovesound='"+		getValueData("confirmmovesound=\"",subdata,confirmmovesound)+		"'"+find3+"\n" +
																"		"+find1+getStr("confirmattacksound",confirmattacksound,i)+		find2+"confirmattacksound='"+	getValueData("confirmattacksound=\"",subdata,confirmattacksound)+	"'"+find3+"\n" +
																"		"+find1+getStr("nomanasound",nomanasound,i)+					find2+"nomanasound='"+			getValueData("nomanasound=\"",subdata,nomanasound)+					"'"+find3+"\n" +
																"		"+find1+getStr("cooldownsound",cooldownsound,i)+				find2+"cooldownsound='"+		getValueData("cooldownsound=\"",subdata,cooldownsound)+				"'"+find3+"\n" +
																"		"+find1+getStr("tauntedsound",tauntedsound,i)+					find2+"tauntedsound='"+			getValueData("tauntedsound=\"",subdata,tauntedsound)+				"'"+find3+"\n" +
																"		"+find1+getStr("tauntkillsound",tauntkillsound,i)+				find2+"tauntkillsound='"+		getValueData("tauntkillsound=\"",subdata,tauntkillsound)+			"'"+find3+"\n";
				}
			}
			else {
				find1 = "";
				find2 = "";
				find3 = "";
				mod1 =	"		<find><![CDATA[<modifier key='"+key[i]+"' mod]]></find><replace><![CDATA[<!--<modifier key='"+key[i]+"']]></replace>\n" +
						"		<find><![CDATA[</modifier>]]></find><replace><![CDATA[</modifier>-->]]></replace>\n" +
						"		<insert position='after'><![CDATA[\n	<modifier key='"+key[i]+"' modpriority='1'\n";
				//Checking if Alt Enable'd;
				if(yp.getValue(key[0],true) == true) {
					mod1 +=	"		altavatar='true'\n";
				}
				mod2 = ">\n	</modifier>";
				mod3 =  "]]></insert>\n";
				//Initializing some strings;
				infoheightstr = "		"+find1+getStr("infoheight",infoheight,i)+find2+"infoheight='"+getValueData("infoheight=\"",subdata,infoheight)+"'"+find3+"\n";
				tiltfactorstr = "		"+find1+getStr("tiltfactor",tiltfactor,i)+find2+"tiltfactor='"+getValueData("tiltfactor=\"",subdata,tiltfactor)+"'"+find3+"\n";
				tiltspeedstr = "		"+find1+getStr("tiltspeed",tiltspeed,i)+find2+"tiltspeed='"+getValueData("tiltspeed=\"",subdata,tiltspeed)+"'"+find3+"\n";
				if(i == 0) {
					find = "<hero";
				}
				else {
					find = "<modifier key=\""+key[i]+"\" mod";
				}
				pos = data.indexOf(find);
				if(pos == -1) {
					pos = data.indexOf("<pet");
				}
				attackprojectilestr = "		attackprojectile='"+getValueData("attackprojectile=\"",data.substring(data.indexOf(find),data.indexOf(">",pos)+">".length()),attackprojectile)+"'\n";
				attackactioneffectstr = "		"+find1+getStr("attackactioneffect",attackactioneffect,i)+find2+"attackactioneffect='"+getValueData("attackactioneffect=\"",subdata,attackactioneffect)+"'"+find3+"\n";
				attackimpacteffectstr = "		"+find1+getStr("attackimpacteffect",attackimpacteffect,i)+find2+"attackimpacteffect='"+getValueData("attackimpacteffect=\"",subdata,attackimpacteffect)+"'"+find3+"\n";
				attackoffsetstr = "		"+find1+getStr("attackoffset",attackoffset,i)+					find2+"attackoffset='"+			getValueData("attackoffset=\"",subdata,attackoffset)+				"'"+find3+"\n";
				attackstarteffectstr = "		"+find1+getStr("attackstarteffect",attackstarteffect,i)+		find2+"attackstarteffect='"+	getValueData("attackstarteffect=\"",subdata,attackstarteffect)+		"'"+find3+"\n";
				storemodelstr = "		"+find1+getStr("storemodel",storemodel,i)+find2+"storemodel='"+getValueData("storemodel=\"",subdata,storemodel)+"'"+find3+"\n";
				storeposstr = "		"+find1+getStr("storepos",storepos,i)+find2+"storepos='"+getValueData("storepos=\"",subdata,storepos)+"'"+find3+"\n";
				storescalestr = "		"+find1+getStr("storescale",storescale,i)+find2+"storescale='"+getValueData("storescale=\"",subdata,storescale)+"'"+find3+"\n";
				soundstr =	"		"+find1+getStr("selectedsound",selectedsound,i)+				find2+"selectedsound='"+		getValueData("selectedsound=\"",subdata,selectedsound)+				"'"+find3+"\n" +
							"		"+find1+getStr("selectedflavorsound",selectedflavorsound,i)+	find2+"selectedflavorsound='"+	getValueData("selectedflavorsound=\"",subdata,selectedflavorsound)+	"'"+find3+"\n" +
							"		"+find1+getStr("confirmmovesound",confirmmovesound,i)+			find2+"confirmmovesound='"+		getValueData("confirmmovesound=\"",subdata,confirmmovesound)+		"'"+find3+"\n" +
							"		"+find1+getStr("confirmattacksound",confirmattacksound,i)+		find2+"confirmattacksound='"+	getValueData("confirmattacksound=\"",subdata,confirmattacksound)+	"'"+find3+"\n" +
							"		"+find1+getStr("nomanasound",nomanasound,i)+					find2+"nomanasound='"+			getValueData("nomanasound=\"",subdata,nomanasound)+					"'"+find3+"\n" +
							"		"+find1+getStr("cooldownsound",cooldownsound,i)+				find2+"cooldownsound='"+		getValueData("cooldownsound=\"",subdata,cooldownsound)+				"'"+find3+"\n" +
							"		"+find1+getStr("tauntedsound",tauntedsound,i)+					find2+"tauntedsound='"+			getValueData("tauntedsound=\"",subdata,tauntedsound)+				"'"+find3+"\n" +
							"		"+find1+getStr("tauntkillsound",tauntkillsound,i)+				find2+"tauntkillsound='"+		getValueData("tauntkillsound=\"",subdata,tauntkillsound)+			"'"+find3+"\n";
			}
			//Checking if req to edit model;
			if(editmodel == false) {
				modelstr = getValueData("model=\"",subdata,model);
				modelssttrr = "";
			}
			else {
				if(i == 0) {
					find = "<hero";
				}
				else {
					find = "<modifier key=\""+key[i]+"\" mod";
				}
				pos = data.indexOf(find);
				if(pos == -1) {
					pos = data.indexOf("<pet");
				}
				modelstr = getValueData("model=\"",data.substring(data.indexOf(find),data.indexOf(">",pos)+">".length()),model);
				modelssttrr = getModel(modelstr,getValueData("model=\"",subdata,model),zf);
			}			
			//Let's get to business!!!;
			re += 	"	<!-- Modifying "+name[0]+"'s avatar "+i+" to "+ii+" -->\n" +
					"	<editfile name='"+alt[0]+".entity'>\n" +
					mod1 +
			//Models;
					"		"+find1+getStr("icon",icon,i)+									find2+"icon='"+					getValueData("icon=\"",subdata,portrait)+							"'"+find3+"\n" +
					"		"+find1+getStr("portrait",portrait,i)+							find2+"portrait='"+				getValueData("portrait=\"",subdata,portrait)+						"'"+find3+"\n" +
					"		"+find1+getStr("model",model,i)+								find2+"model='"+				modelstr+															"'"+find3+"\n" +
					"		"+find1+getStr("previewmodel",previewmodel,i)+					find2+"previewmodel='"+			getValueData("previewmodel=\"",subdata,previewmodel)+				"'"+find3+"\n" +
					storemodelstr;
			if(yp.getValue("config2",false) == false) {
			re +=	"		"+find1+getStr("modelscale",modelscale,i)+						find2+"modelscale='"+			getValueData("modelscale=\"",subdata,modelscale)+					"'"+find3+"\n" +
					"		"+find1+getStr("effectscale",effectscale,i)+					find2+"effectscale='"+			getValueData("effectscale=\"",subdata,effectscale)+					"'"+find3+"\n";
			}
			if(yp.getValue("config2",false) == false || yp.getValue(i+key[0]+8,true) == false) {
			re +=	"		"+find1+getStr("passiveeffect",passiveeffect,i)+				find2+"passiveeffect='"+		getValueData("passiveeffect=\"",subdata,passiveeffect)+				"'"+find3+"\n";
			}
			//Scales;
			re +=	"		"+find1+getStr("preglobalscale",preglobalscale,i)+				find2+"preglobalscale='"+		getValueData("preglobalscale=\"",subdata,preglobalscale)+			"'"+find3+"\n" +
					infoheightstr +
					tiltfactorstr +
					tiltspeedstr +
			//Sounds;
					soundstr +
			//Attack;
					attackoffsetstr +
					attackprojectilestr +
					attackstarteffectstr +
					attackactioneffectstr +
					attackimpacteffectstr +
					"		"+find1+getStr("previewpos",previewpos,i)+						find2+"previewpos='"+			getValueData("previewpos=\"",subdata,previewpos)+					"'"+find3+"\n" +
					"		"+find1+getStr("previewscale",previewscale,i)+					find2+"previewscale='"+			getValueData("previewscale=\"",subdata,previewscale)+				"'"+find3+"\n" +
					storeposstr +
					storescalestr +
					mod2 +
					mod3 +
					"	</editfile>\n" +
					modelssttrr;
			//Spell
			for(int iii = 0;iii <= spellnum;iii++) {
				//Just making a shortcut;
				Spell spell = this.spell[iii];
				//If Spell doens't have a null filepath it should be modified;
				if(spell.filepath[i].contains("null") == false && spell.filepath[ii].contains("null") == false) {
					//If the body ain't null then this spell should be modifed the following way;
					if(spell.body[i].contains("null") == false) {
						//If the avatar being modifed is not 0 then we need to use something else;
						String mod = "";
						if(i != 0) {
							mod = "		<find><![CDATA[<modifier key='"+key[i]+"' mod]]></find>\n";
						}
						re += 	"	<editfile name='heroes/"+key[0]+"/"+spell.filepath[0]+"'>\n" +
								mod +
								"		<find><![CDATA["+spell.body[i]+"]]></find><replace><![CDATA["+spell.body[ii]+"]]></replace>\n" +
								"	</editfile>\n";
					}
					//Else the spell should be modifed as normal spells;
					else {
						String[] path = new String[10];
						path = ("heroes/"+key[0]+"/"+spell.filepath[ii]).split("/");
						String dir0 = "/";
						String dir1 = "/";
						String dir2 = "/";
						String dir3 = "/";
						//making dir's
						for(int ii1 = 0;ii1 <= path.length-2;ii1++) {
							dir0 += path[ii1]+"/";
						}
						for(int ii1 = 0;ii1 <= path.length-3;ii1++) {
							dir1 += path[ii1]+"/";
						}
						for(int ii1 = 0;ii1 <= path.length-4;ii1++) {
							dir2 += path[ii1]+"/";
						}
						for(int ii1 = 0;ii1 <= path.length-5;ii1++) {
							dir3 += path[ii1]+"/";
						}
						//System.out.println("dir0 = "+dir0);
						//System.out.println("dir1 = "+dir1);
						//System.out.println("dir2 = "+dir2);
						//System.out.println("dir3 = "+dir3);
						re +=	"	<copyfile name='heroes/"+key[0]+"/"+spell.filepath[i]+"' source='heroes/"+key[0]+"/"+spell.filepath[ii]+"' overwrite='yes' fromresource='true' />\n" +
								"	<editfile name='heroes/"+key[0]+"/"+spell.filepath[i]+"'>\n" +
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
						String projectilestr = "";
						if(spell.projectile == true) {
							String datai =	LLZipFile.getData("heroes/"+key[0]+"/"+spell.filepath[i],zf);
							String dataii =	LLZipFile.getData("heroes/"+key[0]+"/"+spell.filepath[ii],zf);
							
							projectilestr = "		<find><![CDATA["+getValueData("name=\"",dataii,"")+"]]></find><replace><![CDATA["+getValueData("name=\"",datai,"")+"]]></replace><find position='start'/>\n";
						}
						re +=	projectilestr +
								"	</editfile>\n";
					}
				}
			}
		}
		return re;
	}
	public String getValueData(String value,String father,String backup) {
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
	public String getModel(String modeli,String modelii,ZipFile zf) {
		String[] s = new String[2];
		s[0] = "";
		s[1] = "";
		//Reading the hero entity;
		ZipEntry ze = zf.getEntry("heroes"+"/"+key[0]+"/"+modeli);
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
		ze = zf.getEntry("heroes"+"/"+key[0]+"/"+modelii);
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
		String dir0 = "/heroes/"+key[0]+"/";
		String dir1 = "/heroes/"+key[0]+"/";
		String dir2 = "/heroes/"+key[0]+"/";
		String dir3 = "/heroes/"+key[0]+"/";
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
		String re = 	"	<editfile name='heroes"+"/"+key[0]+"/"+modeli+"'>\n" +
						"		<findall><![CDATA[<!--]]></findall><delete/><find position='start'/><findall><![CDATA[-->]]></findall><delete/><find position='start'/>\n" +
						"		<find><![CDATA[<model]]></find><replace><![CDATA["+s[0]+"\n\n<!--<model]]></replace>\n" +
						"		<find><![CDATA[</model>]]></find><replace><![CDATA[</model>-->]]></replace>\n" +
						"	</editfile>\n";
		return re.replace("<?xml version=\"1.0\" encoding=\"UTF-8\"?>","").replaceAll("file=\"","file=\""+dir0).replaceAll("low=\"","low=\""+dir0).replaceAll("med=\"","med=\""+dir0).replaceAll("high=\"","high=\""+dir0);
	}
	//_____________________________//________________________________________\\_______________________________\\
	public String getIndicatorXAML(LLProperty yp,ZipFile zf) {
		String re = "";
		String[] preglobalscale = new String[10];
		boolean x = true;
		for(int i = 0;i <= indicnum;i++) {
			Indicator indic = this.indic[i];
			if(yp.getValue("i"+key[0]+indic.key,true)) {
				if(x == true) {
					String data = LLZipFile.getData(alt[0]+".entity",zf);
					String defaultscale = getValueData("preglobalscale=\"",data,null);
					for(int ii = 0;ii <= 9;ii++) {
						preglobalscale[ii] = getValueData("preglobalscale=\"",data,defaultscale);
						data = data.replaceFirst("preglobalscale=\"","preglobalINVAILDscale=\"");
					}
					re +=		"	<editfile name='"+alt[0]+".entity'>\n";
					for(int ii = 0;ii <= LLString.containsCount(data,"effectscale=\"")-1;ii++) {
						re +=	"		<find><![CDATA[effectscale='"+getValueData("effectscale=\"",data,null)+"']]></find>\n" +
								"		<replace><![CDATA[effectscale='0.9']]></replace><find position='start'/>\n";
						data = data.replaceFirst("effectscale=\"","effectINVAILDscale=\"");
					}
					for(int ii = 0;ii <= LLString.containsCount(data,"modelscale=\"")-1;ii++) {
						re +=	"		<find><![CDATA[modelscale='"+getValueData("modelscale=\"",data,null)+"']]></find>\n" +
								"		<replace><![CDATA[modelscale='1']]></replace><find position='start'/>\n";
						data = data.replaceFirst("modelscale=\"","modelINVAILDscale=\"");
					}
					re +=		"	</editfile>\n";
					x = false;
				}
				//Collecting the entity data;
				String data = LLZipFile.getData("heroes/"+key[0]+"/"+indic.filepath,zf);
				String[] arange = indic.range.split(",");
				if(indic.range.equals("")) {
					arange = getValueData("range=\"",data,null).split(",");
				}
				int[] range = new int[arange.length];
				for(int ii = 0;ii <= arange.length-1;ii++) {
					range[ii] = Integer.parseInt(arange[ii])+50;
				}
				int times = LLString.containsCount(data,"passiveeffect=\"");
				if(times == 0) {
					times = 1;
				}
				for(int iiii = 0;iiii <= times-1;iiii++) {
					boolean y = true;
					String passiveeffect = getValueData("passiveeffect=\"",data,null);
					data = data.replaceFirst("passiveeffect=\"","passiveINVAILDeffect=\"");
					String rep;
					String d = "";
					if(passiveeffect == null) {
						//modifing this huh? remember to modify all three states!!!
						rep =	"		<find><![CDATA[name=']]></find>\n" +
								"		<insert position='before'><![CDATA[passiveeffect='null'\n" +
								"	]]></insert>";
						passiveeffect = "passive.effect";
					}
					else if(passiveeffect.equals("") == true) {
						//modifing this huh? remember to modify all three states!!!
						rep =	"		<find><![CDATA[passiveeffect='']]></find>\n" +
								"		<replace><![CDATA[passiveeffect='null']]></replace>";
						passiveeffect = "passive.effect";
					}
					else {
						d = " fromresource='true'";
						//modifing this huh? remember to modify all three states!!!
						String[] path = new String[10];
						path = ("heroes/"+key[0]+"/"+indic.filepath).split("/");
						String dir0 = "/";
						String dir1 = "/";
						String dir2 = "/";
						String dir3 = "/";
						//making dir's
						for(int ii = 0;ii <= path.length-2;ii++) {
							dir0 += path[ii]+"/";
						}
						for(int ii = 0;ii <= path.length-3;ii++) {
							dir1 += path[ii]+"/";
						}
						for(int ii = 0;ii <= path.length-4;ii++) {
							dir2 += path[ii]+"/";
						}
						for(int ii = 0;ii <= path.length-5;ii++) {
							dir3 += path[ii]+"/";
						}
						//modifing this huh? remember to modify all three states!!!
						rep =	"		<find><![CDATA[passiveeffect='"+passiveeffect+"']]></find>\n" +
								"		<replace><![CDATA[passiveeffect='null']]></replace>";
						passiveeffect = (dir0+passiveeffect).replaceAll(dir0+"/heroes","/heroes").replaceAll(dir0+"../../../",dir3).replaceAll(dir0+"../../",dir2).replaceAll(dir0+"../",dir1).substring(1);
						String subdata = LLZipFile.getData(passiveeffect,zf);
						if(subdata.contains("useentityeffectscale=\"true\"") == false && subdata.contains("useentityeffectscale=\"1\"") == false) {
							y = false;
						}
					}
					String passive = "";
					int colornum = indic.key;
					if(colornum%2 != 0) {
						colornum = (indic.key-1)/2;
					}
					else {
						colornum = colornum/2;
					}
					double iii = 1/255;
					String color = ((yp.getValue("color"+colornum+2,0)/255.0)+" "+(yp.getValue("color"+colornum+3,0)/255.0)+" "+(yp.getValue("color"+colornum+4,0)/255.0));
					for(int ii = 0;ii <= range.length-1;ii++) {
						String size;
						if(y == false) {
							System.out.println(range[ii]+" "+iiii+" "+preglobalscale[iiii]+" "+1.0);
							size = (range[ii]/(Double.parseDouble(preglobalscale[iiii])*1.0))+"";
						}
						else {
							size = (range[ii]/(0.9*1.0))+"";
						}
						int iiiii = iiii;
						if(yp.getValue("config0",false) == true) {
							iiiii = yp.getValue(key[0]+iiii,iiii);
						}
						passive += ",/heroes/"+key[0]+"/passive"+indic.key+ii+iiiii+".effect";
						//Making the dirs;
						String[] path = new String[10];
						path = (passiveeffect).split("/");
						String dir0 = "/";
						String dir1 = "/";
						String dir2 = "/";
						String dir3 = "/";
						//making dir's
						for(int ii1 = 0;ii1 <= path.length-2;ii1++) {
							dir0 += path[ii1]+"/";
						}
						for(int ii1 = 0;ii1 <= path.length-3;ii1++) {
							dir1 += path[ii1]+"/";
						}
						for(int ii1 = 0;ii1 <= path.length-4;ii1++) {
							dir2 += path[ii1]+"/";
						}
						for(int ii1 = 0;ii1 <= path.length-5;ii1++) {
							dir3 += path[ii1]+"/";
						}
						re +=	"	<copyfile name='heroes/"+key[0]+"/passive"+indic.key+ii+iiii+".effect' source='"+passiveeffect+"' overwrite='no'"+d+"/>\n" +
								"	<editfile name='heroes/"+key[0]+"/passive"+indic.key+ii+iiii+".effect'>\n" +
								"		<find><![CDATA[</definitions]]></find>\n" +
								"		<insert position='before'><![CDATA[	<particlesystem name='system55' space='world' scale='1'>\n" +
								"			<groundsprite\n" +
								"				life='1'\n" +
								"				loop='true'\n" +
								"				material='/shared/materials/cic.material'\n" +
								"				size='"+size+"'\n" +
								"				midsizepos='.5'\n" +
								"				alpha='1'\n" +
								"				startcolor='"+color+"'\n" +
								"				midcolor='"+color+"'\n" +
								"				endcolor='"+color+"'\n" +
								"				midcolorpos='.2'\n" +
								"				position='0 0 0'\n" +
								"				minangle='0'\n" +
								"				maxangle='0'\n" +
								"				angles='0 0 0'\n" +
								"				yawspeed='0'\n" +
								"			/>\n" +
								"		</particlesystem>\n" +
								"	]]></insert><find position='start'/>\n" +
								"		<find><![CDATA[<thread>]]></find>\n" +
								"		<insert position='after'><![CDATA[\n" +
								"		<spawnparticlesystem instance='instance55' particlesystem='system55'/>]]></insert><find position='start'/>\n" +
								"		<find><![CDATA[</thread]]></find>\n" +
								"		<insert position='before'><![CDATA[	<waitfordeath instance='instance55'/>\n" +
								"	]]></insert><find position='start'/>\n" +
								//Making the dirs correct;
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
								"		<findall><![CDATA["+dir0+"../]]></findall><replace><![CDATA["+dir1+"]]></replace><find position='start'/>\n" +
								"	</editfile>\n";
					}
					re +=	"	<editfile name='heroes/"+key[0]+"/"+indic.filepath+"'>\n" +
							(rep.replace("null",(passive.substring(1))))+"\n" +
							"	</editfile>\n";
				}
			}
		}
		return re;
	}
}
