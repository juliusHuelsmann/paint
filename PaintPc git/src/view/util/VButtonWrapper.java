//package declaration
package view.util;

//import declaration
import javax.swing.JButton;
import settings.Error;


/**
 * Wrapper class which delivers a certain object to ActionListener.
 * 
 * @author Julius Huelsmann
 * @version %U%, %I%
 */
@SuppressWarnings("serial")
public class VButtonWrapper extends JButton {

	/**
	 * The object that is to be delivered to the actionListener.
	 */
	private Object o_delivered;
	
	
	/**
	 * Constructor: save the object.
	 * @param _o_delivered the object to be delivered.
	 */
	public VButtonWrapper(final Object _o_delivered) {
	    
	    //if delivered item is null
	    if (_o_delivered == null) {

            Error.printError(getClass().getSimpleName(), 
                    "paintComponent(...)", "exception occured", 
                    new Exception(), Error.ERROR_MESSAGE_INTERRUPT); 
	    }
	    
	    //set delivered item
		this.o_delivered = _o_delivered;
	}
	
	
	/**
	 * simple getter method which returns the delivered object.
	 * @return the delivered object.
	 */
	public final Object wrapObject() {
		return o_delivered;
	}
}
