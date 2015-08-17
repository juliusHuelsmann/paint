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
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionAdapter;
import java.io.Serializable;


/**
 * The controller class which changes the position of a component by 
 * a user dragging it.
 * 
 * @author Julius Huelsmann
 * @version %I%, %U%
 */
@SuppressWarnings("serial")
public class MousePositionTracker extends MouseMotionAdapter implements 
MouseListener, Serializable {
	
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
	private Point pnt_startPosition;


	/**
	 * Constructor saves component of which this class changes the location.
	 * @param _view the component
	 */
	public MousePositionTracker(final Component _view) {

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
	public final void mouseExited(final MouseEvent _event) { }

	/**
	 * {@inheritDoc}
	 */
	public final void mousePressed(final MouseEvent _event) {

		//save start position and set pressed
		pnt_startPosition = new Point(_event.getXOnScreen() - cmp_moved
				.getLocation().x, _event.getYOnScreen() - cmp_moved
				.getLocation().y);
		pressed = true;

	}

	/**
	 * {@inheritDoc}
	 */
    public final void mouseReleased(final MouseEvent _event) {
		
		//set not pressed and repaint component if mouse is released
		pressed = false;
		cmp_moved.repaint();
	}

	/**
	 * {@inheritDoc}
	 */
	public final synchronized void mouseDragged(final MouseEvent _event) {


		//if pressed change location
		if (pressed) {
			cmp_moved.setLocation(_event.getXOnScreen() - pnt_startPosition.x,
					_event.getYOnScreen() - pnt_startPosition.y);
		}

		//repaint
		cmp_moved.repaint();
	}
	
}
