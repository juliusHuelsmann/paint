package model.debug;

/**
 * This Implementation of Action contains all the actions that are performed
 * by model classes and not directly by the user.
 * 
 * @author Julius Huelsmann
 * @version %I%, %U%
 */
public class ProgramAction implements Action {

	
	/**
	 * The title of the userAction.
	 */
	private String title;
	
	
	/**
	 * Constructor: saves title of the action.
	 * @param _title	the title that is saved.
	 */
	public ProgramAction(final String _title) {
		this.title = _title;
	}
	
	
	/**
	 * {@inheritDoc}
	 */
	public final String getString() {
		return "PA\t" + title;
	}
}
