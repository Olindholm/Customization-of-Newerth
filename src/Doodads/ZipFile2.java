package Doodads;

import java.io.File;
import java.io.IOException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipException;
import java.util.zip.ZipFile;

public class ZipFile2 extends ZipFile {

	public ZipFile2(String name) throws IOException {
		super(name);
		// TODO Auto-generated constructor stub
	}
	
	public ZipFile2(File file) throws ZipException, IOException {
		super(file);
		// TODO Auto-generated constructor stub
	}
	
	@Override
	public ZipEntry getEntry(String name) {
		return super.getEntry(name.toLowerCase()); // Now, non-case sensitive.
	}
	
}