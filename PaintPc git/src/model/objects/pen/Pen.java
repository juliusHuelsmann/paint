//package declaration
package model.objects.pen;

//import declarations
import java.awt.Color;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import javax.swing.ImageIcon;
import view.forms.Page;
import model.objects.painting.po.PaintObjectWriting;
import model.settings.Constants;
import model.settings.Status;
import model.util.DPoint;
import model.util.list.List;
import model.util.solveLGS.Matrix;

/**
 * The pen is an abstract class of which the method for painting a point has 
 * to be overridden and the method for aborting a paint object can be 
 * overridden.<br> <br>
 * 
 * Contained attributes<br>
 *      a)  the color of painting<br>
 *      b)  the thickness of painting<br>
 *      c)  the operation id which specifies whether the paint option is 
 *          printing lines, printing rounded or just printing points.<br>
 *      d)  a serialVersion UID for being able to save this pen. <br><br>
 * 
 * Contained methods<br>
 *      a)  paint a PaintObject <br>
 *          a i)    to screen <br>
 *          a ii)   to screen and to BufferedImage which is necessary to save 
 *                  and for operations like pipette and fill.<br>
 *      b)  save and load an instance of pen.<br><br><br>
 * 
 * @author Julius Huelsmann
 * @version %I%, %U%
 */
public abstract class Pen implements Serializable {

	/**
	 * serial version of pen.
	 */
	private static final long serialVersionUID = 0L;

	/**
	 * color.
	 */
	private Color clr_foreground;
	
	/**
	 * thickness.
	 */
	private int thickness;
	
	/**
	 * id of operation.
	 */
	private int id_operation;
	
	/**
	 * boolean indicating whether the current pen is painting to selected
	 * item.
	 */
	private boolean selected = false;
	
	
	/**
	 * Constructor: saves the operation.
	 * 
	 * @param _operation the operation
	 * @param _thickness the thickness
	 * @param _clr the percentage
	 */
	public Pen(final int _operation, final int _thickness, 
	        final Color _clr) {
	
	    //save the operation
	    this.id_operation = _operation;
		this.thickness = _thickness;
		this.clr_foreground = _clr;
	}
	
	
	/**
	 * this method can be overwritten.
	 */
	public void abort() { }
	
	/**
	 * paint certain PaintObject to image.
	 * 
	 * @param _bi BufferedIsmage 
	 * @param _o PaintObject.
	 * @param _final wheter to print finally to bufferedImage or to paint to
	 *         Graphics _g of the VPaintLabel.
	 * @param _p_start the point which is added to the coordinates of each 
	 *         point drawn at the image.
	 * @param _g the graphics to which is painted
	 * 
	 * @return the new BufferedImage.
	 */
	public final BufferedImage paintToImage(final BufferedImage _bi,
			final PaintObjectWriting _o, final boolean _final, 
			final DPoint _p_start, final BufferedImage _g) {

		//fetch list of points and go to the beginning of the list
		List<DPoint> ls_point = _o.getPoints();
		model.util.list.Element<DPoint> d = _o.getPoints().getElement();
		ls_point.toFirst();

		//if list is empty return the bufferedImage which is not changed.
		if (ls_point.isEmpty()) {
			return _bi;
		}	
		
		
		//if the painting is not final and id is for mathematics.
		int tempId_operation = id_operation;
		if (!_final && id_operation == Constants.PEN_ID_MATHS) {
			tempId_operation = Constants.PEN_ID_LINES;
		}
		
		switch(tempId_operation) {
		case Constants.PEN_ID_POINT:
		    operationPoint(ls_point, _bi, _final, _p_start, _g);
			
			break;
		case Constants.PEN_ID_LINES:

			operationLine(ls_point, _bi, _final, _p_start, _g);
			
			break;
        case Constants.PEN_ID_MATHS:

            operationMaths(
                    ls_point, _bi, _final, _p_start, _g);
            break;

        case Constants.PEN_ID_MATHS_SILENT:

            System.out.println("c1");
            operationMaths(
                    ls_point, _bi, _final, _p_start, _g);
            break;

        case Constants.PEN_ID_MATHS_SILENT_2:

            System.out.println("c2");
            operationMaths2(
                    _o, _bi, _final, _p_start, _g);

            break;

        default:
            break;
		}
		_o.getPoints().setCurrentElement(d);
		return _bi;
	}

    /**
     * paint certain PaintObject to image.
     * 
     * @param _bi BufferedIsmage 
     * @param _o PaintObject.
     * @param _p_start the point which is added to the coordinates of each 
     *         point drawn at the image.
     * 
     * @return the new BufferedImage.
     */
    public final BufferedImage paintLast(final BufferedImage _bi,
            final PaintObjectWriting _o,  
            final DPoint _p_start) {

        //fetch list of points and go to the beginning of the list
        List<DPoint> ls_point = _o.getPoints();

        ls_point.toFirst();
        //if list is empty return the bufferedImage which is not changed.
        if (ls_point.isEmpty()) {
            return _bi;
        }   

        ls_point.toLast();
        ls_point.previous();
        if (ls_point.getItem() == null) {
            return _bi;
        }
        DPoint pnt_previous = new DPoint(ls_point.getItem());
        ls_point.next();
        
        switch(id_operation) {
        case Constants.PEN_ID_POINT:
            
            break;
        case Constants.PEN_ID_LINES:

            paintLine(new DPoint(ls_point.getItem()),
                    pnt_previous, _bi, false, _bi, _p_start);
            
            break;
        case Constants.PEN_ID_MATHS:

//          operationMaths(
//                  ls_point, _bi, _final, _p_start, _g);
          break;
        case Constants.PEN_ID_MATHS_SILENT:

            operationMaths(
                    ls_point, _bi, false, _p_start, _bi);
            break;
        case Constants.PEN_ID_MATHS_SILENT_2:

            operationMaths2(_o, _bi, false, _p_start, _bi);
            break;

        default:
            break;
        }
        return _bi;
    }
	
	/**
	 * the operation for painting a point.
	 * 
	 * @param _ls_point the list which is painted
	 * @param _bi the bufferedImage to which is painted
     * @param _final 
     *          if true:
     *              paint both to Graphics and BufferedImage
     *              
     *          if false:
     *              do only paint to given Graphics
     *              
	 * @param _p_start the start point which is added to the coordinates of 
	 *         each point
	 * @param _g the graphics to which is painted.
	 */
	private void operationPoint(final List<DPoint> _ls_point, 
	        final BufferedImage _bi, final boolean _final, 
	        final DPoint _p_start, final BufferedImage _g) {
    
        //go through the list of points and print point
        while (!_ls_point.isBehind()) {
    
            //print point
            paintPoint(_ls_point.getItem(), _bi, _final, _p_start, _g);
            _ls_point.next();
            
        }
    }


	
	/**
	 * Print lines to the screen.
	 * 
     * @param _ls_point the list which is painted
     * @param _bi the bufferedImage to which is painted
     * @param _final 
     *          if true:
     *              paint both to Graphics and BufferedImage
     *              
     *          if false:
     *              do only paint to given Graphics
     *              
     * @param _p_shift the start point which is added to the coordinates of 
     *         each point
     * @param _g the graphics to which is painted.
	 */
    private void operationLine(final List<DPoint> _ls_point, 
            final BufferedImage _bi, final boolean _final,
            final DPoint _p_shift, final BufferedImage _g) {
    
        
        
        if (selected) {

            //set foreground and thickness and backup old foreground
            final int increasingThick = 2;
            final Color clr_new = new Color(50, 200, 170);
            Color clr_prev = new Color(clr_foreground.getRGB());
            clr_foreground = clr_new;
            thickness += increasingThick;
            
            //previous point
            _ls_point.toFirst();
            DPoint pnt_previous = new DPoint(
                    _ls_point.getItem().getX(),
                    _ls_point.getItem().getY());

            
            paintPoint(pnt_previous, _bi, _final, _p_shift, _g);
            _ls_point.next();
            
            //go through the list of points and print point
            while (!_ls_point.isBehind()) {
        
                //if the current point is not equal to the last point
                //print the line and update the last element.
                if (!(_ls_point.getItem().getX() 
                        == pnt_previous.getX() 
                        && _ls_point.getItem().getY()  
                        == pnt_previous.getY())) {
                 
        
                    paintLine(new DPoint(
                            _ls_point.getItem().getX(),
                            _ls_point.getItem().getY()),
                            pnt_previous, _bi, _final, _g, _p_shift);
                    pnt_previous = new DPoint(
                            _ls_point.getItem().getX(),
                            _ls_point.getItem().getY());
                }
                _ls_point.next();
            }
            
            //reset foreground and thickness
            clr_foreground = clr_prev;
            thickness -= increasingThick;
        } 


        //previous point
        _ls_point.toFirst();
        DPoint pnt_previous = new DPoint(
                _ls_point.getItem().getX(),
                _ls_point.getItem().getY());
        
        paintPoint(pnt_previous, _bi, _final, _p_shift, _g);
        _ls_point.next();
                
        //go through the list of points and print point
        while (!_ls_point.isBehind()) {
            
            //if the current point is not equal to the last point
            //print the line and update the last element.
            if (!(_ls_point.getItem().getX() 
                    == pnt_previous.getX() 
                    && _ls_point.getItem().getY()  
                    == pnt_previous.getY())) {
                
                
                paintLine(new DPoint(
                        _ls_point.getItem().getX(),
                        _ls_point.getItem().getY()),
                        pnt_previous, _bi, _final, _g, _p_shift);
                pnt_previous = new DPoint(
                        _ls_point.getItem().getX(),
                        _ls_point.getItem().getY());
            }
            _ls_point.next();
        }
        
    }
    

    /**
     *  paint certain PaintObject to image.
     * 
     * @param _o the PaintObjectWriting.
     * @param _bi BufferedIsmage 
     * @param _final wheter to print finally to bufferedImage or to paint to
     *         Graphics _g of the VPaintLabel.
     * @param _p_shift the point which is added to the coordinates of each 
     *         point drawn at the image.
     * @param _g the graphics to which is painted
     */
    private void operationMaths2(
            final PaintObjectWriting _o,
            final BufferedImage _bi,
            final boolean _final,
            final DPoint _p_shift,
            final BufferedImage _g) {
        
        if (0 == 0)
            return;
        
        List<DPoint> ls_point = _o.getPoints();
        ls_point.toFirst();

        if (ls_point.isEmpty()) {
            try {
                throw new Exception("asdf");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        

        ls_point.toFirst();
        DPoint p1 = ls_point.getItem();
        ls_point.next();
        
        if (ls_point.isBehind()) {
            paintPoint(new DPoint(p1), _bi, _final, _p_shift, _g);
        } else {

            DPoint p2 = ls_point.getItem();
            int anzahl = 2;
            
            //direction
            // +1 -> p1 > p2 wert -> p2 lieght weiter links
            // -1 -> p2 > p1 wert -> p2 liegt weiter rechts
            // 0 -> gerade, hier aufhoeren
            int direction = (int) (p1.getX() - p2.getX() 
                    / Math.abs(p1.getX() - p2.getX()));
            boolean abschnitt = true;
            while (abschnitt && !ls_point.isBehind()) {
                ls_point.next();
                DPoint p3 = ls_point.getItem();
                if (p3 != null && p2 != null) {

                    int directionTemp = (int) (p2.getX() - p3.getX() 
                            / Math.abs(p2.getX() - p3.getX()));
                    
                    abschnitt = (directionTemp == direction);
                    anzahl++;
                    if (!abschnitt) {
                        ls_point.previous();
                        anzahl--;
                    }
                }
            }

            anzahl = anzahl + 0;
            //funktion berechnen. Habe $anzahl polynome fom Grad $anzahl.
        //  aXWERT³ + bXWERT² + cXWERT + d = YWERT
        //  a = YWERT - bXWERT - 
            
        }
        
        
        
        
        /*
         * compute function
         */
        
        
        
        int x = (int) ls_point.getItem().getX();
        int y = (int) ls_point.getItem().getY();

        paintPoint(new DPoint(x, y), _bi, _final, _p_shift, _g);

        int xOld = x;
        int yOld = y;

        while (!ls_point.isEmpty() && !ls_point.isBehind()) {

            x = (int) ls_point.getItem().getX();
            y = (int) ls_point.getItem().getY();

            paintLine(new DPoint(x, y), new DPoint(xOld, yOld), 
                    _bi, _final, _g, _p_shift);

            xOld = x;
            yOld = y;
            ls_point.next();
        }
    }

    
    /**
     * paint certain PaintObject to image.
     * 
     * @param _ls_point the list of points.
     * @param _bi BufferedIsmage 
     * @param _final wheter to print finally to bufferedImage or to paint to
     *         Graphics _g of the VPaintLabel.
     * @param _p_shift the point which is added to the coordinates of each 
     *         point drawn at the image.
     * @param _g the graphics to which is painted
     */
    private void operationMaths(final List<DPoint> _ls_point,
            final BufferedImage _bi,
            final boolean _final, final DPoint _p_shift, 
            final BufferedImage _g) {

        final int amountOfRuns = 1;
        DPoint pnt_1 = null, pnt_2 = null, pnt_3 = null;
        
        //does only contain the new new points (not merged)
        List<DPoint> ls_newPoints = new List<DPoint>();
        
        //contains all new and old points, new ones merged together
        //if necessary
        List<DPoint> ls_allPoints = new List<DPoint>();
        thickness = amountOfRuns;
        thickness = 1;
        for (int i = 0; i < amountOfRuns; i++) {
            
            ls_newPoints = new List<DPoint>();
            //reinitialize the ls_newPoints and go to the beginning of the
            //points list.
            if (ls_allPoints.isEmpty()) {
                //init
                _ls_point.toFirst();
                while (!_ls_point.isBehind()) {
                    ls_newPoints.insertAtTheEnd(new DPoint(
                            _ls_point.getItem().getX(), 
                            _ls_point.getItem().getY()));
                    _ls_point.next();
                }
            } else {

                ls_newPoints = new List<DPoint>();
                ls_allPoints.toFirst();
                while (!ls_allPoints.isBehind()) {
                    ls_newPoints.insertAtTheEnd(new DPoint(
                            ls_allPoints.getItem().getX(), 
                            ls_allPoints.getItem().getY()));
                    ls_allPoints.next();
                }
                ls_allPoints = new List<DPoint>();
            }
            ls_newPoints.toFirst();

            //0 items
            if (ls_newPoints.isBehind()) {
                return;
            }
            pnt_1 = ls_newPoints.getItem();
            ls_newPoints.next();
            
            //1 item
            if (ls_newPoints.isBehind()) {
                paintPoint(pnt_1, _bi, _final, _p_shift, _g);
                return;
            }
            pnt_2 = ls_newPoints.getItem();
            ls_newPoints.next();
            
            //2 items
            if (ls_newPoints.isBehind()) {
                paintLine(pnt_1, pnt_2, _bi, _final, _g, _p_shift);
                return;
            }
            
//            clr_foreground = Color.black;
//            paintPoint(pnt_1, _bi);
//            paintPoint(pnt_2, _bi);
            ls_allPoints.insertAtTheEnd(pnt_1);
            ls_allPoints.insertAtTheEnd(pnt_2);
            
            while (!ls_newPoints.isBehind()) {
                pnt_3 = ls_newPoints.getItem();
                ls_allPoints.insertAtTheEnd(pnt_3);

                
                //calculate 2 extra points and insert them at the right 
                //position
                Rectangle r = op_mathsGetNewPoints(pnt_1, pnt_2, pnt_3, _bi);

                if (r.x != -1) {
                ls_allPoints.toLast();
                ls_allPoints.previous();
                ls_allPoints.previous();
                if (!ls_allPoints.getItem().equals(pnt_1)) {
                    //merge
                    
                    DPoint pnt_new = new DPoint(
                            (ls_allPoints.getItem().getX() + r.x) / 2, 
                            (ls_allPoints.getItem().getY() + r.y) / 2);
                    ls_allPoints.remove();
                    ls_allPoints.insertBehind(pnt_new);
                } else  {

                    ls_allPoints.insertBehind(new DPoint(r.getLocation()));  
                }
                } else {
                    ls_allPoints.toLast();
                    ls_allPoints.previous();
                    ls_allPoints.previous();
                }
                ls_allPoints.next();
                if (r.width != -1) {

                    ls_allPoints.insertBehind(new DPoint(r.width, r.height));   
                }
//                paintPoint(r.getLocation(), _bi);
//                paintPoint(new Point(r.width, r.height), _bi);
                
                
                //set pnt_1, pnt_2 for next run
                pnt_1 = pnt_2;
                pnt_2 = pnt_3;
                
                //next
                ls_newPoints.next();
            }
            
            ls_allPoints.toFirst();

//            if( i == amountOfRuns - 1){
//
//                thickness = 2;
//                ls_allPoints.toFirst();
//                int k = 0;
//                while(!ls_allPoints.isBehind() && k < 43){
//                    paintPoint(ls_allPoints.getItem(), _bi);
//                    ls_allPoints.next();
//                    k++;
//                }
//            }
//            else{

//            }
//            thickness -= 1;
            

        }

        operationLine(ls_allPoints, _bi, _final, _p_shift, _g);   
//      thickness =  1;
//      operationLine(ls_allPoints, _bi, _p_start);
        //add old points to the total list 
        // 1 2 3 4 5 6 7 ... 19 20
        // o n o n n n n ... n  o
    }


    /**
	 * return the height of a triangle. Help method for the 
	 * operationMaths operation.
	 * 
	 * @param _p1 the first
	 * @param _p2 the second
	 * @param _p3 and the third point
	 * @return the height of triangle.
	 */
	private double op_mathsGetTriangleHeight(final DPoint _p1, 
	        final DPoint _p2, final DPoint _p3) {

        double growthNormalX = (_p3.getX() - _p1.getX());
        double growthNormalY = (_p3.getY() - _p1.getY());
	    
        double growthOrthogonalX = -growthNormalY;
        double growthOrthogonalY = growthNormalX;

        
        //Matrix
        //  x               :   y                   |   result
        //  growthNormalX   :   -growthOrthogonalX  |   _p2.x - _p1.x
        //  growthNormalY   :   -growthOrthogonalY  |   _p2.y - _p1.y

        Matrix m = new Matrix(2, 2 + 1);
        m.setValue(0, 0, growthNormalX);
        m.setValue(0, 1, -growthOrthogonalX);
        m.setValue(0, 2, _p2.getX() - _p1.getX());
        m.setValue(1, 0, growthNormalY);
        m.setValue(1, 1, -growthOrthogonalY);
        m.setValue(1, 2, _p2.getY() - _p1.getY());
        double [] d = m.solve();

        //the point of which distance to _p2 is to be calculated
        
        double vX = _p1.getX() + d[0] * growthNormalX;
        double vY = _p1.getY() + d[0] * growthNormalY;

        if (Double.isNaN(d[0])) {
            vX = _p1.getX() + d[1] * growthOrthogonalX;
            vY = _p1.getY() + d[1] * growthOrthogonalY;
        }
        
        return Math.sqrt(Math.abs(vX - _p2.getX()) * Math.abs(vX - _p2.getX())
                + Math.abs(vY - _p2.getY()) * Math.abs(vY - _p2.getY()));
	}
	
	
	
	/**
	 * 
	 * @param _p1 the first of the 3 points of which the triangle consists
	 * @param _p2 the second of the 3 points of which the triangle consists
	 * @param _p3 the third of the 3 points of which the triangle consists
	 * 
	 * @param _bi the bufferedImage where to paint.
	 * 
	 * @return the rectangle.
	 */
	private Rectangle op_mathsGetNewPoints(final DPoint _p1, final DPoint _p2,
	        final DPoint _p3, final BufferedImage _bi) {
        
        
        double triangleHeight = op_mathsGetTriangleHeight(_p1, _p2, _p3);
        if (Double.isNaN(triangleHeight)) {
            triangleHeight = 0.0;
        }
            
        DPoint p_new1 = null, p_new2 = null;
        
        final int groesserGleich  = 10;
        int dx = (int) (_p1.getX() - _p2.getX());
        int dy = (int) (_p1.getY() - _p2.getY());
        if (Math.sqrt(dx * dx + dy * dy) > groesserGleich) {
            p_new1 = op_mathsAddPoint(_p1, _p2, triangleHeight);
        }
    
        dx = (int) (_p3.getX() - _p2.getX());
        dy = (int) (_p3.getY() - _p2.getY());
        if (Math.sqrt(dx * dx + dy * dy) > groesserGleich) {
            p_new2 = op_mathsAddPoint(_p2, _p3, triangleHeight);
        }
        
        Rectangle toReturn = new Rectangle(-1, -1, -1, -1);
        if (p_new1 != null) {
            toReturn.x = (int) p_new1.getX();
            toReturn.y = (int) p_new1.getY();
        }
        if (p_new2 != null) {
            toReturn.width = (int) p_new2.getX();
            toReturn.height = (int) p_new2.getY();
        }

        return toReturn;
	}


	/**
	 * help method .
	 * 
	 * @param _p1 the first point of two between which the new point is
	 *             inserted
	 *             
	 * @param _p2 the second point of two between which the new point is
     *             inserted
	 *             
	 * @param _triangleHeight the height of the triangle
	 * 
	 * @return the point.
	 */
    private DPoint op_mathsAddPoint(final DPoint _p1, final DPoint _p2, 
            final double _triangleHeight) {
    
            double growthNormalX = (_p2.getX() - _p1.getX());
            double growthNormalY = (_p2.getY() - _p1.getY());
            
            double growthOrthogonalX = -growthNormalY;
            double growthOrthogonalY = growthNormalX;
    
    
            double centerX = _p1.getX() + (_p2.getX() - _p1.getX()) / 2;
            double centerY = _p1.getY() + (_p2.getY() - _p1.getY()) / 2;
            
    //        clr_foreground = Color.green;
    //        paintPoint(new Point((int)centerX, (int)centerY), _bi);
    //        clr_foreground = Color.blue;
            //add normed growth orthogonal a² + b² = 1
            double newPointX = centerX + growthOrthogonalX * _triangleHeight 
                    / ((2 + 2) * Math.sqrt(growthOrthogonalX * growthOrthogonalX
                            + growthOrthogonalY * growthOrthogonalY));
            double newPointY = centerY + growthOrthogonalY * _triangleHeight 
                    / ((2 + 2) * Math.sqrt(growthOrthogonalX * growthOrthogonalX
                            + growthOrthogonalY * growthOrthogonalY));
            
            if (Double.isNaN(newPointX) || Double.isNaN(newPointY)) {
                return null;
            }
            return new DPoint((int) newPointX, (int) newPointY);
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
	 */
	public final void paintLine(final DPoint _p1, final DPoint _p2, 
	        final BufferedImage _bi, final boolean _final, 
	        final BufferedImage _g, final DPoint _pnt_shift) {

		//compute delta values
		int dX = (int) (_p1.getX() - _p2.getX());
		int dY = (int) (_p1.getY() - _p2.getY());

        //print the line between the two points
        for (int a = 0; a < Math.max(Math.abs(dX), Math.abs(dY)); a++) {
            int plusX = a * dX /  Math.max(Math.abs(dX), Math.abs(dY));
            int plusY = a * dY /  Math.max(Math.abs(dX), Math.abs(dY));
            paintPoint(new DPoint(_p1.getX() - plusX, _p1.getY() - plusY), 
                    _bi, _final, _pnt_shift, _g);
        }
	}
	
	/**
	 * print a point to BufferedImage _bi.
	 * @param _p the point
	 * @param _bi the BufferedImage
     * @param _final 
     *          if true:
     *              paint both to Graphics and BufferedImage
     *              
     *          if false:
     *              do only paint to given Graphics
     *              
	 * @param _g the line to which is painted.
	 * @param _shift the point in which the painting to graphics is shifted.
	 */
	protected abstract void paintPoint(DPoint _p, BufferedImage _bi, 
	        boolean _final, DPoint _shift, BufferedImage _g);
		
	
	/*
	 * save and load pen.
	 */
	
	
	/**
	 * Save the current instance of pen at path.
	 * 
	 * @param _path the path where pen is to be saved.
	 * @throws IOException exception.
	 */
	public final void save(final String _path) throws IOException {
		
		//create output streams
		FileOutputStream fos = new FileOutputStream(new File(_path));
		ObjectOutputStream oos = new ObjectOutputStream(fos);

		//write this
		oos.writeObject(this);
		
		//close output streams
		oos.close();
		fos.close();
	}
	
	
	/**
	 * loads an instance of pen and returns it.
	 * 
	 * @param _path file which is to be loaded
	 * @return the instance of pen.
	 * @throws IOException exception
	 * @throws ClassNotFoundException exception
	 */
	public static final Pen load(final String _path) throws IOException,
	ClassNotFoundException {
		
		//create input streams
		FileInputStream fis = new FileInputStream(_path);
		ObjectInputStream ois = new ObjectInputStream(fis);
		
		//load pen
		Pen p = (Pen) ois.readObject();
		
		//close input streams
		ois.close();
		fis.close();
		
		//return instance of pen in case of success.
		return p;
	}
	
	/*
	 * getter and setter methods
	 */
	
	/**
     * return the string identifier for the pen operation like
     * lines, mathematics, and points.
     * 
     * @return the string.
     */
    public final String getID() {
    	
        //switch of operation
        switch (id_operation) {
    	
        //the id of lines
        case Constants.PEN_ID_LINES:
    		return "lines";
    	
    	//the id of mathematics
        case Constants.PEN_ID_MATHS:
    		return "maths";
    	
    	//the id of point
        case Constants.PEN_ID_POINT:
    		return "points";
    	
        //if unknown id print unknown
        default:
    		return "unknown";
    	}
    }


    /**
	 * get the foreground color.
	 * @return the foreground color.
	 */
	public final Color getClr_foreground() {
		return clr_foreground;
	}

	/**
	 * set the foreground color of the pen.
	 * @param _clr_foreground the foreground color.
	 */
	public final void setClr_foreground(final Color _clr_foreground) {
		this.clr_foreground = _clr_foreground;
	}

	/**
	 * get the thickness.
	 * @return the thickness.
	 */
	public final int getThickness() {
		return thickness;
	}

	/**
	 * set the thickness.
	 * @param _thickness the thickness.
	 */
	public final void setThickness(final int _thickness) {
		this.thickness = _thickness;
	}


    /**
     * @return the id_operation
     */
    public final int getId_operation() {
        return id_operation;
    }


    /**
     * @param _id_operation the id_operation to set
     */
    public final void setId_operation(final int _id_operation) {
        this.id_operation = _id_operation;
    }


    /**
     * @return the selected
     */
    public final boolean isSelected() {
        return selected;
    }


    /**
     * @param _selected the selected to set
     */
    public final void setSelected(final boolean _selected) {
        this.selected = _selected;
    }



    /**
     * Print a preview of the painting.
     * @param _x the x coordinate
     * @param _y the y coordinate
     */
    public final void preprint(final int _x, final int _y) {

        final double factorW = 1.0 * Status.getImageSize().width 
                / Status.getImageShowSize().width;
        final double factorH = 1.0 * Status.getImageSize().height
                / Status.getImageShowSize().height;
        BufferedImage bi = Page.getInstance().getEmptyBISelection();
        paintPoint(new DPoint(_x * factorW, _y * factorH), bi, false, 
                new DPoint(0, 0),
                bi);
        
        Page.getInstance().getJlbl_selectionBG().setIcon(new ImageIcon(bi));
    }
}
