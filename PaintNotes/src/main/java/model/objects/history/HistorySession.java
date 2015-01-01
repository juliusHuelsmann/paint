package model.objects.history;

//import declaration
import model.util.list.List;


/**
 * History class which contains the history of the current session.
 * @author Julius Huelsmann
 * @version %I%, %U%
 */
public class HistorySession {


	/**
	 * The list of history objects of one session.
	 */
	private List<HistoryObject> ls_history;
	
	
	/**
	 * Constructor of history session.
	 */
	public HistorySession() {
		ls_history = new List <HistoryObject>();
	}
	
	
	
	/**
	 * Add a history item behind the current item. If there are items behind
	 * it, remove them. After the action, the current element of the list is
	 * the new one.
	 * 
	 * @param _newContent 
	 * 						the new HistoryObject
	 */
	public final void addHistoryItem(final HistoryObject _newContent) {
		
		
		ls_history.insertBehind(_newContent);
		
		while (ls_history.isBehind()) {
			
			ls_history.remove();
		}
	}
	
	
	
	/**
	 * Apply the previous.
	 */
	public void applyPrevious() {
		
	}
}

