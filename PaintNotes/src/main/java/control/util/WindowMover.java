//package declaration
package control.util;


/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */


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
	 * {@inheritDoc}
	 */
	public final void mouseExited(final MouseEvent _event) { }

	
	/**
	 * {@inheritDoc}
	 */
	public final void mousePressed(final MouseEvent _event) {

		if (!ViewSettings.isFullscreen()) {

			
			// if the operating system does not maintain a resize functionality
			if (noResizeFunctionality()) {

				currentOperation = getOperation(_event);
			}
			
			
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

						
						cmp_moved.setLocation(
								(int) pnt_origLoc.getX(),
								(int) (pnt_origLoc.getY() 
										- pnt_diff2.getY() + _event.getYOnScreen())
								);
						
						cmp_moved.setSize(
								(int) (pnt_origSize.getX() - pnt_diff2.getX() 
										+ _event.getXOnScreen()),
								(int) (pnt_origSize.getY() 
										+ pnt_diff2.getY() - _event.getYOnScreen())
								);
						break;
					case operationRightBottom:


						
						cmp_moved.setSize(
								(int) (pnt_origSize.getX() - pnt_diff2.getX() 
										+ _event.getXOnScreen()),
										(int) (pnt_origSize.getY() - pnt_diff2.getY() 
												+ _event.getYOnScreen())
								);

						
						break;
					case operationBottomLeft:


						cmp_moved.setLocation(
								(int) (pnt_origLoc.getX() - pnt_diff2.getX() 
										+ _event.getXOnScreen()),
								(int) pnt_origLoc.getY()
								);
						
						cmp_moved.setSize(
								(int) (pnt_origSize.getX() + pnt_diff2.getX()
										- _event.getXOnScreen()),
								(int) (pnt_origSize.getY() - pnt_diff2.getY() 
										+ _event.getYOnScreen())
								);

						
						break;
					case operationLeftTop:

						cmp_moved.setLocation(
								(int) (pnt_origLoc.getX() - pnt_diff2.getX() 
										+ _event.getXOnScreen()),
								(int) (pnt_origLoc.getY() 
										- pnt_diff2.getY() + _event.getYOnScreen())
								);
						
						cmp_moved.setSize(
								(int) (pnt_origSize.getX() + pnt_diff2.getX()
										- _event.getXOnScreen()),
								(int) (pnt_origSize.getY() 
										+ pnt_diff2.getY() - _event.getYOnScreen())
								);

						break;
					default: break;
					}
				}

				//repaint
				cmp_moved.repaint();
			}

			//repaint
			cmp_moved.repaint();
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
		if (
				
				//Left corner
				_event.getX() <= marge) {
			cOperation = operationLeft;
		} else if (
				
				//right corner
				_event.getX() >= cmp_moved.getWidth() - marge) {
			cOperation = operationRight;
		} else {
			
			// either not resizing or N or S.
			cOperation = operationNo;
		}
	
		// N, NE, NW
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
	 * Check whether the currently used operating system does not support a 
	 * resize possiblity.
	 * 
	 * @return whether there is no resize functionality delivered by the OS.
	 */
	private static boolean noResizeFunctionality() {
		return System.getProperty("os.name").equals("Mac OS X") 
				|| System.getProperty("os.name").equals("Windows");
	}
}
