//package declaration
package model.util.list;

//import declaration
import java.io.Serializable;

import model.objects.painting.po.PaintObject;
import model.settings.Status;
import model.util.DPoint;

/**
 * Double linked list (not a ring - list).
 *
 *
 * @author Julius Huelsmann
 * @version Milestone 2
 * @param <Type>
 */
public class List<Type> implements Serializable {

    /**
     * Default serial version UID.
     */
    private static final long serialVersionUID = 1L;

    /**
     * First element.
     */
    private final Element<Type> elemFirst;

    /**
     * Current element.
     */
    private Element<Type> elemCurrent;

    /**
     * Last element.
     */
    private final Element<Type> elemLast;
    
    /**
     * Element for transactions. If the current element of the list
     * is to be maintained after a performed action which passes the list 
     * start transaction before the action and call endTransaction afterwards.
     */
    private  Element<Type> elem_transaction;
    
    /**
     * String containing the title of current transaction.
     * @see elem_transaction
     */
    private String strg_transactionTitle;
    
    /**
     * whether to sort ascending or descending.
     */
    private boolean sortAsc = true;

    /**
     * Initialize instance of List - initialize first and last element.
     */
    public List() {
        this.elemFirst = new Element<Type>(null, null, null);
        this.elemLast = new Element<Type>(null, null, elemFirst);
        this.strg_transactionTitle = "";

        this.elemFirst.setElemSuccessor(elemLast);
        this.elemCurrent = elemFirst;
    }

    /**
     * Returns weather is empty.
     *
     * @return weather list is empty.
     */
    public final boolean isEmpty() {
        return (elemFirst.getElemSuccessor() == elemLast);
    }

    /**
     * Return weather it is in front of.
     *
     * @return weather list is in front of.
     */
    public final boolean isInFrontOf() {
        return (elemCurrent == elemFirst);
    }

    /**
     * Return weather list is behind.
     *
     * @return weather list is behind.
     */
    public final boolean isBehind() {
        return (elemCurrent == elemLast);
    }

    /**
     * Proceed one step in the list.
     */
    public final void next() {
        if (!isBehind()) {
            elemCurrent = elemCurrent.getElemSuccessor();
        }
    }

    /**
     * Step back in the list.
     */
    public final void previous() {
        if (!isInFrontOf()) {
            elemCurrent = elemCurrent.getElemPredecessor();
        }
    }

    /**
     * Go to the beginning of the list.
     */
    public final void toFirst() {
        if (isEmpty()) {
            elemCurrent = elemFirst;
        } else {
            elemCurrent = elemFirst.getElemSuccessor();
        }
    }
    
    /**
     * create subList.
     * @return list after current item.
     */
    public final List<Type> subList() {
        
        //create list to be returned.
    	List<Type> ls = new List<Type>();
    	
    	//go through the list beginning at current element
    	//and insert item
    	while (!isBehind()) {
    		ls.insertAtTheEnd(getItem());
    		next();
    	}
    	
    	//return list
    	return ls;
    }

    /**
     * Go to the end of the list.
     */
    public final void toLast() {
        if (isEmpty()) {
            elemCurrent = elemLast;
        } else {
            elemCurrent = elemLast.getElemPredecessor();
        }
    }

    /**
     * Return current Element.
     *
     * @return current Element.
     */
    public final Type getItem() {
        if (isEmpty() || isInFrontOf() || isBehind()) {
            return null;
        } else {
            return elemCurrent.getContent();
        }
    }
    /**
     * Return current Element.
     *
     * @return current Element.
     */
    public final Element<Type> getElement() {
        if (isEmpty() || isInFrontOf() || isBehind()) {
            return null;
        } else {
            return elemCurrent;
        }
    }
    
    /**
     * Set the current element.
     * @param _elemCurrent the current elmenet.
     */
    public final void setCurrentElement(final Element<Type> _elemCurrent) {
        elemCurrent = _elemCurrent;
    }

    /**
     * Return sort index of the current Element.
     *
     * @return sorted index of current Element.
     */
    public final double getItemSortionIndex() {
        if (isEmpty() || isInFrontOf() || isBehind()) {
            return -1;
        } else {
            return elemCurrent.getSortedIndex();
        }
    }

    /**
     * Replaces current element with newContent.
     *
     * @param _newContent contains the content which is to be inserted.
     */
    public final void replace(final Type _newContent) {
        if (!isEmpty() && !isBehind() && !isInFrontOf()) {
            elemCurrent.setContent(_newContent);
        }
    }

    /**
     * Insert element after current position.
     *
     * @param _newContent contains the content which is to be inserted.
     */
    public final void insertBehind(final Type _newContent) {

        Element<Type> elemNew;
        if (isEmpty()) {
            elemNew = new Element<Type>(_newContent, elemLast, elemFirst);
            elemFirst.setElemSuccessor(elemNew);
            elemLast.setElemPredecessor(elemNew);
        } else if (isBehind()) {
            elemNew = new Element<Type>(_newContent, elemLast,
                    elemLast.getElemPredecessor());
            elemLast.getElemPredecessor().setElemSuccessor(elemNew);
            elemLast.setElemPredecessor(elemNew);
        } else if (isInFrontOf()) {
            elemNew = new Element<Type>(_newContent,
                    elemFirst.getElemSuccessor(), elemFirst);
            elemFirst.getElemSuccessor().setElemPredecessor(elemNew);
            elemFirst.setElemSuccessor(elemNew);
        } else {
            elemNew = new Element<Type>(_newContent,
                    elemCurrent.getElemSuccessor(), elemCurrent);
            elemCurrent.getElemSuccessor().setElemPredecessor(elemNew);
            elemCurrent.setElemSuccessor(elemNew);
        }

        this.elemCurrent = elemNew;
    }

    /**
     * Insert element in front of current position.
     *
     * @param _newContent contains the content which is to be inserted.
     */
    public final void insertInFrontOf(final Type _newContent) {
        Element<Type> elemNew;
        if (isEmpty()) {
            elemNew = new Element<Type>(_newContent, elemLast, elemFirst);
            elemFirst.setElemSuccessor(elemNew);
            elemLast.setElemPredecessor(elemNew);
        } else if (isBehind()) {
            elemNew = new Element<Type>(_newContent, elemLast,
                    elemLast.getElemPredecessor());
            elemLast.getElemPredecessor().setElemSuccessor(elemNew);
            elemLast.setElemPredecessor(elemNew);
        } else if (isInFrontOf()) {
            elemNew = new Element<Type>(_newContent,
                    elemFirst.getElemSuccessor(), elemFirst);
            elemFirst.getElemSuccessor().setElemPredecessor(elemNew);
            elemFirst.setElemSuccessor(elemNew);
        } else {
            elemNew = new Element<Type>(_newContent, elemCurrent,
                    elemCurrent.getElemPredecessor());
            elemCurrent.getElemPredecessor().setElemSuccessor(elemNew);
            elemCurrent.setElemPredecessor(elemNew);
        }
        this.elemCurrent = elemNew;
    }

    /**
     * Removes current element.
     * Afterwards in front of the removed element.
     */
    public final void remove() {
    	
    	
        if (!isEmpty() && !isBehind() && !isInFrontOf()) {
        	
        	if (elemCurrent.getContent() instanceof PaintObject)
        		System.out.println("removing " + ((PaintObject)elemCurrent.getContent()).getElementId());
           
        	Element<Type> succ = elemCurrent.getElemSuccessor();
            succ.setElemPredecessor(elemCurrent.getElemPredecessor());
            
            Element <Type> pred = elemCurrent.getElemPredecessor();
            pred.setElemSuccessor(succ);
            
//            if (pred == null) {
//            	elemCurrent =  elemFirst;
//            	elemCurrent.setElemSuccessor(succ);
//            } else {

                elemCurrent = pred;
                
//            }
            
            
        } else {
        	System.err.println("ELZ RM");
            if (isInFrontOf()) {
                next();
            }
            if (isBehind()) {
                previous();
                
            }
            
        }
    }

    /**
     * Inserts s.th. at the beginning of the list.
     *
     * @param _newContent contains the content which is to be inserted.
     */
    public final void insertAfterHead(final Type _newContent) {
        toFirst();
        insertInFrontOf(_newContent);
    }

    /**
     * Inserts thing at the end of the list.
     *
     * @param _newContent contains the content which is to be inserted.
     */
    public final void insertAtTheEnd(final Type _newContent) {
        toLast();
        insertBehind(_newContent);
    }

    /**
     * insert one item if it does not already exist in list.
     * @param _type the item to be inserted.
     */
    public final void insertOnce(final Type _type) {
    	
        //if item does not already exist in list
        if (!find(_type)) {
            
            //insert
            insertAfterHead(_type);
    	}
    }
    
    
    /**
     * check whether item does already exist in list.
     * 
     * @param _type which is checked
     * @return whether already exist
     */
    public final boolean find(final Type _type) {
    	
        //go to first element
        toFirst();
    	
    	//go through list
    	while (!isBehind() && !isEmpty()) {

    	    //if found item return (and exit)
    		if (getItem().equals(_type)) {
                return true;
    		}
    		
    		//next
            next();  
    	}
    	
    	//not found and reached end of list.
    	return false;
    }
    
    
    /**
     * insert sorted ASC.
     * @param _content the content to be inserted.
     * @param _searchCriteria the index of sorting.
     */
    public final synchronized void insertSorted(final Type _content, 
            final double _searchCriteria) {
        
        findSorted(_searchCriteria);
        if (sortAsc) {
            insertInFrontOf(_content);
        } else {
            insertBehind(_content);
        }
        elemCurrent.setSortedIndex(_searchCriteria);
    }

    
    /**
     * Set sort criteria to ascending.
     */
    public final synchronized void setSortASC() {
        if (isEmpty()) {
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
        if (isEmpty()) {
            sortAsc = false;
        } else {
            Status.getLogger().warning("tried to change sorting order without"
                    + "success: The list is not empty and thus may have"
                    + "been sorted in a different order. " + sortAsc + "false");
        }
    }
    
    /**
     * goes behind the searched position.
     * @param _searchCriteria the search index.
     */
    public final synchronized void findSorted(final double _searchCriteria) {
        
        toFirst();
        while (!isBehind() && elemCurrent.getSortedIndex() < _searchCriteria) {
            next();
        }
    }
    
    /**
     * print items with search index.
     */
    public final void printIndex() {

    	Element<Type> oldCurrent = elemCurrent;
        System.out.println("\n\nprint\n");
        toFirst();
        while (!isBehind()) {
            
            System.out.println("Index" + elemCurrent.getSortedIndex() 
                    + ":   "  + getItem());
            next();
        }
        elemCurrent = oldCurrent;
        System.out.println("\n\n");
    }

    
    /**
     * print items with search index.
     */
    public final void printAddcounter() {
        
    	Element<Type> oldCurrent = elemCurrent;
    	
        Status.getLogger().info("\n\nprint counter. Caution; only valid if list"
        		+ "is holding PaintObjects.\n");
        toFirst();
        while (!isBehind()) {
            
        	if (getItem() instanceof PaintObject) {
        		System.out.println(">" 
        				+ ((PaintObject)getItem()).getElementId());
        	}
            next();
        }
        
        elemCurrent = oldCurrent;
        System.out.println("\n\n");
    }
    
    
    
    /**
     * List to array method.
     * @return the array from list.
     */
    public final synchronized DPoint[] toArray() {
    	int length = 0;
    	toFirst();
    	while (!isBehind()) {
    		if (getItem() instanceof DPoint) {

        		length++;
    		}
    		next();
    	}

    	DPoint[] ret = new DPoint[length];
    	toFirst();
    	int i = 0;
    	while (!isBehind()) {

    		if (getItem() instanceof DPoint) {
    			ret [i] = (DPoint) getItem();
    			i++;
    		}
    		next();
    	}
    	return ret;
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
    public final void startTransaction(String _operationName) {
    	
    	if (elem_transaction != null) {
    		Status.getLogger().severe("Transaction " + strg_transactionTitle 
    				+ " not terminated!");
    	} 

    	elem_transaction = elemCurrent;
    	strg_transactionTitle = _operationName + System.currentTimeMillis();
    }

    
    /**
     * Finish transaction; reset the state of the List and afterwards the
     * transaction values.
     */
    public final void finishTransaction() {
    	if (elem_transaction != null || strg_transactionTitle != "") {

        	elemCurrent = elem_transaction;
        	elem_transaction = null;
        	strg_transactionTitle = "";
    	} else {
    		Status.getLogger().severe("transaction error.");
    	}
    }
    
    
//    public static void main(String[]args){
//        List<String> ls = new List<String>();
//        ls.printIndex();
//        ls.insertSorted("hi", 1);
//        ls.printIndex();
//        ls.insertSorted("du", 2);
//        ls.printIndex();
//        ls.insertSorted("da", 3);
//        ls.printIndex();
//        ls.insertSorted("er sagte", 0);
//        ls.printIndex();
//        ls.insertSorted("w", -1);
//        ls.printIndex();
//        ls.insertSorted("__", -1);
//        ls.printIndex();
//        ls.insertSorted("asdf", -2);
//        ls.printIndex();
//        ls.insertSorted("d", 1);
//        ls.printIndex();
//    }
}
