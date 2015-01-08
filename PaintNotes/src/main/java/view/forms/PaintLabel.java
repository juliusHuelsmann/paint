package view.forms;

//import declarations
import java.awt.Point;
import java.awt.Rectangle;

//
import control.interfaces.MoveEvent;
import control.interfaces.PaintListener;

//
import view.util.mega.MLabel;
import view.util.mega.MPanel;
import model.settings.Status;


/**
 * 
 * @author Julius Huelsmann
 * @version %I%, %U%
 */
@SuppressWarnings("serial")
public class PaintLabel extends MLabel {
    
    /**
     * The location which can be changed and given.
     */
    private int x = 0, y = 0;    

	/**
	 * The menuListener provides an interface for listening for set of location
	 * and size events.
	 */
    private PaintListener paintListener;

    /**
     * JPanel which is to be updated if location changes in
     * PaintLabel.
     */
    private MPanel jpnl_toMove;
    
    /**
     * Constructor.
     * 
     * @param _jpnl_toMove JPanel which is to be updated if location changes in
     * PaintLabel.
     */
    public PaintLabel(final MPanel _jpnl_toMove) {
        this.jpnl_toMove = _jpnl_toMove;
    }

    /**
     * Set the PaintListener which provides an interface for  listening for set 
     * of location and size events outside this utility class.
     * 
     * It is only possible to set one MenuListener at a time.
     * 
     * @param _pl the PaintListener
     */
    public final void setPaintListener(final PaintListener _pl) {
    	this.paintListener = _pl;
    }
    
    
    /*
     * Location is not set directly because the paint methods shell be able 
     * to decide for themselves what to paint at which position.
     * 
     * Thus, the methods for changing the location and for getting it are
     * overwritten and alter / return the locally saved location.
     * 
     * methods for changing location:
     */
    

    /**
     * This method saves the location in x and y coordinate for being able
     * to display the correct painting sub image.
     * 
     * for x and y location are saved. Thus, the painting methods
     * are able to calculate for themselves what to paint at what position.
     * @param _x the new x coordinate which is saved
     * @param _y the new y coordinate which is saved
     */
    @Override public final synchronized void setLocation(
    		final int _x, final int _y) {
        
    	//TODO: error-checking (formally used for zooming in, did not work
    	//because method is not designed for that.)
        //update the JPanel location because the ScrollPane fetches information
        //out of that panel
        jpnl_toMove.setBounds(_x, _y, jpnl_toMove.getWidth(), 
                jpnl_toMove.getHeight());
        
        //Forward the set location event to the instance of paintListener
        //if it has been set.
        if (paintListener != null) {
        	paintListener.onLocationChange(new MoveEvent(new Point(_x, _y)));
        } else {
        	Status.getLogger().severe("PaintListener not set.");
        }
    }
    

    /**
     * Really set the location.
     * 
     * E.g. called by controlling class in case of zooming in.
     * 
     * @param _x the new x coordinate which is saved
     * @param _y the new y coordinate which is saved
     */
    public final void setLoc(final int _x, final int _y) {
        
        //if something changed, repaint
        super.setLocation(_x, _y);
    }
    

    /**
     * in here, the location is not set as usual, but just the values
     * for x and y location are saved. Thus, the painting methods
     * are able to calculate for themselves what to paint at what position.
     * @param _p the new coordinates which are saved
     */
    @Override public final void setLocation(final Point _p) {
        
        //save the new location
        this.x = _p.x;
        this.y = _p.y;

        //update the JPanel location because the ScrollPane fetches information
        //out of that panel
        jpnl_toMove.setBounds(x, y, jpnl_toMove.getWidth(), 
                jpnl_toMove.getHeight());


        //Forward the set location event to the instance of paintListener
        //if it has been set.
        if (paintListener != null) {
        	paintListener.onExternalLocationChange(new MoveEvent(_p));
        } else {
        	Status.getLogger().severe("PaintListener not set.");
        }
    }
    
    
    /**
     * set the size of the JLabel and save the new location. Location is not 
     * set because the paint methods shell be able to decide for themselves
     * what to paint at which position.
     * 
     * @param _x the x coordinate which is saved
     * @param _y the y coordinate which is saved
     * @param _widht the width which is set
     * @param _height the height which is set
     */
    @Override public final void setBounds(final int _x, final int _y, 
            final int _widht, final int _height) {
        
        //save the new location 
        this.x = _x;
        this.y = _y;

        //update the JPanel location because the ScrollPane fetches information
        //out of that panel
        jpnl_toMove.setBounds(_x, _y, jpnl_toMove.getWidth(), 
                jpnl_toMove.getHeight());
        
        //set width and height.
        super.setBounds(0, 0, _widht, _height);
        

        //Forward the set location event to the instance of paintListener
        //if it has been set.
        if (paintListener != null) {
        	paintListener.onExternalLocationChange(new MoveEvent(
        			new Point(_x, _y)));
        	paintListener.onExternalSizeChange(new MoveEvent(
        			new Point(_widht, _height)));
        } else {
        	Status.getLogger().severe("PaintListener not set.");
        }
    }

    
    
    
    /*
     * methods for getting location
     */

    
    /**
     * @return the real coordinate!
     */
    @Deprecated
    @Override public final int getX() {
        return super.getX();
    }

    /**
     * @return the real coordinate!
     */
    @Deprecated
    @Override public final int getY() {
        return super.getY();
    }
    
    /**
     * returns the saved but not applied x and y coordinates.
     * @return the saved but not applied x and y coordinates (point).
     */
    @Override public final Point getLocation() {
        return new Point(x, y);
    }
    
    /**
     * returns the saved but not applied x and y coordinates together with the
     * applied size in a rectangle. 
     * @return the saved but not applied x and y coordinates together with the
     * applied size in a rectangle. 
     */
    @Override public final Rectangle getBounds() {
        return new Rectangle(x, y, getWidth(), getHeight());
    }
}
