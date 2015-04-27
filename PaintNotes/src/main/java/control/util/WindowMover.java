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
	 * Bytes indicating the current operation.
	 */
	private final byte 
	operationTop = 0, 
	operationTopRight = 1, 
	operationRight = 2, 
	operationRightBottom = 3, 
	operationBottom = 4, 
	operationBottomLeft = 5, 
	operationLeft = 6, 
	operationLeftTop = 7,
	operationNo = -1;

	/**
	 * The current operation indicated by the above-declared IDs.
	 */
	private byte currentOperation = operationNo;

	
	/**
	 * Previous image identifier.
	 */
	private byte prevImage = operationNo;
	
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
	 * @param 	_operation the operation
	 */
	private void setCursor(final byte _operation) {

		switch(_operation) {
		case operationTop:
		    cmp_moved.setCursor(Cursor.getPredefinedCursor(
		    		Cursor.N_RESIZE_CURSOR));
			break;
		case operationTopRight:
		    cmp_moved.setCursor(Cursor.getPredefinedCursor(
		    		Cursor.NE_RESIZE_CURSOR));
			break;
		case operationRight:
		    cmp_moved.setCursor(Cursor.getPredefinedCursor(
		    		Cursor.E_RESIZE_CURSOR));
			break;
		case operationRightBottom:
		    cmp_moved.setCursor(Cursor.getPredefinedCursor(
		    		Cursor.SE_RESIZE_CURSOR));
			break;
		case operationBottom:
		    cmp_moved.setCursor(Cursor.getPredefinedCursor(
		    		Cursor.S_RESIZE_CURSOR));
			break;
		case operationBottomLeft:
		    cmp_moved.setCursor(Cursor.getPredefinedCursor(
		    		Cursor.SW_RESIZE_CURSOR));
			break;
		case operationLeft:
		    cmp_moved.setCursor(Cursor.getPredefinedCursor(
		    		Cursor.W_RESIZE_CURSOR));
			break;
		case operationLeftTop:
		    cmp_moved.setCursor(Cursor.getPredefinedCursor(
		    		Cursor.NW_RESIZE_CURSOR));
			break;
		default:
		    cmp_moved.setCursor(Cursor.getPredefinedCursor(
		    		Cursor.DEFAULT_CURSOR));
			break;
		}
	}

	
	/**
	 * 
	 * @param _event the mouseEvent
	 * @return the operation
	 */
	private byte getOperation(final MouseEvent _event) {

		//reset current operation
		byte cOperation = operationNo;
		
		//if the click is in the right area
		if (_event.getX() <= marge) {
			cOperation = operationLeft;
		} else if (_event.getX() >= cmp_moved.getWidth() - marge) {
			cOperation = operationRight;
		}
	
		if (_event.getY() <= marge) {
			
			switch(cOperation) {
			case operationNo:
				cOperation = operationTop;
				break;
			case operationLeft:
				cOperation = operationLeftTop;
				break;
			case operationRight:
				cOperation = operationTopRight;
				break;
			default:
				cOperation = operationTop;
				break;
			}
		} else if (_event.getY() >= cmp_moved.getHeight() - marge) {
			cOperation = operationBottom;
			switch(cOperation) {
			case operationNo:
				cOperation = operationBottom;
				break;
			case operationLeft:
				cOperation = operationBottomLeft;
				break;
			case operationRight:
				cOperation = operationRightBottom;
				break;
			default:
				cOperation = operationBottom;
				break;
			}
		}
		return cOperation;
	}
	/**
	 * {@inheritDoc}
	 */
	public final void mouseExited(final MouseEvent _event) { }

	
	/**
	 * {@inheritDoc}
	 */
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

			if (al != null && currentOperation != operationNo) {
				
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

				case operationNo:
					
					int newX = _event.getXOnScreen() - pnt_startPosition.x;
					int newY = _event.getYOnScreen() - pnt_startPosition.y;
					cmp_moved.setLocation(newX, newY);
					System.out.println(newX + ".." + newY);
					break;
				case operationTop:

					cmp_moved.setLocation(
							(int) pnt_origLoc.getX(),
							(int) (pnt_origLoc.getY() 
									- pnt_diff2.getY() + _event.getYOnScreen())
							);
					
					cmp_moved.setSize(
							(int) pnt_origSize.getX(),
							(int) (pnt_origSize.getY() 
									+ pnt_diff2.getY() - _event.getYOnScreen())
							);
					break;
				case operationBottom:

					
					cmp_moved.setSize(
							(int) pnt_origSize.getX(),
							(int) (pnt_origSize.getY() - pnt_diff2.getY() 
									+ _event.getYOnScreen())
							);
					break;
				case operationRight:
					

					
					cmp_moved.setSize(
							(int) (pnt_origSize.getX() - pnt_diff2.getX() 
									+ _event.getXOnScreen()),
							(int) pnt_origSize.getY()
							);
					break;
				case operationLeft:
					cmp_moved.setLocation(
							(int) (pnt_origLoc.getX() - pnt_diff2.getX() 
									+ _event.getXOnScreen()),
							(int) pnt_origLoc.getY()
							);
					
					cmp_moved.setSize(
							(int) (pnt_origSize.getX() + pnt_diff2.getX()
									- _event.getXOnScreen()),
							(int) pnt_origSize.getY()
							);
					break;

				case operationTopRight:
					break;
				case operationRightBottom:
					break;
				case operationBottomLeft:

					cmp_moved.setSize(_event.getXOnScreen() - pnt_startPosition
							.x,
							_event.getYOnScreen() - pnt_startPosition.y);
					break;
				case operationLeftTop:

					cmp_moved.setLocation(_event.getXOnScreen() 
							- pnt_startPosition.x,
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
