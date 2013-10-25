package Interface;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

public class LLGui {
	public static int show(String title,String message,String details,String[] buttons) {
		
		final JDialog dialog = new JDialog();
		CPanel panel = new CPanel(500,400);
		
		JTextArea a;
		JButton b;
		
		a = new JTextArea();
		a.setLineWrap(true);
		a.setWrapStyleWord(true);
		a.setText(message);
		a.setBackground(panel.getBackgroundColor());
		a.setSize(new Dimension(480,1));
		a.setFocusable(false);
		a.setEditable(false);
		panel.add(a,10,10,true);
		
		a = new JTextArea();
		a.setTabSize(1);
		a.setText(details);
		a.setEditable(false);
		JScrollPane pane = new JScrollPane(a);
		pane.setSize(new Dimension(480,250));
		pane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		pane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
		panel.add(pane,10,100);
		
		int pos = 500-110*buttons.length;
		for(int i = 0;i <= buttons.length-1;i++) {
			b = new JButton(buttons[i]);
			b.setSize(new Dimension(100,25));
			
			final int n = i;
			b.addActionListener(
				new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						dialog.setName(n+"");
						dialog.setVisible(false);
					}
				}
			);
			
			panel.add(b,pos+i*110,100+250+10);
			
			
		}
		

		dialog.setName(-1+"");
		dialog.setTitle(title);
        dialog.setModal(true);
        dialog.setContentPane(panel.component());
        dialog.pack();
        dialog.setLocationRelativeTo(null);
        dialog.setVisible(true);
        
        return Integer.parseInt(dialog.getName());
	}
}
