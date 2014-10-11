//package declaration
package model.settings;

//import declarations
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Toolkit;

import javax.swing.BorderFactory;
import javax.swing.border.Border;

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
    private static final int FIFE = 5, TEN = 10, TWENTY_FIFE = 25;
    
    
    //class selection

    /**
     * The maximal zoom in and the maximal zoom out.
     */
    public static final int MAX_ZOOM_IN = 7, MAX_ZOOM_OUT = 3;
    /**
     * colors.
     */
    public static final Color 
    CLR_BACKGROUND_DARK = new Color(241, 242, 243), 
    CLR_BACKGROUND_DARK_X = new Color(236, 237, 238),
    CLR_BACKGROUND_DARK_XX = new Color(226, 227, 228),
    CLR_BORDER = new Color(218, 219, 220),
    CLR_ITEM1BUTTON_BACKGROUND = new Color(212, 229, 255),
    CLR_BACKGROUND = new Color(246, 246, 246);    
    
    
    /**
     * size of a border block in selection.
     * If changed, reinitialize border in Selection.
     */
    public static final int SELECTION_BORDER_BLOCK_SIZE = 9;
    
    /**
     * array of colors of border in selection.
     * If changed, reinitialize border in Selection.
     */
    public static final Color[] SELECTION_BORDER_CLR_BORDER = new Color[] { 
            Color.darkGray, Color.white };
    
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
//        new Color(25, 25, 255, 15), new Color(125, 125, 255, 15) };
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
     * the light background color which can be used everywhere in a view class.
     * 
     * usages:
     *      PaintObject  at Constructor
     */
    public static final Color GENERAL_CLR_BACKGROUND_LIGHT = new Color(
            250, 250, 250);

    /**
     * the light background color which can be used everywhere in a view class.
     * 
     * usages:
     *      PaintObject  at Constructor
     */
    public static final Color GENERAL_CLR_BACKGROUND_LIGHT_FOCUS = new Color(
            245, 246, 247);

    /**
     * headline font which can be used everywhere.
     * 
     * usages:
     *      PaintObject  at Constructor
     */
    public static final Font GENERAL_FONT_HEADLINE_1 = new Font(
            "Courier New", Font.BOLD, 19),
            GENERAL_FONT_HEADLINE_2 = new Font(
                    "Courier New", Font.BOLD, 15);

    /**
     * headline font which can be used everywhere.
     * 
     * usages:
     *      PaintObject  at Constructor
     */
    public static final Font FONT_ITEM1_BUTTON = new Font(
            "Comic Sans MS", Font.PLAIN, 10);

    /**
     * The font for information.
     */
    public static final Font TP_FONT_INFORMATION
    = new Font("Courier new", Font.ITALIC, 12);

    

    /**
     * general font which can be used everywhere.
     * 
     * usages:
     *      PaintObject  at Constructor
     */
    public static final Font GENERAL_FONT_ITEM = new Font(
            "Courier New", Font.ITALIC, 11);

    /**
     * general small font which can be used everywhere.
     * 
     * usages:
     *      PaintObject  at Constructor
     */
    public static final Font GENERAL_FONT_ITEM_SMALL = new Font(
            "Courier New", Font.PLAIN, 10);
    /**
     * general small font which can be used everywhere.
     * 
     */
    public static final Font GENERAL_FONT_ITEM_PLAIN = new Font(
            "Courier New", Font.PLAIN, 11);

    
    /**
     * .
     */
    public static final Dimension PAINT_SIZE 
    = new Dimension(275, 365);

    
    
    /**
     * Whether fullscreen or not.
     */
    private static boolean fullscreen = false;
    //sizes
    
    /**
     * The size of the JFrame.
     */
    private static Dimension size_jframe = 
            new Dimension(
            (int) (Toolkit.getDefaultToolkit().getScreenSize().getWidth() 
                    * 2 / (2 + 1)), 
            (int) (Toolkit.getDefaultToolkit().getScreenSize().getHeight() 
                    * 2 / (2 + 1)));
    
    

    /**
     * .
     */
    public static final Dimension MESSAGE_SIZE
    = new Dimension(500, 40);
    
    /**
     * .
     */
    private static Point messageLocation
    = new Point((size_jframe.width - MESSAGE_SIZE.width) / 2, 
            size_jframe.height - MESSAGE_SIZE.height - 2 * 2 * 2 * 2 * 2);


    /**
     * The Bounds of the Exit button.
     */
    private static Rectangle view_bounds_jbtn_exit = new Rectangle(
            size_jframe.width - TWENTY_FIFE * 2 - FIFE, 0, 
            TWENTY_FIFE * 2 + FIFE, 2  * TEN);

    /**
     * The Bounds of the Exit button.
     */
    private static Rectangle view_bounds_jbtn_fullscreen = new Rectangle(
            getView_bounds_jbtn_exit().x - getView_bounds_jbtn_exit().width, 0, 
            TWENTY_FIFE * 2 + FIFE, 2  * TEN);
    
    /**
     * The bounds of the TabbedPanel.
     */
    public static final Point VIEW_LOCATION_TB = new Point(2, 2);

    /**
     * the width of the TabbedPane.
     */
    private static int view_widthTb = size_jframe.width - 2
            - ((2 * 2 * 2 * 2 + 1) * 2 + 1); //35

    /**
     * the height of the TabbedPane.
     */
    private static int view_heightTB = size_jframe.height - 1;

    /**
     * the visible height of the TabbedPane.
     */
    private static int view_heightTB_visible = 
            (int) (size_jframe.height / (2 + 2 + 1 / 2));

    /**
     * The size of the tabbedPane opener.
     */
    private static int view_heightTB_opener =
            view_heightTB_visible / (2 + 1) / (2 + 1);
    /**
     * the bounds of the PaointObject.
     */
    private static Rectangle view_bounds_po = new Rectangle(
            2, 180, 190, 560);


    /**
     * the size of the scrollPanel.
     */
    public static final int VIEW_SIZE_SP = 30;

    /**
     * The size of the title JButton getSize() / the size of title JButton
     * is the real size.
     */
    public static final int TABBED_PANE_TITLE_PROPORTION_WIDTH = 15,
           TABBED_PANE_TITLE_PROPORTION_HEIGHT = 20;
    /**
     * the bounds of the Page.
     */
    private static Rectangle view_bounds_page_open = new Rectangle(
            1, 1 + view_heightTB_visible 
            + ViewSettings.getView_heightTB_opener() + VIEW_LOCATION_TB.y, 
            size_jframe.width - TWENTY_FIFE - TEN - 2, 
            size_jframe.height - size_jframe.height / FIFE 
            - 2 * VIEW_SIZE_SP - 2 * TWENTY_FIFE);
            //the last 25 is the size 
    /**
     * the bounds of the Page.
     */
    private static Rectangle view_bounds_page_closed = new Rectangle(
            view_bounds_page_open.x, 
            view_bounds_page_open.y - view_heightTB_visible, 
            view_bounds_page_open.width, 
            view_bounds_page_open.height + view_heightTB_visible 
            - TWENTY_FIFE);
    
    /**
     * the bounds of the Page.
     */
    private static Rectangle view_bounds_page = new Rectangle(
            view_bounds_page_open);

    /**
     * distances for items.
     */
    private static int distanceAfterLine = 3, distanceBeforeLine = 2, 
            distanceBetweenItems = 3, 
            itemWidth = size_jframe.width / (TWENTY_FIFE - 2 - 1), 
            itemHeight = size_jframe.height / (TEN + 2);
    
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
    public static final int ZOOM_MULITPLICATOR = 2;
    
    /**
     * the size of the zoom.
     */
    public static final Dimension ZOOM_SIZE 
    = new Dimension(getView_bounds_page().width / ZOOM_MULITPLICATOR, 
            getView_bounds_page().height / ZOOM_MULITPLICATOR);
    
    
    /**
     * utility class constructor.
     */
    private ViewSettings() { }


    /**
     * @return the vIEW_SIZE_JFRAME
     */
    public static Dimension getSizeJFrame() {
        return size_jframe;
    }


    /**
     * @param _size the vIEW_SIZE_JFRAME to set
     */
    public static void setSize_jframe(final Dimension _size) {
        size_jframe = _size;
    }


    /**
     * @return the fULLSCREEN
     */
    public static boolean isFullscreen() {
        return fullscreen;
    }


    /**
     * @param fULLSCREEN the fULLSCREEN to set
     */
    public static void setFULLSCREEN(boolean fULLSCREEN) {
        fullscreen = fULLSCREEN;
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
        return view_widthTb;
    }


    /**
     * @return the view_widthTb
     */
    public static int getView_widthTb() {
        return view_widthTb;
    }


    /**
     * @param view_widthTb the view_widthTb to set
     */
    public static void setView_widthTb(int view_widthTb) {
        ViewSettings.view_widthTb = view_widthTb;
    }


    /**
     * @return the view_heightTB
     */
    public static int getView_heightTB() {
        return view_heightTB;
    }


    /**
     * @param view_heightTB the view_heightTB to set
     */
    public static void setView_heightTB(int view_heightTB) {
        ViewSettings.view_heightTB = view_heightTB;
    }


    /**
     * @return the view_heightTB_visible
     */
    public static int getView_heightTB_visible() {
        return view_heightTB_visible;
    }


    /**
     * @param view_heightTB_visible the view_heightTB_visible to set
     */
    public static void setView_heightTB_visible(int view_heightTB_visible) {
        ViewSettings.view_heightTB_visible = view_heightTB_visible;
    }


    /**
     * @return the view_bounds_jbtn_fullscreen
     */
    public static Rectangle getView_bounds_jbtn_fullscreen() {
        return view_bounds_jbtn_fullscreen;
    }


    /**
     * @param view_bounds_jbtn_fullscreen the view_bounds_jbtn_fullscreen to set
     */
    public static void setView_bounds_jbtn_fullscreen(
            Rectangle view_bounds_jbtn_fullscreen) {
        ViewSettings.view_bounds_jbtn_fullscreen = view_bounds_jbtn_fullscreen;
    }


    /**
     * @return the view_bounds_jbtn_exit
     */
    public static Rectangle getView_bounds_jbtn_exit() {
        return view_bounds_jbtn_exit;
    }


    /**
     * @param view_bounds_jbtn_exit the view_bounds_jbtn_exit to set
     */
    public static void setView_bounds_jbtn_exit(Rectangle view_bounds_jbtn_exit) {
        ViewSettings.view_bounds_jbtn_exit = view_bounds_jbtn_exit;
    }


    /**
     * @return the view_heightTB_opener
     */
    public static int getView_heightTB_opener() {
        return view_heightTB_opener;
    }


    /**
     * @param view_heightTB_opener the view_heightTB_opener to set
     */
    public static void setView_heightTB_opener(int view_heightTB_opener) {
        ViewSettings.view_heightTB_opener = view_heightTB_opener;
    }


    /**
     * @return the view_bounds_po
     */
    public static Rectangle getView_bounds_po() {
        return view_bounds_po;
    }


    /**
     * @param view_bounds_po the view_bounds_po to set
     */
    public static void setView_bounds_po(Rectangle view_bounds_po) {
        ViewSettings.view_bounds_po = view_bounds_po;
    }


    /**
     * @return the view_bounds_page_open
     */
    public static Rectangle getView_bounds_page_open() {
        return view_bounds_page_open;
    }


    /**
     * @param view_bounds_page_open the view_bounds_page_open to set
     */
    public static void setView_bounds_page_open(Rectangle view_bounds_page_open) {
        ViewSettings.view_bounds_page_open = view_bounds_page_open;
    }


    /**
     * @return the view_bounds_page_closed
     */
    public static Rectangle getView_bounds_page_closed() {
        return view_bounds_page_closed;
    }


    /**
     * @param view_bounds_page_closed the view_bounds_page_closed to set
     */
    public static void setView_bounds_page_closed(
            Rectangle view_bounds_page_closed) {
        ViewSettings.view_bounds_page_closed = view_bounds_page_closed;
    }


    /**
     * @return the view_bounds_page
     */
    public static Rectangle getView_bounds_page() {
        return view_bounds_page;
    }


    /**
     * @param view_bounds_page the view_bounds_page to set
     */
    public static void setView_bounds_page(Rectangle view_bounds_page) {
        ViewSettings.view_bounds_page = view_bounds_page;
    }


    /**
     * @return the distanceAfterLine
     */
    public static int getDistanceAfterLine() {
        return distanceAfterLine;
    }


    /**
     * @param distanceAfterLine the distanceAfterLine to set
     */
    public static void setDistanceAfterLine(int distanceAfterLine) {
        ViewSettings.distanceAfterLine = distanceAfterLine;
    }


    /**
     * @return the distanceBetweenItems
     */
    public static int getDistanceBetweenItems() {
        return distanceBetweenItems;
    }


    /**
     * @param distanceBetweenItems the distanceBetweenItems to set
     */
    public static void setDistanceBetweenItems(int distanceBetweenItems) {
        ViewSettings.distanceBetweenItems = distanceBetweenItems;
    }


    /**
     * @return the distanceBeforeLine
     */
    public static int getDistanceBeforeLine() {
        return distanceBeforeLine;
    }


    /**
     * @param distanceBeforeLine the distanceBeforeLine to set
     */
    public static void setDistanceBeforeLine(int distanceBeforeLine) {
        ViewSettings.distanceBeforeLine = distanceBeforeLine;
    }


    /**
     * @return the itemWidth
     */
    public static int getItemWidth() {
        return itemWidth;
    }


    /**
     * @param itemWidth the itemWidth to set
     */
    public static void setItemWidth(int itemWidth) {
        ViewSettings.itemWidth = itemWidth;
    }


    /**
     * @return the itemHeight
     */
    public static int getItemHeight() {
        return itemHeight;
    }


    /**
     * @param itemHeight the itemHeight to set
     */
    public static void setItemHeight(int itemHeight) {
        ViewSettings.itemHeight = itemHeight;
    }


    /**
     * @return the itemMenu1Width
     */
    public static int getItemMenu1Width() {
        return itemMenu1Width;
    }


    /**
     * @param itemMenu1Width the itemMenu1Width to set
     */
    public static void setItemMenu1Width(int itemMenu1Width) {
        ViewSettings.itemMenu1Width = itemMenu1Width;
    }


    /**
     * @return the itemMenu1Height
     */
    public static int getItemMenu1Height() {
        return itemMenu1Height;
    }


    /**
     * @param itemMenu1Height the itemMenu1Height to set
     */
    public static void setItemMenu1Height(int itemMenu1Height) {
        ViewSettings.itemMenu1Height = itemMenu1Height;
    }
    
}
