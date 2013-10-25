package Interface;

import java.awt.Component;
import java.lang.reflect.InvocationTargetException;
import java.util.Vector;

import javax.swing.DefaultListModel;
import javax.swing.JList;
import javax.swing.ListSelectionModel;
import javax.swing.SwingUtilities;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import Engine.Attributes.Hero;

public class CList implements ListSelectionListener {
	public final static int SINGLE_SELECTION = ListSelectionModel.SINGLE_SELECTION;
	
	DefaultListModel model = new DefaultListModel();
	JList list = new JList();
	
	private	Vector<ListSelectionListener>	listeners = new Vector<ListSelectionListener>();
	
	int index = -1;
	int lastindex = -1;
	
	public CList() {
		list.setModel(model);
		list.addListSelectionListener(this);
	}
	
	public void add(final String item) {
		try {
			SwingUtilities.invokeAndWait(new Runnable() {
				public void run() {
					model.addElement(item);
				}
			});
		} catch (InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void clear() {
		try {
			SwingUtilities.invokeAndWait(
				new Runnable() {
					public void run() {
						model.clear();
					}
				}
			);
		} catch (InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public int getSelectedIndex() {
		return list.getSelectedIndex();
	}
	public int getLastSelectedIndex() {
		return lastindex;
	}
	
	public void setSelectionMode(int mode) {
		list.setSelectionMode(mode);
	}
	
	public void addListSelectionListener(ListSelectionListener listener) {
		listeners.add(listener);
	}
	
	public Component component() {
		return list;
	}

	@Override
	public void valueChanged(ListSelectionEvent e) {
		lastindex = index;
		index = this.getSelectedIndex();
		
		for(int i = 0;i <= listeners.size()-1;i++) {
			listeners.get(i).valueChanged(e);
		}
	}
}
