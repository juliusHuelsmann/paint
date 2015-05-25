package model.settings;

import java.awt.Color;
import model.objects.pen.Pen;
import model.objects.pen.normal.BallPen;
import model.objects.pen.normal.Pencil;


/**
 * class contains the independent settings.
 * @author Julius Huelsmann
 * @version %I%, %U%
 */
public final class StateStandard {

	/*
	 * Settings for each startup
	 */

	/**
	 * The current erase index which contains the identifier for the current
	 * erase operation.
	 */
	private static int[] standardEraseIndex = new int[] {
		Constants.ERASE_ALL, 
		Constants.ERASE_DESTROY
	};



    /**
     * The format in which the image files are saved.
     */
    private static String[] standardSaveFormat = new String [] {
    	Constants.SAVE_FORMATS[2 * 2 * 2 - 1],
    	Constants.SAVE_FORMATS[2 * 2 * 2 - 1]
    };
    
    /**
     * Whether to export alpha transparently or to replace it with white color.
     */
	private static boolean[] standardExportAlpha = new boolean[] {
		false,
		false
    };

    /**
     * Whether to export alpha transparently or to replace it with white color.
     */
    private static boolean[] standardShowAlpha = new boolean [] {
    	false,
    	false
    };


    /**
     * the index indicates which operation is to be performed. (e.g. pen/
     * selection mode 1 - 3, ...).
     */
	private static int[] standardIndexOperation = new int [] {
        Constants.CONTROL_PAINTING_INDEX_PAINT_1,
        Constants.CONTROL_PAINTING_INDEX_PAINT_1
	};
    
    /**
     * The displayed Background of the pages.
     */
    private static int[] standardIndexPageBackground = new int [] {

        Constants.CONTROL_PAGE_BACKGROUND_RASTAR,
        Constants.CONTROL_PAGE_BACKGROUND_NONE
    };
    
    
    /**
     * The exported background of pages.
     */
    private static int[] standardIndexPageBackgroundExport = new int [] {
        Constants.CONTROL_PAGE_BACKGROUND_NONE,
        Constants.CONTROL_PAGE_BACKGROUND_NONE
    };
    
    /**
     * index contains the selected kind of selection. (separate paintObjects,
     * whole paintObjects, image).
     */
    private static int[] standardIndexSelection = new int [] {

        Constants.CONTROL_PAINTING_SELECTION_INDEX_COMPLETE_ITEM,
        Constants.CONTROL_PAINTING_SELECTION_INDEX_DESTROY_ITEM
    };

    /**
     * with these values the image size is initialized.
     */
    private static int[] standardImageWidth = new int [] {
    	2000, //2500;
    	2000
    };

    /**
     * with these values the image size is initialized.
     */
    private static final int[] standardImageHeight = new int [] {
    	2800, //3535;
    	2800
    };
    
    
    /**
     * Erase radius.
     */
    private static int[] standardEraseRadius = new int [] {
    	10, 
    	10
    };

    /**
     * The border show percentages.
     */
    private static int []
    standardBorderLeftPercentShow =  new int [] {
    	Constants.BORDER_PRERCENTAGES[2 + 1], 
    	Constants.BORDER_PRERCENTAGES[0]
    },
    standardBorderRightPercentShow = new int [] {
    	getStandardBorderLeftPercentShow()[0], 
    	getStandardBorderLeftPercentShow()[1] 
    },
    standardBorderTopPercentShow = new int [] {
    	Constants.BORDER_PRERCENTAGES[1], 
    	Constants.BORDER_PRERCENTAGES[0]
    },
    standardBorderBottomPercentShow = new int [] {
    	getStandardBorderTopPercentShow()[0], 
    	getStandardBorderTopPercentShow()[1] 
    };
    
    /**
     * The border export percentages.
     */
    private static int []
    standardBorderLeftPercentExport = new int [] {
    	Constants.BORDER_PRERCENTAGES[0], 
    	Constants.BORDER_PRERCENTAGES[0]
    },
    standardBorderRightPercentExport = new int [] {
        	Constants.BORDER_PRERCENTAGES[0], 
        	Constants.BORDER_PRERCENTAGES[0]
    }, 
    standardBorderTopPercentExport  = new int [] {
        	Constants.BORDER_PRERCENTAGES[0], 
        	Constants.BORDER_PRERCENTAGES[0]
    },
    standardBorderBottomPercentExport  = new int [] {
        	Constants.BORDER_PRERCENTAGES[0], 
        	Constants.BORDER_PRERCENTAGES[0]
    };

    
    /**
     * selected pens.
     */
    private static Pen []
    standardPenSelected1 = new Pen [] {
    	new Pencil(Constants.PEN_ID_LINES, 2, Color.gray), 
    	new Pencil(Constants.PEN_ID_LINES, 2, Color.gray)
    },
    standardPenSelected2 = new Pen [] {
    	new BallPen(Constants.PEN_ID_LINES, 2, 
    			new Color(2 * 2 * 2 * 2 * 2 * 2, 
    					(int) (Math.pow(2, 2 + 2 + 2 + 1) 
    							+ Math.pow(2, 2 + 2 + 2) + 2 + 2 + 1), 
                        (2 + 1) * (2 + 1) * (2 + 1) + (2 + 2 + 2 + 1) 
                        * (2 + 2 + 2 + 2 + 2))),
        new BallPen(Constants.PEN_ID_LINES, 2, 
        		new Color(2 * 2 * 2 * 2 * 2 * 2, 
        				(int) (Math.pow(2, 2 + 2 + 2 + 1) 
        						+ Math.pow(2, 2 + 2 + 2) + 2 + 2 + 1), 
        						(2 + 1) * (2 + 1) * (2 + 1) + (2 + 2 + 2 + 1) 
        						* (2 + 2 + 2 + 2 + 2)))
    };	
    
    
    
    
    
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
    
    /**
     * the location of the user Workspace if this mode is selected.
     */
    private static String wsLocation = "";
    
    
    
    /**
     * Used if file not found.
     */
    public static final String ALTERNATIVE_FILE_START = "/home/juli/Software/";
    
    


    /**
     * @return the wsLocation
     */
    public static String getWsLocation() {
        return wsLocation;
    }


    /**
     * @param _wsLocation the wsLocation to set
     */
    public static void setWsLocation(final String _wsLocation) {
        StateStandard.wsLocation = _wsLocation;
    }
    
    


	/**
	 * @return the standardPenSelected2
	 */
	public static Pen [] getStandardPenSelected2() {
		return standardPenSelected2;
	}


	/**
	 * @param standardPenSelected2 the standardPenSelected2 to set
	 */
	public static void setStandardPenSelected2(Pen [] standardPenSelected2) {
		StateStandard.standardPenSelected2 = standardPenSelected2;
	}


	/**
	 * @return the standardPenSelected1
	 */
	public static Pen [] getStandardPenSelected1() {
		return standardPenSelected1;
	}


	/**
	 * @param standardPenSelected1 the standardPenSelected1 to set
	 */
	public static void setStandardPenSelected1(Pen [] standardPenSelected1) {
		StateStandard.standardPenSelected1 = standardPenSelected1;
	}


	/**
	 * @return the standardBorderBottomPercentExport
	 */
	public static int [] getStandardBorderBottomPercentExport() {
		return standardBorderBottomPercentExport;
	}


	/**
	 * @param standardBorderBottomPercentExport the standardBorderBottomPercentExport to set
	 */
	public static void setStandardBorderBottomPercentExport(
			int [] standardBorderBottomPercentExport) {
		StateStandard.standardBorderBottomPercentExport = standardBorderBottomPercentExport;
	}


	/**
	 * @return the standardBorderTopPercentExport
	 */
	public static int [] getStandardBorderTopPercentExport() {
		return standardBorderTopPercentExport;
	}


	/**
	 * @param standardBorderTopPercentExport the standardBorderTopPercentExport to set
	 */
	public static void setStandardBorderTopPercentExport(
			int [] standardBorderTopPercentExport) {
		StateStandard.standardBorderTopPercentExport = standardBorderTopPercentExport;
	}


	/**
	 * @return the standardBorderRightPercentExport
	 */
	public static int [] getStandardBorderRightPercentExport() {
		return standardBorderRightPercentExport;
	}


	/**
	 * @param standardBorderRightPercentExport the standardBorderRightPercentExport to set
	 */
	public static void setStandardBorderRightPercentExport(
			int [] standardBorderRightPercentExport) {
		StateStandard.standardBorderRightPercentExport = standardBorderRightPercentExport;
	}


	/**
	 * @return the standardBorderLeftPercentExport
	 */
	public static int [] getStandardBorderLeftPercentExport() {
		return standardBorderLeftPercentExport;
	}


	/**
	 * @param standardBorderLeftPercentExport the standardBorderLeftPercentExport to set
	 */
	public static void setStandardBorderLeftPercentExport(
			int [] standardBorderLeftPercentExport) {
		StateStandard.standardBorderLeftPercentExport = standardBorderLeftPercentExport;
	}


	/**
	 * @return the standardBorderBottomPercentShow
	 */
	public static int [] getStandardBorderBottomPercentShow() {
		return standardBorderBottomPercentShow;
	}


	/**
	 * @param standardBorderBottomPercentShow the standardBorderBottomPercentShow to set
	 */
	public static void setStandardBorderBottomPercentShow(
			int [] standardBorderBottomPercentShow) {
		StateStandard.standardBorderBottomPercentShow = standardBorderBottomPercentShow;
	}


	/**
	 * @return the standardBorderTopPercentShow
	 */
	public static int [] getStandardBorderTopPercentShow() {
		return standardBorderTopPercentShow;
	}


	/**
	 * @param standardBorderTopPercentShow the standardBorderTopPercentShow to set
	 */
	public static void setStandardBorderTopPercentShow(
			int [] standardBorderTopPercentShow) {
		StateStandard.standardBorderTopPercentShow = standardBorderTopPercentShow;
	}


	/**
	 * @return the standardBorderRightPercentShow
	 */
	public static int [] getStandardBorderRightPercentShow() {
		return standardBorderRightPercentShow;
	}


	/**
	 * @param standardBorderRightPercentShow the standardBorderRightPercentShow to set
	 */
	public static void setStandardBorderRightPercentShow(
			int [] standardBorderRightPercentShow) {
		StateStandard.standardBorderRightPercentShow = standardBorderRightPercentShow;
	}


	/**
	 * @return the standardBorderLeftPercentShow
	 */
	public static int [] getStandardBorderLeftPercentShow() {
		return standardBorderLeftPercentShow;
	}


	/**
	 * @param standardBorderLeftPercentShow the standardBorderLeftPercentShow to set
	 */
	public static void setStandardBorderLeftPercentShow(
			int [] standardBorderLeftPercentShow) {
		StateStandard.standardBorderLeftPercentShow = standardBorderLeftPercentShow;
	}


	/**
	 * @return the standardEraseRadius
	 */
	public static int[] getStandardEraseRadius() {
		return standardEraseRadius;
	}


	/**
	 * @param standardEraseRadius the standardEraseRadius to set
	 */
	public static void setStandardEraseRadius(int[] standardEraseRadius) {
		StateStandard.standardEraseRadius = standardEraseRadius;
	}


	/**
	 * @return the standardimageheight
	 */
	public static int[] getStandardimageheight() {
		return standardImageHeight;
	}


	/**
	 * @return the standardImageWidth
	 */
	public static int[] getStandardImageWidth() {
		return standardImageWidth;
	}


	/**
	 * @param standardImageWidth the standardImageWidth to set
	 */
	public static void setStandardImageWidth(int[] standardImageWidth) {
		StateStandard.standardImageWidth = standardImageWidth;
	}


	/**
	 * @return the standardIndexSelection
	 */
	public static int[] getStandardIndexSelection() {
		return standardIndexSelection;
	}


	/**
	 * @param standardIndexSelection the standardIndexSelection to set
	 */
	public static void setStandardIndexSelection(
			int[] standardIndexSelection) {
		StateStandard.standardIndexSelection = standardIndexSelection;
	}


	/**
	 * @return the standardIndexPageBackgroundExport
	 */
	public static int[] getStandardIndexPageBackgroundExport() {
		return standardIndexPageBackgroundExport;
	}


	/**
	 * @param standardIndexPageBackgroundExport the standardIndexPageBackgroundExport to set
	 */
	public static void setStandardIndexPageBackgroundExport(
			int[] standardIndexPageBackgroundExport) {
		StateStandard.standardIndexPageBackgroundExport = standardIndexPageBackgroundExport;
	}


	/**
	 * @return the standardIndexPageBackground
	 */
	public static int[] getStandardIndexPageBackground() {
		return standardIndexPageBackground;
	}


	/**
	 * @param standardIndexPageBackground the standardIndexPageBackground to set
	 */
	public static void setStandardIndexPageBackground(
			int[] standardIndexPageBackground) {
		StateStandard.standardIndexPageBackground = standardIndexPageBackground;
	}


	/**
	 * @return the standardIndexOperation
	 */
	public static int[] getStandardIndexOperation() {
		return standardIndexOperation;
	}


	/**
	 * @param standardIndexOperation the standardIndexOperation to set
	 */
	public static void setStandardIndexOperation(
			int[] standardIndexOperation) {
		StateStandard.standardIndexOperation = standardIndexOperation;
	}


	/**
	 * @return the standardShowAlpha
	 */
	public static boolean[] getStandardShowAlpha() {
		return standardShowAlpha;
	}


	/**
	 * @param standardShowAlpha the standardShowAlpha to set
	 */
	public static void setStandardShowAlpha(boolean[] standardShowAlpha) {
		StateStandard.standardShowAlpha = standardShowAlpha;
	}


	/**
	 * @return the standardExportAlpha
	 */
	public static boolean[] getStandardExportAlpha() {
		return standardExportAlpha;
	}


	/**
	 * @param standardExportAlpha the standardExportAlpha to set
	 */
	public static void setStandardExportAlpha(boolean[] standardExportAlpha) {
		StateStandard.standardExportAlpha = standardExportAlpha;
	}


	/**
	 * @return the standardSaveFormat
	 */
	public static String[] getStandardSaveFormat() {
		return standardSaveFormat;
	}


	/**
	 * @param standardSaveFormat the standardSaveFormat to set
	 */
	public static void setStandardSaveFormat(String[] standardSaveFormat) {
		StateStandard.standardSaveFormat = standardSaveFormat;
	}


	/**
	 * @return the standardEraseIndex
	 */
	public static int[] getStandardEraseIndex() {
		return standardEraseIndex;
	}


	/**
	 * @param standardEraseIndex the standardEraseIndex to set
	 */
	public static void setStandardEraseIndex(int[] standardEraseIndex) {
		StateStandard.standardEraseIndex = standardEraseIndex;
	}
    

}
