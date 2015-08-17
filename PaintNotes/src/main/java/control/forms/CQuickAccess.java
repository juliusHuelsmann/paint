package control.forms;
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

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import control.ControlPaint;
import view.forms.QuickAccess;

/**
 * 
 * @author Julius Huelsmann
 * @version %I%, %U%
 *
 */
public class CQuickAccess implements ActionListener, MouseListener {

	
	/**
	 * Instance of the root-controller class.
	 */
	private ControlPaint cp;
	
	/**
	 * Error-checked function for getting the QuickAccess view class.
	 * @return	instance of QuickAccess.
	 */
	private QuickAccess getQuickAccess() {
		return cp.getView().getPage().getQuickAccess();
	}
	
	
	/**
	 * Constructor: save instance of controlPaint.
	 * 
	 * @param _cp 	instance of ControlPaint.
	 */
	public CQuickAccess(final ControlPaint _cp) {
		this.cp = _cp;
	}
	
	/**
	 * {@inheritDoc}
	 */
	public final void mouseClicked(final MouseEvent _event) {
		if (_event.getSource().equals(
				getQuickAccess().getJlbl_outsideLeft())) {
			getQuickAccess().openLeft();
		}
	}

	
	/**
	 * {@inheritDoc}
	 */
	public void mouseEntered(final MouseEvent _event) { }

	
	/**
	 * {@inheritDoc}
	 */
	public void mouseExited(final MouseEvent _event) { }

	
	/**
	 * {@inheritDoc}
	 */
	public void mousePressed(final MouseEvent _event) { }

	
	/**
	 * {@inheritDoc}
	 */
	public void mouseReleased(final MouseEvent _event) { }

	
	/**
	 * {@inheritDoc}
	 */
	public void actionPerformed(final ActionEvent _event) { }

}
