//package declaration
package model.settings;

import java.awt.Dimension;

import javax.imageio.ImageIO;


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
	 * URL of the page onto which the bug-reporting takes place.
	 */
	public static final String URL_BUG_PAGE 
	= "http://juliushuelsmann.github.io/paint/issueReport.html";
	
    /**
     * for class view.View.jbtn_exit.
     * static path of the normal image path 
     */
	public static final String 
	        VIEW_JBTN_EXIT_NORMAL_PATH = "/res/img/icon/exit/normal.png", 
	        VIEW_JBTN_EXIT_MOUSEOVER_PATH = "/res/img/icon/exit/mouseover.png", 
	        VIEW_JBTN_EXIT_PRESSED_PATH = "/res/img/icon/exit/pressed.png",

	        VIEW_JBTN_FULLSCREEN_NORMAL_PATH 
	        = "/res/img/icon/fullscreen/normal.png", 
	        VIEW_JBTN_FULLSCREEN_MOUSEOVER_PATH 
	        = "/res/img/icon/fullscreen/mouseover.png", 
	        VIEW_JBTN_FULLSCREEN_PRESSED_PATH 
	        = "/res/img/icon/fullscreen/pressed.png", 
	        
	        VIEW_QA_JBTN_RESIZE = "/res/img/centerResize.png",
	        
	        VIEW_CURSOR_PATH = "/res/img/cursor.png",
	        
	        VIEW_I1M_OPEN_PATH = "/res/img/open.png",
	        
	        VIEW_TAB_INSRT_SELECT = "/res/img/icon/tabs/write/write.png",
	        VIEW_TAB_INSRT_PAINT_LINE = "/res/img/icon/geoForm/line.png",
	        VIEW_TAB_INSRT_PAINT_CURVE = "/res/img/icon/geoForm/curve.png",
	        VIEW_TAB_INSRT_PAINT_ARCH = "/res/img/icon/geoForm/pfeilopen.png",
	        VIEW_TAB_INSRT_PAINT_CIRCLE = "/res/img/icon/geoForm/circle.png",
	        VIEW_TAB_INSRT_PAINT_RECT = "/res/img/icon/geoForm/rectangle.png",	
	        VIEW_TAB_INSRT_PAINT_RECTROUND = "/res/img/icon/geoForm/rectangleRound.png",
	        VIEW_TAB_INSRT_PAINT_TRIANGLE = "/res/img/icon/geoForm/triangle.png",
	        
	        VIEW_TAB_INSRT_PAINT_CIRCLE_F = "/res/img/icon/geoForm/circleFilled.png",
	        VIEW_TAB_INSRT_PAINT_RECT_F = "/res/img/icon/geoForm/rectangleFilled.png",
	        VIEW_TAB_INSRT_PAINT_RECTROUND_F = "/res/img/icon/geoForm/rectangleRoundFilled.png",
	        VIEW_TAB_INSRT_PAINT_TRIANGLE_F = "/res/img/icon/geoForm/triangleFilled.png",
	        VIEW_TAB_INSRT_PAINT_ARCH_F = "/res/img/icon/geoForm/arch.png",
	        
	        
	        VIEW_TAB_PAINT_PALETTE = "/res/img/icon/palette.png",
	        
	        VIEW_SB_VERT1 = "/res/img/sb/n/tp.png",
	        VIEW_SB_HORIZ1 = "/res/img/sb/s/tp.png",
	        VIEW_SB_VERT2 = "/res/img/sb/n/bp.png",
	        VIEW_SB_HORIZ2 = "/res/img/sb/s/bp.png",
	        
	        VIEW_TB_PASTE_PATH = "/res/img/icon/paste.png",
	        VIEW_TB_COPY_PATH = "/res/img/icon/copy.png", 
	        VIEW_TB_CUT_PATH = "/res/img/icon/cut.png", 
	        VIEW_TB_NEW_PATH = "/res/img/icon/new.png",
	        VIEW_TB_ZOOM_IN_PATH = "/res/img/icon/zoomIn.png",
	        VIEW_TB_ZOOM_OUT_PATH = "/res/img/icon/zoomOut.png",
	        VIEW_TB_SAVE_PATH = "/res/img/icon/save.png",
	        VIEW_TB_LOAD_PATH = "/res/img/icon/load.png",
	        VIEW_TB_DOWN_PATH = "/res/img/icon/down.png",
	        VIEW_TB_UP_PATH = "/res/img/icon/up.png",
	        VIEW_TB_SELECT_CURVE_PATH = "/res/img/icon/select_curve.png",
	        VIEW_TB_SELECT_LINE_PATH = "/res/img/icon/select_line.png",
	        VIEW_TB_SELECT_MAGIC_PATH = "/res/img/icon/select_magic.png",
	        VIEW_TB_NEXT_PATH = "/res/img/icon/next.png",
	        VIEW_TB_PREV_PATH = "/res/img/icon/prev.png",
	        VIEW_TB_FILL_PATH = "/res/img/icon/fill.png",
	        VIEW_TB_PIPETTE_PATH = "/res/img/icon/pipette.png",
	        VIEW_TB_MOVE_PATH = "/res/img/centerResize.png",
	        SP_PATH_UP = "/res/img/sb/n/t.png",
	        SP_PATH_DOWN = "/res/img/sb/n/b.png",
	        SP_PATH_LEFT = "/res/img/sb/s/t.png",
	        SP_PATH_RIGHT = "/res/img/sb/s/b.png",
	        VIEW_JBTN_RESIZE_PATH = "/res/img/centerResize.png",
	        PATH_PEN_KULI_POINT = "/res/img/pen/kuliPoint.png",
	        PATH_PEN_KULI_LINE = "/res/img/pen/kuliNormal.png",
	        PATH_PEN_KULI_MATHS = "/res/img/pen/kuliMaths.png",
	        PATH_PEN_BLEI_POINT = "/res/img/pen/bleiPoint.png",
	        PATH_PEN_BLEI_LINE = "/res/img/pen/bleiNormal.png",
	        PATH_PEN_BLEI_MATHS = "/res/img/pen/bleiMaths.png",
	        PATH_PEN_FILLER_POINT = "/res/img/pen/fuellerPoint.png",
	        PATH_PEN_FILLER_LINE = "/res/img/pen/fuellerNormal.png",
	        PATH_PEN_FILLER_MATHS = "/res/img/pen/fuellerMaths.png",
	        PATH_PEN_MARKER_LINE = "/res/img/pen/markerNormal.png",
	        
	        
	        VIEW_PATH_I1M_STANDARD = "/res/img/st2.png",
	        VIEW_PATH_I2_STANDARD =  "/res/img/icon/default.png";

	
	public static final String[] str_loading = new String[] {
		"/res/img/img/icn0.png", 
		"/res/img/img/icn1.png", 
		"/res/img/img/icn2.png", 
		"/res/img/img/icn3.png",
		"/res/img/img/icn4.png", 
		"/res/img/img/icn5.png", 
		"/res/img/img/icn6.png"
	};
	/**
	 * Identifier for the exporting of alpha pixel.
	 */
	public static final String ID_DISPLAY_ALPHA = "transparent", 
	        ID_WHITE = "white";
	
    /**
     * The identifiers for the transparency.
     */
    public static final int ID_TRANSPARENCY_WHITE = 0, 
            ID_TRANSPARENCY_RASTER = 1;
	/**
	 * The background constants.
	 */
	public static final int CONTROL_PAGE_BACKGROUND_RASTAR = 0,
	        CONTROL_PAGE_BACKGROUND_LINES = 1, CONTROL_PAGE_BACKGROUND_NONE = 3;
	
	/**
	 * The size of the mouseIcon.
	 */
	public static final int MOUSE_ICON_SIZE = 5;
	
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
	    CONTROL_PAINTING_INDEX_PAINT_1 = 0,
	    CONTROL_PAINTING_INDEX_PAINT_2 = 1,
	    CONTROL_PAINTING_INDEX_FILL = 2,
	    CONTROL_PAINTING_INDEX_PIPETTE = 3,
	    CONTROL_PAINTING_INDEX_SELECTION_CURVE = 4,
	    CONTROL_PAINTING_INDEX_SELECTION_LINE = 5,
	    CONTROL_PAINTING_INDEX_SELECTION_MAGIC = 6,
	    CONTROL_PAINTING_INDEX_ZOOM_IN = 7,
	    CONTROL_PAINTING_INDEX_MOVE = 8,
	    CONTROL_PAINTING_INDEX_ERASE = 9,
	    CONTROL_PAINTING_INDEX_I_G_LINE = 10,
	    CONTROL_PAINTING_INDEX_I_G_RECTANGLE = 11,
	    CONTROL_PAINTING_INDEX_I_G_CURVE = 12,
	    CONTROL_PAINTING_INDEX_I_G_TRIANGLE = 13,
	    CONTROL_PAINTING_INDEX_I_G_ARCH = 14,
	    CONTROL_PAINTING_INDEX_I_D_DIA = 15,
	    CONTROL_PAINTING_INDEX_I_G_ARCH_FILLED = 16,
	    CONTROL_PAINTING_INDEX_I_G_RECTANGLE_FILLED = 17,
	    CONTROL_PAINTING_INDEX_I_G_TRIANGLE_FILLED = 18,
	    CONTROL_PAINTING_INDEX_I_G_CURVE_2 = 19;
	    
			

	/**
	 * The maximum percentage value.
	 */
    public static final int MAX_PERCENTAGE = 100;

	//pens

    /**
     * constant integer indicates operation in pen (math/line/point).
     */
	public static final int PEN_ID_MATHS = 0, 
	        PEN_ID_LINES = 1, 
	        PEN_ID_POINT = 2,  
	        PEN_ID_MATHS_SILENT = 3,
	        PEN_ID_MATHS_SILENT_2 = 4;
	
	
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
	 * IDs identifying the current erase operation.
	 */
	public static final int ERASE_ALL = 0,  ERASE_DESTROY = 1;
	
	
	/**
	 * The selection possibilities for border [percentage] of page background.
	 */
	public static final int[] BORDER_PRERCENTAGES = new int[]
	        {0, 2, 4, 8, 15, 20, 25, 50};

	
	/**
	 * The save formats.
	 */
    public static final String[] SAVE_FORMATS =  ImageIO.getWriterFormatNames();
	
    
    /**
     * Checks whether a filename ends with a correct extension which contains 
     * save format.
     * @param 	_strgToTest the filename that is tested.
     * @return 	whether a given file name ends with one of the legal formats.
     */
    public static boolean endsWithSaveFormat(final String _strgToTest) {
    	for (String s : SAVE_FORMATS) {
    		if (_strgToTest.endsWith(s)) {
    			return true;
    		}
    	}
    	return false;
    }
    
    
    /**
     * return the file extension of a file with specified _path.
     * @param _path 	the file path.
     * @return 			the file extension
     */
    public static String getFileExtension(final String _path) {
    	if (_path.contains(".")) {
    		String concat = "";
    		for (int cPosition = _path.length() - 1; 
    				cPosition >= 0; cPosition--) {
    			if (_path.charAt(cPosition) == '.') {
    				return concat;
    			} else {
    				concat = _path.charAt(cPosition)  + concat;
    			}
    		}
    		return "";
    	} else {
    		return "";
    	}
    }
	
	/**
	 * Return the selection possibilities for border as a string with a 
	 * percentage sign.
	 * @return the selection possibilities for border [percentage] of page 
	 * background
	 */
	public static String[] getBorderPercentagesTitle() {
	    String [] s = new String[BORDER_PRERCENTAGES.length];
	    
	    for (int i = 0; i < s.length; i++) {
	        s[i] = BORDER_PRERCENTAGES[i] + "%";
	    }
	    
	    return s;
	}
	
	
	public static final int ID_STARTUP_NORMAL = 0, ID_STARTUP_LOAD_IMAGE = 1, ID_STARTUP_LOAD = 2;
	
	
	/**
	 * empty private Constructor; is private because class is designed
	 * to be singleton. This private constructor is only called once in
	 * method getInstance.
	 */
	private Constants() { }
	
}
