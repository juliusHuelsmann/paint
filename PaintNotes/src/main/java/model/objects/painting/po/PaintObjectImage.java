//package declaration
package model.objects.painting.po;

//import declarations
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

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
public class PaintObjectImage extends PaintObject implements Cloneable {


    /**
     * Default serial version UID for being able to identify the list's 
     * version if saved to the disk and check whether it is possible to 
     * load it or whether important features have been added so that the
     * saved file is out-dated.
     */
    private static final long serialVersionUID = -3730582547146097485L;

    
    /**
     * The BufferedImage.
     */
    private SerializBufferedImage bi_image;
    
    
    /**
     * The location of the BufferdImage in picture.
     */
    private Point pnt_locationOfImage;
    
    
    /**
     * Constructor.
     * 
     * @param _elementId the id of the element
     * @param _bi the bufferedImage which is displayed.
     * @param _picture the Picture which is saved.
     */
    public PaintObjectImage(final int _elementId, final BufferedImage _bi, 
    		final Picture _picture) {
        super(_picture, _elementId);
        this.bi_image = new SerializBufferedImage(_bi);
        this.pnt_locationOfImage = new Point(0, 0);
    }

    
    /**
     * {@inheritDoc}
     */
    @Override
    public final boolean isInSelectionImage(final Rectangle _r) {

        //if the image does not exist it is not inside the rectangle; thus 
        //return false
        if (bi_image == null) {
            
            return false;
        }
        
        //check whether the point does exist
        if (pnt_locationOfImage == null) {
            
            //print error message and interrupt immediately
            Error.printError(getClass().getSimpleName(), "isInSelection", 
                    "The BufferedImage is initialized, but its location is "
                    + "not.", new Exception("exception"), 
                    Error.ERROR_MESSAGE_INTERRUPT);
        }
        
        //the image does exist; check the bounds of the image
        return 
                
                //check the x coordinate and width
                (pnt_locationOfImage.x <= _r.x + _r.width 
                && (pnt_locationOfImage.x >= _r.x
                        || pnt_locationOfImage.x + bi_image.getWidth() >= _r.x))
                && (pnt_locationOfImage.y <= _r.y + _r.height
                		&& (pnt_locationOfImage.y >= _r.y
                	    || pnt_locationOfImage.x
                	    + bi_image.getHeight() >= _r.y));
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
        	System.out.println("move from" + pnt_locationOfImage);
            this.pnt_locationOfImage.x += _p.x;
            this.pnt_locationOfImage.y += _p.y;
            System.out.println("move to " + pnt_locationOfImage);
        }
    }
    /**
     * {@inheritDoc}
     */
    @Override public final BufferedImage paint(
    		final BufferedImage _bi, 
    		final boolean _final, 
    		final BufferedImage _g, 
    		final int _x, final int _y, 
    		final DRect _r) {


        
    	
    	// _x, _y ^			scroll location (dependent of the current zoom size)
    	// -> transform to image size

        //if final only paint at BufferedImage
        //if not final only paint at graphics
        if (_final) {
            //print image to BufferedImage.
            int[] rgb = null;
            _bi.setRGB(pnt_locationOfImage.x + _x, pnt_locationOfImage.y + _y, 
                    bi_image.getWidth(), bi_image.getHeight(), 
                    bi_image.getRGB(0, 0, bi_image.getWidth(), 
                            bi_image.getHeight(), rgb, 0, bi_image.getWidth()),
                            0, bi_image.getWidth());
        } else {


            // interrupt if the given values are illegal if the size of 
        	// the area which is to be repainted is equal to zero.
        	final DRect r;
        	if (_r == null) {
        		System.out.println(getClass() + "r0");
        		r = new DRect(
        				0, 0,
        				bi_image.getWidth(), bi_image.getHeight());
        	} else {
        		r = _r;
        	}
        	
            if (r == null || r.getHeight() <= 0 || r.getWidth() <= 0) {
            	return _bi;
            }

        	
        	//fetch the zoom factors for stretching the image if necessary.
            final double cZoomFactorWidth = 1.0 
                    * State.getImageShowSize().width
                    / State.getImageSize().width;
            final double cZoomFactorHeight = 1.0 
                    * State.getImageShowSize().height
                    / State.getImageSize().height;
            
            
            // TODO: this seems to be due to rounding (while method call).
            if (cZoomFactorWidth > 1) {
            	r.setWidth(r.getWidth() + cZoomFactorWidth);
            	r.setX(r.getX() - cZoomFactorWidth / 2);
            }if (cZoomFactorHeight > 1) {
            	r.setHeight(r.getHeight() + cZoomFactorHeight);
            	r.setY(r.getY() - cZoomFactorHeight / 2);
            }
            
            // these values contain the location of the selection inside 
            // the small page image 
            int locXSmallPageImage = (int) ((r.getX()) 
            		* cZoomFactorWidth) + _x;
            int locYSmallPageImage = ((int) ((r.getY())
            		* cZoomFactorHeight)) + _y;
            
            locXSmallPageImage = Math.max(0, locXSmallPageImage);
            locYSmallPageImage = Math.max(0, locYSmallPageImage);

            
            int locXPOI = r.getIX() - pnt_locationOfImage.x;
            int locYPOI = r.getIY() - pnt_locationOfImage.y;
            if (locXPOI < 0) {

            	r.setWidth(r.getWidth() + locXPOI);
                locXSmallPageImage -= locXPOI * cZoomFactorWidth;

            	locXPOI = 0;
                
            }if (locYPOI < 0) {
            	r.setWidth(r.getHeight() + locYPOI);
                locYSmallPageImage -= locYPOI * cZoomFactorHeight;
            	locYPOI = 0;
            }
            
            


            // adapt the width of the selection to the size of the 
            // paint-object-image.
            // 
            // TODO: Find a better solution?
            // to the normal width, 2 * zoom-factor is added 
            // because if zoomed in, there are gaps between the painted
            // sub-images.
            r.setWidth(Math.min(
            		bi_image.getWidth() - locXPOI,
            		r.getIWidth()));
            r.setHeight(Math.min(
            		bi_image.getHeight() - locYPOI,
            		r.getIHeight()));
            
            // interrupt if the given values are illegal if the size of 
        	// the area which is to be repainted is equal to zero.
            if (r == null || r.getIHeight() <= 0 || r.getIWidth() <= 0) {
            	return _bi;
            }
            
            /* 
             * now a sub-image containing the newly painted stuff is created
             * and filled with the image values
             */
            
            // create sub-BufferedImage for the selection and fill it with
            // the pixel from image
            BufferedImage bi_section = new BufferedImage(
            		r.getIWidth(), r.getIHeight(), 
            		BufferedImage.TYPE_INT_ARGB);
            
            // for filling the BufferedImage, the RGB-alpha values are written 
            // into an integer array
            int[] rgbA = new int[r.getIHeight() * r.getIWidth()];
            if (
            		locXPOI >= 0 
            		&& locYPOI >= 0
            		&& locXPOI + r.getIWidth() <= bi_image.getWidth()
            		&& locYPOI + r.getIHeight() <= bi_image.getHeight()) {

                rgbA = bi_image.getRGB(
                		locXPOI, locYPOI, 
                		r.getIWidth(), r.getIHeight(), 
                		rgbA,
                		0, 
                		r.getIWidth());
            } else {

            	State.getLogger().severe("fatal move error: \n" 
            			+ "Section out of scope."
            			+ "x\t" + locXPOI + "\n"
            			+ "y\t" + locYPOI + "\n"
            			+ "x+w\t" + locXPOI + r.getWidth() + "\n"
            			+ "y+h\t" + locYPOI + r.getHeight() + "\n"
            			+ "\n@image\n"
            			+ "width:\t" + bi_image.getWidth()
            			+ "\nheight:\t" + bi_image.getHeight());
            	return bi_image.getContent();
            }
            
            // write the RGB-Alpha values from the integer array to the 
            // section-BufferedImage.
            bi_section.setRGB(
            		0, 0, 
            		r.getIWidth(), 
            		r.getIHeight(),
            		rgbA, 0, r.getIWidth());
            

            final Graphics g = _g.getGraphics();
            int newWidth = (int) Math.max(1, (r.getWidth()) * cZoomFactorWidth);
            int newHeight = (int) Math.max(1, (r.getHeight()) * cZoomFactorHeight);
            g.drawImage(Utils.resizeImageQuick(
                    newWidth, 
                    newHeight , 
                    bi_section),
                    (int) (locXSmallPageImage), 
                    (int) (locYSmallPageImage), 
                    newWidth,
                    newHeight, null);

            return _g;
        }
        
        return bi_image.getContent();
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
    		bi_image.getRGB(0, 0, bi_new.getWidth(), bi_new.getHeight(),
    				content, 0, _r.width);
    		bi_new.setRGB(0, 0, bi_new.getWidth(), bi_new.getHeight(),
    				content, 0, _r.width);
    		bi_image.getContent().setRGB(
    				_r.x - pnt_locationOfImage.x, 
    				_r.y - pnt_locationOfImage.y,
    				_r.width, _r.height, newRGBA, 0, _r.width);
    		
    		//return null because there is no new PaintObject created.

        	po_return[0][0] = getPicture().createPOI(bi_image.getContent());
        	po_return[1][0] = getPicture().createPOI(bi_new);        	

            return po_return;
        } else {

        	po_return[0][0] = getPicture().createPOI(bi_image.getContent());
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

    	final PaintObjectImage[][] p_ret = new PaintObjectImage[2][1];

    	
    	final PaintObjectImage poi_outside = getPicture().createPOI(
    			bi_image.getContent());
    	
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
    				
    				if (cX >= 0 && cX < bi_image.getWidth()
    						&& cY >= 0 && cY < bi_image.getHeight()) {

						
        				//set the RGB value to the new BufferedImage
        				bi_inside.setRGB(i, j, 
        						bi_image.getContent().getRGB(cX, cY));
        				
        				//remove the RGB value from old BufferedImage.
        				bi_image.getContent().setRGB(cX, cY, alpha);
    				}
    			}
        	}
    	}
    	
    	
    	final PaintObjectImage poi_inside = getPicture().createPOI(bi_inside);
    	p_ret[0][0] = poi_outside;
    	p_ret[1][0] = poi_inside;
    	
    	System.out.println("NEu in bielefeld.");
        return p_ret;
    }


    /**
     * {@inheritDoc}
     */
    @Override public final BufferedImage getSnapshot() {

        return bi_image.getContent();
    }

    
    /**
     * set the bufferedImage.
     * @param _bi the BI
     */
    public final void setImage(final BufferedImage _bi) {
    	if (bi_image != null) {

            this.bi_image.setContent(_bi);
    	} else {
    		bi_image = new SerializBufferedImage(_bi);
    	}
    }

    
    /**
     * {@inheritDoc}
     */
    @Override public final Rectangle getSnapshotBounds() {

        //if the image does not exist it is not inside the rectangle; thus 
        //return false
        if (bi_image == null) {
            
            return new Rectangle(0, 0, 0, 0);
        }
        return new Rectangle(pnt_locationOfImage.x, pnt_locationOfImage.y,
                bi_image.getWidth(), bi_image.getHeight());
    }


    
    /**
     * {@inheritDoc}
     */
    @Override public final Rectangle getSnapshotSquareBounds() {

    	return new Rectangle(pnt_locationOfImage.x, pnt_locationOfImage.y,
    			Math.max(bi_image.getWidth(), bi_image.getHeight()),
    			Math.max(bi_image.getWidth(), bi_image.getHeight()));
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
//    		pnt_locationOfImage = new Point(0, 0);
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
        bi_image.setContent(Utils.resizeImage(
                (int) (_pnt_size.getX() * cZoomFactorWidth), 
                (int) (_pnt_size.getY() * cZoomFactorHeight), 
                bi_image.getContent()));
        pnt_locationOfImage = new Point(
                (int) _pnt_from.getX(), (int) _pnt_from.getY());
        
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
    			<= pnt_locationOfImage.x + bi_image.getWidth()
    			&& _pnt_shiftRectangle.x + _field.length
    			>= pnt_locationOfImage.x
    			
    			//check for y location
    			&& _pnt_shiftRectangle.y
    			<= pnt_locationOfImage.y + bi_image.getHeight()
    			&& _pnt_shiftRectangle.y + _field[0].length
    			>= pnt_locationOfImage.y) {
    		
        	for (int i = 0; i < _field.length; i++) {
        		for (int j = 0; j < _field[i].length; j++) {
        			
        			//if occupied
        			if (_field[i][j] != PaintBI.BORDER) {
        				
        				final int cX = i + _pnt_shiftRectangle.x, 
        						cY = j + _pnt_shiftRectangle.y;
        				
        				if (cX >= 0 && cX < bi_image.getWidth()
        						&& cY >= 0 && cY < bi_image.getHeight()) {

        					if (bi_image.getContent().getRGB(cX, cY) != alpha) {
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
		bi_image.getContent().setRGB(
				_r.x - pnt_locationOfImage.x, 
				_r.y - pnt_locationOfImage.y,
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
				if (xPx >= 0 && xPx < bi_image.getWidth()
						&& yPx >= 0 && yPx < bi_image.getHeight()) {

					bi_image.getContent().setRGB(xPx, yPx, rgbAlpha);
				} else {
					
					final String errorMsg = "POI: Delete curve error. "
							+ "PX out of range";

					if (xPx < 0) {

						State.getLogger().severe(errorMsg + "x < 0");
					} else if (xPx >= bi_image.getWidth()) {

						State.getLogger().severe(errorMsg + "x >= widht");
					} else if (yPx < 0) {

						State.getLogger().severe(errorMsg + "y < 0");
					} else if (yPx >= bi_image.getHeight()) {
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
		
		BufferedImage bi_result = new BufferedImage(bi_image.getWidth(),
				bi_image.getHeight(), BufferedImage.TYPE_INT_RGB);
		
		double[][][] borderInfo1 = new double[bi_result.getWidth()][bi_result.getHeight()][2];
		
		/*
		 * Step 1: Detection of border pixel.
		 */
		
		for (int x = 1; x < bi_image.getWidth() - 1; x++) {
			for (int y = 1; y < bi_image.getHeight() - 1; y++) {
				
				//fold with 
				//
				//	-1/4	0	1/4
				//	-2/4	0	2/4
				//	-1/4	0	1/4
				int magnX = (
						-bi_image.getGrayifiedValue(x - 1, y - 1) / 3 
						- bi_image.getGrayifiedValue(x - 1, y) / 3 
						- bi_image.getGrayifiedValue(x - 1, y + 1) / 3 

						+ bi_image.getGrayifiedValue(x + 1, y - 1) / 3  
						+ bi_image.getGrayifiedValue(x + 1, y) / 3
						+ bi_image.getGrayifiedValue(x + 1, y + 1) / 3);
				
				

				int magnY = (
						-bi_image.getGrayifiedValue(x - 1, y - 1) / 3 
						- bi_image.getGrayifiedValue(x - 1, y) / 3 
						- bi_image.getGrayifiedValue(x - 1, y + 1) / 3 

						+ bi_image.getGrayifiedValue(x + 1, y - 1) / 3  
						+ bi_image.getGrayifiedValue(x + 1, y) / 3
						+ bi_image.getGrayifiedValue(x + 1, y + 1) / 3);

				
				
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

		for (int x = 1; x < bi_image.getWidth() - 1; x++) {
			for (int y = 1; y < bi_image.getHeight() - 1; y++) {
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
		for (int x = 1; x < bi_image.getWidth() - 1; x++) {
			for (int y = 1; y < bi_image.getHeight() - 1; y++) {
				if (borderInfo1[x][y][0] >= _lower){
					bi_result.setRGB(x, y, rgb_potential);
				} else {
					bi_result.setRGB(x, y, rgb_noBorder);
				}
			}
		}

		for (int x = 1; x < bi_image.getWidth() - 1; x++) {
			for (int y = 1; y < bi_image.getHeight() - 1; y++) {
				if (borderInfo1[x][y][0] >= _upper){
					followEdge(x, y, bi_result);
				}
			}
		}
		for (int x = 1; x < bi_image.getWidth() - 1; x++) {
			for (int y = 1; y < bi_image.getHeight() - 1; y++) {
				if (bi_result.getRGB(x, y) == rgb_potential){
					bi_result.setRGB(x, y, rgb_noBorder);
				} else if (bi_result.getRGB(x, y) == rgb_border) {
					bi_result.setRGB(x,  y, bi_image.getContent().getRGB(x, y));
				}
			}
		}
		
		
		BufferedViewer.show(bi_result);
		BufferedViewer.getInstance().setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
	}
	
	
	public final BufferedImage hysteresisThreshold(
			final int _lower,
			final int _upper) {

		BufferedImage bi_result = new BufferedImage(bi_image.getWidth(),
				bi_image.getHeight(), BufferedImage.TYPE_INT_RGB);
		
		//hysteresis-threshold
		for (int x = 1; x < bi_image.getWidth() - 1; x++) {
			for (int y = 1; y < bi_image.getHeight() - 1; y++) {
				if (-bi_image.getGrayifiedValue(x, y) >= -_lower){
					bi_result.setRGB(x, y, rgb_potential);
				} else {
					bi_result.setRGB(x, y, rgb_noBorder);
				}
			}
		}

		for (int x = 1; x < bi_image.getWidth() - 1; x++) {
			for (int y = 1; y < bi_image.getHeight() - 1; y++) {
				if (-bi_image.getGrayifiedValue(x, y) >= -_upper){
					followEdge(x, y, bi_result);
				}
			}
		}
		for (int x = 1; x < bi_image.getWidth() - 1; x++) {
			for (int y = 1; y < bi_image.getHeight() - 1; y++) {
				if (bi_result.getRGB(x, y) == rgb_potential){
					bi_result.setRGB(x, y, rgb_noBorder);
				} else if (bi_result.getRGB(x, y) == rgb_border) {
					bi_result.setRGB(x,  y, bi_image.getContent().getRGB(x, y));
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
		Picture.saveBufferedImage(path + fileName + ".", new PaintObjectImage(
		//BufferedViewer.show(bis);
				0, bis, null).hysteresisThreshold(20, 140));
		
	}

	
	
	/**
	 * Because the BufferedImage is not serializable, it is necessary
	 * to remove the BufferedImage for saving and to store its content outwards.
	 */
	public final void prepareForSaving() {
		bi_image.pack();
	}
	
	
	/**
	 * Restore has to be done after saving.
	 */
	public final void restore() {
		bi_image.restore();
	}
}
