package model.util.list;

//import declarations
import model.settings.Status;
import model.util.DPoint;


/**
 * Sorted Secure list.
 * 
 * @author Julius Huelsmann
 * @version %I%, %U%
 * @param <Type>
 */
public class SecureListSort<Type> {

	
    /**
     * Whether to sort ascending or descending.
     */
    private boolean sortAsc = true;


	/**
	 * The list out of which the sorted secure list consists.
	 */
	private SecureList <Type> ls;
	

    /**
     * Constructor.
     */
    public SecureListSort() {
        
        //by default, the sorting is ascending.
        this.sortAsc = true;
        
        //initialize the list
        ls = new SecureList<Type>();
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
    
    /**
     * Proceed one step in the list.
     */
    public final void next() {
    	ls.next();
    }

    
    /**
     * Step back in the list.
     */
    public final void previous() {
    	ls.previous();
    }

    
    /**
     * Go to the beginning of the list.
     */
    public final void toFirst() {
    	ls.toFirst();
    }

    
    /**
     * Go to the end of the list.
     */
    public final void toLast() {
    	ls.toLast();
    }
    

    /**
     * Go to a special element (has to be inside the list).
     * 
     * @param _elemCurrent the current element in the future.
     */
    public final void goToElement(final Element<Type> _elemCurrent) {
    	ls.goToElement(_elemCurrent);
    }
	
	

    /*
     * Methods for getting content of the list's current element.
     */
    
    /**
     * Return current Element.
     *
     * @return current Element.
     */
    public final Type getItem() {
    	return ls.getItem();
    }
    
    
    /**
     * Return current Element.
     *
     * @return current Element.
     */
    public final Element<Type> getElement() {
    	return ls.getElement();
    }
    
    
    /*
     * Special Methods
     */

    /**
     * create subList.
     * @return list after current item.
     */
    public final List<Type> subList() {
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
     */
    public final void remove() {
    	ls.remove();
    }


    
    
    /**
     * Check whether item does already exist in list and if that is the case
     * point at it with elemCurrent.
     * 
     * @param _type which is checked
     * @return whether the element exists or not
     */
    public final boolean find(final Type _type) {
    	return ls.find(_type);
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
     */
    public final void startTransaction(final String _operationName) {
    	ls.startTransaction(_operationName);
    }

    
    /**
     * Finish transaction; reset the state of the List and afterwards the
     * transaction values.
     */
    public final void finishTransaction() {
    	ls.finishTransaction();
    }
    
    
    /**
     * insert sorted ASC.
     * @param _content the content to be inserted.
     * @param _searchCriteria the index of sorting.
     */
    public final synchronized void insertSorted(final Type _content, 
            final double _searchCriteria) {
    	
    	
    	//find the position of the first element which has got a greater index
    	//than the one which is inserted.
        findSorted(_searchCriteria);
        
        //insert the new item depending on sorting order
        if (sortAsc) {
            ls.insertInFrontOf(_content);
        } else {
        	ls.insertBehind(_content);
        }
        
        //apply the sorting index to the new item
        ls.getElement().setSortedIndex(_searchCriteria);
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
     * @param _searchCriteria the search index.
     */
    public final synchronized void findSorted(final double _searchCriteria) {
        
    	//if list is empty there is nothing to do. Thus only perform action 
    	//if list is not empty.
    	if (!ls.isEmpty()) {
    		
    		//if the list is neither behind nor in front of perform action
    		//otherwise go to the last respectively the first item.
	    	if (!ls.isBehind() && !ls.isInFrontOf()) {
	    		
	    		//if the current element has got an inferior index proceed
	    		//in list while the current item has got 
	    		if (ls.getElement().getSortedIndex() < _searchCriteria) {
		    		while (!ls.isBehind() && ls.getElement().getSortedIndex() 
		    				< _searchCriteria) {
		    			ls.next();
		    		}
	    		} else {
		    		while (!ls.isInFrontOf()
		    				
		    				&& ls.getElement().getSortedIndex() 
		    				> _searchCriteria) {
		    			ls.previous();
		    		}
		    		
		    		//if the current element is not in front of the list
		    		//perform one next operation for being behind the last item
		    		//that has got an inferior index. (and for maintaining
		    		//integrity of the result independent of the starting 
		    		//position in the list)
		    		if (!ls.isInFrontOf()) {
		    			ls.next();	
		    		}
	    		}
	    	} else if (ls.isBehind()) {
	    		ls.toLast();
	            findSorted(_searchCriteria);
	    	} else {
	    		ls.toFirst();
	            findSorted(_searchCriteria);
	    	}
    	}
    }
}
