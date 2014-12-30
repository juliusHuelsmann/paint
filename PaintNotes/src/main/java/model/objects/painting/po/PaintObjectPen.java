//package declaration
package model.objects.painting.po;

//import declarations
import java.awt.Color;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

import model.objects.painting.PaintBI;
import model.objects.pen.Pen;
import model.settings.Status;
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
public abstract class PaintObjectPen extends PaintObject {

	/**
     * serial version because the list of PaintObjects is saved.
     */
    private static final long serialVersionUID = -3730582547146097485L;

	
	/**
	 * the pen with which PaintObject is painted. Has got its own paintPoint
	 * method which is called by the prepared Pen classes, a thickness and
	 * a color.
	 */
	private Pen pen;

	
	/**
	 * {@inheritDoc}
	 */
	@Override public abstract boolean isInSelectionImage(final Rectangle _r);
    


    /**
     * Checks whether a certain DPoint is inside a given selection.
     * If the given parameters are not valid, return that the point is not 
     * inside the selection and log an error.
     * 
     * The given coordinates in each given variable have to be model values
     * meaning that they are already stretched if the user has enabled 
     * zooming and shifted by the display's location.
     * 
     * 
     * @param _r the rectangle of which is checked whether theDPoint is inside
     * @param _p theDPoint which is maybe inside the rectangle _r
     * 
     * @return whether theDPoint is inside the given rectangle.
     */
    protected static boolean isInSelectionPoint(
            final Rectangle _r, final DPoint _p) {
        
    	//if the values given to the function are not valid return false
    	//and print an error message
    	if (_p == null || _r == null) {
    		Status.getLogger().severe("Checking invalid point or rectanlge:"
    				+ " Point: " + _p + "\tRectangle: " + _r);
    		return false;
    	}
    	
    	
    	//The parameters are valid (meaning not equal to null)
    	
    	
    	//return whether theDPoint is inside given rectangle.
        return (_p.getX() >= _r.x && _p.getY() >= _r.y 
                && _p.getX() <= _r.x + _r.width 
                && _p.getY() <= _r.y + _r.height);
    }

    /**
     * Checks whether a certain DPoint is inside a given selection.
     * If the given parameters are not valid, return that the point is not 
     * inside the selection and log an error.
     * 
     * The given coordinates in each given variable have to be model values
     * meaning that they are already stretched if the user has enabled 
     * zooming and shifted by the display's location.
     * 
     * 
     * @param _r 	the rectangle consisting of booleans which indicate 
     * 				whether the (shifted) pixel _r [x][y] is selected
     * 				or not
     * 
     * @param _pnt_shiftRectangle 	
     * 				the shifting of the rectangle in x and y direction (or in 
     * 				other words the location of the element _r [0][0] on screen
     * 
     * @param _p 	
     * 				theDPoint which is maybe inside the rectangle _r
     * 
     * @return whether theDPoint is inside the given rectangle.
     */
    protected static boolean isInSelectionPoint(
            final byte[][] _r, 
    		final Point _pnt_shiftRectangle,
            final DPoint _p) {

    	//if the values given to the function are not valid return false
    	//and print an error message
    	if (_p == null || _r == null) {
    		Status.getLogger().severe("Checking invalid point or area:"
    				+ " Point: " + _p + "\tArea: " + _r);
    		return false;
    	}

    	
    	//Here the parameters are valid (meaning not equal to null)

    	//shift the points because the boolean - rectangle is shifted, too
    	int shiftedPointX = (int) _p.getX() - _pnt_shiftRectangle.x;
    	int shiftedPointY = (int) _p.getY() - _pnt_shiftRectangle.y;
    	
    	//return whether theDPoint is inside given rectangle.
    	//if the selection is outside the scope of the rectangle
        if (shiftedPointX < 0 || shiftedPointY < 0 
        		|| shiftedPointX >= _r.length 
        		|| shiftedPointY >= _r[(int) shiftedPointX].length) {
            return false;
        }
        
        
        //Here the coordinates are valid (inside the given array)
        
        //return whether the d point is inside given rectangle
        return (_r[(int) shiftedPointX][(int) shiftedPointY]  == PaintBI.FREE);
    }

	/**
	 * Constructor creates new instance
	 * of list.
	 * 
	 * @param _elementId the id of the element.
	 * @param _pen the pen which is painted
	 */
	public PaintObjectPen(final int _elementId, final Pen _pen) {
		
	    //call super constructor
	    super(_elementId);
		//save values
		
	    pen = Pen.clonePen(_pen);
	}
	
	/**
	 * add aDPoint to the list ofDPoints and update maximum and minimum 
	 * coordinates value.
	 * 
	 * @param _pnt theDPoint.
	 */
	public abstract void addPoint(final DPoint _pnt);

    
    
    /*
     * snapshot things, e.g. for selection.
     */


    /**
     * {@inheritDoc}
     */
    @Override 
	public abstract BufferedImage getSnapshot();

    /**
     * {@inheritDoc}
     */
    @Override 
    public abstract Rectangle getSnapshotBounds();


    /**
     * {@inheritDoc}
     */
    @Override 
    public abstract Rectangle getSnapshotSquareBounds();
    
    
    /**
     * After moving operation re adjust snapshot bounds.
     * @param _dx the delta x coordinate added to x
     * @param _dy the delta y coordinate added to y
     */
    public abstract void adjustSnapshotBounds(final int _dx, final int _dy);
    
    

    /**
     * 
     * {@inheritDoc}
     */
    @Override public abstract void recalculateSnapshotBounds();
    
    
    /**
     * {@inheritDoc}
     */
    @Override public abstract PaintObject[][] separate(
            final Rectangle _r);
    

    
    /**
     * {@inheritDoc}
     */
    @Override public abstract void stretch(
            final DPoint _pnt_from, final DPoint _pnt_totalStretch,
            final DPoint _pnt_size);


    /**
     * Tell the pen to paint this PaintObject as a selected PaintObject.
     */
    public final void enableSelected() {
        pen.setSelected(true);
    }
    
    /**
     * Tell the pen not to paint this PaintObject as a selected PaintObject
     * anymore.
     */
    public final void disableSelected() {
        pen.setSelected(false);
    }
    /*
     * getter methods.
     */

    /**
     * setter method for pen.
     * @param _pen the pen.
     */
    public final void setPen(final Pen _pen) {
        this.pen = _pen;
    }
    
    /**
     * Change the color of given pen.
     * @param _clr the color which is set.
     */
    public final void changeColor(final Color _clr) {
        pen.setClr_foreground(_clr);
    }
    
    /**
	 * getter method for pen. is used (e.g.) to
	 *     reset the Border starting index at Curve Selection Pen.
	 *     
	 * @return the pen
	 */
	public final Pen getPen() {
		return pen;
	}

}
