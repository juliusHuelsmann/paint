//package declaration
package model.objects.painting.po.geo;

//import declarations
import java.awt.Rectangle;

import model.objects.painting.po.PaintObjectWriting;
import model.objects.pen.Pen;
import model.settings.Constants;
import model.util.DPoint;

/**
 * The paintObject corresponds to one item that has been painted. It consists
 * of a list ofDPoints which are added by the user by dragging the mouse and 
 * thus painting an entity to the screen.<br><br>
 * 
 * The paint object takes care of painting the image both to the screen and
 * to the not resized BufferedImage which will be saved as an image (e.g. as
 * .PNG file).
 * 
 * @author Julius Huelsmann
 * @version %U%,%I%
 */
public class POCurve extends PaintObjectWriting {

	/**
     * serial version because the list of PaintObjects is saved.
     */
    private static final long serialVersionUID = -3730582547146097485L;

    
    
    /**
     * Counts the amount of points.
     */
    private boolean ready;
	

	/**
	 * Constructor creates new instance
	 * of list.
	 * 
	 * @param _elementId the id of the element.
	 * @param _pen the pen which is painted
	 */
	public POCurve(final int _elementId, final Pen _pen) {
		
	    //call super constructor
	    super(Constants.PEN_ID_MATHS, _pen);
	    this.ready = true;
	}
	

	/**
	 * Set the PaintObject ready to receive a new point.
	 */
	public final void setReady() {
	    this.ready = true;
	}
    

	/**
	 * {@inheritDoc}
	 */
    @Override
    public final void addPoint(final DPoint _pnt) {

        if (ready) {
            
            boolean empty = getPoints().isEmpty();
            
            if (!empty) {
                
                final int tolerance = 6;
                final Rectangle r = new Rectangle(
                        (int) (_pnt.getX() - tolerance / 2),
                        (int) (_pnt.getY() - tolerance / 2), 
                        tolerance, tolerance);
                getPoints().toFirst();
                DPoint pnt_previous = getPoints().getItem();
                getPoints().next();
                boolean found = false;
                
                while (!found && !getPoints().isEmpty() 
                        && !getPoints().isBehind()) {
                    found = pruefeLine(pnt_previous, getPoints().getItem(), r);
                
                    if (!found) {

                        getPoints().next();
                    }
                }
                getPoints().previous();

                
                
                //find the point next to
            } 

            //update MIN values
            setMinX((int) Math.min(_pnt.getX(), getMinX()));
            setMinY((int) Math.min(_pnt.getY(), getMinY()));

            //update MAX values
            setMaxX((int) Math.max(_pnt.getX(), getMaxX()));
            setMaxY((int) Math.max(_pnt.getY(), getMaxY()));
        
            getPoints().insertBehind(_pnt);
            
            ready = empty;
        } else {
            getPoints().replace(_pnt);
        }
    }

}
