package Doodads;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;

public class LLInputStream extends DataInputStream {
	public LLInputStream(InputStream in) {
		super(in);
	}
	public String readString(int length) throws IOException {
		if(length == 0) {
			return "";
		}
		if(length > super.available()) {
			length = super.available();
		}
		byte[] buffer = new byte[length];
		int read = read(buffer);
		
		return new String(buffer).substring(0,read);
	}
	public int readByteInt() throws IOException {
		byte[] buffer = new byte[1];
		read(buffer);
		return new Byte(buffer[0]).intValue();
	}
}
