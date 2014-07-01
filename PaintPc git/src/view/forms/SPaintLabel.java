package view.forms;

//import declarations
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;

import settings.ViewSettings;
import start.utils.Utils;
import view.util.VPaintLabel;
import model.objects.painting.Picture;


/**
 * 
 * @author Julius Huelsmann
 * @version %I%, %U%
 */
@SuppressWarnings("serial")
public class SPaintLabel extends VPaintLabel {

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
    public SPaintLabel() { }
    
    /**
     * {@inheritDoc}
     */
    @Override public final void paint(final Graphics _g) {
        
        //if is set for the first time, refreshPaint
        if (Picture.getInstance().alterGraphics(getGraphics()) && isVisible()) {
            refreshPaint();
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override public final void refreshPaint() {

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
        getGraphics().drawRect(_x + 1, _y + 1, _width - 1, _height - 1);
        this.r_old = new Rectangle(_x + 1, _y + 1, _width - 1, _height - 1);
    }
    
    
    /**
     * Paint the entire selection stuff.
     * @param _r the rectangle which is selected.
     */
    public final void paintEntireSelection(final Rectangle _r) {

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
        getGraphics().drawRect(_x + 1, _y + 1, _width - 1, _height - 1);
        this.r_old = new Rectangle(_x + 1, _y + 1, _width - 1, _height - 1);
    }
    
    
    /**
     * Remove the old changed pixel from zoom.
     */
    public final void removeOldRectangle() {

        if (r_old == null) {
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
        Picture.getInstance().updateRectangle(
                
                //the coordinates in total image
                r_old.x - Page.getInstance().getJlbl_painting().getX(),
                r_old.y - Page.getInstance().getJlbl_painting().getY(),
                
                //the size of the image- part that is printed.
                //is one bigger than the rectangle because the lines are
                //painted outside of the rectangle, thus it is necessary
                //to repaint there.
                r_old.width + 1, 1,
                
                //the graphics coordinates.
                r_old.x,
                r_old.y);

        //clear the rectangle which is to be repainted
        //and update the painted stuff of the image afterwards.
        Picture.getInstance().updateRectangle(
                
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
                r_old.y);

        //clear the rectangle which is to be repainted
        //and update the painted stuff of the image afterwards.
        Picture.getInstance().updateRectangle(
                
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
                r_old.y + r_old.height);

        //clear the rectangle which is to be repainted
        //and update the painted stuff of the image afterwards.
        Picture.getInstance().updateRectangle(
                
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
                r_old.y);

        //update the background raster
        Utils.getRastarImage(g, 

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
    public final Graphics refreshRectangle(final int _x, final int _y, 
            final int _width, final int _height) {

        if(isVisible()){
        //paint the painted stuff at graphics
        Graphics g_out = Picture.getInstance().updateRectangle(
                -getX() + _x, -getY() + _y, _width, _height, _x, _y);

        //paint the painting background (e.g. raster / lines) at the graphical
        //user interface.
        if (g_out != null) {
            Utils.getRastarImage(g_out, -getX() + _x, 
                    -getY() + _y, -getX() + _x + _width, 
                    -getY() + _y + _height, _x, _y);  
        }
        
        return g_out;}
        return null;
    }
    

    /**
     * paint the border and start thread.
     * time for one run 
     *     0,115 sec./run whole screen (not whole image).
     * Performance okay because it's threaded.    
     * @param _r the rectangle of the border.
     */
    private void paintBorder(final Rectangle _r) {

        //initialize the index for the first run of the thread
        index = 0;
        if (thrd_moveBorder != null) {
            thrd_moveBorder.interrupt();
        }
        
        //initialize the thread
        initializeBorderThread(_r);
        
        thrd_moveBorder.start();
    }
    
    
    
    /**
     * Initialize the border movement thread. Which prints and moves the border
     * @param _r the rectangle.
     */
    private void initializeBorderThread(final Rectangle _r) {

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
    
    

    
}