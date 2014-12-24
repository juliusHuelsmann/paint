//package declaration
package control.util;

//import declarations
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import view.util.Item2Menu;
import view.util.VButtonWrapper;

/**
 * Controller class for CItemAufklappen (MouseListener).
 * 
 * @author Julius Huelsmann
 * @version %I%, %U%
 */
public final class CItemAufklappen implements MouseListener {

	/**
	 * the only instance of this class.
	 */
	private static CItemAufklappen instance = null;

	/**
	 * private constructor which can only be called out of
	 * this class. Realized by getInstance.
	 */
	private CItemAufklappen() { }

	
	/**
	 * this method guarantees that only one instance of this
	 * class can be created ad runtime.
	 * 
	 * @return the only instance of this class.
	 */
	public static CItemAufklappen getInstance() {
		
		//if class is not instanced yet instantiate
		if (instance == null) {
			instance = new CItemAufklappen();
		}
		
		//return the only instance of this class.
		return instance;
	}


    /**
     * {@inheritDoc}
     */
	public void mouseClicked(final MouseEvent _event) {
		fetchItemAufklappen(_event).applyMouseClicked();
	}


    /**
     * {@inheritDoc}
     */
	public void mouseEntered(final MouseEvent _event) {
		fetchItemAufklappen(_event).applyMouseEntered();
	}


    /**
     * {@inheritDoc}
     */
	public void mouseExited(final MouseEvent _event) {
		fetchItemAufklappen(_event).applyMouseExited();
	}


	/**
	 * {@inheritDoc}
	 */
	public void mousePressed(final MouseEvent _event) { }


    /**
     * {@inheritDoc}
     */
	public void mouseReleased(final MouseEvent _event) { }

    /**
     * return the ItemAufklappen out of MouseEvent.
     * @param _event the event
     * @return the ItemAufklappen.
     */
    private Item2Menu fetchItemAufklappen(final MouseEvent _event) {

        VButtonWrapper wb = (VButtonWrapper) (_event.getSource());
        return (Item2Menu) wb.wrapObject();
        
    }
}
