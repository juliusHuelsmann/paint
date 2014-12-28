//package declaration
package model.objects.painting.po;

//import declarations
import java.awt.Color;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

import model.objects.painting.PaintBI;
import model.objects.pen.Pen;
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
     * checks whether a certainDPoint is inside a given rectangle.
     * 
     * @param _r the rectangle of which is checked whether theDPoint is inside
     * @param _p theDPoint which is maybe inside the rectangle _r
     * 
     * @return whether theDPoint is inside the given rectangle.
     */
    protected static boolean isInSelectionPoint(
            final Rectangle _r, final DPoint _p) {
        
        //return whether theDPoint is inside given rectangle.
        return (_p.getX() >= _r.x && _p.getY() >= _r.y 
                && _p.getX() <= _r.x + _r.width 
                && _p.getY() <= _r.y + _r.height);
    }

    /**
     * checks whether a certainDPoint is inside a given rectangle.
     * 
     * @param _r the rectangle of which is checked whether theDPoint is inside
     * @param _p theDPoint which is maybe inside the rectangle _r
     * 
     * @param _shiftX the x shift
     * @param _shiftY the y shift
     * 
     * @return whether theDPoint is inside the given rectangle.
     */
    protected static boolean isInSelectionPoint(
            final byte[][] _r, final int _shiftX, final int _shiftY,

            final DPoint _p) {
        
        
        //TODO: spaeter angleichen an zoom und an page scroll. 

        int newX = (int) _p.getX() - _shiftX,
                newY = (int) _p.getY() - _shiftY;
        
        if (newX < 0 || newY < 0 
                || newX >= _r.length || newY >= _r[newX].length) {
            return false;
        }
//        PaintBI.drawPaintBI(_r);
        return (_r[newX][newY]  == PaintBI.FREE);
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
