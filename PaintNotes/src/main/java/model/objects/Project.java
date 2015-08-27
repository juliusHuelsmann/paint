package model.objects;


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
import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.IOException;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.graphics.image.LosslessFactory;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;
import model.objects.history.HistorySession;
import model.objects.painting.Picture;
import model.settings.State;
import model.util.DPoint;
import model.util.paint.Utils;
import model.util.pdf.XDocument;

/**
 * Project class: the main model class which contains
 * pages and their history.
 * 
 * By now, it is only possible to have one page inside one Project.
 * That is going to be changed in the future.
 * 
 * @author Julius Huelsmann
 * @version %I%, %U%
 */
public class Project {

	
	/**
	 * The PDF document on which the current project is based.
	 */
	private XDocument document;
	
	
	/**
	 * The index of the displayed page.
	 */
	private int currentlyDisplayedPage;
	
	/**
	 * HistorySession of one Pictureclass.
	 */
	private HistorySession[] history;
	
	
	/**
	 * Picture class.
	 */
	private Picture[] pictures;


	/**
	 * Constructor of project: 
	 * Initializes the sub-classes Picture and History.
	 */
	public Project() {
		
		// create new instances of picture and history.
		pictures = new Picture[1];
		history  = new HistorySession[1];

		// initialize picture and history
		pictures[0] = new Picture();
		history [0] = new HistorySession(pictures[0]);
		
		// set current display page
		this.currentlyDisplayedPage = 0;
	}


	/**
	 * Constructor of project: 
	 * Initializes the sub-classes Picture and History.
	 */
	public void initialize(final String _pString) {

		

		try {
			this.document = new XDocument(_pString, this);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if (document != null) {

			// create new instances of picture and history.
			final int amount = document.getNumberOfPages();
			pictures = new Picture[amount];
			history  = new HistorySession[amount];

			// initialize picture and history
			for (int i = 0; i < history.length; i++) {
				pictures[i] = new Picture();
				history [i] = new HistorySession(pictures[0]);
				
			}

			initialize();
			Dimension d = document.initialize();

			State.setImageSize(d);
			State.setImageShowSize(d);

			
			
		} else {

			// create new instances of picture and history.
			pictures = new Picture[1];
			history  = new HistorySession[1];

			// initialize picture and history
			pictures[0] = new Picture();
			history [0] = new HistorySession(pictures[0]);

			initialize();
		}
		

		// set current display page
		this.currentlyDisplayedPage = 0;
	
	}
	
	

	/**
	 * load an image to the picture. and write it into bi_resized.
	 * 
	 * @param _wsLoc
	 *            the location of the image
	 * @return the size of the image
	 */
	public DPoint load(final String _wsLoc) {
//		BufferedImage bi_normalSize;
			
			if (_wsLoc.endsWith(".pdf")) {

	            initialize(_wsLoc);
	            return null;
			} else {

				return 	pictures[0].load(_wsLoc);

			} 
	}


	
	/**
	 * initialize the history of the picture.
	 */
	public final void initialize() {
		
		//set the Picture into Status and set history to the picture.
		for (int i = 0; i < history.length; i++) {
			pictures[i].initialize(history[i]);
			
		}
	}
	
	
	/**
	 * 
	 * @return the current page of the PDF document.
	 */
	public final PDPage getCurrentPage() {
		if (document != null) {
			return document.getPage(currentlyDisplayedPage);	
		}
		return null;
	}



	/**
	 * @return the history
	 */
	public final HistorySession getCurrentHistory() {
		
		if (currentlyDisplayedPage < history.length) {

			return history[currentlyDisplayedPage];
		}
		return null;
	}




	/**
	 * @return the picture
	 */
	public final Picture getCurrentPicture() {

		if (currentlyDisplayedPage < pictures.length) {

			return pictures[currentlyDisplayedPage];
		}
		return null;
	}


	
	public void resetCurrentPage() {
		currentlyDisplayedPage = 0;
	}
	
	public void increaseCurrentPage() {
		if (document != null 
				&& currentlyDisplayedPage < document.getNumberOfPages() - 1) {

			currentlyDisplayedPage++;

			// fetch zoom factor
			double zW = 1.0 * State.getImageSize().getWidth() / State.getImageShowSize().getWidth();
			double zH = 1.0 * State.getImageSize().getHeight() / State.getImageShowSize().getHeight();
			
			Rectangle r = document.getPdfPages()[currentlyDisplayedPage].getSnapshotBounds();
			
			State.setImageSize(r.getSize());
			State.setImageShowSize(new Dimension((int) (r.getWidth() / zW), (int) (r.getHeight() / zH)));

		}
	}
	
	
	public void decreaseCurrentPage() {

		if (currentlyDisplayedPage > 0) {

			
			currentlyDisplayedPage--;

			// fetch zoom factor
			double zW = 1.0 * State.getImageSize().getWidth() / State.getImageShowSize().getWidth();
			double zH = 1.0 * State.getImageSize().getHeight() / State.getImageShowSize().getHeight();
			
			Rectangle r = document.getPdfPages()[currentlyDisplayedPage].getSnapshotBounds();
			
			State.setImageSize(r.getSize());
			State.setImageShowSize(new Dimension((int) (r.getWidth() / zW), (int) (r.getHeight() / zH)));
		}
	}
	/**
	 * @return the pictures
	 */
	public Picture getPicture(int _pictureID) {
		return pictures[_pictureID];
	}


	/**
	 * @return the currentlyDisplayedPage
	 */
	public int getCurrentPageNumber() {
		return currentlyDisplayedPage;
	}


	/**
	 * 
	 * @param _doc
	 * @param _bi
	 * @param _pageindex 		index of page to which the BufferedImage is 
	 * 							inserted.
	 * 							If it is equal to -1, new page is created.
	 * 						
	 */
	public void attatchToPDF(
			final PDDocument _doc, 
			final BufferedImage _bi,
			final int _pageindex){
	    PDPage page = null;
	    try {
	    	if (_pageindex == -1) {
	    		page = new PDPage(new PDRectangle(State.getImageSize().width, State.getImageSize().height));

		        _doc.addPage(page);
	    	} else {
	    		page = _doc.getPage(_pageindex);
//	    		page.setCropBox(new PDRectangle(State.getImageSize().width , 
//	    				State.getImageSize().height ));
	    		
	    	}

		        PDPageContentStream content = new PDPageContentStream(_doc, page);
		        
		        int width = (int) page.getCropBox().getWidth();
		        int height = (int) page.getCropBox().getHeight();
		        PDImageXObject ximage =  LosslessFactory.createFromImage(_doc, 
//		        		_bi);
		        		Utils.resizeImage(width, height, _bi));
		        content.drawImage(ximage, 0, 0);
		        content.close();
	    }
	    catch (IOException ie){
	        //handle exception
	    }
	}
	
	
	
	
	
	public void savePDF(final String firstPath) throws IOException {

    	
    	
    	PDDocument doc = document.getPDDocument();
        try
        {
        	
        	final boolean newlyCreated = (doc == null);
        	
        	if (newlyCreated) {

            	// create new document and insert empty page to the document.
            	doc = new PDDocument();
        	} else {
        		// reset wrong settings.
        		State.setBorderBottomPercentExport(0);
        		State.setBorderRightPercentExport(0);
        		State.setBorderLeftPercentExport(0);
        		State.setBorderTopPercentExport(0);
        		State.setExportAlpha(true);
        	}
        	
        	
        	for (int i = 0; i < pictures.length; i++) {
        		int index = i + 0;
        		if (newlyCreated) {
        			index = -1;
        		}
        		
        		attatchToPDF(doc, pictures[i].getBufferedImage(0, 0), index);
			}


    	    //save and close
    	    doc.save(firstPath);
        }
        finally
        {
            if( doc != null )
            {
                doc.close();
            }
        }
    }

}
