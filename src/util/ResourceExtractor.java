package util;

import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.Vector;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import util.ent.Avatar;
import util.ent.Hero;

public class ResourceExtractor {
	// STATIC Variables

	// STATIC Methods

	// Variables
	public NameTable nameTable;
	
	public Vector<Hero>	heroes	= new Vector<Hero>();
	public Vector<Avatar>	avatars	= new Vector<Avatar>();

	// Constructors
	public ResourceExtractor(ZipFile resources) throws IOException {
		extract(resources);
	}
	
	//Fetching...
	private void extract(ZipFile r) throws IOException {
		
		//NameTable
		try(InputStream is = r.getInputStream(r.getEntry("stringtables/interface_en.str"))) {
			nameTable = new NameTable(is);
		}
		
		//Content(Avatars mainly, possibly taunts, curiours etc in future)
		ZipEntry entry = r.getEntry("content/store_avatars.package");
		try(InputStream is = r.getInputStream(entry)) {
			extractContent(is);
		} catch (ParserConfigurationException | SAXException e) {
			throw new RuntimeException("Could not load or parse " + r.getName() + ", " + entry.toString(), e);
		}
		
		//Fetching entities
		extractEntities(r);
	}
	private void extractEntities(ZipFile resources) throws IOException {
		Enumeration<? extends ZipEntry> entries = resources.entries();
		
		while(entries.hasMoreElements()) {
			ZipEntry entry = entries.nextElement();
			String entryString = entry.toString();
			
			if(entryString.startsWith("heroes/") && entryString.endsWith(".entity")) {
				try {
					extractEntity(resources.getInputStream(entry), entryString);
				} catch (ParserConfigurationException | SAXException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}
	private void extractEntity(InputStream is, String entryString) throws ParserConfigurationException, SAXException, IOException {
		DocumentBuilder db = DocumentBuilderFactory.newInstance().newDocumentBuilder();
		Element element = db.parse(is).getDocumentElement();
		String tagName = element.getTagName();
		
		if(tagName.equalsIgnoreCase("hero")) {
			Hero hero = new Hero(entryString, element);
			
			heroes.add(hero);
		}
		else {
			//System.out.println("Undentified entity: " + tagName + " at " + entryString);
		}
	}
	
	private void extractContent(InputStream is) throws ParserConfigurationException, SAXException, IOException {
		DocumentBuilder db = DocumentBuilderFactory.newInstance().newDocumentBuilder();
		Element element = db.parse(is).getDocumentElement();
		
		NodeList list = element.getElementsByTagName("instance");
		for(int i = 0;i < list.getLength();i++) {
			extractFasion((Element) list.item(i));
		}
	}
	private void extractFasion(Element element) {
		String fashionType = element.getAttribute("name");
		
		if(fashionType.equalsIgnoreCase("altAvatarPreviewPanel")) { //This is an Avatar
			String product	= element.getAttribute("product");
			String id		= element.getAttribute("id");
			String ultimate	= element.getAttribute("ultimateAvatar");
			
			Avatar avatar = new Avatar(product, id);
			avatar.setEnabled(ultimate.equalsIgnoreCase("true"));
			avatars.add(avatar);
		}
		//else if(courierPreviewPanel, tauntPreviewPanel)
	}
	
	//Transform...
}
