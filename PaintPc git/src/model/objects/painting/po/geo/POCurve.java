//package declaration
package model.objects.painting.po.geo;

//import declarations
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import view.forms.Page;
import model.objects.painting.po.PaintObject;
import model.objects.painting.po.PaintObjectPen;
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
public class POCurve extends PaintObjectPen {

	/**
     * serial version because the list of PaintObjects is saved.
     */
    private static final long serialVersionUID = -3730582547146097485L;

    /**
	 * List ofDPoints. They are combined and treated different depending on 
	 * pen and the kind of painting ("point", "normal", "math")
	 */
	private DPoint pnt_first, pnt_last;

	
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

	    //check whether the PaintObject is completed
	    if (pnt_first == null || pnt_last == null) {
	        Status.getLogger().warning("Paint object line not ready");
	        return false;
	    }

        //check whether is in rectangle.
        if (isInSelectionPoint(_r, pnt_first)
                || isInSelectionPoint(_r, pnt_last)
                || pruefeLine(pnt_last, pnt_first, _r)) {
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
	public POCurve(final int _elementId, final Pen _pen) {
		
	    //call super constructor
	    super(_elementId, _pen);

//        if (_pen instanceof BallPen) {
//
//            pen = new Pencil(_pen.getId_operation(),
//                    _pen.getThickness(), _pen.getClr_foreground());
//        } else if (_pen instanceof PenSelection) {
//
//            pen = new PenSelection();
//        } else if (_pen instanceof Pencil) {
//
//            pen = new Pencil(_pen.getId_operation(),
//                    _pen.getThickness(), _pen.getClr_foreground());
//        } else {
//            
//            //alert user.
//            JOptionPane.showMessageDialog(View.getInstance(), 
//                    "PROGRAMMIERFEHLER @ paintobjectwriting: " 
//                    + "Stift noch nicht hinzugefuegt.");
//            
//            
//            //throw exception
//            throw new Error("Fehler: stift noch nicht hinzugefuegt.");
//        }
	}
	
	/**
	 * add aDPoint to the list ofDPoints and update maximum and minimum 
	 * coordinates value.
	 * 
	 * @param _pnt theDPoint.
	 */
	public final void addPoint(final DPoint _pnt) {
	    
	    //save values
        if (pnt_first == null) {
            pnt_first = new DPoint(_pnt);
        } else {
            pnt_last = new DPoint(_pnt);
        }
	   
	    
	    //update MIN values
		minX = (int) Math.min(_pnt.getX(), pnt_first.getX());
		minY = (int) Math.min(_pnt.getY(), pnt_first.getY());

		//update MAX values
		maxX = (int) Math.max(_pnt.getX(), pnt_first.getX());
		maxY = (int) Math.max(_pnt.getY(), pnt_first.getY());

		final int lineThickness = 4;
        minX -= lineThickness;
        minY -= lineThickness;
        maxX += lineThickness;
        maxY += lineThickness;
	}


    /**
     * {@inheritDoc}
     */
    @Override 
    public final BufferedImage paint(final BufferedImage _bi, 
            final boolean _final, final BufferedImage _g, final int _x, 
            final int _y) {
        

        if (pnt_first == null || pnt_last == null) {
            return _bi;
        }
        getPen().paintLine(
                pnt_first, pnt_last, _bi, _final, _g, new DPoint(_x, _y));
        return _bi;
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
                Page.getInstance().getJlbl_painting().getLocation().x,
                Page.getInstance().getJlbl_painting().getLocation().y)
                .getSubimage(
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
    public final Rectangle getSnapshotSquareBounds() {
    	if (minX == Integer.MAX_VALUE || minY == Integer.MAX_VALUE) {
    		return new Rectangle(0, 0, 0, 0);
    	}
    	Rectangle r =  new Rectangle(minX, minY, Math.max(maxX - minX, 
    	        maxY - minY), Math.max(maxX - minX, maxY - minY));
    	return r;
    }
    
    
    /**
     * After moving operation re adjust snapshot bounds.
     * @param _dx the delta x coordinate added to x
     * @param _dy the delta y coordinate added to y
     */
    public final void adjustSnapshotBounds(final int _dx, final int _dy) {
        minX += _dx;
        minY += _dy;
        
        maxX += _dx;
        maxY += _dy;
    }
    
    
    
    /**
     * check whether line selected.
     * Has to work JUST LIKE the corresponding paint method paintLine!
     * No matter, if it is penMath...
     * 
     * @see Pen.paintLine(...)
     * 
     * @param _p1 the first DPoint
     * @param _p2 the secondDPoint
     * @param _r the rectangle
     * 
     * @return true or false
     */
    private boolean pruefeLine(final DPoint _p1, final DPoint _p2, 
            final Rectangle _r) {

        //compute delta values
        int dX = (int) (_p1.getX() - _p2.getX());
        int dY = (int) (_p1.getY() - _p2.getY());

        //print the line between the twoDPoints
        for (int a = 0; a < Math.max(Math.abs(dX), Math.abs(dY)); a++) {
            int plusX = a * dX /  Math.max(Math.abs(dX), Math.abs(dY));
            int plusY = a * dY /  Math.max(Math.abs(dX), Math.abs(dY));
            

            if (isInSelectionPoint(_r, 
                    new DPoint(_p1.getX() - plusX, _p1.getY() - plusY))) {
                return true;
            }
            
        }
        
        return false;
    }

    /**
     * 
     * {@inheritDoc}
     */
    @Override public final synchronized void recalculateSnapshotBounds() {

        //update MIN values
        minX = (int) Math.min(pnt_last.getX(), pnt_first.getX());
        minY = (int) Math.min(pnt_last.getY(), pnt_first.getY());

        //update MAX values
        maxX = (int) Math.max(pnt_last.getX(), pnt_first.getX());
        maxY = (int) Math.max(pnt_last.getY(), pnt_first.getY());
    }
    
    
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
    @Override public final synchronized void stretch(
            final DPoint _pnt_from, final DPoint _pnt_totalStretch,
            final DPoint _pnt_size) {

        if (pnt_first == null || pnt_last == null) {
            return;
        }

        minX = Integer.MAX_VALUE; 
        minY = Integer.MAX_VALUE; 
        maxX = Integer.MIN_VALUE; 
        maxY = Integer.MIN_VALUE;
        
            
            DPoint pnt_vector = new DPoint(
                    pnt_first.getX() - _pnt_from.getX(),
                    pnt_first.getY() - _pnt_from.getY());

            double dX = _pnt_from.getX() 
                    + pnt_vector.getX() * _pnt_size.getX() 
                            / (_pnt_size.getX() + _pnt_totalStretch.getX());
            double dY = _pnt_from.getY()               
                    +    pnt_vector.getY() * _pnt_size.getY() 
                    / (_pnt_size.getY() + _pnt_totalStretch.getY());
                
            pnt_vector.setX(dX);
            pnt_vector.setY(dY);
            
            pnt_first = new DPoint(pnt_vector);

            pnt_vector = new DPoint(
                    pnt_last.getX() - _pnt_from.getX(),
                    pnt_last.getY() - _pnt_from.getY());

            dX = _pnt_from.getX() 
                    + pnt_vector.getX() * _pnt_size.getX() 
                            / (_pnt_size.getX() + _pnt_totalStretch.getX());
            dY = _pnt_from.getY()               
                    +    pnt_vector.getY() * _pnt_size.getY() 
                    / (_pnt_size.getY() + _pnt_totalStretch.getY());
                
            pnt_vector.setX(dX);
            pnt_vector.setY(dY);
            
            pnt_last = new DPoint(pnt_vector);
    }




    /**
     * @return the pnt_first
     */
    public final DPoint getPnt_first() {
        return pnt_first;
    }



    /**
     * @return the pnt_last
     */
    public final DPoint getPnt_last() {
        return pnt_last;
    }
}
