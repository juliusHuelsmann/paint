//package declaration
package model.objects.painting;

//import declarations
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.util.Random;

import settings.Status;
import view.forms.Page;
import model.objects.pen.Pen;
import model.objects.pen.normal.PenKuli;
import model.util.list.List;


/**
 * The paintObject corresponds to one item that has been painted. It consists
 * of a list of points which are added by the user by dragging the mouse and 
 * thus painting an entity to the screen.<br><br>
 * 
 * The paint object takes care of painting the image both to the screen and
 * to the not resized BufferedImage which will be saved as an image (e.g. as
 * .PNG file).
 * 
 * @author Julius Huelsmann
 * @version %U%,%I%
 */
public class PaintObjectWriting extends PaintObject {

	/**
     * serial version because the list of PaintObjects is saved.
     */
    private static final long serialVersionUID = -3730582547146097485L;

    /**
	 * List of points. They are combined and treated different depending on 
	 * pen and the kind of painting ("point", "normal", "math")
	 */
	private List<Point> ls_point;
	
	/**
	 * the pen with which PaintObject is painted. Has got its own paintPoint
	 * method which is called by the prepared Pen classes, a thickness and
	 * a color.
	 */
	private Pen pen;

	
	/**
	 * save minimal and maximal pixel in both x and y coordinate for being
	 * able to reduce the amount of pixel which have to be repainted if 
	 * there is a change in this paintObject and for being able to find
	 * out what PaintObjects are engaged in selection.<br><br>
	 * Initialized with MAX / MIN value because these values are only updated
	 * if a smaller / greater value in coordinate is added to this PaintObject.
	 * At the beginning, the first paint
	 */
	private int minX = Integer.MAX_VALUE, minY = Integer.MAX_VALUE, 
	        maxX = Integer.MIN_VALUE, maxY = Integer.MIN_VALUE;
	
	
	/**
	 * {@inheritDoc}
	 */
	@Override public final boolean isInSelectionImage(final Rectangle _r) {

        /*
         * check whether first point is in rectangle.
         */

        ls_point.toFirst();
	    Point pnt_previous = new Point(ls_point.getItem());
        if (isInSelectionPoint(_r, pnt_previous)) {
            return true;
        }
        

        /*
         * go through the list of points and check the lines between
         * the following items.
         */
        ls_point.next();
        while (!ls_point.isBehind()) {

            //if one part of the line is in rectangle return true
            if (pruefeLine(ls_point.getItem(), pnt_previous, _r)) {
                return true;
            }
            
            //otherwise save the current point as the previous point
            //for the next time the loop is passed.
            pnt_previous = new Point(ls_point.getItem());
            ls_point.next();
        }
        
        //if the item has not been found return false
        return false;
    }
    
    

	/**
	 * checks whether a certain point is inside a given rectangle.
	 * 
	 * @param _r the rectangle of which is checked whether the point is inside
	 * @param _p the point which is maybe inside the rectangle _r
	 * 
	 * @return whether the point is inside the given rectangle.
	 */
    private static boolean isInSelectionPoint(
            final Rectangle _r, final Point _p) {
        
        //return whether the point is inside given rectangle.
        return (_p.x >= _r.x && _p.y >= _r.y 
                && _p.x <= _r.x + _r.width && _p.y <= _r.y + _r.height);
    }

	/**
	 * Constructor creates new instance
	 * of list.
	 * 
	 * @param _elementId the id of the element.
	 * @param _pen the pen which is painted
	 */
	public PaintObjectWriting(final int _elementId, final Pen _pen) {
		
	    //call super constructor
	    super(_elementId);
	    
		//save values
		this.ls_point = new List<Point>();
		this.pen = new PenKuli(_pen.getId_operation(), _pen.getThickness(), _pen.getClr_foreground());
	}
	
	/**
	 * add a point to the list of points and update maximum and minimum 
	 * coordinates value.
	 * 
	 * @param _pnt the point.
	 */
	public final void addPoint(final Point _pnt) {

	    //update MIN values
		minX = Math.min(_pnt.x, minX);
		minY = Math.min(_pnt.y, minY);

		//update MAX values
		maxX = Math.max(_pnt.x, maxX);
		maxY = Math.max(_pnt.y, maxY);
	
		//insert at the end of the point list.
		ls_point.insertAtTheEnd(_pnt);
	}


    /**
     * {@inheritDoc}
     */
    @Override 
    public final BufferedImage paint(final BufferedImage _bi, 
            final boolean _final, final Graphics _g, final int _x, 
            final int _y) {

        
        if (_g != null) {
            _g.setColor(pen.getClr_foreground());
        }
        
        return pen.paintToImage(
                _bi, this, _final, new Point(_x, _y), _g);
    }
    
    
    /*
     * snapshot things, e.g. for selection.
     */


    /**
     * {@inheritDoc}
     */
    @Override 
	public final BufferedImage getSnapshot() {
		
		Rectangle rect = getSnapshotBounds();
		if (rect.width <= 0 || rect.height <= 0) {
			rect.width = 1;
			rect.height = 1;
		}
		if (rect.x <= 0 || rect.y <= 0) {
			rect.x = 0;
			rect.y = 0;
		}
		
	
		return paint(new BufferedImage(
		        Status.getImageSize().width, 
				Status.getImageSize().height, 
				BufferedImage.TYPE_INT_ARGB), 
				false, null, 
                Page.getInstance().getJpnl_toMove().getX(),
                Page.getInstance().getJpnl_toMove().getY()).getSubimage(
						rect.x, 
						rect.y,
						rect.width, 
						rect.height);
	}

    /**
     * {@inheritDoc}
     */
    @Override 
    public final Rectangle getSnapshotBounds() {
        if (minX == Integer.MAX_VALUE || minY == Integer.MAX_VALUE 
                || maxX == Integer.MIN_VALUE || maxY == Integer.MAX_VALUE) {
            return new Rectangle(0, 0, 1, 1);
        }
        Rectangle r =  new Rectangle(minX, minY, maxX - minX, maxY - minY);
        return r;
    }


    /**
     * {@inheritDoc}
     */
    @Override 
    public final Rectangle getSnapshotRectBounds() {
    	if (minX == Integer.MAX_VALUE || minY == Integer.MAX_VALUE) {
    		return new Rectangle(0, 0, 0, 0);
    	}
    	Rectangle r =  new Rectangle(minX, minY, Math.max(maxX - minX, 
    	        maxY - minY), Math.max(maxX - minX, maxY - minY));
    	return r;
    }
    
    
    
    
    /**
     * check whether line selected.
     * Has to work JUST LIKE the corresponding paint method paintLine!
     * No matter, if it is penMath...
     * 
     * @see Pen.paintLine(...)
     * 
     * @param _p1 the first  point
     * @param _p2 the second point
     * @param _r the rectangle
     * 
     * @return true or false
     */
    private boolean pruefeLine(final Point _p1, final Point _p2, 
            final Rectangle _r) {



        //compute delta values
        int dX = (_p1.x - _p2.x);
        int dY = (_p1.y - _p2.y);

        //print the line between the two points
        for (int a = 0; a < Math.max(Math.abs(dX), Math.abs(dY)); a++) {
            int plusX = a * dX /  Math.max(Math.abs(dX), Math.abs(dY));
            int plusY = a * dY /  Math.max(Math.abs(dX), Math.abs(dY));
            

            if (isInSelectionPoint(_r, 
                    new Point(_p1.x - plusX, _p1.y - plusY))) {
                return true;
            }
            
        }
        
        return false;
    }


    /*
     * getter methods.
     */
    
    
    /**
	 * getter method for pen. is used (e.g.) to
	 *     reset the Border starting index at Curve Selection Pen.
	 *     
	 * @return the pen
	 */
	public final Pen getPen() {
		return pen;
	}
	
	
	/**
	 * setter method for pen.
	 * @param _pen the pen.
	 */
	public final void setPen(final Pen _pen) {
	    this.pen = _pen;
	}
    
    /**
     * return list of points.
     * 
     * @return the list.
     */
    public final List<Point> getPoints() {
        return ls_point;
    }
    
    /**
     * Change the color of given pen.
     * @param _clr the color which is set.
     */
    public final void changeColor(final Color _clr) {
        pen.setClr_foreground(_clr);
    }



}
