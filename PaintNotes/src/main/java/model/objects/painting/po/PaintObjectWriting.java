//package declaration
package model.objects.painting.po;

//import declarations
import java.awt.Color;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.ObjectInputStream.GetField;

import view.forms.Page;
import model.objects.painting.PaintBI;
import model.objects.painting.Picture;
import model.objects.pen.Pen;
import model.settings.Status;
import model.util.DPoint;
import model.util.Util;
import model.util.list.List;
import model.util.list.SecureListSort;
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
public class PaintObjectWriting extends PaintObjectPen {

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
     * {@inheritDoc}
     */
    @Override public final boolean isInSelectionImage(
    		final byte[][] _r,
            final Point _pnt_shiftRectangle) {

        /*
         * check whether firstDPoint is in rectangle.
         */

        ls_point.toFirst();
        DPoint pnt_previous = new DPoint(ls_point.getItem());
        if (isInSelectionPoint(_r, _pnt_shiftRectangle, pnt_previous)) {
            return true;
        }
        

        /*
         * go through the list ofDPoints and check the lines between
         * the following items.
         */
        ls_point.next();
        while (!ls_point.isBehind()) {

            //if one part of the line is in rectangle return true
            if (pruefeLine(ls_point.getItem(), 
            		pnt_previous, _r, _pnt_shiftRectangle)) {
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
	 * Constructor creates new instance
	 * of list.
	 * 
	 * @param _elementId the id of the element.
	 * @param _pen the pen which is painted
	 */
	public PaintObjectWriting(final int _elementId, final Pen _pen) {
		
	    //call super constructor
	    super(_elementId, _pen);

		//save values
		this.ls_point = new List<DPoint>();
	}
	
	/**
	 * add aDPoint to the list ofDPoints and update maximum and minimum 
	 * coordinates value.
	 * 
	 * @param _pnt theDPoint.
	 */
	public void addPoint(final DPoint _pnt) {

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
            final int _y, final Rectangle _rVisibleScope) {
        
        return getPen().paintToImage(
                _bi, this, _final, new DPoint(_x, _y), _g, _rVisibleScope);
    }

    /**
     * Only print last added point.
     * @param _bi the BufferedImage
     * @param _x shifted
     * @param _y shifted
     * @return the painted BufferedImage
     */
    public final BufferedImage paintLast(final BufferedImage _bi, 
            final int _x, final int _y) {
        
        return getPen().paintLast(_bi, this, new DPoint(_x, _y));
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
        rect.width += 1;
        rect.height += 1;
		if (rect.width <= 0 || rect.height <= 0) {
			rect.width = 1;
			rect.height = 1;
		}
		if (rect.x <= 0 || rect.y <= 0) {
			rect.x = 0;
			rect.y = 0;
		}
		
		

		int x = rect.x, y = rect.y;
        if (x > Status.getImageSize().width) {
            x = Status.getImageSize().width;
        }

        if (x < 0) {
            x = 0;
        } 
        if (y > Status.getImageSize().height) {
            y = Status.getImageSize().height;
        }

        if (y < 0) {
            y = 0;
        } 

        int width = rect.width;
        int height = rect.height;

        if (x + width > Status.getImageSize().width) {
            width = Status.getImageSize().width - x;
        }
        if (y + height > Status.getImageSize().height) {
            height = Status.getImageSize().height - y;
        }
        
        if (width == 0 || height == 0) {

            if (Status.getImageSize().width >= 1 
                    && Status.getImageSize().height >= 1) {

                return paint(new BufferedImage(
                        Status.getImageSize().width, 
                        Status.getImageSize().height, 
                        BufferedImage.TYPE_INT_ARGB), 
                        true, null, 
                        Page.getInstance().getJlbl_painting().getLocation().x,
                        Page.getInstance().getJlbl_painting().getLocation().y,
                        null)
                        .getSubimage(
                                0, 0, 1, 1);
            } else {
                Status.getLogger().warning(
                        "image size zero -> resulting error");
                return null;
            }
        } else {

            return paint(new BufferedImage(
                    Status.getImageSize().width, 
                    Status.getImageSize().height, 
                    BufferedImage.TYPE_INT_ARGB), 
                    true, null, 
                    Page.getInstance().getJlbl_painting().getLocation().x,
                    Page.getInstance().getJlbl_painting().getLocation().y,
                    null)
                    .getSubimage(
                            x, y, width, height);
        }
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
    public static boolean pruefeLine(final DPoint _p1, final DPoint _p2, 
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
     * Utility method to return the "best" intersection between a line and a 
     * Rectangle.
     * 
	 * 
	 * Return the first point which is inside the rectangle, thus the 
	 * intersection.
	 * Important: order of DPOints, the first one has to be the point outside
	 * the rectangle and the second one the point inside the rectangle.
	 * 
     * Has to work JUST LIKE the corresponding paint method paintLine!
     * No matter, if it is penMath...
     * 
     * @see Pen.paintLine(...)
     * 
     * @param _p_outsideRectanlge the first DPoint
     * @param _p_insideRectanlge the secondDPoint
     * @param _r the Rectangle which is to be intersected
     * 
     * @return the  intersectionDPoint between line and rectangle
     */
    public static DPoint checkIntersection(
    		final DPoint _p_outsideRectanlge, 
    		final DPoint _p_insideRectanlge, 
            final Rectangle _r) {

        //compute delta values
        int dX = (int) (_p_outsideRectanlge.getX() - _p_insideRectanlge.getX());
        int dY = (int) (_p_outsideRectanlge.getY() - _p_insideRectanlge.getY());

    	int divisorX = Math.max(Math.abs(dX), Math.abs(dY));
    	int divisorY = Math.max(Math.abs(dX), Math.abs(dY));
    	
        //print the line between the twoDPoints
        for (int a = 0; a <= Math.max(Math.abs(dX), Math.abs(dY)); a++) {


        	int plusX, plusY;
        	if (divisorX == 0) {
        		plusX = 0;
        	} else {
        		plusX = a * dX / divisorX;
        	}
        	if (divisorY == 0) {
        		plusY = 0;
        	} else {
        		plusY = a * dY /  divisorY;
        	}
            
            DPoint pnt_current = new DPoint(
            		_p_outsideRectanlge.getX() - plusX, 
            		_p_outsideRectanlge.getY() - plusY);
            if (isInSelectionPoint(_r, pnt_current)) {
                return pnt_current;
            }
        }
        
        return null;
    }
    


    /**
     * check whether line selected.
     * Has to work JUST LIKE the corresponding paint method paintLine!
     * No matter, if it is penMath...
     * 
     * 
     * @param _p1 point 1
     * @param _p2 point 2
     * @param _r the byte array of booleans
     * @param _pnt_shiftRectangle
     * 				the shifting of the rectangle in x and y direction (or in 
     * 				other words the location of the element _r [0][0] on screen)
     * @return whether between the line is selected.
     */
    public static boolean pruefeLine(final DPoint _p1, final DPoint _p2, 
            final byte[][] _r, final Point _pnt_shiftRectangle) {

        //compute delta values
        int dX = (int) (_p1.getX() - _p2.getX());
        int dY = (int) (_p1.getY() - _p2.getY());

        //print the line between the twoDPoints
        for (int a = 0; a < Math.max(Math.abs(dX), Math.abs(dY)); a++) {
            int plusX = a * dX /  Math.max(Math.abs(dX), Math.abs(dY));
            int plusY = a * dY /  Math.max(Math.abs(dX), Math.abs(dY));
            

            if (isInSelectionPoint(_r, _pnt_shiftRectangle, new DPoint(
                    _p1.getX() - plusX, _p1.getY() - plusY))) {
                return true;
            }
            
        }
        
        return false;
    }
    
    
    
    

    /**
     * Check the space between two points.
     * 
     * The connected points inside the selection are combined inside a 
     * new PaintObjectWriting and thus inserted into the list of PaintObjects
     * that are outside the selection.
     * 
     * The first point is not added to one of the PaintObjectWritings whereas 
     * the last point is. The last PaintObjectWriting is not added to one
     * of the lists.
     * 
     * This function has to work just like the corresponding paintLine method
     * which prints the linking line to graphical user interface.
     * 
     * 
     * The given coordinates in each given variable have to be model values
     * meaning that they are already stretched if the user has enabled 
     * zooming and shifted by the display's location.
     * 
     * 
     * @param _pnt_first 
     * 				The first of the two points between which the space
     * 				is separated into those POWs that are inside and those
     * 				that are outside the selection
     * 
     * @param _pnt_second 
     * 				The second point.
     * 
     * @param _r_selection 
     * 				The byte array containing different states of a picture's
     * 				section that are necessary for the detection of the
     * 				selected Points
     * 
     * @param _pnt_shiftRectangle 	
     * 				the shifting of the rectangle in x and y direction (or in 
     * 				other words the location of the element _r [0][0] on screen
     * 
     * @param _ls_inside 
     * 				the list of PaintObjectWritings that contains all those
     * 				that are completely inside the selection. This list
     * 				(and the one which contains all the elements outside
     * 				the selection) is edited by this method 
     * 
     * @param _ls_outside
     * 				the list of PaintObjectWritings that contains all those
     * 				that are completely inside the selection.
     * 
     * @param _pow
     * 				the current paintObjectWriting (which may contain previous
     * 				points (e.g. from previous method calls))
     * 
     * @return 		The PaintObjectWriting which has not been added to a list
     * 				inside this method (for not double - adding something).
	 * 
     */
    public static PaintObjectWriting checkPointLink(
    		
    		//the first and the second point between which the selection is
    		//checked
    		final DPoint _pnt_first, 
    		final DPoint _pnt_second, 
    		
    		//values concerning the selection rectangle
            final byte[][] _r_selection, 
            final Point _pnt_shiftRectangle,
            
            //lists into which the method inserts the (new) PaintObjectWritings
            final List<PaintObjectWriting> _ls_inside,
            final List<PaintObjectWriting> _ls_outside,
            
            //the current PaintObjectWriting which is altered and maybe inserted
            //into one of the above mentioned lists.
            final PaintObjectWriting _pow) {

        //compute delta values and their maximum value
        int dX = (int) (_pnt_first.getX() - _pnt_second.getX());
        int dY = (int) (_pnt_first.getY() - _pnt_second.getY());
        int dMax = Math.max(Math.abs(dX), Math.abs(dY));
        
        //create PaintObjectWriting and add first point.
        PaintObjectWriting pow_current = _pow;
        
        //check whether the first point is inside the selection list
        boolean insidePrevious = isInSelectionPoint(
        		_r_selection, _pnt_shiftRectangle, _pnt_first);
        
        
        //if the maximal delta value is smaller than 1 meaning that the
        //two given points are the same insert the first point into the
        //corresponding list
        if (dMax < 1) {
        	
        	return pow_current;
        } else {
        	
        	//in this if clause it is certain that the two points are not
        	//at the same location.

        	
        	//calculate line step by step beginning with _pnt_first.
        	//The first point which is inside a different section than the 
        	//first
            for (int a = 0; a <= dMax; a++) {
                
            	//the current change of coordinates starting from first
            	//point
            	int plusX = a * dX / dMax;
                int plusY = a * dY / dMax;

                //calculate the coordinates of the current point
                DPoint pnt_current = new DPoint(
                        (int) (_pnt_first.getX() - plusX), 
                        (int) (_pnt_first.getY() - plusY));


                //check whether the current point is inside the selection
                boolean insideCurr = isInSelectionPoint(
                		_r_selection, _pnt_shiftRectangle, pnt_current);
                    	
                
                //if the last point is inside a different section from the
                //current point
                if (insideCurr != insidePrevious) {

                	pow_current.addPoint(new DPoint(pnt_current));
                	
                	//insert the current point into the suitable list.
                	//(depending on the configuration of the previous item)
                	if (insidePrevious) {
                		_ls_inside.insertBehind(pow_current);
                	} else {
                		_ls_outside.insertBehind(pow_current);
                	}   
                	
                	//update the previous item
                	insidePrevious = insideCurr;

                	//create new list
                	pow_current = Picture.createPOW(Pen.clonePen(
                			pow_current.getPen()));
                	pow_current.addPoint(pnt_current);
                	
                } else  if (
                		//if the current Point is a new point of the
                		//PaintObjectWriting and therefore necessary 
                		//for the form of the POW
                		a == dMax) {
                	
                	pow_current.addPoint(new DPoint(pnt_current));
                	

                } 
            }
        }
        
        //return the current PaintObjectWrititng because it may be possible 
        //that something will be added to it afterwards.
        return pow_current;
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
    
    
    
    
//    /**
//     * {@inheritDoc}
//     */
//    @Override public final synchronized PaintObjectWriting[][] separateOld(
//            final Rectangle _r) {
//
//        //initialize lists and go to the beginning of the point list.
//        List<PaintObjectWriting> 
//        	ls_pow_outside = new List<PaintObjectWriting>(),
//        	ls_pow_inside = new List<PaintObjectWriting>();
//        
//        ls_point.toFirst();
//        
//        //Initialize current Point and verify whether it is outside or inside.
//        //Insert it into the new created PaintObject.
//        DPoint pc = ls_point.getItem();
//        boolean lInside = 
//        		   (pc.getX() >= _r.x && pc.getX() <= _r.x + _r.width) 
//                && (pc.getY() >= _r.y && pc.getY() <= _r.y + _r.height);
//        
//        //create new PaintObjectWriting where to insert the following points.
//        PaintObjectWriting pow_current = new PaintObjectWriting(
//        		Picture.getInstance().getIncreaseCID(), getPen());
//        pow_current.addPoint(new DPoint(pc));
//        
//        
//        ls_point.next();
//        while (!ls_point.isBehind()) {
//            
//            //Initialize current Point and verify whether it is outside or 
//            //inside the rectangle
//            DPoint pcNew = ls_point.getItem();
//            boolean cInside = 
//            		(pcNew.getX() >= _r.x && pcNew.getX() <= _r.x + _r.width)
//            		&& 
//            		(pcNew.getY() >= _r.y && pcNew.getY() <= _r.y + _r.height);
//            
//            
//            if (cInside && lInside) {
//            	//if both are inside just add the point to the new created 
//            	//paintObject.
//                pow_current.addPoint(new DPoint(pcNew));
//                
//            } else if (cInside && !lInside) {
//                
//            	//if the previous point is outside and the current point
//            	//is inside
//            	
//            	//calculate border point
//                DPoint pnt_border = findIntersection(_r, pc, new DPoint(
//                        pcNew.getX() - pc.getX(),
//                        pcNew.getY() - pc.getY()));
//
//                //add the borderDPoint to the last PaintObject and insert 
//                //paintObject to list
//                if (pnt_border != null) {
//                    pow_current.addPoint(new DPoint(pnt_border));
//                    ls_pow_outside.insertBehind((pow_current));
//                } else {
//                	Status.getLogger().warning("error calc border");
//                }
//                
//                //crate new PaintObject and add the borderDPoint to the 
//                //new PaintObject
//                pow_current = new PaintObjectWriting(
//                		Picture.getInstance().getIncreaseCID(), 
//                		getPen());
//                if (pnt_border != null) {
//                    pow_current.addPoint(new DPoint(pnt_border));
//                }
//                
//                //add new DPoint to the PaintObject
//                pow_current.addPoint(new DPoint(pcNew));
//            
//            } else if (!cInside && lInside) {
//
//
//                //calculate borderDPoint
//                DPoint pnt_border = findIntersection(_r, pc, new DPoint(
//                		pcNew.getX() - pc.getX(), 
//                		pcNew.getY() - pc.getY()));
//               
//                //add the borderDPoint to the last PaintObject and insert 
//                //paintObject to list
//                if (pnt_border != null) {
//                    pow_current.addPoint(new DPoint(pnt_border));
//                }
//                ls_pow_inside.insertBehind(pow_current);
//                
//                //crate new PaintObject and add the borderDPoint to the 
//                //new PaintObject
//                pow_current = new PaintObjectWriting(
//                		Picture.getInstance().getIncreaseCID(), 
//                        getPen());
//                if (pnt_border != null) {
//                    pow_current.addPoint(new DPoint(pnt_border));
//                }
//                
//                //add new DPoint to the PaintObject
//                pow_current.addPoint(new DPoint(pcNew));
//            
//            } else if (!cInside && !lInside) {
//
//                
//                //if both items are outside the rectangle, this does not
//                //directly indicate that the line between them does
//                //not cross the selected rectangle. Example:
//                //       _____          Thus if the equation x1 + l * dX
//                //  x1  |     |  x2     has got (two) solutions with l in
//                //      |_____|         (0,1) we have to treat this case
//                //differently.
//                Rectangle d = findSilentIntersection(_r, pc, new DPoint(
//                        pcNew.getX() - pc.getX(), pcNew.getY() - pc.getY()));
//                if (d == null) {
//                    pow_current.addPoint(new DPoint(pcNew));
//                } else {
//
//                    pow_current.addPoint(new DPoint(d.x, d.y));
//                    ls_pow_outside.insertBehind(pow_current);
//                    
//                    pow_current = new PaintObjectWriting(getElementId(), 
//                            getPen());
//                    pow_current.addPoint(new DPoint(d.x, d.y));
//                    pow_current.addPoint(new DPoint(d.width, d.height));
//                    ls_pow_inside.insertBehind(pow_current);
//
//                    pow_current = new PaintObjectWriting(getElementId(), 
//                            getPen());
//                    pow_current.addPoint(new DPoint(d.width, d.height));
//                    pow_current.addPoint(new DPoint(pcNew));
//                }
//            } else {
//            	Status.getLogger().severe("Fatal");
//            	System.exit(1);
//            }
//
//            pc = pcNew;
//            lInside = cInside;
//            ls_point.next();
//        }
//        if (lInside) {
//
//            ls_pow_inside.insertBehind(pow_current); 
//        } else {
//
//            ls_pow_outside.insertBehind(pow_current); 
//        }
//
//        /*
//         * Transform lists to arrays
//         */
//        PaintObjectWriting [][] pow = new PaintObjectWriting[2][2];
//        pow[0] = Util.poLsToArray(ls_pow_outside);
//        pow[1] = Util.poLsToArray(ls_pow_inside);
//        
//        return pow;
//    }
    
    

    
    /**
     * {@inheritDoc}
     */
    @Override public final synchronized PaintObjectWriting[][] separate(
            final Rectangle _r) {

        //initialize lists and go to the beginning of the point list.
        List<PaintObjectWriting> 
        	ls_pow_outside = new List<PaintObjectWriting>(),
        	ls_pow_inside = new List<PaintObjectWriting>();
        ls_point.toFirst();
        
        //Initialize current Point and verify whether it is outside or inside.
        //Insert it into the new created PaintObject.
        DPoint pc = ls_point.getItem();
        boolean lInside = 
        		   (pc.getX() >= _r.x && pc.getX() <= _r.x + _r.width) 
                && (pc.getY() >= _r.y && pc.getY() <= _r.y + _r.height);
        
        //create new PaintObjectWriting where to insert the following points.
        PaintObjectWriting pow_current = new PaintObjectWriting(
        		Picture.getInstance().getIncreaseCID(), getPen());
        pow_current.addPoint(new DPoint(pc));
        
        ls_point.next();
        while (!ls_point.isBehind()) {
            
            //Initialize current Point and verify whether it is outside or 
            //inside the rectangle
            DPoint pcNew = ls_point.getItem();
            boolean cInside = 
            		(pcNew.getX() >= _r.x && pcNew.getX() <= _r.x + _r.width)
            		&& 
            		(pcNew.getY() >= _r.y && pcNew.getY() <= _r.y + _r.height);
            
            if (cInside && lInside) {
            	//if both are inside just add the point to the new created 
            	//paintObject.
                pow_current.addPoint(new DPoint(pcNew));
                
            } else if (cInside && !lInside) {
                
            	//if the previous point is outside and the current point
            	//is inside
            	//calculate border point
                DPoint pnt_border = checkIntersection(pc, pcNew, _r);

                //add the borderDPoint to the last PaintObject and insert 
                //paintObject to list
                if (pnt_border != null) {
                    pow_current.addPoint(new DPoint(pnt_border));
                    ls_pow_outside.insertBehind((pow_current));
                } else {
                	Status.getLogger().warning("error calc border");
                }
                
                //crate new PaintObject and add the borderDPoint to the 
                //new PaintObject
                pow_current = new PaintObjectWriting(
                		Picture.getInstance().getIncreaseCID(), 
                		getPen());
                if (pnt_border != null) {
                    pow_current.addPoint(new DPoint(pnt_border));
                }
                
                //add new DPoint to the PaintObject
                pow_current.addPoint(new DPoint(pcNew));
            
            } else if (!cInside && lInside) {
                //calculate borderDPoint
                DPoint pnt_border = checkIntersection(pcNew, pc, _r);
               
                //add the borderDPoint to the last PaintObject and insert 
                //paintObject to list
                if (pnt_border != null) {
                    pow_current.addPoint(new DPoint(pnt_border));
                }
                ls_pow_inside.insertBehind(pow_current);
                
                //crate new PaintObject and add the borderDPoint to the 
                //new PaintObject
                pow_current = new PaintObjectWriting(
                		Picture.getInstance().getIncreaseCID(), 
                        getPen());
                if (pnt_border != null) {
                    pow_current.addPoint(new DPoint(pnt_border));
                }
                
                //add new DPoint to the PaintObject
                pow_current.addPoint(new DPoint(pcNew));
            
            } else if (!cInside && !lInside) {

                DPoint pnt_border1 = checkIntersection(pc, pcNew, _r);
                if (pnt_border1 != null) {
                	
                	//if the previous point is outside and the current point
                	//is inside
                	//calculate border point
                    DPoint pnt_border2 = checkIntersection(
                    		pcNew, pnt_border1, _r);
                    
                    //outside
                    pow_current.addPoint(new DPoint(pc));
                    pow_current.addPoint(new DPoint(pnt_border1));
                    ls_pow_outside.insertBehind((pow_current));
                    
                    //inside
                    pow_current = new PaintObjectWriting(
                    		Picture.getInstance().getIncreaseCID(), 
                    		getPen());
                    if (pnt_border2 != null) {
                        pow_current.addPoint(new DPoint(pnt_border1));
                        pow_current.addPoint(new DPoint(pnt_border2));
                        ls_pow_inside.insertBehind((pow_current));
                    } else {

                    	//         x (PC_NEW)   
                    	//        o             this may be the case 
                    	//       i______        in following scenario:
                    	//      o|     |
                    	//     o |     |        (x indicates point, o
                    	//    x  |_____|        indicates inside rectangle
                    	//(PC)                  whereas i is inside)
                        pow_current.addPoint(new DPoint(pnt_border1));
                        ls_pow_inside.insertBehind((pow_current));
                    }
                    
                    pow_current = new PaintObjectWriting(
                    		Picture.getInstance().getIncreaseCID(), getPen());

                    if (pnt_border2 != null) {
                        pow_current.addPoint(new DPoint(pnt_border2));
                    } else {
                    	pow_current.addPoint(new DPoint(pnt_border1));
                    }
                    //add new DPoint to the PaintObject
                    pow_current.addPoint(new DPoint(pcNew));
                } else {
                  pow_current.addPoint(new DPoint(pcNew));
                }
                
            } else {
            	Status.getLogger().severe("Fatal");
            	System.exit(1);
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
        PaintObjectWriting [][] pow = new PaintObjectWriting[2][2];
        pow[0] = Util.poLsToArray(ls_pow_outside);
        pow[1] = Util.poLsToArray(ls_pow_inside);
        return pow;
    }
    
    
    
    
    

    /**
     * {@inheritDoc}
     */
    public final synchronized List<PaintObjectWriting> deleteRectangle(
            final Rectangle _r, 
            final List<PaintObjectWriting> _ls_pow_outside) {

    	final List<PaintObjectWriting> ls_pow_outside;
        //initialize lists and go to the beginning of the point list.
    	if (_ls_pow_outside == null) {

        	ls_pow_outside = new List<PaintObjectWriting>();
    	} else {
    		ls_pow_outside = _ls_pow_outside;
    	}
        ls_point.toFirst();
        
        //Initialize current Point and verify whether it is outside or inside.
        //Insert it into the new created PaintObject.
        DPoint pc = ls_point.getItem();
        boolean lInside = 
        		   (pc.getX() >= _r.x && pc.getX() <= _r.x + _r.width) 
                && (pc.getY() >= _r.y && pc.getY() <= _r.y + _r.height);
        
        //create new PaintObjectWriting where to insert the following points.
        PaintObjectWriting pow_current = new PaintObjectWriting(
        		Picture.getInstance().getIncreaseCID(), getPen());
        pow_current.addPoint(new DPoint(pc));
        
        
        ls_point.next();
        while (!ls_point.isBehind()) {
            
            //Initialize current Point and verify whether it is outside or 
            //inside the rectangle
            DPoint pcNew = ls_point.getItem();
            boolean cInside = 
            		(pcNew.getX() >= _r.x && pcNew.getX() <= _r.x + _r.width)
            		&& 
            		(pcNew.getY() >= _r.y && pcNew.getY() <= _r.y + _r.height);
            
            
                
            if (cInside && !lInside) {

            	
            	//calculate border point and insert into the outside list
                DPoint pnt_border = checkIntersection(pc, pcNew, _r);
                if (pnt_border != null) {
                    pow_current.addPoint(new DPoint(pnt_border));
                    ls_pow_outside.insertBehind((pow_current));
                } else {
                	Status.getLogger().warning("error calc border");
                }
                
                pow_current = new PaintObjectWriting(
                		Picture.getInstance().getIncreaseCID(), 
                		getPen());
            
            } else if (!cInside && lInside) {


                DPoint pnt_border = checkIntersection(pcNew, pc, _r);
                
                if (pnt_border != null) {
                    pow_current.addPoint(new DPoint(pnt_border));
                }
                
                //add new DPoint to the PaintObject
                pow_current.addPoint(new DPoint(pcNew));
            
            } else if (!cInside && !lInside) {

            	
            	//TODO: erase speed improvement

                Point pnt_relativeLast, pnt_relativeNew;
                pnt_relativeLast = Pen.isInRectanlge(
                			pc,
                			_r);
                pnt_relativeNew = Pen.isInRectanlge(
                			pcNew,
                			_r);
            	if (
            			//if the two points are not at the same side of the
            			//rectangle
            			(pnt_relativeLast.x 
            					!= pnt_relativeNew.x 
            			&& pnt_relativeLast.y 
            					!= pnt_relativeNew.y)) {

                    DPoint pnt_border1 = checkIntersection(pc, pcNew, _r);
                    if (pnt_border1 != null) {
                    	
                    	
                    	//if the previous point is outside and the current point
                    	//is inside
                    	
                    	//calculate border point
                        DPoint pnt_border2 = checkIntersection(pcNew, 
                        		pnt_border1, _r);
                        
                        //outside
                        pow_current.addPoint(new DPoint(pc));
                        pow_current.addPoint(new DPoint(pnt_border1));
                        ls_pow_outside.insertBehind((pow_current));
                        
                        

                        pow_current = new PaintObjectWriting(
                        		Picture.getInstance().getIncreaseCID(), 
                        		getPen());

                        if (pnt_border2 != null) {
                            pow_current.addPoint(new DPoint(pnt_border2));
                        } else {
                        	pow_current.addPoint(new DPoint(pnt_border1));
                        }
                        //add new DPoint to the PaintObject
                        pow_current.addPoint(new DPoint(pcNew));
                        
                    	
                    } else {

                      pow_current.addPoint(new DPoint(pcNew));
                    }
            	} else {

                    pow_current.addPoint(new DPoint(pcNew));
                  }
            }

            pc = pcNew;
            lInside = cInside;
            ls_point.next();
        }
        if (!lInside) {

            ls_pow_outside.insertBehind(pow_current); 
        }

        
        return ls_pow_outside;
    }
    
    

    
    /**
     * {@inheritDoc}
     */
    @Override
	public final PaintObject[][] separate(
    		final byte[][] _r, final Point _pnt_shiftRectangle) {
    	
    	//initialize for those paintObjects that are inside and outside 
    	//the selection.
    	List<PaintObjectWriting> 
    	ls_pow_outside = new List<PaintObjectWriting>(),
    	ls_pow_inside = new List<PaintObjectWriting>();
    	
    	//go to the beginning of the list and fetch teh first element
    	//as first predecessor
    	ls_point.toFirst();
    	DPoint pnt_predecessor = ls_point.getItem();
    	ls_point.next();
    	
    	//insert first point into new PaintObjectWriting
		PaintObjectWriting pow_current = Picture.createPOW(getPen());
    	pow_current.addPoint(pnt_predecessor);
		while (!ls_point.isEmpty() && !ls_point.isBehind()) {
    		
    		
    		pow_current = checkPointLink(
    				pnt_predecessor, ls_point.getItem(), 
    				_r, _pnt_shiftRectangle, 
    				ls_pow_inside, ls_pow_outside, pow_current);
    		
    		pnt_predecessor = ls_point.getItem();
    		ls_point.next();
    	}
		
		
		if (isInSelectionPoint(_r, _pnt_shiftRectangle, pnt_predecessor)) {

			ls_pow_inside.insertBehind(pow_current);
		} else {
			ls_pow_outside.insertBehind(pow_current);
		}

    	/*
    	 * Transform lists to arrays
    	 */
    	PaintObjectWriting [][] pow = new PaintObjectWriting[2][2];
    	pow[0] = Util.poLsToArray(ls_pow_outside);
    	pow[1] = Util.poLsToArray(ls_pow_inside);
    	return pow;
    	
    }

//    
//    
//    /**
//     * {@inheritDoc}
//     */
//    @Override
//	public final PaintObject[][] separate(
//    		final byte[][] _r, final int _xShift, final int _yShift) {
//
//        //initialize for those paintObjects that are inside and outside 
//    	//the selection.
//        List<PaintObjectWriting> 
//        ls_pow_outside = new List<PaintObjectWriting>(),
//        ls_pow_inside = new List<PaintObjectWriting>();
//        
//        //Initialize current Point and verify whether it is outside or inside.
//        //Insert it into the new created PaintObject.
//        ls_point.toFirst();
//        DPoint pnt_pred = ls_point.getItem();
//
//        //check whether the first point is inside the rectangle and initialize 
//        //a new PaintObject which will contain the first section of the old 
//        //one
//        boolean lInside = isInSelectionPoint(
//                _r, _xShift, _yShift, new DPoint(pnt_pred));
//        PaintObjectWriting pow_current = Picture.createPOW(getPen());
//        pow_current.addPoint(new DPoint(pnt_pred));
//        ls_point.next();
//        
//        while (!ls_point.isBehind()) {
//        	
//            //Initialize current Point and verify whether it is outside or 
//            //inside the rectangle
//            
//        	DPoint pnt_current = ls_point.getItem();
//            boolean cInside = isInSelectionPoint(
//                    _r, _xShift, _yShift, new DPoint(pnt_current));
//            
//            if (cInside && lInside) {
//                pow_current.addPoint(new DPoint(pnt_current));
//            } else if (cInside) {
//                
//                //calculate borderDPoint
//                DPoint pnt_border = findIntersection(pnt_pred, pnt_current, _r, 
//                        _xShift, _yShift);
//
//                //add the borderDPoint to the last PaintObject and insert 
//                //paintObject to list
//                if (pnt_border != null) {
//                    pow_current.addPoint(new DPoint(pnt_border));
//                } else {
//
//                    Status.getLogger().severe("found no intersectino");
//                }
//                ls_pow_outside.insertBehind((pow_current));
//                
//                //crate new PaintObject and add the borderDPoint to the 
//                //new PaintObject
//                pow_current = new PaintObjectWriting(getElementId(), getPen());
//                if (pnt_border != null) {
//                    pow_current.addPoint(new DPoint(pnt_border));
//                }
//                
//                //add new DPoint to the PaintObject
//                pow_current.addPoint(new DPoint(pnt_current));
//            
//            } else if (!cInside && lInside) {
//
//
//                //calculate borderDPoint
//                DPoint pnt_border = findIntersection(pnt_pred, new DPoint(
//                        pnt_current.getX() - pnt_pred.getX(), 
//                        pnt_current.getY() - pnt_pred.getY()), _r, 
//                        _xShift, _yShift);
//               
//                //add the borderDPoint to the last PaintObject and insert 
//                //paintObject to list
//                if (pnt_border != null) {
//                    pow_current.addPoint(new DPoint(pnt_border));
//                } else {
//
//                    Status.getLogger().severe("found no intersectino");
//                }
//                ls_pow_inside.insertBehind(pow_current);
//                
//                //crate new PaintObject and add the borderDPoint to the 
//                //new PaintObject
//                pow_current = new PaintObjectWriting(getElementId(), 
//                        getPen());
//                if (pnt_border != null) {
//                    pow_current.addPoint(new DPoint(pnt_border));
//                }
//                
//                //add new DPoint to the PaintObject
//                pow_current.addPoint(new DPoint(pnt_current));
//            
//            } else if (!cInside) {
//
//                //if both items are outside the rectangle, this does not
//                //directly indicate that the line between them does
//                //not cross the selected rectangle. Example:
//                //       _____          Thus if the equation x1 + l * dX
//                //  x1  |     |  x2     has got (two) solutions with l in
//                //      |_____|         (0,1) we have to treat this case
//                //differently.
//                
//
//                DPoint pnt_border1 = findIntersection(pnt_pred, new DPoint(
//                        pnt_current.getX() - pnt_pred.getX(), pnt_current.getY() - pnt_pred.getY()),
//                        _r, 
//                        _xShift, _yShift);
//                DPoint pnt_border2 = findIntersection(new DPoint(
//                        pnt_current.getX() - pnt_pred.getX(), pnt_current.getY() - pnt_pred.getY()), pnt_pred,
//                        _r, 
//                        _xShift, _yShift);
//                
//                if (pnt_border1 == null && pnt_border2 == null) {
//                    pow_current.addPoint(new DPoint(pnt_current));
//                } else if (pnt_border1 == null || pnt_border2 == null) {
//                    Status.getLogger().severe("error. This must not happen."
//                            + "");
//                } else {
//
//                    pow_current.addPoint(new DPoint(pnt_border1));
//                    ls_pow_outside.insertBehind(pow_current);
//                    
//                    pow_current = new PaintObjectWriting(getElementId(), 
//                            getPen());
//                    pow_current.addPoint(new DPoint(pnt_border1));
//                    pow_current.addPoint(new DPoint(pnt_border2));
//                    ls_pow_inside.insertBehind(pow_current);
//
//                    pow_current = new PaintObjectWriting(getElementId(), 
//                            getPen());
//                    pow_current.addPoint(new DPoint(pnt_border2));
//                }
//            }
//            pnt_pred = pnt_current;
//            lInside = cInside;
//            ls_point.next();
//        }
//        ls_point = null;
//        if (lInside) {
//
//            ls_pow_inside.insertBehind(pow_current); 
//        } else {
//
//            ls_pow_outside.insertBehind(pow_current); 
//        }
//        /*
//         * Transform lists to arrays
//         */
//        PaintObjectWriting [][] pow = new PaintObjectWriting[2][2];
//        pow[0] = Util.poLsToArray(ls_pow_outside);
//        pow[1] = Util.poLsToArray(ls_pow_inside);
//        return pow;
//    }

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

    	SecureListSort<DPoint> ls = findIntersections(_r, _p, _v, false);
//        System.out.println("jetzt hier");
        ls.toFirst();
        while (!ls.isEmpty() 
                && ls.getItem() != null 
                && ls.getItemSortionIndex() < 0
                ) {
        	
        	//"delete" item out of list by performing next()
//            System.out.println("remove item" + ls.getItemSortionIndex());
//            verifyPnt(ls.getItem(), Color.green);
            ls.next();
        }
        if (!ls.isEmpty() && ls.getItem() != null) {
            
           
            
//            System.out.println("\ndrin hier werden die Lambdas kommen:");
            double lambda1 = ls.getItemSortionIndex();
            DPoint p1 = ls.getItem();
//            System.out.println("lambda1" + lambda1);
            if (lambda1 <= 1 && lambda1 >= 0) {
                
                ls.next();
                double lambda2 = ls.getItemSortionIndex();
//                System.out.println("lambda2" + lambda2);
                DPoint p2 = ls.getItem();
                
                //if both are equal (maybe there is some intersection at 
                //edges, thus there may be four possible intersects.
                //thus delete the doubled values if the successor does not
                //equal null.
                //	 1	
                //		*_________
                //      | *  	|
                //      |	 *	|
                //		|_______*
                //					2
                if (p2 != null && p1.getX() == p2.getX() 
                		&& p1.getY() == p2.getY()) {
                	ls.next();
                	DPoint p3 = ls.getItem();
                	if (p3 != null) {
                		p2 = p3;
                	}
                }
//                System.out.println(p2.getX() + "\t"+ p2.getY());
//                System.out.println(p1.getX() + "\t"+ p1.getY());
                if (lambda2 <= 1 && lambda2 >= 0) {
                    return new Rectangle((int) p1.getX(), (int) p1.getY(), 
                            (int) p2.getX(), (int) p2.getY());
                }
            }
        } else {
        	if (Status.isDebug()) {
              System.out.println("elz" + getClass()
            		  + ls.isEmpty() + ".." + ls.getItem());
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
     * @param _sortAbs whether to sort the absolute lambda value or the normal
     * value
     * @return the intersectionDPoints between line and rectangle
     */
    public static synchronized SecureListSort<DPoint> findIntersections(
            final Rectangle _r, 
            final DPoint _p, final DPoint _v, final boolean _sortAbs) {
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
        SecureListSort<DPoint> ls = new SecureListSort<DPoint>();
        ls.setSortASC();
        DPoint intersection1 = null, intersection2 = null, 
                intersection3 = null, intersection4 = null;
        if (factor1 != null) {
            intersection1 = new DPoint(
                    (_p.getX() + factor1[0] * _v.getX()), 
                    (_p.getY() + factor1[0] * _v.getY()));
            //check whether suitable.
            if (
                    _r.y + _r.height  - (int) intersection1.getY() < 0
                    || _r.y - (int) intersection1.getY() > 0
                    ) {
                intersection1 = null;
            } else {
                if (_sortAbs) {
                    ls.insertSorted(intersection1, Math.abs(factor1[0]));
                } else {
                    ls.insertSorted(intersection1, factor1[0]);
                }
            }
        }
        if (factor2 != null) {
            intersection2 = new DPoint(
                    (_p.getX() + factor2[0] * _v.getX()), 
                    (_p.getY() + factor2[0] * _v.getY()));
            //check whether suitable.
            if (
                    _r.x + _r.width  - (int) intersection2.getX() < 0
                    || _r.x - (int) intersection2.getX() > 0
                    ) {
                intersection2 = null;
            } else {
                if (_sortAbs) {
                    ls.insertSorted(intersection2, Math.abs(factor2[0]));
                } else {
                    ls.insertSorted(intersection2, (factor2[0]));
                }
            }
        }
        if (factor3 != null) {
            intersection3 = new DPoint(
                    (_p.getX() + factor3[0] * _v.getX()), 
                    (_p.getY() + factor3[0] * _v.getY()));
            //check whether suitable.
            if (
                    _r.y + _r.height  - (int) intersection3.getY() < 0
                    || _r.y - (int) intersection3.getY() > 0
                    ) {
                intersection3 = null;
            } else {
                if (_sortAbs) {
                    ls.insertSorted(intersection3, Math.abs(factor3[0]));
                } else {
                    ls.insertSorted(intersection3, (factor3[0]));
                }
            }
        }
        if (factor4 != null) {
            intersection4 = new DPoint(
                    (_p.getX() + factor4[0] * _v.getX()), 
                    (_p.getY() + factor4[0] * _v.getY()));
            //check whether suitable.
            if (
                    _r.x + _r.width  - (int) intersection4.getX() < 0
                    || _r.x - (int) intersection4.getX() > 0
//                    || 
//                    f4x < 0 || f4x > _v.getX() 
//                    || f4y < 0 || f4y > _v.getY()
                    ) {
                intersection4 = null;
            } else {
//                System.err.println("f4" + factor4[0]);
                if (_sortAbs) {
                    ls.insertSorted(intersection4, Math.abs(factor4[0]));
                } else {
                    ls.insertSorted(intersection4, (factor4[0]));
                }
            }
        }
        
        //string which can be printed if errors occur. Contains the matrix and
        //each step of its solution.
        String s = s1 + s2 + s3 + s4;
        s = s + "\n";
//        System.out.println(s);
//        ls.printIndex();
        ls.toFirst();
        return ls;
    }
    
    
    
//    /**
//     * Utility method to return the "best" intersection between a line and a 
//     * Rectangle.
//     * 
//     * "best" means in this case that the factor with which the direction 
//     * vector
//     * of the line is multiplied is as small as possible. The equation can be
//     * found below:
//     * 
//     * @param _r the Rectangle which is to be intersected
//     * @param _p theDPoint (thus the "support vector") 
//     * @param _v the "direction vector"
//     * @return the "best" intersectionDPoint between line and rectangle
//     */
//    public static synchronized DPoint findIntersectionOld(final Rectangle _r, 
//            final DPoint _p, 
//            final DPoint _v) {
//    
//        List<DPoint> ls = findIntersections(_r, _p, _v, false);
//        ls.toFirst();
//        if (ls.isEmpty() || ls.getItem() == null) {
//            
//            String s = "PaintObjectWriting@static method; not a single "
//                    + "equation matched. That should be impossible.\n";
//            while (!ls.isBehind() && ls.isEmpty()) {
//                s += ("item " + ls.getItem() 
//                        + "\tSortIndex" + ls.getItemSortionIndex() + "\n");
//                ls.next();
//            }
//            s += "\n";
//            Status.getLogger().warning(s);
//
////            verifyPnt(new DPoint((int) (_p.getX() + ls.getItem().getX() 
////                    * _v.getX()), 
////                    (int) (_p.getY() + ls.getItem().getY() * _v.getY())),
////                    Color.blue);
//            return null;
//            
//        } else {
//            
////        	int signum = -1;
//        	
//            if (ls.getItemSortionIndex() <= 0 
//                    ||  ls.getItemSortionIndex() > 1
//                    || ((int) Math.abs(ls.getItem().getX() - _p.getX()) <= 1 
//                    && 
//    				(int) Math.abs(ls.getItem().getY() - _p.getY()) <= 1)) {
//                ls.next();
//            }
////            System.out.println("soriterungsindex" 
////                    + ((int) (ls.getItem().getX() - _p.getX())) + "und y " 
////                    + (int) (ls.getItem().getY() - _p.getY()) + "\n");
//            
//            //if the point is at the border of the rectangle the nearest 
//            //intersection is the point itself. Then We do not have to find
//            //the nearest intersection but the second one.
//            int dX = (int) (ls.getItemSortionIndex() * _v.getX()), 
//                    dY = (int) (ls.getItemSortionIndex() * _v.getY());
//            if (dX == 0 && dY == 0) {
//                ls.next();
//                System.out.println("next");
//            } else if (dX == 1 && dY == 1) {
//                ls.next();
//            }
////            System.out.println(ls.getItemSortionIndex() + "|" + "\n" 
////            + dX + ".." + dY + "ueberpruefung"
////                    + "exakt" + ls.getItemSortionIndex() * _v.getX() + ".." 
////                    + ls.getItemSortionIndex() * _v.getY());
////            verifyPnt(new DPoint(_p.getX() 
////                    + ls.getItemSortionIndex() * _v.getX(), _p.getY() 
////                    + ls.getItemSortionIndex() * _v.getY()), Color.red);
//            
//            
//            return ls.getItem();
//        }
//    }
    
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


    	//reset minimum and maximum values because they need to be recomputed.
        minX = Integer.MAX_VALUE; 
        minY = Integer.MAX_VALUE; 
        maxX = Integer.MIN_VALUE; 
        maxY = Integer.MIN_VALUE;
        
        //pass the list.
        ls_point.toFirst();
        while (!ls_point.isBehind()) {
            
            DPoint pnt_vector = new DPoint(
                    ls_point.getItem().getX() - _pnt_from.getX(),
                    ls_point.getItem().getY() - _pnt_from.getY());

            double dX = _pnt_from.getX() 
                    + pnt_vector.getX() * 1.0 * _pnt_size.getX() 
                            / (_pnt_size.getX() + _pnt_totalStretch.getX());
            double dY = _pnt_from.getY()               
                    +    pnt_vector.getY() * 1.0 * _pnt_size.getY() 
                    / (_pnt_size.getY() + _pnt_totalStretch.getY());
                
            pnt_vector.setX((int) dX);
            pnt_vector.setY((int) dY);
            
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
     * return list ofDPoints.
     * 
     * @return the list.
     */
    public final List<DPoint> getPoints() {
        return ls_point;
    }


    /**
     * @return the minX
     */
    protected final int getMinX() {
        return minX;
    }


    /**
     * @param _minX the minX to set
     */
    protected final void setMinX(final int _minX) {
        this.minX = _minX;
    }


    /**
     * @return the minY
     */
    protected final int getMinY() {
        return minY;
    }


    /**
     * @param _minY the minY to set
     */
    protected final void setMinY(final int _minY) {
        this.minY = _minY;
    }


    /**
     * @return the maxX
     */
    protected final int getMaxX() {
        return maxX;
    }


    /**
     * @param _maxX the maxX to set
     */
    protected final void setMaxX(final int _maxX) {
        this.maxX = _maxX;
    }


    /**
     * @return the maxY
     */
    protected final int getMaxY() {
        return maxY;
    }


    /**
     * @param _maxY the maxY to set
     */
    protected final void setMaxY(final int _maxY) {
        this.maxY = _maxY;
    }

}
