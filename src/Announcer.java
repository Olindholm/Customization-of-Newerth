import java.awt.Image;
import javax.swing.ImageIcon;

public class Announcer {
	
	String name;
	String key;
	String voice;
	String arcade;
	
	Image image;
	
	//Sound Locations
	int immortal = 0;
	int smackdown = 1;
	int humiliation = 2;
	int onekill = 11;
	int twokill = 12;
	int threekill = 13;
	int fourkill = 14;
	int fivekill = 15;
	int genocide = 16;
	int massacre = 17;
	int rival = 18;
	int retribution = 19;
	int startgame = 20;
	int getiton = 21;
	int ragequit = 22;
	int denied = 23;
	int win = 24;
	int lost = 25;
	int kongor = 26;
	int zorgath = 27;
	int transmutantstein = 28;
	int nemesis = 29;
	int payback = 30;
	int hellbarrack = 31;
	int helltower = 32;
	int hellwin = 33;
	int legibarrack = 34;
	int legitower = 35;
	int legiwin = 36;
	int underatk = 37;
	String[] sound = new String[38];
	boolean soundmove = false;
	//arcade text locations
	int tannihilation = 0;
	int tarrow = 1;
	//2 remember arrow2.clip, 
	int tbloodbath = 2;
	int tbloodlust = 3;
	int tcountdown = 4;
	int tdefeat = 5;
	int tdenied = 6;
	int tdoubletap = 7;
	int tgenocide = 8;
	int thattrick = 9;
	int thumiliation = 10;
	int timmortal = 11;
	int tinvis = 12;
	int tnemesis = 13;
	int tpaused = 14;
	int tpayback = 15;
	int tquadkill = 16;
	int tragequit = 17;
	int tsmackdown = 18;
	int tvictory = 19;
	String[] text = new String[20];
	
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
		
		ImageIcon image = new ImageIcon(getClass().getResource(this.voice+".png"));
		this.image = image.getImage();
	}
}
