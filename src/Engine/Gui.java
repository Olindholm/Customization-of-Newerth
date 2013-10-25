package Engine;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import javax.swing.ButtonGroup;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JList; 
import javax.swing.JOptionPane;
import javax.swing.JProgressBar;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import Main.Main;

import SimpleJava.YUi;

import Lindholm.LLMethod;
import Lindholm.LLBoolean;
import Lindholm.com.LLExplorer;
import Lindholm.com.LLFile;
import Lindholm.ui.LLCheckBox;
import Lindholm.ui.LLFrame;
import Lindholm.ui.LLPanel;

public class Gui extends LLFrame implements ListSelectionListener, ActionListener, Runnable, KeyListener, FocusListener {
	//Variables;
	private	Main				main;
	private Config				config;
	private JList				list;
	private	Thread				run;
	private	int					command;
	private	int					index	=	0;
	//Upper tab items;
	private	ButtonGroup[]		avatar_group = new ButtonGroup[10];
	private	JRadioButton[][]	avatar_radio = new JRadioButton[10][10];
	private	JLabel[]			avatar_label = new JLabel[10];
	private	JCheckBox[]			indic_check = new JCheckBox[12];
	private	JTextField[][]		indic_text = new JTextField[6][3];
	private	ButtonGroup[]		announcer_group = new ButtonGroup[10];
	private	ButtonGroup[]		taunt_group = new ButtonGroup[10];
	//Avatar tab items;
	private JCheckBox			avatar_alt;
	private LLCheckBox			avatar_all_alt;
	//Lower tab items;
	private	JTextField			config_name;
	private	JComboBox			config_dir; private String[] fdirs = {"File Folder","Desktop","User Folder","Mod Folder"}; private String[] rdirs = new String[4];
	private	String[]			checks = {"Avatars","Shop","Indicator","Announcer","Music","Taunts"};
	private	JCheckBox[]			config_check = new JCheckBox[checks.length];
	private JButton				config_compile;
	private JProgressBar		config_bar;
	public	JTextArea[]			command_log = new JTextArea[3];
	//Constructors;
	public Gui(Main main) {
		rdirs[0] = main.EngSrc;
		rdirs[1] = LLExplorer.getDesktop();
		rdirs[2] = LLExplorer.getUser();
		rdirs[3] = main.config.property.getValue("hondir","")+File.separator+"game"+File.separator+"mods"+File.separator;
		//Saving the main parameter;
		this.main = main;
		config = main.config;
		//Forming the frame;
		setTitle(main.EngNme);
		try {
			setIconImage(main.images.getImage("icon"));
		} catch (FileNotFoundException e) {
			//This Exception shouldn't get caught;
		}
		setResizable(false);
		LLPanel panel = new LLPanel(this,915,555);
		//Forming the default tabs;
		JTabbedPane tab = new JTabbedPane();
		tab.setPreferredSize(new Dimension(919,350));
		//Forming the unit tab;
		LLPanel panel_unit = new LLPanel(1000,323);
		//Adding items to the unit panel;
		DefaultListModel listModel = new DefaultListModel();
		listModel.addElement("Courier");
		for(int i = 1;i <= config.h;i++) {
			listModel.addElement(config.hero.get(i).name[0]);
		}
		panel_unit.add(YUi.JScrollPane(list = YUi.JList(listModel),150,314,JScrollPane.HORIZONTAL_SCROLLBAR_NEVER,JScrollPane.VERTICAL_SCROLLBAR_ALWAYS),5,5);
		list.setSelectedIndex(index);
		list.addListSelectionListener(this);
		//Froming the inner unit tabs;
		JTabbedPane tab_unit = new JTabbedPane();
		tab_unit.setPreferredSize(new Dimension(748,314));
		//Forming the avatar tab;
		LLPanel panel_avatar = new LLPanel(744,287);
		panel_avatar.setRepaint(this,LLMethod.getMethod(this,"avatar",Graphics.class));
		//Adding items to the avatar panel;
		for(int i = 0;i <= 9;i++) {
			avatar_group[i] = new ButtonGroup();
			panel_avatar.add(avatar_label[i] = new JLabel(i+""),380,5+32+5+24*i);
			for(int ii = 0; ii <= 9;ii += 1) {
				avatar_group[i].add(avatar_radio[i][ii] = new JRadioButton(""));
				avatar_radio[i][ii].setActionCommand(""+ii);
				panel_avatar.add(avatar_radio[i][ii],5+6+(32+5)*i,5+32+5+24*ii);
			}
		}
		for(int i = 0;i <= 9;i++) {
			avatar_radio[i][i].setSelected(true);
		}
		panel_avatar.add(new JLabel("Set this hero to:"),609,5+5);
		panel_avatar.add(YUi.JButton("Default",145,25,this,-1),583+1+5,5+5+15+5);
		panel_avatar.add(YUi.JButton("Standard",145,25,this,0),583+1+5,5+5+15+5+25+5);
		panel_avatar.add(YUi.JButton("1",70,25,this,1),583+1+5,5+5+15+5+25+5+25+5);	panel_avatar.add(YUi.JButton("2",70,25,this,2),583+1+5+70+5,5+5+15+5+25+5+25+5); //3rd Size = 45;
		panel_avatar.add(avatar_alt = new JCheckBox("Enable alt"),609,5+5+15+5+25+5+25+5+25+5);
		avatar_alt.addActionListener(this);
		avatar_alt.setActionCommand("-55");
		panel_avatar.add(new JLabel("Set all heroes to:"),609,5+5+15+5+25+5+25+5+25+5+15+10);
		panel_avatar.add(YUi.JButton("Default",145,25,this,-11),583+1+5,5+5+15+5+25+5+25+5+25+5+15+5+15+10);
		panel_avatar.add(YUi.JButton("Standard",145,25,this,10),583+1+5,5+5+15+5+25+5+25+5+25+5+15+5+15+10+25+5);
		panel_avatar.add(YUi.JButton("1",70,25,this,11),583+1+5,5+5+15+5+25+5+25+5+25+5+15+5+15+10+25+5+25+5);	panel_avatar.add(YUi.JButton("2",70,25,this,12),583+1+5+70+5,5+5+15+5+25+5+25+5+25+5+15+5+15+10+25+5+25+5); //3rd Size = 45;
		panel_avatar.add(avatar_all_alt = new LLCheckBox("Enable alt"),609,5+5+15+5+25+5+25+5+25+5+15+5+15+10+25+5+25+5+25+5);
		avatar_all_alt.addActionListener(this);
		avatar_all_alt.setActionCommand("-56");
		//Adding the avatar panel to the avatar tab;
		tab_unit.addTab("Avatar",panel_avatar);
		//Forming the shop tab;
		LLPanel panel_shop = new LLPanel(744,287);
		panel_shop.setRepaint(this,LLMethod.getMethod(this,"shop",Graphics.class));
		//Adding items to the shop panel;
		String[] shopnames = {"Supplies","Accessors","Weapons","Relics","Legendary","Initiation","Supportive","Protective","Combative","Morth Atk"};
		for(int i = 0;i <= 9;i++) {
			panel_shop.add(YUi.JButton(shopnames[i],100,25,this,i),5+310+2+5,5+(25+3)*i);
		}
		//Adding the shop panel to the shop tab;
		tab_unit.addTab("Shop",panel_shop);
		//Forming the indicators tab;
		LLPanel panel_indicators = new LLPanel(744,287);
		panel_indicators.setRepaint(this,LLMethod.getMethod(this,"indicators",Graphics.class));
		//Adding items to the indicators panel;
		String[] ssssss = {"AoE range","Cast range","R","B","G",""};
		String[][] ssss = {{"127","0","0"},{"0","19","127"},{"0","127","14"},{"255","127","182"},{"64","64","64"},{"87","0","127"}};
		for(int i = 0;i <= 12/2-1;i++) {
			panel_indicators.add(new JLabel(ssssss[i]),5+6+(32+5)*6,5+32+5+24*i);
			for(int ii = 0;ii <= 2-1;ii++) {
				//panel_indicators.add(new JLabel(""+(ii+2*i)),5+6+(32+5)*i,5+32+5+24*ii);
				panel_indicators.add(indic_check[ii+2*i] = new JCheckBox(""),5+6+(32+5)*i,5+32+5+24*ii);
			}
			for(int ii = 0;ii <= 3-1;ii++) {
				panel_indicators.add(indic_text[i][ii] = new JTextField(config.property.getValue("color"+i+(ii+2),ssss[i][ii])),5+3+(32+5)*i,5+32+5+24*(ii+2));
				indic_text[i][ii].setPreferredSize(new Dimension(25+2,20));
				indic_text[i][ii].addFocusListener(this);
			}
		}
		//Adding the indicators panel to the indicators tab;
		tab_unit.addTab("Indicator",panel_indicators);
		//Finnishing the unit tabs;
		panel_unit.add(tab_unit,160,5-2);
		//Adding the unit panel to the unit tab;
		tab.addTab("Unit",panel_unit);
		//Forming the sound tab;
		LLPanel panel_sound = new LLPanel(1000,323);
		//Froming the inner unit tabs;
		JTabbedPane tab_sound = new JTabbedPane();
		tab_sound.setPreferredSize(new Dimension(748,314));
		//Forming the announcer tab;
		LLPanel panel_announcer = new LLPanel(744,287);
		panel_announcer.setRepaint(this,LLMethod.getMethod(this,"announcer",Graphics.class));
		//Adding items to the announcer panel;
		JRadioButton[][] announcer_radio = new JRadioButton[config.a+1][config.a+1];
		for(int i = 0;i <= config.a;i++) {
			announcer_group[i] = new ButtonGroup();
				panel_announcer.add(new JLabel(config.announcer.get(i).name),5+32+5+(32+5)*config.a,5+32+5+24*i);
			for(int ii = 0; ii <= config.a;ii += 1) {
				announcer_group[i].add(announcer_radio[i][ii] = new JRadioButton(""));
				announcer_radio[i][ii].setActionCommand(""+ii);
				panel_announcer.add(announcer_radio[i][ii],5+6+(32+5)*i,5+32+5+24*ii);
			}
		}
		for(int i = 0;i <= config.a;i++) {
			announcer_radio[i][config.property.getValue(config.announcer.get(i).voice,i)].setSelected(true);
		}
		//Adding the announcer panel to the announcer tab;
		tab_sound.addTab("Announcer",panel_announcer);
		//Forming the music tab;
		LLPanel panel_music = new LLPanel(744,287);
		//Adding items to the music panel;
		//Adding the music panel to the music tab;
		tab_sound.addTab("Music",panel_music);
		//Finnishing the sounds tabs;
		panel_sound.add(tab_sound,5,5-2);
		//Adding items to the sound panel;
		//Adding the sound panel to the sound tab;
		tab.addTab("Sound",panel_sound);
		//Forming the effect tab;
		LLPanel panel_effect = new LLPanel(1000,323);
		panel_effect.setRepaint(this,LLMethod.getMethod(this,"effect",Graphics.class));
		//Adding items to the effect panel;
		JRadioButton[][] taunt_radio = new JRadioButton[config.t+1][config.t+1];
		for(int i = 0;i <= config.t;i++) {
			taunt_group[i] = new ButtonGroup();
			panel_effect.add(new JLabel(config.taunt.get(i).name),5+32+5+(32+5)*config.t,5+32+5+24*i);
			for(int ii = 0; ii <= config.t;ii += 1) {
				taunt_group[i].add(taunt_radio[i][ii] = new JRadioButton(""));
				taunt_radio[i][ii].setActionCommand(""+ii);
				panel_effect.add(taunt_radio[i][ii],5+6+(32+5)*i,5+32+5+24*ii);
			}
		}
		for(int i = 0;i <= config.t;i++) {
			taunt_radio[i][config.property.getValue(config.taunt.get(i).key,i)].setSelected(true);
		}
		for(int i = 1;i <= config.t;i++) {
			taunt_radio[0][i].setEnabled(false);
		}
		//Adding the effect panel to the effect tab;
		tab.addTab("Effect",panel_effect);
		//Finnishing the default tabs;
		panel.add(tab,-2,-2);
		//Forming the command tabs;
		JTabbedPane tab2 = new JTabbedPane();
		tab2.setPreferredSize(new Dimension(919,(27+1+(15+5)*checks.length)+5+25+5));
		//Forming the config tab;
		LLPanel panel_config = new LLPanel(919,(1+(15+5)*checks.length)+5+25+5);
		//Adding items to the config panel;
		for(int i = 0;i <= checks.length-1;i++) {
			panel_config.add(config_check[i] = new JCheckBox(checks[i]),800,1+(15+5)*i);
			config_check[i].setSelected(config.property.getValue("config"+i,false));
		}
		//Diabled due not finnished;
		config_check[1].setEnabled(false);
		config_check[2].setEnabled(false);
		config_check[4].setEnabled(false);
		//continue;
		panel_config.add(new JLabel("Name:"),645,5);
		panel_config.add(config_name = YUi.JTextField("(CoN)",150,25),645,5+15+5);
		panel_config.add(new JLabel("Output Directory:"),645,5+15+5+25+5);
		panel_config.add(config_dir = YUi.JComboBox(fdirs,150,25),645,5+15+5+25+5+15+5);
		config_dir.setSelectedIndex(config.property.getValue("outdir",0));
		panel_config.add(config_compile = YUi.JButton("Compile",919-(12+645),25,this,55),645,(1+(15+5)*checks.length-1)+5);
		//If resources failed disable the compile button;
		if(main.config.resources == null) {
			config_compile.setEnabled(false);
		}
		panel_config.add(config_bar = YUi.JProgressBar(0,919-(12+645),25),645,(1+(15+5)*checks.length-1)+5);
		config_bar.setVisible(false);
		//Adding the config panel to the config tab;
		tab2.addTab("Config",panel_config);
		//Forming the changelog tab;
		LLPanel panel_changelog = new LLPanel(919,(1+(15+5)*checks.length)+5+25+5);
		//Adding items to the changelog panel;
		panel_changelog.add(YUi.JScrollPane(command_log[0] = new JTextArea("\n\nPress any key to download further changelog history."),919-10,((1+(15+5)*checks.length)+5+25+5)-10,JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED,JScrollPane.VERTICAL_SCROLLBAR_ALWAYS),5,5);
		command_log[0].setEditable(false);
		command_log[0].addKeyListener(this);
		command_log[0].setFont(new Font("courier new",Font.BOLD,12));
		//Adding the changelog panel to the changelog tab;
		tab2.addTab("Changelog",panel_changelog);
		//Forming the buglog tab;
		LLPanel panel_buglog = new LLPanel(919,(1+(15+5)*checks.length)+5+25+5);
		//Adding items to the buglog panel;
		panel_buglog.add(YUi.JScrollPane(command_log[1] = new JTextArea("Downloading..."),919-10,((1+(15+5)*checks.length)+5+25+5)-10,JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED,JScrollPane.VERTICAL_SCROLLBAR_ALWAYS),5,5);
		command_log[1].setEditable(false);
		command_log[1].setFont(new Font("courier new",Font.BOLD,12));
		//Adding the buglog panel to the buglog tab;
		tab2.addTab("Buglog",panel_buglog);
		//Forming the creditlog tab;
		LLPanel panel_creditlog = new LLPanel(919,(1+(15+5)*checks.length)+5+25+5);
		//Adding items to the creditlog panel;
		panel_creditlog.add(YUi.JScrollPane(command_log[2] = new JTextArea(main.credit),919-10,((1+(15+5)*checks.length)+5+25+5)-10,JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED,JScrollPane.VERTICAL_SCROLLBAR_ALWAYS),5,5);
		command_log[2].setEditable(false);
		command_log[2].setFont(new Font("courier new",Font.BOLD,12));
		//Adding the creditlog panel to the creditlog tab;
		tab2.addTab("Creditlog",panel_creditlog);
		//Finnishing the command tabs;
		panel.add(tab2,-2,(350-2)+5);
		//Just adding a little label of default information;
		JLabel il;
		panel.add(il = new JLabel(	"Version: "+main.EngVer+"."+config.property.getValue("minor",0) +
									" | Branch: "+ main.EngSte +
									" | Auther: Wiggy boy <Lindholm>"),
									5,(350-2)+5+(27+1+(15+5)*checks.length)+5+25+5);
		il.setEnabled(false);
		//Finnishing the frame;
		bag();
		//Loading first heroes attributes...;
		load(index);
		calcState();
	}
	public void avatar(Graphics g) {
		try {
			for(int i = 0;i <= config.hero.get(index).avatar;i++) {
				g.drawImage(main.images.getImage(config.hero.get(index).key[0]+i),5+(32+5)*i,5,32,32,null);
			}
			for(int i = config.hero.get(index).avatar+1;i <= 9;i++) {
				g.drawImage(main.images.getImage("error"),5+(32+5)*i,5,32,32,null);
			}
		} catch (FileNotFoundException e) {
			//This Exception shouldn't get caught;
		}
		g.setColor(new Color(99,130,191));
		g.drawRect(583,5+1,155,275);
	}
	public void shop(Graphics g) {
		g.setColor(new Color(99,130,191));
		g.drawRect(5,5,312-1,277-1);
		try {
			g.drawImage(main.images.getImage("shopbg"),6,6,310,275,null);
		} catch (FileNotFoundException e) {
			//This Exception shouldn't get caught;
		}
		g.drawRect(5+310+2+5+100+5,5,312-1,277-1);
		try {
			g.drawImage(main.images.getImage("shopbg"),1+5+310+2+5+100+5,1+5,310,275,null);
		} catch (FileNotFoundException e) {
			//This Exception shouldn't get caught;
		}
		for(int i = 0;i <= 4;i++) {
			for(int ii = 0;ii <= 4;ii++) {
				/*g.setColor(Color.black);
				g.drawRect(5+310+2+5+100+5+2+5+(32+2+5)*i,5+2+5+(32+2+23)*ii,34-1,34-1);
				g.setColor(Color.gray);
				g.fillRect(5+310+2+5+100+5+2+5+1+(32+2+5)*i,5+2+5+1+(32+2+23)*ii,32,32);*/
			}
		}
	}
	public void indicators(Graphics g) {
		try {
			g.drawImage(main.images.getImage("atk"),5+(32+5)*4,5,32,32,null);
			g.drawImage(main.images.getImage("exp"),5+(32+5)*5,5,32,32,null);
		} catch (FileNotFoundException e) {
			//This Exception shouldn't get caught;
		}
		for(int i = 0;i <= 4-1;i++) {
			try {
				g.drawImage(main.images.getImage(config.hero.get(index).key[0]+"s"+i),5+(32+5)*i,5,32,32,null);
			} catch (FileNotFoundException e) {
				try {
					g.drawImage(main.images.getImage("error"),5+(32+5)*i,5,32,32,null);
				} catch (FileNotFoundException e1) {
					//This Exception shouldn't get caught;
				}
			}
		}
		for(int i = 0;i <= 6-1;i++) {
			g.setColor(new Color(config.property.getValue("color"+i+2,0),config.property.getValue("color"+i+3,0),config.property.getValue("color"+i+4,0)));
			g.fillRect(5+4+(32+5)*i,5+32+5+24*5,24,4);
		}
		g.setColor(new Color(99,130,191));
		g.drawRect(583,5+1,155,275);
	}
	public void announcer(Graphics g) {
		for(int i = 0;i <= config.a;i++) {
			try {
				g.drawImage(main.images.getImage(config.announcer.get(i).voice),5+(32+5)*i,5,32,32,null);
			} catch (FileNotFoundException e) {
				//This Exception shouldn't get caught;
			}
		}
	}
	public void effect(Graphics g) {
		for(int i = 0;i <= config.t;i++) {
			try {
				g.drawImage(main.images.getImage(config.taunt.get(i).key),5+(32+5)*i,5,32,32,null);
			} catch (FileNotFoundException e) {
				//This Exception shouldn't get caught;
			}
		}
	}
	public void store(int index) {
		//Storing the avatar elements;
		for(int i = 0;i <= config.hero.get(index).avatar;i++) {
			config.property.setProperty(config.hero.get(index).key[0]+i,avatar_group[i].getSelection().getActionCommand());
		}
		//No neeed to store this, if it's changed it's saved in actionpreformed -55;
		//config.property.setProperty(config.hero.get(index).key[0],avatar_alt.isSelected()+"");
		//Saving the indicator elements;
		for(int i = 0;i <= config.hero.get(index).indicnum;i++) {
			int ii = config.hero.get(index).indic[i].key;
			config.property.setProperty("i"+config.hero.get(index).key[0]+ii,indic_check[ii].isSelected()+"");
		}
		//Saving the announcer elements;
		for(int i = 0;i <= config.a;i++) {
			config.property.setProperty(config.announcer.get(i).voice,announcer_group[i].getSelection().getActionCommand());
		}
		//Saving the taunt elements;
		for(int i = 0;i <= config.t;i++) {
			config.property.setProperty(config.taunt.get(i).key,taunt_group[i].getSelection().getActionCommand());
		}
		//Saving the config elements;
		for(int i = 0;i <= checks.length-1;i++) {
			config.property.setProperty("config"+i,config_check[i].isSelected()+"");
		}
		//Saving the outputdir;
		config.property.setProperty("outdir",config_dir.getSelectedIndex()+"");
		//Saving the data to the disk;
		config.property.save();
	}
	public void load(int index) {
		//Setting up which continent to be disabled and which to be enabled;
		for(int i = 0;i <= config.hero.get(index).avatar;i++) {
			for(int ii = 0;ii <= config.hero.get(index).avatar;ii++) {
				avatar_radio[i][ii].setEnabled(true);
			}
			for(int ii = config.hero.get(index).avatar+1;ii <= 9;ii++) {
				avatar_radio[i][ii].setEnabled(false);
			}
		}
		for(int i = config.hero.get(index).avatar+1;i <= 9;i++) {
			for(int ii = 0;ii <= 9;ii++) {
				avatar_radio[i][ii].setEnabled(false);
			}
		}
		//Setting the right avatar id's to the right radios;
		for(int i = 0;i <= config.hero.get(index).avatar;i++) {
			avatar_radio[i][config.property.getValue(config.hero.get(index).key[0]+i,i)].setSelected(true);
		}
		//Setting the right names to the right radio cords;
		for(int i = 0;i <= 9;i++) {
			avatar_label[i].setText(config.hero.get(index).name[i]);
		}
		//setting up the alt state;
		avatar_alt.setEnabled(true);
		avatar_alt.setSelected(config.property.getValue(config.hero.get(index).key[0],true));
		if(config.hero.get(index).avatar == 0) {
			avatar_alt.setEnabled(false);
		}
		//Setting right checkboxes for indicators
		for(int i = 0;i <= 12-1;i++) {
			indic_check[i].setEnabled(false);
			indic_check[i].setSelected(false);
		}
		for(int i = 0;i <= config.hero.get(index).indicnum;i++) {
			int ii = config.hero.get(index).indic[i].key;
			indic_check[ii].setEnabled(true);
			indic_check[ii].setSelected(config.property.getValue("i"+config.hero.get(index).key[0]+ii,true));
		}
		//Repainting the whole gui(so the icons will update);
		repaint();
	}
	//Tristate CheckBox()
	public void calcState() {
		int state = 0;
		for(int i = 0;i <= config.h;i++) {
			boolean value = config.property.getValue(config.hero.get(i).key[0],true);
			if(value == false) {
				if(state > 0) {
					avatar_all_alt.setState(LLCheckBox.HALF);
					return;
				}
				state -= 1;
				continue;
			}
			else {
				if(state < 0) {
					avatar_all_alt.setState(LLCheckBox.HALF);
					return;
				}
				state += 1;
				continue;
			}
		}
		if(state == config.h+1) {
			avatar_all_alt.setState(LLCheckBox.CHECKED);
			return;
		}
		avatar_all_alt.setState(LLCheckBox.UNCHECKED);
		return;
	}
	public void comp(boolean state,int max) {
		config_compile.setVisible(LLBoolean.getOpposite(state));
		config_bar.setMaximum(max);
		config_bar.setValue(0);
		config_bar.setVisible(state);
	}
	@Override
	public void run() {
		//Checking if compile buttons has been pressed;
		if(command == 55) {
			//Saving unsaved data;
			store(index);
			//Setting up progress bar;
			comp(true,config.h*2+config.a+config.t);
			String mod = "";
			String info = "";
			//Checking if units are selected;
			if(config_check[0].isSelected() == true) {
				//Generating units;
				try {
					config_bar.setValue(config_bar.getValue()+1);
					mod += main.config.hero.get(0).getCourierXAML(main.config.property,main.config.resources);
				} catch (NullPointerException e) {
					main.log(config.hero.get(0).key[0]+":");
					main.report(12,e);
				} catch (StringIndexOutOfBoundsException e) {
					main.log(config.hero.get(0).key[0]+":");
					main.report(13,e);
				}
				//Generating from 1-config.h due 0 is couriers and uses a different generation;
				for(int i = 1;i <= config.h;i++) {
					try {
						config_bar.setValue(config_bar.getValue()+1);
						mod += main.config.hero.get(i).getXAML(main.config.property,main.config.resources);
					} catch (NullPointerException e) {
						main.log(config.hero.get(i).key[0]+":");
						main.report(12,e);
						continue;
					} catch (StringIndexOutOfBoundsException e) {
						main.log(config.hero.get(i).key[0]+":");
						main.report(13,e);
						continue;
					}
				}
				info += "*Avatars;";
			}
			else {
				config_bar.setValue(config_bar.getValue()+config.h);
			}
			if(config_check[2].isSelected() == true) {
				mod +=	"	<copyfile name='shared/materials/cic.material' source='cic.material' />\n" +
						"	<copyfile name='shared/materials/cic.tga' source='cic.tga' />\n";
				for(int i = 1;i <= config.h;i++) {
					try {
						config_bar.setValue(config_bar.getValue()+1);
						mod += main.config.hero.get(i).getIndicatorXAML(main.config.property,main.config.resources);
					} catch (NullPointerException e) {
						main.log("i"+config.hero.get(i).key[0]+":");
						main.report(12,e);
						continue;
					} catch (StringIndexOutOfBoundsException e) {
						main.log("i"+config.hero.get(i).key[0]+":");
						main.report(13,e);
						continue;
					}
				}
				info += "*Indicators;";
			}
			else {
				config_bar.setValue(config_bar.getValue()+config.h);
			}
			if(config_check[3].isSelected() == true) {
				boolean x = false;	
				for(int i = 0; i <= config.a;i++) {
					if(config.property.getValue(config.announcer.get(i).voice,i) != i) {
						x = true;
					}
				}
				if(x == true) {
					info += "*Announcers;";
					//Installing default package & normal arcade text
					for(int i = 0;i <= 37;i++) {
						mod += "	<copyfile name='"+config.announcer.get(0).sound[i].replace("/announcer/","/announcer/default/")+"' source='"+config.announcer.get(0).sound[i]+"' overwrite='yes' fromresource='true' />\n";
					}
					for(int i = 0;i <= 19;i++) {
						mod += "	<copyfile name='"+config.announcer.get(0).text[i].replace("/arcade_text/","/normal/")+".mdf' source='"+config.announcer.get(0).text[i]+".mdf' overwrite='yes' fromresource='true' />\n";
						mod += "	<copyfile name='"+config.announcer.get(0).text[i].replace("/arcade_text/","/normal/")+".model' source='"+config.announcer.get(0).text[i]+".model' overwrite='yes' fromresource='true' />\n";
						mod += "	<copyfile name='"+config.announcer.get(0).text[i].replace("/arcade_text/","/normal/")+".clip' source='"+config.announcer.get(0).text[i]+".clip' overwrite='yes' fromresource='true' />\n";
					}
					mod += "	<copyfile name='ui/common/models/normal/arrow2.clip' source='ui/common/models/arcade_text/arrow2.clip' overwrite='yes' fromresource='true' />\n";
					mod += "	<copyfile name='ui/common/models/normal/material.material' source='ui/common/models/arcade_text/material.material' overwrite='yes' fromresource='true' />\n";
					mod += "	<editfile name='ui/common/models/normal/material.material'><findall><![CDATA[color.tga]]></findall><replace><![CDATA[/ui/common/models/arcade_text/color.tga]]></replace></editfile>";
					mod += "	<copyfile name='ui/common/models/normal/material2.material' source='ui/common/models/arcade_text/material2.material' overwrite='yes' fromresource='true' />\n";
					mod += "	<editfile name='ui/common/models/normal/material.material'><findall><![CDATA[color.tga]]></findall><replace><![CDATA[/ui/common/models/arcade_text/color.tga]]></replace></editfile>";
					//Generating announcer;
					for(int i = 0; i <= config.a;i++) {
						mod += config.announcer.get(i).getXAML(config.announcer,i,config.property.getValue(config.announcer.get(i).voice,i));
					}
				}
			}
			else {
				config_bar.setValue(config_bar.getValue()+config.a);
			}
			//Checking if taunts are selected;
			if(config_check[5].isSelected() == true) {
				info += "*Taunts;";
				//Generating taunts;
				for(int i = 0;i <= config.t;i++) {
					if(main.config.property.getValue(main.config.taunt.get(i).key,i) != i) {
						mod += main.config.taunt.get(i).getXAML(main.config.taunt.get(main.config.property.getValue(main.config.taunt.get(i).key,i)));
					}
				}
			}
			else {
				config_bar.setValue(config_bar.getValue()+config.t);
			}
			//If any of the above fields have been true of modification then compile the data;
			if(info.equals("") != true) {
				mod =  ("<?xml version='1.0' encoding='UTF-8'?>\n" +
						"<modification\n" +
						"	application='Heroes of Newerth'\n" +
						"	appversion='*'\n" +
						"	mmversion='1.3.6'\n" +
						"	name='Customization of Newerth'\n" +
						"	version='"+main.EngVer+"."+config.property.getValue("minor",0)+"'\n" +
						"	author='(CoN).jar'\n" +
						"	weblink='http://forums.heroesofnewerth.com/showthread.php?t=338461'\n" +
						"	description='"+info+"'\n" +
						">\n" +
						mod+"</modification>").replace("'","\"");
			    try {
			    	//making the zipfile;
			    	ZipOutputStream out = new ZipOutputStream(new FileOutputStream(LLFile.getNewFile(rdirs[config_dir.getSelectedIndex()]+config_name.getText()+".honmod")));
					//Compiling the icon;
					out.putNextEntry(new ZipEntry("icon.png"));
					out.write(main.images.getImageBytes("icon"));
					out.closeEntry();
					//Compiling the passive.effect file(IF INDICATOR HAS BEEN COMPILED);
					if(config_check[2].isSelected() == true) {
						//Printing passive.effect,cic.material,cic.tga
						out.putNextEntry(new ZipEntry("passive.effect"));
						for(int i = 0;i <= config.passive.length()-1;i++) {
							out.write(((int)config.passive.charAt(i)));
						}
						out.putNextEntry(new ZipEntry("cic.material"));
						for(int i = 0;i <= config.material.length()-1;i++) {
							out.write(((int)config.material.charAt(i)));
						}
						out.putNextEntry(new ZipEntry("cic.tga"));
						out.write(main.images.getImageBytes("cic.tga"));
						out.closeEntry();
					}
					out.closeEntry();
					//Compiling the mod.xml file;
					out.putNextEntry(new ZipEntry("mod.xml"));
					for(int i = 0;i < mod.length();i++) {
						out.write(mod.charAt(i));
					}
					out.closeEntry();
					//Compiling other optional files;
					//finishing the compilation;
					out.close();
					JOptionPane.showMessageDialog(null,"Great Success!");
				} catch (FileNotFoundException e) {
					main.report(14,e);
				} catch (IOException e) {
					main.report(7,e);
				}
			}
			else {
				JOptionPane.showMessageDialog(null,"Nothing to compile!");
			}
			//restoring the default gui look;
			comp(false,1);
		}
		//Checking if "Set this hero:" buttons have been pressed;
		else if(command >= -1 && command <= 2) {
			//Default;
			if(command == -1) {
				for(int i = 0;i <= main.config.hero.get(index).avatar;i++) {
					main.config.property.setProperty(main.config.hero.get(index).key[0]+i,i+"");
				}
			}
			//Any other...
			else {
				if(main.config.hero.get(index).avatar >= command) {
					for(int i = 0;i <= main.config.hero.get(index).avatar;i++) {
						main.config.property.setProperty(main.config.hero.get(index).key[0]+i,command+"");
					}
				}
			}
			load(index);
		}
		//Checking if "Set all heroes:" buttons have been pressed;
		else if(command >= -11 && command <= 12) {
			//Defualt
			if(command == -11) {
				for(int i = 0;i <= main.config.h;i++) {
					for(int ii = 0;ii <= main.config.hero.get(i).avatar;ii++) {
						main.config.property.setProperty(main.config.hero.get(i).key[0]+ii,ii+"");
					}
				}
			}
			//Any other...
			else {
				for(int i = 0;i <= main.config.h;i++) {
					if(main.config.hero.get(i).avatar >= command-10) {
						for(int ii = 0;ii <= main.config.hero.get(i).avatar;ii++) {
							main.config.property.setProperty(main.config.hero.get(i).key[0]+ii,(command-10)+"");
						}
					}
				}
			}
			load(index);
		}
		//Checking if "Alt" box have been pressed;
		else if(command == -55) {
			//Saving the alt state;
			config.property.setProperty(config.hero.get(index).key[0],avatar_alt.isSelected()+"");
			calcState();
		}
		//Checking if "all Alt" box have been pressed;
		else if(command == -56) {
			int state = avatar_all_alt.getState();
			if(state == LLCheckBox.UNCHECKED) {
				for(int i = 0;i <= config.h;i++) {
					config.property.setProperty(config.hero.get(i).key[0],true+"");
				}
				avatar_all_alt.setState(LLCheckBox.CHECKED);
			}
			//if state = half or checked make unchecked;
			else {
				for(int i = 0;i <= config.h;i++) {
					config.property.setProperty(config.hero.get(i).key[0],false+"");
				}
				avatar_all_alt.setState(LLCheckBox.UNCHECKED);
			}
			avatar_alt.setSelected(config.property.getValue(config.hero.get(index).key[0],true));
		}
	}
	@Override
	public void actionPerformed(ActionEvent e) {
		command = Integer.parseInt(e.getActionCommand());
		run = new Thread(this);
		run.start();
	}
	@Override
	public void valueChanged(ListSelectionEvent arg0) {
		store(index);
		index = list.getAnchorSelectionIndex();
		load(index);
	}
	@Override
	public void keyPressed(KeyEvent arg0) {
		//No use;
	}
	@Override
	public void keyReleased(KeyEvent arg0) {
		//No use;
	}
	@Override
	public void keyTyped(KeyEvent arg0) {
		//Download next history shit for changelog;
		main.update.getNextChangelog(command_log[0]);
	}
	@Override
	public void focusGained(FocusEvent e) {
		//DERP AIN*T GO DO ANYTHING!!!
	}
	@Override
	public void focusLost(FocusEvent e) {
		//Saving the colors;
		JTextField tf = (JTextField)e.getSource();
		int value = Integer.parseInt(tf.getText());
		if(value > 255) {
			value = 255;
			tf.setText(value+"");
		}
		else if(value < 0) {
			value = 0;
			tf.setText(value+"");
		}
		int i = tf.getX();
		int ii = tf.getY();
		//Calculating the color index + r/b/g and setting the new value;
		config.property.setProperty("color"+((i-8)/37)+((ii-(5+32+5))/24),value+"");
		config.property.save();
		repaint();
	}
}
