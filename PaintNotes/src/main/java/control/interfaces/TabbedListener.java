package control.interfaces;


/**
 * Listener class for TabbedPane.
 * @author Julius Huelsmann
 * @version %I%, %U%
 *
 */
public interface TabbedListener {

	
	/**
	 * The event which is thrown if the TabbedPane is closed.
	 */
	void closeListener();
	

	/**
	 * The event which is thrown if the TabbedPane is moved.
	 * @param _event 
	 * 			the MoveEvent containing the current coordinates of the ending
	 * 			of the TabbedPane's visibility scope, thus the location
	 * 			added to the visible size.
	 * 
	 */
	void moveListener(final MoveEvent _event);
	
	/**
	 * The event which is thrown if the TabbedPane is opened.
	 */
	void openListener();
	
	
}
