//package declaration
package model.objects.history;

import java.util.Observable;

import model.settings.Status;
import model.util.adt.list.List;


/**
 * 
 * @author Julius Huelsmann
 * @version %I%, %U%
 */
public final class History extends Observable {

    /**
     * the list of history items.
     */
    private static List<String> ls_history;
    
    /**
     * only instance of history.
     */
    private static History instance;
    
    /**
     * Utility class constructor.
     */
    private History() { }

    /**
     * add new history.
     * @param _hist the history to add
     */
    public static void addHistory(final String _hist) {
        ls_history.insertAtTheEnd(_hist);
        
        getInstance().setChanged();
        getInstance().notifyObservers("add " + _hist);
    }    
    
    
    /**
     * remove history.
     * @param _hist the history to be removed
     */
    public static void removeHistory(final String _hist) {
        if (ls_history.find(_hist)) {
            ls_history.remove();
        } else {
            Status.getLogger().warning("History" + "error");
        }
        getInstance().setChanged();
        getInstance().notifyObservers("remove " + _hist);
    }  
    

    /**
     * this method guarantees that only one instance of this
     * class can be created ad runtime.
     * 
     * @return the only instance of this class.
     */
    public static History getInstance() {
        
        
        if (instance == null) {
            instance = new History();
        }
        return instance;
    }
    
    
}
