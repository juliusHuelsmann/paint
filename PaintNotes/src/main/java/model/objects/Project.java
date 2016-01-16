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
import javax.swing.ImageIcon;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.graphics.image.LosslessFactory;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;
import view.forms.Console;
import view.forms.Page;
import view.tabs.Debug;
import control.ContorlPicture;
import control.ControlSelectionTransform;
import control.forms.tabs.CTabSelection;
import model.objects.history.HistorySession;
import model.objects.painting.Picture;
import model.objects.painting.po.PaintObject;
import model.objects.painting.po.PaintObjectDrawImage;
import model.objects.painting.po.PaintObjectWriting;
import model.objects.painting.po.PoSelection;
import model.objects.painting.po.geo.POLine;
import model.settings.State;
import model.settings.StateStandard;
import model.settings.ViewSettings;
import model.util.DPoint;
import model.util.DRect;
import model.util.Util;
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
public class Project extends Observable implements Serializable {


	
	/**
	 * List of PaintObjects which contains all currently selected items.
	 * These items have to be saved somewhere outside the first list because 
	 * they can be transformed or removed.
	 */
	private SecureList<PoSelection> ls_poSelected;
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
		pictures[0] = new Picture(0);
		
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
				pictures[i] = new Picture(i);
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
			pictures[0] = new Picture(0);
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
			pictures[0] = new Picture(0);
			pictures[0].initialize(history,
					new Dimension((int) pnt.getX(), (int) pnt.getY()));
			pictures[0].load(_wsLoc);

			
			
//			setSize(d[1]);
			setSize(new Dimension((int) pnt.getX(), (int) pnt.getY()));

			System.out.println(size);
			System.out.println(pnt.getX() +" " + pnt.getY());
			System.out.println("dp0" + document.getPageSize(0));
			System.out.println(pictures[0].getSize());
			
			
			System.out.println("loading information:");
			System.out.println("proj size " + getSize());
			System.out.println("page size " + getPicture(0).getSize());
			System.out.println("pdf  size " + document.getPage(0).getBBox());
			
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
					0,
					y,
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
	
	private Point getPageLocinProject(final int _pageNumber) {
		
		return getPageRectanlgeinProject(_pageNumber).getLocation();
		
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

	/**
	 * Paint the selected BufferedImages to a new BufferedImage which has got
	 * the size of the selection. This method is used for copying the
	 * paintObjects as images to clipboard.
	 * 
	 * @return the painted BufferedImage.
	 */
	public BufferedImage paintSelectedBI(final DRect _recSelection) {

		if (_recSelection == null) {

			State.getLogger().warning("the selection square does not exist!");
			return null;
		}

		BufferedImage bi = new BufferedImage(_recSelection.getIWidth() + 1, 
				_recSelection.getIHeight() + 1, BufferedImage.TYPE_INT_ARGB);


    	//start transaction and closed action.
    	final int transaction = ls_poSelected
    			.startTransaction("paint selected bi", 
    					SecureList.ID_NO_PREDECESSOR);
    	final int closedAction = ls_poSelected
    			.startClosedAction("paint selected bi", 
    					SecureList.ID_NO_PREDECESSOR);
        
		ls_poSelected.toFirst(transaction, closedAction);
		while (!ls_poSelected.isEmpty() && !ls_poSelected.isBehind()) {

			PaintObject po = ls_poSelected.getItem().getPaintObject();

			if (po instanceof PaintObjectWriting) {
				PaintObjectWriting pow = (PaintObjectWriting) po;

				// TODO: zoom, scroll adjust?
				pow.paint(bi, false, bi, -_recSelection.getIX(), 
						-_recSelection.getIY(), null);

			} else if (po instanceof PaintObjectDrawImage) {
				PaintObjectDrawImage poi = (PaintObjectDrawImage) po;
				poi.paint(bi, false, bi, -_recSelection.getIX(), 
						-_recSelection.getIY(), null);

			} else {
				State.getLogger().warning("unknown kind of PaintObject" + po);
			}
			ls_poSelected.next(transaction, closedAction);

		}

    	//close transaction and closed action.
		ls_poSelected.finishTransaction(
    			transaction);
		ls_poSelected.finishClosedAction(
    			closedAction);
        
		return bi;
	}
	
	

	/*
	 * Selection methods.
	 */

	/**
	 * create selected List.
	 */
	public void createSelected() {
		if (ls_poSelected == null) {
			ls_poSelected = new SecureList<PoSelection>();
		} else {
			if (!ls_poSelected.isEmpty()) {
				State.getLogger().warning(
						"creating new selection list but list is not empty.");
			}
		}
	}

	/**
	 * insert into selected list.
	 * 
	 * @param _po
	 *            the paintObject to be inserted.
	 */
	public synchronized void insertIntoSelected(
			final PaintObject _po,
			final Debug _paintObjects) {

		// deactivates to change operations of selected items
		if (ls_poSelected == null) {
			State.getLogger().warning("insert into null list");
		} else if (_po != null) {

//			CTabSelection.activateOp();
//			if (_po instanceof PaintObjectWriting) {
//
//				PaintObjectWriting pow = (PaintObjectWriting) _po;
//				CTabSelection.getInstance().change(ls_poSelected.isEmpty(),
//						pow.getPen().getId_operation(),
//						pow.getPen().getClr_foreground().getRGB());
//			}

			ls_poSelected.insertAfterHead(new PoSelection(_po, getPageLocinProject(_po.getPicture().getPageNumber())), SecureList.ID_NO_PREDECESSOR);
			new PictureOverview(_paintObjects).addSelected(_po);

		}
	}
	
	
	/**
	 * Finish paintObjectSelection: go through the list of selected paint
	 * objects and tell the paintObjects selection interface controller
	 * the color and the pen.
	 */
	public synchronized void finishSelection(
			final CTabSelection _ctabSelection) {

		// deactivates to change operations of selected items
		if (ls_poSelected == null) {
			State.getLogger().warning("finish selection list which is"
					+ " null.");
		} else {

			//start transaction and closedAction.
			final int transaction = ls_poSelected.startTransaction(
					"finishSelection", 
					SecureList.ID_NO_PREDECESSOR);
			final int closedAction = ls_poSelected.startClosedAction(
					"finishSelection", 
					SecureList.ID_NO_PREDECESSOR);
			
			ls_poSelected.toFirst(transaction, closedAction);
			_ctabSelection.activateOp();

			while (!ls_poSelected.isEmpty() && !ls_poSelected.isBehind()) {
				

				if (ls_poSelected.getItem().getPaintObject() instanceof PaintObjectWriting) {

					PaintObjectWriting pow = 
							(PaintObjectWriting) ls_poSelected.getItem().getPaintObject();
					_ctabSelection.change(ls_poSelected.isEmpty(),
							pow.getPen().getId_operation(),
							pow.getPen().getClr_foreground().getRGB());
				}
				
				ls_poSelected.next(transaction, closedAction);
			}

			//finish actions.
			ls_poSelected.finishClosedAction(closedAction);
			ls_poSelected.finishTransaction(transaction);
		}
	}


	/**
	 * Move selected items.
	 * 
	 * @param _dX
	 *            the x difference from current position
	 * @param _dY
	 *            the y difference from current position
	 */
	public synchronized void moveSelected(final int _dX, final int _dY) {

		if (ls_poSelected == null) {
			return;
		}
		

		SecureList<PoSelection> sl_oldMove 
		= cloneSecureListPaintObject(ls_poSelected);
//		add a new history item that indicates an add operation.


    	//start transaction and closed action.
    	final int transaction = ls_poSelected
    			.startTransaction("move image", 
    					SecureList.ID_NO_PREDECESSOR);
    	final int closedAction = ls_poSelected
    			.startClosedAction("move image", 
    					SecureList.ID_NO_PREDECESSOR);
        
		ls_poSelected.toFirst(transaction, closedAction);

		while (!ls_poSelected.isBehind()) {

			PaintObject pow = (PaintObject) ls_poSelected
					.getItem().getPaintObject();
			pow.movePaintObject(_dX, _dY);
			ls_poSelected.next(transaction, closedAction);
			
		}


		SecureList<PoSelection> sl_newMoved 
		= cloneSecureListPaintObject(ls_poSelected);

//		history.addHistoryItem(history.createMoveItem(
//				sl_oldMove, sl_newMoved, null));
		
    	//close transaction and closed action.
		ls_poSelected.finishTransaction(
    			transaction);
		ls_poSelected.finishClosedAction(
    			closedAction);
        
	}
	
	

	/**
	 * Paint the selected items to the selection JLabel.
	 * 
	 * @return whether there is something to be painted or not.
	 */
	public boolean paintSelected(
			final Page _page,
			final ContorlPicture _cp,
			final ControlSelectionTransform _cps) {


		// If the list of PaintObjects has not been initialized yet or
		// the list is empty return that there is nothing to do
		if (ls_poSelected == null 
				|| ls_poSelected.isEmpty()) {
			return false;
		}
		
		// Start a transaction. That means that after the transaction has
		// been terminated, the current item of the list is reset.
    	final int id_transaction = ls_poSelected
    			.startTransaction("paintSelected", 
    					SecureList.ID_NO_PREDECESSOR);
    	final int id_closedAction = ls_poSelected
    			.startClosedAction("paintSelected", 
    					SecureList.ID_NO_PREDECESSOR);

		// Initialize new list into which the Items are inserted that are inside
		// the specified rectangle. List is sorted by id for painting the
		// items chronologically.
		SecureListSort<PaintObject> ls_poChronologic 
		= new SecureListSort<PaintObject>();

		// reset value for debugging and speed testing.
		State.setCounter_paintedPoints(0);

		
		ls_poSelected.toFirst(id_transaction, id_closedAction);
		Rectangle r_max = null;
		while (!ls_poSelected.isEmpty() && !ls_poSelected.isBehind()) {

			// if the current item is not initialized only perform the
			// next operation.
			if (ls_poSelected.getItem() != null) {

				// create new Rectangle consisting of the bounds of the current
				// paitnObject otherwise adjust the existing bounds
				if (r_max == null) {
					Rectangle b = ls_poSelected.getItem().getPaintObject().getSnapshotBounds();
					r_max = new Rectangle(b.x, b.y, b.width + b.x, b.height
							+ b.y);
				} else {
					Rectangle b = ls_poSelected.getItem().getPaintObject().getSnapshotBounds();
					r_max.x = Math.min(r_max.x, b.x);
					r_max.y = Math.min(r_max.y, b.y);
					r_max.width = Math.max(r_max.width, b.x + b.width);
					r_max.height = Math.max(r_max.height, b.y + b.height);
				}


				//insert the current element into the list containing 
				//the selected items by sorted by time of insertion 
				//indicated by their index.
				ls_poChronologic.insertSorted(ls_poSelected.getItem().getPaintObject(),
						ls_poSelected.getItem().getPaintObject().getElementId(),
						SecureList.ID_NO_PREDECESSOR);
			} else {

				// log severe error because of unnecessary PaintObject inside
				// list.
				State.getLogger().severe(
						"Error. Null PaintObject inside"
								+ " the list of sorted paintObjects.");
			}
			ls_poSelected.next(id_closedAction, id_transaction);
		}

		/*
		 * Go through the sorted list of items and paint them
		 */
		ls_poChronologic.toFirst(SecureList.ID_NO_PREDECESSOR, 
				SecureList.ID_NO_PREDECESSOR);
		BufferedImage verbufft = Util.getEmptyBISelection();
		BufferedImage verbufft2 = Util.getEmptyBISelection();
		int counter = 0;
		while (!ls_poChronologic.isBehind() && !ls_poChronologic.isEmpty()) {

			if (ls_poChronologic.getItem() instanceof PaintObjectWriting) {
				PaintObjectWriting pow = (PaintObjectWriting) ls_poChronologic
						.getItem();
				pow.enableSelected();
			}
			// paint the object.
			ls_poChronologic.getItem().paint(verbufft2, false, verbufft,
					_page.getJlbl_painting().getLocation().x,
					_page.getJlbl_painting().getLocation().y, 
					null);

			if (ls_poChronologic.getItem() instanceof PaintObjectWriting) {
				PaintObjectWriting pow = (PaintObjectWriting) ls_poChronologic
						.getItem();
				pow.disableSelected();
			}
			
			counter++;
			ls_poChronologic.next(SecureList.ID_NO_PREDECESSOR, 
					SecureList.ID_NO_PREDECESSOR);
		}
		//log repainting action in console.
		if (counter > 0) {
			Console.log(counter
					+ " Item painted in rectanlge (" + r_max + ").",
					Console.ID_INFO_UNIMPORTANT, 
					getClass(), "repaintRectangle");
		}
		
		// print logging method
		State.getLogger().info("Painted " + State.getCounter_paintedPoints()
						+ "pixel points for this operation. (paint selected)");

		//finish transaction and finish closed action; adjust the current
		//element to its state before the list transaction.
		ls_poSelected.finishTransaction(id_transaction);
		ls_poSelected.finishClosedAction(id_closedAction);

		_page.getJlbl_selectionPainting()
				.setIcon(new ImageIcon(verbufft));

		if (r_max != null) {

			Rectangle realRect = new Rectangle(r_max.x, r_max.y, r_max.width
					- r_max.x, r_max.height - r_max.y);

			// adapt the rectangle to the currently used zoom factor.
			final double zoomFactor = State.getZoomFactorToModelSize();
			realRect.x = (int) (1.0 * realRect.x / zoomFactor);
			realRect.width = (int) (1.0 * realRect.width / zoomFactor);
			realRect.y = (int) (1.0 * realRect.y / zoomFactor);
			realRect.height = (int) (1.0 * realRect.height / zoomFactor);

			realRect.x += _page.getJlbl_painting().getLocation().x;
			realRect.y += _page.getJlbl_painting().getLocation().y;

			_cp.refreshRectangle(realRect.x, realRect.y, realRect.width,
							realRect.height);
			_cps.setR_selection(realRect);
			_cp.paintEntireSelectionRect(realRect);

			return true;
		}
		return false;	
	}
	
	
	
	
	
	
	
	
	
	/**
	 * Paint the selected items to the selection JLabel.
	 * NOT USED ANYMORE
	 * 
	 * @return whether there is something to be painted or not.
	 */
	public boolean paintSelectedOld(
			final Page _page,
			final ContorlPicture _cp,
			final ControlSelectionTransform _cps) {

		_page.getJlbl_selectionPainting().setLocation(0, 0);
		BufferedImage verbufft = Util.getEmptyBISelection();
		BufferedImage verbufft2 = Util.getEmptyBISelection();

    	//start transaction and closed action.
    	final int transaction = ls_poSelected
    			.startTransaction("paintSelected", 
    					SecureList.ID_NO_PREDECESSOR);
    	final int closedAction = ls_poSelected
    			.startClosedAction("paintSelected", 
    					SecureList.ID_NO_PREDECESSOR);
        
		
		ls_poSelected.toFirst(transaction, closedAction);
		Rectangle r_max = null;
		while (!ls_poSelected.isEmpty() && !ls_poSelected.isBehind()) {
			if (ls_poSelected.getItem() != null) {

				// create new Rectangle consisting of the bounds of the current
				// paitnObject otherwise adjust the existing bounds
				if (r_max == null) {
					Rectangle b = ls_poSelected.getItem().getPaintObject().getSnapshotBounds();
					r_max = new Rectangle(b.x, b.y, b.width + b.x, b.height
							+ b.y);
				} else {
					Rectangle b = ls_poSelected.getItem().getPaintObject().getSnapshotBounds();
					r_max.x = Math.min(r_max.x, b.x);
					r_max.y = Math.min(r_max.y, b.y);
					r_max.width = Math.max(r_max.width, b.x + b.width);
					r_max.height = Math.max(r_max.height, b.y + b.height);
				}

				if (ls_poSelected.getItem().getPaintObject() instanceof PaintObjectWriting) {
					PaintObjectWriting pow = (PaintObjectWriting) ls_poSelected
							.getItem().getPaintObject();
					pow.enableSelected();
				}
				// paint the object.
				ls_poSelected.getItem().getPaintObject().paint(verbufft2, false, verbufft,
						_page.getJlbl_painting().getLocation().x,
						_page.getJlbl_painting().getLocation().y, 
						null);

				if (ls_poSelected.getItem().getPaintObject() instanceof PaintObjectWriting) {
					PaintObjectWriting pow = (PaintObjectWriting) ls_poSelected
							.getItem().getPaintObject();
					pow.disableSelected();
				}

			}
			ls_poSelected.next(transaction, closedAction);
		}


    	//close transaction and closed action.
		ls_poSelected.finishTransaction(
    			transaction);
		ls_poSelected.finishClosedAction(
    			closedAction);
    	
		_page.getJlbl_selectionPainting()
				.setIcon(new ImageIcon(verbufft));

		if (r_max != null) {

			Rectangle realRect = new Rectangle(r_max.x, r_max.y, r_max.width
					- r_max.x, r_max.height - r_max.y);

			// adapt the rectangle to the currently used zoom factor.
			final double zoomFactor = State.getZoomFactorToModelSize();
			realRect.x = (int) (1.0 * realRect.x / zoomFactor);
			realRect.width = (int) (1.0 * realRect.width / zoomFactor);
			realRect.y = (int) (1.0 * realRect.y / zoomFactor);
			realRect.height = (int) (1.0 * realRect.height / zoomFactor);

			realRect.x += _page.getJlbl_painting().getLocation().x;
			realRect.y += _page.getJlbl_painting().getLocation().y;

			_cp.refreshRectangle(realRect.x, realRect.y, realRect.width,
							realRect.height);
			_cps.setR_selection(realRect);
			_cp.paintEntireSelectionRect(realRect);

			return true;
		}
		return false;
	}

	/**
	 * Paint the selected items to the selection JLabel.
	 * 
	 * @return whether there is something to be painted or not.
	 */
	public boolean paintSelectedInline(
			final ControlSelectionTransform _controlPaintSelection,
			final Page _page,
			final ContorlPicture _controlPic) {

		// it occurred that the start point equal to 0. Why?
		int px, py;
		if (_controlPaintSelection.getPnt_start() == null) {
			px = 0;
			py = 0;
		} else {
			px = (int) (_controlPaintSelection.getPnt_start().getX() 
					- _page.getJlbl_painting().getLocation().getX());
			py = (int) (_controlPaintSelection.getPnt_start().getY()
					- _page.getJlbl_painting().getLocation().getY());

		}
		
		
		//reset the point which shaves the shift of the selection
		//because now the shift that has been done is applied.
		_controlPaintSelection.resetPntStartLocationLabel();
		_page.getJlbl_selectionPainting().setLocation(0, 0);
		
		

		BufferedImage verbufft = Util.getEmptyBISelection();
		BufferedImage verbufft2 = Util.getEmptyBISelection();
		

    	//start transaction and closed action.
    	final int transaction = ls_poSelected
    			.startTransaction("paintSelectedInline", 
    					SecureList.ID_NO_PREDECESSOR);
    	final int closedAction = ls_poSelected
    			.startClosedAction("paintSelectedInline", 
    					SecureList.ID_NO_PREDECESSOR);
        
		ls_poSelected.toFirst(transaction, closedAction);
		Rectangle r_max = null;
		while (!ls_poSelected.isEmpty() && !ls_poSelected.isBehind()) {

			if (ls_poSelected.getItem() != null) {

				// create new Rectangle consisting of the bounds of the current
				// paitnObject otherwise adjust the existing bounds
				if (r_max == null) {
					Rectangle b = ls_poSelected.getItem().getPaintObject().getSnapshotBounds();
					r_max = new Rectangle(b.x, b.y, b.width + b.x, b.height
							+ b.y);
				} else {
					Rectangle b = ls_poSelected.getItem().getPaintObject().getSnapshotBounds();
					r_max.x = Math.min(r_max.x, b.x);
					r_max.y = Math.min(r_max.y, b.y);
					r_max.width = Math.max(r_max.width, b.x + b.width);
					r_max.height = Math.max(r_max.height, b.y + b.height);
				}

				if (ls_poSelected.getItem().getPaintObject() instanceof PaintObjectWriting) {
					PaintObjectWriting pow = (PaintObjectWriting) ls_poSelected
							.getItem().getPaintObject();
					pow.enableSelected();
				}
				// paint the object.
				ls_poSelected.getItem().getPaintObject().paint(
						verbufft2,
						false,
						verbufft,
						_page.getJlbl_painting().getLocation().x
								- px,
								_page.getJlbl_painting().getLocation().y
								- py, null);

				if (ls_poSelected.getItem().getPaintObject() instanceof PaintObjectWriting) {
					PaintObjectWriting pow = (PaintObjectWriting) ls_poSelected
							.getItem().getPaintObject();
					pow.disableSelected();
				}

			}
			ls_poSelected.next(transaction, closedAction);
		}

    	//close transaction and closed action.
		ls_poSelected.finishTransaction(
    			transaction);
		ls_poSelected.finishClosedAction(
    			closedAction);
		
		_page.getJlbl_selectionPainting()
				.setIcon(new ImageIcon(verbufft));

		if (r_max != null) {

			Rectangle realRect = new Rectangle(r_max.x, r_max.y, r_max.width
					- r_max.x, r_max.height - r_max.y);

			// adapt the rectangle to the currently used zoom factor.
			final double zoomFactor = State.getZoomFactorToModelSize();
			realRect.x = (int) (1.0 * realRect.x / zoomFactor);
			realRect.width = (int) (1.0 * realRect.width / zoomFactor);
			realRect.y = (int) (1.0 * realRect.y / zoomFactor);
			realRect.height = (int) (1.0 * realRect.height / zoomFactor);

			realRect.x += _page.getJlbl_painting().getLocation().x;
			realRect.y += _page.getJlbl_painting().getLocation().y;

			_controlPic
					.refreshRectangle(realRect.x, realRect.y, realRect.width,
							realRect.height);
			_controlPaintSelection.setR_selection(realRect);
			_controlPic
					.paintEntireSelectionRect(realRect);
			return true;
		}
		
		return false;
	}

	/**
	 * release selected elements to normal list.
	 */
	public synchronized void releaseSelected(
			final ControlSelectionTransform _controlPaintSelection,
			final CTabSelection _controlTabSelection,
			final Debug _paintObjects,
			final int _xLocationPaint, final int _yLocationPaint) {


		// deactivates to change operations of selected items
		_controlTabSelection.deactivateOp();
		if (ls_poSelected == null) {
			State.getLogger().info("o selected elements");
			return;
		}
		
		//create new transaction
		int transaction = ls_poSelected.startTransaction(
				"picture release selected", 
				SecureList.ID_NO_PREDECESSOR);
		
		//pass the list.
		ls_poSelected.toFirst(transaction, SecureList.ID_NO_PREDECESSOR);
		while (!ls_poSelected.isEmpty()) {

			PoSelection pos = ls_poSelected.getItem();
			PaintObject po = pos.getPaintObject();

			if (po == null) {
				State.getLogger().warning("error: empty list item");
			}
			
			// find the page which contains the current PoSelection
			final Point locPageOriginal = pos.getLocationPageProject().getLocation();
			final Point locInPageOriginal = po.getSnapshotBounds().getLocation();
			// compute the page and the 
			final Point pnt_pageStart = getPageAndPageStartFromPX(new Point(
					locPageOriginal.x + locInPageOriginal.x,
					locPageOriginal.y + locInPageOriginal.y));
			final int pageNumber  = pnt_pageStart.x;
			final int pageY       = pnt_pageStart.y;
			final int locInPageDY = locPageOriginal.y - pageY;
			System.out.println("insert into page number " + pageNumber);
			final Picture pic_toInsert = pictures[pageNumber];
			// adjust the y location in new page.
			po.movePaintObject(0, locInPageDY);
			po.setPicture(pic_toInsert);
			
			
			
			
			
			
			

			new PictureOverview(_paintObjects).removeSelected(po);

			if (po instanceof PaintObjectWriting) {
				PaintObjectWriting pow = (PaintObjectWriting) po;
				new PictureOverview(_paintObjects).add(pow);
				pic_toInsert.getLs_po_sortedByY().insertSorted(pow, pow.getSnapshotBounds().y,
						SecureList.ID_NO_PREDECESSOR);
			} else if (po instanceof PaintObjectDrawImage) {
				PaintObjectDrawImage poi = (PaintObjectDrawImage) po;
				new PictureOverview(_paintObjects).add(poi);

				pic_toInsert.getLs_po_sortedByY().insertSorted(poi, poi.getSnapshotBounds().y,
						SecureList.ID_NO_PREDECESSOR);
			} else if (po instanceof POLine) {

				POLine p = (POLine) po;
				p.recalculateSnapshotBounds();
				new PictureOverview(_paintObjects).add(p);

				pic_toInsert.getLs_po_sortedByY().insertSorted(p, p.getSnapshotBounds().y,
						SecureList.ID_NO_PREDECESSOR);
			} else if (po != null) {
				State.getLogger().warning("unknown kind of PaintObject" + po);
			}
			ls_poSelected.remove(transaction);
			ls_poSelected.toFirst(transaction, SecureList.ID_NO_PREDECESSOR);
		}
		
		//finish transaction and destroy list of selected items.
		ls_poSelected.finishTransaction(transaction);
		ls_poSelected = null;
	}

	/**
	 * release selected elements to normal list.
	 */
	public synchronized void deleteSelected(
			final Debug _pos,
			final CTabSelection _cts) {

		if (ls_poSelected == null) {
			State.getLogger().info("o selected elements");
			return;
		}
		//create new transaction
		int transaction = ls_poSelected.startTransaction(
				"picture delete selected", 
				SecureList.ID_NO_PREDECESSOR);
		
		//pass the list.
		ls_poSelected.toFirst(transaction, SecureList.ID_NO_PREDECESSOR);
		while (!ls_poSelected.isEmpty()) {

			if (ls_poSelected.getItem() == null) {
				State.getLogger().warning("error: empty list item");
			}

			new PictureOverview(_pos).removeSelected(ls_poSelected.getItem().getPaintObject());
			ls_poSelected.remove(transaction);
			ls_poSelected.toFirst(transaction, SecureList.ID_NO_PREDECESSOR);
		}
		// deactivates to change operations of selected items
		_cts.deactivateOp();

		//finish transaction and destroy list of selected items.
		ls_poSelected.finishTransaction(transaction);
		ls_poSelected = null;
	}

	
	

	/**
	 * Return whether some PaintObjects are selected or not.
	 * @return whether there is something selected or not.
	 */
	public boolean isSelected() {
		return !(ls_poSelected == null || ls_poSelected.isEmpty());
	}
	
	
	
	
	
	
	

	/**
	 * 
	 * @param _slpo
	 * @return
	 */
	public static SecureList<PoSelection> cloneSecureListPaintObject(
			final SecureList<PoSelection> _slpo) {
		
		SecureList<PoSelection> sl_new = new SecureList<PoSelection>();
		
		if (_slpo != null) {

			_slpo.toFirst(SecureList.ID_NO_PREDECESSOR, 
					SecureList.ID_NO_PREDECESSOR);
			while (!_slpo.isBehind() && !_slpo.isEmpty()) {
				
				sl_new.insertBehind(_slpo.getItem().clone(), 
						SecureList.ID_NO_PREDECESSOR);
				_slpo.next(SecureList.ID_NO_PREDECESSOR,
						SecureList.ID_NO_PREDECESSOR);
			}
		}
		return sl_new;
		
	}

	
	
	
	

	/**
	 * Empty each paintObject.
	 */
	public void emptyImage() {
		ls_poSelected = new SecureList<PoSelection>();
	}


	/**
	 * @return the ls_poSelected
	 */
	public SecureList<PoSelection> getLs_poSelected() {
		return ls_poSelected;
	}

}
