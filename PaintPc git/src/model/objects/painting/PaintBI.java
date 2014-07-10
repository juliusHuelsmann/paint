package model.objects.painting;

import java.awt.Color;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;


/**
 * Utility class for painting stuff to a buffered image.
 * @author Julius Huelsmann
 *
 */
public final class PaintBI {

    /**
     * Empty utility class constructor.
     */
    private PaintBI() {
        
    }
    
    
    /**
     * Paints the borders of a rectangle in a special 
     * color to a bufferedImage.
     * 
     * @param _bi the buffered image on which is painted
     * @param _clr the color in which is painted.
     * @param _r the rectangle location and size which is 
     *           painted.
     */
    public static void paintRectangle(
            final BufferedImage _bi, 
            final Color _clr,
            final Rectangle _r) {
        
    }
    
    
    /**
     * Paints the borders of a rectangle in a special 
     * color to a bufferedImage.
     * 
     * @param _bi the buffered image on which is painted
     * @param _clr the color in which is painted.
     * @param _r the rectangle location and size which is 
     *           painted.
     */
    public static void fillRectangle(
            final BufferedImage _bi, 
            final Color _clr,
            final Rectangle _r) {
        
    }
    

    
    
    /**
     * Paints a line in a special color to a bufferedImage.
     * 
     * @param _bi the buffered image on which is painted
     * @param _clr the color in which is painted.
     * @param _pStart the location of start pixel
     *           painted.
     * @param _pEnd the location of end pixel painted.
     */
    public static void paintLine(
            final BufferedImage _bi, 
            final Color _clr,
            final Point _pStart,
            final Point _pEnd) {
        
    }
    

    /**
     * Paints a point in a special color to a bufferedImage.
     * 
     * @param _bi the buffered image on which is painted
     * @param _clr the color in which is painted.
     * @param _p the location of painted pixel.
     */
    public static void paintPoint(
            final BufferedImage _bi, 
            final Color _clr,
            final Point _p) {
        
    }
}
