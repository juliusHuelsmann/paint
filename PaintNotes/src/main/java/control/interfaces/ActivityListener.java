package control.interfaces;

import java.awt.event.MouseEvent;


/**
 * Activity Listener interface is a general Listener-interface for a bunch
 * of activities that may occur.
 * 
 * The only constraint for the applicability is, that the event that is passed
 * to the activityListener is a MouseEvent.
 * 
 * @author Julius Huelsmann
 * @version %I%, %U%
 */
public interface ActivityListener {

	/**
	 * Is called each time a certain activity occurs.
	 * @param _event 	the passed MouseEvent.
	 */
	void activityOccurred(final MouseEvent _event);
}
