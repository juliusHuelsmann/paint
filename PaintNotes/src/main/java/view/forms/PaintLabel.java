package view.forms;

//import declarations
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import javax.swing.ImageIcon;
import start.test.BufferedViewer;
import view.tabs.PaintObjects;
import view.util.BorderThread;
import view.util.mega.MLabel;
import view.util.mega.MPanel;
import model.objects.painting.Picture;
import model.objects.painting.po.PaintObject;
import model.objects.pen.special.PenSelection;
import model.settings.Status;
import model.util.paint.Utils;


/**
 * 
 * @author Julius Huelsmann
 * @version %I%, %U%
 */
@SuppressWarnings("serial")
public class PaintLabel extends MLabel {

    /**
     * The bufferedImage containing the currently displayed painting stuff.
     */
    private BufferedImage bi;
    
    /**
     * The location which can be changed and given.
     */
    private int x = 0, y = 0;
    
    /**
     * The thread which moves the border.
     */
    private BorderThread thrd_moveBorder;
    
    
    /**
     * Point is subtracted from new location of item JLabel.
     */
    @SuppressWarnings("unused")
    private Point pnt_start;

    /**
     * JPanel which is to be updated if location changes in
     * PaintLabel.
     */
    private MPanel jpnl_toMove;
    
    /**
     * Constructor.
     * 
     * @param _jpnl_toMove JPanel which is to be updated if location changes in
     * PaintLabel.
     */
    public PaintLabel(final MPanel _jpnl_toMove) {
        this.jpnl_toMove = _jpnl_toMove;
    }

    
    /**
     * Refresh the entire image.
     */
    public final void refreshPaint() {


        Status.getLogger().finest("refreshing PaintLabel. \nValues: "
                + "\n\tgetSize:\t" + getSize() + " vs. " + super.getSize()
                + "\n\tgetLocation:\t" + getLocation() 
                + " vs. " + super.getLocation()
                + "\n\t" + "_x:\t\t"
                + "\n\t" + "_y\t\t"
                + "\n\t" + "_width\t\t" + getWidth()
                + "\n\t" + "_height\t\t" + getHeight() + "\n");

        //paint the painted stuff at graphics
        setBi(Picture.getInstance().updateRectangle(
                -getLocation().x, 
                -getLocation().y, getWidth() , getHeight(), 0, 0, getBi()));

        
        refreshPaintBackground();
        
        setIcon(new ImageIcon(getBi()));
        PaintObjects.getInstance().repaint();
    }

    
    
    /**
     * Refresh the background of painting.
     *  //TODO: quicker! like in repainting of painted content.
     */
    private void refreshPaintBackground() {

        //paint the painting background (e.g. raster / lines) at the graphical
        //user interface.
        if (Page.getInstance().getJlbl_background2().getWidth() != 0
                && Page.getInstance().getJlbl_background2().getHeight() != 0) {

            
            BufferedImage ret = new BufferedImage(
                    Page.getInstance().getJlbl_background2().getWidth(),
                    Page.getInstance().getJlbl_background2().getHeight(),
                    BufferedImage.TYPE_INT_ARGB);
            ret = Picture.getInstance().emptyRectangle(
                    -getLocation().x, 
                    -getLocation().y, getWidth(), getHeight(), 0, 0, ret);
            Page.getInstance().getJlbl_background2().setIcon(
                    new ImageIcon((Utils.getBackground(ret, -getLocation().x, 
                    -getLocation().y, -getLocation().x + getWidth(), 
                    -getLocation().y + getHeight(), 0, 0))));  
        }

    }
    
    @Override public final void repaint() {
        super.repaint();
        PaintObjects.getInstance().repaint(); 
        New.getInstance().setVisible(false);
    }
    
    
    /**
     * das gleiche wie unten nur mut zoom rec.t.
     * @param _x the x coordinate in view
     * @param _y the y coordinate in view
     * @param _width the width
     * @param _height the height
     */
    public final void paintZoom(final int _x, final int _y, 
            final int _width, final int _height) {
        
        Page.getInstance().getJlbl_border().setBounds(_x, _y, _width, _height);
    }
    
    
    /**
     * Paint the entire selection stuff.
     * @param _r the rectangle which is selected.
     */
    public final void paintEntireSelectionRect(final Rectangle _r) {

        //close old border thread.
        if (thrd_moveBorder != null) {
            thrd_moveBorder.interrupt();

            repaint();
        }

        //initialize the thread and start it.
        thrd_moveBorder = new BorderThread(_r, true, null, null);
        thrd_moveBorder.start();
//        
        //paint the background
//        BufferedImage bi_fresh = Page.getInstance().getEmptyBI();
//        Utils.paintRastarBlock(bi_fresh, 
//                ViewSettings.SELECTION_BACKGROUND_CLR, _r);

        //        
        //show resize buttons
        
        for (int a = 0; a < Page.getInstance().getJbtn_resize().length; a++) {
            for (int b = 0; b < Page.getInstance().getJbtn_resize().length;
                    b++) {
                //size of JButton
                final int b_size = Page.getInstance().getJbtn_resize()[a][b]
                        .getWidth();
                
                Page.getInstance().getJbtn_resize()[a][b]
                        .setLocation(_r.x + _r.width * a / 2  - b_size / 2, 
                                _r.y + _r.height * b / 2 - b_size / 2);
            }
        }
        Page.getInstance().getJlbl_border().setBounds(_r);
    }
    
    
    
    /**
     * repaint a special rectangle.
     * @param _x the x coordinate in view
     * @param _y the y coordinate in view
     * @param _width the width
     * @param _height the height
     * @return the graphics
     */
    public final BufferedImage refreshRectangle(final int _x, final int _y, 
            final int _width, final int _height) {

        Status.getLogger().finest("refreshing PaintLabel. \nValues: "
                + "\n\tgetSize:\t" + getSize() + " vs. " + super.getSize()
                + "\n\tgetLocation:\t" + getLocation() 
                + " vs. " + super.getLocation()
                + "\n\t" + "_x:\t\t" + _x
                + "\n\t" + "_y\t\t" + _y
                + "\n\t" + "_width\t\t" + _width
                + "\n\t" + "_height\t\t" + _height + "\n");

        //paint the painted stuff at graphics
        setBi(Picture.getInstance().updateRectangle(
                -getLocation().x + _x, 
                -getLocation().y + _y, _width, _height, _x, _y, getBi()));

        setIcon(new ImageIcon(getBi()));
        
        PaintObjects.getInstance().repaint();
        return getBi();
        
        
    }
    

    
    /**
     * Not necessary.
     */
    public void refreshPopup() {
        //not necessary anymore because paitned to bi and autorefreshes.
//        refreshPaint();
    }
    
    
    /**
     * Paint a zoom box.
     * 
     * @param _x the x coordinate
     * @param _y the y coordinate
     * @param _width the width 
     * @param _height the height
     */
    public final void setZoomBox(final int _x, final int _y, 
            final int _width, final int _height) {
        paintZoom(_x, _y, _width, _height);
    }
    

    
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
     * This method saves the location in x and y coordinate for being able
     * to display the correct painting sub image.
     * 
     * for x and y location are saved. Thus, the painting methods
     * are able to calculate for themselves what to paint at what position.
     * @param _x the new x coordinate which is saved
     * @param _y the new y coordinate which is saved
     */
    @Override public final synchronized void setLocation(
    		final int _x, final int _y) {
        
    	//TODO: error-checking (formally used for zooming in, did not work
    	//because method is not designed for that.)
        //update the JPanel location because the ScrollPane fetches information
        //out of that panel
        jpnl_toMove.setBounds(_x, _y, jpnl_toMove.getWidth(), 
                jpnl_toMove.getHeight());
        
        //if something changed, repaint
        if (_x != x || _y != y) {

            if (isVisible()) {
            	
            	
            	int maintainStartX = 0, 
	            	maintainStartY = 0, 
	            	maintainWidth = bi.getWidth(), 
	            	maintainHeight = bi.getHeight();
            	

            	int shiftedStartX = 0,
            		shiftedStartY = 0;
            	
            	//move to the left (new location is smaller than the old one)
            	if (_x > x) {

            		shiftedStartX = _x - x;
            		maintainStartX = 0;
            		maintainWidth = bi.getWidth() - shiftedStartX;
            	} else if (_x < x) {

            		shiftedStartX = 0;
            		maintainStartX =  x - _x;
            		maintainWidth = bi.getWidth() - maintainStartX;
            	}

            	//moved up (old location is greater than new location)
            	if (_y > y) {
            		
            		shiftedStartY = _y - y;
            		maintainStartY = 0;
            		maintainHeight = bi.getHeight() - shiftedStartY;
            	} else if (_y < y) {

            		shiftedStartY = 0;
            		maintainStartY =  y - _y;
            		maintainHeight = bi.getHeight() - maintainStartY;
            	}
            	
            	/*
            	 * shift the maintained stuff
            	 */
            	
            	if (maintainWidth > 0 && maintainHeight > 0) {
            		//fetch the the RGB array of the subImage which is to be 
                	//maintained but moved somewhere.
                	//TODO: zoom error occurs.
                	int[] rgbArray = new int[maintainWidth * maintainHeight];
                	rgbArray = bi.getRGB(
                			maintainStartX, 
                			maintainStartY, 
                			maintainWidth, 
                			maintainHeight, 
                			rgbArray, 0, maintainWidth);
                	
                	
                	//write the maintained RGB array to shifted coordinates.
                	bi.setRGB(shiftedStartX, 
                			shiftedStartY, 
                			maintainWidth,
                			maintainHeight, 
                			rgbArray,  0, maintainWidth);
                	
                	/*
                	 * paint the new stuff. 
                	 * The rectangle location is the complement of the 
                	 * maintained in size of bufferedImage.
                	 */
                	
                	//Refresh both in direction of width and of height.
                    //In the simplest way of doing this, there may be an area 
                	//which is painted twice (depicted with '!'). 
                    //For further optimization of displaying speed this should 
                    //be eliminated by the way of refreshing done beneath the 
                	//picture. Picture:
                	//shiftedStartY == 0
                	//		____________		____________
                    //		| ! W W W W	|       | H	x      	|
                    // 		| H	x x x x |       | H	x       |
                    // 		| H	x      	|       | H	x     	|
                    // 		| H	x      	|       | H	x     	|
                    // 		| H	x       |       | H	x x x x	|
                	//		|_H_x_______|		|_!_W_W_W_W_|
                	//		____________		____________
                    //	    | W	W W W! !|		|     x	H H	|
                    // 	    | x x x x H	|		|     x	H H	|
                    // 	    |  	   	x H	|       |     x	H H	|
                    // 	    |      	x H	|       |     x	H H	|
                    // 	    |       x H	|		| x x x	H H	|
                	//		|_______x_H_|		|_W_W_W_!_!_|
                	
                	
                	/*
                	 * Width
                	 */
                	//here the (!) are painted!
                	int refreshWidthWidth = bi.getWidth();
                	int refreshWidthHeight = bi.getHeight() - maintainHeight;
                	int refreshWidthY = 0;
                	int refreshWidthX = 0;
                	
                	if (shiftedStartY == 0) {
                		refreshWidthY = maintainHeight;
                	}

                	
                	/*
                	 * height
                	 */
                	int refreshHeightWidth = bi.getWidth() - maintainWidth;
                	int refreshHeightHeight = bi.getHeight()
                			- refreshWidthHeight;
                	int refreshHeightY = 0;
                	int refreshHeightX = 0;
                	
                	if (shiftedStartX == 0) {
                		refreshHeightX = maintainWidth;
                	}

                    //save values
                    this.x = _x;
                    this.y = _y;

//                    for (int xw = 0; xw < refreshWidthWidth; xw ++) {
//                    	for (int yw = 0; yw < refreshWidthHeight; yw ++) {
//                        	getBi().setRGB(xw + refreshWidthX, 
//                        			yw + refreshWidthY, 
//                        			Color.red.getRGB());
//                        }
//                    }
//                    for (int xw = 0; xw < refreshHeightWidth; xw ++) {
//                    	for (int yw = 0; yw < refreshHeightHeight; yw ++) {
//                        	getBi().setRGB(xw + refreshHeightX, 
//                        			yw + refreshHeightY, 
//                        			Color.green.getRGB());
//                        }
//                    }
                    
                    
                    //BufferedImage
                    refreshPaintBackground();
                	refreshRectangle(refreshWidthX, refreshWidthY, 
                			refreshWidthWidth, refreshWidthHeight);
                	refreshRectangle(refreshHeightX, refreshHeightY, 
                			refreshHeightWidth, refreshHeightHeight);

//                	System.out.println("maintain:\tnew"
//                			+ "\nx:\t" 	+ maintainStartX
//                			+ "\t" 		+ shiftedStartX
//                			+ "\ny: \t"	+ maintainStartY
//                			+ "\t" 		+ shiftedStartY
//                			+ "\n\nw:\t" + maintainWidth 
//                			+ "\nh:\t" + maintainHeight
//                			);
            	}
            } else {

                
                //save values
                this.x = _x;
                this.y = _y;
            }
        }
    }
    

    /**
     * Really set the location.
     * 
     * E.g. called by controlling class in case of zooming in.
     * 
     * @param _x the new x coordinate which is saved
     * @param _y the new y coordinate which is saved
     */
    public final void setLoc(final int _x, final int _y) {
        
        //if something changed, repaint
        super.setLocation(_x, _y);
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

        //update the JPanel location because the ScrollPane fetches information
        //out of that panel
        jpnl_toMove.setBounds(x, y, jpnl_toMove.getWidth(), 
                jpnl_toMove.getHeight());

        if (isVisible()) {
            
            //set changed
            refreshPaint();
        }
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

        //update the JPanel location because the ScrollPane fetches information
        //out of that panel
        jpnl_toMove.setBounds(_x, _y, jpnl_toMove.getWidth(), 
                jpnl_toMove.getHeight());
        
        //set width and height.
        super.setBounds(0, 0, _widht, _height);
        bi = new BufferedImage(_widht, _height, BufferedImage.TYPE_INT_ARGB);

        if (isVisible()) {
            
            //set changed
            refreshPaint();
        }
    }

    /**
     * Paint line selection.
     * @param _po the PaintObject.
     * @param _pen the pen.
     */
    public final void paintSelection(final PaintObject _po, 
            final PenSelection _pen) {

    
        //interrupt border thread.
        stopBorderThread();
        
        //initialize the thread and start it.
        thrd_moveBorder = new BorderThread(null, false, _po, _pen);
        thrd_moveBorder.start();
        
        //paint the background
    }
    
    
    /**
     * Stop the border - thread.
     */
    public final void stopBorderThread() {

        //close old border thread.
        if (thrd_moveBorder != null) {
            thrd_moveBorder.interrupt();
        }
    }
    
    
    /*
     * methods for getting location
     */

    
    /**
     * @return the real coordinate!
     */
    @Deprecated
    @Override public final int getX() {
        return super.getX();
    }

    /**
     * @return the real coordinate!
     */
    @Deprecated
    @Override public final int getY() {
        return super.getY();
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
