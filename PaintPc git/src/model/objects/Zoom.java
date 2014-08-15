package model.objects;

import settings.Status;
import settings.ViewSettings;
import view.forms.Page;


/**
 * JLabel which shows where to zoom in.
 * 
 * @author Julius Huelsmann
 * @version %I%, %U%
 */
public final class Zoom {

    
    /**
     * The only instance of this class.
     */
    private static Zoom instance;
    
    /**
     * Initialize instance.
     */
    private Zoom() { }
    
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
     */
    public synchronized void setLocation(final int _x, final int _y) {

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
            int shiftAlinedX = -Page.getInstance().getJpnl_toMove().getX() 
                    % imagePixelSizeX,
                    shiftAlinedY = -Page.getInstance().getJpnl_toMove().getY() 
                    % imagePixelSizeY;

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

            Page.getInstance().getJlbl_painting().removeZoomBox();
            
            this.x = xNewAligned;
            this.y = yNewAligned;
            
            Page.getInstance().getJlbl_painting().setZoomBox(
                    x, y, 
                    ViewSettings.ZOOM_SIZE.width,
                    ViewSettings.ZOOM_SIZE.height);
        }
    }
    

    /**
     * this method guarantees that only one instance of this
     * class can be created ad runtime.
     * 
     * @return the only instance of this class.
     */
    public static Zoom getInstance() {
        
        if (instance == null) {
            instance = new Zoom();
        }
        return instance;
    }
}
