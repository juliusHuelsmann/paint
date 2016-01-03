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
import model.settings.State;
import model.util.DPoint;
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
     * This bufferedImage contains backuped image in case the current operation
     * is to be invertible.
     */
    private BufferedImage bi_backupEdit;
    
    /**
	 * The page number inside the XDocument.
	 */
	private int pageNumberPDF;

	
	/**
	 * If this object has not got a corresponding page inside the saved PDF
	 * document, {@link #pageNumberPDF} stores {@link #ID_NO_PAGE_NUMBER} which 
	 * is equal to {@value #ID_NO_PAGE_NUMBER}.
	 */
	private static final int ID_NO_PAGE_NUMBER = -1;
	
	/**
	 * Link to the Project for being able to fetch the XDocument. 
	 * The XDocument is not saved directly because it is not serializable.
	 * 
	 * For saving, the XDocument is removed out of the project class and
	 * restored afterwards.
	 */
	private final Project pro;



	/**
	 * Constructor: calls super-constructor and saves instances of important 
	 * classes.
	 * 
	 * @see #pro
	 * @see #pageNumberPDF
	 * @see #remind()
	 * @see #forget()
	 * 
	 * @param _elementId	the id of the current element which is unique in 
	 * 						the entire picture
	 * @param _project		the project which stores the PDF document
	 * @param _pageNr		the pageNumber inside PDF document
	 * @param _picture		instance of the model class picture to which the
	 * 						document is added.
	 */
	public PaintObjectPdf(
			final int _elementId, final Project _project, final int _pageNr, 
			final Picture _picture) {
		
		// call super constructor and save variables.
	
		super(_elementId, null, _picture);
	    this.pro = _project;
		this.pageNumberPDF = _pageNr;
		
	}

	
	
	/**
	 * 
	 */
	public final void startBackup() {
		this.bi_backupEdit = new BufferedImage(getBi_image().getWidth(), getBi_image().getHeight(), getBi_image().getContent().getType());
		int [] rgbA = null;
		rgbA = getBi_image().getContent().getRGB(0, 0, bi_backupEdit.getWidth(), bi_backupEdit.getHeight(), rgbA, 0, bi_backupEdit.getWidth());
		bi_backupEdit.setRGB(0, 0, bi_backupEdit.getWidth(), bi_backupEdit.getHeight(), rgbA, 0, bi_backupEdit.getWidth());
	}
	
	
	/**
	 * 
	 */
	public final void applyBackup() {
		if (bi_backupEdit != null) {

			getBi_image().setContent(bi_backupEdit);
		} else {
			State.getLogger().severe("error backup image");
		}
	}
	

	/**
	 * Constructor: calls super-constructor and saves instances of important 
	 * classes. Is called if the instance of 
	 * {@link #PaintObjectPdf(int, Project, int, Picture)} has not got a 
	 * corresponding page inside the PDF document stored in {@link #pro}.
	 * 
	 * @see #pro
	 * @see #pageNumberPDF
	 * @see #ID_NO_PAGE_NUMBER
	 * @see #remind()
	 * @see #forget()
	 * 
	 * @param _elementId	the id of the current element which is unique in 
	 * 						the entire picture
	 * @param _project		the project which stores the PDF document
	 * @param _picture		instance of the model class picture to which the
	 * 						document is added.
	 */
	public PaintObjectPdf(
			final int _elementId, final Project _project, 
			final Picture _picture) {
		
		// call super constructor and save variables.
	
		super(_elementId, null, _picture);
	    this.pro = _project;
		this.pageNumberPDF = ID_NO_PAGE_NUMBER;
		
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
     * Returns the page number inside the project, thus the corresponding 
     * picture identifier. <br>
     * Does not have anything to do with {@link #pageNumberPDF} which 
     * indicates the page number inside the PDF document which may differ
     * from those inside the project.
     * 
     * @see Project.#getPictureNumber(Picture)
     * 
     * @return 		the page number inside the project.
     */
    public final int getPageNumberProject() {
    
    	return pro.getPictureNumber(super.getPicture());
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
	 * Reload the content of the PDF Document saved in {@link #pro} at page
	 * {@link #pageNumberPDF} and store it as bufferedImage inside this 
	 * {@link #PaintObjectPdf(int, Project, int, Picture)}.
	 * 
	 * @see #pageNumberPDF
	 * @see #pro
	 * @return			the restored BufferedImage.
	 */
	public BufferedImage remind() {
		
		// if the page number is equal to NO_PAGE_NUMBER, this object has not
		// a corresponding PDF page which is to be loaded. Thus it has not to 
		// be reminded of its content.
		if (pageNumberPDF == ID_NO_PAGE_NUMBER) {
			
			return null;
		} else {

			super.getBi_image().setContent(PDFUtils.pdf2image(
					pro.getDocument().getPDDocument(), pageNumberPDF));
			return super.getBi_image().getContent();
		}
	}

	public void forget() {

		getBi_image().setContent(null);
	}
	
	
	/**
	 * Check whether the paintObjectPDF remembers its content.
	 * @return
	 */
	public boolean checkRemember() {
		return getBi_image().getContent() != null;
	}




	@Override
	public void recalculateSnapshotBounds() {
		
	}




	@Override
	public boolean isEditable() {
		return false;
	}
	
}
