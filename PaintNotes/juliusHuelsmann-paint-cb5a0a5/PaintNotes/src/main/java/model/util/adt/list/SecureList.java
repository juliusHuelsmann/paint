package model.util.adt.list;

import java.io.Serializable;

import model.settings.Status;
import model.util.DPoint;
import model.util.adt.stack.Stack;


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
 * If no other operation except of children of the current transaction
 * shell be able to change the state of the list, the method start-transaction
 * is called before the action and end-transaction afterwards.
 * 
 * @author Julius Huelsmann
 * @version %I%, %U%
 * @param <SecureListType>
 */
public class SecureList<SecureListType> implements Serializable {

    /**
     * Default serial version UID for being able to identify the list's 
     * version if saved to the disk and check whether it is possible to 
     * load it or whether important features have been added so that the
     * saved file is out-dated.
     */
    private static final long serialVersionUID = 1L;

	
	/**
	 * The list which is the base of the SecureList and which methods
	 * are used.
	 */
	private List <SecureListType> ls;
	
	
	/**
	 * The id which is given to methods for transaction and closed actions if
	 * there is no predecessor transaction / closed action.
	 */
	public static final int ID_NO_PREDECESSOR = -1;

	
	/**
	 * This boolean indicates, whether even though an error occurred that
	 * destroyed the order of transactions / closed actions the action is 
	 * completed for not throwing an exception and for keeping the program
	 * running.
	 */
	private final boolean debug_stay_running = true;
    
    /*
     * Variables for transactions
     */
    
    /**
     * Stack which contains elements used for closed action. 
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
     * Stack which contains elements used for transactions. 
     * 
	 * If an action is not to be interrupted by other actions, this gives the
	 * possibility to start a transaction (which do also allow nested scopes).
	 * Thus it is impossible to perform an action outside the current 
	 * transaction.
     */
    private  Stack<Transaction<SecureListType> > stck_transaction;
    
    /*
     * Constructor
     */
	
	/**
	 * Constructor of SecureList initializes the list.
	 */
	public SecureList() {
		
		//initialize the list of which the SecureList consists
		ls = new List<SecureListType>();
		
		//initialize lists for closed actions and transactions
		stck_closedAction = new Stack<ClosedAction<SecureListType>>();
		stck_transaction = new Stack<Transaction<SecureListType>>();
	}
	
	

    /*
     * Functions returning the state of the list. They do not change the list;
     * thus they are not interesting for the closed action and transaction
     * extensions.
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
     * Methods for navigating through the list. These Methods change the state
     * of the current-element-pointer. Thus only those closed-actions and
     * transactions are allowed to perform them that are current.
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
    public final synchronized void next(
    		final int _transactionID, final int _closedActionID) {
    	
    	//the method name for being able to print additional information in 
    	//check methods
    	final String methodName = "next";
    	
    	//check whether the current transaction and the current closed action
    	//are okay
    	if (checkTransaction(_transactionID, methodName) 
    			&& checkClosedAction(_closedActionID, methodName)) {
    		
    		//perform method call.
    		ls.next();
    	} else if (debug_stay_running) {
    		ls.next();
    	}
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

    	//the method name for being able to print additional information in 
    	//check methods
    	final String methodName = "previous";
    	
    	//check whether the current transaction and the current closed action
    	//are okay
    	if (checkTransaction(_transactionID, methodName) 
    			&& checkClosedAction(_closedActionID, methodName)) {
    		
    		//perform method call.
    		ls.previous();
    	} else if (debug_stay_running) {
    		ls.previous();
    	}
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

    	//the method name for being able to print additional information in 
    	//check methods
    	final String methodName = "toFirst";
    	
    	//check whether the current transaction and the current closed action
    	//are okay
    	if (checkTransaction(_transactionID, methodName) 
    			&& checkClosedAction(_closedActionID, methodName)) {
    		
    		//perform method call.
    		ls.toFirst();
    	} else if (debug_stay_running) {
    		ls.toFirst();
    	}
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

    	//the method name for being able to print additional information in 
    	//check methods
    	final String methodName = "toLast";
    	
    	//check whether the current transaction and the current closed action
    	//are okay
    	if (checkTransaction(_transactionID, methodName) 
    			&& checkClosedAction(_closedActionID, methodName)) {
    		
    		//perform method call.
    		ls.toLast();
    	} else if (debug_stay_running) {
    		ls.toLast();
    	}
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

    	//the method name for being able to print additional information in 
    	//check methods
    	final String methodName = "goToElement";
    	
    	//check whether the current transaction and the current closed action
    	//are okay
    	if (checkTransaction(_transactionID, methodName) 
    			&& checkClosedAction(_closedActionID, methodName)) {
    		
    		//perform method call.
    		ls.goToElement(_elemCurrent);
    	} else if (debug_stay_running) {
    		ls.goToElement(_elemCurrent);
    	}
    }
	
	

    /*
     * Methods for getting content of the list's current element or for printing
     * the content of the list or transforming the list into an array. 
     * They are not changing the current state of the list; thus it is not 
     * necessary to check the state of transaction or closedAction.
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

    
    /*
     * Method that change the list (not only by changing the current pointer 
     * position). 
     * 
     * In Closed actions that is forbidden because the state of the list
     * is not reproducible.
     */
    
    /**
     * Replaces current element with newContent.
     *
     * @param _newContent contains the content which is to be inserted.
     * 
     * @param _transactionID 
     * 				the id of the transaction to which performs the
     * 				method call.
     */
    public final void replace(final SecureListType _newContent,
    		final int _transactionID) {

    	//the method name for being able to print additional information in 
    	//check methods
    	final String methodName = "replace";
    	
    	//check whether the current transaction and the current closed action
    	//are okay
    	if (checkTransaction(_transactionID, methodName) 
    			&& checkClosedAction(ID_NO_PREDECESSOR, methodName)) {
    		
    		//perform method call.
    		ls.replace(_newContent);
    	} else if (debug_stay_running) {
    		ls.replace(_newContent);
    	}
    }

    
    /**
     * Insert element after current position.
     *
     * @param _newContent contains the content which is to be inserted.
     * 
     * @param _transactionID 
     * 				the id of the transaction to which performs the
     * 				method call.
     */
    public final void insertBehind(final SecureListType _newContent,
    		final int _transactionID) {

    	//the method name for being able to print additional information in 
    	//check methods
    	final String methodName = "insertBehind";
    	
    	//check whether the current transaction and the current closed action
    	//are okay
    	if (checkTransaction(_transactionID, methodName) 
    			&& checkClosedAction(ID_NO_PREDECESSOR, methodName)) {
    		
    		//perform method call.
    		ls.insertBehind(_newContent);
    	} else if (debug_stay_running) {
    		ls.insertBehind(_newContent);
    	}
    }

    
    /**
     * Insert element in front of current position.
     *
     * @param _newContent contains the content which is to be inserted.
     * 
     * @param _transactionID 
     * 				the id of the transaction to which performs the
     * 				method call.
     * 
     */
    public final void insertInFrontOf(final SecureListType _newContent,
    		final int _transactionID) {

    	//the method name for being able to print additional information in 
    	//check methods
    	final String methodName = "insertInFrontOf";
    	
    	//check whether the current transaction and the current closed action
    	//are okay
    	if (checkTransaction(_transactionID, methodName) 
    			&& checkClosedAction(ID_NO_PREDECESSOR, methodName)) {
    		
    		//perform method call.
    		ls.insertInFrontOf(_newContent);
    	} else if (debug_stay_running) {
    		ls.insertInFrontOf(_newContent);
    	}
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

    	//the method name for being able to print additional information in 
    	//check methods
    	final String methodName = "remove";
    	
    	//check whether the current transaction and the current closed action
    	//are okay
    	if (checkTransaction(_transactionID, methodName) 
    			&& checkClosedAction(ID_NO_PREDECESSOR, methodName)) {
    		
    		//perform method call.
    		ls.remove();
    	} else if (debug_stay_running) {
    		ls.remove();
    	}
    }

    
    /**
     * Inserts s.th. at the beginning of the list.
     *
     * @param _newContent contains the content which is to be inserted.
     * 
     * @param _transactionID 
     * 				the id of the transaction to which performs the
     * 				method call.
     */
    public final void insertAfterHead(final SecureListType _newContent,
    		final int _transactionID) {

    	//the method name for being able to print additional information in 
    	//check methods
    	final String methodName = "insertAfterHead";
    	
    	//check whether the current transaction and the current closed action
    	//are okay
    	if (checkTransaction(_transactionID, methodName) 
    			&& checkClosedAction(ID_NO_PREDECESSOR, methodName)) {
    		
    		//perform method call.
    		ls.insertAfterHead(_newContent);
    	} else if (debug_stay_running) {
    		ls.insertAfterHead(_newContent);
    	}
    }

    
    /**
     * Inserts thing at the end of the list.
     *
     * @param _newContent contains the content which is to be inserted.
     * 
     * @param _transactionID 
     * 				the id of the transaction to which performs the
     * 				method call.
     */
    public final void insertAtTheEnd(final SecureListType _newContent,
    		final int _transactionID) {

    	//the method name for being able to print additional information in 
    	//check methods
    	final String methodName = "insertAtTheEnd";
    	
    	//check whether the current transaction and the current closed action
    	//are okay
    	if (checkTransaction(_transactionID, methodName) 
    			&& checkClosedAction(ID_NO_PREDECESSOR, methodName)) {
    		
    		//perform method call.
    		ls.insertAtTheEnd(_newContent);
    	} else if (debug_stay_running) {
    		ls.insertAtTheEnd(_newContent);
    	}
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

    	//the method name for being able to print additional information in 
    	//check methods
    	final String methodName = "find";
    	
    	//check whether the current transaction and the current closed action
    	//are okay
    	if (checkTransaction(_transactionID, methodName) 
    			&& checkClosedAction(ID_NO_PREDECESSOR, methodName)) {
    		
    		//perform method call.
    		return ls.find(_type);
    	} else if (debug_stay_running) {
    		return ls.find(_type);
    	}
    	
    	//return false if unable to perform action.
    	return false;
    }
    
    
    
	
	/*
	 * Method for starting and finishing closed action and transactions and
	 * for checking whether it is okay to perform an action in special 
	 * action and transaction.
	 */
	
	
	

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
    		stck_closedAction = new Stack<ClosedAction<SecureListType>>();
    		return -1;
    	}
    	
    	//check whether it is valid to start a new closed action
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
    		stck_closedAction = new Stack<ClosedAction<SecureListType>>();
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
    	
    	
    	//threshold: the stack must not be null. catch this error.
    	if (stck_transaction == null) {
    		Status.getLogger().severe("The stack is null! That should not "
    				+ "happen! New transaction name: "
        				+ _transactionName);
    		stck_transaction = new Stack<Transaction<SecureListType>>();
    		return -1;
    	}
    	
    	//check whether it is valid to start a new closed action
    	if (!stck_transaction.isEmpty()) {

        	//the old closedAciton
    		Transaction<SecureListType> ca_old = 
        			((Transaction<SecureListType>)  
        					stck_transaction.getElem_last().getContent());
        	
        	
        	//if the given id does not equal the one of the last transaction
        	//and the current element is null and the stack is not empty
        	if (ca_old != null 
        			&& !stck_transaction.isEmpty()
        			&& ca_old.getId_secureList() != _oldTransactionID) {
        		
        		Status.getLogger().severe("Transaction " + ca_old.getName() 
        				+ " not terminated! New transaction name: "
        				+ _transactionName);
        		return ID_NO_PREDECESSOR;
        	} 
    	}
    	

    	//create new closed action and insert it into the stack
    	Transaction<SecureListType> ca_new 
    	= new Transaction<SecureListType>(_transactionName);
    	stck_transaction.insert(ca_new);
    	return ca_new.getId_secureList();
    }

    
    /**
     * Finish transaction; delete the current transaction.
     * 
     * @param _oldTransactionID the id of the transaction which is to be closed
     * 
     * @return the unique id of the current transaction
     */
    public final int finishTransaction(final int _oldTransactionID) {

    	//threshold: the stack must not be null. catch this error.
    	if (stck_transaction == null) {
    		Status.getLogger().severe("The stack is null! That should not "
    				+ "happen!");
    		stck_transaction = new Stack<Transaction<SecureListType>>();
    		return -1;
    	}
    	
    	

    	//the old closedAciton
    	Transaction<SecureListType> ca_old = 
    			((Transaction<SecureListType>)  
    					stck_transaction.getElem_last().getContent());
    	
    	
    	//if the given id does not equal the one of the last transaction
    	if (ca_old != null 
    			&& ca_old.getId_secureList() != _oldTransactionID) {
    		
    		Status.getLogger().severe("Wrong closed action to be terminated."
    				+ " Current one: " 
    				+ ca_old.getName()
    				+ " Old id: " + _oldTransactionID);
    		return ca_old.getId_secureList();
    	} else {

    		//remove the current transaction.
        	stck_transaction.remove();

    		if (!stck_transaction.isEmpty()) {

        		//return the new current id.
    			Transaction<SecureListType> ca_current = 
            			((Transaction<SecureListType>)  
            					stck_transaction.getElem_last().getContent());
        		return ca_current.getId_secureList();
    		} else {
    			return -1;
    		}
    	}
    }
    
    
    
    
    
    
    
    
    
    

    
    

    /**
     * Check whether the closed action with given id is okay to start. Otherwise
     * print different errors. The error messages depend on the current state
     * of the stack and contain the name of the calling method.
     * 
     * @param _closedActionID
     * 				the id of the closed action to which the action belong
     * 
     * @param _methodName
     * 				the name of the method which calls the startClosedAction
     * 				check method used for printing detailed error message.
     *
     * @return whether it is okay to start the action or not.
     */
    private boolean checkClosedAction(final int _closedActionID, 
    		final String _methodName) {
    	
    	/*
    	 * Initialize values. 
    	 */
    	
    	//boolean containing the return value
    	boolean success = false;
    	
    	//integer containing the currently active closed action id. Used
    	//for detailed error message in case of no success.
    	int currentCAID = -1 - 1;

    	//String containing the currently active closed action name. Used
    	//for detailed error message in case of no success.
    	String currentCAName = "Undefined (currentCAID == -2)";
    	
    	//String containing the error message which is appended to additional
    	//information on the action which is to be performed, the closed action
    	//which is given to this method and the closed action which is currently
    	//active and then logged in case an error occurred.
    	String errorMessage = "";
    	
    	/*
    	 * Check whether the action is okay to start
    	 */
    	
    	//threshold: the stack must not be null. catch this error.
    	if (stck_closedAction == null) {
    		
    		//print error
    		errorMessage = ("The stack is null! That should never "
    				+ "happen!");
    		
    		//re-initialize the stack for being able to proceed without 
    		//throwing this error each time a function is called that
    		//deals with closed actions.
    		stck_closedAction = new Stack<ClosedAction<SecureListType>>();
    		
    		//return true because obviously no closed action is running; 
    		//thus it is impossible that the current closed action is not 
    		//suitable.
    		success = true;
    	} else if (
    			//check whether it is valid to start a new closed action
    			!stck_closedAction.isEmpty()) {

        	//the current closed action
        	ClosedAction<SecureListType> ca_old = 
        			((ClosedAction<SecureListType>)  
        			stck_closedAction.getElem_last().getContent());
        	

    		//if the closed action id is not equal to the id which is passed
    		//if there is nor closed action running print an error. Otherwise
    		//everything is okay and the next - method can be performed.
    		if (ca_old == null) {

        		//print error
    			errorMessage = ("Trying to perform an action "
        				+ "which is part of a not - existing closed action."
        				+ " There is not closed action running."
        				+ " The isEmpty method is not working or the "
        				+ "method is called twice at a time.");

    			//print error message because otherwise the error might 
    			//disappear if the second approach is successful
            	Status.getLogger().severe(errorMessage); 
        		
        		//remove the current (null) - closed action for being able to 
        		//proceed without  throwing this error each time a function 
        		//is called that deals with closed actions.
        		stck_closedAction.remove();
        		
        		//re - all this method with the altered stack. 
        		success = checkClosedAction(_closedActionID, _methodName);

    		} else {
    			
    			//fetch the values for printing information in case an
    			//error occurs.
    			currentCAName = ca_old.getName();
    			currentCAID = ca_old.getId_secureList();
    			
    			if (
    					//if the given id does not equal the one of the current 
    					//closed action
    					ca_old.getId_secureList() != _closedActionID) {

    				//print error message.
    				errorMessage = ("Trying to perform an action "
            				+ "which is part a different closed action.");
            		
            		//return false because obviously this is the wrong closed 
    				//action
    				success = false;
            	} else {
            		success = true;
            	}
    		}
    				
    	} else {
    		
    		//if the closed action id is not equal to the id which is passed
    		//if there is nor closed action running print an error. Otherwise
    		//everything is okay and the next - method can be performed.
    		if (_closedActionID != ID_NO_PREDECESSOR) {

        		//print error
    			errorMessage = ("Trying to perform an action "
        				+ "which is part of a not - existing closed action."
        				+ " There is not closed action running.");
            	
        		//return true because if there is no closed action that
        		//is running, the action can be completed.
        		success =  true;
    		} else {
    			
    			success = true;
    		}
    	}
    	
    	
    	//if an error occurred print error message.
    	if (!success) {
    		
    		//the text which is appended to the error string generated during 
        	//a pass of this method.
        	final String errorInformationText = 
        			"Error starting closed action."
        					+ "\n\tDemanded for Action:  " + _methodName
        					+ "\n\tDemanded action-id:   " + _closedActionID
        					+ "\n\tCurrenct action-id:   " + currentCAID
        					+ "\n\tCurrent  action-name: " + currentCAName;
        	
        	//print error message
        	Status.getLogger().severe(errorInformationText 
        			+ ":\n" + errorMessage); 
        	
    	}
    	
    	//return success
		return success;
    	
	}
    

    
    

    /**
     * Check whether the transaction with given id is okay to start. Otherwise
     * print different errors. The error messages depend on the current state
     * of the stack and contain the name of the calling method.
     * 
     * @param _transactionID
     * 				the id of the transaction to which the action belong
     * 
     * @param _methodName
     * 				the name of the method which calls the startTransaction
     * 				check method used for printing detailed error message.
     *
     * @return whether it is okay to start the action or not.
     */
    private boolean checkTransaction(final int _transactionID, 
    		final String _methodName) {

    	/*
    	 * Initialize values. 
    	 */
    	
    	//boolean containing the return value
    	boolean success = false;
    	
    	//integer containing the currently active transaction id. Used
    	//for detailed error message in case of no success.
    	int currentCAID = -1 - 1;

    	//String containing the currently active transaction name. Used
    	//for detailed error message in case of no success.
    	String currentCAName = "";
    	
    	//String containing the error message which is appended to additional
    	//information on the action which is to be performed, the transaction
    	//which is given to this method and the transaction which is currently
    	//active and then logged in case an error occurred.
    	String errorMessage = "";
    	
    	/*
    	 * Check whether the action is okay to start
    	 */
    	//threshold: the stack must not be null. catch this error.
    	if (stck_transaction == null) {
    		
    		//print error
    		errorMessage = ("The stack is null! That should never "
    				+ "happen!");
    		
    		//re-initialize the stack for being able to proceed without 
    		//throwing this error each time a function is called that
    		//deals with transactions.
    		stck_transaction = new Stack<Transaction<SecureListType>>();
    		
    		//return true because obviously no transaction is running; 
    		//thus it is impossible that the current transaction is not 
    		//suitable.
    		success = true;
    	}
    	
    	//check whether it is valid to start a new transaction
    	if (!stck_transaction.isEmpty()) {

        	//the current transaction
    		Transaction<SecureListType> ca_old = 
        			((Transaction<SecureListType>)  
        					stck_transaction.getElem_last().getContent());
        	

    		//if the transaction id is not equal to the id which is passed
    		//if there is nor transaction running print an error. Otherwise
    		//everything is okay and the next - method can be performed.
    		if (ca_old == null) {

        		//print error
    			errorMessage = ("Trying to perform an action "
        				+ "which is part of a not - existing transaction."
        				+ " There is not transaction running."
        				+ " The isEmpty method is not working or the "
        				+ "method is called twice at a time.");

    			//print error message because otherwise the error might 
    			//disappear if the second approach is successful
            	Status.getLogger().severe(errorMessage); 
        		
        		//remove the current (null) - transaction for being able to 
        		//proceed without  throwing this error each time a function 
        		//is called that deals with transactions.
        		stck_closedAction.remove();
        		
        		//re - all this method with the altered stack. 
        		success = checkClosedAction(_transactionID, _methodName);
        		
    		} else {
    			
    			//fetch the values for printing information in case an
    			//error occurs.
    			currentCAName = ca_old.getName();
    			currentCAID = ca_old.getId_secureList();
    			
    			if (
    					//if the given id does not equal the one of the current 
    					//transaction
    					ca_old.getId_secureList() != _transactionID) {

    				//print error message.
    				errorMessage = ("Trying to perform an action "
            				+ "which is part a different transaction.");
            		
            		//return false because obviously this is the wrong closed 
    				//action
    				success = false;
            	} else {
            		success = true;
            	}
    		}
    				
    	} else {
    		
    		//if the transaction id is not equal to the id which is passed
    		//if there is nor transaction running print an error. Otherwise
    		//everything is okay and the next - method can be performed.
    		if (_transactionID != ID_NO_PREDECESSOR) {

        		//print error
    			errorMessage = ("Trying to perform an action "
        				+ "which is part of a not - existing transaction."
        				+ " There is not transaction running.");
            	
        		//return true because if there is no transaction that
        		//is running, the action can be completed.
    			success = true;
    		} else {
    			
    			success = true;
    		}
    	}

    	//if an error occurred print error message.
    	if (!success) {
    		
    		//the text which is appended to the error string generated during 
        	//a pass of this method.
        	final String errorInformationText = 
        			"Error starting transaction."
        					+ "\n\tDemanded for Action:  " + _methodName
        					+ "\n\tDemanded action-id:   " + _transactionID
        					+ "\n\tCurrenct action-id:   " + currentCAID
        					+ "\n\tCurrent  action-name: " + currentCAName;
        	
        	//print error message
        	Status.getLogger().severe(errorInformationText 
        			+ ":\n" + errorMessage); 
        	
    	}
    	
    	//return success
		return success;
    	
	}
}
    