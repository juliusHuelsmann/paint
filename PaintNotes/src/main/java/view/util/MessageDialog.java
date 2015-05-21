package view.util;

import java.awt.Component;
import java.awt.ScrollPane;
import java.awt.Window;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;

import control.util.WindowFocusListener;

@SuppressWarnings("serial")
public class MessageDialog extends JFrame  {
	
	private JTextArea jta_text;
	private final Component cmp_root;
	private WindowFocusListener wfl;
	
	
	
	private MessageDialog(final String _message, final Component _cmp_root) {

		// save root component which will be disabled and re-enabled when
		// this window disposes.
		this.cmp_root = _cmp_root;
		
		jta_text = new JTextArea(_message);
		
		JScrollPane sp = new JScrollPane(jta_text);
		super.add(sp);
		
		super.setSize(
				(int) Math.max(jta_text.getPreferredSize().getWidth() + 10, 50),
				Math.max(_cmp_root.getHeight(), 50));
		super.setLocationRelativeTo(_cmp_root);
		super.setDefaultCloseOperation(DISPOSE_ON_CLOSE);

		cmp_root.setEnabled(false);
		super.setVisible(true);

		if(cmp_root instanceof Window) {
			wfl = new WindowFocusListener(this);
			Window jf = (Window) cmp_root;
			jf.addWindowListener(wfl);
		}
	}
	
	public void dispose() {
		super.dispose();
		cmp_root.setEnabled(true);
		if (wfl != null) {

			if(cmp_root instanceof Window) {
				Window jf = (Window) cmp_root;
				jf.removeWindowListener(wfl);
			}
			
		}
	}
	
	public static void showMessage(final String _message, final Component _cmp_root) {
		
		new MessageDialog(_message, _cmp_root);
	}
}
