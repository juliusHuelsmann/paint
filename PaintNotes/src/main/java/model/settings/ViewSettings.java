//package declaration
package model.settings;


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
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.io.ObjectInputStream.GetField;

import javax.swing.BorderFactory;
import javax.swing.border.Border;

import control.ContorlPicture;
import control.ControlPaint;

/**
 * 
 * @author Julius Huelsmann
 * @version %I% %U%
 */
public final class ViewSettings {

    
    //constants that are used.
	
    /**
     * constants.
     */
    private static final int 
    THREE = 3, FIVE = 5, SEVEN = 7, TEN = 10, TWENTY_FIFE = 25;
    

    /**
     * minimal widht and height of the window.
     */
    private static final int MIN_WIDTH_WINDOW = 500, MIN_HEIGHT_WINDOW = 300;
    
    /**
	 * Distance between items and window borders (for being able to move and 
	 * resize).
	 */
	public static final int DISTANCE_TO_WINDOW = FIVE;

    /**
     * the size of the scrollPanel.
     */
    public static final int VIEW_SIZE_SP = 30;
	//class selection

    /**
     * The maximal zoom in and the maximal zoom out.
     */
    public static final int MAX_ZOOM_IN = SEVEN, MAX_ZOOM_OUT = THREE;
    
    /*
     * general stuff.
     */
    
    /**
     * colors.
     */
    public static final Color 
    GENERAL_CLR_BACKGROUND_DARK = new Color(245, 246, 253), 
    GENERAL_CLR_BACKGROUND_GREEN = new Color(224, 240, 250), //(218, 233, 244), 
    GENERAL_CLR_BACKGROUND_DISABLED = new Color(245, 220, 240),
    GENERAL_CLR_BACKGROUND_DARK_X = new Color(236, 237, 238),
    GENERAL_CLR_BACKGROUND_DARK_XX = new Color(226, 227, 228),
    GENERAL_CLR_BORDER = new Color(218, 219, 220),
    GENERAL_CLR_ITEM1BUTTON_BACKGROUND = new Color(212, 229, 255),
    GENERAL_CLR_ITEM1BUTTON_BACKGROUND_NORMAL = GENERAL_CLR_BACKGROUND_DARK, 
    GENERAL_CLR_BACKGROUND = new Color(246, 246, 246), 
    GENERAL_CLR_BACKGROUND_LIGHT = new Color(250, 250, 250),
    CLR_BACKGROUND_MENU_1_OPEN = new Color(235, 232, 237),
//    GENERAL_CLR_BACKGROUND_DARK_2 = new Color(240, 237, 242),
    GENERAL_CLR_BACKGROUND_LIGHT_FOCUS = new Color(245, 246, 247);

    /**
     * headline font which can be used everywhere.
     * 
     * usages:
     *      PaintObject  at Constructor
     */
    public static final Font 
    GENERAL_FONT_HEADLINE_1 = new Font("Courier New", Font.BOLD, 19),
    GENERAL_FONT_HEADLINE_2 = new Font("Courier New", Font.BOLD, 15),
    GENERAL_FONT_ITEM1_BUTTON = new Font("Comic Sans MS", Font.PLAIN, 10),
    GENERAL_TP_FONT_INFORMATION = new Font("Courier new", Font.ITALIC, 12),
    GENERAL_FONT_ITEM_PLAIN = new Font("Courier New", Font.PLAIN, 11),
    GENERAL_FONT_ITEM_SMALL = new Font("Courier New", Font.PLAIN, 10),
    GENERAL_FONT_ITEM = new Font("Courier New", Font.ITALIC, 11);
    
    
    
    /*
     * 
     *
     * Selection stuff
     */

    
    /**
     * distances for items.
     */
    private static int 
    distanceAfterLine = THREE, 
    distanceBeforeLine = 2, 
    distanceBetweenItems = THREE;
    
    /**
     * array of colors of border in selection.
     * If changed, reinitialize border in Selection.
     */
    public static final Color[] SELECTION_BORDER_CLR_BORDER = new Color[] { 
            Color.darkGray, Color.white };
    
    
    /**
     * size of a border block in selection.
     * If changed, reinitialize border in Selection.
     */
    public static final int SELECTION_BORDER_BLOCK_SIZE = 9;
    
    /**
     * time to sleep before recreating border.
     * If changed, reinitialize border in Selection.
     */
    public static final int SELECTION_BORDER_SLEEP_TIME = 100;
    
    /**
     * the amount of pixel changed in one run.
     * If changed, reinitialize border in Selection.
     */
    public static final int SELECTION_BORDER_MOVE_SPEED_PX = 1;
    

    /**
     * array of colors of background raster in selection.
     * If changed, reinitialize background in Selection.
     */
    public static final Color[] SELECTION_BACKGROUND_CLR = new Color[] { 
        new Color(120, 125, 140, 15), new Color(30, 30, 50, 15) };
    /**
     * color for simple background of selection (shown if resizing).
     * If changed, reinitialize background in Selection.
     */
    public static final Color SELECTION_BACKGROUND_CLR_SIMPLE = 
            new Color(25, 25, 255, 40);
    
    /**
     * size of one background raster in selection.
     * If changed, reinitialize background in Selection.
     */
    public static final int SELECTION_BACKGROUND_BLOCK_SIZE = 9;
    
    
    
    /*
     * general
     */

    
    /**
     * Whether fullscreen or not.
     */
    private static boolean fullscreen = false;
    //sizes
    
    
    /**
     * The standard sizes of JFrame in different modes.
     */
    private static Dimension 
    standard_size_jframe_normalSize = new Dimension(
    		Math.max(
    				(int) (Toolkit.getDefaultToolkit().getScreenSize()
    						.getWidth()) * 2 / 3, 
    				MIN_WIDTH_WINDOW), 
    		Math.max(
    				(int) (Toolkit.getDefaultToolkit().getScreenSize()
    						.getHeight()) * 2 / 3,
    				MIN_HEIGHT_WINDOW)),
    				
    standard_size_jframe_fullscreen = new Dimension(
    		(int) (Toolkit.getDefaultToolkit().getScreenSize().getWidth()), 
    		(int) (Toolkit.getDefaultToolkit().getScreenSize().getHeight()));


    
    /**
     * The size of the opened color panel.
     */
    private static Dimension sizePnlClr = new Dimension(275, 365);

    
    

    /**
     * The size of the message dialog opened in case of a warning, an error or
     * just for information purpose.
     */
    private static Dimension sizeMessage = new Dimension(500, 40);

    
    
    
    /**
     * .
     */
    private static Point messageLocation
    = new Point((getSize_jframe().width - getSizeMessage().width) / 2, 
            getSize_jframe().height - getSizeMessage().height 
            - 2 * 2 * 2 * 2 * 2);


    

    /**
     * the width of the TabbedPane.
     */
    private static int viewWidthTb = getSize_jframe().width - 2
            - DISTANCE_TO_WINDOW * 2 - VIEW_SIZE_SP;

    /**
     * the height of the TabbedPane.
     */
    private static int viewHeightTB = getSize_jframe().height - 1;



    /**
     * The size of the tabbedPane opener.
     */
    private static int viewHeightTBOpener =
            getView_heightTB_visible() / (2 + 1) / (2 + 1);
    /**
     * the bounds of the PaointObject.
     */
    private static Rectangle viewBoundsPo = new Rectangle(
            2, 180, 190, 560);



    /**
     * The size of the title JButton getSize() / the size of title JButton
     * is the real size.
     */
    public static final int TABBED_PANE_TITLE_PROPORTION_WIDTH = 15,
           TABBED_PANE_TITLE_PROPORTION_HEIGHT = 20;

    
    /**
	 * The bounds of the TabbedPanel.
	 */
	public static final Point VIEW_LOCATION_TB = new Point(
			DISTANCE_TO_WINDOW, DISTANCE_TO_WINDOW);


    /**
     * the bounds of the Page.
     */
    private static Rectangle viewBoundsPageOpen = new Rectangle(
            VIEW_LOCATION_TB.x,  getView_heightTB_visible() 
            + ViewSettings.getView_heightTB_opener() + VIEW_LOCATION_TB.y, 
            getSize_jframe().width - TWENTY_FIFE - TEN - 2, 
            
//            getSize_jframe().height - getSize_jframe().height / FIFE 
//            - 2 * VIEW_SIZE_SP - 2 * TWENTY_FIFE);
    
    		getSize_jframe().height - (getView_heightTB_visible() 
            + ViewSettings.getView_heightTB_opener() 
            + VIEW_LOCATION_TB.y + VIEW_SIZE_SP));
            //the last 25 is the size 
    /**
     * the bounds of the Page.
     */
    private static Rectangle view_bounds_page_closed = new Rectangle(
            viewBoundsPageOpen.x, 
            viewBoundsPageOpen.y - getView_heightTB_visible(), 
            viewBoundsPageOpen.width, 
            viewBoundsPageOpen.height + getView_heightTB_visible() 
            - TWENTY_FIFE);
    
    /**
     * the bounds of the Page.
     */
    private static Rectangle view_bounds_page = new Rectangle(
            viewBoundsPageOpen);

    

    /**
     * The size of the new - JPanel.
     */
    private static Dimension viewSizeNew = new Dimension(
    		(int) Toolkit.getDefaultToolkit().getScreenSize().getWidth()
    	    * (2 + 2 + 1) / (2 + 2 + 2) / 2 + (2 + 1) * distanceBetweenItems, 
    	    
    	    
    	    (int) (
    	    		//the width
    	    		(int) Toolkit.getDefaultToolkit().getScreenSize().getWidth()
    	    	    / (2 + 2 + 2) + (2 + 2 + 2) * distanceBetweenItems
    	    	    
    	    		/ Math.sqrt(2) + 2 * distanceBetweenItems));

    
    /**
     * The default location of the new - JPanel.
     */
    private static Point viewLocationNew = new Point(
    		(view_bounds_page.width - viewSizeNew.width) / 2, 
    		(view_bounds_page.height - viewSizeNew.height) / 2);
    
    
    /**
     * width of item1 menues.
     */
    private static int itemMenu1Width 
    = (int) (ViewSettings.getItemWidth() * 1.4),
    itemMenu1Height = 2 * ViewSettings.getItemHeight();
    
    /**
     * The size of the history.
     */
    public static final Dimension SIZE_OF_HISTORY = new Dimension(200, 200);

    
    /**
     * The background colors of the zoom.
     */
    public static final Color[] ZOOM_CLR_BG = {
        new Color(244, 0, 0, 15), 
        new Color(255,  100, 100, 15)};

    
    /**
     * The border of the zoom.
     */
    public static final Border ZOOM_BORDER 
    = BorderFactory.createEtchedBorder(new Color(200, 200, 200), 
            new Color(240, 240, 240)),
            BRD_ITEM1BUTTON = BorderFactory.createLineBorder(
                    new Color(98, 162, 228));

    
    /**
     * The amount of pieces in which the page is cut in each dimension
     * with each zoom.
     */
    public static final double getZoomMultiplicator() {
    	
    	return 1.0 * ZOOM_MULTIPLICATOR_NUMINATOR / ZOOM_MULTIPLICATOR_DENUMINATOR;
    }
    
    public static final boolean isZoomIn() {
    	return State.getZoomFactorToModelSize() <= 1;
    }
    
    /**
     * this function is used for deciding whether to ceil or floor
     * the double value which is computed to be the new width or height
     * of the picture. 
     * 
     * The value is to alternate between -1 and 1 (for neither getting too much
     * smaller than the original size that can be used nor getting too big 
     * large to be displayed entirely.
     * 
     * @return
     */
    public static final boolean isZoomCeil() {
    	return Math.abs(State.getZoomState()) % 2 == 0;
    }
    
    public static final boolean isZoomOut() {
    	return !isZoomIn();
    }
    
    
    public static final double ZOOM_MULTIPLICATOR_DENUMINATOR = 2;
    public static final double ZOOM_MULTIPLICATOR_NUMINATOR = 3;
    
    
    /**
     * the size of the zoom.
     */
    public static final Dimension ZOOM_SIZE 
    = new Dimension(
    		(int) (getView_bounds_page().width / getZoomMultiplicator()), 
            (int) (getView_bounds_page().height / getZoomMultiplicator()));
    
    
    /**
     * utility class constructor.
     */
    private ViewSettings() { }

    
    
    
    /**
     * Recalculate the contents after the image size (sizeJframe has been 
     * changed).
     */
    private static void recalculate() {
         messageLocation
        = new Point((getSize_jframe().width - getSizeMessage().width) / 2, 
                getSize_jframe().height 
                - getSizeMessage().height - 2 * 2 * 2 * 2 * 2);
        

         viewWidthTb = getSize_jframe().width - 2
                - ((2 * 2 * 2 * 2 + 1) * 2 + 1); //35
         viewHeightTB = getSize_jframe().height - 1;

         final int h1 = 180, h2 = 190, h3 = 560;
         viewHeightTBOpener =
                getView_heightTB_visible() / (2 + 1) / (2 + 1);
         viewBoundsPo = new Rectangle(
                2, h1, h2, h3);

         
         
         final int openWidth = ContorlPicture.adaptToSize(
        		 getSize_jframe().width - TWENTY_FIFE - TEN - 2,
        		 false).x;
         

         final int openHeight = ContorlPicture.adaptToSize(
        		 getSize_jframe().height - (getView_heightTB_visible() 
                         + ViewSettings.getView_heightTB_opener() 
                         + VIEW_LOCATION_TB.y + VIEW_SIZE_SP),
        		 false).x;
         
         final int closedHeight = ContorlPicture.adaptToSize(
        		 getSize_jframe().height - (getView_heightTB_visible() 
                         + ViewSettings.getView_heightTB_opener() 
                         + VIEW_LOCATION_TB.y + VIEW_SIZE_SP) + getView_heightTB_visible() 
                 - TWENTY_FIFE, false).x;
         
         
         
         // round the values so that the display size can be transformed
         // into model size without rounding.
         viewBoundsPageOpen = new Rectangle(
                 VIEW_LOCATION_TB.x,  
                 getView_heightTB_visible() 
                 + ViewSettings.getView_heightTB_opener() + VIEW_LOCATION_TB.y, 
                 openWidth,
                 openHeight);
         
         
                //the last 25 is the size 
         view_bounds_page_closed = new Rectangle(
                 viewBoundsPageOpen.x, 
                 viewBoundsPageOpen.y - getView_heightTB_visible(), 
                 viewBoundsPageOpen.width, 
                 closedHeight);
        
         view_bounds_page = new Rectangle(
                viewBoundsPageOpen);

         distanceAfterLine = 2 + 1; distanceBeforeLine = 2; 
                distanceBetweenItems = 2 + 1; 
        
         itemMenu1Width 
        = (int) (ViewSettings.getItemWidth() * (1 + 1.0 / (2 + 2)));
        itemMenu1Height = 2 * ViewSettings.getItemHeight();

        
        
    }

    
    /**
	 * Return the bounds of a fullscreen window.
     * @return the bounds of a fullscreen window.
     */
    public static Dimension getSizeViewFullscreen() {

    	//return the size of the screen in pixel.
    	return new Dimension(
    			(int) (Toolkit.getDefaultToolkit().getScreenSize()
    					.getWidth()), 
    			(int) (Toolkit.getDefaultToolkit().getScreenSize()
    					.getHeight()));
    }
    
    /**
	 * Return the bounds of a not-fullscreen window.
     * @return the bounds of a not-fullscreen window.
     */
    public static Dimension getSizeViewWindow() {

    	//return the size of the screen in pixel.
    	return new Dimension(
    			(int) (Toolkit.getDefaultToolkit().getScreenSize()
    					.getWidth() * 2 / (2 + 1)), 
    			(int) (Toolkit.getDefaultToolkit().getScreenSize()
    					.getHeight() * 2 / (2 + 1)));
    }
    
    

    /**
     * @return the vIEW_SIZE_JFRAME
     */
    public static Dimension getSizeJFrame() {
        return getSize_jframe();
    }


    /**
     * @param _size the vIEW_SIZE_JFRAME to set
     */
    public static void setSize_jframe(final Dimension _size) {
        
        standard_size_jframe_normalSize = new Dimension(
        	    		Math.max(
        	    				_size.width, 
        	    				MIN_WIDTH_WINDOW), 
        	    		Math.max(
        	    				_size.height,
        	    				MIN_HEIGHT_WINDOW));
        recalculate();
    }


    /**
     * @return the fULLSCREEN
     */
    public static boolean isFullscreen() {
        return fullscreen;
    }


    /**
     * @param _fullscreen the fULLSCREEN to set
     */
    public static void setFULLSCREEN(final boolean _fullscreen) {
        fullscreen = _fullscreen;
    }


    /**
     * @return the messagelocation
     */
    public static Point getMessagelocation() {
        return messageLocation;
    }


    /**
     * @return the viewBoundsJbtnExit
     */
    public static Rectangle getViewBoundsJbtnExit() {
        return getView_bounds_jbtn_exit();
    }


    /**
     * @return the viewWidthtb
     */
    public static int getViewWidthtb() {
        return viewWidthTb;
    }


    /**
     * @return the view_widthTb
     */
    public static int getView_widthTb() {
        return viewWidthTb;
    }


    /**
     * @param _view_widthTb the view_widthTb to set
     */
    public static void setView_widthTb(final int _view_widthTb) {
        ViewSettings.viewWidthTb = _view_widthTb;
    }


    /**
     * @return the view_heightTB
     */
    public static int getView_heightTB() {
        return viewHeightTB;
    }


    /**
     * @param _view_heightTB the view_heightTB to set
     */
    public static void setView_heightTB(final int _view_heightTB) {
        ViewSettings.viewHeightTB = _view_heightTB;
    }


    /**
     * @return the view_heightTB_visible
     */
    public static int getView_heightTB_visible() {
    	return Math.max(
    			(int) (getSize_jframe().height / (2 + 2 + 0.8)),
    			
    			//TODO: adapt window size
        		120);
    }




    /**
     * @return the view_bounds_jbtn_fullscreen
     */
    public static Rectangle getView_bounds_jbtn_fullscreen() {
    	return new Rectangle(
                getView_bounds_jbtn_exit().x 
                - getView_bounds_jbtn_exit().width, 
                getView_bounds_jbtn_exit().y, 
                TWENTY_FIFE * 2 + FIVE, 2  * TEN);
    }




    /**
     * @return the view_bounds_jbtn_exit
     */
    public static Rectangle getView_bounds_jbtn_exit() {
        return new Rectangle(getSize_jframe().width - TWENTY_FIFE * 2 
        		- FIVE - DISTANCE_TO_WINDOW, 
                DISTANCE_TO_WINDOW, 
                TWENTY_FIFE * 2 + FIVE, 2  * TEN);
    }


    /**
     * @return the view_heightTB_opener
     */
    public static int getView_heightTB_opener() {
        return viewHeightTBOpener;
    }


    /**
     * @param _view_heightTB_opener the view_heightTB_opener to set
     */
    public static void setView_heightTB_opener(
            final int _view_heightTB_opener) {
        ViewSettings.viewHeightTBOpener = _view_heightTB_opener;
    }


    /**
     * @return the view_bounds_po
     */
    public static Rectangle getView_bounds_po() {
        return viewBoundsPo;
    }


    /**
     * @param _view_bounds_po the view_bounds_po to set
     */
    public static void setView_bounds_po(final Rectangle _view_bounds_po) {
        ViewSettings.viewBoundsPo = _view_bounds_po;
    }


    /**
     * @return the view_bounds_page_open
     */
    public static Rectangle getView_bounds_page_open() {
        return viewBoundsPageOpen;
    }


    /**
     * @param _view_bounds_page_open the view_bounds_page_open to set
     */
    public static void setView_bounds_page_open(
            final Rectangle _view_bounds_page_open) {
        ViewSettings.viewBoundsPageOpen = _view_bounds_page_open;
    }


    /**
     * @return the view_bounds_page_closed
     */
    public static Rectangle getView_bounds_page_closed() {
        return view_bounds_page_closed;
    }


    /**
     * @param _view_bounds_page_closed the view_bounds_page_closed to set
     */
    public static void setView_bounds_page_closed(
            final Rectangle _view_bounds_page_closed) {
        ViewSettings.view_bounds_page_closed = _view_bounds_page_closed;
    }


    /**
     * @return the view_bounds_page
     */
    public static Rectangle getView_bounds_page() {
        return view_bounds_page;
    }


    /**
     * @param _view_bounds_page the view_bounds_page to set
     */
    public static void setView_bounds_page(final Rectangle _view_bounds_page) {
        ViewSettings.view_bounds_page = _view_bounds_page;
    }


    /**
     * @return the distanceAfterLine
     */
    public static int getDistanceAfterLine() {
        return distanceAfterLine;
    }


    /**
     * @param _distanceAfterLine the distanceAfterLine to set
     */
    public static void setDistanceAfterLine(final int _distanceAfterLine) {
        ViewSettings.distanceAfterLine = _distanceAfterLine;
    }


    /**
     * @return the distanceBetweenItems
     */
    public static int getDistanceBetweenItems() {
        return distanceBetweenItems;
    }


    /**
     * @param _distanceBetweenItems the distanceBetweenItems to set
     */
    public static void setDistanceBetweenItems(
            final int _distanceBetweenItems) {
        ViewSettings.distanceBetweenItems = _distanceBetweenItems;
    }


    /**
     * @return the distanceBeforeLine
     */
    public static int getDistanceBeforeLine() {
        return distanceBeforeLine;
    }


    /**
     * @param _distanceBeforeLine the distanceBeforeLine to set
     */
    public static void setDistanceBeforeLine(final int _distanceBeforeLine) {
        ViewSettings.distanceBeforeLine = _distanceBeforeLine;
    }


    /**
     * @return the itemWidth
     */
    public static int getItemWidth() {
    	int itemWidth = getSize_jframe().width / (TWENTY_FIFE - 2 - 1);
    	itemWidth = getView_heightTB_visible() / 3;
        return itemWidth;
    }



    /**
     * @return the itemHeight
     */
    public static int getItemHeight() {
        int itemHeight = getSize_jframe().height / (TEN + 2);
        itemHeight = getView_heightTB_visible() / 3;
        return itemHeight;
    }




    /**
     * @return the itemMenu1Width
     */
    public static int getItemMenu1Width() {
        return itemMenu1Width;
    }


    /**
     * @param _itemMenu1Width the itemMenu1Width to set
     */
    public static void setItemMenu1Width(final int _itemMenu1Width) {
        ViewSettings.itemMenu1Width = _itemMenu1Width;
    }


    /**
     * @return the itemMenu1Height
     */
    public static int getItemMenu1Height() {
        return itemMenu1Height;
    }


    /**
     * @param _itemMenu1Height the itemMenu1Height to set
     */
    public static void setItemMenu1Height(final int _itemMenu1Height) {
        ViewSettings.itemMenu1Height = _itemMenu1Height;
    }



    /**
     * @return the sIZE_PNL_CLR
     */
    public static Dimension getSIZE_PNL_CLR() {
        return sizePnlClr;
    }



    /**
     * @param _sizePnlClr the sIZE_PNL_CLR to set
     */
    public static void setSIZE_PNL_CLR(final Dimension _sizePnlClr) {
        sizePnlClr = _sizePnlClr;
    }



    /**
     * @return the sizeMessage
     */
    public static Dimension getSizeMessage() {
        return sizeMessage;
    }



    /**
     * @param _sizeMessage the sizeMessage to set
     */
    public static void setSizeMessage(final Dimension _sizeMessage) {
        ViewSettings.sizeMessage = _sizeMessage;
    }


    /**
     * Return the height of tab added to tabbedPane.
     * @return the height of each tab
     */
    public static int getView_heightTab() {
        return getSize_jframe().height - getView_heightTB() / ViewSettings
                .TABBED_PANE_TITLE_PROPORTION_HEIGHT - 1;
    }




	/**
	 * @return the view_size_new
	 */
	public static Dimension getView_size_new() {
		return viewSizeNew;
	}




	/**
	 * @return the view_location_new
	 */
	public static Point getView_location_new() {
		return viewLocationNew;
	}


    private static Rectangle view_bounds_loading = new Rectangle(
    		getView_bounds_page().width / (2 + 1),
    		getView_bounds_page().height / (2 + 1),
    		getView_bounds_page().width / (2 + 1),
    		getView_bounds_page().height / (2 + 1));






	/**
	 * @return the size of the JFrame.
	 */
	public static Dimension getSize_jframe() {
		if (isFullscreen()) {
			return standard_size_jframe_fullscreen;
		}
		return standard_size_jframe_normalSize;
	}




	/**
	 * @return the view_bounds_loading
	 */
	public static Rectangle getView_bounds_loading() {
		return view_bounds_loading;
	}




	public static int getWidthInfoForm() {
		return 100;
	}




	/**
	 * 
	 * @return
	 */
	public static Rectangle getView_bounds_jbtn_minimize() {
    	return new Rectangle(
                getView_bounds_jbtn_fullscreen().x 
                - getView_bounds_jbtn_fullscreen().width, 
                getView_bounds_jbtn_fullscreen().y, 
                TWENTY_FIFE * 2 + FIVE, 2  * TEN);
    }


	/**
	 * 
	 * @return
	 */
	public static Rectangle getView_bounds_jbtn_pinRight() {
    	return new Rectangle(
    			getView_bounds_jbtn_minimize().x 
                - 2 * TEN , 
                getView_bounds_jbtn_minimize().y, 
                2 * TEN, 2  * TEN);
    }


	/**
	 * 
	 * @return
	 */
	public static Rectangle getView_bounds_jbtn_pinLeft() {
    	return new Rectangle(
    			getView_bounds_jbtn_pinRight().x 
                - getView_bounds_jbtn_pinRight().width, 
                getView_bounds_jbtn_pinRight().y, 
                2 * TEN, 2  * TEN);
    }


	
    
}
