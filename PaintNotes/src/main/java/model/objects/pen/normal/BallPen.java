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
public class BallPen extends Pen {

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
	public BallPen(final int _index, final int _thickness, 
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
	

	
	private int getSizeBorder() {

		int sizeBorder = 0;
		switch(getThickness()) {
		case 0:
		case 1:
		case 2:
			sizeBorder = 0;
			break;
		case 3:
			sizeBorder = 1;
			break;
		default:
			sizeBorder = 2;
				break;
		}
		return sizeBorder;
	}
	private void paintBorder(final DPoint _p, 
	        final BufferedImage _bi, final boolean _final, 
	        final DPoint _pnt_shift, final BufferedImage _g, 
	        final Rectangle _r_visibleScope) {

		
	    for (int i = -getThickness() / 2;
                i < (getThickness() / 2) + (getThickness() % 2); i++) {
	    	
	    	while (i >= getThickness() / 2 + getSizeBorder()
	    			&& i < (getThickness() / 2) + (getThickness() % 2) - getSizeBorder()) {
	    		i++;
	    	}
	    	
            for (int j = -getThickness() / 2;
                    j < (getThickness() / 2) + (getThickness() % 2); j++) {


    	    	while (j >= getThickness() / 2 + getSizeBorder()
    	    			&& j < (getThickness() / 2) + (getThickness() % 2) - getSizeBorder()) {
    	    		j++;
    	    	}
    	    	
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
				        	
				        	final Color clr_item = new Color(_bi.getRGB((int) _p.getX() + i, 
				        			(int) _p.getY() + j), true); 
	        					
				        	if (!(clr_item.getRed() == getClr_foreground().getRed()
				        			&& clr_item.getGreen() == getClr_foreground().getGreen()
				        			&& clr_item.getBlue() == getClr_foreground().getBlue()
				        			)) {

					        		int amountPxNeighbor = 0;
					        		int amountRed = 0,
					        				amountG = 0, amountB = 0;

									for (int kX = -1; kX <= 1; kX++) {
					        			for (int kY = -1; kY <= 1; kY++) {
					        				
					        				int cpx = (int) _p.getX() + i + kX;
					        				int cpy = (int) _p.getY() + j + kY;

					        				//if in range
					        				if (cpx >= 0 && cpy >= 0 
					        						&& cpx < _bi.getWidth() 
					        						&& cpy < _bi.getHeight()) {

					        					final Color clr = new Color(_bi.getRGB(cpx, cpy), true); 
					        					
					        					if (kX == 0 && kY == 0) {
					        						if (clr.getAlpha() != 255
					        								&& clr != Color.white) {

					        							int gewichtung = 4;
							        					amountRed += gewichtung * clr.getRed();
							        					amountG += gewichtung * clr.getGreen();
							        					amountB += gewichtung * clr.getBlue();
							        					amountPxNeighbor += gewichtung;
					        						}
					        					} else {

						        					amountRed += clr.getRed();
						        					amountG += clr.getGreen();
						        					amountB += clr.getBlue();
						        					amountPxNeighbor++;
					        					}
					        					
					        				}
										}
									}

									//should never be 0
									if (amountPxNeighbor != 0) {

							        	_bi.setRGB(x, y, new Color(amountRed / amountPxNeighbor,
							        			amountG / amountPxNeighbor, amountB / amountPxNeighbor).getRGB());
									}	
	        					}
				        		
				        } catch (ArrayIndexOutOfBoundsException e) {
				            Error.printError(getClass().getSimpleName(), 
				                    "paintPoint", "point is out of range", 
				                    e, Error.ERROR_MESSAGE_INTERRUPT);
				        }
				    }
				}

                if (!_final) {


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
                    
                    //adjust the location at the zoom.
                    //add the shift coordinates for painting.
                    x = (int) (((_p.getX() + i) * imagePixelSizeX + _pnt_shift.getX()));
                    y = (int) (((_p.getY() + j) * imagePixelSizeY + _pnt_shift.getY()));
                    
                    //the color which is printed.
                    Color clr_pixel = null;

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
                        	


				        	final Color clr_item = new Color(
				        			_g.getRGB((int) ((_p.getX() + i + 0) 
	                        		* imagePixelSizeX + _pnt_shift.getX()),
	                        		(int) ((_p.getY() + j + 0) 
			                        		* imagePixelSizeY + _pnt_shift.getY())), true); 
	        					
				        	if (!(clr_item.getRed() == getClr_foreground().getRed()
				        			&& clr_item.getGreen() == getClr_foreground().getGreen()
				        			&& clr_item.getBlue() == getClr_foreground().getBlue()
				        			)) {
				        		
				        		
				        		int amountPxNeighbor = 0;
				        		int amountRed = 0,
				        				amountG = 0, amountB = 0;
				        		
								for (int dX = -1; dX <= 1; dX++) {
				        			for (int dY = 0; dY <= 1; dY++) {

				                        int cpx = (int) ((_p.getX() + i + dX) 
				                        		* imagePixelSizeX + _pnt_shift.getX());
				                        int cpy = (int) ((_p.getY() + j + dY) 
				                        		* imagePixelSizeY + _pnt_shift.getY());

				        				//if in range
				        				if (cpx >= 0 && cpy >= 0 
				        						&& cpx < _g.getWidth() 
				        						&& cpy < _g.getHeight()) {
				        					final Color clr = new Color(_g.getRGB(cpx, cpy)); 
				        					amountRed += clr.getRed();
				        					amountG += clr.getGreen();
				        					amountB += clr.getBlue();
				        					amountPxNeighbor++;
				        				}
									}
								}

								//should never be 0
								if (amountPxNeighbor != 0) {

									clr_pixel = new Color(amountRed / amountPxNeighbor,
						        			amountG / amountPxNeighbor, amountB / amountPxNeighbor);
						        	_g.setRGB(x, y, clr_pixel.getRGB());
								}
				        	} else {
				        		clr_pixel = getClr_foreground();
				        	}
                            
                        } catch (ArrayIndexOutOfBoundsException e) { 
                        	clr_pixel = getClr_foreground();
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
                                    		clr_pixel.getRGB());
                                    
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
	
	private void paintOrig(final DPoint _p, 
	        final BufferedImage _bi, final boolean _final, 
	        final DPoint _pnt_shift, final BufferedImage _g, 
	        final Rectangle _r_visibleScope) {

	    for (int i = -getThickness() / 2 + getSizeBorder() ;
                i < (getThickness() / 2) + (getThickness() % 2) - getSizeBorder(); i++) {
            
            for (int j = -getThickness() / 2 + getSizeBorder();
                    j < (getThickness() / 2) + (getThickness() % 2) - getSizeBorder(); j++) {


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
	@Override protected final void paintPoint(final DPoint _p, 
	        final BufferedImage _bi, final boolean _final, 
	        final DPoint _pnt_shift, final BufferedImage _g, 
	        final Rectangle _r_visibleScope) {

		
		
		paintOrig(_p, _bi, _final, _pnt_shift, _g, _r_visibleScope);
		paintBorder(_p, _bi, _final, _pnt_shift, _g, _r_visibleScope);
		
	}

	
	
	

	/**
	 * {@inheritDoc}
	 */
	@Override
	public final String getName() {
		return TextFactory.getInstance().getName_ballPen();
	}
}
