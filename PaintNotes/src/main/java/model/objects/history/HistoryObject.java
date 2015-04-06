package model.objects.history;

//import declarations
import java.io.Serializable;
import model.objects.painting.po.PaintObject;
import model.settings.Status;
import model.util.adt.list.SecureList;
import model.util.adt.list.SecureListSort;



/**
 * HistoryObject.
 * @author Julius Huelsmann
 *
 */
public class HistoryObject implements Serializable {

	
	/**
     * Default serial version UID for being able to identify the list's 
     * version if saved to the disk and check whether it is possible to 
     * load it or whether important features have been added so that the
     * saved file is out-dated.
	 */
	private static final long serialVersionUID = 1L;


	/**
	 * The PaintObject.
	 */
	private Object o_statePrevious, o_stateNext;
	
	
	/**
	 * The identifier of the action that is performed in applyNext.
	 * the
	 */
	private final int id_next, id_previous;
	
	private HistorySession session;
	
	
	
	/**
	 * Constructor.
	 * @param _currentID the current ID
	 * @param _o_previous the previous object
	 * @param _o_next the next object
	 */
	public HistoryObject(
			
			final HistorySession _session,
			final int _currentID, final Object _o_previous,
			final Object _o_next) {
		
		this.session = _session;
	
		//save next id
		this.id_next = _currentID;
		
		//save objects
		this.o_stateNext = _o_next;
		this.o_statePrevious = _o_previous;
		
		//compute previous id
		switch (id_next) {
		case HistorySession.ID_HISTORY_PO_ADD:
			id_previous = HistorySession.ID_HISTORY_PO_REMOVE;
			break;
		case HistorySession.ID_HISTORY_PO_MOVE:
			id_previous = HistorySession.ID_HISTORY_PO_MOVE;
			break;
		case HistorySession.ID_HISTORY_PO_REMOVE:
			id_previous = HistorySession.ID_HISTORY_PO_ADD;
			break;
		case HistorySession.ID_HISTORY_PO_RESIZE:
			id_previous = HistorySession.ID_HISTORY_PO_RESIZE;
			break;
		case HistorySession.ID_HISTORY_POW_CHANGE_CLR:
			id_previous = HistorySession.ID_HISTORY_POW_CHANGE_CLR;
			break;
		case HistorySession.ID_HISTORY_POW_CHANGE_PEN:
			id_previous = HistorySession.ID_HISTORY_POW_CHANGE_PEN;
			break;
		case HistorySession.ID_HISTORY_SELECTION_DESTORY:
			id_previous = HistorySession.ID_HISTORY_SELECTION_DESTORY;
			break;
		case HistorySession.ID_HISTORY_ERASE:
			id_previous = HistorySession.ID_HISTORY_ERASE;
			break;
		case HistorySession.ID_HISTORY_EXP_ALPHA:
			id_previous = HistorySession.ID_HISTORY_EXP_ALPHA;
			break;
		case HistorySession.ID_HISTORY_EXP_BG:
			id_previous = HistorySession.ID_HISTORY_EXP_BG;
			break;
		case HistorySession.ID_HISTORY_EXP_FORMAT:
			id_previous = HistorySession.ID_HISTORY_EXP_FORMAT;
			break;
		case HistorySession.ID_HISTORY_EXP_MARGE_LEFT:
			id_previous = HistorySession.ID_HISTORY_EXP_MARGE_LEFT;
			break;
		case HistorySession.ID_HISTORY_EXP_MARGE_RIGHT:
			id_previous = HistorySession.ID_HISTORY_EXP_MARGE_RIGHT;
			break;
		case HistorySession.ID_HISTORY_EXP_MARGE_TOP:
			id_previous = HistorySession.ID_HISTORY_EXP_MARGE_TOP;
			break;
		case HistorySession.ID_HISTORY_EXP_MARGE_BOTTOM:
			id_previous = HistorySession.ID_HISTORY_EXP_MARGE_BOTTOM;
			break;
		default:
			id_previous = -1;
			Status.getLogger().severe("illegal history id. Do not know which"
					+ " action to perform.");
			break;
			
		}
	}


	/**
	 * Apply the action that the HistoryObject contains.
	 */
	public final void applyNext() {
		apply(id_next, o_stateNext);
	}	
	
	
	/**
	 * Apply the state before the HistoryObject took place.
	 */
	public final void applyPrevious() {
		apply(id_previous, o_statePrevious);
		
	}
	
	
	/**
	 * Apply state with special identifier and object.
	 * @param _id the identifier (next or previous)
	 * @param _o the object (o_next or o_previous)
	 */
	public final void apply(final int _id, final Object _o) {
		
		Object otherObj = null;
		if (_o.equals(o_stateNext)) {
			otherObj = o_statePrevious;
		} else if (_o.equals(o_statePrevious)) {
			otherObj = o_stateNext;
		} else {
			Status.getLogger().severe("wrong object passed to method.");
		}
		
		
		switch (_id) {
		case HistorySession.ID_HISTORY_PO_ADD:
			applyAdd(_o);
			break;
		case HistorySession.ID_HISTORY_PO_MOVE:
			applyMove(_o, otherObj);
			break;
		case HistorySession.ID_HISTORY_PO_REMOVE:
			applyRemove(_o);
			break;
		case HistorySession.ID_HISTORY_PO_RESIZE:
			applyResize(_o, otherObj);
			break;
		case HistorySession.ID_HISTORY_POW_CHANGE_CLR:
			applyChangeColor(_o, otherObj);
			break;
		case HistorySession.ID_HISTORY_POW_CHANGE_PEN:
			applyChangePen(_o, otherObj);
			break;
		case HistorySession.ID_HISTORY_SELECTION_DESTORY:
			applySelectionDestroy(_o, otherObj);
			break;
		case HistorySession.ID_HISTORY_ERASE:
			applyErase(_o, otherObj);
			break;
		case HistorySession.ID_HISTORY_EXP_ALPHA:
			applyExpAlpha(_o);
			break;
		case HistorySession.ID_HISTORY_EXP_BG:
			applyExpBg(_o);
			break;
		case HistorySession.ID_HISTORY_EXP_FORMAT:
			applyExpFormat(_o);
			break;
		case HistorySession.ID_HISTORY_EXP_MARGE_LEFT:
			applyExpMargeLeft(_o);
			break;
		case HistorySession.ID_HISTORY_EXP_MARGE_RIGHT:
			applyExpMargeRight(_o);
			break;
		case HistorySession.ID_HISTORY_EXP_MARGE_TOP:
			applyExpMargeTop(_o);
			break;
		case HistorySession.ID_HISTORY_EXP_MARGE_BOTTOM:
			applyExpMargeBottom(_o);
			break;
		default:

			Status.getLogger().severe("illegal history id. Do not know which"
					+ " action to perform.");
			break;
		}
	}
	
	
	


	/**
	 * 
	 * @param _o
	 */
	private void applyExpAlpha(final Object _o) {
		// TODO Auto-generated method stub
		new Exception("Not implemented yet.").printStackTrace();
	}


	private void applyExpBg(final Object _o) {
		// TODO Auto-generated method stub
		new Exception("Not implemented yet.").printStackTrace();
		
	}


	private void applyExpFormat(final Object _o) {
		// TODO Auto-generated method stub
		new Exception("Not implemented yet.").printStackTrace();
		
	}


	private void applyExpMargeLeft(final Object _o) {
		// TODO Auto-generated method stub
		new Exception("Not implemented yet.").printStackTrace();
		
	}


	private void applyExpMargeRight(final Object _o) {
		// TODO Auto-generated method stub
		new Exception("Not implemented yet.").printStackTrace();
		
	}


	private void applyExpMargeTop(final Object _o) {
		// TODO Auto-generated method stub
		new Exception("Not implemented yet.").printStackTrace();
		
	}


	private void applyExpMargeBottom(final Object _o) {
		// TODO Auto-generated method stub
		new Exception("Not implemented yet.").printStackTrace();
		
	}

	
	
	
	
	
	
	
	
	

	
	
	/**
	 * Apply add action.
	 * @param _object the object to be removed out of list of PaintObjects.
	 */
	private void applyAdd(final Object _object) {

		if (_object instanceof PaintObject) {
			
			//start a new transaction
			final int transactionID = session.getPicture()
					.getLs_po_sortedByX().startTransaction(
							"history apply add", 
							SecureList.ID_NO_PREDECESSOR);

			//add item.
			session.getPicture().getLs_po_sortedByX().insertSorted(
					((PaintObject) _object).clone(), 
					((PaintObject) _object).getSnapshotBounds().x, 
					transactionID);

			//finish current transaction.
			session.getPicture().getLs_po_sortedByX().finishTransaction(
					transactionID);
				
		} else {
			
			//print error because the object does not match 
			Status.getLogger().severe("Fatal error. Remove. "
					+ "Objects does not match identifier.");
		}
	}

	
	
	public boolean findInList(final int _transactionID, final int _closedActionID, final int _elementid) {

		return findInList(session.getPicture().getLs_po_sortedByX(),
				_transactionID, _closedActionID, _elementid);
	}
	
	
	public boolean findInList(
			
			final SecureListSort<PaintObject> _sl, 
			final int _transactionID, final int _closedActionID, 
			final int _elementid) {
		_sl.toFirst(_transactionID, _closedActionID);
		while (
				!_sl.isEmpty()
				&& !_sl.isBehind()) {
			if (_sl.getItem().getElementId() == _elementid) {
				return true;
			}
			_sl.next(_transactionID, _closedActionID);
		}
		return false;
		
	}
	
	
	/**
	 * Apply remove action.
	 * @param _object the object to be removed out of list of PaintObjects.
	 */ 
	private void applyRemove(final Object _object) {

		if (_object instanceof PaintObject) {
			
			//start a new transaction
			final int transactionID = session.getPicture()
					.getLs_po_sortedByX().startTransaction(
							"history apply remove", 
							SecureList.ID_NO_PREDECESSOR);
			
			//search the element which is to be removed
			boolean found = findInList(
					transactionID, SecureList.ID_NO_PREDECESSOR, 
					((PaintObject) _object).getElementId());
			

			//if the element was found, remove it.
			if (found) {
				
				//remove item.
				session.getPicture().getLs_po_sortedByX().remove(
						transactionID);
				
			} else {
				
				//otherwise print error.
				Status.getLogger().severe("Fatal error. Remove. "
						+ "Object not found.");
			}
			
			//finish current transaction.
			session.getPicture().getLs_po_sortedByX().finishTransaction(
					transactionID);
		} else {
			
			//print error because the object does not match 
			Status.getLogger().severe("Fatal error. Remove. "
					+ "Objects does not match identifier.");
		}
	}
	


	/**
	 * Apply move action.
	 * @param _objectMoved the moved object which is added
	 * @param _objectOrig the original (not moved) object which is deleted.
	 */
	private void applyMove(final Object _objectMoved, 
			final Object _objectOrig) {

		if (_objectOrig instanceof SecureList<?>
				&& _objectMoved instanceof SecureList<?>) {
			
			//start a new transaction
			final int transactionID = session.getPicture()
					.getLs_po_sortedByX().startTransaction(
							"history apply move", 
							SecureList.ID_NO_PREDECESSOR);

			final SecureList<?> ls_orig =  (SecureList<?>) _objectOrig;
			final SecureList<?> ls_moved = (SecureList<?>) _objectMoved;
			
			ls_orig.toFirst(
					SecureList.ID_NO_PREDECESSOR, SecureList.ID_NO_PREDECESSOR);
			
			while (!ls_orig.isBehind() && !ls_orig.isEmpty()) {
				

				PaintObject po_orig = (PaintObject) ls_orig.getItem();
				PaintObject po_moved = (PaintObject) ls_moved.getItem();
				
				if (po_orig == null || po_moved == null) {


					//otherwise print error.
					Status.getLogger().severe("Fatal error. apply move. "
							+ "Paint Object null. "
							+ "Maybe Length of lists does not match. "
							+ "Maybe forgot to copy items.");
				
				} else {

					//search the element which is to be removed
					boolean found = findInList(transactionID, SecureList.ID_NO_PREDECESSOR, po_orig.getElementId());

					//if the element was found, remove it.
					if (found) {
						
						//remove item.
						session.getPicture().getLs_po_sortedByX().remove(
								transactionID);

						//insert moved item.
						session.getPicture().getLs_po_sortedByX().insertSorted(
								(PaintObject) po_moved.clone(), 
								((PaintObject) po_moved).getSnapshotBounds().x, 
								transactionID);
					
					} else {
					
						//otherwise print error.
						Status.getLogger().severe("Fatal error. apply move. "
								+ "Object not found.");
					}
				}
				
				//proceed
				ls_orig.next(SecureList.ID_NO_PREDECESSOR, SecureList.ID_NO_PREDECESSOR);
				ls_moved.next(SecureList.ID_NO_PREDECESSOR, SecureList.ID_NO_PREDECESSOR);
			}
			
			if (ls_moved.isBehind() != ls_orig.isBehind()) {

				//otherwise print error.
				Status.getLogger().severe("Fatal error. apply move. "
						+ "Length of lists does not match.");
			}

			//finish current transaction.
			session.getPicture().getLs_po_sortedByX().finishTransaction(
					transactionID);
		
			
		
		} else {
			
			//print error because the object does not match 
			Status.getLogger().severe("Fatal error. Remove. "
					+ "Objects does not match identifier.");
		}
	}
	
	



	/**
	 * Apply resize action.
	 * @param _objectResized the resized object which is added
	 * @param _objectOrig the original (not resized) object which is deleted.
	 */
	private void applyResize(final Object _objectResized, 
			final Object _objectOrig) {

		applyMove(_objectResized, _objectOrig);
	}
	
	



	/**
	 * Apply applyChangeColor action.
	 * @param _objectResized the new object which is added
	 * @param _objectOrig the original object which is deleted.
	 */
	private void applyChangeColor(final Object _objectResized, 
			final Object _objectOrig) {

		applyMove(_objectResized, _objectOrig);		
	}

	/**
	 * Apply applyChangeColor action.
	 * @param _objectResized the new object which is added
	 * @param _objectOrig the original object which is deleted.
	 */
	private void applyChangePen(final Object _objectResized, 
			final Object _objectOrig) {

		applyMove(_objectResized, _objectOrig);		
	}
	
	

	
	/*
	 * Methods that deal with lists.
	 */


	/**
	 * Apply selection destroy option.
	 * @param _objectNew list of new PaintObjects (to be inserted) 
	 * @param _objectOld list of old PaintObjects (to be removed )
	 */
	private void applySelectionDestroy(final Object _objectNew, 
			final Object _objectOld) {

		//if the objects contain SecureLists
		if ((_objectOld instanceof SecureList<?>)
				&& _objectOld instanceof SecureList<?>) {
			
			//start a new transaction
			final int transactionID = session.getPicture()
					.getLs_po_sortedByX().startTransaction(
							"history apply move", 
							SecureList.ID_NO_PREDECESSOR);
			

			//fetch lists
			SecureList<?> sl_toAdd = (SecureList<?>) _objectNew;
			SecureList<?> sl_toRemove = (SecureList<?>) _objectOld;


			
			
			//remove those PaintObjects out of sorted list of PaintObjects
			//contained in Picture, that are members of the list sl_toRemove.
			//Therefore parse the list
			sl_toRemove.toFirst(SecureList.ID_NO_PREDECESSOR,
					SecureList.ID_NO_PREDECESSOR);
			while (!sl_toRemove.isBehind()) {

				
				if (sl_toRemove.getItem() instanceof PaintObject) {

					//search the element which is to be removed
					boolean found =
							session.getPicture().getLs_po_sortedByX().find(
							(PaintObject) sl_toRemove.getItem(), 
							SecureList.ID_NO_PREDECESSOR);
					

					//if the element was found, remove it.
					if (found) {
						
						//remove item.
						session.getPicture().getLs_po_sortedByX().remove(
								transactionID);

					} else {
						
						//otherwise print error.
						Status.getLogger().severe("Fatal error. apply selectin"
								+ " destroy. "
								+ "Object to be removed not found.");
					}
				} else {

					//otherwise print error.
					Status.getLogger().severe("Fatal error. apply selectin"
							+ " destroy. "
							+ "Object not instance of PaintObject.");
				}
				sl_toRemove.next(SecureList.ID_NO_PREDECESSOR,
					SecureList.ID_NO_PREDECESSOR);
			}
			
			
			
			
			

			//Add those PaintObjects into the sorted list of PaintObjects
			//contained in Picture, that are members of the list sl_toAdd.
			//Therefore parse the list
			sl_toAdd.toFirst(SecureList.ID_NO_PREDECESSOR,
					SecureList.ID_NO_PREDECESSOR);
			while (!sl_toAdd.isBehind()) {

				
				if (sl_toAdd.getItem() instanceof PaintObject) {

					//search the element which is to be removed
					boolean found =
							session.getPicture().getLs_po_sortedByX().find(
							(PaintObject) sl_toAdd.getItem(), 
							SecureList.ID_NO_PREDECESSOR);
					

					//if the element was found, remove it.
					if (found) {
						

						//insert moved item.
						session.getPicture().getLs_po_sortedByX().insertSorted(
								((PaintObject) sl_toAdd.getItem()).clone() , 
								((PaintObject) sl_toAdd.getItem())
								.getSnapshotBounds().x, 
								transactionID);
						

					} else {
						
						//otherwise print error.
						Status.getLogger().severe("Fatal error. apply selectin"
								+ " destroy. "
								+ "Object to be removed not found.");
					}
				} else {

					//otherwise print error.
					Status.getLogger().severe("Fatal error. apply selectin"
							+ " destroy. "
							+ "Object not instance of PaintObject.");
				}
				sl_toAdd.next(SecureList.ID_NO_PREDECESSOR,
					SecureList.ID_NO_PREDECESSOR);
			}
			

			
			//finish current transaction.
			session.getPicture().getLs_po_sortedByX().finishTransaction(
					transactionID);
		} else {
			
			//print error because the object does not match 
			Status.getLogger().severe("Fatal error. Remove. "
					+ "Objects does not match identifier.");
		}
	}

	/**
	 * Apply selection destroy option.
	 * @param _objectNew list of new PaintObjects (to be inserted) 
	 * @param _objectOld list of old PaintObjects (to be removed )
	 */
	private void applyErase(final Object _objectNew, 
			final Object _objectOld) {
		applySelectionDestroy(_objectNew, _objectOld);
	}

}
