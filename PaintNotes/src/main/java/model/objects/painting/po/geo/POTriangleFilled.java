//package declaration
package model.objects.painting.po.geo;

//import declarations
import java.awt.Point;
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
public class POTriangleFilled extends POInsertion {

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
	public POTriangleFilled(final int _elementId, final Pen _pen, final Picture _pic) {
		
	    //call super constructor
	    super(_elementId, _pen,_pic);

	}


    /**
     * {@inheritDoc}
     */
    @Override 
    public final BufferedImage paint(final BufferedImage _bi, 
            final boolean _final, final BufferedImage _g, final int _x, 
            final int _y, final Rectangle _r) {
        

        if (getPnt_first() == null || getPnt_last() == null) {
            return _bi;
        }
        
        //hier rein und das unten machen.
        getPen().paintLine(
                getPnt_first(), getPnt_last(), _bi, _final, _g,
                new DPoint(_x, _y));
        
        
        getPen().paintLine(
                getPnt_first(), new DPoint(getPnt_first().getX(), 
                        getPnt_last().getY()),
                _bi, _final, _g, new DPoint(_x, _y));
        getPen().paintLine(
                getPnt_last(), new DPoint(getPnt_first().getX(), 
                        getPnt_last().getY()),
                _bi, _final, _g, new DPoint(_x, _y));
        
        Point finalSize = new Point(0, (int) getPnt_last().getY());
        paintLineIntegral(getPnt_first(), getPnt_last(), _bi, _final, _g, 
                new DPoint(_x, _y), finalSize);
        return _bi;
    }
    
    
    /**
     * print line between points _p1 and _p2 and to the BufferedImage _bi.
     * 
     * @param _p1 the fist point
     * @param _p2 the second point
     * @param _bi the buffered image which is extended
     * @param _final 
     *          if true:
     *              paint both to Graphics and BufferedImage
     *              
     *          if false:
     *              do only paint to given Graphics
     *              
     * @param _pnt_shift the point to shift.
     * @param _g the graphics to which line is painted.
     * @param _finalSize the size (point) to point to
     */
    public final void paintLineIntegral(final DPoint _p1, final DPoint _p2, 
            final BufferedImage _bi, final boolean _final, 
            final BufferedImage _g, final DPoint _pnt_shift, 
            final Point _finalSize) {

        //compute delta values
        int dX = (int) (_p1.getX() - _p2.getX());
        int dY = (int) (_p1.getY() - _p2.getY());

        //print the line between the two points
        for (int a = 0; a < Math.max(Math.abs(dX), Math.abs(dY)); a++) {
            int plusX = a * dX /  Math.max(Math.abs(dX), Math.abs(dY));
            int plusY = a * dY /  Math.max(Math.abs(dX), Math.abs(dY));
            
            if (_finalSize.x == 0) {

                getPen().paintLine(
                        new DPoint(_p1.getX() - plusX, 
                                _p1.getY() - plusY), 
                        new DPoint(_p1.getX() - plusX, 
                                _finalSize.y),
                        _bi, _final, _g, _pnt_shift);
            } else {
                getPen().paintLine(
                        new DPoint(_p1.getX() - plusX, 
                                _p1.getY() - plusY), 
                        new DPoint(_finalSize.y, 
                                _p1.getY() - plusY), 
                        _bi, _final, _g, _pnt_shift);
            }
        }
    }
    

    /**
     * {@inheritDoc}
     */
    @Override public final synchronized PaintObject[][] separate(
            final Rectangle _r) {
        Status.getLogger().severe("not impklemented yet");
        return null;
    }

}
