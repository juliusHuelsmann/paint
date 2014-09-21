package model.objects;

import java.util.Observable;

import model.objects.painting.PaintObject;
import view.forms.PaintObjects;

/**
 * The class corresponding to the view PaintObjects class. 
 * Lists amount of PaintObjects.
 * 
 * @author Julius Huelsmann
 * @version %I%, %U%
 */
public final class PictureOverview extends Observable {


	/**
	 * the only instance of this class.
	 */
	private static PictureOverview instance = null;

	/**
	 * the current index.
	 */
	private int number;
	
	/**
	 * the current PaintObject.
	 */
	private PaintObject currentPO;

    
    /**
     * add paintObject.
     * @param _po the paintObject to be added.
     */
    public synchronized void add(final PaintObject _po) { 
        
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
    public synchronized void remove(final PaintObject _po) { 
        
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
    public synchronized void addSelected(final PaintObject _po) { 
        
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
    public synchronized void removeSelected(final PaintObject _po) { 
        
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
	private PictureOverview() {
		super.addObserver(PaintObjects.getInstance());
	}
	
	/**
	 * this method guarantees that only one instance of this
	 * class can be created ad runtime.
	 * 
	 * @return the only instance of this class.
	 */
	public static PictureOverview getInstance() {
		
		//if class is not instanced yet instantiate
		if (instance == null) {
			instance = new PictureOverview();
		}
		
		//return the only instance of this class.
		return instance;
	}


    /**
     * @return the number
     */
    public int getNumber() {
        return number;
    }


    /**
     * @param _number the number to set
     */
    public void setNumber(final int _number) {
        this.number = _number;
    }


    /**
     * @return the currentPO
     */
    public PaintObject getCurrentPO() {
        return currentPO;
    }


    /**
     * @param _currentPO the currentPO to set
     */
    public void setCurrentPO(final PaintObject _currentPO) {
        this.currentPO = _currentPO;
    }
}
