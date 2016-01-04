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
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Observable;
import java.util.Observer;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.graphics.image.LosslessFactory;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;

import model.objects.history.HistorySession;
import model.objects.painting.Picture;
import model.settings.State;
import model.settings.StateStandard;
import model.settings.ViewSettings;
import model.util.DPoint;
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
public class Project extends Observable implements Serializable {

	
	/**
	 * Serial version UID.
	 */
	private static final long serialVersionUID = 1L;


	/**
	 * The PDF document on which the current project is based.
	 */
	private XDocument document;
	
	
	/**
	 * HistorySession of one Pictureclass.
	 */
	private HistorySession history;
	
	
	/**
	 * Picture class.
	 */
	private Picture[] pictures;

	
	
	/**
	 * If the document is saved, the path to the PDF file is saved in here.
	 */
	private String pathToPDF;

	
    /**
     * The size of the project which consists of multiple pages having diverse
     * imageSizes.
     */
    private Dimension size;


	/**
	 * Constructor of project: 
	 * Initializes the sub-classes Picture and History.
	 */
	public Project() {
		
		// create new instances of picture and history.
		pictures = new Picture[1];
		history  = new HistorySession();
	
		// initialize picture and history
		pictures[0] = new Picture();
		
		// store the default project size
    	setSize(new Dimension(
    			StateStandard.getStandardImageWidth()
    			[State.getStartupIdentifier()],
    			StateStandard.getStandardimageheight()
    			[State.getStartupIdentifier()]));
	}



	/**
	 * Returns the amount of pages which is indicated by the length of the 
	 * array containing the pages {@link #pictures} which should be equal
	 * to those of the {@link #history} and of the {@link #document}'s
	 * amount of pages. <br>
	 * 
	 * Thus, if those values differ, a terminal notification
	 * is thrown. Always return the length of the {@link #pictures} array.
	 * 
	 * @see #pictures
	 * @see #history
	 * @see #document
	 * @return 			the amount of pages contained by the project.
	 */
	public int getAmountPages() {
		
		//
		// fetch the amount of pictures.
		//
		final int amountPictures;
		if (pictures == null) {
			amountPictures = 0;
		} else {
			amountPictures = pictures.length;
		}

		//
		// fetch the amount of pages added to the PDF document.
		//
		final int amountPDFPages;
		if (document == null) {
			amountPDFPages = 0;
		} else {
			amountPDFPages = document.getNumberOfPages();
		}
		
		
		//
		// if the three values are not equal, throw a notification.
		//
		if (amountPDFPages != amountPictures) {
			State.getLogger().info("Error: the amount of PDF pages,"
					+ " pictures or history sessions do not match:\n"
					+ "pic " + amountPictures + "\n"
					+ "PDF " + amountPDFPages + "\n"
					+ "This may be possible because pages are inserted "
					+ "and removed.");
		}
		
		return amountPictures;
	}
	
	
	
	/**
	 * Returns the page number of given picture.
	 * @see #pictures
	 * 
	 * @param _pic	the picture which page number is returned
	 * @return		the page number of the picture.
	 */
	public int getPictureNumber(final Picture _pic) {
		
		if (pictures == null) {
			return -1;
		}
		
		for (int i = 0; i < pictures.length; i++) {
			if (_pic.equals(pictures[i])) {
				return i;
			}
		}
		
		// not found, return -1
		return -1;
	}
	
	/**
	 * initialize the history of the picture and the document.
	 */
	public final void initialize(final Dimension _dim_page) {
		
		//set the Picture into Status and set history to the picture.
		for (int i = 0; i < pictures.length; i++) {
			pictures[i].initialize(history, _dim_page);
		}

		
		if (document != null) {
			document.close();
		}
		
		document = new XDocument(this);
		PDPage page = new PDPage();
		page.setCropBox(new PDRectangle(
				_dim_page.width, 
				_dim_page.height));
		document.addPage(page);

		Dimension[] d = document.initialize();
		setSize(d[1]);
//		setSize(_dim_page);
		
	}
	



	/**
	 * Initializes the components of project by loading a PDF image from 
	 * given path
	 * @param _location 	the path to the PDF image.
	 */
	public void initialize(final String _location) {
		

		// 
		// if a document is already initialized, close it. 
		//
		if (document != null) {
			document.close();
		}

		
		// 
		// Load new PDF document.
		//
		try {
			this.document = new XDocument(_location, this);
			this.pathToPDF = _location;
		} catch (IOException e) {
			State.getLogger().severe("Could not load pdf image at " 
					+ _location);
		}
		
		//
		// initialization of the other components of the Project.
		//
		if (document != null) {

			//
			// create new instances of picture and history.
			//
			final int amount = document.getNumberOfPages();
			pictures = new Picture[amount];
			history  = new HistorySession();

			//
			// Instantiation of picture 
			//
			for (int i = 0; i < pictures.length; i++) {
				pictures[i] = new Picture();
			}

			//
			// Initialization of picture.
			//set the Picture into Status and set history to the picture.
			//
			for (int i = 0; i < pictures.length; i++) {
				pictures[i].initialize(history, new Dimension(
						StateStandard.getStandardImageWidth()
						[State.getStartupIdentifier()],
						StateStandard.getStandardimageheight()
						[State.getStartupIdentifier()]));
				
			}

			
			
			Dimension[] d = document.initialize();
			setSize(d[1]);

			
			
		} else {

			// create new instances of picture and history.
			pictures = new Picture[1];
			history  = new HistorySession();

			// initialize picture and history
			pictures[0] = new Picture();
			initialize(new Dimension(
						StateStandard.getStandardImageWidth()
						[State.getStartupIdentifier()],
						StateStandard.getStandardimageheight()
						[State.getStartupIdentifier()]));
		}
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

			initialize(_wsLoc);
			return null;
		} else {
			
			

			if (document != null) {
				document.close();
			}
			
			document = new XDocument(this);
			PDPage page = new PDPage();
			document.addPage(page);
			DPoint pnt = pictures[0].load(_wsLoc);
			document.setPageSize(0, (int) pnt.getX()
					 ,
					(int) pnt.getY() );

			System.out.println("dp0" + document.getPageSize(0));
			history = new HistorySession();
			pictures[0] = new Picture();
			pictures[0].initialize(history,
					new Dimension((int) pnt.getX(), (int) pnt.getY()));
			pictures[0].load(_wsLoc);

			
			
//			setSize(d[1]);
			setSize(new Dimension((int) pnt.getX(), (int) pnt.getY()));

			System.out.println(size);
			System.out.println(pnt.getX() +" " + pnt.getY());
			System.out.println("dp0" + document.getPageSize(0));
			System.out.println(pictures[0].getSize());
			return pnt;
			
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

			
//			// get size of the first picture.
//			BufferedImage bi = PDFUtils.pdf2image(
//					document.getPDDocument(), 1);
			
			State.resetZoomState();
			
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
	


	
//	public void increaseCurrentPage() {
//		if (document != null 
//				&& currentlyDisplayedPage < document.getNumberOfPages() - 1) {
//
//			document.getPdfPages()[currentlyDisplayedPage].forget();
//			
//			currentlyDisplayedPage++;
//
//			// fetch zoom factor
//			double zW = 1.0 * State.getImageSize().getWidth() / State.getImageShowSize().getWidth();
//			double zH = 1.0 * State.getImageSize().getHeight() / State.getImageShowSize().getHeight();
//
//			document.getPdfPages()[currentlyDisplayedPage].remind();
//			Rectangle r = document.getPdfPages()[currentlyDisplayedPage].getSnapshotBounds();
//			
//			State.setImageSize(r.getSize());
//			State.setImageShowSize(new Dimension((int) (r.getWidth() / zW), (int) (r.getHeight() / zH)));
//
//		}
//	}
	
	
//	public void decreaseCurrentPage() {
//
//		if (document != null && currentlyDisplayedPage > 0) {
//
//
//			document.getPdfPages()[currentlyDisplayedPage].forget();
//			currentlyDisplayedPage--;
//
//			// fetch zoom factor
//			double zW = 1.0 * State.getImageSize().getWidth() / State.getImageShowSize().getWidth();
//			double zH = 1.0 * State.getImageSize().getHeight() / State.getImageShowSize().getHeight();
//
//			document.getPdfPages()[currentlyDisplayedPage].remind();
//			Rectangle r = document.getPdfPages()[currentlyDisplayedPage].getSnapshotBounds();
//			
//			State.setImageSize(r.getSize());
//			State.setImageShowSize(new Dimension((int) (r.getWidth() / zW), (int) (r.getHeight() / zH)));
//		}
//	}
	/**
	 * @return the pictures
	 */
	public Picture getPicture(int _pictureID) {
		return pictures[_pictureID];
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
	    		page = new PDPage(new PDRectangle(
	    				(int) StateStandard.getStandardImageWidth()[State.getStartupIdentifier()],
	    				(int) StateStandard.getStandardimageheight()[State.getStartupIdentifier()]));

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


	
	/**
	 * Return the location of the page.
	 * @param _pageNumber
	 * @return
	 */
	public Rectangle getPageRectanlgeinProject(final int _pageNumber) {

		int y = 0;
		for (int j = 0; j < _pageNumber; j++) {

			// compute size
			PDRectangle b = document.getPage(j).getBBox();
			final int realPageHeight = Math.round(b.getHeight() 
					* PDFUtils.dpi / 72);

			y += realPageHeight;
		}

		PDRectangle b;
		if (document.getPage(_pageNumber) == null) {
			
			b = new PDRectangle(
					pictures[_pageNumber].getShowSize().width, 
					pictures[_pageNumber].getShowSize().height);
		} else {
			b = document.getPage(_pageNumber).getBBox();
		}
		final int width = Math.round(b.getWidth() 
				* PDFUtils.dpi / 72);
		final int height = Math.round(b.getHeight() 
				* PDFUtils.dpi / 72);
		
		return new Rectangle(0, y, width, height);
	}
	
	
	/**
	 * Calculate number of page which contains a certain pixel. 
	 * 
	 * @param _px	the pixel
	 * @return		the page's number
	 */
	public int getPageFromPX(final Point _px) {
		
		
		if (document == null) {
			return 0;
		}
		int sumHeight = 0;
		
		for (int i = 0; i < document.getNumberOfPages(); i++) {

			// compute size
			PDRectangle b = document.getPage(i).getBBox();
//			final int realPageWidth = Math.round(b.getWidth() 
//					* PDFUtils.dpi / 72);
			final int realPageHeight = Math.round(b.getHeight() 
					* PDFUtils.dpi / 72);
			
			sumHeight += realPageHeight;
			
			if (_px.y <= sumHeight) {
				return i;
			}
		}
		return document.getNumberOfPages() - 1;
	}
	
	

	/**
	 * Calculate number of page which contains a certain pixel. 
	 * 
	 * @param _px	the pixel
	 * @return		the page's number
	 */
	public Point getPageAndPageStartFromPX(final Point _px) {
		
		
		if (document == null) {
			return new Point(0, 0);
		}
		int sumHeight = 0;
		int pageY = 0;
		
		for (int i = 0; i < document.getNumberOfPages(); i++) {

			// compute size
			PDRectangle b = document.getPage(i).getBBox();
//			final int realPageWidth = Math.round(b.getWidth() 
//					* PDFUtils.dpi / 72);
			final int realPageHeight = Math.round(b.getHeight() 
					* PDFUtils.dpi / 72);
			
			pageY = sumHeight;
			sumHeight += realPageHeight;
			
			if (_px.y <= sumHeight) {
				return new Point(i, pageY);
			}
		}
		return new Point(document.getNumberOfPages() - 1, pageY);
	}


	public int getPictureID(final int _x, final int _y) {
		final double zoomToModel = State.getZoomFactorToModelSize();
		final int id = getPageFromPX(new Point(
				(int) (_x * zoomToModel),
				(int) (_y * zoomToModel)));
		return id;
	}

	public Picture getCurrentPicture(final int _x, final int _y) {
		return pictures[getPictureID(_x, _y)];
	}




	/**
	 * @return the size
	 */
	public Dimension getSize() {
		return size;
	}



	/**
	 * @param _size the size to set
	 */
	public void setSize(final Dimension _size) {

		this.size = _size;

		setChanged();
		notifyObservers(getShowSize());
        
	}

	/**
	 * @return the projectShowSize
     * the size of the shown project (is equal to {@link #projectSize} * zoom).
     *
	 */
	public Dimension getShowSize() {
		return new Dimension(
				(int) (size.width  * State.getZoomFactorToShowSize()),
				(int) (size.height * State.getZoomFactorToShowSize()));
	}



	/**
	 * Re-initialize the project with one page with the size of 
	 * @param _dim_page
	 */
	public void reload(final Dimension _dim_page) {
		
		initialize(_dim_page);
	}
}
