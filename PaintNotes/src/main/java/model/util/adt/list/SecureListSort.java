package model.util.adt.list;

//import declarations
import model.settings.Status;
import model.util.DPoint;


/**
 * Sorted Secure list.
 * 
 * @author Julius Huelsmann
 * @version %I%, %U%
 * @param <SecureListType>
 */
public class SecureListSort<SecureListType> {

	
    /**
     * Whether to sort ascending or descending.
     */
    private boolean sortAsc = true;

	/**
	 * The id which is given to methods for transaction and closed actions if
	 * there is no predecessor transaction / closed action.
	 */
	public static final int ID_NO_PREDECESSOR = SecureList.ID_NO_PREDECESSOR;
    
    /**
     * The string which is added to the internal operations title for 
     * transaction or closed actions for knowing where the action has been 
     * started.
     */
    private final String internalAction = "Internal: ";


	/**
	 * The list out of which the sorted secure list consists.
	 */
	private SecureList <SecureListType> ls;
	

    /**
     * Constructor.
     */
    public SecureListSort() {
        
        //by default, the sorting is ascending.
        this.sortAsc = true;
        
        //initialize the list
        ls = new SecureList<SecureListType>();
    }
    

    /*
     * Functions returning the state of the list
     */

    /**
     * Returns weather is empty.
     *
     * @return weather list is empty.
     */
    public final boolean isEmpty() {
        return ls.isEmpty();
    }

    
    /**
     * Return weather it is in front of.
     *
     * @return weather list is in front of.
     */
    public final boolean isInFrontOf() {
        return ls.isInFrontOf();
    }

    
    /**
     * Return weather list is behind.
     *
     * @return weather list is behind.
     * @see List.isBehind();
     */
    public final boolean isBehind() {
        return ls.isBehind();
    }


    
    /*
     * Methods for navigating through the list
     */

    

    /*
     * Methods for navigating through the list
     */
    
    /**
     * Proceed one step in the list.
     * 
     * @param _transactionID 
     * 				the id of the transaction to which performs the
     * 				method call.
     * 
     * @param _closedActionID 
     * 				the id of the closed action to which performs the
     * 				method call.
     * 
     */
    public final void next(
    		final int _transactionID, final int _closedActionID) {
    	ls.next(_transactionID, _closedActionID);
    }

    
    /**
     * Step back in the list.
     * 
     * @param _transactionID 
     * 				the id of the transaction to which performs the
     * 				method call.
     * 
     * @param _closedActionID 
     * 				the id of the closed action to which performs the
     * 				method call.
     */
    public final void previous(
    		final int _transactionID, final int _closedActionID) {
    	ls.previous(_transactionID, _closedActionID);
    }

    
    /**
     * Go to the beginning of the list.
     * 
     * @param _transactionID 
     * 				the id of the transaction to which performs the
     * 				method call.
     * 
     * @param _closedActionID 
     * 				the id of the closed action to which performs the
     * 				method call.
     */
    public final void toFirst(
    		final int _transactionID, final int _closedActionID) {
    	ls.toFirst(_transactionID, _closedActionID);
    }

    
    /**
     * Go to the end of the list.
     * 
     * @param _transactionID 
     * 				the id of the transaction to which performs the
     * 				method call.
     * 
     * @param _closedActionID 
     * 				the id of the closed action to which performs the
     * 				method call.
     */
    public final void toLast(
    		final int _transactionID, final int _closedActionID) {
    	ls.toLast(_transactionID, _closedActionID);
    }
    

    /**
     * Go to a special element (has to be inside the list).
     * 
     * @param _elemCurrent the current element in the future.
     * 
     * @param _transactionID 
     * 				the id of the transaction to which performs the
     * 				method call.
     * 
     * @param _closedActionID 
     * 				the id of the closed action to which performs the
     * 				method call.
     */
    public final void goToElement(final Element<SecureListType> _elemCurrent,
    		final int _transactionID, final int _closedActionID) {
    	ls.goToElement(_elemCurrent, _transactionID, _closedActionID);
    }
	

	
	

    /*
     * Methods for getting content of the list's current element.
     */
    
    /**
     * Return current Element.
     *
     * @return current Element.
     */
    public final SecureListType getItem() {
    	return ls.getItem();
    }
    
    
    /**
     * Return current Element.
     *
     * @return current Element.
     */
    public final Element<SecureListType> getElement() {
    	return ls.getElement();
    }
    
    
    /*
     * Special Methods
     */

    /**
     * create subList.
     * @return list after current item.
     */
    public final List<SecureListType> subList() {
    	return ls.subList();
    }

    /**
     * Return sort index of the current Element.
     *
     * @return sorted index of current Element.
     */
    public final double getItemSortionIndex() {
    	return ls.getItemSortionIndex();
    }


    /**
     * Removes current element. Afterwards the current element points
     * to the predecessor of the removed item.
     * 
     * @param _transactionID 
     * 				the id of the transaction to which performs the
     * 				method call.
     */
    public final void remove(
    		final int _transactionID) {
    	ls.remove(_transactionID);
    }
    
    

    /**
     * Check whether item does already exist in list and if that is the case
     * point at it with elemCurrent.
     * 
     * @param _type which is checked
     * 
     * @param _transactionID 
     * 				the id of the transaction to which performs the
     * 				method call.
     * 
     * @return whether the element exists or not
     */
    public final boolean find(final SecureListType _type,
    		final int _transactionID) {
    	return ls.find(_type, _transactionID);
    }
    
    
    
    
    /**
     * print items with search index.
     */
    public final void printIndex() {
    	ls.printIndex();
    }

    
    /**
     * print items with search index.
     */
    public final void printAddcounter() {
    	ls.printAddcounter();
    }
    
    
    
    /**
     * List to array method.
     * @return the array from list.
     */
    public final synchronized DPoint[] toArray() {
    	return ls.toArray();
    }
    
    
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	

    /**
     * Start a transaction with specified operation name (for identifying the
     * not terminated transaction in case an error occurred).
     * 
     * 
     * If the current element of the list is to be maintained after a 
     * performed action which passes the list, this method is called 
     * before the action.
     * 
     * After the action has been done endTransaction has to be called.
     * Otherwise there will occur an error if a new Transaction is started
     * without terminating the old one.
     * 
     * @param _operationName the name of specified transaction
     * @param _oldOperationID the unique id of the old transaction
     * @return the unique id of the current transaction
     */
    public final int startClosedAction(final String _operationName,
    		final int _oldOperationID) {
    	return ls.startClosedAction(_operationName, _oldOperationID);
    }

    
    /**
     * Finish transaction; reset the state of the List and afterwards the
     * transaction values.
     * @param _oldOperationID the id of the transaction which is to be closed
     * @return the unique id of the current transaction
     */
    public final int finishClosedAction(final int _oldOperationID) {
    	return ls.finishClosedAction(_oldOperationID);
    }
    

    /**
     * Start a transaction with specified operation name (for identifying the
     * not terminated transaction in case an error occurred).
     * 
     * If no other operation except of children of the current transaction
     * shell be able to change the state of the list, this method is called
     * before the action.
     * 
     * After the action has been done endTransaction has to be called.
     * Otherwise there will occur an error if a new Transaction is started
     * without terminating the old one.
     * 
     * @param _transactionName
     * 					the name of specified transaction
     * 
     * @param _oldTransactionID 
     * 					the unique id of the old transaction
     * 
     * @return the unique id of the current transaction
     */
    public final int startTransaction(final String _transactionName,
    		final int _oldTransactionID) {
    	return ls.startTransaction(_transactionName, _oldTransactionID);
    }
    

    
    /**
     * Finish transaction; delete the current transaction.
     * 
     * @param _oldTransactionID the id of the transaction which is to be closed
     * 
     * @return the unique id of the current transaction
     */
    public final int finishTransaction(final int _oldTransactionID) {
    	return ls.finishTransaction(_oldTransactionID);
    }

    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    /**
     * insert sorted ASC.
     * 
     * @param _content 
     * 				the content to be inserted.
     * 
     * @param _searchCriteria 
     * 				the index of sorting.
     * 
     * @param _oldTransactionID
     * 				the id of the current operation for being able to start a 
     * 				new transaction.
     */
    public final synchronized void insertSorted(final SecureListType _content, 
            final double _searchCriteria, final int _oldTransactionID) {
    	
    	final int transactionID = ls.startTransaction(
    			internalAction + "insert Sorted", _oldTransactionID);
    	
    	//find the position of the first element which has got a greater index
    	//than the one which is inserted.
        findSorted(_searchCriteria, transactionID, ID_NO_PREDECESSOR);
        
        //insert the new item depending on sorting order
        if (sortAsc) {
            ls.insertInFrontOf(_content, transactionID);
        } else {
        	ls.insertBehind(_content, transactionID);
        }
        
        //apply the sorting index to the new item
        ls.getElement().setSortedIndex(_searchCriteria);
        
        ls.finishTransaction(transactionID);
    }

    
    /**
     * Set sort criteria to ascending.
     */
    public final synchronized void setSortASC() {
        if (ls.isEmpty()) {
            sortAsc = true;
        } else {
            Status.getLogger().warning("tried to change sorting order without"
                    + "success: The list is not empty and thus may have"
                    + "been sorted in a different order. " + sortAsc + "true");
        }
    }
    
    
    /**
     * Set sort criteria to descending.
     */
    public final synchronized void setSortDESC() {
        if (ls.isEmpty()) {
            sortAsc = false;
        } else {
            Status.getLogger().warning("tried to change sorting order without"
                    + "success: The list is not empty and thus may have"
                    + "been sorted in a different order. " + sortAsc + "false");
        }
    }
    
    
    /**
     * Change the sort index of the current element.
     * @param _sortedIndex the new sorted index of the current element.
     */
    public final void changeSortIndex(final int _sortedIndex) {
    	
    	//if the current element is not null change sort index.
    	if (getElement() != null) {
    		getElement().setSortedIndex(_sortedIndex);
    	} else {
    		Status.getLogger().warning("The current element is null. Thus it "
    				+ "is impossible to change the sort index.");
    	}
    }
    
    
    /**
     * goes behind the searched position.
     * 
     * 
     * @param _searchCriteria 
     * 				the index of sorting.
     * 
     * @param _transactionID
     * 				the id of the current operation for being able to start a 
     * 				new transaction.
     * 
     * @param _closedActionID 
     * 				the id of the closed action to which performs the
     * 				method call.
     */
    public final synchronized void findSorted(final double _searchCriteria,
    		final int _transactionID, final int _closedActionID) {
        
    	//if list is empty there is nothing to do. Thus only perform action 
    	//if list is not empty.
    	if (!ls.isEmpty()) {
    		
    		//if the list is neither behind nor in front of perform action
    		//otherwise go to the last respectively the first item.
	    	if (!ls.isBehind() && !ls.isInFrontOf()) {
	    		
	    		//if the current element has got an inferior index proceed
	    		//in list while the current item has got 
	    		if (ls.getElement() != null 
	    				&& ls.getElement().getSortedIndex() < _searchCriteria) {
		    		while (!ls.isBehind() && ls.getElement().getSortedIndex() 
		    				< _searchCriteria) {
		    			ls.next(_transactionID, _closedActionID);
		    		}
	    		} else if (ls.getElement() != null) {
		    		while (!ls.isInFrontOf()
		    				
		    				&& ls.getElement().getSortedIndex() 
		    				> _searchCriteria) {
		    			ls.previous(_transactionID, _closedActionID);
		    		}
		    		
		    		//if the current element is not in front of the list
		    		//perform one next operation for being behind the last item
		    		//that has got an inferior index. (and for maintaining
		    		//integrity of the result independent of the starting 
		    		//position in the list)
		    		if (!ls.isInFrontOf()) {
		    			ls.next(_transactionID, _closedActionID);	
		    		}
	    		} else {
	    			ls.toFirst(_transactionID, _closedActionID);
	    			
	    			while (ls.getItem() == null) {
	    				ls.next(_transactionID, _closedActionID);
	    			}
	    			if (!ls.isBehind()) {

		    			findSorted(_searchCriteria, _transactionID, 
		    					_closedActionID);
	    			}
	    		}
	    	} else if (ls.isBehind()) {
	    		ls.toLast(_transactionID, _closedActionID);
	            findSorted(_searchCriteria, _transactionID, _closedActionID);
	    	} else {
	    		ls.toFirst(_transactionID, _closedActionID);
	            findSorted(_searchCriteria, _transactionID, _closedActionID);
	    	}
    	}
    }
}
