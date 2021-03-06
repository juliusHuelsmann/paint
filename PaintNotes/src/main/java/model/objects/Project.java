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
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.graphics.image.LosslessFactory;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;

import model.objects.history.HistorySession;
import model.objects.painting.Picture;
import model.objects.painting.po.PaintObject;
import model.objects.painting.po.PaintObjectDrawImage;
import model.settings.State;
import model.util.DPoint;
import model.util.adt.list.SecureList;
import model.util.adt.list.SecureListSort;
import model.util.paint.Utils;
import model.util.pdf.PDFUtils;
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
public class Project implements Serializable {

	
	/**
	 * Serial version UID.
	 */
	private static final long serialVersionUID = 1L;


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

	public int getAmountPages() {
		if (pictures == null) {
			return 0;
		}
		return pictures.length;
	}
	
	/**
	 * If the document is saved, the path to the PDF file is saved in here.
	 */
	private String pathToPDF;

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
			this.pathToPDF = _pString;
		} catch (IOException e) {
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
			
			if (_wsLoc.endsWith(".pdf")) {

				this.currentlyDisplayedPage = 0;
	            initialize(_wsLoc);
	            return null;
			} else {
				this.currentlyDisplayedPage = 0;
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
	 * Set the project class serializable by removing the XDocument.
	 * The path to the PDF file that has been edited is saved in the 
	 * String pathToPDF.
	 * 
	 * For being able to restore the PDF file, it is necessary that
	 * the original PDF file has not been removed.
	 */
	public final void setSerializable() {
		
		document.setSerializable();
	}
	
	
	
	/**
	 * Restore the XDocument file from the path which is saved in the
	 * class-variable pathToPDF.
	 * 
	 * @see setSerializable()
	 */
	public final void restoreFormSerializable() {
			document.restoreFormSerializable(pathToPDF);

			
			// get size of the first picture.
			BufferedImage bi = PDFUtils.pdf2image(
					document.getPDDocument(), 1);
			
			State.setImageSize(new Dimension(bi.getWidth(), bi.getHeight()));
			State.setImageShowSize(new Dimension(bi.getWidth(), bi.getHeight()));
			
			currentlyDisplayedPage = 0;
	}
	
	/**
	 * Restore the XDocument file from the path which is saved in the
	 * class-variable pathToPDF.
	 * 
	 * @see setSerializable()
	 */
	public final void restoreFormSerializable(final PDDocument _xD) {
		if (!document.restoreFormSerializable(_xD)) {
			restoreFormSerializable();
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

			document.getPdfPages()[currentlyDisplayedPage].forget();
			
			currentlyDisplayedPage++;

			// fetch zoom factor
			double zW = 1.0 * State.getImageSize().getWidth() / State.getImageShowSize().getWidth();
			double zH = 1.0 * State.getImageSize().getHeight() / State.getImageShowSize().getHeight();

			document.getPdfPages()[currentlyDisplayedPage].remember();
			Rectangle r = document.getPdfPages()[currentlyDisplayedPage].getSnapshotBounds();
			
			State.setImageSize(r.getSize());
			State.setImageShowSize(new Dimension((int) (r.getWidth() / zW), (int) (r.getHeight() / zH)));

		}
	}
	
	
	public void decreaseCurrentPage() {

		if (document != null && currentlyDisplayedPage > 0) {


			document.getPdfPages()[currentlyDisplayedPage].forget();
			currentlyDisplayedPage--;

			// fetch zoom factor
			double zW = 1.0 * State.getImageSize().getWidth() / State.getImageShowSize().getWidth();
			double zH = 1.0 * State.getImageSize().getHeight() / State.getImageShowSize().getHeight();

			document.getPdfPages()[currentlyDisplayedPage].remember();
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
	    	
	    	

	        int width = (int) page.getCropBox().getWidth();
	        int height = (int) page.getCropBox().getHeight();
	        
	        
	        PDImageXObject ximage =  LosslessFactory.createFromImage(_doc, 
//	        		_bi);
	        		Utils.resizeImage(width, height, _bi));

		        PDPageContentStream content = new PDPageContentStream(_doc, page, true, true);

	            // contentStream.drawImage(ximage, 20, 20 );
	            // better method inspired by http://stackoverflow.com/a/22318681/535646
	            // reduce this value if the image is too large
	            float scale = 1f;
	            content.drawImage(ximage, 20, 20, ximage.getWidth()*scale, ximage.getHeight()*scale);

	            content.close();
		        //  LosslessFactory.createFromImage(doc, bim)
//		        content.drawImage(ximage, 0, 0);
//		        content.close();
	    }
	    catch (IOException ie){
	        //handle exception
	    }
	}
	
	
	
	public final void saveProject(final String _wsLoc) {
		try {
			FileOutputStream fos = new FileOutputStream(new File(_wsLoc));
			ObjectOutputStream oos = new ObjectOutputStream(fos);

			// store the content of the BufferedImages elsewhere and delete 
			// it afterwards because it is not serializable. After saving 
			// operation has been completed, the bufferedImages are 
			// automatically loaded.
			// pack images
			for (int i = 0; i < pictures.length; i++) {
				pictures[i].pack();
			}

			// pack project
			PDDocument temp = document.getPDDocument();
			this.setSerializable();
			
			
			oos.writeObject(this);
			
			this.restoreFormSerializable(temp);

			for (int i = 0; i < pictures.length; i++) {
				pictures[i].unpack();
			}
			oos.flush();
			oos.close();
			fos.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}
	
	
	
	public void savePDF(final String firstPath) throws IOException {

    	
    	
    	PDDocument doc = document.getPDDocument();
    	System.out.println(doc);
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
        		
        		attatchToPDF(doc, pictures[i].getBufferedImage(0, 0, false), index);
			}


    	    //save and close
    	    doc.save(firstPath);
        }
        finally
        {
            if( doc != null )
            {
//                doc.close();
            }
        }
    }


	/**
	 * @return the document
	 */
	public XDocument getDocument() {
		return document;
	}

}
