package model.objects.history;

//import declaration
import java.io.Serializable;
import java.util.ListIterator;

import model.objects.painting.Picture;
import model.objects.painting.po.PaintObject;
import model.settings.Status;
import model.util.adt.list.List;
import model.util.adt.list.SecureList;


/**
 * History class which contains the history of the current session.
 * @author Julius Huelsmann
 * @version %I%, %U%
 */
public class HistorySession implements Serializable {

	
	
	/**
     * Default serial version UID for being able to identify the list's 
     * version if saved to the disk and check whether it is possible to 
     * load it or whether important features have been added so that the
     * saved file is out-dated.
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Identifiers that are contained by the instances of HistoryObject.
	 * They identify on the one hand the action that is applied after performing
	 * one click at the previous or next button.
	 * 
	 * On the other hand they do also specify the type of the Object passed to
	 * a history object.
	 */
	public static final int 
	
			//Identifiers for paintObject change. If these identifiers are 
			//saved in HistoryObject, the HistoryObjects delivers tow 
			//paintObjects; one contains the previous state and the second
			//one the current state.
			//Remark: The history add identifier can be used for copy, paste 
			//and cut actions, too.
			ID_HISTORY_PO_ADD = 1,
			ID_HISTORY_PO_REMOVE = -1,
			ID_HISTORY_PO_MOVE = 2,
			ID_HISTORY_PO_RESIZE = 3,
			ID_HISTORY_POW_CHANGE_PEN = 4,
			ID_HISTORY_POW_CHANGE_CLR = 5,
			
			//Identifiers for export setting change
			//if these identifiers are saved in history object, the history
			//object contains other identifiers specifying the applied setting
			ID_HISTORY_EXP_BG = 6,
			ID_HISTORY_EXP_MARGE_LEFT = 7,
			ID_HISTORY_EXP_MARGE_RIGHT = 8,
			ID_HISTORY_EXP_MARGE_TOP = 9,
			ID_HISTORY_EXP_MARGE_BOTTOM = 10,
			ID_HISTORY_EXP_ALPHA = 11,
			ID_HISTORY_EXP_FORMAT = 12,
			
			//Identifiers for erasing. 
			//if these identifiers are saved in history object, the history
			//object contains two lists of PaintObjects:
			//	(1) one list for the paint objects that are deleted and 
			//	(2) one for the new paintObjects that are new-created and 
			//		inserted.
			ID_HISTORY_ERASE = 13,
			ID_HISTORY_SELECTION_DESTORY = 14;

	/**
	 * The list of history objects of one session.
	 * 
	 * They contain the actions that are performed if the applyNext() or 
	 * applyPrevious() methods are used.
	 * 
	 * The action is identified by one of the above initialized IDs and
	 * by an object passed to the HistoryObject.
	 * 
	 * The secureList is only used by the "add history item" and the 
	 * "apply next" and "apply previous" methods. 
	 * Thus it is save to use a secureList which opens a transaction each
	 * time one of the above mentioned methods is called.
	 */
	private SecureList<HistoryObject> ls_history;
	
	private Picture picture;
	/**
	 * Constructor of history session.
	 * 
	 * Initializes the list which contains the history items of the current
	 * session.
	 */
	public HistorySession(Picture _picture) {
		
		//initialize secure list.
		ls_history = new SecureList <HistoryObject>();
		this.picture = _picture;
	}
	
	
	
	/**
	 * Add a history item behind the current item. If there are items behind
	 * it, remove them. After the action, the current element of the list is
	 * the new one.
	 * 
	 * @param _newContent 
	 * 						the new HistoryObject
	 */
	public final synchronized void addHistoryItem(
			final HistoryObject _newContent) {
		
		if (
				//if neither the list nor the new-inserted content are null
				//it is save to add the history item.
				ls_history != null  && _newContent != null) {

			//start new transaction
			final int transactionID = ls_history.startTransaction(
					"Add History Item", 
					SecureList.ID_NO_PREDECESSOR);
			
			//insert after the current element the new content and go to its
			//successor.
			ls_history.insertBehind(_newContent, transactionID);
			ls_history.next(transactionID, SecureList.ID_NO_PREDECESSOR);

			//remove all items that are behind the new inserted one.
			while (!ls_history.isBehind()) {
				ls_history.remove(transactionID);
				ls_history.next(transactionID, SecureList.ID_NO_PREDECESSOR);
			}
			
			
			//finish the current transaction.
			ls_history.finishTransaction(transactionID);
		} else if (
				//if the list containing the history is null log an error 
				//because it is initialized inside the constructor of this
				//class and never deleted. || _newContent == null 
				//For avoiding further errors re-initialize the list.
				ls_history == null) {
			
			//throw error and reinitialize the list.
			Status.getLogger().severe("Error: list of History Items is not"
					+ " initialized. Reinitializing.");
			ls_history = new SecureList <HistoryObject>();
			
		} else {
			
			//if the content which is to be inserted is not initialized
			//throw an error. that should never happen. For further information
			//throw a stack trace.
			Status.getLogger().severe("Error: history item which is to be "
					+ "inserted is not initialized.");
			
			//print stack trace.
			new Exception("Not initialized").printStackTrace();
		}
	}
	
	
	/*
	 * Methods for creating HistoryObjects:
	 * 
	 */

	/**
	 * Create new add history object.
	 * 
	 * @param _poPrev the previous paintObject
	 * @param _poNext the next paintObject
	 * @return the new created History Object.
	 */
	public final HistoryObject createAddItem(final PaintObject _poPrev,
			final PaintObject _poNext) {
		return new HistoryObject(this, ID_HISTORY_PO_ADD, _poPrev, _poNext);
	}

	/**
	 * Create new add history object.
	 * 
	 * @param _poPrev the previous paintObject
	 * @param _poNext the next paintObject
	 * @return the new created History Object.
	 */
	public final HistoryObject createRemoveItem(final PaintObject _poPrev,
			final PaintObject _poNext) {
		return new HistoryObject(this, ID_HISTORY_PO_REMOVE, _poPrev, _poNext);
	}

	/**
	 * Create new add history object.
	 * 
	 * @param _poPrev the previous paintObject
	 * @param _poNext the next paintObject
	 * @return the new created History Object.
	 */
	public final HistoryObject createMoveItem(
			final SecureList<PaintObject> _poPrev,
			final SecureList<PaintObject> _poNext) {
		return new HistoryObject(this, ID_HISTORY_PO_MOVE, _poPrev, _poNext);
	}

	/**
	 * Create new add history object.
	 * 
	 * @param _poPrev the previous paintObject
	 * @param _poNext the next paintObject
	 * @return the new created History Object.
	 */
	public final HistoryObject createResizeItem(final PaintObject _poPrev,
			final PaintObject _poNext) {
		return new HistoryObject(this, ID_HISTORY_PO_RESIZE, _poPrev, _poNext);
	}


	/**
	 * Create new add history object.
	 * 
	 * @param _poPrev the previous paintObject
	 * @param _poNext the next paintObject
	 * @return the new created History Object.
	 */
	public final HistoryObject createResizeItem(
			final SecureList<PaintObject> _poPrev,
			final SecureList<PaintObject> _poNext) {
		return new HistoryObject(this, ID_HISTORY_ERASE, _poPrev, _poNext);
	}
	
	/**
	 * Create new add history object.
	 * 
	 * @param _poPrev the previous paintObject
	 * @param _poNext the next paintObject
	 * @return the new created History Object.
	 */
	public final HistoryObject createSelectionDestroyItem(
			final SecureList<PaintObject> _poPrev,
			final SecureList<PaintObject> _poNext) {
		return new HistoryObject(
				this, ID_HISTORY_SELECTION_DESTORY, _poPrev, _poNext);
	}
	
	
	/*
	 * Methods for applying previous or next state:
	 */
	

	/**
	 * Apply the previous.
	 */
	public final void applyPrevious() {
		
		ls_history.previous(SecureList.ID_NO_PREDECESSOR, SecureList.ID_NO_PREDECESSOR);
		if (!ls_history.isEmpty()
				&& !ls_history.isInFrontOf()) {

			System.out.println(ls_history.getItem());
			ls_history.getItem().applyPrevious();
			ls_history.previous(SecureList.ID_NO_PREDECESSOR, 
					SecureList.ID_NO_PREDECESSOR);
		}
	}
	
	
	/**
	 * Apply the previous.
	 */
	public final void applyNext() {
		
		if (!ls_history.isBehind()) {

			ls_history.getItem().applyNext();
		}
	}



	/**
	 * @return the picture
	 */
	public Picture getPicture() {
		return picture;
	}



	/**
	 * @param picture the picture to set
	 */
	public void setPicture(Picture picture) {
		this.picture = picture;
	}
}

