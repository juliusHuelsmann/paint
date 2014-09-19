//package declaration
package model.objects.painting;

//import declarations
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import settings.Error;


/**
 * 
 * @author Julius Huelsmann
 * @version %I%, %U%
 */
public class PaintObjectImage extends PaintObject {


    /**
     * serial version because the list of PaintObjects is saved.
     */
    private static final long serialVersionUID = -3730582547146097485L;

    
    /**
     * The BufferedImage.
     */
    private BufferedImage bi_image;
    
    
    /**
     * The location of the BufferdImage in picture.
     */
    private Point pnt_locationOfImage;
    
    /**
     * Constructor.
     * 
     * @param _elementId the id of the element
     * @param _bi the bufferedImage which is displayed.
     */
    public PaintObjectImage(final int _elementId, final BufferedImage _bi) {
        super(_elementId);
        this.bi_image = _bi;
    }

    
    /**
     * {@inheritDoc}
     */
    @Override
    public final boolean isInSelectionImage(final Rectangle _r) {

        //if the image does not exist it is not inside the rectangle; thus 
        //return false
        if (bi_image == null) {
            
            return false;
        }
        
        //check whether the point does exist
        if (pnt_locationOfImage == null) {
            
            //print error message and interrupt immediately
            Error.printError(getClass().getSimpleName(), "isInSelection", 
                    "The BufferedImage is initialized, but its location is "
                    + "not.", new Exception("exception"), 
                    Error.ERROR_MESSAGE_INTERRUPT);
        }
        
        //the image does exist; check the bounds of the image
        return 
                
                //check the x coordinate and width
                (pnt_locationOfImage.x < _r.x + _r.width 
                        && (pnt_locationOfImage.x > _r.x
                        || pnt_locationOfImage.x + bi_image.getWidth() > _r.x))
                && (pnt_locationOfImage.y < _r.y + _r.height
                		&& (pnt_locationOfImage.y > _r.y
                	    || pnt_locationOfImage.x
                	    + bi_image.getHeight() > _r.y));
    }


    
    /**
     * {@inheritDoc}
     */
    @Override public final BufferedImage paint(final BufferedImage _bi, 
            final boolean _final, final BufferedImage _g, final int _x,
            final int _y) {

        //if final only paint at BufferedImage
        //if not final only paint at graphics
        if (_final) {
            
            //print image to BufferedImage.
            int[] rgb = null;
            _bi.setRGB(pnt_locationOfImage.x, pnt_locationOfImage.y, 
                    bi_image.getWidth(), bi_image.getHeight(), 
                    bi_image.getRGB(0, 0, bi_image.getWidth(), 
                            bi_image.getHeight(), rgb, 0, bi_image.getWidth()),
                            0, bi_image.getWidth());
            
        } else {
            
            //print image to graphical user interface
            int x =  pnt_locationOfImage.x;
            int y = pnt_locationOfImage.y;
            int width = bi_image.getWidth();
            int height = bi_image.getHeight();
            _g.getGraphics().drawImage(bi_image, x, y, width, height, null);
            
        }
        
        return bi_image;
    }
    
    
    
    /**
     * {@inheritDoc}
     */
    @Override public final PaintObject[][] separate(final Rectangle _r) {
        
        new Exception(getClass() + "not implementetd yet").printStackTrace();
        return null;
        
    }



    
    /**
     * {@inheritDoc}
     */
    @Override public final BufferedImage getSnapshot() {

        return bi_image;
    }


    
    /**
     * {@inheritDoc}
     */
    @Override public final Rectangle getSnapshotBounds() {

        return new Rectangle(pnt_locationOfImage.x, pnt_locationOfImage.y,
                bi_image.getWidth(), bi_image.getHeight());
    }


    
    /**
     * {@inheritDoc}
     */
    @Override public final Rectangle getSnapshotSquareBounds() {

    	return new Rectangle(pnt_locationOfImage.x, pnt_locationOfImage.y,
    			Math.max(bi_image.getWidth(), bi_image.getHeight()),
    			Math.max(bi_image.getWidth(), bi_image.getHeight()));
    }
}
