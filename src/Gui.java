import java.awt.Dimension;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.IOException;
import java.util.Vector;
import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JProgressBar;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

public class Gui implements ListSelectionListener, ActionListener, Runnable {
	Main main;
	int last = 0;
	
	ActionEvent e;
	Thread run;
	
	JFrame frame;
	ImageIcon image;
	Image shopbg;
	JLabel label;// String derp = "Version: "+main.config.version+"-"+main.config.versionstate+" | Date: "+main.config.date+" | Author: Wiggy boy | ";
	
	JTabbedPane tab_frame;
	JTabbedPane tab_frame_hero;
	JTabbedPane tab_frame_hero_althero;
	JTabbedPane tab_frame_hero_recommended;
	JTabbedPane tab_compile;
	
	Panel panel_frame;
	Panel panel_frame_hero;
	Panel panel_frame_hero_althero;
	Panel panel_frame_hero_recommended;
	Panel panel_frame_hero_indicator;
	Panel panel_frame_announcers;
	Panel panel_frame_taunt;
	Panel panel_compile;
	Panel panel_changelog;
	Panel panel_buglog;
	Panel panel_credit;
	Panel panel_donate;
	
	JList list; Vector<String> hero_list = new Vector<String>();
	ButtonGroup[] hero_group = new ButtonGroup[10];
	JRadioButton[][] hero_group_radio = new JRadioButton[10][10];
	JLabel[] hero_group_name = new JLabel[10];
	JLabel advanced_setto;
	JLabel hero_setto;
	JButton advanced_reset;
	JButton advanced_alt;
	JButton advanced_standard;
	JButton advanced_alttrue;
	JButton advanced_altfalse;
	JButton hero_reset;
	JButton hero_alt;
	JButton hero_standard;
	JCheckBox hero_alttrue;
	
	ButtonGroup[] announcer_group = new ButtonGroup[10];
	JRadioButton[][] announcer_group_radio = new JRadioButton[10][10];
	JLabel[] announcer_group_name = new JLabel[10];
	ButtonGroup[] taunt_group = new ButtonGroup[10];
	JRadioButton[][] taunt_group_radio = new JRadioButton[10][10];
	JLabel[] taunt_group_name = new JLabel[10];
	
	JLabel config_proplabel;
	JTextField config_prop;
	JTextField config_value;
	JButton config_setprop;
	JButton config_reset;
	JLabel compile_modlabel;
	JLabel compile_moddirlabel;
	JTextField compile_modname;
	JComboBox compile_moddir; String[] dirarray = {"File folder","Desktop","My Documents","Mod Folder"}; String[] dir;
	JCheckBox compile_althero;
	JCheckBox compile_recommended;
	JCheckBox compile_indicator;
	JCheckBox compile_announcers;
	JCheckBox compile_taunt;
	JButton compile_compile;
	
	JTextArea changelog_edit;
	JScrollPane changelog_edit_scrollpane;
	JTextArea buglog_edit;
	JScrollPane buglog_edit_scrollpane;
	JTextArea credit_edit;
	JScrollPane credit_edit_scrollpane;
	
	JFrame compileFrame;
	JProgressBar compileProgres;
	JTextArea compileOutput;
	JScrollPane compileOutputScroll;
	
	public Gui(Main main) {
		this.main = main;
		
		Gamepath gp = new Gamepath(main);
		
		String[] array = {"",new JFileChooser().getFileSystemView().getHomeDirectory().toString()+File.separator,new JFileChooser().getFileSystemView().getDefaultDirectory().toString()+File.separator,gp.findModFolder(main.config.config.getProperty("gamepath")) + File.separator};
		dir = array;
		//Loading the icon and shop background;
		image = new ImageIcon(getClass().getResource("icon.png"));
		shopbg = new ImageIcon(getClass().getResource("shopbg.png")).getImage();
		
		//Forming the gui;
		frame = new JFrame(main.config.name);
		frame.setIconImage(image.getImage());
		frame.setResizable(false);
			panel_frame = new Panel(main);
			panel_frame.setPreferredSize(new Dimension(782,550));
				//Forming the default tabs;
				tab_frame = new JTabbedPane();
				tab_frame.setPreferredSize(new Dimension(800,352));
					//Forming the hero tab;
					panel_frame_hero = new Panel(main);
						//Adding all heroes to the list;
						for(int i = 0; i <= main.config.h;i += 1) {
							hero_list.add(i,main.config.hero[i].name[0]);
							//System.out.println(i+"."+main.config.hero[i].name[0]);
						}
						main.config.hero[0].name[0] = "Monkey"; //Changing Courier name to "Monkey";
						list = new JList(hero_list);
						list.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
						list.setLayoutOrientation(JList.VERTICAL);
						list.addListSelectionListener(this);
						JScrollPane listScroller = new JScrollPane(list);
						listScroller.setPreferredSize(new Dimension(125,313));
						listScroller.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
						//Forming the hero tabs;
						tab_frame_hero = new JTabbedPane();
						tab_frame_hero.setPreferredSize(new Dimension(640,313));
							//Forming the hero avatar bar;
							panel_frame_hero_althero = new Panel(main);
							panel_frame_hero_althero.state = 1; //Switching the painting order;
								//Forming the radios;
								for(int i = 0; i <= 9;i += 1) {
									hero_group[i] = new ButtonGroup();
									panel_frame_hero_althero.add(hero_group_name[i] = new JLabel(i+""),380,12+32+20*i);
									for(int ii = 0; ii <= 9;ii += 1) {
										hero_group[i].add(hero_group_radio[i][ii] = new JRadioButton(""));
										hero_group_radio[i][ii].setActionCommand(""+ii);
										panel_frame_hero_althero.add(hero_group_radio[i][ii],11+37*i,10+32+20*ii);
										if(i == ii) {
											hero_group_radio[i][ii].setSelected(true);
										}
									}
								}
								//Forming the hero avatar menu;
								tab_frame_hero_althero = new JTabbedPane();
								tab_frame_hero_althero.setPreferredSize(new Dimension(130,275));
									advanced_setto = new JLabel("    Set all heroes to:");
									advanced_reset = new JButton("Default");
									advanced_reset.setPreferredSize(new Dimension(120,25));
									advanced_reset.addActionListener(this);
									advanced_reset.setActionCommand("advanced_reset");
									advanced_standard = new JButton("Standard");
									advanced_standard.setPreferredSize(new Dimension(120,25));
									advanced_standard.addActionListener(this);
									advanced_standard.setActionCommand("advanced_standard");
									advanced_alt = new JButton("Alt");
									advanced_alt.setPreferredSize(new Dimension(120,25));
									advanced_alt.addActionListener(this);
									advanced_alt.setActionCommand("advanced_alt");
									advanced_alttrue = new JButton("Alt E");
									advanced_alttrue.setPreferredSize(new Dimension(60,25));
									advanced_alttrue.addActionListener(this);
									advanced_alttrue.setActionCommand("advanced_alttrue");
									advanced_altfalse = new JButton("Alt D");
									advanced_altfalse.setPreferredSize(new Dimension(60,25));
									advanced_altfalse.addActionListener(this);
									advanced_altfalse.setActionCommand("advanced_altfalse");
									hero_setto = new JLabel("     Set this hero to:");
									hero_reset = new JButton("Default");
									hero_reset.setPreferredSize(new Dimension(120,25));
									hero_reset.addActionListener(this);
									hero_reset.setActionCommand("hero_reset");
									hero_standard = new JButton("Standard");
									hero_standard.setPreferredSize(new Dimension(120,25));
									hero_standard.addActionListener(this);
									hero_standard.setActionCommand("hero_standard");
									hero_alt = new JButton("Alt");
									hero_alt.setPreferredSize(new Dimension(120,25));
									hero_alt.addActionListener(this);
									hero_alt.setActionCommand("hero_alt");
									hero_alttrue = new JCheckBox("Enable alt");
								panel_frame_hero_althero.add(hero_setto,505,10);
								panel_frame_hero_althero.add(hero_reset,505,30);
								panel_frame_hero_althero.add(hero_standard,505,60);
								panel_frame_hero_althero.add(hero_alt,505,90);
								panel_frame_hero_althero.add(hero_alttrue,520,115);
								panel_frame_hero_althero.add(advanced_setto,505,140);
								panel_frame_hero_althero.add(advanced_reset,505,160);
								panel_frame_hero_althero.add(advanced_standard,505,190);
								panel_frame_hero_althero.add(advanced_alt,505,220);
								panel_frame_hero_althero.add(advanced_alttrue,505,250);
								panel_frame_hero_althero.add(advanced_altfalse,565,250);
							panel_frame_hero_althero.add(tab_frame_hero_althero,500,5);
						tab_frame_hero.addTab("Hero Avatars", null, panel_frame_hero_althero, "Alt hero managment");
							//Forming the recommended tab;
							panel_frame_hero_recommended = new Panel(main);
							panel_frame_hero_recommended.state = 2; //Switching the painting order;
							tab_frame_hero_recommended = new JTabbedPane();
							tab_frame_hero_recommended.setPreferredSize(new Dimension(310,275));
								//Insert items here;
							panel_frame_hero_recommended.add(tab_frame_hero_recommended,320,5);
						tab_frame_hero.addTab("Recommended Items", null, panel_frame_hero_recommended, "Recommended shop managment");
							//Forming the indicator tab;
							panel_frame_hero_indicator = new Panel(main);
								JLabel derp4 = new JLabel("This part is still under heavy development");
							panel_frame_hero_indicator.add(derp4,10,10);
						tab_frame_hero.addTab("Indicators", null, panel_frame_hero_indicator, "Indication managment");
					panel_frame_hero.add(tab_frame_hero,135,5);
					panel_frame_hero.add(listScroller,5,5);
				tab_frame.addTab("Hero",null,panel_frame_hero,"Hero options");
					//Forming the announcers tab;
					panel_frame_announcers = new Panel(main);
					panel_frame_announcers.state = 3; //Switching the painting order;
						//Forming the radios;
						for(int i = 0; i <= main.config.a;i += 1) {
							announcer_group[i] = new ButtonGroup();
							panel_frame_announcers.add(announcer_group_name[i] = new JLabel(main.config.announcer[i].name),10+37*(main.config.a+1),12+32+20*i);
							for(int ii = 0; ii <= main.config.a;ii += 1) {
								announcer_group[i].add(announcer_group_radio[i][ii] = new JRadioButton(""));
								announcer_group_radio[i][ii].setActionCommand(""+ii);
								panel_frame_announcers.add(announcer_group_radio[i][ii],11+37*i,10+32+20*ii);
								if(ii == main.config.getValue(main.config.announcer[i].voice,i)) {
									announcer_group_radio[i][ii].setSelected(true);
								}
							}
						}
						/*for(int i = 0; i <= main.config.a;i += 1) {
							announcer_group_radio[main.config.sell][i].setEnabled(false);
						}
						announcer_group_radio[main.config.sell][main.config.sell].setEnabled(true);
						announcer_group_radio[main.config.sell][main.config.sell].setSelected(true);*/
					//Add items here;
				tab_frame.addTab("Announcers",null,panel_frame_announcers,"Announcers options");
					//Forming the taunt tab;
					panel_frame_taunt = new Panel(main);
					panel_frame_taunt.state = 4; //Switching the painting order;
						for(int i = 0; i <= main.config.t;i += 1) {
							taunt_group[i] = new ButtonGroup();
							panel_frame_taunt.add(taunt_group_name[i] = new JLabel(main.config.taunt[i].name),10+37*(main.config.t+1),12+32+20*i);
							for(int ii = 0; ii <= main.config.t;ii += 1) {
								taunt_group[i].add(taunt_group_radio[i][ii] = new JRadioButton(""));
								taunt_group_radio[i][ii].setActionCommand(""+ii);
								panel_frame_taunt.add(taunt_group_radio[i][ii],11+37*i,10+32+20*ii);
								if(ii == main.config.getValue(main.config.taunt[i].key,i)) {
									taunt_group_radio[i][ii].setSelected(true);
								}
							}
						}
						for(int i = 0; i <= main.config.t;i += 1) {
							taunt_group_radio[0][i].setEnabled(false);
						}
						taunt_group_radio[0][0].setEnabled(true);
						taunt_group_radio[0][0].setSelected(true);
					//Add items here;
				tab_frame.addTab("Taunt",null,panel_frame_taunt,"Taunt options");
				//Forming the config tabs;
				tab_compile = new JTabbedPane();
				tab_compile.setPreferredSize(new Dimension(800,168));
					//Forming the compile tab;
					panel_compile = new Panel(main);
						config_proplabel = new JLabel("Property:                   Value:");
						config_prop = new JTextField("");
						config_prop.setPreferredSize(new Dimension(100,25));
						config_value = new JTextField("");
						config_value.setPreferredSize(new Dimension(150,25));
						config_setprop = new JButton("Set");
						config_setprop.addActionListener(this);
						config_setprop.setActionCommand("setprop");
						config_setprop.setPreferredSize(new Dimension(55,24));
						config_reset = new JButton("Reset config");
						config_reset.addActionListener(this);
						config_reset.setActionCommand("reset");
						config_reset.setPreferredSize(new Dimension(125,25));
						compile_modlabel = new JLabel("Name:");
						compile_modname = new JTextField("(CoN)");
						compile_modname.setPreferredSize(new Dimension(150,25));
						compile_moddirlabel = new JLabel("Output directory:");
						compile_moddir = new JComboBox(dirarray);
						compile_moddir.setPreferredSize(new Dimension(150,25));
						compile_moddir.setSelectedIndex(main.config.getValue("compilepath",0));
						compile_althero = new JCheckBox("Hero Avatars",main.config.getValue("althero",false));
						compile_recommended = new JCheckBox("Recommended Items",main.config.getValue("recommended",false));
						compile_recommended.setEnabled(false);
						compile_indicator = new JCheckBox("Indicators",main.config.getValue("indicator",false));
						compile_indicator.setEnabled(false);
						compile_announcers = new JCheckBox("Announcers",main.config.getValue("announcers",false));
						compile_taunt = new JCheckBox("Taunt",main.config.getValue("taunt",false));
						compile_compile = new JButton("Compile");
						compile_compile.addActionListener(this);
						compile_compile.setActionCommand("compile");
						compile_compile.setPreferredSize(new Dimension(285,25));
					panel_compile.add(config_proplabel,5,5);
					panel_compile.add(config_prop,5,25);
					panel_compile.add(config_value,110,25);
					panel_compile.add(config_setprop,265,25);
					panel_compile.add(config_reset,5,110);
					panel_compile.add(compile_modlabel,490,5);
					panel_compile.add(compile_modname,490,25);
					panel_compile.add(compile_moddirlabel,490,55);
					panel_compile.add(compile_moddir,490,75);
					panel_compile.add(compile_althero,647,5);
					panel_compile.add(compile_recommended,647,25);
					panel_compile.add(compile_indicator,647,45);
					panel_compile.add(compile_announcers,647,65);
					panel_compile.add(compile_taunt,647,85);
					panel_compile.add(compile_compile,490,110);
				tab_compile.addTab("Compile", null, panel_compile, "Compile management");
					//Forming the changelog tab;
					panel_changelog = new Panel(main);
						changelog_edit = new JTextArea("Downloading...");
						changelog_edit.setEditable(false);
						changelog_edit_scrollpane = new JScrollPane(changelog_edit);
						changelog_edit_scrollpane.setPreferredSize(new Dimension(772,131));
						changelog_edit_scrollpane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
					panel_changelog.add(changelog_edit_scrollpane,5,5);
				tab_compile.addTab("Changelog", null,panel_changelog, "Changelog: "+main.config.version);
				//Forming the buglog tab;
					panel_buglog = new Panel(main);
						buglog_edit = new JTextArea("Downloading...");
						buglog_edit.setEditable(false);
						buglog_edit_scrollpane = new JScrollPane(buglog_edit);
						buglog_edit_scrollpane.setPreferredSize(new Dimension(772,131));
						buglog_edit_scrollpane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
					panel_buglog.add(buglog_edit_scrollpane,5,5);
				tab_compile.addTab("Buglog", null,panel_buglog, "Buglog");
					//Forming the credit tab;
					panel_credit = new Panel(main);
						credit_edit = new JTextArea(main.config.credit);
						credit_edit.setEditable(false);
						credit_edit_scrollpane = new JScrollPane(credit_edit);
						credit_edit_scrollpane.setPreferredSize(new Dimension(772,131));
						credit_edit_scrollpane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
					panel_credit.add(credit_edit_scrollpane,5,5);
				tab_compile.addTab("Credits", null, panel_credit, "Credit management");
				label = new JLabel("Version: "+main.config.version+"-"+main.config.versionstate+" | Date: "+main.config.date+" | Author: Wiggy boy | ");
				label.setEnabled(false);
			panel_frame.add(tab_frame,-1,0);
			panel_frame.add(tab_compile,-1,357);
			panel_frame.add(label,5,528);
		frame.add(panel_frame);
		list.setSelectedIndex(0); //Index set changed to 0(Courier);
		frame.pack();
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
		frame.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				e.getWindow().setVisible(false);
				System.exit(0);
			}
		});
	}
	
	public void storeConfig(Config c) {
		//Storing the hero;
		for(int i = 0; i <= c.hero[last].avatar;i += 1) {
			c.config.setProperty(""+c.hero[last].key[0]+i,hero_group[i].getSelection().getActionCommand());
		}
		//Storing the alt;
		boolean alttrue;
		if(hero_alttrue.getSelectedObjects() != null) {
			alttrue = true;
		} else {
			alttrue = false;
		}
		c.config.setProperty(c.hero[last].key[0]+"alt",""+alttrue);
		//Storing the Announcers;
		for(int i = 0; i <= c.a;i++) {
			c.config.setProperty(""+c.announcer[i].voice,announcer_group[i].getSelection().getActionCommand());
		}
		//Storing the Taunts;
		for(int i = 0; i <= c.t;i++) {
			c.config.setProperty(""+c.taunt[i].key,taunt_group[i].getSelection().getActionCommand());
		}
		//Storing the compilepath;
		c.config.setProperty("compilepath",compile_moddir.getSelectedIndex()+"");
		//Storing the compile checkboxes;
		boolean x;
		if(compile_althero.getSelectedObjects() != null) {
			x = true;
		} else {
			x = false;
		}
		c.config.setProperty("althero",""+x);
		if(compile_recommended.getSelectedObjects() != null) {
			x = true;
		} else {
			x = false;
		}
		c.config.setProperty("recommended",""+x);
		if(compile_indicator.getSelectedObjects() != null) {
			x = true;
		} else {
			x = false;
		}
		c.config.setProperty("indicator",""+x);
		if(compile_announcers.getSelectedObjects() != null) {
			x = true;
		} else {
			x = false;
		}
		c.config.setProperty("announcer",""+x);
		if(compile_taunt.getSelectedObjects() != null) {
			x = true;
		} else {
			x = false;
		}
		c.config.setProperty("taunt",""+x);
		//Writing data to disc;
		c.save();	
	}
	@Override
	public void run() {
		//Storing data;
		storeConfig(main.config);
		//Finding the trigger;
		if(e.getActionCommand().equals("compile")) {
			main.compile.get();
		}
		else if (e.getActionCommand().equals("setprop")) {
			String property = config_prop.getText();
			String value = config_value.getText();
			if(property.isEmpty() == false) {
				if(value.equals("?") == true) {
					JOptionPane.showMessageDialog(frame,property+" = "+main.config.getValue(property,"-1"));
				}
				else {
					main.config.config.setProperty(property,value);
					JOptionPane.showMessageDialog(frame,property+" = "+value);
					main.config.save();
					String[] cmd = {"java", "-jar",new File("(CoN).jar").getAbsolutePath()};
					try {
						Runtime.getRuntime().exec(cmd);
					} catch (IOException e1) {
						e1.printStackTrace();
					}
					System.exit(0);
				}
			}
		}
		else if (e.getActionCommand().equals("reset")) {
			main.config.config.clear();
			main.config.save();
			String[] cmd = {"java", "-jar",new File("(CoN).jar").getAbsolutePath()};
			try {
				Runtime.getRuntime().exec(cmd);
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			System.exit(0);
		}
		else {
			//hero menu buttons;
			if(e.getActionCommand().equals("advanced_reset")) {
				for(int h = 0;h <= main.config.h;h += 1) {
					for(int i = 0;i <= main.config.hero[h].avatar;i += 1) {
						main.config.config.setProperty(""+main.config.hero[h].key[0]+i,i+"");
					}
				}
			}
			else if(e.getActionCommand().equals("advanced_standard")) {
				for(int h = 0;h <= main.config.h;h += 1) {
					for(int i = 0;i <= main.config.hero[h].avatar;i += 1) {
						main.config.config.setProperty(""+main.config.hero[h].key[0]+i,"0");
					}
				}
			}
			else if(e.getActionCommand().equals("advanced_alt")) {
				for(int h = 0;h <= main.config.h;h += 1) {
					for(int i = 0;i <= main.config.hero[h].avatar;i += 1) {
						main.config.config.setProperty(""+main.config.hero[h].key[0]+i,main.config.hero[h].avatar+"");
					}
				}
			}
			else if(e.getActionCommand().equals("advanced_alttrue")) {
				for(int h = 0;h <= main.config.h;h += 1) {
					if(main.config.hero[h].avatar > 0) {
						main.config.config.setProperty(main.config.hero[h].key[0]+"alt","true");
					}
				}
			}
			else if(e.getActionCommand().equals("advanced_altfalse")) {
				for(int h = 0;h <= main.config.h;h += 1) {
					main.config.config.setProperty(main.config.hero[h].key[0]+"alt","false");
				}
			}
			else if(e.getActionCommand().equals("hero_reset")) {
				for(int i = 0;i <= main.config.hero[last].avatar;i += 1) {
					main.config.config.setProperty(""+main.config.hero[last].key[0]+i,i+"");
				}
			}
			else if(e.getActionCommand().equals("hero_standard")) {
				for(int i = 0;i <= main.config.hero[last].avatar;i += 1) {
					main.config.config.setProperty(""+main.config.hero[last].key[0]+i,"0");
				}
			}
			else if(e.getActionCommand().equals("hero_alt")) {
				for(int i = 0;i <= main.config.hero[last].avatar;i += 1) {
					main.config.config.setProperty(""+main.config.hero[last].key[0]+i,main.config.hero[last].avatar+"");
				}
			}
			for(int i = 0; i <= main.config.hero[last].avatar;i += 1) {
				hero_group_radio[i][main.config.getValue(""+main.config.hero[last].key[0]+i,i)].setSelected(true);
				for(int ii = 0; ii <= main.config.hero[last].avatar;ii += 1) {
					hero_group_radio[i][ii].setEnabled(true);
				}
			}
			for(int i = 0; i <= 9;i += 1) {
				hero_group_name[i].setText(main.config.hero[last].name[i]);
				if(i > main.config.hero[last].avatar) {
					for(int ii = 0; ii <= 9;ii += 1) {
						hero_group_radio[i][ii].setEnabled(false);
					}
				}
				else {
					for(int ii = main.config.hero[last].avatar+1; ii <= 9;ii += 1) {
						hero_group_radio[i][ii].setEnabled(false);
					}
				}
			}
			if(main.config.hero[last].avatar < 1) {
				hero_alttrue.setSelected(main.config.getValue(main.config.hero[last].key[0]+"alt",false));
				hero_alttrue.setEnabled(false);
			}
			else {
				hero_alttrue.setSelected(main.config.getValue(main.config.hero[last].key[0]+"alt",true));
				hero_alttrue.setEnabled(true);
			}
		}
	}
	@Override
	public void actionPerformed(ActionEvent e) {
		this.e = e;
		run = new Thread(this);
		run.start();
	}
	@Override
	public void valueChanged(ListSelectionEvent e) {
		//If hero has been changed
		if(last != list.getAnchorSelectionIndex()) {
			//Storing data;
			storeConfig(main.config);
			//Get new hero id;
			last = list.getAnchorSelectionIndex();
			//Repaint with new hero id;
			panel_frame.repaint();
			//Set the selected radios;
			for(int i = 0; i <= main.config.hero[last].avatar;i += 1) {
				hero_group_radio[i][main.config.getValue(""+main.config.hero[last].key[0]+i,i)].setSelected(true);
				for(int ii = 0; ii <= main.config.hero[last].avatar;ii += 1) {
					hero_group_radio[i][ii].setEnabled(true);
				}
			}
			//Set the new names;
			for(int i = 0; i <= 9;i += 1) {
				hero_group_name[i].setText(main.config.hero[last].name[i]);
				if(i > main.config.hero[last].avatar) {
					for(int ii = 0; ii <= 9;ii += 1) {
						hero_group_radio[i][ii].setEnabled(false);
					}
				}
				else {
					for(int ii = main.config.hero[last].avatar+1; ii <= 9;ii += 1) {
						hero_group_radio[i][ii].setEnabled(false);
					}
				}
			}
			if(main.config.hero[last].avatar < 1) {
				hero_alttrue.setSelected(main.config.getValue(main.config.hero[last].key[0]+"alt",false));
				hero_alttrue.setEnabled(false);
			}
			else {
				hero_alttrue.setSelected(main.config.getValue(main.config.hero[last].key[0]+"alt",true));
				hero_alttrue.setEnabled(true);
			}
		}
		//On boot selection required to be painted;
		else if(main.config.state == 0) {
			last = list.getAnchorSelectionIndex();
			panel_frame.repaint();
			for(int i = 0; i <= main.config.hero[last].avatar;i += 1) {
				hero_group_radio[i][main.config.getValue(""+main.config.hero[last].key[0]+i,i)].setSelected(true);
				for(int ii = 0; ii <= main.config.hero[last].avatar;ii += 1) {
					hero_group_radio[i][ii].setEnabled(true);
				}
			}
			for(int i = 0; i <= 9;i += 1) {
				hero_group_name[i].setText(main.config.hero[last].name[i]);
				if(i > main.config.hero[last].avatar) {
					for(int ii = 0; ii <= 9;ii += 1) {
						hero_group_radio[i][ii].setEnabled(false);
					}
				}
				else {
					for(int ii = main.config.hero[last].avatar+1; ii <= 9;ii += 1) {
						hero_group_radio[i][ii].setEnabled(false);
					}
				}
			}
			if(main.config.hero[last].avatar < 1) {
				hero_alttrue.setSelected(main.config.getValue(main.config.hero[last].key[0]+"alt",false));
				hero_alttrue.setEnabled(false);
			}
			else {
				hero_alttrue.setSelected(main.config.getValue(main.config.hero[last].key[0]+"alt",true));
				hero_alttrue.setEnabled(true);
			}
			main.config.state = 1;
		}
	}
	
	public JFrame createCompileFrame() {
		JFrame frame = new JFrame("Compiling: 0%");
		frame.setPreferredSize(new Dimension(300,200));
		
		frame.pack();
		frame.setLocationRelativeTo(this.frame);
		frame.setVisible(true);
		frame.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				e.getWindow().setVisible(false);
				System.exit(0);
			}
		});
		
		/*compile_progres = new JProgressBar(0,main.config.h+main.config.a);
						compile_progres.setPreferredSize(new Dimension(285,25));
						compile_progres.setVisible(false);*/
		return frame;
	}
}