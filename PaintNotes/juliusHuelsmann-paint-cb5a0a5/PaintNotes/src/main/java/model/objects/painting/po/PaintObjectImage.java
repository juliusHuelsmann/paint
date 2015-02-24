//package declaration
package model.objects.painting.po;

//import declarations
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

import model.objects.painting.Picture;
import model.settings.Error;
import model.settings.Status;
import model.util.DPoint;
import model.util.adt.list.List;
import model.util.paint.Utils;


/**
 * 
 * @author Julius Huelsmann
 * @version %I%, %U%
 */
public class PaintObjectImage extends PaintObject implements Cloneable {


    /**
     * Default serial version UID for being able to identify the list's 
     * version if saved to the disk and check whether it is possible to 
     * load it or whether important features have been added so that the
     * saved file is out-dated.
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
    public PaintObjectImage(final int _elementId, final BufferedImage _bi, Picture _picture) {
        super(_picture, _elementId);
        this.bi_image = _bi;
        this.pnt_locationOfImage = new Point(0, 0);
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
                (pnt_locationOfImage.x <= _r.x + _r.width 
                && (pnt_locationOfImage.x >= _r.x
                        || pnt_locationOfImage.x + bi_image.getWidth() >= _r.x))
                && (pnt_locationOfImage.y <= _r.y + _r.height
                		&& (pnt_locationOfImage.y >= _r.y
                	    || pnt_locationOfImage.x
                	    + bi_image.getHeight() >= _r.y));
    }


    
    
    /**
     * Move the BufferedImage to new coordinates.
     * @param _p the new coordinates
     */
    public final void move(final Point _p) {

        //check whether the point does exist
        if (_p == null) {
            
            //print error message and interrupt immediately
            Error.printError(getClass().getSimpleName(), "isInSelection", 
                    "Error moving PaintObject into nirvana (the band) ",
                    new Exception("exception"), 
                    Error.ERROR_MESSAGE_INTERRUPT);
        } else {
            this.pnt_locationOfImage.x += _p.x;
            this.pnt_locationOfImage.y += _p.y;
        }
    }
    /**
     * {@inheritDoc}
     */
    @Override public final BufferedImage paint(final BufferedImage _bi, 
            final boolean _final, final BufferedImage _g, final int _x,
            final int _y, final Rectangle _r) {

        //if final only paint at BufferedImage
        //if not final only paint at graphics
        if (_final) {
            
            //print image to BufferedImage.
            int[] rgb = null;
            _bi.setRGB(pnt_locationOfImage.x + _x, pnt_locationOfImage.y + _y, 
                    bi_image.getWidth(), bi_image.getHeight(), 
                    bi_image.getRGB(0, 0, bi_image.getWidth(), 
                            bi_image.getHeight(), rgb, 0, bi_image.getWidth()),
                            0, bi_image.getWidth());
            
        } else {
            
            //print image to graphical user interface
            final double cZoomFactorWidth = 1.0 
                    * Status.getImageShowSize().width
                    / Status.getImageSize().width;
            final double cZoomFactorHeight = 1.0 
                    * Status.getImageShowSize().height
                    / Status.getImageSize().height;
            int x = (int) (pnt_locationOfImage.x * cZoomFactorWidth + _x);
            int y = (int) (pnt_locationOfImage.y * cZoomFactorHeight + _y);
            int width = bi_image.getWidth();
            int height = bi_image.getHeight();
            _g.getGraphics().drawImage(Utils.resizeImage(
                    (int) (width * cZoomFactorWidth),
                    (int) (height * cZoomFactorHeight), 
                    bi_image), 
                    (int) (x), 
                    (int) (y), 
                    (int) (width * cZoomFactorWidth),
                    (int) (height * cZoomFactorHeight), null);
            
        }
        
        return bi_image;
    }
    
    
    
    /**
     * {@inheritDoc}
     */
    @Override public final PaintObject[][] separate(final Rectangle _r) {
        
        if (isInSelectionImage(_r)) {

            new Exception(getClass() + " not implemenented yet")
            .printStackTrace();
            return null;
        } else {

            new Exception(getClass() + " not implemenented yet")
            .printStackTrace();
            return null;
        }
    }



    
    /**
     * {@inheritDoc}
     */
    @Override
    public final PaintObject[][] separate(
    		final byte[][] _r, final Point _pnt_selectionShift) {

        new Exception(getClass() + " not implemenented yet")
        .printStackTrace();
        return null;
    }


    /**
     * {@inheritDoc}
     */
    @Override public final BufferedImage getSnapshot() {

        return bi_image;
    }

    
    /**
     * set the bufferedImage.
     * @param _bi the BI
     */
    public final void setImage(final BufferedImage _bi) {
        this.bi_image = _bi;
    }

    
    /**
     * {@inheritDoc}
     */
    @Override public final Rectangle getSnapshotBounds() {

        //if the image does not exist it is not inside the rectangle; thus 
        //return false
        if (bi_image == null) {
            
            return new Rectangle(0, 0, 0, 0);
        }
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


    
    /**
     * {@inheritDoc}
     */
    @Override public final synchronized void recalculateSnapshotBounds() {
        new Exception(getClass() + "not implemented yet").printStackTrace();
    }


    /**
     * {@inheritDoc}
     */
    @Override public final void stretch(final DPoint _pnt_from, 
            final DPoint _pnt_totalStretch, final DPoint _pnt_size) {

        //print image to graphical user interface
        final double cZoomFactorWidth = 1.0 
                * Status.getImageSize().width
                / Status.getImageShowSize().width;
        final double cZoomFactorHeight = 1.0 
                * Status.getImageSize().height
                / Status.getImageShowSize().height;
        bi_image = Utils.resizeImage(
                (int) (_pnt_size.getX() * cZoomFactorWidth), 
                (int) (_pnt_size.getY() * cZoomFactorHeight), bi_image);
        pnt_locationOfImage = new Point(
                (int) _pnt_from.getX(), (int) _pnt_from.getY());
        
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public final boolean isInSelectionImage(
            final byte[][] _field, final Point _pnt_shiftRectangle) {

        new Exception("not implemented yet").printStackTrace();
        return false;
    }


    /**
     * {@inheritDoc}
     */
	@Override
	public final List<PaintObjectWriting> deleteRectangle(
			final Rectangle _r, 
			final List<PaintObjectWriting> _ls_pow_outside) {

        new Exception("not implemented yet").printStackTrace();
		return null;
	}


    /**
     * {@inheritDoc}
     */
	@Override
	public final List<PaintObjectWriting> deleteCurve(
			final byte[][] _r,
			final Point _pnt_shiftRectangle, 
			final DPoint _pnt_stretch, 
			final List<PaintObjectWriting> _l) {

        new Exception("not implemented yet").printStackTrace();
        return null;
	}


	/**
	 * {@inheritDoc}
	 */
	@Override
	public final boolean isInSelectionImageStretched(
			final byte[][] _field,
			final Point _pnt_shiftRectangle, 
			final DPoint _pnt_stretch) {

        new Exception("not implemented yet").printStackTrace();
		return false;
	}
}
