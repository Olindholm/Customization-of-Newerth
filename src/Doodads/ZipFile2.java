package Doodads;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipException;
import java.util.zip.ZipFile;

public class ZipFile2 {
	
	ZipFile[] zipFiles;

	public ZipFile2(String... names) throws IOException {
		zipFiles = new ZipFile[names.length];
		
		for (int i = 0; i < names.length; i++) {
			zipFiles[i] = new ZipFile(names[i]);
		}
	}
	
	public ZipFile2(File... files) throws IOException {
		zipFiles = new ZipFile[files.length];
		
		for (int i = 0; i < files.length; i++) {
			zipFiles[i] = new ZipFile(files[i]);
		}
	}
	
	public InputStream getInputStream(ZipEntry entry) throws IOException {
		for (ZipFile zipFile : zipFiles) {
			InputStream in = zipFile.getInputStream(entry);
			
			if (in != null) {
				return in;
			}
		}
		
		return null;
	}
	
	public ArrayList<ZipEntry> entries() {
		ArrayList<ZipEntry> list = new ArrayList<ZipEntry>();
		
		for (ZipFile zipFile : zipFiles) {
			for (Enumeration<? extends ZipEntry> e = zipFile.entries(); e.hasMoreElements();) {
				list.add(e.nextElement());
			}
		}
		
		return list;
	}
	
	public ZipEntry getEntry(String name) {
		name = name.toLowerCase();
		/*
		 * NullPointerExceptions are easily caused here.
		 * Due to heroes not existing, i.e. Behemoth Atlas and POG Thunderbringer
		 */
		
		for (ZipFile zipFile : zipFiles) {
			ZipEntry entry = zipFile.getEntry(name);
			
			if (entry != null) {
				return entry;
			}
		}
		
		return null;
	}
	
}