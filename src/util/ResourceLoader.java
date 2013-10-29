package util;

import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.Scanner;
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

public class ResourceLoader {
	// STATIC Variables

	// STATIC Methods
	public static String convertStreamToString(InputStream is) {
		String reString = "";
		
		Scanner s = new Scanner(is, "UTF-8");
		s.useDelimiter("\\A");
		if(s.hasNext()) reString = s.next();
		
		s.close();
		return reString;
	}

	// Variables
	NameTable nameTable;
	
	Vector<Hero>	heroes	= new Vector<Hero>();
	Vector<Avatar>	avatars	= new Vector<Avatar>();

	// Constructors
	public ResourceLoader(ZipFile resources) throws IOException {
		fetchResources(resources);
		transformResources();
	}
	
	//Fetching...
	private void fetchResources(ZipFile resources) throws IOException {
		ZipEntry entry;
		InputStream in;
		
		//NameTable
		entry = resources.getEntry("stringtables/interface_en.str");
		in = resources.getInputStream(entry);
		nameTable = new NameTable(in);
		
		//Content(Avatars mainly, possibly taunts, curiours etc in future)
		entry = resources.getEntry("content/store_avatars.package");
		in = resources.getInputStream(entry);
		try {
			fetchContent(in);
		} catch (ParserConfigurationException | SAXException e) {
			throw new RuntimeException("Could not load or parse " + resources.getName() + ", " + entry.toString(), e);
		}
		
		//Fetching entities
		fetchEntities(resources);
	}
	private void fetchEntities(ZipFile resources) throws IOException {
		Enumeration<? extends ZipEntry> entries = resources.entries();
		
		while(entries.hasMoreElements()) {
			ZipEntry entry = entries.nextElement();
			String entryString = entry.toString();
			
			if(entryString.startsWith("heroes/") && entryString.endsWith(".entity")) {
				try {
					fetchEntity(resources.getInputStream(entry), entryString);
				} catch (ParserConfigurationException | SAXException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}
	private void fetchEntity(InputStream is, String entryString) throws ParserConfigurationException, SAXException, IOException {
		DocumentBuilder db = DocumentBuilderFactory.newInstance().newDocumentBuilder();
		Element element = db.parse(is).getDocumentElement();
		String tagName = element.getTagName();
		
		if(tagName.equalsIgnoreCase("hero")) {
			Hero hero = new Hero(entryString, element);
			
			heroes.add(hero);
		}
		else {
			System.out.println("Undentified entity: " + tagName + " at " + entryString);
		}
	}
	
	private void fetchContent(InputStream is) throws ParserConfigurationException, SAXException, IOException {
		DocumentBuilder db = DocumentBuilderFactory.newInstance().newDocumentBuilder();
		Element element = db.parse(is).getDocumentElement();
		
		NodeList list = element.getElementsByTagName("instance");
		for(int i = 0;i < list.getLength();i++) {
			fetchFasion((Element) list.item(i));
		}
	}
	private void fetchFasion(Element element) {
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
	private void transformResources() {
		
	}
}
