package control.util;

import java.awt.Component;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

public class WindowFocusListener implements WindowListener {

	private Component c_focus;
	
	public WindowFocusListener(
			final Component _c_focus) {
		
		this.c_focus = _c_focus;
	}
	

	public final void windowOpened(WindowEvent e) { }
	
	public final void windowIconified(WindowEvent e) { }
	
	public final void windowDeiconified(WindowEvent e) { }
	
	public final void windowDeactivated(WindowEvent e) { }
	
	public final void windowClosing(WindowEvent e) { }
	
	public final void windowClosed(WindowEvent e) { }
	
	public final void windowActivated(WindowEvent e) {
		c_focus.requestFocus();
	}
	
}
