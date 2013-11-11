package res;

import res.ent.Hero;

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
		
		return hero;
	}
}
