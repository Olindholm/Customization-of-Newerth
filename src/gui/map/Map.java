package gui.map;

import res.ent.Product;

public interface Map {
	// STATIC Variables

	// STATIC Methods

	// Variables

	// Constructors

	// Setters
	public void setProduct(Product product, Product[] alternatives);
	public void setSelected(Product selected);

	// Getters
	public Product getProduct();
	public Product getSelected();

	// Adders

	// Removers

	// Others Methods

	// Implementation Methods

	// Internal Classes

}
