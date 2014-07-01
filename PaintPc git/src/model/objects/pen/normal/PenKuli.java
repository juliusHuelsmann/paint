//package declaration
package model.objects.pen.normal;

//import declarations
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.image.BufferedImage;

import settings.Error;
import settings.Status;
import model.objects.pen.Pen;


/**
 * Loesungsvorschlag mathematische Schrift: abschnittweise approximieren.
 * @author Julius Huelsmann
 */
public class PenKuli extends Pen {

	/**
	 * serial version of pen.
	 */
	private static final long serialVersionUID = 0L;
	

	/**
	 * Constructor: calls super -constructor.
	 * @param _index the index of the pen painting method (like line, 
	 *         point, mathematics)
	 * @param _thickness the thickness of painting
	 * @param _background the background.
	 */
	public PenKuli(final int _index, final int _thickness, 
	        final Color _background) {
		
		//call super constructor
		super(_index, _thickness, _background);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override protected final void paintPoint(final Point _p, 
	        final BufferedImage _bi, final boolean _final, 
	        final Point _pnt_shift, final Graphics _g) {

	    for (int i = -getThickness() / 2;
                i < (getThickness() / 2) + (getThickness() % 2); i++) {
            
            for (int j = -getThickness() / 2;
                    j < (getThickness() / 2) + (getThickness() % 2); j++) {


                //x and y location because is used twice for line 
                //drawing.
                int x, y;

                x = _p.x + i;
                y = _p.y + j;
                
                
				//if is in range
				if (_final && _p.x + i >= 0 && _p.y + j >= 0 && _p.x 
				        + i < _bi.getWidth() && _p.y + j < _bi.getHeight()) {

                    
				    //set the given pixel in buffered image
				    if (_final) {
				        try {
				        _bi.setRGB(x, y, 
				                getClr_foreground().getRGB());
				        } catch (ArrayIndexOutOfBoundsException e) {
				            Error.printError(getClass().getSimpleName(), 
				                    "paintPoint", "point is out of range", 
				                    e, Error.ERROR_MESSAGE_INTERRUPT);
				        }
				    }
				}

                if (_g != null) {

                    //set as color the foreground color of this pen.
                    _g.setColor(getClr_foreground());
                    
                    //adjust the location at the zoom.
                    x = (x * Status.getImageShowSize().width)
                            / Status.getImageSize().width;
                    y = (y * Status.getImageShowSize().height)
                            / Status.getImageSize().height;

                    //add the shift coordinates for painting.
                    x +=  _pnt_shift.x;
                    y +=  _pnt_shift.y;
                    
                    //the image pixel size in pixel for painting.
                    //for example if zoomed in once, an image pixel has
                    //got the size of two painted pixel in each dimension.
                    //thus it is necessary to paint 4 pixel. These pixel
                    //are painted under the previous pixel. Example:
                    //      [x] [a] [ ]         (x is the pixel which is 
                    //      [a] [a] [ ]         already printed, a are those
                    //      [ ] [ ] [ ]         which are added to avoid gaps.
                    int imagePixelSizeX = Status.getImageShowSize().width 
                            / Status.getImageSize().width,
                            imagePixelSizeY = Status.getImageShowSize().height 
                            / Status.getImageSize().height;
                    
                    //for loop because i want to paint the gaps between the 
                    //pixel if zoomed in.
                    
                    //print the pixel because if the pixel size is 0 the pixel 
                    //has to be printed.
                    _g.drawLine(x, y, x, y);
                    
                    for (int kx = 0; kx < imagePixelSizeX; kx++) {
                        for (int ky = 0; ky < imagePixelSizeY; ky++) {

                            //draw line.
                            _g.drawLine(x + kx, y + ky, x + kx, y + ky);
                        }
                    }
                }
			}
		}
	}
}