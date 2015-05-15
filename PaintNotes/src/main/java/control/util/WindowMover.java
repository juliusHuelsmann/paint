//package declaration
package control.util;

//import declarations
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionAdapter;
import java.io.Serializable;
import model.settings.ViewSettings;
import control.interfaces.ActivityListener;


/**
 * The controller class which changes the position of a component by 
 * a user dragging it.
 * 
 * @author Julius Huelsmann
 * @version %I%, %U%
 */
@SuppressWarnings("serial")
public class WindowMover extends MouseMotionAdapter implements 
MouseListener, Serializable {
	
	
	/**
	 * The activityListener.
	 */
	private ActivityListener al;
	
	
	/**
	 * set an activity listener. 
	 * @param _al the activityListener
	 */
	public final void setActivityListener(final ActivityListener _al) {
		this.al = _al;
	}
	/**
	 * contains whether the user currently drags the component.
	 */
	private boolean pressed = false;

	/**
	 * the component which is to be moved.
	 */
	private final Component cmp_moved;

	/**
	 * the position from where the drag started.
	 */
	private Point pnt_startPosition, pnt_diff2, pnt_origSize, pnt_origLoc;

	
	
	
	/**
	 * Marge.
	 */
	private final int marge = 10;
	
	/**
	 * Constructor saves component of which this class changes the location.
	 * @param _view the component
	 */
	public WindowMover(final Component _view) {

		//save component
		this.cmp_moved = _view;
	}


	/**
	 * {@inheritDoc}
	 */
	public final void mouseClicked(final MouseEvent _event) { }

	/**
	 * {@inheritDoc}
	 */
	public final void mouseEntered(final MouseEvent _event) { }
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public final void mouseMoved(final MouseEvent _event) { }


	
	/**
	 * {@inheritDoc}
	 */
	public final void mouseExited(final MouseEvent _event) { }

	
	/**
	 * {@inheritDoc}
	 */
	public final void mousePressed(final MouseEvent _event) {

		if (!ViewSettings.isFullscreen()) {

			
			
			//save start position and set pressed
			pnt_startPosition = 
					new Point(_event.getXOnScreen() - cmp_moved
					.getLocation().x, _event.getYOnScreen() - cmp_moved
					.getLocation().y);
			
			pnt_diff2 = 
					new Point(_event.getXOnScreen(), _event.getYOnScreen());
			pnt_origSize = new Point(cmp_moved.getWidth(), cmp_moved
					.getHeight());
			pnt_origLoc = new Point(cmp_moved.getX(), cmp_moved.getY());
			pressed = true;

		}
	}

	/**
	 * {@inheritDoc}
	 */
    public final void mouseReleased(final MouseEvent _event) {

		if (!ViewSettings.isFullscreen()) {

			//set not pressed and repaint component if mouse is released
			pressed = false;
			cmp_moved.repaint();
			

		}
	}

	/**
	 * {@inheritDoc}
	 */
	public final synchronized void mouseDragged(final MouseEvent _event) {

		if (!ViewSettings.isFullscreen()) {


			//if pressed change location
			if (pressed) {
					
					int newX = _event.getXOnScreen() - pnt_startPosition.x;
					int newY = _event.getYOnScreen() - pnt_startPosition.y;
					cmp_moved.setLocation(newX, newY);
			}

			//repaint
			cmp_moved.repaint();
		}

	}
	
}
