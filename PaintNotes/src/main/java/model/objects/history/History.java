//package declaration
package model.objects.history;

import java.util.Observable;

import model.objects.painting.Picture;
import model.settings.State;
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
    private static List<String> lsHistory;
    
    /**
     * The Picture.
     */
    private Picture pic;
    
    /**
     * Utility class constructor.
     * @param _pic the picture.
     */
    public History(final Picture _pic) {
    	this.pic = _pic;
    }

    /**
     * add new history.
     * @param _hist the history to add
     */
    public void addHistory(final String _hist) {
        lsHistory.insertAtTheEnd(_hist);
        
        setChanged();
        notifyObservers("add " + _hist);
    }    
    
    
    /**
     * remove history.
     * @param _hist the history to be removed
     */
    public void removeHistory(final String _hist) {
        if (lsHistory.find(_hist)) {
            lsHistory.remove();
        } else {
            State.getLogger().warning("History" + "error");
        }
        setChanged();
        notifyObservers("remove " + _hist);
    }

	/**
	 * @return the pic
	 */
	public Picture getPic() {
		return pic;
	}

    
    
}
