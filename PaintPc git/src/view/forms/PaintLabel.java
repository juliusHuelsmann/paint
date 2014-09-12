package view.forms;

//import declarations
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import _test.BufferedViewer;
import settings.Status;
import settings.ViewSettings;
import start.utils.Utils;
import model.objects.painting.Picture;


/**
 * 
 * @author Julius Huelsmann
 * @version %I%, %U%
 */
@SuppressWarnings("serial")
public class PaintLabel extends JLabel {

    
    /**
     * The bufferedImage.
     */
    private BufferedImage bi;
    
    /**
     * The location which can be changed and given.
     */
    private int x = 0, y = 0;
    
    /**
     * The thread which moves the border.
     */
    private Thread thrd_moveBorder;
    
    /**
     * Index for border movement in thread thrd_moveBorder. Determines the 
     */
    private int index = -1;
    
    /**
     * Point is subtracted from new location of item JLabel.
     */
    @SuppressWarnings("unused")
    private Point pnt_start;

    /**
     * Constructor.
     */
    public PaintLabel() { }
    
    /**
     * {@inheritDoc}
     */
    @Override public final void paint(final Graphics _g) {
        super.paint(_g);
//        System.out.println("1");
//        double time00 = System.currentTimeMillis();
//        //if is set for the first time, refreshPaint
//        if (Picture.getInstance().alterGraphics(getBi()) && isVisible()) {
//            double time01 = System.currentTimeMillis();
//            System.out.println("halbzeit" + (time01 -time00));
//            BufferedViewer.show(getBi());
//            refreshPaint();
//            double time02 = System.currentTimeMillis();
//            System.out.println("ganzzeit" + (time02 -time00));
//        }
    }

    
    /**
     * Refresh the entire image.
     */
    public final void refreshPaint() {

        this.refreshRectangle(0, 0, getWidth(), getHeight());
    }
    
    /**
     * The previous rectangle.
     */
    private Rectangle r_old;
    
    /**
     * das gleiche wie unten nur mut zoom rec.t.
     * @param _x the x coordinate in view
     * @param _y the y coordinate in view
     * @param _width the width
     * @param _height the height
     */
    public final void paintZoom(final int _x, final int _y, 
            final int _width, final int _height) {
        BufferedViewer.show(getBi());
        System.out.println(getBi().getWidth());
        System.out.println(getBi().getHeight());
        removeOldRectangle();
        Graphics g = getGraphics();
        g.setColor(Color.red);
        g.drawRect(_x + 1, _y + 1, _width - 1, _height - 1);
        this.r_old = new Rectangle(_x + 1, _y + 1, _width - 1, _height - 1);
    }
    
    
    /**
     * Paint the entire selection stuff.
     * @param _r the rectangle which is selected.
     */
    public final void paintEntireSelection(final Rectangle _r) {

        System.out.println("3");
        //paint the background
        Utils.paintRastarBlock(getGraphics(), 
                ViewSettings.SELECTION_BACKGROUND_CLR, _r);
        
        //paint the border.
        paintBorder(_r);
    }
    
    /**
     * das gleiche wie unten nur mut zoom rec.t.
     * @param _x the x coordinate in view
     * @param _y the y coordinate in view
     * @param _width the width
     * @param _height the height
     */
    public final void paintSelection(final int _x, final int _y, 
            final int _width, final int _height) {
        System.out.println("4");
        getGraphics().drawRect(_x + 1, _y + 1, _width - 1, _height - 1);
        this.r_old = new Rectangle(_x + 1, _y + 1, _width - 1, _height - 1);
    }
    
    
    /**
     * Remove the old changed pixel from zoom.
     */
    public final void removeOldRectangle() {

        System.out.println("5");
        super.setIcon(new ImageIcon(getBi()));
        super.repaint();
        if (true || r_old == null) {
            return;
        }
        //set color and fetch Graphics
        Graphics g = getGraphics();
        Color c = Color.white;
        g.setColor(c);

        
        //TODO: current problem: if there is something painted to the rectangle,
        //it it painted 4 times by now and not removed
        //(so in case of transparency there'll be an error)
        
        //clear the rectangle which is to be repainted
        //and update the painted stuff of the image afterwards.
        Picture.getInstance().emptyRectangle(
                
                //the coordinates in total image
                r_old.x - Page.getInstance().getJlbl_painting().getLocation().x,
                r_old.y - Page.getInstance().getJlbl_painting().getLocation().y,
                
                //the size of the image- part that is printed.
                //is one bigger than the rectangle because the lines are
                //painted outside of the rectangle, thus it is necessary
                //to repaint there.
                r_old.width + 1, 1,
                
                //the graphics coordinates.
                r_old.x,
                r_old.y,
                
                getBi());

        //clear the rectangle which is to be repainted
        //and update the painted stuff of the image afterwards.
        Picture.getInstance().emptyRectangle(
                
                //the coordinates in total image
                r_old.x - Page.getInstance().getJlbl_painting().getX()
                     + r_old.width,
                r_old.y - Page.getInstance().getJlbl_painting().getY(),
                //the size of the image- part that is printed.
                //is one bigger than the rectangle because the lines are
                //painted outside of the rectangle, thus it is necessary
                //to repaint there.
                1, r_old.height + 1, 
                
                //the graphics coordinates.
                r_old.x + r_old.width,
                r_old.y,
                
                getBi());

        //clear the rectangle which is to be repainted
        //and update the painted stuff of the image afterwards.
        Picture.getInstance().emptyRectangle(
                
                //the coordinates in total image
                r_old.x - Page.getInstance().getJlbl_painting().getX(),
                r_old.y - Page.getInstance().getJlbl_painting().getY()
                        + r_old.height,
                
                //the size of the image- part that is printed.
                //is one bigger than the rectangle because the lines are
                //painted outside of the rectangle, thus it is necessary
                //to repaint there.
                r_old.width + 1, 1, 
                
                //the graphics coordinates.
                r_old.x,
                r_old.y + r_old.height,
                
                getBi());
        
        //clear the rectangle which is to be repainted
        //and update the painted stuff of the image afterwards.
        Picture.getInstance().emptyRectangle(
                
                //the coordinates in total image
                r_old.x - Page.getInstance().getJlbl_painting().getX(),
                r_old.y - Page.getInstance().getJlbl_painting().getY(),
                
                //the size of the image- part that is printed.
                //is one bigger than the rectangle because the lines are
                //painted outside of the rectangle, thus it is necessary
                //to repaint there.
                1, r_old.height + 1, 
                
                //the graphics coordinates.
                r_old.x,
                r_old.y,
                
                getBi());

        Picture.getInstance().repaintRectangle(
                
                //the coordinates in total image
                r_old.x - Page.getInstance().getJlbl_painting().getX(),
                r_old.y - Page.getInstance().getJlbl_painting().getY(),
                
                //the size of the image- part that is printed.
                //is one bigger than the rectangle because the lines are
                //painted outside of the rectangle, thus it is necessary
                //to repaint there.
                r_old.width + 1, r_old.height + 1,
                
                //the graphics coordinates.
                r_old.x,
                r_old.y,
                
                getBi());
        //update the background raster
        Utils.getRastarImage(getBi(), 

                //the rectangle which is to be taken
                -Page.getInstance().getJlbl_painting().getX() + r_old.x,
                -Page.getInstance().getJlbl_painting().getY() + r_old.y,
                
                //the size of the 
                r_old.x + r_old.width + 1 
                - Page.getInstance().getJlbl_painting().getX(),
                r_old.y + r_old.height + 1 
                - Page.getInstance().getJlbl_painting().getY(), 
                
                //the location at graphics where to paint the raster
                r_old.x, r_old.y);  
        
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

        //paint the painting background (e.g. raster / lines) at the graphical
        //user interface.
        if (getBi() != null) {
            setBi(Utils.getRastarImage(getBi(), -getLocation().x + _x, 
                    -getLocation().y + _y, -getLocation().x + _x + _width, 
                    -getLocation().y + _y + _height, _x, _y));  
        }

        setIcon(new ImageIcon(getBi()));
        
        
        return getBi();
    }
    

    /**
     * paint the border and start thread.
     * time for one run 
     *     0,115 sec./run whole screen (not whole image).
     * Performance okay because it's threaded.    
     * @param _r the rectangle of the border.
     */
    private void paintBorder(final Rectangle _r) {

        System.out.println("7");
        //initialize the index for the first run of the thread
        index = 0;
        if (thrd_moveBorder != null) {
            thrd_moveBorder.interrupt();
        }
        
        //initialize the thread
        initializeBorderThread(_r);
        
        thrd_moveBorder.start();
    }
    
    
    
    
    public void refreshPopup(){
        //not necessary anymore because paitned to bi and autorefreshes.
//        refreshPaint();
    }
    
    
    public void removeZoomBox(){
        removeOldRectangle();
    }
    
    
    public void setZoomBox(final int _x, final int _y, 
            final int _width, final int _height){
        paintZoom(_x, _y, _width, _height);
    }
    
    
    /**
     * Initialize the border movement thread. Which prints and moves the border
     * @param _r the rectangle.
     */
    private void initializeBorderThread(final Rectangle _r) {

        System.out.println("8");
        thrd_moveBorder = new Thread() {
            @Override public void run() {

                //initialize start index
                int startIndex = index;

                //perform border movement 
                while (index < Integer.MAX_VALUE / 2 && !isInterrupted()) {
                    
                    //save graphics
                    Graphics graph = getGraphics();
                    
                    //because the index after one run is not equal to the 
                    //new one if the amount of pixel painted for the border
                    //is not congruent to 0 (modulo sizeOfOneColor)
                    startIndex = (startIndex 
                            + ViewSettings.SELECTION_BORDER_MOVE_SPEED_PX);
                    index = startIndex  
                            / ViewSettings.SELECTION_BORDER_BLOCK_SIZE;
                    int addIndex = startIndex
                            % ViewSettings.SELECTION_BORDER_BLOCK_SIZE;
                    
                    //stop thread if interrupted
                    try {
                        Thread.sleep(ViewSettings.SELECTION_BORDER_SLEEP_TIME);
                    } catch (InterruptedException e) {
                        interrupt();
                        continue;
                    }
                    if (isInterrupted()) {
                        continue;
                    }

                    /*
                     *  upper border
                     */
                    //create start and finish values
                    final int sizeBB = ViewSettings.SELECTION_BORDER_BLOCK_SIZE;
                    final int startX = _r.x / sizeBB;
                    final int plusX = _r.x % sizeBB;
                    final int untilX = (_r.width + _r.x - addIndex - plusX) 
                            / sizeBB;
                    
                    //print the first half line
                    graph.setColor(getColorBorderPx());
                    graph.drawLine(_r.x + 1,
                            _r.y + 1,
                            addIndex + plusX + startX * sizeBB,
                            _r.y + 1);
                    index++;
                    
                    //go through the for loop and fill all the entire border
                    //blocks of upper border
                    for (int x = startX; x < untilX; x++) {

                        graph.setColor(getColorBorderPx());
                        graph.drawLine(addIndex + plusX + x * sizeBB,
                                _r.y + 1, 
                                addIndex + plusX + (x + 1) * sizeBB,
                                _r.y + 1);
                        
                        //increase index.
                        index++;
                    }

                    int braucheNoch = Math.abs(sizeBB - (_r.width + _r.x 
                            - (addIndex + plusX + untilX * sizeBB)));

                    //paint the missing piece at the end of last line
                    graph.setColor(getColorBorderPx());
                    graph.drawLine(
                            plusX + addIndex + untilX * sizeBB, 
                            _r.y + 1, 
                            _r.width + _r.x,
                            _r.y + 1);

                    //because the pixel at the edge is not to be painted twice
                    braucheNoch++;
                    
                    //paint missing piece at the beginning of new line
                    graph.drawLine(
                            _r.width + _r.x, 
                            _r.y + 1, 
                            _r.width + _r.x,
                            _r.y + 1 + braucheNoch);
                    

                    //increase index
                    index++;
                    
                    /*
                     *  right border
                     */
                    //border block
                    int fromX = braucheNoch + _r.y % sizeBB;
                    for (int y = _r.y / sizeBB; y < (_r.y + _r.height 
                            - (braucheNoch + (_r.y + _r.height) % sizeBB)) 
                            / sizeBB; y++) {

                        graph.setColor(getColorBorderPx());

                        graph.drawLine(_r.x + _r.width, y * sizeBB + fromX,
                                _r.x + _r.width, (y + 1) * sizeBB + fromX);

                        //increase index.
                        index++;
                    }
                    
                    /*
                     *  bottom border
                     */
                    //border block
                    for (int x = (_r.width + _r.x) / sizeBB - 1; x >= _r.x 
                            / sizeBB; x--) {

                        graph.setColor(getColorBorderPx());

                        graph.drawLine((x) * sizeBB, _r.y + _r.height
                                , (x + 1) * sizeBB,  _r.y
                                + _r.height);

                        //increase index.
                        index++;
                    }

                    /*
                     * left border
                     */
                    //border block
                    for (int y = (_r.y + _r.height) 
                            / ViewSettings.SELECTION_BORDER_BLOCK_SIZE; y 
                            >= _r.y / ViewSettings.SELECTION_BORDER_BLOCK_SIZE; 
                            y--) {

                        graph.setColor(getColorBorderPx());

                        graph.drawLine(_r.x + 1, 
                                y * ViewSettings.SELECTION_BORDER_BLOCK_SIZE,
                                _r.x + 1,  (y + 1) 
                                * ViewSettings.SELECTION_BORDER_BLOCK_SIZE);

                        //increase index.
                        index++;
                    }
                }
            }
        };
    }
    
    
    /**
     * returns the color of the current border pixel. Used by thread which 
     * moves and sets the border.
     * 
     * @return the calculated color.
     */
    private Color getColorBorderPx() {
        
        return ViewSettings.SELECTION_BORDER_CLR_BORDER[index % ViewSettings
                     .SELECTION_BORDER_CLR_BORDER.length];
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
        
        //set width and height.
        super.setBounds(0, 0, _widht, _height);
        bi = new BufferedImage(_widht, _height, BufferedImage.TYPE_INT_ARGB);

        if (isVisible()) {
            
            //set changed
            refreshPaint();
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
        System.out.println("göt location");
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