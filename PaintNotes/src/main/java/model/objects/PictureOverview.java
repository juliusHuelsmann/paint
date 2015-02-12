package model.objects;

import java.util.Observable;

import model.objects.painting.po.PaintObject;
import view.tabs.PaintObjects;

/**
 * The class corresponding to the view PaintObjects class. 
 * Lists amount of PaintObjects.
 * 
 * @author Julius Huelsmann
 * @version %I%, %U%
 */
public class PictureOverview extends Observable {


	/**
	 * the current index.
	 */
	private static int number;
	
	/**
	 * the current PaintObject.
	 */
	private PaintObject currentPO;

    
    /**
     * add paintObject.
     * @param _po the paintObject to be added.
     */
    public final synchronized void add(final PaintObject _po) { 
        
        //increase number and save current PaintObject
        number++;
        this.currentPO = _po;
        
        //notify observer
        setChanged();
        notifyObservers(PaintObjects.ID_ADD_ITEM);
    }
    
    /**
     * add paintObject.
     * @param _po the paintObject to be added.
     */
    public final synchronized void remove(final PaintObject _po) { 
        
        //increase number and save current PaintObject
        number--;
        this.currentPO = _po;
        
        //notify observer
        setChanged();
        notifyObservers(PaintObjects.ID_REMOVE_ITEM);
    }
    
    /**
     * add paintObject.
     * @param _po the paintObject to be added.
     */
    public final synchronized void addSelected(final PaintObject _po) { 
        
        //increase number and save current PaintObject
        number++;
        this.currentPO = _po;
        
        //notify observer
        setChanged();
        notifyObservers(PaintObjects.ID_ADD_ITEM_SELECTED);
    }
    
    /**
     * add paintObject.
     * @param _po the paintObject to be added.
     */
    public final synchronized void removeSelected(final PaintObject _po) { 
        
        //increase number and save current PaintObject
        number--;
        this.currentPO = _po;
        
        //notify observer
        setChanged();
        notifyObservers(PaintObjects.ID_REMOVE_ITEM_SELECTED);
    }

	
	/**
	 * Constructor: adds observer.
	 */
	public PictureOverview(PaintObjects _po) {
		super.addObserver(_po);
	}


    /**
     * @return the number
     */
    public final int getNumber() {
        return number;
    }


    /**
     * @param _number the number to set
     */
    public final void setNumber(final int _number) {
        PictureOverview.number = _number;
    }


    /**
     * @return the currentPO
     */
    public final PaintObject getCurrentPO() {
        return currentPO;
    }


    /**
     * @param _currentPO the currentPO to set
     */
    public final void setCurrentPO(final PaintObject _currentPO) {
        this.currentPO = _currentPO;
    }
}
