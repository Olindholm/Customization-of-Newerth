package Doodads;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public class LLOutputStream extends DataOutputStream {
	public LLOutputStream(OutputStream out) {
		super(out);
	}
	public void writeString(String s) throws IOException {
		for(int ii = 0;ii <= s.length()-1;ii++) {
			writeByte((int)s.charAt(ii));
		}
	}
	public void writeByteInt(int i) throws IOException {
		if(i < 0 || i > 255) {
			try {
				throw new Exception("A byte integer is only consistant within the values of 0-255.");
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		else {
			writeByte(i);
		}
	}
}
