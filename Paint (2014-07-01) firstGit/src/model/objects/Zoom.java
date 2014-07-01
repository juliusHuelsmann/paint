package model.objects;

import settings.ViewSettings;
import view.forms.Page;


/**
 * JLabel which shows where to zoom in.
 * 
 * @author Julius Huelsmann
 * @version %I%, %U%
 */
public final class Zoom {

    
    /**
     * The only instance of this class.
     */
    private static Zoom instance;
    
    /**
     * Initialize instance.
     */
    private Zoom() { }
    
    /**
     * Location of zoom.
     */
    private int x, y; 
    
    
    /**
     * return the x location.
     * @return the x location
     */
    public int getX() {
       return x; 
    }
    /**
     * 
     * return the y location.
     * @return the y location
     */
    public int getY() {
        return y;
    }
    /**
     * .
     * @param _x the x coordinate
     * @param _y the y coordinate.
     */
    public synchronized void setLocation(final int _x, final int _y) {
        
        Page.getInstance().getJlbl_painting().removeOldRectangle();
        
        this.x = _x;
        this.y = _y;
        
        Page.getInstance().getJlbl_painting().paintZoom(
                x, y, 
                ViewSettings.ZOOM_SIZE.width,
                ViewSettings.ZOOM_SIZE.height);
    }
    

    /**
     * this method guarantees that only one instance of this
     * class can be created ad runtime.
     * 
     * @return the only instance of this class.
     */
    public static Zoom getInstance() {
        
        if (instance == null) {
            instance = new Zoom();
        }
        return instance;
    }
}
