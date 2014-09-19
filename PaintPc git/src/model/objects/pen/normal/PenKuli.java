//package declaration
package model.objects.pen.normal;

//import declarations
import java.awt.Color;
import java.awt.Point;
import java.awt.image.BufferedImage;
import settings.Error;
import settings.Status;
import view.forms.Page;
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
	        final Point _pnt_shift, final BufferedImage _g) {

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
                    

                    //error prevention (divide by zero if zoomed out a little 
                    //bit too much)
                    if (imagePixelSizeX == 0) {
                        imagePixelSizeX = 1;
                    }
                    if (imagePixelSizeY == 0) {
                        imagePixelSizeY = 1;
                    }
                    
                    //if the data is displayed paint lines to graphics. 
                    //otherwise nothing to do.
                    if (x / imagePixelSizeX >= 0 && y / imagePixelSizeY >= 0
                            
                            //if the x coordinates are in range (displayed
                            //at the right edge of the screen)
                            && (int) x / imagePixelSizeX + 1 
                            <= (int) Page.getInstance().getJlbl_painting()
                            .getWidth() / imagePixelSizeX

                            //if the x coordinates are in range (displayed
                            //at the bottom edge of the screen)
                            && (int) y / imagePixelSizeY + 1 
                            <= (int) Page.getInstance().getJlbl_painting()
                            .getHeight() / imagePixelSizeY) {
                        
                        Status.setCounter_paintedPoints(Status
                                .getCounter_paintedPoints() + 1);
                        
                        //print the pixel because even if the pixel size in 
                        //pixel is (rounded) equal to 0 the pixel has to be 
                        //printed.
                        Point[] point = new Point[1];
                        point[0] = new Point(x, y);
                        
//                        PaintBI.paintPolygonN(_g, getClr_foreground(), 
//                                getThickness(), point, true);
                        _g.setRGB(x, y, getClr_foreground().getRGB());
                        
                        //for loop because i want to paint the gaps between the 
                        //pixel if zoomed in.
                        for (int kx = 0; kx < imagePixelSizeX; kx++) {
                            for (int ky = 0; ky < imagePixelSizeY; ky++) {

                                //draw line.
                                point = new Point[1];
                                point[0] = new Point(x + kx, y + ky);
                                
//                                PaintBI.paintPolygonN(_g, getClr_foreground(),
//                                        getThickness(), point, true);
                                _g.setRGB(x + kx, y + ky, 
                                        getClr_foreground().getRGB());
                            }
                        }
                    }
                }
			}
		}
	}
}