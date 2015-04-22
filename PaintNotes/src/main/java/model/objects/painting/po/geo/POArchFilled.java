//package declaration
package model.objects.painting.po.geo;

//import declarations
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

import model.objects.painting.Picture;
import model.objects.painting.po.POInsertion;
import model.objects.painting.po.PaintObject;
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
public class POArchFilled extends POInsertion {

	/**
     * Default serial version UID for being able to identify the list's 
     * version if saved to the disk and check whether it is possible to 
     * load it or whether important features have been added so that the
     * saved file is out-dated.
     */
    private static final long serialVersionUID = -3730582547146097485L;

	
	/**
	 * {@inheritDoc}
	 */
	@Override public final boolean isInSelectionImage(final Rectangle _r) {

	    //check whether the PaintObject is completed
	    if (getPnt_first() == null || getPnt_last() == null) {
	        Status.getLogger().warning("Paint object line not ready");
	        return false;
	    }

        //check whether is in rectangle.
        if (isInSelectionPoint(_r, getPnt_first())
                || isInSelectionPoint(_r, getPnt_last())
                || pruefeLine(getPnt_last(), getPnt_first(), _r)) {
            return true;
        }
        
        //if the item has not been found return false
        return false;
    }
    

	/**
	 * Constructor creates new instance
	 * of list.
	 * 
	 * @param _elementId the id of the element.
	 * @param _pen the pen which is painted
	 */
	public POArchFilled(final int _elementId, final Pen _pen, 
			final Picture _pic) {
		
	    //call super constructor
	    super(_elementId, _pen, _pic);
	}
	


    /**
     * {@inheritDoc}
     */
    @Override 
    public final BufferedImage paint(final BufferedImage _bi, 
            final boolean _final, final BufferedImage _g, final int _x, 
            final int _y, final Rectangle _r) {
        
        getPen().setThickness(2);

        if (getPnt_first() == null || getPnt_last() == null) {
            return _bi;
        }
        final int lengthTotal = 15;
        DPoint pnt_vector = new DPoint(
                getPnt_last().getX() - getPnt_first().getX(), 
                getPnt_last().getY() - getPnt_first().getY());
        final double length = Math.sqrt(pnt_vector.getX() * pnt_vector.getX() 
                + pnt_vector.getY() * pnt_vector.getY());
        DPoint pnt_arch = new DPoint(
                getPnt_last().getX() 
                - pnt_vector.getX() * lengthTotal / length , 
                getPnt_last().getY() 
                - pnt_vector.getY() * lengthTotal / length);

        getPen().paintLine(
                getPnt_first(), getPnt_last(), _bi, 
                _final, _g, new DPoint(_x, _y));
        
        DPoint pnt_a1 = new DPoint(
                pnt_arch.getX() + pnt_vector.getY() * lengthTotal / length,
                pnt_arch.getY() - pnt_vector.getX() * lengthTotal / length);
        DPoint pnt_a2 = new DPoint(
                pnt_arch.getX() - pnt_vector.getY() * lengthTotal / length,
                pnt_arch.getY() + pnt_vector.getX() * lengthTotal / length);

        getPen().paintLine(
                pnt_a1, pnt_a2, _bi, _final, _g, new DPoint(_x, _y));
        
        getPen().paintLine(
                pnt_a1, getPnt_last(), _bi, _final, _g, new DPoint(_x, _y));
        getPen().paintLine(
                getPnt_last(), pnt_a2, _bi, _final, _g, new DPoint(_x, _y));
        

        final double lengthZwischen = 1.0 / 2;
        for (int i = 0; i < lengthTotal / lengthZwischen; i++) {

            DPoint pnt_a3 = new DPoint(
                    pnt_arch.getX() + pnt_vector.getY() 
                    * i * lengthZwischen / length,
                    pnt_arch.getY() - pnt_vector.getX() 
                    * i * lengthZwischen / length);
            DPoint pnt_a4 = new DPoint(
                    pnt_arch.getX() - pnt_vector.getY() 
                    * i * lengthZwischen / length,
                    pnt_arch.getY() + pnt_vector.getX() 
                    * i * lengthZwischen / length);
            getPen().paintLine(
                    pnt_a3, getPnt_last(), _bi, _final, _g, new DPoint(_x, _y));
            getPen().paintLine(
                    getPnt_last(), pnt_a4, _bi, _final, _g, new DPoint(_x, _y));
        }

        return _bi;
    }
    
    
    /*
     * snapshot things, e.g. for selection.
     */


    
    
    /**
     * {@inheritDoc}
     */
    @Override public final synchronized PaintObject[][] separate(
            final Rectangle _r) {
        Status.getLogger().severe("not impklemented yet");
        return null;
    }
    
}
