package Doodads;

import java.awt.Dimension;
import java.awt.image.BufferedImage;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public class DDS {
	
	static DDS dds = new DDS();
	
	private static final String DXT1 = "DXT1";
	private static final String DXT2 = "DXT2";
	private static final String DXT3 = "DXT3";
	private static final String DXT4 = "DXT4";
	private static final String DXT5 = "DXT5";
	
	private static final int DDPF_FOURCC = 0x0004;

	private static final int DDSCAPS_TEXTURE = 0x1000;

	protected static class Color {

		private int r, g, b;

		public Color() {
			this.r = this.g = this.b = 0;
		}

		public Color(int r, int g, int b) {
			this.r = r;
			this.g = g;
			this.b = b;
		}

		public boolean equals(Object o) {
			if (this == o) {
				return true;
			}
			if (o == null || getClass() != o.getClass()) {
				return false;
			}

			final Color color = (Color) o;

			if (b != color.b) {
				return false;
			}
			if (g != color.g) {
				return false;
			}
			//noinspection RedundantIfStatement
			if (r != color.r) {
				return false;
			}

			return true;
		}

		public int hashCode() {
			int result;
			result = r;
			result = 29 * result + g;
			result = 29 * result + b;
			return result;
		}
	}

	protected static DxtHeader readHeaderDxt(ByteBuffer buffer) {
		buffer.rewind();

		byte[] magic = new byte[4];
		buffer.get(magic);
		assert new String(magic).equals("DDS ");

		int version = buffer.getInt();
		assert version == 124;

		int flags = buffer.getInt();
		int height = buffer.getInt();
		int width = buffer.getInt();
		int pixels = buffer.getInt(); // ???
		int depth = buffer.getInt();
		int mipmaps = buffer.getInt();

		buffer.position(buffer.position() + 44); // 11 unused double-words

		int pixelFormatSize = buffer.getInt(); // ???
		int fourCC = buffer.getInt();
		assert fourCC == DDPF_FOURCC;

		byte[] format = new byte[4];
		buffer.get(format);
		//System.out.println(new String(format));
		assert new String(format).equals("DXT3");
		

		int bpp = buffer.getInt(); // bits per pixel for RGB (non-compressed) formats
		buffer.getInt(); // rgb bit masks for RGB formats
		buffer.getInt(); // rgb bit masks for RGB formats
		buffer.getInt(); // rgb bit masks for RGB formats
		buffer.getInt(); // alpha mask for RGB formats

		int unknown = buffer.getInt();
		assert unknown == DDSCAPS_TEXTURE;
		int ddsCaps = buffer.getInt(); // ???
		buffer.position(buffer.position() + 12);

		return dds.new DxtHeader(version, height, width, new String(format));
	}

	public static BufferedImage readDxt(ByteBuffer buffer) {
		buffer.order(ByteOrder.LITTLE_ENDIAN);
		DxtHeader header = readHeaderDxt(buffer);
		//System.out.println(header.width + ", "+ header.height + " , " + header.format);
		if(header.format.contentEquals(DXT1)) {
			return readDxt1Buffer(buffer, header.width, header.height);
		}
		else if(header.format.contentEquals(DXT2) || header.format.contentEquals(DXT3)) {
			return readDxt3Buffer(buffer, header.width, header.height);
		}
		else if(header.format.contentEquals(DXT4) || header.format.contentEquals(DXT5)) {
			return readDxt5Buffer(buffer, header.width, header.height);
		}
		
		return null;
	}

	public static BufferedImage readDxt1Buffer(ByteBuffer buffer, int width, int height) {
		buffer.order(ByteOrder.LITTLE_ENDIAN);

		int[] pixels = new int[16];
		//int[] alphas = new int[16];

		BufferedImage result = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB_PRE);

		int numTilesWide = width / 4;
		int numTilesHigh = height / 4;
		for (int i = 0; i < numTilesHigh; i++) {
			for (int j = 0; j < numTilesWide; j++) {
				// Read the alpha table.
				//long alphaData = buffer.getLong();
				/*for (int k = alphas.length - 1; k >= 0; k--) {
					alphas[k] = (int) (alphaData >>> (k * 4)) & 0xF; // Alphas are just 4 bits per pixel
					alphas[k] <<= 4;
				}*/

				short minColor = buffer.getShort();
				short maxColor = buffer.getShort();
				Color[] lookupTable = expandLookupTable(minColor, maxColor);

				int colorData = buffer.getInt();

				for (int k = pixels.length - 1; k >= 0; k--) {
					int colorCode = (colorData >>> k * 2) & 0x03;
					pixels[k] = getPixel888(lookupTable[colorCode]);
					pixels[k] = (255 << 24) | getPixel888(lookupTable[colorCode]);
				}

				result.setRGB(j * 4, i * 4, 4, 4, pixels, 0, 4);
			}
		}
		return result;
	}
	
	public static BufferedImage readDxt3Buffer(ByteBuffer buffer, int width, int height) {
		buffer.order(ByteOrder.LITTLE_ENDIAN);

		int[] pixels = new int[16];
		int[] alphas = new int[16];

		BufferedImage result = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB_PRE);

		int numTilesWide = width / 4;
		int numTilesHigh = height / 4;
		for (int i = 0; i < numTilesHigh; i++) {
			for (int j = 0; j < numTilesWide; j++) {
				// Read the alpha table.
				long alphaData = buffer.getLong();
				for (int k = alphas.length - 1; k >= 0; k--) {
					alphas[k] = (int) (alphaData >>> (k * 4)) & 0xF; // Alphas are just 4 bits per pixel
					alphas[k] <<= 4;
				}

				short minColor = buffer.getShort();
				short maxColor = buffer.getShort();
				Color[] lookupTable = expandLookupTable(minColor, maxColor);

				int colorData = buffer.getInt();

				for (int k = pixels.length - 1; k >= 0; k--) {
					int colorCode = (colorData >>> k * 2) & 0x03;
					pixels[k] = (alphas[k] << 24) | getPixel888(multiplyAlpha(lookupTable[colorCode], alphas[k]));
				}

				result.setRGB(j * 4, i * 4, 4, 4, pixels, 0, 4);
			}
		}
		return result;
	}
	
	public static BufferedImage readDxt5Buffer(ByteBuffer buffer, int width, int height) {
		buffer.order(ByteOrder.LITTLE_ENDIAN);

		int[] pixels = new int[16];
		int[] alphas = new int[16];
		
		int[] alphavalues = new int[8];

		BufferedImage result = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB_PRE);

		int numTilesWide = width / 4;
		int numTilesHigh = height / 4;
		for (int i = 0; i < numTilesHigh; i++) {
			for (int j = 0; j < numTilesWide; j++) {
				// Read the alpha table.

				long alphadata = buffer.getLong();

				alphavalues[0] = (int) ((alphadata >>> 0) & 0xFF);
				
				alphavalues[1] = (int) ((alphadata >>> 8) & 0xFF);
				
				
				
				
				if(alphavalues[0] > alphavalues[1]) {
					for(int v = 1; v<7; v++) {
						alphavalues[1+v] = ((7-v)*alphavalues[0]+v*alphavalues[1])/7;
					}
				}
				else {
					for(int v = 1; v<5; v++) {
						alphavalues[1+v] = ((5-v)*alphavalues[0]+v*alphavalues[1])/7;
					}
					alphavalues[6] = 0;
					alphavalues[7] = 255;
					
				}
				
				long alphakey = (alphadata >>> 16);
				for(int v=0; v<16;v++) {

					int subkey = (int) (alphakey >>> (v*3)) & 0x7;

					alphas[v] = alphavalues[subkey];
				}
				

				short minColor = buffer.getShort();
				short maxColor = buffer.getShort();
				Color[] lookupTable = expandLookupTable(minColor, maxColor);

				int colorData = buffer.getInt();

				for (int k = pixels.length - 1; k >= 0; k--) {
					int colorCode = (colorData >>> k * 2) & 0x03;
					pixels[k] = (alphas[k] << 24) | getPixel888(multiplyAlpha(lookupTable[colorCode], alphas[k]));
				}

				result.setRGB(j * 4, i * 4, 4, 4, pixels, 0, 4);
			}
		}
		return result;
	}

	private static Color multiplyAlpha(Color color, int alpha) {
		Color result = new Color();

		double alphaF = alpha / 256.0;

		result.r = (int) (color.r * alphaF);
		result.g = (int) (color.g * alphaF);
		result.b = (int) (color.b * alphaF);
		return result;
	}

	protected static Color getColor565(int pixel) {
		Color color = new Color();

		color.r = (int) (((long) pixel) & 0xf800) >>> 8;
		color.g = (int) (((long) pixel) & 0x07e0) >>> 3;
		color.b = (int) (((long) pixel) & 0x001f) << 3;

		return color;
	}

	private static Color[] expandLookupTable(short minColor, short maxColor) {
		Color[] result = new Color[]{getColor565(minColor), getColor565(maxColor), new Color(), new Color()};

		result[2].r = (2 * result[0].r + result[1].r + 1) / 3;
		result[2].g = (2 * result[0].g + result[1].g + 1) / 3;
		result[2].b = (2 * result[0].b + result[1].b + 1) / 3;

		result[3].r = (result[0].r + 2 * result[1].r + 1) / 3;
		result[3].g = (result[0].g + 2 * result[1].g + 1) / 3;
		result[3].b = (result[0].b + 2 * result[1].b + 1) / 3;

		return result;
	}

	protected static int getPixel888(Color color) {
		int r = color.r;
		int g = color.g;
		int b = color.b;
		return r << 16 | g << 8 | b;
	}

	class DxtHeader {
		int version;

		int flags;
		int height;
		int width;
		int pixels; // ???
		int depth;
		int mipmaps;

		int pixelFormatSize; // ???
		int fourCC;

		String format;
		

		int bpp;

		int ddsCaps; // ???
		
		public DxtHeader(int version, int height, int width, String format) {
			this.version = version;
			this.height = height;
			this.width = width;
			this.format = format;
		}
	}

}