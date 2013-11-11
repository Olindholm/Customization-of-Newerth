package util;

import java.util.Vector;

import util.ent.Avatar;
import util.ent.Hero;

public class ResourceTransformer {
	// Variables
	ResourceExtractor extractor;
	
	int totalElements;
	
	// Constructors
	public ResourceTransformer(ResourceExtractor extractor) {
		this.extractor = extractor;
		totalElements = extractor.heroes.size();
	}
	
	public int totalElements() {
		return totalElements;
	}
	
	public int remainingElements() {
		return extractor.heroes.size();
	}

	public Hero nextElement() {
		Hero hero = extractor.heroes.remove(0);
		//Avatar[] avatars = getAvatarsByHeroName(hero.getName());
		
		return hero;
	}
	
	@SuppressWarnings("unused")
	private Avatar[] getAvatarsByHeroName(String heroName) {
		Vector<Avatar> avatars = extractor.avatars;
		Vector<Avatar> fittingAvatars = new Vector<Avatar>();
		
		for(int i = 0;i < avatars.size();i++) {
			Avatar avatar = avatars.get(i);
			
			if(avatar.getHeroName().equalsIgnoreCase(heroName)) {
				fittingAvatars.add(avatars.remove(i--));
			}
		}
		
		return fittingAvatars.toArray(new Avatar[fittingAvatars.size()]);
	}
	
}
