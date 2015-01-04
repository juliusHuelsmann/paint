//package declaration
package model.objects.pen.normal;

//import declarations
import java.awt.Color;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

import view.forms.Page;
import model.objects.pen.Pen;
import model.settings.Constants;
import model.settings.Error;
import model.settings.Status;
import model.settings.TextFactory;
import model.util.DPoint;


/**
 * Loesungsvorschlag mathematische Schrift: abschnittweise approximieren.
 * @author Julius Huelsmann
 */
public class Marker extends Pen {

	/**
	 * serial version of pen.
	 */
	private static final long serialVersionUID = 0L;

	
	/**
	 * The opacity of text marker.
	 */
    private final int opacity = 100;

	/**
	 * Constructor: calls super -constructor.
	 * @param _index the index of the pen painting method (like line, 
	 *         point, mathematics)
	 * @param _thickness the thickness of painting
	 * @param _background the background.
	 */
	public Marker(final int _index, final int _thickness, 
	        final Color _background) {
		
		//call super constructor
		super(_index, _thickness, _background, Constants.PATH_PEN_MARKER_LINE);

        setClr_foreground(new Color(
                _background.getRed(), 
                _background.getGreen(), 
                _background.getBlue(), 
                opacity));
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override protected final void paintPoint(final DPoint _p, 
	        final BufferedImage _bi, final boolean _final, 
	        final DPoint _pnt_shift, final BufferedImage _g,
	        final Rectangle _r_visibleScope) {

	    final int plus = 3;
	    
	    for (int i = -getThickness() / 2;
                i < (getThickness() / 2) + (getThickness() % 2); i++) {
            
            for (int j = -getThickness() / 2 - plus;
                    j < (getThickness() / 2) + (getThickness() % 2) + plus; 
                    j++) {


                //x and y location because is used twice for line 
                //drawing.
                int x, y;

                x = (int) (_p.getX() + i);
                y = (int) (_p.getY() + j);
                
                
				//if is in range
				if (_final && _p.getX() + i >= 0 && _p.getY() + j >= 0 
				        && _p.getX() + i < _bi.getWidth() 
				        && _p.getY() + j < _bi.getHeight()) {

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
                    x = ((x) * Status.getImageShowSize().width)
                            / Status.getImageSize().width;
                    y = ((y) * Status.getImageShowSize().height)
                            / Status.getImageSize().height;

                    //add the shift coordinates for painting.
                    x +=  _pnt_shift.getX();
                    y +=  _pnt_shift.getY();
                    
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
                        
                        //for loop because i want to paint the gaps between the 
                        //pixel if zoomed in.
                        for (int kx = 0; 
                                kx < Math.max(imagePixelSizeX, 1); kx++) {
                            for (int ky = 0; 
                                    ky < Math.max(imagePixelSizeY, 1); ky++) {

                                try {
                                    Color c = new Color(
                                            _g.getRGB(x + kx, y + ky), true);

                                    
                                    _g.setRGB(x + kx, y + ky, 
                                            mergeColors(c, 
                                                    getClr_foreground()));
                                    
                                } catch (ArrayIndexOutOfBoundsException e) { 
                                    Status.getLogger().warning("FEHLER" 
                                            + x + ";" + y + "width"
                                            + _g.getWidth() 
                                            + "height" + _g.getHeight());
                                }
                            }
                        }
                    }
                }
			}
		}
	}
	
	
	
	/**
	 * Merge the two colors in the way that is best for text marker.
	 * @param _c1 first color
	 * @param _c2 second color
	 * @return the RGB value of the mixture.
	 */
	private int mergeColors(final Color _c1, final Color _c2) {
	    
	    
	    final int maxRGB = 255, divisor = 5;
        int alpha1 = _c1.getAlpha();
        int alpha2 = _c2.getAlpha();
        int alphaTotal = alpha1 + alpha2;
        
        int newAlpha = Math.min(Math.max(alpha1, alpha2) 
                + Math.abs(alpha1 - alpha2) / divisor, maxRGB);
        
        if (alphaTotal == 0) {
            return new Color(maxRGB, maxRGB, maxRGB, 0).getRGB();
        }
        

        int red = (_c1.getRed() * alpha1 + _c2.getRed() * alpha2)
                / alphaTotal;
        int green = (_c1.getGreen() * alpha1 + _c2.getGreen() * alpha2) 
                / alphaTotal;
        int blue = (_c1.getBlue() * alpha1 + _c2.getBlue() * alpha2) 
                / alphaTotal;
	    
	    return new Color(red, green, blue, newAlpha).getRGB();
	}
	

	/**
	 * {@inheritDoc}
	 */
	@Override
	public final String getName() {
		return TextFactory.getInstance().getName_marker();
	}
}

