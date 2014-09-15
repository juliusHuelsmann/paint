//package declaration
package model.objects.pen.special;

//import declarations
import java.awt.Color;
import java.awt.Point;
import java.awt.image.BufferedImage;
import settings.ViewSettings;
import model.objects.pen.Pen;


/**
 * .
 * @author Julius Huelsmann
 * @version %I%, %U%
 */
@SuppressWarnings("serial")
public class PenSelection extends Pen {

    /**
     * values for moving the border.
     */
    private int currentBorderValue = 0, 
            currentBorderStart = 0;
    
    /**
     * Constructor calls super constructor.
     * @param _operation operation like math and point etc.
     * @param _thickness thickness in px
     * @param _clr color
     */
	public PenSelection(final int _operation, final int _thickness, 
	        final Color _clr) {
		super(_operation, _thickness, _clr);
	}
	

	/**
	 * {@inheritDoc}
	 */
	@Override protected final void paintPoint(final Point _p, 
	        final BufferedImage _bi, final boolean _final, 
	        final Point _pnt_shift, final BufferedImage _g) {

        int s = currentBorderValue / ViewSettings.SELECTION_BORDER_BLOCK_SIZE
                % ViewSettings.SELECTION_BORDER_CLR_BORDER.length;

        _bi.setRGB(_p.x, _p.y, 
                ViewSettings.SELECTION_BORDER_CLR_BORDER[s].getRGB()); 

		currentBorderValue++;
	}
	
	
	/**
	 * reset the current border value.
	 */
	public final void resetCurrentBorderValue() {
	    this.currentBorderStart += ViewSettings.SELECTION_BORDER_MOVE_SPEED_PX;
	    this.currentBorderValue = currentBorderStart;
	}


}
