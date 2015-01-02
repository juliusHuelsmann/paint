package control.tabs;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import view.forms.QuickAccess;

/**
 * 
 * @author Julius Huelsmann
 * @version %I%, %U%
 *
 */
public class CQuickAccess implements ActionListener, MouseListener {

	
	/**
	 * The only instance of the quick access controller class.
	 */
	private static CQuickAccess instance;
	
	/**
	 * {@inheritDoc}
	 */
	public final void mouseClicked(final MouseEvent _event) {
		if (_event.getSource().equals(
				QuickAccess.getInstance().getJlbl_outsideLeft())) {
			QuickAccess.getInstance().openLeft();
		}
	}

	
	/**
	 * {@inheritDoc}
	 */
	public void mouseEntered(final MouseEvent _event) { }

	
	/**
	 * {@inheritDoc}
	 */
	public void mouseExited(final MouseEvent _event) { }

	
	/**
	 * {@inheritDoc}
	 */
	public void mousePressed(final MouseEvent _event) { }

	
	/**
	 * {@inheritDoc}
	 */
	public void mouseReleased(final MouseEvent _event) { }

	
	/**
	 * {@inheritDoc}
	 */
	public void actionPerformed(final ActionEvent _event) { }

	
	/**
	 * Return the only instance of this class.
	 * @return the only instance of this class
	 */
	public static final CQuickAccess getInstance() {
		if (instance == null) {
			instance = new CQuickAccess();
		}
		return instance;
	}

}
