//package declaration
package model.util.adt.stack;

//import declarations
import java.io.Serializable;

/**
 * Element class contains a certain content of type <Type> and a predecessor 
 * of which the stack consists.
 *
 * Contains getter and setter methods for content, predecessor, successor and
 * implements serializable.
 *
 * @author Julius Huelsmann
 * @version %I%, %U%
 * @param <Type>
 */
public class StackElement<Type> implements Serializable {

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
     * Element- specific variables.
     */
    
    /**
     * Content of the ELement.
     */
    private Type content;


    /**
     * Predecessor element.
     */
    private StackElement<Type> elemPredecessor;
    

    /**
     * Constructor: Saves values such as content, successor, predecessor.
     *
     * @param _content 
     * 					the content we want to save.
     * @param _elemPredecessor 
     * 					the predecessor element.
     */
    public StackElement(final Type _content,
            final StackElement<Type> _elemPredecessor) {
    	
        // save values
        this.content = _content;
        this.elemPredecessor = _elemPredecessor;
    }
    

    /**
     * Sets the content.
     *
     * @param _content content for current element.
     */
    public final void setContent(final Type _content) {
        this.content = _content;
    }
    

    /**
     * Returns the content.
     *
     * @return content
     */
    public final Type getContent() {
        return content;
    }

    
    /**
     * Returns the predecessor - element.
     *
     * @return elemPredecessor.
     */
    public final StackElement<Type> getElemPredecessor() {
        return elemPredecessor;
    }

    
    /**
     * Sets the predecessor - element.
     *
     * @param _elemPredecessor .
     */
    public final void setElemPredecessor(
            final StackElement<Type> _elemPredecessor) {
        this.elemPredecessor = _elemPredecessor;
    }
}
