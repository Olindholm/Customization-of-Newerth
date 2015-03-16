package Engine;

import java.awt.Desktop;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.IOException;
import java.util.Locale;
import java.util.Vector;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import Doodads.LLAccessories;
import Doodads.LLFile;
import Interface.CComboBox;
import Interface.CFrame;
import Interface.CList;
import Interface.CPanel;
import Interface.CTabPane;

public class Gui extends CFrame implements ActionListener, Runnable {
	//STATIC variables;
	public	static	int		ACTION_APPLY			= 1;
	public	static	int		ACTION_UPDATE			= 2;
	public	static	int		ACTION_REFRESH			= 3;
	public	static	int		ACTION_SETTINGS			= 4;
	public	static	int		ACTION_HELP				= 5;
	public	static	int		ACTION_CHANGELOG		= 6;
	public	static	int		ACTION_CREDITS			= 7;
	public	static	int		ACTION_ABOUT			= 8;
	public	static	int		ACTION_APPLYNLAUNCH		= 9;
	public	static	int		ACTION_UNAPPLY			= 10;
	
	//Variables;
	private Main	main;
	public	Thread	run = new Thread(this);
	public	int		action;
	
	private	boolean	change = true;
	
	public	CList			herolist;
	public	JProgressBar	progressbar;
	public	JLabel			progresslabel;
	
	//Setup;
	
	//Constructor;
	public Gui(final Main main) {
		this.main = main;
		
		Locale.setDefault(Locale.ENGLISH);
		JComponent.setDefaultLocale(Locale.ENGLISH);
		
		setTitle(Main.NAME+" - "+Main.BRANCH);
		setIconImage(new ImageIcon(ClassLoader.getSystemResource("Images/icon.png")).getImage());
		setResizable(false);
		
		CPanel panel = new CPanel(836-5,363);
		add(panel.component());
		
		JMenuBar menubar;
		JMenu menu;
		JMenuItem menuitem;
		
		//Menubar;
		menubar = new JMenuBar();
		setJMenuBar(menubar);
		
		//File menu;
		menu = new JMenu("File");
		menu.setMnemonic(KeyEvent.VK_F);
		menubar.add(menu);
		
		//Compile Modifications item;
		menuitem = new JMenuItem("Apply Modifications",KeyEvent.VK_A);
		menuitem.addActionListener(this);
		menuitem.setActionCommand(Gui.ACTION_APPLY+"");
		menu.add(menuitem);
		
		menuitem = new JMenuItem("Apply Modifications & Launch Game",KeyEvent.VK_S);
		menuitem.addActionListener(this);
		menuitem.setActionCommand(Gui.ACTION_APPLYNLAUNCH+"");
		menu.add(menuitem);
		
		menuitem = new JMenuItem("Unapply Modifications",KeyEvent.VK_U);
		menuitem.addActionListener(this);
		menuitem.setActionCommand(Gui.ACTION_UNAPPLY+"");
		menu.add(menuitem);
		
		menu.addSeparator();
		
		//Check for Updates item;
		menuitem = new JMenuItem("Check for Updates",KeyEvent.VK_C);
		menuitem.addActionListener(this);
		menuitem.setActionCommand(Gui.ACTION_UPDATE+"");
		menu.add(menuitem);
		
		menu.addSeparator();
		
		//Exit item;
		menuitem = new JMenuItem("Exit",KeyEvent.VK_X);
		menuitem.addActionListener(
			new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					main.exit();
				}
			}
		);
		menu.add(menuitem);
		
		//View menu;
		menu = new JMenu("View");
		menu.setMnemonic(KeyEvent.VK_V);
		menubar.add(menu);
		
		//View Export Folder item;
		menuitem = new JMenuItem("View Game Folder",KeyEvent.VK_V);
		menuitem.addActionListener(
			new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent ae) {
					if(Desktop.isDesktopSupported()) {
						Desktop explorer = Desktop.getDesktop();
						try {
							File file = new File(main.config.property.getProperty("Setting_Resources",LLFile.getProgram()[0]+"Heroes of Newerth"+File.separator+"game"+File.separator+"resources0.s2z"));
							if(!file.exists()) {
								String[] filters = {"Compressed S2Games file","s2z"};
								file = LLFile.chooseFile("Open - Heroes of Newerth resources",filters,new File(LLFile.getProgram()[0]));
								if(file == null) {
									return;
								}
								main.config.property.setProperty("Setting_Resources",file.getAbsolutePath());
							}
							
							explorer.open(file.getParentFile());
						} catch (IOException e) {
							//new LLException(e);
						}
					}
					else {
						JOptionPane.showMessageDialog(null,"Request could not be completed due gui access was denied.");
					}
				}
			}
		);
		menu.add(menuitem);
		
		menu.addSeparator();
		
		//Refresh Data item;
		menuitem = new JMenuItem("Refresh Data",KeyEvent.VK_R);
		menuitem.addActionListener(this);
		menuitem.setActionCommand(Gui.ACTION_REFRESH+"");
		menu.add(menuitem);
		
		//Options menu;
		menu = new JMenu("Options");
		menu.setMnemonic(KeyEvent.VK_O);
		menubar.add(menu);
		
		//View Export Folder item;
		menuitem = new JMenuItem("Settings",KeyEvent.VK_S);
		menuitem.addActionListener(this);
		menuitem.setActionCommand(Gui.ACTION_SETTINGS+"");
		menu.add(menuitem);
		
		//Help menu;
		menu = new JMenu("Help");
		menu.setMnemonic(KeyEvent.VK_H);
		menubar.add(menu);
		
		//Help Instructions item;
		menuitem = new JMenuItem("Help Instructions",KeyEvent.VK_H);
		menuitem.addActionListener(this);
		menuitem.setActionCommand(Gui.ACTION_HELP+"");
		menu.add(menuitem);
		
		menu.addSeparator();
		
		//Changelog item;
		menuitem = new JMenuItem("Changelog",KeyEvent.VK_C);
		menuitem.addActionListener(this);
		menuitem.setActionCommand(Gui.ACTION_CHANGELOG+"");
		menu.add(menuitem);
		
		//Credits item;
		menuitem = new JMenuItem("Credits",KeyEvent.VK_R);
		menuitem.addActionListener(this);
		menuitem.setActionCommand(Gui.ACTION_CREDITS+"");
		menu.add(menuitem);
		
		//About item;
		menuitem = new JMenuItem("About",KeyEvent.VK_A);
		menuitem.addActionListener(this);
		menuitem.setActionCommand(Gui.ACTION_ABOUT+"");
		menu.add(menuitem);
		
		JScrollPane pane;
		
		//Main tab;
		CTabPane tab = new CTabPane(831+5,310+28);
		panel.add(tab.component(),-2,0);
		
		//Hero page;
		CPanel hero_panel = new CPanel(831,310);
		tab.addTab("Heroes",hero_panel.component());
		
		herolist = new CList(/*herolist*/);
		hero_panel.add(pane = new JScrollPane(herolist.component()),5,5);
		pane.setSize(new Dimension(150,300));
		pane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		pane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		herolist.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		
		//Hero tab
		CTabPane hero_tab = new CTabPane(661+5,272+28);
		hero_panel.add(hero_tab.component(),5+150+5,5-2);
		
		//Avatar page;
		CPanel avatar_hero_panel = new CPanel(661,272);
		hero_tab.addTab("Avatars",avatar_hero_panel.component());
		
		final CPanel avatarpanel;
		avatarpanel = new CPanel(0,0);

		final Vector<CComboBox> boxes = new Vector<CComboBox>();
		final Vector<JLabel>	icons = new Vector<JLabel>();
		
		avatar_hero_panel.add(pane = new JScrollPane(avatarpanel.component()),5,5);
		pane.setSize(new Dimension(436+18,260+2));
		pane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		pane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		pane.getVerticalScrollBar().setUnitIncrement(6);
		avatarpanel.setSize(5555,0);
		
		final JCheckBox checkbox;
		checkbox = new JCheckBox("Force all avatars to...");
		checkbox.setSize(checkbox.getPreferredSize().width,checkbox.getPreferredSize().height-3*2);
		avatar_hero_panel.add(checkbox,464,5);
		checkbox.setEnabled(false);

		final CComboBox combobox;
		combobox = new CComboBox(192,39);
		avatar_hero_panel.add(combobox.component(),464,5+checkbox.getPreferredSize().height+5-6);
		combobox.setEnabled(false);
		
		//...
		herolist.addListSelectionListener(
			new ListSelectionListener() {
				@Override
				public void valueChanged(ListSelectionEvent e) {
					change = false;
					int index = herolist.getSelectedIndex();
					
					if(herolist.getSelectedIndex() != herolist.getLastSelectedIndex()) {
						if(herolist.getSelectedIndex() == -1) {
							checkbox.setSelected(false);
							checkbox.setEnabled(false);
							combobox.clear();
							combobox.setEnabled(false);
							
							for(int i = 0;i <= boxes.size()-1;i++) {
								boxes.get(i).setVisible(false);
								icons.get(i/2).setVisible(false);
							}
							return;
						}
						//Creating required elements;
						for(int i = boxes.size();i <= main.config.heroes.get(index).getAvatarCount()*2-1;i++) {
							CComboBox box = new CComboBox(192,39);
							box.setEnabled(i%2 != 0);
							avatarpanel.add(box.component(),5+(i%2)*234,5+(i-(i%2))/2*(39+5));
							boxes.add(box);
							if(i%2 != 0) {
								final int boxindex = i;
								final int altindex = i/2;
								
								box.addActionListener(
									new ActionListener() {
										@Override
										public void actionPerformed(ActionEvent e) {
											if(change && herolist.getSelectedIndex() != -1) {
												//System.out.println("Saving..."+main.config.heroes.get(herolist.getSelectedIndex()).getName()+"."+main.config.heroes.get(herolist.getSelectedIndex()).getAvatar(altindex).getKey()+" to "+main.config.heroes.get(herolist.getSelectedIndex()).getAvatar(boxes.get(boxindex).getSelectedIndex()).getKey());
												main.config.property.setProperty(main.config.heroes.get(herolist.getSelectedIndex()).getName()+"."+main.config.heroes.get(herolist.getSelectedIndex()).getAvatar(altindex).getKey(),main.config.heroes.get(herolist.getSelectedIndex()).getAvatar(boxes.get(boxindex).getSelectedIndex()).getKey());
											}
										}
									}
								);
							}
							
							Icon image;
							if(i == 0) {
								image = new ImageIcon(ClassLoader.getSystemResource("Images/arrow.png"));
							}
							else {
								image = icons.get(0).getIcon();
							}
							if(i%2 == 0) {
								JLabel l = new JLabel(image);
								l.setSize(new Dimension(32,32));
								icons.add(l);
								
								avatarpanel.add(l,202,8+(i/2)*44);
							}
						}
						
						//Collecting images;
						ImageIcon[] images = new ImageIcon[main.config.heroes.get(index).getAvatarCount()];
						for(int i = 0;i <= images.length-1;i++) {
							//System.out.println(main.config.heroes.get(index).getAvatar(i).getName()+"("+main.config.heroes.get(index).getAvatar(i).getKey()+")="+main.config.heroes.get(index).getAvatar(i).getIcon());
							images[i] = main.config.getImage(main.config.heroes.get(index).getAvatar(i).getIcon());
						}
						
						//Local boxes
						for(int i = 0;i <= main.config.heroes.get(index).getAvatarCount()-1;i++) {
							boxes.get(i*2).clear();
							boxes.get(i*2).add(images[i],main.config.heroes.get(index).getAvatar(i).getName());
							boxes.get(i*2).setVisible(true);
							
							icons.get(i).setVisible(true);
						}
						for(int i = 0;i <= main.config.heroes.get(index).getAvatarCount()-1;i++) {
							boxes.get(i*2+1).clear();
							
							for(int ii = 0;ii <= main.config.heroes.get(index).getAvatarCount()-1;ii++) {
								boxes.get(i*2+1).add(images[ii],main.config.heroes.get(index).getAvatar(ii).getName());
								boxes.get(i*2+1).setVisible(true);
							}
							
							boxes.get(i*2+1).setEnabled(!(main.config.getAvatarIndex(main.config.heroes.get(index),-1) >= 0));
							boxes.get(i*2+1).setSelectedIndex(main.config.getAvatarIndex(main.config.heroes.get(index),i));
						}
						for(int i = main.config.heroes.get(index).getAvatarCount()*2;i <= boxes.size()-1;i++) {
							boxes.get(i).setVisible(false);
							
							icons.get(i/2).setVisible(false);
						}
						avatarpanel.setSize(500,5+main.config.heroes.get(index).getAvatarCount()*(39+5));
						
						//Global boxes
						checkbox.setEnabled(true);
						checkbox.setSelected(main.config.getAvatarIndex(main.config.heroes.get(index),-1) >= 0);
						combobox.clear();
						for(int i = 0;i <= main.config.heroes.get(index).getAvatarCount()-1;i++) {
							combobox.add(images[i],main.config.heroes.get(index).getAvatar(i).getName());
						}
						combobox.setEnabled(main.config.getAvatarIndex(main.config.heroes.get(index),-1) >= 0);
						if(main.config.getAvatarIndex(main.config.heroes.get(index),-1) >= 0) {
							combobox.setSelectedIndex(main.config.getAvatarIndex(main.config.heroes.get(index),-1));
						}
					}
					change = true;
				}
			}
		);
		checkbox.addActionListener(
			new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					int index = herolist.getSelectedIndex();
					
					if(checkbox.isSelected()) {
						main.config.property.setProperty(main.config.heroes.get(index).getName(),main.config.heroes.get(index).getAvatar(combobox.getSelectedIndex()).getKey());
					}
					else {
						main.config.property.setProperty(main.config.heroes.get(index).getName(),null);
					}

					combobox.setEnabled(main.config.getAvatarIndex(main.config.heroes.get(index),-1) >= 0);
					for(int i = 0;i <= main.config.heroes.get(index).getAvatarCount()-1;i++) {
						boxes.get(i*2+1).setEnabled(!(main.config.getAvatarIndex(main.config.heroes.get(index),-1) >= 0));
					}
				}
			}
		);
		combobox.addActionListener(
			new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					if(change && herolist.getSelectedIndex() != -1) {
						//System.out.println("Saved:"+main.config.heroes.get(herolist.getSelectedIndex()).getAvatar(combobox.getSelectedIndex()).getKey());
						main.config.property.setProperty(main.config.heroes.get(herolist.getSelectedIndex()).getName(),main.config.heroes.get(herolist.getSelectedIndex()).getAvatar(combobox.getSelectedIndex()).getKey());
					}
				}
			}
		);

		//Indicator page;
		CPanel indicator_hero_panel = new CPanel(744,287);
		hero_tab.addTab("Indicators",indicator_hero_panel.component());
		hero_tab.setEnabledTab(2-1,false);
		
		//Announcer page;
		CPanel announcer_panel = new CPanel(1000,360);
		tab.addTab("Announcers",announcer_panel.component());
		tab.setEnabledTab(2-1,false);
		
		//Taunt page;
		CPanel taunt_panel = new CPanel(1000,360);
		tab.addTab("Taunts",taunt_panel.component());
		tab.setEnabledTab(3-1,false);
		
		//End of the frame;
		panel.add(progressbar = new JProgressBar(),5,310+28+5);
		progressbar.setSize(new Dimension(250,15));
		progressbar.setValue(100);
		panel.add(progresslabel = new JLabel("Finished request in 0ms"),5+250+5,310+28+5);
		progresslabel.setSize(new Dimension(750,15));
		progresslabel.setEnabled(false);
		
		//Finishing the frame;
		finish();
		addWindowListener(
			new WindowAdapter() {
				public void windowClosing(WindowEvent we) {
					main.exit();
				}
			}
		);
	}
	//Set;
	
	//Get;
	
	//Add;
	
	//Remove;
	
	//Do;
	
	//Other;
	
	//Implements;
	@Override
	public void actionPerformed(ActionEvent e) {
		if(!run.isAlive()) {
			action = Integer.parseInt(e.getActionCommand());
			
			run = new Thread(this);
			run.start();
		}
		else {
			JOptionPane.showMessageDialog(null,"asd");
		}
	}
	@Override
	public void run() {
		long time = System.currentTimeMillis();
		boolean success = true;
		String command = "";
		progressbar.setValue(0);
		
		if(action == Gui.ACTION_APPLY) {
			success = main.config.compile();
			command = "Applying Modifcations";
		}
		else if(action == Gui.ACTION_UPDATE) {
			success = main.config.update();
			if(success) {
				try {
					String[] cmd = {"java","-jar", Main.PATH + "jcustom.jar"};
					Runtime.getRuntime().exec(cmd);
					
					System.exit(0);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			command = "Update Check";
		}
		else if(action == Gui.ACTION_REFRESH) {
			main.config.refresh();
			command = "Refreshing";
		}
		else if(action == Gui.ACTION_APPLYNLAUNCH) {
			success = main.config.compile();
			
			if(success) {
				try {
					String[] cmd = {new LLFile(main.config.property.getProperty("Setting_Resources",""),false).getParentFile().getParentFile().getAbsolutePath()+File.separator+"hon.exe"};
					Runtime.getRuntime().exec(cmd);
					
					main.exit();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
		}
		else if(action == Gui.ACTION_UNAPPLY) {
			command = "Unapplying modifcations";
			try {
				File file = new LLFile(new LLFile(main.config.property.getProperty("Setting_Resources",""),false).getParentFile().getAbsolutePath()+File.separator+"resourcesCoN.s2z",false);
				success = file.delete();
			} catch (IOException e) {
				try {
					main.log.print(e,"Failed to access to "+new LLFile(main.config.property.getProperty("Setting_Resources",""),false).getParentFile().getAbsolutePath()+File.separator+"resourcesCoN.s2z",true);
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
			
			if(!success) {
				JOptionPane.showMessageDialog(null,"Failed to unapply modifications, make sure no proccess, for example hon.exe is running");
			}
			
		}
		else {
			return;
		}
		
		progressbar.setValue(progressbar.getMaximum());
		progresslabel.setText((success ? "Finished" : "Failed to")+" "+command+" "+(success ? "in" : "after")+" "+LLAccessories.toStringNumber((System.currentTimeMillis()-time)+"")+"ms.");
	}
}
