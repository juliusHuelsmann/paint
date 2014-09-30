//package declaration
package model.objects.painting;

//import declarations
import java.awt.Color;
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
            final Rectangle _r, final DPoint _p) {
        
        //return whether theDPoint is inside given rectangle.
        return (_p.getX() >= _r.x && _p.getY() >= _r.y 
                && _p.getX() <= _r.x + _r.width 
                && _p.getY() <= _r.y + _r.height);
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
		this.ls_point = new List<DPoint>();
		
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
		minX = (int) Math.min(_pnt.getX(), minX);
		minY = (int) Math.min(_pnt.getY(), minY);

		//update MAX values
		maxX = (int) Math.max(_pnt.getX(), maxX);
		maxY = (int) Math.max(_pnt.getY(), maxY);
	
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
                minX = (int) Math.min(ls_point.getItem().getX(), minX);
                minY = (int) Math.min(ls_point.getItem().getY(), minY);

                //update MAX values
                maxX = (int) Math.max(ls_point.getItem().getX(), maxX);
                maxY = (int) Math.max(ls_point.getItem().getY(), maxY);
                
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
        lInside = (pc.getX() >= _r.x && pc.getX() <= _r.x + _r.width 
                && pc.getY() >= _r.y && pc.getY() <= _r.y + _r.height);

        PaintObjectWriting pow_current = new PaintObjectWriting(getElementId(), 
                getPen());
        pow_current.addPoint(new DPoint(pc));
        
                
        while (!ls_point.isBehind()) {
            
           DPoint pcNew = ls_point.getItem();
           boolean cInside = (pcNew.getX() >= _r.x && pcNew.getX() 
                   <= _r.x + _r.width && pcNew.getY() >= _r.y 
                   && pcNew.getY() <= _r.y + _r.height);
            
            if (cInside) {
            
                if (lInside) {

                    pow_current.addPoint(new DPoint(pcNew));
                } else {

                    //calculate borderDPoint
                    DPoint pnt_border = findIntersection(_r, pc, new DPoint(
                            pcNew.getX() - pc.getX(),
                            pcNew.getY() - pc.getY()));
                   

                    //add the borderDPoint to the last PaintObject and insert 
                    //paintObject to list
                    if (pnt_border != null) {
                        pow_current.addPoint(new DPoint(pnt_border));
                    }
                    ls_pow_outside.insertBehind((pow_current));
                    
                    //crate new PaintObject and add the borderDPoint to the 
                    //new PaintObject
                    pow_current = new PaintObjectWriting(getElementId(), 
                            getPen());
                    if (pnt_border != null) {
                        pow_current.addPoint(new DPoint(pnt_border));
                    }
                    
                    //add new DPoint to the PaintObject
                    pow_current.addPoint(new DPoint(pcNew));
                }
                
            } else {

                if (lInside) {

                    //calculate borderDPoint
                    DPoint pnt_border = findIntersection(_r, pc, new DPoint(
                            pcNew.getX() - pc.getX(), 
                            pcNew.getY() - pc.getY()));
                   
                    //add the borderDPoint to the last PaintObject and insert 
                    //paintObject to list
                    if (pnt_border != null) {
                        pow_current.addPoint(new DPoint(pnt_border));
                    }
                    ls_pow_inside.insertBehind(pow_current);
                    
                    //crate new PaintObject and add the borderDPoint to the 
                    //new PaintObject
                    pow_current = new PaintObjectWriting(getElementId(), 
                            getPen());
                    if (pnt_border != null) {
                        pow_current.addPoint(new DPoint(pnt_border));
                    }
                    
                    //add new DPoint to the PaintObject
                    pow_current.addPoint(new DPoint(pcNew));
                } else {

                    
                    //if both items are outside the rectangle, this does not
                    //directly indicate that the line between them does
                    //not cross the selected rectangle. Example:
                    //       _____          Thus if the equation x1 + l * dX
                    //  x1  |     |  x2     has got (two) solutions with l in
                    //      |_____|         (0,1) we have to treat this case
                    //differently.
                    Rectangle d = findSilentIntersection(_r, pc, new DPoint(
                            pcNew.getX() - pc.getX(), 
                            pcNew.getY() - pc.getY()));
                    if (d == null) {
                        pow_current.addPoint(new DPoint(pcNew));
                    } else {

                        System.out.println("hier bin ich");
                        pow_current.addPoint(new DPoint(d.x, d.y));
                        ls_pow_outside.insertBehind(pow_current);
                        
                        pow_current = new PaintObjectWriting(getElementId(), 
                                getPen());
                        pow_current.addPoint(new DPoint(d.x, d.y));
                        pow_current.addPoint(new DPoint(d.width, d.height));
                        ls_pow_inside.insertBehind(pow_current);

                        pow_current = new PaintObjectWriting(getElementId(), 
                                getPen());
                        pow_current.addPoint(new DPoint(d.width, d.height));
                        pow_current.addPoint(new DPoint(pcNew));
                    }
                    
                }
            }

            pc = pcNew;
            lInside = cInside;
            ls_point.next();
        }
        if (lInside) {

            ls_pow_inside.insertBehind(pow_current); 
        } else {

            ls_pow_outside.insertBehind(pow_current); 
        }

        
        /*
         * Transform lists to arrays
         */
        int lengthInside = 0, lengthOutside = 0;
        ls_pow_inside.toFirst();
        ls_pow_outside.toFirst();
        
        //it is necessary to double check behind and empty because an empty
        //list returns the length of 1 otherwise.
        while (!ls_pow_inside.isBehind() && !ls_pow_inside.isEmpty()) {
            ls_pow_inside.next();
            lengthInside++;
        }
        
        while (!ls_pow_outside.isBehind() && !ls_pow_outside.isEmpty()) {
            ls_pow_outside.next();
            lengthOutside++;
        }
        PaintObjectWriting [][] pow = new PaintObjectWriting[2][2];
        pow[0] = new PaintObjectWriting[lengthOutside];
        pow[1] = new PaintObjectWriting[lengthInside];

        ls_pow_inside.toFirst();
        ls_pow_outside.toFirst();
        int index = 0;
        while (!ls_pow_inside.isBehind() && !ls_pow_inside.isEmpty()) {
            pow[1][index] = ls_pow_inside.getItem();
            ls_pow_inside.next();
            index++;
        }

        index = 0;
        while (!ls_pow_outside.isBehind() && !ls_pow_outside.isEmpty()) {
            pow[0][index] = ls_pow_outside.getItem();
            ls_pow_outside.next();
            index++;
        }
        
        
        return pow;
    }
    
    /**
     * @see findIntersection.
     * The difference between find intersection and findSilentIntersection
     * is that the lambda value has to be inside the interval (0,1) and that
     * the two best points are returned in case of success.
     * 
     * @param _r the Rectangle which is to be intersected
     * @param _p theDPoint (thus the "support vector") 
     * @param _v the "direction vector"
     * @return the intersectionDPoints between line and rectangle
     */
    public final synchronized Rectangle findSilentIntersection(
            final Rectangle _r, final DPoint _p, final DPoint _v) {

        List<DPoint> ls = findIntersections(_r, _p, _v);
        ls.toFirst();
        if (!ls.isEmpty() || ls.getItem() != null) {
            
            double lambda1 = ls.getItemSortionIndex();
            DPoint p1 = ls.getItem();
            if (lambda1 <= 1 && lambda1 >= 0) {
                
                ls.next();
                double lambda2 = ls.getItemSortionIndex();
                DPoint p2 = ls.getItem();
                if (lambda2 <= 1 && lambda2 >= 0) {
                    return new Rectangle((int) p1.getX(), (int) p1.getY(), 
                            (int) p2.getX(), (int) p2.getY());
                }
            }
        }
        return null;
    }
    
    /**
     * Utility method to return the intersections between a line and a 
     * Rectangle.
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
     * First calculate the intersection between theDPoint vector and the 
     * 4 rectangle vectors.
     * 
     * In the second step, thoseDPoints are deleted that are outside the
     * rectangle border (because the Rectangle vectors are (falsely) logically
     * infinite lines)
     * 
     * In the last step, the DPoints are inserted into a list sorted by their
     * factor and afterwards returned.
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
     * @return the intersectionDPoints between line and rectangle
     */
    public static List<DPoint> findIntersections(final Rectangle _r, 
            final DPoint _p, final DPoint _v) {
        //Step 1
        //Visualization s____________
        //              s    x      |
        //      x       s___________|
        //  LEFT        s
        Matrix m = new Matrix(2, 2 + 1);
        m.setValue(0, 0, _v.getX());
        m.setValue(1, 0, _v.getY());
        m.setValue(0, 1, 0);
        m.setValue(1, 1, 1);
        m.setValue(0, 2, _r.x - _p.getX());
        m.setValue(1, 2, _r.y - _p.getY());
        String s1 = m.printMatrix();
        double [] factor1 = m.solve();
        //Visualization s s s s s s s s s s s s s
        //              |    x      |
        //  TOP  x      |___________|
        m = new Matrix(2, 2 + 1);
        m.setValue(0, 0, _v.getX());
        m.setValue(1, 0, _v.getY());
        m.setValue(0, 1, 1);
        m.setValue(1, 1, 0);
        m.setValue(0, 2, _r.x - _p.getX());
        m.setValue(1, 2, _r.y - _p.getY());
        String s2 = m.printMatrix();
        double [] factor2 = m.solve();
        //Visualization ____________s
        //      x       |           s
        //              |    x      s
        //  RIGHT       |___________s
        m = new Matrix(2, 2 + 1);
        m.setValue(0, 0, _v.getX());
        m.setValue(1, 0, _v.getY());
        m.setValue(0, 1, 0);
        m.setValue(1, 1, -1);
        m.setValue(0, 2, _r.x + _r.width - _p.getX());
        m.setValue(1, 2, _r.y + _r.height - _p.getY());
        String s3 = m.printMatrix();
        double [] factor3 = m.solve();
        //Visualization _____________
        //      x       |           |
        //              |    x      |
        //  BOTTOM    s s s s s s s s s
        m = new Matrix(2, 2 + 1);
        m.setValue(0, 0, _v.getX());
        m.setValue(1, 0, _v.getY());
        m.setValue(0, 1, -1);
        m.setValue(1, 1, 0);
        m.setValue(0, 2, _r.x + _r.width - _p.getX());
        m.setValue(1, 2, _r.y + _r.height - _p.getY());
        String s4 = m.printMatrix();
        double [] factor4 = m.solve();
        
        
        //STEP 2
        List<DPoint> ls = new List<DPoint>();
        ls.setSortASC();
        DPoint intersection1 = null, intersection2 = null, 
                intersection3 = null, intersection4 = null;
        if (factor1 != null) {
            intersection1 = new DPoint(
                    (_p.getX() + factor1[0] * _v.getX()), 
                    (_p.getY() + factor1[0] * _v.getY()));
            //check whether suitable.
            if (_r.y + _r.height  - (int) intersection1.getY() < 0
                    || _r.y - (int) intersection1.getY() > 0) {
                intersection1 = null;
            } else {
                ls.insertSorted(intersection1, Math.abs(factor1[0]));
            }
        }
        if (factor2 != null) {
            intersection2 = new DPoint(
                    (_p.getX() + factor2[0] * _v.getX()), 
                    (_p.getY() + factor2[0] * _v.getY()));
            //check whether suitable.
            if (_r.x + _r.width  - (int) intersection2.getX() < 0
                    || _r.x - (int) intersection2.getX() > 0) {
                intersection2 = null;
            } else {
                ls.insertSorted(intersection2, Math.abs(factor2[0]));
            }
        }
        if (factor3 != null) {
            intersection3 = new DPoint(
                    (_p.getX() + factor3[0] * _v.getX()), 
                    (_p.getY() + factor3[0] * _v.getY()));
            //check whether suitable.
            if (_r.y + _r.height  - (int) intersection3.getY() < 0
                    || _r.y - (int) intersection3.getY() > 0) {
                intersection3 = null;
            } else {
                ls.insertSorted(intersection3, Math.abs(factor3[0]));
            }
        }
        if (factor4 != null) {
            intersection4 = new DPoint(
                    (_p.getX() + factor4[0] * _v.getX()), 
                    (_p.getY() + factor4[0] * _v.getY()));
            //check whether suitable.
            if (_r.x + _r.width  - (int) intersection4.getX() < 0
                    || _r.x - (int) intersection4.getX() > 0) {
                intersection4 = null;
            } else {
                ls.insertSorted(intersection4, Math.abs(factor4[0]));
            }
        }
        
        //string which can be printed if errors occur. Contains the matrix and
        //each step of its solution.
        String s = s1 + s2 + s3 + s4;
        s = s + "\n";
        
        return ls;
    }
    
    /**
     * Utility method to return the "best" intersection between a line and a 
     * Rectangle.
     * 
     * "best" means in this case that the factor with which the direction vector
     * of the line is multiplied is as small as possible. The equation can be
     * found below:
     * 
     * @param _r the Rectangle which is to be intersected
     * @param _p theDPoint (thus the "support vector") 
     * @param _v the "direction vector"
     * @return the "best" intersectionDPoint between line and rectangle
     */
    public static DPoint findIntersection(final Rectangle _r, final DPoint _p, 
            final DPoint _v) {
    
        List<DPoint> ls = findIntersections(_r, _p, _v);
        ls.toFirst();
        if (ls.isEmpty() || ls.getItem() == null) {
            
            String s = "PaintObjectWriting@static method; not a single "
                    + "equation matched. That should be impossible.\n";
            while (!ls.isBehind() && ls.isEmpty()) {
                s += ("item " + ls.getItem() 
                        + "\tSortIndex" + ls.getItemSortionIndex() + "\n");
                ls.next();
            }
            s += "\n";
            Status.getLogger().warning(s);

            verifyPnt(_p, Color.blue);
            verifyPnt(new DPoint(_p.getX() + _v.getX(), _p.getY() + _v.getY()),
                    Color.orange);
            return null;
            
        } else {
            return ls.getItem();
        }
    }
    
    /**
     * Utility method for printing aDPoint.
     * Move to utils.
     * 
     * @param _p theDPoint which is to be printed
     * @param _c the color in which the point is to be printed.
     */
    public static void verifyPnt(final DPoint _p, final Color _c) {

        new Thread() {
            public void run() {
                
                
                final int sleepTime = 500;
                
                while (!isInterrupted()) {
                    
                    final int thickness = 5;
                    PaintBI.fillRectangleQuick(
                            Page.getInstance().getJlbl_painting().getBi(),
                            _c, new Rectangle((int) _p.getX(), 
                                    (int) _p.getY(), thickness, thickness));
                    
                    try {
                        Thread.sleep(sleepTime);
                    } catch (Exception e) {
                        interrupt();
                    }
                }
            }
        } .start();
    }


    
    /**
     * {@inheritDoc}
     */
    @Override public final synchronized void stretch(
            final DPoint _pnt_from, final DPoint _pnt_totalStretch,
            final DPoint _pnt_size) {


        minX = Integer.MAX_VALUE; 
        minY = Integer.MAX_VALUE; 
        maxX = Integer.MIN_VALUE; 
        maxY = Integer.MIN_VALUE;
        
        ls_point.toFirst();
        while (!ls_point.isBehind()) {
            
            DPoint pnt_vector = new DPoint(
                    ls_point.getItem().getX() - _pnt_from.getX(),
                    ls_point.getItem().getY() - _pnt_from.getY());

            double dX = _pnt_from.getX() 
                    + pnt_vector.getX() * _pnt_size.getX() 
                            / (_pnt_size.getX() + _pnt_totalStretch.getX());
            double dY = _pnt_from.getY()               
                    +    pnt_vector.getY() * _pnt_size.getY() 
                    / (_pnt_size.getY() + _pnt_totalStretch.getY());
                
            pnt_vector.setX(dX);
            pnt_vector.setY(dY);
            
            ls_point.replace(pnt_vector);
            
            //update MIN values
            minX = (int) Math.min(pnt_vector.getX(), minX);
            minY = (int) Math.min(pnt_vector.getY(), minY);

            //update MAX values
            maxX = (int) Math.max(pnt_vector.getX(), maxX);
            maxY = (int) Math.max(pnt_vector.getY(), maxY);
            
            ls_point.next();
        }
    }


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
    
    /**
     * return list ofDPoints.
     * 
     * @return the list.
     */
    public final List<DPoint> getPoints() {
        return ls_point;
    }

}
