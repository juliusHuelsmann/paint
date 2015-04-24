//package declaration
package model.objects.painting.po;

//import declarations
import java.awt.Color;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

import model.objects.painting.PaintBI;
import model.objects.painting.Picture;
import model.settings.Error;
import model.settings.Status;
import model.util.DPoint;
import model.util.SerializBufferedImage;
import model.util.adt.list.List;
import model.util.paint.Utils;


/**
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
            this.pnt_locationOfImage.x += _p.x;
            this.pnt_locationOfImage.y += _p.y;
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
    		
    		final Rectangle _r) {

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
            if (_r == null || _r.height <= 0 || _r.width <= 0) {
            	return _bi;
            }

        	
        	//fetch the zoom factors for stretching the image if necessary.
            final double cZoomFactorWidth = 1.0 
                    * Status.getImageShowSize().width
                    / Status.getImageSize().width;
            final double cZoomFactorHeight = 1.0 
                    * Status.getImageShowSize().height
                    / Status.getImageSize().height;
            
            // these values contain the location of the selection inside 
            // the picture
            int locXPage = (int) ((_r.x - pnt_locationOfImage.x) 
            		* cZoomFactorWidth) + _x;
            int locYPage = (int) ((_r.y - pnt_locationOfImage.y)
            		* cZoomFactorHeight) + _y;
            
            
            // these values contain the location of the selection inside the
            // current paint-object-image
            int locXPOI = (int) (_r.x - pnt_locationOfImage.x);
            int locYPOI = (int) (_r.y - pnt_locationOfImage.y);

            // adapt the width of the selection to the size of the 
            // paint-object-image.
            // 
            // TODO: Find a better solution?
            // to the normal width, 2 * zoom-factor is added 
            // because if zoomed in, there are gaps between the painted
            // sub-images.
            _r.width = Math.min(
//            		Math.min(
            				bi_image.getWidth() - locXPOI,
            				_r.width + (int)  (2 * cZoomFactorWidth));
//            		_bi.getWidth() - locXPOI);
            _r.height = Math.min(
//            		Math.min(
            				bi_image.getHeight() - locYPOI,
            				_r.height + (int) (2 * cZoomFactorHeight));
//            		_bi.getHeight() - locYPOI);
            
            // interrupt if the given values are illegal if the size of 
        	// the area which is to be repainted is equal to zero.
            if (_r == null || _r.height <= 0 || _r.width <= 0) {
            	return _bi;
            }

            
            /* 
             * now a sub-image containing the newly painted stuff is created
             * and filled with the image values
             */
            
            // create sub-BufferedImage for the selection and fill it with
            // the pixel from image
            BufferedImage bi_section = new BufferedImage(
            		_r.width, _r.height, 
            		BufferedImage.TYPE_INT_ARGB);
            
            // for filling the BufferedImage, the RGB-alpha values are written 
            // into an integer array
            int[] rgbA = new int[_r.height * _r.width];
            rgbA = bi_image.getRGB(
            		locXPOI, locYPOI, 
            		_r.width, _r.height, 
            		rgbA,
            		0, 
            		_r.width);
            
            // write the RGB-Alpha values from the integer array to the 
            // section-BufferedImage.
            bi_section.setRGB(
            		0, 0, 
            		_r.width, 
            		_r.height,
            		rgbA, 0, _r.width);
            
            /*
             * The Content of the BufferedImage is resized and afterwards 
             * printed into the BufferedImage that displays the currently 
             * visible section of the image.
             */
            _g.getGraphics().drawImage(Utils.resizeImageQuick(
                    (int) Math.max(1, (_r.width) * cZoomFactorWidth),
                    (int) Math.max(1, (_r.height) * cZoomFactorHeight), 
                    bi_section),
                    (int) (locXPage), 
                    (int) (locYPage), 
                    (int) Math.max(1, (_r.width) * cZoomFactorWidth),
                    (int) Math.max(1, ((_r.height) * cZoomFactorHeight)), null);
        }
        
        return bi_image.getContent();
    }
    
    
    
    /**
     * {@inheritDoc}
     */
    @Override public final PaintObject[][] separate(final Rectangle _r) {
        
        if (isInSelectionImage(_r)) {
        	
        	
        	deleteRectangle(_r, null);

            new Exception(getClass() + " not implemenented yet")
            .printStackTrace();
            return null;
        } else {

            new Exception(getClass() + " not implemenented yet")
            .printStackTrace();
            return null;
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
    			if (_r[i][j] == PaintBI.OCCUPIED) {
    				
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
                * Status.getImageSize().width
                / Status.getImageShowSize().width;
        final double cZoomFactorHeight = 1.0 
                * Status.getImageSize().height
                / Status.getImageShowSize().height;
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
        			if (_field[i][j] == PaintBI.OCCUPIED) {
        				
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

		
		//TODO: this is not adapted to the location of the POI
		//usually, the location of the POI is (0,0) because
		//tested by loading PNG or JPEG image.
		
		//eliminate the image values out of specified area by replacing
		//them with (white) alpha values.
		//
		//The white color is necessary because if the pipette tool is used
		//the transparency color is to be fetched as white.
		int[] newRGBA = new int[_r.width * _r.height];
		final int maxRGB = 255;
		int rgba = new Color(maxRGB, maxRGB, maxRGB, 0).getRGB();
		for (int i = 0; i < newRGBA.length; i++) {
			newRGBA[i] = rgba;
		}
		bi_image.getContent().setRGB(
				_r.x, _r.y, _r.width, _r.height, newRGBA, 0, _r.width);
		
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

						Status.getLogger().severe(errorMsg + "x < 0");
					} else if (xPx >= bi_image.getWidth()) {

						Status.getLogger().severe(errorMsg + "x >= widht");
					} else if (yPx < 0) {

						Status.getLogger().severe(errorMsg + "y < 0");
					} else if (yPx >= bi_image.getHeight()) {
						Status.getLogger().severe(errorMsg + "y >= heights");
					}
				}
			}	
		}

        new Exception("not implemented yet").printStackTrace();
        return null;
	}


	/**
	 * {@inheritDoc}
	 */
	@Override
	public final boolean isInSelectionImageStretched(
			final byte[][] _field,
			final Point _pnt_shiftRectangle, 
			final DPoint _pnt_stretch) {

        new Exception("not implemented yet").printStackTrace();
		return false;
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
