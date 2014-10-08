package model.settings;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.util.logging.Logger;
import view.forms.Page;
import model.objects.painting.PaintBI;
import model.objects.pen.Pen;
import model.objects.pen.normal.BallPen;
import model.util.paint.Utils;

/**
 * the current status of the program. E.g. which which operation is performed
 * (pen/selection/pipette...)
 * 
 * @author Julius Huelsmann
 * @version %I%, %U%
 */
public final class Status {

    /**
     * The open project.
     */
    private static String openProject = "";
    
    /**
     * the index indicates which operation is to be performed. (e.g. pen/
     * selection mode 1 - 3, ...).
     */
    private static int indexOperation = 
            Constants.CONTROL_PAINTING_INDEX_PAINT_1;
    
    /**
     * The Background of the pages.
     */
    private static int indexPageBackground = 
            Constants.CONTROL_PAGE_BACKGROUND_RASTAR;
    
    /**
     * index contains selection kind. (separate paintObjects,
     * whole paintObjects, image).
     */
    private static int indexSelection = 
            Constants.CONTROL_PAINTING_SELECTION_INDEX_COMPLETE_ITEM;

    /**
     * with these values the image size is initialized.
     */
    private static final int START_IMAGE_WIDTH = 2000; //2500;

    /**
     * with these values the image size is initialized.
     */
    private static final int START_IMAGE_HEIGHT = 2800; //3535;
    
    /**
     * the size of the image.
     */
    private static Dimension imageSize = new Dimension(
            START_IMAGE_WIDTH, START_IMAGE_HEIGHT);
    
    /**
     * the size of the shown image (zoom).
     */
    private static Dimension imageShowSize = imageSize;
    
    
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
     * selected pens.
     */
    private static Pen 
    penSelected1 = new BallPen(Constants.PEN_ID_LINES, 2, Color.black), 
    penSelected2 = new BallPen(Constants.PEN_ID_LINES, 2, Color.black);
    
    
    /**
     * This counter counts the amount of image points that are printed
     * to screen (just for debugging purpose).
     */
    private static int counter_paintedPoints = 0;
    
    /**
     * The path where to save the things.
     */
    private static String savePath = "";
    
    
    /**
     * The transparency id.
     */
    private static int id_transparency = Constants.ID_TRANSPARENCY_WHITE;
    
    /**
     * The BufferedImage which is shown for the transparency.
     * Is contained by a JLabel in background of the image.
     */
    private static BufferedImage bi_transparency;
    
    /**
     * The logger.
     */
    private static final Logger LOGGER = Logger.getLogger("info"); 
    
    /**
     * uncommitted changes.
     */
    private static boolean uncommittedChanges = false;
    
    /**
     * private utility class constructor.
     */
    private Status() { }

    
    /**
     * Return the size of the raster which is printed by the
     * class Utils.
     * 
     * @return the size.
     */
    public static int getRasterSize() {
        final int min = 1;
        final int normal = 28;
        
        return Math.max(min, normal * imageShowSize.width / imageSize.width);
    }
    
    
    /**
     * return the size of the first border (left edge) for the raster.
     * (the border is not filled with background raster).
     * 
     * @return the size
     */
    public static int getRasterBorderFront() {
        
        //the amount of boxes which are empty.
        final int amountOfBoxes = 6;
        return getRasterSize() * amountOfBoxes;
    }

    /**
     * return the size of the last border (right edge) for the raster.
     * (the border is not filled with background raster).
     * 
     * @return the size
     */
    public static int getRasterBorderEnd() {
        return (getRasterBorderFront() / 2) / getRasterSize() * getRasterSize()

                //thus, it is impossible that the last boxes do not
                //terminate.
                + (imageShowSize.width % getRasterSize() - 1);
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
     * @return the indexSelection
     */
    public static int getIndexSelection() {
        return indexSelection;
    }

    /**
     * @param _indexSelection the indexSelection to set
     */
    public static void setIndexSelection(final int _indexSelection) {
        Status.indexSelection = _indexSelection;
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
        Status.imageSize = _imageSize;
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
        Status.normalRotation = _normalRotation;
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
        Status.penSelected1 = _penSelected1;
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
        Status.penSelected2 = _penSelected2;
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
        Status.imageShowSize = _imageShowSize;
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
        Status.savePath = _savePath;
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
        return counter_paintedPoints;
    }


    /**
     * @param _counter_paintedPoints the counter_paintedPoints to set
     */
    public static void setCounter_paintedPoints(
            final int _counter_paintedPoints) {
        Status.counter_paintedPoints = _counter_paintedPoints;
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
        Status.indexPageBackground = _indexPageBackground;
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
        Status.openProject = _openProject;
    }


    /**
     * @param _id_transparency the id_transparency to set
     */
    public static void setId_transparency(final int _id_transparency) {
        

        id_transparency = _id_transparency;
        
        switch (id_transparency) {
        case Constants.ID_TRANSPARENCY_RASTER:

            final int transparencyRectanlgeSize = 25;
            final Color color1 = new Color(234, 239, 242);
            final Color color2 = new Color(250, 252, 255);
            
            bi_transparency = Utils.paintRastarBlock(
                    new BufferedImage(
                            Page.getInstance().getJlbl_background().getWidth(), 
                            Page.getInstance().getJlbl_background()
                            .getHeight(), 
                            BufferedImage.TYPE_INT_RGB), 
                    new Color[]{color1, color2},
                    new Rectangle(0, 0, 
                            Page.getInstance().getJlbl_background().getWidth(), 
                            Page.getInstance().getJlbl_background()
                            .getHeight()), 
                            transparencyRectanlgeSize);
            break;
        case Constants.ID_TRANSPARENCY_WHITE:
            
            bi_transparency =  
                    new BufferedImage(
                            Page.getInstance().getJlbl_background().getWidth(), 
                            Page.getInstance().getJlbl_background()
                            .getHeight(), 
                            BufferedImage.TYPE_INT_RGB);
            PaintBI.fillRectangleQuick(bi_transparency, Color.white, 
                    new Rectangle(0, 0, 
                            Page.getInstance().getJlbl_background().getWidth(), 
                            Page.getInstance().getJlbl_background()
                            .getHeight()));
            break;
        default: 
            getLogger().warning("Wrong transparency id.");
            break;
        }
        
    }


    /**
     * @return the bi_transparency
     */
    public static BufferedImage getBi_transparency() {
        
        if (bi_transparency == null) {
            
            setId_transparency(id_transparency);
        }
        return bi_transparency;
    }
}
