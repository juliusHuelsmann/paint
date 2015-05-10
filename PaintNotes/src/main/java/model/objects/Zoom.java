package model.objects;

import control.ContorlPicture;
import model.settings.Status;
import model.settings.ViewSettings;
import view.forms.Page;


/**
 * JLabel which shows where to zoom in.
 * 
 * @author Julius Huelsmann
 * @version %I%, %U%
 */
public final class Zoom {

	
	/**
	 * Controller class which handles the painting.
	 */
	private ContorlPicture controlPicture;
    
    /**
     * Initialize instance.<br><br>
     * 
     * @param _cp 		instance of the controller class which handles the 
     * 					painting.
     */
    public Zoom(final ContorlPicture _cp) { 
    	this.controlPicture = _cp;
    }
    
    /**
     * Location of zoom.
     */
    private int x, y; 
    
    
    /**
     * return the x location.
     * @return the x location
     */
    public int getX() {
       return x; 
    }
    /**
     * 
     * return the y location.
     * @return the y location
     */
    public int getY() {
        return y;
    }
    /**
     * .
     * @param _x the x coordinate
     * @param _y the y coordinate.
     * @param _page
     */
    public synchronized void setLocation(final int _x, final int _y,
    		final Page _page) {

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

        int xNewAligned, yNewAligned;
        
        if (imagePixelSizeX != 0 && imagePixelSizeY != 0) {
            int shiftAlinedX = -_page.getJlbl_painting()
                    .getLocation().x % imagePixelSizeX,
                    shiftAlinedY = -_page.getJlbl_painting()
                    .getLocation().y % imagePixelSizeY;

            xNewAligned = _x + shiftAlinedX;
            yNewAligned = _y + shiftAlinedY;
            
            xNewAligned = xNewAligned - (xNewAligned % imagePixelSizeX);
            yNewAligned = yNewAligned - (yNewAligned % imagePixelSizeY);
        } else {
            xNewAligned = _x;
            yNewAligned = _y;
        }
       
        
        //if the zoom has moved essentially
        if (xNewAligned != x || yNewAligned != y) {
            
            this.x = xNewAligned;
            this.y = yNewAligned;
            
            controlPicture.setZoomBox(
                    x, y, 
                    ViewSettings.ZOOM_SIZE.width,
                    ViewSettings.ZOOM_SIZE.height);
        }
    }
}
