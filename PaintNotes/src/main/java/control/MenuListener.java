package control;


/**
 * The menuListener provides an interface for listening for menu - closing
 * and menu - opening events.
 * 
 * @author Julius Huelsmann
 * @version %I%, %U%
 *
 */
public interface MenuListener {

	
	/**
	 * Event which is fired before the MenuItem is opened.
	 */
	void beforeOpen();
	

	/**
	 * Event which is fired before the MenuItem is closed.
	 */
	void beforeClose();
	

	/**
	 * Event which is fired after the MenuItem is opened.
	 */
	void afterOpen();


	/**
	 * Event which is fired after the MenuItem is closed.
	 */
	void afterClose();
}
