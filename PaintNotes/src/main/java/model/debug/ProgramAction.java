package model.debug;

/**
 * This Implementation of Action contains all the actions that are performed
 * by model classes and not directly by the user.
 * 
 * @author Julius Huelsmann
 * @version %I%, %U%
 */
public class ProgramAction extends Action {

	
	/**
	 * The title of the userAction.
	 */
	private String title;
	
	
	/**
	 * Constructor: saves title of the action.
	 * @param _title	the title that is saved.
	 */
	public ProgramAction(final String _title,
			final long _time) {
		super(_time);
		this.title = _title;
	}
	
	
	/**
	 * {@inheritDoc}
	 */
	public final String getString() {
		return getTime() + "\tPA\t" + title;
	}
}
