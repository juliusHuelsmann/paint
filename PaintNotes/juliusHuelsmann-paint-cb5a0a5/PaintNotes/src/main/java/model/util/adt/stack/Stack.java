package model.util.adt.stack;

//import declarations
import java.io.Serializable;


/**
 * Simple Stack.
 * It is possible to add one item. The current pointer points to the last
 * element of the stack which can be deleted (and by performing this action
 * the pointer moves to its predecessor). It is also possible to add new 
 * elements at the end of the stack (afterwards the pointer points to the new
 * inserted item).
 * 
 * There are basic methods for getting the current state of the list (whether
 * the list currently is empty).
 * 
 * @author Julius Huelsmann
 * @version %I%, %U%
 * @param <StackType>
 */
public class Stack<StackType> implements Serializable {


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
     * stack elements
     */
    
    /**
     * The last element in list. By using this element, it is possible to 
     * reach all element in front of by removing action.
     */
    private StackElement<StackType> elem_last;
	
    
	/**
	 * Constructor.
	 */
	public Stack() {
		elem_last = null;
	}
	
	
	/**
	 * Returns whether the list is empty.
	 * @return whether the list is empty.
	 */
	public final boolean isEmpty() {
		return (elem_last == null);
	}
	
	/**
	 * Removes the last element.
	 * @return whether the list is empty after removing process.
	 */
	public final boolean remove() {
		
		//if the last element is not equal to null, reset the last element.
		if (!isEmpty()) {

			//reset the last element and return whether the list is currently
			//empty
			elem_last = elem_last.getElemPredecessor();
			return isEmpty();
		} else {
			
			//return that the list is empty.
			return true;
		}
	}
	
	/**
	 * Insert element behind the currently last element. Afterwards the 
	 * last element points to the new one.
	 * @param _content 
	 * 					the content of the new item.
	 */
	public final void insert(final StackType _content) {
		
		//set new last element.
		elem_last = new StackElement<StackType>(_content, elem_last);
	}


	/**
	 * @return the elem_last
	 */
	public final StackElement<StackType> getElem_last() {
		return elem_last;
	}
}
