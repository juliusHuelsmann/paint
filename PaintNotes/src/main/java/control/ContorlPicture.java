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
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

import javax.swing.ImageIcon;

import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.tools.ant.property.GetProperty;

import control.forms.BorderThread;
import control.interfaces.MoveEvent;
import control.interfaces.PaintListener;
import model.objects.painting.PaintBI;
import model.objects.painting.po.PaintObject;
import model.objects.pen.special.PenSelection;
import model.settings.State;
import model.settings.ViewSettings;
import model.util.Util;
import model.util.paint.Utils;
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
	private BufferedImage bi, bi_background;
	
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
	
	
	/**
	 * Refresh the background of painting.
	 *  //TODO: quicker! like in repainting of painted content.
	 */
	private void refreshPaintBackground() {

		//paint the painting background (e.g. raster / lines) at the graphical
		//user interface.
		if (getPage().getJlbl_backgroundStructure().getWidth() != 0
				&& getPage().getJlbl_backgroundStructure().getHeight() != 0) {

			
			bi_background = new BufferedImage(
					Math.max(
							ViewSettings.getView_bounds_page().getSize().width,
							1),
					Math.max(
							ViewSettings.getView_bounds_page().getSize().height,
							1),
					BufferedImage.TYPE_INT_ARGB);
			
			
			bi_background = emptyRect(0, 0,
					bi_background.getWidth(), 
					bi_background.getHeight(), bi_background);
			
			bi_background = Utils.getBackground(
					bi_background, 
					-getPaintLabel().getLocation().x, 
					-getPaintLabel().getLocation().y, 
					-getPaintLabel().getLocation().x
					+ getPaintLabel().getWidth(), 
					-getPaintLabel().getLocation().y 
					+ getPaintLabel().getHeight(),
					0, 0);
			getPage().getJlbl_backgroundStructure().setIcon(
					new ImageIcon(bi_background));  
		}

	}
	
	
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
		refreshPaintBackground();
		getPaintLabel().setIcon(new ImageIcon(getBi()));
	}





	/**
	 * repaint a special rectangle.
	 * @param _x the x coordinate in view
	 * @param _y the y coordinate in view
	 * @param _width the width
	 * @param _height the height
	 * @return the graphics
	 */
	public final BufferedImage refreshRectangle(final int _x, final int _y, 
			final int _width, final int _height) {

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
		// save values
		//
		final Point pnt_start = new Point(
				-getPaintLabel().getLocation().x + _x, 
				-getPaintLabel().getLocation().y + _y);
		final Point pnt_end = new Point(
				pnt_start.x + _width,
				pnt_start.y + _height);
		final double zoomStretchW = 1.0 * State.getImageSize().width 
				/ State.getImageShowSize().width,
				zoomStretchH = 1.0 * State.getImageSize().height 
				/ State.getImageShowSize().height;
		
		// 
		// Check which pages need to be painted. Therefore, the saved values 
		// have to be converted into [model-size]
		//
		final int firstPrintedPage = cp.getProject().getPageFromPX(
				new Point((int) (pnt_start.x * zoomStretchW), 
						(int) (pnt_start.y * zoomStretchH)));
		final int lastPrintedPage = cp.getProject().getPageFromPX(
				new Point((int) (pnt_end.x * zoomStretchW), 
						(int) (pnt_end.y * zoomStretchH)));
		
		Rectangle[] pagePrintScope = new Rectangle[lastPrintedPage 
		                                           - firstPrintedPage + 1];
		Point [] yOfPageScope = new Point [lastPrintedPage 
                                       - firstPrintedPage + 1];
		
		pagePrintScope[0] = cp.getProject().getPageRectanlgeinProject(firstPrintedPage);
		yOfPageScope[0] = new Point(pagePrintScope[0].y, pagePrintScope[0].height);
		pagePrintScope[0].height = pagePrintScope[0].height - (int) (pnt_start.y * zoomStretchH);
		pagePrintScope[0].y = (int) (pnt_start.y * zoomStretchH) - pagePrintScope[0].y;
		pagePrintScope[0].x = (int) (pnt_start.x * zoomStretchW);
		pagePrintScope[0].width = (int) (_width * zoomStretchW);
		for (int i = 1; i < pagePrintScope.length; i++) {
			
			//
			// get size of current page.
			//
			final int currentPage = firstPrintedPage + i;
			PDRectangle b = cp.getProject().getDocument().getPage(currentPage).getBBox();
			// TODO: width has to be adapted to page width because page width may variy.
//			final int width = Math.round(b.getWidth() 
//					* PDFUtils.dpi / 72);
			final int height = Math.round(b.getHeight() 
					* PDFUtils.dpi / 72);

			yOfPageScope[i] = new Point(yOfPageScope[i - 1].x + yOfPageScope[i - 1].y, (int) b.getHeight()) ;
			pagePrintScope[i] = new Rectangle(
					(int) (pnt_start.x * zoomStretchW), 
					pagePrintScope[i - 1].y + pagePrintScope[i - 1].height,
					(int) (_width * zoomStretchW), height);
			
		}
		
		
		
//		if (pagePrintScope.length != 1) {

			// bei dem letzten Element die height korrigieren
			pagePrintScope[pagePrintScope.length - 1].height = 
					(int) (_height * zoomStretchH
					- pagePrintScope[pagePrintScope.length - 1].y
					+ pagePrintScope[0].y);
			
//		}

		//
		// paint the painted stuff at graphics
		//

		//TODO: update page number in project (currentPage).
		for (int i = 0; i < pagePrintScope.length; i++) {

			final int currentPage = firstPrintedPage + i;
			String d = ("page" + currentPage + " of " + (pagePrintScope.length + firstPrintedPage - 1));
			Console.log(d, 
					Console.ID_INFO_UNIMPORTANT, 
					getClass(), "before repaintRectangle");
			
			
			// if the pdfpage object has to be reminded of its function, do so.
			if (!cp.getProject().getDocument().getPdfPages()[currentPage].checkRemember()) {

				cp.getProject().getDocument().getPdfPages()[currentPage].remind();
			}
			
			if (i < pagePrintScope.length - 1) {
				
				final int y = (int) getPaintLabel().getLocation().getY() - _y
						+ (int) ((
								
								// this is the height of the current page.
								+ yOfPageScope[i].y
								
								// this is the y coordinate of the page.
								+ yOfPageScope[i].x
								) / zoomStretchW);

				for (int j = 0; j < bi.getWidth(); j++) {

					bi.setRGB(j, y,
							Color.black.getRGB());
				}
			}
			
			setBi(cp.getProject().getPicture(currentPage).updateRectangle(
					(int) (pagePrintScope[i].x 		/ zoomStretchW),
					(int) (pagePrintScope[i].y 		/ zoomStretchH),
					(int) (pagePrintScope[i].width  / zoomStretchW),
					(int) (pagePrintScope[i].height / zoomStretchH),
					_x, _y, getBi(), 
					cp.getControlPic()));
		}
		
//		setBi(cp.getPicture().updateRectangle(
//				pnt_start.x, pnt_start.y,
//				_width, _height, _x, _y, getBi(), 
//				cp.getControlPic()));
		
		return getBi();
	}

	
	/**
	 * repaint a special rectangle.
	 * @param _x the x coordinate in view
	 * @param _y the y coordinate in view
	 * @param _width the width
	 * @param _height the height
	 * @return the graphics
	 */
	public final synchronized BufferedImage refreshRectangleBackground(
			final int _x, final int _y, 
			final int _width, final int _height) {

		State.getLogger().finest("refreshing rectangle background. \nValues: "
				+ "\n\tgetSize:\t" + getPaintLabel().getSize()
				+ " vs. " + getJPnlToMove().getSize()
				+ "\n\tgetLocation:\t" + getPaintLabel().getLocation() 
				+ " vs. " + getJPnlToMove().getLocation()
				+ "\n\t" + "_x:\t\t" + _x
				+ "\n\t" + "_y\t\t" + _y
				+ "\n\t" + "_width\t\t" + _width
				+ "\n\t" + "_height\t\t" + _height + "\n");

		
		bi_background = emptyRect(
				_x , _y , 
				_width + 0, 
				_height +  0, bi_background);

		
		bi_background = Utils.getBackground(
				bi_background, 
				-getPaintLabel().getLocation().x + _x,
				-getPaintLabel().getLocation().y + _y,
				-getPaintLabel().getLocation().x + _x + _width,
				-getPaintLabel().getLocation().y + _y + _height, 
				_x, _y);
		getPage().getJlbl_backgroundStructure().setIcon(
				new ImageIcon(bi_background));
								

		//paint the painted stuff at graphics
		///setBi(cp.getPicture().updateRectangle(
//				-getPaintLabel().getLocation().x + _x, 
//				-getPaintLabel().getLocation().y + _y, 
//				_width, _height, _x, _y, getBi(), 
//				cp.getControlPic()));
		
		return getBi();
	}

	
	/**
	 * repaint a special rectangle.
	 * @param _x the x coordinate in view
	 * @param _y the y coordinate in view
	 * @param _width the width
	 * @param _height the height
	 * @return the graphics
	 */
	public final synchronized BufferedImage refreshRectangleBackground(
			final int _x, final int _y, 
			final int _width, final int _height,
			BufferedImage _bi_origin) {

		State.getLogger().finest("refreshing rectangle background. \nValues: "
				+ "\n\tgetSize:\t" + getPaintLabel().getSize()
				+ " vs. " + getJPnlToMove().getSize()
				+ "\n\tgetLocation:\t" + getPaintLabel().getLocation() 
				+ " vs. " + getJPnlToMove().getLocation()
				+ "\n\t" + "_x:\t\t" + _x
				+ "\n\t" + "_y\t\t" + _y
				+ "\n\t" + "_width\t\t" + _width
				+ "\n\t" + "_height\t\t" + _height + "\n");

		
		BufferedImage bi_background = _bi_origin;
		bi_background = Utils.getBackground(
				bi_background, 
				-getPaintLabel().getLocation().x + _x,
				-getPaintLabel().getLocation().y + _y,
				-getPaintLabel().getLocation().x + _x + _width,
				-getPaintLabel().getLocation().y + _y + _height, 
				_x, _y);
		
		getPage().getJlbl_painting().setIcon(
				new ImageIcon(bi_background));
								
		return getBi();
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
		setBi(cp.getPicture().emptyRectangle(
				-getPaintLabel().getLocation().x + _x, 
				-getPaintLabel().getLocation().y + _y, _width, 
				_height, _x, _y, getBi()));

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
					
					final boolean backgroundEnabled 
					= State.isBorder();

					if (backgroundEnabled) {
						//for the background the same stuff:
						rgbArray = bi_background.getRGB(
								maintainStartX, 
								maintainStartY, 
								maintainWidth, 
								maintainHeight, 
								rgbArray, 0, maintainWidth);
						
						//write the maintained RGB array to shifted coordinates.
						bi_background.setRGB(shiftedStartX, 
								shiftedStartY, 
								maintainWidth,
								maintainHeight, 
								rgbArray,  0, maintainWidth);
						
						
						
					}
					
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
					
					if (backgroundEnabled) {

						refreshRectangleBackground(refreshWidthX, refreshWidthY, 
								refreshWidthWidth, refreshWidthHeight);
						
						refreshRectangleBackground(refreshHeightX, refreshHeightY, 
								refreshHeightWidth, refreshHeightHeight);
						
					}
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
		// TODO Auto-generated method stub
		
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
			cp.getControlPic().setBi_background(
					new BufferedImage(
							Math.max(ViewSettings.getView_bounds_page()
									.getSize().width, 1),
							Math.max(ViewSettings.getView_bounds_page()
									.getSize().height,
									1),
							BufferedImage.TYPE_INT_ARGB));
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
			cp.getControlPic().setBi_background(
					new BufferedImage(
							Math.max(ViewSettings.getView_bounds_page()
									.getSize().width, 1),
							Math.max(ViewSettings.getView_bounds_page()
									.getSize().height,
									1),
							BufferedImage.TYPE_INT_ARGB));
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



	/**
	 * @return the bi_background
	 */
	public final BufferedImage getBi_background() {
		return bi_background;
	}



	/**
	 * @param _bi_background the bi_background to set
	 */
	public final void setBi_background(final BufferedImage _bi_background) {
		this.bi_background = _bi_background;
	}

}
