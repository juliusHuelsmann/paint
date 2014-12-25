package control.tabs;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import view.forms.QuickAccess;
import model.settings.ViewSettings;

/**
 * 
 * @author juli
 *
 */
public class CQuickAccess implements ActionListener, MouseListener {

	private static CQuickAccess instance;
	
	/**
	 * {@inheritDoc}
	 * @param _event
	 */
	public void mouseClicked(final MouseEvent _event) {
		if (_event.getSource().equals(
				QuickAccess.getInstance().getJlbl_outsideLeft())) {
			QuickAccess.getInstance().openLeft();
		}
	}

	
	/**
	 * {@inheritDoc}
	 * @param _event
	 */
	public void mouseEntered(final MouseEvent _event) { }

	
	/**
	 * {@inheritDoc}
	 * @param _event
	 */
	public void mouseExited(final MouseEvent _event) { }

	
	/**
	 * {@inheritDoc}
	 * @param _event
	 */
	public void mousePressed(final MouseEvent _event) { }

	
	/**
	 * {@inheritDoc}
	 * @param _event
	 */
	public void mouseReleased(final MouseEvent _event) { }

	
	/**
	 * {@inheritDoc}
	 * @param _event
	 */
	public void actionPerformed(final ActionEvent _event) { }

	
	public static CQuickAccess getInstance() {
		if (instance == null) {
			instance = new CQuickAccess();
		}
		return instance;
	}

}
