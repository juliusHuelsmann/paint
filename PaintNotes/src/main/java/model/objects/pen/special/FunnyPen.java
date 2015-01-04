//package declaration
package model.objects.pen.special;

//import declarations
import java.awt.Color;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

import model.objects.pen.Pen;
import model.settings.Constants;
import model.settings.TextFactory;
import model.settings.ViewSettings;
import model.util.DPoint;


/**
 * .
 * @author Julius Huelsmann
 * @version %I%, %U%
 */
@SuppressWarnings("serial")
public class FunnyPen extends Pen {

    /**
     * values for moving the border.
     */
    private int currentBorderValue = 0, 
            currentBorderStart = 0;
    
    /**
     * Constructor calls super constructor.
     */
	public FunnyPen() {
		super(Constants.PEN_ID_POINT, 1, Color.black, 
		        Constants.PATH_PEN_FILLER_LINE);
	}
	
	/**
	 * The last printed point.
	 */
	private DPoint pnt_lastPrinted;

	/**
	 * {@inheritDoc}
	 */
	@Override protected final void paintPoint(final DPoint _p, 
	        final BufferedImage _bi, final boolean _final, 
	        final DPoint _pnt_shift, final BufferedImage _g, 
	        final Rectangle _r_visibleScope) {

		//TODO: update (apply) the r_visible scope update
	    //first printed point
	    if (pnt_lastPrinted == null) {

	        
	        _bi.setRGB((int) _p.getX(), (int) _p.getY(), 
	                ViewSettings.SELECTION_BORDER_CLR_BORDER[0].getRGB()); 
	        pnt_lastPrinted = _p;

	    } else {
	        
	        int distanceX =  (int) Math.abs(_p.getX() - pnt_lastPrinted.getX());
	        int distanceY = (int) Math.abs(_p.getY() - pnt_lastPrinted.getY());
	        
	        int distance = Math.max(distanceX, distanceY);
	        
	        if (distance >= ViewSettings.SELECTION_BORDER_BLOCK_SIZE) {

	            //calculate the color
	            int s = (currentBorderValue 
	                    / ViewSettings.SELECTION_BORDER_CLR_BORDER.length)
	                    % ViewSettings.SELECTION_BORDER_CLR_BORDER.length;

	            //generate vector from pnt_lastPrinted towards the new one
	            //and norm it afterwards to the length of the maximal selection
	            //border size
	            DPoint vector = new DPoint(_p.getX() - pnt_lastPrinted.getX(), 
	                    _p.getY() - pnt_lastPrinted.getY());
	            double length = Math.sqrt(Math.pow(vector.getX(), 2)
	                    + Math.pow(vector.getY(), 2));
	            vector.setX((int) (vector.getX() 
	                    * ViewSettings.SELECTION_BORDER_BLOCK_SIZE / length));
	            vector.setY((int) (vector.getY()
                        * ViewSettings.SELECTION_BORDER_BLOCK_SIZE / length));
	            
	            
	            for (int i = 1; 
	                    i <= ViewSettings.SELECTION_BORDER_BLOCK_SIZE; i++) {

	                _bi.setRGB((int) (pnt_lastPrinted.getX() + vector.getX() 
	                        * i / ViewSettings.SELECTION_BORDER_BLOCK_SIZE), 
	                        (int) (pnt_lastPrinted.getY() + vector.getY() 
                            * i / ViewSettings.SELECTION_BORDER_BLOCK_SIZE), 
	                        ViewSettings.SELECTION_BORDER_CLR_BORDER[s]
	                                .getRGB()); 
	            }
	            
	            pnt_lastPrinted.setX(pnt_lastPrinted.getX() + vector.getX());
	            pnt_lastPrinted.setY(pnt_lastPrinted.getY() + vector.getY());
	            
	            currentBorderValue++;
	        }
	    }

	}
	
	
	/**
	 * reset the current border value.
	 */
	public final void resetCurrentBorderValue() {
	    this.currentBorderStart += ViewSettings.SELECTION_BORDER_MOVE_SPEED_PX;
	    this.currentBorderValue = currentBorderStart;
	}

	

	/**
	 * {@inheritDoc}
	 */
	@Override
	public final String getName() {
		return TextFactory.getInstance().getName_Funny();
	}

}
