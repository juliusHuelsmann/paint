package model.util.adt.list;

import org.omg.CORBA._IDLTypeStub;

import model.settings.Status;
import model.util.DPoint;
import model.util.adt.stack.Stack;
import model.util.adt.stack.StackElement;


/**
 * SecureList is an indirect extension of the list class.
 * 
 * There are two special new features:
 * 	1) Closed Actions
 * 	2) Transactions
 * 
 * 1) Closed Actions:
 * If the current element of the list is to be maintained after a performed 
 * action which passes the list start-closed-action before the action and 
 * call end-closed-action afterwards.
 * 
 * During the closed action it is impossible to remove items out of the list
 * and to add items to list but it is only possible to pass the list
 * by using next and previous methods and to check the current element.
 * 
 * 2) Transactions:
 * -
 * 
 * @author Julius Huelsmann
 * @version %I%, %U%
 * @param <SecureListType>
 */
public class SecureList<SecureListType> {

	
	/**
	 * The list which is the base of the SecureList and which methods
	 * are used.
	 */
	private List <SecureListType> ls;
	

    
    /*
     * Variables for transactions
     */
    
    /**
     * Stack which contains elements used for transactions. 
     * 
     * If the current element of the list is to be maintained after a performed 
     * action which passes the list start-closed-action before the action and 
     * call end-closed-action afterwards.
     * 
     * During the closed action it is impossible to remove items out of the list
     * and to add items to list but it is only possible to pass the list
     * by using next and previous methods and to check the current element.
     */
    private  Stack<ClosedAction<SecureListType> > stck_closedAction;
    
    
	
	/**
	 * Constructor of SecureList initializes the list.
	 */
	public SecureList() {
		ls = new List<SecureListType>();
		stck_closedAction = new Stack<ClosedAction<SecureListType> >();
		
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
    public final void goToElement(final Element<SecureListType> _elemCurrent) {
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
     * Replaces current element with newContent.
     *
     * @param _newContent contains the content which is to be inserted.
     */
    public final void replace(final SecureListType _newContent) {

    	ls.replace(_newContent);
    }

    /**
     * Insert element after current position.
     *
     * @param _newContent contains the content which is to be inserted.
     */
    public final void insertBehind(final SecureListType _newContent) {

    	ls.insertBehind(_newContent);
    }

    /**
     * Insert element in front of current position.
     *
     * @param _newContent contains the content which is to be inserted.
     */
    public final void insertInFrontOf(final SecureListType _newContent) {

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
    public final void insertAfterHead(final SecureListType _newContent) {

    	ls.insertAfterHead(_newContent);
    }

    /**
     * Inserts thing at the end of the list.
     *
     * @param _newContent contains the content which is to be inserted.
     */
    public final void insertAtTheEnd(final SecureListType _newContent) {

    	ls.insertAtTheEnd(_newContent);
    }

    
    
    /**
     * Check whether item does already exist in list and if that is the case
     * point at it with elemCurrent.
     * 
     * @param _type which is checked
     * @return whether the element exists or not
     */
    public final boolean find(final SecureListType _type) {
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
     * @param _oldOperationID the unique id of the old transaction
     * @return the unique id of the current transaction
     */
    public final int startClosedAction(final String _operationName,
    		final int _oldOperationID) {
    	
    	
    	//threshold: the stack must not be null. catch this error.
    	if (stck_closedAction == null) {
    		Status.getLogger().severe("The stack is null! That should not "
    				+ "happen!");
    		return -1;
    	}
    	
    	//check whether it is valid to start a new closed aciton
    	if (!stck_closedAction.isEmpty()) {

        	//the old closedAciton
        	ClosedAction<SecureListType> ca_old = 
        			((ClosedAction<SecureListType>)  
        			stck_closedAction.getElem_last().getContent());
        	
        	
        	//if the given id does not equal the one of the last transaction
        	//and the current element is null and the stack is not empty
        	if (ca_old != null 
        			&& !stck_closedAction.isEmpty()
        			&& ca_old.getId_secureList() != _oldOperationID) {
        		
        		Status.getLogger().severe("Transaction " + ca_old.getName() 
        				+ " not terminated! and new transaction");
        		return -1;
        	} 
    	}
    	

    	//create new closed action and insert it into the stack
    	ClosedAction<SecureListType> ca_new 
    	= new ClosedAction<SecureListType>(
    			_operationName, ls.getElement());
    	stck_closedAction.insert(ca_new);
    	return ca_new.getId_secureList();
    }

    
    /**
     * Finish transaction; reset the state of the List and afterwards the
     * transaction values.
     * @param _oldOperationID the id of the transaction which is to be closed
     * @return the unique id of the current transaction
     */
    public final int finishClosedAction(final int _oldOperationID) {

    	//threshold: the stack must not be null. catch this error.
    	if (stck_closedAction == null) {
    		Status.getLogger().severe("The stack is null! That should not "
    				+ "happen!");
    		return -1;
    	}
    	
    	

    	//the old closedAciton
    	ClosedAction<SecureListType> ca_old = 
    			((ClosedAction<SecureListType>)  
    			stck_closedAction.getElem_last().getContent());
    	
    	
    	//if the given id does not equal the one of the last transaction
    	if (ca_old != null 
    			&& ca_old.getId_secureList() != _oldOperationID) {
    		
    		Status.getLogger().severe("Wrong closed action to be terminated."
    				+ " Current one: " 
    				+ ca_old.getName()
    				+ " Old id: " + _oldOperationID);
    		return ca_old.getId_secureList();
    	} else {

    		//apply state before secure action.
        	ls.goToElement(ca_old.getElem_secure());
    		stck_closedAction.remove();

    		if (!stck_closedAction.isEmpty()) {

        		//return the new current id.
            	ClosedAction<SecureListType> ca_current = 
            			((ClosedAction<SecureListType>)  
            			stck_closedAction.getElem_last().getContent());
        		return ca_current.getId_secureList();
    		} else {
    			return -1;
    		}
    	}
    }
}
