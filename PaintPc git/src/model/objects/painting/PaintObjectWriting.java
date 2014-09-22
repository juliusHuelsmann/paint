//package declaration
package model.objects.painting;

//import declarations
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

import javax.swing.JOptionPane;

import settings.Status;
import view.View;
import view.forms.Page;
import model.objects.pen.Pen;
import model.objects.pen.normal.PenKuli;
import model.objects.pen.special.PenSelection;
import model.util.DPoint;
import model.util.list.List;
import model.util.solveLGS.Matrix;

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
public class PaintObjectWriting extends PaintObject {

	/**
     * serial version because the list of PaintObjects is saved.
     */
    private static final long serialVersionUID = -3730582547146097485L;

    /**
	 * List ofDPoints. They are combined and treated different depending on 
	 * pen and the kind of painting ("point", "normal", "math")
	 */
	private List<DPoint> ls_point;
	
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
         * check whether firstDPoint is in rectangle.
         */

        ls_point.toFirst();
        DPoint pnt_previous = new DPoint(ls_point.getItem());
        if (isInSelectionPoint(_r, pnt_previous)) {
            return true;
        }
        

        /*
         * go through the list ofDPoints and check the lines between
         * the following items.
         */
        ls_point.next();
        while (!ls_point.isBehind()) {

            //if one part of the line is in rectangle return true
            if (pruefeLine(ls_point.getItem(), pnt_previous, _r)) {
                return true;
            }
            
            //otherwise save the currentDPoint as the previousDPoint
            //for the next time the loop is passed.
            pnt_previous = new DPoint(ls_point.getItem());
            ls_point.next();
        }
        
        //if the item has not been found return false
        return false;
    }
    
    

	/**
	 * checks whether a certainDPoint is inside a given rectangle.
	 * 
	 * @param _r the rectangle of which is checked whether theDPoint is inside
	 * @param _p theDPoint which is maybe inside the rectangle _r
	 * 
	 * @return whether theDPoint is inside the given rectangle.
	 */
    private static boolean isInSelectionPoint(
            final Rectangle _r, finalDPoint _p) {
        
        //return whether theDPoint is inside given rectangle.
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
		
		if (_pen instanceof PenKuli) {
            this.pen = new PenKuli(_pen.getId_operation(),
//            this.pen = new PenKuli(Constants.PEN_ID_POINT,
                    _pen.getThickness(), _pen.getClr_foreground());
		} else if (_pen instanceof PenSelection) {

            this.pen = new PenSelection();
		} else {
		    
		    //alert user.
		    JOptionPane.showMessageDialog(View.getInstance(), 
		            "PROGRAMMIERFEHLER @ paintobjectwriting: " 
		            + "Stift noch nicht hinzugefuegt.");
		    
		    
		    //throw exception
		    throw new Error("Fehler: stift noch nicht hinzugefuegt.");
		}
	}
	
	/**
	 * add aDPoint to the list ofDPoints and update maximum and minimum 
	 * coordinates value.
	 * 
	 * @param _pnt theDPoint.
	 */
	public final void addPoint(final DPoint _pnt) {

	    //update MIN values
		minX = (int) Math.min(_pnt.x, minX);
		minY = (int) Math.min(_pnt.y, minY);

		//update MAX values
		maxX = (int) Math.max(_pnt.x, maxX);
		maxY = (int) Math.max(_pnt.y, maxY);
	
		//insert at the end of theDPoint list.
		ls_point.insertAtTheEnd(_pnt);
	}


    /**
     * {@inheritDoc}
     */
    @Override 
    public final BufferedImage paint(final BufferedImage _bi, 
            final boolean _final, final BufferedImage _g, final int _x, 
            final int _y) {

        return pen.paintToImage(
                _bi, this, _final, new DPoint(_x, _y), _g);
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
    private boolean pruefeLine(finalDPoint _p1, finalDPoint _p2, 
            final Rectangle _r) {

        //compute delta values
        int dX = (_p1.x - _p2.x);
        int dY = (_p1.y - _p2.y);

        //print the line between the twoDPoints
        for (int a = 0; a < Math.max(Math.abs(dX), Math.abs(dY)); a++) {
            int plusX = a * dX /  Math.max(Math.abs(dX), Math.abs(dY));
            int plusY = a * dY /  Math.max(Math.abs(dX), Math.abs(dY));
            

            if (isInSelectionPoint(_r, 
                    new DPoint(_p1.x - plusX, _p1.y - plusY))) {
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
        
        //if the list is not empty
        if (!ls_point.isEmpty()) {
            
            //go to the beginning of the list
            ls_point.toFirst();

            //reset values.
            minX = Integer.MAX_VALUE;
            minY = Integer.MAX_VALUE;
            maxX = Integer.MIN_VALUE;
            maxY = Integer.MIN_VALUE;
            
            //go through the list
            while (!ls_point.isBehind()) {

                //update MIN values
                minX = Math.min(ls_point.getItem().x, minX);
                minY = Math.min(ls_point.getItem().y, minY);

                //update MAX values
                maxX = Math.max(ls_point.getItem().x, maxX);
                maxY = Math.max(ls_point.getItem().y, maxY);
                
                //proceed one step
                ls_point.next();
            }
        }
    }
    
    
    /**
     * {@inheritDoc}
     */
    @Override public final PaintObject[][] separate(final Rectangle _r) {

        //TODO: kann sein dass es pen probleme gibt wenn der pen eines
        //separierten PaintObjects geaendert wurde.
        List <PaintObjectWriting>   
            ls_pow_outside = new List<PaintObjectWriting>(), 
            ls_pow_inside = new List<PaintObjectWriting>();
        
        //The last element that was inside.
        boolean lInside;
        ls_point.toFirst();

        //initialize the lInside Element
       DPoint pc = ls_point.getItem();
        lInside = (pc.x >= _r.x && pc.x <= _r.x + _r.width 
                && pc.y >= _r.y && pc.y <= _r.y + _r.height);

        PaintObjectWriting pow_current = new PaintObjectWriting(getElementId(), 
                getPen());
        pow_current.addPoint(pc);
        
                
        while (!ls_point.isBehind()) {
            
           DPoint pcNew = ls_point.getItem();
            boolean cInside = (pcNew.x >= _r.x && pcNew.x <= _r.x + _r.width 
                    && pcNew.y >= _r.y && pcNew.y <= _r.y + _r.height);
            
            if (cInside) {
            
                if (lInside) {

                    pow_current.addPoint(pcNew);
                } else {

                    //calculate borderDPoint
                   DPoint pnt_border = findIntersection(_r, pc, new DPoint(
                            pcNew.x - pc.x, pcNew.y - pc.y));

                    //add the borderDPoint to the last PaintObject and insert 
                    //paintObject to list
                    pow_current.addPoint(new DPoint(pnt_border));
                    ls_pow_outside.insertBehind((pow_current));
                    
                    //crate new PaintObject and add the borderDPoint to the 
                    //new PaintObject
                    pow_current = new PaintObjectWriting(getElementId(), 
                            getPen());
                    pow_current.addPoint(new DPoint(pnt_border));
                    
                    //add new DPoint to the PaintObject
                    pow_current.addPoint(new DPoint(pcNew));
                }
                
            } else {

                if (lInside) {

                    //calculate borderDPoint
                   DPoint pnt_border = findIntersection(_r, pc, new DPoint(
                            pcNew.x - pc.x, pcNew.y - pc.y));
                    
                    //add the borderDPoint to the last PaintObject and insert 
                    //paintObject to list
                    pow_current.addPoint(new DPoint(pnt_border));
                    ls_pow_inside.insertBehind(pow_current);
                    
                    //crate new PaintObject and add the borderDPoint to the 
                    //new PaintObject
                    pow_current = new PaintObjectWriting(getElementId(), 
                            getPen());
                    pow_current.addPoint(new DPoint(pnt_border));
                    
                    //add new DPoint to the PaintObject
                    pow_current.addPoint(new DPoint(pcNew));
                } else {

                    pow_current.addPoint(new DPoint(pcNew));
                }
            }

            pc = pcNew;
            lInside = cInside;
            ls_point.next();
        }
        if (lInside) {

            ls_pow_outside.insertBehind(pow_current); 
        } else {

            ls_pow_outside.insertBehind(pow_current); 
        }

        
        /*
         * Transform lists to arrays
         */
        int lengthInside = 0, lengthOutside = 0;
        ls_pow_inside.toFirst();
        ls_pow_outside.toFirst();
        while (!ls_pow_inside.isBehind()) {
            ls_pow_inside.next();
            lengthInside++;
        }
        
        while (!ls_pow_outside.isBehind()) {
            ls_pow_outside.next();
            lengthOutside++;
        }
        PaintObjectWriting [][] pow = new PaintObjectWriting[2][2];
        pow[0] = new PaintObjectWriting[lengthOutside];
        pow[1] = new PaintObjectWriting[lengthInside];

        ls_pow_inside.toFirst();
        ls_pow_outside.toFirst();
        int index = 0;
        while (!ls_pow_inside.isBehind()) {
            pow[1][index] = ls_pow_inside.getItem();
            ls_pow_inside.next();
            index++;
        }

        index = 0;
        while (!ls_pow_outside.isBehind()) {
            pow[0][index] = ls_pow_outside.getItem();
            ls_pow_outside.next();
            index++;
        }
        
        
        return pow;
    }
    
    
    /**
     * Utility method to return the "best" intersection between a line and a 
     * Rectangle.
     * 
     * "best" means in this case that the factor with which the direction vector
     * of the line is multiplied is as small as possible. The equation can be
     * found below:
     * 
     * Vector:
     * ( _p.x )                               ( _v.x)
     * ( _p.y )             +   factor   *    ( _v.y)
     * 
     * 
     * Rectangle vectors
     * ( _r.x )                               ( 0 )
     * ( _r.y )             +   factor2  *    ( 1 )
     * 
     * ( _r.x )                               ( 1 )
     * ( _r.y )             +   factor2  *    ( 0 )
     * 
     * ( _r.x + _r.width  )                   ( 0 )
     * ( _r.y + _r.height ) +   factor2  *    ( 1 )
     * 
     * ( _r.x + _r.width  )                   ( 1 )
     * ( _r.y + _r.height ) +   factor2  *    ( 0 )
     * 
     * 
     * The means to calculate the best intersectionDPoint is to first calculate
     * the intersection between theDPoint vector and the 4 rectangle vectors.
     * 
     * In the second step, thoseDPoints are deleted that are outside the
     * rectangle border (because the Rectangle vectors are (falsely) logically
     * infinite lines)
     * 
     * In the last step, theDPoint with the smallest factor is chosen to be
     * returned.
     * Visualization _____________
     *       x       |           |
     *               |    x      |
     *               |           |
     *               |___________|
     *   
     * Step 1: calculate the 4 intersections and their factors
     * Thus, the equations to be solved are
     * _v.x     - _d.x      |   _s.x - _p.x
     * _v.y     - _d.y      |   _s.y - _p.y 
     * If _v and _p are named as inside the header of this method, _d and _s
     * the direction and support vectors of the current rectangle line 
     * 
     * Step2: Calculate Intersection1...4 and check whether they are at
     * the rectangle border. Otherwise set them null
     * 
     * 
     * 
     * 
     * @param _r the Rectangle which is to be intersected
     * @param _p theDPoint (thus the "support vector") 
     * @param _v the "direction vector"
     * @return the "best" intersectionDPoint between line and rectangle
     */
    public static DPoint findIntersection(final Rectangle _r, final DPoint _p, 
            final DPoint _v) {
        /*
         * Step 1
         */
        //Visualization s____________
        //      x       s           |
        //              s    x      |
        //              s___________|
        //  LEFT        s
        Matrix m = new Matrix(2, 2 + 1);
        m.setValue(0, 0, _v.x);
        m.setValue(1, 0, _v.y);
        m.setValue(0, 1, 0);
        m.setValue(1, 1, 1);
        m.setValue(0, 2, _r.x - _p.x);
        m.setValue(1, 2, _r.y - _p.y);
        double [] factor1 = m.solve();
        //Visualization s s s s s s s s s s s s s
        //      x       |           |
        //              |    x      |
        //  TOP         |___________|
        m = new Matrix(2, 2 + 1);
        m.setValue(0, 0, _v.x);
        m.setValue(1, 0, _v.y);
        m.setValue(0, 1, 1);
        m.setValue(1, 1, 0);
        m.setValue(0, 2, _r.x - _p.x);
        m.setValue(1, 2, _r.y - _p.y);
        double [] factor2 = m.solve();
        //Visualization ____________s
        //      x       |           s
        //              |    x      s
        //  RIGHT       |___________s
        m = new Matrix(2, 2 + 1);
        m.setValue(0, 0, _v.x);
        m.setValue(1, 0, _v.y);
        m.setValue(0, 1, 0);
        m.setValue(1, 1, -1);
        m.setValue(0, 2, _r.x + _r.width - _p.x);
        m.setValue(1, 2, _r.y + _r.height - _p.y);
        double [] factor3 = m.solve();
        //Visualization _____________
        //      x       |           |
        //              |    x      |
        //  BOTTOM    s s s s s s s s s
        m = new Matrix(2, 2 + 1);
        m.setValue(0, 0, _v.x);
        m.setValue(1, 0, _v.y);
        m.setValue(0, 1, -1);
        m.setValue(1, 1, 0);
        m.setValue(0, 2, _r.x + _r.width - _p.x);
        m.setValue(1, 2, _r.y + _r.height - _p.y);
        double [] factor4 = m.solve();
        /*
         * Step 2
         */
       DPoint intersection1 = null, intersection2 = null, 
                intersection3 = null, intersection4 = null;
        //fetchDPoint
        if (factor1 != null) {
            intersection1 = new DPoint((int) (_p.x + factor1[0] * _v.x), 
                    (int) (_p.y + factor1[0] * _v.y));
            //check whether suitable.
            if (_r.y + _r.height  - (int) intersection1.y < 0
                    || _r.y - (int) intersection1.y > 0) {
                intersection1 = null;
            }
        }
        if (factor2 != null) {
            //fetchDPoint
            intersection2 = new DPoint((int) (_p.x + factor2[0] * _v.x), 
                    (int) (_p.y + factor2[0] * _v.y));
            //check whether suitable.
            if (_r.x + _r.width  - (int) intersection2.x < 0
                    || _r.x - (int) intersection2.x > 0) {
                intersection2 = null;
            }
        }
        if (factor3 != null) {
            //fetchDPoint
            intersection3 = new DPoint((int) (_p.x + factor3[0] * _v.x), 
                    (int) (_p.y + factor3[0] * _v.y));
            //check whether suitable.
            if (_r.y + _r.height  - (int) intersection3.y < 0
                    || _r.y - (int) intersection3.y > 0) {
                intersection3 = null;
            }
        }
        if (factor4 != null) {
            //fetchDPoint
            intersection4 = new DPoint((int) (_p.x + factor4[0] * _v.x), 
                    (int) (_p.y + factor4[0] * _v.y));
            //check whether suitable.
            if (_r.x + _r.width  - (int) intersection4.x < 0
                    || _r.x - (int) intersection4.x > 0) {
                intersection4 = null;
            }
        }
        double minLambda = Double.MAX_VALUE;
        int minIndex = -1;
        if (intersection1 != null) {
           if (minLambda > Math.abs(factor1[0])) {
               minLambda = Math.abs(factor1[0]);
               minIndex = 1;
           }
        }
        if (intersection2 != null) {
            if (minLambda > Math.abs(factor2[0])) {
                minLambda = Math.abs(factor2[0]);
                minIndex = 2;
            }
        }
        if (intersection3 != null) {
            if (minLambda > Math.abs(factor3[0])) {
                minLambda = Math.abs(factor3[0]);
                minIndex = 2 + 1;
            }
        }
        if (intersection4 != null) {
            if (minLambda > Math.abs(factor4[0])) {
                minLambda = Math.abs(factor4[0]);
                minIndex = 2 + 2;
            }
        }
        switch(minIndex) {
        case -1:
            Status.getLogger().warning("PaintObjectWriting@static method; not"
                    + " a single equation matched. That should be impossible"
                    + "\n\nmin index: " + minIndex + "\n\nIntersections:\n"
                    + "1:\t" +  intersection1 + "\n2:\t" + intersection2 
                    + "\n3:\t" + intersection3 + "\n4:\t" + intersection4 
                    + "\n\nFactors:\n1:\t" + factor1[0] + "\n2:\t" + factor2[0]
                    + "\n3:\t" + factor3[0] + "\n4:\t" + factor4[0]);
            return null;
        case 1:
            return intersection1;
        case 2:
            return intersection2;
        case 2 + 1:
            return intersection3;
        case 2 + 2:
            return intersection4;
        default:
            new Exception("PaintObjectWriting@static method; Unexpected case")
            .printStackTrace();
            return null;
        }
    }
    
    
    /**
     * Utility method for printing aDPoint.
     * Move to utils.
     * 
     * @param _p theDPoint which is to be printed
     */
    public static void verify(final DPoint _p) {

        new Thread() {
            public void run() {
                
                
                final int sleepTime = 500;
                
                while (!isInterrupted()) {
                    Graphics g = Page.getInstance()
                            .getJlbl_selectionPainting().getGraphics();
                    
                    g.setColor(Color.orange);
                    g.drawRect((int)_p.x, (int)_p.y, 2, 2);
                    
                    try {
                        Thread.sleep(sleepTime);
                    } catch (Exception e) {
                        interrupt();
                    }
                }
            }
        } .start();
    }


    @Override
    public synchronized void stretch(
            final DPoint _pnt_from, final DPoint _pnt_totalStretch,
            final DPoint _pnt_size) {


        minX = Integer.MAX_VALUE; 
        minY = Integer.MAX_VALUE; 
        maxX = Integer.MIN_VALUE; 
        maxY = Integer.MIN_VALUE;
        
        ls_point.toFirst();
        while (!ls_point.isBehind()) {
            
           DPoint pnt_vector = new DPoint(ls_point.getItem().x - _pnt_from.x,
                    ls_point.getItem().y - _pnt_from.y);

            double dX = pnt_vector.x + _pnt_from.x 
                    - (_pnt_totalStretch.x * pnt_vector.x) / _pnt_size.x;

            double dY = pnt_vector.y + _pnt_from.y 
                    - (_pnt_totalStretch.y * pnt_vector.y) / _pnt_size.y;
            pnt_vector.x = (int)dX;
            pnt_vector.y = (int)dY;
            
            ls_point.replace(pnt_vector);
            
            //update MIN values
            minX = (int) Math.min(pnt_vector.x, minX);
            minY = (int) Math.min(pnt_vector.y, minY);

            //update MAX values
            maxX = (int) Math.max(pnt_vector.x, maxX);
            maxY = (int) Math.max(pnt_vector.y, maxY);
            
            ls_point.next();
        }
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
    
    /**
     * return list ofDPoints.
     * 
     * @return the list.
     */
    public final List<DPoint> getPoints() {
        return ls_point;
    }

}
