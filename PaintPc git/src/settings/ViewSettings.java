//package declaration
package settings;

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
    private static final int FIFE = 5, TEN = 10, TWENTY_FIFE = 25, 
            HUNDRED = 100;
    
    
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
            Color.red, Color.white };
    
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
            new Color(25, 25, 255, 15), new Color(125, 125, 255, 15) };
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
    public static final Font GENERAL_FONT_HEADLINE = new Font(
            "Courier New", Font.BOLD, 19);

    /**
     * headline font which can be used everywhere.
     * 
     * usages:
     *      PaintObject  at Constructor
     */
    public static final Font FONT_ITEM1_BUTTON = new Font(
            "Comic Sans MS", Font.PLAIN, 10);


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
     * .
     */
    public static final Dimension PAINT_SIZE 
    = new Dimension(275, 365);
    
    //sizes
    
    /**
     * The size of the JFrame.
     */
    public static final Dimension VIEW_SIZE_JFRAME = new Dimension(
            (int) (Toolkit.getDefaultToolkit().getScreenSize().getWidth()), 
            (int) (Toolkit.getDefaultToolkit().getScreenSize().getHeight()));
    
    /**
     * The Bounds of the Exit button.
     */
    public static final Rectangle VIEW_BOUNDS_JBTN_EXIT = new Rectangle(
            VIEW_SIZE_JFRAME.width - HUNDRED - TEN, 0, 
            TWENTY_FIFE * 2 + FIFE, 2  * TEN);
    
    /**
     * The bounds of the TabbedPanel.
     */
    public static final Point VIEW_LOCATION_TB = new Point(0, 0);

    /**
     * the width of the TabbedPane.
     */
    public static final int VIEW_WIDTH_TB = VIEW_SIZE_JFRAME.width 
            - ((2 * 2 * 2 * 2 + 1) * 2 + 1); //35

    /**
     * the height of the TabbedPane.
     */
    public static final int VIEW_HEIGHT_TB = VIEW_SIZE_JFRAME.height;

    /**
     * the visible height of the TabbedPane.
     */
    public static final int VIEW_HEIGHT_TB_VISIBLE = 
            (int) (VIEW_SIZE_JFRAME.height / 4.5);

    /**
     * the bounds of the PaointObject.
     */
    public static final Rectangle VIEW_BOUNDS_PO = new Rectangle(
            2, 180, 190, 560);


    /**
     * the size of the scrollPanel.
     */
    public static final int VIEW_SIZE_SP = 30;

    /**
     * the bounds of the Page.
     */
    public static final Rectangle VIEW_BOUNDS_PAGE = new Rectangle(
            0, VIEW_HEIGHT_TB_VISIBLE  , 
            VIEW_SIZE_JFRAME.width - TWENTY_FIFE - TEN, 
            VIEW_SIZE_JFRAME.height - VIEW_SIZE_JFRAME.height / FIFE 
            - 2 * VIEW_SIZE_SP);
    
    /**
     * The font for information.
     */
    public static final Font TP_FONT_INFORMATION
    = new Font("Courier new", Font.ITALIC, 12);

    
    /**
     * distances for items.
     */
    public static final int DISTANCE_AFTER_LINE = 3, 
            DISTANCE_BEFORE_LINE = 2, DISTANCE_BETWEEN_ITEMS = 3, 
            ITEM_WIDTH = VIEW_SIZE_JFRAME.width / (TWENTY_FIFE - 2 - 1), 
            ITEM_HEIGHT = VIEW_SIZE_JFRAME.height / (TEN + 2);
    
    /**
     * width of item1 menues.
     */
    public static final int ITEM_MENU1_WIDTH 
    = (int) (ViewSettings.ITEM_WIDTH * 1.5),
            ITEM_MENU1_HEIGHT = 2 * ViewSettings.ITEM_HEIGHT;
    
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
    = new Dimension(VIEW_BOUNDS_PAGE.width / ZOOM_MULITPLICATOR, 
            VIEW_BOUNDS_PAGE.height / ZOOM_MULITPLICATOR);
    
    
    /**
     * utility class constructor.
     */
    private ViewSettings() { }
    
}
