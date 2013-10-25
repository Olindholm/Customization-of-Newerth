package Engine.Attributes;

import java.util.Vector;

public class Announcer {
	
	public String name;
	public String key;
	public String voice;
	public String arcade;
	
	//Sound Locations
	static final int immortal = 0;
	static final int smackdown = 1;
	static final int humiliation = 2;
	static final int onekill = 11;
	static final int twokill = 12;
	static final int threekill = 13;
	static final int fourkill = 14;
	static final int fivekill = 15;
	static final int genocide = 16;
	static final int massacre = 17;
	static final int rival = 18;
	static final int retribution = 19;
	static final int startgame = 20;
	static final int getiton = 21;
	static final int ragequit = 22;
	static final int denied = 23;
	static final int win = 24;
	static final int lost = 25;
	static final int kongor = 26;
	static final int zorgath = 27;
	static final int transmutantstein = 28;
	static final int nemesis = 29;
	static final int payback = 30;
	static final int hellbarrack = 31;
	static final int helltower = 32;
	static final int hellwin = 33;
	static final int legibarrack = 34;
	static final int legitower = 35;
	static final int legiwin = 36;
	static final int underatk = 37;
	
	public String[] sound = new String[38];
	public boolean movesound = false;
	//arcade text locations
	static final int tannihilation = 0;
	static final int tarrow = 1;
	//2 remember arrow2.clip, 
	static final int tbloodbath = 2;
	static final int tbloodlust = 3;
	static final int tcountdown = 4;
	static final int tdefeat = 5;
	static final int tdenied = 6;
	static final int tdoubletap = 7;
	static final int tgenocide = 8;
	static final int thattrick = 9;
	static final int thumiliation = 10;
	static final int timmortal = 11;
	static final int tinvis = 12;
	static final int tnemesis = 13;
	static final int tpaused = 14;
	static final int tpayback = 15;
	static final int tquadkill = 16;
	static final int tragequit = 17;
	static final int tsmackdown = 18;
	static final int tvictory = 19;
	public String[] text = new String[20];
	
	public Announcer(String name,String key,String voice,String arcade) {
		this.name = name;
		this.key = key;
		this.voice = voice;
		this.arcade = arcade;
		
		sound[immortal] = "shared/sounds/announcer/"+voice+"/immortal.ogg";
		sound[smackdown] = "shared/sounds/announcer/"+voice+"/smackdown.ogg";
		sound[humiliation] = "shared/sounds/announcer/"+voice+"/humiliation.ogg";
		for(int i = 3;i <= 10;i++) {
			sound[i] = "shared/sounds/announcer/"+voice+"/"+i+"_kills.ogg";
		}
		sound[onekill] = "shared/sounds/announcer/"+voice+"/first_blood.ogg";
		sound[twokill] = "shared/sounds/announcer/"+voice+"/double_kill.ogg";
		sound[threekill] = "shared/sounds/announcer/"+voice+"/triple_kill.ogg";
		sound[fourkill] = "shared/sounds/announcer/"+voice+"/quad_kill.ogg";
		sound[fivekill] = "shared/sounds/announcer/"+voice+"/annihilation.ogg";
		sound[genocide] = "shared/sounds/announcer/"+voice+"/genocide.ogg";
		sound[massacre] = "shared/sounds/announcer/"+voice+"/massacre.ogg";
		sound[rival] = "shared/sounds/announcer/"+voice+"/rival.ogg";
		sound[retribution] = "shared/sounds/announcer/"+voice+"/retribution.ogg";
		sound[startgame] = "shared/sounds/announcer/"+voice+"/startgame.ogg";
		sound[getiton] = "shared/sounds/announcer/"+voice+"/get_it_on.ogg";
		sound[ragequit] = "shared/sounds/announcer/"+voice+"/rage_quit.ogg";
		sound[denied] = "shared/sounds/announcer/"+voice+"/denied.ogg";
		sound[win] = "shared/sounds/announcer/"+voice+"/victory.ogg";
		sound[lost] = "shared/sounds/announcer/"+voice+"/defeat.ogg";
		sound[kongor] = "shared/sounds/announcer/"+voice+"/kongor_slain.ogg";
		sound[zorgath] = "shared/sounds/announcer/"+voice+"/zorgath_slain.ogg";
		sound[transmutantstein] = "shared/sounds/announcer/"+voice+"/transmutantstein_slain.ogg";
		sound[nemesis] = "shared/sounds/announcer/"+voice+"/nemesis.ogg";
		sound[payback] = "shared/sounds/announcer/"+voice+"/rival.ogg";
		sound[hellbarrack] = "shared/sounds/announcer/"+voice+"/hellbourne_barracks_destroyed.ogg";
		sound[helltower] = "shared/sounds/announcer/"+voice+"/hellbourne_destroy_legion_tower.ogg";
		sound[hellwin] = "shared/sounds/announcer/"+voice+"/hellbourne_wins.ogg";
		sound[legibarrack] = "shared/sounds/announcer/"+voice+"/legion_barracks_destroyed.ogg";
		sound[legitower] = "shared/sounds/announcer/"+voice+"/legion_destroy_hellbourne_tower.ogg";
		sound[legiwin] = "shared/sounds/announcer/"+voice+"/legion_wins.ogg";
		sound[underatk] = "shared/sounds/announcer/"+voice+"/structure_under_attack.ogg";
		
		text[tannihilation] = "ui/common/models/"+arcade+"/annihilation";
		text[tarrow] = "ui/common/models/"+arcade+"/arrow";
		text[tbloodlust] = "ui/common/models/"+arcade+"/bloodlust";
		text[tbloodbath] = "ui/common/models/"+arcade+"/bloodbath";
		text[tcountdown] = "ui/common/models/"+arcade+"/countdown";
		text[tdefeat] = "ui/common/models/"+arcade+"/defeat";
		text[tdenied] = "ui/common/models/"+arcade+"/denied";
		text[tdoubletap] = "ui/common/models/"+arcade+"/doubletap";
		text[tgenocide] = "ui/common/models/"+arcade+"/genocide";
		text[thattrick] = "ui/common/models/"+arcade+"/hattrick";
		text[thumiliation] = "ui/common/models/"+arcade+"/humiliation";
		text[timmortal] = "ui/common/models/"+arcade+"/immortal";
		text[tinvis] = "ui/common/models/"+arcade+"/invis";
		text[tnemesis] = "ui/common/models/"+arcade+"/nemesis";
		text[tpaused] = "ui/common/models/"+arcade+"/paused";
		text[tpayback] = "ui/common/models/"+arcade+"/payback";
		text[tquadkill] = "ui/common/models/"+arcade+"/quadkill";
		text[tragequit] = "ui/common/models/"+arcade+"/ragequit";
		text[tsmackdown] = "ui/common/models/"+arcade+"/smackdown";
		text[tvictory] = "ui/common/models/"+arcade+"/victory";
	}
	//Announcer
	public String getXAML(Vector<Announcer> anno,int cur,int alt) {
		String string = "";
		String mod = "";
		if(anno.get(cur).movesound == false) {
			String arcade2 = null;
			if(anno.get(alt).arcade.equals("arcade_text")) {
				arcade2 = "normal";
			}
			else {
				arcade2 = anno.get(alt).arcade;
			}
			mod += "	<editfile name='base.upgrades'>\n" +
					"		<find><![CDATA[<announcervoice name='"+anno.get(cur).key+"' voiceset='"+anno.get(cur).voice+"' arcadetext='"+anno.get(cur).arcade+"' />]]></find><replace><![CDATA[<announcervoice name='"+anno.get(cur).key+"' voiceset='"+anno.get(alt).voice+"' arcadetext='"+arcade2+"' />]]></replace><find position='start'/>\n" +
					"	</editfile>\n";
		}
		else {
			for(int i = 0;i <= 37;i++) {
				string += "	<copyfile name='"+anno.get(cur).sound[i]+"' source='"+anno.get(alt).sound[i]+"' overwrite='yes' fromresource='true' />\n";
			}
			for(int i = 0;i <= 19;i++) {
				string += "	<copyfile name='"+anno.get(cur).text[i]+".mdf' source='"+anno.get(alt).text[i]+".mdf' overwrite='yes' fromresource='true' />\n";
				string += "	<copyfile name='"+anno.get(cur).text[i]+".model' source='"+anno.get(alt).text[i]+".model' overwrite='yes' fromresource='true' />\n";
				string += "	<copyfile name='"+anno.get(cur).text[i]+".clip' source='"+anno.get(alt).text[i]+".clip' overwrite='yes' fromresource='true' />\n";
			}
			string += 	"	<copyfile name='ui/common/models/"+anno.get(cur).arcade+"/material.material' source='ui/common/models/"+anno.get(alt).arcade+"/material.material' overwrite='yes' fromresource='true' />\n" +
						"	<editfile name='ui/common/models/"+anno.get(cur).arcade+"/material.material'><findall><![CDATA[color.tga]]></findall><replace><![CDATA[/ui/common/models/"+anno.get(alt).arcade+"/color.tga]]></replace></editfile>" +
						"	<copyfile name='ui/common/models/"+anno.get(cur).arcade+"/material2.material' source='ui/common/models/"+anno.get(alt).arcade+"/material.material' overwrite='yes' fromresource='true' />\n" +
						"	<editfile name='ui/common/models/"+anno.get(cur).arcade+"/material2.material'><findall><![CDATA[color.tga]]></findall><replace><![CDATA[/ui/common/models/"+anno.get(alt).arcade+"/color.tga]]></replace></editfile>" +
						"	<copyfile name='ui/common/models/"+anno.get(cur).arcade+"/arrow2.clip' source='ui/common/models/"+anno.get(cur).arcade+"/arrow2.clip' overwrite='yes' fromresource='true' />\n";
		}
		string += mod;
		return string;
	}
}
