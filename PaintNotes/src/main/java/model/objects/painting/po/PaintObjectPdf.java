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


import java.awt.Point;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.Serializable;

import model.objects.Project;
import model.objects.painting.Picture;
import model.settings.Error;
import model.util.DPoint;
import model.util.SerializBufferedImage;
import model.util.adt.list.List;
import model.util.pdf.PDFUtils;


/**
 * Paint object PDF for the .
 * 
 * @author Julius Huelsmann
 * @version %I%, %U%
 */
public class PaintObjectPdf extends PaintObjectBI implements Serializable {
	
	



    /**
     * Default serial version UID for being able to identify the list's 
     * version if saved to the disk and check whether it is possible to 
     * load it or whether important features have been added so that the
     * saved file is out-dated.
     */
    private static final long serialVersionUID = -3730582547146097485L;

    
    
    
    

    
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
    	
    }
    
    
    
    
    
    /**
     * {@inheritDoc}
     */
    @Override public final PaintObject[][] separate(final Rectangle _r) {

    	/*
    	 * Afterwards return the the two dimensional array which contains
    	 * 	@[0]	the content that is outside the selection (the current 
    	 * 			POI inserted into a new POI because the current POI 
    	 * 			is removed by default because that makes more sense 
    	 * 			if is coped with POWs and all kind of paintObjects
    	 * 			are treated in the same way for code-simplicity.
    	 *  @[1]	the content that is inside the selection (the new 
    	 *  		created POI)
    	 */
	PaintObjectPdf[][] pdfs = new PaintObjectPdf[2][1];
	pdfs[1][0] = this;
	return pdfs;
    }



    
    /**
     * {@inheritDoc}
     */
    @Override
    public final PaintObject[][] separate(
    		final byte[][] _r, final Point _pnt_selectionShift) {

    	/*
    	 * Afterwards return the the two dimensional array which contains
    	 * 	@[0]	the content that is outside the selection (the current 
    	 * 			POI inserted into a new POI because the current POI 
    	 * 			is removed by default because that makes more sense 
    	 * 			if is coped with POWs and all kind of paintObjects
    	 * 			are treated in the same way for code-simplicity.
    	 *  @[1]	the content that is inside the selection (the new 
    	 *  		created POI)
    	 */
    	PaintObjectPdf[][] pdfs = new PaintObjectPdf[2][1];
    	pdfs[1][0] = this;
    	return pdfs;
    }


    /**
     * {@inheritDoc}
     */
    @Override public final BufferedImage getSnapshot() {

        return getBi_image().getContent();
    }

    

    
    /**
     * {@inheritDoc}
     */
    @Override public final Rectangle getSnapshotBounds() {

        //if the image does not exist it is not inside the rectangle; thus 
        //return false
        if (getBi_image() == null || getBi_image().getContent() == null) {
            
            return new Rectangle(0, 0, 0, 0);
        }
        return new Rectangle(0, 0,
                getBi_image().getWidth(), getBi_image().getHeight());
    }


    
    /**
     * {@inheritDoc}
     */
    @Override public final Rectangle getSnapshotSquareBounds() {

    	return new Rectangle(0, 0,
    			Math.max(getBi_image().getWidth(), getBi_image().getHeight()),
    			Math.max(getBi_image().getWidth(), getBi_image().getHeight()));
    }


    


    /**
     * {@inheritDoc}
     */
    @Override public final void stretch(final DPoint _pnt_from, 
            final DPoint _pnt_totalStretch, final DPoint _pnt_size) {

    }


    /**
     * {@inheritDoc}
     */
    @Override
    public final boolean isInSelectionImage(
            final byte[][] _field, final Point _pnt_shiftRectangle) {

    	return false;
    }


    /**
     * {@inheritDoc}
     */
	@Override
	public final List<PaintObjectWriting> deleteRectangle(
			final Rectangle _r, 
			final List<PaintObjectWriting> _ls_pow_outside) {

		
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
		
		
        return null;
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	/**
	 * The page number inside the XDocument.
	 */
	final int pageNumber;
	
	/**
	 * Link to the Project for being able to fetch the XDocument. 
	 * The XDocument is not saved directly because it is not serializable.
	 * 
	 * For saving, the XDocument is removed out of the project class and
	 * restored afterwards.
	 */
	final Project pro;
	
	
	/**
	 * Constructor: calls super-constructor and saves instances of important 
	 * classes.
	 * 
	 * @param _elementId
	 * @param _project
	 * @param _pageNr
	 * @param _picture
	 */
	public PaintObjectPdf(
			final int _elementId, final Project _project, final int _pageNr, 
			final Picture _picture) {
		
		// call super constructor and save variables.

		super(_elementId, null, _picture);
        this.pro = _project;
		this.pageNumber = _pageNr;
		
	}

	public void remember() {

		super.getBi_image().setContent(PDFUtils.pdf2image(
				pro.getDocument().getPDDocument(), pageNumber));
	}

	public void forget() {

		getBi_image().setContent(null);
	}




	@Override
	public void recalculateSnapshotBounds() {
		
	}




	@Override
	public boolean isEditable() {
		return false;
	}
	
}
