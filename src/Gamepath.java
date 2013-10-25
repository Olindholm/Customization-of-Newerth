import java.io.File;
import javax.swing.JOptionPane;

public class Gamepath {
	Main main;
	
	public Gamepath(Main main) {
		this.main = main;
		
		String path = getPath();
		if(path.isEmpty()) {
			while(new File(path).exists() == false) {
				path = JOptionPane.showInputDialog(null,"Could not find your game folder, please insert it.");
				if(path.isEmpty()) {
					System.exit(0);
				}
				path += File.separator;
				path = path.replace(File.separator+File.separator,File.separator);
			}
		}
		main.config.config.setProperty("gamepath",path);
		System.out.println(path);
	}
	
	public String getPath() {
		if (main.config.config.getProperty("gamepath") != null){
			String path = main.config.config.getProperty("gamepath");
			if(new File(path).exists() == true) {
				return path;
			}
		}
		if(System.getProperty("os.name").toLowerCase().contains("windows")) {
			String[] honFolder = {"C:\\Program Files\\Heroes of Newerth\\", "C:\\Program Files (x86)\\Heroes of Newerth\\"};
			for (int i = 0; i < honFolder.length; i++) {
				File f = new File(honFolder[i]);
				if (f.exists()) {
					return f.getAbsolutePath();
				}
			}
			// Get the folder from uninstall info in windows registry saved by HoN
	        String registryData = WindowsRegistry.readRegistry("SOFTWARE\\Microsoft\\Windows\\CurrentVersion\\Uninstall\\hon", "InstallLocation");
	        if (registryData != null && !registryData.isEmpty()) {
	            return registryData;
	        }
	        registryData = WindowsRegistry.readRegistry("SOFTWARE\\Notausgang\\HoN_ModMan", "hondir");
	        if (registryData != null && !registryData.isEmpty()) {
	            return registryData;
	        }
	        registryData = WindowsRegistry.readRegistry("SOFTWARE\\Wow6432Node\\Microsoft\\Windows\\CurrentVersion\\Uninstall\\hon", "InstallLocation");
	        if (registryData != null && !registryData.isEmpty()) {
	            return registryData;
	        }
	        // From Notausgang's ModManager
	        registryData = WindowsRegistry.readRegistry("SOFTWARE\\Notausgang\\HoN_ModMan", "hondir");
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
	public String findModFolder(String gameFolder) {
        // Try to find HoN folder in case we are on Windows
		if(System.getProperty("os.name").toLowerCase().contains("windows")) {
            if (gameFolder != null) {
                File folder = new File(gameFolder + File.separator + "game" + File.separator + "mods");
                if (folder.exists() && folder.isDirectory()) {
                    return folder.getAbsolutePath();
                }
            }
        }
        // Try to find HoN folder in case we are on Linux
        if (System.getProperty("os.name").toLowerCase().contains("linux")) {
            File folder = new File(System.getProperty("user.home") + File.separator + ".Heroes of Newerth" + File.separator + "mods");
            if (folder.exists() && folder.isDirectory()) {
                return folder.getAbsolutePath();
            }
            if (gameFolder != null) {
                folder = new File(gameFolder + File.separator + "game" + File.separator + "mods");
                if (folder.exists() && folder.isDirectory()) {
                    return folder.getAbsolutePath();
                }
            }
        }
        // Try to find HoN folder in case we are on Mac
        if (System.getProperty("os.name").toLowerCase().contains("mac")) {
            File a = new File(System.getProperty("user.home") + File.separator + "Library/Application Support/Heroes of Newerth/game/mods");
            return a.exists() ? a.getPath() : null;
        }

        return null;
    }
}
