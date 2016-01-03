//package declaration
package model.objects.painting.po;


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
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.Serializable;

import javax.swing.JFrame;

import start.test.BufferedViewer;
import model.objects.painting.PaintBI;
import model.objects.painting.Picture;
import model.settings.Error;
import model.settings.State;
import model.util.DPoint;
import model.util.DRect;
import model.util.SerializBufferedImage;
import model.util.Triple;
import model.util.adt.list.List;
import model.util.paint.Utils;


/**
 * PaintObject for images.
 *
 * @author Julius Huelsmann
 * @version %I%, %U%
 */
public class PaintObjectDrawImage extends PaintObjectBI implements Cloneable, Serializable {


    /**
     * Default serial version UID for being able to identify the list's 
     * version if saved to the disk and check whether it is possible to 
     * load it or whether important features have been added so that the
     * saved file is out-dated.
     */
    private static final long serialVersionUID = -3730582547146097485L;

    
    
    
    /**
     * Constructor.
     * 
     * @param _elementId the id of the element
     * @param _bi the bufferedImage which is displayed.
     * @param _picture the Picture which is saved.
     */
    public PaintObjectDrawImage(final int _elementId, final BufferedImage _bi, 
    		final Picture _picture) {
    	super(_elementId, _bi, _picture);
    }

    
    /**
     * {@inheritDoc}
     */
    @Override
    public final boolean isInSelectionImage(final Rectangle _r) {

        //if the image does not exist it is not inside the rectangle; thus 
        //return false
        if (getBi_image() == null) {
            
            return false;
        }
        
        //check whether the point does exist
        if (getPnt_locationOfImage() == null) {
            
            //print error message and interrupt immediately
            Error.printError(getClass().getSimpleName(), "isInSelection", 
                    "The BufferedImage is initialized, but its location is "
                    + "not.", new Exception("exception"), 
                    Error.ERROR_MESSAGE_INTERRUPT);
        }
        
        //the image does exist; check the bounds of the image
        return 
                
                //check the x coordinate and width
                (getPnt_locationOfImage().x <= _r.x + _r.width 
                && (getPnt_locationOfImage().x >= _r.x
                        || getPnt_locationOfImage().x + super.getBi_image().getWidth() >= _r.x))
                && (getPnt_locationOfImage().y <= _r.y + _r.height
                		&& (getPnt_locationOfImage().y >= _r.y
                	    || getPnt_locationOfImage().x
                	    + super.getBi_image().getHeight() >= _r.y));
    }


    
    
    /**
     * Move the BufferedImage to new coordinates.
     * @param _p the new coordinates
     */
    public final void move(final Point _p) {

        //check whether the point does exist
        if (_p == null) {
            
            //print error message and interrupt immediately
            Error.printError(getClass().getSimpleName(), "isInSelection", 
                    "Error moving PaintObject into nirvana (the band). ",
                    new Exception("exception"), 
                    Error.ERROR_MESSAGE_INTERRUPT);
        } else {
        	System.out.println("move from" + getPnt_locationOfImage());
            this.getPnt_locationOfImage().x += _p.x;
            this.getPnt_locationOfImage().y += _p.y;
            System.out.println("move to " + getPnt_locationOfImage());
        }
    }
    
    
    /**
     * {@inheritDoc}
     */
    @Override public final PaintObject[][] separate(final Rectangle _r) {
        
    	final PaintObject[][] po_return = new PaintObject[2][1];
        if (isInSelectionImage(_r)) {
        	
        	
        	/*
        	 * paint the section that is to be separated as white inside the 
        	 * image and generate a new POI into which the stuff out of the 
        	 * section is inserted.
        	 * 
        	 * Afterwards return the the two dimensional array which contains
        	 * 	@[0]	the content that is outside the selection (the current 
        	 * 			POI inserted into a new POI because the current POI 
        	 * 			is removed by default because that makes more sense 
        	 * 			if is coped with POWs and all kind of paintObjects
        	 * 			are treated in the same way for code-simplicity.
        	 *  @[1]	the content that is inside the selection (the new 
        	 *  		created POI)
        	 */
        	
    		//The white color is necessary because if the catch-color-tool 
        	// is used, the transparency color is to be fetched as white.
    		int[] newRGBA = new int[_r.width * _r.height];
    		final int maxRGB = 255;
    		int rgba = new Color(maxRGB, maxRGB, maxRGB, 0).getRGB();
    		for (int i = 0; i < newRGBA.length; i++) {
    			newRGBA[i] = rgba;
    		}
    		BufferedImage bi_new = new BufferedImage(
    				_r.width, _r.height, BufferedImage.TYPE_INT_ARGB);

    		int[] content = new int[_r.width * _r.height];
    		super.getBi_image().getRGB(0, 0, bi_new.getWidth(), bi_new.getHeight(),
    				content, 0, _r.width);
    		bi_new.setRGB(0, 0, bi_new.getWidth(), bi_new.getHeight(),
    				content, 0, _r.width);
    		super.getBi_image().getContent().setRGB(
    				_r.x - getPnt_locationOfImage().x, 
    				_r.y - getPnt_locationOfImage().y,
    				_r.width, _r.height, newRGBA, 0, _r.width);
    		
    		//return null because there is no new PaintObject created.

        	po_return[0][0] = getPicture().createPOI(super.getBi_image().getContent());
        	po_return[1][0] = getPicture().createPOI(bi_new);        	

            return po_return;
        } else {

        	po_return[0][0] = getPicture().createPOI(super.getBi_image().getContent());
        	po_return[1]  = new PaintObject[0];
            return po_return;
        }
    }



    
    /**
     * {@inheritDoc}
     */
    @Override
    public final PaintObject[][] separate(
    		final byte[][] _r, final Point _pnt_selectionShift) {

    	final PaintObjectDrawImage[][] p_ret = new PaintObjectDrawImage[2][1];

    	
    	final PaintObjectDrawImage poi_outside = getPicture().createPOI(
    			super.getBi_image().getContent());
    	
    	final BufferedImage bi_inside = new BufferedImage(
    			_r.length,
    			_r[0].length, BufferedImage.TYPE_INT_ARGB); 

    	//alpha value
    	final int alpha = new Color(255, 255, 255, 0).getRGB();

    	// remove the separated stuff from the BufferedImage that is outside
    	// and insert it into the new created BufferedImage.
    	for (int i = 0; i < bi_inside.getWidth(); i++) {
    		for (int j = 0; j < bi_inside.getHeight(); j++) {
    			
    			//if occupied
    			if (_r[i][j] != PaintBI.BORDER) {
    				
    				final int cX = i + _pnt_selectionShift.x, 
    						cY = j + _pnt_selectionShift.y;
    				
    				if (cX >= 0 && cX < super.getBi_image().getWidth()
    						&& cY >= 0 && cY < super.getBi_image().getHeight()) {

						
        				//set the RGB value to the new BufferedImage
        				bi_inside.setRGB(i, j, 
        						super.getBi_image().getContent().getRGB(cX, cY));
        				
        				//remove the RGB value from old BufferedImage.
        				super.getBi_image().getContent().setRGB(cX, cY, alpha);
    				}
    			}
        	}
    	}
    	
    	
    	final PaintObjectDrawImage poi_inside = getPicture().createPOI(bi_inside);
    	p_ret[0][0] = poi_outside;
    	p_ret[1][0] = poi_inside;
    	
    	System.out.println("NEu in bielefeld.");
        return p_ret;
    }


    /**
     * {@inheritDoc}
     */
    @Override public final BufferedImage getSnapshot() {

        return super.getBi_image().getContent();
    }

    
    /**
     * set the bufferedImage.
     * @param _bi the BI
     */
    public final void setImage(final BufferedImage _bi) {
    	if (super.getBi_image() != null) {

            super.getBi_image().setContent(_bi);
    	} else {
    		super.setBi_image(new SerializBufferedImage(_bi));
    	}
    }

    
    /**
     * {@inheritDoc}
     */
    @Override public final Rectangle getSnapshotBounds() {

        //if the image does not exist it is not inside the rectangle; thus 
        //return false
        if (super.getBi_image() == null || super.getBi_image().getContent() == null) {
            
            return new Rectangle(0, 0, 0, 0);
        }
        return new Rectangle(getPnt_locationOfImage().x, getPnt_locationOfImage().y,
                super.getBi_image().getWidth(), super.getBi_image().getHeight());
    }


    
    /**
     * {@inheritDoc}
     */
    @Override public final Rectangle getSnapshotSquareBounds() {

    	return new Rectangle(getPnt_locationOfImage().x, getPnt_locationOfImage().y,
    			Math.max(super.getBi_image().getWidth(), super.getBi_image().getHeight()),
    			Math.max(super.getBi_image().getWidth(), super.getBi_image().getHeight()));
    }


    
    /**
     * {@inheritDoc}
     */
    @Override public final synchronized void recalculateSnapshotBounds() {
    	
    	//TODO: das sollte noch gemacht werden.
    	//es muss darauf geachtt werden, dass, wenn das hier implementiert wird,
    	//die implementation auch in dem sinne sinnvolls ist.
    	//also nicht nur nach unten rechts schauen ob da alles mit alpha px voll
    	//ist, sondern auch an allen anderen kanten schauen (OL, OR, UL)
//    	final int maxRgb = 255;
//    	final int alpha = new Color(maxRgb, maxRgb, maxRgb, 0).getRGB();
//    	int checkedWidth = -1, checkedHeight = -1;
//    	
//    	for (int width = bi_image.getWidth(); width > 0; width--) {
//    		for (int height = bi_image.getHeight(); height > 0; height--) {
//    			
//        		checkedWidth = width;
//        		checkedHeight = height;
//        		if (bi_image.getContent().getRGB(width, height) != alpha) {
//        			
//        			//interrupt loop
//        			width = -1; 
//        			height = -1;
//        		}
//        	}
//    	}
//    	
//    	//here, the correct new width has been found.
//    	
//    	//if width is equal to 0, there is no image anymore.
//    	if (checkedWidth == 0) {
//    		bi_image = null;
//    		getPnt_locationOfImage() = new Point(0, 0);
//    		
//    	} else {
//
//    		//otherwise done because then the height is found.
//        	if (checkedHeight != bi_image.getHeight()) {
//        		for (int height = bi_image.getHeight(); height > 0; height--) {
//            		for (int width = bi_image.getWidth(); width > 0; width--) {
//            			
//                		checkedHeight = height;
//                		if (bi_image.getContent().getRGB(width, height)
//                				!= alpha) {
//                			
//                			//interrupt loop
//                			width = -1; 
//                			height = -1;
//                		}
//                	}
//            	}
//        	} 
//        	
//        	
//        	if (checkedHeight != bi_image.getHeight()
//        			|| checkedWidth != bi_image.getWidth()) {
//        		
//        	}
//        	
//    	}
    	
    	//found checkWidth 
//        new Exception(getClass() + "not implemented yet").printStackTrace();
    }


    /**
     * {@inheritDoc}
     */
    @Override public final void stretch(final DPoint _pnt_from, 
            final DPoint _pnt_totalStretch, final DPoint _pnt_size) {

        //print image to graphical user interface
        final double zoomFactor = State.getZoomFactorToModelSize();
        super.getBi_image().setContent(Utils.resizeImage(
                (int) (_pnt_size.getX() * zoomFactor), 
                (int) (_pnt_size.getY() * zoomFactor), 
                super.getBi_image().getContent()));
        setPnt_locationOfImage(new Point(
                (int) _pnt_from.getX(), (int) _pnt_from.getY()));
        
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public final boolean isInSelectionImage(
            final byte[][] _field, final Point _pnt_shiftRectangle) {

    	final int maxRgb = 255;
    	final int alpha = new Color(maxRgb, maxRgb, maxRgb, 0).getRGB();
    	if (
    			//check for x location
    			_pnt_shiftRectangle.x 
    			<= getPnt_locationOfImage().x + super.getBi_image().getWidth()
    			&& _pnt_shiftRectangle.x + _field.length
    			>= getPnt_locationOfImage().x
    			
    			//check for y location
    			&& _pnt_shiftRectangle.y
    			<= getPnt_locationOfImage().y + super.getBi_image().getHeight()
    			&& _pnt_shiftRectangle.y + _field[0].length
    			>= getPnt_locationOfImage().y) {
    		
        	for (int i = 0; i < _field.length; i++) {
        		for (int j = 0; j < _field[i].length; j++) {
        			
        			//if occupied
        			if (_field[i][j] != PaintBI.BORDER) {
        				
        				final int cX = i + _pnt_shiftRectangle.x, 
        						cY = j + _pnt_shiftRectangle.y;
        				
        				if (cX >= 0 && cX < super.getBi_image().getWidth()
        						&& cY >= 0 && cY < super.getBi_image().getHeight()) {

        					if (super.getBi_image().getContent().getRGB(cX, cY) != alpha) {
        						return true;
        					}
        				}
        			}
            	}
        	}
    	}
        return false;
    }


    /**
     * {@inheritDoc}
     */
	@Override
	public final List<PaintObjectWriting> deleteRectangle(
			final Rectangle _r, 
			final List<PaintObjectWriting> _ls_pow_outside) {

		
		//eliminate the image values out of specified area by replacing
		//them with (white) alpha values.
		//
		//The white color is necessary because if the catch-color-tool is used,
		//the transparency color is to be fetched as white.
		int[] newRGBA = new int[_r.width * _r.height];
		final int maxRGB = 255;
		int rgba = new Color(maxRGB, maxRGB, maxRGB, 0).getRGB();
		for (int i = 0; i < newRGBA.length; i++) {
			newRGBA[i] = rgba;
		}
		super.getBi_image().getContent().setRGB(
				_r.x - getPnt_locationOfImage().x, 
				_r.y - getPnt_locationOfImage().y,
				_r.width, _r.height, newRGBA, 0, _r.width);
		
		//return null because there is no new PaintObject created.
		return null;
	}


    /**
     * {@inheritDoc}
     */
	@Override
	public final List<PaintObjectWriting> deleteCurve(
			final byte[][] _r,
			final Point _pnt_shiftRectangle, 
			final DPoint _pnt_stretch, 
			final List<PaintObjectWriting> _l) {
		
		
		//TODO: neither used nor tested
		State.getLogger().severe("not tested implementation yet");
		final int maxRGB = 255;
		final int rgbAlpha = new Color(maxRGB, maxRGB, maxRGB, 0).getRGB();
		for (int xPx = _pnt_shiftRectangle.x; 
				xPx < _r.length * _pnt_stretch.getX();
				xPx++) {
			for (int yPx = _pnt_shiftRectangle.x; 
					yPx < _r.length * _pnt_stretch.getX();
					yPx++) {
				if (xPx >= 0 && xPx < super.getBi_image().getWidth()
						&& yPx >= 0 && yPx < super.getBi_image().getHeight()) {

					super.getBi_image().getContent().setRGB(xPx, yPx, rgbAlpha);
				} else {
					
					final String errorMsg = "POI: Delete curve error. "
							+ "PX out of range";

					if (xPx < 0) {

						State.getLogger().severe(errorMsg + "x < 0");
					} else if (xPx >= super.getBi_image().getWidth()) {

						State.getLogger().severe(errorMsg + "x >= widht");
					} else if (yPx < 0) {

						State.getLogger().severe(errorMsg + "y < 0");
					} else if (yPx >= super.getBi_image().getHeight()) {
						State.getLogger().severe(errorMsg + "y >= heights");
					}
				}
			}	
		}

        return null;
	}
	
	
	
	/**
	 * For editing scanned images.
	 */
	public final void editScans(final Object _o) {
		
		int thr1, thr2;
		double sat, val;
		if (_o == null) {
			thr1 = 8;
			thr2 = 200;
			sat = 1.5;
			val = 0.9;
		} else {

			double[] i = (double[]) (_o);
			thr1 = (int) i[0];
			thr2 = (int) i[1];
			sat = i[2];
			val = i[3];
		}
		
		// remove background noise
		justHysterisysThreshold(thr1, thr2);
		
		
		// intensify the resulting colors
		for (int i = 0; i < getBi_image().getWidth(); i++) {

			// intensify the resulting colors
			for (int j = 0; j < getBi_image().getHeight(); j++) {
				int rgb = getBi_image().getContent().getRGB(i, j);

				Color c2;
				Color clor = new Color(rgb);
				if (clor.getRed() == clor.getGreen() && clor.getGreen() == clor.getBlue() && clor.getRed() == 255) {

					c2 = new Color(255, 255, 255);
				
				} else {

					Color c = new Color(rgb);

					Triple hsv = rgbToHsv(new Triple(c.getRed(), c.getGreen(), c.getBlue()));
					double hue = hsv.getA();
					double saturation = Math.min(100, hsv.getB() *sat);
					double value = hsv.getC();
					if (hsv.getC() <= 90) {

						value = Math.min(100, hsv.getC() * val);
					}
					Triple rgb2 = hsvToRgb(new Triple(hue, saturation, value));
					
					c2 = new Color((int) rgb2.getA(), (int)  rgb2.getB(), (int) rgb2.getC());
					
				}
				
				getBi_image().getContent().setRGB(i, j, c2.getRGB());
			}
		}

		BufferedViewer.show(getBi_image().getContent());
		BufferedViewer.getInstance().setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		
	}
	
	
	
	/**
	 * Applies the hysteresis threshold to the contained BufferedImage.
	 * 
	 */
	public final void borderHysteresisThreshold(
			final int _lower,
			final int _upper) {
		
		BufferedImage bi_result = new BufferedImage(super.getBi_image().getWidth(),
				super.getBi_image().getHeight(), BufferedImage.TYPE_INT_RGB);
		
		double[][][] borderInfo1 = new double[bi_result.getWidth()][bi_result.getHeight()][2];
		
		/*
		 * Step 1: Detection of border pixel.
		 */
		
		for (int x = 1; x < super.getBi_image().getWidth() - 1; x++) {
			for (int y = 1; y < super.getBi_image().getHeight() - 1; y++) {
				
				//fold with 
				//
				//	-1/4	0	1/4
				//	-2/4	0	2/4
				//	-1/4	0	1/4
				int magnX = (
						-super.getBi_image().getGrayifiedValue(x - 1, y - 1) / 3 
						- super.getBi_image().getGrayifiedValue(x - 1, y) / 3 
						- super.getBi_image().getGrayifiedValue(x - 1, y + 1) / 3 

						+ super.getBi_image().getGrayifiedValue(x + 1, y - 1) / 3  
						+ super.getBi_image().getGrayifiedValue(x + 1, y) / 3
						+ super.getBi_image().getGrayifiedValue(x + 1, y + 1) / 3);
				
				

				int magnY = (
						-super.getBi_image().getGrayifiedValue(x - 1, y - 1) / 3 
						- super.getBi_image().getGrayifiedValue(x - 1, y) / 3 
						- super.getBi_image().getGrayifiedValue(x - 1, y + 1) / 3 

						+ super.getBi_image().getGrayifiedValue(x + 1, y - 1) / 3  
						+ super.getBi_image().getGrayifiedValue(x + 1, y) / 3
						+ super.getBi_image().getGrayifiedValue(x + 1, y + 1) / 3);

				
				
				//direction
				double direction;
				if (magnY != 0 && magnX != 0) {
					direction = Math.atan(magnX / magnY);
				} else if (magnY == 0) {
					direction = Math.atan(magnX / 0.01);
				} else {
					direction = Math.atan(0);
				}
				double magnitude = Math.sqrt(magnX * magnX + magnY * magnY);
				
				magnY = Math.abs(magnY);
				magnX = Math.abs(magnX);

				borderInfo1[x][y][0] = magnitude;
				borderInfo1[x][y][1] = direction;
			}
		}
		
		
		
		//non-maximum-suppression


		for (int x = 1; x < super.getBi_image().getWidth() - 1; x++) {
			for (int y = 1; y < super.getBi_image().getHeight() - 1; y++) {
				//nY
				// 
				// |alpha
				// |
				// --------->nX
				// arctan(nX / ny) = Winkel(alpha). gegenkathete / ankathete
				//find neighbors of the pixel (x, y) by direction:
				double direction = borderInfo1[x][y][1];

				int nX = 1;
				int nY = 1;
				if (direction < 0)
					nX = -1;
				else if (direction > 0)
					nX = 1;
				
				
				
				if (direction >= 0 && direction <= Math.PI / 4) {
					nX = 1;
					nY = 0;
				} else if (direction >=  Math.PI / 4 && direction <= Math.PI * 2 / 4) {
					nX = 1;
					nY = 1;
				} else if (direction >=  -Math.PI / 4 && direction <= 0) {
					nX = 0; 
					nY = 1;
				} else {
					
				}
				// - pi / 2 ; + pi / 2
//				System.out.println();
				
				if (Math.abs(borderInfo1[x][y][0]) >= Math.abs(borderInfo1[x + nX][y + nY][0])
						&& Math.abs(borderInfo1[x][y][0]) >= Math.abs(borderInfo1[x + nX][y + nY][0])) {
//					borderInfo2[x][y][0] = borderInfo1[x][y][0];
//					borderInfo2[x][y][1] = borderInfo1[x][y][1];
				}
			}
		}
		
		
		//hysteresis-threshold
		for (int x = 1; x < super.getBi_image().getWidth() - 1; x++) {
			for (int y = 1; y < super.getBi_image().getHeight() - 1; y++) {
				if (borderInfo1[x][y][0] >= _lower){
					bi_result.setRGB(x, y, rgb_potential);
				} else {
					bi_result.setRGB(x, y, rgb_noBorder);
				}
			}
		}

		for (int x = 1; x < super.getBi_image().getWidth() - 1; x++) {
			for (int y = 1; y < super.getBi_image().getHeight() - 1; y++) {
				if (borderInfo1[x][y][0] >= _upper){
					followEdge(x, y, bi_result);
				}
			}
		}
		for (int x = 1; x < super.getBi_image().getWidth() - 1; x++) {
			for (int y = 1; y < super.getBi_image().getHeight() - 1; y++) {
				if (bi_result.getRGB(x, y) == rgb_potential){
					bi_result.setRGB(x, y, rgb_noBorder);
				} else if (bi_result.getRGB(x, y) == rgb_border) {
					bi_result.setRGB(x,  y, super.getBi_image().getContent().getRGB(x, y));
				}
			}
		}
		
		
		getBi_image().setContent(bi_result);
		
	}
	
	public final void justHysterisysThreshold(
			final int _lower, final int _upper) {

		BufferedImage bi_result = new BufferedImage(super.getBi_image().getWidth(),
				super.getBi_image().getHeight(), BufferedImage.TYPE_INT_RGB);
		
		double[][][] borderInfo1 = new double[bi_result.getWidth()][bi_result.getHeight()][2];
		
		//hysteresis-threshold
		for (int x = 1; x < super.getBi_image().getWidth() - 1; x++) {
			for (int y = 1; y < super.getBi_image().getHeight() - 1; y++) {
				final Color c =  new Color(getBi_image().getContent().getRGB(x, y));
				borderInfo1[x][y][0] = c.getRed() + c.getGreen() + c.getBlue();
				if (borderInfo1[x][y][0] >= _lower){
					bi_result.setRGB(x, y, rgb_potential);
				} else {
					bi_result.setRGB(x, y, rgb_noBorder);
				}
			}
		}

		for (int x = 1; x < super.getBi_image().getWidth() - 1; x++) {
			for (int y = 1; y < super.getBi_image().getHeight() - 1; y++) {
				if (borderInfo1[x][y][0] >= _upper){
					followEdge(x, y, bi_result);
				}
			}
		}
		for (int x = 1; x < super.getBi_image().getWidth() - 1; x++) {
			for (int y = 1; y < super.getBi_image().getHeight() - 1; y++) {
				if (bi_result.getRGB(x, y) == rgb_potential){
					bi_result.setRGB(x, y, rgb_noBorder);
				} else if (bi_result.getRGB(x, y) == rgb_border) {
					bi_result.setRGB(x,  y, super.getBi_image().getContent().getRGB(x, y));
				}
			}
		}
		
		
		getBi_image().setContent(bi_result);
	}
	
	
	
	/**
	 * Invert the colors of the BufferedImage.
	 */
	public final void invertColors() {

		//hysteresis-threshold
		for (int x = 1; x < super.getBi_image().getWidth() - 1; x++) {
			for (int y = 1; y < super.getBi_image().getHeight() - 1; y++) {

				final Color c = new Color(getBi_image().getContent().getRGB(
						x, y));
				getBi_image().getContent().setRGB(x, y, new Color(
						255 - c.getRed(), 
						255 - c.getGreen(), 
						255 - c.getBlue()).getRGB());
			}
		}
	}
	
	/**
	 * The hue values for colors.
	 */
	private final int h_red = 0, h_yellow = 60, h_green = 120, 
			h_lightBlue = 180, h_darkBlue = 240, h_pink = 300;
	
	/**
	 * The standard deviation values for the given colors:
	 * 		{@link #h_red}, {@link #h_yellow}, {@link #h_green}, 
	 * 		{@link #h_lightBlue}, {@link #h_darkBlue}, {@link #h_pink}.
	 * 
	 */
	private final int sigma_red = 60, sigma_yellow = 60, sigma_green = 60,
			sigma_lightBlue = 60, sigma_darkBlue = 60, sigma_pink = 60;

	/*
	 * red
	 */

	/**
	 * Wrapper for {@link #convertColorToColor(int, int, int, int)}
	 * for converting red to yellow color.
	 */
	public final void convertRedToYelllow() {
		convertColorToColor(h_red, sigma_red, h_yellow, sigma_yellow);
	}
	

	/**
	 * Wrapper for {@link #convertColorToColor(int, int, int, int)}
	 * for converting red to green color.
	 */
	public final void convertRedToGreen() {
		convertColorToColor(h_red, sigma_red, h_green, sigma_green);
	}
	

	/**
	 * Wrapper for {@link #convertColorToColor(int, int, int, int)}
	 * for converting red to lightblue color.
	 */
	public final void convertRedToLightBlue() {
		convertColorToColor(h_red, sigma_red, h_lightBlue, sigma_lightBlue);
	}
	

	/**
	 * Wrapper for {@link #convertColorToColor(int, int, int, int)}
	 * for converting red to dark blue color.
	 */
	public final void convertRedToDarkBlue() {
		convertColorToColor(h_red, sigma_red, h_darkBlue, sigma_darkBlue);
	}
	

	/**
	 * Wrapper for {@link #convertColorToColor(int, int, int, int)}
	 * for converting red to pink color.
	 */
	public final void convertRedToPink() {
		convertColorToColor(h_red, sigma_red, h_pink, sigma_pink);
	}

	/*
	 * yellow
	 */

	/**
	 * Wrapper for {@link #convertColorToColor(int, int, int, int)}
	 * for converting red to yellow color.
	 */
	public final void convertYellowToRed() {
		convertColorToColor(h_yellow, sigma_yellow, h_yellow, sigma_yellow);
	}
	

	/**
	 * Wrapper for {@link #convertColorToColor(int, int, int, int)}
	 * for converting Yellow to green color.
	 */
	public final void convertYellowToGreen() {
		convertColorToColor(h_yellow, sigma_yellow, h_green, sigma_green);
	}
	

	/**
	 * Wrapper for {@link #convertColorToColor(int, int, int, int)}
	 * for converting Yellow to lightblue color.
	 */
	public final void convertYellowToLightBlue() {
		convertColorToColor(h_yellow, sigma_yellow, h_lightBlue, sigma_lightBlue);
	}
	

	/**
	 * Wrapper for {@link #convertColorToColor(int, int, int, int)}
	 * for converting Yellow to dark blue color.
	 */
	public final void convertYellowToDarkBlue() {
		convertColorToColor(h_yellow, sigma_yellow, h_darkBlue, sigma_darkBlue);
	}
	

	/**
	 * Wrapper for {@link #convertColorToColor(int, int, int, int)}
	 * for converting Yellow to pink color.
	 */
	public final void convertYellowToPink() {
		convertColorToColor(h_yellow, sigma_yellow, h_pink, sigma_pink);
	}
	
	/*
	 * green
	 */

	/**
	 * Wrapper for {@link #convertColorToColor(int, int, int, int)}
	 * for converting Yellow to yellow color.
	 */
	public final void convertGreenToYelllow() {
		convertColorToColor(h_green, sigma_green, h_yellow, sigma_yellow);
	}
	

	/**
	 * Wrapper for {@link #convertColorToColor(int, int, int, int)}
	 * for converting Green to green color.
	 */
	public final void convertGreenToRed() {
		convertColorToColor(h_green, sigma_green, h_red, sigma_red);
	}
	

	/**
	 * Wrapper for {@link #convertColorToColor(int, int, int, int)}
	 * for converting Green to lightblue color.
	 */
	public final void convertGreenToLightBlue() {
		convertColorToColor(h_green, sigma_green, h_lightBlue, sigma_lightBlue);
	}
	

	/**
	 * Wrapper for {@link #convertColorToColor(int, int, int, int)}
	 * for converting Green to dark blue color.
	 */
	public final void convertGreenToDarkBlue() {
		convertColorToColor(h_green, sigma_green, h_darkBlue, sigma_darkBlue);
	}
	

	/**
	 * Wrapper for {@link #convertColorToColor(int, int, int, int)}
	 * for converting Green to pink color.
	 */
	public final void convertGreenToPink() {
		convertColorToColor(h_green, sigma_green, h_pink, sigma_pink);
	}
	
	/*
	 * lightBlue
	 */

	/**
	 * Wrapper for {@link #convertColorToColor(int, int, int, int)}
	 * for converting lightBlue to yellow color.
	 */
	public final void convertLightBlueToYelllow() {
		convertColorToColor(h_lightBlue, sigma_lightBlue, h_yellow, sigma_yellow);
	}
	

	/**
	 * Wrapper for {@link #convertColorToColor(int, int, int, int)}
	 * for converting lightBlue to green color.
	 */
	public final void convertLightBlueToGreen() {
		convertColorToColor(h_lightBlue, sigma_lightBlue, h_green, sigma_green);
	}
	

	/**
	 * Wrapper for {@link #convertColorToColor(int, int, int, int)}
	 * for converting lightBlue to lightblue color.
	 */
	public final void convertLightBlueToRed() {
		convertColorToColor(h_lightBlue, sigma_lightBlue, h_red, sigma_red);
	}
	

	/**
	 * Wrapper for {@link #convertColorToColor(int, int, int, int)}
	 * for converting lightBlue to dark blue color.
	 */
	public final void convertLightBlueToDarkBlue() {
		convertColorToColor(h_lightBlue, sigma_lightBlue, h_darkBlue, sigma_darkBlue);
	}
	

	/**
	 * Wrapper for {@link #convertColorToColor(int, int, int, int)}
	 * for converting lightBlue to pink color.
	 */
	public final void convertLightBlueToPink() {
		convertColorToColor(h_lightBlue, sigma_lightBlue, h_pink, sigma_pink);
	}
	
	/*
	 * darkBlue
	 */

	/**
	 * Wrapper for {@link #convertColorToColor(int, int, int, int)}
	 * for converting darkBlue to yellow color.
	 */
	public final void convertDarkBlueToYelllow() {
		convertColorToColor(h_darkBlue, sigma_darkBlue, h_yellow, sigma_yellow);
	}
	

	/**
	 * Wrapper for {@link #convertColorToColor(int, int, int, int)}
	 * for converting darkBlue to green color.
	 */
	public final void convertDarkBlueToGreen() {
		convertColorToColor(h_darkBlue, sigma_darkBlue, h_green, sigma_green);
	}
	

	/**
	 * Wrapper for {@link #convertColorToColor(int, int, int, int)}
	 * for converting darkBlue to lightblue color.
	 */
	public final void convertDarkBlueToLightBlue() {
		convertColorToColor(h_darkBlue, sigma_darkBlue, h_lightBlue, sigma_lightBlue);
	}
	

	/**
	 * Wrapper for {@link #convertColorToColor(int, int, int, int)}
	 * for converting darkBlue to dark blue color.
	 */
	public final void convertDarkBlueToRed() {
		convertColorToColor(h_darkBlue, sigma_darkBlue, h_red, sigma_red);
	}
	

	/**
	 * Wrapper for {@link #convertColorToColor(int, int, int, int)}
	 * for converting darkBlue to pink color.
	 */
	public final void convertDarkBlueToPink() {
		convertColorToColor(h_darkBlue, sigma_darkBlue, h_pink, sigma_pink);
	}
	
	/*
	 * pink
	 */

	/**
	 * Wrapper for {@link #convertColorToColor(int, int, int, int)}
	 * for converting pink to yellow color.
	 */
	public final void convertPinkToYelllow() {
		convertColorToColor(h_pink, sigma_pink, h_yellow, sigma_yellow);
	}
	

	/**
	 * Wrapper for {@link #convertColorToColor(int, int, int, int)}
	 * for converting pink to green color.
	 */
	public final void convertPinkToGreen() {
		convertColorToColor(h_pink, sigma_pink, h_green, sigma_green);
	}
	

	/**
	 * Wrapper for {@link #convertColorToColor(int, int, int, int)}
	 * for converting pink to lightblue color.
	 */
	public final void convertPinkToLightBlue() {
		convertColorToColor(h_pink, sigma_pink, h_lightBlue, sigma_lightBlue);
	}
	

	/**
	 * Wrapper for {@link #convertColorToColor(int, int, int, int)}
	 * for converting pink to dark blue color.
	 */
	public final void convertPinkToDarkBlue() {
		convertColorToColor(h_pink, sigma_pink, h_darkBlue, sigma_darkBlue);
	}
	

	/**
	 * Wrapper for {@link #convertColorToColor(int, int, int, int)}
	 * for converting pink to pink color.
	 */
	public final void convertPinkToRed() {
		convertColorToColor(h_pink, sigma_pink, h_red, sigma_red);
	}
	
	

	/**
	 * Convert pixel of first color with special range to a second color.
	 * If the hue value h(x, y) of the pixel located at (x, y) is inside the 
	 * legal range <code>_hue +- _hueSigma</code>, he percentage of the deviation
	 * 							 <code>h(x, y) - _hue</code> 
	 * is taken into account at the resulting value.
	 * 
	 * @param _hue			the hue value of the color that is to be changed.
	 * @param _hue2			the hue value of the color that replaces the old 
	 * 						one.
	 * @param _hueSigma		the standard deviation of the #_hue.
	 * @param _hue2Sigma	the standard deviation of the #_hue2.
	 */
	public final void convertColorToColor(
			final int _hue, final int _hueSigma,
			final int _hue2, final int _hue2Sigma) {

		//hysteresis-threshold
		for (int x = 1; x < super.getBi_image().getWidth() - 1; x++) {
			for (int y = 1; y < super.getBi_image().getHeight() - 1; y++) {

				final Color c = new Color(getBi_image().getContent().getRGB(
						x, y));
				
				final Triple rgb = new Triple(
						c.getRed(), c.getGreen(), c.getBlue());
				final Triple hsv = rgbToHsv(rgb);
				
				// if the current pixel is a red pixel.
				final double currentHue = hsv.getA();
				if (Math.abs(currentHue - _hue) <= _hueSigma) {
					
					// the percentage may be negative; is element of [-1,1]
					final double perc = 1.0 * (currentHue - _hue) / _hueSigma;
					hsv.setA(_hue2 + perc * (_hue2 - _hue2Sigma));
				}
				
				final Triple resultingRGB = hsvToRgb(hsv);
				
				getBi_image().getContent().setRGB(x, y, new Color(
						(int) resultingRGB.getA(),
						(int) resultingRGB.getB(),
						(int) resultingRGB.getC()).getRGB());
			}
		}
	}

	/**
	 * Wrapper for {@link #grayifyColor(int, int)}.
	 */
	public void grayifyRed() {
		grayifyColor(h_red, sigma_red);
	}
	

	/**
	 * Wrapper for {@link #grayifyColor(int, int)}.
	 */
	public final void grayifyYellow() {
		grayifyColor(h_yellow, sigma_yellow);
	}
	

	/**
	 * Wrapper for {@link #grayifyColor(int, int)}.
	 */
	public final void grayifyGreen() {
		grayifyColor(h_green, sigma_green);
	}
	

	/**
	 * Wrapper for {@link #grayifyColor(int, int)}.
	 */
	public final void grayifyLightBlue() {
		grayifyColor(h_lightBlue, sigma_lightBlue);
	}
	

	/**
	 * Wrapper for {@link #grayifyColor(int, int)}.
	 */
	public final void grayifyDarkBlue() {
		grayifyColor(h_darkBlue, sigma_darkBlue);
	}
	

	/**
	 * Wrapper for {@link #grayifyColor(int, int)}.
	 */
	public final void grayifyPink() {
		grayifyColor(h_pink, sigma_pink);
	}

	
	
	/**
	 * Convert pixel of first color with special range to a second color.
	 * If the hue value h(x, y) of the pixel located at (x, y) is inside the 
	 * legal range <code>_hue +- _hueSigma</code>, he percentage of the deviation
	 * 							 <code>h(x, y) - _hue</code> 
	 * is taken into account at the resulting value.
	 * 
	 * @param _hue			the hue value of the color that is to be changed.
	 * @param _hue2			the hue value of the color that replaces the old 
	 * 						one.
	 * @param _hueSigma		the standard deviation of the #_hue.
	 * @param _hue2Sigma	the standard deviation of the #_hue2.
	 */
	public final void grayifyColor(
			final int _hue, final int _hueSigma) {

		//hysteresis-threshold
		for (int x = 1; x < super.getBi_image().getWidth() - 1; x++) {
			for (int y = 1; y < super.getBi_image().getHeight() - 1; y++) {

				final Color c = new Color(getBi_image().getContent().getRGB(
						x, y));
				
				final Triple rgb = new Triple(
						c.getRed(), c.getGreen(), c.getBlue());
				final Triple hsv = rgbToHsv(rgb);
				
				// if the current pixel is a red pixel.
				final double currentHue = hsv.getA();
				if (Math.abs(currentHue - _hue) <= _hueSigma) {
					
					//set saturation to zero.
					hsv.setB(0);
				}
				
				final Triple resultingRGB = hsvToRgb(hsv);
				
				getBi_image().getContent().setRGB(x, y, new Color(
						(int) resultingRGB.getA(),
						(int) resultingRGB.getB(),
						(int) resultingRGB.getC()).getRGB());
			}
		}
	}
	
	
	
	
	

	/**
	 * Convert RGB-triple to HSV-triple.
	 * @param _rgb 	the RGB triple
	 * @return 		the HSV triple
	 */
	public static Triple rgbToHsv(final Triple _rgb) {

		final Triple hsv;
		final double r = 1.0 * _rgb.getA() / 255.0;
		final double g = 1.0 * _rgb.getB() / 255.0;
		final double b = 1.0 * _rgb.getC() / 255.0;

		double h = 0, s = 0, v = 0;

		double min = Math.min(Math.min(r, g), b);
		double max = Math.max(Math.max(r, g), b);

		/*
		 * Computation of hue. Hue is type of the color that displayed
		 * (red, green, blue, pink etc.)
		 * It is dependent on the relation between the different colors
		 */
	
		// if _r == g == _b the h value is undefined. Thus, if color is a shade of
		// gray (white, gray, black) it does not matter which hue to take; by 
		// convention it is set to be zero.
		if (max == min) {
			h = 0;
		} 

		// if there is a color to be computed:
		else {
			if (r == max) {
				h = (
						// if the greatest value is red we start at the angle zero
						// because the color will be in red range
						0.0

						// difference between other colors:
						// if the green value is greater than the blue value,
						// we have to go counterclockwise, otherwise clockwise.
						// (_g - b)  <= (max - min)
						// (_g - _b) >= (min - max)
						+ (g - b) * 1.0

						// divided by the maximal difference between colors (between
						// r and min)
						/ (max - min)

						)
						// (...) in [-1,1]. Multiplied by 60 it takes charge of one
						// third of the color - circle. 
						* 60.0;
			} else if (g == max) {
				// here we start at 120 and do the 'same' as above
				// in [60 (= 120-60), 180]
				h = 120.0 + (b - r) * 1.0 / (max - min) * 60.0;
			} // blue value
			else if (b == max) {
		
				// in [180, 300]
				h = 240.0 + (r - g) * 1.0/ (max - min) * 60;
			}
		}

		// if we go counterclockwise we have to re-transform the h into an angle
		// thus we regard h as 5 modulo 360 positive.
		if (h < 0) {
			h = h + 360;
		}


		/*
		 * Computation of saturation (how much color there is)
		 */
	
		// if MAX == 0 (== MIN) the displayed color is black. We do not have to
		// worry about s, but by convention it is defined to be 0
		// if MAX == MIN == 255, the displayed color is white and we do not have to
		// worry about s either, but the following computation formula is well
		// defined
		if (max == 0) {
			s = 0;
		} 
		
		else {
			//difference between MAX and MIN divided by max
			s = 100.0 * (max - min) / max;
		}


		/*
		 * computation of brightness value:
		 */	
		v = 100 * max;
	
	
		/*
		 * Write the result into the content of the pointer.
		 */
		hsv = new Triple(h, s, v);


		return hsv;
	}
	
	
	
	public static void testrgbconversion(String[]args) {
		System.out.println("hier");
		Triple start = new Triple(10, 100, 10);
		System.out.println(start.getA() + ".." + start.getB() + ".." + start.getC());
		
		Triple result = rgbToHsv(start);
		System.out.println(result.getA() + ".." + result.getB() + ".." + result.getC());

		Triple result2 = hsvToRgb(result);
		System.out.println("final " + result2.getA() + ".." + result2.getB() + ".." + result2.getC());

//		Triple start2 = new Triple(125, 99, 39.2);
//		Triple result23 = hsvToRgb(start2);
//		System.out.println(result23.getA() + ".." + result23.getB() + ".." + result23.getC());
	}

	/**
	 * Convert HSV triple to RGB triple.
	 * @param _hsv	the HSV triple
	 * @return		the RGB triple
	 */
	public static Triple hsvToRgb(final Triple _hsv) {
	
		//fetch HSV values from triple & initialize RGB values
		double h = _hsv.getA();
		double s = _hsv.getB();
		double v = _hsv.getC();
		
		double r = 0, g = 0, b = 0;
		
		v =  v / 100.0;
		s = s / 100.0;
	
	
		// gray
		if (s == 0) {
			r = v; 
			g = v;
			b = v;
		} 
		
		
		//color
		else {
			int i;
			double f, p, q, t;
		
			//i indicates the image sector. There are 6 sectors from 0 to 360 degree
			//TODO: e.g. use simple conversion to interger instead of round.
			i = (int) (1.0 * Math.floor( h  / 60.0));
			
			//factorial part of h (degree inside sector)
			f = 1.0 * h / 60.0 - i;			
			
			//value divided by saturatioin
			p = 1.0 * v * ( 1.0 - s ) ;
			q = 1.0 * v * ( 1.0 - s * f ) ;
			t = 1.0 * v * ( 1.0 - s * ( 1 - f ) ) ;
			
			// oder 
	
			//printf("%f %i %f %f %f %f  ", h, i, f, p, q, t);
			switch( i ) {
				case 0:
					r = v;
					g = t;
					b = p;
					break;
				case 1:
					r = q;
					g = v;
					b = p;
					break;
				case 2:
					r = p;
					g = v;
					b = t;
					break;
				case 3:
					r = p;
					g = q;
					b = v;
					break;
				case 4:
					r = t;
					g = p;
					b = v;
					break;
				default:		// case 5:
					r = v;
					g = p;
					b = q;
					break;
			}
		}
		
		
		r = r * 255.0;
		g *= 255;
		b *= 255;
		
		return new Triple(r, g, b);
	
	}
	
	

	
	
	
	public final BufferedImage hysteresisThreshold(
			final int _lower,
			final int _upper) {

		BufferedImage bi_result = new BufferedImage(super.getBi_image().getWidth(),
				super.getBi_image().getHeight(), BufferedImage.TYPE_INT_RGB);
		
		//hysteresis-threshold
		for (int x = 1; x < super.getBi_image().getWidth() - 1; x++) {
			for (int y = 1; y < super.getBi_image().getHeight() - 1; y++) {
				if (-super.getBi_image().getGrayifiedValue(x, y) >= -_lower){
					bi_result.setRGB(x, y, rgb_potential);
				} else {
					bi_result.setRGB(x, y, rgb_noBorder);
				}
			}
		}

		for (int x = 1; x < super.getBi_image().getWidth() - 1; x++) {
			for (int y = 1; y < super.getBi_image().getHeight() - 1; y++) {
				if (-super.getBi_image().getGrayifiedValue(x, y) >= -_upper){
					followEdge(x, y, bi_result);
				}
			}
		}
		for (int x = 1; x < super.getBi_image().getWidth() - 1; x++) {
			for (int y = 1; y < super.getBi_image().getHeight() - 1; y++) {
				if (bi_result.getRGB(x, y) == rgb_potential){
					bi_result.setRGB(x, y, rgb_noBorder);
				} else if (bi_result.getRGB(x, y) == rgb_border) {
					bi_result.setRGB(x,  y, super.getBi_image().getContent().getRGB(x, y));
				}
			}
		}
		
		return bi_result;
		
	}

	private final int rgb_border = new Color(0, 0, 0).getRGB(),
			rgb_potential = new Color(255, 0, 0).getRGB(),
			rgb_noBorder = new Color(255, 255, 255).getRGB();
	
	
	private void followEdge(final int _x, final int _y, final BufferedImage _bi) {

		_bi.setRGB(_x, _y, rgb_border);
		
		for (int dX = 1; dX <= 1; dX++) {
			for (int dY = 1; dY <= 1; dY++) {
				if (!(dX == dY && dY == 1)) {
					int x = _x + dX; 
					int y = _y + dY;
					
					if (x >= 0 && y >= 0 && x < _bi.getWidth() && y < _bi.getHeight()) {
						if (_bi.getRGB(x, y) == rgb_potential) {
							followEdge(x, y, _bi);
						}
					}
				}
			}
		}
	}
	
	
	public static void rhein(String [] args) {
		final String path = "/home/juli/"
				+ "";
		final String fileName = "";
		final String fileExtension = ".jpg";
		BufferedImage bis = Utils.normalResizeImageFromOutside(
				2500, 3300, path + fileName + fileExtension);
		Picture.saveBufferedImage(path + fileName + ".", new PaintObjectDrawImage(
		//BufferedViewer.show(bis);
				0, bis, null).hysteresisThreshold(20, 140));
		
	}


	@Override
	public boolean isEditable() {
		return true;
	}

	
	
	
}
