//package declaration
package model.util.adt.list;

//import declaration
import java.io.Serializable;

import model.objects.painting.po.PaintObject;
import model.settings.Status;
import model.util.DPoint;

/**
 * Double linked list which is not a ring list. Contains methods for checking
 * and changing the current position in list, for 
 *
 * @author Julius Huelsmann
 * @version %I%, %U%
 * @param <Type>
 */
public class List<Type> implements Serializable {

	/*
	 * variable for saving list:
	 */
	
    /**
     * Default serial version UID for being able to identify the list's 
     * version if saved to the disk and check whether it is possible to 
     * load it or whether important features have been added so that the
     * saved file is out-dated.
     */
    private static final long serialVersionUID = 1L;

    
    /*
     * Variables defining the fundamental list structure:
     */
    
    /**
     * The first element in list, saved for being able to jump to the beginning
     * of the list very quickly and for a simple is-in-front-of check.
     */
    private final Element<Type> elemFirst;

    
    /**
     * The current element in list.
     */
    private Element<Type> elemCurrent;

    
    /**
     * The last element in list, saved for being able to jump to the end
     * of the list very quickly and for a simple is-behind check.
     */
    private final Element<Type> elemLast;
    
    
    /*
     * Variables for list sorting.
     */
    
    
    /**
     * Initialize instance of List - initialize first and last element.
     */
    public List() {
    	
    	//initialize first and last element
        this.elemFirst = new Element<Type>(null, null, null);
        this.elemLast = new Element<Type>(null, null, elemFirst);
        this.elemFirst.setElemSuccessor(elemLast);

        //set the current element to the first
        this.elemCurrent = elemFirst;
        
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

    
    /*
     * Methods for navigating through the list
     */
    
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
     * Go to a special element (has to be inside the list).
     * 
     * @param _elemCurrent the current element in the future.
     */
    public final void goToElement(final Element<Type> _elemCurrent) {
        elemCurrent = _elemCurrent;
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
    
    
    /*
     * Special Methods
     */

    /**
     * create subList.
     * @return list after current item.
     */
    public final List<Type> subList() {
        
        //create list to be returned.
    	List<Type> ls = new List<Type>();
    	Element<Type> elem_currSaved = elemCurrent;
    	
    	//go through the list beginning at current element
    	//and insert item
    	while (!isBehind()) {
    		ls.insertAtTheEnd(getItem());
    		next();
    	}
    	
    	//reset the previous current element.
    	elemCurrent = elem_currSaved;
    	
    	//return list
    	return ls;
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

        	//set insert sorted to be false because this method performs a non-
        	//sorted insertion.
//        	isSorted = false;
        	
        	//set the new content
            elemCurrent.setContent(_newContent);
        }
    }

    /**
     * Insert element after current position.
     *
     * @param _newContent contains the content which is to be inserted.
     */
    public final void insertBehind(final Type _newContent) {

    	//set insert sorted to be false because this method performs a non-
    	//sorted insertion.
//    	isSorted = false;
    	
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
     * Removes current element. Afterwards the current element points
     * to the predecessor of the removed item.
     */
    public final void remove() {
    	
    	
    	//if the current element can be removed
        if (!isEmpty() && !isBehind() && !isInFrontOf()) {
        	
        	//remove the current element. 
        	Element<Type> succ = elemCurrent.getElemSuccessor();
            succ.setElemPredecessor(elemCurrent.getElemPredecessor());
            
            Element<Type> pred = elemCurrent.getElemPredecessor();
            pred.setElemSuccessor(succ);
            
            //The new current element is the predecessor of the removed element
            elemCurrent = pred;
            
        } else {
        	
        	
        	//print warning message
        	Status.getLogger().warning("remove null item out of list: Perform"
        			+ " previous or next for being able to pass the list"
        			+ " and for not creating an infinite loop");
        	
        	//go to one element that is removable
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
    	
    	//go to the first item of the list and insert in front of it the new
    	//content
        toFirst();
        insertInFrontOf(_newContent);
    }

    /**
     * Inserts thing at the end of the list.
     *
     * @param _newContent contains the content which is to be inserted.
     */
    public final void insertAtTheEnd(final Type _newContent) {

    	//go to the last item of the list and insert behind it the new content
        toLast();
        insertBehind(_newContent);
    }

    
    
    /**
     * Check whether item does already exist in list and if that is the case
     * point at it with elemCurrent.
     * 
     * @param _type which is checked
     * @return whether the element exists or not
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
        				+ ((PaintObject) getItem()).getElementId());
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

    	//save the current element of the beginning of passing the list
    	Element<Type> oldCurrent = elemCurrent;
    	
    	//get the length of the array by passing the list once. If the element
    	//is not a DPoint do not count it.
    	int length = 0;
    	toFirst();
    	while (!isBehind()) {
    		if (getItem() instanceof DPoint) {
        		length++;
    		}
    		next();
    	}

    	//create array of DPoints
    	DPoint[] ret = new DPoint[length];
    	
    	
    	//fill the array
    	toFirst();
    	int i = 0;
    	while (!isBehind()) {

    		//if item is not a DPoint it is not
    		if (getItem() instanceof DPoint) {
    			ret [i] = (DPoint) getItem();
    			i++;
    		}
    		next();
    	}
    	
    	//reset current element
        elemCurrent = oldCurrent;
        
        //return the array
    	return ret;
    }
    

    
    /**
     * List to array method.
     * @return the array from list.
     */
    public final synchronized String[] toArrayString() {

    	//save the current element of the beginning of passing the list
    	Element<Type> oldCurrent = elemCurrent;
    	
    	//get the length of the array by passing the list once. If the element
    	//is not a DPoint do not count it.
    	int length = 0;
    	toFirst();
    	while (!isBehind()) {
    		if (getItem() instanceof String) {
        		length++;
    		}
    		next();
    	}

    	//create array of DPoints
    	String[] ret = new String[length];
    	
    	
    	//fill the array
    	toFirst();
    	int i = 0;
    	while (!isBehind()) {

    		//if item is not a DPoint it is not
    		if (getItem() instanceof String) {
    			ret [i] = (String) getItem();
    			i++;
    		}
    		next();
    	}
    	
    	//reset current element
        elemCurrent = oldCurrent;
        
        //return the array
    	return ret;
    }
    
}
