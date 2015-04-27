package control.forms;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import control.ControlPaint;
import view.forms.QuickAccess;

/**
 * 
 * @author Julius Huelsmann
 * @version %I%, %U%
 *
 */
public class CQuickAccess implements ActionListener, MouseListener {

	
	/**
	 * Instance of the root-controller class.
	 */
	private ControlPaint cp;
	
	/**
	 * Error-checked function for getting the QuickAccess view class.
	 * @return	instance of QuickAccess.
	 */
	private QuickAccess getQuickAccess() {
		return cp.getView().getPage().getQuickAccess();
	}
	
	
	/**
	 * Constructor: save instance of controlPaint.
	 * 
	 * @param _cp 	instance of ControlPaint.
	 */
	public CQuickAccess(final ControlPaint _cp) {
		this.cp = _cp;
	}
	
	/**
	 * {@inheritDoc}
	 */
	public final void mouseClicked(final MouseEvent _event) {
		if (_event.getSource().equals(
				getQuickAccess().getJlbl_outsideLeft())) {
			getQuickAccess().openLeft();
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

}
