//package declaration
package model.objects.pen.normal;


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
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import model.objects.pen.Pen;
import model.settings.Constants;
import model.settings.Error;
import model.settings.State;
import model.settings.TextFactory;
import model.settings.ViewSettings;
import model.util.DPoint;


/**
 * Loesungsvorschlag mathematische Schrift: abschnittweise approximieren.
 * @author Julius Huelsmann
 */
public class BallPenBackup extends Pen {

	/**
     * Default serial version UID for being able to identify the list's 
     * version if saved to the disk and check whether it is possible to 
     * load it or whether important features have been added so that the
     * saved file is out-dated.
	 */
	private static final long serialVersionUID = 0L;
	

	/**
	 * Constructor: calls super -constructor.
	 * @param _index the index of the pen painting method (like line, 
	 *         point, mathematics)
	 * @param _thickness the thickness of painting
	 * @param _background the background.
	 */
	public BallPenBackup(final int _index, final int _thickness, 
	        final Color _background) {
		
		//call super constructor
		super(_index, _thickness, _background, getPath(_index));
	}
	
	
	/**
	 * Return the path of the icon depending on kind of pen.
	 * @param _kind the kind of pen
	 * @return the path
	 */
	private static String getPath(final int _kind) {
	    switch (_kind) {
	    case Constants.PEN_ID_LINES:
            return Constants.PATH_PEN_KULI_LINE;
	    case Constants.PEN_ID_MATHS:
            return Constants.PATH_PEN_KULI_MATHS;
	    case Constants.PEN_ID_POINT:
	        return Constants.PATH_PEN_KULI_POINT;
	    default:
	        State.getLogger().severe("unknown paint index");
	        return null;
	    }
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override protected final void paintPoint(final DPoint _p, 
	        final BufferedImage _bi, final boolean _final, 
	        final DPoint _pnt_shift, final BufferedImage _g, 
	        final Rectangle _r_visibleScope) {

		
		
		// getThickness px. center
		
	    for (int i = -getThickness() / 2;
                i < (getThickness() / 2) + (getThickness() % 2); i++) {
            
            for (int j = -getThickness() / 2;
                    j < (getThickness() / 2) + (getThickness() % 2); j++) {


                //x and y location because is used twice for line 
                //drawing.
                int x, y;

                x = (int) (_p.getX() + i);
                y = (int) (_p.getY() + j);
                
                
				//if is in range
				if (_final && _p.getX() + i >= 0 && _p.getY() + j >= 0 
				        && _p.getX() + i < _bi.getWidth() 
				        && _p.getY() + j < _bi.getHeight()) {

				    //set the given pixel in buffered image
				    if (_final) {
				        try {
				        	
				        	_bi.setRGB(x, y, 
				        			getClr_foreground().getRGB());
				        } catch (ArrayIndexOutOfBoundsException e) {
				            Error.printError(getClass().getSimpleName(), 
				                    "paintPoint", "point is out of range", 
				                    e, Error.ERROR_MESSAGE_INTERRUPT);
				        }
				    }
				}

                if (!_final) {

                    //adjust the location at the zoom.
                    x = ((x) * State.getImageShowSize().width)
                            / State.getImageSize().width;
                    y = ((y) * State.getImageShowSize().height)
                            / State.getImageSize().height;

                    //add the shift coordinates for painting.
                    x +=  _pnt_shift.getX();
                    y +=  _pnt_shift.getY();
                    
                    //the image pixel size in pixel for painting.
                    //for example if zoomed in once, an image pixel has
                    //got the size of two painted pixel in each dimension.
                    //thus it is necessary to paint 4 pixel. These pixel
                    //are painted under the previous pixel. Example:
                    //      [x] [a] [ ]         (x is the pixel which is 
                    //      [a] [a] [ ]         already printed, a are those
                    //      [ ] [ ] [ ]         which are added to avoid gaps.
                    double imagePixelSizeX = 1.0 * State.getImageShowSize().width 
                            / State.getImageSize().width,
                            imagePixelSizeY = 1.0 * State.getImageShowSize().height 
                            / State.getImageSize().height;
                    

                    //error prevention (divide by zero if zoomed out a little 
                    //bit too much)
                    if (imagePixelSizeX == 0) {
                        imagePixelSizeX = 1;
                    }
                    if (imagePixelSizeY == 0) {
                        imagePixelSizeY = 1;
                    }
                    
                    //if the data is displayed paint lines to graphics. 
                    //otherwise nothing to do.
                    if (x / imagePixelSizeX >= 0 && y / imagePixelSizeY >= 0
                            
                            //if the x coordinates are in range (displayed
                            //at the right edge of the screen)
                            && (int) x / imagePixelSizeX + 1 
                            <= (int) ViewSettings.getView_bounds_page().width
                            
//                            Page.getInstance().getJlbl_painting()
//                            .getWidth() 
                            / imagePixelSizeX

                            //if the x coordinates are in range (displayed
                            //at the bottom edge of the screen)
                            && (int) y / imagePixelSizeY + 1 
                            <= (int) ViewSettings.getView_bounds_page().height
//                            Page.getInstance().getJlbl_painting().getHeight() 
                            / imagePixelSizeY) {
                        
                        State.setCounter_paintedPoints(State
                                .getCounter_paintedPoints() + 1);
                        
                        //print the pixel because even if the pixel size in 
                        //pixel is (rounded) equal to 0 the pixel has to be 
                        //printed.
                        
                        try {
                            _g.setRGB(x, y, getClr_foreground().getRGB());
                            
                        } catch (ArrayIndexOutOfBoundsException e) { 
                            State.getLogger().warning("FEHLER" + x + ";" 
                                    + y + "width"
                                    + _g.getWidth() + "h" + _g.getHeight());
                        }
                        
                        //for loop because i want to paint the gaps between the 
                        //pixel if zoomed in.
                        for (int kx = 0; kx < imagePixelSizeX; kx++) {
                            for (int ky = 0; ky < imagePixelSizeY; ky++) {

                                try {
                                    _g.setRGB(x + kx, y + ky, 
                                            getClr_foreground().getRGB());
                                    
                                } catch (ArrayIndexOutOfBoundsException e) { 
                                    State.getLogger().warning("FEHLER" 
                                            + x + ";" + y + "width"
                                            + _g.getWidth() 
                                            + "height" + _g.getHeight());
                                }
                            }
                        }
                    }
                }
			}
		}
	}


	/**
	 * {@inheritDoc}
	 */
	@Override
	public final String getName() {
		return TextFactory.getInstance().getName_ballPen();
	}
}
