package control.interfaces;

import java.awt.Point;


/**
 * MoveEvent is a event class used in the listener of TabbedPane in
 * method onMove().
 * 
 * @author Julius Huelsmann
 * @version %I%, %U%
 */
public class MoveEvent {

	
	/**
	 * The location of the tabbedPane.
	 */
	private Point pnt_bottomLocation;

	
	/**
	 * Constructor: save point.
	 * @param _pnt_location the point.
	 */
	public MoveEvent(final Point _pnt_location) {
		this.pnt_bottomLocation = _pnt_location;
	}


	/**
	 * @return the pnt_bottomLocation
	 */
	public final Point getPnt_bottomLocation() {
		return pnt_bottomLocation;
	}

	
}
