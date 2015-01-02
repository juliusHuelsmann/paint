package model.util.adt.list;


/**
 * Secure element which contains a string identifying the transaction and
 * its unique id. It is used inside the list of Transactions in SecureList.
 * 
 * @author Julius Huelsmann
 * @version %I%, %U%
 * @param <ClosedActionType>
 */
public class Transaction<ClosedActionType> {


	/**
     * The unique id of the current closed action.
     */
    private int id_secureList = 0;

    
    /**
     * The unique id of the current closed action.
     */
    private static int maxID = 0;

    
	/**
	 * The name identifying the transaction for debugging purpose and
	 * for being able to print the current list action.
	 */
	private String name;

	
	/**
	 * Constructor: (initializes) the content with start values.
	 */
	public Transaction() {

        //set title of transaction
        this.name = "";

        //set unique id and change the max id.
        id_secureList = maxID++;
	}
	
	
	/**
	 * Constructor: (initializes) the content with start values passed by 
	 * calling methods.
	 * @param _name 
	 * 					the name of the transaction
	 */
	public Transaction(final String _name) {

        //set title of transaction
        this.name = _name;
	}
	
	
	/**
	 * @return the name
	 */
	public final String getName() {
		return name;
	}
	
	
	/**
	 * @param _name the name to set
	 */
	public final void setName(final String _name) {
		this.name = _name;
	}


	/**
	 * @return the id_secureList
	 */
	public final int getId_secureList() {
		return id_secureList;
	}


}
