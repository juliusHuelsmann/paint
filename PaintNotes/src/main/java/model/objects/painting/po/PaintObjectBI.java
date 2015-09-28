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
public abstract class PaintObjectBI extends PaintObject implements Cloneable, Serializable {


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
    public PaintObjectBI(final int _elementId, final BufferedImage _bi, 
    		final Picture _picture) {
        super(_picture, _elementId);
        this.bi_image = new SerializBufferedImage(_bi);
        this.pnt_locationOfImage = new Point(0, 0);
    }

    
    /**
     * {@inheritDoc}
     */
    @Override public final BufferedImage paint(
    		
    		// The BufferedImage which is exported in case of
    		// _final.
    		// if _final, paint to this BufferedImage.
    		// The BufferedImage's scale is [Model-Size]
    		final BufferedImage _bi_final, 
    		
    		// if _final, the image is painted without adapting to a 
    		// show size. Final images are exported as image files
    		// to the hard drive.
    		// 
    		// if not _final, the scroll size and the scale have 
    		// to be considered.
    		final boolean _final,

    		// The BufferedImage of which the displayed section of the 
    		// picture consists.
    		// if not _final, paint to this BufferedImage.
    		// The BufferedImage's scale is [Show-Size]
    		final BufferedImage _bi_notFinal, 
    		
    		// The _x and _y indicate the location of the JLabel which displays
    		// the BufferedImage. Therefore they are none-positive and scaled
    		// in [Show-Size]
    		final int _x, final int _y, 
    		
    		// The Rectangle which gives the current location of the section
    		// which is to be repainted. This section is a part of the 
    		// currently displayed Picture. It is scaled [Model-Size]
    		final DRect _r) {

    	
    	/*
    	 * Check whether to paint 
    	 * 
    	 * 	A)	_final 	(which is much easier because one does not have to take
    	 * 				into account the shifting and the difference between 
    	 * 				image- and show- size scaling.
    	 * 
    	 *  B) !_final	which is linked to more computations.
    	 */
        if (_final) {
           
        	
        	/*
        	 * print image to the export-buffered-Image. The location of the
        	 * current PaintObject is given by pnt_locationOfImage.
        	 * The shift values _x and _y should be always equal to 
        	 * zero. 
        	 */
        	
            int[] rgb = null;
            _bi_final.setRGB(
            		
            		
            		/*
            		 * Step A.1)	Adapt the location to the size of the 
            		 * 				picture. It may be possible that the
            		 *				current PaintObject is shifted to 
            		 *				somewhere outside the displayable area
            		 *				(pnt_locationOfImage < 0 or 
            		 *				pnt_locationOfImage + PO_size > image 
            		 */
            		
            		
            		
            		//TODO: if the image exists the displayed size, this
            		// will lead to an error.
            		
            		// location of the current PaintObject
            		pnt_locationOfImage.x, // + _x
            		pnt_locationOfImage.y, // + _y
            		
            		// size of the current PaintObject
                    bi_image.getWidth(), 
                    bi_image.getHeight(), 
                    
                    // get the RGB - values out of the current image
                    bi_image.getRGB(
                    		
                    		//start location in image (0, 0)
                    		0, 0, 
                    		
                    		// width and height
                    		bi_image.getWidth(), 
                            bi_image.getHeight(), 
                            
                            // RGB array 
                            rgb, 
                            
                            // export settings which equal those 
                            // for import
                            0, bi_image.getWidth()),
                            
                            // import settings.
                            0, bi_image.getWidth());
        } else {


        	/*
        	 * Step B1: 	First of all, the values in different size have to be 
        	 * 			adjusted. It is possible that a half-pixel is to 
        	 *			be displayed in [Show-Size] scaled coordinates.
        	 *
        	 *			If it is rounded to pixel, there is a little gap between
        	 *			the already displayed stuff and the newly painted
        	 *			area.
        	 */
        	
        	
        	/* 
        	 * Compute stretch - factors
        	 */
        	//fetch the zoom factors for stretching the image if necessary.
            final double cZoomFactorWidth = 1.0 
                    * State.getImageShowSize().width
                    / State.getImageSize().width;
            final double cZoomFactorHeight = 1.0 
                    * State.getImageShowSize().height
                    / State.getImageSize().height;

            /*
             * Adapt values for performing step B1.
             */
            // rounded x and y values.
            int rounded_x = (int)((int)(_x / cZoomFactorWidth) * cZoomFactorWidth);
            int rounded_y = (int)((int)(_y / cZoomFactorHeight) * cZoomFactorHeight);
            
            // interrupt if the given values are illegal if the size of 
        	// the area which is to be repainted is equal to zero.
        	final DRect r;
        	if (_r == null) {
        		r = new DRect(
        				0, 0,
        				bi_image.getWidth(), bi_image.getHeight());
        	} else {
        		r = new DRect(_r.getRectangle());
        	}
        	
            if (r == null || r.getHeight() <= 0 || r.getWidth() <= 0) {
            	return _bi_notFinal;
            }
            
            // Because the this seems to be due to rounding (while method call).
            if (cZoomFactorWidth > 1) {
            	r.setWidth(r.getWidth() +  2);
            	r.setX(r.getX() - 1);
            }if (cZoomFactorHeight > 1) {
            	r.setHeight(r.getHeight() + 2);
            	r.setY(r.getY() - 1);
            }

            
            /*
             * Step B2	Compute necessary values for applying painting of 
             * 			image. 
             */

            // these values contain the location of the selection inside 
            // the currently displayed section of the page (location
            // at the JLabel). Therefore it is in [Show-Size].
            // 
            // The shift is saved as a negative integer (points from the
            // upper left corner to the origin of the page (somewhere
            // at the upper - left of the upper left corner).
            //
            // Thus the current location of the user in image is subtracted
            // of the location of the repainted rectangle.
            int locX_at_label = (int) ((r.getX()) 
            		* cZoomFactorWidth) + rounded_x;
            int locY_at_label = ((int) ((r.getY())
            		* cZoomFactorHeight)) + rounded_y;
            
            // error-checking. The location of the small page image should 
            // never be not displayable (less than zero, greater than the
            // image width respectively height).
            // This may occur because of the rounding done above.
            if (locX_at_label < 0|| locY_at_label < 0) {
                locX_at_label = Math.max(0, locX_at_label);
                locY_at_label = Math.max(0, locY_at_label);
            }

            
            // This will contain the location of the painted stuff
            // inside the PaintObjectImage's BufferedImage.
            // Thus, after the initialization of this value, the
            // following section of the BufferedImage is painted:
            //
            //		bi_image.getRGB(	locX_at_bi_poi, 
            //							locY_at_bi_poi, 
            //							...)
            //
            // In other, more complicated words:
            // This is the location of the visible scope's location
            // from the origin of the PaintObjectImage's BufferedImage:
            //
            // ____________________________________________________
            // |                           image start            |
            // |              |<------------------|               |
            // |          vis. scope                              |
            // |__________________________________________________|
            // | GRAPHIC: Displays the defined vector (negative)  |
            // |__________________________________________________|
            // If the image starts somewhere right from the visible
            // scope's location (see graphic), 
            //
            // Then its value is negative and the display - image size
            // has to be adapted to the displayable area's size.
            // 
            // used:		
            //			(1)	Rectangle r (Model-Size),
            //			(2) locationOfImage (Model-Size),
            // thus comparable. The computed size should be [Model-Size].
            //
            int locX_at_bi_poi = (int) (r.getIX() - pnt_locationOfImage.x);
            int locY_at_bi_poi = (int) (r.getIY() - pnt_locationOfImage.y);
            if (locX_at_bi_poi < 0) {
            	
            	// adapt the repaint width to the image.
            	r.setWidth(r.getWidth() + locX_at_bi_poi);
            	
            	// Adapt the location of the repainting scope at the 
            	// JLabel.
                locX_at_label -= locX_at_bi_poi * cZoomFactorWidth;

                // Adapt the location at the POI's BufferedImage.
            	locX_at_bi_poi = 0;
            }
            
            
            if (locY_at_bi_poi < 0) {

            	// adapt the repaint width to the image.
            	r.setHeight(r.getHeight() + locY_at_bi_poi);

            	// Adapt the location of the repainting scope at the 
            	// JLabel.
                locY_at_label -= locY_at_bi_poi * cZoomFactorHeight;

                // Adapt the location at the POI's BufferedImage.
            	locY_at_bi_poi = 0;
            }

            

            /*
             * Step B3	Adapt the repaint size to the visible scope.
             * 			if locX_at_bi_poi != 0 it is > 0. Then, the 
             * 			POI starts in front of the visible scope:
             * 
             * ____________________________________________________
             * |                           visible scope start    |
             * |              |                     x             |
             * |          image start                             |
             * |__________________________________________________|
             * | GRAPHIC: Displays situation if width is changed. |
             * |__________________________________________________|
             * 
             * 			Thus, if the painted scope does not start at
             * 			the image's origin, it is necessary to change
             * 			the size of the scope that is to be extracted
             * 			from image.
             */

            // adapt the width of the selection to the size of the 
            // paint-object-image.
            r.setWidth(Math.min(
            		
            		// if image is greater than scope.
            		bi_image.getWidth() - locX_at_bi_poi,
            		
            		// if scope grater than image.
            		r.getIWidth()));
            r.setHeight(Math.min(
            		bi_image.getHeight() - locY_at_bi_poi,
            		r.getIHeight()));
            
            // interrupt if the given values are illegal if the size of 
        	// the area which is to be repainted is equal to zero.
            if (r == null || r.getIHeight() <= 0 || r.getIWidth() <= 0) {
            	return _bi_notFinal;
            }
            

            /* 
             * now a sub-image containing the newly painted stuff is created
             * and filled with the image values
             */
            
            // for filling the BufferedImage, the RGB-alpha values are written 
            // into an integer array
            int[] rgbA = new int[r.getIHeight() * r.getIWidth()];
            if (
            		locX_at_bi_poi >= 0 
            		&& locY_at_bi_poi >= 0
            		&& locX_at_bi_poi + r.getIWidth() <= bi_image.getWidth()
            		&& locY_at_bi_poi + r.getIHeight() <= bi_image.getHeight()) {

                rgbA = bi_image.getRGB(
                		locX_at_bi_poi, locY_at_bi_poi, 
                		r.getIWidth(), r.getIHeight(), 
                		rgbA,
                		0, 
                		r.getIWidth());
            } else {

            	State.getLogger().severe("fatal move error: \n" 
            			+ "Section out of scope."
            			+ "x\t" + locX_at_bi_poi + "\n"
            			+ "y\t" + locY_at_bi_poi + "\n"
            			+ "x+w\t" + locX_at_bi_poi + r.getWidth() + "\n"
            			+ "y+h\t" + locY_at_bi_poi + r.getHeight() + "\n"
            			+ "\n@image\n"
            			+ "width:\t" + bi_image.getWidth()
            			+ "\nheight:\t" + bi_image.getHeight());
            	return bi_image.getContent();
            }


            /*
             * Compute the new size of the image in [Display-Size].
             */
            int newWidth = (int) Math.max(0, 
            		(r.getWidth()) * cZoomFactorWidth);
            int newHeight = (int) Math.max(0, 
            		(r.getHeight()) * cZoomFactorHeight);

            newWidth = Math.min(
            		newWidth,
            		_bi_notFinal.getWidth() - locX_at_label);
            newHeight = Math.min(
            		newHeight,
            		_bi_notFinal.getHeight() - locY_at_label);
            
            if (newWidth <= 0 || newHeight <= 0) {
            	return _bi_notFinal;
            }
            
            // create sub-BufferedImage for the selection and fill it with
            // the pixel from image
            BufferedImage bi_section = new BufferedImage(
            		r.getIWidth(), r.getIHeight(), 
            		BufferedImage.TYPE_INT_ARGB);
            
            // write the RGB-Alpha values from the integer array to the 
            // section-BufferedImage.
            bi_section.setRGB(
            		0, 0, 
            		r.getIWidth(), 
            		r.getIHeight(),
            		rgbA, 0, r.getIWidth());
            

            bi_section = Utils.resizeImageQuick(
                    newWidth, newHeight, 
                    bi_section);
            rgbA = new int[newWidth * newHeight];
            rgbA = bi_section.getRGB(
            		0, 0, 
            		newWidth, newHeight,
            		rgbA, 0, newWidth);
            _bi_notFinal.setRGB(
            		locX_at_label, locY_at_label, 
            		newWidth, newHeight,
            		rgbA, 0, newWidth);
            
//            final Graphics g = _bi_notFinal.getGraphics();
//            g.drawImage(Utils.resizeImageQuick(
//                    newWidth, 
//                    newHeight , 
//                    bi_section),
//                    (int) (locX_at_label), 
//                    (int) (locY_at_label), 
//                    newWidth,
//                    newHeight, null);

            return _bi_notFinal;
        }
        
        return bi_image.getContent();
    }


	/**
	 * @return the bi_image
	 */
	public SerializBufferedImage getBi_image() {
		return bi_image;
	}


	/**
	 * @param bi_image the bi_image to set
	 */
	public void setBi_image(SerializBufferedImage bi_image) {
		this.bi_image = bi_image;
	}


	/**
	 * @return the pnt_locationOfImage
	 */
	public Point getPnt_locationOfImage() {
		return pnt_locationOfImage;
	}


	/**
	 * @param pnt_locationOfImage the pnt_locationOfImage to set
	 */
	public void setPnt_locationOfImage(Point pnt_locationOfImage) {
		this.pnt_locationOfImage = pnt_locationOfImage;
	}
}
