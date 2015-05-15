package model.debug;


/**
 * This Implementation of Action contains all the actions that are performed
 * directly by the user interacting with the graphical user interface.
 * 
 * @author Julius Huelsmann
 * @version %I%, %U%
 */
public class UserAction implements Action {

	
	/**
	 * The title of the userAction.
	 */
	private String title;
	
	/**
	 * Whether the userAction is started or terminated.
	 */
	private boolean start;
	
	
	
	/**
	 * Constructor: saves title and whether the action is started or terminated.
	 * @param _title	the title that is saved.
	 * @param _start	whether the action is started or terminated.
	 */
	public UserAction(final String _title, final boolean _start) {
		this.title = _title;
		this.start = _start;
	}
	
	
	/**
	 * {@inheritDoc}
	 */
	public final String getString() {
		if (start) {
			return "UA\tStart: \t\t" + title;
		} else {
			return "UA\t finished: \t" + title;
		}
	}
}
