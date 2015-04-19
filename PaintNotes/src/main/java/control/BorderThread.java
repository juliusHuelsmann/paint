package control;

import java.awt.Color;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

import javax.swing.ImageIcon;

import model.objects.painting.po.PaintObject;
import model.objects.pen.special.PenSelection;
import model.settings.ViewSettings;
import model.util.Util;
import view.forms.Page;
import view.forms.Tabs;

/**
 * BorderThread contains the necessary methods for painting moving rectangle
 * border to the PaintLabel.
 * @author Julius Huelsmann
 * 
 * @version %I%, %U%
 *
 */
public class BorderThread extends Thread {

    /*
     * Stuff for line painting.
     */
    
    /**
     * The current paintObject for line painting.
     */
    private PenSelection pen_curr;
    
    /**
     * The current pen for line painting.
     */
    private PaintObject po_curr;
    
    /*
     * Stuff for rectangle painting
     */

    /**
     * The rectangle which is to be painted.
     */
    private Rectangle rect;
    
    /**
     * Index for border movement in thread thrd_moveBorder. Determines the 
     */
    private int indexColor = 0;
    
    /*
     * Boolean to switch operation.
     */
    
    /**
     * Whether it is operation rectangle or operation line.
     */
    private boolean operationRectangle;
    
    /**
     * Instance of view- class tab.
     */
    private final Tabs tab;
    
    
    /**
     * Instance of the view - class page.
     */
    private final Page page;
    
    /**
     * Constructor: save instance of PaintLabel and Rectangle.
     * 
     * which operation to take
     * @param _operationRectangle whether to paint a rectangle or a closed line
     * 
     * rectangle operation values:
     * @param _r the rectangle which is to be painted.
     * 
     * line operation values:
     * @param _po the current PaintObject used for line operation
     * @param _pen the current Pen used for line operation
     * 
     * @param _tab the Tab for refreshing
     */
    public BorderThread(final Rectangle _r, 
            final boolean _operationRectangle, final PaintObject _po,
            final PenSelection _pen, final Tabs _tab, final Page _page) {
        
        /*
         * save values
         */
        //boolean to switch operation
        this.operationRectangle = _operationRectangle;
        
        //save rectangle values
        this.rect = _r;
        
        //save line values
        this.pen_curr = _pen;
        this.po_curr = _po;
    	this.tab = _tab;
    	this.page = _page;
    }

    
    /**
     * Run method contains code for painting border.
     */
    @Override public final void run() {

        final int sizeBB = ViewSettings.SELECTION_BORDER_BLOCK_SIZE;
        int indexStep = 0;
        final int sleepTimeRectangle = 100;
        final int sleepTimeLine = 400;
        int sleepTime;
        BufferedImage bi_neutral = Util.getEmptyBISelection();
        
        //perform border movement 
        if (!(System.getProperties().getProperty("os.name").contains("Windows"))) {
	        while (!isInterrupted()) {
	
	            if (operationRectangle) {
	
	                sleepTime = sleepTimeRectangle;
	                indexStep = paintRectangleStep(sizeBB, indexStep, bi_neutral);
	            } else {
	                sleepTime = sleepTimeLine;
	                paintLineStep(indexStep);
	                indexStep++;
	            }
	            
	            try {
	                Thread.sleep(sleepTime);
	            } catch (InterruptedException e) {
	                interrupt();
	            }
	        }
        }
    }
    
    
    /**
     * One step of painting a rectangle.
     * 
     * @param _sizeBB the size of the border block
     * @param _indexStep the index step
     * @param _bi_neutral the BufferedImage to paint on
     * 
     * @return the new index step
     */
    private int paintRectangleStep(final int _sizeBB, final int _indexStep, 
            final BufferedImage _bi_neutral) {

        int indexStep = _indexStep;
        /*
         * save stuff for entire loop
         */
        
        //increase color index if necessary 
        indexColor += indexStep / _sizeBB;
        
        //make index step modulo value.
        indexStep = indexStep % _sizeBB;

        //integer for resetting color index at the end of the loop
        int indexBackup = indexColor;

        /*
         * top
         */
        int begin = indexStep;
        begin = paintRectangleTop(begin, _bi_neutral, _sizeBB);
        
        /*
         * right
         */
        begin = paintRectangleRight(begin, _bi_neutral, _sizeBB);
        
        /*
         * bottom
         */
        begin = paintRectangleBottom(begin, _bi_neutral, _sizeBB);
        
        /*
         * left
         */
        begin = paintRectangleLeft(begin, _bi_neutral, _sizeBB);
        
        
        //reset color index; is increased at the beginning of the 
        //loop and increase step index.
        indexColor = indexBackup;
        indexStep += ViewSettings.SELECTION_BORDER_MOVE_SPEED_PX;

        tab.repaint();
        page.getJlbl_selectionBG().setIcon(
        		new ImageIcon(
                _bi_neutral));
        tab.revalidate();
        
        return indexStep;
        
    }
    
    
    /**
     * One step for painting a free line - selection.
     * @param _step the step counter
     */
    private void paintLineStep(final int _step) {


        //sample code; to be written later.
        BufferedImage bi_transformed = Util.getEmptyBISelection();
        if (po_curr == null) {
            interrupt();
        } else {

            bi_transformed = po_curr.paint(bi_transformed, false, 
                    bi_transformed, 
                    page.getJlbl_painting().getLocation().x, 
                    page.getJlbl_painting().getLocation().y, 
                    null);


            page.getJlbl_selectionBG().setIcon(
                    new javax.swing.ImageIcon(bi_transformed));
            //color shift.
            pen_curr.resetCurrentBorderValue();
            
        }
    
    }
    
    
    /*
     * paint rectangle stuff
     */
    
    /**
     * paint the top edge of the rectangle.
     * @param _begin the beginning pixel
     * @param _bi_neutral the BufferedImage to paint on
     * @param _sizeBB the size
     * 
     * @return the new _begin value
     */
    private int paintRectangleTop(final int _begin, 
            final BufferedImage _bi_neutral, 
            final int _sizeBB) {

        for (int step = 0;; step++) {
            //setColor and calculate from and until values
            int c = getColorBorderPx().getRGB();
            int fromX = rect.x - (_sizeBB - _begin) + step * _sizeBB;
            int toX = rect.x - (_sizeBB - _begin) + (step + 1) * _sizeBB;

            //exit condition. (Starting in front of the line, going
            //right after the line.
            if (fromX > rect.x + rect.width) { 
                
                //tell the next line where to begin & exit for loop
                return (fromX - rect.x - rect.width) % _sizeBB;
            }

            //paint border
            for (int pixelX = fromX; pixelX <= toX; pixelX++) {
                if (pixelX >= rect.x && pixelX < rect.x + rect.width) {
                    int x = pixelX - page
                            .getJlbl_selectionBG().getX();
                    int y = rect.y - page
                            .getJlbl_selectionBG().getY();
                    
                    if (x >= 0 && x < _bi_neutral.getWidth()
                            && y >= 0 && y < _bi_neutral.getHeight()) {

                        _bi_neutral.setRGB(x, y, c);
                    }
                } else if (pixelX >= rect.x + rect.width) {

                    //decrease index; otherwise increased 2 often.
                    indexColor--;
                    break;
                }
            }
            indexColor++;
        }
    }
    
    
    /**
     * paint the right edge of the rectangle.
     * @param _begin the beginning pixel
     * @param _bi_neutral the BufferedImage to paint on
     * @param _sizeBB the size
     * 
     * @return the new _begin value
     */
    private int paintRectangleRight(final int _begin, 
            final BufferedImage _bi_neutral, 
            final int _sizeBB) {

        //begin = value set inside last loop
        for (int step = 0;; step++) {
            //setColor and calculate from and until values
            int c = getColorBorderPx().getRGB();
            int fromY = rect.y - (_sizeBB - _begin) + step * _sizeBB;
            int toY = rect.y - (_sizeBB - _begin) + (step + 1) * _sizeBB;
            
            //exit condition. (Starting in front of the line, going
            //right after the line.
            if (fromY > rect.y + rect.height) { 

                //tell the next line where to begin & exit for loop
                return (fromY - rect.y - rect.height) % _sizeBB;
            }

            //paint border
            for (int pixelY = fromY; pixelY <= toY; pixelY++) {
                if (pixelY >= rect.y && pixelY < rect.y + rect.height) {
                   int x = rect.x + rect.width - page
                           .getJlbl_selectionBG().getX() - 1;
                   int y = pixelY - page
                           .getJlbl_selectionBG().getY();
                   
                   if (x >= 0 && x < _bi_neutral.getWidth() 
                           && y >= 0 && y < _bi_neutral.getHeight()) {
                       _bi_neutral.setRGB(x, y, c);
                   }
                } else if (pixelY >= rect.y + rect.height) {
                    //decrease index color because has been 
                    //increased too often.
                    indexColor--;
                    break;
                }
            }
            indexColor++;
        }
    }
    

    
    /**
     * paint the bottom edge of the rectangle.
     * @param _begin the beginning pixel
     * @param _bi_neutral the BufferedImage to paint on
     * @param _sizeBB the size
     * 
     * @return the new _begin value
     */
    private int paintRectangleBottom(final int _begin, 
            final BufferedImage _bi_neutral, 
            final int _sizeBB) {

        for (int step = 0;; step++) {

            //setColor and calculate from and until values
            int c = getColorBorderPx().getRGB();
            int fromX = rect.x + rect.width + (_sizeBB - _begin) 
                    - step * _sizeBB;
            int toX =  rect.x  + rect.width + (_sizeBB - _begin) 
                    - (step + 1) * _sizeBB;
            
            //exit condition. (Starting in front of the line, going
            //right after the line.
            if (fromX < rect.x) { 

                //tell the next line where to begin & exit loop
                return (rect.x - fromX) % _sizeBB;
            }
            //paint border
            for (int pixelX = fromX; pixelX >= toX; pixelX--) {
                if (pixelX <= rect.x + rect.width && pixelX > rect.x) {
                   int x = pixelX - page
                           .getJlbl_selectionBG().getX();
                   int y = rect.y + rect.height - page
                           .getJlbl_selectionBG().getY() - 1;

                   if (x >= 0 && x < _bi_neutral.getWidth() 
                           && y >= 0 && y < _bi_neutral.getHeight()) {
                       _bi_neutral.setRGB(x, y, c);
                   }
                } else if (pixelX <= rect.x) {
                    //decrease index color because has been 
                    //increased too often.
                    indexColor--;
                    break;
                }
            }
            indexColor++;
        }
    }
    
    
    /**
     * paint the left edge of the rectangle.
     * @param _begin the beginning pixel
     * @param _bi_neutral the BufferedImage to paint on
     * @param _sizeBB the size
     * 
     * @return the new _begin value
     */
    private int paintRectangleLeft(final int _begin, 
            final BufferedImage _bi_neutral, 
            final int _sizeBB) {
        for (int step = 0;; step++) {
            //setColor and calculate from and until values
            int c = getColorBorderPx().getRGB();
            int fromY = rect.y + rect.height + (_sizeBB - _begin) 
                    - step * _sizeBB;
            int toY =  rect.y  + rect.height + (_sizeBB - _begin) 
                    - (step + 1) * _sizeBB;
            
            //exit condition. (Starting in front of the line, going
            //right after the line.
            if (fromY < rect.y) { 

                //tell the next line where to begin. 
                //unimportant in this last step
                return (rect.y - fromY) % _sizeBB;
            }

            //paint border
            for (int pixelY = fromY; pixelY >= toY; pixelY--) {
                if (pixelY <= rect.y + rect.height && pixelY > rect.y) {
                   int x = rect.x - page
                           .getJlbl_selectionBG().getX();
                   int y = pixelY - page
                           .getJlbl_selectionBG().getY();
                   if (x >= 0 && x < _bi_neutral.getWidth() 
                           && y >= 0 && y < _bi_neutral.getHeight()) {
                       _bi_neutral.setRGB(x, y, c);
                   }
                } else if (pixelY <= rect.y) {

                    //decrease index color because has been 
                    //increased too often.
                    indexColor--;
                    break;
                }
            }
            indexColor++;
        }
    }

    
    
    /**
     * returns the color of the current border pixel. Used by thread which 
     * moves and sets the border.
     * 
     * @return the calculated color.
     */
    private Color getColorBorderPx() {
        
        return ViewSettings.SELECTION_BORDER_CLR_BORDER
                [indexColor % ViewSettings.SELECTION_BORDER_CLR_BORDER.length];
    }

}
