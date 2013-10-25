import java.awt.Dimension;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JProgressBar;
import javax.swing.JTextArea;

public class Update {
	public Update(Main main) {
		//update
		ArrayList<String> versions = new ArrayList<String>();
		versions.add(main.config.version);
		this.main = main;
		URL verionsFile = null;
		URLConnection connection = null;
		try {
			verionsFile = new URL(link+"version.txt");
		} catch (MalformedURLException e1) {
			JOptionPane.showMessageDialog(null,"Could not find which server the version file was located.");
			e1.printStackTrace();
		}
		try {
			connection = verionsFile.openConnection();
			connection.setConnectTimeout(10000);
		} catch (IOException e1) {
			JOptionPane.showMessageDialog(null,"Could not connect to the update server.");
			e1.printStackTrace();
		}
		BufferedReader br;
		try {
			br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
			while (br.ready()) {
				versions.add(br.readLine());
			}
			br.close();
		} catch (IOException e1) {
			JOptionPane.showMessageDialog(null,"Could not download update data correctly.");
			e1.printStackTrace();
		}
		if(versions.get(versions.size()-1).equals(this.main.config.version) != true) {
			String[] option = {"Yes","No"};
			if(JOptionPane.showOptionDialog(this.main.gui.frame,versions.get(versions.size()-1)+" is out!\nWould you like to download and install it?","Update available!",JOptionPane.YES_NO_OPTION,-1,null,option,option[1]) == JOptionPane.YES_OPTION) {
				this.main.gui.frame.setVisible(false);
				ImageIcon image = new ImageIcon(getClass().getResource("icon.png"));
				frame = new JFrame(this.main.config.name+" - Updater");
				frame.setIconImage(image.getImage());
					Panel panel_frame = new Panel(this.main);
					panel_frame.setPreferredSize(new Dimension(500,250));
						JProgressBar progress = new JProgressBar(0,100);
						progress.setPreferredSize(new Dimension(490,25));
					panel_frame.add(progress,5,5);
				frame.add(panel_frame);
				frame.pack();
				frame.setLocationRelativeTo(null);
				frame.setVisible(true);
				frame.addWindowListener(new WindowAdapter() {
					public void windowClosing(WindowEvent e) {
						//whatever
						start = false;
					}
				});
				URL url = null;
				try {
					url = new URL(link+versions.get(versions.size()-1)+"/(CoN).jar");
				} catch (MalformedURLException e1) {
					JOptionPane.showMessageDialog(null,"Could not find which server the updated client was located.");
					e1.printStackTrace();
				}
				URLConnection con = null;
				try {
					con = url.openConnection();
					con.setConnectTimeout(10000);
				} catch (IOException e1) {
					JOptionPane.showMessageDialog(null,"Could not connect to the update server. Error number: 2");
					e1.printStackTrace();
				}
				progress.setMaximum(con.getContentLength()*2);
				File f = new File(App_Data+File.separator+"Lindholm"+File.separator+"Customization_of_Newerth"+File.separator+"(CoN).jar");
				try {
					InputStream in = con.getInputStream();
					OutputStream out = new FileOutputStream(f);
					copyInputStream(in,out,progress);
					in.close();
					out.close();
					FileInputStream fis = new FileInputStream(f);
					File managerJar = new File(currDir);
					FileOutputStream fos = new FileOutputStream(managerJar);
					copyInputStream(fis,fos,progress);
					in.close();
					out.close();
				} catch (IOException e1) {
					JOptionPane.showMessageDialog(null,"Could not download the client correctly.");
					e1.printStackTrace();
				}
				if(start == true) {
					String[] cmd = {"java", "-jar", currDir};
					try {
						Runtime.getRuntime().exec(cmd);
					} catch (IOException e1) {
						e1.printStackTrace();
					}
				}
				System.exit(0);
			}
		}
		//changelog
		JTextArea edit = main.gui.changelog_edit;
		String changelog = "";
		//ArrayList<String> change = new ArrayList<String>();
		URL changelogfile = null;
		URLConnection changelogconnection = null;
		try {
			changelogfile = new URL(link+main.config.version+"/changelog.txt");
		} catch (MalformedURLException e1) {
			edit.setText("Failed to find changelog server.");
			e1.printStackTrace();
		}
		try {
			changelogconnection = changelogfile.openConnection();
			changelogconnection.setConnectTimeout(10000);
		} catch (IOException e) {
			edit.setText("Failed to connect to changelog server.");
			e.printStackTrace();
		}
		BufferedReader changelogbr;
		try {
			changelogbr = new BufferedReader(new InputStreamReader(changelogconnection.getInputStream()));
			while (changelogbr.ready()) {
				changelog += "\n"+changelogbr.readLine();
				//change.add(changelogbr.readLine());
			}
			changelogbr.close();
			edit.setText(changelog.replaceFirst("\n",""));
		} catch (IOException e1) {
			edit.setText("Failed to download changelog.");
			e1.printStackTrace();
		}
		//buglog
		edit = main.gui.buglog_edit;
		String buglog = "";
		//ArrayList<String> change = new ArrayList<String>();
		URL buglogfile = null;
		URLConnection buglogconnection = null;
		try {
			buglogfile = new URL(link+"/buglog.txt");
		} catch (MalformedURLException e1) {
			edit.setText("Failed to find buglog server.");
			e1.printStackTrace();
		}
		try {
			buglogconnection = buglogfile.openConnection();
			buglogconnection.setConnectTimeout(10000);
		} catch (IOException e) {
			edit.setText("Failed to connect to buglog server.");
			e.printStackTrace();
		}
		BufferedReader buglogbr;
		try {
			buglogbr = new BufferedReader(new InputStreamReader(buglogconnection.getInputStream()));
			while (buglogbr.ready()) {
				buglog += "\n"+buglogbr.readLine();
				//change.add(changelogbr.readLine());
			}
			buglogbr.close();
			edit.setText(buglog.replaceFirst("\n",""));
		} catch (IOException e1) {
			edit.setText("Failed to download buglog.");
			e1.printStackTrace();
		}
	}
	Main main;
	
	JFrame frame;
	
	String currDir = new File("(CoN).jar").getAbsolutePath();
	String App_Data = System.getenv("APPDATA");
	String link = "http://dl.dropbox.com/u/38414202/Java/Customization_of_Newerth/";
	boolean start = true;
	String string;
	public static void copyInputStream(InputStream in, OutputStream out, JProgressBar bar) throws IOException {
		byte[] buffer = new byte[1024];
		int len;
		while (((len = in.read(buffer)) >= 0)) {
			if (bar != null) {
				bar.setValue(bar.getValue() + 1024);
			}
			out.write(buffer, 0, len);
		}
	}
}
