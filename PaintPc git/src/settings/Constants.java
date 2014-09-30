//package declaration
package settings;

import java.awt.Dimension;


/**
 * singleton class containing static constants (e.g. static paths or integers)
 * @author Julius Huelsmann
 * @version %I%, %U%
 */
public final class Constants {

    //static instances to which can be accessed by other classes.
    
    //package view and descendants
    
    //class view.View
    
    //paths
    
    /**
     * for class view.View.jbtn_exit.
     * static path of the normal image path 
     */
	public static final String VIEW_JBTN_EXIT_NORMAL_PATH = "normal.png", 
	        VIEW_JBTN_EXIT_MOUSEOVER_PATH = "", 
	        VIEW_JBTN_EXIT_PRESSED_PATH = "",
	        VIEW_TB_PASTE_PATH = "icon/paste.png",
	        VIEW_TB_COPY_PATH = "icon/copy.png", 
	        VIEW_TB_CUT_PATH = "icon/cut.png", 
	        VIEW_TB_NEW_PATH = "icon/new.png",
	        VIEW_TB_ZOOM_IN_PATH = "icon/zoomIn.png",
	        VIEW_TB_ZOOM_OUT_PATH = "icon/zoomOut.png",
	        VIEW_TB_SAVE_PATH = "icon/save.png",
	        VIEW_TB_LOAD_PATH = "icon/load.png",
	        VIEW_TB_DOWN_PATH = "icon/down.png",
	        VIEW_TB_UP_PATH = "icon/up.png",
	        VIEW_TB_SELECT_CURVE_PATH = "icon/select_curve.png",
	        VIEW_TB_SELECT_LINE_PATH = "icon/select_line.png",
	        VIEW_TB_SELECT_MAGIC_PATH = "icon/select_magic.png",
	        VIEW_TB_NEXT_PATH = "icon/next.png",
	        VIEW_TB_PREV_PATH = "icon/prev.png",
	        VIEW_TB_FILL_PATH = "icon/fill.png",
	        VIEW_TB_PIPETTE_PATH = "icon/pipette.png",
	        SP_PATH_UP = "sb/n/t.png",
	        SP_PATH_DOWN = "sb/n/b.png",
	        SP_PATH_LEFT = "sb/s/t.png",
	        SP_PATH_RIGHT = "sb/s/b.png";

	
	
	/**
	 * The size of the mouseIcon.
	 */
	public static final int MOUSE_ICON_SIZE = 20;
	
	//variable containing boolean for debug outputs
	
	/**
	 * if true, debug messages are printed.
	 */
	public static final boolean DEBUG = true;
	
	
	//constants for internal use
	
	
	//control painting
	

	/**
	 * constant indicates operation for separation in ControlPainting.
	 * Operations:
	 * 		complete selection by adding every engaged item
	 * 		separate items at selection line and thus create several new items
	 * 		separate items and merge all engaged to one item.
	 */
	public static final int CONTROL_PAINTING_SELECTION_INDEX_COMPLETE_ITEM = 1, 
		CONTROL_PAINTING_SELECTION_INDEX_DESTROY_ITEM = 2,
		CONTROL_PAINTING_SELECTION_INDEX_IMAGE = 3;
	
	/**
	 * constant indicates operation in ControlPainting (for instance
	 * paint1 or paint2, different versions of selection, pipette, fill etc.).
	 */
	public static final int 
	    CONTROL_PATINING_INDEX_PAINT_1 = 0,
	    CONTROL_PATINING_INDEX_PAINT_2 = 1,
	    CONTROL_PAINTING_INDEX_FILL = 2,
	    CONTROL_PAINTING_INDEX_PIPETTE = 3,
	    CONTROL_PAINTING_INDEX_SELECTION_CURVE = 4,
	    CONTROL_PAINTING_INDEX_SELECTION_LINE = 5,
	    CONTROL_PAINTING_INDEX_SELECTION_MAGIC = 6,
	    CONTROL_PAINTING_INDEX_ZOOM_IN = 7,
	    CONTROL_PAINTING_INDEX_MOVE = 8,
	    CONTROL_PAINTING_INDEX_ERASE = 9;
	
			

	/**
	 * The maximum percentage value.
	 */
    public static final int MAX_PERCENTAGE = 100;

	//pens

    /**
     * constant integer indicates operation in pen (math/line/point).
     */
	public static final int PEN_ID_MATHS = 0, PEN_ID_LINES = 1, 
	        PEN_ID_POINT = 2;
	
	
	/**
	 * 
	 */
	public static final int MAX_PEN_THICKNESS = 25;

	
	/**
	 * The different sizes of different page formats.
	 */
	public static final Dimension 
	SIZE_A4 = new Dimension(2500, 3535),
	SIZE_A5 = new Dimension(SIZE_A4.width, SIZE_A4.height / 2),
	SIZE_A6 = new Dimension(SIZE_A5.width / 2, SIZE_A5.height),
    SIZE_A7 = new Dimension(SIZE_A6.width, SIZE_A6.height / 2);
	/**
	 * empty private Constructor; is private because class is designed
	 * to be singleton. This private constructor is only called once in
	 * method getInstance.
	 */
	private Constants() { }
	
}