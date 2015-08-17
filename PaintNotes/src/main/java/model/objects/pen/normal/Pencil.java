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


import java.awt.Color;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.util.Random;
import model.objects.pen.Pen;
import model.settings.Constants;
import model.settings.State;
import model.settings.ViewSettings;
import model.util.DPoint;


/**
 * Pencil fetched from paper.
 * @author Julius Huelsmann
 * @version %I%, %U%
 */
public class Pencil extends Pen {

    /**
     * Default serial version UID for being able to identify the list's 
     * version if saved to the disk and check whether it is possible to 
     * load it or whether important features have been added so that the
     * saved file is out-dated.
     */
    private static final long serialVersionUID = 0L;
    
    /**
     * The variance and the expectancies for inside and outside.
     */
    private final int variance = 50, expectancyInside = 140, 
            expectancyOutside = 65;
    

    /**
     * Constructor: calls super -constructor.
     * @param _index the index of the pen painting method (like line, 
     *         point, mathematics)
     * @param _thickness the thickness of painting
     * @param _background the background.
     */
    public Pencil(final int _index, final int _thickness, 
            final Color _background) {

        //call super constructor
        super(_index, _thickness, _background, getPath(_index));
    
    }
    
    
    /**
     * {@inheritDoc}
     */
    @Override
    public final String getName() {
    	return "Pencil";
    }
    /**
     * Return the path of the icon depending on kind of pen.
     * @param _kind the kind of pen
     * @return the path
     */
    private static String getPath(final int _kind) {
        switch (_kind) {
        case Constants.PEN_ID_LINES:
            return Constants.PATH_PEN_BLEI_LINE;
        case Constants.PEN_ID_MATHS:
            return Constants.PATH_PEN_BLEI_MATHS;
        case Constants.PEN_ID_POINT:
            return Constants.PATH_PEN_BLEI_POINT;
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

        
        /*
         * Looks just like that
         * 
         *      |a1|  |  |a2|  |  |
         *      ---------------------
         *      |  | x|i2|  |  |  |
         *      ---------------------
         *      |  |i4|i3|  |  |  |
         *      ---------------------
         *      |a4|  |  |a3|  |  |
         *      ---------------------
         */
        for (int i = -getThickness() / 2; i <= getThickness() / 2; i++) {
            
            for (int j = -getThickness() / 2; j <= getThickness() / 2; j++) {


                //x and y location because is used twice for line 
                //drawing.
                int x, y;

                x = (int) _p.getX();
                y = (int) _p.getY();
                
                
                //if is in range
                if (_final && x + i >= 0 && x + i < _bi.getWidth() 
                        && y + i < _bi.getHeight() && y + i >= 0) {

                    //set the given pixel in buffered image
                    if (_final && x >= 0 && x < _bi.getWidth() 
                    		&& y >= 0 && y < _bi.getHeight()) {

                        final int rbg = printPixel(x, y, i, j, x, y, _bi);
                        
                        if (x + i >= 0 && x + i < _bi.getWidth()
                                && y + j >= 0 && y + j < _bi.getHeight()) {

                            _bi.setRGB(x + i, y + j, rbg);
                        }
                    }
                }

                if (!_final) {

                    //adjust the location at the zoom.
                    int rx = ((x + i) * State.getImageShowSize().width)
                            / State.getImageSize().width;
                    int ry = ((y + j) * State.getImageShowSize().height)
                            / State.getImageSize().height;

                    //add the shift coordinates for painting.
                    rx +=  _pnt_shift.getX();
                    ry +=  _pnt_shift.getY();
                    
                    //the image pixel size in pixel for painting.
                    //for example if zoomed in once, an image pixel has
                    //got the size of two painted pixel in each dimension.
                    //thus it is necessary to paint 4 pixel. These pixel
                    //are painted under the previous pixel. Example:
                    //      [x] [a] [ ]         (x is the pixel which is 
                    //      [a] [a] [ ]         already printed, a are those
                    //      [ ] [ ] [ ]         which are added to avoid gaps.
                    int imagePixelSizeX = State.getImageShowSize().width 
                            / State.getImageSize().width,
                            imagePixelSizeY = State.getImageShowSize().height 
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
                    if (rx / imagePixelSizeX >= 0 && ry / imagePixelSizeY >= 0
                            
                            //if the x coordinates are in range (displayed
                            //at the right edge of the screen)
                            && (int) rx / imagePixelSizeX + 1 
                            <= (int) ViewSettings.getView_bounds_page().width
//                            <= (int) Page.getInstance().getJlbl_painting()
//                            .getWidth() 
                            / imagePixelSizeX

                            //if the x coordinates are in range (displayed
                            //at the bottom edge of the screen)
                            && (int) ry / imagePixelSizeY + 1 
                            <= (int) ViewSettings.getView_bounds_page().height
//                            <= (int) Page.getInstance().getJlbl_painting()
//                            .getHeight() 
                            / imagePixelSizeY) {

                        int rbg = 0;
                        if (rx >= 0 && rx < _g.getWidth()
                                && ry >= 0 && ry < _g.getHeight()) {
                            rbg = printPixel(x, y, i, j, (int)
                                (rx),
                                (int) (ry), _g);
                        }
                        State.setCounter_paintedPoints(State
                                .getCounter_paintedPoints() + 1);

                        if (rx >= 0 && rx < _g.getWidth()
                                && ry >= 0 && ry < _g.getHeight()) {

                            _g.setRGB(rx, ry, rbg);
                        } 
                        
                        //for loop because i want to paint the gaps between the 
                        //pixel if zoomed in.
                        for (int kx = 0; kx < imagePixelSizeX; kx++) {
                            for (int ky = 0; ky < imagePixelSizeY; ky++) {

                                if (rx + kx >= 0 && rx + kx < _g.getWidth()
                                        && ry + ky >= 0 
                                        && ry + ky < _g.getHeight()) {

                                    _g.setRGB(rx + kx, ry + ky, rbg);
                                }
                            }
                        }
                    }
                }
            }
        }
    }
    
    
    /**
     * Merge inverted colors. Usually called once for each 
     * color (red, green, blue).
     *  
     * @param _clrA the INVERTED! value part of the first color
     * @param _clrB the INVERTED!value part of the second color
     * @return the merged color value part (not inverted anymore)
     */
    private int mergeColors(final int _clrA, final int _clrB) {
        
        final int maxRBG = 255;
        
        double ma = 1.0 * _clrA / (_clrA + _clrB);
        double mb = 1.0 * _clrB / (_clrA + _clrB);
        
        double value = maxRBG - 1.0 * (_clrA * (1 + ma) + _clrB * (1 + mb)) 
                / (2 * 2);
        return (int) value;
    }
    
    
    
    /**
     * Print one pixe.
     * @param _x
     * @param _y
     * @param _i
     * @param _j
     * @param _rX
     * @param _rY
     * @param _bi
     * @return
     */
    private int printPixel(final int _x, 
            final int _y, final int _i, final int _j, final int _rX, 
            final int _rY, final BufferedImage _bi) {
    	
    	boolean oldVersion = false;
    	
    	if (oldVersion) {
    		return printPixelArea(_x, _y, _i, _j, _rX, _rY, _bi);
    	} else {
    		return printPixelArea2(_x, _y, _i, _j, _rX, _rY, _bi);
    	}
    	
    }

    
    /**
     * Return a part of the pixel area around point (x,y) at point (x+i, y+j).
     * @param _x the x point 
     * @param _y the y point
     * @param _i the x distance to center
     * @param _j the y distance to center
     * @param _rX the real x coordinate in image for fetching point
     * @param _rY the real y coordinate in image for fetching point.
     * @param _bi the BufferedImage for fetching old point.
     * @return the RGB value of the pixel which will be printed.
     */
    private int printPixelArea(final int _x, 
            final int _y, final int _i, final int _j, final int _rX, 
            final int _rY, final BufferedImage _bi) {

        
        //the (inverted) colors
        Color rgbInversOld, rgbInversNew;
        final int maxRBG = 255;
        
        //use different functions depending on whether the 
        //point is outside (or in the else branch inside) 
        if (
                (_i == -1 && _j == -1)
                || (_i == 2)
                || (_j == 2)) {
            int v = (int) normalDistribution(
                    expectancyOutside, variance);
            if (v >= maxRBG - 1) {
                v = maxRBG - 2;
            }
            
               rgbInversNew = new Color(v, v, v);
        } else {

            int v = (int) normalDistribution(
                    expectancyInside, variance);
            if (v >= maxRBG - 1) {
                v = maxRBG - 2;
            }
            rgbInversNew = new Color(v, v + 1, v + 2);
        }
        
        final Color clr_old = new Color(_bi.getRGB(_rX, _rY), true);

        //fetch old color if the color is not completely white
        //or transparent
        if (clr_old.getAlpha() == 0 
                || (clr_old.getRed() == maxRBG
                && clr_old.getGreen() == maxRBG
                && clr_old.getBlue() == maxRBG)) {
            rgbInversOld = rgbInversNew;
        } else {

            rgbInversOld = new Color(maxRBG - clr_old.getRed(), 
                    maxRBG - clr_old.getGreen(), 
                    maxRBG - clr_old.getBlue());
        }
        
        Color merged = new Color(mergeColors(
                rgbInversNew.getRed(), rgbInversOld.getRed()),
                mergeColors(rgbInversNew.getGreen(), 
                        rgbInversOld.getGreen()), mergeColors(
                                rgbInversNew.getBlue(), 
                                rgbInversOld.getBlue()));
        
        

        double fgSum = getClr_foreground().getRed()
               + getClr_foreground().getGreen()
               + getClr_foreground().getBlue();
        final double hellerD = 2.5;
        
       double anteilR = 1.0 * getClr_foreground().getRed() / fgSum;
       double anteilG = 1.0 * getClr_foreground().getGreen() / fgSum;
       double anteilB = 1.0 * getClr_foreground().getBlue() / fgSum;

       double mw = (anteilR + anteilG + anteilB) / (2 + 1);
       double anteilR2 = Math.abs(anteilR - (anteilR - mw) / 2);
       double anteilG2 = Math.abs(anteilG - (anteilG - mw) / 2);
       double anteilB2 = Math.abs(anteilB - (anteilB - mw) / 2);
       
       int red =  (int) (hellerD * anteilR2 * merged.getRed());
       int green = (int) (hellerD * anteilG2 * merged.getGreen());
       int blue = (int) (hellerD * anteilB2 * merged.getBlue());

       if (red > maxRBG) {
           red = maxRBG;
       }
       if (green > maxRBG) {
           green = maxRBG;
       }
       if (blue > maxRBG) {
           blue = maxRBG;
       }
       
       int abstand = Math.max(Math.abs(_i) * 2 + Math.abs(_j), 1);
       red = maxRBG - (maxRBG - red) / abstand;
       green = maxRBG - (maxRBG - green) / abstand;
       blue = maxRBG - (maxRBG - blue) / abstand;
       
       
       final int alpha = 175;
        return new Color(red, green, blue, alpha).getRGB();
    
    }
    

    
    /**
     * Return a part of the pixel area around point (x,y) at point (x+i, y+j).
     * @param _x the x point 
     * @param _y the y point
     * @param _i the x distance to center
     * @param _j the y distance to center
     * @param _rX the real x coordinate in image for fetching point
     * @param _rY the real y coordinate in image for fetching point.
     * @param _bi the BufferedImage for fetching old point.
     * @return the RGB value of the pixel which will be printed.
     */
    private int printPixelArea2(
    		final int _x, 
            final int _y, 
            
            final int _i, final int _j, 
            
            final int _rX, final int _rY, 
            
            final BufferedImage _bi) {

        
        //the (inverted) colors
        Color clr_new0;
        final int maxRBG = 255;
		final int tripelAmout = 3;
        final int divisor = 1;
        
        //use different functions depending on whether the 
        //point is outside (or in the else branch inside) 
        if (
                (_i == -1 && _j == -1)
                || (_i == 2)
                || (_j == 2)) {
        	final int alphaOutside = 5;
            int v = (int) normalDistribution(
                    expectancyOutside, variance);
            if (v >= maxRBG - 1) {
                v = maxRBG - 2;
            }
            
            final int sum = getClr_foreground().getRed() 
            		+ getClr_foreground().getGreen()
            		+ getClr_foreground().getBlue();
         
            if (sum != 0) {

	                
	            clr_new0 = new Color(
	            		   v * getClr_foreground().getRed() / sum / divisor, 
	            		   v * getClr_foreground().getGreen() / sum / divisor, 
	            		   v * getClr_foreground().getBlue() / sum / divisor,
	            		   alphaOutside);
	        } else {
	
	            clr_new0 = new Color(
	         		   v,
	         		   (v + 1),
	         		   (v + 2),
	         		  alphaOutside);
	        }
        } else {

            int v = (int) normalDistribution(
                    expectancyInside, variance);
            if (v >= maxRBG - 1) {
                v = maxRBG - 2;
            }

            final int alphaInside = 140;
            final int sum = getClr_foreground().getRed() 
            		+ getClr_foreground().getGreen()
            		+ getClr_foreground().getBlue();
            if (sum != 0) {

                clr_new0 = new Color(
             		   v * getClr_foreground().getRed() / sum / divisor, 
             		   (v + 1) * getClr_foreground().getGreen() / sum / divisor,
             		   (v + 2) * getClr_foreground().getBlue() / sum / divisor,
             		  alphaInside);
            } else {

                clr_new0 = new Color(
             		   v,
             		   (v + 1),
             		   (v + 2),
             		  alphaInside);
            }
        }
        
        final Color clr_old = new Color(_bi.getRGB(_rX, _rY), true);

        
        
        final int valueOld = (clr_old.getRed()
        		+ clr_old.getGreen() + clr_old.getBlue()) / tripelAmout;
        final int valueNew = (getClr_foreground().getRed() 
        		+ getClr_foreground().getGreen() 
        		+ getClr_foreground().getBlue()) / tripelAmout;

        final int value = (valueOld * 1 + valueNew * 2) / tripelAmout;
        
        //TODO generate the alpha value (new)
        final int alphaTotal = clr_old.getAlpha() + clr_new0.getAlpha();
        
        Color clr_new = new Color(
        		clr_old.getRed() * clr_old.getAlpha() / alphaTotal
        		+ clr_new0.getRed() * clr_new0.getAlpha() / alphaTotal,
        		
        		clr_old.getGreen() * clr_old.getAlpha() / alphaTotal
        		+ clr_new0.getGreen() * clr_new0.getAlpha() / alphaTotal,
        		
        		clr_old.getBlue() * clr_old.getAlpha() / alphaTotal
        		+ clr_new0.getBlue() * clr_new0.getAlpha() / alphaTotal,
        		
        		Math.min(maxRBG,
        				(2 * Math.max(clr_new0.getAlpha(), clr_old.getAlpha())
        				 + 1 * Math.min(clr_new0.getAlpha(), 
        						 clr_old.getAlpha())) / tripelAmout
        				
        				));
        
        final int valueCalced = Math.max(
        		(clr_new.getRed() + clr_new.getGreen()
        				+ clr_new.getBlue()) / tripelAmout, 1);
        
        
        Color clr_new_intensityAdapted = new Color(
        		Math.min(clr_new.getRed() * value / valueCalced, maxRBG),
        		Math.min(clr_new.getGreen() * value / valueCalced, maxRBG),
        		Math.min(clr_new.getBlue() * value / valueCalced, maxRBG),
        		clr_new.getAlpha());
        
        
        return clr_new_intensityAdapted.getRGB();
    
    }
    
    
    /**
     * Calculate random integer inside random distribution.
     * @param _expectancy the expectancy value
     * @param _variance the variance
     * @return random double value
     */
    private static double normalDistribution(final int _expectancy, 
            final int _variance) {
        double q = 0;
        double u1 = 0, u2;
        while (q == 0 || q > 1) {

            u1 = new Random().nextDouble();
            u2 = new Random().nextDouble();
            
            q = u1 * u1 + u2 * u2;
        }
        
        double p = Math.sqrt((-1 * 2 * Math.log(q)) / q);
        
        return u1 * p * _variance + _expectancy;
    }

}
