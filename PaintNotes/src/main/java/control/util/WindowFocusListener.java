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


import java.awt.Component;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

public class WindowFocusListener implements WindowListener {

	private Component c_focus;
	
	public WindowFocusListener(
			final Component _c_focus) {
		
		this.c_focus = _c_focus;
	}
	

	public final void windowOpened(WindowEvent e) { }
	
	public final void windowIconified(WindowEvent e) { }
	
	public final void windowDeiconified(WindowEvent e) { }
	
	public final void windowDeactivated(WindowEvent e) { }
	
	public final void windowClosing(WindowEvent e) { }
	
	public final void windowClosed(WindowEvent e) { }
	
	public final void windowActivated(WindowEvent e) {
		c_focus.requestFocus();
	}
	
}
