package control;


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


import java.awt.Color;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.util.Random;
import javax.swing.ImageIcon;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import control.forms.BorderThread;
import control.interfaces.MoveEvent;
import control.interfaces.PaintListener;
import model.objects.painting.PaintBI;
import model.objects.painting.po.PaintObject;
import model.objects.pen.special.PenSelection;
import model.settings.State;
import model.settings.ViewSettings;
import model.util.Util;
import model.util.pdf.PDFUtils;
import view.forms.Console;
import view.forms.Page;
import view.forms.PaintLabel;
import view.forms.Tabs;
import view.util.mega.MPanel;


/**
 * Listener class for PaintLabel.
 * @author Julius Huelsmann
 * @version %I%, %U%
 */
public class ContorlPicture implements PaintListener {

	/**
	 * The bufferedImage containing the currently displayed painting stuff.
	 */
	private BufferedImage bi;
	
	/**
	 * The thread which moves the border.
	 */
	private BorderThread thrd_moveBorder;
	
	
	/**
	 * The ControlPaint.
	 */
	private ControlPaint cp;
	
	/**
	 * Constructor.
	 * @param _cp the ControlPaint
	 */
	public ContorlPicture(
			final ControlPaint _cp) {
		
		
		this.cp = _cp;
	}

	
	
	
	
	/**
	 * Return the page.
	 * @return the page.
	 */
	private Page getPage() {
		return cp.getView().getPage();
	}
	/**
	 * Return the page.
	 * @return the tabs.
	 */
	private Tabs getTabs() {
		return cp.getView().getTabs();
	}
	
//	
//	/**
//	 * Refresh the background of painting.
//	 *  //TODO: quicker! like in repainting of painted content.
//	 */
//	private void refreshPaintBackground() {
//
//		//paint the painting background (e.g. raster / lines) at the graphical
//		//user interface.
//		if (getPage().getJlbl_painting().getWidth() != 0
//				&& getPage().getJlbl_painting().getHeight() != 0) {
//
//			
//			bi_background = new BufferedImage(
//					Math.max(
//							ViewSettings.getView_bounds_page().getSize().width,
//							1),
//					Math.max(
//							ViewSettings.getView_bounds_page().getSize().height,
//							1),
//					BufferedImage.TYPE_INT_ARGB);
//			
//			
//			bi_background = emptyRect(0, 0,
//					bi_background.getWidth(), 
//					bi_background.getHeight(), bi_background);
//			
//			Picture pic = cp.getPicture();
//			bi_background = Utils.getBackground(
//					bi_background, 
//					-getPaintLabel().getLocation().x, 
//					-getPaintLabel().getLocation().y, 
//					-getPaintLabel().getLocation().x
//					+ getPaintLabel().getWidth(), 
//					-getPaintLabel().getLocation().y 
//					+ getPaintLabel().getHeight(),
//					0, 0, pic.getShowSize());
//			getPage().getJlbl_backgroundStructure().setIcon(
//					new ImageIcon(bi_background));  
//		}
//
//	}
	
	
	/**
	 * das gleiche wie unten nur mut zoom rec.t.
	 * @param _x the x coordinate in view
	 * @param _y the y coordinate in view
	 * @param _width the width
	 * @param _height the height
	 */
	public final void paintZoom(final int _x, final int _y, 
			final int _width, final int _height) {
		
		getPage().getJlbl_border().setBounds(_x, _y, _width, _height);
	}
	
	
	/**
	 * Remove the zoom box because operation zoom has changed.
	 */
	public final void removeZoomBox() {
		paintZoom(0, 0, 0, 0);
	}
	
	
	/**
	 * Paint the entire selection stuff.
	 * @param _r the rectangle which is selected.
	 */
	public final void paintEntireSelectionRect(final Rectangle _r) {

		//close old border thread.
		if (thrd_moveBorder != null) {
			thrd_moveBorder.interrupt();

			getPaintLabel().repaint();
		}

		//initialize the thread and start it.
		thrd_moveBorder = new BorderThread(_r, true, null, null, 
				getTabs(), getPage());
		thrd_moveBorder.start();
//		
		//paint the background
//		BufferedImage bi_fresh = getPage().getEmptyBI();
//		Utils.paintRastarBlock(bi_fresh, 
//				ViewSettings.SELECTION_BACKGROUND_CLR, _r);

		//		
		//show resize buttons
		
		for (int a = 0; a < getPage().getJbtn_resize().length; a++) {
			for (int b = 0; b < getPage().getJbtn_resize().length;
					b++) {
				//size of JButton
				final int b_size = getPage().getJbtn_resize()[a][b]
						.getWidth();
				
				getPage().getJbtn_resize()[a][b]
						.setLocation(_r.x + _r.width * a / 2  - b_size / 2, 
								_r.y + _r.height * b / 2 - b_size / 2);
			}
		}
		getPage().getJlbl_border().setBounds(_r);
	}
	
	
	
	/**
	 * Refresh the entire image.
	 */
	public final void refreshPaint() {
	
		State.getLogger().finest("refreshing entire PaintLabel. \nValues: "
				+ "\n\tgetSize:\t" + getPaintLabel().getSize() + " vs. " 
				+ getJPnlToMove().getSize()
				+ "\n\tgetLocation:\t" + getPaintLabel().getLocation() 
				+ " vs. " + getJPnlToMove().getLocation()
				+ "\n\t" + "_x:\t\t"
				+ "\n\t" + "_y\t\t"
				+ "\n\t" + "_width\t\t" + getPaintLabel().getWidth()
				+ "\n\t" + "_height\t\t" + getPaintLabel().getHeight() + "\n");
	
		refreshRectangle(
				0,0,
//				-getPaintLabel().getLocation().x, 
//				-getPaintLabel().getLocation().y, 
				getPaintLabel().getWidth() ,
				getPaintLabel().getHeight());
//		//paint the painted stuff at graphics
//		setBi(cp.getPicture().updateRectangle(
//				-getPaintLabel().getLocation().x, 
//				-getPaintLabel().getLocation().y, getPaintLabel().getWidth() ,
//				getPaintLabel().getHeight(), 0, 0, getBi(), 
//				cp.getControlPic()));
	

//		setBi(cp.getPicture().updateRectangle(
//				pnt_start.x, pnt_start.y,
//				_width, _height, _x, _y, getBi(), 
//				cp.getControlPic()));
//		refreshPaintBackground();
		getPaintLabel().setIcon(new ImageIcon(getBi()));
	}





	/**
	 * Repaint a special rectangle. Repaints both the PaintObjects and
	 * the selected background (e.g. lines, raster, none).<br>
	 * The shift of the currently displayed section of the image must
	 * not be applied to the parameters. <br> <br>
	 * 
	 * The parameters are converted inside the method into [MODEL-SIZE]
	 * by performing the following computation:<br>
	 * <code> v_model = (v_param + jlbl_picture.getLocation().getY())
	 * 						* State.getZoomFactorToModelSize()</code>
	 * 
	 * The following functions are essential for the method:
	 * @see Project.#getPictureID(int, int) <br>
	 * 		for getting the affected Pictures.
	 * 
	 * @see Utils.#getBackground(BufferedImage, int, int, int, int, int,
	 * 		int, Dimension) <br>
	 * 		for getting the background image of the given scope.
	 * 
	 * @see Picture.#repaintRectangle(Point, Point, Dimension, BufferedImage,
	 * 		boolean, boolean) <br>
	 * 		for repainting the <code>PaintObjects</code> inside the repaint
	 * 		scope.
	 * 
	 * 
	 * 
	 * @param _x 		the x coordinate of the rectangle that is to be 
	 * 					repainted in [view-size]. This is the exact vew-
	 * 					coordinate, thus the model-shift of the JLabel
	 * 					which displays the painting is not added to the
	 * 					parameter. Thus this value should be inside the
	 * 					following range:
	 * 					[0, jlbl_painting.getWidth();]<br>
	 *
	 * @param _y 		the x coordinate of the rectangle that is to be 
	 * 					repainted in [view-size].This is the exact view-
	 * 					coordinate, thus the model-shift of the JLabel
	 * 					which displays the painting is not added to the
	 * 					parameter. Thus this value should be inside the
	 * 					following range:
	 * 					[0, jlbl_painting.getHeight();]<br>
	 * 					
	 * @param _width	the width of the rectangle that is to be 
	 * 					repainted in [view-size].This is the exact view-
	 * 					coordinate, thus the model-shift of the JLabel
	 * 					which displays the painting is not added to the
	 * 					parameter. Thus this value should be inside the
	 * 					following range:
	 * 					[0, jlbl_painting.getWidth();]<br>
	 * 
	 * @param _height	the height of the rectangle that is to be 
	 * 					repainted in [view-size].This is the exact view-
	 * 					coordinate, thus the model-shift of the JLabel
	 * 					which displays the painting is not added to the
	 * 					parameter. Thus this value should be inside the
	 * 					following range:
	 * 					[0, jlbl_painting.getHeight();]<br>
	 * 
	 * @return the resulting BufferedImage.
	 */
	public final BufferedImage refreshRectangle(
			final int _x, final int _y, 
			final int _width, final int _height) {

		/*
		 * This function has to call the functions 
		 * - <code>Picture.updateRectangle</code> 
		 * 			of the Picture classes that
		 * 			are inside the visible scope for updating the painted 
		 * 			<code>PaintObjects</code>
		 * - <code> Utils.getBackground</code>
		 * 			which paints the background the user selected onto
		 * 			the updated BufferedImage.
		 * 
		 * 1) For that purpose, it is necessary to get the affected Pages.
		 * That is done in the first step by calling the method
		 * <code>Project.#getPageFromPX(int, int)</code>.
		 * 
		 * The index of the first, and last page are stored inside the 
		 * following variables:
		 * - firstPrintedPage,
		 * - lastPrintedPage.
		 * 
		 * 2) After the identifiers for the pictures have been computed, it is 
		 * necessary to get the following values for each displayed page:
		 * 
		 * 	- loc_picture	the location of the selection inside the entire
		 * 					page. The BufferedImage which is given to this
		 * 					very method is only the section, which is 
		 * 					currently visible on screen for the user.
		 * 
		 * 	- loc_bi		The location of the rectangle which is to be 
		 * 					repainted inside the BufferdImage. 
		 * 
		 * 	- _size_bi		The size of the rectangle that is to be 
		 * 					repainted inside the BufferedImage.
		 * 
		 * They are stored in arrays named
		 * 	- Point[] loc_picture,
		 * 	- Point[] loc_bi,
		 * 	- Point[] size_bi.
		 * 
		 * Finally, the BufferedImage is reset by calling the 
		 * #setBi(BufferedImage) method, which updates the view classes.
		 * 
		 */
		
		
		/*    _______________________________________________
		 *    |                                             |
		 *    |              page 5                         |
		 *    |_____________________________________________|
		 *    |                                             |
		 *    |              page 6                         |
		 *    |                                             |
		 *    |                                             |
		 *    |                                             |
		 *    |                                             |
		 *    |                                             |
		 *    |                                             |
		 *    |      o o o o o o o o o o o o o o o o o o o  |
		 *    |      o                                   o  |
		 *    |      o                                   o  |
		 *    |______o___________________________________o__|
		 *    |      o                                   o  |
		 *    |      o                                   o  |
		 *    |      o X x x x x x x x x x x x x x x x x o  |
		 *    |      o x x x x x x x x x x x x x x x x x o  |
		 *    |      o o o o o o o o o o o o o o o o o o o  |
		 *                      ...
		 * 
		 * Repaint scope: 		x
		 * BufferedImage:		o
		 * 
		 * We want to get the location of Point X in BufferedImage 
		 * and relative to the current page (page 7 in the draft).
		 * 
		 * These values and the size of the rectangle have to 
		 * match and be adapted to match the current zoom size.
		 * 
		 * The location inside the Picture (loc_poi) is in model size
		 * 
		 *    
		 */
		
		
		
		//
		// Log information on what is painted.
		//
		Console.log("refreshing PaintLabel. \nValues: "
				+ "\n\tgetSize:\t" + getPaintLabel().getSize()
				+ " vs. " + getJPnlToMove().getSize()
				+ "\n\tgetLocation:\t" + getPaintLabel().getLocation() 
				+ " vs. " + getJPnlToMove().getLocation()
				+ "\n\t" + "_x:\t\t" + _x
				+ "\n\t" + "_y\t\t" + _y
				+ "\n\t" + "_width\t\t" + _width
				+ "\n\t" + "_height\t\t" + _height + "\n",
				Console.ID_INFO_UNIMPORTANT, ContorlPicture.class, 
				"refreshRanctangle");

		
		//
		// Step 1) Compute important values such as
		// 	- the first printed point							[VIEW-SIZE]
		// 	      last
		// 	- the index of the first affected page
		// 	                   last
		
		// the start- and end-location applied to the shift of the paint-JLabel
		// in [VIEW-SIZE].
		final Point pnt_start = new Point(
				-getPaintLabel().getLocation().x + _x, 
				-getPaintLabel().getLocation().y + _y);
		final Point pnt_end = new Point(
				pnt_start.x + _width,
				pnt_start.y + _height);
		
		// The stretch-factor for converting the above values into 
		// [MODEL-SIZE]. Is saved for not having to recompute it each
		// time it is used.
		final double zoomStretch = State.getZoomFactorToModelSize();
		
		// Check which pages need to be painted. Therefore, the saved values
		// are converted into [MODEL-SIZE] and given to a method which 
		// gets the pages from pixel.
		final int firstPrintedPage = cp.getProject().getPageFromPX(
				new Point((int) (pnt_start.x * zoomStretch), 
						(int) (pnt_start.y * zoomStretch)));
		final int lastPrintedPage = cp.getProject().getPageFromPX(
				new Point((int) (pnt_end.x * zoomStretch), 
						(int) (pnt_end.y * zoomStretch)));
		
		

		// Step 2)
		// Compute the following variables.
		// - Point[]     pnt_loc_pic	 	[MODEL-SIZE],
		// - Point[]     pnt_loc_bi			[VIEW-SIZE],
		// - Dimension[] dim_size_bi		[VIEW-SIZE].
		
		// Rectangle which contains the page-print-scope of each affected
		// page (thus the pages from firstPrintPage until lastPrintedPage).
		final Point[] pnt_loc_bi = new Point[lastPrintedPage //VIEW-SIZE
	                                       - firstPrintedPage + 1];
		final Point[] pnt_loc_pic = new Point[lastPrintedPage //VIEW-SIZE
                                   - firstPrintedPage + 1];
		final Dimension [] dim_size_bi = new Dimension[lastPrintedPage 
		                                               - firstPrintedPage + 1];
		
		// Utility; contains the bounds of each affected page. [MODEL_SIZE]
		Rectangle[] pageScope = new Rectangle[lastPrintedPage 
		                                           - firstPrintedPage + 1];
		// Utility; the location of the scope in model coordinates; is computed
		// as (location of Page + location of bi @ scope) * zoomFactor
		// location of the page is negative because of the relation to the 
		// location of gui element.
		final Point pnt_locScopeMDL = new Point(
				(int) ((int) (pnt_start.x * zoomStretch)),
				(int) ((int) (pnt_start.y * zoomStretch)));
		
		//
		// Compute the first value. Afterwards, the rest of the values are 
		// calculated inside a for-loop.
		//
		pageScope[0] = cp.getProject().getPageRectanlgeinProject(firstPrintedPage);

		// The location inside the BufferedImage is pnt_start.
		pnt_loc_bi[0] = new Point(
				(int) ((int) (_x / zoomStretch) * zoomStretch),
				(int) ((int) (_y /zoomStretch) * zoomStretch));
		
		// The location inside the (first) picture inside the currently active 
		// space is equal to 
		// pnt_locScopeMDL - pageScope[0].y
		// 
		//
		pnt_loc_pic[0] = new Point(
				
				// for the sake of abstractness, the x coordinate of the page-
				// scope is contained inside the computation; for now, the 
				// page x- location is equal to 0 because there is only one 
				// page layout
				(pnt_locScopeMDL.x - pageScope[0].x),
				(pnt_locScopeMDL.y - pageScope[0].y));
		
		// for now, it is possible that the scope consists of more than one
		// page. Thus, the size of the section inside the BufferedImage is
		// set as the entire page size of the first page inside the section.
		// If the section does only consist of one page, the first dimension
		// inside the Dimension-array is also the very last one, which size
		// is adapted to the scope-size after the for-loop has terminated.
		dim_size_bi[0] = new Dimension(
				(int) (Math.min((pageScope[0].getWidth() - pnt_loc_bi[0].x),
						_width)),
				(int) Math.min(((pageScope[0].getHeight() - pnt_loc_bi[0].y)),
						_height));
		
		
		
		//
		// Inside the for-loop, the values of the four arrays
		// - (Rectangle[])	pageScope
		// - (Point[])	 	pnt_loc_pic
		// - (Point[])	 	pnt_loc_bi
		// - (Dimension[]) 	dim_size_bi
		// are computed for the succeeding pictures inside the scope. If the
		// scope does only consist of one single picture, the for loop is never
		// entered.
		for (int i = 1; i < pageScope.length; i++) {
			
			//
			// get size of current page.
			//
			final int currentPage = firstPrintedPage + i;
			
			// the size of the current page.
			PDRectangle b = cp.getProject().getDocument().getPage(currentPage).getBBox();
			
			// TODO: width has to be adapted to page width because page width may variy.
			final int height = Math.round(b.getHeight() 
					* PDFUtils.dpi / 72);
			final int width = Math.round(b.getWidth() 
					* PDFUtils.dpi / 72);

			pageScope[i] = new Rectangle(
					0, 
					pageScope[i - 1].y + pageScope[i - 1].height,
					width,
					height);
			

			pnt_loc_bi[i] = new Point(
					pnt_loc_bi[i - 1].x,
					(int) pnt_loc_bi[i - 1].y + (int) (dim_size_bi[i - 1].height));

			pnt_loc_pic[i] = new Point(
					
					// for the sake of abstractness, the x coordinate of the page-
					// scope is contained inside the computation; for now, the 
					// page x- location is equal to 0 because there is only one 
					// page layout
					pnt_locScopeMDL.x - pageScope[i].x,
					0);
			
			dim_size_bi[i] = new Dimension(
					(int) Math.min(pageScope[i].getWidth() - pnt_loc_bi[i].x, _width),
					(int) (_height - pnt_loc_bi[i].y));
		}

		// If the section does only consist of one page, the first dimension
		// inside the Dimension-array is also the very last one, which size
		// is adapted to the scope-size after the for-loop has terminated.
		// Adapt the very last size inside BufferdImage.
		dim_size_bi[dim_size_bi.length - 1].height = 
				(int) (_height 
						+ (pageScope[0].y
						- pageScope[pageScope.length - 1].y
						+ pnt_loc_pic[0].y
						- pnt_loc_pic[pageScope.length - 1].y) / zoomStretch);
		

//		if (pagePrintScope.length != 1) {

		
		
		// adapt to zoom size
		
		
		
//		}
		for (int i = 0; i < pageScope.length; i++) {
//			pnt_loc_pic[i].x = adaptToZoomSize(pnt_loc_pic[i].x);
//			pnt_loc_pic[i].y = adaptToZoomSize(pnt_loc_pic[i].y);
//			pnt_loc_bi[i].x = adaptToZoomSize(pnt_loc_bi[i].x);
//			pnt_loc_bi[i].y = adaptToZoomSize(pnt_loc_bi[i].y);
//			dim_size_bi[i].width = adaptToZoomSize(dim_size_bi[i].width);
//			dim_size_bi[i].height = adaptToZoomSize(dim_size_bi[i].height);
			
//			if (i > 0)
//			pagePrintScope[i].y = 0;
			if (dim_size_bi[i].width != 0 && dim_size_bi[i].height != 0) {
				
				if (pageScope.length >= 2) {

					System.out.println();
					System.err.println("ps" + pageScope[i]);
					System.err.println(i + ", pnt_loc_pic:\t" + pnt_loc_pic[i]);
					System.err.println(i + ", pnt_loc_bi :\t" + pnt_loc_bi[i]);
					System.err.println(i + ", dim_size_bi:\t" + dim_size_bi[i]);
					System.err.println(_height * zoomStretch);
//					System.err.println("ih" + cp.getProject().getPicture(i).getSize().height);
				} else {

					System.out.println();
					System.out.println("ps" + pageScope[i]);
					System.out.println(i + ", pnt_loc_pic:\t" + pnt_loc_pic[i]);
					System.out.println(i + ", pnt_loc_bi :\t" + pnt_loc_bi[i]);
					System.out.println(i + ", dim_size_bi:\t" + dim_size_bi[i]);
//					System.out.println("ih" + cp.getProject().getPicture(i).getSize().height);
				}
			}
		}
		

		//
		// paint the painted stuff at graphics
		//
			
		// The painted BufferedImage is not directly printed to view each time 
		// is changed, but stored inside the BufferedImage bi. That would be 
		// possible by typing
		//	<code> 	bi = ... </code>
		// instead of using the setter method which directly updates the view
		// classes, but for the sake of readability, the changes are stored 
		// inside the following reference to bi.
		BufferedImage bi_progress = getBi();

		final boolean backgroundEnabled 
		= State.isBorder();
		//TODO: update page number in project (currentPage).
		for (int i = 0; i < pageScope.length; i++) {

			final int currentPage = firstPrintedPage + i;
			String d = ("page" + currentPage + " of " 
					+ (pageScope.length + firstPrintedPage - 1));
			Console.log(d, 
					Console.ID_INFO_UNIMPORTANT, 
					getClass(), "before repaintRectangle");
			
			
			// if the pdfpage object has to be reminded of its function, do so.
			if (!cp.getProject().getDocument().getPdfPages()[currentPage]
					.checkRemember()) {

				cp.getProject().getDocument().getPdfPages()[currentPage].remind();
			}
			
			if (i < pageScope.length - 1) {
				
				// paint the lines between pages.
				final int y = (int) getPaintLabel().getLocation().getY() - _y
						+ (int) ((
								
								// this is the height of the current page.
								+ pageScope[i].height
								
								// this is the y coordinate of the page.
								+ pageScope[i].y
								) / zoomStretch);

				for (int j = 0; j < bi.getWidth(); j++) {

					bi.setRGB(j, y,
							Color.black.getRGB());
				}
			}

			pnt_loc_pic[i].x = (int) (pnt_loc_pic[i].x / zoomStretch);
			pnt_loc_pic[i].y = (int) (pnt_loc_pic[i].y / zoomStretch);
			
			//
			// Perform foreground- printing
			//
			bi_progress = cp.getProject().getPicture(currentPage).updateRectangle(
					pnt_loc_pic[i],
					pnt_loc_bi[i],
					dim_size_bi[i],
//					new Point((int) (pageScope[i].x 		/ zoomStretch),
//							(int) (pageScope[i].y 		/ zoomStretch)),
//					new Point(_x, _y),
//					new Dimension((int) (pageScope[i].width  / zoomStretch),
//							(int) (pageScope[i].height / zoomStretch)),
					bi_progress);

//			cp.getProject().getPicture(currentPage).highlightRect(
//					pnt_loc_pic[i],
//					pnt_loc_bi[i],
//					dim_size_bi[i],
//					bi_progress);
			
			//
			// Perform background-printing.
			//
//			bi_progress = (Utils.getBackground(
//					bi_progress, 
//					-getPaintLabel().getLocation().x + _x,
//					-getPaintLabel().getLocation().y + _y,
//					-getPaintLabel().getLocation().x + _x + _width,
//					-getPaintLabel().getLocation().y + _y + _height, 
//					_x, _y, cp.getProject().getPicture(currentPage).getShowSize()));

			if (backgroundEnabled) {
//				bi_progress = (Utils.getBackground(
//						bi_progress, 
//						(int) (pagePrintScope[i].x 		/ zoomStretch) - getPaintLabel().getLocation().x,
//						(int) (pagePrintScope[i].y 	+ _y	/ zoomStretch) - getPaintLabel().getLocation().y,
//						(int) ((pagePrintScope[i].x + pagePrintScope[i].width) / zoomStretch - getPaintLabel().getLocation().x),
//						(int) ((pagePrintScope[i].y + pagePrintScope[i].height) / zoomStretch - getPaintLabel().getLocation().y),
//						_x, _y, cp.getProject().getPicture(currentPage).getShowSize()));

				final int adaptedPageLocationY = getPaintLabel().getLocation().y + 
						(int) (pageScope[0].y * State.getZoomFactorToShowSize());
//				bi_progress = (Utils.getBackground(
//						bi_progress, 
//						-getPaintLabel().getLocation().x + _x,
//						-adaptedPageLocationY + _y ,
//						-getPaintLabel().getLocation().x + _x + _width,
//						-adaptedPageLocationY + _y + _height, 
//						_x, _y, cp.getProject().getPicture(currentPage).getShowSize()));
			}

			
				
		}
		
		// _x      -> pnt_start.x
		// _y      -> pnt_start.y
		// _width  -> _width
		// _height -> _height
		//pnt_start.x + 

		setBi(bi_progress);
//		refreshRectangleBackground(_x, _y, _width, _height);	
//		refreshRectangleBackground(pnt_start.x, pnt_start.y,
//		_width, _height);		
//		setBi(cp.getPicture().updateRectangle(
//				pnt_start.x, pnt_start.y,
//				_width, _height, _x, _y, getBi(), 
//				cp.getControlPic()));
		
		return getBi();
	}

//	
//	/**
//	 * repaint a special rectangle.
//	 * @param _x the x coordinate in view
//	 * @param _y the y coordinate in view
//	 * @param _width the width
//	 * @param _height the height
//	 * @return the graphics
//	 */
//	private final synchronized void refreshRectangleBackground(
//			final int _x, final int _y, 
//			final int _width, final int _height) {
//
//		final boolean backgroundEnabled 
//		= State.isBorder();
//		if (backgroundEnabled) {
//
//			State.getLogger().finest("refreshing rectangle background. \nValues: "
//					+ "\n\tgetSize:\t" + getPaintLabel().getSize()
//					+ " vs. " + getJPnlToMove().getSize()
//					+ "\n\tgetLocation:\t" + getPaintLabel().getLocation() 
//					+ " vs. " + getJPnlToMove().getLocation()
//					+ "\n\t" + "_x:\t\t" + _x
//					+ "\n\t" + "_y\t\t" + _y
//					+ "\n\t" + "_width\t\t" + _width
//					+ "\n\t" + "_height\t\t" + _height + "\n");
//
//			
////			setBi(highlightRect(
////					_x , _y , 
////					_width + 0, 
////					_height +  0, getBi()));
//
//
//			Picture pic = cp.getPicture();
//			setBi(Utils.getBackground(
//					getBi(), 
//					-getPaintLabel().getLocation().x + _x,
//					-getPaintLabel().getLocation().y + _y,
//					-getPaintLabel().getLocation().x + _x + _width,
//					-getPaintLabel().getLocation().y + _y + _height, 
//					_x, _y, pic.getShowSize()));
//			//paint the painted stuff at graphics
//			///setBi(cp.getPicture().updateRectangle(
////					-getPaintLabel().getLocation().x + _x, 
////					-getPaintLabel().getLocation().y + _y, 
////					_width, _height, _x, _y, getBi(), 
////					cp.getControlPic()));
//			
//		}
//		
//	}

	

	
	private int adaptToZoomSize(final int _y) {

		final double toModel = State.getZoomFactorToModelSize();
		int z1 = (int) (((int) _y * toModel) / toModel);
		z1 = (int) (((int) z1 / toModel) * toModel);
		
		return z1;
	}





	/**
	 * repaint the items that are in a rectangle to the (view) page (e.g. if the
	 * JLabel is moved))..
	 * 
	 * @param _x
	 *            the x coordinate
	 * @param _y
	 *            the y coordinate
	 * @param _width
	 *            the width
	 * @param _height
	 *            the height
	 * @param _bi
	 *            the BufferedImage
	 * @return the graphics
	 */
	public final synchronized BufferedImage emptyRect(
			final int _x, final int _y, 
			final int _width, final int _height, final BufferedImage _bi) {

		// check whether the rectangle concerns the blue border of the
		// image which is not to be emptied and then repainted.
		// If that's the case, the rectangle width or height are decreased.
		int rectWidth = _width, rectHeight = _height;
		if (_x + _width > _bi.getWidth()) {
			rectWidth = _bi.getWidth() - _x;
		}

		if (_y + _height > _bi.getHeight()) {
			rectHeight = _bi.getHeight() - _y;
		}

		final int maxRGB = 255;
		PaintBI.fillRectangleQuick(_bi, new Color(maxRGB, maxRGB, maxRGB, 0), 
				new Rectangle(_x, _y, rectWidth, rectHeight));

		return _bi;

	}
	

	
	/**
	 * repaint the items that are in a rectangle to the (view) page (e.g. if the
	 * JLabel is moved))..
	 * 
	 * @param _x
	 *            the x coordinate
	 * @param _y
	 *            the y coordinate
	 * @param _width
	 *            the width
	 * @param _height
	 *            the height
	 * @param _bi
	 *            the BufferedImage
	 * @return the graphics
	 */
	public final synchronized BufferedImage highlightRect(
			final int _x, final int _y, 
			final int _width, final int _height, final BufferedImage _bi) {

		// check whether the rectangle concerns the blue border of the
		// image which is not to be emptied and then repainted.
		// If that's the case, the rectangle width or height are decreased.
		int rectWidth = _width, rectHeight = _height;
		if (_x + _width > _bi.getWidth()) {
			rectWidth = _bi.getWidth() - _x;
		}

		if (_y + _height > _bi.getHeight()) {
			rectHeight = _bi.getHeight() - _y;
		}

		final int r = new Random().nextInt(255),
				g = new Random().nextInt(255),
				b = new Random().nextInt(255);
		PaintBI.fillRectangleQuick(_bi, new Color(r,g,b), 
				new Rectangle(_x, _y, rectWidth, rectHeight));

		return _bi;

	}
	/**
	 * clear a special rectangle.
	 * @param _x the x coordinate in view
	 * @param _y the y coordinate in view
	 * @param _width the width
	 * @param _height the height
	 * @return the graphics
	 */
	public final BufferedImage clrRectangle(final int _x, final int _y, 
			final int _width, final int _height) {

		State.getLogger().finest("clr PaintLabel. \nValues: "
				+ "\n\tgetSize:\t" + getPaintLabel().getSize() 
				+ " vs. " + getJPnlToMove().getSize()
				+ "\n\tgetLocation:\t" + getPaintLabel().getLocation() 
				+ " vs. " + getJPnlToMove().getLocation()
				+ "\n\t" + "_x:\t\t" + _x
				+ "\n\t" + "_y\t\t" + _y
				+ "\n\t" + "_width\t\t" + _width
				+ "\n\t" + "_height\t\t" + _height + "\n");

		//paint the painted stuff at graphics
		cp.getPicture().emptyRectangle(
				new Point(-getPaintLabel().getLocation().x + _x, 
						-getPaintLabel().getLocation().y + _y), 
				new Point(_x, _y),
				new Dimension(_width, _height), getBi());

		getPaintLabel().setIcon(new ImageIcon(getBi()));
		
		return getBi();
	}

	
	/**
	 * Not necessary.
	 */
	public void refreshPopup() {
		//not necessary anymore because paitned to bi and autorefreshes.
//		refreshPaint();
	}
	
	
	/**
	 * Paint a zoom box.
	 * 
	 * @param _x the x coordinate
	 * @param _y the y coordinate
	 * @param _width the width 
	 * @param _height the height
	 */
	public final void setZoomBox(final int _x, final int _y, 
			final int _width, final int _height) {
		paintZoom(_x, _y, _width, _height);
	}
	

	

	/**
	 * Paint line selection.
	 * @param _po the PaintObject.
	 * @param _pen the pen.
	 */
	public final void paintSelection(final PaintObject _po, 
			final PenSelection _pen) {

	
		//interrupt border thread.
		stopBorderThread();
		
		//initialize the thread and start it.
		thrd_moveBorder = new BorderThread(
				null, false, _po, _pen, getTabs(), getPage());
		thrd_moveBorder.start();
		
		//paint the background
	}
	

	/**
	 * Release selected items and add them to normal list.
	 */
	public final void releaseSelected() {
		releaseSelected(true);
	}
	
	
	
	public void releaseSelected(final boolean _changeTab) {

		// change the location of the resize - buttons. Now that the selection
		// is released, the entire page can be resized.
		cp.getcTabPaint().updateResizeLocation();
		
		// stop the border thread (if that has not already been done).
		stopBorderThread();
		
		// remove painted selection.
		BufferedImage emptyBI = Util.getEmptyBISelection();
		getPage().getJlbl_selectionBG().setIcon(new ImageIcon(emptyBI));
		getPage().getJlbl_selectionPainting().setIcon(new ImageIcon(emptyBI));

		// remove the shift of the border, selection background and painting.
		getPage().getJlbl_border().setBounds(0, 0, 0, 0);
		getPage().getJlbl_selectionBG().setLocation(0, 0);
		getPage().getJlbl_selectionPainting().setLocation(0, 0);
		
		// refresh painting at selected area.
		refreshPaint();
		
		//open the first tab if the currently opened tab is the selection tab.
        // open selection tab
		if (_changeTab && getTabs().getOpenTab() == State.getIdTabSelection()) {

	        // open selection tab
	        getTabs().openTab(0);
		}
		
	}
	
	
	/**
	 * Stop the border - thread.
	 */
	public final void stopBorderThread() {

		//close old border thread.
		if (thrd_moveBorder != null) {
			thrd_moveBorder.interrupt();
//			getTabs().repaint();
		}
	}
	
	
	/*
	 * methods for getting location
	 */


	/**
	 * @return the bi
	 */
	public final BufferedImage getBi() {
		return bi;
	}

	/**
	 * @param _bi the _bi to set
	 */
	public final void setBi(final BufferedImage _bi) {
		
		cp.getView().getPage().getJlbl_painting().setIcon(new ImageIcon(_bi));
		cp.getView().getPage().getJlbl_painting().repaint();
		this.bi = _bi;
	}





	/**
	 * {@inheritDoc}
	 */
	public final synchronized void afterLocationChange(
			final MoveEvent _evNew, final MoveEvent _evOld) {
		


		int xNew = (int) _evNew.getPnt_bottomLocation().getX();
		int yNew = (int) _evNew.getPnt_bottomLocation().getY();
		
		int xOld2 = (int) getPaintLabel().getLocation().getX();
		int yOld2 = (int) getPaintLabel().getLocation().getY();

		int xOld = (int) _evOld.getPnt_bottomLocation().getX();
		int yOld = (int) _evOld.getPnt_bottomLocation().getY();
		
		
		//hier ist irgendein shift drin!
		//wenn nach rechts oder unten geschoben wird, dann taucht das repaintete
		//auf der falschen seite auf.
		
		
		State.getLogger().finest("beforelocation change:\n"
				+ "x: " + xNew + "\t" + xOld + "\t" + xOld2
				+ "y: " + yNew + "\t" + yOld + "\t" + yOld2);
		
		//if something changed, repaint
		if (xNew != xOld || yNew != yOld) {

			if (getPaintLabel().isVisible()) {
				
				int maintainStartX = 0, 
					maintainStartY = 0, 
					maintainWidth = bi.getWidth(), 
					maintainHeight = bi.getHeight();
				

				int shiftedStartX = 0,
					shiftedStartY = 0;
				
				//move to the left (new location is smaller than the old one)
				if (xNew > xOld) {

					shiftedStartX = xNew - xOld;
					maintainStartX = 0;
					maintainWidth = bi.getWidth() - shiftedStartX;
				} else if (xNew < xOld) {

					shiftedStartX = 0;
					maintainStartX =  xOld - xNew;
					maintainWidth = bi.getWidth() - maintainStartX;
				}

				//moved up (old location is greater than new location)
				if (yNew > yOld) {
					
					shiftedStartY = yNew - yOld;
					maintainStartY = 0;
					maintainHeight = bi.getHeight() - shiftedStartY;
				} else if (yNew < yOld) {

					shiftedStartY = 0;
					maintainStartY =  yOld - yNew;
					maintainHeight = bi.getHeight() - maintainStartY;
				}
				
				/*
				 * shift the maintained stuff
				 */
				
				if (maintainWidth > 0 && maintainHeight > 0) {
					//fetch the the RGB array of the subImage which is to be 
					//maintained but moved somewhere.
					//TODO: zoom error occurs.
					int[] rgbArray = new int[maintainWidth * maintainHeight];
					rgbArray = bi.getRGB(
							maintainStartX, 
							maintainStartY, 
							maintainWidth, 
							maintainHeight, 
							rgbArray, 0, maintainWidth);

					//write the maintained RGB array to shifted coordinates.
					bi.setRGB(shiftedStartX, 
							shiftedStartY, 
							maintainWidth,
							maintainHeight, 
							rgbArray,  0, maintainWidth);
					

//					if (backgroundEnabled) {
//						//for the background the same stuff:
//						rgbArray = bi_background.getRGB(
//								maintainStartX, 
//								maintainStartY, 
//								maintainWidth, 
//								maintainHeight, 
//								rgbArray, 0, maintainWidth);
//						
//						//write the maintained RGB array to shifted coordinates.
//						bi_background.setRGB(shiftedStartX, 
//								shiftedStartY, 
//								maintainWidth,
//								maintainHeight, 
//								rgbArray,  0, maintainWidth);
//						
//						
//						
//					}
					
					/*
					 * paint the new stuff. 
					 * The rectangle location is the complement of the 
					 * maintained in size of bufferedImage.
					 */
					//Refresh both in direction of width and of height.
					//In the simplest way of doing this, there may be an area 
					//which is painted twice (depicted with '!'). 
					//For further optimization of displaying speed this should 
					//be eliminated by the way of refreshing done beneath the 
					//picture. Picture:
					//shiftedStartY == 0
					//		____________		____________
					//		| ! W W W W	|	   | H	x	  	|
					// 		| H	x x x x |	   | H	x	    |
					// 		| H	x	  	|	   | H	x	 	|
					// 		| H	x	  	|	   | H	x	 	|
					// 		| H	x	    |	   | H	x x x x	|
					//		|_H_x_______|	   |_!_W_W_W_W_ |
					//		____________		____________
					//		| W	W W W! !|		|	 x	H H	|
					// 		| x x x x H	|		|	 x	H H	|
					// 		|  	   	x H	|	    |	 x	H H	|
					// 		|	  	x H	|	    |	 x	H H	|
					// 		|	    x H	|		| x x x	H H	|
					//		|_______x_H_|		|_W_W_W_!_!_|
					/*
					 * Width
					 */
					//here the (!) are painted!
					int refreshWidthWidth = bi.getWidth();
					int refreshWidthHeight = bi.getHeight() - maintainHeight;
					int refreshWidthY = 0;
					int refreshWidthX = 0;
					
					if (shiftedStartY == 0) {
						refreshWidthY = maintainHeight;
					}
					/*
					 * height
					 */
					int refreshHeightWidth = bi.getWidth() - maintainWidth;
					int refreshHeightHeight = bi.getHeight()
							- refreshWidthHeight;
					int refreshHeightY = 0;
					int refreshHeightX = 0;
					
					if (shiftedStartX == 0) {
						refreshHeightX = maintainWidth;
					}

					//BufferedImage
					
					refreshRectangle(refreshWidthX, refreshWidthY, 
							refreshWidthWidth, refreshWidthHeight);
					refreshRectangle(refreshHeightX, refreshHeightY, 
							refreshHeightWidth, refreshHeightHeight);
					
//					refreshPaintBackground();
					
					/*
					 * for debugging purpose
					 * //TODO: put this variable into the settings. Thus it
					 * may be enabled in testing mode (tab)
					 */
					boolean showRefreshRectangle = false;

					/*
					 * 
					 */
					if (showRefreshRectangle) {
						
						/*
						 * The debugging colors and values
						 */
						final int clr_wdth = new Color(200, 240, 255).getRGB();
						final int clr_hght = new Color(150, 190, 200).getRGB();
						
						final int modulo = 10;
						final int thickness = 1;
					
						for (int x = refreshWidthX;
								x < refreshWidthX + refreshWidthWidth;
								x++) {
	
							for (int y = refreshWidthY;
									y < refreshWidthY + refreshWidthHeight;
									y++) {
								
								int valOnScreen = (x + y)  % modulo;
								
								if (valOnScreen >= 0
										&& valOnScreen < thickness) {
									bi.setRGB(x, y, clr_wdth);
								}
							}
						}
						
	
						for (int x = refreshHeightX;
								x < refreshHeightX + refreshHeightWidth;
								x++) {
	
							for (int y = refreshHeightY;
									y < refreshHeightY + refreshHeightHeight;
									y++) {

								int valOnScreen = (x + y)  % modulo;
								
								if (valOnScreen >= 0
										&& valOnScreen < thickness) {
									
									bi.setRGB(x, y, clr_hght);
								}
							}
						}	
					}
				}
			}
			
			if (!cp.getPicture().isSelected()) {

				// update the location of the resize buttons
				cp.getcTabPaint().updateResizeLocation();	
			}
		}
	}



	/**
	 * {@inheritDoc}
	 */
	public final void beforeExternalLocationChange(final MoveEvent _ev) {
		// TODO Auto-generated method stub
		
	}



	/**
	 * {@inheritDoc}
	 */
	public final void beforeExternalSizeChange(final MoveEvent _ev) {

	}



	/**
	 * {@inheritDoc}
	 */
	public final void beforeLocationChange(final MoveEvent _ev, 
			final MoveEvent _evOld) {

		cp.changeLocationSelectionOnScroll(
				_ev.getPnt_bottomLocation().x,
				_ev.getPnt_bottomLocation().y);
	}

	
	
	/*
	 * error - checked getter methods.
	 */


	/**
	 * Method for getting the view component which is error-checked.
	 * @return the MPanel jpnlToMove.
	 */
	private MPanel getJPnlToMove() {
		if (cp != null) {
			if (cp.getView() != null) {
				if (cp.getView().getPage() != null) {
					if (cp.getView().getPage().getJpnl_toMove() != null) {

						return cp.getView().getPage().getJpnl_toMove();
					} else {
						State.getLogger().severe(
								"error: cp.getView().getPage().getJpnl_toMove()"
								+ " is null");
					} 
				} else {

					State.getLogger().severe(
							"error: cp.getView().getPage() "
							+ "is null");
				}
			} else {

				State.getLogger().severe(
						"error: cp.getView() "
						+ "is null");
			}
		} else {

			State.getLogger().severe(
					"error: cp "
					+ "is null");
		}
		return null;
	}

	/**
	 * Method for getting the view component which is error-checked.
	 * @return the MPanel jpnlToMove.
	 */
	private PaintLabel getPaintLabel() {
		if (cp != null) {
			if (cp.getView() != null) {
				if (cp.getView().getPage() != null) {
					if (cp.getView().getPage().getJlbl_painting() != null) {

						return cp.getView().getPage().getJlbl_painting();
					} else {
						State.getLogger().severe(
								"error: cp.getView().getPage()"
								+ ".getJlbl_painting() "
								+ "is null");
					} 
				} else {

					State.getLogger().severe(
							"error: cp.getView().getPage() "
							+ "is null");
				}
			} else {

				State.getLogger().severe(
						"error: cp.getView() "
						+ "is null");
			}
		} else {

			State.getLogger().severe(
					"error: cp "
					+ "is null");
		}
		return null;
	}
	
	
	
	
	
	
	
	
	
	
	
	
	

	/**
	 * {@inheritDoc}
	 */
	public final void afterExternalBoundsChange(
			final MoveEvent _evOldLoc, final MoveEvent _evOldSize) {

		if (getPaintLabel().isVisible()) {
			

			cp.setBi_preprint(
					new BufferedImage(
							Math.max(ViewSettings.getView_bounds_page()
									.getSize().width, 1),
							Math.max(ViewSettings.getView_bounds_page()
									.getSize().height,
									1),
							BufferedImage.TYPE_INT_ARGB));
//			cp.getControlPic().setBi_background(
//					new BufferedImage(
//							Math.max(ViewSettings.getView_bounds_page()
//									.getSize().width, 1),
//							Math.max(ViewSettings.getView_bounds_page()
//									.getSize().height,
//									1),
//							BufferedImage.TYPE_INT_ARGB));
			cp.getControlPic().setBi(new BufferedImage(
							Math.max(ViewSettings.getView_bounds_page()
									.getSize().width, 1),
							Math.max(ViewSettings.getView_bounds_page()
									.getSize().height, 
									1),
							BufferedImage.TYPE_INT_ARGB));

			
			//set changed
			refreshPaint();

			if (cp.getPicture().isSelected()) {

				// update the location of the resize buttons
				cp.getcTabPaint().updateResizeLocation();	
			}
		}		
	}


	/**
	 * {@inheritDoc}
	 */
	public final void afterExternalLocationChange(final MoveEvent _ev) {

		if (getPaintLabel().isVisible()) {

			//set changed
			refreshPaint();

			if (!cp.getPicture().isSelected()) {

				// update the location of the resize buttons
				cp.getcTabPaint().updateResizeLocation();	
			}
		}		
	}



	/**
	 * {@inheritDoc}
	 */
	public final void afterExternalSizeChange(final MoveEvent _ev) {

		if (getPaintLabel().isVisible()) {


			//re-initialize the image with the correct size
			cp.setBi_preprint(
					new BufferedImage(
							Math.max(ViewSettings.getView_bounds_page()
									.getSize().width, 1),
							Math.max(ViewSettings.getView_bounds_page()
									.getSize().height,
									1),
							BufferedImage.TYPE_INT_ARGB));
//			cp.getControlPic().setBi_background(
//					new BufferedImage(
//							Math.max(ViewSettings.getView_bounds_page()
//									.getSize().width, 1),
//							Math.max(ViewSettings.getView_bounds_page()
//									.getSize().height,
//									1),
//							BufferedImage.TYPE_INT_ARGB));
			cp.getControlPic().setBi(new BufferedImage(
							Math.max(ViewSettings.getView_bounds_page()
									.getSize().width, 1),
							Math.max(ViewSettings.getView_bounds_page()
									.getSize().height, 
									1),
							BufferedImage.TYPE_INT_ARGB));
			
			//set changed
			refreshPaint();

			if (!cp.getPicture().isSelected()) {

				// update the location of the resize buttons
				cp.getcTabPaint().updateResizeLocation();	
			}
		}		
	}


}
