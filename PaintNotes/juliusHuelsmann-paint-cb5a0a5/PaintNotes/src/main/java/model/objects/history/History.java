//package declaration
package model.objects.history;

import java.util.Observable;

import model.objects.painting.Picture;
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
    
    private Picture pic;
    /**
     * Utility class constructor.
     */
    public History(Picture _pic) {
    	this.pic = _pic;
    }

    /**
     * add new history.
     * @param _hist the history to add
     */
    public void addHistory(final String _hist) {
        ls_history.insertAtTheEnd(_hist);
        
        setChanged();
        notifyObservers("add " + _hist);
    }    
    
    
    /**
     * remove history.
     * @param _hist the history to be removed
     */
    public void removeHistory(final String _hist) {
        if (ls_history.find(_hist)) {
            ls_history.remove();
        } else {
            Status.getLogger().warning("History" + "error");
        }
        setChanged();
        notifyObservers("remove " + _hist);
    }  
    

    
    
}
