import java.awt.Image;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Properties;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;

public class Config {
	public Config(Main main) {
		this.main = main;
		//config load
		config = new Properties();
		file = new File(App_Data+File.separator+"Lindholm"+File.separator+"Customization_of_Newerth"+File.separator+"config.ini");
		if(file.exists() == false) {
			new File(App_Data+File.separator+"Lindholm"+File.separator+"Customization_of_Newerth"+File.separator+"").mkdirs();
			try {
				file.createNewFile();
			} catch (IOException e) {
				JOptionPane.showMessageDialog(null,"Could not import config data, please check your promissions.");
				System.exit(0);
			}
		}
		try {
			in = new FileInputStream(file);
		} catch (FileNotFoundException e) {
			JOptionPane.showMessageDialog(null,"Could not import config data, please check your promissions.");
			System.exit(0);
		}
		try {
			config.load(in);
		} catch (IOException e) {
			JOptionPane.showMessageDialog(null,"Could not import config data, please check your promissions.");
			System.exit(0);
		}
		try {
			in.close();
		} catch (IOException e) {
			JOptionPane.showMessageDialog(null,"Could not export config data, please check your promissions.");
			System.exit(0);
		}
		ImageIcon image = new ImageIcon(getClass().getResource("error.png"));
		this.image = image.getImage();
		//version check
		if(getValue("version","version").equals(version) != true) {
			String[] option = {"I Agree","I Disagree"};
			
			URL Tosversion = null;
			URLConnection connection = null;
			try {
				Tosversion = new URL(link+"tosdate.txt");
			} catch (MalformedURLException e) {
				JOptionPane.showMessageDialog(null,"Could not download important data, please check your connection.");
				e.printStackTrace();
			}
			try {
				connection = Tosversion.openConnection();
			} catch (IOException e) {
				JOptionPane.showMessageDialog(null,"Could not download important data, please check your connection.");
				e.printStackTrace();
			}
			connection.setConnectTimeout(10000);
			BufferedReader br = null;
			try {
				br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
			} catch (IOException e) {
				JOptionPane.showMessageDialog(null,"Could not download important data, please check your connection.");
				System.exit(0);
			}
			ArrayList<String> tosdate = new ArrayList<String>();
			try {
				while (br.ready()) {
					tosdate.add(br.readLine());
				}
			} catch (IOException e) {
				JOptionPane.showMessageDialog(null,"Could not download important data, please check your connection.");
				e.printStackTrace();
			}
			try {
				br.close();
			} catch (IOException e) {
				JOptionPane.showMessageDialog(null,"Could not download important data, please check your connection.");
				e.printStackTrace();
			}
			if(getValue("tosdate","11/11/11").equals(tosdate.get(tosdate.size()-1)) != true) {
				URL Tos = null;
				URLConnection tosconnection = null;
				try {
					Tos = new URL(link+"tos.txt");
				} catch (MalformedURLException e) {
					e.printStackTrace();
				}
				try {
					tosconnection = Tos.openConnection();
				} catch (IOException e) {
					e.printStackTrace();
				}
				tosconnection.setConnectTimeout(10000);
				BufferedReader tosbr = null;
				try {
					tosbr = new BufferedReader(new InputStreamReader(tosconnection.getInputStream()));
				} catch (IOException e) {
					e.printStackTrace();
				}
				ArrayList<String> tosdata = new ArrayList<String>();
				try {
					while (tosbr.ready()) {
						tosdata.add(tosbr.readLine());
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
				try {
					tosbr.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
				String tosstring = "";
				for(int i = 0;i <= tosdata.size()-1;i++) {
					tosstring += tosdata.get(i)+"\n";
				}
				config.setProperty("tos",tosstring);
				config.setProperty("tosdate",tosdate.get(tosdate.size()-1));
			}
			config.setProperty("version",version);
			if(JOptionPane.showOptionDialog(null,getValue("tos","1* This software is liscensed and not sold or given. Everything inside it belongs to the owner(in this case Wiggy boy <Lindholm>), except the Heroes of Newerth picture's borrowed from S2Games.\n2* All belonging files to this software has a mark and if you happen to find one, leave it.\n3* You shall never try to extract or decode either this software or a generated file by this software.\n4* You shall never upload this software to any website or host server.\n5* This software is freely licensed and should never be sold or tried to be sold.\n6* The owner of this software has all rights to change, delete or add any terms to the agreement at anytime.\n"),"Terms of Service!",JOptionPane.YES_NO_OPTION,-1,null,option,option[1]) != JOptionPane.YES_OPTION) {
				System.exit(0);
			}
			save();
		}
		//Hero avatars
		hero[h += 1] = new Hero("Courier","cur");
		hero[h].ground[0] = "icon='icon.tga' portrait='icon.tga' model='model.mdf' preglobalscale='1.33' passiveeffect='' previewmodel='model.mdf' previewpos='0 0 -50' previewscale='1.33'";
		hero[h].flying[0] = "icon='icon.tga' portrait='icon.tga' model='model.mdf' preglobalscale='1.33' passiveeffect='' previewmodel='model.mdf' previewpos='0 0 -50' previewscale='1.33'";
		hero[h].addAlt("Penguin","C_Penguin",null);
		hero[h].ground[1] = "icon='/items/couriers/penguin/icon.tga' portrait='/items/couriers/penguin/icon.tga' model='/items/couriers/penguin/model.mdf' preglobalscale='1.7' passiveeffect='' previewmodel='/items/couriers/penguin/model.mdf' previewpos='0 0 -50' previewscale='1.8'";
		hero[h].flying[1] = "icon='/items/couriers/penguin_fly/icon.tga' portrait='/items/couriers/penguin_fly/icon.tga' model='/items/couriers/penguin_fly/model.mdf' preglobalscale='1.65' passiveeffect='/items/couriers/penguin_fly/effects/body.effect' previewmodel='/items/couriers/penguin_fly/model.mdf' previewpos='0 0 -30' previewscale='1.8'";
		hero[h].addAlt("Dragon","C_Dragon",null);
		hero[h].ground[2] = "icon='/items/couriers/dragon/icon.tga' portrait='/items/couriers/dragon/icon.tga' model='/items/couriers/dragon/model.mdf' preglobalscale='1.8' passiveeffect='' previewmodel='/items/couriers/dragon/model.mdf' previewpos='0 0 -50' previewscale='1.8'";
		hero[h].flying[2] = "icon='/items/couriers/dragon_fly/icon.tga' portrait='/items/couriers/dragon_fly/icon.tga' model='/items/couriers/dragon_fly/model.mdf' preglobalscale='1.25' passiveeffect='' previewmodel='/items/couriers/dragon_fly/model.mdf' previewpos='0 0 -10' previewscale='1.8'";
		hero[h].addAlt("Chicken","C_Chicken",null);
		hero[h].ground[3] = "icon='/items/couriers/chicken/icon.tga' portrait='/items/couriers/chicken/icon.tga' model='/items/couriers/chicken/model.mdf' preglobalscale='1.5' passiveeffect='' previewmodel='/items/couriers/chicken/model.mdf' previewpos='0 0 -50' previewscale='1.8'";
		hero[h].flying[3] = "icon='/items/couriers/chicken_fly/icon.tga' portrait='/items/couriers/chicken_fly/icon.tga' model='/items/couriers/chicken_fly/model.mdf' preglobalscale='1.25' passiveeffect='' previewmodel='/items/couriers/chicken_fly/model.mdf' previewpos='0 0 -10' previewscale='1.8'";
		hero[h].addAlt("Goblin","C_Goblin",null);
		hero[h].ground[4] = "icon='/items/couriers/goblin/icon.tga' portrait='/items/couriers/goblin/icon.tga' model='/items/couriers/goblin/model.mdf' preglobalscale='1.5' passiveeffect='' previewmodel='/items/couriers/goblin/model.mdf' previewpos='0 0 -50' previewscale='1.8'";
		hero[h].flying[4] = "icon='/items/couriers/goblin_fly/icon.tga' portrait='/items/couriers/goblin_fly/icon.tga' model='/items/couriers/goblin_fly/model.mdf' preglobalscale='1.5' passiveeffect='' previewmodel='/items/couriers/goblin_fly/model.mdf' previewpos='0 0 -10' previewscale='1.8'";
		hero[h].addAlt("Pig","C_Pig",null);
		hero[h].ground[5] = "icon='/items/couriers/pig/icon.tga' portrait='/items/couriers/pig/icon.tga' model='/items/couriers/pig/model.mdf' preglobalscale='1.4' passiveeffect='' previewmodel='/items/couriers/pig/model.mdf' previewpos='0 0 -50' previewscale='1.8'";
		hero[h].flying[5] = "icon='/items/couriers/pig_fly/icon.tga' portrait='/items/couriers/pig_fly/icon.tga' model='/items/couriers/pig_fly/model.mdf' preglobalscale='1.15' passiveeffect='' previewmodel='/items/couriers/pig_fly/model.mdf' previewpos='0 0 -30' previewscale='1.8'";
		hero[h].addAlt("Rat","C_Rat",null);
		hero[h].ground[6] = "icon='/items/couriers/rat/icon.tga' portrait='/items/couriers/rat/icon.tga' model='/items/couriers/rat/model.mdf' preglobalscale='0.75' passiveeffect='' previewmodel='/items/couriers/rat/model.mdf' previewpos='0 0 -50' previewscale='1'";
		hero[h].flying[6] = "icon='/items/couriers/rat_fly/icon.tga' portrait='/items/couriers/rat_fly/icon.tga' model='/items/couriers/rat_fly/model.mdf' preglobalscale='1.5' passiveeffect='' previewmodel='/items/couriers/rat_fly/model.mdf' previewpos='0 0 -10' previewscale='1.6'";
		hero[h].addAlt("Robot","C_Robot",null);
		hero[h].ground[7] = "icon='/items/couriers/robot/icon.tga' portrait='/items/couriers/robot/icon.tga' model='/items/couriers/robot/model.mdf' preglobalscale='1.8' passiveeffect='/items/couriers/robot/effects/body.effect' previewmodel='/items/couriers/robot/model.mdf' previewpos='0 0 -50' previewscale='1.8'";
		hero[h].flying[7] = "icon='/items/couriers/ufo/icon.tga' portrait='/items/couriers/ufo/icon.tga' model='/items/couriers/ufo/model.mdf' preglobalscale='1.6' passiveeffect='/items/couriers/ufo/effects/body.effect' previewmodel='/items/couriers/ufo/model.mdf' previewpos='0 0 -10' previewscale='1.8'";
		hero[h].addAlt("Cat","C_Cat",null);
		hero[h].ground[8] = "icon='/items/couriers/cat/icon.tga' portrait='/items/couriers/cat/icon.tga' model='/items/couriers/cat/model.mdf' preglobalscale='1.6' passiveeffect='' previewmodel='/items/couriers/cat/model.mdf' previewpos='0 0 -50' previewscale='2.0'";
		hero[h].flying[8] = "icon='/items/couriers/cat_fly/icon.tga' portrait='/items/couriers/cat_fly/icon.tga' model='/items/couriers/cat_fly/model.mdf' preglobalscale='1.2' passiveeffect='/items/couriers/cat_fly/body.effect' previewmodel='/items/couriers/cat_fly/model.mdf' previewpos='0 0 -40' previewscale='1.8'";
		hero[h += 1] = new Hero("Accursed","accursed");
		hero[h].addSpell("ability_01/ability.entity","icon='icon.tga'",false);
		hero[h].addSpell("ability_01/ability.effect",null,false);
		hero[h].addSpell("ability_01/ability_ally.effect",null,false);
		hero[h].addSpell("ability_01/cast.effect",null,false);
		hero[h].addSpell("ability_02/ability.entity","icon='icon.tga'",false);
		hero[h].addSpell("ability_02/effects/ability.effect",null,false);
		hero[h].addSpell("ability_02/effects/state.effect",null,false);
		hero[h].addSpell("ability_03/ability.entity","icon='icon.tga'",false);
		hero[h].addSpell("ability_03/ability.effect",null,false);
		hero[h].addSpell("ability_03/state.effect",null,false);
		hero[h].addSpell("ability_04/ability.entity","icon='icon.tga'",false);
		hero[h].addAlt("Sub Zero Accursed","Alt","alt");
		hero[h].spell[0].body[1] = "icon='/heroes/accursed/alt/ability_01/icon.tga'";
		hero[h].spell[1].filepath[1] = "alt/ability_01/effects/ability.effect";
		hero[h].spell[2].filepath[1] = "alt/ability_01/effects/ability_ally.effect";
		hero[h].spell[3].filepath[1] = "alt/ability_01/effects/cast.effect";
		hero[h].spell[4].body[1] = "icon='/heroes/accursed/alt/ability_02/icon.tga'";
		hero[h].spell[5].filepath[1] = "alt/ability_02/effects/ability.effect";
		hero[h].spell[6].filepath[1] = "alt/ability_02/effects/state.effect";
		hero[h].spell[7].body[1] = "icon='/heroes/accursed/alt/ability_03/icon.tga'";
		hero[h].spell[8].filepath[1] = "alt/ability_03/effects/ability.effect";
		hero[h].spell[9].filepath[1] = "alt/ability_03/effects/state.effect";
		hero[h].spell[10].body[1] = "icon='/heroes/accursed/alt/ability_04/icon.tga'";
		hero[h += 1] = new Hero("Aluna","aluna");
		hero[h += 1] = new Hero("Amun-Ra","ra");
		hero[h].addAlt("King-Ra","Alt","alt");
		hero[h += 1] = new Hero("Andromeda","andromeda");
		hero[h].addSpell("ability_01/effects/trail.effect",null,false);
		hero[h].addSpell("ability_01/effects/state.effect",null,false);
		hero[h].addSpell("projectile/projectile.entity",null,true);
		hero[h].addAlt("Mandromeda","Alt","alt");
		hero[h += 1] = new Hero("Arachna","arachna");
		hero[h].addAlt("Queen Arachna","Alt","alt");
		hero[h += 1] = new Hero("Armadon","armadon");
		hero[h].addAlt("Winston Charmadon","Alt","alt");
		hero[h += 1] = new Hero("Balphagore","bephelgor");
		hero[h += 1] = new Hero("Behemoth","behemoth");
		hero[h].addAlt("Infernal Behemoth","Alt","alt");
		hero[h += 1] = new Hero("Blacksmith","dwarf_magi");
		hero[h].addAlt("Leprechaun Blacksmith","Alt","alt");
		hero[h += 1] = new Hero("Blood Hunter","hunter");
		hero[h += 1] = new Hero("Bombardier","bomb");
		hero[h].addAlt("Cannon Bombardier","Alt","alt");
		hero[h += 1] = new Hero("Bubbles","bubbles");
		hero[h].addAlt("Ninja Bubbles","Alt","alt");
		hero[h += 1] = new Hero("Chronos","chronos");
		hero[h].editmodel = true;
		hero[h].addSpell("ability_01/effects/walk.effect",null,false);
		hero[h].addSpell("ability_04/effects/orb.effect",null,false);
		hero[h].addAlt("Father Time Chronos","Alt","alt");
		hero[h].spell[1].filepath[1] = null;
		hero[h].addAlt("GrandFather Time Chronos","Alt2","alt2");
		hero[h += 1] = new Hero("Corrupted Disciple","corrupted_disciple");
		hero[h].addAlt("Steampunk Disciple","Alt","alt");
		hero[h += 1] = new Hero("Cthulhuphant","cthulhuphant");
		hero[h].editmodel = true;
		hero[h].addAlt("King Cthulhuphant","Alt","alt");
		hero[h += 1] = new Hero("Dampeer","dampeer");
		hero[h].addAlt("Vacation Dampeer","Alt","alt");
		hero[h += 1] = new Hero("Deadwood","deadwood");
		hero[h].addSpell("ability_04/effects/cast.effect",null,false);
		hero[h].addSpell("ability_04/effects/cast_self.effect",null,false);
		hero[h].addAlt("Jollywood","Alt","alt");
		hero[h].spell[0].filepath[1] = null;
		hero[h].spell[1].filepath[1] = null;
		hero[h].addAlt("Flaming Deadwood","Alt2","alt2");
		hero[h += 1] = new Hero("Defiler","defiler");
		hero[h].editmodel = true;
		hero[h].addSpell("ability_04/effects/cast.effect",null,false);
		hero[h].addAlt("Phantom Defiler","Alt","alt");
		hero[h += 1] = new Hero("Demented Shaman","shaman");
		hero[h].addSpell("projectile/projectile.entity",null,true);
		hero[h].addAlt("Demented Witch","Alt","alt");
		hero[h += 1] = new Hero("Devourer","devourer");
		hero[h].addSpell("ability_01/projectile.entity",null,true);
		hero[h].addAlt("Clown Devourer","Clown","alt");
		hero[h].spell[0].filepath[1] = null;
		hero[h].addAlt("Oni Devourer","Alt2","alt2");
		hero[h += 1] = new Hero("Doctor Repulsor","doctor_repulsor");
		hero[h].addAlt("Proffesor Repulsor","Alt","alt");
		hero[h += 1] = new Hero("Drunken Master","drunkenmaster");
		hero[h].addSpell("ability_03/effects/warp.effect",null,false);
		hero[h].addAlt("Hillbilly Brawler","Alt","alt");
		hero[h += 1] = new Hero("Electrician","electrician");
		hero[h += 1] = new Hero("Emerald Warden","emerald_warden");
		hero[h].addSpell("projectile/attack_projectile.entity",null,true);	
		hero[h].addAlt("Dryad Emerald","Alt","alt");
		hero[h += 1] = new Hero("Empath","empath");
		hero[h].addSpell("ability_04/effects/state.effect",null,false);
		hero[h].addAlt("Mage Empath","Alt","alt");
		hero[h].spell[0].filepath[1] = "alt/ability_04/effects/state.effect";
		hero[h += 1] = new Hero("Engineer","engineer");
		hero[h].editmodel = true;
		hero[h].addAlt("Rosie Engineer","Alt","alt");
		hero[h += 1] = new Hero("Fayde","fade");
		hero[h += 1] = new Hero("Flint Beastwood","flint_beastwood");
		hero[h].addSpell("projectile/attack_projectile.entity",null,true);
		hero[h].addSpell("ability_02/effects/impact.effect",null,false);
		hero[h].addSpell("ability_04/effects/impact_kill.effect",null,false);
		hero[h].addAlt("Captain Flint","Alt","alt");
		hero[h].spell[0].filepath[1] = null;
		hero[h].addAlt("Flint Boomstick","Alt2","alt2");
		hero[h += 1] = new Hero("Flux","flux");
		hero[h].addAlt("Steam Flux","Alt","alt");
		hero[h += 1] = new Hero("Forsaken Archer","forsaken_archer");
		hero[h].addSpell("ability_04/gadget.entity","model='../model.mdf'",false);
		hero[h].addSpell("ability_04/gadget.entity","preglobalscale='1.00'",false);
		hero[h].addAlt("Forsaken Strider","Alt","alt");
		hero[h].spell[0].body[1] = "model='../alt/model.mdf'";
		hero[h].spell[1].body[1] = "preglobalscale='1.9'";
		hero[h += 1] = new Hero("Gauntlet","gauntlet");
		hero[h].addAlt("El Gauntlet","Alt","alt");
		hero[h += 1] = new Hero("Gemini","gemini");
		hero[h].addSpell("firepet_projectile/model.mdf",null,false);
		hero[h].addSpell("frostpet_projectile/model.mdf",null,false);
		hero[h].addSpell("firepet/model.mdf",null,false);
		hero[h].addSpell("frostpet/model.mdf",null,false);
		hero[h].addAlt("Panthera Gemini","Alt","alt");
		hero[h].spell[0].filepath[1] = "alt/firepet_projectile/model.mdf";
		hero[h].spell[1].filepath[1] = "alt/frost_projectile/model.mdf";
		hero[h].spell[2].filepath[1] = "alt/firepet/model.mdf";
		hero[h].spell[3].filepath[1] = "alt/frostpet/model.mdf";
		hero[h += 1] = new Hero("Geomancer","geomancer");
		hero[h].editmodel = true;
		hero[h].addAlt("Centipedalisk","Alt","alt");
		hero[h += 1] = new Hero("Glacius","frosty");
		hero[h].addAlt("Female Glacius","Female","alt");
		hero[h += 1] = new Hero("Hammerstorm","hammerstorm");
		hero[h].alt[0] = "hammerstorm";
		hero[h].addAlt("Hammer Female","Alt","alt");
		hero[h += 1] = new Hero("Hellbringer","hellbringer");
		hero[h].addSpell("ability_04/effects/star.effect",null,false);
		hero[h].addSpell("summon/pet.entity",null,true);
		hero[h].addSpell("summon/ability_02/impact.effect",null,false);
		hero[h].addSpell("summon/ability_02/ability.effect",null,false);
		hero[h].addSpell("projectile/projectile.entity",null,true);
		hero[h].addAlt("Undead Hellbringer","Alt","alt");
		hero[h].spell[0].filepath[1] = "alt/ability_04/star.effect";
		hero[h].spell[1].filepath[1] = "alt/summon/pet.entity";
		hero[h].spell[2].filepath[1] = "alt/summon/ability_02/impact.effect";
		hero[h].spell[3].filepath[1] = "alt/summon/ability_02/ability.effect";
		hero[h += 1] = new Hero("Jeraziah","jereziah");
		hero[h].addAlt("Dark Jeraziah","Alt","alt");
		hero[h += 1] = new Hero("Keeper of the Forest","treant");
		hero[h].editmodel = true;
		hero[h].addSpell("ability_04/effects/affector.effect",null,false);
		hero[h].addAlt("Chrismas Keeper","Alt","alt");
		hero[h].spell[0].filepath[1] = null;
		hero[h].addAlt("Mother Nature Keeper","Alt2","alt2");
		hero[h += 1] = new Hero("Kraken","kraken");
		hero[h].addAlt("Crustacean Kraken","Alt","alt");
		hero[h += 1] = new Hero("Legionnaire","legionnaire");
		hero[h].addAlt("Logger Legionnaire","Alt","alt");
		hero[h += 1] = new Hero("Lord Salforis","dreadknight");
		hero[h].editmodel = true;
		hero[h].addSpell("ability_01/effects/cast.effect",null,false);
		hero[h].addSpell("ability_02/effects/state.effect",null,false);
		hero[h].addSpell("ability_02/effects/attack_action.effect",null,false);
		hero[h].addSpell("ability_04/effects/cast.effect",null,false);
		hero[h].addAlt("Dreadknight Salforis","Alt","alt");
		hero[h].spell[0].filepath[1] = "alt/ability_01/effects/cast.effect";
		hero[h].spell[1].filepath[1] = "alt/ability_02/effects/state.effect";
		hero[h].spell[2].filepath[1] = "alt/ability_02/effects/attack_action.effect";
		hero[h].spell[3].filepath[1] = "alt/ability_04/effects/cast.effect";
		hero[h += 1] = new Hero("Magebane","javaras");
		hero[h].addAlt("Magus Bane","Alt","alt");
		hero[h += 1] = new Hero("Magmus","magmar");
		hero[h].addAlt("Scorpion Magmus","Scorpion","alt");
		hero[h += 1] = new Hero("Maliken","maliken");
		hero[h += 1] = new Hero("Martyr","martyr");
		hero[h].addSpell("projectile/projectile.entity",null,true);
		hero[h].addAlt("Sister Martyr","Alt","alt");
		hero[h += 1] = new Hero("Master of Arms","master_of_arms");
		hero[h].addAlt("Commander of Arms","Alt2","alt2");
		hero[h += 1] = new Hero("Midas","midas");
		hero[h].addSpell("ability_01/projectile1.entity",null,true);
		hero[h].addSpell("ability_01/projectile2.entity",null,true);
		hero[h].addSpell("ability_01/projectile3.entity",null,true);
		hero[h].addSpell("ability_01/projectile4.entity",null,true);
		hero[h].addSpell("ability_01/projectile5.entity",null,true);
		hero[h].addSpell("ability_02/effects/cast.effect",null,false);
		hero[h].addSpell("ability_02/effects/explosion.effect",null,false);
		hero[h].addSpell("ability_02/effects/impact_damage.effect",null,false);
		hero[h].addSpell("ability_02/effects/impact_heal.effect",null,false);
		hero[h].addSpell("ability_03/projectile.entity",null,true);
		hero[h].addSpell("ability_04/effects/gold.effect",null,false);
		hero[h].addSpell("ability_04/effects/sound.effect",null,false);
		hero[h].addSpell("ability_04/effects/sound_hero.effect",null,false);
		hero[h].addSpell("ability_04/effects/state.effect",null,false);
		hero[h].addSpell("projectile/projectile.entity",null,true);
		hero[h].addSpell("ability_02/effects/trail.effect",null,false);
		hero[h].addAlt("Lord Midas","Alt","alt");
		hero[h].spell[0].filepath[1] = null;
		hero[h].spell[1].filepath[1] = null;
		hero[h].spell[2].filepath[1] = null;
		hero[h].spell[3].filepath[1] = null;
		hero[h].spell[4].filepath[1] = null;
		hero[h].spell[5].filepath[1] = null;
		hero[h].spell[6].filepath[1] = null;
		hero[h].spell[7].filepath[1] = null;
		hero[h].spell[8].filepath[1] = null;
		hero[h].spell[9].filepath[1] = null;
		hero[h].spell[10].filepath[1] = null;
		hero[h].spell[11].filepath[1] = null;
		hero[h].spell[12].filepath[1] = null;
		hero[h].spell[13].filepath[1] = null;
		hero[h].spell[14].filepath[1] = null;
		hero[h].spell[15].filepath[1] = null;
		hero[h].addAlt("Christmas Midas","Alt2","alt2");
		hero[h += 1] = new Hero("Monarch","monarch");
		hero[h].editmodel = true;
		hero[h].addAlt("Queen Monarch","Alt","alt");
		hero[h += 1] = new Hero("Monkey King","monkey_king");
		hero[h].addAlt("Jade Warrior","Alt","alt");
		hero[h += 1] = new Hero("Moon Queen","krixi");
		hero[h].addSpell("projectile/attack_projectile.entity",null,true);
		hero[h].alt[0] = "krixi";
		hero[h].addAlt("Sexy Moon Queen","Sexy","alt");
		hero[h += 1] = new Hero("Moraxus","moraxus");
		hero[h += 1] = new Hero("Myrmidon","hydromancer");
		hero[h].addSpell("ability_04/effects/passive.effect",null,false);
		hero[h].addSpell("ability_04/effects/ult_form/model.mdf",null,false);
		hero[h].addAlt("Mermaidon","Alt","alt");
		hero[h].spell[0].filepath[1] = "alt/ability_04/effects/passive.effect";
		hero[h].spell[1].filepath[1] = "alt/ability_04/effects/ult_form/model.mdf";
		hero[h += 1] = new Hero("Night Hound","hantumon");
		hero[h].alt[0] = "hantumon";
		hero[h].addAlt("Teen Hound","Alt","alt");
		hero[h += 1] = new Hero("Nomad","nomad");
		hero[h].alt[0] = "nomad";
		hero[h].editmodel = true;
		hero[h].addAlt("Aladin nomad","Alt","alt");
		hero[h += 1] = new Hero("Nymphora","fairy");
		hero[h].addSpell("projectile.entity",null,true);
		hero[h].addAlt("Spriticus","Alt","alt");
		hero[h].spell[0].filepath[1] = "alt/projectile/projectile.entity";
		hero[h += 1] = new Hero("Ophelia","ophelia");
		hero[h += 1] = new Hero("Pandamonium","panda");
		hero[h].addAlt("Kangamonium","Alt","alt");
		hero[h += 1] = new Hero("Parasite","parasite");
		hero[h].addSpell("ability_02/effects/cast.effect",null,false);
		hero[h].addAlt("Mutant Parasite","Alt","alt");
		hero[h].spell[0].filepath[1] = "alt/ability_02/effects/cast.effect";
		hero[h += 1] = new Hero("Pebbles","rocky");
		hero[h].addSpell("ability_01/effects/affector.effect",null,false);
		hero[h].addSpell("ability_01/effects/trail.effect",null,false);
		hero[h].addAlt("Golden Pebbles","Alt","alt");
		hero[h += 1] = new Hero("Pestilence","pestilence");
		hero[h].editmodel = true;
		hero[h].addSpell("ability_04/effects/cast.effect",null,false);
		hero[h].addSpell("ability_04/effects/projectile.effect",null,false);
		hero[h].addSpell("ability_04/effects/state.effect",null,false);
		hero[h].addAlt("Female Pestilence","Alt","alt");
		hero[h].spell[0].filepath[1] = "alt/ability_04/effects/cast.effect";
		hero[h].spell[1].filepath[1] = "alt/ability_04/effects/projectile.effect";
		hero[h].spell[2].filepath[1] = "alt/ability_04/effects/state.effect";
		hero[h += 1] = new Hero("Pharaoh","mumra");
		hero[h].addAlt("Cleopatra Pharoh","Alt","alt");
		hero[h += 1] = new Hero("Plague Rider","diseasedrider");
		hero[h].addSpell("ability_01/ability.entity","icon='icon.tga'",false);
		hero[h].addSpell("ability_01/effects/impact.effect",null,false);
		hero[h].addSpell("ability_02/ability.entity","icon='icon.tga'",false);
		hero[h].addSpell("ability_02/effects/state.effect",null,false);
		hero[h].addSpell("ability_04/ability.entity","icon='icon.tga'",false);
		hero[h].addSpell("ability_04/effects/trail.effect",null,false);
		hero[h].addSpell("ability_04/effects/model.mdf",null,false);
		hero[h].addSpell("projectile/projectile.entity",null,true);
		hero[h].addAlt("Frost Rider","Alt","alt");
		hero[h].spell[0].body[1] = "icon='../alt/ability_01/icon.tga'";
		hero[h].spell[2].body[1] = "icon='../alt/ability_02/icon.tga'";
		hero[h].spell[4].body[1] = "icon='../alt/ability_04/icon.tga'";
		hero[h += 1] = new Hero("Pollywog Priest","pollywogpriest");
		hero[h].alt[0] = "pollywogpriest";
		hero[h].addAlt("Pollywog Chieftain","Chieftain","alt");
		hero[h += 1] = new Hero("Predator","predator");
		hero[h].addAlt("Bunny Predator","Alt","alt");
		hero[h += 1] = new Hero("Puppet Master","puppetmaster");
		hero[h += 1] = new Hero("Pyromancer","pyromancer");
		hero[h].addAlt("Female Pyromancer","Female","alt");
		hero[h += 1] = new Hero("Rampage","rampage");
		hero[h].editmodel = true;
		hero[h].addSpell("ability_01/effects/charge.effect",null,false);
		hero[h].addSpell("ability_01/effects/charge_magic.effect",null,false);
		hero[h].addSpell("ability_01/effects/warp.effect",null,false);
		hero[h].addAlt("Unicorn Rampage","Alt","alt");
		hero[h].spell[0].filepath[1] = "alt/ability_1/charge.effect";
		hero[h].spell[1].filepath[1] = "alt/ability_1/charge_magic.effect";
		hero[h].spell[2].filepath[1] = "alt/ability_1/warp.effect";
		hero[h].addAlt("Rainbow Rampage","Alt2","alt2");
		hero[h].spell[0].filepath[2] = "alt2/ability_1/charge.effect";
		hero[h].spell[1].filepath[2] = "alt2/ability_1/charge_magic.effect";
		hero[h].spell[2].filepath[2] = "alt2/ability_1/warp.effect";
		hero[h += 1] = new Hero("Revenant","revenant");
		hero[h].addSpell("ability_01/effects/impact.effect",null,false);
		hero[h].addSpell("ability_01/effects/state_buff.effect",null,false);
		hero[h].addSpell("ability_01/effects/state_slow.effect",null,false);
		hero[h].addSpell("ability_02/effects/state.effect",null,false);
		hero[h].addSpell("ability_02/effects/state_aura.effect",null,false);
		hero[h].addSpell("ability_02/effects/state_debuff.effect",null,false);
		hero[h].addSpell("ability_03/effects/state.effect",null,false);
		hero[h].addSpell("ability_03/effects/passive.effect",null,false);
		hero[h].addSpell("ability_04/effects/state1.effect",null,false);
		hero[h].addSpell("ability_04/effects/state2.effect",null,false);
		hero[h].addSpell("ability_04/effects/state3.effect",null,false);
		hero[h].addSpell("projectile/projectile.entity",null,true);
		hero[h].addAlt("Wraith Revenant","Alt","alt");
		hero[h].spell[0].filepath[1] = "alt/ability_01/effects/impact.effect";
		hero[h].spell[1].filepath[1] = "alt/ability_01/effects/state_buff.effect";
		hero[h].spell[2].filepath[1] = "alt/ability_01/effects/state_slow.effect";
		hero[h].spell[3].filepath[1] = "alt/ability_02/effects/state.effect";
		hero[h].spell[4].filepath[1] = "alt/ability_02/effects/state_aura.effect";
		hero[h].spell[5].filepath[1] = "alt/ability_02/effects/state_debuff.effect";
		hero[h].spell[6].filepath[1] = "alt/ability_03/effects/state.effect";
		hero[h].spell[7].filepath[1] = "alt/ability_03/effects/passive.effect";
		hero[h].spell[8].filepath[1] = "alt/ability_04/effects/state1.effect";
		hero[h].spell[9].filepath[1] = "alt/ability_04/effects/state2.effect";
		hero[h].spell[10].filepath[1] = "alt/ability_04/effects/state3.effect";
		hero[h += 1] = new Hero("Rhapsody","rhapsody");
		hero[h].addSpell("ability_01/effects/1.effect",null,false);
		hero[h].addSpell("ability_01/effects/2.effect",null,false);
		hero[h].addSpell("ability_01/effects/3.effect",null,false);
		hero[h].addSpell("ability_01/effects/4.effect",null,false);
		hero[h].addSpell("ability_01/effects/5.effect",null,false);
		hero[h].addSpell("ability_01/effects/impact.effect",null,false);
		hero[h].addSpell("ability_01/effects/impact_self.effect",null,false);
		hero[h].addSpell("ability_01/effects/state_enemy.effect",null,false);
		hero[h].addSpell("ability_01/effects/state_self.effect",null,false);
		hero[h].addSpell("ability_02/effects/aoe.effect",null,false);
		hero[h].addSpell("ability_02/effects/state_ally.effect",null,false);
		hero[h].addSpell("ability_02/effects/state_ally_gadget.effect",null,false);
		hero[h].addSpell("ability_02/effects/state_enemy.effect",null,false);
		hero[h].addSpell("ability_02/effects/state_enemy_gadget.effect",null,false);
		hero[h].addSpell("ability_03/effects/state.effect",null,false);
		hero[h].addSpell("ability_04/effects/cast.effect",null,false);
		hero[h].addSpell("ability_04/effects/state.effect",null,false);
		hero[h].addSpell("ability_04/effects/state_self.effect",null,false);
		hero[h].addSpell("projectile/projectile.entity",null,true);
		hero[h].addAlt("Death Metal Rhapsody","Alt","alt");
		hero[h].spell[0].filepath[1] = "alt/ability_01/effects/1.effect";
		hero[h].spell[1].filepath[1] = "alt/ability_01/effects/2.effect";
		hero[h].spell[2].filepath[1] = "alt/ability_01/effects/3.effect";
		hero[h].spell[3].filepath[1] = "alt/ability_01/effects/4.effect";
		hero[h].spell[4].filepath[1] = "alt/ability_01/effects/5.effect";
		hero[h].spell[5].filepath[1] = "alt/ability_01/effects/impact.effect";
		hero[h].spell[6].filepath[1] = "alt/ability_01/effects/impact_self.effect";
		hero[h].spell[7].filepath[1] = "alt/ability_01/effects/state_enemy.effect";
		hero[h].spell[8].filepath[1] = "alt/ability_01/effects/state_self.effect";
		hero[h].spell[9].filepath[1] = "alt/ability_02/effects/aoe.effect";
		hero[h].spell[10].filepath[1] = "alt/ability_02/effects/state_ally.effect";
		hero[h].spell[11].filepath[1] = "alt/ability_02/effects/state_ally_gadget.effect";
		hero[h].spell[12].filepath[1] = "alt/ability_02/effects/state_enemy.effect";
		hero[h].spell[13].filepath[1] = "alt/ability_02/effects/state_enemy_gadget.effect";
		hero[h].spell[14].filepath[1] = "alt/ability_03/effects/state.effect";
		hero[h].spell[15].filepath[1] = "alt/ability_04/effects/cast.effect";
		hero[h].spell[16].filepath[1] = "alt/ability_04/effects/state.effect";
		hero[h].spell[17].filepath[1] = "alt/ability_04/effects/state_self.effect";
		hero[h += 1] = new Hero("Sand Wraith","sand_wraith");
		hero[h += 1] = new Hero("Scout","scout");
		hero[h].addAlt("Mist Runner Scout","Alt","alt");
		hero[h += 1] = new Hero("Shadowblade","shadowblade");
		hero[h].addSpell("ability_01/effects/sword_state.effect",null,false);
		hero[h].addSpell("ability_02/effects/trail.effect",null,false);
		hero[h].addSpell("effects/int_body.effect",null,false);
		hero[h].addSpell("effects/agi_body.effect",null,false);
		hero[h].addSpell("ability_01/effects/shadowq/model.mdf",null,false);
		hero[h].addSpell("ability_02/effects/shadoww/model.mdf",null,false);
		hero[h].addSpell("ability_03/effects/shadowe/model.mdf",null,false);
		hero[h].addAlt("Corporeal shadowblade","Alt","alt");
		hero[h += 1] = new Hero("Silhouette","silhouette");
		hero[h].addSpell("ability_02/projectile.entity",null,true);
		hero[h].addSpell("ability_04/effects/cast.effect",null,false);
		hero[h].addAlt("White Locus Silhouette","Alt","alt");
		hero[h].spell[1].filepath[1] = "alt/ability_04/cast.effect";
		hero[h += 1] = new Hero("Slither","ebulus");
		hero[h].addAlt("Mslvy Slither","Alt","alt");
		hero[h += 1] = new Hero("Soul Reaper","helldemon");
		hero[h].addSpell("projectile/projectile.entity",null,true);
		hero[h].addAlt("Grim Reaper","Alt","alt");
		hero[h].spell[0].filepath[1] = "alt/effects/projectile/projectile.entity";
		hero[h += 1] = new Hero("Soulstealer","soulstealer");
		hero[h].addAlt("Raven Soulstealer","Alt","alt");
		hero[h += 1] = new Hero("Succubus","succubis");
		hero[h].addAlt("Naughty Succubus","Naughty","alt_1");
		hero[h += 1] = new Hero("Swiftblade","hiro");
		hero[h].alt[0] = "hiro";
		hero[h].addAlt("Sachi Swiftblade","Alt","alt");
		hero[h].addAlt("Ronin Swiftblade","Alt2","alt2");
		hero[h += 1] = new Hero("Tempest","tempest");
		hero[h].addAlt("Mystic Tempest","Alt","alt");
		hero[h += 1] = new Hero("The Chipper","chipper");
		hero[h].addSpell("projectile/projectile.entity",null,true);
		hero[h].addAlt("Mr. Chipper","Alt","alt");
		hero[h += 1] = new Hero("The Dark Lady","vanya");
		hero[h += 1] = new Hero("The Gladiator","gladiator");
		hero[h].addSpell("ability_02/ability.entity","icon='icon.tga'",false);
		hero[h].addSpell("ability_02/effects/cast.effect",null,false);
		hero[h].addSpell("ability_02/effects/impact.effect",null,false);
		hero[h].addSpell("ability_04/ability.entity","icon='icon.tga'",false);
		hero[h].addSpell("ability_04/projectile.entity",null,true);
		//hero[h].addSpell("ability_04/projectile_chatiot.entity"," modelscale='1.0' model='/shared/models/invis.mdf' > </projectile>","projectile","<projectile name='Projectile_Gladiator_Ability4_Chariot'");
		//hero[h].addSpell("ability_04/projectile_horse.entity"," modelscale='1.0' model='/shared/models/invis.mdf' > </projectile>","projectile","<projectile name='Projectile_Gladiator_Ability4_Horse'");
		hero[h].addSpell("ability_01/effects/gate.effect",null,false);
		hero[h].addSpell("ability_01/effects/dust.effect",null,false);
		hero[h].addSpell("ability_04/effects/area.effect",null,false);
		hero[h].addAlt("Adventure Gladiator","Alt","alt");
		hero[h].spell[0].filepath[1] = null;
		hero[h].spell[1].filepath[1] = null;
		hero[h].spell[2].filepath[1] = null;
		hero[h].spell[3].filepath[1] = null;
		//hero[h].spell[5].filepath[1] = null;
		//hero[h].spell[6].filepath[1] = null;
		hero[h].spell[5].filepath[1] = null;
		hero[h].spell[6].filepath[1] = null;
		hero[h].spell[7].filepath[1] = "alt/ability_04/effects/smash.effect";
		hero[h].addAlt("Gladius Beardicus","Alt2","alt2");
		hero[h].spell[0].body[2] = "icon='../alt2/ability_02/icon.tga'";
		hero[h].spell[3].body[2] = "icon='../alt2/ability_04/icon.tga'";
		//hero[h].spell[5].filepath[2] = null;
		//hero[h].spell[6].filepath[2] = null;
		hero[h += 1] = new Hero("The Madman","scar");
		hero[h].addSpell("ability_1/effects/cast.effect",null,false);
		hero[h].addSpell("ability_1/effects/castback.effect",null,false);
		hero[h].addSpell("ability_1/effects/state.effect",null,false);
		hero[h].addAlt("Nightmare Madman","Scary","alt");
		hero[h].spell[0].filepath[1] = "alt/ability_1/effects/cast.effect";
		hero[h].spell[1].filepath[1] = "alt/ability_1/effects/castback.effect";
		hero[h].spell[2].filepath[1] = "alt/ability_1/effects/state.effect";
		hero[h += 1] = new Hero("Thunderbringer","kunas");
		hero[h].alt[0] = "kunas";
		hero[h].addAlt("Thor Thunderbringer","Alt","alt");
		hero[h += 1] = new Hero("Torturer","xalynx");
		hero[h].addAlt("Dominatrix Torturer","Alt","alt");
		hero[h += 1] = new Hero("Tremble","tremble");
		hero[h].addAlt("Quadropod Tremble","Alt","alt");
		hero[h += 1] = new Hero("Tundra","tundra");
		hero[h].addSpell("ability_01/effects/trail.effect",null,false);
		hero[h].addAlt("Mountain Tundra","Alt","alt");
		hero[h].spell[0].filepath[1] = "alt/ability_01/effects/trail.effect";
		hero[h += 1] = new Hero("Valkyrie","valkyrie");
		hero[h].addSpell("ability_02/projectile.entity",null,true);
		hero[h].addSpell("projectile/projectile.entity",null,true);
		hero[h].addAlt("Legenadry Valkyrie","Alt","alt");
		hero[h].spell[0].filepath[1] = null;
		hero[h].spell[1].filepath[1] = null;
		hero[h].addAlt("Fallen Valkyrie","Alt2","alt2");
		hero[h += 1] = new Hero("Vindicator","vindicator");
		hero[h].addSpell("projectile/projectile.entity",null,true);
		hero[h].addAlt("Vigilante Vindicator","Alt","alt");
		hero[h += 1] = new Hero("Voodoo Jester","voodoo");
		hero[h += 1] = new Hero("War Beast","wolfman");
		hero[h += 1] = new Hero("Wildsoul","yogi");
		hero[h += 1] = new Hero("Witch Slayer","witch_slayer");
		hero[h].addSpell("projectile/projectile.entity",null,true);
		hero[h].addAlt("Pimp Slayer","Pimp","alt");
		hero[h += 1] = new Hero("Wretched Hag","babayaga");
		hero[h].editmodel = true;
		hero[h].addAlt("Wretched Hottie","Alt","alt");
		hero[h += 1] = new Hero("Zephyr","zephyr");
		hero[h].addAlt("Snow Zephyr","Alt","alt");
		hero[h].addAlt("Turkey Zephyr","Alt2","alt2");
		//Announcers
		announcer[a += 1] = new Announcer("Default Pack","Standard","default","arcade_text");
		announcer[a].soundmove = true;
		for(int i = 0;i <= 37;i++) {
			announcer[a].sound[i] = announcer[a].sound[i].replaceAll("/default/","/");
		}
		announcer[a += 1] = new Announcer("Flamboyant Pack","Flamboyant","flamboyant","unicorn");
		announcer[a].sound[announcer[a].startgame] = announcer[a].sound[announcer[a].startgame].replace("/"+announcer[a].voice+"/","/");
		announcer[a += 1] = new Announcer("Pudding Pack","Female","female","arcade_text");
		announcer[a += 1] = new Announcer("Badass Pack","Badass","ballsofsteel","balls");
		announcer[a += 1] = new Announcer("British Gentlemen Pack","British","english","english");
		announcer[a].sound[announcer[a].zorgath] = announcer[a].sound[announcer[a].zorgath].replace("/"+announcer[a].voice+"/","/");
		announcer[a].sound[announcer[a].transmutantstein] = announcer[a].sound[announcer[a].transmutantstein].replace("/"+announcer[a].voice+"/","/");
		announcer[a += 1] = new Announcer("BreakyCPK Pack","BreakyCPK","breakycpk","arcade_text");
		announcer[a].sound[announcer[a].zorgath] = announcer[a].sound[announcer[a].zorgath].replace("/"+announcer[a].voice+"/","/");
		announcer[a].sound[announcer[a].transmutantstein] = announcer[a].sound[announcer[a].transmutantstein].replace("/"+announcer[a].voice+"/","/");
		//announcer[a=sell] = new Announcer("Seductive Female Pack","Sell","sell","arcade_text");
		//Taunts
		taunt[t += 1] = new Taunt("Taunt","standard");
		taunt[t].file = "shared/effects/drains/manaburnimpact.effect";
		taunt[t += 1] = new Taunt("Gore Taunt","gib");
		taunt[t += 1] = new Taunt("Too bad Taunt","toobad");
		taunt[t += 1] = new Taunt("Baby Taunt","cryingbaby");
		taunt[t += 1] = new Taunt("Heavy Metal Taunt","hellbourne");
		taunt[t += 1] = new Taunt("Fist of Sol Taunt","fist");
	}
	public void save() {
		try {
			out = new FileOutputStream(file);
		} catch (FileNotFoundException e) {
		}
		try {
			config.store(out,"This file belongs to Customization of Newerth and before going any further I would recommended you to read the Terms of Service");
		} catch (IOException e) {
			JOptionPane.showMessageDialog(null,"Could not export config data, please check your promissions.");
			System.exit(0);
		}
	}
	public String getValue(String property,String value) {
		String re;
		re = config.getProperty(property);
		if(re == null) {
			config.setProperty(property,value);
		}
		else {
			value = re;
		}
		return value;
	}
	public int getValue(String property,int value) {
		String re;
		re = config.getProperty(property);
		if(re == null) {
			config.setProperty(property,""+value);
		}
		else {
			value = Integer.parseInt(re);
		}
		return value;
	}
	public boolean getValue(String property,boolean value) {
		String re;
		re = config.getProperty(property);
		if(re == null) {
			config.setProperty(property,""+value);
		}
		else {
			value = Boolean.parseBoolean(re);
		}
		return value;
	}
	
	Main main;
	int state = 0;
	int projectile = 1;
	String s2 = '"'+"";
	
	String version = "1.16.9";
	String versionstate = "Beta";
	String date = "18/12/11";
	
	String credit = "Xplitter	-	For teaching me the basic's about mod applying." +
					"\nsaTo	-	For his All alter. I learned from his code." +
					"\nSHiRKiT	-	For helping me with the updating method & lending me his code, also for his cooperation the HoNMoDManager" +
					"\nSoulBeaver	-	For helping me with some Java and keeping me on track." +
					"\nLanboost	-	For teaching me Java & being just how awesome as he is!" +
					"\nRean	-	For explaining how the models/animations work." +
					"\nCoke	-	For keeping my brain work properly." +
					"\nS2Games	-	For launching Heroes of Newerth.";

	Properties config;
	Image image;
	FileInputStream in;
	FileOutputStream out;
	File file;
	String name = "Customization of Newerth";
	String link = "http://dl.dropbox.com/u/38414202/Java/Customization_of_Newerth/";
	String App_Data = System.getenv("APPDATA");
	
	int h = -1;
	Hero[] hero = new Hero[100];
	int a = -1;
	int sell = 6;
	Announcer[] announcer = new Announcer[10];
	int t = -1;
	Taunt[] taunt = new Taunt[10];
}