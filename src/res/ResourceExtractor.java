package res;

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

import res.ent.Hero;


public class ResourceExtractor {
	// STATIC Variables

	// STATIC Methods

	// Variables
	public NameTable nameTable;
	
	public Vector<Hero>		heroes = new Vector<Hero>();

	// Constructors
	public ResourceExtractor(ZipFile r) throws IOException {
		
		//NameTable
		try(InputStream is = r.getInputStream(r.getEntry("stringtables/interface_en.str"))) {
			nameTable = new NameTable(is);
		}
		//Fetching entities
		Enumeration<? extends ZipEntry> entries = r.entries();
		
		while(entries.hasMoreElements()) {
			ZipEntry entry = entries.nextElement();
			String entryString = entry.toString();
			
			if(entryString.startsWith("heroes/") && entryString.endsWith(".entity")) {
				try {
					DocumentBuilder db = DocumentBuilderFactory.newInstance().newDocumentBuilder();
					Element element = db.parse(r.getInputStream(entry)).getDocumentElement();
		
					extract(element, entryString);
				} catch (ParserConfigurationException | SAXException e) {
					// Bad Entity;
					e.printStackTrace();
				}
			}
		}
		
		//Content(Avatars mainly, possibly taunts, curiours etc in future)
		ZipEntry entry = r.getEntry("content/store_avatars.package");
		try(InputStream is = r.getInputStream(entry)) {
			DocumentBuilder db = DocumentBuilderFactory.newInstance().newDocumentBuilder();
			Element element = db.parse(is).getDocumentElement();
			
			NodeList list = element.getElementsByTagName("instance");
			for(int i = 0;i < list.getLength();i++) {
				extract((Element) list.item(i), null);
			}
		} catch (ParserConfigurationException | SAXException e) {
			throw new RuntimeException("Could not load or parse " + r.getName() + ", " + entry.toString(), e);
		}
	}
	
	
	private void extract(Element element, String entryString) {
		
		String tagName = element.getTagName();
		if(tagName.equalsIgnoreCase("hero")) {
			String heroName = element.getAttribute("name");
			String avatarName = nameTable.getHeroName(heroName);
			
			heroes.add(new Hero(entryString, element, avatarName));
		}
		else if(element.getAttribute("name").equalsIgnoreCase("altAvatarPreviewPanel")) { //If it's an alt avatar
			//.add(element);
		}
		else {
			System.out.println("Undentified entity: " + tagName + " at " + entryString);
		}
	}
}
