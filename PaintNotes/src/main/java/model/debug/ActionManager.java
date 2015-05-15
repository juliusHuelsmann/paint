package model.debug;

import model.util.adt.stack.Stack;



/**
 * The status class contains the method for saving the performed user actions 
 * and program answers.
 * That is done because if an error occurs, it can be reconstructed.
 * 
 * @author Julius Huelsmann
 * @version %I%, %U%
 */
public final class ActionManager {

	
	/**
	 * Stack of actions.
	 */
	private static Stack<Action> lsAction;
	
	
	/**
	 * Empty utility class constructor.
	 */
	private ActionManager() { }
	
	
	
	/**
	 * Register, that the user performed an action for debugging purpose.
	 * If an error occurs, the user actions and the auto-generated program
	 * answers are saved and the error can be reconstructed.
	 * 
	 * 
	 * @param _actionTitle		the title of the action
	 * @param _start			whether the action is started or ended.
	 */
	public static void addUserAction(
			final String _actionTitle,
			final boolean _start) {
		getLs_action().insert(new UserAction(_actionTitle, _start));
	}
	
	
	
	/**
	 * Register a program answer to a user action. This is done for debugging.
	 * 
	 * If an error occurs, the user actions and the auto-generated program
	 * answers are saved and the error can be reconstructed.
	 * 
	 * @param _title			the answer of the program which contains
	 * 							information on how the action has been 
	 * 							computed.
	 */
	public static void addAnswer(
			final String _title) {
		getLs_action().insert(new ProgramAction(_title));
	}
	
	
	
	/**
	 * This function generates a string which contains information on all the 
	 * actions that have been performed.
	 * Therefore, the saved actionStack is emptied.
	 * 
	 * @return 		a string which contains information on all the actions 
	 * 				that have been performed.
	 */
	public static String externalizeAction() {
		
		//the string that is returned at the end and into which the information
		//on the actions are inserted.
		String content = "";
		while (!getLs_action().isEmpty()) {
			content = getLs_action().getElem_last().getContent() 
					+ "\n" + content;
			getLs_action().remove();
		}
		
		//return the content.
		return content;
	}



	/**
	 * @return the ls_action
	 */
	public static Stack<Action> getLs_action() {
		
		// if the stack has not been not instantiated yet create it:
		if (lsAction == null) {
			lsAction = new Stack<Action>();
		}
		
		// return the stack.
		return lsAction;
	}
}
