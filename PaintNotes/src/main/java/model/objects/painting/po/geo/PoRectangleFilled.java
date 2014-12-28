//package declaration
package model.objects.painting.po.geo;

//import declarations
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

import model.objects.painting.PaintBI;
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
public class PoRectangleFilled extends POInsertion {

	/**
     * serial version because the list of PaintObjects is saved.
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
	public PoRectangleFilled(final int _elementId, final Pen _pen) {
		
	    //call super constructor
	    super(_elementId, _pen);
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

        int cMinX = Math.min((int) getPnt_first().getX(),
                (int) getPnt_last().getX());
        int cMinY = Math.min((int) getPnt_first().getY(),
                (int) getPnt_last().getY());
        int cMaxX = Math.max((int) getPnt_first().getX(),
                (int) getPnt_last().getX());
        int cMaxY = Math.max((int) getPnt_first().getY(),
                (int) getPnt_last().getY());
        if (_final) {

            PaintBI.fillRectangleQuick(_bi, getPen().getClr_foreground(), 
                    new Rectangle(cMinX, cMinY, cMaxX - cMinX, cMaxY - cMinY));
        } else {
            

            //adjust the location at the zoom.
            cMinX = ((cMinX) * Status.getImageShowSize().width)
                    / Status.getImageSize().width;
            cMinY = ((cMinY) * Status.getImageShowSize().height)
                    / Status.getImageSize().height;
            cMaxX = ((cMaxX) * Status.getImageShowSize().width)
                    / Status.getImageSize().width;
            cMaxY = ((cMaxY) * Status.getImageShowSize().height)
                    / Status.getImageSize().height;

            //add the shift coordinates for painting.
            cMinX +=  _x;
            cMinY +=  _y;
            //add the shift coordinates for painting.
            cMaxX +=  _x;
            cMaxY +=  _y;
            
            

            PaintBI.fillRectangleQuick(_g, getPen().getClr_foreground(), 
                    new Rectangle(cMinX, cMinY, cMaxX - cMinX, cMaxY - cMinY));
        }
        
        getPen().paintLine(
                getPnt_first(), new DPoint(getPnt_last().getX(), 
                        getPnt_first().getY()), 
                _bi, _final, _g, new DPoint(_x, _y));
        getPen().paintLine(
                getPnt_first(), new DPoint(getPnt_first().getX(),
                        getPnt_last().getY()), 
                _bi, _final, _g, new DPoint(_x, _y));
        getPen().paintLine(
                getPnt_last(), new DPoint(getPnt_first().getX(), 
                        getPnt_last().getY()), 
                _bi, _final, _g, new DPoint(_x, _y));
        getPen().paintLine(
                getPnt_last(), new DPoint(getPnt_last().getX(),
                        getPnt_first().getY()), 
                _bi, _final, _g, new DPoint(_x, _y));
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

    /**
     * {@inheritDoc}
     */
    @Override
    public final PaintObject[][] separate(final byte[][] _r, final int _xShift, 
            final int _yShift) {

        new Exception(getClass() + " not implemenented yet")
        .printStackTrace();
        return null;
    }

}
