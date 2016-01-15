package model.objects.painting.po;

import java.awt.Point;
import java.awt.Rectangle;
import java.io.Serializable;


/**
 * This is the type the list of selected PaintObjects consists of.
 * For being able to 
 * 	(1) 	remember the page owning the PaintObject 
 *  (2)		change the owning page by dragging the PaintObject to other page
 * the location of the owning page is saved inside po_selection.
 *  
 * By moving instance of PaintObject, the bounds relative to the owning page
 * is changed.
 * In case the y location is less than 0, the page number is decreased,
 * in case the y location is greater than the page's height, the page number is#
 * increased.
 * 
 * @author Julius Huelsmann
 * @version %I%, %U%
 *
 */
public class PoSelection implements Serializable, Cloneable {
	
	/**
	 * The location of the page which owns the PaintObject at the time
	 * the PaintObject is added to the list of selected items of type
	 * <code>PoSelection</code>
	 */
	private final Point pnt_locationOfPage;
	
	
	
	/**
	 * The selected PaintObject.
	 */
	private final PaintObject po;
	
	
	/**
	 * 
	 * Save the location of the page which owns the PaintObject while the 
	 * PaintObject is selected and the selected paintObject itself.
	 * 
	 * @see #PoSelection.
	 * 
	 * @param _po					the PaintObject
	 * @param _pnt_locationOfPage	the location of the page which contains
	 * 								the PaintObject the moment the PaintObject
	 * 								is selected.
	 */
	public PoSelection(
			final PaintObject _po, 
			final Point _pnt_locationOfPage) {
		this.pnt_locationOfPage = _pnt_locationOfPage;
		this.po = _po;
	}
	
	
	
	/**
	 * Returns location in project.
	 */
	public Point getLocationInProject() {
		
		
		final Rectangle r_bounds = po.getSnapshotBounds();
		return new Point(
				pnt_locationOfPage.x + r_bounds.x,
				pnt_locationOfPage.y + r_bounds.y);
	}


    
    /**
     * Clone this paint object.
     * @return the cloned object
     */
    public final PoSelection clone() {
    	
    	try {
			return (PoSelection) super.clone();
		} catch (CloneNotSupportedException e) {
			e.printStackTrace();
			return null;
		}
    }
  

	/**
	 * @return the po
	 */
	public PaintObject getPaintObject() {
		return po;
	}

}
