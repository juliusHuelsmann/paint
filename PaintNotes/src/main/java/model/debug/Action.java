package model.debug;


/**
 * 
 * @author Julius Huelsmann
 * @version %I%, %U%
 */
public abstract class Action {

	private final long time;
	
	public Action(final long _time) {
		this.time = _time;
	}
	
	
	
	
	/**
	 * Convert the Action into a string, which is returned.
	 * @return		the action converted into a string which contains all the
	 * 				necessary information on the action.
	 */
	public abstract String getString();




	/**
	 * @return the time
	 */
	public long getTime() {
		return time;
	}
}
