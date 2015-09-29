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


import java.awt.Color;
import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.util.logging.Logger;

import javax.swing.JOptionPane;

import control.ControlPaint;
import control.forms.tabs.CTabWrite;
import view.View;
import view.forms.Page;
import model.objects.painting.PaintBI;
import model.objects.painting.Picture;
import model.objects.pen.Pen;
import model.objects.pen.normal.BallPen;
import model.objects.pen.normal.Marker;
import model.objects.pen.normal.Pencil;
import model.util.paint.Utils;

/**
 * the current status of the program. E.g. which which operation is performed
 * (pen/selection/pipette...)
 * 
 * @author Julius Huelsmann
 * @version %I%, %U%
 */
public final class State {


	/*
	 * Values that never change (once initialized).
	 */
    
    /**
     * The entire list of available pens.
     */
    private static final Pen[] PEN_AVAILABLE = new Pen[]{
    		new Pencil(Constants.PEN_ID_POINT, 2, Color.black),
            new Pencil(Constants.PEN_ID_LINES, 2, Color.black),
            new Pencil(Constants.PEN_ID_MATHS, 2, Color.black),

            new BallPen(Constants.PEN_ID_MATHS, 2, Color.black),
            new BallPen(Constants.PEN_ID_LINES, 2, Color.black),
            new BallPen(Constants.PEN_ID_POINT, 2, Color.black),

            new Marker(Constants.PEN_ID_LINES, 2, Color.black)};

    
    /**
     * The BufferedImage which is shown for the transparency.
     * Is contained by a JLabel in background of the image.
     */
    private static BufferedImage biTransparency;
    
    
    
    
    
    
    
    
    
	
    
    
    
    
    
	/*
	 * Instance of the main controller class for being able to inform the
	 * rest of the program if the status has changed somehow and instance
	 * of a logger.
	 */

	/**
	 * Instance of the paint controller.
	 */
	private static ControlPaint controlPaint;

    /**
     * The logger.
     */
    private static final Logger LOGGER = Logger.getLogger("info"); 
    
	
    
    /**
     * Identifier for startup.
     */
	private final int startupIdentifier;
    
    
    
    
    
    
    
    
    
    
	

	/*
	 * Status values that often change during the execution of the program
	 * but demand initialization by settings-class.
	 */
	

	/**
	 * The current erase index which contains the identifier for the current
	 * erase operation.
	 */
	private static int eraseIndex;


    /**
     * The format in which the image files are saved.
     */
    private static String saveFormat;
    
    /**
     * Whether to export alpha transparently or to replace it with white color.
     */
    private static boolean exportAlpha;

    /**
     * Whether to export alpha transparently or to replace it with white color.
     */
    private static boolean showAlpha;


    /**
     * the index indicates which operation is to be performed. (e.g. pen/
     * selection mode 1 - 3, ...).
     */
    private static int indexOperation;
    
    /**
     * The displayed Background of the pages.
     */
    private static int indexPageBackground;
    
    
    /**
     * The exported background of pages.
     */
    private static int indexPageBackgroundExport;
    
    /**
     * index contains the selected kind of selection. (separate paintObjects,
     * whole paintObjects, image).
     */
    private static int indexSelection;

    
    
    /**
     * Erase radius.
     */
    private static int eraseRadius;

    /**
     * The border show percentages.
     */
    private static int
    borderLeftPercentShow,
    borderRightPercentShow,
    borderTopPercentShow,
    borderBottomPercentShow;
    
    /**
     * The border export percentages.
     */
    private static int 
    borderLeftPercentExport,
    borderRightPercentExport,
    borderTopPercentExport,
    borderBottomPercentExport;

    
    /**
     * selected pens.
     */
    private static Pen 
    penSelected1,
    penSelected2;

    /**
     * the size of the image. (depends on start image width and start image 
     * height, those values are determined by settings).
     */
    private static Dimension imageSize;

    
    /**
     * The current zoom is equal to 
     * <code> imageSize 
     * 		.* ViewSettings.ZOOM_MULITPLICATOR ^ State.getZoomState();</code>.
     */
    private static int zoomState = 0;
    

    /**
     * Returns the <code>zoomState</code>. The image size can be extracted
     * out of the current <code>zoomState</code> by applying the following
     * formula: <br>
     * <code> imageSize 
     * 		.* ViewSettings.ZOOM_MULITPLICATOR ^ State.getZoomState();</code>.
     * 
     * @see #zoomState
     * @see #zoomStateZoomIn()
     * @see #zoomStateZoomOut()
     */
    public static final int getZoomState() {
    	return zoomState;
    }

    /**
     * Increases the <code>zoomState</code>.
     * @see #zoomState
     * @see #zoomStateZoomOut()
     * @see #getZoomState()
     */
    public static final void zoomStateZoomIn() {
    	zoomState++;
    }
    
    /**
     * Decreases the <code>zoomState</code>.
     * @see #zoomState
     * @see #zoomStateZoomIn()
     * @see #getZoomState()
     */
    public static final void zoomStateZoomOut() {
    	zoomState--;
    }

    /**
     * Initialize the values that depend on the standard state (depending
     * on the way the program is opened (open image file/ launch program
     * without parameter).
     */
    public void initialize() {

    	eraseIndex = StateStandard.getStandardEraseIndex()
    			[startupIdentifier];
    	
    	
    	saveFormat = StateStandard.getStandardSaveFormat()[startupIdentifier];
    	exportAlpha 
    	= StateStandard.getStandardExportAlpha()[startupIdentifier];
    	
    	showAlpha 
    	= StateStandard.getStandardShowAlpha()[startupIdentifier];
    	
    	indexOperation = StateStandard.getStandardIndexOperation()
    			[startupIdentifier];
    	
    	indexPageBackground 
    	= StateStandard.getStandardIndexPageBackground()
    	[startupIdentifier];
    	
    	indexPageBackgroundExport 
    	= StateStandard.getStandardIndexPageBackgroundExport()
    	[startupIdentifier];
    	
    	indexSelection = StateStandard.getStandardIndexSelection()
    			[startupIdentifier];
    	
    	
    	eraseRadius = StateStandard.getStandardEraseRadius()
    			[startupIdentifier];
    	
    	borderLeftPercentShow 
    	= StateStandard.getStandardBorderLeftPercentShow()
    	[startupIdentifier];
    	borderRightPercentShow 
    	= StateStandard.getStandardBorderRightPercentShow()
    	[startupIdentifier];
    	borderBottomPercentShow 
    	= StateStandard.getStandardBorderBottomPercentShow()
    	[startupIdentifier];
    	borderTopPercentShow 
    	= StateStandard.getStandardBorderTopPercentShow()
    	[startupIdentifier];
    	
    	
    	borderLeftPercentExport 
    	= StateStandard.getStandardBorderLeftPercentExport()
    	[startupIdentifier];
    	borderRightPercentExport 
    	= StateStandard.getStandardBorderRightPercentExport()
    	[startupIdentifier];
    	borderBottomPercentExport 
    	= StateStandard.getStandardBorderBottomPercentExport()
    	[startupIdentifier];
    	borderTopPercentExport 
    	= StateStandard.getStandardBorderTopPercentExport()
    	[startupIdentifier];
    	
    	penSelected1 = StateStandard.getStandardPenSelected1()
    			[startupIdentifier];
    	penSelected2 = StateStandard.getStandardPenSelected1()
    			[startupIdentifier];
    	
    	
    	imageSize = new Dimension(
    			StateStandard.getStandardImageWidth()
    			[startupIdentifier],
    			StateStandard.getStandardimageheight()
    			[startupIdentifier]);
    	
    	imageShowSize = imageSize;
    	
    }
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    

	/*
	 * Other status values that often change during the execution of the program
	 * and do not demand initialization by the settings-class.
	 */
	
	/**
	 * The pen which is replaced by pen selection. (To be restored after 
	 * selection line has been done).
	 */
	private static Pen pen_selectedReplaced;

    /**
     * the size of the shown image (zoom).
     */
    private static Dimension imageShowSize;
	
	
	/**
	 * Whether the initialization process has finished or not. If it has,
	 * the fadeOut can disappear.
	 */
	private static int initializationFinished = 0;
	
	
    /**
     * The open project.
     */
    private static String openProject = "";
    
    
    /**
     * whether currently rotated or not. Used if images change for getting
     * the right rotation of the images.
     */
    private static boolean normalRotation = true;
   
    
    /**
     * This value contains whether to print debug options or to print
     * normal output.
     */
    private static boolean debug = false;

    
    /**
     * This counter counts the amount of image points that are printed
     * to screen (just for debugging purpose).
     */
    private static int counterPaintedPoints = 0;
    
    /**
     * The path where to save the things.
     */
    private static String savePath = "";
    
    
    /**
     * uncommitted changes.
     */
    private static boolean uncommittedChanges = false;
    
    
    
    
    
    
    
    
    
    /*
     * Constructor.
     *  
     */
    
    
    
    /**
     * Empty, private utility class constructor.
     */
    public State(final int _startupIdentifier) {
    	startupIdentifier = _startupIdentifier;
    }

    
    
    
    
    
    
    
    
    
    
    /*
     * Functions.
     */
    
    
    
    /**
     * Return the size of the raster which is printed by the
     * class Utils.
     * 
     * @return the size.
     */
    public static int getRasterSize() {
        final int min = 1;
        final int normal = 10;
        
        return Math.max(min, normal * imageShowSize.width / imageSize.width);
    }
    
    
    /**
     * return the size of the first border (left edge) for the raster.
     * (the border is not filled with background raster).
     * 
     * @return the size
     */
    public static int getRasterBorderFront() {
        
        final int maxPercent = 100;
        return State.getImageShowSize().width 
                * borderLeftPercentShow / maxPercent;
    }

    /**
     * return the size of the last border (right edge) for the raster.
     * (the border is not filled with background raster).
     * 
     * @return the size
     */
    public static int getRasterBorderEnd() {

        final int maxPercent = 100;

        int lastBorder = State.getImageShowSize().width 
                * borderRightPercentShow / maxPercent;
        int size = imageShowSize.width - getRasterBorderFront() - lastBorder;
        
        return lastBorder
                //thus, it is impossible that the last boxes do not
                //terminate.
                + (size % getRasterSize() - 1);
    }
    

    
    /**
     * return the size of the first border (left edge) for the raster.
     * (the border is not filled with background raster).
     * 
     * @return the size
     */
    public static int getRasterBorderTop() {
        
        final int maxPercent = 100;
        return State.getImageShowSize().height
                * borderTopPercentShow / maxPercent;
    }

    /**
     * return the size of the last border (right edge) for the raster.
     * (the border is not filled with background raster).
     * 
     * @return the size
     */
    public static int getRasterBorderBottom() {

        final int maxPercent = 100;

        int lastBorder = State.getImageShowSize().height 
                * borderBottomPercentShow / maxPercent;
        int size = imageShowSize.height - getRasterBorderTop() - lastBorder;
        
        return lastBorder
                //thus, it is impossible that the last boxes do not
                //terminate.
                + (size % getRasterSize() - 1);
    }
    

    /**
     * Get the border size.
     * @return the border size.
     */
    public static int getMargeLeft() {
        final int hundred = 100;
        return borderLeftPercentShow * getImageSize().width / hundred;
    }

    /**
     * Get the border size.
     * @return the border size.
     */
    public static int getMargeRight() {
        final int hundred = 100;
        return borderRightPercentShow * getImageSize().width / hundred;
    }
    /**
     * Get the border size.
     * @return the border size.
     */
    public static int getMargeTop() {
        final int hundred = 100;
        return borderTopPercentShow * getImageSize().height / hundred;
    }
    /**
     * Get the border size.
     * @return the border size.
     */
    public static int getMargeBottom() {
        final int hundred = 100;
        return borderBottomPercentShow * getImageSize().height / hundred;
    }
    
    /**
     * setter method for index operation.
     * @param _index the index to set
     */
    public static void setIndexOperation(final int _index) {
        indexOperation = _index;
    }
    
    /**
     * simple getter method for index operation.
     * @return the operation index.
     */
    public static int getIndexOperation() {
        return indexOperation;
    }
    

    /**
     * make the index readable.
     * 
     * @param _index 	the index that is checked.
     * @return 			the operation index.
     */
    public static String getIndexName(final int _index) {
        switch (_index) {
        case Constants.CONTROL_PAINTING_INDEX_PAINT_1:
        	return "paint1";
        case Constants.CONTROL_PAINTING_INDEX_PAINT_2:
        	return "paint2";
        case Constants.CONTROL_PAINTING_INDEX_FILL:
        	return "fill";
        case Constants.CONTROL_PAINTING_INDEX_PIPETTE:
        	return "pipette";
        case Constants.CONTROL_PAINTING_INDEX_SELECTION_CURVE:
        	return "selection curve";
        case Constants.CONTROL_PAINTING_INDEX_SELECTION_LINE:
        	return "selection line";
        case Constants.CONTROL_PAINTING_INDEX_SELECTION_MAGIC:
        	return "selection magic";
        case Constants.CONTROL_PAINTING_INDEX_ZOOM_IN:
        	return "zoom in";
        case Constants.CONTROL_PAINTING_INDEX_MOVE:
        	return "move";
        case Constants.CONTROL_PAINTING_INDEX_ERASE:
        	return "erase";
        case Constants.CONTROL_PAINTING_INDEX_I_G_LINE:
        	return "i_g_line";
        case Constants.CONTROL_PAINTING_INDEX_I_G_RECTANGLE:
        	return "i_g_rect";
        case Constants.CONTROL_PAINTING_INDEX_I_G_CURVE:
        	return "i_g_curve";
        case Constants.CONTROL_PAINTING_INDEX_I_G_TRIANGLE:
        	return "i_g_triangle";
        case Constants.CONTROL_PAINTING_INDEX_I_G_ARCH:
        	return "i_g_arch";
        case Constants.CONTROL_PAINTING_INDEX_I_D_DIA:
        	return "i_d_dia";
        case Constants.CONTROL_PAINTING_INDEX_I_G_ARCH_FILLED:
        	return "i_g_arch_filled";
        case Constants.CONTROL_PAINTING_INDEX_I_G_RECTANGLE_FILLED:
        	return "i_g_rect_filled";
        case Constants.CONTROL_PAINTING_INDEX_I_G_TRIANGLE_FILLED:
        	return "i_g_tri_filled";
        case Constants.CONTROL_PAINTING_INDEX_I_G_CURVE_2:
        	return "i_g_curve";
        default:
        	return "index not found. " + _index;
        }
    }

    /**
     * @return the indexSelection
     */
    public static int getIndexSelection() {
        return indexSelection;
    }

    /**
     * @param _indexSelection the indexSelection to set
     */
    public static void setIndexSelection(final int _indexSelection) {
        State.indexSelection = _indexSelection;
    }

    /**
     * @return the imageSize
     */
    public static Dimension getImageSize() {
        return imageSize;
    }

    /**
     * @param _imageSize the imageSize to set
     */
    public static void setImageSize(final Dimension _imageSize) {
        
        State.imageSize = _imageSize;
    }

    /**
     * @return the normalRotation
     */
    public static boolean isNormalRotation() {
        return normalRotation;
    }

    /**
     * @param _normalRotation the normalRotation to set
     */
    public static void setNormalRotation(final boolean _normalRotation) {
        State.normalRotation = _normalRotation;
    }

    /**
     * @return the penSelected1
     */
    public static Pen getPenSelected1() {
        return penSelected1;
    }

    /**
     * @param _penSelected1 the penSelected1 to set
     */
    public static void setPenSelected1(final Pen _penSelected1) {
        State.penSelected1 = _penSelected1;
        getControlTabWrite().penChanged();
        getPicture().changePen(penSelected1);
    }

    /**
     * @return the penSelected2
     */
    public static Pen getPenSelected2() {
        return penSelected2;
    }

    /**
     * @param _penSelected2 the penSelected2 to set
     */
    public static void setPenSelected2(final Pen _penSelected2) {
        State.penSelected2 = _penSelected2;
        getControlTabWrite().penChanged();
    }

    /**
     * @return the imageShowSize
     */
    public static Dimension getImageShowSize() {
        return imageShowSize;
    }

    /**
     * @param _imageShowSize the imageShowSize to set
     */
    public static void setImageShowSize(final Dimension _imageShowSize) {
        State.imageShowSize = _imageShowSize;
        //frefresh sps due to the new size of the image.
        controlPaint.getView().getPage().flip();
        controlPaint.getView().getPage().refrehsSps();
    }

    /**
     * @return the debug
     */
    public static boolean isDebug() {
        return debug;
    }


    /**
     * @return the savePath
     */
    public static String getSavePath() {
        if (savePath == "") {
            return null;
        }
        return savePath;
    }


    /**
     * @param _savePath the savePath to set
     */
    public static void setSavePath(final String _savePath) {
        State.savePath = _savePath;
    }


    /**
     * @return the uncommittedChanges
     */
    public static boolean isUncommittedChanges() {
        return uncommittedChanges;
    }


    /**
     * @param _uncommittedChanges the uncommittedChanges to set
     */
    public static void setUncommittedChanges(
            final boolean _uncommittedChanges) {

        uncommittedChanges = _uncommittedChanges;
    }


    /**
     * @return the counter_paintedPoints
     */
    public static int getCounter_paintedPoints() {
        return counterPaintedPoints;
    }


    /**
     * @param _counter_paintedPoints the counter_paintedPoints to set
     */
    public static void setCounter_paintedPoints(
            final int _counter_paintedPoints) {
        State.counterPaintedPoints = _counter_paintedPoints;
    }


    /**
     * @return the logger
     */
    public static Logger getLogger() {
        return LOGGER;
    }


    /**
     * @return the indexPageBackground
     */
    public static int getIndexPageBackground() {
        return indexPageBackground;
    }


    /**
     * @param _indexPageBackground the indexPageBackground to set
     */
    public static void setIndexPageBackground(final int _indexPageBackground) {
        State.indexPageBackground = _indexPageBackground;
    }


    /**
     * @param _indexPageBackground the indexPageBackground to set
     */
    public static void setIndexPageBackgroundExport(
            final int _indexPageBackground) {
        State.indexPageBackgroundExport = _indexPageBackground;
    }
    /**
     * @return the indexPageBackground
     */
    public static int getIndexPageBackgroundExport() {
        return indexPageBackgroundExport;
    }


    /**
     * @return the openProject
     */
    public static String getOpenProject() {
        return openProject;
    }


    /**
     * @param _openProject the openProject to set
     */
    public static void setOpenProject(final String _openProject) {
        State.openProject = _openProject;
    }



    /**
     * @return the bi_transparency
     */
    public static BufferedImage getBi_transparency() {
        
        if (biTransparency == null) {
            
            setShowAlpha(showAlpha);
        }
        return biTransparency;
    }




    /**
     * @param _borderLeftPercent the borderLeftPercent to set
     */
    public static void setBorderLeftPercent(final int _borderLeftPercent) {
        State.borderLeftPercentShow = _borderLeftPercent;
    }



    /**
     * @param _borderRightPercent the borderRightPercent to set
     */
    public static void setBorderRightPercent(final int _borderRightPercent) {
        State.borderRightPercentShow = _borderRightPercent;
    }




    /**
     * @param _borderTopPercent the borderTopPercent to set
     */
    public static void setBorderTopPercent(final int _borderTopPercent) {
        State.borderTopPercentShow = _borderTopPercent;
    }



    /**
     * @param _borderBottomPercent the borderBottomPercent to set
     */
    public static void setBorderBottomPercent(final int _borderBottomPercent) {
        State.borderBottomPercentShow = _borderBottomPercent;
    }
    

    /**
     * Simple getter method.
     * @return the percentage
     */
    public static int getBorderLeftPercent() {
        return borderLeftPercentShow;
    }
    /**
     * Simple getter method.
     * @return the percentage
     */
    public static int getBorderRightPercent() {
        return borderRightPercentShow;
    }
    /**
     * Simple getter method.
     * @return the percentage
     */
    public static int getBorderTopPercent() {
        return borderTopPercentShow;
    }
    /**
     * Simple getter method.
     * @return the percentage
     */
    public static int getBorderBottomPercent() {
        return borderBottomPercentShow;
    }


    /**
     * @return the borderLeftPercentExport
     */
    public static int getBorderLeftPercentExport() {
        return borderLeftPercentExport;
    }


    /**
     * @param _borderLeftPercentExport the borderLeftPercentExport to set
     */
    public static void setBorderLeftPercentExport(
            final int _borderLeftPercentExport) {
    	
        State.borderLeftPercentExport = _borderLeftPercentExport;
    }


    /**
     * @return the borderRightPercentExport
     */
    public static int getBorderRightPercentExport() {
        return borderRightPercentExport;
    }


    /**
     * @param _borderRightPercentExport the borderRightPercentExport to set
     */
    public static void setBorderRightPercentExport(
            final int _borderRightPercentExport) {
        State.borderRightPercentExport = _borderRightPercentExport;
    }


    /**
     * @return the borderTopPercentExport
     */
    public static int getBorderTopPercentExport() {
    
        return borderTopPercentExport;
    }


    /**
     * @param _borderTopPercentExport the borderTopPercentExport to set
     */
    public static void setBorderTopPercentExport(
            final int _borderTopPercentExport) {
        State.borderTopPercentExport = _borderTopPercentExport;
    }


    /**
     * @return the borderBottomPercentExport
     */
    public static int getBorderBottomPercentExport() {
        return borderBottomPercentExport;
    }


    /**
     * @param _borderBottomPercentExport the borderBottomPercentExport to set
     */
    public static void setBorderBottomPercentExport(
            final int _borderBottomPercentExport) {
        State.borderBottomPercentExport = _borderBottomPercentExport;
    }


    /**
     * @return the exportAlpha
     */
    public static boolean isExportAlpha() {
        return exportAlpha;
    }


    /**
     * @param _exportAlpha the exportAlpha to set
     */
    public static void setExportAlpha(final boolean _exportAlpha) {
        exportAlpha = _exportAlpha;
    }


    /**
     * @return the showAlpha
     */
    public static boolean isShowAlpha() {
        return showAlpha;
    }


    /**
     * @param _showAlpha the showAlpha to set
     */
    public static void setShowAlpha(final boolean _showAlpha) {
        
        
        State.showAlpha = _showAlpha;
        
        if (getPage().getJlbl_background().getWidth() <= 0
                || getPage().getJlbl_background().getHeight() <= 0) {
            return;
        }
        if (_showAlpha) {

            final int transparencyRectanlgeSize = 25;
            final Color color1 = new Color(234, 239, 242);
            final Color color2 = new Color(250, 252, 255);
            
            biTransparency = Utils.paintRastarBlock(
                    new BufferedImage(
                            getPage().getJlbl_background().getWidth(), 
                            getPage().getJlbl_background()
                            .getHeight(), 
                            BufferedImage.TYPE_INT_RGB), 
                    new Color[]{color1, color2},
                    new Rectangle(0, 0, 
                            getPage().getJlbl_background().getWidth(), 
                            getPage().getJlbl_background()
                            .getHeight()), 
                            transparencyRectanlgeSize);

        } else {
            
            biTransparency =  
                    new BufferedImage(
                            getPage().getJlbl_background().getWidth(), 
                            getPage().getJlbl_background()
                            .getHeight(), 
                            BufferedImage.TYPE_INT_RGB);
            PaintBI.fillRectangleQuick(biTransparency, Color.white, 
                    new Rectangle(0, 0, 
                            getPage().getJlbl_background().getWidth(), 
                            getPage().getJlbl_background()
                            .getHeight()));
        }
    }

    
    


    /**
     * Apply the standard pen operation in picture and status.
     * @param _picture the picture
     */
	public static void applyStandardPen(final Picture _picture) {

		getPicture().initializePen(
                new BallPen(Constants.PEN_ID_POINT, 1, Color.black));
        setIndexOperation(Constants.CONTROL_PAINTING_INDEX_PAINT_1);	
	}

    /**
     * @return the saveFormat
     */
    public static String getSaveFormat() {
        return saveFormat;
    }


    /**
     * @param _saveFormat the saveFormat to set
     */
    public static void setSaveFormat(final String _saveFormat) {
        State.saveFormat = _saveFormat;
    }


	/**
	 * @return the eraseRadius
	 */
	public static int getEraseRadius() {
		return eraseRadius;
	}


	/**
	 * @param _eraseRadius the eraseRadius to set
	 */
	public static void setEraseRadius(final int _eraseRadius) {
		State.eraseRadius = _eraseRadius;
	}


	/**
	 * @return the pen_available
	 */
	public static Pen[] getPen_available() {
		return PEN_AVAILABLE;
	}
	


	/**
	 * @return the initializationFinished
	 */
	public static boolean isInitializationFinished() {
		return (initializationFinished >= 2);
	}

	
	/**
	 * Set the current operation finished.
	 */
	public static synchronized void increaseInitializationFinished() {
		initializationFinished++;
	}
	
	/**
	 * Method for showing dialog.
	 * @param _message the text message
	 */
	public static void showMessageDialog(final String _message) {
		JOptionPane.showMessageDialog(getView(), _message);
	}
	/**
	 * @return the eraseIndex
	 */
	public static int getEraseIndex() {
		return eraseIndex;
	}
	/**
	 * @param _eraseIndex the eraseIndex to set
	 */
	public static void setEraseIndex(final int _eraseIndex) {
		State.eraseIndex = _eraseIndex;
	}
	
	
	/**
	 * 
	 * @return the borderRightPercentShow
	 */
	public static int getBorderRightPercentShow() {
		return borderRightPercentShow;
	}
	
	/**
	 * 
	 * @param _borderRightPercentShow
	 */
	public static void setBorderRightPercentShow(
			final int _borderRightPercentShow) {
		State.borderRightPercentShow = _borderRightPercentShow;
	}
	
	/**
	 * @return the border bottom percent show
	 */
	public static int getBorderBottomPercentShow() {
		return borderBottomPercentShow;
	}
	/**
	 * @return the pen_selectedReplaced
	 */
	public static Pen getPen_selectedReplaced() {
		return pen_selectedReplaced;
	}
	/**
	 * @param _pen_selectedReplaced the pen_selectedReplaced to set
	 */
	public static void setPen_selectedReplaced(
			final Pen _pen_selectedReplaced) {
		State.pen_selectedReplaced = _pen_selectedReplaced;
	}

	
	
	
	/*
	 * Error-checked getter methods.
	 */
	
	
	
	


	/**
	 * error-checked getter method.
	 * @return the controller class of the tab write.
	 */
	private static CTabWrite getControlTabWrite() {
		if (controlPaint != null) {

			return controlPaint.getcTabWrite();
		} else {
			State.getLogger().severe("controlPaint.cTabWrite is null");
		}
		return null;
	}
	/**
	 * error-checked getter method.
	 * @return the view class page.
	 */
	private static Page getPage() {
		
		if (getView() != null) {
			return getView().getPage();
		} else {
			State.getLogger().severe("controlPaint.getView() is null.");
		}
		return controlPaint.getView().getPage();
	}

	/**
	 * error-checked getter method.
	 * @return the main view class.
	 */
	private static View getView() {
		if (controlPaint != null) {
			return controlPaint.getView();
		} else {
			State.getLogger().severe("controlPaint is null");
		}
		
		return null;
	}
	
	

	/**
	 * Setter method for control paint.
	 * @param _controlPaint the control paint.
	 */
	public static void setControlPaint(
			final ControlPaint _controlPaint) {
		if (_controlPaint != null) {

			controlPaint = _controlPaint;
		} else {
			
			if (controlPaint == null) {

				getLogger().severe("initialized controlPaint "
						+ "in Status with "
						+ "not existing controller class.");
			} else {

				getLogger().severe("Tried to overwrite"
						+ " existing controller class in"
						+ " Status.");
			}
		}
	}
	
	
	/**
	 * Error-checked getter method.
	 * @return	instance of the main model class picture.
	 */
	private static Picture getPicture() {
		if (controlPaint != null) {
			return controlPaint.getPicture();
		} else {
			State.getLogger().severe("controlPaint is null");
		}
		return null;
	}











	public static boolean isBorder() {
		
		return (indexPageBackground != Constants.CONTROL_PAGE_BACKGROUND_NONE
				|| borderTopPercentShow != 0
				|| borderRightPercentShow != 0
				|| borderBottomPercentShow != 0
				|| borderLeftPercentShow != 0);
	}
}
