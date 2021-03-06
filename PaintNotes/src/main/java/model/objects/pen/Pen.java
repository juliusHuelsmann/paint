//package declaration
package model.objects.pen;


/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */


//import declarations
import java.awt.Color;
import java.awt.Point;
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
import javax.swing.JLabel;

import start.test.BufferedViewer;
import model.objects.painting.po.PaintObjectWriting;
import model.objects.pen.normal.BallPen;
import model.objects.pen.normal.Marker;
import model.objects.pen.normal.Pencil;
import model.objects.pen.special.PenSelection;
import model.settings.Constants;
import model.settings.State;
import model.util.DPoint;
import model.util.DRect;
import model.util.adt.list.List;
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
	 * The icon path.
	 */
	private String iconPath = "";
	
	/**
	 * Constructor: saves the operation.
	 * 
	 * @param _operation the operation
	 * @param _thickness the thickness
	 * @param _clr the percentage
	 * @param _iconPath the path of the icon
	 */
	public Pen(final int _operation, final int _thickness, 
	        final Color _clr, final String _iconPath) {
	
	    //save the operation
	    this.id_operation = _operation;
		this.thickness = _thickness;
		this.clr_foreground = _clr;
		this.iconPath = _iconPath;
	}

	
	/**
	 * Return the displayed name of the pen depending on its specific 
	 * implementation.
	 * @return the name of the pen.
	 */
	public abstract String getName();
	
	
	/**
	 * Clone the pen.
	 * @param _pen the pen
	 * @return the cloned pen.
	 */
	public static final Pen clonePen(final Pen _pen) {

	    Pen pen;
        if (_pen instanceof BallPen) {

            pen = new BallPen(_pen.getId_operation(),
                    _pen.getThickness(), 
                    new Color(_pen.getClr_foreground().getRGB(), true));
        } else if (_pen instanceof PenSelection) {

            pen = new PenSelection();
        } else if (_pen instanceof Pencil) {

            pen = new Pencil(_pen.getId_operation(),
                    _pen.getThickness(), 
                    new Color(_pen.getClr_foreground().getRGB(), true));
        } else if (_pen instanceof Marker) {

            pen = new Marker(_pen.getId_operation(),
                    _pen.getThickness(), 
                    new Color(_pen.getClr_foreground().getRGB(), true));
        } else {
            
            //alert user.
        	State.showMessageDialog("PROGRAMMIERFEHLER @ paintobjectwriting: "
        			+ "Stift noch nicht hinzugefuegt.");
            
            
            //throw exception
            throw new Error("Fehler: stift noch nicht hinzugefuegt.");
        }
        
        return pen;
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
			final DPoint _p_start, final BufferedImage _g, 
			final DRect _rVisibleScope) {

		//fetch list of points and go to the beginning of the list
		List<DPoint> ls_point = _o.getPoints();
		model.util.adt.list.Element<DPoint> d = _o.getPoints().getElement();
		ls_point.toFirst();

		//if list is empty return the bufferedImage which is not changed.
		if (ls_point.isEmpty()) {
			return _bi;
		}	
		
		final Rectangle rect;
		if (_rVisibleScope == null) {
			rect = null;
		} else {
			rect = _rVisibleScope.getRectangle();
		}
		
		
		//if the painting is not final and id is for mathematics.
		int tempId_operation = id_operation;
		if (!_final && id_operation == Constants.PEN_ID_MATHS) {
			tempId_operation = Constants.PEN_ID_LINES;
		}
		
		switch (tempId_operation) {
		case Constants.PEN_ID_POINT:
		    operationPoint(ls_point, _bi, _final, _p_start, _g, rect);
			
			break;
		case Constants.PEN_ID_LINES:

			operationLine(ls_point, _bi, _final, _p_start, _g, rect);
			
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
		_o.getPoints().goToElement(d);
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

        //if list is empty return the bufferedImage which is not changed.
        if (ls_point.isEmpty()) {
            return _bi;
        }   

        ls_point.toLast();
        DPoint pnt_current = new DPoint(ls_point.getItem());
        ls_point.previous();
        if (ls_point.getItem() == null) {
        	//if there is only one point print it.
        	paintPoint(pnt_current, _bi, false, _p_start, _bi, null);
            return _bi;
        }
        DPoint pnt_previous = new DPoint(ls_point.getItem());
        
        switch (id_operation) {
        case Constants.PEN_ID_POINT:
            
            paintPoint(pnt_current, 
                    _bi, false, _p_start, _bi, null);
            break;
        case Constants.PEN_ID_LINES:

            paintLine(pnt_current,
                    pnt_previous, _bi, false, _bi, _p_start);
            
            break;
        case Constants.PEN_ID_MATHS:

//            paintLine(new DPoint(ls_point.getItem()),
//                    pnt_previous, _bi, false, _bi, _p_start);
          operationMaths(
                  ls_point, _bi, false, _p_start, _bi);
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
	        final DPoint _p_start, final BufferedImage _g, 
	        final Rectangle _rVisibleScope) {
    
	    
        //go through the list of points and print point
        while (!_ls_point.isBehind()) {
    
            //print point
            paintPoint(_ls_point.getItem(), _bi, _final, _p_start, _g, 
            		_rVisibleScope);
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
            final DPoint _p_shift, final BufferedImage _g, 
            final Rectangle _rVisibleScope) {
    
        
        
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

            
            paintPoint(pnt_previous, _bi, _final, _p_shift, _g, _rVisibleScope);
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
        
        paintPoint(pnt_previous, _bi, _final, _p_shift, _g, _rVisibleScope);
        _ls_point.next();
        
        /**
         * the byte value indicates the location of the point towards the 
         * rectangle.
         * 		1: 		top
         * 		2:		top - left
         * 		3:		left
         * 		4:		bottom - left
         * 		
         * 		-1:		bottom
         * 		-2:		bottom - right
         * 		-3: 	right
         * 		-4:		top - right
         * 
         * 		0:		inside rectangle
         */
        Point pnt_relativePositionLast;
        if (_rVisibleScope == null) {
        	pnt_relativePositionLast = new Point(0, 0);
        } else {
        	pnt_relativePositionLast = isInRectanlge(pnt_previous, 
        			_rVisibleScope);
        }
                
        //go through the list of points and print point
        while (!_ls_point.isBehind()) {
            
            //if the current point is not equal to the last point
            //print the line and update the last element.
            if (!(_ls_point.getItem().getX() == pnt_previous.getX() 
            		&& _ls_point.getItem().getY()  == pnt_previous.getY())) {


                Point pnt_relativePositionCurrent;
                if (_rVisibleScope == null) {
                	pnt_relativePositionCurrent = new Point(0, 0);
                } else {
                	pnt_relativePositionCurrent = isInRectanlge(
                			_ls_point.getItem(),
                			_rVisibleScope);
                }
                    
            	
            	//if at least one of the items is inside the rectangle, paint 
            	//the line between them. Otherwise there is nothing to do
            	//but to update the previous point and the boolean indicating
            	//its position relative to the rectangle.
            	if (
            			//if the two points are not at the same side of the
            			//rectangle
            			(pnt_relativePositionLast.x 
            					!= pnt_relativePositionCurrent.x 
            			|| pnt_relativePositionLast.y 
            					!= pnt_relativePositionCurrent.y)
            			
            			//or if the last point is inside the rectangle
            			|| (pnt_relativePositionLast.x == 0
            					&& pnt_relativePositionLast.y == 0)
            					
            			//or if the current point is inside the rectangle
            			|| (pnt_relativePositionCurrent.x == 0 
            					&& pnt_relativePositionCurrent.y == 0)) {

                    paintLine(new DPoint(
                            _ls_point.getItem().getX(),
                            _ls_point.getItem().getY()),
                            pnt_previous, _bi, _final, _g, _p_shift);
            	}
                
            	//save current values for next loop iteration
                pnt_previous = new DPoint(
                        _ls_point.getItem().getX(),
                        _ls_point.getItem().getY());
                pnt_relativePositionLast = pnt_relativePositionCurrent;
            }
            _ls_point.next();
        }
    }
    
    
    
    /**
     * @param _p
     * @param _r
     * @return		
     */
    public static Point isInRectanlge(
    		final DPoint _p, 
    		final Rectangle _r) {
    	//			horizontal
    	//			 -1 0 1
    	//			 _______
    	//VER-	-1	 |_|_|_|
    	//TIC-	 0	 |_|_|_|
    	//AL	 1	 |_|_|_|
    	
    	/**
    	 * Point.x ^ vertical position
    	 * Point.y ^ horizontal positoin.
    	 */
    	Point pnt_position = new Point();
    	
    	//left vs. right
    	if (_p.getX() < _r.x) {
    		pnt_position.x = -1;
    	} else if (_p.getX() > _r.x + _r.width) {

    		pnt_position.x = 1;
    	} else {

    		pnt_position.x = 0;
    	}

    	if (_p.getY() < _r.y) {
    		pnt_position.y = -1;
    	} else if (_p.getY() > _r.y + _r.height) {

    		pnt_position.y = 1;
    	} else {

    		pnt_position.y = 0;
    	}
    	return pnt_position;
    	
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
        
        
        //try to perform spline interpolation.
        
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
            paintPoint(new DPoint(p1), _bi, _final, _p_shift, _g, null);
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

        paintPoint(new DPoint(x, y), _bi, _final, _p_shift, _g, null);

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
     * 
     * @param _ls_point
     * @param _testing
     * @param _pnt_shift
     * @param _bi_paint
     * @param _pen
     * @return
     */
    public static double[][] spline(final List<DPoint> _ls_point, 
    		final boolean _testing,
    		final DPoint _pnt_shift,
    		final BufferedImage _bi_paint,
    		final Pen _pen) {

    	
    	
    	double [] a, b, c, d;
    	double [] x, y;
		
    	
    	/*
    	 * Calculate amount of points.
    	 */
    	int len = 0;
    	_ls_point.toFirst();
    	while (!_ls_point.isEmpty() && !_ls_point.isBehind()) {
    		_ls_point.next();
    		len++;
    	}

    	/*
    	 * Initialize the values which will contain the temporary result.
    	 * The length of b is not equal to the length of a, b, c, d for
    	 * computation reason.
    	 */
    	a = new double[len - 1];
    	b = new double[len - 0];
    	c = new double[len - 1];
    	d = new double[len - 1];
    	
    	// the x and y values from point
    	x = new double [len];
    	y = new double [len];
    	
    	/*
    	 * step 1: calculation fo d_i and of the x and y values
    	 * 
    	 */
    	_ls_point.toFirst();
    	for (int i = 0; i < x.length; i++) {
    		x[i] = _ls_point.getItem().getX();
    		y[i] = _ls_point.getItem().getY();
    		if (i < d.length) {
        		d[i] = y[i];
    		}
    		_ls_point.next();
    	}
    	
    	/*
    	 * Step 2: Calulcation of b_i
    	 */
    	b[0] = 0;
    	b[b.length - 1] = 0;
    	Matrix m = new Matrix(len - 2, len - 1);
    	for (int i = 0; i < b.length - 2; i++) {

    		//
    		// Diagonale und sub&super diagonale
    		//

    		if (i <= len - 2 - 1) {
	    		if (i - 1 >= 0) {
	    			m.setValue(i, i - 1, x[i + 2] - x[i + 1]);
	    		}
    		
	        	m.setValue(i, i, 2 * (x[i + 2] - x[i]));
    		

	    		if (i + 1 <= len - 2 - 1) {
	    			m.setValue(i, i + 1, x[i + 2] - x[i + 1]);
	    		}
    		}
    		
    		
    		// The stuff at the result fields
    		if (i < len - 2) {
    			int j = i + 1;
    			//max j = len - 2, len - 1 also 
    			double sVal;
    			final double three = 3.0;
    			if (j + 1 >= d.length) {
    				sVal = -three * ((d[j] - d[j - 1]) 
    						/ (x[j] - x[j - 1]));
    			} else {
    				sVal = three * ((d[j + 1] - d[j]) 
    						/ (x[j + 1] - x[j]) 
    						- (d[j] - d[j - 1]) / (x[j] - x[j - 1]));
    			}
    			m.setValue(i, len - 2, sVal);
    		}
    		
    	}
    	
    	System.out.println(m.printMatrix());
    	
    	double[] a2_i = m.solve();
    	for (int i = 1; i < b.length - 1; i++) {
    		b[i] = a2_i[i - 1];
    	}
    	
    	
    	/*
    	 * Step 3
    	 */
    	
    	for (int i = 0; i < c.length; i++) {
    		final int three = 3;
    		int j = i + 1;
    		a[j - 1] = (b[j] - b[j - 1]) / (three * (x[j] - x[j - 1]));

    		if (i + 1 >= d.length) {

//        		c[i] = 
//        				+ (b[i+1] - b[i]) * (x[i + 1] - x[i]) / 3
//        				- b[i] * (x[i+1] - x[i]);

        		c[i] = 
        				- (b[i + 1] - b[i]) * (x[i + 1] - x[i]) / three
        				- b[i] * (x[i + 1] - x[i]);

    		} else {

        		c[i] = (d[i + 1] - d[i]) / (x[i + 1] - x[i]) 
        				- (b[i + 1] - b[i]) * (x[i + 1] - x[i]) / three
        				- b[i] * (x[i + 1] - x[i]);
    		}
    	}
		System.out.println("a\tb\tc\td\t");
    	for (int i = 0; i < c.length; i++) {

    		System.out.println(a[i] + "\t" + b[i] + "\t" + c[i] + "\t" + d[i]);
    	}

		double[][] abcd = null;
    	if (_testing) {
    		abcd = new double[2 + 2][a.length];
    		for (int i = 0; i < abcd[0].length; i++) {
				abcd[0][i] = a[i];
				abcd[1][i] = b[i];
				abcd[2][i] = c[i];
				abcd[2 + 1][i] = d[i];
			}
    	} else {
    		int currentXIndex = 0;
    		boolean interrupt = false;
    		int lastX = -1;
    		int lastY = -1;
    		for (double cX = (int) x[0]; cX < (int) x[x.length - 1]; cX += 
    				Math.max(Math.min(1, Math.abs(
    						(x[currentXIndex + 1] - x[currentXIndex]) 
    						/ (y[currentXIndex + 1] - y[currentXIndex]))), 
    						0.01)) {
    			
    			while (cX > x[currentXIndex]) {
    				currentXIndex++;
    				
    				if (currentXIndex >= x.length - 1) {
    					interrupt = true;
    					break;
    				}
    			}
    			if (interrupt) {
    			 	break;
    			}
    			
    			
    			int xCoordiante = (int) (cX);
    			int yCoordinate = (int)
    					(a[currentXIndex] * Math.pow(cX - x[currentXIndex], 
    							2 + 1)
    					+ b[currentXIndex] * Math.pow(cX - x[currentXIndex], 2)
    					+ c[currentXIndex] * Math.pow(cX - x[currentXIndex], 1)
    					+ d[currentXIndex]);

    			if (!(lastX == -1 && lastY == -1)) {
    				_pen.setClr_foreground(Color.green);
    				_pen.paintLine(new DPoint(lastX, lastY),
    						new DPoint(xCoordiante, yCoordinate), _bi_paint, 
    						true, _bi_paint, _pnt_shift);
    				_pen.paintLine(new DPoint(lastX, lastY), 
    						new DPoint(xCoordiante, yCoordinate), _bi_paint, 
    						false, _bi_paint, _pnt_shift);
    				_pen.setClr_foreground(Color.black);
//                    try{
//
//                        _bi_paint.setRGB(
//                        		(int)(xCoordiante - _pnt_shift.getX()),
//                        		(int) (yCoordinate - _pnt_shift.getY()),
//                        		new Color(255, 5, 5).getRGB());
//                        
//                    } catch (Exception e) {
//                    	System.out.println("fehler");
//                    }
    			}
    			lastX = xCoordiante;
    			lastY = yCoordinate;
            
                
    		}
    		BufferedViewer.show(_bi_paint);
    	}
    	
    	
    	
    	
    	return abcd;
	
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

    	if (_final) {
    		spline(_ls_point, false, _p_shift, _bi, this);
    	} else {
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
                    paintPoint(pnt_1, _bi, _final, _p_shift, _g, null);
                    return;
                }
                pnt_2 = ls_newPoints.getItem();
                ls_newPoints.next();
                
                //2 items
                if (ls_newPoints.isBehind()) {
                    paintLine(pnt_1, pnt_2, _bi, _final, _g, _p_shift);
                    return;
                }
//                clr_foreground = Color.black;
//                paintPoint(pnt_1, _bi);
//                paintPoint(pnt_2, _bi);
                ls_allPoints.insertAtTheEnd(pnt_1);
                ls_allPoints.insertAtTheEnd(pnt_2);
                
                while (!ls_newPoints.isBehind()) {
                    pnt_3 = ls_newPoints.getItem();
                    ls_allPoints.insertAtTheEnd(pnt_3);
                    //calculate 2 extra points and insert them at the right 
                    //position
                    Rectangle r = op_mathsGetNewPoints(
                    		pnt_1, pnt_2, pnt_3, _bi);

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

                        ls_allPoints.insertBehind(
                        		new DPoint(r.getLocation()));  
                    }
                    } else {
                        ls_allPoints.toLast();
                        ls_allPoints.previous();
                        ls_allPoints.previous();
                    }
                    ls_allPoints.next();
                    if (r.width != -1) {

                        ls_allPoints.insertBehind(
                        		new DPoint(r.width, r.height));   
                    }
//                    paintPoint(r.getLocation(), _bi);
//                    paintPoint(new Point(r.width, r.height), _bi);
                    
                    
                    //set pnt_1, pnt_2 for next run
                    pnt_1 = pnt_2;
                    pnt_2 = pnt_3;
                    
                    //next
                    ls_newPoints.next();
                }
                
                ls_allPoints.toFirst();

//                if( i == amountOfRuns - 1){
    //
//                    thickness = 2;
//                    ls_allPoints.toFirst();
//                    int k = 0;
//                    while(!ls_allPoints.isBehind() && k < 43){
//                        paintPoint(ls_allPoints.getItem(), _bi);
//                        ls_allPoints.next();
//                        k++;
//                    }
//                }
//                else{

//                }
//                thickness -= 1;

            }

            operationLine(ls_allPoints, _bi, _final, _p_shift, _g, null);   
    	}
    	
    	
        
            
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
                    _bi, _final, _pnt_shift, _g, null);
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
	        boolean _final, DPoint _shift, BufferedImage _g, 
	        final Rectangle _rVisibleScope);
		
	
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
     * @param _biEmtpy the BufferedImage
     * @param _jlbl_selectionBG the JLabel for the selection
     */
    public final void preprint(final int _x, final int _y,
    		final BufferedImage _biEmtpy,
    		final JLabel _jlbl_selectionBG) {

        final double factorW = 1.0 * State.getImageSize().width 
                / State.getImageShowSize().width;
        final double factorH = 1.0 * State.getImageSize().height
                / State.getImageShowSize().height;
        paintPoint(new DPoint(_x * factorW, _y * factorH), _biEmtpy, false, 
                new DPoint(0, 0),
                _biEmtpy, null);
        
        _jlbl_selectionBG.setIcon(new ImageIcon(_biEmtpy));
    }

    
    
    /**
     * Check whether the object equals the current pen object.
     * @param _obj the object.
     * @return whether equals
     */
    @Override
    public final boolean equals(final Object _obj) {

        Class<? extends Object> classname = _obj.getClass();
        Class<? extends Object> classname2 = getClass();
        
        
        if (_obj instanceof Pen) {

            final boolean b1 = id_operation == ((Pen) _obj).getId_operation();
            final boolean b2 = clr_foreground.equals(
                    ((Pen) _obj).getClr_foreground());
            final boolean b3 = thickness == ((Pen) _obj).getThickness();
            final boolean b4 = iconPath.equals(((Pen) _obj).getIconPath());
            final boolean b5 = (classname.equals(classname2));
            return b1 && b2 && b3 && b4 && b5;
        }
        return false;
    }

    /**
     * {@inheritDoc}
     */
    @Override public final int hashCode() {
        return super.hashCode();
    }

    /**
     * @return the iconPath
     */
    public final String getIconPath() {
        return iconPath;
    }



    /**
     * @param _iconPath the iconPath to set
     */
    public final void setIconPath(final String _iconPath) {
        this.iconPath = _iconPath;
    }
}
