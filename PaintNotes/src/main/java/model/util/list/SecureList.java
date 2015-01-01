package model.util.list;

import model.settings.Status;
import model.util.DPoint;


/**
 * 
 * 
 * @author Julius Huelsmann
 * @version %I%, %U%
 * @param <Type>
 */
public class SecureList<Type> {

	
	/**
	 * 
	 */
	private List <Type> ls;
	

    
    /*
     * Variables for transactions
     */
    
    /**
     * Element used for transactions. 
     * 
     * If the current element of the list
     * is to be maintained after a performed action which passes the list 
     * start transaction before the action and call endTransaction afterwards.
     */
    private  Element<Type> elem_closedAction;
    
    
    /**
     * String containing the title of current transaction.
     * @see elem_transaction
     */
    private String strg_closedActionTitle;
    
	
	/**
	 * Constructor of SecureList initializes the list.
	 */
	public SecureList() {
		ls = new List<Type>();
        //set title of transaction
        this.strg_closedActionTitle = "";
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
     * Replaces current element with newContent.
     *
     * @param _newContent contains the content which is to be inserted.
     */
    public final void replace(final Type _newContent) {

    	ls.replace(_newContent);
    }

    /**
     * Insert element after current position.
     *
     * @param _newContent contains the content which is to be inserted.
     */
    public final void insertBehind(final Type _newContent) {

    	ls.insertBehind(_newContent);
    }

    /**
     * Insert element in front of current position.
     *
     * @param _newContent contains the content which is to be inserted.
     */
    public final void insertInFrontOf(final Type _newContent) {

    	ls.insertInFrontOf(_newContent);
    }

    /**
     * Removes current element. Afterwards the current element points
     * to the predecessor of the removed item.
     */
    public final void remove() {
    	ls.remove();
    }

    /**
     * Inserts s.th. at the beginning of the list.
     *
     * @param _newContent contains the content which is to be inserted.
     */
    public final void insertAfterHead(final Type _newContent) {

    	ls.insertAfterHead(_newContent);
    }

    /**
     * Inserts thing at the end of the list.
     *
     * @param _newContent contains the content which is to be inserted.
     */
    public final void insertAtTheEnd(final Type _newContent) {

    	ls.insertAtTheEnd(_newContent);
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
    	
    	if (elem_closedAction != null) {
    		Status.getLogger().severe("Transaction " + strg_closedActionTitle 
    				+ " not terminated!");
    	} 

    	elem_closedAction = ls.getElement();
    	strg_closedActionTitle = _operationName + System.currentTimeMillis();
    }

    
    /**
     * Finish transaction; reset the state of the List and afterwards the
     * transaction values.
     */
    public final void finishTransaction() {
    	if (elem_closedAction != null || strg_closedActionTitle != "") {

        	ls.goToElement(elem_closedAction);
        	elem_closedAction = null;
        	strg_closedActionTitle = "";
    	} else {
    		Status.getLogger().severe("transaction error.");
    	}
    }
}
