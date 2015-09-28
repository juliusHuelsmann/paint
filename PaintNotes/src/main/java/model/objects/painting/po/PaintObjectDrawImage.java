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
        final double cZoomFactorWidth = 1.0 
                * State.getImageSize().width
                / State.getImageShowSize().width;
        final double cZoomFactorHeight = 1.0 
                * State.getImageSize().height
                / State.getImageShowSize().height;
        super.getBi_image().setContent(Utils.resizeImage(
                (int) (_pnt_size.getX() * cZoomFactorWidth), 
                (int) (_pnt_size.getY() * cZoomFactorHeight), 
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

		double[][][] borderInfo2 = new double[bi_result.getWidth()][bi_result.getHeight()][2];

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
				System.out.println();
				
				if (Math.abs(borderInfo1[x][y][0]) >= Math.abs(borderInfo1[x + nX][y + nY][0])
						&& Math.abs(borderInfo1[x][y][0]) >= Math.abs(borderInfo1[x + nX][y + nY][0])) {
					borderInfo2[x][y][0] = borderInfo1[x][y][0];
					borderInfo2[x][y][1] = borderInfo1[x][y][1];
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
		
		
		BufferedViewer.show(bi_result);
		BufferedViewer.getInstance().setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
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
