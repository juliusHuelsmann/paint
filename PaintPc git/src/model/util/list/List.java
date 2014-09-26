//package declaration
package model.util.list;

//import declaration
import java.io.Serializable;

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
     * Initialize instance of List - initialize first and last element.
     */
    public List() {
        this.elemFirst = new Element<Type>(null, null, null);
        this.elemLast = new Element<Type>(null, null, elemFirst);

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
     * Return sort index of the current Element.
     *
     * @return sorted index of current Element.
     */
    public final int getItemSortionIndex() {
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
     */
    public final void remove() {
        if (!isEmpty() && !isBehind() && !isInFrontOf()) {
            Element<Type> succ = elemCurrent.getElemSuccessor();
            succ.setElemPredecessor(elemCurrent.getElemPredecessor());
            elemCurrent.getElemPredecessor().setElemSuccessor(succ);
            elemCurrent = succ.getElemPredecessor();
        } else {
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
    	while (!isBehind()) {

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
            final int _searchCriteria) {
        
        findSorted(_searchCriteria);
        insertInFrontOf(_content);
        elemCurrent.setSortedIndex(_searchCriteria);

    }
    
    /**
     * goes behind the searched position.
     * @param _searchCriteria the search index.
     */
    public final synchronized void findSorted(final int _searchCriteria) {
        
        toFirst();
        while (!isBehind() && elemCurrent.getSortedIndex() < _searchCriteria) {
            next();
        }
    }
    
    /**
     * print items with search index.
     */
    public final void printIndex() {
        
        System.out.println("\n\nprint\n");
        toFirst();
        while (!isBehind()) {
            
            System.out.println("Index" + elemCurrent.getSortedIndex() 
                    + ":   "  + getItem());
            next();
        }
        System.out.println("\n\n");
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
