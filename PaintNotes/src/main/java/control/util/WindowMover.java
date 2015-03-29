//package declaration
package control.util;

//import declarations
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionAdapter;
import java.awt.event.MouseMotionListener;
import java.io.Serializable;

import javax.net.ssl.SSLEngineResult.Status;
import javax.swing.ImageIcon;

import model.settings.Constants;
import model.settings.ViewSettings;
import model.util.paint.Utils;

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
	
	private ActivityListener al;
	
	public void setActivityListener(ActivityListener _al) {
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

	private byte currentOperation = -1;
	private final byte 
	OPERATION_TOP = 0, 
	OPERATION_TOP_RIGHT = 1, 
	OPERATION_RIGHT = 2, 
	OPERATION_RIGHT_BOTTOM = 3, 
	OPERATION_BOTTOM = 4, 
	OPERATION_BOTTOM_LEFT = 5, 
	OPERATION_LEFT = 6, 
	OPERATION_LEFT_TOP = 7,
	OPERATION_NO = -1;

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
	
	private byte prevImage = OPERATION_NO;
	/**
	 * {@inheritDoc}
	 */
	@Override
	public final void mouseMoved(final MouseEvent _event) { 
		
		if (!ViewSettings.isFullscreen()) {

			byte cOperation = getOperation(_event);
			if (cOperation != prevImage) {

				setCursor(cOperation);
				prevImage = cOperation;
			}
		}
	}

	/**
	 * set the cursor.
	 * 
	 * @param _path the path of the cursor image.
	 * @param _name the name of the cursor
	 */
	private void setCursor(final byte _operation) {

		switch(_operation){
		case OPERATION_TOP:
		    cmp_moved.setCursor(Cursor.getPredefinedCursor(Cursor.N_RESIZE_CURSOR));
			break;
		case OPERATION_TOP_RIGHT:
		    cmp_moved.setCursor(Cursor.getPredefinedCursor(Cursor.NE_RESIZE_CURSOR));
			break;
		case OPERATION_RIGHT:
		    cmp_moved.setCursor(Cursor.getPredefinedCursor(Cursor.E_RESIZE_CURSOR));
			break;
		case OPERATION_RIGHT_BOTTOM:
		    cmp_moved.setCursor(Cursor.getPredefinedCursor(Cursor.SE_RESIZE_CURSOR));
			break;
		case OPERATION_BOTTOM:
		    cmp_moved.setCursor(Cursor.getPredefinedCursor(Cursor.S_RESIZE_CURSOR));
			break;
		case OPERATION_BOTTOM_LEFT:
		    cmp_moved.setCursor(Cursor.getPredefinedCursor(Cursor.SW_RESIZE_CURSOR));
			break;
		case OPERATION_LEFT:
		    cmp_moved.setCursor(Cursor.getPredefinedCursor(Cursor.W_RESIZE_CURSOR));
			break;
		case OPERATION_LEFT_TOP:
		    cmp_moved.setCursor(Cursor.getPredefinedCursor(Cursor.NW_RESIZE_CURSOR));
			break;
		default:
		    cmp_moved.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
			break;
		}
			    
	}
	
	private byte getOperation(final MouseEvent _event) {

		//reset current operation
		byte cOperation = OPERATION_NO;
		
		//if the click is in the right area
		if (_event.getX() >= 0 && _event.getX() <= marge) {
			cOperation = OPERATION_LEFT;
		} else if (_event.getX() >= cmp_moved.getWidth() - marge) {
			cOperation = OPERATION_RIGHT;
		}
	
		if (_event.getY() >= 0 && _event.getY() <= marge) {
			
			switch(cOperation){
			case OPERATION_NO:
				cOperation = OPERATION_TOP;
				break;
			case OPERATION_LEFT:
				cOperation = OPERATION_LEFT_TOP;
				break;
			case OPERATION_RIGHT:
				cOperation = OPERATION_TOP_RIGHT;
				break;
			default:
				cOperation = OPERATION_TOP;
				break;
			}
		} else if (_event.getY() >= cmp_moved.getHeight() - marge) {
			cOperation = OPERATION_BOTTOM;
			switch(cOperation){
			case OPERATION_NO:
				cOperation = OPERATION_BOTTOM;
				break;
			case OPERATION_LEFT:
				cOperation = OPERATION_BOTTOM_LEFT;
				break;
			case OPERATION_RIGHT:
				cOperation = OPERATION_RIGHT_BOTTOM;
				break;
			default:
				cOperation = OPERATION_BOTTOM;
				break;
			}
		}
		return cOperation;
	}
	/**
	 * {@inheritDoc}
	 */
	public final void mouseExited(final MouseEvent _event) { }

	public final void mousePressed(final MouseEvent _event) {

		if (!ViewSettings.isFullscreen()) {

			currentOperation = getOperation(_event);
			
			//save start position and set pressed
			pnt_startPosition = 
					new Point(_event.getXOnScreen() - cmp_moved
					.getLocation().x, _event.getYOnScreen() - cmp_moved
					.getLocation().y);
			
			pnt_diff2 = 
					new Point(_event.getXOnScreen(), _event.getYOnScreen());
			pnt_origSize = new Point(cmp_moved.getWidth(), cmp_moved.getHeight());
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

			if (al != null && currentOperation != OPERATION_NO) {
				
				al.activityOccurred(_event);
			}
		}
	}

	/**
	 * {@inheritDoc}
	 */
	public final synchronized void mouseDragged(final MouseEvent _event) {

		if (!ViewSettings.isFullscreen()) {


			//if pressed change location
			if (pressed) {
				switch (currentOperation) {

				case OPERATION_NO:
					
					cmp_moved.setLocation(_event.getXOnScreen() - pnt_startPosition.x,
							_event.getYOnScreen() - pnt_startPosition.y);
					break;
				case OPERATION_TOP:

					cmp_moved.setLocation(
							(int) pnt_origLoc.getX(),
							(int) (pnt_origLoc.getY() - pnt_diff2.getY() + _event.getYOnScreen())
							);
					
					cmp_moved.setSize(
							(int) pnt_origSize.getX(),
							(int) (pnt_origSize.getY() + pnt_diff2.getY() - _event.getYOnScreen())
							);
					break;
				case OPERATION_BOTTOM:

					
					cmp_moved.setSize(
							(int) pnt_origSize.getX(),
							(int) (pnt_origSize.getY() - pnt_diff2.getY() + _event.getYOnScreen())
							);
					break;
				case OPERATION_RIGHT:
					

					
					cmp_moved.setSize(
							(int) (pnt_origSize.getX() - pnt_diff2.getX() + _event.getXOnScreen()),
							(int) pnt_origSize.getY()
							);
					break;
				case OPERATION_LEFT:
					cmp_moved.setLocation(
							(int) (pnt_origLoc.getX() - pnt_diff2.getX() + _event.getXOnScreen()),
							(int) pnt_origLoc.getY()
							);
					
					cmp_moved.setSize(
							(int) (pnt_origSize.getX() + pnt_diff2.getX() - _event.getXOnScreen()),
							(int) pnt_origSize.getY()
							);
					break;

				case OPERATION_TOP_RIGHT:
					break;
				case OPERATION_RIGHT_BOTTOM:
					break;
				case OPERATION_BOTTOM_LEFT:

					cmp_moved.setSize(_event.getXOnScreen() - pnt_startPosition.x,
							_event.getYOnScreen() - pnt_startPosition.y);
					break;
				case OPERATION_LEFT_TOP:

					cmp_moved.setLocation(_event.getXOnScreen() - pnt_startPosition.x,
							_event.getYOnScreen() - pnt_startPosition.y);
					break;
				default: break;
				}
			}

			//repaint
			cmp_moved.repaint();
		}

	}
	
}
