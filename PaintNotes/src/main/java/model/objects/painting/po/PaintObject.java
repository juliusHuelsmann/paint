//package declaration
package model.objects.painting.po;

//import declarations
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.Serializable;

import model.util.DPoint;


/**
 * The paintObject corresponds to one item that has been painted. It consists
 * of a list of points which are added by the user by dragging the mouse and 
 * thus painting an entity to the screen.<br><br>
 * 
 * The paint object takes care of painting the image both to the screen and
 * to the not resized BufferedImage which will be saved as an image (e.g. as
 * .PNG file).
 * 
 * Implemented by PaintObjectImage and PaintObjectWriting.
 * 
 * @author Julius Huelsmann
 * @version %U%,%I%
 */
public abstract class PaintObject implements Serializable {

	/**
     * serial version because the list of PaintObjects is saved.
     */
    private static final long serialVersionUID = -3730582547146097485L;

	/**
	 * identifier of the current PaintObject. Thus each paint object can be 
	 * identified (e.g. if it is deleted, changed or something).
	 */
	private int elementId;

    
    /**
     * This method checks whether this paintObject is inside the given 
     * selection rectangle _r. It checks every point in list of points
     * 
     * @param _r the rectangle.
     * @return whether is in rectangle.
     */
    public abstract boolean isInSelectionImage(final Rectangle _r);

    
    /**
     * This method checks whether this paintObject is inside the given 
     * selection rectangle _r. It checks every point in list of points
     * 
     * @param _field the field .
     * @param _xShift the shift of the field in x direction.
     * @param _yShift the shift of the field in y direction.
     * @return whether is in rectangle.
     */
    public abstract boolean isInSelectionImage(final byte[][] _field, 
            final int _xShift, final int _yShift);
    
	
	/**
	 * Stretch the points of entire paintObject and recalculate bounds on the
	 * fly.
	 * 
	 * @param _pnt_from The Point from where to stretch
	 * @param _pnt_totalStretch the total delta stretch
	 * @param _pnt_size the new size of the rectangle which is indicating
	 * the new size of the stretched PaintObject.
	 */
	public abstract void stretch(final DPoint _pnt_from, 
	        final DPoint _pnt_totalStretch,
            final DPoint _pnt_size);
    

	/**
	 * Constructor creates new instance
	 * of list.
	 * 
	 * @param _elementId the id of the element.
	 */
	public PaintObject(final int _elementId) {
		
		//save values
		this.elementId = _elementId;
	}

	
	/**
	 * paint the current list of points to the BufferedImage and to the 
	 * graphics.
	 * 
	 * At the graphics only those points are painted, which belong to 
	 * a paint object which is in the given rectangle (_x, _y, _width + _x,
	 * _height + _y). They are painted shifted by the point _p_shift.
	 *
     * Painting at the graphics is shifted by (-_x, -_y).
     * 
	 * @param _bi the bufferedImage to which is printed.
	 * 
	 * @param _final if _final is true, paint to the Graphics and directly
	 *         at the bufferedImage. If it is false, only paint to the graphics
	 *         of a temporary image which is repainted after each painting.
	 * 
	 * @param _g the Graphics at which is painted if the boolean _final is 
	 *         true
	 * 
     * @param _x the x position where the rectangle which is to be painted to
     *         the JLabel starts
     *         painting at the graphics is shifted by (-_x, -_y)
	 * 
	 * @param _y the y position where the rectangle which is to be painted to
	 *         the JLabel starts
	 *         painting at the graphics is shifted by (-_x, -_y)
	 * 
	 *         
	 * @return the new BufferedImage.
	 */
    public abstract BufferedImage paint(final BufferedImage _bi, 
            final boolean _final, final BufferedImage _g, final int _x, 
            final int _y);
    
    
    /*
     * snapshot things, e.g. for selection.
     */

	
    /**
     * returns a snapshot bufferedImage of this PaintObject.
     * @return snapshot bufferedImage.
     */
	public abstract BufferedImage getSnapshot();
	
	/**
     * return the bounds of the rectangle of the great image for this 
     * paintObject snapshot.
     * 
     * @return the rectangle.
     */
    public abstract Rectangle getSnapshotBounds();


    /**
     * return the bounds of the smallest square rectangle which contains the 
     * whole PaintObject.
     * 
     * @return the bounds of the rectangle.
     */
    public abstract Rectangle getSnapshotSquareBounds();

    
    /**
     * Fully recalculate the snapshot bounds.
     */
    public abstract void recalculateSnapshotBounds();


    /**
     * Separates the PaintObject; thus there are parts that are inside the
     * given rectangle and ones that are outside.
     * @param _r the rectangle
     * @return the PaintObject array [0][x] outside, [1] [x] inside.s
     */
    public abstract PaintObject[][] separate(Rectangle _r);
    
    /**
     * Separates the PaintObject; thus there are parts that are inside the
     * given rectangle and ones that are outside.
     * @param _r the rectangle
     * @return the PaintObject array [0][x] outside, [1] [x] inside.s
     */
    public abstract PaintObject[][] separate(byte[][] _r, int _xShift, 
            int _yShift);
  
    /*
     * getter methods.
     */
    
    /**
     * getter.
     * @return the elementId
     */
    public final int getElementId() {
        return elementId;
    }
    
}
