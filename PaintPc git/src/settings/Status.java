package settings;

import java.awt.Color;
import java.awt.Dimension;
import java.util.logging.Logger;

import model.objects.pen.Pen;
import model.objects.pen.normal.PenKuli;

/**
 * the current status of the program. E.g. which which operation is performed
 * (pen/selection/pipette...)
 * 
 * @author Julius Huelsmann
 * @version %I%, %U%
 */
public final class Status {

    /**
     * the index indicates which operation is to be performed. (e.g. pen/
     * selection mode 1 - 3, ...).
     */
    private static int indexOperation = 
            Constants.CONTROL_PATINING_INDEX_PAINT_1;

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
    penSelected1 = new PenKuli(Constants.PEN_ID_LINES, 2, Color.black), 
    penSelected2 = new PenKuli(Constants.PEN_ID_LINES, 2, Color.black);
    
    
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
     * @param counter_paintedPoints the counter_paintedPoints to set
     */
    public static void setCounter_paintedPoints(int counter_paintedPoints) {
        Status.counter_paintedPoints = counter_paintedPoints;
    }


    /**
     * @return the logger
     */
    public static Logger getLogger() {
        return LOGGER;
    }
}
