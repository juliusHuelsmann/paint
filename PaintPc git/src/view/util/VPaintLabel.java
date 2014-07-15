package view.util;

//import declarations
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

import javax.swing.JLabel;


/**
 * 
 * @author Julius Huelsmann
 * @version %I%, %U%
 */
@SuppressWarnings("serial")
public abstract class VPaintLabel extends JLabel {

    
    /**
     * The location which can be changed and given.
     */
    private int x = 0, y = 0;

    
    /**
     * The bufferedImage.
     */
    private BufferedImage bi;
    
    /**
     * Constructor.
     */
    public VPaintLabel() {

        super();
//        super.setIgnoreRepaint(true);
//        super.dispatchEvent(e)
//        super.doLayout();
//        super.grabFocus();
//        super.(r)
        
    }
//    
//    /**
//     * {@inheritDoc}
//     */
//    @Override public abstract void paint(final Graphics _g);
//    @Override public final void repaint() { }
//    @Override public final void paintComponent(final Graphics _g) { }
//    @Override public final void paintChildren(final Graphics _g) { }
//    @Override public final void paintComponents(final Graphics _g) { }
//    @Override public final void paintImmediately(final Rectangle _g) { }
//    @Override public final void paintBorder(final Graphics _g) { }
//    @Override public final void update(final Graphics _g) { }

    
    
    /**
     * update the graphics after location change.
     */
    public abstract void refreshPaint();
    
    
    
    /*
     * Location is not set directly because the paint methods shell be able 
     * to decide for themselves what to paint at which position.
     * 
     * Thus, the methods for changing the location and for getting it are
     * overwritten and alter / return the locally saved location.
     * 
     * methods for changing location:
     */
    
    

    /**
     * in here, the location is not set as usual, but just the values
     * for x and y location are saved. Thus, the painting methods
     * are able to calculate for themselves what to paint at what position.
     * @param _x the new x coordinate which is saved
     * @param _y the new y coordinate which is saved
     */
    @Override public final void setLocation(final int _x, final int _y) {
        
        //if something changed, repaint
        if (_x != x || _y != y) {

            //save values
            this.x = _x;
            this.y = _y;
            
            if (isVisible()) {
                
                //set changed
                refreshPaint();
            }
        }
    }
    

    /**
     * in here, the location is not set as usual, but just the values
     * for x and y location are saved. Thus, the painting methods
     * are able to calculate for themselves what to paint at what position.
     * @param _p the new coordinates which are saved
     */
    @Override public final void setLocation(final Point _p) {
        
        //save the new location
        this.x = _p.x;
        this.y = _p.y;

        
        //repaint
        refreshPaint();
    }
    
    
    /**
     * set the size of the JLabel and save the new location. Location is not 
     * set because the paint methods shell be able to decide for themselves
     * what to paint at which position.
     * 
     * @param _x the x coordinate which is saved
     * @param _y the y coordinate which is saved
     * @param _widht the width which is set
     * @param _height the height which is set
     */
    @Override public final void setBounds(final int _x, final int _y, 
            final int _widht, final int _height) {
        
        //save the new location 
        this.x = _x;
        this.y = _y;
        
        //set width and height.
        super.setBounds(0, 0, _widht, _height);
        bi = new BufferedImage(_widht, _height, BufferedImage.TYPE_INT_ARGB);
        
        //repaint
        refreshPaint();
    }
    
    
    
    /*
     * methods for getting location
     */

    
    /**
     * returns the saved but not applied x coordinate.
     * @return the saved but not applied x coordinate.
     */
    @Override public final int getX() {
        return this.x;
    }
    
    /**
     * returns the saved but not applied y coordinate.
     * @return the saved but not applied y coordinate.
     */
    @Override public final int getY() {
        return this.y;
    }
    
    /**
     * returns the saved but not applied x and y coordinates.
     * @return the saved but not applied x and y coordinates (point).
     */
    @Override public final Point getLocation() {
        return new Point(x, y);
    }
    
    /**
     * returns the saved but not applied x and y coordinates together with the
     * applied size in a rectangle. 
     * @return the saved but not applied x and y coordinates together with the
     * applied size in a rectangle. 
     */
    @Override public final Rectangle getBounds() {
        return new Rectangle(x, y, getWidth(), getHeight());
    }

    /**
     * @return the bi
     */
    public final BufferedImage getBi() {
        return bi;
    }

    /**
     * @param _bi the _bi to set
     */
    public final void setBi(final BufferedImage _bi) {
        this.bi = _bi;
    }
    
}
