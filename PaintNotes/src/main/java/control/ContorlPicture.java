package control;

import java.awt.Color;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

import javax.swing.ImageIcon;

import control.interfaces.MoveEvent;
import control.interfaces.PaintListener;
import model.objects.painting.Picture;
import model.objects.painting.po.PaintObject;
import model.objects.pen.special.PenSelection;
import model.settings.Status;
import model.util.Util;
import model.util.paint.Utils;
import start.test.BufferedViewer;
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
	 * Point is subtracted from new location of item JLabel.
	 */
	@SuppressWarnings("unused")
	private Point pnt_start;

	
	
	
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

	

	private MPanel getJPnlToMove() {
		return cp.getView().getPage().getJpnl_toMove();
	}
	private PaintLabel getPaintLabel() {
		return cp.getView().getPage().getJlbl_painting();
	}
	/**
	 * Refresh the entire image.
	 */
	public final void refreshPaint() {


		Status.getLogger().warning("refreshing entire PaintLabel. \nValues: "
				+ "\n\tgetSize:\t" + getPaintLabel().getSize() + " vs. " 
				+ getJPnlToMove().getSize()
				+ "\n\tgetLocation:\t" + getPaintLabel().getLocation() 
				+ " vs. " + getJPnlToMove().getLocation()
				+ "\n\t" + "_x:\t\t"
				+ "\n\t" + "_y\t\t"
				+ "\n\t" + "_width\t\t" + getPaintLabel().getWidth()
				+ "\n\t" + "_height\t\t" + getPaintLabel().getHeight() + "\n");

		//paint the painted stuff at graphics
		setBi(Picture.getInstance().updateRectangle(
				-getPaintLabel().getLocation().x, 
				-getPaintLabel().getLocation().y, getPaintLabel().getWidth() ,
				getPaintLabel().getHeight(), 0, 0, getBi(), 
				cp.getControlPic()));

		
		refreshPaintBackground();
		
		getPaintLabel().setIcon(new ImageIcon(getBi()));
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
		if (getPage().getJlbl_background2().getWidth() != 0
				&& getPage().getJlbl_background2().getHeight() != 0) {

			
			BufferedImage ret = new BufferedImage(
					getPage().getJlbl_background2().getWidth(),
					getPage().getJlbl_background2().getHeight(),
					BufferedImage.TYPE_INT_ARGB);
			ret = Picture.getInstance().emptyRectangle(
					-getPaintLabel().getLocation().x, 
					-getPaintLabel().getLocation().y, 
					getPaintLabel().getWidth(), 
					getPaintLabel().getHeight(), 0, 0, ret);
			getPage().getJlbl_background2().setIcon(
					new ImageIcon((Utils.getBackground(ret, -getPaintLabel().getLocation().x, 
					-getPaintLabel().getLocation().y, -getPaintLabel().getLocation().x + getPaintLabel().getWidth(), 
					-getPaintLabel().getLocation().y + getPaintLabel().getHeight(), 0, 0))));  
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
		thrd_moveBorder = new BorderThread(_r, true, null, null, getTabs(), getPage());
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
	 * repaint a special rectangle.
	 * @param _x the x coordinate in view
	 * @param _y the y coordinate in view
	 * @param _width the width
	 * @param _height the height
	 * @return the graphics
	 */
	public final BufferedImage refreshRectangle(final int _x, final int _y, 
			final int _width, final int _height) {

		Status.getLogger().finest("refreshing PaintLabel. \nValues: "
				+ "\n\tgetSize:\t" + getPaintLabel().getSize()
				+ " vs. " + getJPnlToMove().getSize()
				+ "\n\tgetLocation:\t" + getPaintLabel().getLocation() 
				+ " vs. " + getJPnlToMove().getLocation()
				+ "\n\t" + "_x:\t\t" + _x
				+ "\n\t" + "_y\t\t" + _y
				+ "\n\t" + "_width\t\t" + _width
				+ "\n\t" + "_height\t\t" + _height + "\n");

		//paint the painted stuff at graphics
		setBi(Picture.getInstance().updateRectangle(
				-getPaintLabel().getLocation().x + _x, 
				-getPaintLabel().getLocation().y + _y, 
				_width, _height, _x, _y, getBi(), 
				cp.getControlPic()));
		
		return getBi();
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

		Status.getLogger().finest("clr PaintLabel. \nValues: "
				+ "\n\tgetSize:\t" + getPaintLabel().getSize() + " vs. " + getJPnlToMove().getSize()
				+ "\n\tgetLocation:\t" + getPaintLabel().getLocation() 
				+ " vs. " + getJPnlToMove().getLocation()
				+ "\n\t" + "_x:\t\t" + _x
				+ "\n\t" + "_y\t\t" + _y
				+ "\n\t" + "_width\t\t" + _width
				+ "\n\t" + "_height\t\t" + _height + "\n");

		//paint the painted stuff at graphics
		setBi(Picture.getInstance().emptyRectangle(
				-getPaintLabel().getLocation().x + _x, 
				-getPaintLabel().getLocation().y + _y, _width, _height, _x, _y, getBi()));

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
	public void releaseSelected() {

		for (int i = 0; i < getPage().getJbtn_resize().length; i++) {
			for (int j = 0; j < getPage().getJbtn_resize()[i].length; j++) {
				int width = getPage().getJbtn_resize()[i][j].getWidth();

				getPage().getJbtn_resize()[i][j].setLocation(-width - 1, -1);
			}
		}
		//method for setting the MButtons to the size of the entire image.
		cp.getcTabPaint().updateResizeLocation();
		
		stopBorderThread();
		
		BufferedImage emptyBI = Util.getEmptyBISelection();
		getPage().getJlbl_selectionBG().setIcon(new ImageIcon(emptyBI));
		getPage().getJlbl_selectionPainting().setIcon(new ImageIcon(emptyBI));
		getPage().getJlbl_selectionPainting().repaint();

		getPage().getJlbl_border().setBounds(0, 0, 0, 0);
		getPage().getJlbl_selectionBG().setLocation(0, 0);
		getPage().getJlbl_selectionPainting().setLocation(0, 0);
		
		refreshPaint();
	}
	
	
	/**
	 * Stop the border - thread.
	 */
	public final void stopBorderThread() {

		//close old border thread.
		if (thrd_moveBorder != null) {
			thrd_moveBorder.interrupt();
			getTabs().repaint();
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
		
		BufferedViewer.show(_bi);
		cp.getView().getPage().getJlbl_painting().setIcon(new ImageIcon(_bi));
		cp.getView().getPage().getJlbl_painting().repaint();
		this.bi = _bi;
	}





	/**
	 * {@inheritDoc}
	 */
	public final void afterLocationChange(
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
		
		System.out.println("beforelocation cahnge");
		System.out.println("x: " + xNew + "\t" + xOld + "\t" + xOld2);
		System.out.println("y: " + yNew + "\t" + yOld + "\t" + yOld2);
		System.out.println();
		
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
					refreshPaintBackground();
					refreshRectangle(refreshWidthX, refreshWidthY, 
							refreshWidthWidth, refreshWidthHeight);
					refreshRectangle(refreshHeightX, refreshHeightY, 
							refreshHeightWidth, refreshHeightHeight);
					
					
					
					
					/*
					 * for debugging purpose
					 */
					boolean showRefreshRectangle = !(true == false);

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
								
								int valOnScreen = (x + y)  % modulo ;
								
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

								int valOnScreen = (x + y)  % modulo ;
								
								if (valOnScreen >= 0
										&& valOnScreen < thickness) {
									
									bi.setRGB(x, y, clr_hght);
								}
							}
						}	
					}
				}
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
	public final void beforeLocationChange(final MoveEvent _ev, final MoveEvent _evOld) {
		// TODO Auto-generated method stub
		
	}



	/**
	 * {@inheritDoc}
	 */
	public final void afterExternalLocationChange(final MoveEvent _ev) {

		if (getPaintLabel().isVisible()) {
			
			//set changed
			refreshPaint();
		}		
	}



	/**
	 * {@inheritDoc}
	 */
	public final void afterExternalSizeChange(final MoveEvent _ev) {

		if (getPaintLabel().isVisible()) {
			
			//set changed
			refreshPaint();
		}		
	}

}
