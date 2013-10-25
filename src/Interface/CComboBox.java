package Interface;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionListener;
import java.util.Vector;

import javax.swing.ImageIcon;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.ListCellRenderer;

public class CComboBox {
	
	JComboBox combobox = new JComboBox();
	ComboBoxRenderer cbr = new ComboBoxRenderer();

	public	Vector<ImageIcon>	images		= new Vector<ImageIcon>();
	public	Vector<String>		labels		= new Vector<String>();
	
	public CComboBox(int width,int height) {
		combobox.setSize(new Dimension(width,height));
		combobox.setRenderer(cbr);
	}
	
	public void add(ImageIcon image,String label) {
		images.add(image);
		labels.add(label);
		combobox.addItem(images.size()-1);
	}
	
	public void clear() {
		//Sets an empty so that when you clear it's not displaying old images/items.
		images.add(null);
		labels.add("");
		combobox.addItem(images.size()-1);
		combobox.setSelectedIndex(images.size()-1);
		
		images.clear();
		labels.clear();
		combobox.removeAllItems();
	}
	
	public Component component() {
		return combobox;
	}
	
	public void addActionListener(ActionListener a) {
		combobox.addActionListener(a);
	}
	
	
    class ComboBoxRenderer extends JLabel implements ListCellRenderer {
        private Font uhOhFont;

        public ComboBoxRenderer() {
            setOpaque(true);
            //setHorizontalAlignment(CENTER);
            //setVerticalAlignment(CENTER);
        }

        /*
         * This method finds the image and text corresponding
         * to the selected value and returns the label, set up
         * to display the text and image.
         */
        public Component getListCellRendererComponent(
                                           JList list,
                                           Object value,
                                           int index,
                                           boolean isSelected,
                                           boolean cellHasFocus) {
        	
            //Get the selected index. (The index param isn't
            //always valid, so just use the value.)

            if (isSelected) {
                setBackground(list.getSelectionBackground());
                setForeground(list.getSelectionForeground());
            } else {
                setBackground(list.getBackground());
                setForeground(list.getForeground());
            }

            //Set the icon and text.  If icon was null, say so.
            if(value != null) {
	            int selectedIndex = ((Integer)value).intValue();
	            //System.out.println(value);
	            
	            ImageIcon icon = images.get(selectedIndex);
	            String pet = labels.get(selectedIndex);
	            setIcon(icon);
	            
	            setText(pet);
	            setFont(list.getFont());
            }

            return this;
        }

        //Set the font and text when no image was found.
        protected void setUhOhText(String uhOhText, Font normalFont) {
            if (uhOhFont == null) { //lazily create this font
                uhOhFont = normalFont.deriveFont(Font.ITALIC);
            }
            setFont(uhOhFont);
            setText(uhOhText);
        }
    }
	public void setEnabled(boolean b) {
		combobox.setEnabled(b);
	}
	public void setVisible(boolean b) {
		combobox.setVisible(b);
	}
	public void setSelectedIndex(int index) {
		combobox.setSelectedIndex(index);
	}
	public int getSelectedIndex() {
		return combobox.getSelectedIndex();
	}
}
